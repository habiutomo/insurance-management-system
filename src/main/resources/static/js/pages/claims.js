const claimsPage = {
  data: [],
  currentId: null,

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div class="d-flex gap-2">
              <input type="text" class="form-control search-box" id="claim-search" placeholder="Search by claim number...">
            </div>
            <button class="btn btn-primary" onclick="claimsPage.openForm()"><i class="fas fa-plus me-1"></i>Add Claim</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Claim #</th><th>Policy</th><th>Customer</th><th>Incident Date</th><th>Claim Amount</th><th>Status</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="claim-table-body">
                <tr><td colspan="8" class="text-center text-muted py-4">Loading...</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;
    document.getElementById('claim-search').addEventListener('input', app.debounce(() => this.loadData(), 300));
    await this.loadData();
  },

  async loadData() {
    try {
      const q = document.getElementById('claim-search')?.value?.trim();
      const endpoint = q ? `/api/claims/search?q=${encodeURIComponent(q)}` : '/api/claims';
      const res = await api.get(endpoint);
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading claims: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('claim-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted py-4">No claims found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((c, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(c.claimNumber)}</td>
        <td>${c.policy ? this.esc(c.policy.policyNumber) : '-'}</td>
        <td>${c.policy?.customer ? this.esc(c.policy.customer.fullName) : '-'}</td>
        <td>${c.incidentDate || '-'}</td>
        <td>${this.fmt(c.claimAmount)}</td>
        <td>${this.statusBadge(c.status)}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="claimsPage.changeStatus(${c.id})" title="Change Status"><i class="fas fa-flag"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="claimsPage.confirmDelete(${c.id})" title="Delete"><i class="fas fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm() {
    this.currentId = null;
    let policies = [];
    try {
      const pres = await api.get('/api/policies');
      if (pres.success) policies = pres.data || [];
    } catch {}

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="claim-form">
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label">Policy *</label>
            <select class="form-select" name="policyId" required>
              <option value="">-- Select Policy --</option>
              ${policies.map(p => `<option value="${p.id}">${this.esc(p.policyNumber)} - ${p.customer ? this.esc(p.customer.fullName) : ''} (${p.policyType})</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Incident Date *</label>
            <input type="date" class="form-control" name="incidentDate" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Claim Amount *</label>
            <input type="number" step="0.01" class="form-control" name="claimAmount" required>
          </div>
          <div class="col-12">
            <label class="form-label">Description</label>
            <textarea class="form-control" name="description" rows="3" maxlength="1000"></textarea>
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Add Claim';
    document.getElementById('modal-save-btn').onclick = () => this.save();
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async save() {
    const form = document.getElementById('claim-form');
    const data = Object.fromEntries(new FormData(form));
    data.claimAmount = parseFloat(data.claimAmount);
    data.policyId = parseInt(data.policyId);
    Object.keys(data).forEach(k => { if (data[k] === '') data[k] = null; });

    try {
      const res = await api.post('/api/claims', data);
      if (res.success) {
        bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
        app.showToast('Claim created successfully', 'success');
        await this.loadData();
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  changeStatus(id) {
    const claim = this.data.find(c => c.id === id);
    if (!claim) return;
    const statuses = ['SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'PAID'];
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="claim-status-form">
        <p>Claim: <strong>${this.esc(claim.claimNumber)}</strong></p>
        <label class="form-label">Current: <strong>${claim.status}</strong></label>
        <select class="form-select mt-2" name="status">
          ${statuses.map(s => `<option value="${s}" ${claim.status === s ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Change Claim Status';
    document.getElementById('modal-save-btn').onclick = async () => {
      const status = document.querySelector('#claim-status-form [name="status"]').value;
      try {
        const res = await api.patch(`/api/claims/${id}/status?status=${status}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Claim status updated', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete Claim';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this claim?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/claims/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('Claim deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  statusBadge(s) {
    const colors = { SUBMITTED: 'info', UNDER_REVIEW: 'warning', APPROVED: 'success', REJECTED: 'danger', PAID: 'primary' };
    return `<span class="badge bg-${colors[s] || 'secondary'}">${s || '-'}</span>`;
  },

  fmt(n) { return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(n || 0); },
  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

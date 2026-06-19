const commissionsPage = {
  data: [],

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div></div>
            <button class="btn btn-primary" onclick="commissionsPage.openForm()"><i class="fas fa-plus me-1"></i>Add Commission</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Commission #</th><th>Agent</th><th>Policy</th><th>Rate</th><th>Premium</th><th>Commission</th><th>Branch</th><th>Status</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="commission-table-body">
                <tr><td colspan="10" class="text-center text-muted py-4">Loading...</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;
    await this.loadData();
  },

  async loadData() {
    try {
      const res = await api.get('/api/commissions');
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading commissions: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('commission-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="10" class="text-center text-muted py-4">No commissions found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((c, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(c.commissionNumber)}</td>
        <td>${c.agent ? this.esc(c.agent.fullName) : '-'}</td>
        <td>${c.policy ? this.esc(c.policy.policyNumber) : '-'}</td>
        <td>${c.commissionRate ? c.commissionRate + '%' : '-'}</td>
        <td>${this.fmt(c.premiumAmount)}</td>
        <td>${this.fmt(c.commissionAmount)}</td>
        <td>${this.esc(c.branchOffice || '-')}</td>
        <td>${this.statusBadge(c.status)}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="commissionsPage.changeStatus(${c.id})" title="Change Status"><i class="fas fa-flag"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm() {
    let agents = [], policies = [];
    try {
      const [ares, pres] = await Promise.all([api.get('/api/agents'), api.get('/api/policies')]);
      if (ares.success) agents = ares.data || [];
      if (pres.success) policies = pres.data || [];
    } catch {}

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="commission-form">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Agent *</label>
            <select class="form-select" name="agentId" required>
              <option value="">-- Select Agent --</option>
              ${agents.map(a => `<option value="${a.id}">${this.esc(a.fullName)} (${this.esc(a.agentCode)})</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Policy *</label>
            <select class="form-select" name="policyId" required>
              <option value="">-- Select Policy --</option>
              ${policies.map(p => `<option value="${p.id}">${this.esc(p.policyNumber)} - ${p.customer ? this.esc(p.customer.fullName) : ''}</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Commission Amount *</label>
            <input type="number" step="0.01" class="form-control" name="commissionAmount" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Commission Rate (%)</label>
            <input type="number" step="0.01" class="form-control" name="commissionRate">
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Add Commission';
    document.getElementById('modal-save-btn').onclick = async () => {
      const form = document.getElementById('commission-form');
      const data = Object.fromEntries(new FormData(form));
      data.commissionAmount = parseFloat(data.commissionAmount);
      data.commissionRate = data.commissionRate ? parseFloat(data.commissionRate) : null;
      data.agentId = parseInt(data.agentId);
      data.policyId = parseInt(data.policyId);
      Object.keys(data).forEach(k => { if (data[k] === '' || data[k] === null) data[k] = null; });
      try {
        const res = await api.post('/api/commissions', data);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Commission created successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  changeStatus(id) {
    const commission = this.data.find(c => c.id === id);
    if (!commission) return;
    const statuses = ['PENDING', 'PAID', 'CANCELLED'];
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="commission-status-form">
        <p>Commission: <strong>${this.esc(commission.commissionNumber)}</strong></p>
        <label class="form-label">Current: <strong>${commission.status}</strong></label>
        <select class="form-select mt-2" name="status">
          ${statuses.map(s => `<option value="${s}" ${commission.status === s ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Change Commission Status';
    document.getElementById('modal-save-btn').onclick = async () => {
      const status = document.querySelector('#commission-status-form [name="status"]').value;
      try {
        const res = await api.patch(`/api/commissions/${id}/status?status=${status}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Commission status updated', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  statusBadge(s) {
    const colors = { PENDING: 'warning', PAID: 'success', CANCELLED: 'danger' };
    return `<span class="badge bg-${colors[s] || 'secondary'}">${s || '-'}</span>`;
  },

  fmt(n) { return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(n || 0); },
  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

const policiesPage = {
  data: [],
  currentId: null,

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div class="d-flex gap-2">
              <input type="text" class="form-control search-box" id="policy-search" placeholder="Search by policy number...">
            </div>
            <button class="btn btn-primary" onclick="policiesPage.openForm()"><i class="fas fa-plus me-1"></i>Add Policy</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Policy #</th><th>Type</th><th>Customer</th><th>Premium</th><th>Coverage</th><th>Start</th><th>End</th><th>Status</th><th>UW Status</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="policy-table-body">
                <tr><td colspan="11" class="text-center text-muted py-4">Loading...</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;
    document.getElementById('policy-search').addEventListener('input', app.debounce(() => this.loadData(), 300));
    await this.loadData();
  },

  async loadData() {
    try {
      const q = document.getElementById('policy-search')?.value?.trim();
      const endpoint = q ? `/api/policies/search?q=${encodeURIComponent(q)}` : '/api/policies';
      const res = await api.get(endpoint);
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading policies: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('policy-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="11" class="text-center text-muted py-4">No policies found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((p, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(p.policyNumber)}</td>
        <td><span class="badge bg-info bg-opacity-10 text-info">${p.policyType}</span></td>
        <td>${p.customer ? this.esc(p.customer.fullName) : '-'}</td>
        <td>${this.fmt(p.premiumAmount)}</td>
        <td>${this.fmt(p.coverageAmount)}</td>
        <td>${p.startDate || '-'}</td>
        <td>${p.endDate || '-'}</td>
        <td>${this.statusBadge(p.status)}</td>
        <td>${this.uwBadge(p.underwritingStatus)}</td>
        <td class="text-center table-actions">
          <div class="dropdown">
            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown"><i class="fas fa-ellipsis-v"></i></button>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item" href="#" onclick="policiesPage.openForm(${p.id})"><i class="fas fa-edit me-2"></i>Edit</a></li>
              <li><a class="dropdown-item" href="#" onclick="policiesPage.openRenew(${p.id})"><i class="fas fa-sync me-2"></i>Renew</a></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#" onclick="policiesPage.changeStatus(${p.id})"><i class="fas fa-flag me-2"></i>Change Status</a></li>
              <li><a class="dropdown-item" href="#" onclick="policiesPage.changeUnderwriting(${p.id})"><i class="fas fa-search me-2"></i>Underwriting</a></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item text-danger" href="#" onclick="policiesPage.confirmDelete(${p.id})"><i class="fas fa-trash me-2"></i>Delete</a></li>
            </ul>
          </div>
        </td>
      </tr>
    `).join('');
  },

  async openForm(id = null) {
    this.currentId = id;
    let policy = { policyType: 'HEALTH', premiumAmount: '', coverageAmount: '', startDate: '', endDate: '', customerId: '', insuredName: '', beneficiaryName: '', beneficiaryRelationship: '', bankAccountNumber: '', bankAccountName: '', bankName: '', premiumPaymentMethod: '', premiumFrequency: '', autoRenew: false, branchOffice: '' };

    if (id) {
      try {
        const res = await api.get(`/api/policies/${id}`);
        if (res.success) {
          policy = res.data;
          policy.customerId = policy.customer?.id || '';
        }
      } catch (err) { app.showToast('Error loading policy: ' + err.message, 'danger'); return; }
    }

    let customers = [];
    try {
      const cres = await api.get('/api/customers');
      if (cres.success) customers = cres.data || [];
    } catch {}

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="policy-form">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Customer *</label>
            <select class="form-select" name="customerId" required>
              <option value="">-- Select Customer --</option>
              ${customers.map(c => `<option value="${c.id}" ${policy.customerId == c.id ? 'selected' : ''}>${this.esc(c.fullName)} (${this.esc(c.email)})</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Policy Type *</label>
            <select class="form-select" name="policyType" required>
              ${['AUTO','HEALTH','LIFE','PROPERTY','TRAVEL'].map(t => `<option value="${t}" ${policy.policyType === t ? 'selected' : ''}>${t}</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Premium Amount *</label>
            <input type="number" step="0.01" class="form-control" name="premiumAmount" value="${policy.premiumAmount || ''}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Coverage Amount *</label>
            <input type="number" step="0.01" class="form-control" name="coverageAmount" value="${policy.coverageAmount || ''}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Start Date *</label>
            <input type="date" class="form-control" name="startDate" value="${policy.startDate || ''}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">End Date *</label>
            <input type="date" class="form-control" name="endDate" value="${policy.endDate || ''}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Insured Name</label>
            <input type="text" class="form-control" name="insuredName" value="${this.esc(policy.insuredName || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Beneficiary Name</label>
            <input type="text" class="form-control" name="beneficiaryName" value="${this.esc(policy.beneficiaryName || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Beneficiary Relationship</label>
            <input type="text" class="form-control" name="beneficiaryRelationship" value="${this.esc(policy.beneficiaryRelationship || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Premium Payment Method</label>
            <select class="form-select" name="premiumPaymentMethod">
              <option value="">-- Select --</option>
              ${['AUTO_DEBIT','TRANSFER','VIRTUAL_ACCOUNT','KARTU_KREDIT','TUNAI'].map(m => `<option value="${m}" ${policy.premiumPaymentMethod === m ? 'selected' : ''}>${m}</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Premium Frequency</label>
            <select class="form-select" name="premiumFrequency">
              <option value="">-- Select --</option>
              ${['BULANAN','TRIWULAN','SEMESTER','TAHUNAN','SEKALIGUS'].map(f => `<option value="${f}" ${policy.premiumFrequency === f ? 'selected' : ''}>${f}</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Branch Office</label>
            <input type="text" class="form-control" name="branchOffice" value="${this.esc(policy.branchOffice || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Bank Account Number</label>
            <input type="text" class="form-control" name="bankAccountNumber" value="${this.esc(policy.bankAccountNumber || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Bank Account Name</label>
            <input type="text" class="form-control" name="bankAccountName" value="${this.esc(policy.bankAccountName || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Bank Name</label>
            <input type="text" class="form-control" name="bankName" value="${this.esc(policy.bankName || '')}">
          </div>
          <div class="col-md-6">
            <div class="form-check mt-4">
              <input class="form-check-input" type="checkbox" name="autoRenew" value="true" ${policy.autoRenew ? 'checked' : ''}>
              <label class="form-check-label">Auto Renew</label>
            </div>
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = id ? 'Edit Policy' : 'Add Policy';
    document.getElementById('modal-save-btn').onclick = () => this.save();
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async save() {
    const form = document.getElementById('policy-form');
    const data = Object.fromEntries(new FormData(form));
    data.premiumAmount = parseFloat(data.premiumAmount);
    data.coverageAmount = parseFloat(data.coverageAmount);
    data.customerId = parseInt(data.customerId);
    data.autoRenew = data.autoRenew === 'true';
    data.premiumPaymentMethod = data.premiumPaymentMethod || null;
    data.premiumFrequency = data.premiumFrequency || null;
    Object.keys(data).forEach(k => { if (data[k] === '' || data[k] === null) data[k] = null; });

    try {
      let res;
      if (this.currentId) {
        res = await api.put(`/api/policies/${this.currentId}`, data);
      } else {
        res = await api.post('/api/policies', data);
      }
      if (res.success) {
        bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
        app.showToast(this.currentId ? 'Policy updated successfully' : 'Policy created successfully', 'success');
        await this.loadData();
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  changeStatus(id) {
    const policy = this.data.find(p => p.id === id);
    if (!policy) return;
    const statuses = ['ACTIVE', 'CANCELLED', 'PENDING'];
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="status-form">
        <label class="form-label">Current Status: <strong>${policy.status}</strong></label>
        <select class="form-select mt-2" name="status">
          ${statuses.map(s => `<option value="${s}" ${policy.status === s ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Change Policy Status';
    document.getElementById('modal-save-btn').onclick = async () => {
      const status = document.querySelector('#status-form [name="status"]').value;
      try {
        const res = await api.patch(`/api/policies/${id}/status?status=${status}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Status updated', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  changeUnderwriting(id) {
    const policy = this.data.find(p => p.id === id);
    if (!policy) return;
    const statuses = ['PENDING', 'REVIEW', 'APPROVED', 'DECLINED', 'VOID'];
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="uw-form">
        <label class="form-label">Current UW Status: <strong>${policy.underwritingStatus}</strong></label>
        <select class="form-select mt-2" name="underwritingStatus">
          ${statuses.map(s => `<option value="${s}" ${policy.underwritingStatus === s ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Update Underwriting Status';
    document.getElementById('modal-save-btn').onclick = async () => {
      const status = document.querySelector('#uw-form [name="underwritingStatus"]').value;
      try {
        const res = await api.patch(`/api/policies/${id}/underwriting?status=${status}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Underwriting status updated', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async openRenew(id) {
    try {
      const res = await api.get(`/api/policies/${id}`);
      if (!res.success) return;
      const policy = res.data;

      const modalBody = document.getElementById('modal-body');
      modalBody.innerHTML = `
        <form id="renew-form">
          <p class="text-muted">Renewing policy: <strong>${policy.policyNumber}</strong></p>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Start Date *</label>
              <input type="date" class="form-control" name="startDate" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">End Date *</label>
              <input type="date" class="form-control" name="endDate" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Premium Amount *</label>
              <input type="number" step="0.01" class="form-control" name="premiumAmount" value="${policy.premiumAmount}" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Bank Account Number</label>
              <input type="text" class="form-control" name="bankAccountNumber" value="${this.esc(policy.bankAccountNumber || '')}">
            </div>
            <div class="col-md-6">
              <label class="form-label">Bank Account Name</label>
              <input type="text" class="form-control" name="bankAccountName" value="${this.esc(policy.bankAccountName || '')}">
            </div>
            <div class="col-md-6">
              <label class="form-label">Bank Name</label>
              <input type="text" class="form-control" name="bankName" value="${this.esc(policy.bankName || '')}">
            </div>
            <div class="col-md-6">
              <div class="form-check mt-4">
                <input class="form-check-input" type="checkbox" name="autoRenew" value="true" ${policy.autoRenew ? 'checked' : ''}>
                <label class="form-check-label">Auto Renew</label>
              </div>
            </div>
          </div>
        </form>
      `;
      document.getElementById('modal-title').textContent = 'Renew Policy';
      document.getElementById('modal-save-btn').onclick = async () => {
        const form = document.getElementById('renew-form');
        const data = Object.fromEntries(new FormData(form));
        data.premiumAmount = parseFloat(data.premiumAmount);
        data.autoRenew = data.autoRenew === 'true';
        Object.keys(data).forEach(k => { if (data[k] === '' || data[k] === null) data[k] = null; });
        try {
          const r = await api.post(`/api/policies/${id}/renew`, data);
          if (r.success) {
            bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
            app.showToast('Policy renewed successfully', 'success');
            await this.loadData();
          }
        } catch (err) { app.showToast(err.message, 'danger'); }
      };
      bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete Policy';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this policy?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/policies/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('Policy deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  statusBadge(s) {
    const colors = { ACTIVE: 'success', EXPIRED: 'secondary', CANCELLED: 'danger', PENDING: 'warning' };
    return `<span class="badge bg-${colors[s] || 'secondary'}">${s || '-'}</span>`;
  },

  uwBadge(s) {
    const colors = { PENDING: 'warning', REVIEW: 'info', APPROVED: 'success', DECLINED: 'danger', VOID: 'secondary' };
    return `<span class="badge bg-${colors[s] || 'secondary'} bg-opacity-10 text-${colors[s] || 'secondary'}">${s || '-'}</span>`;
  },

  fmt(n) { return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(n || 0); },
  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

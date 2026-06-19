const paymentsPage = {
  data: [],

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div></div>
            <button class="btn btn-primary" onclick="paymentsPage.openForm()"><i class="fas fa-plus me-1"></i>Add Payment</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Payment #</th><th>Policy</th><th>Customer</th><th>Amount</th><th>Date</th><th>Method</th><th>Status</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="payment-table-body">
                <tr><td colspan="9" class="text-center text-muted py-4">Loading...</td></tr>
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
      const res = await api.get('/api/payments');
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading payments: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('payment-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="9" class="text-center text-muted py-4">No payments found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((p, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(p.paymentNumber)}</td>
        <td>${p.policy ? this.esc(p.policy.policyNumber) : '-'}</td>
        <td>${p.policy?.customer ? this.esc(p.policy.customer.fullName) : '-'}</td>
        <td>${this.fmt(p.amount)}</td>
        <td>${p.paymentDate || '-'}</td>
        <td>${this.methodBadge(p.paymentMethod)}</td>
        <td>${this.statusBadge(p.status)}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="paymentsPage.changeStatus(${p.id})" title="Change Status"><i class="fas fa-flag"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="paymentsPage.confirmDelete(${p.id})" title="Delete"><i class="fas fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm() {
    let policies = [];
    try {
      const pres = await api.get('/api/policies');
      if (pres.success) policies = pres.data || [];
    } catch {}

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="payment-form">
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label">Policy *</label>
            <select class="form-select" name="policyId" required>
              <option value="">-- Select Policy --</option>
              ${policies.map(p => `<option value="${p.id}">${this.esc(p.policyNumber)} - ${p.customer ? this.esc(p.customer.fullName) : ''}</option>`).join('')}
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Amount *</label>
            <input type="number" step="0.01" class="form-control" name="amount" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Payment Date *</label>
            <input type="date" class="form-control" name="paymentDate" required>
          </div>
          <div class="col-12">
            <label class="form-label">Payment Method *</label>
            <select class="form-select" name="paymentMethod" required>
              <option value="">-- Select --</option>
              <option value="BANK_TRANSFER">Bank Transfer</option>
              <option value="CREDIT_CARD">Credit Card</option>
              <option value="DEBIT_CARD">Debit Card</option>
              <option value="CASH">Cash</option>
              <option value="E_WALLET">E-Wallet</option>
            </select>
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Add Payment';
    document.getElementById('modal-save-btn').onclick = async () => {
      const form = document.getElementById('payment-form');
      const data = Object.fromEntries(new FormData(form));
      data.amount = parseFloat(data.amount);
      data.policyId = parseInt(data.policyId);
      data.paymentMethod = data.paymentMethod || null;
      Object.keys(data).forEach(k => { if (data[k] === '') data[k] = null; });
      try {
        const res = await api.post('/api/payments', data);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Payment created successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  changeStatus(id) {
    const payment = this.data.find(p => p.id === id);
    if (!payment) return;
    const statuses = ['PENDING', 'PAID', 'OVERDUE', 'CANCELLED'];
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="payment-status-form">
        <p>Payment: <strong>${this.esc(payment.paymentNumber)}</strong></p>
        <label class="form-label">Current: <strong>${payment.status}</strong></label>
        <select class="form-select mt-2" name="status">
          ${statuses.map(s => `<option value="${s}" ${payment.status === s ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </form>
    `;
    document.getElementById('modal-title').textContent = 'Change Payment Status';
    document.getElementById('modal-save-btn').onclick = async () => {
      const status = document.querySelector('#payment-status-form [name="status"]').value;
      try {
        const res = await api.patch(`/api/payments/${id}/status?status=${status}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('Payment status updated', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete Payment';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this payment?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/payments/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('Payment deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  methodBadge(m) {
    const colors = { BANK_TRANSFER: 'primary', CREDIT_CARD: 'info', DEBIT_CARD: 'info', CASH: 'success', E_WALLET: 'warning' };
    return `<span class="badge bg-${colors[m] || 'secondary'} bg-opacity-10 text-${colors[m] || 'secondary'}">${(m || '-').replace(/_/g, ' ')}</span>`;
  },

  statusBadge(s) {
    const colors = { PENDING: 'warning', PAID: 'success', OVERDUE: 'danger', CANCELLED: 'secondary' };
    return `<span class="badge bg-${colors[s] || 'secondary'}">${s || '-'}</span>`;
  },

  fmt(n) { return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(n || 0); },
  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

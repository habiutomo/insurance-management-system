const agentsPage = {
  data: [],
  currentId: null,

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div></div>
            <button class="btn btn-primary" onclick="agentsPage.openForm()"><i class="fas fa-plus me-1"></i>Add Agent</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Agent Code</th><th>Full Name</th><th>Email</th><th>Phone</th><th>Branch</th><th>Supervisor</th><th>Active</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="agent-table-body">
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
      const res = await api.get('/api/agents');
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading agents: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('agent-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="9" class="text-center text-muted py-4">No agents found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((a, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(a.agentCode)}</td>
        <td>${this.esc(a.fullName)}</td>
        <td>${this.esc(a.email || '-')}</td>
        <td>${this.esc(a.phone || '-')}</td>
        <td>${this.esc(a.branchOffice || '-')}</td>
        <td>${this.esc(a.supervisorName || '-')}</td>
        <td>${a.active ? '<span class="badge bg-success">Active</span>' : '<span class="badge bg-danger">Inactive</span>'}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="agentsPage.openForm(${a.id})" title="Edit"><i class="fas fa-edit"></i></button>
          <button class="btn btn-sm btn-outline-warning" onclick="agentsPage.toggleStatus(${a.id})" title="Toggle Status"><i class="fas fa-toggle-on"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="agentsPage.confirmDelete(${a.id})" title="Delete"><i class="fas fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm(id = null) {
    this.currentId = id;
    let agent = { fullName: '', email: '', phone: '', branchOffice: '', supervisorName: '' };
    if (id) {
      try {
        const res = await api.get(`/api/agents/${id}`);
        if (res.success) agent = res.data;
      } catch (err) { app.showToast('Error loading agent: ' + err.message, 'danger'); return; }
    }

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="agent-form">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Full Name *</label>
            <input type="text" class="form-control" name="fullName" value="${this.esc(agent.fullName)}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" name="email" value="${this.esc(agent.email || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Phone</label>
            <input type="text" class="form-control" name="phone" value="${this.esc(agent.phone || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Branch Office</label>
            <input type="text" class="form-control" name="branchOffice" value="${this.esc(agent.branchOffice || '')}">
          </div>
          <div class="col-12">
            <label class="form-label">Supervisor Name</label>
            <input type="text" class="form-control" name="supervisorName" value="${this.esc(agent.supervisorName || '')}">
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = id ? 'Edit Agent' : 'Add Agent';
    document.getElementById('modal-save-btn').onclick = () => this.save();
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async save() {
    const form = document.getElementById('agent-form');
    const data = Object.fromEntries(new FormData(form));
    Object.keys(data).forEach(k => { if (data[k] === '') data[k] = null; });

    try {
      let res;
      if (this.currentId) {
        res = await api.put(`/api/agents/${this.currentId}`, data);
      } else {
        res = await api.post('/api/agents', data);
      }
      if (res.success) {
        bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
        app.showToast(this.currentId ? 'Agent updated successfully' : 'Agent created successfully', 'success');
        await this.loadData();
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  async toggleStatus(id) {
    try {
      const res = await api.patch(`/api/agents/${id}/toggle-status`);
      if (res.success) {
        app.showToast('Agent status toggled', 'success');
        await this.loadData();
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete Agent';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this agent?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/agents/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('Agent deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

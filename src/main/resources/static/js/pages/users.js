const usersPage = {
  data: [],
  currentId: null,

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div></div>
            <button class="btn btn-primary" onclick="usersPage.openForm()"><i class="fas fa-plus me-1"></i>Add User</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Username</th><th>Full Name</th><th>Email</th><th>Role</th><th>Enabled</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="user-table-body">
                <tr><td colspan="7" class="text-center text-muted py-4">Loading...</td></tr>
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
      const res = await api.get('/api/users');
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading users: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('user-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted py-4">No users found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((u, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(u.username)}</td>
        <td>${this.esc(u.fullName)}</td>
        <td>${this.esc(u.email)}</td>
        <td><span class="badge bg-primary bg-opacity-10 text-primary">${u.role ? u.role.replace('ROLE_', '') : '-'}</span></td>
        <td>${u.enabled ? '<span class="badge bg-success">Enabled</span>' : '<span class="badge bg-danger">Disabled</span>'}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="usersPage.openForm(${u.id})" title="Edit"><i class="fas fa-edit"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="usersPage.confirmDelete(${u.id})" title="Delete"><i class="fas fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm(id = null) {
    this.currentId = id;
    let user = { username: '', email: '', fullName: '', password: '', role: 'ROLE_AGENT' };
    if (id) {
      try {
        const res = await api.get(`/api/users/${id}`);
        if (res.success) user = res.data;
      } catch (err) { app.showToast('Error loading user: ' + err.message, 'danger'); return; }
    }

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="user-form">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Username *</label>
            <input type="text" class="form-control" name="username" value="${this.esc(user.username)}" ${id ? 'disabled' : 'required'}>
          </div>
          <div class="col-md-6">
            <label class="form-label">Full Name *</label>
            <input type="text" class="form-control" name="fullName" value="${this.esc(user.fullName)}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Email *</label>
            <input type="email" class="form-control" name="email" value="${this.esc(user.email)}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Password ${id ? '(leave blank to keep)' : '*'}</label>
            <input type="password" class="form-control" name="password" ${id ? '' : 'required'}>
          </div>
          <div class="col-md-6">
            <label class="form-label">Role *</label>
            <select class="form-select" name="role" required>
              <option value="ROLE_ADMIN" ${user.role === 'ROLE_ADMIN' ? 'selected' : ''}>Admin</option>
              <option value="ROLE_AGENT" ${user.role === 'ROLE_AGENT' ? 'selected' : ''}>Agent</option>
              <option value="ROLE_CUSTOMER" ${user.role === 'ROLE_CUSTOMER' ? 'selected' : ''}>Customer</option>
            </select>
          </div>
          ${id ? `
          <div class="col-md-6">
            <div class="form-check mt-4">
              <input class="form-check-input" type="checkbox" name="enabled" value="true" ${user.enabled !== false ? 'checked' : ''}>
              <label class="form-check-label">Enabled</label>
            </div>
          </div>` : ''}
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = id ? 'Edit User' : 'Add User';
    document.getElementById('modal-save-btn').onclick = () => this.save(id);
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async save(id) {
    const form = document.getElementById('user-form');
    const data = Object.fromEntries(new FormData(form));

    if (id) {
      const updateData = {};
      updateData.fullName = data.fullName;
      updateData.email = data.email;
      updateData.role = data.role;
      updateData.enabled = data.enabled === 'true';
      try {
        const res = await api.put(`/api/users/${id}`, updateData);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('User updated successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    } else {
      try {
        const res = await api.post('/api/users', data);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
          app.showToast('User created successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    }
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete User';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this user?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/users/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('User deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  esc(str) { if (str === null || str === undefined) return ''; return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }
};

const customersPage = {
  data: [],
  currentId: null,

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="card">
        <div class="card-body">
          <div class="page-header mb-3">
            <div class="d-flex gap-2">
              <input type="text" class="form-control search-box" id="customer-search" placeholder="Search by name, email, phone, NIK...">
            </div>
            <button class="btn btn-primary" onclick="customersPage.openForm()"><i class="fas fa-plus me-1"></i>Add Customer</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle">
              <thead><tr>
                <th>#</th><th>Full Name</th><th>Email</th><th>Phone</th><th>NIK</th><th>Gender</th><th>Occupation</th><th class="text-center">Actions</th>
              </tr></thead>
              <tbody id="customer-table-body">
                <tr><td colspan="8" class="text-center text-muted py-4">Loading...</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;

    document.getElementById('customer-search').addEventListener('input', app.debounce(() => this.loadData(), 300));
    await this.loadData();
  },

  async loadData() {
    try {
      const q = document.getElementById('customer-search')?.value?.trim();
      const endpoint = q ? `/api/customers/search?q=${encodeURIComponent(q)}` : '/api/customers';
      const res = await api.get(endpoint);
      this.data = res.success ? (res.data || []) : [];
      this.renderTable();
    } catch (err) {
      app.showToast('Error loading customers: ' + err.message, 'danger');
    }
  },

  renderTable() {
    const tbody = document.getElementById('customer-table-body');
    if (!this.data.length) {
      tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted py-4">No customers found</td></tr>';
      return;
    }
    tbody.innerHTML = this.data.map((c, i) => `
      <tr>
        <td>${i + 1}</td>
        <td class="fw-semibold">${this.esc(c.fullName)}</td>
        <td>${this.esc(c.email)}</td>
        <td>${this.esc(c.phone || '-')}</td>
        <td>${this.esc(c.nik || '-')}</td>
        <td>${c.gender ? this.formatGender(c.gender) : '-'}</td>
        <td>${this.esc(c.occupation || '-')}</td>
        <td class="text-center table-actions">
          <button class="btn btn-sm btn-outline-primary" onclick="customersPage.openForm(${c.id})" title="Edit"><i class="fas fa-edit"></i></button>
          <button class="btn btn-sm btn-outline-danger" onclick="customersPage.confirmDelete(${c.id})" title="Delete"><i class="fas fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async openForm(id = null) {
    this.currentId = id;
    let customer = { fullName: '', email: '', phone: '', address: '', dateOfBirth: '', placeOfBirth: '', gender: '', religion: '', nik: '', npwp: '', occupation: '', maritalStatus: '', motherMaidenName: '', nationality: '' };

    if (id) {
      try {
        const res = await api.get(`/api/customers/${id}`);
        if (res.success) customer = res.data;
      } catch (err) { app.showToast('Error loading customer: ' + err.message, 'danger'); return; }
    }

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
      <form id="customer-form">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Full Name *</label>
            <input type="text" class="form-control" name="fullName" value="${this.esc(customer.fullName)}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Email *</label>
            <input type="email" class="form-control" name="email" value="${this.esc(customer.email)}" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Phone</label>
            <input type="text" class="form-control" name="phone" value="${this.esc(customer.phone || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">NIK</label>
            <input type="text" class="form-control" name="nik" value="${this.esc(customer.nik || '')}" maxlength="16">
          </div>
          <div class="col-md-6">
            <label class="form-label">NPWP</label>
            <input type="text" class="form-control" name="npwp" value="${this.esc(customer.npwp || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Date of Birth</label>
            <input type="date" class="form-control" name="dateOfBirth" value="${customer.dateOfBirth || ''}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Place of Birth</label>
            <input type="text" class="form-control" name="placeOfBirth" value="${this.esc(customer.placeOfBirth || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Gender</label>
            <select class="form-select" name="gender">
              <option value="">-- Select --</option>
              <option value="LAKI_LAKI" ${customer.gender === 'LAKI_LAKI' ? 'selected' : ''}>Male</option>
              <option value="PEREMPUAN" ${customer.gender === 'PEREMPUAN' ? 'selected' : ''}>Female</option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Religion</label>
            <select class="form-select" name="religion">
              <option value="">-- Select --</option>
              <option value="ISLAM" ${customer.religion === 'ISLAM' ? 'selected' : ''}>Islam</option>
              <option value="KRISTEN" ${customer.religion === 'KRISTEN' ? 'selected' : ''}>Christian</option>
              <option value="KATOLIK" ${customer.religion === 'KATOLIK' ? 'selected' : ''}>Catholic</option>
              <option value="HINDU" ${customer.religion === 'HINDU' ? 'selected' : ''}>Hindu</option>
              <option value="BUDHA" ${customer.religion === 'BUDHA' ? 'selected' : ''}>Buddha</option>
              <option value="KONGHUCU" ${customer.religion === 'KONGHUCU' ? 'selected' : ''}>Confucian</option>
              <option value="LAINNYA" ${customer.religion === 'LAINNYA' ? 'selected' : ''}>Other</option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Marital Status</label>
            <select class="form-select" name="maritalStatus">
              <option value="">-- Select --</option>
              <option value="BELUM_MENIKAH" ${customer.maritalStatus === 'BELUM_MENIKAH' ? 'selected' : ''}>Unmarried</option>
              <option value="MENIKAH" ${customer.maritalStatus === 'MENIKAH' ? 'selected' : ''}>Married</option>
              <option value="CERAI_HIDUP" ${customer.maritalStatus === 'CERAI_HIDUP' ? 'selected' : ''}>Divorced (Alive)</option>
              <option value="CERAI_MATI" ${customer.maritalStatus === 'CERAI_MATI' ? 'selected' : ''}>Divorced (Deceased)</option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Occupation</label>
            <input type="text" class="form-control" name="occupation" value="${this.esc(customer.occupation || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Mother's Maiden Name</label>
            <input type="text" class="form-control" name="motherMaidenName" value="${this.esc(customer.motherMaidenName || '')}">
          </div>
          <div class="col-md-6">
            <label class="form-label">Nationality</label>
            <input type="text" class="form-control" name="nationality" value="${this.esc(customer.nationality || '')}">
          </div>
          <div class="col-12">
            <label class="form-label">Address</label>
            <textarea class="form-control" name="address" rows="2">${this.esc(customer.address || '')}</textarea>
          </div>
        </div>
      </form>
    `;
    document.getElementById('modal-title').textContent = id ? 'Edit Customer' : 'Add Customer';
    document.getElementById('modal-save-btn').onclick = () => this.save();
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-form')).show();
  },

  async save() {
    const form = document.getElementById('customer-form');
    const data = Object.fromEntries(new FormData(form));
    data.dateOfBirth = data.dateOfBirth || null;
    data.gender = data.gender || null;
    data.religion = data.religion || null;
    data.maritalStatus = data.maritalStatus || null;
    Object.keys(data).forEach(k => { if (data[k] === '') data[k] = null; });

    try {
      let res;
      if (this.currentId) {
        res = await api.put(`/api/customers/${this.currentId}`, data);
      } else {
        res = await api.post('/api/customers', data);
      }
      if (res.success) {
        bootstrap.Modal.getInstance(document.getElementById('modal-form')).hide();
        app.showToast(this.currentId ? 'Customer updated successfully' : 'Customer created successfully', 'success');
        await this.loadData();
      }
    } catch (err) {
      app.showToast(err.message, 'danger');
    }
  },

  confirmDelete(id) {
    document.getElementById('confirm-title').textContent = 'Delete Customer';
    document.getElementById('confirm-body').textContent = 'Are you sure you want to delete this customer?';
    document.getElementById('confirm-yes-btn').onclick = async () => {
      try {
        const res = await api.del(`/api/customers/${id}`);
        if (res.success) {
          bootstrap.Modal.getInstance(document.getElementById('modal-confirm')).hide();
          app.showToast('Customer deleted successfully', 'success');
          await this.loadData();
        }
      } catch (err) { app.showToast(err.message, 'danger'); }
    };
    bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-confirm')).show();
  },

  formatGender(g) {
    return g === 'LAKI_LAKI' ? 'Male' : g === 'PEREMPUAN' ? 'Female' : g;
  },

  esc(str) {
    if (str === null || str === undefined) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
  }
};

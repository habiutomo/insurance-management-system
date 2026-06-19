const profilePage = {
  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div class="row g-4">
        <div class="col-lg-6">
          <div class="card">
            <div class="card-body">
              <h6 class="fw-semibold mb-3"><i class="fas fa-user me-2 text-primary"></i>Profile Information</h6>
              <form id="profile-form">
                <div class="mb-3">
                  <label class="form-label">Username</label>
                  <input type="text" class="form-control" id="profile-username" disabled>
                </div>
                <div class="mb-3">
                  <label class="form-label">Full Name</label>
                  <input type="text" class="form-control" name="fullName" id="profile-fullName" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Email</label>
                  <input type="email" class="form-control" name="email" id="profile-email" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Role</label>
                  <input type="text" class="form-control" id="profile-role" disabled>
                </div>
                <button type="submit" class="btn btn-primary"><i class="fas fa-save me-1"></i>Update Profile</button>
              </form>
            </div>
          </div>
        </div>
        <div class="col-lg-6">
          <div class="card">
            <div class="card-body">
              <h6 class="fw-semibold mb-3"><i class="fas fa-key me-2 text-primary"></i>Change Password</h6>
              <form id="password-form">
                <div class="mb-3">
                  <label class="form-label">Current Password</label>
                  <input type="password" class="form-control" name="currentPassword" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">New Password</label>
                  <input type="password" class="form-control" name="newPassword" required>
                </div>
                <button type="submit" class="btn btn-warning"><i class="fas fa-key me-1"></i>Change Password</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    `;

    await this.loadProfile();
    document.getElementById('profile-form').addEventListener('submit', (e) => {
      e.preventDefault();
      this.updateProfile();
    });
    document.getElementById('password-form').addEventListener('submit', (e) => {
      e.preventDefault();
      this.changePassword();
    });
  },

  async loadProfile() {
    try {
      const res = await api.get('/api/profile');
      if (res.success && res.data) {
        const u = res.data;
        document.getElementById('profile-username').value = u.username || '';
        document.getElementById('profile-fullName').value = u.fullName || '';
        document.getElementById('profile-email').value = u.email || '';
        document.getElementById('profile-role').value = u.role ? u.role.replace('ROLE_', '') : '';
      }
    } catch (err) {
      app.showToast('Error loading profile: ' + err.message, 'danger');
    }
  },

  async updateProfile() {
    const fullName = document.querySelector('#profile-form [name="fullName"]').value;
    const email = document.querySelector('#profile-form [name="email"]').value;
    try {
      const res = await api.put('/api/profile', { fullName, email });
      if (res.success) {
        app.showToast('Profile updated successfully', 'success');
        if (res.data) {
          const user = JSON.parse(localStorage.getItem('user') || '{}');
          user.fullName = res.data.fullName || fullName;
          localStorage.setItem('user', JSON.stringify(user));
          auth.updateUI();
        }
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  },

  async changePassword() {
    const currentPassword = document.querySelector('#password-form [name="currentPassword"]').value;
    const newPassword = document.querySelector('#password-form [name="newPassword"]').value;
    try {
      const res = await api.put('/api/profile/password', { currentPassword, newPassword });
      if (res.success) {
        app.showToast('Password changed successfully', 'success');
        document.getElementById('password-form').reset();
      }
    } catch (err) { app.showToast(err.message, 'danger'); }
  }
};

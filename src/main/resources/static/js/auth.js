const auth = {
  user: null,

  init() {
    const stored = localStorage.getItem('user');
    if (stored) {
      try { this.user = JSON.parse(stored); } catch { this.user = null; }
    }
    const token = api.getToken();
    if (token && this.user) {
      this.updateUI();
      return true;
    }
    return false;
  },

  async login(username, password) {
    const res = await api.post('/api/auth/login', { username, password });
    if (res.success && res.data) {
      api.setToken(res.data.token);
      this.user = {
        username: res.data.username,
        role: res.data.role,
        fullName: res.data.fullName,
      };
      localStorage.setItem('user', JSON.stringify(this.user));
      this.updateUI();
      return true;
    }
    throw new Error(res.message || 'Login failed');
  },

  logout() {
    api.removeToken();
    localStorage.removeItem('user');
    this.user = null;
    document.getElementById('login-page').classList.remove('d-none');
    document.getElementById('main-app').classList.add('d-none');
    document.getElementById('loading-screen').classList.add('d-none');
  },

  updateUI() {
    if (!this.user) return;
    document.getElementById('user-display-name').textContent = this.user.fullName || this.user.username;
    document.getElementById('user-role-badge').textContent = this.user.role || '';

    const isAdmin = this.user.role === 'ROLE_ADMIN';
    document.querySelectorAll('.admin-only').forEach(el => {
      el.classList.toggle('d-none', !isAdmin);
    });
  },

  isAdmin() {
    return this.user && this.user.role === 'ROLE_ADMIN';
  },

  isAgent() {
    return this.user && this.user.role === 'ROLE_AGENT';
  },
};

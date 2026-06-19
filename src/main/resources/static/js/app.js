const app = {
  currentPage: 'dashboard',
  pageTitleMap: {
    dashboard: 'Dashboard',
    customers: 'Customer Management',
    policies: 'Policy Management',
    claims: 'Claim Management',
    payments: 'Payment Management',
    agents: 'Agent Management',
    commissions: 'Commission Management',
    users: 'User Management',
    profile: 'My Profile',
  },

  init() {
    document.getElementById('loading-screen').classList.remove('d-none');

    if (auth.init()) {
      this.showMainApp();
    } else {
      document.getElementById('loading-screen').classList.add('d-none');
      document.getElementById('login-page').classList.remove('d-none');
    }

    this.bindEvents();
  },

  bindEvents() {
    document.getElementById('login-form').addEventListener('submit', async (e) => {
      e.preventDefault();
      const username = document.getElementById('login-username').value;
      const password = document.getElementById('login-password').value;
      const errorDiv = document.getElementById('login-error');
      errorDiv.classList.add('d-none');

      try {
        await auth.login(username, password);
        this.showMainApp();
      } catch (err) {
        errorDiv.textContent = err.message;
        errorDiv.classList.remove('d-none');
      }
    });

    document.getElementById('logout-btn').addEventListener('click', (e) => {
      e.preventDefault();
      auth.logout();
    });

    document.querySelectorAll('[data-page]').forEach(link => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        const page = link.dataset.page;
        this.navigate(page);

        const nav = document.getElementById('sidebarNav');
        const bsCollapse = bootstrap.Collapse.getInstance(nav);
        if (bsCollapse) bsCollapse.hide();
      });
    });
  },

  showMainApp() {
    document.getElementById('loading-screen').classList.add('d-none');
    document.getElementById('login-page').classList.add('d-none');
    document.getElementById('main-app').classList.remove('d-none');
    this.navigate('dashboard');
  },

  async navigate(page) {
    this.currentPage = page;

    document.querySelectorAll('[data-page]').forEach(link => {
      link.classList.toggle('active', link.dataset.page === page);
    });

    document.getElementById('page-title').textContent = this.pageTitleMap[page] || page;

    document.getElementById('page-container').innerHTML =
      '<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>';

    try {
      switch (page) {
        case 'dashboard': await dashboardPage.render(); break;
        case 'customers': await customersPage.render(); break;
        case 'policies': await policiesPage.render(); break;
        case 'claims': await claimsPage.render(); break;
        case 'payments': await paymentsPage.render(); break;
        case 'agents': await agentsPage.render(); break;
        case 'commissions': await commissionsPage.render(); break;
        case 'users': await usersPage.render(); break;
        case 'profile': await profilePage.render(); break;
        default: await dashboardPage.render();
      }
    } catch (err) {
      document.getElementById('page-container').innerHTML =
        `<div class="alert alert-danger">Error loading page: ${err.message}</div>`;
    }
  },

  showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    const colors = { success: 'bg-success', danger: 'bg-danger', warning: 'bg-warning text-dark', info: 'bg-info' };
    const bgClass = colors[type] || 'bg-success';
    const icon = { success: 'fa-check-circle', danger: 'fa-exclamation-circle', warning: 'fa-exclamation-triangle', info: 'fa-info-circle' };

    const id = 'toast-' + Date.now();
    const html = `
      <div id="${id}" class="toast toast-custom align-items-center text-white border-0 ${bgClass}" role="alert">
        <div class="d-flex">
          <div class="toast-body">
            <i class="fas ${icon[type] || 'fa-check-circle'} me-2"></i>${this.esc(message)}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>
    `;
    container.insertAdjacentHTML('beforeend', html);
    const el = document.getElementById(id);
    const toast = bootstrap.Toast.getOrCreateInstance(el, { delay: 4000 });
    toast.show();
    el.addEventListener('hidden.bs.toast', () => el.remove());
  },

  debounce(fn, delay) {
    let timer;
    return (...args) => {
      clearTimeout(timer);
      timer = setTimeout(() => fn(...args), delay);
    };
  },

  esc(str) {
    if (str === null || str === undefined) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
  }
};

document.addEventListener('DOMContentLoaded', () => app.init());

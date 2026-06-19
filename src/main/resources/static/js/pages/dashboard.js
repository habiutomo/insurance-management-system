const dashboardPage = {
  chartInstances: {},

  async render() {
    const container = document.getElementById('page-container');
    container.innerHTML = `
      <div id="dashboard-content">
        <div class="row g-3 mb-4" id="stats-cards"></div>
        <div class="row g-3">
          <div class="col-lg-8">
            <div class="card">
              <div class="card-body">
                <h6 class="fw-semibold mb-3"><i class="fas fa-chart-bar me-2 text-primary"></i>Policy Distribution</h6>
                <div class="chart-container"><canvas id="chart-policies"></canvas></div>
              </div>
            </div>
          </div>
          <div class="col-lg-4">
            <div class="card">
              <div class="card-body">
                <h6 class="fw-semibold mb-3"><i class="fas fa-chart-doughnut me-2 text-primary"></i>Claims Status</h6>
                <div class="chart-container"><canvas id="chart-claims"></canvas></div>
              </div>
            </div>
          </div>
        </div>
        <div class="row g-3 mt-2">
          <div class="col-lg-6">
            <div class="card">
              <div class="card-body">
                <h6 class="fw-semibold mb-3"><i class="fas fa-chart-line me-2 text-primary"></i>Underwriting Status</h6>
                <div class="chart-container"><canvas id="chart-underwriting"></canvas></div>
              </div>
            </div>
          </div>
          <div class="col-lg-6">
            <div class="card">
              <div class="card-body">
                <h6 class="fw-semibold mb-3"><i class="fas fa-coins me-2 text-primary"></i>Payment Status</h6>
                <div class="chart-container"><canvas id="chart-payments"></canvas></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
    await this.loadStats();
  },

  async loadStats() {
    try {
      const res = await api.get('/api/dashboard/stats');
      if (res.success && res.data) {
        this.renderStats(res.data);
        this.renderCharts(res.data);
      }
    } catch (err) {
      app.showToast('Error loading dashboard: ' + err.message, 'danger');
    }
  },

  renderStats(data) {
    const cards = [
      { label: 'Total Customers', value: data.totalCustomers || 0, icon: 'users', color: '#4361ee' },
      { label: 'Total Policies', value: data.totalPolicies || 0, icon: 'file-contract', color: '#3a86ff' },
      { label: 'Active Policies', value: data.activePolicies || 0, icon: 'check-circle', color: '#06d6a0' },
      { label: 'Total Claims', value: data.totalClaims || 0, icon: 'file-invoice', color: '#fb5607' },
      { label: 'Total Agents', value: data.totalAgents || 0, icon: 'user-tie', color: '#8338ec' },
      { label: 'Paid Payments', value: data.paidPayments || 0, icon: 'credit-card', color: '#06d6a0' },
      { label: 'Pending Payments', value: data.pendingPayments || 0, icon: 'clock', color: '#ffbe0b' },
      { label: 'Pending Commissions', value: data.pendingCommissions || 0, icon: 'hand-holding-dollar', color: '#ff006e' },
      { label: 'Total Commissions', value: data.totalCommissions || 0, icon: 'coins', color: '#4cc9f0' },
    ];

    const container = document.getElementById('stats-cards');
    container.innerHTML = cards.map(c => `
      <div class="col-xl-3 col-lg-4 col-md-6">
        <div class="card stat-card" style="border-left-color: ${c.color}">
          <div class="card-body position-relative">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <div class="stat-value">${c.value}</div>
                <div class="stat-label">${c.label}</div>
              </div>
              <div class="stat-icon"><i class="fas fa-${c.icon}"></i></div>
            </div>
          </div>
        </div>
      </div>
    `).join('');
  },

  renderCharts(data) {
    this.destroyCharts();

    if (document.getElementById('chart-policies')) {
      this.chartInstances.policies = new Chart(document.getElementById('chart-policies'), {
        type: 'bar',
        data: {
          labels: ['Auto', 'Health', 'Life', 'Property', 'Travel'],
          datasets: [{
            label: 'Policies',
            data: [data.autoPolicies || 0, data.healthPolicies || 0, data.lifePolicies || 0, data.propertyPolicies || 0, data.travelPolicies || 0],
            backgroundColor: ['#4361ee', '#3a86ff', '#4cc9f0', '#06d6a0', '#ffbe0b'],
            borderRadius: 6,
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
        }
      });
    }

    if (document.getElementById('chart-claims')) {
      this.chartInstances.claims = new Chart(document.getElementById('chart-claims'), {
        type: 'doughnut',
        data: {
          labels: ['Submitted', 'Under Review', 'Approved', 'Rejected', 'Paid'],
          datasets: [{
            data: [data.submittedClaims || 0, data.underReviewClaims || 0, data.approvedClaims || 0, data.rejectedClaims || 0, data.paidClaims || 0],
            backgroundColor: ['#ffbe0b', '#fb5607', '#06d6a0', '#ef476f', '#4361ee'],
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'bottom', labels: { boxWidth: 12, padding: 8 } } }
        }
      });
    }

    if (document.getElementById('chart-underwriting')) {
      this.chartInstances.underwriting = new Chart(document.getElementById('chart-underwriting'), {
        type: 'bar',
        data: {
          labels: ['Pending', 'Approved', 'Declined'],
          datasets: [{
            label: 'Underwriting',
            data: [data.pendingUnderwriting || 0, data.approvedUnderwriting || 0, data.declinedUnderwriting || 0],
            backgroundColor: ['#ffbe0b', '#06d6a0', '#ef476f'],
            borderRadius: 6,
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
        }
      });
    }

    if (document.getElementById('chart-payments')) {
      this.chartInstances.payments = new Chart(document.getElementById('chart-payments'), {
        type: 'doughnut',
        data: {
          labels: ['Paid', 'Pending', 'Overdue', 'Cancelled'],
          datasets: [{
            data: [data.paidPayments || 0, data.pendingPayments || 0, data.overduePayments || 0, 0],
            backgroundColor: ['#06d6a0', '#ffbe0b', '#ef476f', '#adb5bd'],
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'bottom', labels: { boxWidth: 12, padding: 8 } } }
        }
      });
    }
  },

  destroyCharts() {
    Object.values(this.chartInstances).forEach(c => {
      if (c && typeof c.destroy === 'function') c.destroy();
    });
    this.chartInstances = {};
  }
};

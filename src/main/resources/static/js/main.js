/**
 * ReliefHub: A Web-Based Disaster Management System
 * Core Client-Side JavaScript Logic
 */

const API_BASE = '/api';

// Toast Notification System
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `
    <span>${message}</span>
  `;

  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(10px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

// Global API Request Wrapper
async function apiRequest(endpoint, options = {}) {
  const url = `${API_BASE}${endpoint}`;
  const defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  };

  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...(options.headers || {})
    }
  };

  try {
    const response = await fetch(url, config);
    const data = await response.json();

    if (!response.ok) {
      const errorMsg = data && data.message ? data.message : 'Server request failed';
      throw new Error(errorMsg);
    }
    return data;
  } catch (err) {
    console.error(`API Error on [${endpoint}]:`, err);
    throw err;
  }
}

// User Session Management
function getStoredUser() {
  const raw = localStorage.getItem('reliefhub_user');
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch (e) {
    return null;
  }
}

function setStoredUser(user) {
  if (user) {
    localStorage.setItem('reliefhub_user', JSON.stringify(user));
  } else {
    localStorage.removeItem('reliefhub_user');
  }
}

async function verifyAuth(allowedRoles = []) {
  try {
    const res = await apiRequest('/auth/me');
    if (res.success && res.data) {
      setStoredUser(res.data);
      const user = res.data;

      if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
        showToast('Access denied for this role.', 'error');
        redirectToRoleDashboard(user.role);
        return null;
      }
      return user;
    }
  } catch (e) {
    console.warn('Session verification failed:', e.message);
    setStoredUser(null);
    if (allowedRoles.length > 0) {
      window.location.href = 'login.html';
    }
  }
  return null;
}

function redirectToRoleDashboard(role) {
  if (role === 'ADMIN') {
    window.location.href = 'admin-dashboard.html';
  } else if (role === 'CAMP_MANAGER') {
    window.location.href = 'manager-dashboard.html';
  } else if (role === 'VICTIM') {
    window.location.href = 'victim-dashboard.html';
  } else {
    window.location.href = 'index.html';
  }
}

async function logoutUser() {
  try {
    await apiRequest('/auth/logout', { method: 'POST' });
  } catch (e) {
    console.warn('Logout server request failed:', e);
  } finally {
    setStoredUser(null);
    showToast('You have been logged out', 'info');
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 500);
  }
}

// Modal Helpers
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
  }
}

// Status badge helper
function getStatusBadge(status) {
  const st = (status || 'Pending').toLowerCase();
  if (st === 'pending') {
    return `<span class="badge badge-pending">Pending</span>`;
  } else if (st === 'assigned') {
    return `<span class="badge badge-assigned">Assigned</span>`;
  } else if (st === 'in-progress' || st === 'in_progress') {
    return `<span class="badge badge-progress">In-Progress</span>`;
  } else if (st === 'resolved' || st === 'completed') {
    return `<span class="badge badge-resolved">Resolved</span>`;
  }
  return `<span class="badge badge-pending">${status}</span>`;
}

// Capacity Progress Bar Helper
function renderCapacityBar(occupied, total) {
  if (!total || total <= 0) return '';
  const pct = Math.min(100, Math.round((occupied / total) * 100));
  let fillClass = 'progress-fill-low';
  if (pct > 75) fillClass = 'progress-fill-high';
  else if (pct > 40) fillClass = 'progress-fill-medium';

  return `
    <div style="font-size:0.775rem; display:flex; justify-content:space-between; margin-bottom:2px;">
      <span><strong>${occupied}</strong> / ${total} Beds Occupied</span>
      <span><strong>${pct}%</strong></span>
    </div>
    <div class="progress-bar-container">
      <div class="progress-bar-fill ${fillClass}" style="width: ${pct}%;"></div>
    </div>
  `;
}

// Quick Demo Login Credential Filler
function quickFillLogin(role) {
  const emailInput = document.getElementById('email');
  const passwordInput = document.getElementById('password');
  if (!emailInput || !passwordInput) return;

  if (role === 'admin') {
    emailInput.value = 'admin@reliefhub.org';
    passwordInput.value = 'admin123';
  } else if (role === 'manager') {
    emailInput.value = 'rahul.manager@reliefhub.org';
    passwordInput.value = 'manager123';
  } else if (role === 'victim') {
    emailInput.value = 'anand.k@example.com';
    passwordInput.value = 'victim123';
  }
  showToast(`Filled ${role.toUpperCase()} credentials`, 'info');
}

// Public Tracker Function
async function trackRequestPublic(reqId) {
  if (!reqId || !reqId.trim()) {
    showToast('Please enter a Request ID (e.g., REQ-1001)', 'warning');
    return;
  }
  const resultDiv = document.getElementById('tracker-result');
  if (resultDiv) {
    resultDiv.innerHTML = '<div style="padding:1rem; text-align:center;">Looking up request...</div>';
  }

  try {
    const res = await apiRequest(`/public/track/${encodeURIComponent(reqId.trim())}`);
    if (res.success && res.data) {
      const r = res.data;
      if (resultDiv) {
        resultDiv.innerHTML = `
          <div style="background:#fff; border-radius:10px; padding:1.25rem; margin-top:1rem; border:1px solid #e2e8f0; color:#0f172a; box-shadow:0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.75rem;">
              <h4 style="margin:0; font-size:1.1rem; color:#1e3a8a;">Request ${r.requestId}</h4>
              ${getStatusBadge(r.status)}
            </div>
            <p style="margin:0.25rem 0; font-size:0.9rem;"><strong>Type:</strong> ${r.requestType} | <strong>Date:</strong> ${r.requestDate}</p>
            <p style="margin:0.25rem 0; font-size:0.9rem;"><strong>Assigned Camp:</strong> ${r.campName || '<span style="color:#d97706;">Pending Camp Assignment</span>'}</p>
            ${r.campLocation ? `<p style="margin:0.25rem 0; font-size:0.85rem; color:#64748b;">Camp Location: ${r.campLocation}</p>` : ''}
            <p style="margin:0.5rem 0 0; font-size:0.875rem; background:#f8fafc; padding:0.6rem; border-radius:6px; border:1px solid #f1f5f9;">
              <strong>Details:</strong> ${r.details || 'No additional details'}
            </p>
          </div>
        `;
      }
    }
  } catch (err) {
    if (resultDiv) {
      resultDiv.innerHTML = `
        <div style="background:#fef2f2; border:1px solid #fecaca; color:#991b1b; padding:1rem; border-radius:8px; margin-top:1rem;">
          ${err.message || 'Request not found. Please verify the ID.'}
        </div>
      `;
    }
  }
}

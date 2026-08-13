const USERS_API = 'http://localhost:8083';
const SOL_API = 'http://localhost:8084';

const state = {
  token: '',
  role: '',
  email: '',
};

const loginForm = document.getElementById('loginForm');
const loginResult = document.getElementById('loginResult');
const actionResult = document.getElementById('actionResult');
const rolesList = document.getElementById('rolesList');
const requestsList = document.getElementById('requestsList');

function pretty(data) {
  return JSON.stringify(data, null, 2);
}

function showJson(el, value) {
  el.textContent = typeof value === 'string' ? value : pretty(value);
}

function headers(auth = false) {
  const result = { 'Content-Type': 'application/json' };
  if (auth && state.token) {
    result.Authorization = `Bearer ${state.token}`;
  }
  return result;
}

async function requestJson(url, options = {}) {
  const response = await fetch(url, options);
  const text = await response.text();
  let body;
  try {
    body = text ? JSON.parse(text) : {};
  } catch {
    body = text;
  }

  if (!response.ok) {
    throw new Error(typeof body === 'string' ? body : (body.responseMessage || body.message || 'Error desconocido'));
  }

  return body;
}

loginForm.addEventListener('submit', async event => {
  event.preventDefault();
  const email = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;

  try {
    const data = await requestJson(`${USERS_API}/api/v1/auth/login`, {
      method: 'POST',
      headers: headers(),
      body: JSON.stringify({ email, password })
    });

    state.token = data.data.token;
    state.email = data.data.email;
    state.role = Array.from(data.data.roles || []).join(', ');
    showJson(loginResult, {
      message: 'Login correcto',
      email: data.data.email,
      roles: data.data.roles,
      tokenPreview: `${state.token.slice(0, 24)}...`
    });
    showJson(actionResult, 'Sesión iniciada. Ya puedes cargar roles o solicitudes.');
  } catch (error) {
    showJson(loginResult, `Error: ${error.message}`);
  }
});

document.getElementById('btnRoles').addEventListener('click', async () => {
  try {
    const data = await requestJson(`${USERS_API}/api/v1/roles`);
    rolesList.innerHTML = data.data.map(role => `
      <article class="card-item">
        <strong>${role.name}</strong>
        <p class="muted">${role.description || 'Sin descripción'}</p>
        <small>${role.fechaCreacion || ''}</small>
      </article>
    `).join('');
    showJson(actionResult, data);
  } catch (error) {
    showJson(actionResult, `Error cargando roles: ${error.message}`);
  }
});

document.getElementById('btnUsers').addEventListener('click', async () => {
  try {
    const data = await requestJson(`${USERS_API}/api/v1/auth/register`, {
      method: 'POST',
      headers: headers(),
      body: JSON.stringify({
        email: `demo${Date.now()}@mail.com`,
        firstName: 'Demo',
        lastName: 'User',
        password: '123456',
        roleNames: ['CLIENTE']
      })
    });
    showJson(actionResult, data);
  } catch (error) {
    showJson(actionResult, `Error registrando usuario: ${error.message}`);
  }
});

document.getElementById('btnRequest').addEventListener('click', async () => {
  if (!state.token) {
    showJson(actionResult, 'Primero inicia sesión.');
    return;
  }

  try {
    const data = await requestJson(`${SOL_API}/solicitudes`, {
      method: 'POST',
      headers: headers(true),
      body: JSON.stringify({
        titulo: 'Solicitud demo',
        descripcion: 'Creada desde el frontend sencillo',
        estado: 'PENDIENTE',
        observaciones: 'Prueba visual'
      })
    });
    showJson(actionResult, data);
  } catch (error) {
    showJson(actionResult, `Error creando solicitud: ${error.message}`);
  }
});

document.getElementById('btnMyRequests').addEventListener('click', async () => {
  if (!state.token) {
    showJson(actionResult, 'Primero inicia sesión.');
    return;
  }

  try {
    const data = await requestJson(`${SOL_API}/solicitudes/mis-solicitudes`, {
      headers: headers(true)
    });
    requestsList.innerHTML = data.map(req => `
      <article class="card-item">
        <strong>${req.titulo}</strong>
        <p>${req.descripcion || ''}</p>
        <small>${req.estado || 'SIN_ESTADO'}</small>
      </article>
    `).join('');
    showJson(actionResult, data);
  } catch (error) {
    showJson(actionResult, `Error obteniendo solicitudes: ${error.message}`);
  }
});

showJson(loginResult, 'Listo para iniciar sesión.');
showJson(actionResult, 'Frontend listo.');

const API_BASE = '/api/stock-movements';

const testConnectionBtn = document.getElementById('testConnectionBtn');
const refreshBtn = document.getElementById('refreshBtn');
const movementForm = document.getElementById('movementForm');
const connectionStatus = document.getElementById('connectionStatus');
const createStatus = document.getElementById('createStatus');
const movementsTableBody = document.getElementById('movementsTableBody');

function toIsoFromLocalDateTime(localDateTime) {
    return new Date(localDateTime).toISOString();
}

function setStatus(element, message, ok = true) {
    element.textContent = message;
    element.style.color = ok ? '#15803d' : '#b91c1c';
}

async function fetchMovements() {
    const res = await fetch(API_BASE);
    if (!res.ok) {
        throw new Error(`HTTP ${res.status}`);
    }
    return res.json();
}

function renderMovements(data) {
    movementsTableBody.innerHTML = '';

    data.forEach(movement => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${movement.publicId ?? ''}</td>
            <td>${movement.productName ?? ''}</td>
            <td>${movement.movementType ?? ''}</td>
            <td>${movement.quantity ?? ''}</td>
            <td>${movement.oldStock ?? ''}</td>
            <td>${movement.newStock ?? ''}</td>
            <td>${movement.movementDate ?? ''}</td>
        `;
        movementsTableBody.appendChild(row);
    });
}

async function testConnection() {
    try {
        const data = await fetchMovements();
        setStatus(connectionStatus, `Connection OK. API reachable, rows fetched: ${data.length}`);
    } catch (e) {
        setStatus(connectionStatus, `Connection failed: ${e.message}`, false);
    }
}

async function refreshList() {
    try {
        const data = await fetchMovements();
        renderMovements(data);
    } catch (e) {
        setStatus(createStatus, `Unable to refresh list: ${e.message}`, false);
    }
}

function buildPayload() {
    const movementDateLocal = document.getElementById('movementDate').value;

    return {
        productPublicId: document.getElementById('productPublicId').value.trim(),
        productName: document.getElementById('productName').value.trim(),
        movementType: document.getElementById('movementType').value,
        quantity: Number(document.getElementById('quantity').value),
        oldStock: Number(document.getElementById('oldStock').value),
        newStock: Number(document.getElementById('newStock').value),
        sourceType: document.getElementById('sourceType').value,
        sourceReference: document.getElementById('sourceReference').value.trim() || null,
        movementDate: toIsoFromLocalDateTime(movementDateLocal),
        comment: document.getElementById('comment').value.trim() || null
    };
}

async function createMovement(event) {
    event.preventDefault();
    const payload = buildPayload();

    try {
        const res = await fetch(API_BASE, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errBody = await res.json().catch(() => ({}));
            throw new Error(errBody.message || `HTTP ${res.status}`);
        }

        const created = await res.json();
        setStatus(createStatus, `Created movement ${created.publicId}`);
        movementForm.reset();
        await refreshList();
    } catch (e) {
        setStatus(createStatus, `Create failed: ${e.message}`, false);
    }
}

testConnectionBtn.addEventListener('click', testConnection);
refreshBtn.addEventListener('click', refreshList);
movementForm.addEventListener('submit', createMovement);

refreshList();

/**
 * MONSTER CONVERSOR - CLIENT INTERACTIVITY (JSP / JAKARTA EE MVC)
 */

const UNIDADES = {
    LONGITUD: [
        { id: 'MILIMETRO', label: 'Milímetro (mm)' },
        { id: 'CENTIMETRO', label: 'Centímetro (cm)' },
        { id: 'METRO', label: 'Metro (m)' },
        { id: 'KILOMETRO', label: 'Kilómetro (km)' },
        { id: 'YARDA', label: 'Yarda (yd)' }
    ],
    MASA: [
        { id: 'MILIGRAMO', label: 'Miligramo (mg)' },
        { id: 'GRAMO', label: 'Gramo (g)' },
        { id: 'KILOGRAMO', label: 'Kilogramo (kg)' },
        { id: 'TONELADA', label: 'Tonelada (t)' },
        { id: 'ONZA', label: 'Onza (oz)' }
    ],
    TEMPERATURA: [
        { id: 'CELSIUS', label: 'Celsius (°C)' },
        { id: 'FAHRENHEIT', label: 'Fahrenheit (°F)' },
        { id: 'KELVIN', label: 'Kelvin (K)' },
        { id: 'RANKINE', label: 'Rankine (°R)' }
    ]
};

document.addEventListener('DOMContentLoaded', () => {
    initMagnitudeTabs();
    initSwapButton();
    initPasswordToggle();
    initCopyResult();
    syncInitialUnits();
});

function syncInitialUnits() {
    const inputMagnitud = document.getElementById('inputMagnitud');
    const selectOrigen = document.getElementById('selectOrigen');
    const selectDestino = document.getElementById('selectDestino');

    if (!inputMagnitud || !selectOrigen || !selectDestino) return;

    const mag = inputMagnitud.value || 'LONGITUD';

    // Si los selects están vacíos o se cargaron sin opciones
    if (selectOrigen.options.length === 0) {
        cargarOpciones(mag, selectOrigen, selectDestino);
    }
}

function initMagnitudeTabs() {
    const tabs = document.querySelectorAll('.tab-btn');
    const inputMagnitud = document.getElementById('inputMagnitud');
    const selectOrigen = document.getElementById('selectOrigen');
    const selectDestino = document.getElementById('selectDestino');

    if (!tabs.length || !selectOrigen || !selectDestino) return;

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            const mag = tab.dataset.magnitude;
            if (inputMagnitud) inputMagnitud.value = mag;

            cargarOpciones(mag, selectOrigen, selectDestino);
        });
    });
}

function cargarOpciones(magnitud, selOrigen, selDestino) {
    const lista = UNIDADES[magnitud] || UNIDADES.LONGITUD;

    selOrigen.innerHTML = '';
    selDestino.innerHTML = '';

    lista.forEach((u) => {
        const opt1 = new Option(u.label, u.id);
        const opt2 = new Option(u.label, u.id);

        selOrigen.add(opt1);
        selDestino.add(opt2);
    });

    if (lista.length > 1) {
        selOrigen.selectedIndex = 0;
        selDestino.selectedIndex = 1;
    }
}

function initSwapButton() {
    const btnSwap = document.getElementById('btnSwap');
    const selectOrigen = document.getElementById('selectOrigen');
    const selectDestino = document.getElementById('selectDestino');

    if (!btnSwap || !selectOrigen || !selectDestino) return;

    btnSwap.addEventListener('click', (e) => {
        e.preventDefault();
        const tmp = selectOrigen.value;
        selectOrigen.value = selectDestino.value;
        selectDestino.value = tmp;
    });
}

function initCopyResult() {
    const displayResultado = document.getElementById('displayResultado');
    if (!displayResultado) return;

    displayResultado.style.cursor = 'pointer';
    displayResultado.title = 'Haz clic para copiar el resultado';

    displayResultado.addEventListener('click', () => {
        const text = displayResultado.textContent.trim();
        if (text && text !== '0.0000') {
            navigator.clipboard.writeText(text).then(() => {
                const original = displayResultado.textContent;
                displayResultado.textContent = '¡Copiado! ✓';
                setTimeout(() => {
                    displayResultado.textContent = original;
                }, 1000);
            });
        }
    });
}

function initPasswordToggle() {
    const btnToggle = document.getElementById('btnTogglePassword');
    const passInput = document.getElementById('contrasenia');

    if (!btnToggle || !passInput) return;

    btnToggle.addEventListener('click', (e) => {
        e.preventDefault();
        const type = passInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passInput.setAttribute('type', type);
        btnToggle.textContent = type === 'password' ? '👁️' : '🔒';
    });
}

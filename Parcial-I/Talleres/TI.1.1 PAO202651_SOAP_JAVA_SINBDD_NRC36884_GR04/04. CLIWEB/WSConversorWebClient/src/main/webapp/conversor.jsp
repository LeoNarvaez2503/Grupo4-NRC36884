<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ec.edu.monster.wsconversorweb.model.*" %>
<%
    UsuarioSession usuario = (UsuarioSession) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String magnitud = (String) request.getAttribute("magnitud");
    if (magnitud == null) magnitud = "LONGITUD";

    Double valor = (Double) request.getAttribute("valor");
    String unidadInicial = (String) request.getAttribute("unidadInicial");
    String unidadFinal = (String) request.getAttribute("unidadFinal");
    String resultadoFormateado = (String) request.getAttribute("resultadoFormateado");
    String mensajeError = (String) request.getAttribute("mensajeError");

    String titulo = "Conversor de Unidades";
    if ("MASA".equalsIgnoreCase(magnitud)) {
        titulo = "Conversor de Masa";
    } else if ("TEMPERATURA".equalsIgnoreCase(magnitud)) {
        titulo = "Conversor de Temperatura";
    } else if ("LONGITUD".equalsIgnoreCase(magnitud)) {
        titulo = "Conversor de Longitudes";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= titulo %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<!-- Barra superior simple con usuario y salir -->
<div class="top-bar">
    <div class="top-bar-title">
        <img src="${pageContext.request.contextPath}/images/sullivan.png" alt="Sulley" style="width: 26px; height: 26px; vertical-align: middle; margin-right: 6px; object-fit: contain;">
        WS Conversor
    </div>
    <div class="top-bar-user">
        <span>Usuario: <strong><%= usuario.getUsuario() %></strong></span>
        <a href="${pageContext.request.contextPath}/logout" class="link-logout">Cerrar Sesión</a>
    </div>
</div>

<div class="window-wrapper">
    <div class="window-card window-card-large">

        <div style="text-align: center; margin-bottom: 8px;">
            <img src="${pageContext.request.contextPath}/images/sullivan.png" 
                 alt="James P. Sullivan - Monsters Inc" 
                 style="width: 80px; height: 80px; object-fit: contain; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.15));">
        </div>

        <h2 class="window-title" id="tituloConversor"><%= titulo %></h2>

        <!-- Mensaje de error si ocurre -->
        <% if (mensajeError != null) { %>
            <div class="alert-box alert-box-error">
                <%= mensajeError %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/conversor" method="post" id="formConversor">

            <!-- Fila 1: Magnitud y Valor -->
            <div class="form-row">
                <label for="cmbMagnitud" class="form-label">Magnitud:</label>
                <select id="cmbMagnitud" name="magnitud" class="form-control" style="max-width: 180px;">
                    <option value="LONGITUD" <%= "LONGITUD".equalsIgnoreCase(magnitud) ? "selected" : "" %>>LONGITUD</option>
                    <option value="MASA" <%= "MASA".equalsIgnoreCase(magnitud) ? "selected" : "" %>>MASA</option>
                    <option value="TEMPERATURA" <%= "TEMPERATURA".equalsIgnoreCase(magnitud) ? "selected" : "" %>>TEMPERATURA</option>
                </select>

                <input type="number" 
                       step="any" 
                       id="valor" 
                       name="valor" 
                       class="form-control" 
                       placeholder="Valor" 
                       value="<%= valor != null ? valor : "1.0" %>" 
                       required 
                       autofocus>
            </div>

            <!-- Fila 2: undInit  a  undFinal  [Convertir] -->
            <div class="conversion-row">
                <select id="undInit" name="unidadInicial" class="form-control" required>
                    <!-- Opciones actualizadas por JS -->
                </select>

                <span class="conversion-to">a</span>

                <select id="undFinal" name="unidadFinal" class="form-control" required>
                    <!-- Opciones actualizadas por JS -->
                </select>

                <button type="submit" class="btn btn-primary" id="btnConvertir" style="min-width: 120px;">
                    Convertir
                </button>
            </div>

        </form>

        <!-- Fila 3: Resultado: [ conversionResultado ] -->
        <div class="result-box">
            <span class="result-label">Resultado:</span>
            <span class="result-value" id="conversionResultado">
                <%= resultadoFormateado != null ? resultadoFormateado : "" %>
            </span>
        </div>

    </div>
</div>

<script>
    // Unidades idénticas al cliente de escritorio
    const UNIDADES = {
        LONGITUD: [
            { id: 'MILIMETRO', label: 'MILIMETRO' },
            { id: 'CENTIMETRO', label: 'CENTIMETRO' },
            { id: 'METRO', label: 'METRO' },
            { id: 'KILOMETRO', label: 'KILOMETRO' },
            { id: 'YARDA', label: 'YARDA' }
        ],
        MASA: [
            { id: 'MILIGRAMO', label: 'MILIGRAMO' },
            { id: 'GRAMO', label: 'GRAMO' },
            { id: 'KILOGRAMO', label: 'KILOGRAMO' },
            { id: 'TONELADA', label: 'TONELADA' },
            { id: 'ONZA', label: 'ONZA' }
        ],
        TEMPERATURA: [
            { id: 'CELSIUS', label: 'CELSIUS' },
            { id: 'FAHRENHEIT', label: 'FAHRENHEIT' },
            { id: 'KELVIN', label: 'KELVIN' },
            { id: 'RANKINE', label: 'RANKINE' }
        ]
    };

    const TITULOS = {
        LONGITUD: 'Conversor de Longitudes',
        MASA: 'Conversor de Masa',
        TEMPERATURA: 'Conversor de Temperatura'
    };

    const cmbMagnitud = document.getElementById('cmbMagnitud');
    const undInit = document.getElementById('undInit');
    const undFinal = document.getElementById('undFinal');
    const tituloConversor = document.getElementById('tituloConversor');

    // Valor seleccionado previo del servidor si existe
    const initSeleccionado = '<%= unidadInicial != null ? unidadInicial : "" %>';
    const finalSeleccionado = '<%= unidadFinal != null ? unidadFinal : "" %>';

    function actualizarUnidades(conservarSeleccion) {
        const seleccion = cmbMagnitud.value || 'LONGITUD';
        tituloConversor.textContent = TITULOS[seleccion] || 'Conversor de Unidades';
        document.title = TITULOS[seleccion] || 'Conversor de Unidades';

        const lista = UNIDADES[seleccion] || UNIDADES.LONGITUD;

        undInit.innerHTML = '';
        undFinal.innerHTML = '';

        lista.forEach(u => {
            const opt1 = new Option(u.label, u.id);
            const opt2 = new Option(u.label, u.id);

            if (conservarSeleccion && u.id === initSeleccionado) opt1.selected = true;
            if (conservarSeleccion && u.id === finalSeleccionado) opt2.selected = true;

            undInit.add(opt1);
            undFinal.add(opt2);
        });

        // Si no se conservó selección, por defecto primero y segundo
        if (!conservarSeleccion && lista.length > 1) {
            undInit.selectedIndex = 0;
            undFinal.selectedIndex = 1;
        }
    }

    cmbMagnitud.addEventListener('change', () => {
        actualizarUnidades(false);
        const resLabel = document.getElementById('conversionResultado');
        if (resLabel) resLabel.textContent = '';
    });

    // Carga inicial
    actualizarUnidades(initSeleccionado !== '');
</script>

</body>
</html>

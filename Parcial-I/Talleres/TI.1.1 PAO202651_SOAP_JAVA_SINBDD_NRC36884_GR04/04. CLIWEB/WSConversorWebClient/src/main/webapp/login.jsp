<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión | Conversor SOAP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="window-wrapper">
    <div class="window-card">
        
        <div style="text-align: center; margin-bottom: 12px;">
            <img src="${pageContext.request.contextPath}/images/sullivan.png" 
                 alt="James P. Sullivan - Monsters Inc" 
                 style="width: 120px; height: 120px; object-fit: contain; filter: drop-shadow(0 4px 8px rgba(0,0,0,0.2));">
        </div>

        <h2 class="window-title">INICIAR SESIÓN</h2>

        <!-- Mensaje de error -->
        <% if (request.getAttribute("mensajeError") != null) { %>
            <div class="alert-box alert-box-error">
                <%= request.getAttribute("mensajeError") %>
            </div>
        <% } %>

        <!-- Mensaje de info -->
        <% if (request.getAttribute("mensajeInfo") != null) { %>
            <div class="alert-box alert-box-info">
                <%= request.getAttribute("mensajeInfo") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <div class="form-row">
                <label for="usuario" class="form-label">Usuario:</label>
                <input type="text" 
                       id="usuario" 
                       name="usuario" 
                       class="form-control" 
                       value="${usuarioIngresado != null ? usuarioIngresado : ''}" 
                       required 
                       autofocus>
            </div>

            <div class="form-row">
                <label for="contrasenia" class="form-label">Contraseña:</label>
                <input type="password" 
                       id="contrasenia" 
                       name="contrasenia" 
                       class="form-control" 
                       required>
            </div>

            <div class="form-check">
                <input type="checkbox" id="chkMostrar">
                <label for="chkMostrar" style="cursor: pointer;">Mostrar contraseña</label>
            </div>

            <div class="buttons-row">
                <button type="submit" class="btn btn-primary" id="btnIngresar">Ingresar</button>
                <button type="button" class="btn btn-secondary" id="btnLimpiar">Limpiar</button>
            </div>
        </form>

        <div class="demo-help">
            Credenciales: <strong>MONSTER</strong> / <strong>MONSTER9</strong>
        </div>

    </div>
</div>

<script>
    // Mostrar/ocultar contraseña (idéntico al checkbox de Swing)
    const chkMostrar = document.getElementById('chkMostrar');
    const passInput = document.getElementById('contrasenia');
    if (chkMostrar && passInput) {
        chkMostrar.addEventListener('change', () => {
            passInput.type = chkMostrar.checked ? 'text' : 'password';
        });
    }

    // Botón Limpiar (idéntico a btnLimpiarActionPerformed en Swing)
    const btnLimpiar = document.getElementById('btnLimpiar');
    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', () => {
            document.getElementById('usuario').value = '';
            document.getElementById('contrasenia').value = '';
            document.getElementById('usuario').focus();
        });
    }
</script>

</body>
</html>

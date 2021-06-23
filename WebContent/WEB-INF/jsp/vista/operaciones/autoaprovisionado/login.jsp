<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.autoregistrado.controlador.ControladorUsuarioAutoregistrado"%>
<%@ page import="es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioAutoregistrado bean = (VistaUsuarioAutoregistrado)uvdatos.getVistas().get(VistaUsuarioAutoregistrado.class.getName());
%>
<style>

.login-block {
    width: 320px;
    padding: 20px;
    background: #F1F1F1;
    border-radius: 5px;
    border-top: 5px solid #006D38;
}

.login-block input {
    width: 100%;
    height: 42px;
    box-sizing: border-box;
    border-radius: 5px;
    border: 1px solid #ccc;
    margin-bottom: 20px;
    font-size: 14px;
    font-family: Montserrat;
    padding: 0 20px 0 50px;
    outline: none;
}

.login-block input#username {
    background: #fff url('/img/autoregistrado/usuario.png') 20px top no-repeat;
    background-size: 16px 80px;
}

.login-block input#username:focus {
    background-size: 16px 80px;
}

.login-block input#password {
    background: #fff url('/img/autoregistrado/candado.png') 20px top no-repeat;
    background-size: 16px 80px;
}

.login-block input#password:focus {
    background-size: 16px 80px;
}

.login-block input:active, .login-block input:focus {
    border: 1px solid #006D38;
}

.login-block input[type=submit] {
    width: 50%;
    height: 40px;
    background: #006D38;
    box-sizing: border-box;
    border-radius: 5px;
    border: 1px solid #004921;
    color: #fff;
    font-weight: bold;
    text-transform: uppercase;
    font-size: 14px;
    font-family: Montserrat;
    outline: none;
    cursor: pointer;
}

.login-block input[type=submit]:hover {
    background: #004921;
}

.logintic {
	margin-top:3em;
	padding-right:10em;
	padding-bottom:3em;
}

.logintic a {
	margin-top:3em;
	padding: 0.8em;
    background: #006D38;
    box-sizing: border-box;
    border-radius: 5px;
    border: 1px solid #004921;
    color: #fff;
    font-weight: bold;
    text-transform: uppercase;
    font-size: 14px;
    font-family: Montserrat;
    outline: none;
    cursor: pointer;
}
</style>
<h2>Acceso a <%=bean.getDescripcionModulo() %></h2>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%=bean.formatearMensajesDeExito()%>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<br/>
			<br/>
			<br/>
			<%=bean.formatearMensajesDeError()%>
		</div>
  	<% } %>

	<div class="logintic" style="text-align:right">
		<a href="<%=bean.getPaginaRedireccion()%>">Acceso cuenta TIC (SIDUJA)</a>
	</div>

	<div class="login-block">
		<h3>Acceso usuarios externos (sin cuenta TIC)</h3>
		<form method="post" action="<%=request.getRequestURI()%>" id="formulario"> 
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_VALIDA%>" />
			<input type="hidden" id="g-recaptcha-response-valida" name="g-recaptcha-response" value="" />
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ID_MODULO%>" value="<%=bean.getIdModulo()%>" />
		<input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>" value="" placeholder="Correo electrónico" id="username" />
		<input type="password" name="<%=ControladorUsuarioAutoregistrado.PARAM_CLAVE%>" value="" placeholder="Clave" id="password" />
		<input type="submit" value="Entrar">
		</form>

		<h4><a href="" onclick="registro()">Registrarse</a></h4>
		<h4><a href="" onclick="olvido()">He olvidado mi clave</a></h4>
	</div>

<% if (bean.isMostrarCaptcha()) { %>
		<script src="https://www.google.com/recaptcha/api.js?render=<%=bean.getCaptchaPublica()%>"></script>
		<script>
			grecaptcha.ready(function() {
				grecaptcha.execute('<%=bean.getCaptchaPublica()%>', {action: 'olvidoClave'}).then(function(token) {
					$("#g-recaptcha-response-valida").val(token);
					$("#g-recaptcha-response-olvido").val(token);
				});
			});
			function olvido() {
				event.preventDefault()
				document.getElementById("accionFormulario").value = "<%=ControladorUsuarioAutoregistrado.ACCION_MUESTRA_OLVIDO%>";
				document.getElementById("formulario").submit();
			}
			
			function registro() {
				event.preventDefault()
				document.getElementById("accionFormulario").value = "<%=ControladorUsuarioAutoregistrado.ACCION_MOSTRAR_CREAR%>";
				document.getElementById("formulario").submit();
			}
		</script>
<% } %>
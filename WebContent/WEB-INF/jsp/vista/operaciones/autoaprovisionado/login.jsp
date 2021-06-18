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
    margin: 0 auto;
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
    background: #fff url('http://i.imgur.com/u0XmBmv.png') 20px top no-repeat;
    background-size: 16px 80px;
}

.login-block input#username:focus {
    background-size: 16px 80px;
}

.login-block input#password {
    background: #fff url('http://i.imgur.com/Qf83FTt.png') 20px top no-repeat;
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
</style>
<h2>Acceso a <%=bean.getDescripcionModulo() %></h2>
    <h3>Acceso </h3>
	<input type="submit" value="Entrar">
    <h3 style="padding-top: 2em;">Acceder con correo</h3>
<div class="login-block">
  		<form method="post" action="<%=request.getRequestURI()%>" id="formularioExiste"> 
	<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_VALIDA%>" />
	<input type="hidden" id="g-recaptcha-response-valida" name="g-recaptcha-response" value="" />
	<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ID_MODULO%>" value="<%=bean.getIdModulo()%>" />
	
	</form>
    <input type="text" value="" placeholder="Correo" id="username" />
    <input type="password" value="" placeholder="Clave" id="password" />
	<input type="submit" value="Entrar">
</div>
    <h4><a href="">Registrarse</a></h4>     
    <h4><a href="">Olvido clave</a></h4>

<% if (bean.isMostrarCaptcha()) { %>
		<script src="https://www.google.com/recaptcha/api.js?render=<%=bean.getCaptchaPublica()%>"></script>
		<script>
			grecaptcha.ready(function() {
				grecaptcha.execute('<%=bean.getCaptchaPublica()%>', {action: 'olvidoClave'}).then(function(token) {
					console.log(token);
					$("#g-recaptcha-response-valida").val(token);
					$("#g-recaptcha-response-olvido").val(token);
				});
			});
		</script>
<% } %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.autoregistrado.controlador.ControladorUsuarioAutoregistrado"%>
<%@ page import="es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioAutoregistrado bean = (VistaUsuarioAutoregistrado)uvdatos.getVistas().get(VistaUsuarioAutoregistrado.class.getName());
%>
<div>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%=bean.formatearMensajesDeExito()%>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%=bean.formatearMensajesDeError()%>
		</div>
  	<% } %>
	<h2>Acceso a <%=bean.getDescripcionModulo() %></h2>
	<div>
		Si se ha registrado y no recuerda su clave, introduzca su correo
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioOlvido"> 
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_OLVIDO%>" />
			<input type="hidden" id="g-recaptcha-response-olvido" name="g-recaptcha-response" value="" />
			<input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>">
			<input type="submit" value="Olvido Clave">
		</form>
	</div>
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
		</script>
<% } %>
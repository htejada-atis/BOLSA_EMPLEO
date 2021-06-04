<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.autoregistrado.controlador.ControladorUsuarioAutoregistrado"%>
<%@ page import="es.ujaen.uvirtual.modulo.autoregistrado.beans.vista.VistaUsuarioAutoregistrado" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioAutoregistrado bean = (VistaUsuarioAutoregistrado)uvdatos.getVistas().get(VistaUsuarioAutoregistrado.class.getName());
%>
<div>
	<%
	if (bean.getMensajesDeExito().size() > 0) {
	%>
		<div id="exito" class="success">
			<%=bean.formatearMensajesDeExito()%>
		</div>
	<%
	}
	%>
	<%
	if (bean.getMensajesDeError().size() > 0) {
	%>
		<div id="error" class="error">
			<%=bean.formatearMensajesDeError()%>
		</div>
	<%
	} else {
	%>
	<h2>Acceso de usuarios autoregistrados</h2>
	<div>
		Si ya tiene usuario acceda con este <a href="/srv">enlace</a>
		<hr/>
		Verificación usuario	
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioExiste"> 
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_VALIDA%>" />
			<input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>">
			<input type="password" name="<%=ControladorUsuarioAutoregistrado.PARAM_CLAVE%>">
			<input type="submit" value="usuario ya existente">
		</form>
		
		<hr/>	
		Para crear un nuevo usuario
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioNoExiste"> 
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_MOSTRAR_CREAR%>" />
			<input type="submit" value="Crear nuevo usuario">
		</form>
		<hr/>	
		Para recuperar su clave 
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioOlvido"> 
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_OLVIDO%>" />
			<input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>">
			<input type="submit" value="Olvido Clave">
		</form>
	</div>
	<%} %>
</div>
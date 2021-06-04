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
	<% } else { %>
		Se ha mandado un código temporal a su correo electrónico. Introduzca el código temporal en el siguiente formulario para validarlo.
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioCrear">
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_VALIDA_CODIGO_TEMPORAL%>" />
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>" value="<%=bean.getCorreo()%>" />
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ID_CAMBIO%>" value="<%=bean.getIdSolicitud()%>" />
			Código Temporal <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CODIGO_TEMPORAL%>" />
			<input type="submit" value="validar" />
		</form>
	<% } %>
</div>	
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
		<form method="post" action="<%=request.getRequestURI()%>" id="formularioCrear">
			<input type="hidden" name="<%=ControladorUsuarioAutoregistrado.PARAM_ACCION%>" id="accionFormulario" value="<%=ControladorUsuarioAutoregistrado.ACCION_CREAR%>" />
			Tipo Documento 
			<select name="<%=ControladorUsuarioAutoregistrado.PARAM_TIPO_DOCUMENTO%>">
				<option value="NIF">(NIF) N&uacute;mero de Identificaci&oacute;n Fiscal</option>
				<option value="PAS">(PAS) Pasaporte</option>
				<option value="NIE">(NIF) N&uacute;mero de Identificaci&oacute;n de Extranjero</option>
			</select> <br/>
			N&uacute;mero documento <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_DOCUMENTO%>"/> <br/>
			Nombre <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_NOMBRE%>"/> <br/>
			Primer apellido <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_APELLIDO1%>"/> <br/>
			Segundo apellido <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_APELLIDO2%>"/> <br/>
			Email <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO%>"/> <br/>
			Repetir email <input type="text" name="<%=ControladorUsuarioAutoregistrado.PARAM_CORREO_REPETIDO%>"/> <br/>
			<input type="submit" value="Crear" />
		</form>
	<% } %>
</div>	
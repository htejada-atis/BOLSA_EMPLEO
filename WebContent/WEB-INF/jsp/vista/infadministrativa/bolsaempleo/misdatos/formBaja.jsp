<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class="bolsa-empleo usuarios-buscar">

	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
	<h2>Dar de baja Usuario</h2>
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
   		<input type="hidden" name="<%= ControladorMisDatos.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorMisDatos.PARAM_ID%>" id="usuario_id" value="" />
    	<div class="form-group">
    		<label for="razon_exclusion">Razón borrado</label>
    		<textarea class="params" id="razon_exclusion" name="<%= ControladorMisDatos.PARAM_RAZON_BORRADO %>" rows="3" cols="50"></textarea>
    	</div>
    	<div class="form-btn">
   			<input id="usuario_borrar" type="submit" name="<%= ControladorMisDatos.ACCION_BAJA_USUARIO %>" value="Dar de baja"/>
    	</div>
    </form>
    
</div>

<script>

	function bajaUsuario(event, submit_input) {
		event.preventDefault();

		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorMisDatos.ACCION_BAJA_USUARIO %>';
		input_id = document.getElementById("usuario_id");
		input_id.value = '<%= bean.getUsuario().getCodNum() %>';

		submit_input.form.submit();
	}

	document.getElementById("usuario_borrar").addEventListener("click", function(event) {
		bajaUsuario(event, this);
	});

</script>
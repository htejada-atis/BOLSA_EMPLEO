<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML"%>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());
ApartadoBaremacion apartado = bean.getApartadoBaremacion(); 
%>


<div class="bolsa-empleo">
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
	
	<h2><%= apartado != null ? "Editar bloque" : "Nuevo bloque" %></h2>
	
	<form id="actualizar_apartado" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		<input type="hidden" 
			   name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" 
			   id="accion_formulario" 
			   value="<%= apartado != null ? 
					   ControladorItemsBaremacion.ACCION_EDITAR_APARTADO_CONFIRM : ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO_CONFIRM %>" /> 
		<input type="hidden" 
			   name="<%= ControladorItemsBaremacion.PARAM_APARTADO %>" 
			   id="apartado_id" 
			   value="<%= apartado != null ? apartado.getCodNum() : "" %>" />
			
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="apartado_codigo">Código</label> 
				<input type="text" class="form-input-custom" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_CODIGO %>" id="apartado_codigo" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_CODIGO, apartado != null ? apartado.getCodigo() : bean.getUltimoCodigo()) %>" />
			</div>
			<div class="form-group">
				<label for="apartado_nombre">Nombre</label>
				<input type="text" class="form-input-custom" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE %>" id="apartado_nombre" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE, apartado != null ? apartado.getNombre() : "") %>" />
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="apartado_puntuacionmaxima">Puntuación máxima del bloque</label> 
				<input type="text" class="form-input-custom" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_PUNTUACIONMAXIMA %>" id="apartado_puntuacionmaxima"
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_PUNTUACIONMAXIMA, apartado != null && apartado.getPuntuacionMaxima() != null ? apartado.getPuntuacionMaxima().toString() : "") %>"/>
			</div>
			<div class="form-group">
				<label for="apartado_porcentajemaximo">Porcentaje máximo del bloque</label> 
				<input type="text" class="form-input-custom" name="<%=ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO%>" id="apartado_porcentajemaximo" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO, apartado != null && apartado.getPorcentajeMaximo() != null ? apartado.getPorcentajeMaximo().toString() : "") %>"/>
			</div>
		</div>
		<div class="form-btn">
			<input id="apartado_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>"
				value="<%= apartado != null ? "Guardar cambios" : "Insertar apartado" %>" />
		</div>
	</form>
</div>

<script>

</script>


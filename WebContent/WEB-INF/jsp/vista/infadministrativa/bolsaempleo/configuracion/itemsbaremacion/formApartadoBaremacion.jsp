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
String codigo = apartado.getCodigo();
String nombre = "";
%>


<div class="bolsa-empleo">

	<%
	if (bean.getMensajesDeError().size() > 0) {
		out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
	}

	if (apartado.getCodNum() != null) {
		nombre = apartado.getNombre();
		out.print("<h2>Editar bloque</h2>");
	} else {
		out.print("<h2>Nuevo bloque</h2>");
	}
	%>


	<form id="actualizar_apartado" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_ACCION%>" id="accion_formulario" value="" /> 
		<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_APARTADO%>" id="apartado_id" value="" />
			
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="apartado_codigo">Código</label> 
				<input type="text"
					class="form-input-custom"
					name="<%=ControladorItemsBaremacion.PARAM_APARTADO_CODIGO%>"
					id="apartado_codigo" value="<%=codigo%>" />
			</div>
			<div class="form-group">
				<label for="apartado_nombre">Nombre</label> 
				<input type="text"
					class="form-input-custom"
					name="<%=ControladorItemsBaremacion.PARAM_APARTADO_NOMBRE%>"
					id="apartado_nombre" value="<%=nombre%>" />
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="apartado_puntuacionmaxima">Puntuación máxima del bloque</label> 
				<input type="text" class="form-input-custom"
					name="<%=ControladorItemsBaremacion.PARAM_APARTADO_PUNTUACIONMAXIMA%>"
					id="apartado_puntuacionmaxima"
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_PUNTUACIONMAXIMA, apartado.getCodNum() != null && apartado.getPuntuacionMaxima() != null ? apartado.getPuntuacionMaxima().toString() : "") %>"/>
			</div>
			<div class="form-group">
				<label for="apartado_porcentajemaximo">Porcentaje máximo del bloque</label> 
				<input
					type="text" class="form-input-custom"
					name="<%=ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO%>"
					id="apartado_porcentajemaximo" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_APARTADO_PORCENTAJEMAXIMO, apartado.getCodNum() != null && apartado.getPorcentajeMaximo() != null ? apartado.getPorcentajeMaximo().toString() : "") %>"/>
			</div>
		</div>

		<div class="form-btn">
			<input id="apartado_enviar" type="submit"
				name="<%=ControladorItemsBaremacion.PARAM_ENVIAR%>"
				value="<%=bean.getApartadoBaremacion().getCodNum() != null ? "Guardar cambios" : "Insertar apartado"%>" />
		</div>
	</form>

</div>

<script>

	function enviarApartadoBaremacion(event, submit_input) {
		event.preventDefault();
		
		<%if (bean.getApartadoBaremacion().getCodNum() != null) {%>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%=ControladorItemsBaremacion.ACCION_EDITAR_APARTADO%>';
			input_id = document.getElementById("apartado_id");
			input_id.value = '<%=bean.getApartadoBaremacion().getCodNum()%>';
		<%} else {%>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%=ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO%>';
		<%}%>
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		document.getElementById("apartado_enviar").addEventListener("click", function(event) {
			enviarApartadoBaremacion(event, this);
		});
	});
</script>
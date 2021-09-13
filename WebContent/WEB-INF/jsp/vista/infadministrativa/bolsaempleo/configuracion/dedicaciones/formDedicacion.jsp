<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorDedicaciones" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloDedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaDedicacion bean = (VistaDedicacion) uvdatos.getVistas().get(VistaDedicacion.class.getName());
Dedicacion dedicacion = bean.getDedicacion();

String texto = BolsaEmpleoUtils.getParamForm(request, ControladorDedicaciones.PARAM_TEXTO_DEDICACION, dedicacion != null ? dedicacion.getTexto() : "");
String sueldo = BolsaEmpleoUtils.getParamForm(request, ControladorDedicaciones.PARAM_SUELDO, dedicacion != null ? dedicacion.getSueldo().toString() : "");
String tipo = BolsaEmpleoUtils.getParamForm(request, ControladorDedicaciones.PARAM_TIPO, dedicacion != null ? dedicacion.getTipo() : ModeloDedicacion.TIPO_TIEMPO_COMPLETO);
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	if (dedicacion != null) { %>
		<h2>Editar dedicación</h2>
<%	} else { %>
		<h2>Nueva dedicación</h2>
<%	} %>

	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_dedicacion" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		<input type="hidden" name="<%=ControladorDedicaciones.PARAM_ACCION%>" id="accion_formulario"
				value="<%=dedicacion != null ? ControladorDedicaciones.ACCION_EDITAR_DEDICACION: ControladorDedicaciones.ACCION_NUEVA_DEDICACION%>" />
		<input type="hidden" name="<%=ControladorDedicaciones.PARAM_DEDICACION%>" id="dedicacion_id" 
				value="<%=dedicacion != null ? dedicacion.getCodNum() : ""%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="dedicacion_texto" class="bold-label">Texto:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorDedicaciones.PARAM_TEXTO_DEDICACION%>" id="dedicacion_texto" value="<%=texto%>" required/>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Tipo:</label>
				<select id="plaza_centro_destino" name="<%=ControladorDedicaciones.PARAM_TIPO%>" style="width:100%;" required>
				<%	for (Entry<String, String> tip: ModeloDedicacion.TIPOS_DEDICACION.entrySet()) { %>
						<option value="<%= tip.getKey() %>" <%= tipo != null && tipo.equals(tip.getKey()) ? "selected" : ""%>><%= tip.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="dedicacion_sueldo" class="bold-label">Sueldo:</label>
				<input class="form-input-custom" type="number" name="<%=ControladorDedicaciones.PARAM_SUELDO%>" id="dedicacion_sueldo" 
						value="<%=sueldo%>" min="0" step="any" required/>
			</div>
		</div>
		<div class="form-btn">
			<input id="dedicacion_enviar" type="submit" name="<%=ControladorDedicaciones.PARAM_ENVIAR%>" value="<%=dedicacion != null ? "Guardar cambios" : "Insertar dedicacion"%>"/>
		</div>
	</form>
	
</div>

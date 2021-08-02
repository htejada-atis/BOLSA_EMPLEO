<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
PlazaOfertada plaza = bean.getPlazaOfertada();

String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, plaza != null ? plaza.getJustificacion() : "");
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, plaza != null ? plaza.getDuracionPrevista() : "");
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	if (plaza != null) { %>
		<h2>Editar plaza ofertada</h2>
<%	} else { %>
		<h2>Nueva plaza</h2>
<%	} %>

	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_dedicacion" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=plaza != null ? ControladorContratacion.ACCION_EDITAR_PLAZA_OFERTADA: ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA%>" />
		<input type="hidden" name="<%=ControladorContratacion.PARAM_PLAZA_OFERTADA%>" id="plaza_ofertada_id" 
				value="<%=plaza != null ? plaza.getCodNum() : ""%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area">Área: </label>
				<select class="params" id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;">
					<option value="">----------------</option>
				<%	for(Area area: bean.getListaAreas()) { %>
						<option value="<%=area.getCodNum()%>" <%= plaza != null && plaza.getArea().getCodNum().equals(area.getCodNum()) ? "selected" : "" %>><%= area.getIdAreaExterno() + " " + area.getDescripcion() %></option>
				<% } %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_area">Dedicación: </label>
				<select class="params" id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;">
					<option value="">----------------</option>
				<%	for(Dedicacion dedicacion: bean.getListaDedicaciones()) { %>
						<option value="<%=dedicacion.getCodNum()%>"><%= dedicacion.getTexto() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_ofertada_justificacion" class="bold-label">Justificación:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" id="plaza_ofertada_justificacion" value="<%=justificacion%>" required/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="dedicacion_sueldo" class="bold-label">Duración prevista:</label>
				<input class="form-input-custom" type="number" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_ofertada_duracion_prevista" value="<%=duracionPrevista%>" required/>
			</div>
		</div>
		<div class="form-btn">
			<input id="dedicacion_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="<%=plaza != null ? "Guardar cambios" : "Insertar plaza"%>"/>
		</div>
	</form>
	
</div>
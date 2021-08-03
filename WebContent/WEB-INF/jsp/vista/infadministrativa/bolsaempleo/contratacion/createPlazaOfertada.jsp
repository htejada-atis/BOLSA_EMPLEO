<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Map.Entry" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
PlazaOfertada plaza = bean.getPlazaOfertada();

String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, plaza != null ? EscapaHTML.escapa(plaza.getJustificacion()) : "");
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, plaza != null ? EscapaHTML.escapa(plaza.getDuracionPrevista()) : "");
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Nueva plaza</h2>

	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_dedicacion" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA%>" />
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área: </label>
				<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;">
					<option value="">----------------</option>
				<%	for(Area area: bean.getListaAreas()) { %>
						<option value="<%=area.getCodNum()%>" <%= plaza != null && plaza.getArea().getCodNum().equals(area.getCodNum()) ? "selected" : "" %>><%= area.getIdAreaExterno() + " " + area.getDescripcion() %></option>
				<% } %>
				</select>
			</div>
			
		<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
				<div class="form-group">
					<label for="plaza_area">Dedicación: </label>
					<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;">
						<option value="">----------------</option>
					<%	for(Dedicacion dedicacion: bean.getListaDedicaciones()) { %>
							<option value="<%=dedicacion.getCodNum()%>"><%= dedicacion.getTexto() %></option>
					<%	} %>
					</select>
				</div>
		<%	} %>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_ofertada_justificacion" class="bold-label">Justificación:</label>
				<textarea class="form-input-custom" id="plaza_ofertada_justificacion" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" rows="3" cols="60" required><%=justificacion%></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
		<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
				<div class="form-group">
					<label for="plaza_cuatrimestre">Cuatrimestre:</label>
					<select id="plaza_cuatrimestre" name="<%=ControladorContratacion.PARAM_CUATRIMESTRE%>" style="width:100%;">
					<%	for (Entry<String, String> cuatrimestre: ModeloPlazaOfertada.CUATRIMESTRES.entrySet()) { %>
							<option value="<%= cuatrimestre.getKey() %>"><%= cuatrimestre.getValue() %></option>
					<%	} %>
					</select>
				</div>
		<%	} %>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<select id="plaza_centro_destino" name="<%=ControladorContratacion.PARAM_CENTRO_DESTINO%>" style="width:100%;">
				<%	for (Entry<String, String> cuatrimestre: ModeloPlazaOfertada.CENTROS_DESTINO.entrySet()) { %>
						<option value="<%= cuatrimestre.getKey() %>"><%= cuatrimestre.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="dedicacion_sueldo">Duración prevista:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_ofertada_duracion_prevista" value="<%=duracionPrevista%>" required/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
				<input id="dedicacion_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="<%=plaza != null ? "Guardar cambios" : "Insertar plaza"%>" style="float:right;"/>
			</div>
		</div>
	</form>
	
</div>

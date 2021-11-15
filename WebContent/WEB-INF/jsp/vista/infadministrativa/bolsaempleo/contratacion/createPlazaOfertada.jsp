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

String area = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_AREA, "0");
String centroDestino = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CENTRO_DESTINO, "");
String codigo = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CODIGO, "");
String cuatrimestre = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CUATRIMESTRE, "");
String curso = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_CURSO, BolsaEmpleoUtils.getCurrentCourse());
String dedicacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DEDICACION, "0");
String duracionPrevista = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_DURACION_PREVISTA, "");
String fechaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_FECHA_FIN_OFERTA, "");
String horaFinOferta = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_HORA_FIN_OFERTA, "");
String justificacion = BolsaEmpleoUtils.getParamForm(request, ControladorContratacion.PARAM_JUSTIFICACION, "");
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Nueva plaza</h2>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_plaza" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
		<input type="hidden" name="<%=ControladorContratacion.PARAM_ACCION%>" id="accion_formulario"
				value="<%=ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA%>" />
	<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
			<div class="form-group-container col2">
				<div class="form-group">
					<label for="plaza_codigo">Código:</label>
					<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_CODIGO%>" id="plaza_codigo" value="<%=codigo%>"/>
				</div>
			</div>
	<%	} %>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área:</label>
				<select id="plaza_area" name="<%=ControladorContratacion.PARAM_AREA%>" style="width:100%;" required>
					<option value="">----------------</option>
				<%	for(Area are: bean.getListaAreas()) { %>
						<option value="<%=are.getCodNum()%>" <%= Integer.parseInt(area) == are.getCodNum() ? "selected" : "" %>><%= are.getIdAreaExterno() + " " + are.getDescripcion() %></option>
				<% } %>
				</select>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<select id="plaza_centro_destino" name="<%=ControladorContratacion.PARAM_CENTRO_DESTINO%>" style="width:100%;" required>
				<%	for (Entry<String, String> cen: ModeloPlazaOfertada.CENTROS_DESTINO.entrySet()) { %>
						<option value="<%= cen.getKey() %>" <%= centroDestino != null && centroDestino.equals(cen.getKey()) ? "selected" : ""%>><%= cen.getValue() %></option>
				<%	} %>
				</select>
			</div>
		</div>
	<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
			<div class="form-group-container col2">
				<div class="form-group">
					<label for="plaza_dedicacion">Dedicación: </label>
					<select id="plaza_dedicacion" name="<%=ControladorContratacion.PARAM_DEDICACION%>" style="width:100%;">
						<option value="">----------------</option>
					<%	for(Dedicacion ded: bean.getListaDedicaciones()) { %>
							<option value="<%=ded.getCodNum()%>" <%= Integer.parseInt(dedicacion) == ded.getCodNum() ? "selected" : "" %>><%= ded.getTexto() %></option>
					<%	} %>
					</select>
				</div>
				<div class="form-group">
					<label for="plaza_cuatrimestre">Cuatrimestre:</label>
					<select id="plaza_cuatrimestre" name="<%=ControladorContratacion.PARAM_CUATRIMESTRE%>" style="width:100%;">
						<option value="">----------------</option>
					<%	for (Entry<String, String> cua: ModeloPlazaOfertada.CUATRIMESTRES.entrySet()) { %>
							<option value="<%= cua.getKey() %>" <%= cuatrimestre != null && cuatrimestre.equals(cua.getKey()) ? "selected" : "" %>><%= cua.getValue() %></option>
					<%	} %>
					</select>
				</div>
			</div>
	<%	} %>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_justificacion" class="bold-label">Justificación:</label>
				<textarea class="form-input-custom" id="plaza_justificacion" name="<%=ControladorContratacion.PARAM_JUSTIFICACION %>" rows="3" cols="60" required><%=justificacion%></textarea>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_curso" class="bold-label">Curso académico:</label>
				<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_CURSO%>" id="plaza_curso" value="<%=curso%>" required 
					pattern="<%= ModeloPlazaOfertada.FORMATO_CURSO %>"/>
			</div>
		<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
					<div class="form-group">
						<label for="plaza_duracion_prevista">Duración prevista:</label>
						<input class="form-input-custom" type="text" name="<%=ControladorContratacion.PARAM_DURACION_PREVISTA%>" id="plaza_duracion_prevista" value="<%=duracionPrevista%>"/>
					</div>
				</div>
				<div class="form-group-container col2">
					<div class="form-group">
						<label for="plaza_fecha_fin_oferta">Fecha fin oferta:</label>
						<input class="form-input-custom" name="<%=ControladorContratacion.PARAM_FECHA_FIN_OFERTA%>" id="plaza_fecha_fin_oferta" type="text" autocomplete="off" value="<%= fechaFinOferta %>"/>
					</div>
					<div class="form-group">
						<label for="plaza_hora_fin_oferta">Hora fin oferta:</label>
						<input class="form-input-custom" name="<%=ControladorContratacion.PARAM_HORA_FIN_OFERTA%>" id="plaza_hora_fin_oferta" type="text" autocomplete="off" 
								value="<%= horaFinOferta %>" placeholder="HH:MM:SS"/>
					</div>
				</div>
		<%	} else { %>
				</div>
		<%	} %>
		<div class="form-group-container col2">
			<div class="form-file">
				<label for="plaza_horario" style="margin-bottom: .5rem;">Horario:</label>
				<input id="plaza_horario" type="file" name="<%= ControladorContratacion.PARAM_HORARIO %>"/>
			</div>
	<%	if (bean.getUsuarioLogeado().isServicioPersonal()) { %>
			<div class="form-file">
				<label for="plaza_nri" style="margin-bottom: .5rem;">NRI:</label>
				<input id="plaza_nri" type="file" name="<%= ControladorContratacion.PARAM_NRI %>"/>
			</div>
	<%	} %>
		</div>
		
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
				<input id="plaza_enviar" type="submit" name="<%=ControladorContratacion.PARAM_ENVIAR%>" value="Insertar plaza" style="float:right;"/>
			</div>
		</div>
	</form>
	
</div>

<script>
	
	$(document).ready(function() {
		$("#plaza_fecha_fin_oferta").datepicker();
		
		$('#actualizar_plaza').submit(function(event) {
			$('#plaza_enviar').prop('disabled', true);
			$('#plaza_enviar').attr('value', 'Creando plaza...');
			return true;
		});
	});
	
</script>

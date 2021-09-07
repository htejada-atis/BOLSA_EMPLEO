<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorPlazasOfertadas" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlazasOfertadas" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="java.util.Date" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlazasOfertadas bean = (VistaPlazasOfertadas) uvdatos.getVistas().get(VistaPlazasOfertadas.class.getName());
OfertaCandidato oferta = bean.getOfertaCandidato();
boolean pendiente = bean.getOfertaCandidato().getContratacion().getResultado().equals(ModeloContratacion.RESULTADO_PENDIENTE);
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Confirmación contrato de la plaza<%= !pendiente ? " - " + bean.getOfertaCandidato().getContratacion().getResultado() : "" %></h2>
	
	<h4>Id: <%= oferta.getPlaza().getCodNum() %><br/>Estado: <%= oferta.getPlaza().getEstado() %><br/><%= oferta.getPlaza().getFechaAbierta() != null ? "Fecha abierta: " + Formateador.formatoFecha(oferta.getPlaza().getFechaAbierta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %><br/>
	Área: <%= oferta.getPlaza().getArea().getDescripcion() %>
	</h4>
	
	<p>En el caso de que la cita propuesta no le venga bien envíe un email para concretar una distinta.</p>
	
	<form id="plaza_contrato" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_area" class="bold-label">Área:</label>
				<input class="form-input-custom" id="plaza_area" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getArea().getIdAreaExterno() + " " + oferta.getPlaza().getArea().getDescripcion()) %>"
						readonly disabled/>
			</div>
			<div class="form-group">
				<label for="plaza_centro_destino" class="bold-label">Centro destino:</label>
				<input class="form-input-custom" id="plaza_centro_destino" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getCentroDestino()) %>"
						readonly disabled/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_cuatrimestre" class="bold-label">Cuatrimestre: </label>
				<input class="form-input-custom" id="plaza_cuatrimestre" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getCuatrimestre()) %>"
						readonly disabled/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_dedicacion" class="bold-label">Dedicación:</label>
				<input class="form-input-custom" id="plaza_dedicacion" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getDedicacion().getTexto()) %>"
						readonly disabled/>
			</div>
			<div class="form-group">
				<label for="plaza_dedicacion" class="bold-label">Sueldo:</label>
				<input class="form-input-custom" id="plaza_dedicacion" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getDedicacion().getSueldo().toString()) %>"
						readonly disabled/>
			</div>
		</div>
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="plaza_justificacion" class="bold-label">Justificación:</label>
				<textarea class="form-input-custom" id="plaza_justificacion" rows="3"
						readonly disabled><%= EscapaHTML.escapa(oferta.getPlaza().getJustificacion()) %></textarea>
			</div>
		</div>
	
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_duracion_prevista" class="bold-label">Duración prevista:</label>
				<input class="form-input-custom" id="plaza_duracion_prevista" type="text" value="<%= EscapaHTML.escapa(oferta.getPlaza().getDuracionPrevista()) %>"
						readonly disabled/>
			</div>
			<div class="form-group">
				<label for="plaza_fecha_fin_oferta" class="bold-label">Fecha fin oferta:</label>
				<input class="form-input-custom" id="plaza_fecha_fin_oferta" type="text" value="<%= oferta.getPlaza().getFechaFinOferta() != null ? Formateador.formatoFecha(oferta.getPlaza().getFechaFinOferta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %>"
						readonly disabled/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="plaza_fecha_cita" class="bold-label">Fecha cita:</label>
				<input class="form-input-custom" id="plaza_fecha_cita" type="text" value="<%= oferta.getContratacion().getFechaCita() != null ? Formateador.formatoFecha(oferta.getContratacion().getFechaCita(), Formateador.FORMATO_FECHA_DDMMYYYY) : "" %>"
						readonly disabled/>
			</div>
			<div class="form-group">
				<label for="plaza_hora_cita" class="bold-label">Hora cita:</label>
				<input class="form-input-custom" id="plaza_hora_cita" type="text" value="<%= oferta.getContratacion().getFechaCita() != null ? Formateador.formatoFecha(oferta.getContratacion().getFechaCita(), Formateador.FORMATO_FECHA_HORA_MINUTOS) : "" %>"
						readonly disabled/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group"></div>
			<div class="form-group">
		<%	if (pendiente) { %>
					<button id="cita_rechazar" style="float:right; margin-left: 12px;">Rechazar cita</button>
					<button id="cita_aceptar" style="float:right; margin-left: 12px;">Aceptar cita</button>
		<%	} %>
			</div>
		</div>
	</form>
	
</div>

<script>

$(document).ready(function() {
	
<%	if (pendiente) { %>
	
		document.getElementById("cita_aceptar").addEventListener("click", function() {
			event.preventDefault();
			Atis.confirmDialog("Aceptar cita contrato", "Al aceptar confirma que se presentará el día y hora indicados en la cita de contratación.", {
					'Si': function(row) {
						var params = {
								'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_ACEPTAR_CONTRATACION %>',
								'<%= ControladorPlazasOfertadas.PARAM_PLAZA_OFERTADA %>': <%= oferta.getPlaza().getCodNum() %>
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
			});
		});
		
		document.getElementById("cita_rechazar").addEventListener("click", function() {
			event.preventDefault();
			Atis.confirmDialog("Rechazar cita contrato", "Confirma que rechaza la plaza.", {
					'Si': function(row) {
						var params = {
								'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_RECHAZAR_CONTRATACION %>',
								'<%= ControladorPlazasOfertadas.PARAM_PLAZA_OFERTADA %>': <%= oferta.getPlaza().getCodNum() %>
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
			});
		});
	
<%	} %>
	
});

</script>
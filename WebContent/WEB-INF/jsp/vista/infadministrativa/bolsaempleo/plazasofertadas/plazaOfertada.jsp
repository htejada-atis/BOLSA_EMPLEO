<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
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

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaPlazasOfertadas bean = (VistaPlazasOfertadas) uvdatos.getVistas().get(VistaPlazasOfertadas.class.getName());
OfertaCandidato oferta = bean.getOfertaCandidato();
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Plaza ofertada<%= oferta.isResultado() == null ? "" : oferta.isResultado() ? " - Aceptada" : " - Rechazada" %></h2>
	
	<h4>Id: <%= oferta.getPlaza().getCodNum() %><br/><%= oferta.getPlaza().getFechaAbierta() != null ? "Fecha abierta: " + Formateador.formatoFecha(oferta.getPlaza().getFechaAbierta(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %></h4>
	
	<form id="plaza_ofertada" class="be-form" method="post" action="<%=request.getRequestURI()%>">
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
		<%	if (oferta.getPlaza().getHorario() != null) { %>
			<div class="form-group-container col2">
				<div class="form-file">
					<label for="plaza_horario" style="margin-bottom: .5rem;" class="bold-label">Horario:</label>
					<button id="plaza_descargar_horario" class="btn icon icon-download" title="Descargar horario de la plaza" type="button"
						style="margin-top: .5rem;padding: 1px 6px;">Descargar horario</button>
				</div>
			</div>
		<%	} %>
		<div class="form-group-container col2">
			<div class="form-group">
			<%	if (oferta.isResultado() != null) { %>
					<br/>
					<button id="plaza_volver" style="float:left;">Volver</button>
			<%	} %>
			</div>
			<div class="form-group">
			<%	if (oferta.isResultado() == null) { %>
					<button id="plaza_rechazar" style="float:right; margin-left: 12px;">Rechazar oferta</button>
					<button id="plaza_aceptar" style="float:right; margin-left: 12px;">Aceptar oferta</button>
			<%	} %>
			</div>
		</div>
	</form>
	
</div>

<script>

$(document).ready(function() {
	
<%	if (oferta.getPlaza().getHorario() != null) { %>
		document.getElementById("plaza_descargar_horario").addEventListener("click", function() {
			window.open("<%=ControladorDescargaFicheros.URL_DESCARGA_FICHEROS%>"
					+ "<%="?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_HORARIO_CANDIDATO + "&" + ControladorDescargaFicheros.PARAM_PLAZA_OFERTADA + "=" + oferta.getPlaza().getCodNum()%>");
		});
<%	} %>

<%	if (oferta.isResultado() == null) { %>
		document.getElementById("plaza_aceptar").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.confirmDialog("Aceptar plaza", "Al aceptar confirma que está interesado y entrará en el proceso de selección para la plaza.", {
					'Si': function(row) {
						var params = {
								'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_ACEPTAR_PLAZA %>',
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
		
		document.getElementById("plaza_rechazar").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.confirmDialog("Rechazar plaza", "Al rechazar confirma que no está interesado por lo que no podrá optar a la plaza.", {
					'Si': function(row) {
						var params = {
								'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_RECHAZAR_PLAZA %>',
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
<%	} else { %>
		document.getElementById("plaza_volver").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'<%= ControladorPlazasOfertadas.PARAM_ACCION %>': '<%= ControladorPlazasOfertadas.ACCION_INDEX %>'});
		});
<%	} %>
	
});

</script>
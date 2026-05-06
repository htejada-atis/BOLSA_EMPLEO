<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionMerito"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisAlegaciones beanAlegaciones = (VistaMisAlegaciones) uvdatos.getVistas().get(VistaMisAlegaciones.class.getName());
VistaMisResultados beanResultados = beanAlegaciones.getBeanResultados();
Alegacion alegacion = beanAlegaciones.getAlegacion();
Convocatoria convocatoria = beanAlegaciones.getConvocatoria();
Bolsa bolsa = beanResultados.getBolsa();
BolsaResultado bolsaResultado = beanResultados.getBolsaResultado();
SolMerBolAlegacion solMerBolAlegacion = beanResultados.getSolMerBolAlegacion();
boolean puedeEditar = ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado());
%>

<div class='bolsa-empleo mis-alegaciones'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Resumen de la Alegación</h2>
	<h3>Alegación: <%= EscapaHTML.escapa(convocatoria.getDescripcion()) %></h3>
	<h4>Área: <%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h4>

	<p>Revise los detalles de su alegación. Una vez confirmada no podrá ser editada.</p>

	<h3>Méritos con Alegación</h3>

	<%
		boolean tieneMeritosAlegados = false;
		List<MeritoResultado> todosLosMeritos = new java.util.ArrayList<>();

		if (bolsaResultado.getListaMeritos() != null) {
			todosLosMeritos.addAll(bolsaResultado.getListaMeritos());
		}
		if (bolsaResultado.getListaMeritosExcluidos() != null) {
			todosLosMeritos.addAll(bolsaResultado.getListaMeritosExcluidos());
		}
		if (bolsaResultado.getListaMeritosNoEvaluados() != null) {
			todosLosMeritos.addAll(bolsaResultado.getListaMeritosNoEvaluados());
		}
	%>

	<% if (todosLosMeritos.size() > 0) { %>
		<table class="bluetable bolsaempleo">
			<tr>
				<th scope="col" style="width:15%">Cod. Mérito</th>
				<th scope="col" style="width:40%">Mérito</th>
				<th scope="col" style="width:45%">Descripción de la Alegación / Ficheros</th>
			</tr>
			<tbody>
				<% for (MeritoResultado merito : todosLosMeritos) { %>
					<% if (merito.tieneAlegacion()) { %>
						<% tieneMeritosAlegados = true; %>
						<tr>
							<td><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
							<td><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
							<td>
								<div style="margin-bottom: 10px;">
									<strong>Descripción:</strong><br/>
									<p><%= EscapaHTML.escapa(merito.getDescripcionAlegacion() != null ? merito.getDescripcionAlegacion() : "Sin descripción") %></p>
								</div>
								<% if (merito.getArchivosAlegacion() != null && merito.getArchivosAlegacion().size() > 0) { %>
									<strong>Ficheros adjuntos:</strong><br/>
									<ul style="margin: 5px 0;">
										<% for (ArchivoAlegacionMerito archivo : merito.getArchivosAlegacion()) { %>
											<li><%= EscapaHTML.escapa(archivo.getNombre()) %></li>
										<% } %>
									</ul>
								<% } else { %>
									<p style="color: #999;">Sin ficheros adjuntos</p>
								<% } %>
							</td>
						</tr>
					<% } %>
				<% } %>

				<% if (!tieneMeritosAlegados) { %>
					<tr>
						<td colspan="3" style="text-align: center; color: #999;">No hay méritos con alegaciones</td>
					</tr>
				<% } %>
			</tbody>
		</table>
	<% } else { %>
		<p style="color: #999;">No hay méritos disponibles</p>
	<% } %>

	<h3>Alegación General</h3>

	<% if (solMerBolAlegacion != null && solMerBolAlegacion.getDescripcion() != null && !solMerBolAlegacion.getDescripcion().isEmpty()) { %>
		<div style="margin-bottom: 20px;">
			<strong>Descripción de la Alegación General:</strong>
			<p><%= EscapaHTML.escapa(solMerBolAlegacion.getDescripcion()) %></p>
		</div>

		<% if (solMerBolAlegacion.getArchivosAlegacion() != null && solMerBolAlegacion.getArchivosAlegacion().size() > 0) { %>
			<div>
				<strong>Ficheros adjuntos a la alegación general:</strong>
				<ul style="margin: 10px 0;">
					<% for (ArchivoAlegacionGeneral archivo : solMerBolAlegacion.getArchivosAlegacion()) { %>
						<li><%= EscapaHTML.escapa(archivo.getNombre()) %></li>
					<% } %>
				</ul>
			</div>
		<% } else { %>
			<p style="color: #999;">Sin ficheros adjuntos</p>
		<% } %>
	<% } else { %>
		<p style="color: #999;">Sin alegación general</p>
	<% } %>

	<% if (puedeEditar) { %>
        <div class="form-check" style="margin-top: 2rem; margin-bottom: 2rem;">
            <label for="confirmar_datos" class="bold-label">
                <input type="checkbox" id="confirmar_datos" name="confirmar_datos">
                Confirmo que los datos introducidos son correctos
            </label>
        </div>
	<% } %>

	<div class="btns-by-steps">
		<button class="link-btn" id="resumen_volver">
			Volver
		</button>
		<% if (puedeEditar) { %>
            <button class="link-btn" id="resumen_confirmar" title="Debe marcar el check de confirmar datos">
                Confirmar Alegación
            </button>
		<% } %>
	</div>

</div>

<script>
$(document).ready(function() {
	var backBtn = document.getElementById("resumen_volver");

	var handleBackEvent = function(event) {
		event.preventDefault();
		var params = {
				'<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_VER_DETALLE_ALEGACION %>',
				'<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
		};
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	};

	backBtn.addEventListener("click", handleBackEvent);

	<% if (puedeEditar) { %>
	var confirmBtn = document.getElementById("resumen_confirmar");

	function confirmRequest(message) {
		Atis.confirmDialog(
			"Confirmar alegación", message, {
	        	'Sí': function() {
	        		backBtn.style.pointerEvents = "none";
	        		confirmBtn.style.pointerEvents = "none";
	        		backBtn.disabled = true;
	        		confirmBtn.disabled = true;
	        		confirmBtn.innerHTML = "Confirmando alegación...";

	            	var params = {
            			'<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_CONFIRMAR_ALEGACION %>',
            			'<%= ControladorMisAlegaciones.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
            			'<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': '<%= convocatoria.getCodNum() %>',
            			'<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
           			};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);

	              	$(this).dialog("close");
	            },
	            'No': function() {
	              	$(this).dialog("close");
	            }
	          });
	}

	var handleConfirmEvent = function(event) {
		event.preventDefault();

		var message = "¿Desea confirmar la alegación?<br/><br/>" +
			"<strong>IMPORTANTE:</strong> Una vez confirmada, no podrá modificarla.<br/>" +
			"La alegación será enviada al departamento para su resolución.<br/><br/>" +
			"¿Está seguro de que desea continuar?";

		confirmRequest(message);
	};

	confirmBtn.addEventListener("click", handleConfirmEvent);

	confirmBtn.disabled = true;

	document.getElementById("confirmar_datos").addEventListener('change', function() {
		confirmBtn.title = this.checked ? "" : "Debe marcar el check de confirmar datos";
		confirmBtn.disabled = !this.checked;
	});
	<% } %>
});
</script>

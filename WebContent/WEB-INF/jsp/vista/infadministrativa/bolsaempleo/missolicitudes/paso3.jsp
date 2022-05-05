<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<% if (bean.getSolicitud() != null) { %>
	
		<h2>Paso 3: Resumen de solicitud y confirmación</h2>
		<h3><%=bean.getSolicitud().getConvocatoria().getDescripcion()%></h3>
		
		<p>Revise su solicitud para la convocatoria <%=bean.getSolicitud().getConvocatoria().getDescripcion()%>. Una vez confirmada no podrá ser editada</p>
		
		<% for (BolsaSolicitud bolsa: bean.getListaBolsasSolicitud()) { %>
			
			<h4>Área - <%= bolsa.getArea().getDescripcion() %></h4>
			<table class="bluetable bolsaempleo">
				<tr>
					<th scope="col"	style="width:15%">Cod. mérito</th>
					<th scope="col"	style="width:30%">Mérito</th>
					<th scope="col"	style="width:10%">Valor</th>	
					<th scope="col"	style="width:30%">Descripción</th>
					<th scope="col" style="width:20%">Afinidad</th>
				</tr>
				<tbody>
					<% if (bolsa.getListaMeritos() != null && bolsa.getListaMeritos().size() > 0) { %>
						<% for (MeritoSolicitudTable merito: bolsa.getListaMeritos()) { 
								String codigoItem = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getCodigo();
						%>
							<tr>
								<td><%= codigoItem %></td>
								<td><%= merito.getMerito().getItemBaremacion().getNombre() %></td>
								<td><%= merito.getMerito().getValor() %></td>
								<td><%= merito.getMerito().getDescripcion() %></td>
								<td>
								<% if (merito.getCodNum() != null && merito.getMeritoSolicitud() != null && merito.getMerito().getItemBaremacion().getAfinidad() != null) { %>
									<% if (merito.getValoraciones().size() > 0) { %>
										<% if (merito.getMerito().getItemBaremacion().getIndividualizado()) { %>
											<%= "<b>" + merito.getValoraciones().get(0).getAfinidad().getCodigo() + " " + merito.getValoraciones().get(0).getAfinidad().getModulacion() * 100 + "%</b>" %>
										<% } else {
											for (MeritoSolicitudValoracion valoracion: merito.getValoraciones()) { %>
												<%= "<b>" + valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() * 100 + "%</b><br/>" %>
											<% }
										   } %>
									<% } %>
								<% } %>
								</td>
							</tr>
						<% } %>
					<% } else { %>
						<tr><td colspan="5"><%= ControladorMisSolicitudes.MENSAJE_AREA_SIN_MERITOS %></td><tr>
					<% } %>
				</tbody>
			</table>
		<% } %>
		
		<% if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) { %>
			<div class="form-check" style="margin-top: 1.5rem">
	    		<label for="confirmar_datos" class="bold-label"><input type="checkbox" id="confirmar_datos" name="confirmar_datos">
	    		Confirmo que los datos introducidos son correctos</label>
	    	</div>
		
			<div class="btns-by-steps">
				<button class="link-btn" id="paso3_volver">
			    	 Volver
			    </button>
			    <button class="link-btn" id="paso3_confirmar" title="Debe marcar el check de confirmar datos">
			    	 Confirmar Solicitud
			    </button>
			</div>
		<% } %>
	
	<% } %>
		
</div>

<% if (bean.getSolicitud() != null) { %>

	<script>
	
		$(document).ready(function() {
			<% if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) { %>
			
				var backBtn = document.getElementById("paso3_volver");
				var confirmBtn = document.getElementById("paso3_confirmar");
			
				function confirmRequest(message) {
					Atis.confirmDialog(
						"Confirmar solicitud", message, {
		            	'Confirmar solicitud': function(row) {
		            		backBtn.style.pointerEvents = "none";
		            		confirmBtn.style.pointerEvents = "none";
		            		backBtn.removeEventListener("click", handleBackEvent);
		            		confirmBtn.removeEventListener("click", handleConfirmEvent);
		            		confirmBtn.innerHTML = "Generando solicitud...";
		        			
		            		var params = {
	            				'a': '<%= ControladorMisSolicitudes.ACCION_CONFIRMAR_SOLICITUD %>',
	            				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
	           				};
		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
		        			
		              		$(this).dialog("close");
		            	},
		            	'No': function() {
		              		$(this).dialog("close");
		            	}
		          	});
				}
			
				var handleBackEvent = function(event) {
					event.preventDefault();
					var params = {
							'a': '<%= ControladorMisSolicitudes.ACCION_LISTAR_BOLSAS_SOLICITUD %>',
							'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				};
				
				var handleConfirmEvent = function(event) {
					event.preventDefault();
					
					var message = "";
					
					<% if (bean.getListaTitulaciones() != null && bean.getListaTitulaciones().size() > 0) { %>
						message = 
							"¿Desea confirmar la solicitud?<br/><br/>" +
							"Recuerde añadir ahora todas las titulaciones y acreditaciones, luego no podrá añadirlas.<br/><br/>";
						
						message += "Mis titulaciones: <br/>";						
						<% for (TitulacionUsuario tu : bean.getListaTitulaciones()) { %>
							message += "<%= tu.getTitulacion() != null ? tu.getTitulacion().getNombre() : tu.getOtraTitulacion() %><br/>";
						<% } %>
						
						message += "<br/>Mis acreditaciones: <br/>";
						<% for (MeritoPreferenteUsuario m : bean.getListaMeritosPreferentes()) { %>
							message += "<%= m.getMeritoPreferente().getNombre() + " " + (m.getMeritoPreferenteOpcion() != null ? m.getMeritoPreferenteOpcion().getNombre() : "") %><br/>";
						<% } %>
						
					<% } else { %>					
						message = "No ha añadido ninguna titulación.<br/>Al menos debe incluir una titulación universitaria en el apartado 'Mis titulaciones'.<br/>Si no será excluido del proceseo de selección.<br/><br/>";
						message += "¿Desea confirmar la solicitud?<br/><br/>";
						message += "<br/>ATENCIÓN: NO PODRA EDITAR LA SOLICITUD<br/>DESPUES DE CONFIRMAR SU SOLICITUD.<br/><br/>";
						message += "Posteriormente podrá descargar una copia de su solicitud.<br/><br/>";
					<% } %>
					
					confirmRequest(message);
				};
				
				backBtn.addEventListener("click", handleBackEvent);
				confirmBtn.addEventListener("click", handleConfirmEvent);
				
				confirmBtn.disabled = true;
				
				document.getElementById("confirmar_datos").addEventListener('change', function() {
					confirmBtn.title = this.checked ? "" : "Debe marcar el check de confirmar datos";
					confirmBtn.disabled = !this.checked;
				});
				
			<% } %>
		});
	</script>

<% } %>

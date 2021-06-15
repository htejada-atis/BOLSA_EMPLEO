<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
%>

<div class="bolsa-empleo">	
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resumen solicitud - <%= bean.getCandidato().getCodCuenta() %></h2>
	
<%	for (BolsaSolicitud bolsa: bean.getListaBolsasSolicitud()) { %>
		
		<h4>Área - <%= bolsa.getArea().getDescripcion() %></h4>
		<table class="bluetable bolsaempleo" id="table_solicitud">
			<tr>
				<th scope="col"	style="width:15%">Cod. mérito</th>
				<th scope="col"	style="width:30%">Mérito</th>
				<th scope="col"	style="width:10%">Valor</th>
				<th scope="col"	style="width:30%">Descripción</th>
				<th scope="col" style="width:15%">Afinidad</th>
				<th scope="col" style="width:10%"></th>
			</tr>
			<tbody>
			<%	if (bolsa.getListaMeritos() != null && bolsa.getListaMeritos().size() > 0) { %>
				<%	for (MeritoSolicitudTable merito: bolsa.getListaMeritos()) {
						String codigoItem = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getCodigo();
					%>
						<tr>
							<td><%= codigoItem %></td>
							<td><%= merito.getMerito().getItemBaremacion().getNombre() %></td>
							<td><%= merito.getMerito().getValor() %></td>
							<td><%= merito.getMerito().getDescripcion() %></td>
							<td>
						<%	if (merito.getCodNum() != null && merito.getMeritoSolicitud() != null && merito.getMerito().getItemBaremacion().getAfinidad() != null) { %>
							<%	if (merito.getValoraciones().size() > 0) { %>
								<%	if (merito.getMerito().getItemBaremacion().getIndividualizado()) { %>
										<%= "<b>" + merito.getValoraciones().get(0).getAfinidad().getCodigo() + " " + merito.getValoraciones().get(0).getAfinidad().getModulacion() * 100 + "%</b>" %>
								<%	} else {
										for (MeritoSolicitudValoracion valoracion: merito.getValoraciones()) { %>
											<%= "<b>" + valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() * 100 + "%</b><br/>" %>
									<%	}
									} %>
							<%	} %>
						<%	} %>
							</td>
							<td>
								<button class="btn only-icon icon-download fichero-merito"
										title="Descargar fichero del mérito"
										type="button"
										data-merito="<%= merito.getMerito().getCodNum() %>"></button>
							</td>
						</tr>
				<%	} %>
			<%	} else { %>
					<tr><td colspan="6"><%= ControladorMisSolicitudes.MENSAJE_AREA_SIN_MERITOS %></td><tr>
			<%	} %>
			</tbody>
		</table>
<%	} %>

	<div class="btns-by-steps">
		<button class="link-btn" id="candidato_volver">
	    	 Volver
	    </button>
	<% if (bean.getSolicitud().getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA) &&
			bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) { %>
	    <button class="link-btn" id="reabrir_solicitud">
	    	 Reabrir solicitud
	    </button>
	<%	} %>
	</div>
	
</div>

<script>

	$(document).ready(function() {
		
		$('#table_solicitud').on('click', '.fichero-merito', function() {
			var idMerito = $(this).data('merito');
			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + idMerito);
		});
		
		document.getElementById("candidato_volver").addEventListener("click", function() {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_VOLVER_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>'
			});
		});
		
		document.getElementById("reabrir_solicitud").addEventListener("click", function() {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_REABRIR_SOLICITUD%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
				'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>'
			});
		});
		
	});

</script>

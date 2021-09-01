<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialSBM" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialMerito" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialValoracionMerito" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
MeritoSolicitud merito = bean.getMerito();
Bolsa bolsa = bean.getBolsa();
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= descripcion %></h3>
	<h4><%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h4>
	
	<div style="overflow-x:auto;">
		<table class="bluetable bolsaempleo" id="tableHistorialSBM">
			<caption>Historial del mérito en una solicitud para la bolsa</caption>
			<tr>
				<th scope="col" style="width:55px">Fecha</th>
				<th scope="col" style="width:90px">Log</th>
				<th scope="col" style="width:50px">Excluido</th>
				<th scope="col" style="width:50px">Validado</th>
				<th scope="col" style="width:120px">Observaciones</th>
				<th scope="col" style="width:50px">Resultado</th>
				<th scope="col" style="width:50px">Valor</th>
				<th scope="col" style="width:50px">Ítem</th>
				<th scope="col" style="width:100px">Usuario</th>
				<th scope="col" style="width:100px">Diferencias</th>
			</tr>
			<tbody>
			<%	for (HistorialSBM historial: bean.getHistorialSBM()) { %>
					<tr>
						<td><%= Formateador.formatoFecha(historial.getFechaLog(), Formateador.FORMATO_FECHA_YYYYMMDD_HHMMSS) %></td>
						<td><%= EscapaHTML.escapa(historial.getLog()) %></td>
						<td class="center">
						<%	if (historial.getExcluido()) { %>
								<div title='Mérito excluido' class='circle-true'></div>
						<%	} else { %>
								<div title='Mérito no excluido' class='circle-false'></div>
						<%	} %>
						</td>
						<td class="center">
						<%	if (historial.getValidado()) { %>
								<div title='Mérito validado' class='circle-true'></div>
						<%	} else { %>
								<div title='Mérito no validado' class='circle-false'></div>
						<%	} %>
						</td>
						<td><%= EscapaHTML.escapa(historial.getObservacionCandidato()) %></td>
						<td><%= historial.getResultado() != 0 ? historial.getResultado() : "" %></td>
						<td><%= historial.getValor() != 0 ? historial.getValor() : "" %></td>
						<td><%= EscapaHTML.escapa(historial.getItem() != null ? historial.getItem().getFullCode() : "") %></td>
						<td>
						<%	if (!historial.getRolUsuario().isBlank()) { %>
								<%= EscapaHTML.escapa(historial.getUidUsuario()) %><br/>
								<%= EscapaHTML.escapa("(" + historial.getRolUsuario() + ")") %>
						<%	} %>
						</td>
						<td>
						<%	if (historial.getComparaExcluido() != null && !historial.getComparaExcluido().isBlank()) { %>
								Excluido: <%= historial.getComparaExcluido() %><br/>
						<%	} %>
						
						<%	if (historial.getComparaValidado() != null && !historial.getComparaValidado().isBlank()) { %>
								Validado: <%= historial.getComparaValidado() %><br/>
						<%	} %>
						
						<%	if (historial.getComparaValor() != null && !historial.getComparaValor().isBlank()) { %>
								Valor: <%= historial.getComparaValor() %><br/>
						<%	} %>
						
						<%	if (historial.getComparaItem() != null && !historial.getComparaItem().isBlank()) { %>
								Ítem: <%= historial.getComparaItem() %><br/>
						<%	} %>
						
						<%	if (historial.getComparaResultado() != null && !historial.getComparaResultado().isBlank()) { %>
								Resultado: <%= historial.getComparaResultado() %><br/>
						<%	} %>
						</td>
					</tr>
			<%	} %>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="10" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	
	<div style="overflow-x:auto;">
		<table class="bluetable bolsaempleo" id="tableHistorialMerito">
			<caption>Historial de valoraciones del mérito</caption>
			<tr>
				<th scope="col" style="width:55px">Fecha</th>
				<th scope="col" style="width:84px">Log</th>
				<th scope="col" style="width:50px">Valor</th>
				<th scope="col" style="width:50px">Afinidad</th>
				<th scope="col" style="width:80px">Usuario</th>
				<th scope="col" style="width:100px">Diferencias</th>
			</tr>
			<tbody>
			<%	if (bean.getHistorialValoracionMerito().size() > 0) {
					for (HistorialValoracionMerito historial: bean.getHistorialValoracionMerito()) { %>
						<tr>
							<td><%= Formateador.formatoFecha(historial.getFechaLog(), Formateador.FORMATO_FECHA_YYYYMMDD_HHMMSS) %></td>
							<td><%= EscapaHTML.escapa(historial.getLog()) %></td>
							<td><%= historial.getValor() != 0 ? historial.getValor() : "" %></td>
							<td><%= EscapaHTML.escapa(historial.getAfinidad() != null ? historial.getAfinidad().getCodigo() + " " + historial.getAfinidad().getModulacion() * 100 + "%" : "") %></td>
							<td>
							<%	if (!historial.getRolUsuario().isBlank()) { %>
									<%= EscapaHTML.escapa(historial.getUidUsuario()) %><br/>
									<%= EscapaHTML.escapa("(" + historial.getRolUsuario() + ")") %>
							<%	} %>
							</td>
							<td>
							<%	if (historial.getComparaValoracion() != null && !historial.getComparaValoracion().isBlank()) { %>
									Valor: <%= historial.getComparaValoracion() %><br/>
							<%	} %>
							
							<%	if (historial.getComparaAfinidad() != null && !historial.getComparaAfinidad().isBlank()) { %>
									Afinidad: <%= historial.getComparaAfinidad() %><br/>
							<%	} %>
							</td>
						</tr>
				<%	} %>
			<%	} else { %>
					<tr>
						<td colspan="6">No hay historial de valoraciones del mérito</td>
					</tr>
			<%	} %>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	
	<div style="overflow-x:auto;">
		<table class="bluetable bolsaempleo" id="tableHistorialMerito">
			<caption>Historial del mérito</caption>
			<tr>
				<th scope="col" style="width:55px">Fecha</th>
				<th scope="col" style="width:84px">Log</th>
				<th scope="col" style="width:50px">Valor</th>
				<th scope="col" style="width:50px">Ítem</th>
				<th scope="col" style="width:80px">Usuario</th>
				<th scope="col" style="width:100px">Diferencias</th>
			</tr>
			<tbody>
			<%	for (HistorialMerito historial: bean.getHistorialMerito()) { %>
					<tr>
						<td><%= Formateador.formatoFecha(historial.getFechaLog(), Formateador.FORMATO_FECHA_YYYYMMDD_HHMMSS) %></td>
						<td><%= EscapaHTML.escapa(historial.getLog()) %></td>
						<td><%= historial.getValor() != 0 ? historial.getValor() : "" %></td>
						<td><%= EscapaHTML.escapa(historial.getItem() != null ? historial.getItem().getFullCode() : "") %></td>
						<td>
						<%	if (!historial.getRolUsuario().isBlank()) { %>
								<%= EscapaHTML.escapa(historial.getUidUsuario()) %><br/>
								<%= EscapaHTML.escapa("(" + historial.getRolUsuario() + ")") %>
						<%	} %>
						</td>
						<td>
						<%	if (historial.getComparaValor() != null && !historial.getComparaValor().isBlank()) { %>
								Valor: <%= historial.getComparaValor() %><br/>
						<%	} %>
						
						<%	if (historial.getComparaItem() != null && !historial.getComparaItem().isBlank()) { %>
								Ítem: <%= historial.getComparaItem() %><br/>
						<%	} %>
						</td>
					</tr>
			<%	} %>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	
	<br/>
    <div class="btns-by-steps">
		<button class="link-btn" id="historial_volver">
	    	 Volver
	    </button>
	</div>
	
</div>
	
<script>
$(document).ready(function() {
	
	document.getElementById("historial_volver").addEventListener("click", function() {
		var params = {
				'<%= ControladorValidar.PARAM_ACCION %>': '<%= ControladorValidar.ACCION_MERITO_SELECCIONADO %>',
				'<%= ControladorValidar.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
				'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
				'<%= ControladorValidar.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>',
		}
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	});
	
}); 
</script>
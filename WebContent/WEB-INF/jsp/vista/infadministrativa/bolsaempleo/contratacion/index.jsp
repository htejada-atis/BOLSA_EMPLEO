<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaContratacion bean = (VistaContratacion) uvdatos.getVistas().get(VistaContratacion.class.getName());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Contratación</h2>
    
	    <button class="link-btn" id="nueva_plaza">
	    	 Nueva plaza
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tablePlazasOfertadas">
		<tr>
			<th scope="col" style="width:100px" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:100%" class="area">Área</th>			
			<th scope="col" style="width:100px">Fecha creación</th>
			<th scope="col" style="width:100px">Fecha cerrada</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
	
	var table = new Atis.DataTable('#tablePlazasOfertadas', {
	    "ajax": { url: "<%= ControladorContratacion.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS %>",
	    "columns": [
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'},
	        {'data': 'fechaCreacion', 'filter': {'type': 'date'}},
	        {'data': 'fechaCierre', 'filter': {'type': 'date'}}
	    ]
	});
	
}); 
</script>

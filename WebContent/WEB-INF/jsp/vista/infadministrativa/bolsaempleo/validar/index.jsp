<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria()!=null){
			descripcion = "Última convocatoria abierta:" + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias abiertas en este momento";
		}
	
	if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= descripcion %></h3>

	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%">Id.</th>
			<th scope="col" style="width:5%" title="Código área">Cod. Area.</th>
			<th scope="col" style="width:25%" class="area">Area</th>			
			<th scope="col" style="width:10%" class="center"></th>			
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
	var table = new Atis.DataTable('#table', {
	    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidar.ACCION_DATATABLE %>",
	    "defaultOrderBy": 2,
	    "defaultOrderDirection": 'desc',
	    "columns": [
	    	{'data': 'codNum', 'filter': {'type': 'number'}},
	    	{'data': 'area.idAreaExterno', 'filter': {'type': 'number'}},
	        {'data': 'area.descripcion', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'codnum', 'buttons': [{'label': 'Filtrar bolsa', 'onClick': function(row) {}}]}        
	    ],
	    "actions": [
	    	{'label': 'Filtrar bolsa', 'onClick': function(selected) {  } },	    	
	    ]
	});		
}); 
</script>


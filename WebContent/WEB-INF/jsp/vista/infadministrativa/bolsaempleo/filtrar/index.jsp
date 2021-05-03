<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorFiltrar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFiltrar" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFiltrar bean = (VistaFiltrar)uvdatos.getVistas().get(VistaFiltrar.class.getName());
%>

<div class='bolsas'>

	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
	<h2>Filtrado de candidatos por titulación</h2>
	<p>Filtrado de candidatos por titulaciones, para su posible exclusión.</p>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:40%">Candidato</th>
			<th scope="col" style="width:25%" title="Código área">Nº Titulaciones</th>
			<th scope="col" style="width:25%" class="area">Nº Tit. Validadas</th>			
			<th scope="col" style="width:10%"></th>			
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
		    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/filtrar" },
		    "pageSize": 10,
		    "filterable": true,
		    "action": "<%= ControladorFiltrar.ACCION_DATATABLE %>",
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
	
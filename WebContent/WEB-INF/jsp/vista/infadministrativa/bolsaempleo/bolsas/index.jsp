<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEstadoBolsas bean = (VistaEstadoBolsas) uvdatos.getVistas().get(VistaEstadoBolsas.class.getName());
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
	<% } else { %>
	<h2>Estado de las bolsas</h2>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id de la convocatoria">Id</th>
			<th scope="col" style="width:25%">Area</th>
			<th scope="col" style="width:15%">Estado</th>
			<th scope="col" style="width:15%">Actualizada</th>
			<th scope="col" style="width:15%">Bloqueo</th>
			<th scope="col" style="width:15%">Desbloqueo</th>
			<th scope="col" style="width:15%">Baremable</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="9" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
	
	<script>
	$(document).ready(function() {
		function clickRow(row) {
			console.log("row", row);
		}
		
		var table = new DataTable('#table', {
		    "ajax": { url: "<%= request.getRequestURI() %>" },
		    "selectable": true,
		    "columns": [
		    	{'data': 'idBolsa', 'selectable': true},
		        {'data': 'idBolsa'},
		        {'data': 'area.nombre'},
		        {'data': 'estado'},
		        {'data': 'fechaActualizacion'},
		        {'data': 'fechaBloqueo'},
		        {'data': 'fechaDesBloqueo'},
		        {'data': 'baremable'},
		        {'data': 'idBolsa', 'buttons': [{'label': 'Click', 'onClick': function(row) {console.log("row", row);}}]}		        
		    ],
		});
		
		setInterval(function() {
			//console.log(table.getChecked())
		}, 5000)
	}); 
	</script>
</div>
	
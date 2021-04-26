<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorAfinidades"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaAfinidades"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAfinidades bean = (VistaAfinidades) uvdatos.getVistas().get(VistaAfinidades.class.getName());
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
	<h2>Afinidades</h2>
	
	<table class="bluetable bolsaempleo" id="tableAfinidades">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id de la afinidad">Id</th>
			<th scope="col" class="center" style="width:15%" title="Código de afinidad">Código</th>
			<th scope="col" style="width:65%">Descripcion</th>
			<th scope="col" class="center" style="width:25%">Modulación</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>
	
<script>
$(document).ready(function() {
	var table = new Atis.DataTable('#tableAfinidades', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/afinidades" },
	    "selectable": true,
	    "pageSize": 10,
	    "action": "<%= ControladorAfinidades.ACCION_DATATABLE %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'codigo', 'render': function(row) {
        			return "<div style='text-align: center;'>"+row.codigo+"</div>"; 
        	}},
	        {'data': 'descripcion'},
	        {'data': 'modulacion', 'render': function(row) {
        			return "<div style='text-align: center;'>"+row.modulacion+"</div>"; 
        	}},
	    ]
	});	
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las áreas', 'Seleccione al menos un área.');
			return;
		}

   		Atis.sendForm("<%= request.getRequestURI() %>", params);
	}
}); 
</script>

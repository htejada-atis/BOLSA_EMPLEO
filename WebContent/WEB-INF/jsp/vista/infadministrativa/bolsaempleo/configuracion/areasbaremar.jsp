<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaAreasBaremar"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAreasBaremar bean = (VistaAreasBaremar) uvdatos.getVistas().get(VistaAreasBaremar.class.getName());
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
	<h2>Bolsas baremables</h2>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id de la area">Id</th>
			<th scope="col" style="width:25%" title="Código de area">Código</th>
			<th scope="col" style="width:65%">Area</th>
			<th scope="col" style="width:15%">Baremable</th>			
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
	var table = new DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar" },
	    "selectable": true,
	    "pageSize": 10,
	    "action": "<%= ControladorAreasABaremar.ACCION_DATATABLE %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'area.idAreaExterno'},
	        {'data': 'area.descripcion'},
	        {'data': 'baremable'},
	    ],
	    "actions": [
	    	{'label': 'Baremable', 'onClick': function(selected) { enviaAccion("<%=ControladorAreasABaremar.ACCION_AREA_PASAR_A_BAREMALE%>", selected); } },
	    	{'label': 'No baremable', 'onClick': function(selected) { enviaAccion("<%=ControladorAreasABaremar.ACCION_AREA_PASAR_A_NO_BAREMALE%>", selected); } },	    	
	    ]
	});	
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las áreas', 'Seleccione al menos un área.');
			return;
		}
		
		var params = {
			'a': '<%=ControladorAreasABaremar.ACCION_AREA%>', 
			'<%=ControladorAreasABaremar.PARAM_ACCION_AREA%>': accion, 
			'<%=ControladorAreasABaremar.PARAM_AREAS_SELECCIONADAS%>': Atis.object2Json(selected)
		};
		
   		Atis.sendForm("<%= request.getRequestURI() %>", params);
	}
}); 
</script>
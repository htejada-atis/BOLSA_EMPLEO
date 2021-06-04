<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAreasBaremar"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAreasBaremar bean = (VistaAreasBaremar) uvdatos.getVistas().get(VistaAreasBaremar.class.getName());
%>

<div class='bolsa-empleo areasbaremar'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Áreas baremables</h2>
    
	    <button class="link-btn" id="importar_areas_uvirtual">
	    	Importar Areas UVirtual
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id de la area">Id</th>
			<th scope="col" style="width:25%" class="codigo" title="Código de area">Código</th>
			<th scope="col" style="width:65%">Area</th>
			<th scope="col" class="center" style="width:15%">Baremable</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>
	
<script>
$(document).ready(function() {
	var table = new Atis.DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar" },
	    "selectable": true,
	    "pageSize": 200,
	    "pageSizeOptions": [10, 100, 200],
	    "action": "<%= ControladorAreasABaremar.ACCION_DATATABLE %>",
	    "filterable": true,
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum', 'filter': {'type': 'number'}},
	        {'data': 'area.idAreaExterno', 'filter': true},
	        {'data': 'area.descripcion', 'filter': true},
	        {'data': 'baremable', 'filter': {'type': 'select', 'options': {'true': 'Baremable', 'false': 'No baremable'}}, 'render': function(row) {
        		if(row.baremable){
        			return "<div title='Baremable' class='circle-true'></div>"; 
        		}
        		else{
        			return "<div title='No Baremable' class='circle-false'></div>"; 
        		}
        	}},
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
	
	document.getElementById("importar_areas_uvirtual").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%=ControladorAreasABaremar.ACCION_IMPORTAR_AREAS_UVIRTUAL%>'});
	});
	
}); 
</script>

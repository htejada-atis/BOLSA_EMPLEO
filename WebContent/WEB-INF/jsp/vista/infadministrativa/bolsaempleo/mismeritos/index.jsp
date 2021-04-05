<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritos bean = (VistaMeritos) uvdatos.getVistas().get(VistaMeritos.class.getName());
%>

<div class="bolsa-empleo">
	
	<div class="titulo-bolsa-empleo">
		<h2>Mis méritos</h2>
    
	    <a class="link-btn" id="nuevo_merito" href="<%= request.getRequestURI() %>">
	    	 Nuevo mérito
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableMeritos">
		<caption>MÉRITOS QUE LA COMISIÓN EVALUARÁ</caption>
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:10%" title="Id del mérito">Id</th>
			<th scope="col"	style="width:10%">Apartado</th>
			<th scope="col"	style="width:10%">Ítem</th>
			<th scope="col"	style="width:20%">Descripción</th>
			<th scope="col"	style="width:20%">Valor</th>
			<th scope="col"	style="width:20%">Observación</th>
			<th scope="col"	style="width:20%">Fichero</th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>

	$(document).ready(function() {
		
		var table = new DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorMisMeritos.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%=ControladorMisMeritos.ACCION_DATATABLE%>",
		    "selectable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'codNum'},
		        {'data': 'apartado'},
		        {'data': 'item'},
		        {'data': 'descripcion', 'class': 'overflow-auto'},
		        {'data': 'valor', 'class': 'overflow-auto'},
		        {'data': 'observacion', 'class': 'overflow-auto'},
		        {'data': 'fichero', 'class': 'overflow-auto'},
		    ],
		    "actions": [
		    	{'label': 'Conservar méritos seleccionados', 'onClick': function(selected) {
		    		console.log(selected);
		    	}},
		    ]
		});
		
	});
	}


</script>
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

	<% if (session.getAttribute(ControladorMisMeritos.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorMisMeritos.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorMisMeritos.MENSAJE_ENVIADO);
		} 
	%>
	
	<div class="titulo-bolsa-empleo">
		<h2>Mis méritos</h2>
    
	    <a class="link-btn" id="nuevo_merito" href="<%= request.getRequestURI() %>">
	    	 Nuevo mérito
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableMeritos">
		<!--<caption>MÉRITOS QUE LA COMISIÓN EVALUARÁ</caption>-->
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:8%" title="Id del mérito">Id</th>
			<th scope="col"	style="width:10%">Apartado</th>
			<th scope="col"	style="width:10%">Ítem</th>
			<th scope="col"	style="width:20%">Descripción</th>
			<th scope="col"	style="width:10%">Valor</th>
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
		
		document.getElementById("nuevo_merito").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorMisMeritos.ACCION_AGREGAR_MERITO %>'});
		});
		
		var table = new Atis.DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorMisMeritos.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%=ControladorMisMeritos.ACCION_DATATABLE%>",
		    "selectable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'codNum'},
		        {'data': 'item.bloque.apartado.codigo'},
		        {'data': 'item.codigo'},
		        {'data': 'descripcion', 'class': 'overflow-auto'},
		        {'data': 'valor', 'class': 'overflow-auto'},
		        {'data': 'observacion', 'class': 'overflow-auto'},
		        {'data': 'codnum', 'class': 'overflow-ellipsis', 'render': function(row) {
		        	var link = "<%= request.getRequestURI() %>?a=<%= ControladorMisMeritos.ACCION_DESCARGAR_FICHERO %>&<%= ControladorMisMeritos.PARAM_ID %>=" + row.codNum;
		        	return "<a class='consultar-fichero' href='" +link +"' target='_blank'>" +link +"</a>"; 
		        	}
		        },
		    ],
		    "actions": [
		    	{'label': 'Eliminar', 'onClick': function(selected) {
		    		if(selected.length) {
		    			var mensaje = "¿Desea borrar el mérito seleccionado?";
			        	var titulo = "Borrar mérito";
			        	
			        	if(selected.length > 1) {
			        		mensaje = "¿Desea borrar los méritos seleccionados?";
				        	titulo = "Borrar méritos";
			        	}
			        	
		    			Atis.confirmDialog(titulo, mensaje, {
					        Si: function() {
					        	var params = {
					    				'<%=ControladorMisMeritos.PARAM_ACCION%>': '<%=ControladorMisMeritos.ACCION_ELIMINAR_MERITOS%>', 
					    				'<%=ControladorMisMeritos.PARAM_MERITOS%>': JSON.stringify(selected)};
				        		Atis.sendForm("<%=request.getRequestURI()%>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					        }
					    });
		    			
		    		}
		    	}},
		    ]
		});
		
	});


</script>

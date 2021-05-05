<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>

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
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError()   %>
		</div>
	<% } %>
	
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
			<th scope="col" style="width:10%">Id</th>
			<th scope="col" style="width:10%">Bloque</th>
			<th scope="col"	style="width:12%">Código ítem</th>
			<th scope="col"	style="width:15%">Nombre ítem</th>
			<th scope="col"	style="width:15%">Descripción</th>
			<th scope="col"	style="width:8%">Valor</th>
			<th scope="col"	style="width:15%">Observación</th>
			<th scope="col"	style="width:12%"></th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="9" style="width:100%"></th>
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
		
		var optionsApartados = {};
		
		<% if (bean.getApartados() != null) { %>
			<% for (ApartadoBaremacion apartado: bean.getApartados()) { %>
				optionsApartados[<%=apartado.getCodNum()%>] = '<%=apartado.getNombre()%>';
			<% } %>
		<% } %>
		
		var table = new Atis.DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorMisMeritos.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%=ControladorMisMeritos.ACCION_DATATABLE%>",
		    "selectable": true,
		    "filterable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		    	{'data': 'item.bloque.apartado.nombre', 'filter': {'type': 'select', 'options': optionsApartados}},
		        {'data': 'item', 'filter': true, 'render': function(row) {
		        	return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
	        	}},
		        {'data': 'item.nombre', 'filter': true},
		        {'data': 'descripcion', 'filter': true},
		        {'data': 'valor', 'filter': true, 'overflow': 'auto'},
		        {'data': 'observacion', 'filter': true},
		        {'data': 'codnum', 'buttons': [
	        		{'label': 'Descargar', 'title': 'Descargar fichero del mérito', 'onClick': function(row) {
	        			window.open("<%= request.getRequestURI() %>"
	        		        	+ "?a=<%= ControladorMisMeritos.ACCION_DESCARGAR_FICHERO %>&<%= ControladorMisMeritos.PARAM_ID %>=" + row.codNum);
	        		}},
	   			]}
		    ],
		    "actions": [
		    	{'label': 'Eliminar', 'title': 'Eliminar méritos seleccionados', 'onClick': function(selected) {
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

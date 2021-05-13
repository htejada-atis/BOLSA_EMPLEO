<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());
%>
<div class="bolsa-empleo">
	
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
	
	<div class="titulo-bolsa-empleo">
		<h2>Documentos del sistema</h2>
    
	    <a class="link-btn" id="nuevo_fichero" href="<%= request.getRequestURI() %>">
	    	 Nuevo documento
	    </a>
	</div>

    <table class="bluetable bolsaempleo" id="table_ficheros">
		<tr>
			<th scope="col" style="width:10%"></th>
			<th scope="col"	style="width:20%">Nombre</th>
			<th scope="col"	style="width:25%">Título</th>
			<th scope="col" style="width:35%">Ruta del fichero</th>
			<th scope="col" class="center" style="width:10%">Público</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colspan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>

	$(document).ready(function() {
		
		var table = new Atis.DataTable('#table_ficheros', {
		    "ajax": { url: "<%= ControladorGestionFicheros.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "selectable": true,
		    "filterable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		        {'data': 'nombre', 'filter': true, 'overflow': 'auto'},
		        {'data': 'titulo', 'filter': true, 'overflow': 'auto'},
		        {'data': 'codnum', 'order': {'active': false}, 'class': 'overflow-ellipsis', 'render': function(row) {
		        	var link = "<%= ControladorGestionFicheros.URL_PATTERN_FILES_PRIVADA %>"
		        	+ "?a=<%= ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO %>&<%= ControladorGestionFicheros.PARAM_FICHERO %>=" + row.codNum;
		        	return "<a class='consultar-fichero' title='Descargar fichero' href='" +link +"' target='_blank'>" +link +"</a>"; 
		        	}
		        },
		        {'data': 'publico', 'filter': {'type': 'select', 'options': {'true': 'Públicos', 'false': 'Privados'}}, 'order': {'active': false}, 'render': function(row) {
	        		if(row.publico){
	        			return "<div title='Público' class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div title='Privado' class='circle-false'></div>"; 
	        		}
	        	}},
		        {'data': 'codnum', 'buttons': [{'label': '<label class="tooltiptext">Copiar enlace</label>Copiar', 'class': 'tooltip', 'onClick': function(row) {
			        	$(this).parent().parent().parent().find(".tooltiptext").text("¡Enlace copiado!");
			        	
			        	var link = "<%= ControladorGestionFicheros.URL_PATTERN_FILES_PRIVADA %>"
				        	+ "?a=<%= ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO %>&<%= ControladorGestionFicheros.PARAM_FICHERO %>=" + row.codNum;
			        	navigator.clipboard.writeText(link);
			        }
			    }]}
		    ],
		    "actions": [
		    	{'label': 'Eliminar', 'title': 'Eliminar ficheros seleccionados', 'onClick': function(selected) {
		    		if(selected.length) {
		    			var titulo = selected.length > 1 ? "Eliminar ficheros" : "Eliminar fichero";
		    			var mensaje = selected.length > 1 ? "¿Desea eliminar los ficheros seleccionados?" : "¿Desea eliminar el fichero seleccionado?";
		    			
		    			Atis.confirmDialog(titulo, mensaje, {
			        		Si: function() {
			        			var params = {'a': '<%= ControladorGestionFicheros.ACCION_BORRAR_FICHEROS %>',
			        					'<%= ControladorGestionFicheros.PARAM_FICHEROS %>': JSON.stringify(selected)};
				        		Atis.sendForm("<%= request.getRequestURI() %>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					    	}
					    });
		    		}
		    	}},
		    	{'label': 'Hacer públicos', 'title': 'Los ficheros seleccionados serán visibles para todos', 'onClick': function(selected) {
		    		if(selected.length) {
		    			var titulo = selected.length > 1 ? "Hacer ficheros públicos" : "Hacer fichero público";
		    			var mensaje = selected.length > 1 ? "¿Desea hacer publicos los ficheros seleccionados?" : "¿Desea hacer público el fichero seleccionado?";
		    			
		    			Atis.confirmDialog(titulo, mensaje, {
			        		Si: function() {
			        			var params = {'a': '<%= ControladorGestionFicheros.ACCION_HACER_FICHEROS_PUBLICOS %>',
			        					'<%= ControladorGestionFicheros.PARAM_FICHEROS %>': JSON.stringify(selected)};
				        		Atis.sendForm("<%= request.getRequestURI() %>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					    	}
					    });
		    		}
		    	}},
		    	{'label': 'Hacer privados', 'title': 'Los ficheros seleccionados sólo serán visibles para usuarios logueados', 'onClick': function(selected) {
		    		if(selected.length) {
		    			var titulo = selected.length > 1 ? "Hacer ficheros privados" : "Hacer fichero privado";
		    			var mensaje = selected.length > 1 ? "¿Desea hacer privados los ficheros seleccionados?" : "¿Desea hacer privado el fichero seleccionado?";
		    			
		    			Atis.confirmDialog(titulo, mensaje, {
			        		Si: function() {
			        			var params = {'a': '<%= ControladorGestionFicheros.ACCION_HACER_FICHEROS_PRIVADOS %>',
			        					'<%= ControladorGestionFicheros.PARAM_FICHEROS %>': JSON.stringify(selected)};
				        		Atis.sendForm("<%= request.getRequestURI() %>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					    	}
					    });
		    		}
		    	}}
		    ]
		});
		
		document.getElementById("nuevo_fichero").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionFicheros.ACCION_SUBIR_FICHERO %>'});
		});
		
		$("#table_ficheros").on("mouseover", ".tooltip", function() {
			$(this).find(".tooltiptext").text("Copiar enlace");
		})
		
	});

</script>

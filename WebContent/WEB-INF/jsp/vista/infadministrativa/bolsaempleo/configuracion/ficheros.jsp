<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());
%>
<div class="bolsa-empleo">
	
	<% if (session.getAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO);
		} 
	%>
	
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
			<th scope="col" style="width:10%">Público</th>
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
		
		var table = new DataTable('#table_ficheros', {
		    "ajax": { url: "<%= ControladorGestionFicheros.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "selectable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		        {'data': 'nombre', 'render': function(row) {
		    		return "<div class='overflow-auto'>" +row.nombre +"</div>";
		    	}},
		        {'data': 'titulo', 'render': function(row) {
		    		return "<div class='overflow-auto'>" +row.titulo +"</div>";
		    	}},
		        {'data': 'codnum', 'class': 'overflow-ellipsis', 'render': function(row) {
		        	var link = "<%= request.getRequestURI() %>?a=<%= ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO %>&<%= ControladorGestionFicheros.PARAM_ID %>=" + row.codNum;
		        	return "<a class='consultar-fichero' href='" +link +"' target='_blank'>" +link +"</a>"; 
		        	}
		        },
		        {'data': 'publico'},
		        {'data': 'codnum', 'buttons': [{'label': '<label class="tooltiptext">Copiar enlace</label>Copiar', 'class': 'tooltip', 'onClick': function(row) {
			        	$(this).parent().parent().parent().find(".tooltiptext").text("¡Enlace copiado!");
			        	
			        	var link = "<%= request.getRequestURI() %>?a=<%= ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO %>&<%= ControladorGestionFicheros.PARAM_ID %>=" + row.codNum;
			        	navigator.clipboard.writeText(link);
			        	}
			    }]}
		    ],
		    "actions": [
		    	{'label': 'Eliminar', 'onClick': function(selected) {
		    		if(selected.length) {
		    			var titulo = selected.length > 1 ? "Borrar ficheros" : "Borrar fichero";
		    			var mensaje = selected.length > 1 ? "¿Desea borrar los ficheros seleccionados?" : "¿Desea borrar el fichero seleccionado?";
		    			
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
		    ]
		});
		
		document.getElementById("nuevo_fichero").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionFicheros.ACCION_SUBIR_FICHERO %>'});
		});
		
		$("#table_ficheros").on("mouseover", ".tooltip", function() {
			console.log("oiga")
			$(this).find(".tooltiptext").text("Copiar enlace");
		})
		
	});

</script>
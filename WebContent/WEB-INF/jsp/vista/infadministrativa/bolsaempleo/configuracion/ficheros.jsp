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
			<th scope="col"	style="width:30%">Nombre</th>
			<th scope="col"	style="width:30%">Título</th>
			<th scope="col" style="width:30%">Ruta del fichero</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colspan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>

	function confirmDialog(title, message, id) {
		$('<div></div>').appendTo('body')
	    	.html('<div><h6>' + message + '</h6></div>')
	    	.dialog({
		      modal: true,
		      title: title,
		      zIndex: 10000,
		      autoOpen: true,
		      width: 'auto',
		      resizable: false,
		      buttons: {
		        Si: function() {
		        	var params = {'a': '<%= ControladorGestionFicheros.ACCION_BORRAR_FICHERO %>', '<%= ControladorGestionFicheros.PARAM_ID %>': id};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		          	$(this).dialog("close");
		        },
		        No: function() {
		          	$(this).dialog("close");
		        }
		      },
		      close: function(event, ui) {
		        $(this).remove();
		      }
		});
	}

	$(document).ready(function() {
		
		var table = new DataTable('#table_ficheros', {
		    "ajax": { url: "<%= ControladorGestionFicheros.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "columns": [
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
		        {'data': 'codnum', 'buttons': [{'label': 'Borrar', 'onClick': function(row) {
			        		confirmDialog("Borrar fichero", "¿Desea borrar el fichero seleccionado?", row.codNum);
			        	}
			        },{'label': '<label class="tooltiptext">Copiar enlace</label>Copiar', 'class': 'tooltip', 'onClick': function(row) {
			        	console.log($(this).parent().parent().parent().find(".tooltiptext").text("¡Enlace copiado!"));
			        	
			        	var link = "<%= request.getRequestURI() %>?a=<%= ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO %>&<%= ControladorGestionFicheros.PARAM_ID %>=" + row.codNum;
			        	navigator.clipboard.writeText(link);
			        	}
			        }]}
		    ],
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
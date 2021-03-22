<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionNoticias"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaNoticias bean = (VistaNoticias) uvdatos.getVistas().get(VistaNoticias.class.getName());
%>


<div class="bolsa-empleo">

	<% if (session.getAttribute(ControladorGestionNoticias.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorGestionNoticias.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorGestionNoticias.MENSAJE_ENVIADO);
		} 
	%>

	<div class="titulo-noticias">
		<h2>Noticias</h2>
    
	    <a class="link-btn" id="nueva_noticia" href="<%= request.getRequestURI() %>">
	    	 Nueva noticia
	    </a>
	</div>
    
    <table class="bluetable bolsaempleo" id="table_noticias_insertadas">
		<tr>
			<th scope="col" style="width:20%">Fecha</th>
			<th scope="col"	style="width:30%">Texto</th>
			<th scope="col" style="width:30%">Enlace</th>
			<th scope="col" style="width:10%">Pública</th>
			<th scope="col" style="width:10%">Activa</th>
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
		
		function confirmDialog(title, message, id, activa) {
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
			        	var params = {'a': '<%= ControladorGestionNoticias.ACCION_ELIMINAR_NOTICIA %>',
			        			'id': id,
			        			'<%= ControladorGestionNoticias.PARAM_ACTIVA %>': !activa};
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
		
		document.getElementById("nueva_noticia").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA %>'});
		});

		var table = new DataTable('#table_noticias_insertadas', {
		    "ajax": { url: "<%= request.getRequestURI() %>" },
		    "columns": [
		    	{'data': 'fechaFormato'},
		    	{'data': 'texto'},
		        {'data': 'enlace', 'render': function(row) { return "mi enlace"; }},
		        {'data': 'publica'},
		        {'data': 'activa'},
		        {'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
			        		var params = {'a': '<%= ControladorGestionNoticias.ACCION_EDITAR_NOTICIA %>', 'id': row.codNum};
			        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			        	}
			        }, {'label': function(row) { return "Borrar u otra cosa"; }, 'onClick': function(row) {
			        	var mensaje = "¿Desea borrar la noticia seleccionada?";
			        	var titulo = "Borrar noticia";
			        	if(!row.activa) {
			        		titulo = "Restaurar noticia";
			        		mensaje = "¿Desea restaurar la noticia seleccionada?";
			        	}
			        	
			        	confirmDialog(titulo, mensaje, row.codNum, row.activa);
			        } }]}		        
			    ],
			});
			
			setInterval(function() {
				//console.log(table.getChecked())
			}, 5000)
		});
	
	</script>

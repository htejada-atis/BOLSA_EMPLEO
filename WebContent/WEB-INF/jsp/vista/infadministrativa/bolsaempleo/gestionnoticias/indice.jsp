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

	<% if (session.getAttribute("exito") != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute("exito") %>
		</div>
	<% 
			session.removeAttribute("exito");
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
	$(document).ready(function() {
		
		function confirmDialog(message) {
			console.log(
			$('<div></div>').appendTo('body')
		    	.html('<div><h6>' + message + '?</h6></div>')
		    	.dialog({
			      modal: true,
			      title: 'Delete message',
			      zIndex: 10000,
			      autoOpen: true,
			      width: 'auto',
			      resizable: false,
			      buttons: {
			        Yes: function() {
			          // $(obj).removeAttr('onclick');                                
			          // $(obj).parents('.Parent').remove();
	
			          $('body').append('<h1>Confirm Dialog Result: <i>Yes</i></h1>');
	
			          $(this).dialog("close");
			        },
			        No: function() {
			          $('body').append('<h1>Confirm Dialog Result: <i>No</i></h1>');
	
			          $(this).dialog("close");
			        }
			      },
			      close: function(event, ui) {
			        $(this).remove();
			      }
			}));
		}

		var table = new DataTable('#table_noticias_insertadas', {
		    "ajax": { url: "<%= request.getRequestURI() %>" },
		    "columns": [
		    	{'data': 'fechaFormato'},
		    	{'data': 'texto'},
		        {'data': 'enlace'},
		        {'data': 'publica'},
		        {'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
			        		var params = {'a': '<%= ControladorGestionNoticias.ACCION_EDITAR_NOTICIA %>', 'id': row.codNum};
			        		sendForm("<%= request.getRequestURI() %>", params)
			        	}
			        }, {'label': 'Borrar', 'onClick': function(row) {
			        	confirmDialog("aaaaa");
			        } }]}		        
			    ],
			});
			
			setInterval(function() {
				//console.log(table.getChecked())
			}, 5000)
		}); 
	
		document.getElementById("nueva_noticia").addEventListener("click", function(event) {
			event.preventDefault();
			sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA %>'});
		});
	
	</script>
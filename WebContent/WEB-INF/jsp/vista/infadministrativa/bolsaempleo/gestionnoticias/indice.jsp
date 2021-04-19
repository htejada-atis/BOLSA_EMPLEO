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

	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else {%>

	<div class="titulo-bolsa-empleo">
		<h2>Noticias</h2>
    
	    <a class="link-btn" id="nueva_noticia" href="<%= request.getRequestURI() %>">
	    	 Nueva noticia
	    </a>
	</div>
    
    <table class="bluetable bolsaempleo" id="table_noticias_insertadas">
		<tr>
			<th scope="col" style="width:12%">Fecha</th>
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
	
	<% } %>
</div>

<script>
	$(document).ready(function() {
		
		document.getElementById("nueva_noticia").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA %>'});
		});

		var table = new DataTable('#table_noticias_insertadas', {
		    "ajax": { url: "<%= ControladorGestionNoticias.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "columns": [
		    	{'data': 'fecha'},
		    	{'data': 'texto', 'render': function(row) {
		    		return "<div class='overflow-auto'>" +row.texto +"</div>";
		    	}},
		        {'data': 'enlace', 'class': 'overflow-ellipsis', 'render': function(row) { return "<a href='" +row.enlace +"' target='_blank'>" +row.enlace +"</a>"; }},
		        {'data': 'publica', 'render': function(row) {
	        		if(row.publica){
	        			return "<div class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div class='circle-false'></div>"; 
	        		}
	        	}},
		        {'data': 'activa', 'render': function(row) {
	        		if(row.activa){
	        			return "<div class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div class='circle-false'></div>"; 
	        		}
	        	}},
		        {'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
			        		var params = {'a': '<%= ControladorGestionNoticias.ACCION_EDITAR_NOTICIA %>', 'id': row.codNum};
			        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			        	}
			        }, {'label': function(row) { return row.activa ? "Borrar" : "Restaurar"; }, 'onClick': function(row) {
			        	var mensaje = "¿Desea borrar la noticia seleccionada?";
			        	var titulo = "Borrar noticia";
			        	if(!row.activa) {
			        		titulo = "Restaurar noticia";
			        		mensaje = "¿Desea restaurar la noticia seleccionada?";
			        	}
			        	
			        	Atis.confirmDialog(titulo, mensaje, {
					        Si: function() {
					        	var params = {'a': '<%= ControladorGestionNoticias.ACCION_ELIMINAR_NOTICIA %>',
					        			'id': row.codNum,
					        			'<%= ControladorGestionNoticias.PARAM_ACTIVA %>': !row.activa};
				        		Atis.sendForm("<%= request.getRequestURI() %>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					        }
					      });
			        	
			        } }]}		        
			    ],
			});
			
			setInterval(function() {
				//console.log(table.getChecked())
			}, 5000)
		});
	
	</script>

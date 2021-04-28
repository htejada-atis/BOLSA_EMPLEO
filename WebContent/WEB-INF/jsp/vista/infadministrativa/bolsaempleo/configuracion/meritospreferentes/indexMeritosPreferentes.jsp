<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMeritosPreferentes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritosPreferentes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentes bean = (VistaMeritosPreferentes) uvdatos.getVistas().get(VistaMeritosPreferentes.class.getName());
%>

<div class="bolsa-empleo">	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError()   %>
		</div>
	<% } %>
	
	<div class="titulo-bolsa-empleo">
		<h2>Tipos méritos preferentes</h2>
    
	    <a class="link-btn" id="nuevo_meritopreferente" href="<%= request.getRequestURI() %>">
	    	Nuevo tipo mérito preferente
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%" title="Id del tipo de mérito preferente">Id</th>
			<th scope="col"	style="width:20%" title="Descripción del mérito">Descripción</th>
			<th scope="col"	style="width:20%">Tipo</th>
			<th scope="col"	style="width:20%">Aplicable</th>			
			<th scope="col"	style="width:10%">Factor</th>
			<th scope="col"	style="width:10%">Activo</th>
			<th scope="col"	style="width:10%"></th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="7" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>	
</div>

<script>

	$(document).ready(function() {
		var table = new Atis.DataTable('#table', {
		    "ajax": { url: "<%=  ControladorMeritosPreferentes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%= ControladorMeritosPreferentes.ACCION_DATATABLE %>",
		    "filterable": true,
		    "columns": [
		    	{'data': 'codNum'},
		        {'data': 'descripcion', 'filter': true},
		        {'data': 'tipo', 'render': function(row) {
		        	var text;
		        	
		        	switch(row.tipo) {
		        	case '<%= ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE %>':
		        		text = 'Titulación preferente';
		        		break;
		        	case '<%= ModeloMeritosPreferentes.TIPO_MERITO %>':
		        		text = 'Mérito: ' + row.tipoItemBaremacion.nombre;
		        		break;		        	
		        	}
		        			        	
		        	return text;
		        }},
		        {'data': 'aplicable', 'render': function(row) {
		        	var text;
		        	
		        	switch(row.aplicable) {
		        	case '<%= ModeloMeritosPreferentes.APLICABLE_BLOQUE %>':
		        		text = 'Al apartado. '
		        		break;
		        	case '<%= ModeloMeritosPreferentes.APLICABLE_APARTADO %>':
		        		text = 'Al bloque. ' + row.aplicableApartadoBaremacion.nombre;
		        		break;
		        	case '<%= ModeloMeritosPreferentes.APLICABLE_ITEM %>':
		        		text = 'Al mérito. ';
		        		break;
		        	}
		        	
		        	return text;
		        }},
		        {'data': 'factor'},
		        {'data': 'activo', 
		         'render': function(row) { return '<div title="' + (row.activo ? 'Activo' : 'Inactivo') +'" class="'+ (row.activo ? 'circle-true' : 'circle-false') +'"></div>'; }, 
		         'filter': {'type': 'select', 'options': {'true': 'Activo', 'false': 'Inactivo'}, 'optionDefault': 'true'},		         
		         'order': {'active': false}
		        },
		        {'data': 'codNum', 'buttons': [
		        	{'label': 'Editar', 'onClick': function(row) {
			    			var params = {
			    				'a': '<%=ControladorMeritosPreferentes.ACCION_MODIFICAR%>', 
			    				'<%= ControladorMeritosPreferentes.PARAM_ID %>': row.codNum
			    			};
		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
			    		}
		        	},
		        	{'label': function(row) { 
    						if(row.activo) return "Desactivar"
    						else return "Activar"; 
    					}, 
    					'onClick': function(row) {
    						if(row.activo){
    							Atis.confirmDialog("Desactivar merito", "¿Desea desactivar este mérito preferente en la base de datos?", {
    	    		            	Si: function() {
    	    		            		var params = {
    	    				    				'a': '<%=ControladorMeritosPreferentes.ACCION_DESACTIVAR%>', 
    	    				    				'<%= ControladorMeritosPreferentes.PARAM_ID %>': row.codNum
    	    				    			};
    	    		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
    	    		              		$(this).dialog("close");
    	    		            	},
    	    		            	No: function() {
    	    		              		$(this).dialog("close");
    	    		            	}
    	    		          	});
    						}
    						else {
    							Atis.confirmDialog("Activar merito", "¿Desea activar este mérito preferente en la base de datos?", {
    	    		            	Si: function() {
    	    		            		var params = {
    	    				    				'a': '<%=ControladorMeritosPreferentes.ACCION_ACTIVAR%>', 
    	    				    				'<%= ControladorMeritosPreferentes.PARAM_ID %>': row.codNum
    	    				    			};
    	    		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
    	    		              		$(this).dialog("close");
    	    		            	},
    	    		            	No: function() {
    	    		              		$(this).dialog("close");
    	    		            	}
    	    		          	});	
    						}
		    			}
	        		}
				]}
			]
		});
	});
		
	document.getElementById("nuevo_meritopreferente").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorMeritosPreferentes.ACCION_NUEVO_MERITO %>'});
	});	
	
</script>

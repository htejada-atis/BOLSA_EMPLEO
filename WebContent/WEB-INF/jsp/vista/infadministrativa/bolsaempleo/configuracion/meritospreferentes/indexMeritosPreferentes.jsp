<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMeritosPreferentes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentes bean = (VistaMeritosPreferentes) uvdatos.getVistas().get(VistaMeritosPreferentes.class.getName());
%>

<div class="bolsa-empleo">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Tipos méritos preferentes</h2>
    
	    <a class="link-btn" id="nuevo_meritopreferente" href="<%= request.getRequestURI() %>">
	    	Nuevo tipo mérito preferente
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:10%" title="Código del mérito">Código</th>
			<th scope="col"	style="width:24%" title="Descripción del mérito">Descripción</th>
			<th scope="col"	style="width:20%">Tipo</th>
			<th scope="col"	style="width:15%">Aplicable</th>			
			<th scope="col"	style="width:10%">Factor</th>
			<th scope="col"	style="width:10%">Activo</th>
			<th scope="col"	style="width:11%"></th>
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
		    "defaultOrderBy": 0,
		    "defaultOrderDirection": 'asc',
		    "columns": [
		    	{'data': 'codigo'},
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
		        	case '<%= ModeloMeritosPreferentes.TIPO_POSESION %>':
		        		text = 'Por posesión';
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
		        	case '<%= ModeloMeritosPreferentes.APLICABLE_TOTAL %>':
		        		text = 'Al total';
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

<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatorias bean = (VistaConvocatorias) uvdatos.getVistas().get(VistaConvocatorias.class.getName());
%>

<div class="bolsa-empleo convocatorias">	
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Convocatorias</h2>
    
	    <button class="link-btn" id="nueva_convocatoria">
	    	Nueva convocatoria
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_convocatorias">
		<tr>
			<th scope="col" style="width:10%" title="Id de la convocatoria">Id</th>
			<th scope="col"	class="descripcion" style="width:40%">Descripción</th>
			<th scope="col"	style="width:20%">Fecha cierre</th>
			<th scope="col"	style="width:20%">Estado</th>			
			<th scope="col"	style="width:10%"></th>
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
		var table_titulaciones = new Atis.DataTable('#table_convocatorias', {
		    "ajax": { url: "<%=  ControladorConvocatorias.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
	    	"defaultOrderBy": 2,
		    "action": "<%= ControladorConvocatorias.ACCION_DATATABLE %>",
		    "columns": [
		    	{'data': 'codNum'},
		        {'data': 'descripcion'},
		        {'data': 'fechaCierre'},
		        {'data': 'estado'},
		        {'data': 'codNum', 'buttons': [
		        	{'label': 'Editar', 'onClick': function(row) {
			    			var params = {'a': '<%= ControladorConvocatorias.ACCION_MODIFICAR_CONVOCATORIA %>', '<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>': row.codNum};
		        			Atis.sendForm("<%=request.getRequestURI()%>", params);
			    		}
		        	}, 
		        	{'label': function(row) { 
			    			if(row.estado=="CERRADA"){
			    				return "Abrir"; 
			    			}
			    			else{
			    				return "Cerrar"; 
			    			}
			    		},
			    	'onClick': function(row) {
				    		if(row.estado=="CERRADA") {
				    			Atis.confirmDialog("Cambio de estado", "¿Desea abrir la convocatoria? Recuerde revisar áreas a baremar, titulaciones, etc ...", {
					            	Si: function() {
					            		var params = {
					            				'<%=ControladorConvocatorias.PARAM_ACCION%>': '<%=ControladorConvocatorias.ACCION_ABRIR_CONVOCATORIA%>', 
					            				'<%=ControladorConvocatorias.PARAM_CONVOCATORIA_ID%>': row.codNum
					            			};
					        			Atis.sendForm("<%= request.getRequestURI() %>", params);
					              		$(this).dialog("close");
					            	},
					            	No: function() {
					              		$(this).dialog("close");
					            	}
					          	});
				    		} else {
				    			Atis.confirmDialog("Cambio de estado", "¿Desea cerrar la convocatoria?<br/><br/>Los candidatos no podrán confirmar solicitudes abiertas,<br/>ni añadir o quitar titulaciones y acreditaciones.", {
					            	Si: function() {
					            		var params = {
					            				'<%=ControladorConvocatorias.PARAM_ACCION%>': '<%=ControladorConvocatorias.ACCION_CERRAR_CONVOCATORIA%>',
					            				'<%=ControladorConvocatorias.PARAM_CONVOCATORIA_ID%>': row.codNum
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
		        	},
		        	{'label': 'Borrar', 'onClick': function(row) {
		        		Atis.confirmDialog("¿Desea borrar la convocatoria?", "Borrado de convocatoria", {
			            	Si: function() {
			            		var params = {
			            				'<%=ControladorConvocatorias.PARAM_ACCION%>': '<%=ControladorConvocatorias.ACCION_BORRAR_CONVOCATORIA%>',
			            				'<%=ControladorConvocatorias.PARAM_CONVOCATORIA_ID%>': row.codNum
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
				]}
			]
		});
	});
		
	document.getElementById("nueva_convocatoria").addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorConvocatorias.ACCION_FORMULARIO_CONVOCATORIA %>'});
	});	
	
</script>

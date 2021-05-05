<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorFiltrar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFiltrar" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion" %>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFiltrar bean = (VistaFiltrar)uvdatos.getVistas().get(VistaFiltrar.class.getName());
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>

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
	
	<h2>Filtrado de candidatos por titulación</h2>
	<p>Filtrado de candidatos por titulaciones, para su posible exclusión.</p>
	
	<table class="bluetable bolsaempleo" id="tableCandidatos">
		<tr>
			<th scope="col" style="width:60%">Candidato</th>
			<th scope="col" style="width:15%" title="Código área">Nº Titulaciones</th>
			<th scope="col" style="width:15%" class="area">Nº Tit. Validadas</th>			
			<th scope="col" style="width:10%"></th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<% if (candidato != null) { %>
		<table class="bluetable bolsaempleo" id="tableTitulacionesCandidato">
			<tr>
				<th scope="col" style="width:10%"></th>
				<th scope="col" style="width:60%">Titulación</th>		
				<th scope="col" style="width:40%">Descripción</th>		
				<th scope="col" style="width:15%"></th>		
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="5" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	<% } %>
	
</div>
	
<script>
	$(document).ready(function() {
		var tableCandidatos = new Atis.DataTable('#tableCandidatos', {
		    "ajax": { url: "<%= ControladorFiltrar.URL_PATTERN_AJAX %>", async: false },
		    "action": "<%= ControladorFiltrar.ACCION_DATATABLE_CANDIDATOS %>",
		    "pageSize": 5,
		    "filterable": true,
		    "title": 'Candidatos',
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorFiltrar.ACCION_CANDIDATO_SELECCIONADO %>',
	    				'<%= ControladorFiltrar.PARAM_CANDIDATO %>': row.codNum
		    	};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    <% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
		    "columns": [
		    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
	        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
	        	}},
	        	{'data': 'totalTitulaciones', 'order': {'active': false}},
	        	{'data': 'totalTitulacionesValidadas', 'order': {'active': false}},
		        {'data': 'codnum', 'buttons': [{'label': 'Excluir', 'onClick': function(row) {}}]}
		    ],
		});
		
		<% if (candidato != null) { %>
			var titulacionesValidadas = [];
			
			<%  if (bean.getValidadas() != null) {
				for (Titulacion titulacion: bean.getValidadas()) { %>
					titulacionesValidadas.push(<%=titulacion.getCodNum()%>);
				<% }
			} %>
		
			var tableTitulaciones = new Atis.DataTable('#tableTitulacionesCandidato', {
				"ajax": { url: "<%=ControladorFiltrar.URL_PATTERN_AJAX%>", async: false },
				"selectable": true,
			    "filterable": true,
			    "title": 'Titulaciones: <%= candidato.getNombre() %>',
			    "pageSize": 10,
			    "action": "<%=ControladorFiltrar.ACCION_DATATABLE_TITULACIONES_CANDIDATO%>",
			    "params": {"<%= ControladorFiltrar.PARAM_CANDIDATO %>": <%= candidato.getCodNum() %>},
			    "selected": titulacionesValidadas,
			    "columns": [
			    	{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '<%= ControladorFiltrar.ACCION_TITULACION_SELECCIONADA %>'
			    						: '<%= ControladorFiltrar.ACCION_TITULACION_DESELECCIONADA %>';
				    			var params = {
				    				'a': accion,
				    				'<%= ControladorFiltrar.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
				    				'<%= ControladorFiltrar.PARAM_TITULACION %>': row.codNum,
				    			};
				    			
				    			Atis.sendAjax("<%= request.getRequestURI() %>", params, function(data) {
				    				tableCandidatos.refresh();
				    			}, function(data) {
				    				var response = JSON.parse(data.responseText);
				    				
				    				// alert en caso de error
				    				Atis.alertDialog("Error en la solicitud", response.descripcion);
				    				
				    				checkbox.checked = !checkbox.checked;
				    				checkbox.checked ? $(checkbox).parent().parent().addClass("selected") : $(checkbox).parent().parent().removeClass("selected");
				    			});
				    		}
			    		}
			    	},
			        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
			    	{'data': 'descripcion'},
		        	{'data': 'codnum', 'buttons': [
		        		{'label': 'Descargar', 'onClick': function(row) {
		        			window.open("<%= ControladorFiltrar.URL_PATTERN_FILES_PRIVADA %>"
		        		        	+ "?a=<%= ControladorFiltrar.ACCION_DESCARGAR_FICHERO %>&<%= ControladorFiltrar.PARAM_FICHERO %>=" + row.codNum);
		        		}},
		   			]}	
			    ],
			});
		<% } %>
		
	}); 
</script>
	
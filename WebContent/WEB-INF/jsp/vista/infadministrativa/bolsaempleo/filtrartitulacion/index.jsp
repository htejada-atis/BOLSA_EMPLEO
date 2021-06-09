<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.stream.Collectors" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorFiltrarTitulacion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrar" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFiltrar bean = (VistaFiltrar)uvdatos.getVistas().get(VistaFiltrar.class.getName());
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
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
		    "ajax": { url: "<%=ControladorFiltrarTitulacion.URL_PATTERN_AJAX%>", async: false },
		    "action": "<%=ControladorFiltrarTitulacion.ACCION_DATATABLE_CANDIDATOS%>",
		    "pageSize": 5,
		    "filterable": true,
		    "title": 'CANDIDATOS',
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'<%=ControladorFiltrarTitulacion.PARAM_ACCION %>': '<%=ControladorFiltrarTitulacion.ACCION_CANDIDATO_SELECCIONADO%>',
	    				'<%=ControladorFiltrarTitulacion.PARAM_CANDIDATO%>': row.codNum
		    	};
        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    }},
		    <%if (candidato != null) {%> "selected": <%= candidato.getCodNum() %> ,<%}%>
		    "columns": [
		    	{'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
	        		return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
	        	}},
	        	{'data': 'totalTitulaciones', 'order': {'active': false}},
	        	{'data': 'totalTitulacionesValidadas', 'order': {'active': false}},
		        {'data': 'codnum', 'buttons': [{'label': 'Excluir', 'onClick': function(row) { console.log(row); }}]}
		    ],
		});
		
		<%if (candidato != null) {%>
			var titulacionesValidadas = [<%= bean.getValidadas() != null ? bean.getValidadas().stream().map(t -> t.getCodNum().toString()).collect(Collectors.joining(",")) : "" %>];
			
			var changeTitulacion = function(row) {
				var message = 
        			'<h3>Titulación</h3><br/>' +
        			'<p><select style="max-width:500px"><%= 
        				bean.getTitulaciones().stream()
        				.map(t -> "<option value=\"" + t.getCodNum() + "\" '+ (row.titulacion ? (row.titulacion.codNum == " + t.getCodNum() + " ? 'selected' : '') : '') +'>" + EscapaHTML.escapa(t.getNombre()) + "</option>")
        				.collect(Collectors.joining("")) %></select></p>';
        		
        		Atis.alertDialog("Seleccionar titulación", $(message), function(dialog) {
    				var option = $('select option:selected', dialog);
    				
    				var params = {
	    				'<%=ControladorFiltrarTitulacion.PARAM_ACCION%>': '<%=ControladorFiltrarTitulacion.ACCION_CAMBIAR_TITULACION%>',
	    				'<%=ControladorFiltrarTitulacion.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
	    				'<%=ControladorFiltrarTitulacion.PARAM_TITULACION_USUARIO%>': row.codNum,
	    				'<%=ControladorFiltrarTitulacion.PARAM_TITULACION%>': $(option).val(),		    				
	    			};
	    			
	    			Atis.sendAjax("<%=request.getRequestURI()%>", params, function(data) {
	    				tableCandidatos.refresh();
	    				tableTitulaciones.refresh();
	    				
	    				$(dialog).dialog("close");
	    			}, function(data) {
	    				var response = JSON.parse(data.responseText);
	    				
	    				// alert en caso de error
	    				Atis.alertDialog("Error en la solicitud", response.descripcion);
	    			});
    			});
			};
		
			
			var tableTitulaciones = new Atis.DataTable('#tableTitulacionesCandidato', {
				"ajax": { url: "<%=ControladorFiltrarTitulacion.URL_PATTERN_AJAX%>", async: false },
				"selectable": {'all': false},
			    "filterable": true,
			    "title": 'TITULACIONES: <%=candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido()%>',
			    "pageSize": 10,
			    "action": "<%=ControladorFiltrarTitulacion.ACCION_DATATABLE_TITULACIONES_CANDIDATO%>",
			    "params": {"<%=ControladorFiltrarTitulacion.PARAM_CANDIDATO%>": <%=candidato.getCodNum()%>},
			    "selected": titulacionesValidadas,
			    "columns": [
			    	{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '<%=ControladorFiltrarTitulacion.ACCION_TITULACION_SELECCIONADA%>'
			    						: '<%=ControladorFiltrarTitulacion.ACCION_TITULACION_DESELECCIONADA%>';
				    			
			    				var params = {
				    				'<%=ControladorFiltrarTitulacion.PARAM_ACCION%>': accion,
				    				'<%=ControladorFiltrarTitulacion.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
				    				'<%=ControladorFiltrarTitulacion.PARAM_TITULACION_USUARIO%>': row.codNum,
				    			};
				    			
				    			Atis.sendAjax("<%=request.getRequestURI()%>", params, function(data) {
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
			        {'data': 'titulacion.nombre', 'filter': {'type': 'text'}, 'render': function(row) {
			        	return !row.titulacion ? 'OTRA TITULACION' : row.titulacion.nombre;
			        }},
			    	{'data': 'descripcion', 'overflow': 'auto'},
		        	{'data': 'codnum', 'buttons': [
		        		{'label': 'Descargar', 'onClick': function(row) {
		        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_TITULACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_TITULACION %>=" + row.codNum);
		        		}},
		        		{'label': 'Editar', 'onClick': changeTitulacion},
		   			]}	
			    ],
			});
		<% } %>
		
	}); 
</script>
	

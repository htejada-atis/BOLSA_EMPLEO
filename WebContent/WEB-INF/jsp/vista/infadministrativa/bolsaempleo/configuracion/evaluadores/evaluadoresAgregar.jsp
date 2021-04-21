<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEvaluadores bean = (VistaEvaluadores) uvdatos.getVistas().get(VistaEvaluadores.class.getName());
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
	
	<h2>Evaluadores de un área</h2>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<caption class="table-title">Agregar nuevo evaluador al Área <%= bean.getArea().getDescripcion() %></caption>  
		<tr>
			<th scope="col" style="width:10%"></th>
			<th scope="col" style="width:25%">D.N.I</th>
			<th scope="col"	style="width:65%">Nombre</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>

<script>

	$(document).ready(function() {
		
		var table_usuarios = new Atis.DataTable('#table_usuarios', {
		    "ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>" },
		    "params": {"<%=ControladorGestionEvaluadores.PARAM_AREA%>": "<%= bean.getArea().getCodNum() %>"},
		    "pageSize": 10,
		    "title": 'Agregar Nuevo Evaluador al Área: <%= bean.getArea().getDescripcion() %>',
		    "action": "<%=ControladorGestionEvaluadores.ACCION_DATATABLE_USUARIOS%>",
		    "selectable": true,
		    "filterable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'numdocumento', 'filter': true},
		    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
	        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
	        	}},
		    ],
		    "actions": [
		    	{'label': 'Agregar', 'title': 'Agregar usuario como evaluador en este área', 'onClick': function(selected) {
			    		if(selected.length) {
			    			var params = {
				    				'a': '<%= ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES %>', 
				    				'<%= ControladorGestionEvaluadores.PARAM_USUARIOS %>': JSON.stringify(selected),
				    				'<%= ControladorGestionEvaluadores.PARAM_AREA %>': '<%= bean.getArea().getCodNum() %>'};
			        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    		}
			    	}
		    	},
		    ]
		});
		
	});
	
</script>

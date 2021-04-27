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
			<th scope="col" style="width:10%" title="Id del tipo de mérito preferente">Id</th>
			<th scope="col"	style="width:40%" title="Descripción del mérito">Descripción</th>
			<th scope="col"	style="width:20%">Tipo</th>
			<th scope="col"	style="width:20%">Aplicable</th>			
			<th scope="col"	style="width:10%"></th>
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
		var table = new Atis.DataTable('#table', {
		    "ajax": { url: "<%=  ControladorMeritosPreferentes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%= ControladorMeritosPreferentes.ACCION_DATATABLE %>",
		    "columns": [
		    	{'data': 'codNum'},
		        {'data': 'descripcion'},
		        {'data': 'tipo', 'render': function(row) {
		        	var tiposLabel = {
		        		'<%= ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE %>': 'Titulación preferente',
		        		'<%= ModeloMeritosPreferentes.TIPO_MERITO %>': 'Mérito'
		        	};
		        			        	
		        	return tiposLabel[row.tipo];
		        }},
		        {'data': 'aplicable', 'render': function(row) {
		        	var aplicableTipos = {
		        		'<%= ModeloMeritosPreferentes.APLICABLE_BLOQUE %>': 'Al apartado',
		        		'<%= ModeloMeritosPreferentes.APLICABLE_APARTADO %>': 'Al bloque',
		        		'<%= ModeloMeritosPreferentes.APLICABLE_ITEM %>': 'Al mérito',
		        	};
		        	
		        	return aplicableTipos[row.aplicable];
		        }},
		        {'data': 'codNum', 'buttons': [
		        	{'label': 'Editar', 'onClick': function(row) {
			    			var params = {
			    				'a': '<%=ControladorMeritosPreferentes.ACCION_MODIFICAR%>', 
			    				'<%= ControladorMeritosPreferentes.PARAM_ID %>': row.codNum
			    			};
		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
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

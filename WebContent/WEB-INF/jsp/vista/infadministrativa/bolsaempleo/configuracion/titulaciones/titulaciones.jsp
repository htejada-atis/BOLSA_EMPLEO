<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>

<div class="bolsa-empleo">

	<%
		if (session.getAttribute(ControladorGestionTitulaciones.MENSAJE_ENVIADO) != null) {
		%>
		<div id="exito" class="success">
			<%=session.getAttribute(ControladorGestionTitulaciones.MENSAJE_ENVIADO)%>
		</div>
	<%
			session.removeAttribute(ControladorGestionTitulaciones.MENSAJE_ENVIADO);
		}
	%>
	
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
		<h2>Titulaciones</h2>
    
	    <a class="link-btn" id="nueva_titulacion" href="<%= request.getRequestURI() %>">
	    	 Nueva titulación
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_titulaciones">
		<tr>
			<th scope="col" style="width:10%" title="Id de la titulación">Id</th>
			<th scope="col"	style="width:80%">Nombre</th>
			<th scope="col" style="width:10%"></th>
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
		
		var table_titulaciones = new DataTable('#table_titulaciones', {
		    "ajax": { url: "<%=ControladorGestionTitulaciones.URL_PATTERN_AJAX%>" },
		    "pageSize": 10,
		    "action": "<%=ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES%>",
		    "columns": [
		    	{'data': 'codNum'},
		        {'data': 'nombre', 'class': 'overflow-auto'},
		        {'data': 'codnum', 'buttons': [{'label': 'Editar', 'title': 'Editar titulación', 'onClick': function(row) {
		        	var params = {
		    				'a': '<%=ControladorGestionTitulaciones.ACCION_EDITAR_TITULACION %>', 
		    				'<%=ControladorGestionTitulaciones.PARAM_ID%>': row.codNum};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		        	}
		        }, {'label': 'Borrar', 'title': 'Borrar titulación', 'onClick': function(row) {
		        	Atis.confirmDialog("Borrar titulación", "¿Desea borrar la titulación seleccionada?", {
				        Si: function() {
				        	var params = {
				    				'a': '<%=ControladorGestionTitulaciones.ACCION_BORRAR_TITULACION %>', 
				    				'<%=ControladorGestionTitulaciones.PARAM_ID%>': row.codNum};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				          	$(this).dialog("close");
				        },
				        No: function() {
				          	$(this).dialog("close");
				        }
				      });
		        	
		        } }]}		        
		    ],
		});
		
		document.getElementById("nueva_titulacion").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION %>'});
		});
		
	});
	
</script>

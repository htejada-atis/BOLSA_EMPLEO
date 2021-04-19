<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatorias bean = (VistaConvocatorias) uvdatos.getVistas().get(VistaConvocatorias.class.getName());
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
		<h2>Convocatorias</h2>
    
	    <a class="link-btn" id="nueva_convocatoria" href="<%= request.getRequestURI() %>">
	    	Nueva convocatoria
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_convocatorias">
		<tr>
			<th scope="col" style="width:10%" title="Id de la convocatoria">Id</th>
			<th scope="col"	style="width:50%">Descripción</th>
			<th scope="col"	style="width:20%">Fecha cierre</th>
			<th scope="col"	style="width:20%">Estado</th>			
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
		var table_titulaciones = new DataTable('#table_convocatorias', {
		    "ajax": { url: "<%=  ControladorConvocatorias.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%= ControladorConvocatorias.ACCION_DATATABLE %>",
		    "columns": [
		    	{'data': 'codNum'},
		        {'data': 'descripcion'},
		        {'data': 'fechaCierre'},
		        {'data': 'estado'},
		    ],
		});
		
		document.getElementById("nueva_convocatoria").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorConvocatorias.ACCION_FORMULARIO_CONVOCATORIA %>'});
		});
	});
	
</script>
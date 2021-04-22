<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
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
	
	<h2>Paso 1: Selección de bolsas de empleo</h2>
	<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
	<p>Seleccione las bolsas donde desee participar, hasta un máximo de [X].</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col"	style="width:15%">Código</th>
			<th scope="col"	style="width:60%">Nombre</th>
			<th scope="col" class="center" style="width:10%">Excluido</th>
			<th scope="col" class="center" style="width:10%">Baremable</th>
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
		var table = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "selectable": true,
		    "filterable": true,
		    "action": "<%=ControladorMisSolicitudes.ACCION_DATATABLE_AREAS%>",
		    "columns": [
		    	{'data': 'codnum', 'selectable': true},
		    	{'data': 'codigo', 'filter': true},
		    	{'data': 'nombre', 'filter': true},
		        {'data': 'excluido', 'order': {'active': false}, 'filter': {'type': 'selectBoolean', 'true': 'Excluido', 'false': 'No excluido'}, 'render': function(row) {
	        		if (row.excluido) {
	        			return "<div title='No tienes acceso a ésta bolsa' class='circle-false'></div>";
	        		} else {
	        			return "<div class='circle-true'></div>";
	        		}
	        	}},
		        {'data': 'baremable', 'order': {'active': false}, 'filter': {'type': 'selectBoolean', 'true': 'Baremable', 'false': 'No baremable'}, 'render': function(row) {
	        		if (row.baremable) {
	        			return "<div title='Baremable' class='circle-true'></div>"; 
	        		} else {
	        			return "<div title='No baremable' class='circle-false'></div>"; 
	        		}
	        	}},
	        ],
		});
	});
	
</script>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());
%>

<div class='bolsas'>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else { %>
	
	<h2>Items para las baremaciones</h2>
	
	<table class="bluetable bolsaempleo" id="tableApartadosGenerales">
		<caption>APARTADOS GENERALES</caption>
		<tr>
			<th scope="col" style="width:15%" title="Código apartado">Código</th>
			<th scope="col" style="width:70%" title="Código de area">Nombre del apartado</th>
			<th scope="col" style="width:15%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableBloques">
		<caption>BLOQUES</caption>
		<tr>
			<th scope="col" style="width:15%" title="Código apartado">Código</th>
			<th scope="col" style="width:70%" title="Código de area">Nombre del apartado</th>
			<th scope="col" style="width:15%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>
	
<script>
$(document).ready(function() {
	var tableApartados = new DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion", method: "POST" },
	    "pageSize": 10,
	    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_APARTADOS%>",
	    "columns": [
	    	{'data': 'codigo'},
	        {'data': 'nombre'},
	        {'data': 'activo'},
	    ]
	});
	
	var tableBloques = new DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/itemsbaremacion" },
	    "pageSize": 10,
	    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES%>",
	    "columns": [
	    	{'data': 'codigo'},
	        {'data': 'nombre'},
	        {'data': 'activo'},
	    ]
	});
}); 
</script>
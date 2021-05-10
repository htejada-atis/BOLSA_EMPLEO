<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidarNoAfines bean = (VistaValidarNoAfines)uvdatos.getVistas().get(VistaValidarNoAfines.class.getName());
Bolsa bolsa = bean.getBolsa();
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = "Última convocatoria: " + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
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
	<% } %>
	
	<h2>Validar meritos no sujetos a afinidad</h2>
	<h3><%= descripcion %></h3>
	<h4><%= bolsa.getArea().getDescripcion() %></h4>
	
	<table class="bluetable bolsaempleo" id="tableCandidatos">
		<tr>
			<th scope="col" style="width:10%">D.N.I</th>
			<th scope="col" style="width:50%" class="nombre">Nombre</th>
			<th scope="col" style="width:10%" class="center">No validados</th>
			<th scope="col" style="width:10%" class="center">Validados</th>
			<th scope="col" style="width:10%" class="center">Excluidos</th>
			<th scope="col" style="width:10%" class="center">Total</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableMeritos" style="visibility: hidden">
		<tr>
			<th scope="col" style="width:10%"></th>
			<th scope="col" style="width:50%">Id</th>
			<th scope="col" style="width:10%">Estado</th>
			<th scope="col" style="width:10%">Código</th>
			<th scope="col" style="width:10%">Valor</th>
			<th scope="col" style="width:10%">Fichero</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {
	
	var tableCandidatos = new Atis.DataTable('#tableCandidatos', {
	    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>" },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS %>",
	    "params": {"<%=ControladorValidarNoAfines.PARAM_BOLSA%>": "<%= bolsa.getCodNum() %>"},
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorValidarNoAfines.ACCION_CANDIDATO_SELECCIONADO %>', 
    				'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    <% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
	    "columns": [
	    	{'data': 'numdocumento', 'filter': true},
	    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
        	}},
	        {'data': 'totalMeritosNoValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
	        {'data': 'totalMeritosValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
	        {'data': 'totalMeritosExcluidos', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
	        {'data': 'totalMeritos', 'order': false, 'class': 'center'}
	    ]
	});
	
	<% if (candidato != null) { %>
		document.getElementById("tableMeritos").style.visibility = "visible";
	
		var tableMeritos = new Atis.DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "filterable": true,
		    "selectable": true,
		    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS %>",
		    "params": {
		    	"<%=ControladorValidarNoAfines.PARAM_BOLSA%>": "<%= bolsa.getCodNum() %>",
		    	"<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>": "<%= candidato.getCodNum() %>"
		    	},
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorValidarNoAfines.ACCION_BOLSA_SELECCIONADA %>', 
	    				'<%= ControladorValidarNoAfines.PARAM_BOLSA %>': row.codNum};
	    		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		        {'data': 'codNum', 'order': false, 'class': 'center', 'render': function(row) { return ""; } },
		        {'data': 'item', 'filter': true, 'render': function(row) {
		        	return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
	        	}},
	        	{'data': 'valor', 'filter': true, 'overflow': 'auto'},
	        	{'data': 'codnum', 'buttons': [
	        		{'label': 'Descargar', 'title': 'Descargar fichero del mérito', 'onClick': function(row) {
	        			window.open("<%= request.getRequestURI() %>"
	        		        	+ "?a=<%= ControladorValidarNoAfines.ACCION_DESCARGAR_FICHERO %>&<%= ControladorValidarNoAfines.PARAM_MERITO %>=" + row.codNum);
	        		}},
	   			]}
		    ]
		});
	<% } %>
	
}); 
</script>

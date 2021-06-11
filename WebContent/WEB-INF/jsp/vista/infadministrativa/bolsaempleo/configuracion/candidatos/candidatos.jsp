<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class='bolsa-empleo usuarios'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Usuarios Candidatos</h2>
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<tr>
			<th scope="col" style="width:13%" title="Documento">Documento</th>
			<th scope="col" style="width:14%" title="Nombre de usuario" class="user">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:10%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%" class="center" title="Excluido">Excluido</th>
			<th scope="col" style="width:10%" class="center" title="Eliminado">Eliminado</th>
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
	
	var table_usuarios = new Atis.DataTable('#table_usuarios', {
	    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
	    "pageSize": 10,
	    "filterable": true,
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO %>', 
    				'<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    "action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS_CANDIDATOS%>",
	    "columns": [
	        {'data': 'prsnif', 'filter': true, 'overflow': 'auto'},
	        {'data': 'codcuenta', 'filter': true, 'overflow': 'auto'},
	        {'data': 'apellido1', 'filter': false, 'order': false, 'overflow': 'auto', 'render': function(row) {
        		return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
        	}},
	        {'data': 'listaDist', 'filter': {'type': 'selectBoolean', 'true': 'En Lista', 'false': 'Sin Lista'}, 'render': function(row) {
	        	if(row.listaDist==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		} else {
        			return "<div title='Excluido de lista distribución' class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'excluido', 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'Incluido'}, 'optionDefault': 'false'}, 'render': function(row) {
	        	if(row.excluido==true){
        			return "<div title='Candidato excluido' class='circle-true'></div>"; 
        		}
        	}},
        	{'data': 'borrado', 'filter': {'type': 'select', 'options':{'true': 'Borrado', 'false': 'No Borrado'}, 'optionDefault': 'false'}, 'render': function(row) {
	        	if(row.borrado==true){
        			return "<div title='Candidato eliminado' class='circle-true'></div>"; 
        		}
        	}},
	    ],
	});

}); 
</script>

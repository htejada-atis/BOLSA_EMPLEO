<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class='bolsa-empleo usuarios'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Usuarios del sistema</h2>
	
	<div class="form-group-container col2">
		<div></div>
		<div class="form-group-container-offset col2 gap-5"  id="nuevo_usuario_cont">   
	   		<div class="form-group" style="width:80%">
				<label for="nombre_usuario">Cuenta TIC usuario <i class="tooltip">(?)<span>Introduzca la cuenta TIC sin @ujaen.es</span></i></label>
	   			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>" id="nombre_usuario" value=""/>
	   		</div>
	   		<div class="form-group" style="width:20%;">
	   			<label style="visibility: hidden">.</label>
				<button class="link-btn" id="nuevo_usuario" title="Añadir usuario">
					Buscar
	  	 		</button>
	  		</div>
		</div>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<tr>
			<th scope="col" style="width:15%">Documento</th>
			<th scope="col" style="width:14%" class="user">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:20%">Rol</th>
			<th scope="col" style="width:15%">Excluido</th>
			<th scope="col" style="width:15%" class="center">Eliminado</th>
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
	
	document.getElementById('nuevo_usuario').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {
			'<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>': '<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>',
			'<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': document.getElementById("nombre_usuario").value
		});
	});
	
	var table_usuarios = new Atis.DataTable('#table_usuarios', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "pageSize": 10,
	    "filterable": true,
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_SELECCIONAR_USUARIO %>',
    				'<%= ControladorUsuarioBolsaEmpleo.PARAM_USUARIO %>': row.codNum};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS%>",
	    "columns": [
	    	{'data': 'prsnif', 'filter': true, 'overflow': 'auto'},
	        {'data': 'codcuenta', 'filter': true, 'overflow': 'auto'},
	        {'data': 'apellido1', 'filter': false, 'order': false, 'overflow': 'auto', 'render': function(row) {
	        	return row.nombre + " " + row.apellido1 + " " + row.apellido2;         		
	        }},
	        {'data': 'rol.descripcion', 'filter': {'type': 'select', 'options': {'1050':'Personal', '1051':'Comision', '1053': 'Director departamento'}}  , 'order': {'active': false}},
	        {'data': 'excluido', 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'Incluido'}, 'optionDefault': 'false'}, 'render': function(row) {
	        	if(row.excluido==true){
        			return "<div title='Usuario excluido' class='circle-true'></div>"; 
        		}
        	}},
	        {'data': 'borrado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Borrado', 'false': 'No Borrado'}, 'optionDefault': 'false'},
	    		'render': function(row) {
	        		if (row.borrado) {
	        			return "<div title='Eliminado' class='circle-true'></div>";
	        		}
        		}
	        },
	    ]
	});

}); 
</script>

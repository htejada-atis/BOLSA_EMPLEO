<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class='bolsa-empleo usuarios'>
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
	
	<div class="titulo-bolsa-empleo" style="float:right; margin-top:1.2rem">
		<a class="link-btn" id="nuevo_usuario" href="<%= request.getRequestURI() %>" style="margin-top:0">Nuevo usuario</a>
	</div>
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Usuarios del sistema</h2>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
	  	<caption class="table-title">Usuarios</caption>  
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:12%" title="N Documento">Documento</th>
			<th scope="col" style="width:12%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:35%" title="Email del usuario">Email</th>
			<th scope="col" style="width:20%" title="Rol del usuario" class="rol">Rol</th>
			<th scope="col" style="width:5%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="10" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="table_usuarios_borrados" style="margin-top:2rem">
		<caption class="table-title">Usuarios Borrados</caption>  
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:12%" title="N Documento">Documento</th>
			<th scope="col" style="width:12%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:35%" title="Email del usuario">Email</th>
			<th scope="col" style="width:20%" title="Rol del usuario" class="rol">Rol</th>
			<th scope="col" style="width:5%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="10" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>

	<table class="bluetable bolsaempleo" id="table_usuarios_excluidos" style="margin-top:2rem">
		<caption class="table-title">Usuarios Excluidos de la bolsa de empleo</caption>  
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:12%" title="N Documento">Documento</th>
			<th scope="col" style="width:12%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:35%" title="Email del usuario">Email</th>
			<th scope="col" style="width:20%" title="Rol del usuario" class="rol">Rol</th>
			<th scope="col" style="width:10%" title="Razon exclusion del usuario">Razon Exclusion</th>
			<th scope="col" style="width:5%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="15" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>
	
<script>
$(document).ready(function() {
	document.getElementById('nuevo_usuario').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_FORMULARIO_USUARIO %>'});
	});
	
	
	var table_usuarios = new Atis.DataTable('#table_usuarios', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "filterable": true,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'codcuenta'},
	        {'data': 'apellido1', 'render': function(row) {
        		return row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2; 
        	}},
	        {'data': 'email'},
	        {'data': 'rol.descripcion', 'filter': {'type': 'select', 'options': ['1050', '1051', '1052']} , 'order': {'active': false}},
	        {'data': 'listaDist', 'render': function(row) {
	        	if(row.listaDist==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		}
        		else{
        			return "<div title='Excluido de lista distribución' class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', '<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}},
        	{'label': 'Excluir', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>', '<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}]}	
	    ],
	    "actions": [
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO%>", selected); } }   	
	    ]
	});
	
	var table_usuarios_borrados = new Atis.DataTable('#table_usuarios_borrados', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "filterable": true,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_BORRADOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'codcuenta'},
	        {'data': 'apellido1', 'render': function(row) {
        		return row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2; 
        	}},
	        {'data': 'email'},
	        {'data': 'rol.descripcion', 'filter': {'type': 'select', 'options': ['1050', '1051', '1052']} , 'order': {'active': false}},
	        {'data': 'listaDist', 'render': function(row) {
	        	if(row.listaDist==true){
        			return "<div class='circle-true'></div>"; 
        		}
        		else{
        			return "<div class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', '<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}]}	
	    ],
	    "actions": [
	    	{'label': 'Recuperar', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_RECUPERAR_USUARIO%>", selected); } }   	
	    ]
	});
	
	
	var table_usuarios_excluidos = new Atis.DataTable('#table_usuarios_excluidos', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "filterable": true,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_EXCLUIDOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'codcuenta'},
	        {'data': 'apellido1', 'render': function(row) {
        		return row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2; 
        	}},
	        {'data': 'email'},
	        {'data': 'rol.descripcion', 'filter': {'type': 'select', 'options': ['1050':'Personal', '1051':'Comision', '1052':'Candidato']} , 'order': {'active': false}},
	        {'data': 'razonExclusion'},
	        {'data': 'listaDist', 'render': function(row) {
        		if(row.listaDist==true){
        			return "<div class='circle-true'></div>"; 
        		}
        		else{
        			return "<div class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', '<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}]}	
	    ],
	    "actions": [
	    	{'label': 'Incluir', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_INCLUIR_USUARIO%>", selected); } }
	   
	    ]
	});
	
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de los usuarios', 'Seleccione al menos un usuario.');
			return;
		}
		console.log(accion,selected);
		
		switch(accion){
			case('eliminarusuario'):
        		var mensaje = "¿Desea borrar los usuarios seleccionados?";
    			var titulo = "Borrar Usuarios";
			break;
			case('excluirusuario'):
        		var mensaje = "¿Desea excluir los usuarios seleccionados?";
    			var titulo = "Excluir Usuarios";
			case('incluirusuario'):
        		var mensaje = "¿Desea incluir los usuarios seleccionados?";
    			var titulo = "Incluir Usuarios";
			case('recuperarusuario'):
        		var mensaje = "¿Desea recuperar los usuarios seleccionados?";
    			var titulo = "Recuperar Usuarios";
			break;
		}
		Atis.confirmDialog(titulo, mensaje, {
        	Si: function() {
        		var params = {
        				'a': '<%=ControladorUsuarioBolsaEmpleo.ACCION_USUARIO%>', 
        				'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO%>': accion, 
        				'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIOS_SELECCIONADOS%>': Atis.object2Json(selected)
        			};
    			Atis.sendForm("<%= request.getRequestURI() %>", params);
          		$(this).dialog("close");
        	},
        	No: function() {
          		$(this).dialog("close");
        	}
      	});
	}

}); 
</script>

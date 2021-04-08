<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class='bolsa-empleo'>
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
	
		<div class="titulo-bolsa-empleo">
		<h2>Usuarios</h2>
    
	    <a class="link-btn" id="nuevo_usuario" href="<%= request.getRequestURI() %>">
	    	 Buscar usuario
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:10%" title="Nº Documento">Documento</th>
			<th scope="col" style="width:10%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:25%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:15%" title="Email del usuario">Email</th>
			<th scope="col" style="width:20%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
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
	
		<h2>Usuarios Borrados</h2>
		<table class="bluetable bolsaempleo" id="table_usuarios_borrados">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:10%" title="Nº Documento">Documento</th>
			<th scope="col" style="width:10%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:20%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:20%" title="Email del usuario">Email</th>
			<th scope="col" style="width:20%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
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
	
		<h2>Usuarios Excluidos</h2>
		<table class="bluetable bolsaempleo" id="table_usuarios_excluidos">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:10%" title="Nº Documento">Documento</th>
			<th scope="col" style="width:9%" title="Nombre de usuario">Usuario</th>
			<th scope="col" style="width:24%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:14%" title="Email del usuario">Email</th>
			<th scope="col" style="width:17%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:10%" title="Razon exclusion del usuario">Razon Exclusion</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
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
	
	
	var table_usuarios = new DataTable('#table_usuarios', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'apellido1'},
	        {'data': 'nombre'},
	        {'data': 'email'},
	        {'data': 'rol.descripcion'},
	        {'data': 'listaDist', 'render': function(row) {
        		if(row.listaDist==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', 'nombreusuario': row.admusuidentificador};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}, 
        	{'label': function(row) { return "Borrar"; }, 'onClick': function(row) {
        		var mensaje = "¿Desea borrar el usuario seleccionado?";
        		var titulo = "Borrar Usuario";

        		Atis.confirmDialog(titulo, mensaje, {
		        	Si: function() {
		        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO %>',
		        			'id': row.codNum};
	        			Atis.sendForm("<%= request.getRequestURI() %>", params);
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
        	
        	}}]}	
	    ]
	});
	
	var table_usuarios_borrados = new DataTable('#table_usuarios_borrados', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_BORRADOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'apellido1'},
	        {'data': 'nombre'},
	        {'data': 'email'},
	        {'data': 'rol.descripcion'},
	        {'data': 'listaDist', 'render': function(row) {
        		if(row.listaDist==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', 'nombreusuario': row.admusuidentificador};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}, 
        	{'label': function(row) { return "Borrar"; }, 'onClick': function(row) {
        		var mensaje = "¿Desea borrar el usuario seleccionado?";
        		var titulo = "Borrar Usuario";

        		Atis.confirmDialog(titulo, mensaje, {
		        	Si: function() {
		        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO %>',
		        			'id': row.codNum};
	        			Atis.sendForm("<%= request.getRequestURI() %>", params);
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
        	
        	}}]}	
	    ]
	});
	
	
	var table_usuarios_excluidos = new DataTable('#table_usuarios_excluidos', {
	    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 5,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_EXCLUIDOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'tipodocumento'},
	        {'data': 'numdocumento'},
	        {'data': 'apellido1'},
	        {'data': 'nombre'},
	        {'data': 'email'},
	        {'data': 'rol.descripcion'},
	        {'data': 'razonExclusion'},
	        {'data': 'listaDist', 'render': function(row) {
        		if(row.listaDist==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', 'nombreusuario': row.admusuidentificador};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        	}}, 
        	{'label': function(row) { return "Borrar"; }, 'onClick': function(row) {
        		var mensaje = "¿Desea borrar el usuario seleccionado?";
        		var titulo = "Borrar Usuario";

        		Atis.confirmDialog(titulo, mensaje, {
		        	Si: function() {
		        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO %>',
		        			'id': row.codNum};
	        			Atis.sendForm("<%= request.getRequestURI() %>", params);
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
        	
        	}}]}	
	    ]
	});

}); 
</script>
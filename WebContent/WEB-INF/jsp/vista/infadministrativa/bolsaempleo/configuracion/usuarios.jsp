<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
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
	
		<div class="titulo-bolsa-empleo">
		<h2>Usuarios</h2>
    
	    <a class="link-btn" id="nuevo_usuario" href="<%= request.getRequestURI() %>">
	    	 Nuevo usuario
	    </a>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<tr>
			<th scope="col" style="width:3%"></th>
			<th scope="col" style="width:3%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="DNI del usuario">Tipo</th>
			<th scope="col" style="width:10%" title="DNI del usuario">Documento</th>
			<th scope="col" style="width:10%" title="DNI del usuario">UJA</th>
			<th scope="col" style="width:13%" title="Nombre del usuario">Nombre</th>
			<th scope="col" style="width:22%" title="DNI del usuario">Apellidos</th>
			<th scope="col" style="width:10%" title="Email del usuario">Email</th>
			<th scope="col" style="width:10%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
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
			<th scope="col" style="width:3%"></th>
			<th scope="col" style="width:3%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="DNI del usuario">Tipo</th>
			<th scope="col" style="width:10%" title="DNI del usuario">Documento</th>
			<th scope="col" style="width:10%" title="DNI del usuario">UJA</th>
			<th scope="col" style="width:13%" title="Nombre del usuario">Nombre</th>
			<th scope="col" style="width:22%" title="DNI del usuario">Apellidos</th>
			<th scope="col" style="width:10%" title="Email del usuario">Email</th>
			<th scope="col" style="width:10%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%" title="Borrado del usuario">Borrado</th>
			<th scope="col" style="width:5%" title="Fecha borrado del usuario">Fecha Borrado</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="14" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
		<h2>Usuarios Excluidos</h2>
		<table class="bluetable bolsaempleo" id="table_usuarios_excluidos">
		<tr>
			<th scope="col" style="width:3%"></th>
			<th scope="col" style="width:3%" title="Id del usuario">Id</th>
			<th scope="col" style="width:5%" title="DNI del usuario">Tipo</th>
			<th scope="col" style="width:10%" title="DNI del usuario">Documento</th>
			<th scope="col" style="width:10%" title="DNI del usuario">UJA</th>
			<th scope="col" style="width:13%" title="Nombre del usuario">Nombre</th>
			<th scope="col" style="width:22%" title="DNI del usuario">Apellidos</th>
			<th scope="col" style="width:10%" title="Email del usuario">Email</th>
			<th scope="col" style="width:10%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:5%" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%" title="Excluido del usuario">Excluido</th>
			<th scope="col" style="width:10%" title="Razon exclusion del usuario">Razon Exclusion</th>
			<th scope="col" style="width:10%" title="Fecha exclusion usuario">Fecha Exclusion</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="14" style="width:100%"></th>
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
	        {'data': 'ujaendni'},
	        {'data': 'nombre'},
	        {'data': 'apellido1', 'render': function(row) {
        			return "<p>"+row.apellido1+" "+row.apellido2+"</p>"; 
        	}},
	        {'data': 'email'},
	        {'data': 'rol.descripcion'},
	        {'data': 'listaDist', 'render': function(row) {
        		if(row.listaDist==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}}


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
	        {'data': 'ujaendni'},
	        {'data': 'nombre'},
	        {'data': 'apellido1', 'render': function(row) {
        			return "<p>"+row.apellido1+" "+row.apellido2+"</p>"; 
        	}},
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
	        {'data': 'borrado', 'render': function(row) {
        		if(row.borrado==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}},
	        {'data': 'fechaBorrado'}
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
	        {'data': 'ujaendni'},
	        {'data': 'nombre'},
	        {'data': 'apellido1', 'render': function(row) {
        			return "<p>"+row.apellido1+" "+row.apellido2+"</p>"; 
        	}},
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
	        {'data': 'excluido', 'render': function(row) {
        		if(row.excluido==true){
        			return "<center><img style='width:20px; height:20px' src='/img/md/check_box.svg'/></center>"; 
        		}
        		else{
        			return "<center><img style='width:20px; height:20px' src='/img/md/cancel.svg'/></center>"; 
        		}
        	}},
	        {'data': 'razonExclusion'},
	        {'data': 'fechaExclusion'}
	    ]
	});

}); 
</script>
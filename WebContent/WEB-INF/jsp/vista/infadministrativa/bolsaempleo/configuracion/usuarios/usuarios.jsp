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
			<th scope="col" style="width:7%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:15%" title="N Documento">Documento</th>
			<th scope="col" style="width:14%" title="Nombre de usuario" class="user">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:20%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:15%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:15%" class="center" title="Excluido">Excluido</th>
			<th scope="col" style="width:15%" class="center" title="Eliminado">Eliminado</th>
			<th scope="col" style="width:15%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="10" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
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
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%=ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'tipodocumento', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'numdocumento', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'codcuenta', 'filter': true, 'class': 'overflow-auto'},
	        {'data': 'apellido1', 'filter': true, 'class': 'overflow-auto', 'render': function(row) {
        		return row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2; 
        	}},
	        {'data': 'rol.descripcion', 'filter': {'type': 'select', 'options': {'1050':'Personal', '1051':'Comision'}}  , 'order': {'active': false}},
	        {'data': 'listaDist', 'filter': {'type': 'selectBoolean', 'true': 'En Lista', 'false': 'Sin Lista'}, 'render': function(row) {
	        	if(row.listaDist==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		}
        		else{
        			return "<div title='Excluido de lista distribución' class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'excluido', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'Incluido'}, 'optionDefault': 'false'},
	    		'render': function(row) {
	        		if (row.excluido) {
	        			return "<div title='Excluido' class='circle-true'></div>"; 
	        		}
        		}
	        },
        	{'data': 'borrado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Borrado', 'false': 'No Borrado'}, 'optionDefault': 'false'},
	    		'render': function(row) {
	        		if (row.borrado) {
	        			return "<div title='Eliminado' class='circle-true'></div>"; 
	        		}
        		}
	        },
        	{'data': 'codnum', 'buttons': [
        		{'label': 'Editar', 'onClick': function(row) {
        		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>', '<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
        		}},
        		{'label': function(row) { 
        					if(row.excluido) return "Incluir"
        					else return "Excluir"; 
        				},
        		'onClick': function(row) {
    	    		if(row.excluido){
    	    			Atis.confirmDialog("Incluir usuario", "&iquest;Desea incluir a este usuario en la base de datos?", {
    		            	Si: function() {
    		            		var params = {
    		            				'a': '<%=ControladorUsuarioBolsaEmpleo.ACCION_INCLUIR_USUARIO%>', 
    		            				'<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta,
    		            				'<%=ControladorUsuarioBolsaEmpleo.PARAM_ID%>': row.codNum
    		            			};
    		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
    		              		$(this).dialog("close");
    		            	},
    		            	No: function() {
    		              		$(this).dialog("close");
    		            	}
    		          	});
    	    		}
    	    		else{
    	    			Atis.confirmDialog("Excluir usuario", "&iquest;Desea excluir a este usuario en la base de datos?", {
    		            	Si: function() {
    		            		var params = {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>',
    		            				'<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO %>': row.codcuenta,
    		            				'<%= ControladorUsuarioBolsaEmpleo.PARAM_ID %>': row.codcuenta
    		            		};
    		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
    		              		$(this).dialog("close");
    		            	},
    		            	No: function() {
    		              		$(this).dialog("close");
    		            	}
    		          	});
    	    		}
        		}
        		}
   			]}	
	    ],
	    "actions": [
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO%>", selected); } }   	
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
        		var mensaje = "&iquest;Desea borrar los usuarios seleccionados?";
    			var titulo = "Borrar Usuarios";
			break;
			case('excluirusuario'):
        		var mensaje = "&iquest;Desea excluir los usuarios seleccionados?";
    			var titulo = "Excluir Usuarios";
			case('incluirusuario'):
        		var mensaje = "&iquest;Desea incluir los usuarios seleccionados?";
    			var titulo = "Incluir Usuarios";
			case('recuperarusuario'):
        		var mensaje = "&iquest;Desea recuperar los usuarios seleccionados?";
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

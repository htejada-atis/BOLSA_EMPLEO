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
	
	<div class="titulo-bolsa-empleo">
		<h2>Usuarios Candidatos</h2>
		
		<button class="link-btn" id="nuevo_usuario">Nuevo candidato</button>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_usuarios">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:6%" title="Tipo de documento">Tipo</th>
			<th scope="col" style="width:13%" title="N Documento">Documento</th>
			<th scope="col" style="width:14%" title="Nombre de usuario" class="user">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:20%" title="Rol del usuario">Rol</th>
			<th scope="col" style="width:10%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%" class="center" title="Excluido">Excluido</th>
			<th scope="col" style="width:10%" class="center" title="Eliminado">Eliminado</th>
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
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorUsuarioCandidato.ACCION_FORMULARIO_USUARIO %>'});
	});
	
	
	var table_usuarios = new Atis.DataTable('#table_usuarios', {
	    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
	    "selectable": true,
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS_CANDIDATOS%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'tipodocumento', 'filter': true, 'overflow': 'auto'},
	        {'data': 'numdocumento', 'filter': true, 'overflow': 'auto'},
	        {'data': 'codcuenta', 'filter': true, 'overflow': 'auto'},
	        {'data': 'apellido1', 'filter': true, 'overflow': 'auto', 'render': function(row) {
        		return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
        	}},
	        {'data': 'rol.descripcion', 'order': {'active': false}},
	        {'data': 'listaDist', 'filter': {'type': 'selectBoolean', 'true': 'En Lista', 'false': 'Sin Lista'}, 'render': function(row) {
	        	if(row.listaDist==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		} else {
        			return "<div title='Excluido de lista distribución' class='circle-false'></div>"; 
        		}
        	}},
        	{'data': 'excluido', 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'Incluido'}, 'optionDefault': 'false'}, 'render': function(row) {
	        	if(row.excluido==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		}
        	}},
        	{'data': 'borrado', 'filter': {'type': 'select', 'options':{'true': 'Borrado', 'false': 'No Borrado'}, 'optionDefault': 'false'}, 'render': function(row) {
	        	if(row.borrado==true){
        			return "<div title='En lista distribución' class='circle-true'></div>"; 
        		}
        	}},
        	{'data': 'codnum', 'buttons': [
        		{'label': 'Editar', 'onClick': function(row) {
        				var params = {'a': '<%= ControladorUsuarioCandidato.ACCION_EDITAR_USUARIO_FORM %>', '<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO %>': row.codcuenta};
        				Atis.sendForm("<%= request.getRequestURI() %>", params);
        			}
        		},
        		{'label': function(row) { 
					if(row.excluido) return "Incluir"
					else return "Excluir"; 
				},
				'onClick': function(row) {
    				if(row.excluido){
    					Atis.confirmDialog("Incluir usuario", "&iquest;Desea incluir a este usuario en la base de datos?", {
	            			Si: function() {
	            				var params = {
	            					'a': '<%=ControladorUsuarioCandidato.ACCION_INCLUIR_USUARIO%>', 
	            					'<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO %>': row.codcuenta,
	            					'<%=ControladorUsuarioCandidato.PARAM_ID%>': row.codNum
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
	            				var params = {'a': '<%= ControladorUsuarioCandidato.ACCION_EXCLUIR_USUARIO %>',
	            					'<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO %>': row.codcuenta,
	            					'<%= ControladorUsuarioCandidato.PARAM_ID %>': row.codcuenta
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
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioCandidato.ACCION_ELIMINAR_USUARIO%>", selected); } }   	
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
    		break;
			case('incluirusuario'):
        		var mensaje = "¿Desea incluir los usuarios seleccionados?";
    			var titulo = "Incluir Usuarios";
    		break;
			case('recuperarusuario'):
        		var mensaje = "¿Desea recuperar los usuarios seleccionados?";
    			var titulo = "Recuperar Usuarios";
			break;
		}
		Atis.confirmDialog(titulo, mensaje, {
        	Si: function() {
        		var params = {
        				'a': '<%=ControladorUsuarioCandidato.ACCION_USUARIO%>', 
        				'<%=ControladorUsuarioCandidato.PARAM_ACCION_USUARIO%>': accion,
        				'<%=ControladorUsuarioCandidato.PARAM_USUARIOS_SELECCIONADOS%>': Atis.object2Json(selected)
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

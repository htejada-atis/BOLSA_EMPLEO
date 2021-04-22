<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo usuarios-form">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
		String nombre = "";
		String apellidos = "";
		String email = "";
		String tipo_documento = "";
		String n_documento = "";
		String razon_excluido = "";
		String usuario = "";
		Boolean lista_dist = false;
		Boolean excluido = false;
		
		if(bean.getUsuarioArcos() != null) {
			usuario = bean.getUsuarioArcos().getUid();
			nombre = bean.getUsuarioArcos().getNombre();
			apellidos = bean.getUsuarioArcos().getApellido1() + " " + bean.getUsuarioArcos().getApellido2();
			email = bean.getUsuarioArcos().getEmailCalculado();
			tipo_documento = bean.getUsuarioArcos().getDocumentoTipo();
			n_documento = bean.getUsuarioArcos().getDocumentoNumero();
			
			if(bean.getUsuario() != null){
				lista_dist = bean.getUsuario().getListaDist();
				excluido = bean.getUsuario().getExcluido();

				if(bean.getUsuario().getRazonExcluido()!=null){
					razon_excluido = bean.getUsuario().getRazonExcluido();
				}
				else{
					razon_excluido ="";
				}
				
				if(bean.getBusqueda()){
			    	out.print("<h2>El usuario ya existe</h2>");
				}
				else{
			    	out.print("<h2>Editar Usuario</h2>");
				}
			}
			else{
			    out.print("<h2>Nuevo Usuario</h2>");
			}

		} else {
		    out.print("<h2>Nuevo Usuario</h2>");
		}
	%>
	
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" id="usuario_nombre" value="" />
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="nickname">Usuario: </label>
    			<input class="form-input-custom" id="nickname" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled/>
    		</div>
	   		<div class="form-group">
    			<label for="nombre">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= nombre %>" disabled/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="apellidos">Apellidos: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= apellidos %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="email">Email: </label>
    			<input class="form-input-custom" id="email" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= email %>" disabled/>
    		</div>
    	</div>

		<div class="form-group-container">
	    	<div class="form-group">
    			<label for="tipo_documento">Tipo de documento: </label>
    			<input class="form-input-custom" id="tipo_documento" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= tipo_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="n_documento">Nº de documento: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= n_documento %>" disabled/>
    		</div>
		</div>
		<div class="form-group-container">
	    	<div class="form-check">
    			<label for="usuario_lista_dist"><input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_LISTA %>" value="<%= lista_dist %>" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    		</div>
    		<div class="form-check">
    			<label for="usuario_excluido"><input class="params" type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO %>" value="<%= excluido %>" <%= (excluido ? "checked=''" : "") %>/>Excluido</label>
    		</div>
		</div>
	
		<div class="form-group">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60"><%= razon_excluido %></textarea>
   		</div>
		
		<div class="form-group">
			<label for="select_role">Elija el rol para asociar</label>
			<select class="params" id="select_role" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ROLE %>" style="width:100%;">
				<option value="-1"> - </option>
    				<%for(Rol role: bean.getRoles()){
    					if(bean.getUsuario()!=null){
            				if(bean.getRol().getCodNum().equals(role.getCodNum())){%>
        						<option value="<%=role.getCodNum()%>" selected="selected"><%=role.getDescripcion()%></option>
        					<%}
    						else{%>
								<option value="<%=role.getCodNum()%>"><%=role.getDescripcion()%></option>
							<%}
    					}
						else{%>
							<option value="<%=role.getCodNum()%>"><%=role.getDescripcion()%></option>
						<%}
							
					}%>
			</select>
		</div>
		
    	<div class="form-btn">
    		<input id="usuario_enviar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ENVIAR %> " value="<%if(bean.getUsuario()!=null){%><%if(bean.getBusqueda()){%>Volver<%}else{%>Guardar usuario<%}%><%}else{%>Añadir usuario<%}%>"/>
    	</div>
    </form>
    
    
	<% 
		if(bean.getUsuario().getRol().getValor().equals("bolempcandidato")) { %>
			<table class="bluetable bolsaempleo" id="table_areas_excluidas">
	  			<caption class="table-title">Áreas excluidas para el usuario</caption>  
				<tr>
					<th scope="col" style="width:5%"></th>
					<th scope="col" style="width:20%" title="Id de la area">Id</th>
					<th scope="col" style="width:25%" title="Código de area">Código</th>
					<th scope="col" style="width:65%">Area</th>
					<th scope="col" class="center" style="width:15%">Baremable</th>	
				</tr>
				<tbody>				
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="10" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
			
			
			<table class="bluetable bolsaempleo" id="table_areas">
	  			<caption class="table-title">Listado de áreas</caption>  
				<tr>
					<th scope="col" style="width:5%"></th>
					<th scope="col" style="width:20%" title="Id de la area">Id</th>
					<th scope="col" style="width:25%" title="Código de area">Código</th>
					<th scope="col" style="width:65%">Area</th>
					<th scope="col" class="center" style="width:15%">Baremable</th>	
				</tr>
				<tbody>				
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="10" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>		
		<%}%>   
</div>

<script>

	function enviarUsuario(event, submit_input) {
		event.preventDefault();
	
		<% if(bean.getUsuario() != null) { 
			
			if(bean.getBusqueda()) {%>
				input_accion = document.getElementById("accion_formulario");
				input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_VOLVER_USUARIO %>';
			<%}else{%>
				input_accion = document.getElementById("accion_formulario");
				input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>';
				input_id = document.getElementById("usuario_id");
				input_id.value = '<%= bean.getUsuario().getCodNum() %>';
				input_nombre_usuario = document.getElementById("usuario_nombre");
				input_nombre_usuario.value = '<%= bean.getUsuario().getCodCuenta()%>';
			<%}%>
			
		<% } else { %>
			input_id = document.getElementById("usuario_id");
			input_id.value = '<%= bean.getUsuarioArcos().getUid() %>';
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_AGREGAR_USUARIO %>';
		<% } %>
	
		input_lista_dist = document.getElementById("usuario_lista_dist");
		input_lista_dist.value = input_lista_dist.checked;
	
		input_excluido = document.getElementById("usuario_excluido");
		input_excluido.value = input_excluido.checked;
	
		submit_input.form.submit();
	}

	$(document).ready(function() {
		document.getElementById("usuario_enviar").addEventListener("click", function(event) {
			enviarUsuario(event, this);
		});
		
		<% if(bean.getBusqueda()) {%>
		params = document.getElementsByClassName("params");
		for (var i = 0; i < params.length; i++) { 
			params[i].disabled = true;
		}
		<%}%>
		
		var table = new Atis.DataTable('#table_areas', {
		    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/areasbaremar" },
		    "params": {"<%=ControladorUsuarioBolsaEmpleo.PARAM_ID%>": <%= bean.getUsuario().getCodNum() %>},
		    "selectable": true,
		    "pageSize": 10,
		    "filterable": true,
		    "action": "<%= ControladorAreasABaremar.ACCION_DATATABLE_EXCLUIDOS %>",
		    "columns": [
		    	{'data': 'area.codNum', 'selectable': true},
		        {'data': 'area.codNum', 'filter': {'type': 'number'}},
		        {'data': 'area.idAreaExterno' , 'filter': true, 'class': 'overflow-auto'},
		        {'data': 'area.descripcion', 'filter': true, 'class': 'overflow-auto'},
		        {'data': 'baremable', 'filter': {'type': 'selectBoolean', 'true': 'Baremable', 'false': 'No Baremable'} , 'order': {'active': false}, 'render': function(row) {
	        		if(row.baremable){
	        			return "<div title='Baremable' class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div title='No Baremable' class='circle-false'></div>"; 
	        		}
	        	}},
		    ],
		    "actions": [
		    	{'label': 'Excluir Areas', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO_AREA%>", selected); } }
		    ]
		});	
		
		var table = new Atis.DataTable('#table_areas_excluidas', {
		    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/usuarios" },
		    "params": {"<%=ControladorUsuarioBolsaEmpleo.PARAM_ID%>": <%= bean.getUsuario().getCodNum() %>},
		    "selectable": true,
		    "pageSize": 10,
		    "filterable": true,
		    "action": "<%= ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_EXCLUIDOS_AREA %>",
		    "columns": [
		    	{'data': 'area.codNum', 'selectable': true},
		        {'data': 'area.codNum', 'filter': {'type': 'number'}},
		        {'data': 'area.idAreaExterno', 'filter': true, 'class': 'overflow-auto'},
		        {'data': 'area.descripcion', 'filter': true, 'class': 'overflow-auto'},
		        {'data': 'baremable', 'filter': {'type': 'selectBoolean', 'true': 'Baremable', 'false': 'No Baremable'} , 'order': {'active': false},'render': function(row) {
	        		if(row.baremable){
	        			return "<div title='Baremable' class='circle-true'></div>";
	        		}
	        		else{
	        			return "<div title='No Baremable' class='circle-false'></div>";
	        		}
	        	}},
		    ],
		    "actions": [
		    	{'label': 'Borrar Areas excluidas', 'onClick': function(selected) { enviaAccion("<%=ControladorUsuarioBolsaEmpleo.ACCION_INCLUIR_USUARIO_AREA%>", selected); } }
		    ]
		});	
		
		
		
	});
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de los usuarios', 'Seleccione al menos un usuario.');
			return;
		}
		console.log(accion,selected);

		Atis.confirmDialog("Exclusión de areas", "¿ Desea excluir las areas seleccionadas del usuario ?", {
        	Si: function() {
        		var params = {
        				'a': '<%=ControladorUsuarioBolsaEmpleo.ACCION_USUARIO%>', 
        				'aa': '<%=ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO_AREA%>', 
        				'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO%>': accion, 
        				'<%=ControladorUsuarioBolsaEmpleo.PARAM_ID%>': <%= bean.getUsuario().getCodNum() %>, 
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
	
</script>
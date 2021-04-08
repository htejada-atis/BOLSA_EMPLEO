<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo">

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
				razon_excluido = bean.getUsuario().getRazonExcluido();
				
		    	out.print("<h2>Editar Usuario</h2>");
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
		<div class="form-group">
    		<label for="nickname">Usuario: </label>
    		<input id="nickname" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled/>
    	</div>
	   	<div class="form-group">
    		<label for="nombre_usuario">Nombre: </label>
    		<input id="fichero_titulo" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= nombre %>" disabled/>
    	</div>
    	<div class="form-group">
    		<label for="nombre_usuario">Apellidos: </label>
    		<input id="fichero_titulo" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= apellidos %>" disabled/>
    	</div>
    	<div class="form-group">
    		<label for="nombre_usuario">Email: </label>
    		<input id="fichero_titulo" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= email %>" disabled/>
    	</div>
    	<div class="form-group">
    		<label for="nombre_usuario">Tipo de documento: </label>
    		<input id="fichero_titulo" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= tipo_documento %>" disabled/>
    	</div>
    	<div class="form-group">
    		<label for="nombre_usuario">Nº de documento: </label>
    		<input id="fichero_titulo" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE %>" value="<%= n_documento %>" disabled/>
    	</div>
    	<div class="form-check">
    		<label for="usuario_lista_dist"><input type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_LISTA %>" value="<%= lista_dist %>" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    	</div>
    	<div class="form-check">
    		<label for="usuario_excluido"><input type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO %>" value="<%= excluido %>" <%= (excluido ? "checked=''" : "") %>/>Excluido</label>
    	</div>
    	<div class="form-group">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="50"><%= razon_excluido %></textarea>
    	</div>
		
		<div class="form-group">
			<label for="select_role">Elija el rol para asociar</label>
			<select id="select_role" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ROLE %>">
				<option value="0"> - </option>
    				<%for(Rol role: bean.getRoles()){
    					if(bean.getUsuario() != null){
    						if(bean.getRol().getCodNum().equals(role.getCodNum())){%>
								<option value="<%=role.getCodNum()%>" selected="selected"><%=role.getDescripcion()%></option>
							<%}
    					}
						else{%>
							<option value="<%=role.getCodNum()%>"><%=role.getDescripcion()%></option>
						<%}
							
					}%>
			</select>
		</div>
    	<div class="form-btn">
    		<input id="usuario_enviar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ENVIAR %> " value="<%if(bean.getUsuario()!=null) {%>Guardar usuario<%}else{%>Añadir usuario<%}%>"/>
    	</div>
    </form>
</div>

<script>

	function enviarUsuario(event, submit_input) {
		event.preventDefault();
	
		<% if(bean.getUsuario() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>';
			input_id = document.getElementById("usuario_id");
			input_id.value = '<%= bean.getUsuario().getCodNum() %>';
			input_nombre_usuario = document.getElementById("usuario_nombre");
			input_nombre_usuario.value = '<%= bean.getUsuario().getAdmUsuIdentificador() %>';
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

	document.getElementById("usuario_enviar").addEventListener("click", function(event) {
		enviarUsuario(event, this);
	});

</script>
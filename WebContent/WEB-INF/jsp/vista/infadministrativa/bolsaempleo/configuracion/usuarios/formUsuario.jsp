<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="java.util.Date"%>

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
		Date fecha_ini = null;
		Date fecha_fin = null;
		
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
				fecha_ini = bean.getUsuario().getFechaExclusionInicio();
				fecha_fin = bean.getUsuario().getFechaExclusionFin();

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
	    	<div class="form-check-custom">
    			<label for="usuario_lista_dist"><input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_LISTA %>" value="<%= lista_dist %>" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
    				<label for="usuario_excluido"><input class="params" type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO %>" value="<%= excluido %>" <%= (excluido ? "checked=''" : "") %>/>Excluido</label>
    			</div>    		
    			<div class="form-check-custom" id="excluido_tipo" style="display:none;">
					<label class="form-label-custom" for="indefinido" style="float: none; margin-right:0px; margin-bottom:5px;"><input class="form-input" type="radio" id="indefinido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;">Indefinido</label>
					<label class="form-label-custom" for="temporal" style="float: none; margin-right:0px;"><input class="form-input" type="radio" id="temporal" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;">Temporal</label>
    			</div>
    		</div>
		</div>
		<div class="form-group-container col-2" id="fecha_excluido" style="display:none;">
	    	<div class="form-check">
    			
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
	    			<label for="noticia_fecha"><b>Fecha Inicio</b>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_INICIO %>" id="fecha_ini" autocomplete="off" value="<%= fecha_ini!=null ? fecha_ini : "" %>" style="width:80%"/>
	    		</div>
	    		<div class="form-check-custom">
	    			<label for="noticia_fecha"><b>Fecha Fin</b>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_FIN %>" id="fecha_fin" autocomplete="off" value="<%= fecha_fin!=null ? fecha_fin : "" %>" style="width:80%"/>
	    		</div>
	    	</div>
		</div>
	
		<div class="form-group">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60" <%= (excluido ? "" : "disabled") %>><%= razon_excluido %></textarea>
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
		
		if(input_excluido.checked){
			input_exc_indefinido = document.getElementById("indefinido");
			input_exc_temporal = document.getElementById("temporal");
			
			if(input_exc_indefinido.checked){
				input_exc_indefinido.value = "I";
				input_exc_temporal.value = "I";
			}
			else{
				input_exc_indefinido.value = "T";
				input_exc_temporal.value = "T";
			}
		}
	
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("usuario_enviar").addEventListener("click", function(event) {
			console.log("asda");
			enviarUsuario(event, this);
		});
		
		$("#fecha_ini").datepicker();
		$("#fecha_fin").datepicker();
		
		<% if (fecha_ini == null) { %>
			document.getElementById("fecha_ini").value = getTodayDate();
		<% } %>
		
		document.getElementById("usuario_excluido").addEventListener("change", function(event) {
			if(document.getElementById("usuario_excluido").checked){
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=false;
				
				$("#excluido_tipo").show();
			}
			else{
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=true;
				
				$("#excluido_tipo").hide();
			}
		});
		
		document.getElementById("temporal").addEventListener("change", function(event) {
			if(document.getElementById("temporal").checked) $("#fecha_excluido").show();
		});
		document.getElementById("indefinido").addEventListener("change", function(event) {
			if(document.getElementById("indefinido").checked) $("#fecha_excluido").hide();
		});
	
		<% if(bean.getBusqueda()) {%>
		params = document.getElementsByClassName("params");
		for (var i = 0; i < params.length; i++) { 
			params[i].disabled = true;
		}
		<%}%>
		
		function getTodayDate() {
			var now = new Date();

			var day = ("0" + now.getDate()).slice(-2);
			var month = ("0" + (now.getMonth() + 1)).slice(-2);

			return day + "/" + month + "/" + now.getFullYear();
		}
	});
	
	
</script>
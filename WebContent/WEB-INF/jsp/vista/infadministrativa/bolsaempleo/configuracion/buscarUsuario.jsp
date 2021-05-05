<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo usuarios-buscar">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	%>
	
	<% if(bean.getUsuario()!=null) {%>
		<% if(bean.getBusqueda()) {%>
		<h2>Excluir Usuario</h2>
		<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
   			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" id="usuario_nombre" value="" />
    		<div class="form-group">
    			<label for="razon_exclusion">Razón exclusión</label>
    			<textarea class="params" id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="50"></textarea>
    		</div>
    		<div class="form-btn">
   				<input id="usuario_excluir" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>" value="Excluir"/>
    		</div>
    	</form>
    	<% }%>
	<% } else { %>
		<h2>Nuevo Usuario</h2>
		<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
    		<div class="form-group">
    			<label for="usuario_nombre">Nombre de usuario</label>
    			<input type="text" name="nombre" id="usuario_nombre"/>
    		</div>
    		<div class="form-btn">
    			<input id="usuario_buscar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>" value="Buscar usuario"/>
    		</div>
    	</form>
		
	<% } %>
    
</div>

<script>


	if(document.getElementById("usuario_buscar")!=undefined){
		document.getElementById("usuario_buscar").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>',
				'nombre': document.getElementById("usuario_nombre").value});
		});
	}
	
	if(document.getElementById("usuario_excluir")!=undefined){
		document.getElementById("usuario_excluir").addEventListener("click", function(event) {
			enviarUsuario(event, this);
		});
	}

	function enviarUsuario(event, submit_input) {
		event.preventDefault();

		<%if(bean.getUsuario()!=null){%>
		
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>';
		input_id = document.getElementById("usuario_id");
		input_id.value = '<%= bean.getUsuario().getCodNum() %>';
		input_nombre_usuario = document.getElementById("usuario_nombre");
		input_nombre_usuario.value = '<%= bean.getUsuario().getCodCuenta()%>';

		submit_input.form.submit();
		<%}%>
		
	}

</script>
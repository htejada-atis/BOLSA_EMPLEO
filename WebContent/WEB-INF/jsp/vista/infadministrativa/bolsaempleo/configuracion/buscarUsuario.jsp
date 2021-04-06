<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
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
		
		if(bean.getUsuario() != null) {
			nombre = bean.getUsuario().getNombre();

	    	out.print("<h2>Editar Usuario</h2>");
		} else {
		    out.print("<h2>Nuevo Usuario</h2>");
		}
	%>
    
    <% if(bean.getUsuario() != null) {%>

		<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
			<div class="form-select">
				<label>Elija el usuario para asociar</label>
				<select id="select_area">
					<option value="0">Usuarios</option>

				</select>
	
			</div>
    	</form>
		
	<%} else { %>
	    <form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
    		<div class="form-group">
    			<label for="usuario_nombre">Nombre de usuario</label>
    			<input type="text" name="nombre" id="usuario_nombre" value="<%= nombre %>"/>
    		</div>
    		<div class="form-btn">
    			<input id="usuario_buscar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>" value="Buscar usuario"/>
    		</div>
    	</form>
	<% } %>

    
    
    
    
</div>

<script>

	
	document.getElementById("usuario_buscar").addEventListener("click", function(event) {
		event.preventDefault();
		console.log(document.getElementById("usuario_nombre").value);
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>',
			'nombre': document.getElementById("usuario_nombre").value});
		
		$('#respuesta').modal('show');
	});

</script>
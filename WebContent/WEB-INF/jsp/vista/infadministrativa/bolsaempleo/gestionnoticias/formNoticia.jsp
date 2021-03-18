<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionNoticias"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaNoticias bean = (VistaNoticias) uvdatos.getVistas().get(VistaNoticias.class.getName());
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
		String texto = "";
		String enlace = "";
		String fecha = "";
		Boolean publica = false;
	
		if(bean.getNoticia() != null) {
			texto = bean.getNoticia().getTexto();
			enlace = bean.getNoticia().getEnlace();
			fecha = bean.getNoticia().getFechaFormato();
			publica = bean.getNoticia().isPublica();
	    	out.print("<h2>Editar noticia</h2>");
		} else {
		    out.print("<h2>Nueva noticia</h2>");
		}
	%>
    
    
    <form id="actualizar_noticia" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorGestionNoticias.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorGestionNoticias.PARAM_ID%>" id="noticia_id" value="" />
    	<div class="form-group">
    		<label>Texto</label>
    		<input type="text" name="texto" id="noticia_texto" value="<%= texto %>"/>
    	</div>
    	<div class="form-group">
    		<label>Enlace</label>
    		<input type="text" name="enlace" id="noticia_enlace" value="<%= enlace %>"/>
    	</div>
    	<div class="form-group">
    		<label>Fecha</label>
    		<input type="text" name="fecha" id="noticia_fecha" value="<%= fecha %>"/>
    		<input type="text" id="fechaCampoFormulario" value="<%=Formateador.formatoFecha(bean.getNoticia().getFecha(), "DDMMYYYY")%>"/>
    	</div>
    	<div class="form-check">
    		<label><input type="checkbox" id="noticia_publica" name="publica" value="<%= publica %>" <%= (publica ? "checked=''" : "") %>/>Pública</label>
    	</div>
    	<div class="form-btn">
    		<input id="noticia_enviar" type="submit" name="<%= ControladorGestionNoticias.PARAM_ENVIAR %>" value="<%= bean.getNoticia() != null ? "Guardar cambios" : "Insertar noticia" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function enviarNoticia(event, submit_input) {
		event.preventDefault();
		
		<% if(bean.getNoticia() != null) { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorGestionNoticias.ACCION_EDITAR_NOTICIA %>';
			input_id = document.getElementById("noticia_id");
			input_id.value = '<%= bean.getNoticia().getCodNum() %>';
		<% } else { %>
			input_accion = document.getElementById("accion_formulario");
			input_accion.value = '<%= ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA %>';
		<% } %>
		
		input_publica = document.getElementById("noticia_publica");
		input_publica.value = input_publica.checked;
		submit_input.form.submit();
	}

	$(document).ready(function() {
		$("#noticia_fecha").datepicker();
		$("#fechaCampoFormulario").datepicker();
		
		document.getElementById("noticia_enviar").addEventListener("click", function(event) {
			enviarNoticia(event, this);
		});
	});

</script>
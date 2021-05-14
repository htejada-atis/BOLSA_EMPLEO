<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionNoticias"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaNoticias"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaNoticias bean = (VistaNoticias) uvdatos.getVistas().get(VistaNoticias.class.getName());
Noticia noticia = bean.getNoticia();

String texto = BolsaEmpleoUtils.getParamForm(request, ControladorGestionNoticias.PARAM_TEXTO, noticia != null ? noticia.getTexto() : "");
String enlace = BolsaEmpleoUtils.getParamForm(request, ControladorGestionNoticias.PARAM_ENLACE, noticia != null && noticia.getEnlace() != null ? noticia.getEnlace() : "");
String fecha = BolsaEmpleoUtils.getParamForm(request, ControladorGestionNoticias.PARAM_FECHA, noticia != null ? Formateador.formatoFecha(noticia.getFecha(), Formateador.FORMATO_FECHA_DDMMYYYY) : "");
Boolean publica = noticia != null ? noticia.isPublica() : false;

%>


<div class="bolsa-empleo">

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

	<% if (noticia != null) { %>
	    <h2>Editar noticia</h2>
	<% } else { %>
		<h2>Nueva noticia</h2>
	<% } %>
    
    <div id="erroresForm" class="error" style="display:none;">
		<p>Porfavor primero debe rellenar todos los campos requeridos del formulario:</p>
		<ul id="erroresFormList"></ul>
	</div>
    
    <p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
    
    <form id="actualizar_noticia" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorGestionNoticias.PARAM_ACCION %>" id="accion_formulario" 
    			value="<%= noticia != null ? ControladorGestionNoticias.ACCION_EDITAR_NOTICIA : ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA %>" />
		<input type="hidden" name="<%= ControladorGestionNoticias.PARAM_ID%>" id="noticia_id" 
				value="<%= noticia != null ? noticia.getCodNum() : "" %>" />
		<div class="form-group-container col1">
			<div class="form-group">
	    		<label for="noticia_texto" class="bold-label">Texto:</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorGestionNoticias.PARAM_TEXTO %>" id="noticia_texto" value="<%= texto %>" required/>
	    	</div>
		</div>
    	<div class="form-group-container col2">
    		<div class="form-group">
	    		<label for="noticia_enlace">Enlace:</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorGestionNoticias.PARAM_ENLACE %>" id="noticia_enlace" value="<%= enlace %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="noticia_fecha"><b>Fecha</b>:</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorGestionNoticias.PARAM_FECHA %>" id="noticia_fecha" autocomplete="off" 
	    		value="<%= fecha %>" required/>
	    	</div>
    	</div>
    	<div class="form-check">
    		<label for="noticia_publica" class="bold-label"><input type="checkbox" id="noticia_publica" name="<%= ControladorGestionNoticias.PARAM_PUBLICA %>" 
    		value="<%= publica %>" <%= (publica ? "checked=''" : "") %> />Pública</label>
    	</div>
    	<div class="form-btn">
    		<input id="noticia_enviar" type="submit" name="<%= ControladorGestionNoticias.PARAM_ENVIAR %>" value="<%= noticia != null ? "Guardar cambios" : "Insertar noticia" %>"/>
    	</div>
    </form>
    
</div>

<script>

	function checkRequiredInputs(){
		var cont = true;
		$('#erroresFormList').html("");
		if($('#noticia_texto').val()==""){
			$('#erroresFormList').append( "<li>Texto</li>" );
			$('#erroresForm').show();
			cont = false;
		}
		if($('#noticia_fecha').val()==""){
			$('#erroresFormList').append( "<li>Fecha</li>" );
			$('#erroresForm').show();
			cont = false;
		}
		
		return cont;
	}

	function enviarNoticia(event, submit_input) {
		
		<% if(noticia != null) { %>
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
		

	}
	
	function getTodayDate() {
		var now = new Date();

		var day = ("0" + now.getDate()).slice(-2);
		var month = ("0" + (now.getMonth() + 1)).slice(-2);

		return day + "/" + month + "/" + now.getFullYear();
	}

	$(document).ready(function() {
		$("#noticia_fecha").datepicker();
		
		<% if (noticia == null) { %>
			document.getElementById("noticia_fecha").value = getTodayDate();
		<% } %>
	});

</script>
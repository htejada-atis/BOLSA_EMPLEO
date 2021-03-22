<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaFicheros bean = (VistaFicheros) uvdatos.getVistas().get(VistaFicheros.class.getName());
%>
<div class="bolsa-empleo">
	<h2>Ficheros</h2>
	
	<form id="subir_fichero" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
		<input type="hidden" name="<%= ControladorGestionFicheros.PARAM_ACCION %>" id="accion_formulario" value="" />
		<div class="form-file">
			<input type="file" name="<%= ControladorGestionFicheros.PARAM_FICHERO %>"/>
		</div>
		<div class="form-btn">
    		<input id="fichero_enviar" type="submit" name="<%= ControladorGestionFicheros.PARAM_ENVIAR %>" value="Enviar"/>
    	</div>
	</form>
	
	<% if (session.getAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorGestionFicheros.MENSAJE_ENVIADO);
		} 
	%>

    <table class="bluetable bolsaempleo" id="table_noticias_insertadas">
		<tr>
			<th scope="col" style="width:20%">Id</th>
			<th scope="col"	style="width:30%">Nombre</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colspan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>

function enviarNoticia(event, submit_input) {
	event.preventDefault();
	
	input_accion = document.getElementById("accion_formulario");
	input_accion.value = '<%= ControladorGestionFicheros.ACCION_AGREGAR_FICHERO %>';
	
	submit_input.form.submit();
}

	$(document).ready(function() {
		document.getElementById("fichero_enviar").addEventListener("click", function(event) {
			enviarNoticia(event, this);
		});
	});

</script>
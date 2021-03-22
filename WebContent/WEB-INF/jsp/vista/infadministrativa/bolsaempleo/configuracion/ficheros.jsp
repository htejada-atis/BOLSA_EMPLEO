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

    <table class="bluetable bolsaempleo" id="table_ficheros">
		<tr>
			<th scope="col" style="width:20%">Id</th>
			<th scope="col"	style="width:70%">Nombre</th>
			<th scope="col" style="width:10%">Descargar</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colspan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
</div>

<script>

	function subirFichero(event, submit_input) {
		event.preventDefault();
		
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorGestionFicheros.ACCION_AGREGAR_FICHERO %>';
		
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		var table = new DataTable('#table_ficheros', {
		    "ajax": { url: "<%= request.getRequestURI() %>" },
		    "pageSize": 10,
		    "columns": [
		        {'data': 'codNum'},
		        {'data': 'nombre'},
		        {'data': 'codnum', 'buttons': [{'label': 'Borrar', 'onClick': function(row) {
		        		var params = {'a': '<%= ControladorGestionFicheros.ACCION_BORRAR_FICHERO %>', 'id': row.codNum};
		        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		        	}
		        }]}
		    ],
		});	
		
		document.getElementById("fichero_enviar").addEventListener("click", function(event) {
			subirFichero(event, this);
		});
	});

</script>
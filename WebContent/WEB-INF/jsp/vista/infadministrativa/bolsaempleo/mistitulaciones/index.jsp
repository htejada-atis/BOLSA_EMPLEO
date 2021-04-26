<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>


<div class="bolsa-empleo misdatos-form">
	<div class="titulo-bolsa-empleo" style="float:right; margin-top:1.2rem">
		<a class="link-btn" id="nueva_titulacion" href="<%= request.getRequestURI() %>" style="margin-top:0">Nueva titulación</a>
	</div>
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Mis Titulaciones</h2>
	</div>
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
	
	<table class="bluetable bolsaempleo" id="tableTitulacionesUsuario">
		<tr>
			<th scope="col" style="width:10%"></th>
			<th scope="col" style="width:30%">Titulación</th>		
			<th scope="col" style="width:30%">Descripción</th>		
			<th scope="col" style="width:30%">Archivo</th>		
			<th scope="col" style="width:10%"></th>	
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {	
	
	document.getElementById('nueva_titulacion').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorMisTitulaciones.ACCION_FORMULARIO_TITULACIONES_USUARIO %>'});
	});
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las titulaciones', 'Seleccione al menos una titulación.');
			return;
		}
		Atis.confirmDialog("Borrar titulación", "¿Desea eliminar las titulaciónes seleccionadas, relacionadas con su usuario?", {
        	Si: function() {
        		var params = {
        				'a': accion, 
        				'<%=ControladorMisTitulaciones.PARAM_TITULACIONES_USUARIOS_SELECCIONADOS%>': Atis.object2Json(selected)
        			};
    			Atis.sendForm("<%= request.getRequestURI() %>", params);
          		$(this).dialog("close");
        	},
        	No: function() {
          		$(this).dialog("close");
        	}
      	});
	}
	
	var table_titulaciones = new Atis.DataTable('#tableTitulacionesUsuario', {
		"ajax": { url: "<%=ControladorMisTitulaciones.URL_PATTERN_AJAX%>", async: false },
		"selectable": true,
	    "filterable": true,
	    "pageSize": 10,
	    "title": 'Titulaciones',
	    "action": "<%=ControladorMisTitulaciones.ACCION_DATATABLE_TITULACIONES_USUARIO%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}},
	    	{'data': 'descripcion'},
	        {'data': 'codnum', 'order': {'active': false}, 'class': 'overflow-ellipsis', 'render': function(row) {
	        	var link = "<%= ControladorMisTitulaciones.URL_PATTERN_FILES_PRIVADA %>"
	        	+ "?a=<%= ControladorMisTitulaciones.ACCION_DESCARGAR_FICHERO %>&<%= ControladorMisTitulaciones.PARAM_FICHERO %>=" + row.codNum;
	        	return "<a class='consultar-fichero' title='Descargar fichero' href='" +link +"' target='_blank'>" +link +"</a>"; 
	        	}
	        },
	        {'data': 'codnum', 'buttons': [{'label': '<label class="tooltiptext">Copiar enlace</label>Copiar', 'class': 'tooltip', 'onClick': function(row) {
		        	$(this).parent().parent().parent().find(".tooltiptext").text("¡Enlace copiado!");
		        	
		        	var link = "<%= ControladorMisTitulaciones.URL_PATTERN_FILES_PRIVADA %>"
			        	+ "?a=<%= ControladorMisTitulaciones.ACCION_DESCARGAR_FICHERO %>&<%= ControladorMisTitulaciones.PARAM_FICHERO %>=" + row.codNum;
		        	navigator.clipboard.writeText(link);
		        }
		    }]}
	    ],
	    "actions": [
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorMisTitulaciones.ACCION_ELIMINAR_TITULACION_USUARIO%>", selected); } }   	
	    ]
	});
});

</script>
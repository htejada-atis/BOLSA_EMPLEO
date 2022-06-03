<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisTitulaciones"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>


<div class="bolsa-empleo misdatos-form">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<div class="titulo-bolsa-empleo">
		<h2>Mis titulaciones</h2>
    
    	<% if (bean.getSePuedeAgregar()) { %>
	    	<button class="link-btn" id="nueva_titulacion">Añadir titulación</button>
	    <% } else { %>
			<p>La convocatoria se está evaluando o solicitud cerrada.</p>
		<% } %>
	</div>
		
	<table class="bluetable bolsaempleo" id="tableTitulacionesUsuarioMTI">
		<tr>
			<th scope="col" style="width:10%"></th>
			<th scope="col" style="width:60%">Titulación</th>		
			<th scope="col" style="width:40%">Descripción</th>	
			<th scope="col" style="width:10%">Validada</th>		
			<th scope="col" style="width:15%"></th>		
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
	
	<% if (bean.getSePuedeAgregar()) { %>
		document.getElementById('nueva_titulacion').addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorMisTitulaciones.ACCION_FORMULARIO_TITULACIONES_USUARIO %>'});
		});
	<% } %>
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las titulaciones', 'Seleccione al menos una titulación.');
			return;
		}
		Atis.confirmDialog("Borrar titulación", "¿Desea eliminar las titulaciones seleccionadas, relacionadas con su usuario?", {
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
	
	var table_titulaciones = new Atis.DataTable('#tableTitulacionesUsuarioMTI', {
		"ajax": { url: "<%=ControladorMisTitulaciones.URL_PATTERN_AJAX%>", async: false },
		"selectable": true,
	    "filterable": true,
	    "stateSave": true,
	    "pageSize": 10,
	    "action": "<%=ControladorMisTitulaciones.ACCION_DATATABLE_TITULACIONES_USUARIO%>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'titulacion.nombre', 'overflow': 'auto', 'filter': {'type': 'text'},
	    		'render': function(row) {
        		if (row.otratitulacion!=null) {
        			return "Otra titulación: " + row.otratitulacion; 
        		}
        		else{
        			return row.titulacion.nombre; 
        		}
    		}},
	    	{'data': 'descripcion'},
        	{'data': 'validada', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Validado', 'false': 'Pendiente'}},
	    		'render': function(row) {
	        		if (row.validada) {
	        			return "<div title='Validado' class='circle-true'></div>"; 
	        		}
        		}
	        },
        	{'data': 'codnum', 'buttons': [
        		{'label': 'Descargar', 'onClick': function(row) {
        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
        		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_TITULACION_CANDIDATO + "&" + ControladorDescargaFicheros.PARAM_TITULACION %>=" + row.codNum);
        		}},
   			]}	
	    ],
	    "actions": [
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorMisTitulaciones.ACCION_ELIMINAR_TITULACION_USUARIO%>", selected); } }   	
	    ]
	});
});

</script>

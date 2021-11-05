<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>

<div class="bolsa-empleo titulacionespreferentes">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Titulaciones</h2>
    
	    <button class="link-btn" id="nueva_titulacion">
	    	 Nueva titulación
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="table_titulaciones">
		<tr>
			<th scope="col" style="width:10%" title="Id de la titulación">Id</th>
			<th scope="col"	style="width:80%">Nombre</th>
			<th scope="col" style="width:10%"></th>
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

	$(document).ready(function() {
		
		var table_titulaciones = new Atis.DataTable('#table_titulaciones', {
		    "ajax": { url: "<%=ControladorGestionTitulaciones.URL_PATTERN_AJAX%>" },
		    "pageSize": 10,
		    "action": "<%=ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES%>",
		    "filterable": true,
		    "stateSave": true,
		    "defaultOrderBy": 1,
		    "columns": [
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		        {'data': 'nombre', 'filter': true, 'overflow': 'auto'},
		        {'data': 'codnum', 'buttons': [{'label': 'Borrar', 'title': 'Borrar titulación', 'onClick': function(row) {
		        	Atis.confirmDialog("Borrar titulación", "¿Desea borrar la titulación seleccionada?", {
				        Si: function() {
				        	var params = {
				    				'a': '<%=ControladorGestionTitulaciones.ACCION_BORRAR_TITULACION %>', 
				    				'<%=ControladorGestionTitulaciones.PARAM_ID%>': row.codNum};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				          	$(this).dialog("close");
				        },
				        No: function() {
				          	$(this).dialog("close");
				        }
				      });
		        	
		        } }]}		        
		    ],
		});
		
		document.getElementById("nueva_titulacion").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION %>'});
		});
		
	});
	
</script>

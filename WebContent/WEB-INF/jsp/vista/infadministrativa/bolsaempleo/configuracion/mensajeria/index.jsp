<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMensajes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMensajes"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMensajes bean = (VistaMensajes) uvdatos.getVistas().get(VistaMensajes.class.getName());
%>

<div class='bolsa-empleo mensajes'>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo" style="float:right; margin-top:1.2rem">
		<a class="link-btn" id="nuevo_mensaje" href="<%= request.getRequestURI() %>" style="margin-top:0">Nuevo Mensaje</a>
	</div>
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Mensajes</h2>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableMensajes">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:20%" title="Fecha creación">Fecha creación</th>
			<th scope="col" style="width:55%">Título</th>
			<th scope="col" class="center" style="width:15%">Estado</th>
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
	document.getElementById('nuevo_mensaje').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'<%= ControladorMensajes.PARAM_ACCION %>': '<%= ControladorMensajes.ACCION_NUEVO_MENSAJE %>'});
	});
	
	var borrar = function(row) {
		Atis.confirmDialog("Borrar mensaje", "¿Está seguro de borrar el mensaje?", {
         	'Borrar': function() {
         		console.log(row);
         			
          		var params = {
      				'<%= ControladorMensajes.PARAM_ACCION %>': '<%= ControladorMensajes.ACCION_BORRAR_MENSAJE %>',
      				'<%= ControladorMensajes.PARAM_MENSAJE_ID %>': row.codNum
     			};
     			Atis.sendForm("<%= ControladorMensajes.URL_PATTERN_AJAX %>", params);
     			
           		$(this).dialog("close");
           	},
           	'No': function() {
             	$(this).dialog("close");
           	}
        });
	};
	
	var clickRow = function(row) {
		var params = {
			'<%= ControladorMensajes.PARAM_ACCION %>': '<%= ControladorMensajes.ACCION_DETALLE_MENSAJE %>',
			'<%= ControladorMensajes.PARAM_MENSAJE_ID %>': row.codNum
    	};
		Atis.sendForm("<%=request.getRequestURI()%>", params);
	};
	
	var table = new Atis.DataTable('#tableMensajes', {
	    "ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>' },
	    "pageSize": 10,
    	"defaultOrderBy": 1,
    	"defaultOrderDirection": 'desc',
	    "action": "<%= ControladorMensajes.ACCION_DATATABLE %>",
	    "clickable": {'onClick': clickRow},
	    "columns": [
	    	{'data': 'codNum'},
	        {'data': 'fechaCreacion'},
	        {'data': 'titulo'},
	        {'data': 'estado'},
        	{'data': 'codnum', 'buttons': [
        		{'label': 'Editar', 'onClick': clickRow},
        		{'label': 'Borrar', 'visible': function(row) { return row.estado === 'BORRADOR'; }, 'onClick': borrar},
        	]}
	    ]	    
	});	
}); 
</script>

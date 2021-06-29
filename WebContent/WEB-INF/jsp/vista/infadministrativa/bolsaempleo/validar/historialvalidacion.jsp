<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
MeritoSolicitud merito = bean.getMerito();
Bolsa bolsa = bean.getBolsa();
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= descripcion %></h3>
	<h4><%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h4>
	
	<table class="bluetable bolsaempleo" id="tableHistorialValoracion">
		<tr>
			<th scope="col" style="width:15%">Fecha</th>
			<th scope="col" style="width:15%">Evaluador</th>
			<th scope="col" style="width:15%">Excluido</th>
			<th scope="col" style="width:15%">Validado</th>
			<th scope="col" style="width:15%">Observaciones</th>
			<th scope="col" style="width:20%">Valoración mérito</th>
			<th scope="col" style="width:15%">Tipo</th>
			<th scope="col" style="width:15%">Valor mérito</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<br/>
    <div class="btns-by-steps">
		<button class="link-btn" id="historial_volver">
	    	 Volver
	    </button>
	</div>
	
</div>
	
<script>
$(document).ready(function() {
	
	var tableHistorialValoracion = new Atis.DataTable('#tableHistorialValoracion', {
	    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>" },
	    "pageSize": 100,
	    "action": "<%= ControladorValidar.ACCION_DATATABLE_HISTORIAL_VALIDACION %>",
	    "title": 'Historial de validación del mérito: <%= merito.getMerito().getCodNum() %>',
	    "dropdown": true,
	    "params": {
	    	'<%=ControladorValidar.PARAM_MERITO%>': '<%= merito.getMerito().getCodNum() %>',
	    	'<%= ControladorValidar.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
			'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>'
		},
		"defaultOrderBy": 0,
		"defaultOrderDirection": 'desc',
	    "columns": [
	    	{'data': 'fecha'},
	    	{'data': 'evaluador'},
	        {'data': 'excluido', 'order': false},
	        {'data': 'validado', 'order': false},
	        {'data': 'observacionCandidato', 'order': false},
	        {'data': 'valoracionMerito', 'order': false},
	        {'data': 'codigoItem', 'order': false},
	        {'data': 'valorMerito', 'order': false},
	    ]
	});
	
	document.getElementById("historial_volver").addEventListener("click", function() {
		var params = {
				'<%= ControladorValidar.PARAM_ACCION %>': '<%= ControladorValidar.ACCION_MERITO_SELECCIONADO %>',
				'<%= ControladorValidar.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
				'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
				'<%= ControladorValidar.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>',
		}
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	});
	
}); 
</script>
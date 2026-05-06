<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.List" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAlegaciones"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAlegaciones bean = (VistaAlegaciones) uvdatos.getVistas().get(VistaAlegaciones.class.getName());
boolean isPersonal = ModeloRol.ID_ROL_SERVICIO_PERSONAL.equals(bean.getUsuarioLogeado().getRol().getCodNum());
%>

<div class='bolsa-empleo'>
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Alegaciones</h2>

	<% if (isPersonal) { %>
		<div class="be-info-box" role="note" aria-label="Información estados de las alegaciones" style="margin:0 0 1em;padding:0.8em;border-left:4px solid #2b6ca3;background:#f1f8ff;">
		    <ul>
		        <li><strong><%= ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION %></strong>: Alegación creada pero aún no presentada por el candidato.</li>
		        <li><strong><%= ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION %></strong>: Alegación presentada por el candidato; pendiente de revisión.</li>
		        <li><strong><%= ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION %></strong>: La alegación está siendo tramitada por el personal responsable.</li>
		        <li><strong><%= ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION %></strong>: Enviada al departamento competente para su resolución.</li>
		        <li><strong><%= ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION %></strong>: El departamento ha informado una resolución (fase intermedia).</li>
		        <li><strong><%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %></strong>: Resolución final registrada; expediente cerrado.</li>
		    </ul>
		</div>
	<% } %>

	<table class="bluetable bolsaempleo tablebolsas" id="tableAlegaciones">
		<tr>
			<th scope="col" style="width: 15%;">Curso</th>
			<th scope="col" class="area">Area</th>
			<th scope="col">Documento</th>
			<th scope="col" style="width: 28%;">Candidato</th>
			<th scope="col">Estado de la Alegación</th>
			<th scope="col">Acción</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {
	var cursoOptions = {};
    <% for (String curso : bean.getCursos()) {
        if (curso != null && curso.trim().length() > 0) {
    %>
        cursoOptions["<%= EscapaHTML.escapaHTML(curso) %>"] = "<%= EscapaHTML.escapaHTML(curso) %>";
    <%
        }
    }
    %>

	var table = new Atis.DataTable('#tableAlegaciones', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/alegaciones"},
		"pageSize": 10,
		"pageSizeOptions": [10, 100, 200],
		"filterable": true,
	    "action": "<%= ControladorAlegaciones.ACCION_DATATABLE_ALEGACIONES %>",
		"defaultOrderBy": 3,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
	    "columns": [
	    	{
                'data': 'convocatoria.curso',
                'filter': {
                    'type': 'select',
                    'options': cursoOptions,
                    'optionDefault': '<%= bean.getCursos() != null && !bean.getCursos().isEmpty() ? bean.getCursos().get(0) : "" %>'
                 }
            },
	        { 'data': 'area.descripcion', 'filter': true },
            { 'data': 'candidato.prsnif', 'filter': true },
            {
                'data': 'candidato',
                'order': false,
                'overflow': 'auto',
                'render': function(row) {
                    return row.candidato.nombre + " " + row.candidato.apellido1 + " " + row.candidato.apellido2;
                },
                'filter': true
            },
	        {
	            'data': 'estado',
	            'render': function(row) {
	                switch(row.estado) {
	                    case '<%= ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION %>':
	                        return 'PRESENTADA';
	                    case '<%= ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION %>':
	                        return 'EN RESOLUCIÓN';
	                    case '<%= ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION %>':
	                        return 'ENVIADA AL DEPARTAMENTO';
	                    case '<%= ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION %>':
	                        return 'INFORMADA';
	                    case '<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>':
	                        return 'RESUELTA';
	                    default:
	                        return row.estado; // En caso de valor inesperado
	                }
	            },
	            'filter': {
	                'type': 'select',
	                'options': {
	                    '<%= ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION %>': 'PRESENTADA',
	                    '<%= ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION %>': 'EN RESOLUCIÓN',
	                    '<%= ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION %>': 'ENVIADA AL DEPARTAMENTO',
	                    '<%= ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION %>': 'INFORMADA',
	                    '<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>': 'RESUELTA'
	                }
	            }
	        },
	        {
	            "data": "codNum",
	            "filter": false,
	            "buttons": [
	                {
	                    "label": "Ver Detalle",
	                    "title": "Ver los detalles de la alegación",
	                    "onClick": function(row) {
	                        var params = {
	                            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_VER_DETALLE_ALEGACION %>',
	                            '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': row.codNum
	                        };
	                        Atis.sendForm("<%= request.getRequestURI() %>", params);
	                    }
	                }
	            ]
	        }
	    ],
	});

	if (document.getElementById("filter_curso")) {
	    document.getElementById("filter_curso").onchange = function () {
	        var params = {
	            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_INDEX %>',
	            '<%= ControladorAlegaciones.PARAM_CONVOCATORIA %>': this.value
	        };
	        Atis.sendForm("<%= request.getRequestURI() %>", params);
	    };
	}

});
</script>
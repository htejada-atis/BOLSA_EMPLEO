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

<style>
	#tableAlegaciones {
		width: 100%;
		table-layout: fixed;
	}

	#tableAlegaciones th,
	#tableAlegaciones td {
		box-sizing: border-box;
		vertical-align: top;
		word-wrap: break-word;
		overflow-wrap: break-word;
	}

	#tableAlegaciones th {
		line-height: 1.2em;
	}

	#tableAlegaciones input,
	#tableAlegaciones select {
		max-width: 100%;
		box-sizing: border-box;
	}

	#tableAlegaciones .col-id {
	width: 8%;
}

#tableAlegaciones .col-curso {
	width: 10%;
}

#tableAlegaciones .col-area {
	width: 12%;
}

#tableAlegaciones .col-documento {
	width: 13%;
}

#tableAlegaciones .col-candidato {
	width: 17%;
}

#tableAlegaciones .col-estado {
	width: 15%;
}

#tableAlegaciones .col-fecha-envio {
	width: 13%;
}

#tableAlegaciones .col-accion {
	width: 12%;
	text-align: center;
}

	#tableAlegaciones td:nth-child(1),
	#tableAlegaciones td:nth-child(2),
	#tableAlegaciones td:nth-child(3),
	#tableAlegaciones td:nth-child(4),
	#tableAlegaciones td:nth-child(5),
	#tableAlegaciones td:nth-child(7),
	#tableAlegaciones td:nth-child(8) {
    	white-space: normal;
	}

	#tableAlegaciones td:nth-child(8) button,
	#tableAlegaciones td:nth-child(8) input[type="button"] {
		max-width: 100%;
		white-space: normal;
	}
	#tableAlegaciones td:nth-child(5) {
		white-space: normal;
		word-break: normal;
		overflow-wrap: break-word;
		line-height: 1.25em;
	}
	#tableAlegaciones td:nth-child(3) {
    	white-space: normal;
    	word-break: normal;
    	overflow-wrap: break-word;
    	line-height: 1.25em;
    	overflow: visible;
	}
	#tableAlegaciones td:nth-child(8) button,
#tableAlegaciones td:nth-child(8) input[type="button"] {
	min-width: 70px;
	max-width: 100%;
	white-space: normal;
	line-height: 1.1em;
}
</style>

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
		<colgroup>
			<col class="col-id">
			<col class="col-curso">
			<col class="col-area">
			<col class="col-documento">
			<col class="col-candidato">
			<col class="col-estado">
			<col class="col-fecha-envio">
			<col class="col-accion">
		</colgroup>
		<tr>
			<th scope="col" class="col-id">ID</th>
			<th scope="col" class="col-curso">Curso</th>
			<th scope="col" class="col-area">Área</th>
			<th scope="col" class="col-documento">Documento</th>
			<th scope="col" class="col-candidato">Candidato</th>
			<th scope="col" class="col-estado">Estado de la<br/>Alegación</th>
			<th scope="col" class="col-fecha-envio">Fecha envío<br/>departamento</th>
			<th scope="col" class="col-accion">Acción</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
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
		"defaultOrderBy": 4,
		"defaultOrderDirection": 'desc',
		"stateSave": true,
	    "columns": [
	    	{
	            'data': 'codNum',
	        },
	    	{
                'data': 'convocatoria.curso',
                'filter': {
                    'type': 'select',
                    'options': cursoOptions,
                    'optionDefault': '<%= bean.getCursos() != null && !bean.getCursos().isEmpty() ? bean.getCursos().get(0) : "" %>'
                 }
            },
	        {
	        	'data': 'area.descripcion',
	        	'filter': true,
	        },
            {
            	'data': 'candidato.prsnif',
            	'filter': true
            },
            {
                'data': 'candidato',
                'order': false,
                'render': function(row) {
                    if (!row.candidato) {
                        return '';
                    }

                    return [
                        row.candidato.nombre,
                        row.candidato.apellido1,
                        row.candidato.apellido2
                    ].filter(Boolean).join(' ');
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
	                        return row.estado || '';
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
	            'data': 'fechaEnvioDepartamento',
	            'filter': false,
	            'render': function(row) {
	                return row.fechaEnvioDepartamento ? row.fechaEnvioDepartamento : '-';
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
	    ]
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

<%@ page trimDirectiveWhitespaces="true"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisAlegaciones bean = (VistaMisAlegaciones) uvdatos.getVistas().get(VistaMisAlegaciones.class.getName());
%>

<div class='bolsa-empleo mis-alegaciones'>

    <jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

    <h2>Mis Alegaciones</h2>

    <div id="aviso" class="info aviso-alegaciones">
    	<div>
        	<p>Para crear una alegación, acceda desde la sección de 'Mis Resultados' y pulse el botón 'Crear Alegación'.</p>
        	<p>
            	Durante el plazo de alegaciones, podrá presentar alegaciones a la validación de los méritos realizada
            	por la comisión. Por cada mérito, podrá incluir una descripción y adjuntar archivos en formato PDF.
            	En el caso de que la alegación no esté referida a ningún mérito en particular podrá presentar una
            	alegación general en el campo establecido para ello.
        	</p>
        	<p>
            	Una vez finalizadas las alegaciones, podrá obtener una copia en formato PDF de lo presentado.
            	Recuerde que debe repetir el proceso para cada área de conocimiento en la que desee alegar.
        	</p>
        	<p>
            	Una vez revisada la alegación recibirá un aviso por correo electrónico indicándole que puede consultar
            	el PDF con la contestación a su alegación en el área de conocimiento correspondiente.
        	</p>
    	</div>
	</div>

    <table class="bluetable bolsaempleo tablebolsas" id="tableMisAlegaciones">
        <tr>
            <th scope="col" style="width: 15%;">Convocatoria</th>
            <th scope="col">Área</th>
            <th scope="col" style="width: 15%;">Estado</th>
            <th scope="col" style="width: 15%;">Fecha creación</th>
            <th scope="col" style="width: 15%;">Fecha presentación</th>
            <th scope="col" style="width: 10%;">Acción</th>
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

    var table = new Atis.DataTable('#tableMisAlegaciones', {
        "ajax": { url: "<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>" },
        "pageSize": 10,
        "pageSizeOptions": [10, 50, 100],
        "filterable": true,
        "action": "<%= ControladorMisAlegaciones.ACCION_DATATABLE_MIS_ALEGACIONES %>",
        "defaultOrderBy": 2,
        "defaultOrderDirection": 'desc',
        "stateSave": true,
        "columns": [
            { 'data': 'convocatoria.descripcion', 'filter': true },
            { 'data': 'area.descripcion', 'filter': true },
            {
                'data': 'estado',
                'render': function(row) {
                    switch(row.estado) {
                        case '<%= ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION %>':
                            return 'Pendiente';
                        case '<%= ModeloAlegaciones.ESTADO_EN_TRAMITACION_CANDIDATO %>':
                            return 'En tramitación';
                        case '<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>':
                            return 'Resuelta';
                        default:
                            return '';
                    }
                },
                'filter': {
                    'type': 'select',
                    'options': {
                        '<%= ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION %>': 'Pendiente',
                        '<%= ModeloAlegaciones.ESTADO_EN_TRAMITACION_CANDIDATO %>': 'En tramitación',
                        '<%= ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION %>': 'Resuelta'
                    }
                }
            },
            { 'data': 'fechaCreacion', 'filter': false },
            {
                'data': 'fechaConfirmacion',
                'filter': false,
                'render': function(row) {
                    return row.fechaConfirmacion ? row.fechaConfirmacion : '-';
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
                                '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_VER_DETALLE_ALEGACION %>',
                                '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': row.codNum
                            };
                            Atis.sendForm("<%= request.getRequestURI() %>", params);
                        }
                    }
                ]
            }
        ]
    });
});
</script>
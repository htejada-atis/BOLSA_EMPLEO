<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisAlegaciones beanAlegacionMerito = (VistaMisAlegaciones) uvdatos.getVistas().get(VistaMisAlegaciones.class.getName());
VistaMisResultados bean = beanAlegacionMerito.getBeanResultados();
Bolsa bolsa = bean.getBolsa();
Alegacion alegacion = beanAlegacionMerito.getAlegacion();
Convocatoria convocatoria = bean.getConvocatoria();
MeritoSolicitud meritoSolicitud = beanAlegacionMerito.getMeritoSolicitud();
String nombreMeritoAlegacion = bean.getItemBaremacion().getNombre();
SolMerBolAlegacion solicitudMerBolAlegacion = bean.getSolMerBolAlegacion();
boolean puedeEditar = ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado());
%>

<div class="bolsa-empleo">

    <jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<div class="row">
	    <button class="link-btn" id="volver_misalegaciones" style="float:left">Volver a Mis Alegaciones</button>
	</div>

    <br/>

    <h2>Editar Alegación Mérito</h2>
    <h4><%= EscapaHTML.escapa(nombreMeritoAlegacion) %></h4>

    <p>Describe tu alegación al mérito en la etiqueta <strong>Descripción</strong> y añade los documentos que consideres adecuados en formato PDF en la etiqueta <strong>Adjuntos PDF</strong>.</p>

    <br>

    <!-- Formulario para la Descripción -->
	<form id="agregar_merito" class="be-form" method="post" action="<%=request.getRequestURI()%>" <%= !puedeEditar ? "onsubmit=\"return false;\"" : "" %>>
	    <input type="hidden" id="accion_formulario" name="<%= ControladorMisAlegaciones.PARAM_ACCION %>"
	    	value="<%= ControladorMisAlegaciones.ACCION_AGREGAR_DESCRIPCION_ALEGACION_MERITO_CONFIRM %>" />
	    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />
	    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
	    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>" value="<%= convocatoria.getCodNum() %>" />
	    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>" value="<%= meritoSolicitud.getCodNum() %>" />

	    <div class="form-group-container col1">
	        <div class="form-group">
				<label for="descripcion_alegacion_merito">Descripción</label>
			    <textarea class="form-input-custom"
			              id="descripcion_alegacion_merito"
			              name="<%= ControladorMisAlegaciones.PARAM_DESCRIPCION_ALEGACION_MERITO %>"
			              rows="5"
			              maxlength="4000"
			              required
			              autocomplete="off"
			              <%= !puedeEditar ? "disabled=\"disabled\"" : "" %>
			              style="width: 100%; box-sizing: border-box;"><%= (solicitudMerBolAlegacion != null && solicitudMerBolAlegacion.getDescripcion() != null) ? EscapaHTML.escapa(solicitudMerBolAlegacion.getDescripcion()) : "" %>
			    </textarea>
			</div>
	    </div>

	    <% if (puedeEditar) { %>
            <div class="form-btn">
                <input id="descripcion_enviar" type="submit" name="<%= ControladorMisAlegaciones.PARAM_ENVIAR %>" value="Guardar cambios" />
            </div>
	    <% } %>
	</form>

	<% if (puedeEditar) { %>
		<% if (solicitudMerBolAlegacion == null || solicitudMerBolAlegacion.getDescripcion() == null) { %>
		    <div class="alert alert-warning" style="font-size: 1.2em; background-color: #fff3cd; color: #856404; border-color: #ffeeba; padding: 15px; border-radius: 5px; display: flex; align-items: center;">
			    <i class="fas fa-exclamation-triangle" style="margin-right: 10px; font-size: 1.5em;"></i>
		    	<span>No se podrá añadir ficheros para la alegación del mérito hasta que no se proporcione alguna descripción de la misma.</span>
			</div>
		<% } else { %>
		    <!-- Formulario para Adjuntos PDF -->
		    <form id="form_adjuntos" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
		    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>" value="<%= convocatoria.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>" value="<%= meritoSolicitud.getCodNum() %>" />

			    <!-- Campo de Archivo -->
			    <div class="form-group-container col1">
			        <div class="form-file">
			            <label for="fichero_archivo">Adjuntos PDF</label>
			            <br/>
			            <br/>
			            <input id="fichero_archivo" type="file" name="<%= ControladorMisAlegaciones.PARAM_ARCHIVO %>" accept=".pdf" required/>
			        </div>
			    </div>

			<!-- Botón de Enviar -->
			<div class="form-btn">
			    <input id="adjuntos_enviar" type="submit" name="<%= ControladorMisAlegaciones.PARAM_ENVIAR %>" value="Añadir Adjuntos"/>
			    </div>
			</form>

		    <!-- Tabla de archivos adjuntos -->
	        <table class="bluetable bolsaempleo" id="tableArchivosAlegacionesMerito" style="margin: 0;">
	            <thead>
	                <tr>
	                    <th scope="col" style="width:50px"></th>
			            <th scope="col" style="width:60px">Id</th>
			            <th scope="col" style="width:60%">Nombre archivo</th>
			    	<th scope="col" style="width:40%">Acción</th>
	                </tr>
	            </thead>
	            <tbody>
	            </tbody>
	            <tfoot>
	                <tr>
	                    <th colspan="4" style="width:100%"></th>
	                </tr>
	            </tfoot>
	        </table>
	    <% } %>
	<% } else { %>
    <!-- Archivos adjuntos (solo lectura) -->
    <table class="bluetable bolsaempleo" id="tableArchivosAlegacionesMerito" style="margin: 0;">
        <thead>
            <tr>
	            <th scope="col" style="width:60px">Id</th>
	            <th scope="col" style="width:60%">Nombre archivo</th>
	    		<th scope="col" style="width:40%">Acción</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
        <tfoot>
            <tr>
                <th colspan="3" style="width:100%"></th>
            </tr>
        </tfoot>
    </table>
	<% } %>
</div>

<script>
    $(document).ready(function() {
    	document.getElementById("volver_misalegaciones").addEventListener("click", function() {
            var params = {
                '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_VER_DETALLE_ALEGACION %>',
                '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
            };
            Atis.sendForm("<%= request.getRequestURI() %>", params);
        });

    <% if (puedeEditar && solicitudMerBolAlegacion != null && solicitudMerBolAlegacion.getDescripcion() != null) { %>
        var table = new Atis.DataTable('#tableArchivosAlegacionesMerito', {
            "ajax": { "url": "<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>" },
            "pageSize": 10,
            "action": "<%= ControladorMisAlegaciones.ACCION_DATATATABLE_ALEGACION_MERITO_FICHEROS %>",
            "pageSizeOptions": [10, 50, 100],
            "params": {
             	'<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>': encodeURIComponent('<%= meritoSolicitud.getCodNum() %>'),
             	'<%= ControladorMisAlegaciones.PARAM_BOLSA %>': encodeURIComponent('<%= bolsa.getCodNum() %>'),
             	'<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': encodeURIComponent('<%= bean.getConvocatoria().getCodNum() %>')
            },
            "columns": [
                {'data': 'id', 'selectable': true},
                {'data': 'id', 'filter': {'type': 'number'}},
                {'data': 'nombre', 'filter': true},
                {'data': 'id', 'buttons': [
	        		{'label': 'Descargar', 'title': 'Descargar fichero del mérito', 'onClick': function(row) {
	        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
	        		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACION_MERITO %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=" + encodeURIComponent(row.id));
	        		}},
	   			]}
            ],
            "actions": [
                {'label': 'Eliminar seleccionados', 'title': 'Eliminar archivos seleccionados', 'onClick': function(selected) {
                    if (selected.length) {
                        var mensaje = selected.length > 1 ? "¿Desea borrar los archivos seleccionados?" : "¿Desea borrar el archivo seleccionado?";
                        var titulo = "Borrar archivos";

                        Atis.confirmDialog(titulo, mensaje, {
                            Si: function() {
                                $.ajax({
                                    url: '<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>',
                                    type: 'POST',
                                    data: {
                                        '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_ELIMINAR_ARCHIVOS_ALEGECIONES %>',
                                        '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_MERITO %>': JSON.stringify(selected),
                                        '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
                                    },
                                    success: function() {
                                        table.refresh();
                                    },
                                    error: function() {
                                    	Atis.alertDialog("Error",  "Error al eliminar los archivos.");
                                    }
                                });
                                $(this).dialog("close");
                            },
                            No: function() {
                                $(this).dialog("close");
                            }
                        });
                    }
                }}
            ]
        });
    <% } else if (!puedeEditar) { %>
        var table = new Atis.DataTable('#tableArchivosAlegacionesMerito', {
            "ajax": { "url": "<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>" },
            "pageSize": 10,
            "action": "<%= ControladorMisAlegaciones.ACCION_DATATATABLE_ALEGACION_MERITO_FICHEROS %>",
            "pageSizeOptions": [10, 50, 100],
            "params": {
             	'<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>': encodeURIComponent('<%= meritoSolicitud.getCodNum() %>'),
             	'<%= ControladorMisAlegaciones.PARAM_BOLSA %>': encodeURIComponent('<%= bolsa.getCodNum() %>'),
             	'<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': encodeURIComponent('<%= bean.getConvocatoria().getCodNum() %>')
            },
            "columns": [
                {'data': 'id', 'filter': {'type': 'number'}},
                {'data': 'nombre', 'filter': true},
                {'data': 'id', 'buttons': [
	        		{'label': 'Descargar', 'title': 'Descargar fichero', 'onClick': function(row) {
	        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
	        		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACION_MERITO %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=" + encodeURIComponent(row.id));
	        		}}
	   			]}
            ]
        });
    <% } %>
    });
</script>
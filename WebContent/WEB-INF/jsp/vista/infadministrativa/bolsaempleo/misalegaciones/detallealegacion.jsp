<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisAlegaciones beanAlegaciones = (VistaMisAlegaciones) uvdatos.getVistas().get(VistaMisAlegaciones.class.getName());
VistaMisResultados bean = beanAlegaciones.getBeanResultados();
Convocatoria convocatoria = bean.getConvocatoria();
Alegacion alegacion = beanAlegaciones.getAlegacion();
Bolsa bolsa = bean.getBolsa();
BolsaResultado bolsaResultado = bean.getBolsaResultado();
MeritoPreferente meritoPreferente = bean.getMeritoPreferente();
UsuarioBolsaEmpleo candidato = bean.getUsuarioLogeado();
String acreditaciones = BolsaEmpleoUtils.clobToString(bolsaResultado.getAcreditacionesValidadas());
String titulaciones = BolsaEmpleoUtils.clobToString(bolsaResultado.getTitulacionesValidadas());
SolMerBolAlegacion solicitudMerBolAlegacion = bean.getSolMerBolAlegacion();
boolean puedeEditar = ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado());
boolean alegacionResuelta = ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION.equals(alegacion.getEstado());
%>

<div class='bolsa-empleo mis-alegaciones'>

    <jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

    <h2>Alegaciones del área: <%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h2>
	<h3>Alegaciones del candidato: <%= EscapaHTML.escapa(candidato.getPrsNif()) %></h3>

	<h4>Estado de la Alegación:
		<%
			String estadoAlegacion;
			switch (alegacion.getEstado()) {
				case ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION:
					estadoAlegacion = "Pendiente (no presentada)";
					break;
				case ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION:
					estadoAlegacion = "Presentada";
					break;
				case ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION:
					estadoAlegacion = "En resolución";
					break;
				case ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION:
					estadoAlegacion = "Enviada al departamento";
					break;
				case ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION:
					estadoAlegacion = "Informada";
					break;
				case ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION:
					estadoAlegacion = "Resuelta";
					break;
				default:
					estadoAlegacion = "Desconocido";
			}
		%>
		<%= estadoAlegacion %>
	</h4>

	<div class="row">
		<button class="link-btn" id="volver_misalegaciones" style="float:left">Volver a mis alegaciones</button>
		<% if (!puedeEditar) { %>
		    <div class="col">
		        <button class="link-btn" id="descargar_pdf_alegacion" style="float:right">Descargar PDF Alegación</button>
		        <% if (alegacionResuelta) { %>
		        	<button class="link-btn" id="descargar_pdf_resolucion" style="float:right;margin-right:8px">Descargar PDF Resolución</button>
				<% } %>
		    </div>
		<% } %>
		<% if (puedeEditar) { %>
		    <div class="col">
		        <button id="confirmar_alegacion" class="link-btn" style="float:right">Confirmar Alegación</button>
		    </div>
		<% } %>
	</div>

	<br/>

	<ul>
	    <li>Durante el estado de alegaciones, podrá presentar alegaciones a la validación de los méritos realizada por el equipo de valoración.
	        Por cada mérito, podrá incluir una descripción y adjuntar archivos en formato PDF.</li>
	    <li>Una vez finalizadas las alegaciones, podrá obtener una copia en formato PDF de lo presentado. Recuerde que debe repetir el proceso
	        para cada área de conocimiento en la que desee alegar.</li>
	</ul>

	<h4><%= ModeloResultados.MERITOS_VALIDADOS %></h4>

	<% if (bolsaResultado.getListaMeritos().size() > 0) { %>
	    <table class="bluetable bolsaempleo">
	        <tr>
	            <th scope="col" style="width:50px">Id. Mérito</th>
	            <th scope="col" style="width:60px">Cod. Mérito</th>
	            <th scope="col" style="width:60%">Tipo de Mérito</th>
	            <th scope="col" style="width:100px" title="Valor * Afinidad * Peso Categoría * Peso Bloque">Desglose</th>
	            <th scope="col" style="width:60px" title="Resultado de cada mérito para el área">Resultado</th>
	            <th scope="col" style="width:40%">Observación</th>
	            <th scope="col" style="width:80px">Alegación</th>
	        </tr>
	        <tbody>
	        <% for (MeritoResultado merito : bolsaResultado.getListaMeritos()) { %>
	        	<% boolean conMerito = merito.tieneAlegacion(); %>
	            <tr>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= merito.getCodNum() %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getDesglose()) %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= merito.getResultado() %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
	                <td>
	                	<% if (conMerito) { %>
                            <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Modificar alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Modificar</button>
                                <button class="btn eliminar_alegacion_merito" style="width:100%;margin-top:2px" title="Eliminar alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Eliminar</button>
                            <% } else { %>
                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Ver alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Ver</button>
                            <% } %>
                        <% } else if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
                            <button class="btn agregar_alegacion_merito" style="width:100%" title="Agregar alegación"
                            	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Agregar</button>
                        <% } %>
                    </td>
	            </tr>
	        <% } %>
	        </tbody>
	    </table>
	    <table class="bluetable bolsaempleo">
	        <tbody>
	            <tr>
	                <th scope="col" style="width:100%">Descripción</th>
	                <th scope="col" style="width:100px"></th>
	            </tr>
	            <tr>
	                <td>Total sin aplicar el máximo valor de las titulaciones preferentes y acreditaciones:</td>
	                <td><%= bolsaResultado.getTotalSinAplicar() %></td>
	            </tr>
	            <% if (bolsaResultado.getDesgloseTotal() != null) { %>
	                <tr>
	                    <td>Cálculo final: <%= EscapaHTML.escapa(bolsaResultado.getDesgloseDescripcion()) %></td>
	                    <td><%= EscapaHTML.escapa(bolsaResultado.getDesgloseTotal()) %></td>
	                </tr>
	            <% } %>
	            <tr>
	                <td><b>Total:</b></td>
	                <td><b><%= bolsaResultado.getTotal() %></b></td>
	            </tr>
	        </tbody>
	    </table>
	<% } else { %>
	    <p><%= ModeloResultados.MENSAJE_SIN_MERITOS_EVALUADOS %></p>
	<% } %>

	<h4><%= ModeloResultados.MERITOS_EXCLUIDOS %></h4>

	<% if (bolsaResultado.getListaMeritosExcluidos().size() > 0) { %>
	    <table class="bluetable bolsaempleo">
	        <tr>
	            <th scope="col" style="width:50px">Id. Mérito</th>
	            <th scope="col" style="width:60px">Cod. Mérito</th>
	            <th scope="col" style="width:60%">Tipo de Mérito</th>
	            <th scope="col" style="width:100px">Valor</th>
	            <th scope="col" style="width:40%">Observación</th>
	            <th scope="col" style="width:80px">Alegación</th>
	        </tr>
	        <tbody>
	            <% for (MeritoResultado merito : bolsaResultado.getListaMeritosExcluidos()) { %>
	            	<% boolean conMerito = merito.tieneAlegacion(); %>
	                <tr>
	                    <td class="<%= conMerito ? "bold-label" : "" %>"><%= merito.getCodNum() %></td>
	                    <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
	                    <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
	                    <td class="<%= conMerito ? "bold-label" : "" %>">
	                        <%= merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0
	                            ? merito.getValorMeritoSolicitud()
	                            : merito.getValor() %>
	                    </td>
	                    <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
	                    <td>
	                        <% if (merito.tieneAlegacion()) { %>
	                            <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
	                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Modificar alegación"
	                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Modificar</button>
	                                <button class="btn eliminar_alegacion_merito" style="width:100%;margin-top:2px" title="Eliminar alegación"
	                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Eliminar</button>
	                            <% } else { %>
	                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Ver alegación"
	                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Ver</button>
	                            <% } %>
	                        <% } else if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
	                            <button class="btn agregar_alegacion_merito" style="width:100%" title="Agregar alegación"
	                            	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Agregar</button>
	                        <% } %>
	                    </td>
	                </tr>
	            <% } %>
	        </tbody>
	    </table>
	<% } else { %>
	    <p><%= ModeloResultados.MENSAJE_SIN_MERITOS_EXCLUIDOS %></p>
	<% } %>

	<h4><%= ModeloResultados.MERITOS_NO_EVALUADOS %></h4>

	<% if (bolsaResultado.getListaMeritosNoEvaluados().size() > 0) { %>
	    <table class="bluetable bolsaempleo">
	        <tr>
	            <th scope="col" style="width:50px">Id. Mérito</th>
	            <th scope="col" style="width:60px">Cod. Mérito</th>
	            <th scope="col" style="width:60%">Tipo de Mérito</th>
	            <th scope="col" style="width:100px">Valor</th>
	            <th scope="col" style="width:40%">Observación</th>
	            <th scope="col" style="width:80px">Alegación</th>
	        </tr>
	        <tbody>
	        <% for (MeritoResultado merito : bolsaResultado.getListaMeritosNoEvaluados()) { %>
	        	<% boolean conMerito = merito.tieneAlegacion(); %>
	            <tr>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= merito.getCodNum() %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
	                <td class="<%= conMerito ? "bold-label" : "" %>">
	                    <%= merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0 ? merito.getValorMeritoSolicitud() : merito.getValor() %>
	                </td>
	                <td class="<%= conMerito ? "bold-label" : "" %>"><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
	                <td>
                        <% if (merito.tieneAlegacion()) { %>
                            <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Modificar alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Modificar</button>
                                <button class="btn eliminar_alegacion_merito" style="width:100%;margin-top:2px" title="Eliminar alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Eliminar</button>
                            <% } else { %>
                                <button class="btn agregar_alegacion_merito" style="width:100%" title="Ver alegación"
                                	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Ver</button>
                            <% } %>
                        <% } else if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
                            <button class="btn agregar_alegacion_merito" style="width:100%" title="Agregar alegación"
                            	data-id="<%= merito.getCodNumMeritoSolicitud() %>">Agregar</button>
                        <% } %>
                    </td>
	            </tr>
	        <% } %>
	        </tbody>
	    </table>
	<% } else { %>
	    <p><%= ModeloResultados.MENSAJE_SIN_MERITOS_NO_EVALUADOS %></p>
	<% } %>

	<h4>Titulaciones validadas</h4>
	<p><%= BolsaEmpleoUtils.escapaSaltosDeLinea(titulaciones.isEmpty() ? "No hay titulaciones validadas" : titulaciones) %></p>

	<h4>Acreditaciones validadas</h4>
	<p><%= BolsaEmpleoUtils.escapaSaltosDeLinea(acreditaciones.isEmpty() ? "No hay acreditaciones validadas" : acreditaciones) %></p>

	<!-- Título para la Alegación General -->
	<br/>
	<h3>Alegación general al área</h3>

	<% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
		<!-- Formulario para la Descripción -->
		<form id="agregar_merito" class="be-form" method="post" action="<%=request.getRequestURI()%>">
		    <input type="hidden" id="accion_formulario"
		           name="<%= ControladorMisAlegaciones.PARAM_ACCION %>"
		           value="<%= ControladorMisAlegaciones.ACCION_AGREGAR_DESCRIPCION_ALEGACION_CONFIRM %>" />
		    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
		    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>" value="<%= convocatoria.getCodNum() %>" />
		    <input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />

		    <div class="form-group-container col1">
		        <div class="form-group">
				    <label for="descripcion_alegacion" >Descripción</label>
				    <textarea class="form-input-custom"
				              id="descripcion_alegacion"
				              name="descripcionalegacionmerito"
				              rows="5"
				              maxlength="4000"
				              required
				              autocomplete="off"
				              style="width: 100%; box-sizing: border-box;"><%= solicitudMerBolAlegacion != null && solicitudMerBolAlegacion.getDescripcion() != null ? EscapaHTML.escapa(solicitudMerBolAlegacion.getDescripcion()) : "" %></textarea>
				    <input type="hidden" class="descripcion_oculta_general" id="descripcion_oculta_general"
				       value="<%= solicitudMerBolAlegacion != null && solicitudMerBolAlegacion.getDescripcion() != null ? EscapaHTML.escapa(solicitudMerBolAlegacion.getDescripcion()) : "" %>"
				    >
				</div>
		    </div>

		    <div class="form-btn">
		        <input id="descripcion_enviar" type="submit" name="<%=ControladorMisAlegaciones.PARAM_ENVIAR%>"
		               value="Guardar descripción general" />
		    </div>
		</form>

	    <!-- Formulario para Adjuntos PDF -->
	    <form id="form_adjuntos" class="be-form" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">
	    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
	    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>" value="<%= convocatoria.getCodNum() %>" />
	    	<input type="hidden" name="<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />
		    <div class="form-group-container col1">
		        <div class="form-file">
		            <label for="fichero_archivo">Añadir adjuntos PDF a la alegación general</label>
		            <br/>
		            <br/>
		            <input id="fichero_archivo" type="file" name="<%= ControladorMisAlegaciones.PARAM_ARCHIVO %>" accept=".pdf" required/>
		        </div>
		    </div>
		    <div class="form-btn">
		        <input id="adjuntos_enviar" type="submit" name="<%=ControladorMisAlegaciones.PARAM_ENVIAR%>" value="Añadir Adjuntos"/>
		    </div>
		</form>
	<% } else { %>
		<% if (solicitudMerBolAlegacion != null && solicitudMerBolAlegacion.getDescripcion() != null) { %>
			<div class="form-group-container col1">
				<div class="form-group">
					<label class="bold-label">Descripción</label>
					<p><%= EscapaHTML.escapa(solicitudMerBolAlegacion.getDescripcion()) %></p>
				</div>
			</div>
		<% } %>
	<% } %>

    <!-- Tabla de archivos adjuntos -->
    <table class="bluetable bolsaempleo" id="tableArchivosAlegaciones">
        <thead>
            <tr>
	            <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
	            	<th scope="col" style="width:50px"></th>
	            <% } %>
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
</div>

<script>
$(document).ready(function() {

	document.getElementById("volver_misalegaciones").addEventListener("click", function() {
        window.location.href = "<%= ControladorMisAlegaciones.URL_MIS_ALEGACIONES %>";
    });

	<% if (!puedeEditar) { %>
		document.getElementById("descargar_pdf_alegacion").addEventListener("click", function() {
			var url = "<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>" +
					"?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACIONES_RESULTADOS_SOLICITUD_CANDIDATO %>" +
					"&<%= ControladorDescargaFicheros.PARAM_BOLSA %>=<%= bolsa.getCodNum() %>" +
					"&<%= ControladorDescargaFicheros.PARAM_CONVOCATORIA %>=<%= convocatoria.getCodNum() %>";
			window.open(url, '_blank');
		});

		<% if (alegacionResuelta) { %>
			document.getElementById("descargar_pdf_resolucion").addEventListener("click", function() {
				var url = "<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>" +
						"?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_RESOLUCION_ALEGACION_RESULTADOS_SOLICITUD_CANDIDATO %>" +
						"&<%= ControladorDescargaFicheros.PARAM_BOLSA %>=<%= bolsa.getCodNum() %>" +
						"&<%= ControladorDescargaFicheros.PARAM_CONVOCATORIA %>=<%= convocatoria.getCodNum() %>";
				window.open(url, '_blank');
			});
		<% } %>
	<% } %>

	<% if (puedeEditar) { %>
		document.getElementById('confirmar_alegacion').addEventListener('click', function() {
			let todasDescripcionesVacias = true;

		    const descripcionGeneralOculta = document.querySelector('.descripcion_oculta_general');
		    if (descripcionGeneralOculta && descripcionGeneralOculta.value.trim() !== "") {
		        todasDescripcionesVacias = false;
		    }

		    const descripciones = document.querySelectorAll('.descripcion_oculta');
		    descripciones.forEach(function(descripcion) {
		        if (descripcion.value && descripcion.value.trim() !== "") {
		            todasDescripcionesVacias = false;
		        }
		    });
		    if (todasDescripcionesVacias) {
		        Atis.alertDialog(
		            "Error en la confirmación de la alegación",
		            "No se ha proporcionado ninguna descripción.<br/>Por favor, añade al menos una descripción general o para algún mérito antes de continuar con la confirmación de la alegación."
		        );
		    } else {
		        Atis.confirmDialog(
		            "Resumen de la alegación",
		            "¿Desea revisar el resumen de la alegación?<br/>Desde el resumen podrá confirmar la presentación de la alegación.", {
		            	'Sí': function() {
		                    $(this).dialog("close");
		                    var params = {
		                        '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_RESUMEN_ALEGACION %>',
		                        '<%= ControladorMisAlegaciones.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
		                        '<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': '<%= convocatoria.getCodNum() %>',
		                        '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
		                    };
		                    Atis.sendForm("<%= request.getRequestURI() %>", params);
		                },
		                'No': function() {
		                    $(this).dialog("close");
		                }
		            }
		        );
		    }
		});
	<% } %>

	$(document).on("click", ".agregar_alegacion_merito", function() {
        var meritoId = $(this).data("id");
        var params = {
                '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_ALEGACION_MERITO %>',
                '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>',
                '<%= ControladorMisAlegaciones.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
                '<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': '<%= convocatoria.getCodNum() %>',
                '<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>': meritoId
        };
        Atis.sendForm("<%= ControladorMisAlegaciones.URL_MIS_ALEGACIONES %>", params);
    });

	var table = new Atis.DataTable('#tableArchivosAlegaciones', {
        "ajax": { "url": "<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>" },
        "pageSize": 10,
        "action": "<%= ControladorMisAlegaciones.ACCION_DATATATABLE_ALEGACION_FICHEROS %>",
        "selectable": <%= puedeEditar ? "true" : "false" %>,
	    "filterable": true,
	    "stateSave": true,
	    "defaultOrderBy": 1,
	    "defaultOrderDirection": 'desc',
        "pageSizeOptions": [10, 50, 100],
        "params": {
        	'<%= ControladorMisAlegaciones.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
        	'<%= ControladorMisAlegaciones.PARAM_CONVOCATORIA %>': '<%= convocatoria.getCodNum() %>'
        },
        "columns": [
            <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
            	{'data': 'id', 'selectable': true},
            <% } %>
            {'data': 'id', 'filter': {'type': 'number'}},
            {'data': 'nombre', 'filter': true},
            {'data': 'id', 'buttons': [
        		{'label': 'Descargar', 'title': 'Descargar fichero', 'onClick': function(row) {
        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
        		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACION_MERITO %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=" + encodeURIComponent(row.id));
        		}}
   			]}
        ]
        <% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
        ,"actions": [
            {'label': 'Eliminar', 'title': 'Eliminar archivos seleccionados', 'onClick': function(selected) {
                if (selected.length) {
                    var mensaje = selected.length > 1 ? "¿Desea borrar los archivos seleccionados?" : "¿Desea borrar el archivo seleccionado?";

                    Atis.confirmDialog("Borrar archivos", mensaje, {
                        Si: function() {
                            $.ajax({
                                url: '<%= ControladorMisAlegaciones.URL_PATTERN_AJAX %>',
                                type: 'POST',
                                data: {
                                    '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_ELIMINAR_ARCHIVOS_ALEGECIONES %>',
                                    '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_MERITO %>': JSON.stringify(selected)
                                },
                                success: function() {
                                    table.refresh();
                                },
                                error: function() {
                                	Atis.alertDialog("Error", "Error al eliminar los archivos.");
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
        <% } %>
    });

	<% if (alegacion.getEstado().equals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION)) { %>
		$(document).on("click", ".eliminar_alegacion_merito",  function() {
	        var meritoId = this.getAttribute("data-id");
	        Atis.confirmDialog(
	            "Confirmar eliminación",
	            "¿Desea eliminar la alegación de este mérito?", {
	                'Sí': function() {
	                    var params = {
	                        '<%= ControladorMisAlegaciones.PARAM_ACCION %>': '<%= ControladorMisAlegaciones.ACCION_ELIMINAR_ALEGACION_FROM_MERITO %>',
	                        '<%= ControladorMisAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>',
	                        '<%= ControladorMisAlegaciones.PARAM_SOL_BOL_MERITO %>': meritoId
	                    };
	                    Atis.sendForm("<%= request.getRequestURI() %>", params);
	                    $(this).dialog("close");
	                },
	                'No': function() {
	                    $(this).dialog("close");
	                }
	            }
	        );
	    });
    <% } %>
});
</script>
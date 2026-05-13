<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.List"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAlegaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionMerito"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAlegaciones vistaAlegacionesBean = (VistaAlegaciones) uvdatos.getVistas().get(VistaAlegaciones.class.getName());
VistaMisResultados beanResultados = (VistaMisResultados) uvdatos.getVistas().get(VistaMisResultados.class.getName());
Alegacion alegacion = vistaAlegacionesBean.getAlegacion();
SolMerBolAlegacion solMerBolAlegacion = beanResultados.getSolMerBolAlegacion();
BolsaResultado bolsa = beanResultados.getBolsaResultado();
String usuarioName = alegacion.getCandidato() != null ? Formateador.leeParametroString(alegacion.getCandidato().getPrsNif()) + " " +
        Formateador.leeParametroString(alegacion.getCandidato().getNombre()) + " " +
        Formateador.leeParametroString(alegacion.getCandidato().getPrimerApellido()) + " " +
        Formateador.leeParametroString(alegacion.getCandidato().getSegundoApellido()) : "Usuario no disponible";
boolean isPersonal = vistaAlegacionesBean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL);
String candidatoNif = alegacion.getCandidato() != null ? Formateador.leeParametroString(alegacion.getCandidato().getPrsNif()) : "";
String candidatoCodNum = (alegacion.getCandidato() != null && alegacion.getCandidato().getCodNum() != null) ? alegacion.getCandidato().getCodNum().toString() : "";
String candidatoLink = "";
if (isPersonal && !candidatoCodNum.isEmpty()) {
    candidatoLink = request.getContextPath() + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/candidatos" +
        "?" + ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO +
        "&" + ControladorUsuarioCandidato.PARAM_CANDIDATO + "=" + candidatoCodNum;
}
String convocatoriaName = alegacion.getConvocatoria() != null ? alegacion.getConvocatoria().getDescripcion() : "Convocatoria no asignada";
String areaName = alegacion.getArea() != null ? alegacion.getArea().getDescripcion() : "Área no asignada";
String bolsaId = (beanResultados.getBolsa() != null && beanResultados.getBolsa().getCodNum() != null) ? beanResultados.getBolsa().getCodNum().toString() : "";
String convocatoriaCodNum = (alegacion.getConvocatoria() != null && alegacion.getConvocatoria().getCodNum() != null) ? alegacion.getConvocatoria().getCodNum().toString() : "";
boolean isDirector = vistaAlegacionesBean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO);
boolean estadoEnviadaDepartamento = ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION.equals(alegacion.getEstado());
boolean estadoInformada = ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION.equals(alegacion.getEstado());
boolean estadoResuelta = ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION.equals(alegacion.getEstado());
boolean estadoEnResulucion = ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION.equals(alegacion.getEstado());
boolean verResolucionDepartamento = isPersonal || isDirector;
boolean puedeEditarResolucionDepartamento = (isDirector && estadoEnviadaDepartamento) || (isPersonal && !estadoResuelta);
String accionDescargarAlegacion = isDirector ? ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACION_MERITO_POR_DIRECTOR : ControladorDescargaFicheros.ACCION_DESCARGAR_ALEGACION_MERITO_POR_PERSONAL;
String usuarioUltimaModificacion = alegacion.getCandidato() != null ? EscapaHTML.escapa(alegacion.getCandidato().getCodCuenta()) : "No disponible";
%>

<div class="detalle-alegacion">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

    <h2>Detalle Alegación</h2>

    <h3><%= usuarioName %></h3>
    <% if (isPersonal && !candidatoCodNum.isEmpty() && !candidatoNif.isEmpty()) { %>
        <h3>Resultados del candidato: <a class="bolsaempleo-link" href="<%= candidatoLink %>"><%= EscapaHTML.escapa(candidatoNif) %></a></h3>
    <% } else if (!candidatoNif.isEmpty()) { %>
        <h3>Resultados del candidato: <%= EscapaHTML.escapa(candidatoNif) %></h3>
    <% } else { %>
        <h3>Resultados del candidato: No disponible</h3>
    <% } %>
    <h3>Convocatoria: <%= convocatoriaName %></h3>
    <h3>Área: <%= areaName %></h3>

    <div class="row">
    	<button class="link-btn" id="volver_alegaciones" style="float:left">Volver</button>

    	<% if (isPersonal && ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION.equals(alegacion.getEstado())) { %>
        	<button class="link-btn" id="reabrir_alegacion" style="float:left; margin-left: 8px;">Reabrir alegación</button>
    	<% } %>

    	<% if (isDirector && estadoResuelta) { %>
        	<button class="link-btn" id="descargar_pdf_resolucion" style="float:right">
            	Descargar PDF Resolución
        	</button>
    	<% } %>
	</div>

	<br/>
	<br/>

	<%
		String estadoAlegacion;
		String estado = alegacion.getEstado();
		String descripcionEstado = null;

		if (estado == null) {
			estadoAlegacion = "Estado desconocido";
		} else {
			switch (estado) {
				case ModeloAlegaciones.ESTADO_PRESENTADA_ALEGACION:
					estadoAlegacion = "Presentada";
					descripcionEstado = "Alegación presentada por el candidato; pendiente de revisión.";
					break;
				case ModeloAlegaciones.ESTADO_ENRESOLUCION_ALEGACION:
					estadoAlegacion = "En resolución";
					descripcionEstado = "La alegación está siendo tramitada por el personal responsable.";
					break;
				case ModeloAlegaciones.ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION:
					estadoAlegacion = "Enviada al departamento";
					descripcionEstado = "Enviada al departamento competente para su resolución.";
					break;
				case ModeloAlegaciones.ESTADO_INFORMADA_ALEGACION:
					estadoAlegacion = "Informada";
					descripcionEstado = "El departamento ha informado una resolución (fase intermedia).";
					break;
				case ModeloAlegaciones.ESTADO_RESUELTA_ALEGACION:
					estadoAlegacion = "Resuelta";
					descripcionEstado = "Resolución final registrada; expediente cerrado.";
					break;
				case ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION:
					estadoAlegacion = "Pendiente (no presentada por el candidato)";
					descripcionEstado = "Alegación creada pero aún no presentada por el candidato.";
					break;
				default:
					estadoAlegacion = "Estado desconocido";
					break;
			}
		}
    %>

	<h4>Estado de la Alegación: <%= estadoAlegacion %>
		<% if (isPersonal && descripcionEstado != null) { %>
			<span title="<%= EscapaHTML.escapa(descripcionEstado) %>" style="display:inline-block; width:16px; height:16px; margin-left:6px; text-align:center; line-height:16px; background:#3a6ea5; color:#fff; font-weight:bold; border-radius:50%; cursor:help;">
				?
			</span>
		<% } %>
	</h4>

    <strong>Fecha Creación Alegación: <%= alegacion.getFechaCreacion() != null ? Formateador.formatoFecha(alegacion.getFechaCreacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "Fecha no disponible" %></strong><br/>
    <strong>Fecha Confirmación Alegación: <%= alegacion.getFechaConfirmacion() != null ? Formateador.formatoFecha(alegacion.getFechaConfirmacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "Fecha no disponible" %></strong><br/>
    <strong>Fecha Envío a departamento:
	    <%
	        String fechaEnvioDepartamento = "Sin enviar"; // Valor por defecto si no hay fecha

	        if (alegacion.getFechaResolucionDepartamento() != null) {
	            // Si existe la fecha, formateamos
	            fechaEnvioDepartamento = Formateador.formatoFecha(alegacion.getFechaEnvioDepartamento(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	        }
	    %>
	    <%= fechaEnvioDepartamento %>
	</strong><br/>
	<strong>Fecha Resolución Departamento:
	    <%
	        String fechaResolucionDepartamento = "Sin resolver"; // Valor por defecto si no hay fecha

	        if (alegacion.getFechaResolucionDepartamento() != null) {
	            // Si existe la fecha, formateamos
	            fechaResolucionDepartamento = Formateador.formatoFecha(alegacion.getFechaResolucionDepartamento(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	        }
	    %>
	    <%= fechaResolucionDepartamento %>
	</strong><br/>
	<strong>Fecha Resolución Final:
	    <%
	        String fechaResolucionFinal = "Sin resolver"; // Valor por defecto si no hay fecha

	        if (alegacion.getFechaResolucionFinal() != null) {
	            // Si existe la fecha, formateamos
	            fechaResolucionFinal = Formateador.formatoFecha(alegacion.getFechaResolucionFinal(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	        }
	    %>
	    <%= fechaResolucionFinal %>
	</strong>
	
	<br/>
	<br/>
	
	<strong>Último usuario que modificó la alegación: <%= usuarioUltimaModificacion %></strong>

	<br/>
	<br/>

    <div class="meritos-evaluados">
	    <h3>Méritos Evaluados</h3>

	    <% if (bolsa != null && bolsa.getListaMeritos() != null && !bolsa.getListaMeritos().isEmpty()) { %>
	        <%
	            for (MeritoResultado merito : bolsa.getListaMeritos()) {
	                ItemBaremacion itemBaremacion = merito.getItemMeritoSolicitud();
	                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null && !merito.getDescripcionAlegacion().trim().isEmpty();
	                boolean tieneFicheros = merito.getArchivosAlegacion() != null && !merito.getArchivosAlegacion().isEmpty();

	                if (!tieneDescripcion && !tieneFicheros) {
	                    // Si no hay descripción ni ficheros, omitimos este mérito
	                    continue;
	                }

	              	// Si el usuario es Director de Departamento y el mérito NO tiene afinidad, omitimos este mérito
	                if (vistaAlegacionesBean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO) &&
	                    (merito.getItemBaremacion().getAfinidad() == null ||
	                     merito.getItemBaremacion().getAfinidad().trim().isEmpty())) {
	                    continue;
	                }

	                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getBloqueBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getCodigo();
	        %>
	        <div class="merito-detalle">
	            <p><strong>ID del Mérito:</strong> <%= merito.getCodNum() %> /
	               <strong>Código del Mérito:</strong> <%= codigoItem %> /
	               <strong>Tipo de Mérito:</strong> <%= EscapaHTML.escapa(itemBaremacion.getNombre()) %>
	            </p>
	            <p><strong>Descripción de la Alegación:</strong></p>
	            <p><%= EscapaHTML.escapa(merito.getDescripcionAlegacion()) %></p>

	            <p><strong>Ficheros de la Alegación:</strong></p>
	            <% if (tieneFicheros) {
	                    for (ArchivoAlegacionMerito archivo : merito.getArchivosAlegacion()) {
	            %>
	                        <p>
	                            [<%= archivo.getId() %>] <%= EscapaHTML.escapa(archivo.getNombre()) %> -
	                            <a href="<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>?a=<%= accionDescargarAlegacion %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=<%= archivo.getId() %>"
                               title="Descargar fichero del mérito"
	                               target="_blank">
	                               Descargar
	                            </a>
	                        </p>
	            <%
	                    }
	                } else {
	            %>
	                <p>Sin ficheros asociados</p>
	            <% } %>
	        </div>
	        <hr/>
	        <% } %>
	    <% } else { %>
	        <p>No hay méritos evaluados.</p>
	    <% } %>
	</div>

	<div class="meritos-excluidos">
	    <h3>Méritos Excluidos</h3>

	    <% if (bolsa != null && bolsa.getListaMeritosExcluidos() != null && !bolsa.getListaMeritosExcluidos().isEmpty()) { %>
	        <% for (MeritoResultado merito : bolsa.getListaMeritosExcluidos()) {
	                ItemBaremacion itemBaremacion = merito.getItemBaremacion();

	                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null && !merito.getDescripcionAlegacion().trim().isEmpty();
	                boolean tieneFicheros = merito.getArchivosAlegacion() != null && !merito.getArchivosAlegacion().isEmpty();

	                if (!tieneDescripcion && !tieneFicheros) {
	                    continue;
	                }
	            	// Si el usuario es Director de Departamento y el mérito NO tiene afinidad, omitimos este mérito
	                if (vistaAlegacionesBean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO) &&
	                    (merito.getItemBaremacion().getAfinidad() == null ||
	                     merito.getItemBaremacion().getAfinidad().trim().isEmpty())) {
	                   	continue;
	                }

	                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getBloqueBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getCodigo();
	        %>
			        <div class="merito-detalle">
			            <p><strong>ID del Mérito:</strong> <%= merito.getCodNum() %> /
			               <strong>Código del Mérito:</strong> <%= codigoItem %> /
			               <strong>Tipo de Mérito:</strong> <%= EscapaHTML.escapa(itemBaremacion.getNombre()) %>
			            </p>

			            <p><strong>Descripción de la Alegación:</strong></p>
			            <p><%= EscapaHTML.escapa(merito.getDescripcionAlegacion()) %></p>

			            <p><strong>Ficheros de la Alegación:</strong></p>
			            <%
			                if (tieneFicheros) {
			                    for (ArchivoAlegacionMerito archivo : merito.getArchivosAlegacion()) {
			            %>
			                        <p>
			                            [<%= archivo.getId() %>] <%= EscapaHTML.escapa(archivo.getNombre()) %> -
			                            <a href="<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>?a=<%= accionDescargarAlegacion %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=<%= archivo.getId() %>"
			                               title="Descargar fichero del mérito"
			                               target="_blank">
			                               Descargar
			                            </a>
			                        </p>
			            <%
			                    }
			                } else {
			            %>
			                <p>Sin ficheros asociados</p>
			            <%
			                }
			            %>
			        </div>
	        		<hr/>
	        <% } %>
	    <% } else { %>
	        <p>No hay méritos excluidos.</p>
	    <% } %>
	</div>

    <div class="meritos-no-evaluados">
	    <h3>Méritos No Evaluados</h3>

	    <% if (bolsa != null && bolsa.getListaMeritosNoEvaluados() != null && !bolsa.getListaMeritosNoEvaluados().isEmpty()) { %>
	        <% for (MeritoResultado merito : bolsa.getListaMeritosNoEvaluados()) {
	                ItemBaremacion itemBaremacion = merito.getItemBaremacion();
	                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null && !merito.getDescripcionAlegacion().trim().isEmpty();
	                boolean tieneFicheros = merito.getArchivosAlegacion() != null && !merito.getArchivosAlegacion().isEmpty();

	                if (!tieneDescripcion && !tieneFicheros) {
	                    // Si no hay descripción ni ficheros, omitimos este mérito
	                    continue;
	                }
	              	// Si el usuario es Director de Departamento y el mérito NO tiene afinidad, omitimos este mérito
	                if (vistaAlegacionesBean.getUsuarioLogeado().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO) &&
	                    (merito.getItemBaremacion().getAfinidad() == null ||
	                     merito.getItemBaremacion().getAfinidad().trim().isEmpty())) {
	                    continue;
	                }

	                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getBloqueBaremacion().getCodigo() + "."
	                                    + itemBaremacion.getCodigo();
	        %>
		        <div class="merito-detalle">
		            <p>
		               <strong>ID del Mérito:</strong> <%= merito.getCodNum() %> /
		               <strong>Código del Mérito:</strong> <%= codigoItem %> /
		               <strong>Tipo de Mérito:</strong> <%= EscapaHTML.escapa(itemBaremacion.getNombre()) %>
		            </p>

		            <p><strong>Descripción de la Alegación:</strong></p>
		            <p><%= EscapaHTML.escapa(merito.getDescripcionAlegacion()) %></p>

					<p><strong>Ficheros de la Alegación:</strong></p>

		            <% if (tieneFicheros) {
		                    for (ArchivoAlegacionMerito archivo : merito.getArchivosAlegacion()) {
		            %>
		                        <p>
		                            [<%= archivo.getId() %>] <%= EscapaHTML.escapa(archivo.getNombre()) %> -
		                            <a href="<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>?a=<%= accionDescargarAlegacion %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=<%= archivo.getId() %>"
		                               title="Descargar fichero del mérito"
		                               target="_blank">
		                               Descargar
		                            </a>
		                        </p>
		            <%      }
		                } else {
		            %>
		                <p>Sin ficheros asociados</p>
		            <% } %>
		        </div>
	        	<hr/>
	        <% } %>
	    <% } else { %>
	        <p>No hay méritos no evaluados disponibles.</p>
	    <% } %>
	</div>

	<div class="alegacion-general">
	    <h3>Alegación General</h3>

	    <% if (solMerBolAlegacion != null) {
	            String descripcion = solMerBolAlegacion.getDescripcion();
	            if (descripcion == null || descripcion.isEmpty()) {
		        	        descripcion = "Sin descripción disponible";
	            }
	            List<ArchivoAlegacionGeneral> archivos = solMerBolAlegacion.getArchivosAlegacion();
	    %>
	        <p><strong>Descripción de la Alegación General:</strong></p>
	        <p><%= EscapaHTML.escapa(descripcion) %></p>

	        <p><strong>Ficheros de la Alegación General:</strong></p>
	        <% if (archivos != null && !archivos.isEmpty()) {
	                for (ArchivoAlegacionGeneral archivo : archivos) {
	        %>
			            <p>
		                [<%= archivo.getId() %>] <%= EscapaHTML.escapa(archivo.getNombre()) %> -
			                <a href="<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>?a=<%= accionDescargarAlegacion %>&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=<%= archivo.getId() %>"
		                   title="Descargar fichero de la alegación general"
			                   target="_blank">
			                   Descargar
			                </a>
			            </p>
	        <%      }
	           } else {
	        %>
	             <p>Sin ficheros asociados.</p>
	        <% } %>
	    <% } else { %>
	        <p>No hay información de la alegación general disponible.</p>
	    <% } %>
	</div>

	<br/>

	<% if (verResolucionDepartamento) { %>

		<div class="resolucion-departamento">
		    <h3>Resolución del Departamento</h3>

		    <form id="form_resolucion_departamento" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		        <input type="hidden"
		               id="accion_resolucion_departamento"
		               name="<%= ControladorAlegaciones.PARAM_ACCION %>"
		               value="<%= ControladorAlegaciones.ACCION_AGREGAR_DESCRIPCION_DEPARTAMENTO %>" />
		        <input type="hidden" name="<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />

		        <div class="form-group-container col1">
		            <div class="form-group">
		                <textarea class="form-input-custom" id="resolucion_departamento"
          					name="<%= ControladorAlegaciones.PARAM_DESCRIPCION_RESOLUCION_DEPARTAMENTO %>"
          					rows="8"
         					maxlength="4000"
          					<% if (!puedeEditarResolucionDepartamento) { %>
              					readonly
          					<% } %>
          					style="width: 100%; box-sizing: border-box;"><%= EscapaHTML.escapa(alegacion.getDesResolucionDep()) %></textarea>
		            </div>
		        </div>

				<% if (puedeEditarResolucionDepartamento) { %>
    				<div class="form-btn">
        				<input id="guardar_cambios_resolucion_departamento" 
               				type="submit"
               					name="<%= ControladorAlegaciones.PARAM_GUARDAR_CAMBIOS_RESOLUCION_DEPARTAMENTO %>"
               					class="btn-accion"
               					value="Guardar cambios"
        				/>
    				</div>
				<% } %>

				<% if (!estadoInformada && !estadoEnviadaDepartamento && isDirector) { %>
		            <div class="alert alert-warning" style="font-size: 1.2em; background-color: #fff3cd; color: #856404; border-color: #ffeeba; padding: 15px; border-radius: 5px; display: flex; align-items: center;">
		                <i class="fas fa-exclamation-triangle" style="margin-right: 10px; font-size: 1.5em;"></i>
		                <span>No se podrá confirmar la resolución del departamento hasta que la alegación está en estado 'Enviada al Departamento'.</span>
		            </div>
				<% } %>
		    </form>
		</div>
	<% } %>

	<% if (estadoEnviadaDepartamento && isDirector) { %>
          <button id="confirmar_resolucion_departamento" class="btn-accion">Confirmar Resolución del Departamento</button>
	<% } %>

	<% if (isPersonal) { %>
		<div class="resolucion-final">
		    <h3>Resolución Final</h3>

		    <form id="form_resolucion_final" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		    	<input type="hidden"
	               id="accion_resolucion"
	               name="<%= ControladorAlegaciones.PARAM_ACCION %>"
	               value="<%= ControladorAlegaciones.ACCION_AGREGAR_DESCRIPCION_RESOLUCION_FINAL %>" />
		        <input type="hidden" name="<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>" value="<%= alegacion.getCodNum() %>" />

		        <div class="form-group-container col1">
		            <div class="form-group">
		                <textarea class="form-input-custom"
						          id="resolucion_final"
						          name="<%= ControladorAlegaciones.PARAM_DESCRIPCION_RESOLUCION_FINAL %>"
						          rows="8"
						          maxlength="4000"
						          required
						          autocomplete="off"
						          style="width: 100%; box-sizing: border-box;"
								  <% if (estadoResuelta) { %> readonly <% } %>><%= EscapaHTML.escapa(alegacion.getDesResolucionFinal()) %></textarea>
		            </div>
		        </div>

		        <div class="form-btn">
		       		<%
			            // Verificar el estado de la alegación
						if (!estadoResuelta) {
			        %>
					        <input id="guardar_cambios_resolucion_final"
			                        type="submit"
			                        name="<%= ControladorAlegaciones.PARAM_GUARDAR_CAMBIOS_RESOLUCION_FINAL %>"
			                        class="btn-accion"
			                        value="Guardar cambios"/>
	                <% } %>
		        </div>
				<% if (!estadoEnResulucion && !estadoResuelta) { %>
				        <div class="alert alert-warning" style="font-size: 1.2em; background-color: #fff3cd; color: #856404; border-color: #ffeeba; padding: 15px; border-radius: 5px; display: flex; align-items: center;">
			                <i class="fas fa-exclamation-triangle" style="margin-right: 10px; font-size: 1.5em;"></i>
			                <span>No se podrá confirmar la resolución final hasta que la alegación no está en estado 'En resolución'.</span>
		            	</div>
			        <% } %>
		    </form>
		</div>
		<br/>
		 <!-- Botones superiores -->
	    <div class="botones-accion">
			<% if (estadoResuelta) { %>
	        	<button id="reabrir_resolucion" class="btn-accion">Reabrir resolución</button>
	        <% } else if (!estadoEnResulucion && !estadoResuelta) { %>
	        	<button id="marcar_resolucion" class="btn-accion">Marcar en resolución</button>
	        <% } %>

           	<% if (!estadoResuelta && !estadoEnviadaDepartamento) { %>
	        	<button id="enviar_departamento" class="btn-accion">Enviar al departamento</button>
	        <% } %>

			<% if (estadoEnResulucion) { %>
		       	<button id="confirmar_resolucion" class="btn-accion">Confirmar resolución final</button>
	        <% } %>
	    </div>
	<% } %>
</div>

<script>
$(document).ready(function() {
    document.getElementById("volver_alegaciones").addEventListener("click", function() {
        Atis.sendForm("<%= request.getRequestURI() %>", {
            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_INDEX %>'
        });
    });
    
    <% if (isDirector && estadoResuelta) { %>
    	var descargarPdfResolucion = document.getElementById("descargar_pdf_resolucion");
    	if (descargarPdfResolucion) {
        	descargarPdfResolucion.addEventListener("click", function() {
            	var url = "<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>" +
                    	"?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_RESOLUCION_ALEGACION_RESULTADOS_SOLICITUD_DIRECTOR %>" +
                    	"&<%= ControladorDescargaFicheros.PARAM_ALEGACION %>=<%= alegacion.getCodNum() %>";

            	window.open(url, "_blank");
        	});
    	}
	<% } %>

    var reabrirAlegacionBtn = document.getElementById("reabrir_alegacion");
    if (reabrirAlegacionBtn) {
        reabrirAlegacionBtn.addEventListener("click", function() {
            Atis.confirmDialog(
                "Reabrir alegación",
                "¿Desea reabrir esta alegación?<br/>El candidato podrá modificarla.", {
                    'Sí': function() {
                        var params = {
                            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_REABRIR_ALEGACION %>',
                            '<%= ControladorAlegaciones.PARAM_CANDIDATO %>': '<%= candidatoCodNum %>',
                            '<%= ControladorAlegaciones.PARAM_BOLSA %>': '<%= bolsaId %>',
                            '<%= ControladorAlegaciones.PARAM_CONVOCATORIA %>': '<%= convocatoriaCodNum %>',
                            '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }

    var marcarResolucion = document.getElementById("marcar_resolucion");
    if (marcarResolucion) {
    	marcarResolucion.addEventListener('click', function() {
        	Atis.confirmDialog(
	            "Marcar en Resolución",
	            "¿Estás seguro de marcar esta alegación en estado 'En resolución'?", {
	                'Sí': function() {
	                    var params = {
	                        '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_MARCAR_EN_RESOLUCION %>',
	                        '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }

 	// Botón para enviar al departamento
    var enviarDepartamento = document.getElementById("enviar_departamento");
    if (enviarDepartamento) {
        enviarDepartamento.addEventListener("click", function() {
            Atis.confirmDialog(
                "Enviar al Departamento",
                "¿Estás seguro de enviar esta alegación al departamento correspondiente?", {
                    'Sí': function() {
                        var params = {
                            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_ENVIAR_DEPARTAMENTO %>',
                            '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }

 	// Confirmar Resolución Final
    var confirmarResolucionDepartamento = document.getElementById("confirmar_resolucion_departamento");
    if (confirmarResolucionDepartamento) {
    	confirmarResolucionDepartamento.addEventListener("click", function() {
	        Atis.confirmDialog(
	            "Confirmar Resolución del Departamento",
	            "¿Estás seguro de confirmar la resolución del departamento de esta alegación?", {
	                'Sí': function() {
	                    var params = {
	                        '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_ENVIAR_RESOLUCION_DEPARTAMENTO %>',
	                        '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }

 	// Confirmar Resolución Final
    var confirmarResolucion = document.getElementById("confirmar_resolucion");
    if (confirmarResolucion) {
        confirmarResolucion.addEventListener("click", function() {
	        Atis.confirmDialog(
	            "Confirmar Resolución Final",
	            "¿Estás seguro de confirmar la resolución final de esta alegación?<br/>La alegación se marcará como RESUELTA", {
	                'Sí': function() {
	                    var params = {
	                        '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_ENVIAR_RESOLUCION_FINAL %>',
	                        '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }

    var reabrirResolucion = document.getElementById("reabrir_resolucion");
    if (reabrirResolucion) {
        reabrirResolucion.addEventListener("click", function() {
            Atis.confirmDialog(
                "Reabrir Resolución",
                "¿Estás seguro de reabrir esta alegación finalizada y dejarla en estado 'En resolución'?", {
                    'Sí': function() {
                        var params = {
                            '<%= ControladorAlegaciones.PARAM_ACCION %>': '<%= ControladorAlegaciones.ACCION_MARCAR_EN_RESOLUCION %>',
                            '<%= ControladorAlegaciones.PARAM_ALEGACIONES_ID %>': '<%= alegacion.getCodNum() %>'
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
    }
});
</script>
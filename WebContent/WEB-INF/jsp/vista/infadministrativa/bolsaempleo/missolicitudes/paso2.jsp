<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError()   %>
		</div>
	<% } %>
	
	<div class="titulo-bolsa-empleo">
		<h2>Paso 2: Asignación de méritos a áreas</h2>
		<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
		
		<p>Para cada área seleccione hasta un máximo de [<%= bean.getSolicitud().getConvocatoria().getNumMeritosPorBloque() %>] méritos por bloque</p>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col"	style="width:15%">Código</th>
			<th scope="col"	style="width:60%">Nombre</th>			
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>	
	
	<div class="btns-by-steps">
		<a class="link-btn" id="paso2_volver" href="<%= request.getRequestURI() %>">
	    	 Volver
	    </a>
	    <a class="link-btn" id="paso3_siguiente" href="<%= request.getRequestURI() %>">
	    	 Siguiente
	    </a>
	</div>
</div>

<script>

	$(document).ready(function() {
		var tableAreas = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "selectable": true,
		    "filterable": true,
		    "title": 'Mis Áreas para esta convocatoria',
		    "action": "<%= ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS %>",
		    "params": {"<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>": <%= bean.getSolicitud().getCodNum() %>},
		    "columns": [
		    	{'data': 'codNum', 'selectable': {'exclude': 'excluido'}},
		    	{'data': 'area.idAreaExterno', 'filter': true},
		    	{'data': 'area.descripcion', 'filter': true},		        
	        ],
		});
		
		document.getElementById("paso2_volver").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%= ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD %>',
					'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		document.getElementById("paso3_siguiente").addEventListener("click", function(event) {
			event.preventDefault();
			
		});
		
	});
	
</script>


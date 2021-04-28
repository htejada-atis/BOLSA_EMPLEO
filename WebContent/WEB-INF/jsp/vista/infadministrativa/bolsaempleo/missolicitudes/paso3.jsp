<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito" %>

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
	
	<h2>Paso 3: Resumen de solicitud y confirmación</h2>
	<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
	
	<p>Revise su solicitud para la convocatoria <%= bean.getSolicitud().getConvocatoria().getDescripcion() %>. Una vez confirmada no podrá ser editada</p>
	
	<% for (BolsaSolicitud bolsa: bean.getListaBolsasSolicitud()) { %>
		
		<h4>Área - <%= bolsa.getArea().getDescripcion() %></h4>
		<table class="bluetable bolsaempleo">
			<tr>
				<th scope="col"	style="width:15%">Cod. mérito</th>
				<th scope="col"	style="width:40%">Mérito</th>
				<th scope="col"	style="width:10%">Valor</th>			
				<th scope="col"	style="width:35%">Descripción</th>			
			</tr>
			<tbody>
				<% if (bolsa.getListaMeritos() != null && bolsa.getListaMeritos().size() > 0) { %>
					<% for (Merito merito: bolsa.getListaMeritos()) { 
							String codigoItem = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getItemBaremacion().getCodigo();
					%>
						<tr>
							<td><%= codigoItem %></td>
							<td><%= merito.getItemBaremacion().getNombre() %></td>
							<td><%= merito.getValor() %></td>
							<td><%= merito.getDescripcion() %></td>
						</tr>
					<% } %>
				<% } %>
			</tbody>		
		</table>
	<% } %>
	
	<% if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) { %>
		<div class="btns-by-steps">
			<a class="link-btn" id="paso3_volver" href="<%= request.getRequestURI() %>">
		    	 Volver
		    </a>
		    <a class="link-btn" id="paso3_confirmar" href="<%= request.getRequestURI() %>">
		    	 Confirmar Solicitud
		    </a>
		</div>				
	<% } else { %>
		<div class="btns-by-steps">
			<a class="link-btn" id="paso3_descargar" href="<%= request.getRequestURI() %>">
		    	 Descargar Solicitud
		    </a>
		</div>		
	<% } %>
</div>

<script>
	$(document).ready(function() {
		<% if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) { %>
			$('#paso3_volver').on('click', function(event) {
				event.preventDefault();
				var params = {
						'a': '<%= ControladorMisSolicitudes.ACCION_LISTAR_BOLSAS_SOLICITUD %>',
						'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			});
			$('#paso3_confirmar').on('click', function(event) {
				event.preventDefault();
				Atis.confirmDialog(
					"Confirmar solicitud", 
					"¿Desea confirmar la solicitud?<br/><br/>ATENCIÓN: NO PODRA EDITAR LA SOLICITUD<br/>DESPUES DE CONFIRMAR SU SOLICITUD", {
	            	'Confirmar': function(row) {
	            		var params = {
	            				'a': '<%= ControladorMisSolicitudes.ACCION_CONFIRMAR_SOLICITUD %>',
	            				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
	            				};
	        			Atis.sendForm("<%= request.getRequestURI() %>", params);
	              		$(this).dialog("close");
	            	},
	            	'No': function() {
	              		$(this).dialog("close");
	            	}
	          	});
			});
		<% } else { %>
			$('#paso3_descargar').on('click', function(event) {
				event.preventDefault();
				console.log("genera pdf y descargar");
			});
		<% } %>
	});
</script>

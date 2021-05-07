<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">	
	<%
		if (bean.getMensajesDeExito().size() > 0) {
		%>
		<div id="exito" class="success">
			<%=bean.formatearMensajesDeExito()%>
		</div>
	<%
	}
	%>
	
	<%
		if (bean.getMensajesDeError().size() > 0) {
		%>
		<div id="error" class="error">
			<%=bean.formatearMensajesDeError()%>
		</div>
	<%
	}
	%>
	
	<h2>Paso 3: Resumen de solicitud y confirmación</h2>
	<h3><%=bean.getSolicitud().getConvocatoria().getDescripcion()%></h3>
	
	<p>Revise su solicitud para la convocatoria <%=bean.getSolicitud().getConvocatoria().getDescripcion()%>. Una vez confirmada no podrá ser editada</p>
	
	<%
		for (BolsaSolicitud bolsa: bean.getListaBolsasSolicitud()) {
		%>
		
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
					<% for (MeritoSolicitud merito: bolsa.getListaMeritos()) { 
							String codigoItem = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getCodigo();
					%>
						<tr>
							<td><%= codigoItem %></td>
							<td><%= merito.getMerito().getItemBaremacion().getNombre() %></td>
							<td><%= merito.getMerito().getValor() %></td>
							<td><%= merito.getMerito().getDescripcion() %></td>
						</tr>
					<% } %>
				<% } else { %>
					<tr><td colspan="4"><%= ControladorMisSolicitudes.MENSAJE_AREA_SIN_MERITOS %></td><tr>
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
				
				var message = "";
				
				<% if (bean.getListaTitulaciones() != null && bean.getListaTitulaciones().size() > 0) { %>
					message = "¿Desea confirmar la solicitud?<br/><br/>";
					message += "Mis titulaciones: <br/>";
					
					<% for (Titulacion titulacion: bean.getListaTitulaciones()) { %>
						message += "<%= titulacion.getNombre() %><br/>";
					<% } %>
					
					message += "<br/>ATENCIÓN: NO PODRA EDITAR LA SOLICITUD<br/>DESPUES DE CONFIRMAR SU SOLICITUD.<br/><br/>";
					message += "Posteriormente podrá descargar una copia de su solicitud.<br/><br/>";
					
				<% } else { %>
					message = "No tiene ninguna titulación agregada. <br/> Para confirmar la solicitud debe tener al menos una titulación <br/>" 
							+ "agregada en el apartado 'Mis titulaciones'";
				<% } %>
				
				Atis.confirmDialog(
					"Confirmar solicitud", message, {
	            	'Confirmar': function(row) {
	            		var params = {
	            				'a': '<%= ControladorMisSolicitudes.ACCION_CONFIRMAR_SOLICITUD %>',
	            				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
	            				};
	        			Atis.sendForm("<%= request.getRequestURI() %>", params);
	        			
	        			$("a").attr("disabled", "disabled");
	        			document.getElementById("paso3_confirmar").innerHTML = "Generando solicitud...";
	        			
	              		$(this).dialog("close");
	            	},
	            	'No': function() {
	              		$(this).dialog("close");
	            	}
	          	});
			});
		<% } %>
		
	});
</script>

<%@page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
%>

<div class="bolsa-empleo">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resumen solicitud - <%= bean.getCandidato().getCodCuenta() %></h2>
	
	<h4>Convocatoria: <%= bean.getSolicitud().getConvocatoria().getDescripcion() %>&nbsp;(<%= bean.getSolicitud().getConvocatoria().getEstado() %>)</h4>
	<h4>Nombre: <%= bean.getCandidato().getNombre() + " " + bean.getCandidato().getPrimerApellido() + " " + bean.getCandidato().getSegundoApellido() %></h4>
	<h4>Nº de documento: <%= bean.getCandidato().getPrsNif() %></h4>
	<h4>Estado solicitud: <%= bean.getSolicitud().getEstado() %></h4>
	
	<% if (bean.getSolicitud().getExcluido()) { %>
		<h4>EXCLUIDA: <%= Formateador.formatoFecha(bean.getSolicitud().getFechaExclusion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) %> - <%= bean.getSolicitud().getRazonExclusion() %></h4>
	<% } %>
	
	<div class="form-group-container col2">
		<div class="form-group">
			<button class="link-btn" id="candidato_volver" style="float:left;">
		    	 Volver
		    </button>
		</div>
		<div class="form-group">
	
	<%	if (bean.getSolicitud().getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA) &&
			bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) { %>
		    <button class="link-btn" id="reabrir_solicitud" style="float:right; margin-left: 6px;">
		    	 Reabrir solicitud
		    </button>
	<%	} else if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)){ %>
			<button class="link-btn" id="cerrar_solicitud" style="float:right; margin-left: 6px;">
		    	 Cerrar solicitud
		    </button>
	<%	} %>
	
	<%  if (bean.getSolicitud().getExcluido()) { %>
			<button class="link-btn" id="incluir_solicitud" style="float:right; margin-left: 6px;">
		    	 Incluir solicitud
		    </button>
	<%  } else { %>
			<button class="link-btn" id="excluir_solicitud" style="float:right; margin-left: 6px;">
		    	 Excluir solicitud
		    </button>
	<%	} %>
		
	<%	if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) { %>
			<button class="link-btn icon icon-download" id="descargar_solicitud" style="float:right; padding: 1.5px 6px;">Descargar solicitud</button>
	<%	} %>
		</div>
	</div>
	
<%	for (BolsaSolicitud bolsa: bean.getListaBolsasSolicitud()) { %>
		
		<h4>Área - <%= bolsa.getArea().getDescripcion() %></h4>
		<table class="bluetable bolsaempleo table-solicitud">
			<tr>
				<th scope="col" style="width:8%">Id</th>
				<th scope="col"	style="width:15%">Cod. mérito</th>
				<th scope="col"	style="width:30%">Mérito</th>
				<th scope="col"	style="width:10%">Valor</th>
				<th scope="col"	style="width:30%">Descripción</th>
				<th scope="col" style="width:15%">Afinidad</th>
				<th scope="col" style="width:10%"></th>
			</tr>
			<tbody>
			<%	if (bolsa.getListaMeritos() != null && bolsa.getListaMeritos().size() > 0) { %>
				<%	for (MeritoSolicitudTable merito: bolsa.getListaMeritos()) {
						String codigoItem = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getMerito().getItemBaremacion().getCodigo();
					%>
						<tr>
							<td><%= merito.getCodNum() %></td>
							<td><%= codigoItem %></td>
							<td><%= merito.getMerito().getItemBaremacion().getNombre() %></td>
							<td><%= merito.getMerito().getValor() %></td>
							<td><%= merito.getMerito().getDescripcion() %></td>
							<td>
						<%	if (merito.getCodNum() != null && merito.getMeritoSolicitud() != null && merito.getMerito().getItemBaremacion().getAfinidad() != null) { %>
							<%	if (merito.getValoraciones().size() > 0) { %>
								<%	if (merito.getMerito().getItemBaremacion().getIndividualizado()) { %>
										<%= "<b>" + merito.getValoraciones().get(0).getAfinidad().getCodigo() + " " + merito.getValoraciones().get(0).getAfinidad().getModulacion() * 100 + "%</b>" %>
								<%	} else {
										for (MeritoSolicitudValoracion valoracion: merito.getValoraciones()) { %>
											<%= "<b>" + valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() * 100 + "%</b><br/>" %>
									<%	}
									} %>
							<%	} %>
						<%	} %>
							</td>
							<td>
								<button class="btn only-icon icon-download fichero-merito"
										title="Descargar fichero del mérito"
										type="button"
										data-merito="<%= merito.getMerito().getCodNum() %>"></button>
							</td>
						</tr>
				<%	} %>
			<%	} else { %>
					<tr><td colspan="7"><%= ControladorMisSolicitudes.MENSAJE_AREA_SIN_MERITOS %></td><tr>
			<%	} %>
			</tbody>
		</table>
<%	} %>
	
</div>

<script>

	$(document).ready(function() {
		
		$('.table-solicitud').on('click', '.fichero-merito', function() {
			var idMerito = $(this).data('merito');
			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + idMerito);
		});
		
	<%	if (bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) { %>
		
			document.getElementById("descargar_solicitud").addEventListener("click", function() {
				window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
			        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_SOLICITUD_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_SOLICITUD + "=" + bean.getSolicitud().getCodNum() %>");
			});
		
	<%	} else { %>
			document.getElementById("cerrar_solicitud").addEventListener("click", function() {
				Atis.confirmDialog("Confirmar solicitud", "¿Desea confirmar la solicitud del candidato?", {
			    	Si: function() {
			    		var params = {
	            				'<%= ControladorUsuarioCandidato.PARAM_ACCION %>': '<%= ControladorUsuarioCandidato.ACCION_CONFIRMAR_SOLICITUD %>',
	            				'<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>': '<%= bean.getSolicitud().getCodNum() %>',
	            				'<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>'
	           				};
		        			Atis.sendForm("<%= request.getRequestURI() %>", params);
			          	$(this).dialog("close");
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} %>
		
		document.getElementById("candidato_volver").addEventListener("click", function() {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_VOLVER_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>'
			});
		});
		
	<%	if (bean.getSolicitud().getConvocatoria().getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA) &&
			bean.getSolicitud().getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) { %>
			document.getElementById("reabrir_solicitud").addEventListener("click", function() {
				Atis.confirmDialog("Reabrir solicitud", "¿Desea reabrir la solicitud del candidato?", {
			    	Si: function() {
			    		Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_REABRIR_SOLICITUD%>',
							'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
							'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>'
						});
			          	$(this).dialog("close");
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} %>
	
	<%  if (bean.getSolicitud().getExcluido()) { %>	
			document.getElementById("incluir_solicitud").addEventListener("click", function() {
				var message = "<h3>¿Desea incluir la solicitud del candidato de esta convocatoria?</h3>";
				
				Atis.confirmDialog("Incluir solicitud", message, {
			    	Si: function() {
		    			Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_INCLUIR_SOLICITUD%>',
							'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
							'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>'
						});
			          	$(this).dialog("close");
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	
	<%  } else { %>
			document.getElementById("excluir_solicitud").addEventListener("click", function() {
				var message = 
					"<h3>¿Desea excluir la solicitud del candidato de esta convocatoria?</h3>" +
					"<p>Esta solicitud no entrará en el cálculo.</p><br/>" +
					"<div class='form-group-dialog'>" +
					"	<label for='razon_exclusion'>Razón de exclusión: </label>" +
					"	<textarea id='razon_exclusion' name='razonexclusion' rows='2' required='required'></textarea>" +
					"</div>";
				
				Atis.confirmDialog("Excluir solicitud", message, {
			    	Si: function() {
			    		var inputRazon = this.querySelector('#razon_exclusion');			    		
			    		if (inputRazon.value != '') {
				    		Atis.sendForm("<%= request.getRequestURI() %>", {
								'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_EXCLUIR_SOLICITUD%>',
								'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=bean.getCandidato().getCodNum()%>',
								'<%=ControladorUsuarioCandidato.PARAM_SOLICITUD%>': '<%=bean.getSolicitud().getCodNum()%>',
								'<%=ControladorUsuarioCandidato.PARAM_RAZON_EXCLUSION_SOLICITUD%>': inputRazon.value
							});
				          	$(this).dialog("close");
			    		} else {
			    			inputRazon.setCustomValidity("La razón de exclusión no puede estar vacía");
			    			inputRazon.reportValidity();
			    		}
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} %>
	
	});

</script>

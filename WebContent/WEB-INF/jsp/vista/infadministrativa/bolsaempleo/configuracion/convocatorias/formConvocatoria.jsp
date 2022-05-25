<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorConvocatorias" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria " %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatorias bean = (VistaConvocatorias) uvdatos.getVistas().get(VistaConvocatorias.class.getName());
Convocatoria convocatoria = bean.getConvocatoria();

boolean estadoAbierta = false;
boolean estadoCerrada = false;
boolean estadoFinalizada = false;

if (convocatoria != null) {
	estadoAbierta = convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA);
	estadoCerrada = convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA);
	estadoFinalizada = convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA);
}

%>

<div class="bolsa-empleo convocatoria-form">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	if (convocatoria != null) { %>
		<h2>Convocatoria - Id: <%= convocatoria.getCodNum() %></h2>
		
		<h4>Estado: <%= convocatoria.getEstado() %><br/>
			<%= convocatoria.getFechaCierre() != null ? "Fecha cierre: " + Formateador.formatoFecha(convocatoria.getFechaCierre(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %>
			<%= convocatoria.getFechaFinalizacion() != null ? "<br/>Fecha finalización: " + Formateador.formatoFecha(convocatoria.getFechaFinalizacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS) : "" %></h4>
			
		<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
<%	} else { %>
		<h2>Nueva convocatoria</h2>
		
		<p>Se creará un nueva convocatoria con el estado cerrada, para así revisar antes de abrirla: áreas a baremar, items de baremación, titulaciones y otras configuraciones.</p>
<%	} %>
	
	<form id="convocatoria_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		<input type="hidden" name="<%= ControladorConvocatorias.PARAM_ACCION %>" id="accion_formulario" value="<%= convocatoria != null ? ControladorConvocatorias.ACCION_MODIFICAR_CONVOCATORIA : ControladorConvocatorias.ACCION_AGREGAR_CONVOCATORIA %>" />
		<input type="hidden" name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>" id="convocatoria_id" value="<%= convocatoria != null ? convocatoria.getCodNum() : "" %>" />
		<div class="form-group-container col1">
			<div class="form-group">
				<label for="descripcion" class="bold-label">Descripción convocatoria: </label>
				<input id="descripcion"
					class="form-input-custom"
					type="text"
					name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_DESCRIPCION %>"
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_DESCRIPCION, convocatoria != null ? convocatoria.getDescripcion() : "") %>"
					required
					<%= estadoFinalizada ? " readonly disabled" : "" %>/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="curso" class="bold-label">Curso: (ej. 21/22)</label>
				<input id="curso"
					class="form-input-custom"
					type="text"
					autocomplete="off"
					name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_CURSO %>" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_CURSO, convocatoria != null ? convocatoria.getCurso() : "") %>"
					required
					<%= estadoFinalizada ? " readonly disabled" : "" %>/>
			</div>
			<div class="form-group">
				<label for="fechaCierre" class="bold-label">Fecha cierre: </label>
				<input id="fechaCierre"
					class="form-input-custom"
					type="text"
					autocomplete="off"
					name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_FECHACIERRE %>" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_FECHACIERRE, convocatoria != null ? Formateador.formatoFecha(convocatoria.getFechaCierre(), Formateador.FORMATO_FECHA_DDMMYYYY) : "") %>"
					required
					<%= estadoFinalizada ? " readonly disabled" : "" %>/>
			</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
				<label for="numMaximoBolsas" class="bold-label">Nº bolsa máximo de participación de candidato: </label>
				<input id="numMaximoBolsas"
					class="form-input-custom"
					type="text"
					name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO %>" 
					value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO, convocatoria != null ? convocatoria.getNumBolsasMaximo().toString() : "5") %>"
					required
					<%= estadoFinalizada ? " readonly disabled" : "" %>/>
			</div>
			<div class="form-group">
				<label for="numMeritosPorBloque" class="bold-label">Nº méritos por bloque: </label>
				<input id="numMeritosPorBloque"
					class="form-input-custom"
					type="text"
					name="<%= ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE %>"
					value="<%= convocatoria != null ? convocatoria.getNumMeritosPorBloque() : "10" %>"
					required
					<%= estadoFinalizada ? " readonly disabled" : "" %>/>
			</div>
		</div>
		
	<%	if (!estadoFinalizada) { %>
			<div class="form-group-container col2">
				<div class="form-group">
				<%	if (bean.getConvocatoria() != null) { %>
						<button id="convocatoria_eliminar" style="float:left;">Eliminar</button>
				<%	} %>
				</div>
				<div class="form-group">
					<input id="convocatoria_enviar"
						type="submit"
						name="<%= ControladorConvocatorias.PARAM_ACCION_ENVIAR %>"
						value="<%= convocatoria != null ? "Guardar cambios" : "Insertar convocatoria" %>"
						style="float:right;"/>
					<%	if (estadoAbierta) { %>
							<button id="convocatoria_estado_cerrada" style="float:right; margin-right: 12px;">Cerrar</button>
					<%	} else if (estadoCerrada) { %>
							<button id="convocatoria_estado_finalizada" style="float:right; margin-right: 12px;">Finalizar</button>
							<button id="convocatoria_estado_abierta" style="float:right; margin-right: 12px;">Abrir</button>
					<%	}  %>
				</div>
			</div>
	<%	} else { %>
			<div class="form-group-container col1">
				<div class="form-group">
					<button id="convocatoria_estado_reabrir_finalizada" style="float:right; margin-right: 12px;">Poner en cerrada</button>
				</div>
			</div>
	<%  } %>
	</form>
</div>

<script>
	
	$(document).ready(function() {
		$("#fechaCierre").datepicker();
		
		$('#convocatoria_form').submit(function(event) {
			$('#convocatoria_enviar').prop('disabled', true);
			$('#convocatoria_enviar').attr('value', '<%= convocatoria != null ? "Guardando" : "Creando" %> convocatoria...');
			return true;
		});
		
	<%	if (!estadoFinalizada && bean.getConvocatoria() != null) { %>
			document.getElementById("convocatoria_eliminar").addEventListener("click", function(e) {
				e.preventDefault();
				Atis.confirmDialog("Eliminar convocatoria", "¿Desea eliminar la convocatoria?", {
					'Si': function() {
						var params = {
								"<%= ControladorConvocatorias.PARAM_ACCION %>": "<%= ControladorConvocatorias.ACCION_BORRAR_CONVOCATORIA %>",
								"<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>": <%= bean.getConvocatoria().getCodNum() %>
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				});
			});
		<%	if (estadoAbierta) { %>
				document.getElementById("convocatoria_estado_cerrada").addEventListener("click", function(e) {
					e.preventDefault();
					Atis.confirmDialog("Cambiar estado de la convocatoria", "La convocatoria pasará a estado cerrada.", {
						'Si': function() {
							var params = {
									"<%= ControladorConvocatorias.PARAM_ACCION %>": "<%=ControladorConvocatorias.ACCION_CERRAR_CONVOCATORIA %>",
									"<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>": <%= bean.getConvocatoria().getCodNum() %>
							};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
							$(this).dialog("close");
						},
						'No': function() {
							$(this).dialog("close");
						}
					});
				});
		<%	} else { %>
				document.getElementById("convocatoria_estado_finalizada").addEventListener("click", function(e) {
					e.preventDefault();
					Atis.confirmDialog("Cambiar estado de la convocatoria", "La convocatoria pasará a estado finalizada. Una vez finalizada ya no se podrá alterar.", {
						'Si': function() {
							var params = {
									"<%= ControladorConvocatorias.PARAM_ACCION %>": "<%=ControladorConvocatorias.ACCION_FINALIZAR_CONVOCATORIA %>",
									"<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>": <%= bean.getConvocatoria().getCodNum() %>
							};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
							$(this).dialog("close");
						},
						'No': function() {
							$(this).dialog("close");
						}
					});
				});
				
				document.getElementById("convocatoria_estado_abierta").addEventListener("click", function(e) {
					e.preventDefault();
					Atis.confirmDialog("Cambiar estado de la convocatoria", "La convocatoria pasará a estado abierta.<br/><br/>Revise áreas a baremar, titulaciones preferentes, items de baremación etc.<br/><br/>Recuerde que el estado de las bolsas debe ser 'desbloqueado'.", {
						'Si': function() {
							var params = {
									"<%= ControladorConvocatorias.PARAM_ACCION %>": "<%=ControladorConvocatorias.ACCION_ABRIR_CONVOCATORIA %>",
									"<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>": <%= bean.getConvocatoria().getCodNum() %>
							};
							Atis.sendForm("<%= request.getRequestURI() %>", params);
							$(this).dialog("close");
						},
						'No': function() {
							$(this).dialog("close");
						}
					});
				});
		<%	} %>
	<%	} else if (estadoFinalizada && bean.getConvocatoria() != null) { %>
			document.getElementById("convocatoria_estado_reabrir_finalizada").addEventListener("click", function(e) {
				e.preventDefault();
				Atis.confirmDialog("Cambiar estado de la convocatoria", "La convocatoria pasará a estado cerrada.", {
					'Si': function() {
						var params = {
								"<%= ControladorConvocatorias.PARAM_ACCION %>": "<%=ControladorConvocatorias.ACCION_REABRIR_FINALIZADA_CONVOCATORIA %>",
								"<%= ControladorConvocatorias.PARAM_CONVOCATORIA_ID %>": <%= bean.getConvocatoria().getCodNum() %>
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				});
			});
	<%	} %>
	});
</script>
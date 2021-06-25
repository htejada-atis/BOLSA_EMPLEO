<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="java.util.stream.Collectors" %>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
Bolsa bolsa = bean.getBolsa();
MeritoSolicitud merito = bean.getMerito();
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria() != null) {
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
<% if (merito == null) { %>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	} %>
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= EscapaHTML.escapa(descripcion) %></h3>
	<h4><%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h4>
	
	<table class="bluetable bolsaempleo" id="tableCandidatos">
		<tr>
			<th scope="col" style="width:10%">D.N.I</th>
			<th scope="col" style="width:50%" class="nombre">Nombre</th>
			<th scope="col" style="width:10%" class="center">No validados</th>
			<th scope="col" style="width:10%" class="center">Validados</th>
			<th scope="col" style="width:10%" class="center">Excluidos</th>
			<th scope="col" style="width:10%" class="center">Total</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<% if (candidato != null) { %>
	
		<table class="bluetable bolsaempleo" id="tableMeritos">
			<tr>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:10%">Estado</th>
				<th scope="col" style="width:20%">Código</th>
				<th scope="col" style="width:50%">Valor</th>
				<th scope="col" style="width:10%">Fichero</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="5" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
		<% if (merito != null) { %>
			<div id="anchor_modificar_merito" style="margin-bottom: 24px;"></div>
			
			<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
			
			<h3>Modificar mérito: <%= merito.getMerito().getCodNum() %></h3>
			
			<form id="validar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		    	<input type="hidden" name="<%= ControladorValidar.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorValidar.ACCION_MODIFICAR_MERITO %>" />
		    	<input type="hidden" name="<%= ControladorValidar.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidar.PARAM_CANDIDATO %>" value="<%= candidato.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidar.PARAM_MERITO %>" value="<%= merito.getMerito().getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidar.PARAM_BOLSAS %>" value="[<%= bolsa.getCodNum() %>]" />
		    	<div class="form-group-container col2">
			    	<div class="form-group">
						<label class="bold-label" for="select_item">Categoría:</label>
						<select class="form-input-custom" id="select_item" name="<%= ControladorValidar.PARAM_ITEM %>">
							<%
							for(ItemBaremacion it: bean.getItems()) {
							%>
								<% if (it.getCodNum() == merito.getMerito().getItemBaremacion().getCodNum()) { %>
				    				<option value="<%=it.getCodNum()%>" 
				    						data-unidades="<%= it.getUnidades() %>" 
				    						data-descripcion="<%= it.getDescripcion() %>" 
				    						selected><%=EscapaHTML.escapa(it.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre())%></option>
				    			<% } else { %>
				    				<option value="<%=it.getCodNum()%>" 
				    						data-unidades="<%= it.getUnidades() %>" 
				    						data-descripcion="<%= it.getDescripcion() %>"><%=EscapaHTML.escapa(merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre())%></option>
				    			<% } %>
				    		<%
				    		}
				    		%>
						</select>
					</div>
					<div class="form-group">
			    		<div class="form-group">
			    			<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor:</label>
			    			<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorValidar.PARAM_VALOR %>" value="<%= merito.getMerito().getValor() %>" required/>
			    		</div>
		    		</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-file">
						<label for="merito_archivo" class="bold-label">Fichero:</label>
						<button id="merito_descargar_fichero" class="btn icon icon-download" title="Descargar fichero del mérito" type="button">Descargar fichero del mérito</button>
					</div>
				</div>
				
				<div class="form-group-container col2">
		    		<div class="form-group">
		    			<label for="merito_descripcion" class="bold-label">Descripción:</label>
		    			<textarea class="form-input-custom" id="merito_descripcion" name="" disabled><%= EscapaHTML.escapa(merito.getMerito().getDescripcion()) %></textarea>
		    		</div>
		    		<div class="form-group">
			    		<label for="merito_observacion_comision">Observación para la comisión:</label>
			    		<textarea class="form-input-custom" id="merito_observacion_comision" name="" rows="2" cols="50" disabled><%= EscapaHTML.escapa(merito.getMerito().getObservacion()) %></textarea>
			    	</div>
				</div>
			
		    	<div class="form-btn">
		    		<input id="merito_guardar" type="submit" name="<%= ControladorValidar.PARAM_GUARDAR_MERITO %>" value="Guardar"/>
		    	</div>
		    </form>
		    
		    <h3>Afinidades del mérito en las distintas áreas</h3>
		    
		    <p>
		    	En la siguiente tabla se muestran las áreas de su departamento en las que este mérito está baremado, y aquellas áreas donde el candidato
		     	está inscrito pero el mérito aún no tiene ningún valor. Debe introducir para cada área la afinidad, la observación para el candidato y la acción
		     	a realizar con este mérito en esa área. Los datos de categoría y valor serán los mismos en las distintas áreas de su departamento. 
		    </p>
		    
		    <div>Leyenda del campo 'Acciones':</div>
		    <ul>
		    	<li>Aceptar: Pasar este mérito a estado aceptado. Esto quiere decir que el mérito será tenido en cuenta en la baremación.</li>
		    	<li>Excluir: Pasar este mérito a estado excluido. Esto quiere decir que el mérito no será tenido en cuenta en la baremación.</li>
		    </ul>
		    <br/>
		    <div>Descripción de afinidades:</div>
		    <ul>
		    <%	for(Afinidad afinidad: bean.getListaAfinidades()) {
		    		if (afinidad.getCodigo().equals(merito.getMerito().getItemBaremacion().getAfinidad())) {
		    %>
			    		<li><%= EscapaHTML.escapa(afinidad.getCodigo() + " " + afinidad.getModulacion() * 100 + "%" + " - " + afinidad.getDescripcion()) %></li>
		    <%		}
		    	} %>
		    </ul>
		   
		    
		    <table class="bluetable bolsaempleo" id="tableValoresMeritoBolsas">
				<tr>
					<th scope="col" style="width:20%">Área</th>
					<th scope="col" style="width:10%">Valor</th>
					<th scope="col" style="width:10%">Estado</th>
					<th scope="col" style="width:30%">Afinidad a aplicar</th>
					<th scope="col" style="width:25%">Observación para el candidato</th>
					<th scope="col" style="width:10%">Acciones</th>
				</tr>
				<tbody>
				<%	for (ValorMeritoBolsaTable meritoBolsa: bean.getBolsas()) {
						int idMerito = meritoBolsa.getMeritoSolicitud().getMerito().getCodNum();
						Boolean valoraciones = meritoBolsa.getMeritoSolicitud() != null && meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad() != null
								&& meritoBolsa.getMeritoSolicitud().getValoraciones() != null && meritoBolsa.getMeritoSolicitud().getValoraciones().size() > 0  ? true : false;
						Boolean individualizado = meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getIndividualizado() ? true : false;
					%>
					
						<tr data-individualizado="<%= individualizado %>" data-bolsa="<%= meritoBolsa.getBolsa().getCodNum() %>" 
								data-afinidad="<%= meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad() %>">
							<td><b><%= EscapaHTML.escapa(meritoBolsa.getBolsa().getArea().getIdAreaExterno()) %></b><br/>
								<%= EscapaHTML.escapa(meritoBolsa.getBolsa().getArea().getDescripcion()) %>
							</td>
							<td><%= meritoBolsa.getMeritoSolicitud().getMerito().getValor() %></td>
							<td>
								<% if (meritoBolsa.getMeritoSolicitud().isValidado()) { %>
			        				<div class='text-success'><b>Validado</b></div>
			        			<% } else if (meritoBolsa.getMeritoSolicitud().isExcluido()) { %>
			        				<div class='text-danger'><b>Excluido</b></div>
			        			<% } else { %>
			        				<div class='text-primary'><b>Sin validar</b></div>
			        			<% } %>
							</td>
							<td class='cell-afinidad'>
							<%	if (individualizado) { %>
									<select id="afinidad-individualizada" data-valoracion="<%= valoraciones ? meritoBolsa.getMeritoSolicitud().getValoraciones().get(0).getCodNum() : "0" %>">
									<% 	for (Afinidad afinidad : bean.getListaAfinidades()) { %>
										<%	if (afinidad.getCodigo().equals(meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad())) { %>
											<% 	if (valoraciones && meritoBolsa.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getCodNum() == afinidad.getCodNum()) { %>
												<option value="<%= afinidad.getCodNum() %>" selected><%= afinidad.getModulacion() * 100 + "%: " + afinidad.getDescripcion() %></option>
											<%	} else { %>
												<option value="<%= afinidad.getCodNum() %>"><%= afinidad.getModulacion()* 100 + "%: " + afinidad.getDescripcion() %></option>
											<%	} %>
										<%	} %>
									<%	} %>
									</select>
							<%	} else { %>
									<table class="table-afinidades">
											<tbody>
								<%	int total = 0;
									if (valoraciones) {
										for (MeritoSolicitudValoracion valoracion: meritoBolsa.getMeritoSolicitud().getValoraciones()) {
												total += valoracion.getValor();	%>
												<tr>
													<td style="width:70px">
														<label for="<%= "merito_valoracion_" + valoracion.getAfinidad().getCodNum() + "_" + idMerito %>">
															<i class="tooltip"><%=  EscapaHTML.escapa(valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() * 100 + "%: ") %><span><%= EscapaHTML.escapa(valoracion.getAfinidad().getDescripcion()) %></span></i>
														</label>
													</td>
													<td>
														<input id="<%= "merito_valoracion_" + valoracion.getAfinidad().getCodNum() + "_" + idMerito %>" data-afinidad="<%= valoracion.getAfinidad().getCodNum() %>" class="validar-afinidad field-no-individualizado" type="number" min="0" 
															value="<%= valoracion.getValor() %>" step="0.01"/>
													</td>
												</tr>
										<%	} %>
												
								<%	} else { %>
									<% 	for (int i = 0; i < bean.getListaAfinidades().size(); i++) {
											Afinidad afinidad = bean.getListaAfinidades().get(i); %>
										<%	if (afinidad.getCodigo().equals(meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad())) { %>
												<tr>
													<td style="width:70px">
														<label for="merito_valoracion_<%= i %>">
															<i class="tooltip"><%= EscapaHTML.escapa(afinidad.getCodigo() + " " + afinidad.getModulacion() * 100 + "%: ") %><span><%= EscapaHTML.escapa(afinidad.getDescripcion()) %></span></i>
														</label>
													</td>
													<td>
														<input id="merito_valoracion_<%= i %>" data-afinidad="<%= afinidad.getCodNum() %>" class="validar-afinidad field-no-individualizado" type="number" min="0" 
															value="0" step="0.01"/>
													</td>
												</tr>
										<%	} %>
									<%	} %>
								<%	} %>
										<tr>
											<td style="width:70px">Total:</td>
											<td>
												<input class="validar-afinidad total-no-individualizado" data-total="<%= meritoBolsa.getMeritoSolicitud().getMerito().getValor() %>" type="number" min="0" name="total"
												value="<%= total %>" disabled/>
											</td>
										</tr>
									</tbody>
								</table>
								
								<p class="mensaje" style="color: red; display: none;">El total es superior al valor del mérito.</p>
							<%	} %>
							</td>
							<td>
				    			<textarea class="form-input-custom merito-observacion-candidato" 
				    				id="merito_observacion_candidato_<%= idMerito %>"
				    				name="<%= ControladorValidar.PARAM_OBSERVACION_CANDIDATO %>" 
				    				rows="2" cols="50"
				    				><%= meritoBolsa.getMeritoSolicitud().getObservacionCandidato() != null ? meritoBolsa.getMeritoSolicitud().getObservacionCandidato() : "" %></textarea>
							</td>
							<td style="vertical-align: middle;">
								<span class="btns acciones_merito">
						    		<button class="btn merito-aceptar" id="merito_aceptar_<%= idMerito %>" name="<%= ControladorValidar.PARAM_ACEPTAR_MERITO %>">Aceptar</button>
							    	<button class="btn merito-excluir" id="merito_excluir_<%= idMerito %>" name="<%= ControladorValidar.PARAM_EXCLUIR_MERITO %>">Excluir</button>
					    		</span>
							</td>
						</tr>
				<%	} %>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="6" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
		    
		<% } else { %>
			<p>Elija un mérito para cargar el formulario de edición</p>
		<% } %>
		
	<% } %>
	
</div>

<script>
$(document).ready(function() {
	var tableCandidatos = new Atis.DataTable('#tableCandidatos', {
	    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>", async: false },
	    "pageSize": 200,
	    "pageSizeOptions": [5,10,20,100,200],
	    "action": "<%= ControladorValidar.ACCION_DATATABLE_CANDIDATOS %>",
	    "title": 'LISTA DE USUARIOS',
	    "dropdown": true,
	    "params": {'<%=ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'<%= ControladorValidar.PARAM_ACCION %>': '<%= ControladorValidar.ACCION_CANDIDATO_SELECCIONADO %>',
    				'<%= ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
    				'<%= ControladorValidar.PARAM_CANDIDATO %>': row.codNum
    				};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    <% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
	    "columns": [
	    	{'data': 'prsnif'},
	    	{'data': 'apellido1', 'order': false, 'overflow': 'auto', 'render': function(row) {
        		return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
        	}},
	        {'data': 'totalMeritosNoValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosNoValidados) ? 0 : row.totalMeritosNoValidados; } },
	        {'data': 'totalMeritosValidados', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosValidados) ? 0 : row.totalMeritosValidados; } },
	        {'data': 'totalMeritosExcluidos', 'order': false, 'class': 'center', 'render': function(row) { return isNaN(row.totalMeritosExcluidos) ? 0 : row.totalMeritosExcluidos; } },
	        {'data': 'totalMeritos', 'order': false, 'class': 'center'}
	    ]
	});
	
	<% if (candidato != null) { %>
	
		var tableMeritos = new Atis.DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 100,
		    "filterable": true,
		    "action": "<%= ControladorValidar.ACCION_DATATABLE_MERITOS %>",
		    "title": 'MÉRITOS PARA EL USUARIO: <%=EscapaHTML.escapa(candidato.getIdNif() + " - " + candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido())%>',
		    "dropdown": true,
		    "params": {
		    	'<%=ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
		    	'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>'
		    	},
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorValidar.ACCION_MERITO_SELECCIONADO %>',
	    				'<%= ControladorValidar.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
	    				'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
	    				'<%= ControladorValidar.PARAM_MERITO %>': row.codNum};
	    		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    <% if (merito != null) { %> "selected": <%= merito.getMerito().getCodNum() %> ,<% } %>
		    "columns": [
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		        {'data': 'codNum', 'order': false, 'class': 'center', 'render': function(row) {
		        		if (!row.excluido && !row.validado) {
		        			return "<div title='Mérito no evaluado' class='circle-neutral'></div>";
		        		} else {
		        			if (row.excluido) {
			        			return "<div title='Mérito excluido' class='circle-false'></div>";
			        		} else {
			        			return "<div title='Mérito validado' class='circle-true'></div>";
			        		}
		        		}
		        	}
		    	},
		        {'data': 'item', 'filter': true, 'render': function(row) {
		        	return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
	        	}},
	        	{'data': 'valor', 'filter': true, 'overflow': 'auto', 'render': function(row) {
	        		return row.valor + " (" + row.item.unidades + ")";
	        	}},
	        	{'data': 'codnum', 'buttons': [
	        		{'title': 'Descargar fichero del mérito', 'class': 'only-icon icon-download', 'onClick': function(row) {
	        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
	        		        	+ "?a=<%= bean.getUsuarioLogeado().isMiembroComision() ? ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_COMISION : ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + row.codNum);
	        		}},
	   			]}
		    ]
		});
		
	<%	if (merito != null) { %>
		
			function NoIndividualizado(cell) {
				var self = this;
				
				this.getValoresAfinidades = function() {
					var noIndividualizadas = {};
					$(cell).find('.field-no-individualizado').each(function(index, input) {
						var afinidad = $(input).data('afinidad');
						var value = parseFloat($(input).val());
						noIndividualizadas[afinidad] = value;
					});
					return noIndividualizadas;
				}
				
				this.sumaTotal = function() {
					var total = 0.0;
					var noIndividualizadas = self.getValoresAfinidades();	
					Object.keys(noIndividualizadas).forEach(function(key) {
						total += noIndividualizadas[key];
					});	
					return total;
				}
				
				this.actualizaTotal = function() {
					var inputTotal = $(cell).find('.total-no-individualizado');
					var total = Atis.redondearFloat(self.sumaTotal());
					
					$(inputTotal).val(total);
				}
				
				this.comparaTotal = function(inferior) {
					var inputTotal = $(cell).find('.total-no-individualizado');
					var inputMensaje = $(cell).find('.mensaje');
					var total = Atis.redondearFloat(self.sumaTotal());
					var valorMerito = Atis.redondearFloat(inputTotal.data('total'));
					
					if (total > valorMerito) {
						$(inputMensaje).text('El total es superior al valor del mérito. Actual ' + total + ' Máximo ' + valorMerito);
						$(inputMensaje).show();
						return false;
					} else if (valorMerito > total && inferior) {
						$(inputMensaje).text('El total es inferior al valor del mérito. Actual ' + total + ' Valor ' + valorMerito);
						$(inputMensaje).show();
						return false;
					}
					
					$(inputMensaje).hide();
					
					return true;
				}
			}
			
			function onClickAccionMerito(btn, action) {
				var tr = $(btn).closest('tr');
				var observacionCandidato = $(tr).find('.merito-observacion-candidato').val();
				
				var afinidades = {};
				if (tr.data('individualizado')) {
					var selectAfinidad = $(tr).find('select');
					afinidades[selectAfinidad.val()] = selectAfinidad.data('valoracion');
				} else {
					var noIndividualizado = new NoIndividualizado(tr.find('.cell-afinidad'));
					noIndividualizado.comparaTotal(tr.data('valor'));
					if (!noIndividualizado.comparaTotal(true)) {
						return;
					}
					afinidades = noIndividualizado.getValoresAfinidades();
				}
				
				var idBolsa = tr.data('bolsa');
				
				var params = {
						'<%= ControladorValidar.PARAM_ACCION %>': '<%= ControladorValidar.ACCION_VALIDAR_MERITO %>',
						'<%= ControladorValidar.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
						'<%= ControladorValidar.PARAM_BOLSA_MERITO %>': idBolsa,
						'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
						'<%= ControladorValidar.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>',
						'<%= ControladorValidar.PARAM_OBSERVACION_CANDIDATO %>': observacionCandidato,
						'<%= ControladorValidar.PARAM_AFINIDADES %>': Atis.object2Json(afinidades),
						[action]: true
				}
				Atis.sendForm("<%=request.getRequestURI()%>", params);
			}
			
			document.getElementById("merito_descargar_fichero").addEventListener("click", function() {
				window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
    		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_MERITO + "=" + merito.getMerito().getCodNum() %>");
			});
		
			$('.bluetable').on('input', '.field-no-individualizado', function() {
				var noIndividualizado = new NoIndividualizado($(this).closest('table').closest('td'));
				noIndividualizado.actualizaTotal();
				noIndividualizado.comparaTotal(false);
			});
			
			$('.bluetable').on('click', 'button.merito-aceptar', function() {
				onClickAccionMerito(this, '<%= ControladorValidar.PARAM_ACEPTAR_MERITO %>');
			});
			
			$('.bluetable').on('click', 'button.merito-excluir', function() {
				var self = this;
				Atis.confirmDialog(
					"Excluir mérito", "El mérito se va a excluir de todas las áreas que pueda evaluar.", {
	            	'Si': function(row) {
	            		onClickAccionMerito(self, '<%= ControladorValidar.PARAM_EXCLUIR_MERITO %>');
	        			
	              		$(this).dialog("close");
	            	},
	            	'No': function() {
	              		$(this).dialog("close");
	            	}
	          	});
				
			});
			
			Atis.smoothScrollToAnchor("#anchor_modificar_merito");
	<%	} %>
		
<%	} %>
	
}); 
</script>

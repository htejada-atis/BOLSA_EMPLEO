<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

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
			descripcion = "Última convocatoria: " + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Validar meritos sujetos afinidad</h2>
	<h3><%= descripcion %></h3>
	<h4><%= bolsa.getArea().getDescripcion() %></h4>
	
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
				    						selected><%=it.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre()%></option>
				    			<% } else { %>
				    				<option value="<%=it.getCodNum()%>" 
				    						data-unidades="<%= it.getUnidades() %>" 
				    						data-descripcion="<%= it.getDescripcion() %>"><%=merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + it.getBloqueBaremacion().getCodigo() + "." + it.getCodigo() + "-" + it.getNombre()%></option>
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
						<button class="btn icon icon-download" title="Descargar fichero del mérito" type="button">Descargar fichero del mérito</button>
					</div>
				</div>
				
				<div class="form-group-container col2">
		    		<div class="form-group">
		    			<label for="merito_descripcion" class="bold-label">Descripción:</label>
		    			<textarea class="form-input-custom" id="merito_descripcion" name="" disabled><%= merito.getMerito().getDescripcion() %></textarea>
		    		</div>
		    		<div class="form-group">
			    		<label for="merito_observacion_comision">Observación para la comisión:</label>
			    		<textarea class="form-input-custom" id="merito_observacion_comision" name="" rows="2" cols="50" disabled><%= merito.getMerito().getObservacion() %></textarea>
			    	</div>
				</div>
			
		    	<div class="form-btn">
		    		<input id="merito_guardar" type="submit" name="<%= ControladorValidar.PARAM_GUARDAR_MERITO %>" value="Guardar"/>
		    	</div>
		    </form>
		    
		    <p>
		    	En la siguiente tabla se muestran las áreas de su departamento en las que este mérito está baremado, y aquellas áreas donde el candidato
		     	está inscrito pero el mérito aún no tiene ningún valor. Debe introducir para cada área la afinidad, la observación para el candidato y la acción
		     	a realizar con este mérito en esa área. Los datos de categoría y valor serán los mismos en las distintas áreas de su departamento. 
		      	<br/><br/>
		    	Leyenda del campo 'Acciones a realizar con el mérito'
			    <ul>
			    	<li>Guardar: Guardar los cambios efectuados sobre este mérito. El mérito mantiene su estado actual (aceptado o excluido).</li>
			    	<li>Aceptar: Pasar este mérito a estado aceptado. Esto quiere decir que el mérito será tenido en cuenta en la baremación.</li>
			    	<li>Excluir: Pasar este mérito a estado excluido. Esto quiere decir que el mérito no será tenido en cuenta en la baremación.</li>
			    </ul>
		    </p>
		    
		    <table class="bluetable bolsaempleo" id="tableValoresMeritoBolsas">
				<tr>
					<th scope="col" style="width:20%">Área</th>
					<th scope="col" style="width:20%">Datos actuales</th>
					<th scope="col" style="width:25%">Afinidad a aplicar</th>
					<th scope="col" style="width:25%">Observaciones</th>
					<th scope="col" style="width:10%"></th>
				</tr>
				<tbody>
					<% for (ValorMeritoBolsaTable meritoBolsa: bean.getBolsas()) { 
							int idMerito = meritoBolsa.getMeritoSolicitud().getMerito().getCodNum();
					%>
					
						<tr>
							<td><b><%= meritoBolsa.getBolsa().getArea().getIdAreaExterno() %></b><br/>
								<%= meritoBolsa.getBolsa().getArea().getDescripcion() %>
							</td>
							<td><b>Valor: </b><%= meritoBolsa.getMeritoSolicitud().getMerito().getValor() %><br/>
								<b>Afinidad: </b>
								<br/>
								<% if (meritoBolsa.getMeritoSolicitud() != null && meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad() != null && meritoBolsa.getMeritoSolicitud().getValoraciones() != null) { %>
									<% if (meritoBolsa.getMeritoSolicitud().getValoraciones().size() > 0) { %>
										<% if (meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getIndividualizado()) { %>
											<%= meritoBolsa.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getCodigo() + " " + meritoBolsa.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getModulacion() %>
										<% } else {
											for (MeritoSolicitudValoracion valoracion: meritoBolsa.getMeritoSolicitud().getValoraciones()) { %>
												<%= valoracion.getValor() + " - " + valoracion.getAfinidad().getCodigo() + " " + valoracion.getAfinidad().getModulacion() + "%</br>" %>
											<% }
										   } %>
									<% } %>
								<% } %>
								<b>Estado: </b>Incluido
							</td>
							<td>
								<% if (meritoBolsa.getMeritoSolicitud() != null && meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getAfinidad() != null && meritoBolsa.getMeritoSolicitud().getValoraciones() != null) { %>
									<% if (meritoBolsa.getMeritoSolicitud().getValoraciones().size() > 0) { %>
										<% if (meritoBolsa.getMeritoSolicitud().getMerito().getItemBaremacion().getIndividualizado()) { %>
											
										<% } else {
											int total = 0;
											for (MeritoSolicitudValoracion valoracion: meritoBolsa.getMeritoSolicitud().getValoraciones()) {
												total += valoracion.getValor();
										%>
												<input data-afinidad="<%= valoracion.getAfinidad().getCodNum() %>" class="validar-afinidad field-no-individualizado" type="number" min="0" 
													value="<%= valoracion.getValor() %>" step="0.01"/>
											<% } %>
											<input class="validar-afinidad total-no-individualizado" data-total="<%= meritoBolsa.getMeritoSolicitud().getMerito().getValor() %>" type="number" min="0" name="total"
													value="<%= total %>" disabled/>
											<p class="mensaje" style="color: red; display: none;">El total es superior al valor del mérito.</p>
										<% } %>
									<% } %>
								<% } %>
							
							</td>
							<td>
					    		<label for="merito_observacion_candidato">Observación para el candidato:</label>
					    		<textarea class="form-input-custom" 
					    				id="merito_observacion_candidato_<%= idMerito %>"
					    				name="<%= ControladorValidar.PARAM_OBSERVACION_CANDIDATO %>" 
					    				rows="2" cols="50"><%= merito.getMerito().getObservacion() %></textarea>
							</td>
							<td style="vertical-align: middle;">
								<span class="btns acciones_merito">
									<% if (meritoBolsa.getMeritoSolicitud().isExcluido()) { %>
						    			<button class="btn" id="merito_guardar_<%= idMerito %>" name="<%= ControladorValidar.PARAM_GUARDAR_MERITO %>">Guardar</button>
							    		<button class="btn" id="merito_aceptar_<%= idMerito %>" name="<%= ControladorValidar.PARAM_ACEPTAR_MERITO %>">Aceptar</button>
						    		<% } else if (meritoBolsa.getMeritoSolicitud().isValidado()) { %>
						    			<button class="btn" id="merito_guardar_<%= idMerito %>" name="<%= ControladorValidar.PARAM_GUARDAR_MERITO %>">Guardar</button>
							    		<button class="btn" id="merito_excluir_<%= idMerito %>" name="<%= ControladorValidar.PARAM_EXCLUIR_MERITO %>">Excluir</button>
						    		<% } else { %>
							    		<button class="btn" id="merito_aceptar_<%= idMerito %>" name="<%= ControladorValidar.PARAM_ACEPTAR_MERITO %>">Aceptar</button>
							    		<button class="btn" id="merito_excluir_<%= idMerito %>" name="<%= ControladorValidar.PARAM_EXCLUIR_MERITO %>">Excluir</button>
						    		<% } %>
					    		</span>
							</td>
						</tr>
						
					<% } %>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="5" style="width:100%"></th>
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
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidar.ACCION_DATATABLE_CANDIDATOS %>",
	    "title": 'Lista de usuarios',
	    "dropdown": true,
	    "params": {'<%=ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorValidar.ACCION_CANDIDATO_SELECCIONADO %>',
    				'<%= ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
    				'<%= ControladorValidar.PARAM_CANDIDATO %>': row.codNum
    				};
    		Atis.sendForm("<%= request.getRequestURI() %>", params);
	    }},
	    <% if (candidato != null) { %> "selected": <%= candidato.getCodNum() %> ,<% } %>
	    "columns": [
	    	{'data': 'numdocumento', 'filter': true},
	    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
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
		    "pageSize": 10,
		    "filterable": true,
		    "action": "<%= ControladorValidar.ACCION_DATATABLE_MERITOS %>",
		    "title": 'Méritos para el usuario: <%= candidato.getNumDocumento() %>',
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
	        			window.open("<%= request.getRequestURI() %>"
	        		        	+ "?a=<%= ControladorValidar.ACCION_DESCARGAR_FICHERO %>&<%= ControladorValidar.PARAM_MERITO %>=" + row.codNum);
	        		}},
	   			]}
		    ]
		});
		
		<% if (merito != null) { %>
		
			function onChangeNoIndividualizado(field) {
				var getValoresAfinidades = function() {
					var afinidades = {};
					$(field).parent().children('.field-no-individualizado').each(function(index, input) {
						var afinidad = $(input).data('afinidad');
						var value = parseFloat($(input).val());
						afinidades[afinidad] = value;
					});
					return afinidades;
				};
				
				var sumaTotal = function() {
					var total = 0.0;
					var afinidades = getValoresAfinidades();				
					Object.keys(afinidades).forEach(function(key) {
						total += afinidades[key];
					});				
					return total;
				};
				
				var actualizaTotal = function() {
					var inputTotal = $(field).parent().find('.total-no-individualizado');
					var inputMensaje = $(field).parent().find('.mensaje');
					var total = Atis.redondearFloat(sumaTotal());
					var valorMerito = Atis.redondearFloat(inputTotal.data('total'));
					
					$(inputTotal).val(total);
					
					if (total > valorMerito) {
						$(inputMensaje).text('El total es superior al valor del mérito. Actual ' + total + ' Máximo ' + valorMerito);
						$(inputMensaje).show();
					} else {
						$(inputMensaje).hide();
					}
					return total;
				};
				
				actualizaTotal();
			}
			
			function reserva() {
				var sumaTotal = function() {
					var total = 0.0;
					var afinidades = getValoresAfinidades();				
					Object.keys(afinidades).forEach(function(key) {
						total += afinidades[key];
					});				
					return total;
				};
				
				var actualizaTotal = function() {
					var total = Atis.redondearFloat(sumaTotal());
					var merito = Atis.redondearFloat(row.merito.valor);
					
					$('input.total').val(total);
					
					if (total > merito) {
						$('p.mensaje').text('El total es superior al valor del mérito. Actual ' + total + ' Máximo ' + row.merito.valor);
						$('p.mensaje').show();
					} else {
						$('p.mensaje').hide();
					}
					return total;
				};
			}
		
			$('.bluetable').on('input', '.field-no-individualizado', function() {
				onChangeNoIndividualizado(this);
			});
		
			$('.bluetable').on('click', '.acciones_merito button', function() {
				console.log(this);
			});
		
			window.scrollTo(0,document.body.scrollHeight);
		<% } %>
		
	<% } %>
	
}); 
</script>
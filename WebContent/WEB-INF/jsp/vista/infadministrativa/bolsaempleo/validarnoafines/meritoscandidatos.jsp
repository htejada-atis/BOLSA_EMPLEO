<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidarNoAfines bean = (VistaValidarNoAfines)uvdatos.getVistas().get(VistaValidarNoAfines.class.getName());
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
	
		<div class="row">
			<div class="table-responsive col">
				<table class="bluetable bolsaempleo" id="tableMeritos">
					<tr>
						<th scope="col" style="width:30px">Id</th>
						<th scope="col" style="width:30px">Estado</th>
						<th scope="col" style="width:30px">Código</th>
						<th scope="col" style="width:100%">Valor</th>
						<th scope="col" style="width:30px">Fichero</th>
					</tr>
					<tbody>
					</tbody>
					<tfoot>
						<tr>
							<th colSpan="5" style="width:100%"></th>
						</tr>
					</tfoot>
				</table>
			</div>
			<div class="table-responsive col-md-7">
				<% if (merito != null) { %>
					<table class="bluetable bolsaempleo" id="tableBolsasCandidato">
						<tr>
							<th scope="col" style="width:30px">Bolsa</th>
							<th scope="col" style="width:60%">Nombre</th>
							<th scope="col" style="width:40%">Estado</th>
							<th scope="col" style="width:40px">Propagar</th>
						</tr>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colSpan="4" style="width:100%"></th>
							</tr>
						</tfoot>
					</table>
					
					<table class="bluetable bolsaempleo" id="tableValoresMeritoBolsas">
						<tr>
							<th scope="col" style="width:30px">Mérito</th>
							<th scope="col" style="width:100%">Bolsa</th>
							<th scope="col" style="width:40px">Valor</th>
							<th scope="col" style="width:40px">Excluido</th>
							<th scope="col" style="width:45px">Apuntado en Bolsa</th>
						</tr>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colSpan="5" style="width:100%"></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
			
			<h3>Evaluar mérito: <%= merito.getMerito().getCodNum() %></h3>
			
			<form id="validar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorValidarNoAfines.ACCION_VALIDAR_MERITO %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>" value="<%= candidato.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_MERITO %>" value="<%= merito.getMerito().getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSAS %>" value="[<%= bolsa.getCodNum() %>]" />
		    	<div class="form-group-container col1">
			    	<div class="form-group">
						<label class="bold-label" for="select_item">Categoría:</label>
						<select class="form-input-custom" id="select_item" name="">
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
				</div>
				
				<div class="form-group-container col2">
			    	<div class="form-group">
						<label class="bold-label" for="select_apartado">Afinidad-valor:</label>
						<select class="form-input-custom" id="select_apartado" name="">
							<option value="">Elija la Afinidad y el Valor</option>
						</select>
					</div>
					<div class="form-group">
			    		<div class="form-group">
			    			<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor:</label>
			    			<input class="form-input-custom" id="merito_valor" type="text" name="" value="<%= merito.getMerito().getValor() %>" required/>
			    		</div>
		    		</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-file">
						<label for="merito_archivo" class="bold-label">Fichero:</label>
						<button class="btn icon icon-download" title="Descargar fichero del mérito" type="button">Descargar fichero del mérito</button>
					</div>
				</div>
				
				<div class="form-group-container col1">
		    		<div class="form-group">
		    			<label for="merito_descripcion" class="bold-label">Descripción:</label>
		    			<textarea class="form-input-custom" id="merito_descripcion" name="" disabled><%= merito.getMerito().getDescripcion() %></textarea>
		    		</div>
				</div>
			
		    	<div class="form-group-container col1">
			    	<div class="form-group">
			    		<label for="merito_observacion_comision">Observación para la comisión:</label>
			    		<textarea class="form-input-custom" id="merito_observacion_comision" name="" rows="2" cols="50" disabled><%= merito.getMerito().getObservacion() %></textarea>
			    	</div>
		    	</div>
		    	<div class="form-group-container col1">
			    	<div class="form-group">
			    		<label for="merito_observacion_candidato">Observación para el candidato:</label>
			    		<textarea class="form-input-custom" id="merito_observacion_candidato" name="<%= ControladorValidarNoAfines.PARAM_OBSERVACIONES_CANDIDATO %>" rows="2" cols="50"></textarea>
			    	</div>
		    	</div>
		    	<div class="form-btn">
		    		<% if (merito.isExcluido()) { %>
		    			<input id="merito_guardar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_GUARDAR_MERITO %>" value="Guardar"/>
			    		<input id="merito_aceptar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO %>" value="Aceptar"/>
		    		<% } else if (merito.isValidado()) { %>
		    			<input id="merito_guardar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_GUARDAR_MERITO %>" value="Guardar"/>
			    		<input id="merito_excluir" type="submit" name="<%= ControladorValidarNoAfines.PARAM_EXCLUIR_MERITO %>" value="Excluir"/>
		    		<% } else { %>
			    		<input id="merito_aceptar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO %>" value="Aceptar"/>
			    		<input id="merito_excluir" type="submit" name="<%= ControladorValidarNoAfines.PARAM_EXCLUIR_MERITO %>" value="Excluir"/>
		    		<% } %>
		    	</div>
		    </form>
				<% } else { %>
					<p>Elija un mérito para cargar el formulario de edición</p>
				</div>
			</div>
				<% } %>
	<% } %>
	
</div>

<script>
$(document).ready(function() {
	var tableCandidatos = new Atis.DataTable('#tableCandidatos', {
	    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
	    "pageSize": 10,
	    "filterable": true,
	    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS %>",
	    "title": 'Lista de usuarios',
	    "dropdown": true,
	    "params": {'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>'},
	    "clickable": {'onClick': function(row) {
	    	var params = {
    				'a': '<%= ControladorValidarNoAfines.ACCION_CANDIDATO_SELECCIONADO %>',
    				'<%= ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
    				'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': row.codNum
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
		    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 10,
		    "filterable": true,
		    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_MERITOS %>",
		    "title": 'Méritos para el usuario: <%= candidato.getNumDocumento() %>',
		    "dropdown": true,
		    "params": {
		    	'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
		    	'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>'
		    	},
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorValidarNoAfines.ACCION_MERITO_SELECCIONADO %>',
	    				'<%= ControladorValidarNoAfines.PARAM_BOLSA %>': '<%= bolsa.getCodNum() %>',
	    				'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
	    				'<%= ControladorValidarNoAfines.PARAM_MERITO %>': row.codNum};
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
	        		        	+ "?a=<%= ControladorValidarNoAfines.ACCION_DESCARGAR_FICHERO %>&<%= ControladorValidarNoAfines.PARAM_MERITO %>=" + row.codNum);
	        		}},
	   			]}
		    ]
		});
		
		<% if (merito != null) { %>
			
			var tableBolsasCandidato = new Atis.DataTable('#tableBolsasCandidato', {
			    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "selectable": {'all': false},
			    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS_CANDIDATO %>",
			    "title": 'Bolsas en las que está apuntado actualmente el candidato',
			    "dropdown": true,
			    "params": {
			    	'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
			    	},
			    <% if (bolsa != null) { %> "selected": <%= bolsa.getCodNum() %> ,<% } %>
			    "columns": [
			    	{'data': 'area.idAreaExterno'},
			        {'data': 'area.descripcion'},
			        {'data': 'contieneMerito', 'order': false, 'render': function(row) {
			        	if (row.codNum == <%= bolsa.getCodNum() %>) {
			        		return "<div class='text-primary'>Baremación actual</div>";
			        	} else if (row.contieneMerito) {
			        		return "<div class='text-success'>Inscrito actualmente</div>";
			        	} else {
			        		return "<div class='text-danger'>No inscrito</div>";
			        	}
			        }},
		        	{'data': 'codNum', 'order': false, 'selectable': {'disabled': true}}
			    ]
			});
			
			var tableValoresMeritoBolsas = new Atis.DataTable('#tableValoresMeritoBolsas', {
			    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_VALORES_MERITO_BOLSA %>",
			    "title": 'Valores actuales de un mérito en las distintas bolsas',
			    "dropdown": true,
			    "params": {
			    	'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
			    	},
			    "columns": [
			    	{'data': 'meritoSolicitud.merito.codNum', 'filter': {'type': 'number'}},
			        {'data': 'bolsa.codNum', 'filter': true, 'render': function(row) {
			        	return row.bolsa.area.idAreaExterno + " : " + row.bolsa.area.descripcion;
		        	}},
		        	{'data': 'meritoSolicitud.merito.valor'},
		        	{'data': 'meritoSolicitud.excluido', 'render': function(row) {
		        		return row.meritoSolicitud && row.meritoSolicitud.excluido ? 'SI' : 'NO';
		        	}},
		        	{'data': 'meritoSolicitud.codNum', 'render': function(row) {
		        		if (row.meritoSolicitud.codNum) {
		        			return 'SI';
		        		} else {
		        			return 'NO';
		        		}
		        	}}
			    ]
			});
			
			document.getElementById("validar_merito").addEventListener("submit", function() {
				this.elements['<%= ControladorValidarNoAfines.PARAM_BOLSAS %>'].value = Atis.object2Json(tableBolsasCandidato.getCheckedItems());
			});
			
			Atis.scrollToAnchor("#tableBolsasCandidato");
		<% } %>
		
	<% } %>
	
}); 
</script>

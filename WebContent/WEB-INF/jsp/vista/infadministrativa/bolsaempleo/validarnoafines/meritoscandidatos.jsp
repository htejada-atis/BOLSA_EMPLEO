<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
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
			descripcion = bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias en este momento";
		}
	%>
	
<%	if (merito == null) { %>
	
		<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
<%	} %>
	
	<h2>Validar meritos no sujetos afinidad</h2>
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
	
<%	if (candidato != null) { %>
	
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
		
	<%	if (merito != null) { %>
			<div id="anchor_modificar_merito" style="margin-bottom: 24px;"></div>
			
			<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
			
			<table class="bluetable bolsaempleo" id="tableValoresMeritoBolsas">
				<tr>
					<th scope="col" style="width:12%">Convocatoria</th>
					<th scope="col" style="width:60%">Bolsa</th>
					<th scope="col" style="width:10%">Valor</th>
					<th scope="col" style="width:10%">Excluido</th>
					<th scope="col" style="width:15%">Último evaluador</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="5" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
			
			<table class="bluetable bolsaempleo" id="tableBolsasCandidato">
				<tr>
					<th scope="col" style="width:45%">Bolsa</th>
					<th scope="col" style="width:40%">Estado</th>
					<th scope="col" style="width:15%">Propagar</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="3" style="width:100%"></th>
					</tr>
				</tfoot>
			</table>
			
			<h3>Evaluar mérito: <%= merito.getMerito().getCodNum() %></h3>
			
			<form id="validar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>">
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorValidarNoAfines.ACCION_VALIDAR_MERITO %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSA %>" value="<%= bolsa.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>" value="<%= candidato.getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_MERITO %>" value="<%= merito.getMerito().getCodNum() %>" />
		    	<input type="hidden" name="<%= ControladorValidarNoAfines.PARAM_BOLSAS %>" value="[<%= bolsa.getCodNum() %>]" />
				
				<div class="form-group-container col2">
					<div class="form-group">
						<label class="bold-label" for="select_item">Categoría:</label>
						<select class="form-input-custom" id="select_item" name="<%= ControladorValidarNoAfines.PARAM_ITEM %>">
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
			    			<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorValidarNoAfines.PARAM_VALOR %>" value="<%= merito.getMerito().getValor() %>" required/>
			    		</div>
		    		</div>
				</div>
				
				<div class="form-group-container col1">
					<div class="form-file">
						<label for="merito_archivo" class="bold-label">Fichero:</label>
						<button id="merito_descargar_fichero" class="btn icon icon-download" title="Descargar fichero del mérito" type="button">Descargar fichero del mérito</button>
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
			    		<textarea class="form-input-custom" id="merito_observacion_candidato" name="<%= ControladorValidarNoAfines.PARAM_OBSERVACION_CANDIDATO %>" rows="2" cols="50"></textarea>
			    	</div>
		    	</div>
		    	
		    	<div class="form-btn">
		    		<input id="merito_aceptar" type="submit" name="<%= ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO %>" value="Aceptar"/>
			    	<input id="merito_excluir" type="submit" name="<%= ControladorValidarNoAfines.PARAM_EXCLUIR_MERITO %>" value="Excluir"/>
		    	</div>
		    </form>
	<%	} else { %>
			<p>Elija un mérito para cargar el formulario de edición</p>
	<%	} %>
<%	} %>
	
</div>

<script>
$(document).ready(function() {
	var tableCandidatos = new Atis.DataTable('#tableCandidatos', {
	    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
	    "pageSize": 100,
	    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS %>",
	    "title": 'LISTA DE USUARIOS',
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
		    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 100,
		    "filterable": true,
		    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_MERITOS %>",
		    "title": 'MÉRITOS PARA EL USUARIO: <%=candidato.getIdNif()%>',
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
	        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
	        		        	+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + row.codNum);
	        		}},
	   			]}
		    ]
		});
		
		<% if (merito != null) { %>
			
			var tableBolsasCandidato = new Atis.DataTable('#tableBolsasCandidato', {
			    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "selectable": {'all': false},
			    "selectedAll": true,
			    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS_CANDIDATO %>",
			    "title": 'BOLSAS EN LAS QUE ESTÁ APUNTADO EL MÉRITO EN ESTA SOLICITUD',
			    "dropdown": true,
			    "params": {
			    	'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
			    	},
			    <% if (bolsa != null) { %> "selected": <%= bolsa.getCodNum() %> ,<% } %>
			    "columns": [
			    	{'data': 'bolsa.codNum', 'render': function(row) {
			        	return row.area.idAreaExterno + " : " + row.area.descripcion;
		        	}},
			        {'data': 'contieneMerito', 'order': false, 'render': function(row) {
			        	if (row.codNum == <%= bolsa.getCodNum() %>) {
			        		return "<div class='text-primary'>Baremación actual</div>";
			        	} else {
			        		return "<div class='text-success'>Inscrito actualmente</div>";
			        	}
			        }},
		        	{'data': 'codNum', 'order': false, 'selectable': {'disabled': '<%= bolsa.getCodNum() %>'}}
			    ]
			});
			
			var tableValoresMeritoBolsas = new Atis.DataTable('#tableValoresMeritoBolsas', {
			    "ajax": { url: "<%= ControladorValidarNoAfines.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "action": "<%= ControladorValidarNoAfines.ACCION_DATATABLE_VALORES_MERITO_BOLSA %>",
			    "title": 'HISTORIAL DE VALORACIONES DEL MÉRITO: <%= merito.getMerito().getCodNum() %>',
			    "dropdown": true,
			    "params": {
			    	'<%=ControladorValidarNoAfines.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
			    	'<%= ControladorValidarNoAfines.PARAM_MERITO %>': '<%= merito.getMerito().getCodNum() %>'
			    	},
			    "columns": [
			    	{'data': 'convocatoria.codNum'},
			        {'data': 'bolsa.codNum', 'render': function(row) {
			        	return row.bolsa.area.idAreaExterno + " : " + row.bolsa.area.descripcion;
		        	}},
		        	{'data': 'meritoSolicitud.valor', 'render': function(row) {
		        		console.log(row.meritoSolicitud.valor);
		        		return row.meritoSolicitud.valor != 0 ? row.meritoSolicitud.valor : '<%= merito.getMerito().getValor() %>';
		        	}},
		        	{'data': 'meritoSolicitud.excluido', 'order': false, 'render': function(row) {
		        		return row.meritoSolicitud && row.meritoSolicitud.excluido ? 'SI' : 'NO';
		        	}},
		        	{'data': 'uidUsuario', 'order': false, 'render': function(row) {
		        		return row.uidUsuario != '<%= EscapaHTML.escapa(candidato.getCodCuenta()) %>' ? row.uidUsuario : '';
		        	}}
			    ]
			});
			
			document.getElementById("validar_merito").addEventListener("submit", function() {
				this.elements['<%= ControladorValidarNoAfines.PARAM_BOLSAS %>'].value = Atis.object2Json(tableBolsasCandidato.getCheckedItems());
			});
			
			document.getElementById("merito_descargar_fichero").addEventListener("click", function() {
				window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
    		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_MERITO + "=" + merito.getMerito().getCodNum() %>");
			});
			
			Atis.smoothScrollToAnchor("#anchor_modificar_merito");
		<% } %>
		
	<% } %>
	
});
</script>

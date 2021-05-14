<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
Bolsa bolsa = bean.getBolsa();
Merito merito = bean.getMerito();
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
	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>
	
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
							<th scope="col" style="width:100%">Nombre</th>
							<th scope="col" style="width:30px">Estado</th>
							<th scope="col" style="width:35px">Propagar</th>
						</tr>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colSpan="4" style="width:100%"></th>
							</tr>
						</tfoot>
					</table>
					
					<form id="agregar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
				    	<input type="hidden" name="<%= ControladorValidar.PARAM_ACCION %>" id="accion_formulario" value="" />
				    	<div class="form-group-container col1">
					    	<div class="form-group">
								<label class="bold-label" for="select_apartado">Categoría:</label>
								<select class="form-input-custom" id="select_apartado" name="">
									<option value="">---------------</option>
								</select>
							</div>
						</div>
						
						<div id="contValor" style="display:none;">
							<div class="form-group">
					    		<div class="form-group">
					    			<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor:</label>
					    			<input class="form-input-custom" id="merito_valor" type="text" name="" value=""/>
					    		</div>
				    		</div>
				    	</div>
						
						<div id="contDescripcion">
							<div class="form-group">
					    		<div class="form-group">
					    			<label for="merito_descripcion" class="bold-label">Descripción:</label>
					    			<textarea class="form-input-custom" id="merito_descripcion" name=""></textarea>
					    		</div>
				    		</div>
						</div>
					
				    	<div class="form-group-container col1">
					    	<div class="form-group">
					    		<label for="merito_observacion_comision">Observación para la comisión:</label>
					    		<textarea class="form-input-custom" id="merito_observacion_comision" name="" rows="2" cols="50"></textarea>
					    	</div>
				    	</div>
				    	<div class="form-group-container col1">
					    	<div class="form-group">
					    		<label for="merito_observacion_candidato">Observación para el candidato:</label>
					    		<textarea class="form-input-custom" id="merito_observacion_candidato" name="" rows="2" cols="50"></textarea>
					    	</div>
				    	</div>
				    	<div class="form-btn">
				    		<input id="merito_guardar" type="submit" name="" value="Guardar"/>
				    		<input id="merito_excluir" type="submit" name="" value="Excluir"/>
				    	</div>
				    </form>
				    
				<% } else { %>
					<p>Elija un mérito para cargar el formulario de edición</p>
				<% } %>
			</div>
		</div>
	
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
		    <% if (merito != null) { %> "selected": <%= merito.getCodNum() %> ,<% } %>
		    "columns": [
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		        {'data': 'codNum', 'order': false, 'class': 'center', 'render': function(row) { return ""; } },
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
			
			var tableBolsasCandidato = new Atis.DataTable('#tableBolsasCandidato', {
			    "ajax": { url: "<%= ControladorValidar.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "selectable": true,
			    "action": "<%= ControladorValidar.ACCION_DATATABLE_BOLSAS_CANDIDATO %>",
			    "title": 'Bolsas en las que está apuntado actualmente el candidato',
			    "dropdown": true,
			    "params": {
			    	'<%=ControladorValidar.PARAM_BOLSA%>': '<%= bolsa.getCodNum() %>',
			    	'<%= ControladorValidar.PARAM_CANDIDATO %>': '<%= candidato.getCodNum() %>',
			    	'<%= ControladorValidar.PARAM_MERITO %>': '<%= merito.getCodNum() %>'
			    	},
			    "columns": [
			    	{'data': 'area.idAreaExterno'},
			        {'data': 'area.descripcion'},
			        {'data': 'codnum'},
		        	{'data': 'codnum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '' : '';
				    			var params = {
				    				'a': accion,
				    			};
				    			
				    			Atis.sendForm("<%= request.getRequestURI() %>", params);
				    		}
		        		}
		        	}
			    ]
			});
			
		<% } %>
		
	<% } %>
	
}); 
</script>

<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
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
	
	<h2>Paso 2: Asignación de méritos a áreas</h2>
	<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
	
	<p>Para cada área seleccione hasta un máximo de [<%= bean.getSolicitud().getConvocatoria().getNumMeritosPorBloque() %>] méritos por bloque</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col"	style="width:15%">Código</th>
			<th scope="col"	style="width:65%">Nombre</th>
			<th scope="col"	style="width:20%">Nº méritos en área</th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="3" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" id="tableMeritos" style="visibility: collapse">
		<tr>
			<th scope="col"	style="width:5%"></th>
			<th scope="col"	style="width:10%">Cod.</th>
			<th scope="col"	style="width:15%">Código ítem</th>
			<th scope="col"	style="width:40%">Mérito</th>
			<th scope="col"	style="width:10%">Valor</th>
			<th scope="col"	style="width:10%">Afinidad</th>
			<th scope="col" class="center" style="width:10%">Excluido</th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="7" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>	
	
	<div class="btns-by-steps">
		<a class="link-btn" id="paso2_volver" href="<%= request.getRequestURI() %>">
	    	 Volver
	    </a>
	    <a class="link-btn" id="paso2_siguiente" href="<%= request.getRequestURI() %>">
	    	 Ir a 'Confirmar Solicitud'
	    </a>
	</div>
</div>

<script>
	var afinidades = {};
	<% for (String tipo : bean.getListaTipoAfinidades()) { %>
		afinidades['<%= EscapaHTML.escapa(tipo) %>'] = '<%=
		    bean.getListaAfinidades()
				.stream()
				.filter(a -> a.getCodigo().equals(tipo))
				.map(a -> "<option value=\"" + a.getCodNum() + "\">" + EscapaHTML.escapa(a.getCodigoDescripcion()) + "</option>")
				.collect(Collectors.joining(""))
		%>';	
	<% } %>
	
	function renderIndividualizado(row) {
		var texto = $('<span>Selecciona afinidad <button class="pointer" title="Seleccionar afinidad">Afinidad</button></span>');
		
		console.log(row);
		
		$('button', texto).on('click', function() {
			var message = 
				'<h3>Selecciona la afinidad para el mérito</h3><br/>' +
				'<p>Mérito: ' + Atis.escapeHtml(row.merito.item.nombre) + '</p>' +
				(row.merito.descripcion ? "<p>Descripción: " + Atis.escapeHtml(row.merito.descripcion) + "</p>" : "") +
				(row.merito.observacion ? "<p>Observaciones: " + Atis.escapeHtml(row.merito.observacion) + "</p>" : "") +
				'<p>Valor: ' + row.merito.valor + '</p>' +
				'<br/><p>Area: <strong><%= bean.getArea() != null ? bean.getArea().getArea().getDescripcion() : "" %></strong></p><br/>' +
				'<p>Selecciona el grado de afinidad del mérito con el area:</p>' +
				'<p><select><option value="0">Sin afinidad (0 %)</option>' + afinidades[row.merito.item.afinidad] + '</select></p>'
			; 
			
			Atis.alertDialog("Seleccionar afinidad", $(message), function(dialog) {
				var option = $('select option:selected', dialog);
				
				console.log("hola", $(option).text(), $(option).val());
			});
		});
		
		return texto;
	}
	
	function renderNoIndividualizado(row) {
		return 'no individualizado';
	}

	$(document).ready(function() {
		
		
		var tableAreas = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 10,
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorMisSolicitudes.ACCION_BOLSA_SELECCIONADA  %>',
	    				'<%= ControladorMisSolicitudes.PARAM_BOLSA %>': row.codNum,
	    				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': <%= bean.getSolicitud().getCodNum() %>};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    <% if (bean.getArea() != null) { %> "selected": <%= bean.getArea().getCodNum() %> ,<% } %>
		    "filterable": true,
		    "title": 'Mis Áreas para esta convocatoria',
		    "action": "<%= ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS %>",
		    "params": {"<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>": <%= bean.getSolicitud().getCodNum() %>},
		    "columns": [
		    	{'data': 'area.idAreaExterno', 'filter': true},
		    	{'data': 'area.descripcion', 'filter': true},
		    	{'data': 'numeroMeritos', 'order': {'active': false}},
	        ],
		});
		
		<% if (bean.getArea() != null) { %>
		
			var meritosBolsaSolicitud = [];
		
			<%if (bean.getListaMeritosSolicitud() != null) {
					for (MeritoSolicitud merito: bean.getListaMeritosSolicitud()) {%>
						meritosBolsaSolicitud.push(<%=merito.getCodNum()%>);
					<% }
				} %>
				
			var tableMeritos = new Atis.DataTable('#tableMeritos', {
				"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>", async: false },
			    "pageSize": 10,
			    "selectable": true,
			    "filterable": true,
			    "title": 'Mis méritos: <%=bean.getArea().getArea().getDescripcion()%>',
			    "selected": meritosBolsaSolicitud,
			    "defaultOrderBy": 1,
			    "action": "<%= ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA %>",
			    "params": {"<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>": <%= bean.getSolicitud().getCodNum() %>},
			    "columns": [
			    	{'data': 'merito.codNum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '<%= ControladorMisSolicitudes.ACCION_MERITO_SELECCIONADO %>'
			    						: '<%= ControladorMisSolicitudes.ACCION_MERITO_DESELECCIONADO %>';
				    			var params = {
				    				'a': accion,
				    				'<%= ControladorMisSolicitudes.PARAM_BOLSA %>': '<%=bean.getArea().getCodNum()%>',
				    				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
				    				'<%= ControladorMisSolicitudes.PARAM_MERITO %>': row.merito.codNum,
				    			};
				    			
				    			Atis.sendForm("<%= request.getRequestURI() %>", params);				    			
				    		}
			    		}
			    	},
			    	{'data': 'merito.codNum', 'filter': {'type': 'number'}},
			    	{'data': 'merito.item', 'filter': true, 'render': function(row) {
			        	return row.merito.item.bloque.apartado.codigo + "." + row.merito.item.bloque.codigo + "." + row.merito.item.codigo;
		        	}},
		        	{'data': 'merito.item.nombre', 'filter': true, 'render': function(row) {
			        	return merito;
		        	}},
		        	{'data': 'merito.valor', 'filter': true},
		        	{'data': 'codNum', 'filter': false, 'render': function(row) {
		        		if (!row.codNum) { 
		        			return '';
		        		}
		        		
		        		if (row.merito.item.afinidad) {
		        			if (row.merito.item.individualizado) {
		        				return renderIndividualizado(row);
		        			} else {
		        				return renderNoIndividualizado(row);
		        			}
		        		} else {
		        			return '<span title="El mérito no tiene afinidad con el área seleccionada">Sin afinidad</span>';
		        		}		        				        	
		        	}},
		        	{'data': 'excluido', 'order': {'active': false}, 'filter': {'type': 'selectBoolean', 'true': 'Excluido', 'false': 'No excluido', 'optionDefault': 'false'}, 'render': function(row) {
		        		if (row.excluido) {
		        			return "<div title='Mérito excluido' class='circle-false'></div>";
		        		} else {
		        			return "<div title='Mérito no excluido' class='circle-true'></div>";
		        		}
		        	}},
		        ],
			});
			
			document.getElementById("tableMeritos").style.visibility = "visible";
		
		<% } %>
		
		document.getElementById("paso2_volver").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%= ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD %>',
					'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		document.getElementById("paso2_siguiente").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%= ControladorMisSolicitudes.ACCION_RESUMEN_SOLICITUD %>',
					'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
	});
	
</script>


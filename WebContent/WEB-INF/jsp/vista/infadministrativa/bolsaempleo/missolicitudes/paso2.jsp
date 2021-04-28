<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
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
			<th scope="col"	style="width:10%"></th>
			<th scope="col"	style="width:15%">Código ítem</th>
			<th scope="col"	style="width:45%">Nombre ítem</th>
			<th scope="col"	style="width:10%">Valor</th>
			<th scope="col" class="center" style="width:10%">Afinidad</th>
			<th scope="col" class="center" style="width:10%">Excluido</th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="6" style="width:100%"></th>
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

	$(document).ready(function() {
		var tableAreas = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorMisSolicitudes.ACCION_BOLSA_SELECCIONADA %>',
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
		    	{'data': 'listaMeritos.length', 'order': {'active': false}},
	        ],
		});
		
		<% if (bean.getArea() != null) { %>
		
			var meritosBolsaSolicitud = [];
		
			<%  if (bean.getListaMeritos() != null) {
					for (Merito merito: bean.getListaMeritos()) { %>
						meritosBolsaSolicitud.push("<%=merito.getCodNum()%>");
					<% }
				} %>
		
			var tableMeritos = new Atis.DataTable('#tableMeritos', {
				"ajax": { url: "<%= ControladorMisSolicitudes.URL_PATTERN_AJAX %>" },
			    "pageSize": 10,
			    "selectable": true,
			    "filterable": true,
			    "title": 'Mis méritos',
			    "selected": meritosBolsaSolicitud,
			    "action": "<%= ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA %>",
			    "params": {"<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>": <%= bean.getSolicitud().getCodNum() %>},
			    "columns": [
			    	{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '<%= ControladorMisSolicitudes.ACCION_MERITO_SELECCIONADO %>'
			    						: '<%= ControladorMisSolicitudes.ACCION_MERITO_DESELECCIONADO %>';
				    			var params = {
				    				'a': accion,
				    				'<%= ControladorMisSolicitudes.PARAM_BOLSA %>': '<%=bean.getArea().getCodNum()%>',
				    				'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
				    				'<%= ControladorMisSolicitudes.PARAM_MERITO %>': row.codNum,
				    			};
				    			Atis.sendAjax("<%= request.getRequestURI() %>", params, function(data) {
				    				tableAreas.refresh();
				    			}, function(error) {
				    				console.log(error);
				    			});
				    		}
			    		}
			    	},
			    	{'data': 'item', 'filter': true, 'render': function(row) {
			        	return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
		        	}},
		        	{'data': 'item.nombre', 'filter': true, 'overflow': 'auto'},
		        	{'data': 'valor', 'filter': true},
		        	{'data': 'afinidad', 'order': {'active': false}, 'filter': {'type': 'selectBoolean', 'true': 'Afín', 'false': 'No afín'}, 'render': function(row) {
		        		if(row.afinidad) {
		        			return "<div title='Afín' class='circle-true'></div>";
		        		} else {
		        			return "<div title='No afín' class='circle-false'></div>"; 
		        		}
		        	}},
		        	{'data': 'excluido', 'order': {'active': false}, 'filter': {'type': 'selectBoolean', 'true': 'Excluido', 'false': 'No excluido'}, 'render': function(row) {
		        		if (row.excluido) {
		        			return "<div title='Excluido' class='circle-true'></div>";
		        		} else {
		        			return "<div title='No excluido' class='circle-false'></div>";
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
			
		});
		
	});
	
</script>


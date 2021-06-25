<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorItemsBaremacion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
	<h2>Items para las baremaciones</h2>
    
	    <button class="link-btn" id="descargar_items">
	    	 Descargar Items
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo custom" id="tableBloques">
		<tr>
			<th scope="col" style="width:20%" title="Código apartado">Código</th>
			<th scope="col" style="width:60%" title="Nombre del bloque">Nombre del bloque</th>
			<th scope="col" style="width:10%" title="Factor de ponderación del bloque">Factor</th>
			<th scope="col" class="center" style="width:10%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo custom" id="tableApartados">
		<tr>
			<th scope="col" style="width:10%" title="Código bloque">Código</th>
			<th scope="col" style="width:70%" title="Nombre de apartado">Nombre del apartado</th>
			<th scope="col" style="width:10%" title="Número máximo de méritos por apartado">Num. Max. Méritos</th>
			<th scope="col" style="width:10%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo custom" id="tableItems">
		<tr>
			<th scope="col" style="width:10%" title="Código item">Código</th>
			<th scope="col" style="width:20%" title="Nombre del item">Nombre del item</th>
			<th scope="col" style="width:10%" title="Tipo de unidad para los valores del item">Unidades</th>
			<th scope="col" style="width:10%" title="Valor del item">Valor</th>
			<th scope="col" style="width:10%" title="Valor mínimo para los méritos">Valor Min.</th>
			<th scope="col" style="width:10%" title="Valor máximo para los méritos">Valor Max.</th>
			<th scope="col" style="width:10%" title="Afinidad con el área de conocimiento">Afinidad</th>
			<th scope="col" style="width:10%" title="Individualizado">Indi.</th>
			<th scope="col" style="width:10%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="9" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>
	
<script>

	$(document).ready(function() {
		
	   	var renderTextActivarDesactivar = function(selected) { 
	   		if (selected && selected.length > 0) {
	   			var row = this.getRow(selected[0]);
	   			if (row) {
	   				return row.activo ? "Desactivar" : "Activar";
	   			}		    			
	   		}
	   		return "[error]";
	   	};
    	
    	var onClickActivarDesactivar = function(selected, title, paramObject, actionDesactivar, actionActivar) {
    		if (selected && selected.length > 0) {
    			var row = this.getRow(selected[0]);    			
    			if (row) {
    				var text = row.activo ? "Desactivar" : "Activar"; 
    					
    				Atis.confirmDialog(text + " bloque", title.replace("%s", text.toLowerCase()), {
    	        		Si: function() {
    	        			var params = {'<%=ControladorItemsBaremacion.PARAM_ACCION%>': row.activo ? actionDesactivar : actionActivar};
    	        			params[paramObject] = selected;
    		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
    			          	$(this).dialog("close");
    			        },
    			        No: function() {
    			          	$(this).dialog("close");
    			    	}
    			    });		
    			}
    		}
    	};
    	
    	var onClickEditar = function(action, paramObject, selected) {
    		var params = {'<%=ControladorItemsBaremacion.PARAM_ACCION%>': action};
    		params[paramObject] = selected;
       		Atis.sendForm("<%=request.getRequestURI()%>", params);
    	};
		
		var tableBloques = new Atis.DataTable('#tableBloques', {
		    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 10,
		    "action": "<%= ControladorItemsBaremacion.ACCION_DATATABLE_APARTADOS %>",
		    "filterable": true,
		    "title": "BLOQUES",
		    "defaultOrderBy": 0,
		    "defaultOrderDirection": "asc",
		    "clickable": {'onClick': function(row) {
		    	var params = {
    				'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO%>', 
    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': row.codNum
    			};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    "selected": <%= bean.getApartadoBaremacion() != null ? bean.getApartadoBaremacion().getCodNum() : "null" %>,
		    "columns": [
		    	{'data': 'codigo', 'filter': true},
		        {'data': 'nombre', 'filter': true},
		        {'data': 'porcentajeMaximo'},
		        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'renderBoolean': {'true': 'Bloque activo', 'false': 'Bloque inactivo'}},
		    ],
		    "actions": [		    	
		    	{'label': 'Añadir', 
		    	 'title': 'Añadir un nuevo bloque', 
		    	 'onClick': function(selected) {
		    		var params = {'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO%>'};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}},
		    	{'label': renderTextActivarDesactivar, 
		    	 'title': renderTextActivarDesactivar,
		    	 'showWhenSelected': true,
		    	 'onClick': function(selected) { 
		    		 onClickActivarDesactivar.bind(this,
	    				 selected, 
	    				 "¿Desea %s el bloque seleccionado?", 
	    				 '<%=ControladorItemsBaremacion.PARAM_APARTADO%>', 
	    				 '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO%>', 
	    				 '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO%>'
		    		)();
		    	  }
		    	},
		    	{'label': 'Editar', 
		    	 'showWhenSelected': true, 
		    	 'title': 'Editar bloque seleccionado', 
		    	 'onClick': onClickEditar.bind(this, '<%=ControladorItemsBaremacion.ACCION_EDITAR_APARTADO%>', '<%=ControladorItemsBaremacion.PARAM_APARTADO%>')
		    	}
		    ]
		});
		
		document.getElementById("tableApartados").style.visibility = "hidden";
		document.getElementById("tableItems").style.visibility = "hidden";
		
	<%	if (bean.getApartadoBaremacion() != null) { %>
			var tableApartados = new Atis.DataTable('#tableApartados', {
			    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
			    "params": {"<%=ControladorItemsBaremacion.PARAM_APARTADO%>": <%= bean.getApartadoBaremacion().getCodNum() %>},
			    "pageSize": 10,
			    "title": "APARTADOS: <%= bean.getApartadoBaremacion().getNombre() %>",
			    "filterable": true,
			    "defaultOrderBy": 0,
			    "defaultOrderDirection": "asc",
			    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES%>",
			    "clickable": {'onClick': function(row) {
			    	var params = {
	    				'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO%>', 
	    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': row.codNum
		    		};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    }},
			    "selected": <%= bean.getBloqueBaremacion() != null ? bean.getBloqueBaremacion().getCodNum() : "null" %>,		
			    "columns": [
			    	{'data': 'codigo', 'filter': true, 'render': function(row) { return row.apartado.codigo + "." + row.codigo; }},
			        {'data': 'nombre', 'filter': true},
			        {'data': 'numeroMaximoMeritos'},			        
			        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'renderBoolean': {'true': 'Apartado activo', 'false': 'Apartado inactivo'}},
			    ],
			    "actions": [
			    	{'label': 'Añadir', 'title': 'Añadir un nuevo apartado', 'onClick': function(selected) {
			    		var params = {
		    				'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE%>', 
		    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': '<%=bean.getApartadoBaremacion().getCodNum()%>'
			    		};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	}},
			    	{'label': renderTextActivarDesactivar,
			    	 'title': renderTextActivarDesactivar,
			    	 'showWhenSelected': true,
			    	 'onClick': function(selected) { 
			    		 onClickActivarDesactivar.bind(this,
		    				 selected, 
		    				 "¿Desea %s el apartado seleccionado?", 
		    				 '<%=ControladorItemsBaremacion.PARAM_BLOQUE%>', 
		    				 '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_BLOQUE%>', 
		    				 '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_BLOQUE%>'
				    	 )();
			    	  }
			    	},
			    	{'label': 'Editar', 
			    	 'showWhenSelected': true, 
			    	 'title': 'Editar apartado seleccionado',
			    	 'onClick': onClickEditar.bind(this, '<%=ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE%>', '<%=ControladorItemsBaremacion.PARAM_BLOQUE%>')
			    	}
			    ]
			});
			document.getElementById("tableApartados").style.visibility = "visible";
			
		<%	if (bean.getBloqueBaremacion() != null) { %>
				var tableItems = new Atis.DataTable('#tableItems', {
				    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
				    "params": {"<%=ControladorItemsBaremacion.PARAM_BLOQUE%>": <%= bean.getBloqueBaremacion().getCodNum() %>},
				    "pageSize": 10,
				    "title": "TIPOS DE MÉRITOS: <%= bean.getBloqueBaremacion().getNombre() %>",
				    "filterable": true,
				    "defaultOrderBy": 0,
				    "defaultOrderDirection": "asc",
				    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS%>",
				    "clickable": {'onClick': function(row) {
				    	var params = {
		    				'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_ITEM_SELECCIONADO%>', 
		    				'<%=ControladorItemsBaremacion.PARAM_ITEM%>': row.codNum
		    			};
		        		Atis.sendForm("<%= request.getRequestURI() %>", params);
				    }},
				    "selected": <%= bean.getItemBaremacion() != null ? bean.getItemBaremacion().getCodNum() : "null" %>,
				    "columns": [
				    	{'data': 'codigo', 'filter': true, 'render': function(row) { return row.bloque.apartado.codigo + "." + row.bloque.codigo + "." + row.codigo; }},
				        {'data': 'nombre', 'filter': true},
				        {'data': 'unidades'},
				        {'data': 'valor'},
				        {'data': 'valorMinimo'},
				        {'data': 'valorMaximo'},
				        {'data': 'afinidad', 'render': function(row) { return row.afinidad ? row.afinidad : 'N'; }},
				        {'data': 'individualizado', 'filter': {'type': 'selectBoolean', 'true': 'Si', 'false': 'No'}, 'renderBoolean': {'true': 'Individualizado', 'false': 'No individualizado'}},
				        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'renderBoolean': {'true': 'Item activo', 'false': 'Item inactivo'}},
				    ],
				    "actions": [
				    	{'label': 'Añadir', 'title': 'Añadir un nuevo item', 'onClick': function(selected) {
				    		var params = {
				    			'<%=ControladorItemsBaremacion.PARAM_ACCION%>': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_ITEM%>', 
				    			'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': '<%=bean.getBloqueBaremacion().getCodNum()%>'
				    		};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				    	}},
				    	{'label': renderTextActivarDesactivar,
				    	 'title': renderTextActivarDesactivar,
				    	 'showWhenSelected': true,
				    	 'onClick': function(selected) { 
				    		 onClickActivarDesactivar.bind(this,
			    				 selected,
			    				 "¿Desea %s el item seleccionado?", 
			    				 '<%=ControladorItemsBaremacion.PARAM_ITEM%>',
			    				 '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_ITEM%>', 
			    				 '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_ITEM%>'
					    	 )();
				    	  }
				    	},				    	
				    	{'label': 'Editar', 
				    	 'title': 'Editar item seleccionado', 
				    	 'showWhenSelected': true,
				    	 'onClick': onClickEditar.bind(this, '<%=ControladorItemsBaremacion.ACCION_EDITAR_ITEM%>', '<%=ControladorItemsBaremacion.PARAM_ITEM%>')
				    	}
				    ]
				});
				
				document.getElementById("tableItems").style.visibility = "visible";
			
			<%	if (bean.getItemBaremacion() != null) { %>
					Atis.smoothScrollToAnchor("#tableItems");
			<%	} else { %>
					Atis.smoothScrollToAnchor("#tableApartados");
			<%	} %>
				
		<%	} %>
			
	<%	} %>
		
		document.getElementById("descargar_items").addEventListener("click", function(event) {
			event.preventDefault();
			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        	+ "?<%= ControladorDescargaFicheros.PARAM_ACCION %>=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_RESUMEN_ITEM_BAREMACION %>");
		});
		
	});

</script>

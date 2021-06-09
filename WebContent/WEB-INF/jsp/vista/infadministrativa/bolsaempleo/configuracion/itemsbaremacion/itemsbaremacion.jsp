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
	
	<p id="selectBloque">Seleccione un <strong>BLOQUE</strong> para mostrar los apartados</p>
	
	<table class="bluetable bolsaempleo custom" id="tableBloques">
		<tr>
			<th scope="col" style="width:20%" title="Código apartado">Código</th>
			<th scope="col" style="width:60%" title="Nombre del bloque">Nombre del bloque</th>
			<th scope="col" style="width:10%" title="Puntuación máxima por bloque">Puntuación max.</th>
			<th scope="col" style="width:10%" title="Porcentaje máximo por bloque">Porcentaje max.</th>
			<th scope="col" class="center" style="width:10%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="7" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<p id="selectApartado" style="display:none;">Seleccione un <strong>APARTADO</strong> para mostrar los items asociados al mismo</p>
	
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
	
	<p id="selectItem" style="display:none;">Seleccione un <strong>ITEM</strong> para editarlo o borrarlo</p>
	
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
		
		var activatorApartado = false;
		var activatorBloque = false;
		var activatorItem = false;
		
	<%	if (bean.getApartadoBaremacion() != null) {
    		if (bean.getApartadoBaremacion().getActivo()) {%>
				$("#selectApartado").show();
				$("#selectBloque").hide();
    			activatorBloque = false;
    	<%	} else { %>
				$("#selectBloque").show();
				$("#selectApartado").hide();
    			activatorBloque = true;
    	<%	}
    		if (bean.getBloqueBaremacion() != null) {
    			if (bean.getBloqueBaremacion().isActivo()) {%>
					$("#selectItem").show();
					$("#selectApartado").hide();
    				activatorApartado = false;
        	<%	} else { %>
					$("#selectApartado").show();
					$("#selectItem").hide();
        			activatorApartado = true;
        	<%	}
    			if (bean.getItemBaremacion() != null) {
    				if (bean.getItemBaremacion().getActivo()) {%>
    					activatorItem = false;
        		<%	} else { %>
    					activatorItem = true;
        		<%	}
    			}
    		}
		} %>
    	
    	var activatorApartadoName = activatorApartado ? "Activar" : "Desactivar";
    	var activatorBloqueName = activatorBloque ? "Activar" : "Desactivar";
    	var activatorItemName = activatorItem ? "Activar" : "Desactivar";
		
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
	    				'a': '<%= ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO %>', 
	    				'<%= ControladorItemsBaremacion.PARAM_APARTADO %>': row.codNum};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		 	<%	if (bean.getApartadoBaremacion() != null) { %> "selected": <%= bean.getApartadoBaremacion().getCodNum() %> ,<% } %>
		    "columns": [
		    	{'data': 'codigo', 'filter': true},
		        {'data': 'nombre', 'filter': true},
		        {'data': 'puntuacionMaxima'},
		        {'data': 'porcentajeMaximo'},
		        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'render': function(row) {
	        		if(row.activo){
	        			return "<div title='Activo' class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div title='Desactivado' class='circle-false'></div>"; 
	        		}
	        	}},
		    ],
		    "actions": [		    	
		    	{'label': 'Añadir', 'title': 'Añadir un nuevo bloque', 'onClick': function(selected) {
		    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO%>'};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}},
		    	{'label': activatorBloqueName, 'showWhenSelected': true, 'title': activatorBloqueName + ' bloque seleccionado', 'onClick': function(selected) {
	        		Atis.confirmDialog(activatorBloqueName + " bloque", "¿ Desea " + activatorBloqueName + " el bloque seleccionado?", {
		        		Si: function() {
		        			var params = {
			    				'a': activatorApartado ? '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_APARTADO%>' : '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_APARTADO%>' , 
			    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': selected
			    			};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				          	$(this).dialog("close");
				        },
				        No: function() {
				          	$(this).dialog("close");
				    	}
				    });
		    	}},
		    	{'label': 'Editar', 'showWhenSelected': true, 'title': 'Editar bloque seleccionado', 'onClick': function(selected) {
		    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_APARTADO%>', 
		    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': selected};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}}
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
		    				'a': '<%= ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO %>', 
		    				'<%= ControladorItemsBaremacion.PARAM_BLOQUE %>': row.codNum};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    }},
		<%	if (bean.getBloqueBaremacion() != null) { %> "selected": <%= bean.getBloqueBaremacion().getCodNum() %> ,<% } %>
			    "columns": [
			    	{'data': 'codigo', 'filter': true, 'render': function(row) {
			    		return row.apartado.codigo + "." + row.codigo;
			    	}},
			        {'data': 'nombre', 'filter': true},
			        {'data': 'numeroMaximoMeritos'},			        
			        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'render': function(row) {
		        		if(row.activo){
		        			return "<div class='circle-true'></div>"; 
		        		}
		        		else{
		        			return "<div class='circle-false'></div>"; 
		        		}
		        	}},
			    ],
			    "actions": [
			    	{'label': 'Añadir', 'title': 'Añadir un nuevo apartado', 'onClick': function(selected) {
			    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE%>', 
			    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': '<%=bean.getApartadoBaremacion().getCodNum()%>'};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	}},
			    	{'label': activatorApartadoName, 'showWhenSelected': true, 'title': activatorApartadoName + ' apartado seleccionado', 'onClick': function(selected) {
		        		Atis.confirmDialog(activatorApartadoName + " apartado", "¿ Desea " + activatorApartadoName + " el apartado seleccionado ?", {
			        		Si: function() {
			        			var params = {
				    				'a': activatorBloque ? '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_BLOQUE%>' : '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_BLOQUE%>' , 
				    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': selected
				    			};
				        		Atis.sendForm("<%=request.getRequestURI()%>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					    	}
					    });
			    	}},			    	
			    	{'label': 'Editar', 'showWhenSelected': true, 'title': 'Editar apartado seleccionado', 'onClick': function(selected) {
			    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE%>', 
			    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': selected};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	}}
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
			    				'a': '<%= ControladorItemsBaremacion.ACCION_ITEM_SELECCIONADO %>', 
			    				'<%= ControladorItemsBaremacion.PARAM_ITEM %>': row.codNum};
		        		Atis.sendForm("<%= request.getRequestURI() %>", params);
				    }},
			<%	if (bean.getItemBaremacion() != null) { %> "selected": <%= bean.getItemBaremacion().getCodNum() %> ,<% } %>
				    "columns": [
				    	{'data': 'codigo', 'filter': true, 'render': function(row) {
				    		return row.bloque.apartado.codigo + "." + row.bloque.codigo + "." + row.codigo;
				    	}},
				        {'data': 'nombre', 'filter': true},
				        {'data': 'unidades'},
				        {'data': 'valor'},
				        {'data': 'valorMinimo'},
				        {'data': 'valorMaximo'},				        
				        {'data': 'afinidad', 'render': function(row) { return row.afinidad ? row.afinidad : 'N'; }},
				        {'data': 'individualizado', 'filter': {'type': 'selectBoolean', 'true': 'Si', 'false': 'No'}, 'render': function(row) {
			        		if(row.individualizado){
			        			return '<div class="circle-true" title="Individualizado"></div>'; 
			        		}
			        		else{
			        			return '<div class="circle-false" title="No individualizado"></div>'; 
			        		}
			        	}},
				        {'data': 'activo', 'filter': {'type': 'selectBoolean', 'true': 'Activo', 'false': 'Inactivo'}, 'render': function(row) {
			        		if(row.activo){
			        			return "<div class='circle-true'></div>"; 
			        		}
			        		else{
			        			return "<div class='circle-false'></div>"; 
			        		}
			        	}},
				    ],
				    "actions": [
				    	{'label': 'Añadir', 'title': 'Añadir un nuevo item', 'onClick': function(selected) {
				    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_ITEM%>', 
				    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': '<%=bean.getBloqueBaremacion().getCodNum()%>'};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				    	}},
				    	{'label': activatorItemName, 'showWhenSelected': true, 'title': activatorItemName + ' item seleccionado', 'onClick': function(selected) {
			        		Atis.confirmDialog(activatorItemName + " item", "¿ Desea " + activatorItemName + " el item seleccionado?", {
				        		Si: function() {
				        			var params = {
					    				'a': activatorItem ? '<%=ControladorItemsBaremacion.ACCION_ACTIVAR_ITEM%>' : '<%=ControladorItemsBaremacion.ACCION_DESACTIVAR_ITEM%>' , 
					    				'<%=ControladorItemsBaremacion.PARAM_ITEM%>': selected
					    			};
					        		Atis.sendForm("<%=request.getRequestURI()%>", params);
						          	$(this).dialog("close");
						        },
						        No: function() {
						          	$(this).dialog("close");
						    	}
						    });
				    	}},				    	
				    	{'label': 'Editar', 'title': 'Editar item seleccionado', 'showWhenSelected': true, 'onClick': function(selected) {
				    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_ITEM%>', 
				    				'<%=ControladorItemsBaremacion.PARAM_ITEM%>': selected};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				    	}}
				    ]
				});
				
				document.getElementById("tableItems").style.visibility = "visible";
			<%	if (bean.getItemBaremacion() != null) { %>
					Atis.smoothScrollFromOneToAnotherAnchor("#tableApartados", "#tableItems");
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

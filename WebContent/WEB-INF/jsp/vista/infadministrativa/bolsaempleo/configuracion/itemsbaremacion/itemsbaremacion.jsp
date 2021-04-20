<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());
%>

<div class='bolsas'>
	<% if (session.getAttribute(ControladorItemsBaremacion.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorItemsBaremacion.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorItemsBaremacion.MENSAJE_ENVIADO);
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
	<% } else {%>
	
	<h2>Items para las baremaciones</h2>
	
	<table class="bluetable bolsaempleo" id="tableApartadosGenerales">
		<tr>
			<th scope="col" style="width:15%" title="Código apartado">Código</th>
			<th scope="col" style="width:75%" title="Código de area">Nombre del apartado</th>
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
	
	<table class="bluetable bolsaempleo" id="tableBloques">
		<tr>
			<th scope="col" style="width:15%" title="Código bloque">Código</th>
			<th scope="col" style="width:75%" title="Código de area">Nombre del bloque</th>
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
	
	<table class="bluetable bolsaempleo" id="tableItems">
		<tr>
			<th scope="col" style="width:15%" title="Código ítem">Código</th>
			<th scope="col" style="width:75%" title="Código de area">Nombre del ítem</th>
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
	<% } %>
</div>
	
<script>

	$(document).ready(function() {
		
		var activatorApartado = false;
		var activatorBloque = false;
		var activatorItem = false;
		
		<% 
    	if (bean.getApartadoBaremacion() != null) {
    		if (bean.getApartadoBaremacion().isActivo()) {
    	%>
    			activatorApartado = false;
    	<%
    		} else {
    	%>
    			activatorApartado = true;
    	<%
    		}
    		
    		if (bean.getBloqueBaremacion() != null) {
    			if (bean.getBloqueBaremacion().isActivo()) {
        %>
        			activatorBloque = false;
        <%
        		} else {
        %>
        			activatorBloque = true;
        <%
        		}
    			
    			if (bean.getItemBaremacion() != null) {
    				if (bean.getItemBaremacion().isActivo()) {
    	%>
    					activatorItem = false;
        <%
    				} else {
    	%>
    					activatorItem = true;
        <%
    				}
    			}
    		}
    	}
    	%>
    	
    	var activatorApartadoName = activatorApartado ? "Activar" : "Desactivar";
    	var activatorBloqueName = activatorBloque ? "Activar" : "Desactivar";
    	var activatorItemName = activatorItem ? "Activar" : "Desactivar";
		
		var tableApartados = new DataTable('#tableApartadosGenerales', {
		    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
		    "pageSize": 10,
		    "action": "<%= ControladorItemsBaremacion.ACCION_DATATABLE_APARTADOS %>",
		    "title": "APARTADOS GENERALES",
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'a': '<%= ControladorItemsBaremacion.ACCION_APARTADO_SELECCIONADO %>', 
	    				'<%= ControladorItemsBaremacion.PARAM_APARTADO %>': row.codNum};
        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		    }},
		    <% if (bean.getApartadoBaremacion() != null) { %> "selected": <%= bean.getApartadoBaremacion().getCodNum() %> ,<% } %>
		    "columns": [
		    	{'data': 'codigo'},
		        {'data': 'nombre'},
		        {'data': 'activo', 'render': function(row) {
	        		if(row.activo){
	        			return "<div class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div class='circle-false'></div>"; 
	        		}
	        	}},
		    ],
		    "actions": [
		    	{'label': activatorApartadoName, 'showWhenSelected': true, 'title': activatorApartadoName + ' apartado seleccionado', 'onClick': function(selected) {
	        		Atis.confirmDialog(activatorApartadoName + " apartado", "¿Desea " + activatorApartadoName + " el apartado seleccionado?", {
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
		    	{'label': 'Añadir', 'title': 'Añadir un nuevo apartado', 'onClick': function(selected) {
		    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_APARTADO%>'};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}},
		    	{'label': 'Editar', 'showWhenSelected': true, 'title': 'Editar apartado seleccionado', 'onClick': function(selected) {
		    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_APARTADO%>', 
		    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': selected};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}}
		    ]
		});
		
		document.getElementById("tableBloques").style.visibility = "hidden";
		document.getElementById("tableItems").style.visibility = "hidden";
		
		<% if (bean.getApartadoBaremacion() != null) { %>
			var tableBloques = new DataTable('#tableBloques', {
			    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
			    "params": {"<%=ControladorItemsBaremacion.PARAM_APARTADO%>": <%= bean.getApartadoBaremacion().getCodNum() %>},
			    "pageSize": 10,
			    "title": "BLOQUES",
			    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_BLOQUES%>",
			    "clickable": {'onClick': function(row) {
			    	var params = {
		    				'a': '<%= ControladorItemsBaremacion.ACCION_BLOQUE_SELECCIONADO %>', 
		    				'<%= ControladorItemsBaremacion.PARAM_BLOQUE %>': row.codNum};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    }},
			    <% if (bean.getBloqueBaremacion() != null) { %> "selected": <%= bean.getBloqueBaremacion().getCodNum() %> ,<% } %>
			    "columns": [
			    	{'data': 'codigo', 'render': function(row) {
			    		return row.apartado.codigo + "." + row.codigo;
			    	}},
			        {'data': 'nombre'},
			        {'data': 'activo', 'render': function(row) {
		        		if(row.activo){
		        			return "<div class='circle-true'></div>"; 
		        		}
		        		else{
		        			return "<div class='circle-false'></div>"; 
		        		}
		        	}},
			    ],
			    "actions": [
			    	{'label': activatorBloqueName, 'showWhenSelected': true, 'title': activatorBloqueName + ' bloque seleccionado', 'onClick': function(selected) {
		        		Atis.confirmDialog(activatorBloqueName + " bloque", "¿Desea " + activatorBloqueName + " el bloque seleccionado?", {
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
			    	{'label': 'Añadir', 'title': 'Añadir un nuevo bloque', 'onClick': function(selected) {
			    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE%>', 
			    				'<%=ControladorItemsBaremacion.PARAM_APARTADO%>': '<%=bean.getApartadoBaremacion().getCodNum()%>'};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	}},
			    	{'label': 'Editar', 'showWhenSelected': true, 'title': 'Editar bloque seleccionado', 'onClick': function(selected) {
			    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE%>', 
			    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': selected};
		        		Atis.sendForm("<%=request.getRequestURI()%>", params);
			    	}}
			    ]
			});
			document.getElementById("tableBloques").style.visibility = "visible";
			
			<% if (bean.getBloqueBaremacion() != null) { %>
				var tableItems = new DataTable('#tableItems', {
				    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
				    "params": {"<%=ControladorItemsBaremacion.PARAM_BLOQUE%>": <%= bean.getBloqueBaremacion().getCodNum() %>},
				    "pageSize": 10,
				    "title": "ÍTEMS",
				    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS%>",
				    "clickable": {'onClick': function(row) {
				    	var params = {
			    				'a': '<%= ControladorItemsBaremacion.ACCION_ITEM_SELECCIONADO %>', 
			    				'<%= ControladorItemsBaremacion.PARAM_ITEM %>': row.codNum};
		        		Atis.sendForm("<%= request.getRequestURI() %>", params);
				    }},
				    <% if (bean.getItemBaremacion() != null) { %> "selected": <%= bean.getItemBaremacion().getCodNum() %> ,<% } %>
				    "columns": [
				    	{'data': 'codigo', 'render': function(row) {
				    		return row.bloque.apartado.codigo + "." + row.bloque.codigo + "." + row.codigo;
				    	}},
				        {'data': 'nombre'},
				        {'data': 'activo', 'render': function(row) {
			        		if(row.activo){
			        			return "<div class='circle-true'></div>"; 
			        		}
			        		else{
			        			return "<div class='circle-false'></div>"; 
			        		}
			        	}},
				    ],
				    "actions": [
				    	{'label': activatorItemName, 'showWhenSelected': true, 'title': activatorItemName + ' ítem seleccionado', 'onClick': function(selected) {
			        		Atis.confirmDialog(activatorItemName + " ítem", "¿Desea " + activatorItemName + " el ítem seleccionado?", {
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
				    	{'label': 'Añadir', 'title': 'Añadir un nuevo ítem', 'onClick': function(selected) {
				    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_ITEM%>', 
				    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': '<%=bean.getBloqueBaremacion().getCodNum()%>'};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				    	}},
				    	{'label': 'Editar', 'title': 'Editar ítem seleccionado', 'showWhenSelected': true, 'onClick': function(selected) {
				    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_EDITAR_ITEM%>', 
				    				'<%=ControladorItemsBaremacion.PARAM_ITEM%>': selected};
			        		Atis.sendForm("<%=request.getRequestURI()%>", params);
				    	}}
				    ]
				});
				
				document.getElementById("tableItems").style.visibility = "visible";
			<% } %>
			
		<% } %>
		
	});

</script>

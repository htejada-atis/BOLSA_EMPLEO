<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulacionesArea"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulacionesArea bean = (VistaTitulacionesArea) uvdatos.getVistas().get(VistaTitulacionesArea.class.getName());
%>

<div class="bolsa-empleo">
	
	<% if (session.getAttribute(ControladorGestionTitulacionesArea.MENSAJE_ENVIADO) != null) { %>
		<div id="exito" class="success">
			<%= session.getAttribute(ControladorGestionTitulacionesArea.MENSAJE_ENVIADO) %>
		</div>
	<% 
			session.removeAttribute(ControladorGestionTitulacionesArea.MENSAJE_ENVIADO);
		} 
	%>
	
	<h2>Titulaciones por área</h2>
	
	<div class="form-select">
		<label>Área</label>
		<select id="select_area">
			<option value="0">Elija el área</option>
			<% for(Area area: bean.getAreas()) { %>
    			<option value="<%= area.getCodNum() %>"><%= area.getDescripcion() %></option>
    		<% } %>
		</select>
	
	</div>
	
	<div id="tablas_titulaciones">
		<h4>Titulaciones afines al área</h4>
		<table class="bluetable bolsaempleo" id="table_titulaciones_area">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:10%" title="Id de la titulaciï¿½n">Id</th>
				<th scope="col"	style="width:70%">Nombre</th>
				<th scope="col" style="width:15%">Requerida</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colspan="3" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	
		<h4>Titulaciones</h4>
	    <table class="bluetable bolsaempleo" id="table_titulaciones">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:10%" title="Id de la titulaciï¿½n">Id</th>
				<th scope="col"	style="width:85%">Nombre</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colspan="3" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	
</div>

<script>

	function confirmDialog(title, message, id) {
		$('<div></div>').appendTo('body')
	    	.html('<div><h6>' + message + '</h6></div>')
	    	.dialog({
		      modal: true,
		      title: title,
		      zIndex: 10000,
		      autoOpen: true,
		      width: 'auto',
		      resizable: false,
		      buttons: {
		        Si: function() {
		        	var params = {'a': '<%= ControladorGestionTitulacionesArea.ACCION_BORRAR_TITULACION %>', '<%= ControladorGestionTitulacionesArea.PARAM_ID %>': id};
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
		          	$(this).dialog("close");
		        },
		        No: function() {
		          	$(this).dialog("close");
		        }
		      },
		      close: function(event, ui) {
		        $(this).remove();
		      }
		});
	}

	$(document).ready(function() {
		
		var table_titulaciones_area;
		var table_titulaciones;
		
		function inicializarTablas(id) {
			table_titulaciones_area = new DataTable('#table_titulaciones_area', {
			    "ajax": { url: "<%= ControladorGestionTitulacionesArea.URL_PATTERN_AJAX %>", async: false },
			    "params": {"<%= ControladorGestionTitulacionesArea.PARAM_AREA %>": id},
			    "selectable": true,
			    "pageSize": 5,
			    "action": "<%= ControladorGestionTitulacionesArea.ACCION_DATATABLE_TITULACIONES_AREA %>",
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			    	{'data': 'codNum'},
			        {'data': 'nombre', 'class': 'overflow-auto'},
			        {'data': 'requerida', 'selectable': true, 'render': function(row){
			        	
			        }}
			    ],
			    "actions": [
			    	{'label': 'Eliminar', 'onClick': function(selected) { console.log(selected); } },
			    ]
			});
			
			table_titulaciones = new DataTable('#table_titulaciones', {
			    "ajax": { url: "<%= ControladorGestionTitulacionesArea.URL_PATTERN_AJAX %>", async: false },
			    "selectable": true,
			    "pageSize": 5,
			    "action": "<%= ControladorGestionTitulacionesArea.ACCION_DATATABLE_TITULACIONES %>",
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			    	{'data': 'codNum'},
			        {'data': 'nombre', 'class': 'overflow-auto'},
			    ],
			    "actions": [
			    	{'label': 'Incluir', 'onClick': function(selected) { console.log(selected); } },
			    ]
			});
		}
		
		function cargarTablas(id) {
			table_titulaciones.setParam("<%= ControladorGestionTitulacionesArea.PARAM_AREA %>", id);
			table_titulaciones.refresh();
			table_titulaciones_area.setParam("<%= ControladorGestionTitulacionesArea.PARAM_AREA %>", id);
			table_titulaciones_area.refresh();
		}
		
		document.getElementById("tablas_titulaciones").style.visibility = "hidden";
		
		document.getElementById("select_area").onchange = function () {
			if(this.value != 0) {
				if(document.getElementById("tablas_titulaciones").style.visibility == "hidden") {
					document.getElementById("tablas_titulaciones").style.visibility = "visible";
					inicializarTablas(this.value);
				} else {
					cargarTablas(this.value);
				}
				
			}
		}
		
	});
	
</script>
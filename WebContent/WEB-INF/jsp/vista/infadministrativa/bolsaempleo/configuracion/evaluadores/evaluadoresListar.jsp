<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEvaluadores bean = (VistaEvaluadores) uvdatos.getVistas().get(VistaEvaluadores.class.getName());
%>

<div class="bolsa-empleo">

	<%
		if (session.getAttribute(ControladorGestionEvaluadores.MENSAJE_ENVIADO) != null) {
	%>
			<div id="exito" class="success">
				<%=session.getAttribute(ControladorGestionEvaluadores.MENSAJE_ENVIADO)%>
			</div>
	<%
			session.removeAttribute(ControladorGestionEvaluadores.MENSAJE_ENVIADO);
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
	
	<h2>Evaluadores de un área</h2>
	
	<div class="titulo-bolsa-empleo">
		<div class="form-select">
			<label>Área</label>
			<select id="select_area" autocomplete="off">
				<option value="0">Elija el área</option>
				<%
				for(Area area: bean.getAreas()) {
				%>
	    			<option value="<%=area.getCodNum()%>"><%=area.getDescripcion()%></option>
	    		<%
	    		}
	    		%>
			</select>
		</div>
    
	    <a class="link-btn" id="nuevo_evaluador" href="<%=request.getRequestURI()%>">
	    	 Añadir evaluador
	    </a>
	</div>
	<table class="bluetable bolsaempleo" id="table_evaluadores_area">
		<tr>
			<th scope="col" style="width:25%">D.N.I</th>
			<th scope="col"	style="width:55%">Nombre</th>
			<th scope="col" class="center" style="width:10%">Activo</th>
			<th scope="col" style="width:10%"></th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>

<script>

	$(document).ready(function() {
		
		var table_evaluadores;
		
		function checkVisibility() {
			return document.getElementById("table_evaluadores_area").style.visibility == "hidden";
		}
		
		function toggleVisibility() {
			var visibility = checkVisibility() ? "visible" : "hidden";
			document.getElementById("table_evaluadores_area").style.visibility = visibility;
			document.getElementById("nuevo_evaluador").style.visibility = visibility;
		}
		
		function inicializarTablas(id, title) {
			table_evaluadores = new Atis.DataTable('#table_evaluadores_area', {
			    "ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>" },
			    "params": {"<%=ControladorGestionEvaluadores.PARAM_AREA%>": id},
			    "pageSize": 10,
			    "title": title,
			    "filterable": true,
			    "action": "<%=ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES%>",
			    "columns": [
			    	{'data': 'numdocumento', 'filter': true},
			    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
		        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
		        	}},
		        	{'data': 'activo', 'filter': {'type': 'selectBoolean'}, 'order': {'active': false}, 'render': function(row) {
		        		if (row.activo) {
		        			return "<div title='Activo' class='circle-true'></div>";
		        		} else {
		        			return "<div title='Desactivado' class='circle-false'></div>";
		        		}
		        	}},
		        	{'data': 'codnum', 'buttons': [{'label': function(row) { return row.activo ? "Borrar" : "Restaurar"; }, 
		        		'title':  function(row) { return row.activo ? "Desactivar evaluador" : "Activar evaluador"; }, 'onClick': function(row) {
			        		var mensaje = "¿Desea borrar el evaluador seleccionado?";
				        	var titulo = "Borrar evaluador";
				        	if(!row.activo) {
				        		titulo = "Restaurar evaluador";
				        		mensaje = "¿Desea restaurar el evaluador seleccionado?";
				        	}
				        	
				        	Atis.confirmDialog(titulo, mensaje, {
						        Si: function() {
						        	var params = {'a': '<%= ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR %>',
						        			'<%= ControladorGestionEvaluadores.PARAM_USUARIO %>': row.codNum,
						        			'<%= ControladorGestionEvaluadores.PARAM_AREA %>': row.codNumArea,
						        			'<%= ControladorGestionEvaluadores.PARAM_ACTIVO %>': !row.activo
						        			};
					        		Atis.sendForm("<%= request.getRequestURI() %>", params);
						          	$(this).dialog("close");
						        },
						        No: function() {
						          	$(this).dialog("close");
						        }
						    });
			        	}
		        	}]}
			    ]
			});
		}
		
		function cargarTablas(id, title) {
			table_evaluadores.setParam("<%=ControladorGestionEvaluadores.PARAM_AREA%>", id);
			table_evaluadores.setTitle(title);
			table_evaluadores.refresh();
		}
		
		function onChangeArea(select) {
			var title_evaluadores = "Evaluadores del Área: " + $(select).children("option").filter(":selected").text();
			if(checkVisibility()) {
				toggleVisibility();
				inicializarTablas(select.value, title_evaluadores);
			} else {
				cargarTablas(select.value, title_evaluadores);
			}
		}
		
		toggleVisibility();
		
		document.getElementById("select_area").onchange = function () {
			if(this.value != 0) {
				onChangeArea(this);
			}
		}
		
		document.getElementById("nuevo_evaluador").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%=ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES%>',
					'<%=ControladorGestionEvaluadores.PARAM_AREA%>': document.getElementById("select_area").value
			}
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		<% if(bean.getArea() != null) { %>
			document.getElementById("select_area").value = "<%= bean.getArea().getCodNum() %>";
			toggleVisibility();
			inicializarTablas("<%= bean.getArea().getCodNum() %>");
		<%} else { %>
			document.getElementById("select_area").options[0].selected = true;
		<%} %>
		
	});
	
</script>

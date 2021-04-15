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
	
	<h2>Evaluadores de un área</h2>
	
	<div class="form-select">
		<label>Área</label>
		<select id="select_area">
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
	
	<div id="tablas_evaluadores">
		<div class="titulo-bolsa-empleo">
			<h4 id="title_evaluadores">Evaluadores del Área</h4>
	    
		    <a class="link-btn" id="nuevo_evaluador" href="<%=request.getRequestURI()%>">
		    	 Añadir evaluador
		    </a>
		</div>
		<table class="bluetable bolsaempleo" id="table_evaluadores_area">
			<tr>
				<th scope="col" style="width:20%">D.N.I</th>
				<th scope="col"	style="width:35%">Tipo</th>
				<th scope="col"	style="width:35%">Nombre</th>
				<th scope="col" style="width:10%">Activo</th>
				<th scope="col" style="width:10%"></th>
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colspan="5" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	
</div>

<script>

	$(document).ready(function() {
		
		var table_evaluadores;
		
		function inicializarTablas(id) {
			table_evaluadores = new DataTable('#table_evaluadores_area', {
			    "ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionEvaluadores.PARAM_AREA%>": id},
			    "pageSize": 10,
			    "action": "<%=ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES%>",
			    "columns": [
			    	{'data': 'numdocumento'},
			    	{'data': 'rol.descripcion'},
			    	{'data': 'apellido1', 'render': function(row) {
		        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>"; 
		        	}},
		        	{'data': 'activo'},
		        	{'data': 'codnum', 'buttons': [{'label': function(row) { return row.activo ? "Borrar" : "Restaurar"; }, 'onClick': function(row) {
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
						        			'<%= ControladorGestionEvaluadores.PARAM_ACTIVO %>': !row.activo};
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
		
		function cargarTablas(id) {
			table_evaluadores.setParam("<%=ControladorGestionEvaluadores.PARAM_AREA%>", id);
			table_evaluadores.refresh();
		}
		
		function onChangeArea(select) {
			if(document.getElementById("tablas_evaluadores").style.visibility == "hidden") {
				document.getElementById("tablas_evaluadores").style.visibility = "visible";
				inicializarTablas(select.value);
				$("#title_evaluadores").text("Evaluadores del Área " + $(select).children("option").filter(":selected").text());
			} else {
				cargarTablas(select.value);
				$("#title_evaluadores").text("Evaluadores del Área " + $(select).children("option").filter(":selected").text());
			}
		}
		
		document.getElementById("tablas_evaluadores").style.visibility = "hidden";
		
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
			document.getElementById("tablas_evaluadores").style.visibility = "visible";
			inicializarTablas("<%= bean.getArea().getCodNum() %>");
		<%} %>
		
	});
	
</script>
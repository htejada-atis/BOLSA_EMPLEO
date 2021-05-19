<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEvaluadores bean = (VistaEvaluadores) uvdatos.getVistas().get(VistaEvaluadores.class.getName());
Departamento departamento = bean.getDepartamento();
%>

<div class="bolsa-empleo evaluadores-listar">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Evaluadores de un departamento</h2>
	
	<div class="">
	    <div class="form-group-container col2">
	    	<div class="form-group">   
	    		<div class="form-check-custom w-64">
					<label>Departamento</label>
					<select id="select_departamento">
						<option value="0">Elija el departamento</option>
	    				<%for(Departamento dep: bean.getDepartamentos()){
	    					if (departamento != null) {
	            				if (departamento.getCodNum().equals(dep.getCodNum())) {%>
	        						<option value="<%=dep.getCodNum()%>" selected="selected"><%=dep.getDescripcion()%></option>
	        				<%	} else { %>
									<option value="<%=dep.getCodNum()%>"><%=dep.getDescripcion()%></option>
							<%	}
	    					} else { %>
								<option value="<%=dep.getCodNum()%>"><%=dep.getDescripcion()%></option>
						<%	}
						}%>
					</select>
				</div>
			</div>
			<div class="form-group-container-offset col2"  id="nuevo_evaluador_cont">   
	    		<div class="form-group" style="width:80%">
					<label for="bloque_nombre">Nombre Evaluador</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR %>" id="nombre_evaluador" value=""/>
	    		</div>
	    		<div class="form-group" style="width:20%">
					<a class="link-btn" id="nuevo_evaluador" href="<%=request.getRequestURI()%>" style="display:inline-block !important; padding: 0; margin-top: 1.4rem" title="Buscar evaluador">
						<img alt="nombre de usuario evaluador" id="iconoMenu" src="/img/md/search_white.svg" width="25"/>
	   	 			</a>
	   			</div>
			</div>
		</div>
	</div>
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:10%" title="Id de la area">Id</th>
			<th scope="col" style="width:20%" class="codigo" title="Código de area">Código</th>
			<th scope="col" style="width:60%">Area</th>
			<th scope="col" style="width:15%">Nº Evaluadores</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<table class="bluetable bolsaempleo" id="tableEvaluadoresArea" style="visibility: hidden">
		<tr>
			<th scope="col" class="dni" style="width:25%">D.N.I</th>
			<th scope="col"	style="width:55%">Nombre</th>
			<th scope="col" class="center" style="width:10%">Activo</th>
			<th scope="col" style="width:11%"></th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>

	$(document).ready(function() {
		
		var tableAreas;
		
		function checkVisibility() {
			return document.getElementById("tableAreas").style.visibility == "hidden";
		}
		
		function inicializarTablas(id, title) {
			tableAreas = new Atis.DataTable('#tableAreas', {
			    "ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>", async: false },
			    "pageSize": 10,
			    "action": "<%= ControladorGestionEvaluadores.ACCION_DATATABLE_AREAS %>",
			    "params": {"<%=ControladorGestionEvaluadores.PARAM_DEPARTAMENTO%>": id},
			    "filterable": true,
			    "selectable": true,
			    "title": title,
			    "clickable": {'onClick': function(row) {
			    	var params = {
		    				'a': '<%= ControladorGestionEvaluadores.ACCION_AREA_SELECCIONADA %>', 
		    				'<%= ControladorGestionEvaluadores.PARAM_AREA %>': row.codNum,
			    			'<%= ControladorGestionEvaluadores.PARAM_DEPARTAMENTO %>': document.getElementById("select_departamento").value };
	        		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    }},
			    <% if (bean.getArea() != null) { %> "selected": <%= bean.getArea().getCodNum() %> ,<% } %>
			    "columns": [
			    	{'data': 'codNum', 'selectable': true},
			        {'data': 'codNum', 'filter': {'type': 'number'}},
			        {'data': 'idAreaExterno', 'filter': true},
			        {'data': 'descripcion', 'filter': true},
			        {'data': 'numeroEvaluadores', 'order': true}
				]
			});
		}
		
		function cargarTablas(id, title) {
			tableAreas.setParam("<%=ControladorGestionEvaluadores.PARAM_DEPARTAMENTO%>", id);
			tableAreas.setTitle(title);
			tableAreas.refresh();
		}
		
		function onChangeDepartamento(select) {
			var title_areas = "Áreas del Departamento: " + $(select).children("option").filter(":selected").text();
			if(checkVisibility()) {
				document.getElementById("nuevo_evaluador_cont").style.visibility = "visible";
				document.getElementById("tableAreas").style.visibility = "visible";
				inicializarTablas(select.value, title_areas);
			} else {
				cargarTablas(select.value, title_areas);
			}
		}
		
		document.getElementById("select_departamento").onchange = function () {
			if(this.value != 0) {
				onChangeDepartamento(this);
			}
		}
		
		document.getElementById("nuevo_evaluador").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%=ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES%>',
					'<%=ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR%>': document.getElementById("nombre_evaluador").value,
					'<%=ControladorGestionEvaluadores.PARAM_AREAS%>': Atis.object2Json(tableAreas.getCheckedItems()),
					'<%=ControladorGestionEvaluadores.PARAM_DEPARTAMENTO%>': document.getElementById("select_departamento").value
			}
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		<% if (bean.getArea() != null) { %>
		
			var tableEvaluadores = new Atis.DataTable('#tableEvaluadoresArea', {
			    "ajax": { url: "<%=ControladorGestionEvaluadores.URL_PATTERN_AJAX%>", async: false },
			    "params": {"<%=ControladorGestionEvaluadores.PARAM_AREA%>": "<%= bean.getArea().getCodNum() %>"},
			    "pageSize": 10,
			    "title": "Evaluadores del área: <%= bean.getArea().getDescripcion() %>",
			    "filterable": true,
			    "action": "<%=ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES%>",
			    "columns": [
			    	{'data': 'numdocumento', 'filter': true},
			    	{'data': 'apellido1', 'filter': true, 'render': function(row) {
		        		return "<div class='overflow-auto'>" + row.nombre + "\n" + row.apellido1 + "\n" + row.apellido2 + "</div>";
		        	}},
		        	{'data': 'activo', 'filter': {'type': 'select', 'options':{'true': 'Activo', 'false': 'Inactivo'}, 'optionDefault': 'true'}, 'order': {'active': false}, 'render': function(row) {
		        		if (row.activo) {
		        			return "<div title='Activo' class='circle-true'></div>";
		        		} else {
		        			return "<div title='Desactivado' class='circle-false'></div>";
		        		}
		        	}},
		        	{'data': 'codnum', 'buttons': [{'label': function(row) { return row.activo ? "Borrar" : "Restaurar"; },
		        		'title':  function(row) { return row.activo ? "Desactivar evaluador" : "Activar evaluador"; }, 'onClick': function(row) {
			        		var mensaje = "¿ Desea borrar el evaluador seleccionado?";
				        	var titulo = "Borrar evaluador";
				        	if(!row.activo) {
				        		titulo = "Restaurar evaluador";
				        		mensaje = "¿ Desea restaurar el evaluador seleccionado?";
				        	}
				        	
				        	Atis.confirmDialog(titulo, mensaje, {
						        Si: function() {
						        	var params = {'a': '<%= ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR %>',
						        			'<%= ControladorGestionEvaluadores.PARAM_USUARIO %>': row.codNum,
						        			'<%= ControladorGestionEvaluadores.PARAM_AREA %>': row.codNumArea,
						        			'<%= ControladorGestionEvaluadores.PARAM_ACTIVO %>': !row.activo,
						        			'<%= ControladorGestionEvaluadores.PARAM_DEPARTAMENTO %>': <%= departamento.getCodNum() %>
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
			
			document.getElementById("tableEvaluadoresArea").style.visibility = "visible";
		
		<% } %>
		
		<% if (departamento != null) { %>
			document.getElementById("select_departamento").value = "<%= departamento.getCodNum() %>";
			document.getElementById("nuevo_evaluador_cont").style.visibility = "visible";
			document.getElementById("tableAreas").style.visibility = "visible";
			inicializarTablas("<%= departamento.getCodNum() %>", "Áreas del Departamento: <%= departamento.getDescripcion() %>");
		<%} else { %>
			document.getElementById("select_departamento").options[0].selected = true;
			document.getElementById("nuevo_evaluador_cont").style.visibility = "hidden";
			document.getElementById("tableAreas").style.visibility = "hidden";
		<%} %>
		
	});
	
</script>

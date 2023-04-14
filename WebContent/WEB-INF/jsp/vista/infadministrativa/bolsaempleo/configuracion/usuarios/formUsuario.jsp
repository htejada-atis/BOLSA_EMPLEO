<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="java.util.Date"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo usuarios-form">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<% 
		String nombre = "";
		String apellidos = "";
		String email = "";
		String emailUja = "";
		String tipo_documento = "";
		String n_documento = "";
		String razon_excluido = "";
		String usuario = "";
		Boolean lista_dist = false;
		Boolean excluido = false;
		Date fecha_ini = null;
		Date fecha_fin = null;
		
		if(bean.getUsuarioArcos() != null) {
			usuario = bean.getUsuarioArcos().getUid();
			nombre = bean.getUsuarioArcos().getNombre();
			apellidos = bean.getUsuarioArcos().getApellido1() + " " + bean.getUsuarioArcos().getApellido2();
			emailUja = bean.getUsuarioArcos().getEmailCalculado();
			email = bean.getUsuario().getEmail();
			tipo_documento = bean.getUsuarioArcos().getDocumentoTipo();
			n_documento = bean.getUsuarioArcos().getDocumentoNumero();
			
			if(bean.getUsuario() != null){
				lista_dist = bean.getUsuario().getListaDist();
				excluido = bean.getUsuario().getExcluido();
				fecha_ini = bean.getUsuario().getFechaExclusionInicio();
				fecha_fin = bean.getUsuario().getFechaExclusionFin();

				if(bean.getUsuario().getRazonExcluido()!=null){
					razon_excluido = bean.getUsuario().getRazonExcluido();
				}
				else{
					razon_excluido ="";
				}
				
				if(bean.getBusqueda()){
			    	out.print("<h2>El usuario ya existe</h2>");
				}
				else{
			    	out.print("<h2>Editar Usuario</h2>");
				}
			}
			else{
			    out.print("<h2>Nuevo Usuario</h2>");
			}

		} else {
		    out.print("<h2>Nuevo Usuario</h2>");
		}
	%>
	
	<div id="erroresForm" class="error" style="display:none;">
		<p>Porfavor primero debe rellenar todos los campos requeridos del formulario:</p>
		<ul id="erroresFormList">
		</ul>
	</div>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="usuario_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>" id="usuario_id" value="" />
		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" id="usuario_nombre" value="" />
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="nickname">Usuario: </label>
    			<input class="form-input-custom" id="nickname" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled/>
    		</div>
	   		<div class="form-group">
    			<label for="nombre">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="nombre" value="<%= nombre %>" disabled/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="apellidos">Apellidos: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="apellidos" value="<%= apellidos %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="email">Email: <small>(<%= emailUja %>)</small></label>
    			<input class="form-input-custom" id="email" type="text" name="email" value="<%= email %>" disabled/>
    		</div>
    	</div>

		<div class="form-group-container">
	    	<div class="form-group">
    			<label for="tipo_documento">Tipo de documento: </label>
    			<input class="form-input-custom" id="tipo_documento" type="text" name="tipo_documento" value="<%= tipo_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="n_documento">Nº de documento: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="n_documento" value="<%= n_documento %>" disabled/>
    		</div>
		</div>
		<div class="form-group-container">
	    	<div class="form-check-custom">
    			<label for="usuario_lista_dist"><input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_LISTA %>" 
    				value="true" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
    				<label for="usuario_excluido"><input class="params" type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO %>" 
    					value="true" <%= (excluido ? "checked=''" : "") %>/>Excluido</label>
    			</div>
    			<div class="form-check-custom" id="excluido_tipo" style="display:none;">
					<label class="form-label-custom" for="indefinido" style="float: none; margin-right:0px; margin-bottom:5px;">
						<input class="form-input" type="radio" id="indefinido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>"
							value="I" style="display: inline;">Indefinido
					</label>
					<label class="form-label-custom" for="temporal" style="float: none; margin-right:0px;">
						<input class="form-input" type="radio" id="temporal" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>"
							value="T" style="display: inline;">Temporal
					</label>
    			</div>
    		</div>
		</div>
		<div class="form-group-container col-2" id="fecha_excluido" style="display:none;">
	    	<div class="form-check">
    			
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Inicio</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_INICIO %>" id="fecha_ini" autocomplete="off" value="<%= fecha_ini!=null ? fecha_ini : "" %>" style="width:80%"/>
	    		</div>
	    		<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Fin</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_FIN %>" id="fecha_fin" autocomplete="off" value="<%= fecha_fin!=null ? fecha_fin : "" %>" style="width:80%"/>
	    		</div>
	    	</div>
		</div>
	
		<div class="form-group" id="razon_excluido" style="display:none;">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60" <%= (excluido ? "" : "disabled") %>><%= razon_excluido %></textarea>
   		</div>
		
		<div class="form-group">
			<label for="select_role"><strong>Elija el rol para asociar</strong></label>
			<select class="params" id="select_role" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ROLE %>" style="width:100%;" required>
			<% 	if(bean.getUsuario() == null) { %>
					<option value="">Elija el rol</option>
			<%	} %>
   			<%	for(Rol role: bean.getRoles()) {
   					if(bean.getUsuario()!=null) {
           				if(bean.getRol().getCodNum().equals(role.getCodNum())){%>
       						<option value="<%=role.getCodNum()%>" selected="selected"><%=role.getDescripcion()%></option>
       				<%	} else {%>
							<option value="<%=role.getCodNum()%>"><%=role.getDescripcion()%></option>
					<%	}
   					} else {%>
						<option value="<%=role.getCodNum()%>"><%=role.getDescripcion()%></option>
				<%	}
						
				}%>
			</select>
		</div>
    	
    	<div class="form-group-container col1">
    		<div class="form-group" style="float:right;">
    		
    		<%	if (bean.getUsuario() != null) { %>
    				
    			<%	if (bean.getBusqueda()) { %>
    					<input id="usuario_volver" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_VOLVER %>" value="Volver" style="float:right; margin-left: 12px;"/>
    			<%	} else { %>
    					<input id="usuario_guardar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_GUARDAR %>" value="Guardar" style="float:right; margin-left: 12px;"/>
    				<%	if (bean.getUsuario().getBorrado()) { %>
    						<label for="usuario_restaurar" style="margin-top: 5px; margin-left: 6px; float:right; width: unset !important;">Usuario Borrado</label>
    						<input id="usuario_restaurar" type="button" value="Restaurar" style="float:right;"/>
    				<%	} else { %>
    						<input id="usuario_borrar" type="button" value="Dar de baja usuario" style="float:right;"/>
    				<%	} %>
    					
    			<%	} %>
    				
    		<%	} else { %>
    				<input id="usuario_guardar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_AGREGAR %>" value="Añadir usuario" style="float:right; margin-left: 12px;"/>
    		<%	} %>
    		
    		</div>
    	</div>
    	
    </form>
    
<%	if (bean.getUsuario() != null && (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_MIEMBRO_COMISION) 
		|| bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO))) { %>
	    <ul class="nav-tabs-widget" style="margin-left: 0px; margin-top: 16px;">
	    	<li <%= bean.getApartadoAreasEvaluables() != null ? "class='tab-selected'" : "" %>>
	   	    	<a id="areas_evaluables">Áreas evaluables</a>
	   	    </li>
	   	<%	if (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) { %>
	   			<li <%= bean.getApartadoDepartamentos() != null ? "class='tab-selected'" : "" %>>
		   	    	<a id="departamentos">Departamentos</a>
		   	    </li>
	   	<%	} %>
	   	</ul>
	   	
	<%	if (bean.getApartadoAreasEvaluables() != null) { %>
			<table class="bluetable bolsaempleo" id="tableAreasEvaluablesUSU">
				<tr>
					<th scope="col" style="width:20%" title="Id de la area">Id</th>
					<th scope="col" style="width:25%" title="Código de area">Código</th>
					<th scope="col" style="width:65%">Area</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="3"></th>
					</tr>
				</tfoot>
			</table>
			
		<%	if (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) { %>
				<div class="row">
					<div class="col">
						<button class="link-btn" id="actualizar_areas" style="float:right;">
							Actualizar áreas
						</button>
					</div>
				</div>
		<%	} %>
			
	<%	} %>
	
	<%	if (bean.getApartadoDepartamentos() != null) { %>
			<table class="bluetable bolsaempleo" id="tableDepartamentosUSU">
				<tr>
					<th scope="col" style="width:60px" title="Id del departamento">Id</th>
					<th scope="col" style="width:100px" title="Código de departamento">Código</th>
					<th scope="col" style="width:100%">Departamento</th>
				</tr>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colSpan="3"></th>
					</tr>
				</tfoot>
			</table>
	<%	} %>
	
<%	} %>

</div>

<script>

	function checkRequiredInputs(){
		if($('#select_role').val()==-1){
			$('#erroresFormList').html("");
			$('#erroresFormList').append( "<li>Rol</li>" );
			$('#erroresForm').show();
			return false;
		}
		return true;
	}

	$(document).ready(function() {
		
		$("#usuario_form").on("submit", function() {
		<%	if (bean.getUsuario() != null) { 
			
				if (bean.getBusqueda()) { %>
					$(this).find("#accion_formulario").val('<%= ControladorUsuarioBolsaEmpleo.ACCION_VOLVER_USUARIO %>');
			<%	} else { %>
					$(this).find("#accion_formulario").val('<%= ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO %>');
					input_id = document.getElementById("usuario_id");
					$(this).find("#usuario_id").val('<%= bean.getUsuario().getCodNum() %>');
					input_nombre_usuario = document.getElementById("usuario_nombre");
					$(this).find("#usuario_nombre").val('<%= bean.getUsuario().getCodCuenta()%>');
			<%	} %>
				
		<%	} else { %>
				$(this).find("#usuario_nombre").val('<%= bean.getUsuarioArcos().getUid() %>');
				$(this).find("#accion_formulario").val('<%= ControladorUsuarioBolsaEmpleo.ACCION_AGREGAR_USUARIO %>');
		<%	} %>
		
		});
		
		$("#fecha_ini").datepicker();
		$("#fecha_fin").datepicker();
		
		<% if (fecha_ini == null) { %>
			document.getElementById("fecha_ini").value = getTodayDate();
		<% } %>
		
		document.getElementById("usuario_excluido").addEventListener("change", function(event) {
			if (document.getElementById("usuario_excluido").checked) {
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=false;
				
				$("#excluido_tipo").show();
				$("#razon_excluido").show();
				$("#razon_excluido").children("textarea").attr("required", true);
				$("#excluido_tipo").find("input").attr("required", true);
			} else {
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=true;
				
				$("#excluido_tipo").hide();
				$("#razon_excluido").hide();
				$("#razon_excluido").children("textarea").attr("required", false);
				$("#excluido_tipo").find("input").attr("required", false);
			}
		});
		
		document.getElementById("temporal").addEventListener("change", function(event) {
			if(document.getElementById("temporal").checked) $("#fecha_excluido").show();
		});
		document.getElementById("indefinido").addEventListener("change", function(event) {
			if(document.getElementById("indefinido").checked) $("#fecha_excluido").hide();
		});
	
	<%	if (bean.getBusqueda()) { %>
			params = document.getElementsByClassName("params");
			for (var i = 0; i < params.length; i++) {
				params[i].disabled = true;
			}
	<%	} %>
	
	<%	if (bean.getUsuario() != null && bean.getUsuario().getBorrado()) { %>
			document.getElementById("usuario_restaurar").addEventListener("click", function(event) {
				event.preventDefault();
				
				Atis.confirmDialog("Restaurar usuario", "¿Desea restaurar el usuario?", {
			    	Si: function() {
			    		Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION%>': '<%=ControladorUsuarioBolsaEmpleo.ACCION_RECUPERAR_USUARIO%>',
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>': '<%=bean.getUsuario().getCodNum()%>'
						});
			          	$(this).dialog("close");
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} else if (bean.getUsuario() != null && !bean.getUsuario().getBorrado()) { %>
			document.getElementById("usuario_borrar").addEventListener("click", function(event) {
				event.preventDefault();
				
				var message = "<h3>¿Desea dar de baja al usuario?</h3><br/>" +
				"<div class='form-group-dialog'>" +
				"	<label for='razon_borrado'>Razón borrado: </label>" +
				"	<textarea id='razon_borrado' name='razonborrado' rows='2' required='required'></textarea>" +
				"</div>";
				
				Atis.confirmDialog("Dar de baja usuario", message, {
			    	Si: function() {
			    		var inputRazon = this.querySelector('#razon_borrado');
			    		if (inputRazon.value != '') {
				    		Atis.sendForm("<%= request.getRequestURI() %>", {
								'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION%>': '<%=ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO%>',
								'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>': '<%=bean.getUsuario().getCodNum()%>',
								'<%=ControladorUsuarioBolsaEmpleo.PARAM_RAZON_BORRADO%>': inputRazon.value
							});
				          	$(this).dialog("close");
				    	} else {
			    			inputRazon.setCustomValidity("La razón de borrado no puede estar vacía");
			    			inputRazon.reportValidity();
			    		}
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} %>
	
	<%	if (bean.getUsuario() != null && (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_MIEMBRO_COMISION) 
			|| bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO))) { %>
			
			document.getElementById("areas_evaluables").addEventListener("click", function(event) {
				Atis.sendForm("<%= request.getRequestURI() %>", {
					'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION%>': '<%=ControladorUsuarioBolsaEmpleo.ACCION_AREAS_EVALUABLES%>',
					'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>': <%= bean.getUsuario().getCodNum() %>
				});
			});
			
			<%	if (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) { %>
			
					document.getElementById("departamentos").addEventListener("click", function(event) {
						Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION%>': '<%=ControladorUsuarioBolsaEmpleo.ACCION_DEPARTAMENTOS %>',
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>': <%= bean.getUsuario().getCodNum() %>
						});
					});
				
			<%	} %>
			
		<%	if (bean.getApartadoAreasEvaluables() != null) { %>
				
				var tableAreasEvaluables = new Atis.DataTable('#tableAreasEvaluablesUSU', {
				    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false},
				    "params": {"<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>": <%= bean.getUsuario().getCodNum() %>},
				    "pageSize": 10,
				    "filterable": true,
				    "title": 'LISTADO DE ÁREAS EVALUABLES',
				    "action": "<%= ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO %>",
				    "columns": [
				        {'data': 'area.codNum', 'filter': {'type': 'number'}},
				        {'data': 'area.idAreaExterno' , 'filter': true, 'overflow': 'auto'},
				        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'}
				    ],
				});
				
			<%	if (bean.getUsuario().getRol().getCodNum().equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) { %>
				
					document.getElementById("actualizar_areas").addEventListener("click", function(event) {
						Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_ACCION%>': '<%=ControladorUsuarioBolsaEmpleo.ACCION_ACTUALIZAR_AREAS %>',
							'<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>': <%= bean.getUsuario().getCodNum() %>
						});
					});
				
			<%	} %>
		
				Atis.smoothScrollToAnchor("#areas_evaluables");
		<%	} %>
		
		<%	if (bean.getApartadoDepartamentos() != null) { %>
		
				var tableDepartamentos = new Atis.DataTable('#tableDepartamentosUSU', {
				    "ajax": { url: "<%=ControladorUsuarioBolsaEmpleo.URL_PATTERN_AJAX%>", async: false},
				    "params": {"<%=ControladorUsuarioBolsaEmpleo.PARAM_USUARIO%>": <%= bean.getUsuario().getCodNum() %>},
				    "pageSize": 10,
				    "title": 'LISTADO DE DEPARTAMENTOS',
				    "action": "<%= ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_DEPARTAMENTOS_USUARIO %>",
				    "columns": [
				        {'data': 'codNum', 'order': false},
				        {'data': 'idDepartamentoExterno' , 'order': false},
				        {'data': 'descripcion', 'order': false}
				    ],
				});
		
				Atis.smoothScrollToAnchor("#departamentos");
		<%	} %>
		
	<%	} %>
		
		function getTodayDate() {
			var now = new Date();

			var day = ("0" + now.getDate()).slice(-2);
			var month = ("0" + (now.getMonth() + 1)).slice(-2);

			return day + "/" + month + "/" + now.getFullYear();
		}
	});
	
</script>
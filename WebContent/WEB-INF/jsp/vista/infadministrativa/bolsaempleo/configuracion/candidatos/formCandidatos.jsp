<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAreasABaremar"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="java.util.Date"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
Usuario usuarioArcos = bean.getUsuarioArcos();
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>


<div class="bolsa-empleo usuarios-form">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<% 
	
		Integer codnum = null;
		String nombre = "";
		String apellidos = "";
		String email = "";
		String tipo_documento = "";
		String n_documento = "";
		String razon_excluido = "";
		String usuario = "";
		Boolean lista_dist = false;
		Boolean excluido = false;
		Date fecha_ini = null;
		Date fecha_fin = null;
		
		if(usuarioArcos != null) {
			usuario = usuarioArcos.getUid();
			nombre = usuarioArcos.getNombre();
			apellidos = usuarioArcos.getApellido1() + " " + usuarioArcos.getApellido2();
			email = usuarioArcos.getEmailCalculado();
			tipo_documento = usuarioArcos.getDocumentoTipo();
			n_documento = usuarioArcos.getDocumentoNumero();
			
			if(candidato != null){
				codnum = candidato.getCodNum();
				lista_dist = candidato.getListaDist();
				excluido = candidato.getExcluido();
				fecha_ini = candidato.getFechaExclusionInicio();
				fecha_fin = candidato.getFechaExclusionFin();

				if(candidato.getRazonExcluido()!=null){
					razon_excluido = candidato.getRazonExcluido();
				}
				else{
					razon_excluido ="";
				}
				
				if(bean.getBusqueda()){
			    	out.print("<h2>El candidato ya existe</h2>");
				}
				else{
			    	out.print("<h2>Editar Candidato</h2>");
				}
			}
			else{
			    out.print("<h2>Nuevo Candidato</h2>");
			}

		} else {
		    out.print("<h2>Nuevo Candidato</h2>");
		}
	%>
	
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_ACCION_USUARIO %>" id="accion_formulario_usuario" value="" />
		<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_ID%>" id="usuario_id" value="" />
		<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO%>" id="usuario_nombre" value="" />
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="nickname">Usuario: </label>
    			<input class="form-input-custom" id="nickname" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled/>
    		</div>
	   		<div class="form-group">
    			<label for="nombre">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= nombre %>" disabled/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="apellidos">Apellidos: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= apellidos %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="email">Email: </label>
    			<input class="form-input-custom" id="email" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= email %>" disabled/>
    		</div>
    	</div>

		<div class="form-group-container">
	    	<div class="form-group">
    			<label for="tipo_documento">Tipo de documento: </label>
    			<input class="form-input-custom" id="tipo_documento" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= tipo_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="n_documento">Nº de documento: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= n_documento %>" disabled/>
    		</div>
		</div>
		<div class="form-group-container">
	    	<div class="form-check-custom">
    			<label for="usuario_lista_dist"><input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioCandidato.PARAM_LISTA %>" value="<%= lista_dist %>" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom" style="float: left;">
    				<label for="usuario_excluido"><input class="params" type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioCandidato.PARAM_EXCLUIDO %>"
    				 value="<%= excluido %>" <%= (excluido ? "checked=''" : "") %>
    				 <%= usuarioArcos != null && candidato != null ? "" : "disabled" %>/>Excluido</label>
    			</div>    		
    			<div class="form-check-custom" id="excluido_tipo" style="display:none;">
					<label class="form-label-custom" for="indefinido" style="float: none; margin-right:0px; margin-bottom:5px;"><input class="form-input" type="radio" id="indefinido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;" required>Indefinido</label>
					<label class="form-label-custom" for="temporal" style="float: none; margin-right:0px;"><input class="form-input" type="radio" id="temporal" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;">Temporal</label>
    			</div>
    		</div>
		</div>
		<div class="form-group-container col-2" id="fecha_excluido" style="display:none;">
	    	<div class="form-check">
    			
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Inicio</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_INICIO %>" id="fecha_ini" autocomplete="off" value="<%= fecha_ini!=null ? fecha_ini : "" %>" style="width:80%"/>
	    		</div>
	    		<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Fin</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_FIN %>" id="fecha_fin" autocomplete="off" value="<%= fecha_fin!=null ? fecha_fin : "" %>" style="width:80%"/>
	    		</div>
	    	</div>
		</div>
	
		<div class="form-group" id="razon_excluido" style="display:none;">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioCandidato.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60" <%= (excluido ? "" : "disabled") %>><%= razon_excluido %></textarea>
   		</div>
		
		<div class="form-group-container">
		 	<div class="form-group-custom w100">
    		<%	if (candidato != null && candidato.getBorrado()) { %>
    				<label for="usuario_restaurar" style="margin-top: 4px">Usuario Borrado</label>
    				<input id="usuario_restaurar" type="submit" name="<%= ControladorUsuarioCandidato.PARAM_RESTAURAR %> " value="Restaurar"/>
    		<%	} %>
    		</div>
    		<div class="form-group-custom w100" style="float:right;">
    			<input id="usuario_enviar" type="submit" name="<%= ControladorUsuarioCandidato.PARAM_ENVIAR %>" value="<%= candidato != null ? bean.getBusqueda() ? "Volver" : "Guardar candidato" : "Añadir candidato"%>" style="float:right;"/>
    		</div>
    	</div>
    	
    </form>
    
    <ul class="nav-tabs-widget" id="tabActions" style="margin-left: 0px">
    	<li <%= bean.getApartadoAreasExcluidas() != null ? "class='tab-selected'" : "" %>>
   	    	<a id="areas_excluidas">Áreas excluidas</a>
   	    </li>
   	    <li <%= bean.getApartadoSolicitudes() != null ? "class='tab-selected'" : "" %>>
   			<a id="solicitudes">Solicitudes</a>
   		</li>
   		<li <%= bean.getApartadoComunicaciones() != null ? "class='tab-selected'" : "" %>>
   			<a id="comunicaciones">Comunicaciones</a>
   		</li>
   	</ul>
   	
    
	<% if(bean.getApartadoAreasExcluidas()!=null){ %>
		<table class="bluetable bolsaempleo" id="table_areas_excluidas">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:20%" title="Id de la area">Id</th>
				<th scope="col" style="width:25%" title="Código de area">Código</th>
				<th scope="col" style="width:65%">Area</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="10" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
		
		<table class="bluetable bolsaempleo" id="table_areas">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:20%" title="Id de la area">Id</th>
				<th scope="col" style="width:25%" title="Código de area">Código</th>
				<th scope="col" style="width:65%">Area</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="10" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>		
	<%}%>   
	
	<% if(bean.getApartadoComunicaciones()!=null){ %>
	
		
	<%}%>  
	
	
	<% if(bean.getApartadoSolicitudes()!=null){ %>
		<table class="bluetable bolsaempleo" id="table_solicitudes">
			<tr>
				<th scope="col"	style="width:50%">Descripción</th>
				<th scope="col"	style="width:20%">Fecha cierre</th>
				<th scope="col"	style="width:30%">Estado convocatoria</th>				
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="10" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>	
	<%}%> 

</div>

<script>

	$(document).ready(function() {
		
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
			} else {
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=true;
				
				$("#excluido_tipo").hide();
				$("#razon_excluido").hide();
				$("#razon_excluido").children().attr("required", false);
			}
		});
		
		document.getElementById("temporal").addEventListener("change", function(event) {
			if(document.getElementById("temporal").checked) $("#fecha_excluido").show();
		});
		document.getElementById("indefinido").addEventListener("change", function(event) {
			if(document.getElementById("indefinido").checked) $("#fecha_excluido").hide();
		});
		
		if(document.getElementById("areas_excluidas")!=undefined){
			document.getElementById("areas_excluidas").addEventListener("click", function(event) {
				Atis.sendForm("<%= request.getRequestURI() %>", {
					'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_AREAS_EXCLUIDAS_CANDIDATO%>', 
					'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
				});
			});
		}
		
		if(document.getElementById("solicitudes")!=undefined){
			document.getElementById("solicitudes").addEventListener("click", function(event) {
				Atis.sendForm("<%= request.getRequestURI() %>", {
					'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_SOLICITUDES_CANDIDATO%>', 
					'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
				});
			});
		}
		
		if(document.getElementById("comunicaciones")!=undefined){
			document.getElementById("comunicaciones").addEventListener("click", function(event) {
				Atis.sendForm("<%= request.getRequestURI() %>", {
					'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_SELECCION_APARTADO%>', 
					'<%=ControladorUsuarioCandidato.PARAM_ACCION_USUARIO%>': '<%=ControladorUsuarioCandidato.ACCION_APARTADO_COMUNICACIONES%>',
					'<%=ControladorUsuarioCandidato.PARAM_ID%>': <%= codnum %>
				});
			});
		}
		
		document.getElementById("usuario_enviar").addEventListener("click", function(event) {
			enviarUsuario(event, this);
		});
		
		<% if(bean.getBusqueda()) {%>
		params = document.getElementsByClassName("params");
		for (var i = 0; i < params.length; i++) { 
			params[i].disabled = true;
		}
		<%}%>
		
		<% if(bean.getApartadoSolicitudes()!=null){ %>
			var table = new Atis.DataTable('#table_solicitudes', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>" },
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "pageSize": 10,
			    "title": "SOLICITUDES",
			    "filterable": true,
			    "action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_SOLICITUDES %>",
			    "columns": [
			    	{'data': 'convocatoria.descripcion', order: {'active': false}},
			        {'data': 'convocatoria.fechaCierre', order: {'active': false}},
			        {'data': 'convocatoria.estado', order: {'active': false}}
			    ]
			});	
		<%}%> 
		
		<% if(bean.getApartadoAreasExcluidas() != null) {%>
			var table = new Atis.DataTable('#table_areas_excluidas', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "selectable": true,
			    "pageSize": 10,
			    "filterable": true,
			    "title": 'ÁREAS EXCLUIDAS PARA EL USUARIO',
			    "action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO%>",
			    "columns": [
			    	{'data': 'area.codNum', 'selectable': true},
			        {'data': 'area.codNum', 'filter': {'type': 'number'}},
			        {'data': 'area.idAreaExterno', 'filter': true, 'overflow': 'auto'},
			        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'}
			    ],
			    "actions": [
			    	{'label': 'Borrar Areas excluidas', 'onClick': function(selected) { excluirArea(false, selected); } }
			    ]
			});
		
			var table = new Atis.DataTable('#table_areas', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "selectable": true,
			    "pageSize": 10,
			    "filterable": true,
			    "title": 'LISTADO DE ÁREAS',
			    "action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO %>",
			    "columns": [
			    	{'data': 'area.codNum', 'selectable': true},
			        {'data': 'area.codNum', 'filter': {'type': 'number'}},
			        {'data': 'area.idAreaExterno' , 'filter': true, 'overflow': 'auto'},
			        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'}
			    ],
			    "actions": [
			    	{'label': 'Excluir Areas', 'onClick': function(selected) { excluirArea(true, selected); } }
			    ]
			});
			
			
			function excluirArea(excluir, selected) {
				if (selected.length == 0) {
					Atis.alertDialog("Exclusión de areas", "Seleccione al menos un usuario.");
					return;
				}
				
				var message = excluir ? "¿Desea excluir las areas seleccionadas del usuario?" : "¿Desea borrar las areas excluidas seleccionadas del usuario?";
				Atis.confirmDialog("Exclusión de areas", message, {
		        	Si: function() {
		        		var params = {
		        				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': excluir ? "<%=ControladorUsuarioCandidato.ACCION_EXCLUIR_USUARIO_AREA%>" : "<%= ControladorUsuarioCandidato.ACCION_INCLUIR_USUARIO_AREA %>",
		        				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= candidato.getCodNum() %>,
		        				'<%=ControladorUsuarioCandidato.PARAM_AREAS_SELECCIONADAS%>': Atis.object2Json(selected)
		        			};
		    			Atis.sendForm("<%= request.getRequestURI() %>", params);
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
			}
		
		<%}%>
		
		function enviarUsuario(event, submit_input) {
			event.preventDefault();
		
			<% if(candidato != null) { 
				
				if(bean.getBusqueda()) {%>
					input_accion = document.getElementById("accion_formulario");
					input_accion.value = '<%= ControladorUsuarioCandidato.ACCION_VOLVER_USUARIO %>';
				<%}else{%>
					input_accion = document.getElementById("accion_formulario");
					input_accion.value = '<%= ControladorUsuarioCandidato.ACCION_EDITAR_USUARIO %>';
					input_id = document.getElementById("usuario_id");
					input_id.value = '<%= candidato.getCodNum() %>';
					input_nombre_usuario = document.getElementById("usuario_nombre");
					input_nombre_usuario.value = '<%= candidato.getCodCuenta()%>';
				<%}%>
				
			<% } else { %>
				input_id = document.getElementById("usuario_id");
				input_id.value = '<%= usuarioArcos.getUid() %>';
				input_accion = document.getElementById("accion_formulario");
				input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_AGREGAR_USUARIO %>';
			<% } %>
		
			input_lista_dist = document.getElementById("usuario_lista_dist");
			input_lista_dist.value = input_lista_dist.checked;
		
			input_excluido = document.getElementById("usuario_excluido");
			input_excluido.value = input_excluido.checked;
			
			input_excluido = document.getElementById("usuario_excluido");
			input_excluido.value = input_excluido.checked;
			
			if(input_excluido.checked){
				input_exc_indefinido = document.getElementById("indefinido");
				input_exc_temporal = document.getElementById("temporal");
				
				if(input_exc_indefinido.checked){
					input_exc_indefinido.value = "I";
					input_exc_temporal.value = "I";
				}
				else{
					input_exc_indefinido.value = "T";
					input_exc_temporal.value = "T";
				}
			}
		
			submit_input.form.submit();
		}
		
		function getTodayDate() {
			var now = new Date();

			var day = ("0" + now.getDate()).slice(-2);
			var month = ("0" + (now.getMonth() + 1)).slice(-2);

			return day + "/" + month + "/" + now.getFullYear();
		}
		
		<%if(candidato!=null && !bean.getBusqueda()){%>
			$("#tabActions").show();
		<%}%>

	});
	
	
</script>

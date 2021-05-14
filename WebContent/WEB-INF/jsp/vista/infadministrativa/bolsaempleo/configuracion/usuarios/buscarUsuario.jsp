<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo usuarios-buscar">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<% if(bean.getUsuario()!=null) {%>
		<% if(bean.getBusqueda()) {%>
		<h2>Excluir Usuario</h2>
		<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
   			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO%>" id="usuario_nombre" value="" />
			<div class="form-group-container">
    			<div class="form-group">
    				<label for="razon_exclusion">Razón exclusión</label>
    				<textarea class="params" id="razon_exclusion" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO %>" rows="3" style="width:100%;"></textarea>
    			</div>
    			<div class="form-check" style="margin-top:1.35rem;">	
    			   	<div class="form-check-custom w-35">	
						<label class="form-label-custom" for="indefinido" style="float: none; margin-right:0px;"><input class="form-input" type="radio" id="indefinido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;">Indefinido</label>
						<label class="form-label-custom" for="temporal" style="float: none; margin-right:0px; margin-top:1rem;"><input class="form-input" type="radio" id="temporal" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>" style="display: inline;">Temporal</label>
    				</div>
    				<div class="form-check-custom w-64" id="fecha_excluido" style="display:none;">	
						<div class="form-check-custom">
	    					<label for="noticia_fecha"><b>Fecha Inicio</b>:</label>
	    					<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_INICIO %>" id="fecha_ini" autocomplete="off" style="width:90%"/>
	    				</div>
	    				<div class="form-check-custom">
	    					<label for="noticia_fecha"><b>Fecha Fin</b>:</label>
	    					<input class="form-input-custom" type="text" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_FIN %>" id="fecha_fin" autocomplete="off" style="width:80%"/>
	    				</div>
    				</div>
    			</div>
    			<div class="form-btn">
   					<input id="usuario_excluir" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>" value="Excluir"/>
    			</div>
			</div>
    	</form>
    	<% }%>
	<% } else { %>
		<h2>Nuevo Usuario</h2>
		<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    		<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ACCION %>" id="accion_formulario" value="" />
			<input type="hidden" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_ID%>" id="usuario_id" value="" />
    		<div class="form-group">
    			<label for="usuario_nombre">Nombre de usuario</label>
    			<input type="text" name="nombre" id="usuario_nombre"/>
    		</div>
    		<div class="form-btn">
    			<input id="usuario_buscar" type="submit" name="<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>" value="Buscar usuario"/>
    		</div>
    	</form>
		
	<% } %>
    
</div>

<script>

	$(document).ready(function() {
	
		
		<% if(bean.getUsuario()!=null && bean.getBusqueda()){%>
		$("#fecha_ini").datepicker();
		$("#fecha_fin").datepicker();
		
		document.getElementById("fecha_ini").value = getTodayDate();
		
		document.getElementById("temporal").addEventListener("change", function(event) {
			if(document.getElementById("temporal").checked){
				$("#fecha_excluido").show();
			}
		});
		document.getElementById("indefinido").addEventListener("change", function(event) {
			if(document.getElementById("indefinido").checked){
				$("#fecha_excluido").hide();
			}
		});
		<%}%>

		if(document.getElementById("usuario_buscar")!=undefined){
			document.getElementById("usuario_buscar").addEventListener("click", function(event) {
				event.preventDefault();
				Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO %>',
					'nombre': document.getElementById("usuario_nombre").value});
			});
		}
		
		if(document.getElementById("usuario_excluir")!=undefined){
			document.getElementById("usuario_excluir").addEventListener("click", function(event) {
				enviarUsuario(event, this);
			});
		}
	});
	
	function enviarUsuario(event, submit_input) {
		event.preventDefault();

		<%if(bean.getUsuario()!=null){%>
		
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO %>';
		input_id = document.getElementById("usuario_id");
		input_id.value = '<%= bean.getUsuario().getCodNum() %>';
		input_nombre_usuario = document.getElementById("usuario_nombre");
		input_nombre_usuario.value = '<%= bean.getUsuario().getCodCuenta()%>';
		
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

		submit_input.form.submit();
		<%}%>
	}
	
	function getTodayDate() {
		var now = new Date();

		var day = ("0" + now.getDate()).slice(-2);
		var month = ("0" + (now.getMonth() + 1)).slice(-2);

		return day + "/" + month + "/" + now.getFullYear();
	}

</script>
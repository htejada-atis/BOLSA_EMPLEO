<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMisDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo misdatos-form">

	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } %>

	<h2>Datos Personales</h2>

	<% 
	
	String nombre = "";
	String primer_apellido = "";
	String segundo_apellido = "";
	String email = "";
	String tipo_documento = "";
	String n_documento = "";
	String razon_excluido = "";
	String direccion = "";
	String codigo_postal = "";
	String localidad = "";
	String provincia = "";
	String telefono = "";
	String nacionalidad = "";
	String sexo = "";
	Boolean lista_dist = false;
	
	if(bean.getUsuario().getNombre()!=null) nombre = bean.getUsuario().getNombre();
	if(bean.getUsuario().getPrimerApellido()!=null) primer_apellido = bean.getUsuario().getPrimerApellido();
	if(bean.getUsuario().getSegundoApellido()!=null) segundo_apellido = bean.getUsuario().getSegundoApellido();
	if(bean.getUsuario().getSegundoApellido()!=null) segundo_apellido = bean.getUsuario().getSegundoApellido();
	if(bean.getUsuario().getEmail()!=null) email = bean.getUsuario().getEmail();
	if(bean.getUsuarioArcos().getDocumentoTipo()!=null) tipo_documento = bean.getUsuarioArcos().getDocumentoTipo();
	if(bean.getUsuarioArcos().getDocumentoNumero()!=null) n_documento = bean.getUsuarioArcos().getDocumentoNumero();
	if(bean.getUsuario().getDireccion()!=null) direccion = bean.getUsuario().getDireccion();
	if(bean.getUsuario().getCodigoPostal()!=null) codigo_postal = bean.getUsuario().getCodigoPostal();
	if(bean.getUsuario().getLocalidad()!=null) localidad = bean.getUsuario().getLocalidad();
	if(bean.getUsuario().getProvincia()!=null) provincia = bean.getUsuario().getProvincia();
	if(bean.getUsuario().getTelefono()!=null) telefono = bean.getUsuario().getTelefono();
	if(bean.getUsuario().getNacionalidad()!=null) nacionalidad = bean.getUsuario().getNacionalidad();
	if(bean.getUsuario().getSexo()!=null) sexo = bean.getUsuario().getSexo();
	if(bean.getUsuario()!=null) lista_dist = bean.getUsuario().getListaDist();
	
	%>
	
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMisDatos.PARAM_ACCION %>" id="accion_formulario" value="" />
		<input type="hidden" name="<%= ControladorMisDatos.PARAM_ID%>" id="usuario_id" value="" />
		
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="n_documento"><%= tipo_documento %>: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="<%= ControladorMisDatos.PARAM_DOCUMENTO %>" value="<%= n_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="nombre">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="<%= ControladorMisDatos.PARAM_NOMBRE %>" value="<%= nombre %>"/>
    		</div>
		</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="primer_apellido">Primer apellido: </label>
    			<input class="form-input-custom" id="primer_apellido" type="text" name="<%= ControladorMisDatos.PARAM_PRIMER_APELLIDO %>" value="<%= primer_apellido %>"/>
    		</div>
    		<div class="form-group">
    			<label for="segundo_apellido">Segundo apellido: </label>
    			<input class="form-input-custom" id="segundo_apellido" type="text" name="<%= ControladorMisDatos.PARAM_SEGUNDO_APELLIDO %>" value="<%= segundo_apellido %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="direccion">Direcci&oacute;n: </label>
    			<input class="form-input-custom" id="direccion" type="text" name="<%= ControladorMisDatos.PARAM_DIRECCION %>" value="<%= direccion %>"/>
    		</div>
    		<div class="form-group">
    			<label for="codigo_postal">Codigo Postal: </label>
    			<input class="form-input-custom" id="codigo_postal" type="text" name="<%= ControladorMisDatos.PARAM_CODIGO_POSTAL %>" value="<%= codigo_postal %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="localidad">Localidad: </label>
    			<input class="form-input-custom" id="localidad" type="text" name="<%= ControladorMisDatos.PARAM_LOCALIDAD %>" value="<%= localidad %>"/>
    		</div>
    		<div class="form-group">
    			<label for="provincia">Provincia: </label>
    			<input class="form-input-custom" id="provincia" type="text" name="<%= ControladorMisDatos.PARAM_PROVINCIA %>" value="<%= provincia %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    		<div class="form-group">
    			<label for="telefono">Tel&eacute;fono: </label>
    			<input class="form-input-custom" id="telefono" type="text" name="<%= ControladorMisDatos.PARAM_TELEFONO %>" value="<%= telefono %>"/>
    		</div>
    		    	    <div class="form-group">
    			<label for="nacionalidad">Nacionalidad: </label>
    			<input class="form-input-custom" id="nacionalidad" type="text" name="<%= ControladorMisDatos.PARAM_NACIONALIDAD %>" value="<%= nacionalidad %>"/>
    		</div>
    	</div>
    	
    	    	    <div class="form-group">
    			<label for="apellidos">Email: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorMisDatos.PARAM_EMAIL %>" value="<%= email %>"/>
    		</div>
    	
    	
    	<div class="form-group-container">
    		<div class="form-group-custom">
    			<label>Sexo: </label>
    			<br><br>
				<input class="form-input" type="radio" id="masculino" name="<%= ControladorMisDatos.PARAM_SEXO %>" style="display: inline;">
				<label class="form-label-custom" for="male" style="float: none; margin-right:0px">Masculino</label>
				<br>
    			<input class="form-input" type="radio" id="femenino" name="<%= ControladorMisDatos.PARAM_SEXO %>" style="display: inline;">
				<label class="form-label-custom" for="female" style="float: none; margin-right:0px;">Femenino</label>
    		</div>
    		<div class="form-check">
    			<label for="usuario_lista_dist">Lista Distribucion:</label>
    			<input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorMisDatos.PARAM_LISTA %>" value="<%= lista_dist %>" <%= (lista_dist ? "checked=''" : "") %>/>
    		</div>
    	</div>

    	<div class="form-group-container">
    		<div class="form-group-custom w100">

    		</div>
    		<div class="form-group-custom w100">
    		    <input id="baja_usuario" type="submit" name="<%= ControladorMisDatos.PARAM_DARSE_BAJA %> " 
    			value="Darse de baja de la bolsa" style="float:left;"/>
    			<input id="usuario_enviar" type="submit" name="<%= ControladorMisDatos.PARAM_ENVIAR %> " 
    			value="Enviar" style="float:right;"/>
    		</div>
    	</div>
    </form>
</div>

<script>
$(document).ready(function() {	
	
	<% String acum = bean.getUsuario().getSexo();
	if(bean.getUsuario().getSexo()!=null){
	if(bean.getUsuario().getSexo().equals("M")) { %>
	document.getElementById("masculino").checked = true;
	<%} else { 
		if(bean.getUsuario().getSexo().equals("F")) { %>
			document.getElementById("femenino").checked = true;
		<%}%>
	<%}}%>
	
});

function enviarUsuario(event, submit_input) {
	event.preventDefault();

	input_accion = document.getElementById("accion_formulario");
	input_accion.value = '<%= ControladorMisDatos.ACCION_ENVIAR_MISDATOS %>';
	input_id = document.getElementById("usuario_id");
	input_id.value = '<%= bean.getUsuario().getCodNum() %>';
	input_sexo_m = document.getElementById("masculino");
	input_sexo_f = document.getElementById("femenino");
	
	if(input_sexo_m.checked){
		input_sexo_m.value = "M";
		input_sexo_f.value = "M";
	}
	else{
		input_sexo_m.value = "F";
		input_sexo_f.value = "F";
	}

	submit_input.form.submit();
}

function bajaUsuario(event, submit_input) {
	event.preventDefault();

	input_accion = document.getElementById("accion_formulario");
	input_accion.value = '<%= ControladorMisDatos.ACCION_BAJA_USUARIO %>';
	input_id = document.getElementById("usuario_id");
	input_id.value = '<%= bean.getUsuario().getCodNum() %>';

	submit_input.form.submit();
}

document.getElementById("usuario_enviar").addEventListener("click", function(event) {
	enviarUsuario(event, this);
});

document.getElementById("baja_usuario").addEventListener("click", function(event) {
	bajaUsuario(event, this);
});


</script>
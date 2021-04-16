<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>


<div class="bolsa-empleo misdatos-form">
	<h2>Datos Personales</h2>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } 

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
	String movil = "";
	String telefono = "";
	String nacionalidad = "";
	String sexo = "";
		
	nombre = bean.getUsuarioArcos().getNombre();
	primer_apellido = bean.getUsuarioArcos().getApellido1();
	segundo_apellido = bean.getUsuarioArcos().getApellido2();
	email = bean.getUsuario().getEmail();
	tipo_documento = bean.getUsuarioArcos().getDocumentoTipo();
	n_documento = bean.getUsuarioArcos().getDocumentoNumero();
	direccion = bean.getUsuario().getDireccion();
	codigo_postal = bean.getUsuario().getCodigoPostal();
	localidad = bean.getUsuario().getLocalidad();
	provincia = bean.getUsuario().getProvincia();
	movil = bean.getUsuario().getMovil();
	telefono = bean.getUsuario().getTelefono();
	nacionalidad = bean.getUsuario().getNacionalidad();
	sexo = bean.getUsuario().getSexo();
	
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
    			<label for="direccion">Dirección: </label>
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
    			<label for="movil">Móvil: </label>
    			<input class="form-input-custom" id="movil" type="text" name="<%= ControladorMisDatos.PARAM_MOVIL %>" value="<%= movil %>"/>
    		</div>
    		<div class="form-group">
    			<label for="telefono">Teléfono: </label>
    			<input class="form-input-custom" id="telefono" type="text" name="<%= ControladorMisDatos.PARAM_TELEFONO %>" value="<%= telefono %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="nacionalidad">Nacionalidad: </label>
    			<input class="form-input-custom" id="nacionalidad" type="text" name="<%= ControladorMisDatos.PARAM_NACIONALIDAD %>" value="<%= nacionalidad %>"/>
    		</div>
    	    <div class="form-group">
    			<label for="apellidos">Email: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorMisDatos.PARAM_EMAIL %>" value="<%= email %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    		<div class="form-group-custom">
    			<label>Sexo: </label>
    			<br><br>
				<input class="form-input" type="radio" id="masculino" name="<%= ControladorMisDatos.PARAM_SEXO %>" style="display: inline;" <%if(sexo=="Masculino"){%>checked<%}%> >
				<label class="form-label-custom" for="male" style="float: none; margin-right:0px">Masculino</label>
				<br>
    			<input class="form-input" type="radio" id="femenino" name="<%= ControladorMisDatos.PARAM_SEXO %>" style="display: inline;" <%if(sexo=="Femenino"){%>checked<%}%> >
				<label class="form-label-custom" for="female" style="float: none; margin-right:0px;">Femenino</label>
    		</div>
    	</div>
		
    	<div class="form-group">
    		<input id="usuario_enviar" type="submit" name="<%= ControladorMisDatos.PARAM_ENVIAR %> " 
    		value="Enviar" style="float:right;"/>
    	</div>
    </form>
</div>

<script>


<% if(sexo=="Masculino") { %>
	document.getElementById("masculino").checked = true;
<%} else { 
	if(sexo=="Femenino") { %>
		document.getElementById("femenino").checked = true;
	<%}%>
<%}%>

function enviarUsuario(event, submit_input) {
	event.preventDefault();

	input_accion = document.getElementById("accion_formulario");
	input_accion.value = '<%= ControladorMisDatos.ACCION_ENVIAR_MISDATOS %>';
	input_id = document.getElementById("usuario_id");
	input_id.value = '<%= bean.getUsuario().getCodNum() %>';

	submit_input.form.submit();
}

document.getElementById("usuario_enviar").addEventListener("click", function(event) {
	enviarUsuario(event, this);
});


</script>
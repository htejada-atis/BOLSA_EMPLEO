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

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

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
	
	if(bean.getUsuarioLogeado().getNombre()!=null) nombre = bean.getUsuarioLogeado().getNombre();
	if(bean.getUsuarioLogeado().getPrimerApellido()!=null) primer_apellido = bean.getUsuarioLogeado().getPrimerApellido();
	if(bean.getUsuarioLogeado().getSegundoApellido()!=null) segundo_apellido = bean.getUsuarioLogeado().getSegundoApellido();
	if(bean.getUsuarioLogeado().getSegundoApellido()!=null) segundo_apellido = bean.getUsuarioLogeado().getSegundoApellido();
	if(bean.getUsuarioLogeado().getEmail()!=null) email = bean.getUsuarioLogeado().getEmail();
	if(bean.getUsuarioLogeado().getTipoDocumento()!=null) tipo_documento = bean.getUsuarioLogeado().getTipoDocumento();
	if(bean.getUsuarioLogeado().getPrsNif()!=null) n_documento = bean.getUsuarioLogeado().getPrsNif();
	if(bean.getUsuarioLogeado().getDireccion()!=null) direccion = bean.getUsuarioLogeado().getDireccion();
	if(bean.getUsuarioLogeado().getCodigoPostal()!=null) codigo_postal = bean.getUsuarioLogeado().getCodigoPostal();
	if(bean.getUsuarioLogeado().getLocalidad()!=null) localidad = bean.getUsuarioLogeado().getLocalidad();
	if(bean.getUsuarioLogeado().getProvincia()!=null) provincia = bean.getUsuarioLogeado().getProvincia();
	if(bean.getUsuarioLogeado().getTelefono()!=null) telefono = bean.getUsuarioLogeado().getTelefono();
	if(bean.getUsuarioLogeado().getNacionalidad()!=null) nacionalidad = bean.getUsuarioLogeado().getNacionalidad();
	if(bean.getUsuarioLogeado()!=null) lista_dist = bean.getUsuarioLogeado().getListaDist();
	
	%>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="actualizar_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMisDatos.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorMisDatos.ACCION_ENVIAR_MISDATOS %>" />
		<input type="hidden" name="<%= ControladorMisDatos.PARAM_ID%>" id="usuario_id" value="<%= bean.getUsuarioLogeado().getCodNum() %>" />
		
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="n_documento"><%= tipo_documento %>: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="<%= ControladorMisDatos.PARAM_DOCUMENTO %>" value="<%= n_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="nombre" class="bold-label">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="<%= ControladorMisDatos.PARAM_NOMBRE %>" value="<%= nombre %>" disabled/>
    		</div>
		</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="primer_apellido" class="bold-label">Primer apellido: </label>
    			<input class="form-input-custom" id="primer_apellido" type="text" name="<%= ControladorMisDatos.PARAM_PRIMER_APELLIDO %>" value="<%= primer_apellido %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="segundo_apellido" class="bold-label">Segundo apellido: </label>
    			<input class="form-input-custom" id="segundo_apellido" type="text" name="<%= ControladorMisDatos.PARAM_SEGUNDO_APELLIDO %>" value="<%= segundo_apellido %>" disabled/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="direccion" class="bold-label">Direcci&oacute;n: </label>
    			<input class="form-input-custom" id="direccion" type="text" name="<%= ControladorMisDatos.PARAM_DIRECCION %>" value="<%= direccion %>" required/>
    		</div>
    		<div class="form-group">
    			<label for="codigo_postal" class="bold-label">Codigo Postal: </label>
    			<input class="form-input-custom" id="codigo_postal" type="text" name="<%= ControladorMisDatos.PARAM_CODIGO_POSTAL %>" value="<%= codigo_postal %>" required/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="localidad" class="bold-label">Localidad: </label>
    			<input class="form-input-custom" id="localidad" type="text" name="<%= ControladorMisDatos.PARAM_LOCALIDAD %>" value="<%= localidad %>" required/>
    		</div>
    		<div class="form-group">
    			<label for="provincia" class="bold-label">Provincia: </label>
    			<input class="form-input-custom" id="provincia" type="text" name="<%= ControladorMisDatos.PARAM_PROVINCIA %>" value="<%= provincia %>" required/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    		<div class="form-group">
    			<label for="telefono" class="bold-label">Tel&eacute;fono: </label>
    			<input class="form-input-custom" pattern="[0-9]{1,11}" id="telefono"  type="text" name="<%= ControladorMisDatos.PARAM_TELEFONO %>" value="<%= telefono %>" required/>
    		</div>
    		    	    <div class="form-group">
    			<label for="nacionalidad">Nacionalidad: </label>
    			<input class="form-input-custom" id="nacionalidad" type="text" name="<%= ControladorMisDatos.PARAM_NACIONALIDAD %>" value="<%= nacionalidad %>"/>
    		</div>
    	</div>
    	
    	    	    <div class="form-group">
    			<label for="apellidos" class="bold-label">Email: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorMisDatos.PARAM_EMAIL %>" value="<%= email %>" disabled/>
    		</div>
    	
    	
    	<div class="form-group-container">
    		<div class="form-check">
    			<label for="usuario_lista_dist">Lista Distribucion:</label>
    			<input class="params" type="checkbox" id="usuario_lista_dist" 
    				   name="<%= ControladorMisDatos.PARAM_LISTA %>"
    				   value="true" <%= (lista_dist ? "checked=''" : "") %>/>
    		</div>
    	</div>

   		<div class="form-btn">
   		    <input id="baja_usuario" type="submit" name="<%= ControladorMisDatos.PARAM_DARSE_BAJA %>" value="Darse de baja de la bolsa" style="margin-right: 8px;"/>
   			<input id="usuario_enviar" type="submit" name="<%= ControladorMisDatos.PARAM_ENVIAR %>"  value="Guardar"/>
   		</div>
    </form>
</div>

<script>
	
	function bajaUsuario(event, submit_input) {
		event.preventDefault();
	
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorMisDatos.ACCION_BAJA_USUARIO %>';
		input_id = document.getElementById("usuario_id");
		input_id.value = '<%= bean.getUsuarioLogeado().getCodNum() %>';
	
		submit_input.form.submit();
	}
	
	document.getElementById("baja_usuario").addEventListener("click", function(event) {
		bajaUsuario(event, this);
	});

</script>

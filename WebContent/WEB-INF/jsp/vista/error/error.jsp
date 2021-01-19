<%

String errorCode = "x";
try {
	errorCode = (String)request.getSession().getAttribute("CodigoError");
	request.getSession().removeAttribute("CodigoError");
} catch (Exception e) {
	//
}
String errorDesc = "Error desconocido en la aplicaci&oacute;n";
if ("x".equals(errorCode)) {
	errorDesc = "Sin error definido";
} else if ("UJA0000".equals(errorCode)) { 
	// ConfiguracionGlobal... error en la configuración
	errorDesc = "Servicio actuamente fuera de servicio. Disculpe las molestias";
} else if ("UJA0001".equals(errorCode)) {
	// Cualquiera ... error al acceder a las bases de datos
	errorDesc = "Error en el acceso a la base de datos";
} else if ("UJA0002".equals(errorCode)) {    
	// General .. faltan parámetros
	errorDesc = "Faltan par&aacute;metros";
} else if ("UJA0101".equals(errorCode)) {
	// Filtro validaIdioma
	errorDesc = "Idioma no soportado";
} else if ("UJA0102".equals(errorCode)) {
	// Filtro validaAcceso
	errorDesc = "Servicio indicado no existe";
} else if ("UJA0103".equals(errorCode)) {
	// Filtro validaAcceso
	errorDesc = "Sin privilegios para la acci&oacute;n";
} else if ("UJA0104".equals(errorCode)) {
	// Filtro validaAcceso
	errorDesc = "El servicio solicitado est&aacute; temporalmente deshabilitado";
} else if ("UJA0105".equals(errorCode)) {
	// Filtro validaAcceso
	errorDesc = "Acceso al servicio desde la locaci&oacute;n actual desactivado (intranet)";
} else if ("UJA0201".equals(errorCode)) {
	// Controlador ..administracion.menu.editar
	errorDesc = "No se ha indicado como par&aacute;metro el nombre del men&uacute; a editar";	
} else if ("UJA0202".equals(errorCode)) {
	// Controlador ..administracion.menu.editar
	errorDesc = "El nombre del men&uacute; a editar no existe";	
}

%>
<h2>Error en la aplicaci&oacute;n</h2>
<div id="itemsmainContent">
	<%= errorDesc %>
</div>


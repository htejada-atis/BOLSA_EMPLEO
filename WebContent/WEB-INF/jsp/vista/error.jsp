
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.UVException" %>

<%
	UVException ex = (UVException) request.getAttribute(UVException.class.getName());
%>



<div id="itemsmainContent">
	<div id="divPrincipal" style="font-size: 1.4em; margin: 4px;">
		<%= EscapaHTML.escapa(ex.getMensajeUsuario(false)) %>
	</div>
	
	<div id="divBoton" style="font-size: 1.4em; margin: 4px;">
		<input type="button" id="botonMostrarDetalleError" value="Mostrar informaci&oacute;n adicional" onclick="gestionaDivDetalleError()" style="text-align: center; margin: 10px;" />
	</div>
	
	<div id="divDetalleError" style="display:none; font-size:1.1em; padding:10px; background-color:Bisque; " >
		<%= EscapaHTML.escapa(ex.getCodigoIncidencia()) %>
		<%= EscapaHTML.escapa(ex.getDescripcionExcepcion()) %>
	</div>
</div>
<script type="text/javascript">
	function gestionaDivDetalleError() {
		$('#divBoton').hide();
		$('#divDetalleError').slideDown();
	}
</script>
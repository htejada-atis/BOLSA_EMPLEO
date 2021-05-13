<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionNoticias"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMiembrosComision"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMiembrosComision bean = (VistaMiembrosComision) uvdatos.getVistas().get(VistaMiembrosComision.class.getName());
%>


<div class='bolsa-empleo'>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else {%>
	<h2>Miembros de la comisión</h2>	
	
	<div class="titulo-bolsa-empleo">
		<div class="form-select">
			<label>Bolsas</label>
			<select id="select_area">
				<option value="0">Elija la bolsa</option>
				<%
				for(Area area: bean.getAreas()) {
				%>
	    			<option value="<%=area.getCodNum()%>"><%=area.getDescripcion()%></option>
	    		<%
	    		}
	    		%>
			</select>
		</div>
	</div>
	<% } %>
</div>
	

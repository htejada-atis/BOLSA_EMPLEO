<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorValidar"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaValidar" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaValidar bean = (VistaValidar)uvdatos.getVistas().get(VistaValidar.class.getName());
%>

<div class='bolsa-empleo'>
	<% 
		String descripcion = "";
		if(bean.getConvocatoria()!=null){
			descripcion = "Última convocatoria abierta:" + bean.getConvocatoria().getDescripcion();
		} else {
			descripcion = "No existen convocatorias abiertas en este momento";
		}
	
	if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else {%>
	
	<div class="titulo-bolsa-empleo">
		<h2>Validar meritos sujetos afinidad</h2>
		<h3><%= descripcion %></h3>
	</div>
	
	<div class="form-group-container">
		<div class="form-select">
			<label>Área</label>
			<select id="select_area" autocomplete="off">
				<option value="0">Elija el área</option>
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
	

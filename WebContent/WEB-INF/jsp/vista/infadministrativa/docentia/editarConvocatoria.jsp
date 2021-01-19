<%@page import="es.ujaen.uvirtual.controlador.infadministrativa.docentia.ControladorConvocatoriaCRUD"%>
<%@page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@page import="es.ujaen.uvirtual.utilidades.EscapaHTML"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.docentia.VistaConvocatoriaCRUD" %>
<%@ page import="es.ujaen.uvirtual.modelo.ModeloDocentia.EstadoConvocatoria"  %>
<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD)uvdatos.getVistas().get(VistaConvocatoriaCRUD.class.getName());
%>

<script>
	function actualizarConvocatoria(campoDeFormulario){
		v = document.getElementById("accionFormulario");
		v.value = '<%= ControladorConvocatoriaCRUD.ACCION_CAMBIAR_CONVOCATORIA %>';
		v1 = document.getElementById("idConvocatoria");
		v1.value='<%=bean.getConvocatoria().getIdConvocatoria() %>';
		
		v2= document.getElementById("nombreConvocatoria");
		v2.value=document.getElementById("nombreCampoFormulario").value;
		v3= document.getElementById("estadoConvocatoria");
		v3.value=document.getElementById("estadoCampoFormulario").value;
		v4= document.getElementById("fechaConvocatoria");
		v4.value=document.getElementById("fechaCampoFormulario").value;
		v4= document.getElementById("fechaComision");
		v4.value=document.getElementById("fechaComisionCampoFormulario").value;
		v5= document.getElementById("observacionesConvocatoria");
		v5.value=document.getElementById("observacionesCampoFormulario").value;
		bloquearItemsMainContent();
		campoDeFormulario.form.submit();
		
	}

	$(document).ready(function(){
		  $("#fechaCampoFormulario").datepicker();
		  $("#fechaComisionCampoFormulario").datepicker();
	});
</script>

	<% if(bean.getMensajesDeError().size()>0){
		%><div class="error"><%= bean.formatearMensajesDeError() %></div>
	<% } %>

<div class="editarConvocatoria">
	<div>
		<form method="post" action="<%= request.getRequestURI() %>"> 
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ACCION %>" id="accionFormulario" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ID%>" id="idConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_NOMBRE_CONVOCATORIA%>" id="nombreConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ESTADO%>" id="estadoConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_FECHA_LIMITE%>" id="fechaConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_FECHA_COMISION%>" id="fechaComision" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_OBSERVACIONES%>" id="observacionesConvocatoria" value="" />
			<table class="bluetable">
				<caption>editar convocatoria</caption>
				<tr>
					<th scope="col" style="width:5%" title="Id de la convocatoria">Id conv.</th>
					<th scope="col" style="width:25%">Nombre convocatoria</th>
					<th scope="col" style="width:10%">Estado</th>
					<th scope="col" style="width:20%">Fecha l&iacute;mite</th>
					<th scope="col" style="width:20%">Fecha Comisi&oacute;n</th>
					<th scope="col" style="width:40%">Observaciones</th>
				</tr>
				<tr>
					<td><%=bean.getConvocatoria().getIdConvocatoria() %></td>
					<td><input type="text" id="nombreCampoFormulario" value="<%=EscapaHTML.escapaHTML(bean.getConvocatoria().getNombreConvocatoria())%>"/></td>
					<td>
						<select name="estadoCampoFormulario" id="estadoCampoFormulario">
							<%for(EstadoConvocatoria estado:EstadoConvocatoria.values()){
								if(bean.getConvocatoria().getEstado().equals(estado.toString())){%>
									<option value="<%=estado%>" selected="selected"><%=estado%></option>
								<%}
								else{%>
									<option value="<%=estado%>"><%=estado%></option>
								<%}
							
							}%>
						</select>
					</td>
					<td><input type="text" id="fechaCampoFormulario" value="<%=Formateador.formatoFecha(bean.getConvocatoria().getFechaLimite(), "DDMMYYYY")%>"/></td>
					<td><input type="text" id="fechaComisionCampoFormulario" value="<%=Formateador.formatoFecha(bean.getConvocatoria().getFechaComision(), "DDMMYYYY")%>"/></td>
					<td><input type="text" style="width:99%" id="observacionesCampoFormulario" value="<%=EscapaHTML.escapaHTML(bean.getConvocatoria().getObservaciones()) %>"/></td>
				</tr>
				<tr>
					<td colspan="5">
						<input type="button" value="Guardar cambios" width="50%"
							   id="botonGuardar"
							   onclick="actualizarConvocatoria(this)"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
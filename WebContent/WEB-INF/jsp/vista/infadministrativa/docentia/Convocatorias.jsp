<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.docentia.ControladorConvocatoriaCRUD"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.docentia.VistaConvocatoriaCRUD" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD)uvdatos.getVistas().get(VistaConvocatoriaCRUD.class.getName());
%>
<script>
	function anadirConvocatoria(campoDeFormulario,nombreAccion){
		v = document.getElementById("accionFormulario");
		v.value = nombreAccion;	
		v1=document.getElementById("nombreConvocatoria");
		v1.value=document.getElementById("nombreCampoFormulario").value;
		v2= document.getElementById("estadoConvocatoria");
		v2.value=document.getElementById("estadoCampoFormulario").value;
		v3= document.getElementById("fechaConvocatoria");
		v3.value=document.getElementById("fechaCampoFormulario").value;
		v4= document.getElementById("observacionesConvocatoria");
		v4.value=document.getElementById("observacionesCampoFormulario").value;
		v5= document.getElementById("fechaComision");
		v5.value=document.getElementById("fechaComisionCampoFormulario").value;
		bloquearItemsMainContent();
		campoDeFormulario.form.submit();
	}
	
	function eliminarConvocatoria(campoDeFormulario,nombreAccion,id){
		v=document.getElementById("accionFormulario");
		v.value=nombreAccion;
		v1=document.getElementById("idConvocatoria");
		v1.value=id;
		bloquearItemsMainContent();
		campoDeFormulario.form.submit();
	}
	
	function editarConvocatoria(campoDeFormulario,nombreAccion,id){
		v=document.getElementById("accionFormulario");
		v.value=nombreAccion;
		v1=document.getElementById("idConvocatoria");
		v1.value=id;
		bloquearItemsMainContent();
		campoDeFormulario.form.submit();
	}

	$(document).ready(function(){
		$("#fechaCampoFormulario").datepicker();
		$("#fechaComisionCampoFormulario").datepicker();
	});

</script>
<div class='convocatorias'>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else { %>
	<h2>Convocatorias</h2>
	<div>
		<form method="post" action="<%= request.getRequestURI() %>" id="formulario"> 
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ACCION %>" id="accionFormulario" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ID%>" id="idConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_NOMBRE_CONVOCATORIA%>" id="nombreConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_ESTADO%>" id="estadoConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_FECHA_LIMITE%>" id="fechaConvocatoria" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_FECHA_COMISION%>" id="fechaComision" value="" />
			<input type="hidden" name="<%= ControladorConvocatoriaCRUD.PARAM_OBSERVACIONES%>" id="observacionesConvocatoria" value="" />
		
			<table class="bluetable">
				<caption>convocatorias</caption>
				<tr>
					<th scope="col" style="width:5%" title="Id de la convocatoria">Id conv.</th>
					<th scope="col" style="width:25%">Nombre de la convocatoria</th>
					<th scope="col" style="width:10%">Estado</th>
					<th scope="col" style="width:15%">Fecha l&iacute;mite</th>
					<th scope="col" style="width:15%">Fecha Comisi&oacute;n</th>
					<th scope="col" style="width:30%">Observaciones</th>
					<th scope="col" style="width:15%">Acciones</th>
				</tr>
				
				<% for(Convocatoria convocatoria:bean.getConvocatorias()){ %>
					<tr id="filaConvocatoria<%=convocatoria.getIdConvocatoria()%>">
						<td><%=convocatoria.getIdConvocatoria()%></td>
						<td><%=EscapaHTML.escapaHTML(convocatoria.getNombreConvocatoria())%></td>
						<td><%=convocatoria.getEstado()%></td>
						<td><%=Formateador.formatoFecha(convocatoria.getFechaLimite(), Formateador.FORMATO_FECHA_DDMMYYYY)%></td>
						<td><%=Formateador.formatoFecha(convocatoria.getFechaComision(), Formateador.FORMATO_FECHA_DDMMYYYY)%></td>
						<td><%=EscapaHTML.escapaHTML(convocatoria.getObservaciones())%></td>
						<td><input type="button" value="Eliminar" 
								   id="botonEliminar<%=convocatoria.getIdConvocatoria() %>" class="boton botonEliminar"
								   onclick="eliminarConvocatoria(this,'<%= ControladorConvocatoriaCRUD.ACCION_ELIMINAR_CONVOCATORIA %>','<%=convocatoria.getIdConvocatoria()%>')" 
								   style="width:100%"/>
							<input type="button" value="Editar"
								   id="botonEditar<%=convocatoria.getIdConvocatoria() %>" class="boton botonEditar"
								   onclick="editarConvocatoria(this,'<%= ControladorConvocatoriaCRUD.ACCION_EDITAR_CONVOCATORIA %>','<%=convocatoria.getIdConvocatoria()%>')" 
								   style="width:100%"/>
							<input type="button" value="Enviar correo" onclick="enviarCorreo('<%=convocatoria.getIdConvocatoria()%>')" style="width:100%"/>
						</td>
					</tr>
				<% } %>
			</table>
			<br></br>
			<table class="bluetable">
				<caption>nueva convocatoria</caption>
				<tr>
					<th scope="col" style="width:20%">Nombre de la convocatoria</th>
					<th scope="col" style="width:20%">Estado</th>
					<th scope="col" style="width:20%">Fecha l&iacute;mite</th>
					<th scope="col" style="width:20%">Fecha Comisi&oacute;n</th>
					<th scope="col" style="width:20%">Observaciones</th>
				</tr>
				<tr>
					<td><input type="text" id="nombreCampoFormulario" required/></td>
					<td>
						<select name="estadoCampoFormulario" id="estadoCampoFormulario">
							<option value="CREADA">CREADA</option>
							<option value="ABIERTA">ABIERTA</option>
							<option value="CERRADA">CERRADA</option>
						</select>
					</td>
					<td><input type="text" id="fechaCampoFormulario"/></td>
					<td><input type="text" id="fechaComisionCampoFormulario"/></td>
					<td><input type="text" id="observacionesCampoFormulario"/></td>
				</tr>
				<tr>
					<td>
						<input type="button" value="Insertar convocatoria" width="20%" id="botonInsertaConvocatoria"
							   onclick="anadirConvocatoria(this, '<%= ControladorConvocatoriaCRUD.ACCION_AGREGAR_CONVOCATORIA %>')"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<%} %>
</div>
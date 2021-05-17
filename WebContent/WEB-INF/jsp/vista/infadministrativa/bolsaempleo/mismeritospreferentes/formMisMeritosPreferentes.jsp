<%@ page trimDirectiveWhitespaces="true"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentesCandidato"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentesCandidato bean = (VistaMeritosPreferentesCandidato) uvdatos.getVistas().get(VistaMeritosPreferentesCandidato.class.getName());
%>

<div class="bolsa-empleo">
<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Nuevo mérito preferente</h2>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
    
    <form id="agregar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" id="accion_formulario"
    		   name="<%= ControladorMisMeritosPreferentes.PARAM_ACCION %>"  
    		   value="<%= ControladorMisMeritosPreferentes.ACCION_AGREGAR_MERITO_CONFIRM %>" />
    		   
    	<div class="form-group-container col1">
	    	<div class="form-group">
				<label class="bold-label" for="select_apartado">Mérito preferente:</label>
				<%
					String meritoSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritosPreferentes.PARAM_MERITO_PREFERENTE, "");
				%>
				<select class="form-input-custom" id="select_apartado" name="<%= ControladorMisMeritosPreferentes.PARAM_MERITO_PREFERENTE %>" required>
					<option value="">Elija el tipo de mérito preferente</option>
					<% for (MeritoPreferente m: bean.getMeritosPreferente()) { %>
							<option value="<%= m.getCodNum() %>" observaciones="<%= m.getObservaciones() %>" <%= meritoSelected.equals(m.getCodNum().toString()) ? "selected=\"selected\"" : "" %>><%= bean.getCodigoPadreMeritoPreferente() + "." + m.getCodigo() + " - " + m.getNombre() %></option>				
					<% } %>
				</select>
			</div>		
		</div>
		
		<div class="form-group-container col1" id="observaciones" style="display:none;">
	    	<div class="form-group">
				<p id="observacionesParrafo"></p>
			</div>		
		</div>
		
	
    	<div class="form-file">
			<label for="merito_archivo" class="bold-label">Fichero:</label>
			<input id="merito_archivo" type="file" name="<%= ControladorMisMeritosPreferentes.PARAM_ARCHIVO %>" required/>
		</div>
		
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="merito_observacion">Observación para la comisión:</label>
	    		<textarea class="form-input-custom" id="merito_observacion" name="<%= ControladorMisMeritosPreferentes.PARAM_OBSERVACION %>" rows="2" cols="50"><%= BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritosPreferentes.PARAM_OBSERVACION, "") %></textarea>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="merito_enviar" type="submit" name="<%= ControladorMisMeritosPreferentes.PARAM_ENVIAR %>" value="Agregar mérito"/>
    	</div>
    </form>
    
</div>

<script>
	$(document).ready(function() {
		$('#agregar_merito').submit(function(event) { 
			$('#merito_enviar').prop('disabled', true);
			$('#merito_enviar').attr('value', 'Guardando mérito...');			
			return true;
		});
		
		$('#select_apartado').change(function(event) { 
			if($(this).children("option:selected").attr("value") != "") {
				$("#observaciones").show();
				$("#observacionesParrafo").html("Observaciones del mérito: " + $(this).children("option:selected").attr('observaciones'));
			} 
			else {
				$("#observaciones").hide();
			}
		});
	});
</script>
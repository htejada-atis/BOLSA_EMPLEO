<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

ApartadoBaremacion apartado = bean.getApartadoBaremacion();
BloqueBaremacion bloque = bean.getBloqueBaremacion();
String codigoCompleto = apartado.getCodigo() + "." + (bloque != null ? bloque.getCodigo() : bean.getUltimoCodigo());
%>

<div class="bolsa-empleo">
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
    
    <h2><%= bloque != null ? "Editar apartado" : "Nuevo apartado" %></h2>
    
    <form id="actualizar_bloque" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario"
    		   value="<%= bloque != null ? ControladorItemsBaremacion.ACCION_EDITAR_BLOQUE_CONFIRM : ControladorItemsBaremacion.ACCION_AGREGAR_BLOQUE_CONFIRM %>" /> 
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_APARTADO %>" id="apartado_id" value="<%= apartado.getCodNum() %>" />			  
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE %>" id="bloque_id" value="<%= bloque != null ? bloque.getCodNum() : "" %>" />
		
		<div class="form-group-container col1">
			<div class="form-group">
	    		<label for="bloque_apartado_codigo">Código completo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_APARTADO_CODIGO %>" id="bloque_apartado_codigo" value="<%= codigoCompleto %>" disabled/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_codigo">Código</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO %>" id="bloque_codigo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO, bloque != null ? bloque.getCodigo() : bean.getUltimoCodigo()) %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_nombre">Nombre</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE %>" id="bloque_nombre" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_BLOQUE_NOMBRE, bloque != null ? bloque.getNombre() : "") %>"/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_num_maximo_meritos">Número máximo de meritos por apartado</label>
	    		<input class="form-input-custom" type="number" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_NUMEROMAXIMOMERITOS %>" id="bloque_num_maximo_meritos" 
	    			value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_BLOQUE_NUMEROMAXIMOMERITOS, bloque != null && bloque.getNumeroMaximoMeritos() != null ? bloque.getNumeroMaximoMeritos().toString() : "") %>"/>
	    	</div>
	    </div>
				    	
    	<div class="form-btn">
    		<input id="bloque_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" 
    			   value="<%= bloque != null ? "Guardar cambios" : "Insertar apartado" %>"/>
    	</div>
    </form>    
</div>

<script>
	$(document).ready(function() {
		document.getElementById("bloque_codigo").addEventListener("input", function(event) {
			document.getElementById("bloque_apartado_codigo").value = '<%= apartado.getCodigo() %>.' + this.value;
		});		
	});
</script>

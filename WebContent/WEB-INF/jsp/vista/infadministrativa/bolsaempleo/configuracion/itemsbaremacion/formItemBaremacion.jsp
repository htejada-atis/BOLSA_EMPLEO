<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

ApartadoBaremacion apartado = bean.getApartadoBaremacion();
BloqueBaremacion bloque = bean.getBloqueBaremacion();
ItemBaremacion item = bean.getItemBaremacion();
String codigoCompleto = apartado.getCodigo() + "." + bloque.getCodigo() + "." + (item != null ? item.getCodigo() : bean.getUltimoCodigo());
String descripcion = "";
if(item!=null){
	descripcion = item.getDescripcion();
	if (descripcion==null) {
		descripcion = "";
	}
}
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
	
	<h2><%= item != null ? "Editar item" : "Nuevo item" %></h2>
	
    <form id="actualizar_item" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ACCION %>" id="accion_formulario"
    		   value="<%= item != null ? ControladorItemsBaremacion.ACCION_EDITAR_ITEM_CONFIRM : ControladorItemsBaremacion.ACCION_AGREGAR_ITEM_CONFIRM %>" /> 
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_APARTADO %>" id="apartado_id" value="<%= apartado.getCodNum() %>" />			  
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE %>" id="bloque_id" value="<%= bloque.getCodNum() %>" />
		<input type="hidden" name="<%= ControladorItemsBaremacion.PARAM_ITEM %>" id="item_id" value="<%= item != null ? item.getCodNum() : "" %>" />
		
		<div class="form-group-container col1">
			<div class="form-group">
	    		<label for="bloque_apartado_codigo">Código completo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO %>" id="item_bloque_codigo" value="<%= codigoCompleto %>" disabled/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_codigo">Código</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_CODIGO %>" id="bloque_codigo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_CODIGO, item != null ? item.getCodigo() : bean.getUltimoCodigo()) %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_nombre">Nombre</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_NOMBRE %>" id="bloque_nombre" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_NOMBRE, item != null ? item.getNombre() : "") %>"/>
	    	</div>
		</div>
		
		<div class="form-group-container col1">
			<div class="form-group">
    			<label for="descripcion">Descripción</label>
    			<textarea class="params form-input-custom" id="descripcion" name="<%= ControladorItemsBaremacion.PARAM_ITEM_DESCRIPCION %>" rows="3" cols="60"><%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_DESCRIPCION, item != null ? descripcion : "") %></textarea>
   			</div>
		</div>
   		
   		
		<div class="form-group-container col2">
			<div class="form-group">
				<%
					String unidadesSelected = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_UNIDADES, item != null ? item.getUnidades() : ModeloBaremacion.ITEM_UNIDADES_MEDICION_ENTERO);
				%>
	    		<label for="item_unidades">Unidades</label>
	    		<select class="params" id="item_unidades" name="<%= ControladorItemsBaremacion.PARAM_ITEM_UNIDADES %>" style="width:100%;">
					<option value="<%= ModeloBaremacion.ITEM_UNIDADES_MEDICION_ENTERO %>" <%= unidadesSelected.equals(ModeloBaremacion.ITEM_UNIDADES_MEDICION_ENTERO) ? "selected=\"selected\"" : "" %>>Entero</option>
					<option value="<%= ModeloBaremacion.ITEM_UNIDADES_MEDICION_DECIMAL %>" <%= unidadesSelected.equals(ModeloBaremacion.ITEM_UNIDADES_MEDICION_DECIMAL) ? "selected=\"selected\"" : "" %>>Decimal</option>
					<option value="<%= ModeloBaremacion.ITEM_UNIDADES_MEDICION_SINO %>" <%= unidadesSelected.equals(ModeloBaremacion.ITEM_UNIDADES_MEDICION_SINO) ? "selected=\"selected\"" : "" %>>Si ó No</option>				
				</select>				
	    	</div>
	    	<div class="form-group">
	    		<label for="item_valor">Valor unitario</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR %>" id="item_valor" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR, item != null ? item.getValor().toString() : "") %>"/>
	    	</div>	  	    	
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_valor_minimo">Valor mínimo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR_MINIMO %>" id="bloque_valor_minimo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR_MINIMO, item != null ? item.getValorMinimo().toString() : "0.1") %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_valor_maximo">Valor máximo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR_MAXIMO %>" id="bloque_valor_maximo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR_MAXIMO, item != null ? item.getValorMaximo().toString() : "1000") %>"/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<%
					String afinidadSelected = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_AFINIDAD, item != null && item.getAfinidad() != null ? item.getAfinidad() : "N");
				%>
	    		<label for="item_afinida">Afinidad</label>	    		
	    		<select class="params" id="item_afinida" name="<%= ControladorItemsBaremacion.PARAM_ITEM_AFINIDAD %>" style="width:100%;">
					<option value="N" <%= afinidadSelected.equals("N") ? "selected=\"selected\"" : "" %>>N</option>
					<% for (String afinidad: bean.getAfinidades()) { %>
						<option value="<%=afinidad%>" <%= afinidadSelected.equals(afinidad) ? "selected=\"selected\"" : "" %>><%=afinidad%></option>					
					<% } %>
				</select>
	    	</div>
	    	<div class="form-check" id="individualizadoBloque" style="<%= afinidadSelected.equals("N") ? "display:none;" : "" %> ">
	    		<%
					String individualizado = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_INDIVIDUALIZADO, item != null ? item.getIndividualizado() ? "S" : "N" : "S");
				%>
    			<label for="individualizado">
    			<input class="params" type="checkbox" id="individualizado" name="<%= ControladorItemsBaremacion.PARAM_ITEM_INDIVIDUALIZADO %>" 
    				value="S" <%= individualizado.equals("S") ? "checked=''" : "" %>/>Individualizado</label>
    		</div>
		</div>
		<div class="form-btn">
    		<input id="item_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" 
    			   value="<%= item != null ? "Guardar cambios" : "Insertar item" %>"/>
    	</div>    	
    </form>
</div>

<script>
	$(document).ready(function() {
		document.getElementById("bloque_codigo").addEventListener("input", function(event) {
			document.getElementById("item_bloque_codigo").value = '<%= apartado.getCodigo() %>.<%= bloque.getCodigo() %>.' + this.value;
		});
		$('#item_afinida').on('change', function() {
			console.log("items", $(this).val());
			
			if ($(this).val() == 'N') {
				$('#individualizadoBloque').hide();
			} else {
				$('#individualizadoBloque').show();
			}
		});
	});
</script>

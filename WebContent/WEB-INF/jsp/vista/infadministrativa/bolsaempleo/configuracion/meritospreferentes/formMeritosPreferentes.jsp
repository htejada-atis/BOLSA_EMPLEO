<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.beans.Rol" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentes bean = (VistaMeritosPreferentes) uvdatos.getVistas().get(VistaMeritosPreferentes.class.getName());
MeritoPreferente merito = bean.getMeritoPreferente();
%>

<div class="bolsa-empleo convocatoria-form">
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
	
	<h2><%= merito != null ? "Editar mérito preferente" : "Nuevo mérito preferente" %></h2>
		
	<form id="merito_preferente_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMeritosPreferentes.PARAM_ACCION %>" id="accion_formulario" 
    		   value="<%= merito != null ? ControladorMeritosPreferentes.ACCION_MODIFICAR_MERITO_CONFIRM : ControladorMeritosPreferentes.ACCION_NUEVO_MERITO_CONFIRM %>" />
		<input type="hidden" name="<%= ControladorMeritosPreferentes.PARAM_ID %>" id="merito_preferente_id"
		       value="<%= merito != null ? merito.getCodNum() : "" %>" />		
		       
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="descripcion">Descripción tipo mérito: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_DESCRIPCION %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_DESCRIPCION, merito != null ? merito.getDescripcion() : "") %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<%
    			String tipoSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_TIPO, merito != null ? merito.getTipo() : ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE);
    			%>
	    		<label for="merito_tipo">Tipo: </label>
	    		<select class="params" id="merito_tipo" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_TIPO%>" style="width:100%;">
					<option value="<%=ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE) ? "selected=\"selected\"" : ""%>>Por titulación preferente</option>
					<option value="<%=ModeloMeritosPreferentes.TIPO_MERITO%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_MERITO) ? "selected=\"selected\"" : ""%>>Por mérito</option>					
				</select>	
    		</div>
    		    		
    		<div class="form-group" id="bloque_tipo_merito" style="<%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_MERITO) ? "" : "display:none"%>">
    			<%
    			String tipoItemBaremacionSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_TIPO_ITEMBAREMACION, 
    			    					merito != null && merito.getTipoItemBaremacion() != null ? merito.getTipoItemBaremacion().getCodNum().toString() : "");
    			%>
	    		<label for="merito_tipo_itembaremacion">Mérito: </label>
	    		<select class="params" id="merito_tipo_itembaremacion" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_TIPO_ITEMBAREMACION%>" style="width:100%;">
	    			<option value="">--</option>
					<%
					for(ItemBaremacion item : bean.getItemsBaremacion()) {
					%>
	    					<option value="<%=item.getCodNum()%>" <%=tipoItemBaremacionSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%=item.getNombre()%></option>
	    					<%
	    					}
	    					%>
				</select>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2">
	   		<div class="form-group">
    			<div class="form-group">
	    			<%
	    			String aplicableSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE, 
	    				    					merito != null ? merito.getAplicable() : ModeloMeritosPreferentes.APLICABLE_APARTADO);
	    			%>
		    		<label for="merito_aplicable">Aplicable:</label>
		    		<select class="params" id="merito_aplicable" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE%>" style="width:100%;">
						<option value="<%=ModeloMeritosPreferentes.APLICABLE_APARTADO%>" <%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_APARTADO) ? "selected=\"selected\"" : ""%>>Bloque</option>
						<option value="<%=ModeloMeritosPreferentes.APLICABLE_BLOQUE%>" <%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_BLOQUE) ? "selected=\"selected\"" : ""%>>Apartado</option>										
						<option value="<%=ModeloMeritosPreferentes.APLICABLE_ITEM%>" <%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_ITEM) ? "selected=\"selected\"" : ""%>>Mérito</option>
						<option value="<%=ModeloMeritosPreferentes.APLICABLE_TOTAL%>" <%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_TOTAL) ? "selected=\"selected\"" : ""%>>Total</option>						
					</select>	
    			</div>
    		</div>
    		<div class="form-group" id="bloque_aplicable_bloque" style="<%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_BLOQUE) ? "" : "display:none"%>">
    			<%
    			String aplicableBloqueSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_BLOQUE, 
    			    					merito != null && merito.getAplicableBloqueBaremacion() != null ? merito.getAplicableBloqueBaremacion().getCodNum().toString() : "");
    			%>
	    		<label for="merito_aplicable_bloque">Apartado: </label>
	    		<select class="params" id="merito_aplicable_bloque" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_BLOQUE%>" style="width:100%;">
	    			<option value="">--</option>
					<%
					for(BloqueBaremacion item : bean.getBloqueBaremacion()) {
					%>
	    					<option value="<%=item.getCodNum()%>" <%=aplicableBloqueSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%=item.getNombre()%></option>
	    					<%
	    					}
	    					%>						
				</select>
    		</div>
    		<div class="form-group" id="bloque_aplicable_apartado" style="<%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_APARTADO) ? "" : "display:none"%>">
    			<%
    			String aplicableApartadoSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_APARTADO, 
    			    					merito != null && merito.getAplicableApartadoBaremacion() != null ? merito.getAplicableApartadoBaremacion().getCodNum().toString() : "");
    			%>
	    		<label for="merito_aplicable_apartado">Bloque: </label>
	    		<select class="params" id="merito_aplicable_apartado" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_APARTADO%>" style="width:100%;">
	    			<option value="">--</option>
					<%
					for(ApartadoBaremacion item : bean.getApartadoBaremacion()) {
					%>
	    					<option value="<%=item.getCodNum()%>" <%=aplicableApartadoSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%=item.getNombre()%></option>
	    					<%
	    					}
	    					%>						
				</select>
    		</div>
    		<div class="form-group" id="bloque_aplicable_item" style="<%=aplicableSelected.equals(ModeloMeritosPreferentes.APLICABLE_ITEM) ? "" : "display:none"%>">
    			<%
    			String aplicableItemSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_ITEM, 
    			    					merito != null && merito.getAplicableItemBaremacion() != null ? merito.getAplicableItemBaremacion().getCodNum().toString() : "");
    			%>
	    		<label for="merito_aplicable_apartado">Mérito: </label>
	    		<select class="params" id="merito_aplicable_apartado" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_APLICABLE_ITEM%>" style="width:100%;">
	    			<option value="">--</option>
					<%
					for(ItemBaremacion item : bean.getItemsBaremacion()) {
					%>
	    					<option value="<%=item.getCodNum()%>" <%=aplicableItemSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%=item.getNombre()%></option>
	    					<%
	    					}
	    					%>						
				</select>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2">
    		<div class="form-group">
    			<label for="factor">Factor: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR, merito != null ? merito.getFactor() : "")%>"/>
    			<div id="helpTextFactor">
    				<p>Valor por el cual se multiplica el total del bloque, apartado o item, seleccionado en el campo Aplicable o al total de la puntuación.</p>
    				<p>Puede contener una fórmula de cálculo utilizando la varible 'N' como valor objecto seleccionado en Aplicable.</p>
    			</div>
    		</div>
    		<div class="form-group">
    			<label for="factor">Valor máximo: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO, merito != null && merito.getValorMaximo() != null ? merito.getValorMaximo().toString() : "")%>"/>
    			<div id="helpTextValorMaximo">
    				<p>Valor máximo que puede alcanzar el factor de ponderación. Vacio si no hay máximo.</p>
    			</div>
    		</div>
    	</div>
    	
    	<div class="form-btn">
    		<input id="convocatoria_enviar" type="submit" value="<%=merito != null ? "Guardar" : "Nuevo mérito"%>" />
    	</div>
    </form>
</div>

<script>
	$(document).ready(function() {
		$('#merito_tipo').on('change', function() {
			var value = $(this).children("option:selected").attr('value');
			switch(value) {
			case '<%=ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE%>':
				$('#bloque_tipo_merito').hide();
				break;
			case '<%= ModeloMeritosPreferentes.TIPO_MERITO %>':				
				$('#bloque_tipo_merito').show();
				break;				
			}			
		});
		
		$('#merito_aplicable').on('change', function() {
			var value = $(this).children("option:selected").attr('value');
			switch(value) {
			case '<%=ModeloMeritosPreferentes.APLICABLE_BLOQUE%>':
				$('#bloque_aplicable_bloque').show();
				$('#bloque_aplicable_apartado').hide();
				$('#bloque_aplicable_item').hide();
				break;
			case '<%=ModeloMeritosPreferentes.APLICABLE_APARTADO%>':
				$('#bloque_aplicable_bloque').hide();
				$('#bloque_aplicable_apartado').show();
				$('#bloque_aplicable_item').hide();
				break;
			case '<%=ModeloMeritosPreferentes.APLICABLE_ITEM%>':
				$('#bloque_aplicable_bloque').hide();
				$('#bloque_aplicable_apartado').hide();
				$('#bloque_aplicable_item').show();
				break;
			case '<%=ModeloMeritosPreferentes.APLICABLE_TOTAL%>':
				$('#bloque_aplicable_bloque').hide();
				$('#bloque_aplicable_apartado').hide();
				$('#bloque_aplicable_item').hide();
				break;
			}			
		});
	});	
</script>
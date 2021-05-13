<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentes bean = (VistaMeritosPreferentes) uvdatos.getVistas().get(VistaMeritosPreferentes.class.getName());
MeritoPreferente merito = bean.getMeritoPreferente();
%>

<div class="bolsa-empleo meritos-preferentes-form">

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
		       
		<div class="form-group-container col2">
			<div class="form-group">
    			<label for="codigo">Código: </label>
    			<input id="codigo"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_CODIGO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_CODIGO, merito != null ? merito.getCodigo() : "") %>"/>
    		</div>
    		<div class="form-group">
    			<label for="descripcion">Descripción tipo mérito: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_DESCRIPCION %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_DESCRIPCION, merito != null ? merito.getDescripcion() : "") %>"/>
    		</div>
    	</div>
		
		<!-- DONDE SE APLICA -->

    	<div class="form-group-container col2 helper">
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
	    					<option value="<%=item.getCodNum()%>" <%=aplicableBloqueSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%= item.getApartadoBaremacion().getCodigo() + "." + item.getCodigo() + " - " + item.getNombre() %></option>
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
	    					<option value="<%=item.getCodNum()%>" <%=aplicableApartadoSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%= item.getCodigo() + " - " + item.getNombre()%></option>
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
	    					<option value="<%=item.getCodNum()%>" <%=aplicableItemSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%= item.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + item.getBloqueBaremacion().getCodigo() + "." + item.getCodigo() + " - " + item.getNombre()%></option>
	    					<%
	    					}
	    					%>						
				</select>
    		</div>
    	</div>
    	<div class="form-group-container col1">
    		<div class="form-group">
    			<p class="helper-group">Indica donde se aplica el resultado del cálculo del mérito preferente: a un bloque, apartado, mérito o al total de la puntuación</p>
    		</div>
	   	</div>
    	
    	<!-- TIPO DE MERITO -->
    	
    	<div class="form-group-container col2 helper">
	   		<div class="form-group">
    			<%
    			String tipoSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_TIPO, merito != null ? merito.getTipo() : ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE);
    			%>
	    		<label for="merito_tipo">Tipo de mérito preferente: </label>
	    		<select class="params" id="merito_tipo" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_TIPO%>" style="width:100%;">
					<option value="<%=ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE) ? "selected=\"selected\"" : ""%>>Por titulación preferente</option>
					<option value="<%=ModeloMeritosPreferentes.TIPO_MERITO%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_MERITO) ? "selected=\"selected\"" : ""%>>Por mérito</option>					
					<option value="<%=ModeloMeritosPreferentes.TIPO_POSESION%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_POSESION) ? "selected=\"selected\"" : ""%>>Por posesión</option>
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
	    					<option value="<%=item.getCodNum()%>" <%=tipoItemBaremacionSelected.equals(item.getCodNum().toString()) ? "selected=\"selected\"" : ""%>><%= item.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." + item.getBloqueBaremacion().getCodigo() + "." + item.getCodigo() + " - " + item.getNombre()%></option>
	    					<%
	    					}
	    					%>
				</select>
    		</div>    		
    	</div>
    	<div class="form-group-container col1">
    		<div class="form-group">
   				<p class="helper-group">Indica cuando se lanza el cálculo del mérito preferente.</p>
		    	<ul style="margin-bottom: 1rem;">
		    		<li>Por titulación preferente: si el candidato tiene una titulación preferente al área, se aplicará el cálculo donde se indique en el campo "Aplicable".</li>
		    		<li>Por mérito: si el candidato tiene el mérito validado, se aplicará el cálculo donde se indique en el campo "Aplicable".</li>
		    		<li>Por posesión: el candidato deberá apartar un pdf para justificar el mérito preferente.</li>
		    	</ul>
		    </div>
   		</div>
    	
	
		<!-- TIPO DE CALCULO -->
		    	
    	<div class="form-group-container col2 helper">
    		<div class="form-group">
    			<%    			
    			String tipoCalculoSelected = BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_TIPO_CALCULO, merito != null && merito.getTipoCalculo() != null ? merito.getTipoCalculo() : ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR);
    			%>
	    		<label for="merito_tipo_calculo">Tipo de cálculo: </label>
	    		<select class="params" id="merito_tipo_calculo" name="<%=ControladorMeritosPreferentes.PARAM_MERITO_TIPO_CALCULO%>" style="width:100%;">
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR) ? "selected=\"selected\"" : ""%>>Factor</option>
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR%>" <%=tipoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR) ? "selected=\"selected\"" : ""%>>Por valor del mérito</option>					
				</select>	
    		</div>
    	</div>
    	<div class="form-group-container col1">
    		<div class="form-group">
    			<p>Indica como calcular el valor multiplicador que se aplicará donde se indique en el campo "Aplicable".</p>
    			<ul>
    				<li>Factor: es valor multiplicador es un valor decimal.</li>
    				<li>Por valor del mérito: el valor multiplicador se cálcula con la formula, BASE + FACTOR * VALOR_MERITO</li>    				
    			</ul>
    		</div>
    	</div>
    	
    	<div class="form-group-container col2" id="bloque_calculo_factor" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR) ? "" : "display:none"%>">
    		<div class="form-group">
    			<label for="factor">Factor: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR, merito != null && merito.getFactor() != null ? merito.getFactor().toString() : "")%>"/>    			
    		</div>
    	</div>
    	
    	<div class="form-group-container col3" id="bloque_calculo_valor_merito" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR) ? "" : "display:none"%>">
    		<div class="form-group">
    			<label for="factor">Base: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_BASE%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_BASE, merito != null && merito.getBase() != null ? merito.getBase().toString() : "")%>"/>
    		</div>
    		<div class="form-group">
    			<label for="factor">Factor: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR, merito != null && merito.getFactor() != null ? merito.getFactor().toString() : "")%>"/>
    		</div>
    		<div class="form-group">
    			<label for="factor">Valor máximo: </label>
    			<input id="factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO, merito != null && merito.getValorMaximo() != null ? merito.getValorMaximo().toString() : "")%>"/>
    		</div>
    	</div>
    	
    	<div class="form-btn">
    		<input id="convocatoria_enviar" type="submit" value="<%=merito != null ? "Guardar" : "Nuevo mérito"%>" />
    	</div>
    </form>
</div>

<script>
	$(document).ready(function() {
		function onChangeTipoMerito(value) {
			switch(value) {
			case '<%=ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE%>':
				$('#bloque_tipo_merito').hide();
				break;
			case '<%= ModeloMeritosPreferentes.TIPO_MERITO %>':				
				$('#bloque_tipo_merito').show();
				break;				
			}
		}
		
		$('#merito_tipo').on('change', function () { 
			onChangeTipoMerito($(this).children("option:selected").attr('value')); 
		});
		
		$('#merito_tipo_calculo').on('change', function() {
			var value = $(this).children("option:selected").attr('value');
			console.log(value);
			
			switch(value) {
			case '<%=ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR%>':
				$('#bloque_calculo_factor').show();
				$('#bloque_calculo_factor_helper').show();
							
				$('#bloque_calculo_valor_merito').hide();
				$('#bloque_calculo_valor_merito_helper').hide();
				break;
			case '<%= ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR %>':				
				$('#bloque_calculo_factor').hide();
				$('#bloque_calculo_factor_helper').hide();
							
				$('#bloque_calculo_valor_merito').show();
				$('#bloque_calculo_valor_merito_helper').show();
				
				$('#merito_tipo').val('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
				onChangeTipoMerito('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
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
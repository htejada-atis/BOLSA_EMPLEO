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

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
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
    			<label for="descripcion">Nombre: </label>
    			<input id="descripcion"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_NOMBRE %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_NOMBRE, merito != null ? merito.getNombre() : "") %>"/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col1">
			<div class="form-group">
    			<label for="descripcion">Observaciones</label>
    			<textarea class="params form-input-custom" id="observaciones" 
    					  name="<%=ControladorMeritosPreferentes.PARAM_MERITO_OBSERVACIONES%>" rows="3" cols="60"><%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_OBSERVACIONES, merito != null && merito.getObservaciones() != null ? merito.getObservaciones() : "")%></textarea>
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
	   	
	   	<div class="form-group-container col2">
    		<div class="form-group">
    			<label for="merito_prefijo_informe">Prefijo informe:</label>
    			<input id="merito_prefijo_informe"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMeritosPreferentes.PARAM_MERITO_PREFIJO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_PREFIJO, merito != null ? EscapaHTML.escapa(merito.getPrefijoInforme()) : "") %>"/>
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
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR%>" <%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR) ? "selected=\"selected\"" : ""%>>Factor</option>
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR%>" <%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR) ? "selected=\"selected\"" : ""%>>Por valor del mérito</option>					
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE%>" <%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE) ? "selected=\"selected\"" : ""%>>Por valor del mérito mayor que</option>
					<option value="<%=ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES%>" <%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES) ? "selected=\"selected\"" : ""%>>Opciones</option>
				</select>
    		</div>
    	</div>
    	<div class="form-group-container col1">
    		<div class="form-group">
    			<p>Indica como calcular el valor multiplicador que se aplicará donde se indique en el campo "Aplicable".</p>
    			<ul>
    				<li id="tipo_calculo_helper_factor" class="<%= tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR) ? "selected" : "" %>">Factor: es valor multiplicador es un valor decimal.</li>
    				<li id="tipo_calculo_helper_valor_merito" class="<%= tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR) ? "selected" : "" %>">Por valor del mérito: el valor multiplicador se cálcula con la formula, BASE + FACTOR * VALOR_MERITO</li>
    				<li id="tipo_calculo_helper_valor_merito_mayor_que" class="<%= tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE) ? "selected" : "" %>">Por valor del mérito mayor que: el valor de mérito debe ser mayor o igual al valor indicado en el campo "Valor mínimo del mérito" para aplicar el factor</li>    				
    				<li id="tipo_calculo_helper_opciones" class="<%= tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES) ? "selected" : "" %>">Opciones: el valor multiplicador se selecciona de una lista</li>
    			</ul>
    		</div>
    	</div>
    	
    	<!-- TIPO DE CALCULO FACTOR -->
    	
    	<div class="form-group-container col2" id="bloque_calculo_factor" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR) ? "" : "display:none"%>">
    		<div class="form-group">
    			<label for="bloque_calculo_factor_factor">Factor: </label>
    			<input id="bloque_calculo_factor_factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_FACTOR%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_FACTOR, merito != null && merito.getFactor() != null ? merito.getFactor().toString() : "")%>"/>    			
    		</div>
    	</div>
    	
    	<!--  TIPO DE CALCULO VALOR DEL MERITO -->
    	
    	<div class="form-group-container col3" id="bloque_calculo_valor_merito" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR) ? "" : "display:none"%>">
    		<div class="form-group">
    			<label for="bloque_calculo_valor_merito_base">Base: </label>
    			<input id="bloque_calculo_valor_merito_base"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_BASE_POR_VALOR_MERITO%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_BASE_POR_VALOR_MERITO, merito != null && merito.getBase() != null ? merito.getBase().toString() : "")%>"/>
    		</div>
    		<div class="form-group">
    			<label for="bloque_calculo_valor_merito_factor">Factor: </label>
    			<input id="bloque_calculo_valor_merito_factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_POR_VALOR_MERITO%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_POR_VALOR_MERITO, merito != null && merito.getFactor() != null ? merito.getFactor().toString() : "")%>"/>
    		</div>
    		<div class="form-group">
    			<label for="bloque_calculo_valor_merito_valor_maximo">Valor máximo: </label>
    			<input id="bloque_calculo_valor_merito_valor_maximo"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_VALORMAXIMO, merito != null && merito.getValorMaximo() != null ? merito.getValorMaximo().toString() : "")%>"/>
    		</div>
    	</div>
    	
    	<!--  TIPO DE CALCULO VALOR DEL MERITO MAYOR QUE -->
    	
    	<div class="form-group-container col3" id="bloque_calculo_valor_merito_mayor_que" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE) ? "" : "display:none"%>">
    		<div class="form-group">
    			<label for="bloque_calculo_valor_merito_mayor_que_base">Valor mínimo mérito: </label>
    			<input id="bloque_calculo_valor_merito_mayor_que_base"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_BASE_POR_VALOR_MERITO_MAYOR_QUE%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_BASE_POR_VALOR_MERITO_MAYOR_QUE, merito != null && merito.getBase() != null ? merito.getBase().toString() : "")%>"/>
    		</div>
    		<div class="form-group">
    			<label for="bloque_calculo_valor_merito_mayor_que_factor">Factor: </label>
    			<input id="bloque_calculo_valor_merito_mayor_que_factor"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%=ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_POR_VALOR_MERITO_MAYOR_QUE%>" 
    				   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMeritosPreferentes.PARAM_MERITO_FACTOR_POR_VALOR_MERITO_MAYOR_QUE, merito != null && merito.getFactor() != null ? merito.getFactor().toString() : "")%>"/>
    		</div>
    	</div>
    	
    	<!-- TIPO DE CALCULO OPCIONES -->
    	<div class="form-group-container col1" id="bloque_calculo_opciones" style="<%=tipoCalculoSelected.equals(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES) ? "" : "display:none"%>">
	    	<% if (merito == null) { %>
	    		<p>Cree el mérito antes para poder añadir las opciones</p>
	    	<% } else { %>
	    		<table class="bluetable bolsaempleo" id="tableOpcionesMPR">
					<tr>
						<th scope="col"	style="width:70%" title="Descripción del mérito">Nombre</th>
						<th scope="col"	style="width:20%">Factor</th>
						<th scope="col"	style="width:10%"></th>
					</tr>
					<tbody>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="4" style="width:100%"></th>
						</tr>
					</tfoot>
				</table>
			<% } %>
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
			
			switch(value) {
			case '<%=ModeloMeritosPreferentes.TIPO_CALCULO_FACTOR%>':
				$('#bloque_calculo_factor').show();							
				$('#bloque_calculo_valor_merito').hide();
				$('#bloque_calculo_valor_merito_mayor_que').hide();	
				$('#bloque_calculo_opciones').hide();
				
				$('#tipo_calculo_helper_factor').addClass('selected');
				$('#tipo_calculo_helper_valor_merito').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito_mayor_que').removeClass('selected');
				$('#tipo_calculo_helper_opciones').removeClass('selected');
				break;
			case '<%= ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_FACTOR %>':				
				$('#bloque_calculo_factor').hide();
				$('#bloque_calculo_valor_merito').show();
				$('#bloque_calculo_valor_merito_mayor_que').hide();
				$('#bloque_calculo_opciones').hide();
				
				$('#tipo_calculo_helper_factor').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito').addClass('selected');
				$('#tipo_calculo_helper_valor_merito_mayor_que').removeClass('selected');
				$('#tipo_calculo_helper_opciones').removeClass('selected');
				
				$('#merito_tipo').val('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
				onChangeTipoMerito('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
				break;
			case '<%=ModeloMeritosPreferentes.TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE%>':
				$('#bloque_calculo_factor').hide();
				$('#bloque_calculo_valor_merito').hide();
				$('#bloque_calculo_valor_merito_mayor_que').show();
				$('#bloque_calculo_opciones').hide();
				
				$('#tipo_calculo_helper_factor').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito_mayor_que').addClass('selected');
				$('#tipo_calculo_helper_opciones').removeClass('selected');
				
				$('#merito_tipo').val('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
				onChangeTipoMerito('<%= ModeloMeritosPreferentes.TIPO_MERITO %>');
				break;
			case '<%=ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES%>':
				$('#bloque_calculo_factor').hide();
				$('#bloque_calculo_valor_merito').hide();
				$('#bloque_calculo_valor_merito_mayor_que').hide();
				$('#bloque_calculo_opciones').show();
				
				$('#tipo_calculo_helper_factor').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito').removeClass('selected');
				$('#tipo_calculo_helper_valor_merito_mayor_que').removeClass('selected');
				$('#tipo_calculo_helper_opciones').addClass('selected');
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
		
		<% if (merito != null) { %>
			var table = new Atis.DataTable('#tableOpcionesMPR', {
			    "ajax": { url: "<%=  ControladorMeritosPreferentes.URL_PATTERN_AJAX %>" },
			    "pageSize": 10,
			    "action": "<%= ControladorMeritosPreferentes.ACCION_DATATABLE_OPCIONES %>",
			    "params": {"<%= ControladorMeritosPreferentes.PARAM_ID %>": '<%= merito.getCodNum() %>'},
			    "filterable": true,
			    "defaultOrderBy": 1,
			    "defaultOrderDirection": 'asc',
			    "columns": [
			        {'data': 'nombre'},		       
			        {'data': 'factor'},
			        {'data': 'codNum', 'buttons': [
		        		{'label': 'Borrar', 'onClick': function(row) {
		        			Atis.confirmDialog("Borrar opción", "¿Está seguro de borrar la opción?", {
		            			Si: function() {
		            				var params = {
	    		        				'a': '<%= ControladorMeritosPreferentes.ACCION_DESACTIVAR_OPCION %>', 
	    		        				'<%= ControladorMeritosPreferentes.PARAM_ID %>': '<%= merito.getCodNum() %>',
	    		        				'<%= ControladorMeritosPreferentes.PARAM_ID_OPCION %>': row.codNum
	    		        			};
	    		        			Atis.sendForm("<%=request.getRequestURI()%>", params);
		              				$(this).dialog("close");
		            			},
		            			No: function() {
		              				$(this).dialog("close");
		            			}
		          			});
		        		}},
		        	]}
				],
				"actions": [
			    	{'label': 'Nueva opción', 'onClick': function() {
			    		var params = {
		    				'a': '<%= ControladorMeritosPreferentes.ACCION_NUEVA_OPCION %>',
		    				'<%= ControladorMeritosPreferentes.PARAM_ID %>': '<%= merito.getCodNum() %>'
		    			};
		    	   		Atis.sendForm("<%= request.getRequestURI() %>", params);
					}},
			    ]
			});
		<% } %>
	});	
</script>
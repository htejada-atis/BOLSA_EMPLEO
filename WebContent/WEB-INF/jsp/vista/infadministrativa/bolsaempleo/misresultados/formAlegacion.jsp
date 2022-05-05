<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados"%>

<form id="alegacion_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">

	<input type="hidden" name="<%= ControladorMisResultados.PARAM_ACCION %>" id="accion_formulario" value="" />
	<input type="hidden" name="<%=ControladorMisResultados.PARAM_BOLSA %>" id="usuario_id" value="" />
	
	<div class="form-group-container">
		<div class="form-group">
			<label for="nickname">Texto: </label>
			<textarea class="params form-input-custom" id="texto_alegacion" name="texto_alegacion" rows="3" cols="60"></textarea>
		</div>
	</div>
	<div class="form-group-container col1">
		<div class="form-group" style="float:right;">
			<input id="enviar_alegacion" type="submit" name="<%= ControladorMisResultados.PARAM_ENVIAR %>" value="Enviar alegación" style="float:right; margin-left: 12px;"/>
		</div>
	</div>

</form>
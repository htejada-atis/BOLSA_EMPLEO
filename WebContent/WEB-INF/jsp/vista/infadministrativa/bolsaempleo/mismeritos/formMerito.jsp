<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisMeritos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritos bean = (VistaMeritos) uvdatos.getVistas().get(VistaMeritos.class.getName());

ApartadoBaremacion apartado = bean.getApartado();
String item = BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritos.PARAM_ITEM, "");
String valor = BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritos.PARAM_VALOR, "");
String descripcion = BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritos.PARAM_DESCRIPCION, "");
String observacion = BolsaEmpleoUtils.getParamForm(request, ControladorMisMeritos.PARAM_OBSERVACION, "");
%>


<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Nuevo mérito</h2>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
    
    <form id="agregar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorMisMeritos.PARAM_ACCION %>" id="accion_formulario" 
    		value="<%= ControladorMisMeritos.ACCION_AGREGAR_MERITO %>" />
    	<div class="form-group-container col2">
	    	<div class="form-group">
				<label class="bold-label" for="select_apartado">Apartado:</label>
				<select class="form-input-custom" id="select_apartado" name="<%= ControladorMisMeritos.PARAM_APARTADO %>" required>
					<option value="">Elija el apartado</option>
					<%
					for(ApartadoBaremacion apar: bean.getApartados()) {
					%>	
						<% if (apartado != null && apartado.equals(apar)) { %>
		    				<option value="<%=apar.getCodNum()%>" selected><%=apar.getCodigo()%> - <%=apar.getNombre()%></option>
		    			<% } else { %>
		    				<option value="<%=apar.getCodNum()%>"><%=apar.getCodigo()%> - <%=apar.getNombre()%></option>
		    			<% } %>
		    		<%
		    		}
		    		%>
				</select>
			</div>
		
		<% if (apartado != null) { %>
			<div class="form-group">
				<label class="bold-label" for="select_item">Item:</label>
				<select class="form-input-custom" id="select_item" name="<%= ControladorMisMeritos.PARAM_ITEM %>" required>
					<option value="">Elija el item</option>
					<%
					for(ItemBaremacion it: bean.getItems()) {
					%>
						<% if (item != null && !item.isEmpty() && it.getCodNum() == Integer.parseInt(item)) { %>
		    				<option value="<%=it.getCodNum()%>" data-unidades="<%= it.getUnidades() %>" data-descripcion="<%= it.getDescripcion() %>" selected><%=it.getBloqueBaremacion().getApartadoBaremacion().getCodigo()%>.<%=it.getBloqueBaremacion().getCodigo()%>.<%=it.getCodigo()%> - <%=it.getNombre()%></option>
		    			<% } else { %>
		    				<option value="<%=it.getCodNum()%>" data-unidades="<%= it.getUnidades() %>" data-descripcion="<%= it.getDescripcion() %>"><%=apartado.getCodigo()%>.<%=it.getBloqueBaremacion().getCodigo()%>.<%=it.getCodigo()%> - <%=it.getNombre()%></option>
		    			<% } %>
		    		<%
		    		}
		    		%>
				</select>
			</div>
		
		<% } %>
		
		</div>
		
		<div id="contDescripcionItem" style="display:none;">
			<div class="form-group">
	    		<div class="form-group">
	    			<p id="descripcionItem">Nota aclaratoria: </p>
	    		</div>
    		</div>
		</div>
		
		<div id="contValor" style="display:none;">
			<div class="form-group">
	    		<div class="form-group">
	    			<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor:</label>
	    			<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorMisMeritos.PARAM_VALOR %>" value="<%= valor %>" required/>
	    		</div>
    		</div>
    	</div>
		
		<div id="contDescripcion">
			<div class="form-group">
	    		<div class="form-group">
	    			<label for="merito_descripcion" class="bold-label">Descripción:</label>
	    			<textarea class="form-input-custom" id="merito_descripcion" name="<%= ControladorMisMeritos.PARAM_DESCRIPCION %>" required><%= descripcion %></textarea>
	    		</div>
    		</div>
		</div>
	
    	<div class="form-file">
			<label for="merito_archivo" class="bold-label">Fichero:</label>
			<input id="merito_archivo" type="file" name="<%= ControladorMisMeritos.PARAM_ARCHIVO %>" required/>
		</div>
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="merito_observacion">Observación para la comisión:</label>
	    		<textarea class="form-input-custom" id="merito_observacion" name="<%= ControladorMisMeritos.PARAM_OBSERVACION %>" rows="2" cols="50"><%=observacion%></textarea>
	    	</div>
    	</div>
    	<div class="form-btn">
    		<input id="merito_enviar" type="submit" name="<%= ControladorMisMeritos.PARAM_ENVIAR %>" value="Agregar mérito"/>
    	</div>
    </form>
    
</div>

<script>

	<% if (item.isEmpty()) { %>
		$('#select_item').prop('selectedIndex',0);
	<%} else {%>
		var unidades = $(this).children("option:selected").data('unidades');
		var descripcion = $(this).children("option:selected").data('descripcion');
		//var selected = $(this).children("option:selected").val();
					
		if (descripcion) {
			$('#descripcionItem').html('Nota aclaratoria: <br/>' + descripcion);
			$('#contDescripcionItem').show();
		} else {
			$('#contDescripcionItem').hide();
		}	
		
		if (unidades=="SI/NO") {
			$('#contValor').hide();
			$('#merito_valor').removeAttr('required');				
		} else {
			$('#contValor').show();	
			$('#merito_valor').attr('required', true);
		}
		
		if (unidades) {
			$('#merito_valor_label').text('Valor (' + unidades + ')');
		} else {
			$('#merito_valor_label').text('Valor');
		}
	<%}%>

	$(document).ready(function() {
		
		document.getElementById("select_apartado").onchange = function () {
			var params = {'a': '<%= ControladorMisMeritos.ACCION_AGREGAR_MERITO %>',
					'<%= ControladorMisMeritos.PARAM_APARTADO %>': this.value};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}
		
		$('#select_item').on('change', function() {			
			var unidades = $(this).children("option:selected").data('unidades');
			var descripcion = $(this).children("option:selected").data('descripcion');
			//var selected = $(this).children("option:selected").val();
						
			if (descripcion) {
				$('#descripcionItem').html('Nota aclaratoria: <br/>' + descripcion);
				$('#contDescripcionItem').show();
			} else {
				$('#contDescripcionItem').hide();
			}	
			
			if (unidades=="SI/NO") {
				$('#contValor').hide();
				$('#merito_valor').removeAttr('required');				
			} else {
				$('#contValor').show();	
				$('#merito_valor').attr('required', true);
			}
			
			if (unidades) {
				$('#merito_valor_label').text('Valor (' + unidades + ')');
			} else {
				$('#merito_valor_label').text('Valor');
			}		
		})
		
		$('#agregar_merito').submit(function(event) { 
			$('#merito_enviar').prop('disabled', true);
			$('#merito_enviar').attr('value', 'Guardando mérito...');		
			return true;
		});
		
	});

</script>
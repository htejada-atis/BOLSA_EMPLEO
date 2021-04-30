<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils" %>

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

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	%>
	
	<h2>Nuevo mérito</h2>
	
	<p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
    
    <form id="agregar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorMisMeritos.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<div class="form-group-container col2">
	    	<div class="form-group">
				<label class="bold-label" for="select_apartado">Apartado:</label>
				<select class="form-input-custom" id="select_apartado" name="<%= ControladorMisMeritos.PARAM_APARTADO %>">
					<option value="0">Elija el apartado</option>
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
				<label class="bold-label" for="select_item">Ítem:</label>
				<select class="form-input-custom" id="select_item" name="<%= ControladorMisMeritos.PARAM_ITEM %>">
					<option value="0">Elija el ítem</option>
					<%
					for(ItemBaremacion it: bean.getItems()) {
					%>
						<% if (item != null && !item.isEmpty() && it.getCodNum() == Integer.parseInt(item)) { %>
		    				<option value="<%=it.getCodNum()%>" data-unidades="<%= it.getUnidades() %>" selected><%=it.getCodigo()%> - <%=it.getNombre()%> - <%=it.getDescripcion()%></option>
		    			<% } else { %>
		    				<option value="<%=it.getCodNum()%>" data-unidades="<%= it.getUnidades() %>"><%=it.getCodigo()%> - <%=it.getNombre()%> - <%=it.getDescripcion()%></option>
		    			<% } %>
		    		<%
		    		}
		    		%>
				</select>
			</div>
		
		<% } %>
		
		</div>
		
		<div class="form-group-container col2">
	    	<div class="form-group">
	    		<label for="merito_valor" id="merito_valor_label" class="bold-label">Valor:</label>
	    		<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorMisMeritos.PARAM_VALOR %>" value="<%= valor %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="merito_descripcion" class="bold-label">Descripción:</label>
	    		<input class="form-input-custom" id="merito_descripcion" type="text" name="<%= ControladorMisMeritos.PARAM_DESCRIPCION %>" value="<%= descripcion %>"></input>
	    	</div>
    	</div>
    	<div class="form-file">
			<label for="merito_archivo" class="bold-label">Fichero:</label>
			<input id="merito_archivo" type="file" name="<%= ControladorMisMeritos.PARAM_ARCHIVO %>"/>
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

	function agregarMerito(event, submit_input) {
		event.preventDefault();
		
		document.getElementById("merito_enviar").disabled=true;
		input_accion = document.getElementById("accion_formulario");
		input_accion.value = '<%= ControladorMisMeritos.ACCION_AGREGAR_MERITO %>';
		submit_input.form.submit();
	}

	$(document).ready(function() {
		
		document.getElementById("merito_enviar").addEventListener("click", function(event) {
			agregarMerito(event, this);
		});
		
		document.getElementById("select_apartado").onchange = function () {
			var params = {'a': '<%= ControladorMisMeritos.ACCION_AGREGAR_MERITO %>',
					'<%= ControladorMisMeritos.PARAM_APARTADO %>': this.value};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}
		
		$('#select_item').on('change', function() {			
			var unidades = $(this).children("option:selected").data('unidades');
			//var selected = $(this).children("option:selected").val();
						
			if (unidades) {
				$('#merito_valor_label').text('Valor (' + unidades + ')');
			} else {
				$('#merito_valor_label').text('Valor');
			}			
		})
		
		
	});

</script>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritos"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritos bean = (VistaMeritos) uvdatos.getVistas().get(VistaMeritos.class.getName());
%>


<div class="bolsa-empleo">

	<% 
		if(bean.getMensajesDeError().size()>0) {
			out.print("<div class='error'>" + bean.formatearMensajesDeError() + "</div>");
		}
	
	%>
	
	<h2>Nuevo mérito</h2>
    
    <form id="agregar_merito" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorMisMeritos.PARAM_ACCION %>" id="accion_formulario" value="" />
    	<div class="form-group-container col2">
	    	<div class="form-group">
				<label>Apartado:</label>
				<select class="form-input-custom" id="select_apartado" name="<%= ControladorMisMeritos.PARAM_APARTADO %>">
					<option value="0">Elija el apartado</option>
					<%
					for(ApartadoBaremacion apartado: bean.getApartados()) {
					%>	
						<% if (bean.getApartado() != null && bean.getApartado().equals(apartado)) { %>
		    				<option value="<%=apartado.getCodNum()%>" selected><%=apartado.getCodigo()%> - <%=apartado.getNombre()%></option>
		    			<% } else { %>
		    				<option value="<%=apartado.getCodNum()%>"><%=apartado.getCodigo()%> - <%=apartado.getNombre()%></option>
		    			<% } %>
		    		<%
		    		}
		    		%>
				</select>
			</div>
		
		<% if (bean.getApartado() != null) { %>
			<div class="form-group">
				<label>Ítem:</label>
				<select class="form-input-custom" id="select_item" name="<%= ControladorMisMeritos.PARAM_ITEM %>">
					<option value="0">Elija el ítem</option>
					<%
					for(ItemBaremacion item: bean.getItems()) {
					%>
		    			<option value="<%=item.getCodNum()%>"><%=item.getCodigo()%> - <%=item.getNombre()%></option>
		    		<%
		    		}
		    		%>
				</select>
			</div>
		
		<% } %>
		
		</div>
		
		<div class="form-group-container col2">
	    	<div class="form-group">
	    		<label for="merito_valor">Valor:</label>
	    		<input class="form-input-custom" id="merito_valor" type="text" name="<%= ControladorMisMeritos.PARAM_VALOR %>"/>
	    	</div>
	    	<div class="form-group">
	    		<label for="merito_descripcion">Descripción:</label>
	    		<input class="form-input-custom" id="merito_descripcion" type="text" name="<%= ControladorMisMeritos.PARAM_DESCRIPCION %>"></input>
	    	</div>
    	</div>
    	<div class="form-file">
			<label for="merito_archivo">Fichero:</label>
			<input id="merito_archivo" type="file" name="<%= ControladorMisMeritos.PARAM_ARCHIVO %>"/>
		</div>
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="merito_observacion">Observación para la comisión:</label>
	    		<textarea class="form-input-custom" id="merito_observacion" name="<%= ControladorMisMeritos.PARAM_OBSERVACION %>" rows="2" cols="50"></textarea>
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
		
	});

</script>
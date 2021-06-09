<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaTitulaciones bean = (VistaTitulaciones) uvdatos.getVistas().get(VistaTitulaciones.class.getName());
%>

<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Nueva titulación</h2>
		
		<div class="form-group-container col2-button-left">
			<div class="form-group">
				<p>Seleccione la titulación y complete el formulario inferior. Si no encuentra su titulación, por favor pulse sobre <strong>"Otra titulación"</strong> e introduzca el nombre de la titulación no disponible</p>
			</div>
			<div class="form-group">
				<button class="link-btn" id="btnOtraTitulacion">Otra titulación</button>
			    
			    <button class="link-btn" id="btnVolver" style="display:none;">Volver</button>
			</div>
		</div>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableTitulaciones">
		<tr>
			<th scope="col" style="width:100%">Nombre</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<div id="contenedor_agregar_titulacion" style="<%= bean.getTitulacion() != null ? "" : "display:none;" %>">
		
	<%	if (bean.getTitulacion() != null) { %>
			<h3 style="margin-top: 16px"><%= bean.getTitulacion().getNombre() %></h3>
	<%	} %>
		
		<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
		
		<form id="agregar_titulacion_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
	    	<input type="hidden" name="<%= ControladorMisTitulaciones.PARAM_ACCION %>" id="accion_formulario" 
	    			value="<%= ControladorMisTitulaciones.ACCION_AGREGAR_TITULACION %>" />
			<input type="hidden" name="<%= ControladorMisTitulaciones.PARAM_ID%>" id="titulacion_id" 
					value="<%= bean.getTitulacion() != null ? bean.getTitulacion().getCodNum() : "" %>" />
					
			<div class="form-group" id="otratitulacion" style="display:none;">
	    		<label for="nombre" class="bold-label">Nombre titulación: </label>
	    		<input class="form-input-custom" id="nombre" type="text"
	    			name="<%= ControladorMisTitulaciones.PARAM_OTRA_TITULACION %>" 
	    			value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorMisTitulaciones.PARAM_OTRA_TITULACION, "")%>"/>
	    	</div>
	    	
			<div class="form-group">
	    		<label for="razon_exclusion" class="bold-label">Descripción</label>
	    		<textarea class="params form-input-custom" id="descripcion" name="<%= ControladorMisTitulaciones.PARAM_DESCRIPCION %>" rows="3" cols="60" required><%=BolsaEmpleoUtils.getParamForm(request, ControladorMisTitulaciones.PARAM_DESCRIPCION, "")%></textarea>
	   		</div>
	   		   		
	    	<div class="form-file">
				<label for="fichero_archivo" class="bold-label">Fichero:</label>
				<input id="fichero_archivo" type="file" name="<%= ControladorMisTitulaciones.PARAM_ARCHIVO %>" required/>
			</div>
			
	    	<div class="form-btn">
	    		<input id="agregar_titulacion" type="submit" name="<%= ControladorMisTitulaciones.ACCION_AGREGAR_TITULACION %>" value="Enviar titulación"/>
	    	</div>
	    </form>
    </div>
</div>

<script>

$(document).ready(function() {

	var table_titulaciones = new Atis.DataTable('#tableTitulaciones', {
		"ajax": { url: "<%=ControladorMisTitulaciones.URL_PATTERN_AJAX%>", async: false },
    	"filterable": true,
    	"pageSize": 5,
    	"defaultOrderBy": 1,
    	"title": 'TITULACIONES',
    	"action": "<%=ControladorMisTitulaciones.ACCION_DATATABLE_TITULACIONES%>",
    	"defaultOrderBy": 0,
    	"defaultOrderDirection": "asc",
    	"clickable": {'onClick': function(row) {
    		var params = {
				'<%= ControladorMisTitulaciones.PARAM_ACCION %>': '<%= ControladorMisTitulaciones.ACCION_TITULACION_SELECCIONADA %>', 
				'<%= ControladorMisTitulaciones.PARAM_TITULACION %>': row.codNum
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
    	}},
    	<% if (bean.getTitulacion() != null) { %> 
    	"selected": <%= bean.getTitulacion().getCodNum() %> ,
    	<% } %>
    	"columns": [
        	{'data': 'nombre', 'overflow': 'auto', 'filter': {'type': 'text'}}
    	]
	});

	$('#btnOtraTitulacion').click(function(event) {
		event.preventDefault();
		
		$('#tableTitulaciones').hide();
		$('#otratitulacion').show();

		$('#btnVolver').show();
		$('#btnOtraTitulacion').hide();
		
		$('#contenedor_agregar_titulacion').show();
		
		$('#descripcion').val("");
		$('#fichero_archivo').val(null);
		$('#nombre').val("");
		$('#nombre').attr("required", "");
	});
	
	$('#btnVolver').click(function(event) { 
		event.preventDefault();
		
		$('#tableTitulaciones').show();
		$('#otratitulacion').hide();

		$('#btnVolver').hide();
		$('#btnOtraTitulacion').show();
		
		$('#contenedor_agregar_titulacion').hide();

		$('#descripcion').val("");
		$('#fichero_archivo').val(null);
		$('#nombre').val("");
		$('#nombre').removeAttr("required");		
	});
	
	$('#agregar_titulacion_usuario').submit(function(event) { 
		$('#agregar_titulacion').prop('disabled', true);
		$('#agregar_titulacion').attr('value', 'Guardando titulación...');			
		return true;
	});
});
</script>

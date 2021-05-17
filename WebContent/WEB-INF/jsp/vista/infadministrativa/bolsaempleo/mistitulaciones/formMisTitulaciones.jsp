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

Integer titulacion;

if(bean.getTitulacion()!=null){
	titulacion = bean.getTitulacion().getCodNum();
} else {
	if(!BolsaEmpleoUtils.getParamForm(request, ControladorMisTitulaciones.PARAM_ID, "").equals("")){
		titulacion = Integer.parseInt(BolsaEmpleoUtils.getParamForm(request, ControladorMisTitulaciones.PARAM_ID, ""));
	} else {
		titulacion = null;
	}
}

%>


<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Nueva titulación</h2>
	</div>
	
	  <div class="form-group-container col2">
    		<div class="form-group">
    			<p>Seleccione la titulación y complete el formulario inferior:</p>
    		</div>
    		<div class="form-group">
    			<p>Si no encuentra su titulación, pongase en contacto con nosotros</p>
    		</div>
    	</div>
    
	<table class="bluetable bolsaempleo" id="tableTitulaciones">
		<tr>
			<th scope="col" style="width:15%">ID</th>
			<th scope="col" style="width:85%">Nombre</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<p>Las etiquetas en <strong>negrita</strong> corresponden a campos de relleno obligatorio</p>
	
	<form id="agregar_titulacion_usuario" class="be-form" method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    	<input type="hidden" name="<%= ControladorMisTitulaciones.PARAM_ACCION %>" id="accion_formulario" 
    			value="<%= ControladorMisTitulaciones.ACCION_AGREGAR_TITULACION %>" />
		<input type="hidden" name="<%= ControladorMisTitulaciones.PARAM_ID%>" id="titulacion_id" 
				value="<%= bean.getTitulacion() != null ? bean.getTitulacion().getCodNum() : "" %>" />
		<div class="form-group">
    		<label for="razon_exclusion">Descripción</label>
    		<textarea class="params form-input-custom" id="descripcion" name="<%= ControladorMisTitulaciones.PARAM_DESCRIPCION %>" rows="3" cols="60"><%=BolsaEmpleoUtils.getParamForm(request, ControladorMisTitulaciones.PARAM_DESCRIPCION, "")%></textarea>
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

<script>

$(document).ready(function() {

	var table_titulaciones = new Atis.DataTable('#tableTitulaciones', {
		"ajax": { url: "<%=ControladorMisTitulaciones.URL_PATTERN_AJAX%>", async: false },
    	"filterable": true,
    	"pageSize": 10,
    	"defaultOrderBy": 1,
    	"title": 'Titulaciones',
    	"action": "<%=ControladorMisTitulaciones.ACCION_DATATABLE_TITULACIONES%>",
    	"defaultOrderBy": 0,
    	"defaultOrderDirection": "asc",
    	"clickable": {'onClick': function(row) {
    		var params = {
				'a': '<%= ControladorMisTitulaciones.ACCION_TITULACION_SELECCIONADA %>', 
				'<%= ControladorMisTitulaciones.PARAM_TITULACION %>': row.codNum
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
    	}},
    	<% if (titulacion!=null) { %> 
    	"selected": <%= titulacion %> ,
    	<% } %>
    	"columns": [
    		{'data': 'codNum'},
        	{'data': 'nombre', 'class': 'overflow-auto', 'filter': {'type': 'text'}}
    	]
	});


	document.getElementById("agregar_titulacion_usuario").style.visibility = "hidden";

	<% if (titulacion != null) { %>
		document.getElementById("agregar_titulacion_usuario").style.visibility = "visible";
		document.getElementById("descripcion").value = "";
		document.getElementById("fichero_archivo").value = null;
	<%}%>
	
	$('#agregar_titulacion_usuario').submit(function(event) { 
		$('#agregar_titulacion').prop('disabled', true);
		$('#agregar_titulacion').attr('value', 'Guardando titulación...');			
		return true;
	});
	
	
});
</script>
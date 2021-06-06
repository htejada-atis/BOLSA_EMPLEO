<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorMensajes"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMensajes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMensajes bean = (VistaMensajes) uvdatos.getVistas().get(VistaMensajes.class.getName());
Mensaje mensaje = bean.getMensaje();
%>

<div class="bolsa-empleo afinidad-form">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Mensaje: <%= mensaje.getTitulo() %></h2>
		
	<form id="mensaje_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMensajes.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorMensajes.ACCION_MODIFICAR_MENSAJE %>" />
		<input type="hidden" name="<%= ControladorMensajes.PARAM_MENSAJE_ID %>" id="mensaje_id" value="<%= mensaje.getCodNum() %>" />
		
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="titulo" class="bold-label">Título: </label>
    			<input id="titulo"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMensajes.PARAM_TITULO %>" 
    				   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorMensajes.PARAM_TITULO, mensaje.getTitulo()) %>"
    				   required/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="cuerpo">Cuerpo del mensaje:</label>
	    		<textarea class="form-input-custom" id="cuerpo" name="<%= ControladorMensajes.PARAM_CUERPO %>" rows="5" cols="50"><%= BolsaEmpleoUtils.getParamForm(request, ControladorMensajes.PARAM_CUERPO, "") %></textarea>
	    	</div>
    	</div>
    	
    	<div class="form-btn">
    		<input id="mensaje_enviar" type="submit" value="Enviar" />
    	</div>    
    </form>
    
    <table class="bluetable bolsaempleo" id="tableDestinatarios">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:20%" title="Documento">Documento</th>
			<th scope="col" style="width:55%" title="Nombre y apellidos">Nombre y apellidos</th>
			<th scope="col" style="width:20%" title="Correo">Correo</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>		
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<table class="bluetable bolsaempleo" style="margin-top: 2rem;">
		<caption>AÑADIR USUARIOS DEL SISTEMA AL MENSAJE</caption>		
	</table>
	
	<div class="form-group-container col2 helper">
   		<div class="form-group">
  			<label for="filtro_convocatoria">Filtrar por convocatoria:</label>
    		<select class="params" id="filtro_convocatoria" name="aplicable" style="width:100%;">
    			<option value="">--</option>
    			<% for (Convocatoria c : bean.getConvocatorias()) { %>
    				<option value="<%= c.getCodNum() %>"><%= c.getDescripcion() %></option>
    			<% } %>									
			</select>	
   		</div>
   		<div class="form-group" id="bloque_aplicable_bloque">
   			<label for="filtro_area">Filtar por área: </label>
    		<select class="params" id="filtro_area" name="aplicableBloque" style="width:100%;">
    			<option value="">--</option>
    			<% for (Area a : bean.getAreas()) { %>
    				<option value="<%= a.getCodNum() %>"><%= a.getDescripcion() %></option>
    			<% } %>	
    		</select>
   		</div>
   	</div>
	
	<table class="bluetable bolsaempleo" id="tableAddDestinatario">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:20%" title="Documento">Documento</th>
			<th scope="col" style="width:55%" title="Nombre y apellidos">Nombre y apellidos</th>
			<th scope="col" style="width:20%" title="Correo">Correo</th>
			<th scope="col" style="width:20%" title="Rol">Rol</th>
			<th scope="col" style="width:20%" title="Correo">Lista distribución</th>
		</tr>
		<tbody>
		</tbody>
		<tfoot>		
			<tr>
				<th colSpan="6" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>
$(document).ready(function() {
	var onClickRowDestinatario = function(row, checkbox) {
		console.log("candidato", row, checkbox);
	};
	
	var onFilterConvocatoria = function() {};
	
	var onFilterArear = function() {
		var val = $(this).val();		
		console.log(val);
	};
	
	var addDestinatarios = function(selected) {
		console.log(selected);		
	};
	
	var addTodos = function() {
		
	};
	
	var tableDestinatarios = new Atis.DataTable('#tableDestinatarios', {
		"title": "DESTINATARIOS DEL MENSAJE",
	    "ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>', async: false },
	    "params": {"<%=ControladorMensajes.PARAM_MENSAJE_ID%>": <%= mensaje.getCodNum() %>},
	    "pageSize": 10,
    	"action": "<%= ControladorMensajes.ACCION_DATATABLE_DESTINATARIOS %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': {'onChange': onClickRowDestinatario}},
	        {'data': 'fechaCreacion'},
	        {'data': 'titulo'},
	        {'data': 'estado'},        	
	    ]
	});
	
	var tableAddDestinatario = new Atis.DataTable('#tableAddDestinatario', {
	    "ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>', async: false },
	    "params": {"<%= ControladorMensajes.PARAM_MENSAJE_ID%>": <%= mensaje.getCodNum() %>},
	    "pageSize": 10,
	    "filterable": true,
	    "selectedAll": true,
    	"action": "<%= ControladorMensajes.ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': {'onChange': onClickRowDestinatario}},
	        {'data': 'prsnif', 'overflow': 'auto', 'filter': true,},
	        {'data': 'codNum', 'filter': false, 'order': false, 'overflow': 'auto', 'render': function(row) { return row.nombre + ", " + row.apellido1 + " " + row.apellido2; }},
	        {'data': 'email', 'overflow': 'auto'},
	        {'data': 'rol.descripcion', 
	         'filter': {
	        	'type': 'select', 
	        	'options': {
		        	'<%= ModeloRol.ID_ROL_SERVICIO_PERSONAL %>': 'Personal', 
		        	'<%= ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO %>': 'Director departamento',
		        	'<%= ModeloRol.ID_ROL_MIEMBRO_COMISION %>': 'Comisión',
		        	'<%= ModeloRol.ID_ROL_CANDIDATO %>': 'Candidato'
	        	}
	         }
	        },
	        {'data': 'listaDist', 'filter': {'type': 'selectBoolean', 'true': 'En Lista', 'false': 'Sin Lista', 'optionDefault': 'true'}, 'render': function(row) {
        		if(row.listaDist) {
        			return "<div title='En la lista de distribución' class='circle-true'></div>";
        		} else {
        			return "<div title='No está en la lista de distribución' class='circle-false'></div>";
        		}
        	}},
	    ],
    	"actions": [
	    	{'label': 'Añadir al mensaje', 'onClick': addDestinatarios},	    		    
	    	{'label': 'Añadir todos al mensaje', 'onClick': addTodos},
	    ]
	});
	
	$('#filtro_convocatoria').on('change', onFilterConvocatoria);
	$('#filtro_area').on('change', onFilterArear);
	
});
</script>

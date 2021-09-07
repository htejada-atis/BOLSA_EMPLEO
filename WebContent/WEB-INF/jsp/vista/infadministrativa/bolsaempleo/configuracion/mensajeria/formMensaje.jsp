<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol"%>
<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes"%>
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

boolean enviarDisabled = true;
boolean mensajeBorrador = mensaje.getEstado().equals(ModeloMensajes.MENSAJE_ESTADO_BORRADOR);

if (bean.getDestinatarios().size() > 0 && !mensaje.getCuerpo().isBlank() && !mensaje.getTitulo().equals(ModeloMensajes.MENSAJE_ESTADO_BORRADOR) && mensajeBorrador) {
	enviarDisabled = false;
}
%>

<div class="bolsa-empleo afinidad-form">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Mensaje: <%= EscapaHTML.escapa(mensaje.getTitulo()) %></h2>
		
	<form id="mensaje_form" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorMensajes.PARAM_ACCION %>" id="accion_formulario" value="<%= ControladorMensajes.ACCION_MODIFICAR_MENSAJE %>" />
		<input type="hidden" name="<%= ControladorMensajes.PARAM_MENSAJE_ID %>" id="mensaje_id" value="<%= mensaje.getCodNum() %>" />
		
		<div class="form-group-container col1">
    		<div class="form-group">
    			<label for="mensaje_titulo" class="bold-label">Título: </label>
    			<input id="mensaje_titulo"
    				   class="form-input-custom"
    				   type="text"
    				   name="<%= ControladorMensajes.PARAM_TITULO %>" 
    				   value="<%= EscapaHTML.escapa(BolsaEmpleoUtils.getParamForm(request, ControladorMensajes.PARAM_TITULO, mensaje.getTitulo())) %>"
    				   required
    				   <%= mensajeBorrador ? "" : "disabled" %>/>
    		</div>
    	</div>
    	
    	<div class="form-group-container col1">
	    	<div class="form-group">
	    		<label for="mensaje_cuerpo" class="bold-label">Cuerpo del mensaje:</label>
	    		<textarea class="form-input-custom" id="mensaje_cuerpo"
	    				name="<%= ControladorMensajes.PARAM_CUERPO %>" rows="5" cols="50"
	    				required
	    				<%= mensajeBorrador ? "" : "disabled" %>><%= EscapaHTML.escapa(BolsaEmpleoUtils.getParamForm(request, ControladorMensajes.PARAM_CUERPO, mensaje.getCuerpo())) %></textarea>
	    	</div>
    	</div>
    	
    	<div class="form-group-container col2">
    		<div class="form-group">
    		<%	if (mensajeBorrador) { %>
    			<input id="mensaje_guardar" type="submit" name="<%= ControladorMensajes.PARAM_GUARDAR %>" value="Guardar" style="float:left;" disabled/>
    			<input id="mensaje_borrar" type="button" name="<%= ControladorMensajes.PARAM_BORRAR %>" value="Borrar" style="float:left; margin-left: 10px;" />
    		<%	} %>
    		</div>
    		<div class="form-group">
    			<input id="mensaje_enviar" type="button" name="<%= ControladorMensajes.PARAM_ENVIAR %>" value="Enviar" style="float:right;"
    			<% if (enviarDisabled) { %> disabled <% } %>/>
    		</div>
    	</div>
    </form>
    
    <table class="bluetable bolsaempleo" id="tableDestinatarios">
		<tr>
		<%	if (mensajeBorrador) { %>
				<th scope="col" style="width:5%"></th>
		<% } %>
			<th scope="col" style="width:20%" title="Documento">Documento</th>
			<th scope="col" style="width:50%" title="Nombre y apellidos">Nombre y apellidos</th>
			<th scope="col" style="width:30%" title="Correo">Correo</th>
		<%	if (!mensajeBorrador) { %>
				<th scope="col" style="width:10%" title="Estado">Estado</th>	
		<% } %>
		</tr>
		<tbody>
		</tbody>
		<tfoot>		
			<tr>
				<th colSpan="4" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
<%	if (mensajeBorrador) { %>
		<table class="bluetable bolsaempleo" style="margin-top: 2rem;">
			<caption>AÑADIR USUARIOS DEL SISTEMA AL MENSAJE</caption>		
		</table>
		
		<div class="form-group-container col2 helper">
	   		<div class="form-group">
	  			<label for="filtro_convocatoria">Filtrar por convocatoria: <i class="tooltip">(?)<span>Con solicitud abierta o cerrada en la convocatoria</span></i></label>
	    		<select class="params" id="filtro_convocatoria" name="aplicable" style="width:100%;">
	    			<option value="">Selecciona convocatoria</option>
	    			<% for (Convocatoria c : bean.getConvocatorias()) { %>
	    				<option value="<%= c.getCodNum() %>"><%= c.getDescripcion() %></option>
	    			<% } %>									
				</select>
	   		</div>
	   		<div class="form-group" id="bloque_aplicable_bloque">
	   			<label for="filtro_area">Filtar por área:</label>
	    		<select class="params" id="filtro_area" name="aplicableBloque" style="width:100%;" disabled>
	    			<option value="">Selecciona área</option>
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
<%	} %>
	
</div>

<script>
$(document).ready(function() {
	
	tinyMCE.init({
		mode : "textareas",
		theme : "modern",
		language : "es",
		height: 250,
		menubar: "edit insert view format table",
		plugins: "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking table contextmenu directionality emoticons template textcolor paste fullpage textcolor colorpicker textpattern",
		paste_retain_style_properties: "all",
		toolbar1: "undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
		toolbar2: "preview | forecolor backcolor | code | styleselect | fontselect | fontsizeselect",
		toolbar_items_size: 'small',
	});
	
	<%	if (mensajeBorrador) { %>
	
		var addDestinatarios = function(selected) {
			if (selected.length > 0) {
				Atis.confirmDialog("Añadir destinatarios", "¿Desea añadir los destinatarios seleccionados?", {
		        	Si: function() {
		        		Atis.sendForm("<%= request.getRequestURI() %>", {
		        			'<%=ControladorMensajes.PARAM_ACCION%>': '<%=ControladorMensajes.ACCION_AGREGAR_DESTINATARIOS%>',
		        			'<%=ControladorMensajes.PARAM_MENSAJE_ID%>': <%= mensaje.getCodNum() %>,
		        			'<%=ControladorMensajes.PARAM_DESTINATARIOS %>': Atis.object2Json(selected)
		        		});
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
			}
		};
		
		var deleteDestinatarios = function(selected) {
			if (selected.length > 0) {
				Atis.confirmDialog("Eliminar destinatarios", "¿Desea eliminar los destinatarios seleccionados?", {
		        	Si: function() {
		        		Atis.sendForm("<%= request.getRequestURI() %>", {
		        			'<%=ControladorMensajes.PARAM_ACCION%>': '<%=ControladorMensajes.ACCION_ELIMINAR_DESTINATARIOS%>',
		        			'<%=ControladorMensajes.PARAM_MENSAJE_ID%>': <%= mensaje.getCodNum() %>,
		        			'<%=ControladorMensajes.PARAM_DESTINATARIOS %>': Atis.object2Json(selected)
		        		});
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
			}
		};
	
<%	} %>
	
	var tableDestinatarios = new Atis.DataTable('#tableDestinatarios', {
		"title": "DESTINATARIOS DEL MENSAJE",
		"ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>', async: false },
		"params": {"<%=ControladorMensajes.PARAM_MENSAJE_ID%>": <%= mensaje.getCodNum() %>},
		"pageSize": 10,
		"filterable": true,
		"action": "<%= ControladorMensajes.ACCION_DATATABLE_DESTINATARIOS %>",
		"columns": [
		<%	if (mensajeBorrador) { %>{'data': 'codNum', 'selectable': true},<% } %>
			{'data': 'usuario.prsnif', 'filter': true,},
			{'data': 'codNum', 'filter': true, 'overflow': 'auto', 'render': function(row) { 
				return row.usuario != null ? row.usuario.nombre + ", " + row.usuario.apellido1 + " " + row.usuario.apellido2 : ""; }
			},
			{'data': 'email', 'filter': true, 'overflow': 'auto', 'render': function(row) {
				return row.usuario != null ? row.usuario.email : row.email;
			}},
		<%	if (!mensajeBorrador) { %>{'data': 'estado', 'order': {'active': false}},<% } %>
		],
	<%	if (mensajeBorrador) { %>"actions": [{'label': 'Eliminar del mensaje', 'onClick': deleteDestinatarios}] <% } %>
	});
	
	document.getElementById("mensaje_enviar").addEventListener("click", function() {
		Atis.confirmDialog("Enviar mensaje", "El mensaje se enviará a los destinatarios seleccionados", {
        	Enviar: function() {
        		Atis.sendForm("<%= request.getRequestURI() %>", {
        			'<%=ControladorMensajes.PARAM_ACCION%>': '<%=ControladorMensajes.ACCION_ENVIAR_MENSAJE%>',
        			'<%=ControladorMensajes.PARAM_MENSAJE_ID%>': <%= mensaje.getCodNum() %>
        		});
          		$(this).dialog("close");
        	},
        	Cancelar: function() {
          		$(this).dialog("close");
        	}
      	});
	});
	
	document.getElementById("mensaje_titulo").addEventListener("input", function() {
		if (this.value == '<%= bean.getMensaje().getTitulo() %>') {
			document.getElementById("mensaje_guardar").setAttribute("disabled", "true");
		} else {
			document.getElementById("mensaje_guardar").removeAttribute("disabled");
		}
	});
	
	document.getElementById("mensaje_cuerpo").addEventListener("input", function() {
		if (this.value == '<%= EscapaHTML.escapa(bean.getMensaje().getCuerpo()) %>') {
			document.getElementById("mensaje_guardar").setAttribute("disabled", "true");
		} else {
			document.getElementById("mensaje_guardar").removeAttribute("disabled");
		}
	});
	
<%	if (mensajeBorrador) { %>
			
		var tableAddDestinatario = new Atis.DataTable('#tableAddDestinatario', {
		    "ajax": { url: '<%= ControladorMensajes.URL_PATTERN_AJAX %>', async: false },
		    "params": {"<%= ControladorMensajes.PARAM_MENSAJE_ID%>": <%= mensaje.getCodNum() %>},
		    "pageSize": 10,
		    "filterable": true,
		    "selectedAll": true,
	    	"action": "<%= ControladorMensajes.ACCION_DATATABLE_DESTINATARIOS_DISPONIBLES %>",
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		        {'data': 'prsnif', 'overflow': 'auto', 'filter': true,},
		        {'data': 'codNum', 'filter': true, 'overflow': 'auto', 'render': function(row) { return row.nombre + ", " + row.apellido1 + " " + row.apellido2; }},
		        {'data': 'email', 'filter': true, 'overflow': 'auto'},
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
	        		if (row.listaDist) {
	        			return "<div title='En la lista de distribución' class='circle-true'></div>";
	        		} else {
	        			return "<div title='No está en la lista de distribución' class='circle-false'></div>";
	        		}
	        	}},
		    ],
	    	"actions": [
		    	{'label': 'Añadir al mensaje', 'onClick': addDestinatarios},	    		    
		    ]
		});
	
		document.getElementById("mensaje_borrar").addEventListener("click", function() {
			Atis.confirmDialog("Eliminar mensaje", "¿Desea borrar el mensaje?", {
	        	Si: function() {
	        		Atis.sendForm("<%= request.getRequestURI() %>", {
	    				'<%=ControladorMensajes.PARAM_ACCION%>': '<%=ControladorMensajes.ACCION_BORRAR_MENSAJE%>',
	    				'<%=ControladorMensajes.PARAM_MENSAJE_ID%>': <%= mensaje.getCodNum() %>
	    			});
	          		$(this).dialog("close");
	        	},
	        	No: function() {
	          		$(this).dialog("close");
	        	}
	      	});
		});
		
		document.getElementById("filtro_convocatoria").addEventListener("change", function() {
			tableAddDestinatario.filterBy("6", this.value);
			if (this.value == '') {
				tableAddDestinatario.filterBy("7", '');
				document.getElementById("filtro_area").setAttribute("disabled", "true");
			} else {
				document.getElementById("filtro_area").removeAttribute("disabled");
			}
		});
		
		document.getElementById("filtro_area").addEventListener("change", function() {
			if (document.getElementById("filtro_convocatoria").value != '') {
				tableAddDestinatario.filterBy("6", document.getElementById("filtro_convocatoria").value);
				tableAddDestinatario.filterBy("7", this.value);
			}
		});
<%	} %>
	
});
</script>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="java.util.Date"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
UsuarioBolsaEmpleo candidato = bean.getCandidato();
%>


<div class="bolsa-empleo usuarios-form">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<%
		Integer codnum = null;
		String nombre = "";
		String apellidos = "";
		String email = "";
		String tipo_documento = "";
		String n_documento = "";
		String razon_excluido = "";
		String usuario = "";
		Boolean lista_dist = false;
		Boolean excluido = false;
		Date fecha_ini = null;
		Date fecha_fin = null;
		
		if (candidato != null) {
			usuario = candidato.getCodCuenta();
			nombre = candidato.getNombre();
			apellidos = candidato.getPrimerApellido() + " " + candidato.getPrimerApellido();
			email = candidato.getEmail();
			tipo_documento = candidato.getTipoDocumento();
			n_documento = candidato.getPrsNif();
			
			codnum = candidato.getCodNum();
			lista_dist = candidato.getListaDist();
			excluido = candidato.getExcluido();
			fecha_ini = candidato.getFechaExclusionInicio();
			fecha_fin = candidato.getFechaExclusionFin();
			
			if(candidato.getRazonExcluido()!=null){
				razon_excluido = candidato.getRazonExcluido();
			}
			else{
				razon_excluido ="";
			}
		}
	%>
	
	<form id="actualizar_candidato" class="be-form" method="post" action="<%= request.getRequestURI() %>">
    	<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_ACCION %>" id="accion_formulario" 
    			value="<%= ControladorUsuarioCandidato.ACCION_EDITAR_CANDIDATO %>" />
		<input type="hidden" name="<%= ControladorUsuarioCandidato.PARAM_CANDIDATO%>" id="usuario_id" 
				value="<%= candidato.getCodNum() %>" />
		
		<div class="form-group-container">
    		<div class="form-group">
    			<label for="nickname">Usuario: </label>
    			<input class="form-input-custom" id="nickname" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled/>
    		</div>
	   		<div class="form-group">
    			<label for="nombre">Nombre: </label>
    			<input class="form-input-custom" id="nombre" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= nombre %>" disabled/>
    		</div>
    	</div>
    	
    	<div class="form-group-container">
    	    <div class="form-group">
    			<label for="apellidos">Apellidos: </label>
    			<input class="form-input-custom" id="apellidos" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= apellidos %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="email">Email: </label>
    			<input class="form-input-custom" id="email" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= email %>" disabled/>
    		</div>
    	</div>

		<div class="form-group-container">
	    	<div class="form-group">
    			<label for="tipo_documento">Tipo de documento: </label>
    			<input class="form-input-custom" id="tipo_documento" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= tipo_documento %>" disabled/>
    		</div>
    		<div class="form-group">
    			<label for="n_documento">Nº de documento: </label>
    			<input class="form-input-custom" id="n_documento" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE %>" value="<%= n_documento %>" disabled/>
    		</div>
		</div>
		<div class="form-group-container">
	    	<div class="form-check-custom">
    			<label for="usuario_lista_dist"><input class="params" type="checkbox" id="usuario_lista_dist" name="<%= ControladorUsuarioCandidato.PARAM_LISTA %>"
    				 value="true" <%= (lista_dist ? "checked=''" : "") %>/>Lista Distribución</label>
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom" style="float: left;">
    				<label for="usuario_excluido"><input class="params" type="checkbox" id="usuario_excluido" name="<%= ControladorUsuarioCandidato.PARAM_EXCLUIDO %>"
    				 value="true" <%= (excluido ? "checked=''" : "") %>/>Excluido</label>
    			</div>
    			<div class="form-check-custom" id="excluido_tipo" style="display:none;">
					<label class="form-label-custom" for="indefinido" style="float: none; margin-right:0px; margin-bottom:5px;">
						<input class="form-input" type="radio" id="indefinido" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>"
							value="I" style="display: inline;">Indefinido
					</label>
					<label class="form-label-custom" for="temporal" style="float: none; margin-right:0px;">
						<input class="form-input" type="radio" id="temporal" name="<%= ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO %>"
							value="T" style="display: inline;">Temporal
					</label>
    			</div>
    		</div>
		</div>
		<div class="form-group-container col-2" id="fecha_excluido" style="display:none;">
	    	<div class="form-check">
    			
    		</div>
    		<div class="form-check">
    			<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Inicio</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_INICIO %>" id="fecha_ini" autocomplete="off" value="<%= fecha_ini!=null ? fecha_ini : "" %>" style="width:80%"/>
	    		</div>
	    		<div class="form-check-custom">
	    			<label for="noticia_fecha"><strong>Fecha Fin</strong>:</label>
	    			<input class="form-input-custom" type="text" name="<%= ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_FIN %>" id="fecha_fin" autocomplete="off" value="<%= fecha_fin!=null ? fecha_fin : "" %>" style="width:80%"/>
	    		</div>
	    	</div>
		</div>
	
		<div class="form-group" id="razon_excluido" style="display:none;">
    		<label for="razon_exclusion">Razón exclusión</label>
    		<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioCandidato.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60" <%= (excluido ? "" : "disabled") %>><%= razon_excluido %></textarea>
   		</div>
		
		<div class="form-group-container col1">
    		<div class="form-group" style="float:right;">
    			<input id="usuario_guardar" type="submit" name="<%= ControladorUsuarioCandidato.PARAM_GUARDAR %>" value="Guardar candidato" style="float:right; margin-left: 12px;"/>
    		<%	if (candidato != null && candidato.getBorrado()) { %>
    				<label for="usuario_restaurar" style="margin-top: 5px; margin-left: 6px; float:right; width: unset !important;">Usuario Borrado</label>
    				<input id="usuario_restaurar" type="button" value="Restaurar" style="float:right;"/>
    		<%	} else { %>
    				<input id="usuario_borrar" type="button" value="Borrar usuario" style="float:right;"/>
    		<%	} %>
    		</div>
    	</div>
    	
    </form>
    
    <ul class="nav-tabs-widget" style="margin-left: 0px; margin-top: 16px;">
    	<li <%= bean.getApartadoAreasExcluidas() != null ? "class='tab-selected'" : "" %>>
   	    	<a id="areas_excluidas">Áreas excluidas</a>
   	    </li>
   	    <li <%= bean.getApartadoSolicitudes() != null ? "class='tab-selected'" : "" %>>
   			<a id="solicitudes">Solicitudes</a>
   		</li>
   		<li <%= bean.getApartadoTitulaciones() != null ? "class='tab-selected'" : "" %>>
   			<a id="titulaciones">Titulaciones</a>
   		</li>
   		<li <%= bean.getApartadoAcreditaciones() != null ? "class='tab-selected'" : "" %>>
   			<a id="acreditaciones">Acreditaciones</a>
   		</li>
   		<li <%= bean.getApartadoComunicaciones() != null ? "class='tab-selected'" : "" %>>
   			<a id="comunicaciones">Comunicaciones</a>
   		</li>
   	</ul>


<%	if (bean.getApartadoAreasExcluidas() != null) { %>
		<table class="bluetable bolsaempleo" id="table_areas_excluidas">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:20%" title="Id de la area">Id</th>
				<th scope="col" style="width:25%" title="Código de area">Código</th>
				<th scope="col" style="width:65%">Area</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="4"></th>
				</tr>
			</tfoot>
		</table>
		
		
		<table class="bluetable bolsaempleo" id="table_areas">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col" style="width:20%" title="Id de la area">Id</th>
				<th scope="col" style="width:25%" title="Código de area">Código</th>
				<th scope="col" style="width:65%">Area</th>
			</tr>
			<tbody>				
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="4"></th>
				</tr>
			</tfoot>
		</table>		
<%	} %>

<%	if (bean.getApartadoTitulaciones() != null) { %>
		<table class="bluetable bolsaempleo" id="table_titulaciones">
			<tr>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:50%">Titulación</th>
				<th scope="col" style="width:30%">Descripción</th>
				<th scope="col" style="width:10%">Borrada</th>
				<th scope="col" style="width:10%">Validada</th>
				<th scope="col" style="width:10%" class="center"></th>
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="6"></th>
				</tr>
			</tfoot>
		</table>
<%	} %>

<%	if (bean.getApartadoAcreditaciones() != null) { %>
		<table class="bluetable bolsaempleo" id="table_acreditaciones">
			<tr>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:10%">Código</th>
				<th scope="col"	style="width:40%">Nombre</th>
				<th scope="col"	style="width:20%">Descripción</th>
				<th scope="col" style="width:10%">Borrada</th>
				<th scope="col" style="width:10%">Validada</th>
				<th scope="col"	style="width:10%" class="center"></th>
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="7"></th>
				</tr>
			</tfoot>
		</table>
<%	} %>

<%	if (bean.getApartadoSolicitudes() != null) { %>
		<table class="bluetable bolsaempleo" id="table_solicitudes">
			<tr>
				<th scope="col"	style="width:15%">Id Solicitud</th>
				<th scope="col"	style="width:45%">Descripción convocatoria</th>
				<th scope="col"	style="width:20%">Estado solicitud</th>
				<th scope="col" style="width:20%">Fecha confirmación</th>	
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colSpan="4"></th>
				</tr>
			</tfoot>
		</table>
<%	}%>
	
<%	if (bean.getApartadoComunicaciones() != null) { %>
	
		
<%	} %>

</div>

<script>

	$(document).ready(function() {
		
		$("#fecha_ini").datepicker();
		$("#fecha_fin").datepicker();
		
	<%	if (fecha_ini == null) { %>
			document.getElementById("fecha_ini").value = getTodayDate();
	<%	} %>

		document.getElementById("usuario_excluido").addEventListener("change", function(event) {
			if (document.getElementById("usuario_excluido").checked) {
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=false;
				
				$("#excluido_tipo").show();
				$("#razon_excluido").show();
				$("#razon_excluido").children("textarea").attr("required", true);
				$("#excluido_tipo").find("input").attr("required", true);
			} else {
				document.getElementById("razon_exclusion").value="";
				document.getElementById("razon_exclusion").disabled=true;
				
				$("#excluido_tipo").hide();
				$("#razon_excluido").hide();
				$("#razon_excluido").children("textarea").attr("required", false);
				$("#excluido_tipo").find("input").attr("required", false);
			}
		});
		
		document.getElementById("temporal").addEventListener("change", function(event) {
			if(document.getElementById("temporal").checked) $("#fecha_excluido").show();
		});
		document.getElementById("indefinido").addEventListener("change", function(event) {
			if(document.getElementById("indefinido").checked) $("#fecha_excluido").hide();
		});
		
		document.getElementById("areas_excluidas").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_AREAS_EXCLUIDAS_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
		document.getElementById("solicitudes").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_SOLICITUDES_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
		document.getElementById("titulaciones").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_TITULACIONES_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
		document.getElementById("acreditaciones").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_ACREDITACIONES_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
		document.getElementById("comunicaciones").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
	<%	if (bean.getApartadoSolicitudes() != null) { %>
			var table = new Atis.DataTable('#table_solicitudes', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>" },
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "pageSize": 10,
			    "title": "SOLICITUDES",
			    "filterable": true,
			    "clickable": {'onClick': function(row) {
			    	var params = {
		    				'a': '<%= ControladorUsuarioCandidato.ACCION_SELECCIONAR_SOLICITUD %>',
		    				'<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
		    				'<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>': row.codNum};
		    		Atis.sendForm("<%= request.getRequestURI() %>", params);
			    }},
			    "action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_SOLICITUDES %>",
			    "columns": [
			    	{'data': 'codNum'},
			    	{'data': 'convocatoria.descripcion', order: {'active': false}},
			        {'data': 'estado', order: {'active': false}},
			        {'data': 'fechaConfirmacion', order: {'active': false}, 'render': function(row) {
			        	return row.estado == 'CERRADA' ? '' : row.fechaConfirmacion;
			        }}
			    ]
			});
	<%	} %>
	
	<%	if (bean.getApartadoTitulaciones() != null) { %>
			var tableTitulaciones = new Atis.DataTable('#table_titulaciones', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
			    "title": 'TITULACIONES',
			    "pageSize": 10,
			    "action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_TITULACIONES_CANDIDATO%>",
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%=candidato.getCodNum()%>},
			    "filterable": true,
			    "columns": [
			    	{'data': 'codNum', 'filter': {'type': 'number'}},
			        {'data': 'titulacion.nombre', 'filter': true, 'render': function(row) {
			        	return !row.titulacion ? 'OTRA TITULACION' : row.titulacion.nombre;
			        }},
			    	{'data': 'descripcion', 'overflow': 'auto', 'filter': true},
			    	{'data': 'borrado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Borrada', 'false': 'No borrada'}, 'optionDefault': 'false'}, 'render': function(row) {
		        		return row.borrado ? "<div title='Borrada' class='circle-false'></div>" : "";
		        	}},
		        	{'data': 'validada', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Validada', 'false': 'No validada'}}, 'render': function(row) {
		        		return row.validada ? "<div title='Validada' class='circle-true'></div>" : "";
		        	}},
		        	{'data': 'codnum', 'buttons': [
		        		{'title': 'Descargar titulación', 'class': 'only-icon icon-download', 'onClick': function(row) {
		        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_TITULACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_TITULACION %>=" + row.codNum);
		        		}},
		   			]}
			    ],
			});
	<%	} %>
	
	<%	if (bean.getApartadoAcreditaciones() != null) { %>
			var tableAcreditaciones = new Atis.DataTable('#table_acreditaciones', {
			    "ajax": { url: "<%= ControladorUsuarioCandidato.URL_PATTERN_AJAX %>" },
			    "pageSize": 10,
			    "action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO %>",
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "filterable": true,
			    "title": 'ACREDITACIONES: <%=candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido()%>',
			    "columns": [
			    	{'data': 'codNum', 'filter': {'type': 'number'}},
			    	{'data': 'codigo', 'filter': true, 'render': function(row) {
			    		return '<%= bean.getCodigoPadreMeritoPreferente() %>.' + row.meritoPreferente.codigo;
			    	}},
			    	{'data': 'meritoPreferente.nombre', 'order': {'active': false}, 'render': function(row) {
			    		return row.meritoPreferente.nombre + (row.meritoPreferenteOpcion ? ' (' + row.meritoPreferenteOpcion.nombre + ')' : '');
			    	}},
			    	{'data': 'descripcion'},
			    	{'data': 'borrado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Borrada', 'false': 'No borrada'}, 'optionDefault': 'false'}, 'render': function(row) {
		        		return row.borrado ? "<div title='Borrada' class='circle-false'></div>" : "";
		        	}},
		        	{'data': 'validado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Validada', 'false': 'No validada'}}, 'render': function(row) {
		        		return row.validado ? "<div title='Validada' class='circle-true'></div>" : "";
		        	}},
			        {'data': 'codnum', 'buttons': [
		        		{'title': 'Descargar fichero acreditación', 'class': 'only-icon icon-download', 'onClick': function(row) {
		        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
		        		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_ACREDITACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_ACREDITACION %>=" + row.codNum);
		        		}},
		   			]}
			    ],
			});
	<%	} %>
	
	<%	if (bean.getApartadoAreasExcluidas() != null) { %>
			var table = new Atis.DataTable('#table_areas_excluidas', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "selectable": true,
			    "pageSize": 10,
			    "filterable": true,
			    "title": 'ÁREAS EXCLUIDAS PARA EL USUARIO',
			    "action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO%>",
			    "columns": [
			    	{'data': 'area.codNum', 'selectable': true},
			        {'data': 'area.codNum', 'filter': {'type': 'number'}},
			        {'data': 'area.idAreaExterno', 'filter': true, 'overflow': 'auto'},
			        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'}
			    ],
			    "actions": [
			    	{'label': 'Borrar Areas excluidas', 'onClick': function(selected) { excluirArea(false, selected); } }
			    ]
			});
		
			var table = new Atis.DataTable('#table_areas', {
			    "ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
			    "params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
			    "selectable": true,
			    "pageSize": 10,
			    "filterable": true,
			    "title": 'LISTADO DE ÁREAS',
			    "action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO %>",
			    "columns": [
			    	{'data': 'area.codNum', 'selectable': true},
			        {'data': 'area.codNum', 'filter': {'type': 'number'}},
			        {'data': 'area.idAreaExterno' , 'filter': true, 'overflow': 'auto'},
			        {'data': 'area.descripcion', 'filter': true, 'overflow': 'auto'}
			    ],
			    "actions": [
			    	{'label': 'Excluir Areas', 'onClick': function(selected) { excluirArea(true, selected); } }
			    ]
			});
			
			
			function excluirArea(excluir, selected) {
				if (selected.length == 0) {
					Atis.alertDialog("Exclusión de areas", "Seleccione al menos un usuario.");
					return;
				}
				
				var message = excluir ? "¿Desea excluir las areas seleccionadas del usuario?" : "¿Desea borrar las areas excluidas seleccionadas del usuario?";
				Atis.confirmDialog("Exclusión de areas", message, {
		        	Si: function() {
		        		var params = {
		        				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': excluir ? "<%=ControladorUsuarioCandidato.ACCION_EXCLUIR_USUARIO_AREA%>" : "<%= ControladorUsuarioCandidato.ACCION_INCLUIR_USUARIO_AREA %>",
		        				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= candidato.getCodNum() %>,
		        				'<%=ControladorUsuarioCandidato.PARAM_AREAS_SELECCIONADAS%>': Atis.object2Json(selected)
		        			};
		    			Atis.sendForm("<%= request.getRequestURI() %>", params);
		          		$(this).dialog("close");
		        	},
		        	No: function() {
		          		$(this).dialog("close");
		        	}
		      	});
			}
		
	<%	} %>
		
		function getTodayDate() {
			var now = new Date();
			
			var day = ("0" + now.getDate()).slice(-2);
			var month = ("0" + (now.getMonth() + 1)).slice(-2);
			
			return day + "/" + month + "/" + now.getFullYear();
		}
		
	<%	if (candidato != null && candidato.getBorrado()) { %>
			document.getElementById("usuario_restaurar").addEventListener("click", function(event) {
				event.preventDefault();
				
				Atis.sendForm("<%= request.getRequestURI() %>", {
					'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_RECUPERAR_CANDIDATO%>',
					'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=candidato.getCodNum()%>'
				});
			});
	<%	} else { %>
			document.getElementById("usuario_borrar").addEventListener("click", function(event) {
				event.preventDefault();
				
				Atis.confirmDialog("Eliminar candidato", "¿Desea borrar el candidato?", {
			    	Si: function() {
			    		Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_ELIMINAR_CANDIDATO%>',
							'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=candidato.getCodNum()%>'
						});
			          	$(this).dialog("close");
			        },
			        No: function() {
			          	$(this).dialog("close");
			        }
			    });
			});
	<%	} %>
	});
	
	
</script>

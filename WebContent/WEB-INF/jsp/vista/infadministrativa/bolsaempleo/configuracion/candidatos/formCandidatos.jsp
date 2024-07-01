<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEstadoCandidato" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@	page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Map.Entry" %>

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
		String direccion = "";
		String codigoPostal = "";
		String localidad = "";
		String provincia = "";
		String telefono = "";
		String nacionalidad = "";
		Boolean lista_dist = false;
		Boolean excluido = false;
		Date fecha_ini = null;
		Date fecha_fin = null;
		
		if (candidato != null) {
			usuario = candidato.getCodCuenta();
			nombre = candidato.getNombre();
			apellidos = candidato.getPrimerApellido() + " " + candidato.getSegundoApellido();
			email = candidato.getEmail();
			tipo_documento = candidato.getTipoDocumento();
			n_documento = candidato.getPrsNif();
			direccion = candidato.getDireccion() != null ? candidato.getDireccion() : "";
			codigoPostal = candidato.getCodigoPostal() != null ? candidato.getCodigoPostal() : "";
			localidad = candidato.getLocalidad() != null ? candidato.getLocalidad() : "";
			provincia = candidato.getProvincia() != null ? candidato.getProvincia() : "";
			telefono = candidato.getTelefono() != null ? candidato.getTelefono() : "";
			nacionalidad = candidato.getNacionalidad() != null ? candidato.getNacionalidad() : "";
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
				<input class="form-input-custom" id="nickname" type="text" name="<%= ControladorUsuarioCandidato.PARAM_NOMBRE_USUARIO%>" value="<%= usuario %>" disabled
					style="width: 250px; float: left;"/>
				<button id="btn-change-nickname" style="float: left; margin-left: 5px;">Cambiar</button>
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
			<div class="form-group">
				<label for="direccion">Direcci&oacute;n: </label>
				<input class="form-input-custom" id="direccion" type="text" name="direccion" value="<%= direccion %>" disabled/>
			</div>
			<div class="form-group">
				<label for="codigo_postal">Codigo Postal: </label>
				<input class="form-input-custom" id="codigo_postal" type="text" name="codigo_postal" value="<%= codigoPostal %>" disabled/>
			</div>
		</div>
		
		<div class="form-group-container">
			<div class="form-group">
				<label for="localidad">Localidad: </label>
				<input class="form-input-custom" id="localidad" type="text" name="localidad" value="<%= localidad %>" disabled/>
			</div>
			<div class="form-group">
				<label for="provincia">Provincia: </label>
				<input class="form-input-custom" id="provincia" type="text" name="provincia" value="<%= provincia %>" disabled/>
			</div>
		</div>
		
		<div class="form-group-container">
			<div class="form-group">
				<label for="telefono">Tel&eacute;fono: </label>
				<input class="form-input-custom" pattern="[0-9]{1,11}" id="telefono" type="text" name="telefono" value="<%= telefono %>" disabled/>
			</div>
						<div class="form-group">
				<label for="nacionalidad">Nacionalidad: </label>
				<input class="form-input-custom" id="nacionalidad" type="text" name="nacionalidad" value="<%= nacionalidad %>" disabled/>
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
			<label for="razon_exclusion">Razón exclusión:</label>
			<textarea class="params form-input-custom" id="razon_exclusion" name="<%= ControladorUsuarioCandidato.PARAM_RAZON_EXCLUIDO %>" rows="3" cols="60" <%= (excluido ? "" : "disabled") %>><%= razon_excluido %></textarea>
		</div>
		
		<%	if (candidato != null && candidato.getBorrado()) { %>
				<div class="form-group-container col2">
					<div class="form-group" style="margin-top: 0.4rem; margin-bottom: 0.5rem;">
						<label for="razon_borrado">Razón borrado:</label>
						<textarea class="form-input-custom" id="razon_borrado" name="razonborrado" rows="2" disabled><%= bean.getCandidato().getRazonBorrado() %></textarea>
					</div>
				</div>
		<%	} %>
		
		<div class="form-group-container col2">
			<div class="form-group">
				<%	if (candidato != null && candidato.getBorrado()) { %>
					<input id="usuario_restaurar" type="button" value="Restaurar" style="float:right;"/>
				<%	} else { %>
					<input id="usuario_actualizar_datos" type="button" value="Actualizar datos personales" style="float:left;"/>
				<%  } %>
			</div>
			<div class="form-group">				
				<input id="usuario_guardar" type="submit" name="<%= ControladorUsuarioCandidato.PARAM_GUARDAR %>" value="Guardar candidato" style="float:right; margin-left: 10px;"/>
			<%	if (candidato != null && !candidato.getBorrado()) { %>
					<input id="usuario_borrar" type="button" value="Dar de baja candidato" style="float:right;"/>
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
		<li <%= bean.getApartadoMeritos() != null ? "class='tab-selected'" : "" %>>
			<a id="meritos">Méritos</a>
		</li>
		<li <%= bean.getApartadoTitulaciones() != null ? "class='tab-selected'" : "" %>>
			<a id="titulaciones">Titulaciones</a>
		</li>
		<li <%= bean.getApartadoAcreditaciones() != null ? "class='tab-selected'" : "" %>>
			<a id="acreditaciones">Acreditaciones</a>
		</li>
		<li <%= bean.getApartadoContrataciones() != null ? "class='tab-selected'" : "" %>>
			<a id="contrataciones">Contrataciones</a>
		</li>
	</ul>


<%	if (bean.getApartadoAreasExcluidas() != null) { %>
		<table class="bluetable bolsaempleo" id="tableAreasExcluidasCAN">
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
		
		
		<table class="bluetable bolsaempleo" id="tableAreasCAN">
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
		<table class="bluetable bolsaempleo" id="tableTitulacionesCAN">
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
		<table class="bluetable bolsaempleo" id="tableAcreditacionesCAN">
			<tr>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:10%">Código</th>
				<th scope="col"	style="width:40%">Nombre</th>
				<th scope="col" style="width:10%">Borrada</th>
				<th scope="col" style="width:10%">Validada</th>
				<th scope="col" style="width:10%">Activa</th>
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
		<table class="bluetable bolsaempleo" id="tableSolicitudesCAN">
			<tr>
				<th scope="col"	style="width:15%">Id Solicitud</th>
				<th scope="col"	style="width:45%">Descripción convocatoria</th>
				<th scope="col"	style="width:20%">Estado solicitud</th>
				<th scope="col" style="width:20%">Fecha confirmación</th>
			</tr>
			<tbody></tbody>
			<tfoot>
				<tr>
					<th colSpan="4"></th>
				</tr>
			</tfoot>
		</table>
<%	}%>

<%	if (bean.getApartadoMeritos() != null) { %>

		<table class="bluetable bolsaempleo" id="tableMeritosCAN">
			<tr>
				<th scope="col" style="width:10%">Id</th>
				<th scope="col" style="width:10%">Bloque</th>
				<th scope="col"	style="width:12%">Código ítem</th>
				<th scope="col"	style="width:15%">Nombre ítem</th>
				<th scope="col"	style="width:15%">Descripción</th>
				<th scope="col"	style="width:8%">Valor</th>
				<th scope="col"	style="width:20%">Observación</th>
				<th scope="col"	style="width:10%"></th>
			</tr>
			<tbody>		
			</tbody>
			<tfoot>
				<tr>
					<th colspan="8" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>

<%	} %>
	
<%	if (bean.getApartadoContrataciones() != null) { %>
		
		<table class="bluetable bolsaempleo" id="tableEstadosCandidatoCAN">
			<tr>
				<th scope="col" style="width:15px"></th>
				<th scope="col" style="width:100%">Área</th>
				<th scope="col" style="width:75px">Estado</th>
				<th scope="col" style="width:12%">Plaza</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="4" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
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
		
		document.getElementById("meritos").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_MERITOS_CANDIDATO%>',
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
		
		document.getElementById("contrataciones").addEventListener("click", function(event) {
			Atis.sendForm("<%= request.getRequestURI() %>", {
				'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_CONTRATACIONES_CANDIDATO%>',
				'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': <%= codnum %>
			});
		});
		
		document.getElementById("btn-change-nickname").addEventListener("click", function(event) {
			event.preventDefault();
			
			var mensaje = " <p>Se cambiará el login del usuario.<br/>Tenga en cuenta que tiene que comunicar el cambio de usuario a informática para que tenga validez.</p>"
				mensaje += "<br/>";
				mensaje += "<div class='form-group-container col1'>";
				mensaje += "    <div class='form-group-dialog'>";
				mensaje += "        <label class='bold-label' for='dialog-nickname-old'>Antiguo login: </label>";
				mensaje += "        <input class='form-input-custom' id='dialog-nickname-old' type='text' value='<%= usuario %>' disabled/>";
				mensaje += "    </div>";
				mensaje += "</div><br/><br/>";
				mensaje += "<div class='form-group-container col1'>";
				mensaje += "    <div class='form-group-dialog'>";
				mensaje += "        <label class='bold-label' for='dialog-nickname-tipo'>¿Nuevo login es para un usuario externo o para usuario con cuenta tic?: </label>";
				mensaje += "        <select id='dialog-nickname-tipo' required>";
				mensaje += "            <option value=''>--</option>";
				mensaje += "            <option value='externo'>Externo (con email)</option>";
				mensaje += "            <option value='tic'>Cuenta tic (sin email)</option>";
				mensaje += "        </select>";
				mensaje += "    </div>";
				mensaje += "</div><br/>";
				mensaje += "<div class='form-group-container col1'>";
				mensaje += "    <div class='form-group-dialog'>";
				mensaje += "        <label class='bold-label' for='dialog-nickname-new'>Nuevo login: </label>";
				mensaje += "        <input class='form-input-custom' id='dialog-nickname-new' type='text' value=''/>";
				mensaje += "    </div>";
				mensaje += "</div>";
				
				Atis.confirmDialog("Cambiar login del candidato", mensaje, {
					'Confirmar': function() {
						var selectTipo = this.querySelector('#dialog-nickname-tipo');
						var nuevoLogin = this.querySelector('#dialog-nickname-new');
						
						var params = {
								"<%= ControladorUsuarioCandidato.PARAM_ACCION %>": "<%= ControladorUsuarioCandidato.ACCION_CAMBIAR_LOGIN_CANDIDATO %>",
								"<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>": <%= bean.getCandidato().getCodNum() %>,
								"<%= ControladorUsuarioCandidato.PARAM_TIPOLOGIN %>": selectTipo.value,
								"<%= ControladorUsuarioCandidato.PARAM_LOGINNUEVO %>": nuevoLogin.value
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				});
		});
		
	<%	if (bean.getApartadoSolicitudes() != null) { %>
			var tableSolicitudes = new Atis.DataTable('#tableSolicitudesCAN', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>" },
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"pageSize": 10,
				"filterable": true,
				"defaultOrderBy": 0,
				"defaultOrderDirection": 'desc',
				"title": "SOLICITUDES",
				"clickable": {'onClick': function(row) {
					var params = {
							'<%= ControladorUsuarioCandidato.PARAM_ACCION %>': '<%= ControladorUsuarioCandidato.ACCION_SELECCIONAR_SOLICITUD %>',
							'<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>': '<%= bean.getCandidato().getCodNum() %>',
							'<%= ControladorUsuarioCandidato.PARAM_SOLICITUD %>': row.codNum};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				}},
				"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_SOLICITUDES %>",
				"columns": [
					{'data': 'codNum', 'filter': {'type': 'number'}},
					{'data': 'convocatoria.descripcion', 'filter': true},
					{'data': 'estado', 'filter': false, 'render': function(row) { 
						return row.estado + (row.excluido ? " (excluida)" : "");
					}},
					{'data': 'fechaConfirmacion', order: {'active': false}, 'render': function(row) {
						return row.estado == 'CERRADA' ? row.fechaConfirmacion : '';
					}}
				]
			});
			
			Atis.smoothScrollToAnchor("#solicitudes");
	<%	} %>
	
	<%	if (bean.getApartadoMeritos() != null) { %>
			var optionsApartados = {};
			
			<% if (bean.getApartados() != null) { %>
				<% for (ApartadoBaremacion apartado: bean.getApartados()) { %>
					optionsApartados[<%=apartado.getCodNum()%>] = '<%=apartado.getNombre()%>';
				<% } %>
			<% } %>
	
			var table_meritos = new Atis.DataTable('#tableMeritosCAN', {
				"ajax": { url: "<%= ControladorUsuarioCandidato.URL_PATTERN_AJAX %>" },
				"action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_MERITOS_CANDIDATO%>",
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"filterable": true,
				"defaultOrderBy": 0,
				"defaultOrderDirection": 'desc',
				"pageSize": 10,
				"title": "MERITOS",
				"pageSizeOptions": [10, 50, 100],
				"columns": [
					{'data': 'codNum', 'filter': {'type': 'number'}},
					{'data': 'item.bloque.apartado.nombre', 'filter': {'type': 'select', 'options': optionsApartados}},
					{'data': 'item', 'filter': true, 'render': function(row) {
						return row.item.bloque.apartado.codigo + "." + row.item.bloque.codigo + "." + row.item.codigo;
					}},
					{'data': 'item.nombre', 'filter': true},
					{'data': 'descripcion', 'filter': true},
					{'data': 'valor', 'filter': true, 'overflow': 'auto'},
					{'data': 'observacion', 'filter': true},
					{'data': 'codnum', 'buttons': [
						{'title': 'Descargar fichero del mérito', 'class': 'only-icon icon-download',  'onClick': function(row) {
							window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
									+ "?a=<%= ControladorDescargaFicheros.ACCION_DESCARGAR_MERITO_PERSONAL %>&<%= ControladorDescargaFicheros.PARAM_MERITO %>=" + row.codNum);
						}},
					]}
				]
			});
			
			Atis.smoothScrollToAnchor("#meritos");
	<%	} %>
	
	<%	if (bean.getApartadoTitulaciones() != null) { %>
			var tableTitulaciones = new Atis.DataTable('#tableTitulacionesCAN', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
				"title": 'TITULACIONES',
				"pageSize": 10,
				"action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_TITULACIONES_CANDIDATO%>",
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%=candidato.getCodNum()%>},
				"filterable": true,
				"defaultOrderBy": 0,
				"defaultOrderDirection": 'desc',
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
					{'data': 'codNum', 'buttons': [
						{'title': 'Descargar titulación', 'class': 'only-icon icon-download', 'onClick': function(row) {
							window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
									+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_TITULACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_TITULACION %>=" + row.codNum);
						}},
					]}
				],
			});
			
			Atis.smoothScrollToAnchor("#titulaciones");
	<%	} %>
	
	<%	if (bean.getApartadoAcreditaciones() != null) { %>
			var tableAcreditaciones = new Atis.DataTable('#tableAcreditacionesCAN', {
				"ajax": { url: "<%= ControladorUsuarioCandidato.URL_PATTERN_AJAX %>" },
				"pageSize": 10,
				"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO %>",
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"filterable": true,
				"defaultOrderBy": 0,
				"defaultOrderDirection": 'desc',
				"title": 'ACREDITACIONES: <%=candidato.getNombre() + " " + candidato.getPrimerApellido() + " " + candidato.getSegundoApellido()%>',
				"columns": [
					{'data': 'codNum', 'filter': {'type': 'number'}},
					{'data': 'codigo', 'filter': true, 'render': function(row) {
						return '<%= bean.getCodigoPadreMeritoPreferente() %>.' + row.meritoPreferente.codigo;
					}},
					{'data': 'meritoPreferente.nombre', 'order': {'active': false}, 'render': function(row) {
						var merito = row.meritoPreferente.nombre + (row.meritoPreferenteOpcion ? ' (' + row.meritoPreferenteOpcion.nombre + ')' : ''); 
						return merito + (row.descripcion ? '<br/>Descripción: ' + row.descripcion : '');
					}},
					{'data': 'borrado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Borrada', 'false': 'No borrada'}, 'optionDefault': 'false'}, 'render': function(row) {
						return row.borrado ? "<div title='Borrada' class='circle-false'></div>" : "";
					}},
					{'data': 'validado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Validada', 'false': 'No validada'}}, 'render': function(row) {
						return row.validado ? "<div title='Validada' class='circle-true'></div>" : "";
					}},
					{'data': 'activo', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Activada', 'false': 'Desactivada'}}, 'render': function(row) {
		        		if (row.activo) {
		        			return "<div title='Activa' class='circle-true'></div>"; 
		        		} else {
		        			return "<div title='Inactiva' class='circle-false'></div>";
		        		}
	        		}},
					{'data': 'codnum', 'buttons': [
						{'title': 'Descargar fichero acreditación', 'class': 'only-icon icon-download', 'onClick': function(row) {
							window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
									+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_ACREDITACION_PERSONAL + "&" + ControladorDescargaFicheros.PARAM_ACREDITACION %>=" + row.codNum);
						}},
					]}
				],
			});
			
			Atis.smoothScrollToAnchor("#acreditaciones");
	<%	} %>
	
	<%	if (bean.getApartadoAreasExcluidas() != null) { %>
			var tableAreasExcluidas = new Atis.DataTable('#tableAreasExcluidasCAN', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"selectable": true,
				"pageSize": 10,
				"filterable": true,
				"defaultOrderBy": 2,
				"defaultOrderDirection": 'desc',
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
		
			var tableAreas = new Atis.DataTable('#tableAreasCAN', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false},
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"selectable": true,
				"pageSize": 10,
				"filterable": true,
				"defaultOrderBy": 2,
				"defaultOrderDirection": 'asc',
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
		
			Atis.smoothScrollToAnchor("#areas_excluidas");
	<%	} %>
	
	<%	if (bean.getApartadoContrataciones() != null) { %>
			function cambiarEstado(bolsas) {
				var mensaje = "<p>Se cambiará el estado seleccionado en el candidato para las bolsas<br/>previamente seleccionadas.</p>"
				mensaje += "<br/>";
				mensaje += " <div class='form-group-container col1'>";
				mensaje += "     <div class='form-group-dialog'>";
				mensaje += "         <label class='bold-label' for='select_estado_candidato'>Estado: </label>";
				mensaje += "         <select id='select_estado_candidato' required>";
				
			<%	for (Entry<String, String> estado: ModeloEstadoCandidato.ESTADOS.entrySet()) { %>
					mensaje += "<option value='<%= estado.getKey() %>'><%= estado.getValue() %></option>";
			<%	} %>
				
				mensaje += "         </select>";
				mensaje += "     </div>";
				mensaje += " </div>";
				
				Atis.confirmDialog("Cambiar estado del candidato", mensaje, {
					'Si': function() {
						var selectEstado = this.querySelector('#select_estado_candidato');
						
						var params = {
								"<%= ControladorUsuarioCandidato.PARAM_ACCION %>": "<%= ControladorUsuarioCandidato.ACCION_CAMBIAR_ESTADO_CANDIDATO %>",
								"<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>": <%= bean.getCandidato().getCodNum() %>,
								"<%= ControladorUsuarioCandidato.PARAM_BOLSAS %>": Atis.object2Json(bolsas),
								"<%= ControladorUsuarioCandidato.PARAM_ESTADO %>": selectEstado.value
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				});
			}
			
			var estadosCandidato = {};
		<%	for (Entry<String, String> est: ModeloEstadoCandidato.ESTADOS.entrySet()) { %>
				estadosCandidato["<%= est.getKey() %>"] = "<%= est.getValue() %>";
		<%	} %>
			
			var tableEstadosCandidato = new Atis.DataTable('#tableEstadosCandidatoCAN', {
				"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
				"action": "<%= ControladorUsuarioCandidato.ACCION_DATATABLE_ESTADOS_CANDIDATO %>",
				"params": {"<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>": <%= candidato.getCodNum() %>},
				"selectable": true,
				"pageSize": 10,
				"filterable": true,
				"defaultOrderBy": 1,
				"defaultOrderDirection": 'asc',
				"title": 'LISTADO DE ESTADOS DEL CANDIDATO',
				"columns": [
					{'data': 'bolsa.codNum', 'selectable': true},
					{'data': 'bolsa.area.descripcion', 'filter': true},
					{'data': 'estado', 'render': function(row) {
						return row.codNumEstado != 0 ? row.estado : '<%= ModeloEstadoCandidato.ESTADO_DISPONIBLE %>';
					}, 'filter': {'type': 'select', 'options': estadosCandidato}},
					{'data': 'plaza' , 'render': function(row) {
						var linkPlaza = '';
						if (row.plaza != null) {
							linkPlaza = '<a class="bolsaempleo-link" ';
							linkPlaza += 'href="<%= ControladorContratacion.URL_PATTERN + "?" + ControladorContratacion.PARAM_ACCION + "=" + ControladorContratacion.ACCION_SELECCIONAR_PLAZA_OFERTADA + "&" + ControladorContratacion.PARAM_PLAZA_OFERTADA + "=" %>' + row.plaza.codNum + '"';
							linkPlaza += ' target="_blank" title="Ir a la plaza ' + row.plaza.codNum + '">' + row.plaza.codNum + '</a>';
						}
						return linkPlaza;
					}, 'filter': {'type': 'number'}}
				],
				"actions": [
					{'label': 'Cambiar estado', 'onClick': function(selected) {
							if (selected.length > 0) {
								cambiarEstado(selected);
							}
						}
					},
				]
			});
			
			Atis.smoothScrollToAnchor("#contrataciones");
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
				
				Atis.confirmDialog("Restaurar usuario", "¿Desea restaurar el usuario?", {
					Si: function() {
						Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_RECUPERAR_CANDIDATO%>',
							'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=candidato.getCodNum()%>'
						});
						$(this).dialog("close");
					},
					No: function() {
						$(this).dialog("close");
					}
				});
			});
	<%	} else { %>
			document.getElementById("usuario_borrar").addEventListener("click", function(event) {
				event.preventDefault();
				
				var message = "<h3>¿Desea dar de baja al candidato?</h3><br/>" +
						"<div class='form-group-dialog'>" +
						"	<label for='razon_borrado'>Razón borrado: </label>" +
						"	<textarea id='razon_borrado' name='razonborrado' rows='2' required='required'></textarea>" +
						"</div>";
				
				Atis.confirmDialog("Dar de baja candidato", message, {
					Si: function() {
						var inputRazon = this.querySelector('#razon_borrado');
						if (inputRazon.value != '') {
							Atis.sendForm("<%= request.getRequestURI() %>", {
								'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_ELIMINAR_CANDIDATO%>',
								'<%=ControladorUsuarioCandidato.PARAM_CANDIDATO%>': '<%=candidato.getCodNum()%>',
								'<%=ControladorUsuarioCandidato.PARAM_RAZON_BORRADO%>': inputRazon.value
							});
							$(this).dialog("close");
						} else {
							inputRazon.setCustomValidity("La razón de borrado no puede estar vacía");
							inputRazon.reportValidity();
						}
					},
					No: function() {
						$(this).dialog("close");
					}
				});
			});
			document.getElementById("usuario_actualizar_datos").addEventListener("click", function(event) {
				event.preventDefault();
				
				var message = "<h3>¿Desea actualizar los datos del candidato?</h3><br/>" +
						"<p>Se actualizarán los datos del candidato (<strong><%= usuario %></strong>)</p><br/>" +
						"<ul>" +
						"<li>- Tipo de documento de indentificación: <strong><%= bean.getUsuarioArcos().getDocumentoTipo() %></strong></li>" +
						"<li>- Documento de identificación: <strong><%= bean.getUsuarioArcos().getDocumentoNumero() %></strong></li>" +
						"<li>- Nombre y apellidos: <strong><%= bean.getUsuarioArcos().getNombre() %>, <%= bean.getUsuarioArcos().getApellido1() %>&nbsp;<%= bean.getUsuarioArcos().getApellido2() %></strong></li>" +
						"</ul>" +
						"</div>";
				
				Atis.confirmDialog("Actualizar datos candidato", message, {
					Si: function() {
						Atis.sendForm("<%= request.getRequestURI() %>", {
							'<%=ControladorUsuarioCandidato.PARAM_ACCION%>': '<%=ControladorUsuarioCandidato.ACCION_ACTUALIZAR_DATOS_PERSONALES%>',
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

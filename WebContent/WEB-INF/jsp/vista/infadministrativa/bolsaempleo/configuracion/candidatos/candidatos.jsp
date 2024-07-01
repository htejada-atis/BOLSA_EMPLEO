<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="java.util.Map.Entry" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaCandidatos bean = (VistaCandidatos) uvdatos.getVistas().get(VistaCandidatos.class.getName());
%>

<div class='bolsa-empleo usuarios'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Usuarios Candidatos</h2>
		<div>
			<button class="link-btn" id="darBaja">Dar de baja</button>
			<button class="link-btn" id="exportar">Exportar</button>
		</div>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableUsuariosCAN">
		<tr>
			<th scope="col" style="width:13%" title="Documento">Documento</th>
			<th scope="col" style="width:14%" title="Nombre de usuario" class="user">Usuario</th>
			<th scope="col" style="width:30%" title="Nombre y apellidos">Nombre y Apellidos</th>
			<th scope="col" style="width:10%" class="center" title="Lista del usuario">Lista</th>
			<th scope="col" style="width:10%" class="center" title="Excluido">Excluido</th>
			<th scope="col" style="width:10%" class="center" title="Eliminado">Eliminado</th>
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
	
	var table_usuarios = new Atis.DataTable('#tableUsuariosCAN', {
		"ajax": { url: "<%=ControladorUsuarioCandidato.URL_PATTERN_AJAX%>", async: false },
		"pageSize": 10,
		"filterable": true,
		"stateSave": true,
		"defaultOrderBy": 0,
		"defaultOrderDirection": 'asc',
		"clickable": {'onClick': function(row) {
			var params = {
					'a': '<%= ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO %>', 
					'<%= ControladorUsuarioCandidato.PARAM_CANDIDATO %>': row.codNum};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		}},
		"action": "<%=ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS_CANDIDATOS%>",
		"columns": [
			{'data': 'prsnif', 'filter': true, 'overflow': 'auto'},
			{'data': 'codcuenta', 'filter': true, 'overflow': 'auto'},
			{'data': 'apellido1', 'filter': true, 'order': false, 'overflow': 'auto', 'render': function(row) {
				return row.nombre + " " + row.apellido1 + " " + row.apellido2; 
			}},
			{'data': 'listaDist', 'filter': {'type': 'selectBoolean', 'true': 'En Lista', 'false': 'Sin Lista'}, 'render': function(row) {
				if(row.listaDist==true){
					return "<div title='En lista distribución' class='circle-true'></div>"; 
				} else {
					return "<div title='Excluido de lista distribución' class='circle-false'></div>"; 
				}
			}},
			{'data': 'excluido', 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'Incluido'}, 'optionDefault': 'false'}, 'render': function(row) {
				if(row.excluido==true){
					return "<div title='Candidato excluido' class='circle-true'></div>"; 
				}
			}},
			{'data': 'borrado', 'filter': {'type': 'select', 'options':{'true': 'Borrado', 'false': 'No Borrado'}, 'optionDefault': 'false'}, 'render': function(row) {
				if(row.borrado==true){
					return "<div title='Candidato eliminado' class='circle-true'></div>"; 
				}
			}},
		],
	});
	
	$('#exportar').on('click', function() {
		var mensaje = "<p>Exportar candidatos con solicitudes o no, en una convocatoria.<br/><br/>Seleccione la convocatoria.</p>"
			mensaje += "<br/>";
			mensaje += " <div class='form-group-container col1'>";
			mensaje += "     <div class='form-group-dialog'>";
			mensaje += "         <label class='bold-label' for='select_convocatorias'>Convocatoria: </label>";
			mensaje += "         <select id='select_convocatorias' required>";
			
		<% for (Convocatoria c : bean.getListaConvocatorias()) { %>
			mensaje += "<option value='<%= c.getCodNum() %>'><%= c.getDescripcion() %></option>";
		<%	} %>
		
		mensaje += "         </select>";
		mensaje += "     </div>";
		mensaje += " </div>";
		
		Atis.confirmDialog("Exportar candidatos", mensaje, {
			'Si': function() {
				var selectConvocatoria = this.querySelector('#select_convocatorias');
				
				window.open("<%= request.getRequestURI() + "?" 
					+ ControladorUsuarioCandidato.PARAM_ACCION + "=" + ControladorUsuarioCandidato.ACCION_EXPORTAR %>"
					+ "<%= "&" + ControladorUsuarioCandidato.PARAM_CONVOCATORIA + "=" %>" + selectConvocatoria.value);
				
				$(this).dialog("close");
			},
			'No': function() {
				$(this).dialog("close");
			}
		});
	});
	
	$('#darBaja').on('click', function() {
		var mensaje = "<p>Se darán de baja todos los candidatos que no tienen ninguna solicitud (ni abierta ni cerrada) en cualquier convocatoria.</p>";
			
		Atis.confirmDialog("Dar de baja candidatos", mensaje, {
			'Si': function() {
				var params = {'<%= ControladorUsuarioCandidato.PARAM_ACCION %>': '<%= ControladorUsuarioCandidato.ACCION_DAR_BAJA_CANDIDATOS %>'};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
				$(this).dialog("close");
			},
			'No': function() {
				$(this).dialog("close");
			}
		});
	});
	
}); 
</script>

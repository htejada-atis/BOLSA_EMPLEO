<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorBolsas"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaEstadoBolsas"%>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaEstadoBolsas bean = (VistaEstadoBolsas) uvdatos.getVistas().get(VistaEstadoBolsas.class.getName());
%>

<div class='bolsas'>
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError() %>
		</div>
	<% } else { %>
	<h2>Estado de las bolsas</h2>
	
	<table class="bluetable bolsaempleo" id="table">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:10%" title="Id de la convocatoria">Id</th>
			<th scope="col" style="width:25%" class="area">Area</th>
			<th scope="col" style="width:15%">Estado</th>
			<th scope="col" style="width:20%">Actualizada</th>
			<th scope="col" style="width:18%">Bloqueo</th>
			<th scope="col" style="width:19%">Desbloqueo</th>
			<th scope="col" style="width:18%">Baremable</th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="8" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>
	
<script>
$(document).ready(function() {
	var table = new DataTable('#table', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/bolsas" },
	    "selectable": true,
	    "pageSize": 10,
	    "action": "<%= ControladorBolsas.ACCION_DATATABLE %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'area.descripcion'},
	        {'data': 'estado'},
	        {'data': 'fechaActualizacion'},
	        {'data': 'fechaBloqueo'},
	        {'data': 'fechaDesBloqueo'},
	        {'data': 'baremable'},
	    ],
	    "actions": [
	    	{'label': 'Bloquear', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BLOQUEAR%>", selected); } },
	    	{'label': 'Revisión', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_REVISION%>", selected); } },
	    	{'label': 'Baremación', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BAREMACION%>", selected); } },
	    	{'label': 'Alegación', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_ALEGACION%>", selected); } },
	    	{'label': 'Desbloquear', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_DESBLOQUEAR%>", selected); } },
	    	{'label': 'Baremar', 'onClick': function(selected) { enviaAccion("<%=ControladorBolsas.ACCION_BOLSAS_BAREMAR%>", selected); } },
	    ]
	});	
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las bolsas', 'Seleccione al menos una bolsa para cambiar su estado.');
			return;
		}
		
		var params = {
			'a': '<%=ControladorBolsas.ACCION_BOLSA%>', 
			'<%=ControladorBolsas.PARAM_ACCION_BOLSA%>': accion, 
			'<%=ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS%>': Atis.object2Json(selected)
		};
   		Atis.sendForm("<%= request.getRequestURI() %>", params);
	}
}); 
</script>
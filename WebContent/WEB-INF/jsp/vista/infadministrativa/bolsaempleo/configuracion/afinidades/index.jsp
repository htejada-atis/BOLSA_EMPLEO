<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAfinidades"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaAfinidades"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaAfinidades bean = (VistaAfinidades) uvdatos.getVistas().get(VistaAfinidades.class.getName());
%>

<div class='bolsa-empleo afinidades'>
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
	
	<div class="titulo-bolsa-empleo" style="float:right; margin-top:1.2rem">
		<a class="link-btn" id="nueva_afinidad" href="<%= request.getRequestURI() %>" style="margin-top:0">Nueva Afinidad</a>
	</div>
	
	<div class="titulo-bolsa-empleo" style="margin-top:1rem;">
		<h2>Afinidades</h2>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableAfinidades">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:5%" title="Id de la afinidad">Id</th>
			<th scope="col" class="center codigo" style="width:10%" title="Código de afinidad">Código</th>
			<th scope="col" style="width:55%">Descripcion</th>
			<th scope="col" class="center" style="width:15%">Modulación</th>
			<th scope="col" class="center" style="width:15%">Borrado</th>
			<th scope="col" style="width:15%"></th>			
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="13" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	<% } %>
</div>
	
<script>
$(document).ready(function() {
	document.getElementById('nueva_afinidad').addEventListener("click", function(event) {
		event.preventDefault();
		Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorAfinidades.ACCION_FORMULARIO_AFINIDAD %>'});
	});
	
	
	var table = new Atis.DataTable('#tableAfinidades', {
	    "ajax": { url: "/srv/es/ajax/informacionadministrativa/bolsaempleo/configuracion/afinidades" },
	    "selectable": true,
	    "pageSize": 10,
	    "action": "<%= ControladorAfinidades.ACCION_DATATABLE %>",
	    "columns": [
	    	{'data': 'codNum', 'selectable': true},
	        {'data': 'codNum'},
	        {'data': 'codigo', 'class': 'center'},
	        {'data': 'descripcion'},
	        {'data': 'modulacion', 'class': 'center'},
        	{'data': 'sujetoAfinidad', 'order': {'active': false},
	    		'render': function(row) {
	        		if (row.sujetoAfinidad) {
	        			return "<div title='Afinidad' class='circle-true'></div>"; 
	        		}
        		}
	        },
        	{'data': 'codnum', 'buttons': [
        		{'label': 'Editar', 'onClick': function(row) {
        			var params = {'a': '<%= ControladorAfinidades.ACCION_MODIFICAR_AFINIDAD %>', '<%= ControladorAfinidades.PARAM_ID %>': row.codNum};
        			Atis.sendForm("<%=request.getRequestURI()%>", params);
        			}
        		},
        	]}
	    ],
	    "actions": [
	    	{'label': 'Borrar', 'onClick': function(selected) { enviaAccion("<%=ControladorAfinidades.ACCION_BORRAR_AFINIDAD%>", selected); } }   	
	    ]
	});	
	
	function enviaAccion(accion, selected) {
		if (selected.length == 0) {
			Atis.alertDialog('Estado de las afinidades', 'Seleccione al menos una afinidad.');
			return;
		}
		else {
			Atis.confirmDialog("Borrar afinidad", "¿Desea borrar esta afinidad de la base de datos?", {
            	Si: function(row) {
            		var params = {
            				'a': '<%=ControladorAfinidades.ACCION_BORRAR_AFINIDAD%>', 
            				'<%=ControladorAfinidades.PARAM_ID%>': row.codNum,
            				'<%=ControladorAfinidades.PARAM_AFINIDADES_SELECCIONADAS%>': Atis.object2Json(selected)
            			};
        			Atis.sendForm("<%= request.getRequestURI() %>", params);
              		$(this).dialog("close");
            	},
            	No: function() {
              		$(this).dialog("close");
            	}
          	});
		}
	}
}); 
</script>

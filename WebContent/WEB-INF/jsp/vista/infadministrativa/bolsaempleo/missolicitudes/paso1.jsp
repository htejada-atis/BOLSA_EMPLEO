<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modelo.bolsaempleo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">	
	<% if (bean.getMensajesDeExito().size() > 0) { %>
		<div id="exito" class="success">
			<%= bean.formatearMensajesDeExito() %>
		</div>
	<% } %>
	
	<% if (bean.getMensajesDeError().size() > 0) { %>
		<div id="error" class="error">
			<%= bean.formatearMensajesDeError()   %>
		</div>
	<% } %>
	
	<h2>Paso 1: Selección de áreas</h2>
	<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
	<p>Seleccione las áreas donde desee participar, hasta un máximo de [<%=bean.getSolicitud().getConvocatoria().getNumBolsasMaximo()%>].</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col"	style="width:15%">Código</th>
			<th scope="col"	style="width:60%">Nombre</th>
			<th scope="col" class="center" style="width:10%">Excluido</th>
			<th scope="col" class="center" style="width:10%">Baremable</th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="5" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
	
	<div class="btns-by-steps">
		<a class="link-btn" id="paso1_volver" href="<%= request.getRequestURI() %>">
	    	 Volver
	    </a>
	    <a class="link-btn" id="paso1_siguiente" href="<%= request.getRequestURI() %>">
	    	 Ir a 'Méritos por Área'
	    </a>
	   	<a class="link-btn" id="descargar_pdf" href="<%= request.getRequestURI() %>">
	    	 Descargar
	    </a>
	</div>
	
</div>

<script>

	$(document).ready(function() {
		
		var bolsasSolicitud = [];
		
		<%  if (bean.getListaBolsas() != null) {
				for (Bolsa bolsa: bean.getListaBolsas()) { %>
					bolsasSolicitud.push("<%=bolsa.getCodNum()%>");
				<% }
			} %>
		
		var tableAreas = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%=ControladorMisSolicitudes.URL_PATTERN_AJAX%>" },
		    "pageSize": 10,
		    "selectable": true,
		    "filterable": true,
		    "title": 'Lista de Áreas',
		    "action": "<%=ControladorMisSolicitudes.ACCION_DATATABLE_AREAS%>",
		    "selected": bolsasSolicitud,
		    "columns": [
		    	{'data': 'codNum', 'selectable': {'exclude': 'excluido'}},
		    	{'data': 'area.idAreaExterno', 'filter': true},
		    	{'data': 'area.descripcion', 'filter': true},
		        {'data': 'excluido', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Excluido', 'false': 'No excluido'}, 'optionDefault': 'false'},
		    		'render': function(row) {
		        		if (row.excluido) {
		        			return "<div title='No tienes acceso a éste área' class='circle-false'></div>";
		        		} else {
		        			return "<div class='circle-true'></div>";
		        		}
	        		}
		        },
		        {'data': 'baremable', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Baremable', 'false': 'No baremable'}, 'optionDefault': 'true'}, 'render': function(row) {
	        		if (row.baremable) {
	        			return "<div title='Baremable' class='circle-true'></div>"; 
	        		} else {
	        			return "<div title='No baremable' class='circle-false'></div>"; 
	        		}
	        	}},
	        ],
		});
		
		document.getElementById("paso1_volver").addEventListener("click", function(event) {
			
		});
		
		document.getElementById("paso1_siguiente").addEventListener("click", function(event) {
			event.preventDefault();
			if (tableAreas.getCheckedItems().length < 1) {
				Atis.alertDialog("Seleccionar áreas", "Debes seleccionar al menos un área para continuar");
			} else if (tableAreas.getCheckedItems().length > <%= bean.getSolicitud().getConvocatoria().getNumBolsasMaximo() %>) {
				Atis.alertDialog("Seleccionar áreas", "No puedes superar el número máximo de áreas permitido de la convocatoria");
			} else {
				var params = {
						'a': '<%= ControladorMisSolicitudes.ACCION_SELECCIONAR_BOLSAS %>',
						'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
						'<%= ControladorMisSolicitudes.PARAM_BOLSAS %>': JSON.stringify(tableAreas.getCheckedItems())
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			}
		});
		
		document.getElementById("descargar_pdf").addEventListener("click", function(event) {
			event.preventDefault();
			window.open("<%= request.getRequestURI() %>"
		        	+ "?a=<%= ControladorMisSolicitudes.ACCION_DESCARGAR_PDF %>&<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>=" + '<%= bean.getSolicitud().getCodNum() %>');			
		});
		
	});
	
</script>

<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<% if (bean.getSolicitud() != null) { %>
		
		<h2>Paso 1: Selección de áreas</h2>
		
		<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
		<p>Seleccione las áreas donde desee participar, hasta un máximo de [<%=bean.getSolicitud().getConvocatoria().getNumBolsasMaximo()%>]. 
		Pulse el botón añadir áreas seleccionadas al final de la página.</p>
		<p>A continuación pulse "Ir a 'Méritos por Área'"</p>
		
		<table class="bluetable bolsaempleo" id="tableAreasSeleccionadasMSO1">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col"	style="width:15%">Código</th>
				<th scope="col"	style="width:80%">Nombre</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="3" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
		<div class="btns-by-steps">
			<button class="link-btn" id="paso2_volver">
		    	 Volver a mis solicitudes
		    </button>
			<button class="link-btn" id="paso1_siguiente">
		    	 Ir a 'Méritos por Área'
		    </button>
		</div>
		
		<table class="bluetable bolsaempleo" id="tableAreasMSO1">
			<tr>
				<th scope="col" style="width:5%"></th>
				<th scope="col"	style="width:15%">Código</th>
				<th scope="col"	style="width:60%">Nombre</th>
				<th scope="col" class="center" style="width:10%">Excluido</th>
			</tr>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="4" style="width:100%"></th>
				</tr>
			</tfoot>
		</table>
		
		<div class="btns-by-steps">
			<div class="link-btn"></div>
			<button class="link-btn" id="addAreas">
		    	 Añadir áreas seleccionadas
		    </button>
		</div>
		
	<% } %>
</div>

<% if (bean.getSolicitud() != null) { %>
	<script>
		$(document).ready(function() {
			var quitarBolsasSeleccionadas = function(selected) {
				if (selected.length > 0) {
					var params = {
						'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_DESELECCIONAR_BOLSAS %>',
						'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
						'<%= ControladorMisSolicitudes.PARAM_BOLSAS %>': JSON.stringify(selected)
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				}
			};
			
			var tableAreasSeleccionadas = new Atis.DataTable('#tableAreasSeleccionadasMSO1', {
				"ajax": { url: "<%=ControladorMisSolicitudes.URL_PATTERN_AJAX%>", async: false },
			    "selectable": true,
			    "filterable": true,
			    "stateSave": true,
			    "title": 'LISTA DE ÁREAS SELECCIONADAS',
			    "action": "<%=ControladorMisSolicitudes.ACCION_DATATABLE_AREAS_SELECCIONADAS%>",
			    "params": {"<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>": "<%= bean.getSolicitud().getCodNum() %>"},
			    "columns": [
			    	{'data': 'codNum', 'selectable': {'exclude': 'excluido'}},
			    	{'data': 'area.idAreaExterno', 'filter': true},
			    	{'data': 'area.descripcion', 'filter': true},
		        ],
		        "actions": [
			    	{'label': 'Eliminar', 'onClick': quitarBolsasSeleccionadas },
			    ]
			});
			
			var checkAreasSeleccionadas = function (selected) {
				if (selected.length == 0) {
					Atis.alertDialog("Seleccionar áreas", "Seleccione al menos un área");
					return false;
				}
				
				if (selected.length > <%= bean.getSolicitud().getConvocatoria().getNumBolsasMaximo() %>) {
					var message = "No puedes superar el número máximo de áreas permitido de la convocatoria. <br/>"
						+ "Máximo perimitido: <%= bean.getSolicitud().getConvocatoria().getNumBolsasMaximo() %>. <br/>"
						+ "Áreas seleccionadas: " + selected.length + ".";
					Atis.alertDialog("Seleccionar áreas", message);
					return false;
				}
				
				return true;
			}
			
			var addAreas = function(selected) {
				if (checkAreasSeleccionadas(selected)) {
					var params = {
							'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_SELECCIONAR_BOLSAS %>',
							'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
							'<%= ControladorMisSolicitudes.PARAM_BOLSAS %>': JSON.stringify(selected)
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);	
				}
			}
			
			var bolsasSolicitud = [];
			
			<%  if (bean.getListaBolsas() != null) {
				for (Bolsa bolsa: bean.getListaBolsas()) { %>
					bolsasSolicitud.push(<%=bolsa.getCodNum()%>);
				<% }
			} %>
			
			var tableAreas = new Atis.DataTable('#tableAreasMSO1', {
				"ajax": { url: "<%=ControladorMisSolicitudes.URL_PATTERN_AJAX%>", async: false },
				"pageSize": 200,
			    "pageSizeOptions": [10, 100, 200],
			    "selectable": true,
			    "filterable": true,
			    "stateSave": true,
			    "title": 'ÁREAS DISPONIBLES',
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
		        ],
		        "actions": [
			    	{'label': 'Añadir áreas seleccionadas', 'onClick': addAreas },	    	
			    ]
			});
			
			$('#addAreas').click(function() {
				addAreas(tableAreas.getCheckedItems());
			})
			
			document.getElementById("paso2_volver").addEventListener("click", function(event) {
				event.preventDefault();
				var params = {
					'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_INDEX %>'
				};
				Atis.sendForm("<%= request.getRequestURI() %>", params);
			});
					
			document.getElementById("paso1_siguiente").addEventListener("click", function(event) {
				event.preventDefault();
				if (checkAreasSeleccionadas(tableAreas.getCheckedItems())) {
					var params = {
						'<%= ControladorMisSolicitudes.PARAM_ACCION %>': '<%= ControladorMisSolicitudes.ACCION_IR_A_MERITOS_POR_AREA %>',
						'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
					};
					Atis.sendForm("<%= request.getRequestURI() %>", params);
				}
			});
		});
		
	</script>
<% } %>

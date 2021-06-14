<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisMeritosPreferentes"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMeritosPreferentesCandidato"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>

<% 
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMeritosPreferentesCandidato bean = (VistaMeritosPreferentesCandidato) uvdatos.getVistas().get(VistaMeritosPreferentesCandidato.class.getName());
%>

<div class="bolsa-empleo">
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<div class="titulo-bolsa-empleo">
		<h2>Mis Acreditaciones</h2>
    
	    <button class="link-btn" id="nuevo_merito">
	    	 Nueva acreditación
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableMeritos">
		<tr>
			<th scope="col" style="width:5%"></th>
			<th scope="col" style="width:10%">Id</th>
			<th scope="col" style="width:8%">Código</th>
			<th scope="col"	style="width:40%">Nombre</th>
			<th scope="col"	style="width:15%">Descripción</th>
			<th scope="col" style="width:10%">Validada</th>
			<th scope="col"	style="width:12%"></th>
		</tr>
		<tbody>		
		</tbody>
		<tfoot>
			<tr>
				<th colspan="7" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
</div>

<script>

	$(document).ready(function() {
		
		document.getElementById("nuevo_merito").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.sendForm("<%= request.getRequestURI() %>", {'a': '<%= ControladorMisMeritosPreferentes.ACCION_AGREGAR_MERITO %>'});
		});
		
		var table = new Atis.DataTable('#tableMeritos', {
		    "ajax": { url: "<%= ControladorMisMeritosPreferentes.URL_PATTERN_AJAX %>" },
		    "pageSize": 10,
		    "action": "<%= ControladorMisMeritosPreferentes.ACCION_DATATABLE %>",
		    "selectable": true,
		    "filterable": true,
		    "columns": [
		    	{'data': 'codNum', 'selectable': true},
		    	{'data': 'codNum', 'filter': {'type': 'number'}},
		    	{'data': 'codigo', 'filter': true, 'render': function(row) {
		    		return '<%= bean.getCodigoPadreMeritoPreferente() %>.' + row.meritoPreferente.codigo;
		    	}},
		    	{'data': 'meritoPreferente.nombre', 'filter': true, 'render': function(row) {
		    		return row.meritoPreferente.nombre + (row.meritoPreferenteOpcion ? ' (' + row.meritoPreferenteOpcion.nombre + ')' : '');
		    	}},
		    	{'data': 'descripcion', 'filter': true},
		    	{'data': 'validado', 'order': {'active': false}, 'filter': {'type': 'select', 'options':{'true': 'Validada', 'false': 'Pendiente'}},
		    		'render': function(row) {
		        		if (row.validado) {
		        			return "<div title='Validada' class='circle-true'></div>"; 
		        		}
	        		}
		        },
		        {'data': 'codnum', 'buttons': [
	        		{'label': 'Descargar', 'title': 'Descargar fichero del mérito', 'onClick': function(row) {
	        			window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS %>"
	        		        	+ "<%= "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_ACREDITACION_CANDIDATO + "&" + ControladorDescargaFicheros.PARAM_ACREDITACION %>=" + row.codNum);
	        		}},
	   			]}
		    ],
		    "actions": [
		    	{'label': 'Eliminar', 'title': 'Eliminar méritos seleccionados', 'onClick': function(selected) {
		    		if (selected.length == 0) {
		    			Atis.alertDialog('Borrado de acreditaciones', 'Seleccione al menos una acreditación.');
		    			return;
		    		}
		    		if(selected.length) {
		    			var mensaje = "¿Desea borrar el mérito seleccionado?";
			        	var titulo = "Borrar mérito";
			        	
			        	if(selected.length > 1) {
			        		mensaje = "¿Desea borrar los méritos seleccionados?";
				        	titulo = "Borrar méritos";
			        	}
			        	
		    			Atis.confirmDialog(titulo, mensaje, {
					        Si: function() {
					        	var params = {
					    				'<%=ControladorMisMeritosPreferentes.PARAM_ACCION%>': '<%=ControladorMisMeritosPreferentes.ACCION_ELIMINAR_MERITOS%>', 
					    				'<%=ControladorMisMeritosPreferentes.PARAM_MERITOS%>': JSON.stringify(selected)};
				        		Atis.sendForm("<%=request.getRequestURI()%>", params);
					          	$(this).dialog("close");
					        },
					        No: function() {
					          	$(this).dialog("close");
					        }
					    });
		    			
		    		}
		    	}},
		    ]
		});
		
	});


</script>

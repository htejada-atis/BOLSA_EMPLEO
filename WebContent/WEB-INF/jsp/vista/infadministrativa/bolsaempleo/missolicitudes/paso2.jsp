<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaSolicitudes" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaSolicitudes bean = (VistaSolicitudes) uvdatos.getVistas().get(VistaSolicitudes.class.getName());
%>

<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Paso 2: Asignación de méritos a áreas</h2>
	<h3><%= bean.getSolicitud().getConvocatoria().getDescripcion() %></h3>
	
	<p>Para cada área seleccione hasta un máximo de [<%= bean.getSolicitud().getConvocatoria().getNumMeritosPorBloque() %>] méritos por bloque. Pinche sobre una área para asignar sus méritos.</p>
	
	<table class="bluetable bolsaempleo" id="tableAreas">
		<tr>
			<th scope="col"	style="width:15%">Código</th>
			<th scope="col"	style="width:65%">Nombre</th>
			<th scope="col"	style="width:20%">Nº méritos en área</th>
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
	    	 Volver
	    </button>
	    <button class="link-btn" id="paso2_siguiente">
	    	 Ir a 'Confirmar Solicitud'
	    </button>
	</div>
	
	<table class="bluetable bolsaempleo" id="tableMeritos" style="visibility: collapse">
		<tr>
			<th scope="col"	style="width:5%"></th>
			<th scope="col"	style="width:10%">Cod.</th>
			<th scope="col"	style="width:10%">Código item</th>
			<th scope="col"	style="width:40%">Mérito</th>
			<th scope="col"	style="width:10%">Valor</th>
			<th scope="col"	style="width:15%">Afinidad</th>
			<th scope="col" class="center" style="width:10%">Excluido</th>
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
	var afinidades = {};
	<% for (String tipo : bean.getListaTipoAfinidades()) { %>
		afinidades['<%= EscapaHTML.escapa(tipo) %>'] = [<%=
		    bean.getListaAfinidades()
			.stream()
			.filter(a -> a.getCodigo().equals(tipo))
			.map(a -> "{'id': " + a.getCodNum() + ", 'name': '" + EscapaHTML.escapa(a.getCodigoDescripcion()) + "'}")
			.collect(Collectors.joining(","))			
		%>];
	<% } %>
	
	function renderIndividualizado(row) {
		var valoracion = row.valoraciones[0];
		var texto = "<div>";
		
		if (valoracion) {
			texto += '<b title="' + valoracion.afinidad.descripcion + '">' + valoracion.afinidad.codigo + " " + valoracion.afinidad.modulacion * 100 + " %</strong><br/>"; 
		} else {
			texto += 'Selecciona afinidad<br/>';
		}
		
		texto += '<button class="pointer" title="Seleccionar afinidad">Afinidad</button></span>';
		texto += '</div>';
		
		var nodo = $(texto);
				
		$('button', nodo).on('click', function() {
			var options = "";
			
			for(var i=0; i<afinidades[row.merito.item.afinidad].length; i++) {
				var a = afinidades[row.merito.item.afinidad][i];
				options += '<option value="' + a.id + '">' + Atis.escapeHtml(a.name) + '</option>';
			}
			
			var message = 
				'<h3>Selecciona la afinidad para el mérito</h3><br/>' +
				'<p>Mérito: ' + Atis.escapeHtml(row.merito.item.nombre) + '</p>' +
				(row.merito.descripcion ? "<p>Descripción: " + Atis.escapeHtml(row.merito.descripcion) + "</p>" : "") +
				(row.merito.observacion ? "<p>Observaciones: " + Atis.escapeHtml(row.merito.observacion) + "</p>" : "") +
				'<p>Valor: ' + row.merito.valor + '</p>' +
				'<br/><p>Area: <strong><%= bean.getArea() != null ? bean.getArea().getArea().getDescripcion() : "" %></strong></p>' +
				'<p>Tipo de mérito: <strong>individualizado</strong></p><br/>' +
				'<p>Selecciona el grado de afinidad del mérito con el area:</p>' +
				'<p><select>' + options + '</select></p>'
			;
			
			// enviamos afinidad
			Atis.alertDialog("Seleccionar afinidad mérito individualizado", $(message), function(dialog) {
				var option = $('select option:selected', dialog);
				
				var params = {
    				'a': '<%=ControladorMisSolicitudes.ACCION_MERITO_AFINIDAD_INDIVIDUALIZADO%>',
    				'<%=ControladorMisSolicitudes.PARAM_BOLSA%>': '<%=bean.getArea() != null ? bean.getArea().getCodNum() : ""%>',
    				'<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>': '<%=bean.getSolicitud().getCodNum()%>',
    				'<%=ControladorMisSolicitudes.PARAM_MERITO_ID%>': row.merito.codNum,
    				'<%=ControladorMisSolicitudes.PARAM_AFINIDAD_ID%>': $(option).val(), 
    			};
    			
    			Atis.sendForm("<%=request.getRequestURI()%>", params);				
			});
		});
		
		return nodo;
	}
	
	function renderNoIndividualizado(row) {
		var texto = "<div>";
		
		if (row.valoraciones && row.valoraciones.length > 0) {
			for(var i=0; i<row.valoraciones.length; i++) {
				var valoracion = row.valoraciones[i];
				texto += '<b title="' + valoracion.afinidad.descripcion + '">' + valoracion.valor + ' - ' + valoracion.afinidad.codigo + " " + valoracion.afinidad.modulacion * 100 + ' %</strong><br/>';
			}
		} else {
			texto += 'Selecciona afinidad<br/>';
		}
		
		texto += '<button class="pointer" title="Seleccionar afinidad">Afinidad</button></span>';
		texto += '</div>';
		
		var nodo = $(texto);
		
		$('button', nodo).on('click', function() {
			var message = 
				'<h3>Selecciona la afinidad para el mérito</h3><br/>' +
				'<p>Mérito: ' + Atis.escapeHtml(row.merito.item.nombre) + '</p>' +
				(row.merito.descripcion ? "<p>Descripción: " + Atis.escapeHtml(row.merito.descripcion) + "</p>" : "") +
				(row.merito.observacion ? "<p>Observaciones: " + Atis.escapeHtml(row.merito.observacion) + "</p>" : "") +
				'<p>Valor: ' + row.merito.valor + '</p>' +
				'<br/><p>Area: <strong><%=bean.getArea() != null ? bean.getArea().getArea().getDescripcion() : ""%></strong></p>' +
				'<p>Tipo de mérito: <strong>no individualizado</strong></p><br/>' +
				'<p>Introduce el valor para cada grado de afinidad con el área (la suma de todas debe ' + row.merito.valor + ')</p>'
			;
			
			message += '<table style="margin-top: 10px;">';
			
			for(var i=0; i<afinidades[row.merito.item.afinidad].length; i++) {
				var a = afinidades[row.merito.item.afinidad][i];
				message += '<tr><td style="padding-right: 10px;"><p>' + a.name + '</p></td><td><input data-afinidad="' + a.id + '" class="afinidad" type="number" min="0" value="0" step="0.01" lang="es-ES"/></td></tr>';
			}
			
			message += '<tr><td style="padding-top: 10px; padding-right: 10px;"><p>TOTAL</p></td><td><input name="total" readonly value="0" class="total" type="number" lang="es-ES"/></td></tr>';
			message += '</table>'
			message += '<br/><p class="mensaje" style="display:none; color:red"></p>';
			
			var node = $(message);
			
			var getValoresAfinidades = function() {
				var afinidades = {};
				$('input.afinidad', node).each(function(index, input) {
					var afinidad = $(input).data('afinidad');
					var value = parseFloat($(input).val());
					afinidades[afinidad] = value;					
				});
				return afinidades;
			};
			
			var sumaTotal = function() {
				var total = 0.0;
				var afinidades = getValoresAfinidades();				
				Object.keys(afinidades).forEach(function(key) {
					total += afinidades[key];
				});				
				return total;
			};
			
			var actualizaTotal = function() {
				var total = Atis.redondearFloat(sumaTotal());
				var merito = Atis.redondearFloat(row.merito.valor);
				
				$('input.total').val(total);
				
				if (total > merito) {
					$('p.mensaje').text('El total es superior al valor del mérito. Actual ' + total + ' Máximo ' + row.merito.valor);
					$('p.mensaje').show();
				} else {
					$('p.mensaje').hide();
				}
				return total;
			};
						
			$('input.afinidad', node).on('change', function() {
				var value = parseFloat($(this).val());
				if (value < 0) {
					$(this).val('0');
				}
				actualizaTotal(); 
			});
						
			Atis.alertDialog("Seleccionar afinidad mérito no individualizado", node, function(dialog) {
				var option = $('select option:selected', dialog);
				var total = actualizaTotal();
				var merito = Atis.redondearFloat(row.merito.valor);
												
				if (total != merito) {
					$('p.mensaje').text('El total debe ser igual al valor del mérito. Actual ' + total + ' Valor de mérito ' + merito);
					$('p.mensaje').show();
					return false;
				}
				
				$('p.mensaje').hide();
				
				// enviamos afinidad
				var params = {
    				'<%=ControladorMisSolicitudes.PARAM_ACCION%>': '<%=ControladorMisSolicitudes.ACCION_MERITO_AFINIDAD_NOINDIVIDUALIZADO%>',
    				'<%=ControladorMisSolicitudes.PARAM_BOLSA %>': '<%= bean.getArea() != null ? bean.getArea().getCodNum() : "" %>',
    				'<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
    				'<%=ControladorMisSolicitudes.PARAM_MERITO_ID%>': row.merito.codNum,
    				'<%=ControladorMisSolicitudes.PARAM_AFINIDADES%>': Atis.object2Json(getValoresAfinidades()), 
    			};    			
    			Atis.sendForm("<%=request.getRequestURI()%>", params);				
			
				return true;
			});
		});
		
		return nodo;
	}

	$(document).ready(function() {
		
		
		var tableAreas = new Atis.DataTable('#tableAreas', {
			"ajax": { url: "<%=ControladorMisSolicitudes.URL_PATTERN_AJAX%>", async: false },
		    "pageSize": 10,
		    "filterable": true,
		    "clickable": {'onClick': function(row) {
		    	var params = {
	    				'<%=ControladorMisSolicitudes.PARAM_ACCION%>': '<%=ControladorMisSolicitudes.ACCION_BOLSA_SELECCIONADA%>',
	    				'<%=ControladorMisSolicitudes.PARAM_BOLSA%>': row.codNum,
	    				'<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>': <%=bean.getSolicitud().getCodNum()%>};
        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    }},
		    "selected": <%=bean.getArea() != null ? bean.getArea().getCodNum() : "null"%>,		    
		    "title": 'MIS ÁREAS PARA ESTA CONVOCATORIA',
		    "action": '<%=ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS%>',
		    "params": {'<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>': <%=bean.getSolicitud().getCodNum()%>},
		    "columns": [
		    	{'data': 'area.idAreaExterno', 'filter': true},
		    	{'data': 'area.descripcion', 'filter': true},
		    	{'data': 'numeroMeritos', 'order': {'active': false}},
	        ],
		});
		
		<%if (bean.getArea() != null) {%>
		
			var meritosBolsaSolicitud = [];
		
			<%if (bean.getListaMeritosSolicitud() != null) {
				for (MeritoSolicitud merito: bean.getListaMeritosSolicitud()) {%>
					meritosBolsaSolicitud.push(<%=merito.getMerito().getCodNum()%>);
				<%}
			}%>
				
			var tableMeritos = new Atis.DataTable('#tableMeritos', {
				"ajax": { url: "<%=ControladorMisSolicitudes.URL_PATTERN_AJAX%>", async: false },
			    "pageSize": 10,
			    "selectable": {'all': false},
			    "filterable": true,
			    "title": 'MIS MÉRITOS: <%=bean.getArea().getArea().getDescripcion()%>',
			    "selected": meritosBolsaSolicitud,
			    "defaultOrderBy": 1,
			    "action": "<%=ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA%>",
			    "params": {
			    	"<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>": <%=bean.getSolicitud().getCodNum()%>,
			    	"<%=ControladorMisSolicitudes.PARAM_BOLSA%>": <%=bean.getArea().getCodNum()%>
			    },
			    "columns": [
			    	{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
			    				var accion = checkbox.checked ? '<%=ControladorMisSolicitudes.ACCION_MERITO_SELECCIONADO%>'
			    						: '<%=ControladorMisSolicitudes.ACCION_MERITO_DESELECCIONADO%>';
				    			var params = {
				    				'<%=ControladorMisSolicitudes.PARAM_ACCION%>': accion,
				    				'<%=ControladorMisSolicitudes.PARAM_BOLSA%>': '<%=bean.getArea().getCodNum()%>',
				    				'<%=ControladorMisSolicitudes.PARAM_SOLICITUD_ID%>': '<%=bean.getSolicitud().getCodNum()%>',
				    				'<%=ControladorMisSolicitudes.PARAM_MERITO_ID%>': row.merito.codNum,
				    			};
				    			
				    			Atis.sendForm("<%= request.getRequestURI() %>", params);
				    		}
			    		}
			    	},
			    	{'data': 'codNum', 'filter': {'type': 'number'}},
			    	{'data': 'merito.item', 'filter': true, 'render': function(row) {
			        	return row.merito.item.bloque.apartado.codigo + "." + row.merito.item.bloque.codigo + "." + row.merito.item.codigo;
		        	}},
		        	{'data': 'merito.item.nombre', 'filter': true, 'render': function(row) {
			        	return row.merito.item.nombre + "<br/> Descripción del usuario: " + row.merito.descripcion;
		        	}},
		        	{'data': 'merito.valor', 'filter': true},
		        	{'data': 'codNum', 'filter': false, 'render': function(row) {
		        		if (!row.codNum) { 
		        			return '';
		        		}
		        		
		        		if (row.meritoSolicitud && row.merito.item.afinidad) {
		        			if (row.merito.item.individualizado) {
		        				return renderIndividualizado(row);
		        			} else {
		        				return renderNoIndividualizado(row);
		        			}
		        		} else {
		        			return '<span title="El mérito no tiene afinidad con el Área seleccionada"></span>';
		        		}		        				        	
		        	}},
		        	{'data': 'excluido', 'order': false, 'filter': false, 'render': function(row) {
		        		if (row.excluido) {
		        			return "<div title='Mérito excluido' class='circle-false'></div>";
		        		} else {
		        			return "<div title='Mérito no excluido' class='circle-true'></div>";
		        		}
		        	}},
		        ],
			});
			
			document.getElementById("tableMeritos").style.visibility = "visible";
			Atis.smoothScrollToAnchor("#tableMeritos");
		<% } %>
		
		document.getElementById("paso2_volver").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%= ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD %>',
					'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>',
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
		document.getElementById("paso2_siguiente").addEventListener("click", function(event) {
			event.preventDefault();
			var params = {
					'a': '<%= ControladorMisSolicitudes.ACCION_RESUMEN_SOLICITUD %>',
					'<%= ControladorMisSolicitudes.PARAM_SOLICITUD_ID %>': '<%= bean.getSolicitud().getCodNum() %>'
			};
			Atis.sendForm("<%= request.getRequestURI() %>", params);
		});
		
	});
	
</script>

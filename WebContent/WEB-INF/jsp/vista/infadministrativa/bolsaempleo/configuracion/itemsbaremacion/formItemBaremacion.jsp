<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaItemsBaremacion"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems" %>

<%
UVDatos uvdatos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaItemsBaremacion bean = (VistaItemsBaremacion) uvdatos.getVistas().get(VistaItemsBaremacion.class.getName());

ApartadoBaremacion apartado = bean.getApartadoBaremacion();
BloqueBaremacion bloque = bean.getBloqueBaremacion();
ItemBaremacion item = bean.getItemBaremacion();
String codigoCompleto = apartado.getCodigo() + "." + bloque.getCodigo() + "." + (item != null ? item.getCodigo() : bean.getUltimoCodigo());
String descripcion = "";
if(item!=null){
	descripcion = item.getDescripcion();
	if (descripcion==null) {
		descripcion = "";
	}
}
%>

<div class="bolsa-empleo">

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2><%=item != null ? "Editar item" : "Nuevo item"%></h2>
	
	<p>Las etiquetas en <b>negrita</b> corresponden a campos de relleno obligatorio</p>
	
    <form id="actualizar_item" class="be-form" method="post" action="<%=request.getRequestURI()%>">
    	<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_ACCION%>" id="accion_formulario"
    		   value="<%=item != null ? ControladorItemsBaremacion.ACCION_EDITAR_ITEM_CONFIRM : ControladorItemsBaremacion.ACCION_AGREGAR_ITEM_CONFIRM%>" /> 
		<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_APARTADO%>" id="apartado_id" value="<%=apartado.getCodNum()%>" />			  
		<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_BLOQUE%>" id="bloque_id" value="<%=bloque.getCodNum()%>" />
		<input type="hidden" name="<%=ControladorItemsBaremacion.PARAM_ITEM%>" id="item_id" value="<%=item != null ? item.getCodNum() : ""%>" />
		
		<div class="form-group-container col1">
			<div class="form-group">
	    		<label for="bloque_apartado_codigo">Código completo</label>
	    		<input class="form-input-custom" type="text" name="<%=ControladorItemsBaremacion.PARAM_BLOQUE_CODIGO%>" id="item_bloque_codigo" value="<%=codigoCompleto%>" disabled/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_codigo" class="bold-label">Código</label>
	    		<input class="form-input-custom" type="text" name="<%=ControladorItemsBaremacion.PARAM_ITEM_CODIGO%>" id="bloque_codigo" 
	    			   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_CODIGO, item != null ? item.getCodigo() : bean.getUltimoCodigo())%>"
	    			   required/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_nombre" class="bold-label">Nombre</label>
	    		<input class="form-input-custom" type="text" name="<%=ControladorItemsBaremacion.PARAM_ITEM_NOMBRE%>" id="bloque_nombre" 
	    			   value="<%=BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_NOMBRE, item != null ? item.getNombre() : "")%>"
	    			   required/>
	    	</div>
		</div>
		
		<div class="form-group-container col1">
			<div class="form-group">
    			<label for="descripcion">Descripción</label>
    			<textarea class="params form-input-custom" id="descripcion" name="<%=ControladorItemsBaremacion.PARAM_ITEM_DESCRIPCION%>" rows="3" cols="60"><%=BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_DESCRIPCION, item != null ? descripcion : "")%></textarea>
   			</div>
		</div>
   		
   		
		<div class="form-group-container col2">
			<div class="form-group">
				<%
				String unidadesSelected = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_UNIDADES, item != null ? item.getUnidades() : ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO);
				%>
	    		<label for="item_unidades" class="bold-label">Unidades</label>
	    		<select class="params" id="item_unidades" name="<%=ControladorItemsBaremacion.PARAM_ITEM_UNIDADES%>" style="width:100%;"required>
					<option value="<%=ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO%>" <%=unidadesSelected.equals(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO) ? "selected=\"selected\"" : ""%>>Entero</option>
					<option value="<%=ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_DECIMAL%>" <%=unidadesSelected.equals(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_DECIMAL) ? "selected=\"selected\"" : ""%>>Decimal</option>
					<option value="<%=ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO%>" <%=unidadesSelected.equals(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO) ? "selected=\"selected\"" : ""%>>Si ó No</option>				
				</select>				
	    	</div>
	    	<div class="form-group">
	    		<label for="item_valor" class="bold-label">Valor unitario</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR %>" id="item_valor" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR, item != null ? item.getValor().toString() : "") %>"
	    			   required/>
	    	</div>	  	    	
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<label for="bloque_valor_minimo" class="bold-label">Valor mínimo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR_MINIMO %>" id="bloque_valor_minimo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR_MINIMO, item != null ? item.getValorMinimo().toString() : "0.1") %>"
	    			   required/>
	    	</div>
	    	<div class="form-group">
	    		<label for="bloque_valor_maximo" class="bold-label">Valor máximo</label>
	    		<input class="form-input-custom" type="text" name="<%= ControladorItemsBaremacion.PARAM_ITEM_VALOR_MAXIMO %>" id="bloque_valor_maximo" 
	    			   value="<%= BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_VALOR_MAXIMO, item != null ? item.getValorMaximo().toString() : "1000") %>"
	    			   required/>
	    	</div>
		</div>
		<div class="form-group-container col2">
			<div class="form-group">
	    		<%
					String afinidadSelected = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_AFINIDAD, item != null && item.getAfinidad() != null ? item.getAfinidad() : "N");
				%>
	    		<label for="item_afinida" class="bold-label">Afinidad</label>	    		
	    		<select class="params" id="item_afinida" name="<%= ControladorItemsBaremacion.PARAM_ITEM_AFINIDAD %>" style="width:100%;" required>
					<option value="N" <%= afinidadSelected.equals("N") ? "selected=\"selected\"" : "" %>>N</option>
					<% for (String afinidad: bean.getAfinidades()) { %>
						<option value="<%=afinidad%>" <%= afinidadSelected.equals(afinidad) ? "selected=\"selected\"" : "" %>><%=afinidad%></option>					
					<% } %>
				</select>
	    	</div>
	    	<div class="form-check" id="individualizadoBloque" style="<%= afinidadSelected.equals("N") ? "display:none;" : "" %> ">
	    		<%
					String individualizado = BolsaEmpleoUtils.getParamForm(request, ControladorItemsBaremacion.PARAM_ITEM_INDIVIDUALIZADO, item != null ? item.getIndividualizado() ? "S" : "N" : "S");
				%>
    			<label for="individualizado">
    			<input class="params" type="checkbox" id="individualizado" name="<%= ControladorItemsBaremacion.PARAM_ITEM_INDIVIDUALIZADO %>" 
    				value="S" <%= individualizado.equals("S") ? "checked=''" : "" %>/>Individualizado</label>
    		</div>
		</div>
		<div class="form-btn">
    		<input id="item_enviar" type="submit" name="<%= ControladorItemsBaremacion.PARAM_ENVIAR %>" 
    			   value="<%= item != null ? "Guardar cambios" : "Insertar item" %>"/>
    	</div>    	
    </form>
    
	<table class="bluetable bolsaempleo custom" id="tableMeritosExcluyentes">
		<tr>
			<th scope="col" style="width:10%" title="Id"></th>
			<th scope="col" style="width:20%" title="ID ítem">Id</th>
			<th scope="col" style="width:20%" title="Código ítem">Código</th>
			<th scope="col" style="width:60%" title="Nombre del ítem">Nombre del ítem</th>
			<th scope="col" style="width:15%">Activo</th>
		</tr>
		<tbody>				
		</tbody>
		<tfoot>
			<tr>
				<th colSpan="15" style="width:100%"></th>
			</tr>
		</tfoot>
	</table>
    
</div>

<script>
	$(document).ready(function() {
		
		document.getElementById("bloque_codigo").addEventListener("input", function(event) {
			document.getElementById("item_bloque_codigo").value = '<%= apartado.getCodigo() %>.<%= bloque.getCodigo() %>.' + this.value;
		});
		
		$('#item_afinida').on('change', function() {
			console.log("items", $(this).val());
			
			if ($(this).val() == 'N') {
				$('#individualizadoBloque').hide();
			} else {
				$('#individualizadoBloque').show();
			}
		});
		
		var itemsExcluyentes = [];
		
		<%if (bean.getListaItemsExcluyentes() != null) {
			for (ItemBaremacion it: bean.getListaItemsExcluyentes()) {%>
				itemsExcluyentes.push(<%=it.getCodNum()%>);
			<%}
		}%>
		
		var tableItems = new Atis.DataTable('#tableMeritosExcluyentes', {
		    "ajax": { url: "<%= ControladorItemsBaremacion.URL_PATTERN_AJAX %>", async: false },
		    "params": {"<%=ControladorItemsBaremacion.PARAM_ITEM%>": <%= item.getCodNum() %>},
		    "pageSize": 10,
		    "title": "Items excluyentes:",
		    "filterable": true,
		    "selectable": true,
	    	"defaultOrderBy": 1,
	    	"defaultOrderDirection": 'asc',
		    "selected": itemsExcluyentes,
		    "action": "<%=ControladorItemsBaremacion.ACCION_DATATABLE_ITEMS_EXCLUYENTES%>",
		    "columns": [
		    	{'data': 'codNum', 'selectable': {'onChange': function(row, checkbox) {
    					var accion = checkbox.checked ? '<%=ControladorItemsBaremacion.ACCION_ITEM_EXCLUYENTE_SELECCIONADO%>'
    						: '<%=ControladorItemsBaremacion.ACCION_ITEM_EXCLUYENTE_DESELECCIONADO%>';
	    				var params = {
	    					'a': accion,
	    					'<%=ControladorItemsBaremacion.PARAM_ITEM%>': '<%=item.getCodNum()%>',
	    					'<%=ControladorItemsBaremacion.PARAM_ITEM_EXCLUYENTE%>': row.codNum,
	    				};
	    				
	    				Atis.sendForm("<%= request.getRequestURI() %>", params);
	    				}
    				}
    			},
		    	{'data': 'codNum'},
		    	{'data': 'codigo', 'filter': true, 'render': function(row) {
		    		return row.bloque.apartado.codigo + "." + row.bloque.codigo + "." + row.codigo;
		    	}},
		        {'data': 'nombre', 'filter': true},
		        {'data': 'activo', 'filter': {'type': 'select', 'options':{'true': 'Activo', 'false': 'Inactivo'}, 'optionDefault': 'true'}, 'render': function(row) {
	        		if(row.activo){
	        			return "<div class='circle-true'></div>"; 
	        		}
	        		else{
	        			return "<div class='circle-false'></div>"; 
	        		}
	        	}},
		    ],
		    "actions": [
		    	{'label': 'Añadir', 'title': 'Añadir un nuevo ítem', 'onClick': function(selected) {
		    		var params = {'a': '<%=ControladorItemsBaremacion.ACCION_AGREGAR_ITEM%>', 
		    				'<%=ControladorItemsBaremacion.PARAM_BLOQUE%>': '<%=bean.getBloqueBaremacion().getCodNum()%>'};
	        		Atis.sendForm("<%=request.getRequestURI()%>", params);
		    	}}
		    ]
		});
		
	});
</script>

<%@page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion"%>
<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorDescargaFicheros" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados" %>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador" %>

<%
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaMisResultados bean = (VistaMisResultados)uvdatos.getVistas().get(VistaMisResultados.class.getName());
Bolsa bolsa = bean.getBolsa();
UsuarioBolsaEmpleo candidato = bean.getUsuarioLogeado();
BolsaResultado bolsaResultado = bean.getBolsaResultado();
MeritoPreferente meritoPreferente = bean.getMeritoPreferente();
Alegacion alegacion = bean.getAlegacion();
String acreditaciones = BolsaEmpleoUtils.clobToString(bolsaResultado.getAcreditacionesValidadas());
String titulaciones = BolsaEmpleoUtils.clobToString(bolsaResultado.getTitulacionesValidadas());
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Resultados del área: <%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h2>
	<h3>Resultados del candidato: <%= EscapaHTML.escapa(candidato.getPrsNif()) %></h3>

	<div class="row">
		<div class="col">
			<button class="link-btn" id="resultados_volver" style="float:left; height: 30px;">Volver</button>

			<button class="link-btn icon icon-download" id="descargar_resultados" style="float:right">Descargar resultados</button>

			<% if (bean.isMostrarBotonCrearAlegacion()) { %>
	           	<button class="link-btn" id="button_crear_alegacion" style="float:right; margin-right: 10px; height: 30px;">Crear alegación</button>
	    	<% } %>

			<% if (bean.isMostrarBotonVerAlegacion()) { %>
	            <button class="link-btn" id="ver_alegaciones" style="float:right; margin-right: 10px; height: 30px;">Ver alegación</button>
			<% } %>
		</div>
	</div>

	<br/>
	<h4><%= ModeloResultados.MERITOS_VALIDADOS %></h4>

<%	if (bolsaResultado.getListaMeritos().size() > 0) { %>
		<div>Leyenda del campo "Desglose":</div>
		<ul>
			<li>Méritos desagregables: (D) (valor1 * afinidad1 + valor2 * afinidad2 + valor3 * afinidad3 + valor4 * afinidad4) * Valor unitario * Peso Bloque</li>
			<li>Méritos con bonificación por bloque "<%= meritoPreferente.getAplicableApartadoBaremacion().getCodigo() + " - " + meritoPreferente.getAplicableApartadoBaremacion().getNombre() %>": Valor * Afinidad * Valor unitario * Peso Bloque * (<%= meritoPreferente.getPrefijoInforme() %>) Factor Mérito Preferente</li>
			<li>Resto de méritos: Valor * Afinidad * Valor unitario * Peso Bloque</li>
		</ul>
		<table class="bluetable bolsaempleo">
			<tr>
				<th scope="col"	style="width:50px">Id. Mérito</th>
				<th scope="col"	style="width:60px">Cod. Mérito</th>
				<th scope="col"	style="width:60%">Tipo de Mérito</th>
				<th scope="col"	style="width:100px" title="Valor * Afinidad * Peso Categoría * Peso Bloque">Desglose</th>
				<th scope="col" style="width:60px" title="Resultado de cada mérito para el área">Resultado</th>
				<th scope="col" style="width:40%">Observación</th>
			</tr>
			<tbody>
			<%	for (MeritoResultado merito: bolsaResultado.getListaMeritos()) { %>
					<tr>
						<td><%= merito.getCodNum() %></td>
						<td><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
						<td><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
						<td><%= EscapaHTML.escapa(merito.getDesglose()) %></td>
						<td><%= merito.getResultado() %></td>
						<td><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
					</tr>
			<%	} %>
			</tbody>
		</table>

		<table class="bluetable bolsaempleo">
			<tbody>
				<tr>
					<th scope="col" style="width:100%">Descripción</th>
					<th scope="col" style="width:100px"></th>
				</tr>
				<tr>
					<td>Total sin aplicar el máximo valor de las titulaciones preferentes y acreditaciones:</td>
					<td><%= bolsaResultado.getTotalSinAplicar() %></td>
				</tr>
			<%	if (bolsaResultado.getDesgloseTotal() != null) { %>
				<tr>
					<td>Cálculo final: <%= EscapaHTML.escapa(bolsaResultado.getDesgloseDescripcion()) %></td>
					<td><%= EscapaHTML.escapa(bolsaResultado.getDesgloseTotal()) %></td>
				</tr>
			<%	} %>
				<tr>
					<td><b>Total:</b></td>
					<td><b><%= bolsaResultado.getTotal() %></b></td>
				</tr>
			</tbody>
		</table>
<%	} else { %>
		<p><%= ModeloResultados.MENSAJE_SIN_MERITOS_EVALUADOS %></p>
<%	} %>

	<h4><%= ModeloResultados.MERITOS_EXCLUIDOS %></h4>

<%	if (bolsaResultado.getListaMeritosExcluidos().size() > 0) { %>
		<table class="bluetable bolsaempleo">
			<tr>
				<th scope="col"	style="width:50px">Id. Mérito</th>
				<th scope="col"	style="width:60px">Cod. Mérito</th>
				<th scope="col"	style="width:60%">Tipo de Mérito</th>
				<th scope="col"	style="width:100px">Valor</th>
				<th scope="col" style="width:40%">Observación</th>
			</tr>
			<tbody>
			<%	for (MeritoResultado merito: bolsaResultado.getListaMeritosExcluidos()) { %>
					<tr>
						<td><%= merito.getCodNum() %></td>
						<td><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
						<td><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
						<td><%= merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0 ? merito.getValorMeritoSolicitud() : merito.getValor() %></td>
						<td><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
					</tr>
			<%	} %>
			</tbody>
		</table>
<%	} else { %>
		<p><%= ModeloResultados.MENSAJE_SIN_MERITOS_EXCLUIDOS %></p>
<%	} %>

	<h4><%= ModeloResultados.MERITOS_NO_EVALUADOS %></h4>

<%	if (bolsaResultado.getListaMeritosNoEvaluados().size() > 0) { %>
		<table class="bluetable bolsaempleo">
			<tr>
				<th scope="col"	style="width:50px">Id. Mérito</th>
				<th scope="col"	style="width:60px">Cod. Mérito</th>
				<th scope="col"	style="width:60%">Tipo de Mérito</th>
				<th scope="col"	style="width:100px">Valor</th>
				<th scope="col" style="width:40%">Observación</th>
			</tr>
			<tbody>
			<%	for (MeritoResultado merito: bolsaResultado.getListaMeritosNoEvaluados()) { %>
					<tr>
						<td><%= merito.getCodNum() %></td>
						<td><%= EscapaHTML.escapa(merito.getCodigoMerito()) %></td>
						<td><%= EscapaHTML.escapa(merito.getNombreMerito()) %></td>
						<td><%= merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() > 0 ? merito.getValorMeritoSolicitud() : merito.getValor() %></td>
						<td><%= EscapaHTML.escapa(merito.getObservacionCandidato()) %></td>
					</tr>
			<%	} %>
			</tbody>
		</table>
<%	} else { %>
		<p><%= ModeloResultados.MENSAJE_SIN_MERITOS_NO_EVALUADOS %></p>
<%	} %>

	<h4>Titulaciones validadas</h4>
	<p><%= BolsaEmpleoUtils.escapaSaltosDeLinea(titulaciones.isEmpty() ? "No hay titulaciones validadas" : titulaciones) %></p>

	<h4>Acreditaciones validadas</h4>
	<p><%= BolsaEmpleoUtils.escapaSaltosDeLinea(acreditaciones.isEmpty() ? "No hay acreditaciones validadas" : acreditaciones) %></p>
</div>

<script>

$(document).ready(function() {

	document.getElementById("resultados_volver").addEventListener("click", function() {
		var params = {
				'<%= ControladorMisResultados.PARAM_ACCION %>': '<%= ControladorMisResultados.ACCION_SELECCIONAR_BOLSA %>',
				'<%= ControladorMisResultados.PARAM_BOLSA %>': '<%= bolsaResultado.getCodNum() %>',
				'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'
		};
		Atis.sendForm("<%= request.getRequestURI() %>", params);
	});

	document.getElementById("descargar_resultados").addEventListener("click", function() {
		window.open("<%= ControladorDescargaFicheros.URL_DESCARGA_FICHEROS + "?a=" + ControladorDescargaFicheros.ACCION_DESCARGAR_RESULTADOS_SOLICITUD_CANDIDATO %>"
				+ "<%= "&" + ControladorDescargaFicheros.PARAM_BOLSA + "=" + bolsaResultado.getCodNum() %>"
				+ "<%= "&" + ControladorDescargaFicheros.PARAM_CONVOCATORIA + "=" + bean.getConvocatoria().getCodNum() %>");
	});

	<% if (bean.isMostrarBotonCrearAlegacion()) { %>
		document.getElementById("button_crear_alegacion").addEventListener("click", function() {
			Atis.confirmDialog(
				"Crear alegación",
				"¿Estás seguro de crear una alegación para está área?", {
					'Sí': function() {
						var params = {
							'<%= ControladorMisResultados.PARAM_ACCION %>': '<%= ControladorMisResultados.ACCION_CREAR_ALEGACION %>',
							'<%= ControladorMisResultados.PARAM_BOLSA %>': '<%= bolsaResultado.getCodNum() %>',
	        				'<%= ControladorMisResultados.PARAM_CONVOCATORIA %>': '<%= bean.getConvocatoria().getCodNum() %>'
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
				}
	        );
		});
	<% } %>

	<% if (bean.isMostrarBotonVerAlegacion()) { %>
		document.getElementById("ver_alegaciones").addEventListener("click", function() {
			location.href = '<%= bean.getUrlAlegaciones() %>';
		});
	<% } %>
});

</script>

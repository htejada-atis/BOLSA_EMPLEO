<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorResultados"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaResultados bean = (VistaResultados)uvdatos.getVistas().get(VistaResultados.class.getName());
Bolsa bolsa = bean.getBolsa();
UsuarioBolsaEmpleo candidato = bean.getCandidato();
BolsaResultado bolsaResultado = bean.getBolsaResultado();
%>

<div class='bolsa-empleo'>

	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />
	
	<h2>Resultados</h2>	
	<h3><%= EscapaHTML.escapa(bolsa.getArea().getDescripcion()) %></h3>
	<h4>Resultados del candidato: <%= EscapaHTML.escapa(candidato.getPrsNif()) %></h4>
	
	<h4>Méritos Evaluados</h4>
	<div>Leyenda del campo "Desglose":</div>
    <ul>
    	<li>Méritos no individualizados: (I) (valor1 * afinidad1 + valor2 * afinidad2 + valor3 * afinidad3 + valor4 * afinidad4) * Peso Categoría * Peso Bloque</li>
    	<li>Méritos con bonificación por bloque "III - Actividad Investigadora": (B) Valor * Afinidad * Peso Categoría * Peso Bloque * 1.1</li>
    	<li>Resto de méritos: Valor * Afinidad * Peso Categoría * Peso Bloque</li>
    </ul>
	<table class="bluetable bolsaempleo">
		<tr>
			<th scope="col"	style="width:10%">Id. Mérito</th>
			<th scope="col"	style="width:15%">Cod. Mérito</th>
			<th scope="col"	style="width:30%">Tipo de Mérito</th>	
			<th scope="col"	style="width:15%" title="Valor * Afinidad * Peso Categoría * Peso Bloque">Desglose</th>
			<th scope="col" style="width:10%">Resultado</th>
			<th scope="col" style="width:20%">Observación</th>
		</tr>
		<tbody>
		<%	for (MeritoResultado merito: bolsaResultado.getListaMeritos()) { %>
				<tr>
					<td><%= merito.getCodNum() %></td>
					<td><%= EscapaHTML.escapa(merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "." 
					+ merito.getItemBaremacion().getBloqueBaremacion().getCodigo() + "." + merito.getItemBaremacion().getCodigo()) %></td>
					<td><%= EscapaHTML.escapa(merito.getItemBaremacion().getNombre()) %></td>
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
				<td>Total sin aplicar el máximo valor de los méritos preferentes y acreditaciones:</td>
				<td><%= bolsaResultado.getTotalSinAplicar() %></td>
			</tr>
		<%	if (bolsaResultado.getDesgloseTotal() != null) { %>
			<tr>
				<td><%= EscapaHTML.escapa(bolsaResultado.getDesgloseDescripcion()) %></td>
				<td><%= EscapaHTML.escapa(bolsaResultado.getDesgloseTotal()) %></td>
			</tr>
		<%	} %>
			<tr>
				<td>Total: </td>
				<td><%= bolsaResultado.getTotal() %></td>
			</tr>
		</tbody>
	</table>
	
</div>
	
<script>

$(document).ready(function() {
	
});

</script>

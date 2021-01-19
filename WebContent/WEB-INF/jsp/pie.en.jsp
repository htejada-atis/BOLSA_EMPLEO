<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector" %>
<%@ page import="es.ujaen.uvirtual.beans.Menu" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%@ page import="es.ujaen.uvirtual.comparador.CompMenuPorIdioma" %>
<%@ page import="java.util.Collections" %>
<%! @SuppressWarnings({"unused", "unchecked"}) %>

<%
String idioma="en";
Vector<Menu> menuPrincipal = (Vector<Menu>)request.getAttribute("pie.menuprincipal");
CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuPrincipal, cmp);


if (1 == 2) {
	// Evita jsp problem en eclipse
	%>
	<div>
	<%
}
%>
		
			<div id="footer">
				<h2>University of Ja&eacute;n</h2>
	
				<p>Campus Las Lagunillas s/n | 23071 - Ja&eacute;n<br /> Tlf: +34 953 21 21 21 | Fax: +34 953 21 22 39 | <a href="mailto:uvirtual@ujaen.es">uvirtual@ujaen.es</a><br /> <a href="../copy.html">Legal</a> | <a href="http://administracionelectronica.ujaen.es/node/129">Complaints and Suggestion</a></p>
				<h2 class="skip">Main menu</h2>
	
				<ul class="mainNav">
					<%
					int menuitemcnt = 0;
					for (Menu menuitem : menuPrincipal) {
						menuitemcnt++;
						%>
						<li><a href="<%= AyudaURL.obtenerUrlControlador(menuitem.getControlador(), idioma) %>" accesskey="<%= menuitemcnt %>"> <%= EscapaHTML.escapa(menuitem.getIdiomas().get(idioma))%></a></li> 
						<%
					}
					%>
				</ul>			
			</div>
		</div> <% // del wrapper en cabecera %>
	</body>
</html>
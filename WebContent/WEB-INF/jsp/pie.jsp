<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="es.ujaen.uvirtual.beans.Menu" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%@ page import="es.ujaen.uvirtual.comparador.CompMenuPorIdioma" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.VistaUVirtual"%>
<%@ page import="java.util.Collections" %>
<%! @SuppressWarnings({"unused"}) %>

<%
String idioma="es";
UVDatos uvDatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUVirtual vista = (VistaUVirtual)uvDatos.getVistas().get(VistaUVirtual.class.getName());
ArrayList<Menu> menuPrincipal = vista.getMenuPrincipal();
Menu menu = vista.getMenu();
CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuPrincipal, cmp);
String piwikRuta = "/pi/";
String piwikActivo = "N";
String piwikSite = "1";

if (1 == 2) {
	// Evita jsp problem en eclipse
	%>
	<div><div><div>
	<%
}

%>
	</div>
		</div>
			<div id="footer">
				<h2></h2>
	
				<p>
					<%= (menu.getDireccion() != null)?(EscapaHTML.escapa(menu.getDireccion())):"Campus Las Lagunillas s/n | 23071 - Ja&eacute;n"%>
					<br />
					<%= (menu.getTelefono() != null)?(EscapaHTML.escapa("Tlf: " + menu.getTelefono() +" | ")):"" %> 
					<%= (menu.getFax() != null)?(EscapaHTML.escapa("Fax: " + menu.getFax() +" | ")):"" %>
					<%
					String correo = "Soporte: uvirtual@ujaen.es";
					if (menu.getEmail() != null)
						correo = menu.getEmail(); 
					String[] partes = correo.split(" ");
					for (String parte : partes) {
						if (parte.indexOf("@") > 0) {
							out.write("<a href=\"mailto:" + parte + "\">" + parte + "</a> ");
						} else {
							out.write(parte +" ");
						}
					}
					%> 
					<br /><a href="http://www10.ujaen.es/aviso-legal">Aviso legal</a> | <a href="http://administracionelectronica.ujaen.es/node/129">Sugerencias</a></p>
				<h2 class="skip">Men&uacute; principal</h2>
	
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
		<%
		try {
			piwikRuta = es.ujaen.uvirtual.beans.ConfiguracionGlobal.getParametroCadena("piwik.ruta");
			piwikActivo = es.ujaen.uvirtual.beans.ConfiguracionGlobal.getParametroCadena("piwik.activo");
			piwikSite = es.ujaen.uvirtual.beans.ConfiguracionGlobal.getParametroCadena("piwik.site");
		} catch (Exception e) {
			e.printStackTrace();
		}
		%>
		<% if ("S".equals(piwikActivo)) { %>
		<!-- Piwik -->
		<script type="text/javascript">
			var _paq = _paq || [];
			_paq.push(['trackPageView']);
			_paq.push(['enableLinkTracking']);
			(function() {
				var u="<%=piwikRuta%>";
				_paq.push(['setTrackerUrl', u+'piwik.php']);
				_paq.push(['setSiteId', <%=piwikSite%>]);
				var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
				g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
			})();
		</script>
		<noscript><p><img src="<%=piwikRuta%>piwik.php?idsite=<%=piwikSite%>" style="border:0;" alt="" /></p></noscript>
		<!-- End Piwik Code -->
		<% } %>
	</body>
</html>
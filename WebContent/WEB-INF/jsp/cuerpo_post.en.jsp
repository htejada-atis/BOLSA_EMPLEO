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
String idioma = "en";

Menu menu = (Menu)request.getAttribute("cuerpopost.menu");
Vector<Menu> menuHijos= (Vector<Menu>)request.getAttribute("cuerpopost.menuhijos");
Vector<Menu> menuNivel = (Vector<Menu>)request.getAttribute("cuerpopost.menunivel");

CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuHijos, cmp);
Collections.sort(menuNivel, cmp);


if (1 == 2) {
	// Evita jsp problem en eclipse
	%>
	<div><div>
	<%
}

%>

			<h2 class="skip">Local Menu</h2>
			<ul class="localNav">
				<%
				for (Menu itemNivel : menuNivel) {
					if (itemNivel.getCodigo() != menu.getCodigo()) {
						%>
						<li><a href="<%= AyudaURL.obtenerUrlControlador(itemNivel.getControlador(), idioma) %>"><%= EscapaHTML.escapa(itemNivel.getIdiomas().get(idioma)) %></a></li>
						<%
					} else {
						%>
						<li class="active"><a href="#"><%= EscapaHTML.escapa(menu.getIdiomas().get(idioma)) %></a></li>
						<li>
							<ul>
								<%
								for (Menu menuitem : menuHijos) {
									%>
									<li>
										<a href="<%= AyudaURL.obtenerUrlControlador(menuitem.getControlador(), idioma) %>"><%= EscapaHTML.escapa(menuitem.getIdiomas().get(idioma)) %></a>
									</li>
									<%
								}
								%>
							</ul>
						</li>					
						<%
					}
				}
				%>
			</ul>			
		</div>
	</div>

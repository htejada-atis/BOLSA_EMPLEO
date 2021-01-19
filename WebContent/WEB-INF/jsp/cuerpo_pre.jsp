<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="es.ujaen.uvirtual.beans.ConfiguracionGlobal" %>
<%@ page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%@ page import="es.ujaen.uvirtual.beans.Menu" %>
<%@ page import="es.ujaen.uvirtual.comparador.CompMenuPorIdioma" %>
<%@ page import="es.ujaen.uvirtual.beans.vistas.VistaUVirtual"%>
<%@ page import="java.util.Collections" %>
<%! @SuppressWarnings("unused") %>
<%
UVDatos uvDatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUVirtual vista = (VistaUVirtual)uvDatos.getVistas().get(VistaUVirtual.class.getName());
Menu menuOpciones = vista.getMenu();
ArrayList<Menu> migaDePan = vista.getMigaDePan();
Usuario usuario = vista.getUsuario();

String idioma="es";

Menu menu = vista.getMenu();
ArrayList<Menu> menuHijos= vista.getMenusHijos();
ArrayList<Menu> menuNivel = vista.getMenusMismoNivel();

CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuHijos, cmp);
Collections.sort(menuNivel, cmp);

%>

	<div id="content">
		<img id="logoMobile" class="mobile" src="/css/images3/escudouja.png" alt="Universidad de Ja&eacute;n" onerror="this.onerror=null; this.src='/css/images3/escudouja.png'" height="58"></img>
		<script type="text/javascript">
		//<![CDATA[
		function togglediv(id) {
			var div = document.getElementById(id);
			if ((div.style.display != 'none')&&(div.style.display != '')) {
				div.style.display = 'none';
			} else {
				div.style.display = 'block';
			}
		}
		//]]>
		</script>
		<div id="menuMobile" onclick="togglediv('localNav'); " title="Pulse para desplegar el men&uacute;">
			<img id="iconoMenu" src="/css/images3/menu.svg" alt="Men&uacute;" onerror="this.onerror=null; this.src='/css/images3/menu.png'" width="24"/>
		</div>

		<ul class="breadcrumb" id="breadcrumb">
			<%
			for (int i=0; i < migaDePan.size()-1; i++) {
				Menu menuMiga = migaDePan.get(i);
				%>
				<li>
					<a href="<%= AyudaURL.obtenerUrlControlador(menuMiga.getControlador(), usuario==null, idioma) %>"> <%= EscapaHTML.escapa(menuMiga.getIdiomas().get(idioma)) %> &gt;</a> 
				</li>
				<%
			}
			%>
			<li>
				<%= EscapaHTML.escapa((migaDePan.get(migaDePan.size()-1)).getIdiomas().get(idioma)) %>
			</li>
		</ul>

		<ul class="secondaryNav" style="height:28px">
			<%
			if (menuOpciones.isPdfInterno() == true) {
				%>
				<li id="obt_pdf">
						<%
						//String urlPdf = AyudaURL.obtenerUrlConFormato(request.getRequestURI(), AyudaURL.FORMATO_PDF) + "/" +  AyudaURL.getSeparadorDeParametros() + "/" + uvDatos.getIdentificadorPeticion() + ".pdf";
						String urlPdf = AyudaURL.obtenerUrlSinParametros(request.getRequestURI(), false);
						urlPdf = AyudaURL.obtenerUrlConFormato(urlPdf, AyudaURL.FORMATO_PDF) + "/" +  AyudaURL.getSeparadorDeParametros() + "/" +  uvDatos.getIdentificadorPeticion() + ".pdf";
						
						%>
						<a href="<%= urlPdf %>">
							<img src="<%= ConfiguracionGlobal.getParametroCadenaNE("administracion.urlbase") + "/img/formatos/acrobat24x24.png" %>" alt="pdf" title="obtener documento en formato PDF"/>
						</a>
					</li>
					<%
				}
				if (menuOpciones.isExcel() == true) {
					%>
					<li id="obt_xslx">
						<a href="<%= AyudaURL.obtenerUrlConFormato(request.getRequestURI(), AyudaURL.FORMATO_EXCEL) + "/" +  AyudaURL.getSeparadorDeParametros() + "/" +  uvDatos.getIdentificadorPeticion() + ".xlsx"%>">
							<img src="<%= ConfiguracionGlobal.getParametroCadenaNE("administracion.urlbase") + "/img/formatos/excel24x24.png" %>" alt="Excel" title="obtener documento en formato Excel"/>
						</a>
					</li>
					<%
				}
				if (menuOpciones.isWord() == true) {
					%>
					<li id="obt_docx">
						<a href="<%= AyudaURL.obtenerUrlConFormato(request.getRequestURI(), AyudaURL.FORMATO_WORD) + "/" +  AyudaURL.getSeparadorDeParametros() + "/" + uvDatos.getIdentificadorPeticion() + ".docx"%>">
							<img src="<%= ConfiguracionGlobal.getParametroCadenaNE("administracion.urlbase") + "/img/formatos/word24x24.png" %>" alt="Word" title="obtener documento en formato Word"/>
						</a>
					</li>
					<%
				}
				%>
		</ul>

		<div id="mainContent">
			<h2 class="skip">Men&uacute; local</h2>
			<ul class="localNav" id="localNav" style="">
				<%
				for (Menu itemNivel : menuNivel) {
					if (itemNivel.getCodigo() != menu.getCodigo()) {
						%>
						<li><a href="<%= AyudaURL.obtenerUrlControlador(itemNivel.getControlador(), usuario==null, idioma) %>"><%= EscapaHTML.escapa(itemNivel.getIdiomas().get(idioma)) %></a></li>
						<%
					} else {
						%>
						<li class="active"><a href="<%= AyudaURL.obtenerUrlControlador(itemNivel.getControlador(), usuario==null, idioma) %>"><%= EscapaHTML.escapa(menu.getIdiomas().get(idioma)) %></a></li>
						<li>
							<ul>
								<%
								for (Menu menuitem : menuHijos) {
									%>
									<li>
										<a href="<%= AyudaURL.obtenerUrlControlador(menuitem.getControlador(), usuario==null, idioma) %>"><%= EscapaHTML.escapa(menuitem.getIdiomas().get(idioma)) %></a>
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
		
			<script type="text/javascript">
			<% //estamos en una opcion raiz, tiene 5 %>
			if (location.href.split("/").length-1==5) {
				togglediv('localNav');
			}
			</script>
	<%
	if (1 == 2) {
		// Evita jsp problem en eclipse
		%>
		</div></div>
		<%
	}
	%>


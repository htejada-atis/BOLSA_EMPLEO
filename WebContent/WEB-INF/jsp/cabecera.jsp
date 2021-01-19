<%@page import="es.ujaen.uvirtual.beans.vistas.VistaUVirtual"%>
<%
// Hack para IE inferior a 9
String valoresAccept = "";
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaUVirtual vista = (VistaUVirtual)uvdatos.getVistas().get(VistaUVirtual.class.getName());

try {
	valoresAccept = ((HttpServletRequest)request).getHeader("accept");
} catch (Exception ex1) {
	//
}
if (uvdatos.getDocType()==null){
	if (valoresAccept!=null && valoresAccept.indexOf("application/xhtml+xml") >= 0) {
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	}
}
%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="es.ujaen.uvirtual.beans.*" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%@ page import="es.ujaen.uvirtual.comparador.CompMenuPorIdioma" %>
<%@ page import="java.util.Collections" %>
<%! @SuppressWarnings("unused") %>
<%
String idioma="es";
String idiomaAlt="en";


Menu menu = vista.getMenu();
ArrayList<Menu> menuPrincipal = vista.getMenuPrincipal();
Usuario usuario = vista.getUsuario();
Integer avisosSinLeer = vista.getAvisosSinLeer();

String usuarioReal = vista.getIdentificadorUsuarioAutenticado();
String paginaInicio = vista.getPaginaInicio();
CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuPrincipal, cmp);

if (uvdatos.getDocType()==null){
	if (valoresAccept!=null && valoresAccept.indexOf("application/xhtml+xml") >= 0) {
		out.write("\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
	} else {
		out.write("\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n<html>\n");
	}
} else {
	out.write(uvdatos.getDocType());
	out.write("\n<html>\n");
}
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="-1" />
	<link rel="icon" href="/favicon2.ico" />
	<link rel="shortcut icon" href="/favicon2.ico" />
	<link href="/css/ujaen3.css" rel="stylesheet" type="text/css" media="screen, print" />
	<link href="/css/ujaencv.css" rel="stylesheet" type="text/css" media="screen, print" />
	<link href="/css/ujaen_480.css" rel="stylesheet" type="text/css" media="only screen and (max-width: 980px)" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	
	<%
	for(String cssname: uvdatos.getFicherosCSS()) {
		%>
		<link href="<%= cssname %>" rel="stylesheet" type="text/css" media="screen, print" />
		<%
	}

	for(String jsname: uvdatos.getFicherosJS()) {
		%>
		<script type="text/javascript" src="<%= jsname %>"></script>
		<%
	}
	%>
	<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/js/uvirtual.js"></script>
	<!--[if lte IE 8]>
	<link href="/css/ujaen3ie.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<!--[if IE 6]>
	<link href="/css/ujaen3ie6.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<title>Universidad virtual de la Universidad de Ja&eacute;n - <%= EscapaHTML.escapa(menu.getIdiomas().get(idioma)) %> - <%= java.net.InetAddress.getLocalHost().getHostAddress().replace("192.168.178.", "srv") %></title>
</head>
<body id="micrositeA">
	<div id="wrapper">
		<div id="header">
			<a href="#content" class="skip" title="Ir al principio del contenido de esta p&aacute;gina.">Saltar al contenido principal</a>
			<h1><a href="http://www.ujaen.es/" accesskey="h"><span>Universidad de Ja&eacute;n</span></a></h1>
			<p class="slogan">Calidad e innovaci&oacute;n al servicio de la sociedad</p>

			<h2 class="skip">Men&uacute; principal</h2>
			<ul class="mainNav">
				<li><a href="<%= paginaInicio %>"><span id="pageHome">&nbsp;</span></a></li>
				<%
				int menuitemcnt = 0;
				for (Menu menuitem : menuPrincipal) {
					menuitemcnt++;
					%>
					<li><a href="<%= AyudaURL.obtenerUrlControlador(menuitem.getControlador(), usuario==null, idioma) %>" accesskey="<%= menuitemcnt %>"> <%= EscapaHTML.escapa(menuitem.getIdiomas().get(idioma))%></a></li> 
					<%
				}
				%>
			</ul>
			<h2 class="skip">Informaci&oacute;n de usuario y servicios</h2>
			<ul class="secondaryNav">
				<%
				if ((!"".equals(usuarioReal)) && (usuarioReal != null)) {
					%>
					<li><a href="<%= ConfiguracionGlobal.getParametroCadena("administracion.urlvolverdeusuario") %>">Volver a <%= usuarioReal %></a></li>
					<%
				}
				if (usuario != null) {
					%>
					<li><span style="background:#FF0000;color:#FFFFFF"> <%= EscapaHTML.escapa(usuario.getNombreYApellidos() + " (" + usuario.getUid() + ")") %></span>
					<% if (avisosSinLeer > 0 && usuario.getRoles().contains("alumnos")) {%>
						<a href="/srv/es/informacionacademica/avisospersonales"><%= avisosSinLeer %> mensajes sin leer</a>
					<% } %>
					</li>
					<%
				} else {
					String urlActual = request.getRequestURI();
					String url = AyudaURL.obtenerUrlControlador(AyudaURL.obtenerControlador(urlActual), false, idioma); 
					%>
					<li id="idIniciaSesion"><a href="<%= url %>" style="background:#FF0000;color:#FFFFFF" accesskey="s"> Iniciar sesi&oacute;n </a></li>
					<%
				}
				if (usuario != null) {
					%>
					<li class="intranet"><a href="/salir" accesskey="i">Salir</a></li>
					<%
				} else {
					%>
					<li class="intranet"><a href="https://www.ujaen.es/home/intranet.html" accesskey="i">U. Virtual</a></li>
					<%
				}
				%>
			</ul>
			<div style="display:none">
				<img src="/img/conectando.gif" width="220" height="19" alt="conectando"/>
			</div>
		</div>
	<%
	
	if (1 ==2) {
		//Para eliminar warning de que no existe etiqueta de cierre del wrapper (lo tiene el pie)
		%></div><%
	}
	%>
	
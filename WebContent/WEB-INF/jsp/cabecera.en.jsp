<?xml version="1.0" encoding="UTF-8" ?>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector" %>
<%@ page import="es.ujaen.uvirtual.beans.*" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%@ page import="es.ujaen.uvirtual.comparador.CompMenuPorIdioma" %>
<%@ page import="java.util.Collections" %>
<%! @SuppressWarnings({"unused", "unchecked"}) %>
<%
String idioma="en";
String idiomaAlt="es";

Menu menu = (Menu)request.getAttribute("cabecera.menu");
Vector<Menu> menuPrincipal = (Vector<Menu>)request.getAttribute("cabecera.menuprincipal");
Usuario usuario = (Usuario)request.getAttribute("cabecera.usuario");

String usuarioReal = (String)request.getAttribute("cabecera.usuarioreal");
String paginaInicio = (String)request.getAttribute("cabecera.paginainicio");
CompMenuPorIdioma cmp = new CompMenuPorIdioma(idioma);
Collections.sort(menuPrincipal, cmp);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="-1" />
	<link rel="icon" href="/favicon2.ico" />
	<link rel="shortcut icon" href="/favicon2.ico" />
	<link href="/css/ujaen3.css" rel="stylesheet" type="text/css" media="screen, print" />
	<%
	Vector<String> vjs = (Vector<String>)request.getAttribute(ConfiguracionGlobal.getAtributoJavascript());
	for(String jsname: vjs) {
		%>	
		<script type="text/javascript" src="<%= jsname %>" />
		<%
	} 
	%>
	<!--[if lte IE 8]>
	<link href="/css/ujaen3ie.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<!--[if IE 6]>
	<link href="/css/ujaen3ie6.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<title>Universidad of Ja&eacute;n - Virtual Univiersity - <%= EscapaHTML.escapa(menu.getIdiomas().get(idioma)) %> - <%= request.getServerName() %></title>
</head>
<body id="micrositeA">
	<div id="wrapper">
		<div id="header">
			<a href="#content" class="skip" title="Go to this page main content.">Go to main content</a>
			<ul class="langNav">
				<%
				if (menu.getIdiomas().get("es") != null) {
					%>
					<li id="spanish"><a href="<%= AyudaURL.obtenerUrlConIdioma(request.getRequestURI(), idiomaAlt) %>" lang="es" accesskey="l">Espa&ntilde;ol</a></li>
					<%
				} else { %>
					<li id="spanish">Espa&ntilde;ol (Versi&oacute;n no disponible)</li>
				<%
				}
				%>
				<li id="english">English</li>
			</ul>
			<h1><a href="http://www.ujaen.es/" accesskey="h"><span>University of Ja&eacute;n</span></a></h1>
			<p class="slogan">Calidad e innovaci&oacute;n al servicio de la sociedad</p>

			<h2 class="skip">Main menu</h2>
			<ul class="mainNav">
				<li><a href="<%= paginaInicio %>"><span id="pageHome">&nbsp;</span></a></li>
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
			<h2 class="skip">User and services information</h2>
			<ul class="secondaryNav">
				<%
				if ((!"".equals(usuarioReal)) && (usuarioReal != null)) {
					%>
					<li><a href="<%= ConfiguracionGlobal.getUrlVolverDeUsuario() %>">Back to <%= usuarioReal %></a></li>
					<%
				}
				if (usuario != null) {
					%>
					<li><a href="TODO:definirUrlSalida" style="background:#FF0000;color:#FFFFFF" accesskey="s"> Logout <%= usuario.getUid() %> </a></li>
					<%
				} else {
					%>
					<li><a href="TODO:definirUrlEntrada" style="background:#FF0000;color:#FFFFFF" accesskey="s"> Login </a></li>
					<%					
				}
				%>
				<li class="intranet"><a href="https://www.ujaen.es/home/intranet.html" accesskey="i">Intranet</a></li>
			</ul>
		</div>
	
	<%
	if (1 == 2) {
		// Evita jsp problem en eclipse
		%>
		</div></body></html>
		<%
	}
	%>
	
	
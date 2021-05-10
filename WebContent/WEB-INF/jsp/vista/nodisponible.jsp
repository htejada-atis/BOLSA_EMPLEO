<%@ page contentType="text/html; charset=utf-8" %>
<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.beans.ConfiguracionGlobal" %>
<%@ page import="es.ujaen.uvirtual.beans.ErroresPersonalizados" %>
<%

String titulo = "Opción temporalmente fuera de servicio";
String mensaje = "Actualmente no es posible realizar la operación solicitada";
boolean mostrarVolver = true;
String atributoError = ConfiguracionGlobal.getAtributoError();
if (atributoError != null) {
	String infoSesion = (String)request.getSession().getAttribute(atributoError);
	if (infoSesion != null) {
		String[] apartados = infoSesion.split(":");
		if (ErroresPersonalizados.ERROR_UV_NO_DISPONIBLE.equals(apartados[0])) {
			mostrarVolver = false;
			titulo = "Universidad Virtual está en mantenimiento";
			mensaje = "Actualmente estamos realizando tareas de mantenimiento en Universidad Virtual por lo que no se puede acceder al servicio.";
		} else if (ErroresPersonalizados.ERROR_SERV_NO_DISPONIBLE.equals(apartados[0])) {
			if (apartados.length > 1) {
				mensaje = apartados[1];
				for (int i = 2; i < apartados.length; i++) {
					mensaje += ":" + apartados[i];
				}
			}
		}
	}
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="es" xml:lang="es">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="-1" />
	<link rel="icon" href="/favicon2.ico" />
	<link rel="shortcut icon" href="/favicon2.ico" />
	<link href="/css/ujaen3.css" rel="stylesheet" type="text/css" media="screen, print" />
	<link href="/css/ujaencv.css" rel="stylesheet" type="text/css" media="screen, print" />
	<!--[if lte IE 8]>
	<link href="/css/ujaen3ie.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<!--[if IE 6]>
	<link href="/css/ujaen3ie6.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<title>Universidad virtual de la Universidad de Jaén - Inicio - srv106</title>
</head>
<body id="micrositeA">
	<div id="wrapper">
		<div id="header">
			<a href="#content" class="skip" title="Ir al principio del contenido de esta p&aacute;gina.">Saltar al contenido principal</a>
			<ul class="langNav">
				<li id="spanish">Español</li>
				<li id="english">English version not available</li>
				</ul>
			<h1><a href="http://www.ujaen.es/" accesskey="h"><span>Universidad de Jaén</span></a></h1>
			<p class="slogan">Calidad e innovación al servicio de la sociedad</p>

			<h2 class="skip">Menú principal</h2>
			<ul class="mainNav">
				<li><a href="/srv/es/index"><span id="pageHome"> </span></a></li>
				<li><a href="/srv/es/informaciongeneral" accesskey="1"> Información general</a></li> 
					</ul>
			<h2 class="skip">Información de usuario y servicios</h2>
			<ul class="secondaryNav">
				<li><a href="/srv/es/index" style="background:#FF0000;color:#FFFFFF" accesskey="s"> Iniciar sesión </a></li>
					<li class="intranet"><a href="https://www.ujaen.es/home/intranet.html" accesskey="i">U. Virtual</a></li>
					</ul>
		</div>
	
	
	<div id="content">
		<ul class="breadcrumb">
			<li>
				Inicio</li>
		</ul>

		<ul class="secondaryNav" style="height:28px">
			</ul>

		<div id="mainContent">
		<h2 class="skip">Menú local</h2>
			<ul class="localNav" style="display=block;">
				<li class="active"><a href="/pub/es/index">Inicio</a></li>
				<li>
					<ul>
						<li>
							<a href="/pub/es/informaciongeneral">Información general</a>
						</li>
					</ul>
				</li>
			</ul>
<h2><%= EscapaHTML.escapa(titulo) %></h2>
<div id="itemsmainContent">

<p><%= EscapaHTML.escapa(mensaje) %></p>

<p>Rogamos disculpe las molestias ocasionadas</p>
<%
if (mostrarVolver) {
	%>
	<p><a href="javascript:window.history.back()">Pulse para volver a la pantalla anterior</a></p>
	<%
}
%> 

</div>
		</div>
	</div>
	<div id="footer">
				<h2>Universidad de Jaén</h2>
	
				<p>
					Campus Las Lagunillas s/n | 23071 - Jaén<br />
					Soporte: <a href="mailto:gestion@ujaen.es">gestion@ujaen.es</a> <br /> <a href="http://www10.ujaen.es/aviso-legal">Aviso legal</a> | <a href="http://administracionelectronica.ujaen.es/node/129">Sugerencias</a></p>
				<h2 class="skip">Menú principal</h2>
	
				<ul class="mainNav">
					<li><a href="/srv/es/informaciongeneral" accesskey="1"> Información general</a></li> 
						</ul>			
			</div>
		</div> </body>
</html>

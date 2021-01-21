<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="es" xml:lang="es">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="icon" href="/favicon2.ico" />
	<link rel="shortcut icon" href="/favicon2.ico" />
	<link href="/css/ujaen3.css" rel="stylesheet" type="text/css" media="screen, print" />
	<!--[if lte IE 8]>
	<link href="/css/ujaen3ie.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<!--[if IE 6]>
	<link href="/css/ujaen3ie6.css" rel="stylesheet" type="text/css" media="screen, print" />
	<![endif]-->
	<title>Universidad virtual - Error - </title>

</head>
<body id="micrositeA">
	<div id="wrapper">
		<div id="header">
			<a href="#content" class="skip" title="Ir al principio del contenido de esta p&aacute;gina.">Saltar al contenido principal</a>
			<ul class="langNav">
				<li id="spanish">Español</li>
				<li id="english"><a href="/pub/en/index" lang="en" accesskey="l">English</a></li>

					</ul>
			<h1><a href="http://www.ujaen.es/" accesskey="h"><span>Universidad de Jaén</span></a></h1>
			<p class="slogan">Calidad e innovación al servicio de la sociedad</p>

			<h2 class="skip">Menú principal</h2>
			<ul class="mainNav">

				<li><a href="/srv/es/index"><span id="pageHome">&nbsp;</span></a></li>
			</ul>
			<h2 class="skip">Información de usuario y servicios</h2>
			<ul class="secondaryNav">
				<li><a href="/srv/es/index" style="background:#FF0000;color:#FFFFFF" accesskey="s"> Iniciar sesión </a></li>
			</ul>
		</div>
	
	
	<div id="content">
		<ul class="breadcrumb">
			<li>
				Inicio</li>
		</ul>

		<div id="mainContent">
	
		<h2>Error en la aplicación</h2>
		<div id="itemsmainContent">
		<%
		if (statusCode != null) {
			out.write("Error mientras se procesaba la solicitud " + requestURI + " (" +statusCode + ")");
		}
		if (throwable != null) {
			out.write("Error mientras se procesaba la solicitud " + requestURI + " (" + throwable.getLocalizedMessage() + ")");
		}
		%>
		</div>
		<h2 class="skip">Menú local</h2>

			<ul class="localNav">
				<li class="active"><a href="#">Error</a></li>
			</ul>			
		</div>
	</div>
	<div id="footer">
				<h2>Universidad de Jaén</h2>
	
				<p>
					Campus Las Lagunillas s/n | 23071 - Jaén<br />

					Tlf: +34 953 21 21 21 | 
					Fax: +34 953 21 22 39 | 
					<a href="mailto:gestion@ujaen.es">gestion@ujaen.es</a><br /> <a href="../copy.html">Aviso legal</a> | <a href="http://administracionelectronica.ujaen.es/node/129">Sugerencias</a></p>
				<h2 class="skip">Menú principal</h2>
	
				<ul class="mainNav">
					<li>.</li> 
						</ul>			
			</div>

		</div> 
</body>
</html>
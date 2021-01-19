<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector" %>
<%@ page import="es.ujaen.uvirtual.beans.Menu" %>
<%@ page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.utilidades.AyudaURL" %>
<%! @SuppressWarnings({"unused", "unchecked"}) %>

<%
Vector<Menu> migaDePan = (Vector<Menu>)request.getAttribute("cuerpopre.migadepan");
Usuario usuario = (Usuario)request.getAttribute("cuerpopre.usuario");

String idioma="en";
%>

	<div id="content">
		<ul class="breadcrumb">
			<%
			for (int i=0; i < migaDePan.size()-1; i++) {
				Menu menu = migaDePan.elementAt(i);
				%>
				<li>
					<a href="<%= AyudaURL.obtenerUrlControlador(menu.getControlador(), usuario == null, idioma)  %>"> <%= EscapaHTML.escapa(menu.getIdiomas().get(idioma)) %> &gt;</a> 
				</li>
				<%
			}
			%>
			<li>
				<%= EscapaHTML.escapa((migaDePan.elementAt(migaDePan.size()-1)).getIdiomas().get(idioma)) %>
			</li>
		</ul>
		<div id="mainContent">
	
	<%
	if (1 == 2) {
		// Evita jsp problem en eclipse
		%>
		</div></div>
		<%
	}
	%>
	
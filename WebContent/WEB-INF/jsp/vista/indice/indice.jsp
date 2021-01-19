<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="es.ujaen.uvirtual.beans.Usuario" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.controlador.xdefecto.Indice" %>

<%

@SuppressWarnings("unchecked")
List<String> avisos = (List<String>)request.getAttribute(Indice.AVISOS);
@SuppressWarnings("unchecked")
List<String> advertencias = (List<String>)request.getAttribute(Indice.ADVERTENCIAS);
Usuario usuario = (Usuario)request.getAttribute(Indice.USUARIO);

if (avisos == null)
	avisos = new ArrayList<String>();
if (advertencias == null)
	advertencias = new ArrayList<String>();

// @TODO: obtener si estamos autenticado y los permisos del usuario... para filtrar
%>
<h2><%= (usuario != null)? (EscapaHTML.escapa(usuario.getNombre()) + ", est&aacute;s en la Universidad Virtual de la UJA."):"Universidad Virtual"%></h2>
<div id="itemsmainContent">

<%
if (avisos.size() > 0) {
	%>
	<div id="aviso" class="info">
		<ul>
			<%
			for(String aviso : avisos) {
				out.write("<li>" + EscapaHTML.escapa(aviso).replace("&#10;", "<br />") + "</li>");
			}
			%>
		</ul>
	</div>
	<%
}
if (advertencias.size() > 0) {
	%>
	<div id="advertencias" class="warning">
		<ul>
			<%
			for(String advertencia : advertencias) {
				out.write("<li>" + EscapaHTML.escapa(advertencia).replace("&#10;", "<br />") + "</li>");
			}
			%>
		</ul>
	</div>
	<%
}
%>

<p>
Acceso a los recursos y servicios que ofrece la Universidad para cada uno de los colectivos que integran la comunidad universitaria: Alumnos Universitarios, Personal Docente e Investigador y Personal de Administraci&oacute;n y Servicios.
</p>

</div>
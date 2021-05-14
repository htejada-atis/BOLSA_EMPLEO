<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorInicio"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaInicio" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>


<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaInicio bean = (VistaInicio)uvdatos.getVistas().get(VistaInicio.class.getName());
%>

<div class="bolsa-empleo">
	
	<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

	<h2>Acerca de</h2>
	
	<div class="nav-bolsa-empleo">
        <%@ include file="includes/menu.jsp" %>
    </div>
    
    <div class="descripcion-bolsa-empleo">
    	<p>
		  	 Lorem ipsum dolor sit amet, consectetur adipiscing elit.Praesent pellentesque nisi sit amet ultricies ultricies. Pellentesque at laoreet felis.
		  	  Donec eros eros, scelerisque a lorem a, mollis placerat dui. In hac habitasse platea dictumst. Sed sed tempus sem.
		  	   Quisque ut urna ut dolor cursus pretium. Proin vulputate vehicula tempus. Sed malesuada mi nec orci posuere, eget malesuada libero scelerisque. 
		  	   Duis vitae sem pulvinar diam rutrum iaculis ac eget eros. Quisque nec nisl eu orci tristique facilisis vehicula eu justo.
		  	    Vestibulum id leo luctus dui scelerisque dapibus. Ut eget ante lacus.
			<br/><br/>
			Morbi rhoncus fringilla nisl id egestas. Praesent venenatis bibendum felis, in fermentum neque. Nulla ornare sollicitudin consectetur.
			 Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Mauris luctus in leo ut euismod.
			  Mauris sollicitudin quis ex sit amet dictum. Duis sit amet libero in ex imperdiet lacinia ac nec erat. Vivamus vitae lacinia odio, vitae bibendum arcu.
			   Quisque eleifend nec quam a lacinia. Phasellus scelerisque elit mi, in faucibus nisl ullamcorper tempus. Donec posuere quam id vestibulum suscipit. 
    	</p>
    </div>
	
</div>
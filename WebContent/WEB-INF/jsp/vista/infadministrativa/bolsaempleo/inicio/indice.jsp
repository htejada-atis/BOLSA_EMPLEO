<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorInicio"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaInicio" %>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo"%>
<%@ page import="es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaInicio bean = (VistaInicio)uvdatos.getVistas().get(VistaInicio.class.getName());
VistaUsuarioBolsaEmpleo beanUsuario = (VistaUsuarioBolsaEmpleo) uvdatos.getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
%>

<div class="bolsa-empleo">

	<% if (bean != null) { %>
		<jsp:include page="/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/mensajes.jsp" />

		<% if (beanUsuario != null) { %>
			<% if (beanUsuario.getMensajesDeExito().size() > 0) { %>
				<div id="exito" class="success">
				<%= beanUsuario.formatearMensajesDeExito() %>
				</div>
			<% } %>
		<% } %>
	<% } %>

	<h2>Bolsa de empleo para PDI de la Universidad de Jaén</h2>
	
	<div class="nav-bolsa-empleo">
		<%@ include file="includes/menu.jsp" %>
	</div>
	
<%	if (!bean.getAnonimo() && bean.getUsuarioLogeado().getCodNum() == null) { %>
		<button class="link-btn" id="participar_bolsa_empleo">Participar en la bolsa de empleo</button>
<%	} %>
	
	<div class="descripcion-bolsa-empleo">
		<p></p>
	</div>
	
	<div class="noticias-bolsa-empleo">
		<h4>Noticias y Novedades</h4>
		<ul>
			<% for(Noticia noticia:bean.getNoticias()) { %>
				<li class="row-noticia" id="noticia_<%=noticia.getCodNum()%>">
					<div class="fecha-noticia"><%=Formateador.formatoFecha(noticia.getFecha(), Formateador.FORMATO_FECHA_DDMMYYYY)%></div>
					<a href="<%=noticia.getEnlace()%>" target="_blank"><%=noticia.getTexto()%></a>
				</li>
			<% } %>
		</ul>
		<div class="row-ver-todas" id="view_all"> (<a href="#">Ver todas</a>) </div>
	</div>

</div>

<script type="text/javascript">
	$(document).ready(function(){
		var ids_noticias = new Array();
		
		function comprobarNoticiasCargadas() {
			$(".noticias-bolsa-empleo ul").children("li").each(function() {
				ids_noticias.push($(this).attr("id").split("_")[1]);
			});
		}
		
		function obtenerRestoDeNoticias() {
			ids_noticias = []
			comprobarNoticiasCargadas();
			var params = {'a':'listar_todas_noticias', <%= ControladorInicio.PARAM_NOTICIAS %>: JSON.stringify(ids_noticias)}
			
			$.ajax({
				type: "GET",
				url: "<%= bean.getAnonimo() ? ControladorInicio.URL_PATTERN_AJAX_PUBLICA : ControladorInicio.URL_PATTERN_AJAX_PRIVADA %>",
				contentType: "application/json",
				dataType: "json",
				data: params,
				success: function(response) {
					response.forEach((noticia) => {
						var element = $(".noticias-bolsa-empleo li").first().clone();
						element.attr("id", "id_"+noticia.idNoticia);
						element.find("a").attr("href", noticia.enlace);
						element.find("a").text(noticia.texto);
						element.find(".fecha-noticia").text(noticia.fecha);

						$(".noticias-bolsa-empleo ul").append(element);
						$("#view_all a").text("Ver menos");
						view_all = false;
					});
				},
				error: function(response) {
					alert(response);
				}
			});
		}
		
		var view_all = true;
		document.getElementById("view_all").addEventListener("click", function(event){
			event.preventDefault();
			if(view_all) {
				obtenerRestoDeNoticias();
			} else {
				$(".noticias-bolsa-empleo ul").children("li").each(function(i) {
					if(i > 8) {
						$(this).remove();
					}
				});
				$("#view_all a").text("Ver mas");
				view_all = true;
			}
			
		});
		
		comprobarNoticiasCargadas();
		
		if(ids_noticias.length < 9) {
			$("#view_all").hide();
		}
		
		document.getElementById("participar_bolsa_empleo").addEventListener("click", function(event) {
			event.preventDefault();
			Atis.confirmDialog("Participar en la bolsa de empleo", "Va a registrarse en la bolsa de empleo para optar a una plaza.", {
					'Confirmar': function(row) {
						var params = {
								'<%= ControladorInicio.PARAM_ACCION %>': '<%= ControladorInicio.ACCION_PARTICIPAR_BOLSA_EMPLEO %>'
						};
						Atis.sendForm("<%= request.getRequestURI() %>", params);
						$(this).dialog("close");
					},
					'No': function() {
						$(this).dialog("close");
					}
			});
		});
		
	});

</script>

<%@ page trimDirectiveWhitespaces="true"%>
<%@	page import="es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorIndice"%>
<%@ page import="es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticiasCRUD" %>
<%@ page import="es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia" %>
<%@ page import="es.ujaen.uvirtual.utilidades.EscapaHTML" %>
<%@ page import="es.ujaen.uvirtual.beans.UVDatos"%>
<%@ page import="es.ujaen.uvirtual.utilidades.Formateador"%>

<% 
UVDatos uvdatos = (UVDatos)request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
VistaNoticiasCRUD bean = (VistaNoticiasCRUD)uvdatos.getVistas().get(VistaNoticiasCRUD.class.getName());
%>

<div class="bolsa">
    <h2>Bolsa de empleo para PDI de la Universidad de Ja�n</h2>
    
    <div>
        <ul>
            <li>
                <a href="#" rel="history" title="Inicio">
                    Inicio
                    <!--<img alt="Inicio" title="Inicio" src="images/Home_48x48.png">-->
                </a>
            </li>
            <li>
                <a href="#" rel="history" title="Acerca de la bolsa">
                    Acerca de la bolsa
                    <!--<img alt="Acerca" title="Acerca de la bolsa de empleo" src="images/Gnome-Help-About-48.png">-->
                </a>
            </li>
            <li>
                <a href="#" rel="history" title="Ayuda">
                    Ayuda
                    <!--<img alt="Ayuda" title="Ayuda de la aplicación" src="images/Help_48.png">-->
                </a>
            </li>
            <li>
                <a href="#" rel="history" title="Preguntas Frecuentes">
                    Preguntas Frecuentes
                    <!--<img alt="FAQ" title="Preguntas Frecuentes" src="images/FAQ-48.png">-->
                </a>
            </li>
            <li>
                <a href="#" rel="history" title="Documentos de interés">
                    Documentos de interés
                    <!--<img alt="Doc." title="Documentos de Interés" src="images/PDF-Documents-48.png">-->
                </a>
            </li>
        </ul>
    </div>
    
    <div>
        <div class="noticias-bolsa-empleo">
        	<h4>Noticias y Novedades </h4>
			<p><a href="#">Skip to News</a></p>
			<p>
			    <a href="#" title="Back">« Anterior</a>
			</p>
			<p class="next">
			    <a href="#" title="Next" style="display: block;">Siguiente »</a>
			</p>
			<p class="view_all">31 total ( <a href="#">Ver todas</a> )</p>
			<a name="skip_to_news"></a>
			<ul>
				<% for(Noticia noticia:bean.getNoticias()){ %>
					<li>
						<p>
							<a href="<%=noticia.getEnlace()%>" target="_blank"><%=noticia.getTexto()%></a>
					    </p>
					</li>
				<% } %>
			</ul>
		</div>
    </div>

</div>

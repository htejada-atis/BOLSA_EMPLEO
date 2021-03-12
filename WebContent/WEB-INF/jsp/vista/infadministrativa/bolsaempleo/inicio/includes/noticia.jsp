
<li class="row-noticia" id="noticia_<%= noticia.getIdNoticia() %>">
	<div class="fecha-noticia"><%=Formateador.formatoFecha(noticia.getFecha(), Formateador.FORMATO_FECHA_DDMMYYYY)%></div>
	<a href="<%=noticia.getEnlace()%>" target="_blank"><%=noticia.getTexto()%></a>
</li>
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
	
	<h2>FAQ</h2>
	
	<div class="nav-bolsa-empleo">
        <%@ include file="includes/menu.jsp" %>
    </div>
    
    <div class="descripcion-bolsa-empleo">

		<h3>Algunas categorías vienen expresadas en Ratio-Autor y Ratio-Proyecto. ¿No sé lo que significa?</h3>
		<p><strong>Ratio-autor:</strong><br/>
		    * La puntuación asignada a cada mérito medido en unidades ratio-autor
		    sera de 1 para aquellas contribuciones con 4 o menos autores; para las
		    contribuciones con mas de 4 autores, la puntuación asignada sera de 4 y
		    dividido por el número de autores. <br/>
		
		    <strong>Ratio-proyecto:</strong><br/>
		    * Cada mérito medido mediante esta unidad se contabilizara como 1 si el
		    tipo de participación como investigador principal y 0.5 para el resto de
		    personal investigador.<br/>
		    Ademas, en los proyectos con duración inferior a 3 años se multiplicara
		    la puntuación anterior por el número de años (o fracción) y se dividira
		    por 3.<br/>
		    Ademas, si el proyecto tiene un importe inferior a 60.0000 ?, se
		    multiplicara la puntuación asignada a este mérito por el importe (en
		    euros) y se dividira por 60.000.<br/>
		
		    Debe tenerse en cuenta que solamente se valorara la participación como
		    resto de personal investigador si esta acreditada mediante fotocopia
		    compulsada de las paginas correspondientes de la memoria de solicitud o,
		    en el caso de incorporación posterior, autorización del organismo
		    financiador. Los becarios de investigación adscritos al proyecto tendran
		    la consideración de "resto de personal investigador".<br/>
		</p>
		
		<h3>¿Por qué tengo que entregar los documentos que acreditan mis méritos en formato Pdf?</h3>
		<p>Debido a que nos queremos acercar a la administración electrónica, todos los ficheros que se envíen a esta aplicación deberan ser de algún formato electrónico.</p>
		
		<h3>¿Por qué tengo que insertar los ficheros en formato Pdf?</h3>
		<p>La razón mas importante es que es un formato estandar que se utiliza como especificación de visualización, gracias a la gran calidad de las fuentes utilizadas y a las facilidades que ofrece para el manejo del documento, como búsquedas, hiperenlaces, etc.</p>
		
		<h3>¿Cómo paso mis ficheros al formato Pdf?</h3>
		<p>Con un programa gratuito (por ejemplo <a href="http://sourceforge.net/projects/pdfcreator/" target="_blank">PDFCreator</a>) para convertir ficheros en pdf. Se lo instala y su funcionamiento general es: cualquier fichero que tenga en su disco duro, le pincha con el botón derecho y elige la opción "Crear fichero pdf con PDFCreator". Hecho esto es como si lanzara el fichero a una impresora pero en este caso no imprime en papel sino en pdf, le aparecera una pantalla con unos botones abajo y usted le dara a guardar, le pone un nombre al fichero y lo guarda en su disco duro.</p>
		
		<h3>¿Qué tengo que poner en el campo "valor" del formulario del insertar méritos?</h3>
		
		<p>Tiene que poner el valor del mérito que quiera introducir expresado en las unidades que marca la categoría.<br/>
		    Si tiene un 2 de nota media de expediente pues tendra que poner un 2 en el campo. Si tiene 1 Tesina pues tendra que poner 1 y así con todas las categorías, excepto con las categorías cuya unidad sea Ratio-Autor y Ratio-Proyecto, cuyo valor dependera del número de autores, o participantes.</p>
		
		<h3>¿Qué significa el "valor unitario" que viene en el reglamento de baremo de contratación PDI?</h3>
		<p>Significa que el valor que ponga en el formulario de nuevo mérito se multiplicara por este valor ya que es el peso de esa categoría. No hay que multiplicarlo manualmente, esto lo hace el sistema en el proceso de baremación.</p>
		
		<h3>Tengo 3 revistas del primer cuartil y solo puedo cargar un documento Pdf., o sea, una revista. ¿Cómo puedo hacer para adjuntar las otras dos revistas?</h3>
		<p>Podemos insertar méritos con la misma categoría y es lo que se debe estos casos. Insertar 3 méritos con la misma categoría y adjuntar en cada uno un fichero pdf. Evidentemente el valor no puede ser 3 sino que debera ser 1 para cada mérito.
		    Esto es extensible a todas las categorías.
		</p>
		<h3>¿Por qué no puedo editar mis méritos?</h3>
		<p>Es fundamentalmente por una cuestión de seguridad.</p>
		
		<h3>No recibo mensajes de la bolsa de empleo del PDI de la Universidad en mi cuenta Hotmail/Outlook/MSN</h3>
		<p>Los servidores de Hotmail, Outlook y MSN implementan una política agresiva de correo no deseado que bloquea mensajes legítimos y no legítimos.<br/>
		Si no recibes mensajes de <strong>@ujaen.es</strong> debes añadir el dominio <strong>@ujaen.es</strong> y <strong>@red.ujaen.es</strong> como un dominio de confianza en tu configuración Hotmail.<br/>
		Sigue estos pasos para realizar la configuración. Tenga en cuenta que las opciones y textos pueden variar dependiendo de la versión e idioma de Hotmail/Outlook:</p>
		<span style="font-weight:bold">Alternativa 1: </span><br/>
		<ul>
			<li>Entra en Rueda dentada &gt; Opciones &gt; Correo &gt; Correo electrónico no deseado &gt; Remitentes seguros</li>
			<li>Añade ujaen.es</li>
			<li>Añade red.ujaen.es</li>
		</ul>
		<span style="font-weight:bold">Alternativa 2: </span><br/>
		<ul>
			<li>Entra en Rueda dentada &gt; Opciones &gt; Evitar correo no deseado &gt; Remitentes seguros y bloqueados</li>
			<li>Agregar a la lista @ujaen.es</li>
			<li>Agregar a la lista @red.ujaen.es</li>			
		</ul>
    </div>
	
</div>
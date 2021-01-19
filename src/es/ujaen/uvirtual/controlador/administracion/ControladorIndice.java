package es.ujaen.uvirtual.controlador.administracion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;

/**
 * Clase índice, símplemente muestra las opciones en la vista.
 * Servlet implementation class Indice
 * @author julopez
 */
@WebServlet(
		description = "Índice de administración",
		urlPatterns = {
 			"/srv/es/administracion",
			"/srv/en/administracion",
			"/srv/es/administracion/",
			"/srv/en/administracion/"
		},
		displayName = "administracion.indice",
		name = "administracion.indice"
)
public class ControladorIndice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String INFORMACION = "informacion";
       
    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public ControladorIndice() {
        super();
    }

	/** do get.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> informacion = new HashMap<>();
		
		// Información del contenedor
		informacion.put("Server-Local-IP", request.getLocalAddr());
		informacion.put("Server-Name", request.getServerName());
		informacion.put("Server-Port", Integer.toString(request.getServerPort()));
		informacion.put("Server-Info", request.getServletContext().getServerInfo());
		informacion.put("Server-Java", System.getProperty("java.version"));

		// Información de procesador y memoria
		Runtime runtime = Runtime.getRuntime();
		informacion.put("Available-Processors-Cores", Integer.toString(runtime.availableProcessors()));
		informacion.put("JVM-Free-Memory-Bytes", medidaMemoria(runtime.freeMemory()));
		informacion.put("JVM-Max-Memory-Bytes", (runtime.maxMemory() == Long.MAX_VALUE) ? "Unlimited" : medidaMemoria(runtime.maxMemory()));
		informacion.put("JVM-Used-Memory-Bytes", medidaMemoria(runtime.totalMemory()));

		// Información de sistemas de ficheros
		File[] roots = File.listRoots();
		for (File root : roots) {
			String filesystemPath = "Filesystem[" + root.getAbsolutePath() + "]-";
			informacion.put(filesystemPath + "Total-Space", medidaMemoria(root.getTotalSpace()));
			informacion.put(filesystemPath + "Free-Space", medidaMemoria(root.getFreeSpace()));
			informacion.put(filesystemPath + "Usable-Space", medidaMemoria(root.getUsableSpace()));
		}

		request.setAttribute(INFORMACION, informacion);
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.getFicherosJSP().add("/WEB-INF/jsp/vista/indice/indice.jsp");
	}

	/** do post.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	/** convierte a medida de memoria.
	 * @param bytes numero de bytes
	 * @return medida de memoria
	 */
	@SuppressWarnings("java:S2184")
	protected String medidaMemoria(long bytes) {
		String salida = null;
		final double kilobyte = 1024L;
		final double megabyte = 1024L * 1024L;
		final double gigabyte = 1024L * 1024L * 1024L;
		final double terabyte = 1024L * 1024L * 1024L * 1024L;
	
		double dbytes = (double) bytes;
		
		if ((bytes / terabyte) > 1) {
			salida = String.format("%4.2f TiB", dbytes / terabyte);
		}
		if ((bytes / gigabyte) > 1) {
			salida = String.format("%4.2f GiB", dbytes / gigabyte);
		}
		if ((bytes / megabyte) > 1) { 
			salida = String.format("%4.2f MiB", dbytes / megabyte);
		}
		if ((bytes / kilobyte) > 1) { 
			salida = String.format("%4.2f KiB", dbytes / kilobyte);
		}
		if (salida == null) {
			salida = Long.toString(bytes) + " B";
		}
		return salida;
	}
}

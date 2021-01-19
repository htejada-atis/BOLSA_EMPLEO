package es.ujaen.uvirtual.controlador.xdefecto;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;


/**
 * Servlet implementation class Indice.
 */
@WebServlet(
		description = "Índice por defecto",
		urlPatterns = {
 			"/srv",
			"/srv/es/index",
			"/srv/en/index",
			"/pub/es/index",
			"/pub/en/index"
		},
		displayName = "xdefecto.indice",
		name = "xdefecto.indice"
)
public class Indice extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final String ENLACES = "enlaces";
	public static final String USUARIO = "usuario";
	public static final String AVISOS = "avisos";
	public static final String ADVERTENCIAS = "advertencias";
	public static final Integer ENLACES_POR_FILA = 5;
       
    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public Indice() {
        super();
    }

	/** do get.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		Usuario usuario = datos.getUsuario();
		request.setAttribute(AVISOS, ModeloAdministracion.obtenerInstancia().listaAvisosDelSistema(usuario));
		request.setAttribute(ADVERTENCIAS, ModeloAdministracion.obtenerInstancia().listaAdvertenciasDelSistema(usuario));
		request.setAttribute(USUARIO, usuario);

		datos.getFicherosCSS().add("/css/intranet.css");
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

}

package es.ujaen.uvirtual.controlador;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.utilidades.Formateador;

/**
 * Servlet implementation class ControladorSistemaNoDisponible.
 */
@WebServlet(
		description = "",
		urlPatterns = {
 			"/nodisponible"
		},
		displayName = "ControladorSistemaNoDisponible",
		name = "ControladorSistemaNoDisponible"
)
public class ControladorSistemaNoDisponible extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorSistemaNoDisponible.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public ControladorSistemaNoDisponible() {
        super();
    }

	/** do get.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String lang = "es";
		
		RequestDispatcher despachador = null;
		try {
			String nombreFichero = "WEB-INF/jsp/vista/nodisponible.jsp";
			int posExtension = nombreFichero.lastIndexOf('.');
			String nombreLocalizado = nombreFichero.substring(0, posExtension) + "." + lang + nombreFichero.substring(posExtension);
			File fichero = new File(this.getServletContext().getRealPath(nombreLocalizado));
			if (fichero.exists()) { 
				despachador = request.getRequestDispatcher(nombreLocalizado);
				despachador.include(request, response);
			} else {
				despachador = request.getRequestDispatcher(nombreFichero);
				despachador.include(request, response);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
		}
	}

	/** do post.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

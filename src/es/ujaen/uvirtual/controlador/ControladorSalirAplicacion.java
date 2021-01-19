package es.ujaen.uvirtual.controlador;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ControladorSalirAplicacion.
 */
@WebServlet(
		description = "Controlador para salir de la aplicación (llamada al IdP para SLO y recibe los SLO)",
		urlPatterns = {
 			"/salir",
			"/slo"
		},
		displayName = "logout",
		name = "logout"
)
public class ControladorSalirAplicacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public ControladorSalirAplicacion() {
        super();
    }

	/** do get.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(true);
		sesion.invalidate();
		response.sendRedirect("/");
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

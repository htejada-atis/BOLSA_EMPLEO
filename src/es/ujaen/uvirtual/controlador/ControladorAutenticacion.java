package es.ujaen.uvirtual.controlador;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.filtros.ValidaSAML;

/**
 * Servlet implementation class ControladorAutenticacion.
 */
@WebServlet(name = "autenticacion", description = "Controlador STUB de autenticación", urlPatterns = { "/autenticacion" })
public class ControladorAutenticacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public ControladorAutenticacion() {
        super();
    }

	/** do get.
	 * @param request peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (PrintWriter writer = response.getWriter()) {
			writer.
				append("<html lang=\"es\">").
				append("<head>").
				append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />").
				append("</head>").
				append("<body>").
				append("<form method=\"post\" action=\"/sso\">").
				append("Usuario: ").
				append("<input type=\"text\" name=\"" + ValidaSAML.PARAM_USUARIO + "\" />").
				append("<input type=\"button\" value=\"autenticar\" onclick=\"this.form.submit()\" />").
				append("</form>").
				append("</body>").
				append("</html");
		}
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

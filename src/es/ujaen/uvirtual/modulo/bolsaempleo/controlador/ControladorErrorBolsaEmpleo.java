package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.ujaen.uvirtual.beans.UVDatos;

/**
 * Clase controlador para mostrar errores .
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.error", 
		description = "Controlador para los errores de bolsa empleo", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/error", 
				"/srv/en/informacionadministrativa/bolsaempleo/error"
		})
public class ControladorErrorBolsaEmpleo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /** constructor.
     * @see HttpServlet#HttpServlet()
     */
    public ControladorErrorBolsaEmpleo() {
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
		datos.getFicherosJSP().add("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/error.jsp");
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

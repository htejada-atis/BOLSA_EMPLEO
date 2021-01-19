package es.ujaen.uvirtual.controlador;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;

/** Controlador del que heredar los controladores indice.
 */
public class ControladorIndiceBase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final String vista;
	
	/** Constructor con vista.
	 * @param pvista vista que mostrar como indice
	 */
	public ControladorIndiceBase(String pvista) {
		this.vista = pvista;
	}
	
	/** do get.
	 * @param request peticion
	 * @param response respuesta de extension universitaria
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.getFicherosJSP().add(vista);
	}

	/** do post.
	 * @param request peticion de controlador indice extension universitaria
	 * @param response respuesta
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

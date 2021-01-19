package es.ujaen.uvirtual.controlador.operaciones;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		description = "Controlador índice del menú operaciones",
		urlPatterns = {
			"/srv/es/operaciones",
			"/srv/en/operaciones"
		},
		displayName = "operaciones",
		name = "operaciones"
)
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/operaciones/indice.jsp");
	}
}

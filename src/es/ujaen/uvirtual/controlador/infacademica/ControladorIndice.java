package es.ujaen.uvirtual.controlador.infacademica;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		description = "Indice de información académica",
		urlPatterns = {
			"/srv/es/informacionacademica",
			"/srv/en/informacionacademica"
		},
		displayName = "informacionacademica",
		name = "informacionacademica"
)
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/infacademica/indice.jsp");
	}
}

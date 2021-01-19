package es.ujaen.uvirtual.controlador.infgeneral;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class Indice.
 * 
 * @author julopez
 */
@WebServlet(
		description = "Servlet de entrada a información general",
		urlPatterns = {
 			"/srv/es/informaciongeneral",
			"/srv/en/informaciongeneral",
			"/pub/es/informaciongeneral",
			"/pub/en/informaciongeneral"
		},
		displayName = "informaciongeneral",
		name = "informaciongeneral"
)
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/infgeneral/indice.jsp");
	}
}

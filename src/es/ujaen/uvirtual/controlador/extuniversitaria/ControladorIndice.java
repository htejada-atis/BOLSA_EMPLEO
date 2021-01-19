package es.ujaen.uvirtual.controlador.extuniversitaria;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		description = "Índice de extensión universitaria",
		urlPatterns = {
 			"/srv/es/extensionuniversitaria",
			"/srv/en/extensionuniversitaria"
		},
		displayName = "extensionuniversitaria",
		name = "extensionuniversitaria"
)
public class ControladorIndice extends ControladorIndiceBase {

	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/extuniversitaria/indice.jsp");
	}
}

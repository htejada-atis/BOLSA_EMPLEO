package es.ujaen.uvirtual.controlador.infadministrativa.docentia;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.docentia", 
		description = "Informacion docentia, raiz", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/docentia", 
				"/srv/en/informacionadministrativa/docentia"
		})
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/infadministrativa/docentia/indice.jsp");
	}
}

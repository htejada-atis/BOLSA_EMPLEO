package es.ujaen.uvirtual.controlador.infadministrativa;

import javax.servlet.annotation.WebServlet;

import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 * 
 * @author julopez
 */
@WebServlet(
		description = "Datos índice de servicios administrativos",
		urlPatterns = {
			"/srv/es/informacionadministrativa",
			"/srv/en/informacionadministrativa"
		},
		displayName = "informacionadministrativa",
		name = "informacionadministrativa"
)
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/infadministrativa/indice.jsp");
	}
}

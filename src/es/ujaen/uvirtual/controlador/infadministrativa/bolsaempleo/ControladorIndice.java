package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import javax.servlet.annotation.WebServlet;
import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo", 
		description = "Informacion bolsa empleo, raiz", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo", 
				"/srv/en/informacionadministrativa/bolsaempleo"
		})
public class ControladorIndice extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** 
	 * Contructor por defecto.
	 */
	public ControladorIndice() {
		super("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/indice.jsp");
	}
}

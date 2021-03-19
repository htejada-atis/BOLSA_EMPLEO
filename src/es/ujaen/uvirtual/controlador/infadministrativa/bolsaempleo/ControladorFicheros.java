package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import javax.servlet.annotation.WebServlet;
import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.configuracion", 
		description = "Ficheros", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/configuracion", 
				"/srv/en/informacionadministrativa/bolsaempleo/configuracion"
		})
public class ControladorFicheros extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorFicheros() {
		super("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/configuracion/ficheros.jsp");
	}
}

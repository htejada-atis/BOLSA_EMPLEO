package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import javax.servlet.annotation.WebServlet;
import es.ujaen.uvirtual.controlador.ControladorIndiceBase;

/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.convocatorias", 
		description = "Controlador de configuración de bolsa empleo", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/convocatorias", 
				"/srv/en/informacionadministrativa/bolsaempleo/convocatorias"
		})
public class ControladorConvocatoriasMain extends ControladorIndiceBase {
	private static final long serialVersionUID = 1L;

	/** Contructor por defecto.
	 */
	public ControladorConvocatoriasMain() {
		super("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/convocatorias/indice.jsp");
	}
}

package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloOfertaCandidato {
	
	
	protected static ModeloOfertaCandidato eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloOfertaCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloOfertaCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	
	
}

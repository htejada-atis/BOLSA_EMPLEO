package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;



/**
 * Clase de modelo para la contratación .
 * 
 * @author ATISoluciones 2021
 */
public class ModeloContratacion {

	protected static ModeloContratacion eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloContratacion();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloContratacion obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
}

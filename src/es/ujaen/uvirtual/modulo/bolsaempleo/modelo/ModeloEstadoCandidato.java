package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de modelo para la gestión del estado de los candidatos . 
 * @author ATISoluciones 2021
 */
public class ModeloEstadoCandidato {
	
	public static final String ESTADO_DISPONIBLE = "DISPONIBLE";
	public static final String ESTADO_CONTRATADO = "CONTRATADO";
	public static final String ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE = "CONTRATADO 1º CUATRIMESTRE";
	public static final String ESTADO_CONTRATADO_PARCIAL = "CONTRATADO PARCIAL";
	public static final String ESTADO_SUSPENSION_PROVISIONAL = "SUSPENSION PROVISIONAL";
	public static final String ESTADO_NO_DISPONIBLE = "NO DISPONIBLE";
	public static final Map<String, String> ESTADOS = new HashMap<>();
	
	static {
		ESTADOS.put(ESTADO_DISPONIBLE, "Disponible");
		ESTADOS.put(ESTADO_CONTRATADO, "Contratado");
		ESTADOS.put(ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE, "Contratado 1º cuatrimestre");
		ESTADOS.put(ESTADO_CONTRATADO_PARCIAL, "Contratado parcial");
		ESTADOS.put(ESTADO_SUSPENSION_PROVISIONAL, "Suspensión provisional");
		ESTADOS.put(ESTADO_NO_DISPONIBLE, "No disponible");
	}

	protected static ModeloEstadoCandidato eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloEstadoCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloEstadoCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	
}

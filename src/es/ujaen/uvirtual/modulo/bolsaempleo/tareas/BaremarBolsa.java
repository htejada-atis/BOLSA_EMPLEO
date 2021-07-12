package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para la baremación de bolsas .
 * 
 * @author ATISoluciones 2021
 */
public final class BaremarBolsa {
	private static final String NOMBREDEESTACLASE = BaremarBolsa.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private BaremarBolsa() {
	}

	/**
	 * Ejecuta la tarea, si hay bolsas pendientes de baremación realiza los cálculos .
	 */
	public static void run() {
		try {
			baremarBolsas();
		} catch (SQLException | UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
	
	/**
	 * Metodo para ejecutar la tarea desde los tests.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public static void runFromTest() throws SQLException, UVException {
		baremarBolsas();
	}
	
	private static void baremarBolsas() throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloResultados modeloResultados = ModeloResultados.obtenerInstancia();
		
		List<Bolsa> bolsas = modeloBolsa.getBolsasPendientesBaremacion();
		
		for (Bolsa bolsa: bolsas) {
			List<Solicitud> solicitudes = modeloResultados.listaSolicitudesBolsa(bolsa, ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria());
			
			for (Solicitud solicitud: solicitudes) {
				modeloResultados.calcularSolicitud(solicitud, bolsa);
			}
			
			modeloBolsa.baremarBolsa(bolsa, null);
		}
	}
}

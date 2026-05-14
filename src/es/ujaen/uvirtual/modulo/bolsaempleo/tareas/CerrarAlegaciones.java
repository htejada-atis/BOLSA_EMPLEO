package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para cerrar las alegaciones de bolsas cuya fecha de fin ha expirado.
 * Las bolsas en estado ALEGACIONES con fecha de fin superada pasan a estado BLOQUEADA.
 *
 * @author ATISoluciones 2026
 */
public final class CerrarAlegaciones {
	private static final String NOMBREDEESTACLASE = CerrarAlegaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private CerrarAlegaciones() {
	}

	/**
	 * Ejecuta la tarea, si hay bolsas a cerrar por fecha fin de alegaciones, las
	 * cierra.
	 */
	public static void run() {
		try {
			ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();

			for (Bolsa bolsa : modelo.getBolsasAlegacionesExpiradas()) {
				modelo.bloquearBolsaDesdeAlegaciones(bolsa);
			}
		} catch (SQLException | UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
}

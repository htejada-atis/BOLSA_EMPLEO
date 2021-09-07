package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para cambiar plazas en estado de aceptación a contratación .
 * 
 * @author ATISoluciones 2021
 */
public final class CerrarPeriodoAceptacionPlazas {
	private static final String NOMBREDEESTACLASE = CerrarPeriodoAceptacionPlazas.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private CerrarPeriodoAceptacionPlazas() {
	}

	/**
	 * Ejecuta la tarea, si hay plazas con estado 'ABIERTA' y fecha fin de oferta mayor a la actual cambia el estado a 'CONTRATACION' .
	 */
	public static void run() {
		try {
			ModeloPlazaOfertada modeloPlaza = ModeloPlazaOfertada.obtenerInstancia();

			for (PlazaOfertada plaza: modeloPlaza.listaPlazasOfertadasAContratacion()) {
				modeloPlaza.cambiarEstadoPlazaAContratacion(plaza, null);
			}
		} catch (SQLException | UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
}

package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para la gestión de cierre de convocatorias.
 * 
 * @author ATISoluciones 2021
 */
public final class CerrarConvocatoria {
	private static final String NOMBREDEESTACLASE = CerrarConvocatoria.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private CerrarConvocatoria() {
	}

	/**
	 * Ejecuta la tarea, si hay convocatorias a cerrar por fecha de cierre, las
	 * cierra.
	 */
	public static void run() {
		try {
			ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();

			for (Convocatoria c : modelo.listaConvocatoriasACerrar()) {
				modelo.cerrarConvocatoria(c, null);
			}
		} catch (SQLException | UVException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
}

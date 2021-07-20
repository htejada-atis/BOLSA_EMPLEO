package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para actualizar áreas evaluables del director de un departamento .
 * 
 * @author ATISoluciones 2021
 */
public final class ActualizarAreasDirector {
	private static final String NOMBREDEESTACLASE = ActualizarAreasDirector.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private ActualizarAreasDirector() {
	}

	/**
	 * Ejecuta la tarea, actualiza  .
	 */
	public static void run() {
		try {
			actualizarAreas();
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
		actualizarAreas();
	}
	
	private static void actualizarAreas() throws SQLException, UVException {
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		
	}
}

package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.sql.SQLException;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para la gestión de cierre de convocatorias.
 * @author ATISoluciones 2021
 */
public final class CerrarConvocatoria {
	private CerrarConvocatoria() { }
	
	/**
	 * Ejecuta la tarea, si hay convocatorias a cerrar por fecha de cierre, las cierra.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public static void run() throws SQLException, UVException {
		for (Convocatoria c : ModeloConvocatoria.obtenerInstancia().listaConvocatoriasACerrar()) {
			cerrarConvocatoria(c);
		}
	} 
	
	private static void cerrarConvocatoria(Convocatoria c) {
		
	}
}

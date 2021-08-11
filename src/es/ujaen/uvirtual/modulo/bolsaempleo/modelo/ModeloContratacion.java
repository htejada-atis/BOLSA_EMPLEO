package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/**
 * Clase de modelo para la contratación .
 * 
 * @author ATISoluciones 2021
 */
public class ModeloContratacion {
	
	public static final String BEPPLO_CODNUM = "BEPPLO_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String FECHA_CITA = "FECHA_CITA";
	public static final String RESULTADO = "RESULTADO";
	
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
	
	/** inserta contratación .
	 * @param plaza .
	 * @param fechaCita .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error de base de datos .
	 */
	public void insertarContratacion(PlazaOfertada plaza, java.util.Date fechaCita, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = String.format("INSERT INTO TBEP_CONTRATACIONES (%s,%s,%s,%s) VALUES (?,?,?,?)", 
				BEPPLO_CODNUM, BEPUSU_CODNUM, FECHA_CITA, "UID_USUARIO");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setDate(parameterIndex++, new Date(fechaCita.getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
}

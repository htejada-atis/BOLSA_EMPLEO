package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de parametros de configuracion de la bolsa de
 * empleo .
 * 
 * @author ATISoluciones
 */
public class ModeloParametrosConfiguracion {
	public static final String VERSION = "0.81";
	public static final String JS_BOLSA_EMPLEO = "/js/bolsaempleo/bolsaempleo20210715.min.js";
	public static final String CSS_BOLSA_EMPLEO = "/css/ujaen_bolsa_empleo.css";
	public static final String JS_TINY = "/js/tinymce/tinymce.min.js";
	
	public static final String PARAMETRO_PLANTILLA_APERTURA_PLAZA = "bolsaempleo.local.idPlantillaAperturaPlaza";
	public static final String PARAMETRO_PLANTILLA_APROBACION_PLAZA = "bolsaempleo.local.idPlantillaAprobacionPlaza";
	public static final String PARAMETRO_PLANTILLA_CITA_CONTRATACION = "bolsaempleo.local.idPlantillaCitaContratacion";
	public static final String PARAMETRO_PLANTILLA_CIERRE_PLAZA = "bolsaempleo.local.idPlantillaCierrePlaza";
	public static final String PARAMETRO_EMAILS_CIERRE_PLAZA = "bolsaempleo.local.emailsCierrePlaza";
	public static final String PARAMETRO_REMITENTE = "bolsaempleo.local.remitente";

	// 50MB = 1024 * 1024 * 50 = 52428800
	public static final int MAX_FILE_SIZE = 52_428_800;
	
	protected static ModeloParametrosConfiguracion eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloParametrosConfiguracion();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloParametrosConfiguracion obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Devuelve la versión actual de la app.
	 * @return .
	 */
	public String getVersion() {
		return ModeloParametrosConfiguracion.VERSION;
	}
	
	/**
	 * Consulta los parametros en BBDD y los devuelve.
	 * 
	 * @return todas las titulaciones de la base de datos .
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<ParametrosConfiguracion> listaParametros() throws SQLException {
		List<ParametrosConfiguracion> parametros = new ArrayList<>();
		String consulta = "SELECT * FROM ADM_PARAMETROS WHERE PARAM_CODALF LIKE '%bolsaempleo%'";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ParametrosConfiguracion param = new ParametrosConfiguracion();
					param.setCodNum(rs.getString("CONFIG_CODALF"));
					param.setNombre(rs.getString("PARAM_CODALF"));
					param.setValor(rs.getString("VALOR"));
					param.setDescripcion(rs.getString("DESID"));
					param.setAdm(true);
					parametros.add(param);
				}
			}
		}

		String consultaBep = "SELECT * FROM TBEP_PARAMETROS_CONFIG";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consultaBep)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ParametrosConfiguracion param = new ParametrosConfiguracion();
					param.setCodNum(rs.getString("CODNUM"));
					param.setNombre(rs.getString("NOMBRE"));
					param.setDescripcion(rs.getString("DESCRIPCION"));
					param.setValor(rs.getString("VALOR"));
					param.setAdm(false);
					parametros.add(param);
				}
			}
		}

		return parametros;
	}

	/**
	 * Actualiza un parametro de configuración.
	 * 
	 * @param param ParametrosConfiguracion con los datos nuevos a actualizar
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaParametro(ParametrosConfiguracion param, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (param == null) {
			throw new UVException("parametro obligatorio");
		}
		if (param.getCodNum() == null) {
			throw new UVException("id parametro no válido");
		}
		if (Boolean.TRUE.equals(param.getAdm())) {
			String consulta = "UPDATE ADM_PARAMETROS SET DESID=?, VALOR=? WHERE CONFIG_CODALF=? AND PARAM_CODALF=?";
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, param.getDescripcion());
				stmt.setString(parameterIndex++, param.getValor());
				stmt.setString(parameterIndex++, param.getCodNum());
				stmt.setString(parameterIndex++, param.getNombre());
				stmt.executeUpdate();
			}
		} else {
			String consulta = "UPDATE TBEP_PARAMETROS_CONFIG SET DESCRIPCION=?,VALOR=?,UID_USUARIO=? WHERE CODNUM=? AND NOMBRE=?";
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, param.getDescripcion());
				stmt.setString(parameterIndex++, param.getValor());
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.setString(parameterIndex++, param.getCodNum());				
				stmt.setString(parameterIndex++, param.getNombre());
				stmt.executeUpdate();
			}
		}
	}

	/**
	 * Devuelve un parametro de configuración.
	 * 
	 * @param nombre del parametro a buscar .
	 * @return param .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public ParametrosConfiguracion getParametroByNombre(String nombre) throws SQLException, UVException {
		if (nombre == null) {
			throw new UVException("El nombre no puede estar vacío");
		}
		String[] nombreSplit = nombre.split("\\.");
		if (nombreSplit[1].equals("local")) {
			String consulta = "SELECT * FROM TBEP_PARAMETROS_CONFIG " + " WHERE NOMBRE = ?";

			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				stmt.setString(1, nombre);

				try (ResultSet rs = stmt.executeQuery()) {
					if (!rs.next()) {
						throw new UVException("No existe el parametro con nombre" + nombre);
					}

					ParametrosConfiguracion param = new ParametrosConfiguracion();
					param.setCodNum(rs.getString("CODNUM"));
					param.setNombre(rs.getString("NOMBRE"));
					param.setDescripcion(rs.getString("DESCRIPCION"));
					param.setValor(rs.getString("VALOR"));

					return param;
				}
			}
		} else {
			String consulta = "SELECT * FROM ADM_PARAMETROS " + " WHERE PARAM_CODALF=?";

			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				stmt.setString(1, nombre);

				try (ResultSet rs = stmt.executeQuery()) {
					if (!rs.next()) {
						throw new UVException("No existe el parametro con nombre" + nombre);
					}

					ParametrosConfiguracion param = new ParametrosConfiguracion();
					param.setCodNum(rs.getString("CONFIG_CODALF"));
					param.setNombre(rs.getString("PARAM_CODALF"));
					param.setDescripcion(rs.getString("DESID"));
					param.setValor(rs.getString("VALOR"));

					return param;
				}
			}
		}
	}

	/**
	 * Devuelve una cadena de texto con el valor máximo de subida de ficheros.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public String getMaxEspacioArchivoHuman() throws SQLException, UVException {
		Integer maxSize = Formateador.leeParametroInteger(this.getParametroByNombre("bolsaempleo.maxEspacioArchivo").getValor());
		return BolsaEmpleoUtils.humanReadableByteCountSI(maxSize);
	}
	
}

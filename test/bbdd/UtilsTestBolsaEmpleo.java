package bbdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;

/**
 * Utilidades para los test bolsa de empleo.
 */
public final class UtilsTestBolsaEmpleo {
	private static final String NOMBREDEESTACLASE = UtilsTestBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final String UID_PRUEBAS = "usig";
	private static final String UID_CANDIDATO_PRUEBAS = "candidato1";
	private static final String UID_CANDIDATO2_PRUEBAS = "candidato2";
	private static final String UID_PERSONAL_PRUEBAS = "personal1";
	private static final String ESQUEMA_ARCOS = "arcos";
	private static final String ESQUEMA_RRHH = "rrhh";
	private static final String ESQUEMA_UVIRTUAL = "uvirtual";
	private static final int NUM_AREAS_INSERTAR = 35;
	private static final Pattern RE_FILE_MIGRATION = Pattern.compile("\\d+-(im|eje)-[a-zA-Z]+\\.sql", Pattern.DOTALL);

	public static final boolean VERBOSE = false;

	private UtilsTestBolsaEmpleo() {
	}

	/**
	 * Inicializa la bd para BolsaEmpleo.
	 * 
	 * @throws Exception    .
	 * @throws SQLException .
	 * @throws IOException  .
	 */
	public static void inicializaBolsaEmpleo() throws SQLException, IOException {
		// mocks esquema arcos
		UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("MOCKS ARCOS", "Documentos/scripts/opc.bolsaempleo/mock_arcos",
				ESQUEMA_ARCOS, false);

		// mocks esquema rrhh
		UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("MOCKS RRHH", "Documentos/scripts/opc.bolsaempleo/mock_rrhh",
				ESQUEMA_RRHH, false);

		// limpieza uvirtual
		UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL CLEAN",
				"Documentos/scripts/opc.bolsaempleo/datos_desarrollo/clean", ESQUEMA_UVIRTUAL, true);

		// creación tablas uvirtual
		UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL TABLAS", "Documentos/scripts/opc.bolsaempleo",
				ESQUEMA_UVIRTUAL, false);

		// datos de pruebas => areas
		try {
			logFile("Insertando " + NUM_AREAS_INSERTAR + " primeras areas ...");
			UtilsTestBolsaEmpleo.insertarDepartamentosAreas();
			logFile("Insertando areas => OK");
		} catch (SQLException ex) {
			logFile("Error insertando areas y departamentos: " + ex, false);
			throw ex;
		}

		// datos para pruebas
		UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL DATOS PRUEBA",
				"Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba", ESQUEMA_UVIRTUAL, false);
	}

	/**
	 * obtiene una peticion autenticada con el usuario de pruebas de BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticada() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario personal5 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaPersonal() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_PERSONAL_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_PERSONAL_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario candidato1 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaCandidato() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_CANDIDATO_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_CANDIDATO_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario candidato2 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaCandidato2() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_CANDIDATO2_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_CANDIDATO2_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion sin usuario logueado .
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAnonima() {
		UVDatos datos = new UVDatos();

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	private static void ejecutarMultiplesScripts(String title, String path, String esquema, boolean reverse)
			throws IOException, SQLException {
		if (VERBOSE) {
			LOGGER.log(Level.INFO, "[{0}]", title + " esquema: " + esquema);
		}

		File directoryPath = new File(path);
		File[] filesList = directoryPath.listFiles();
		Arrays.sort(filesList);

		if (!reverse) {
			for (int i = 0; i < filesList.length; i++) {
				UtilsTestBolsaEmpleo.ejecutarFile(filesList[i], esquema);

			}
		} else {
			for (int i = filesList.length - 1; i >= 0; i--) {
				UtilsTestBolsaEmpleo.ejecutarFile(filesList[i], esquema);
			}
		}
	}

	private static void ejecutarFile(File file, String esquema) throws IOException, SQLException {
		String name = file.getName();

		// no queremos directorios
		if (!file.isDirectory()) {

			// comprobamos si el fichero tiene la estructura que queremos
			Matcher matcher = RE_FILE_MIGRATION.matcher(name);
			if (matcher.find()) {
				String operation = matcher.group(1);
				try {
					switch (operation) {
						case "im":
							UtilsTestBolsaEmpleo.ejectuarInsertMasivoFileEsquema(file, esquema);
							logFile(file.getAbsolutePath(), true);
							break;
						case "eje":
							UtilsTestBolsaEmpleo.ejectuarFileEsquema(file, esquema);
							logFile(file.getAbsolutePath(), true);
							break;
						default:
							LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el tipo: {0}", name);
					}
				} catch (IOException | SQLException ex) {
					LOGGER.log(Level.SEVERE, "ERROR EJECUTANDO FICHERO: {0}", name + " => " + ex.toString());
					throw ex;
				}
			} else {
				if (VERBOSE) {
					LOGGER.log(Level.INFO, "MIGRACION NO VÁLIDA: {0}", name);
				}
			}
		}
	}

	private static void ejectuarInsertMasivoFileEsquema(File file, String esquema) throws FileNotFoundException, SQLException {
		switch (esquema) {
			case ESQUEMA_ARCOS:
				BbddRunner.insertMasivoArcos(file.getAbsolutePath());
				break;
			case ESQUEMA_RRHH:
				BbddRunner.insertMasivoRh(file.getAbsolutePath());
				break;
			case ESQUEMA_UVIRTUAL:
				BbddRunner.insertMasivo(file.getAbsolutePath());
				break;
			default:
				LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el esquema: {0}", esquema);
		}
	}

	private static void ejectuarFileEsquema(File file, String esquema) throws SQLException, IOException {
		switch (esquema) {
			case ESQUEMA_ARCOS:
				BbddRunner.ejecutarArcos(file.getAbsolutePath());
				break;
			case ESQUEMA_RRHH:
				BbddRunner.ejecutarRh(file.getAbsolutePath());
				break;
			case ESQUEMA_UVIRTUAL:
				BbddRunner.ejecutar(file.getAbsolutePath());
				break;
			default:
				LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el esquema: {0}", esquema);
		}
	}

	private static void insertarDepartamentosAreas() throws SQLException {
		try (Connection conRh = BbddRunner.obtenerConexionRh(); Connection conUv = BbddRunner.obtenerConexionUvirtual()) {
			// departamentos

			String sqlDeptSelect = "SELECT DISTINCT uvnbrdsa.ID_DEPARTAMENTO, uvnbrdsa.DES_DEPARTAMENTO "
					+ "FROM VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa";

			try (PreparedStatement stmtSelect = conRh.prepareStatement(sqlDeptSelect)) {
				try (ResultSet rs = stmtSelect.executeQuery()) {
					while (rs.next()) {
						insertDepartamento(conUv, rs.getString("ID_DEPARTAMENTO"), rs.getString("DES_DEPARTAMENTO"));
					}
				}
			}

			// areas

			String sqlAreaSelect = "SELECT " 
					+ "		uvnbrdsa.ID_DEPARTAMENTO, "
					+ "		uvnbrdsa.ID_AREA_CONOCIMIENTO, " 
					+ "		MIN(uvnbrdsa.DES_SECCION) DES_SECCION,"
					+ "		MIN(uvnbrdsa.ID_SECCION) ID_SECCION, "
					+ "		MIN(uvnbrdsa.DES_AREA_CONOCIMIENTO) DES_AREA_CONOCIMIENTO "
					+ " FROM VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa "
					+ " GROUP BY uvnbrdsa.ID_DEPARTAMENTO, uvnbrdsa.ID_AREA_CONOCIMIENTO "
					+ " ORDER BY uvnbrdsa.ID_AREA_CONOCIMIENTO " + " FETCH FIRST " + NUM_AREAS_INSERTAR + " ROW ONLY";

			try (PreparedStatement stmtSelect = conRh.prepareStatement(sqlAreaSelect)) {
				try (ResultSet rs = stmtSelect.executeQuery()) {
					while (rs.next()) {
						Integer idDepartamento = getDepartamentoByCodigoRh(conUv, rs.getString("ID_DEPARTAMENTO"));

						if (idDepartamento != null) {
							Integer idArea = insertArea(conUv, rs.getString("ID_AREA_CONOCIMIENTO"),
									rs.getString("DES_AREA_CONOCIMIENTO"));
							insertAreaDepartamento(idDepartamento, idArea, rs.getString("ID_SECCION"),
									rs.getString("DES_SECCION"));
						} else {
							throw new SQLException("CARGA DE AREAS FAIL, DEPARTAMENTO NO ENCONTRADO "
									+ rs.getString("ID_DEPARTAMENTO"));
						}
					}
				}
			}
		}
	}

	private static Integer getDepartamentoByCodigoRh(Connection con, String cod) throws SQLException {
		String sql = "SELECT bepdep.CODNUM FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.ID_DEPARTAMENTO = ? FETCH FIRST 1 ROW ONLY";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, cod);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("CODNUM");
				}
			}
		}

		return null;
	}

	private static void insertDepartamento(Connection con, String idDept, String descripcion) throws SQLException {
		String sql = "INSERT INTO UVIRTUAL.TBEP_DEPARTAMENTOS (ID_DEPARTAMENTO, DES_DEPARTAMENTO) VALUES (?,?)";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idDept);
			stmt.setString(parameterIndex++, descripcion);
			stmt.executeUpdate();
		}
	}

	private static Integer insertArea(Connection con, String idAreaConocimiento, String descripcion) throws SQLException {
		// comprobamos si está ya el area en el sistema
		String sql = "SELECT bepare.CODNUM FROM TBEP_AREAS bepare WHERE bepare.ID_AREA_CONOCIMIENTO = ?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idAreaConocimiento);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("CODNUM");
				}
			}
		}

		// insertamos
		sql = "INSERT INTO TBEP_AREAS (ID_AREA_CONOCIMIENTO, DES_AREA_CONOCIMIENTO) VALUES (?,?)";
		try (PreparedStatement stmt = con.prepareStatement(sql, new String[] {"CODNUM"})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idAreaConocimiento);
			stmt.setString(parameterIndex++, descripcion);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		}
	}

	private static void insertAreaDepartamento(Integer idDepartamento, Integer idArea, String idSeccion, String descSeccion) throws SQLException {
		String sql = "INSERT INTO TBEP_AREAS_DEPARTAMENTOS (BEPARE_CODNUM,BEPDEP_CODNUM,ID_SECCION,DES_SECCION) VALUES (?,?,?,?)";
		try (Connection con = BbddRunner.obtenerConexionUvirtual(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idArea);
			stmt.setInt(parameterIndex++, idDepartamento);
			stmt.setString(parameterIndex++, idSeccion);
			stmt.setString(parameterIndex++, descSeccion);
			stmt.executeUpdate();
		}
	}

	private static void logFile(String name, boolean ok) {
		logFile(String.format("%s%s", name, ok ? " => OK" : ""));
	}

	private static void logFile(String name) {
		if (VERBOSE) {
			LOGGER.log(Level.INFO, "SQL {0}", name);
		}
	}
}

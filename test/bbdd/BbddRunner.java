package bbdd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.Memcache;
import oracle.jdbc.pool.OracleDataSource;

/** Clase para conexión a bbdd y carga de datos para las pruebas. */
public class BbddRunner {
	private static final String NOMBREDEESTACLASE = BbddRunner.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
    private static String cadenaConexionDefecto = "jdbc:oracle:thin:@jenkins.ujaen.es:1521:XE";
    private static String usuarioBdUv = "uvirtual";
    private static String usuarioBdArcos = "arcos";
    private static String usuarioBdRh = "uxxirrhh";
    private static String usuarioBdAc = "uxxiac";
    private static String pwBd = "pwd123";
    private static OracleDataSource odsUv = null;
    private static OracleDataSource odsArcos = null;
    private static OracleDataSource odsRh = null;
    private static OracleDataSource odsAc = null;
    
    private static String servidorMemcacheDefecto = "jenkins.ujaen.es";

    public static final boolean VERBOSE = true;
	
    private BbddRunner() { }
	
    static {
    	LOGGER.log(Level.INFO, "inicializacion de oracle");
		try {
			LOGGER.log(Level.INFO, "Conexión BBDD: {0}", getCadenaConexionBd());
			odsUv = new OracleDataSource();
			odsUv.setURL(getCadenaConexionBd());
			odsUv.setUser(usuarioBdUv);
			odsUv.setPassword(pwBd);

			odsArcos = new OracleDataSource();
			odsArcos.setURL(getCadenaConexionBd());
			odsArcos.setUser(usuarioBdArcos);
			odsArcos.setPassword(pwBd);

			odsRh = new OracleDataSource();
			odsRh.setURL(getCadenaConexionBd());
			odsRh.setUser(usuarioBdRh);
			odsRh.setPassword(pwBd);

			odsAc = new OracleDataSource();
			odsAc.setURL(getCadenaConexionBd());
			odsAc.setUser(usuarioBdAc);
			odsAc.setPassword(pwBd);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** obtiene el datasource de la bbdd.
	 * @return data source de la bbdd
	 */
	public static DataSource obtenerDataSourceUv() {
		return odsUv;
	}
	
	/** obtiene el datasource de la bbdd.
	 * @return data source de la bbdd
	 */
	public static DataSource obtenerDataSourceArcos() {
		return odsArcos;
	}

	/** obtiene el datasource de la bbdd.
	 * @return data source de la bbdd
	 */
	public static DataSource obtenerDataSourceRh() {
		return odsRh;
	}
	
	/** obtiene el datasource de la bbdd.
	 * @return data source de la bbdd
	 */
	public static DataSource obtenerDataSourceAc() {
		return odsAc;
	}

	/** obtiene la conexión con uvirtual.
	 * @return .
	 * @throws SQLException .
	 */
	public static Connection obtenerConexionUvirtual() throws SQLException {
		return odsUv.getConnection();
	}

	/** obtiene la conexión con arcos.
	 * @return .
	 * @throws SQLException .
	 */
	public static Connection obtenerConexionArcos() throws SQLException {
		return odsArcos.getConnection();
	}
	
	private static Connection obtenerConexionAc() throws SQLException {
		return odsAc.getConnection();
	}	
	
	/** obtiene la conexión con rrhh.
	 *
	 * @return .
	 * @throws SQLException .
	 */
	public static Connection obtenerConexionRh() throws SQLException {
		return odsRh.getConnection();
	}	
	
	/** conectar a todas las db.
	 */
	public static void conectarBd() {
    	DataSource dsUv = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(dsUv);
    	DataSource dsArcos = BbddRunner.obtenerDataSourceArcos();
    	Conexion.setConexionArcos(dsArcos);
    	DataSource dsRh = BbddRunner.obtenerDataSourceRh();
    	Conexion.setConexionUxxiRrhh(dsRh);
    	DataSource dsAc = BbddRunner.obtenerDataSourceAc();
    	Conexion.setConexionUxxiAc(dsAc);
    	System.setProperty("memcacheUrl", BbddRunner.getServidorMemcache());
    	System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SunLogger");
    	Logger.getLogger("net.spy.memcached").setLevel(Level.WARNING);
    	Memcache.getInstance();
    	Memcache.disable();
	}
	
	/** ejecuta instrucciones SQL del fichero separadas por ; en bbdd uvirtual.
	 * 
	 * <p>los comentarios deben empezar por -- y terminar en la misma linea en ;.</p>
	 * @param rutaFichero ruta del fichero a cargar
	 * @throws SQLException si error en bd
	 * @throws FileNotFoundException si no existe fichero
	 */
	public static void insertMasivo(String rutaFichero) throws SQLException, FileNotFoundException {
		try (Connection con = obtenerConexionUvirtual()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaInsertMasivo(s, con);
			}
		}
	}
	
	/** ejecuta instrucciones SQL del fichero separadas por ; en bbdd arcos.
	 * 
	 * <p>los comentarios deben empezar por -- y terminar en la misma linea en ;.</p>
	 * @param rutaFichero ruta del fichero a cargar
	 * @throws SQLException si error en bd
	 * @throws FileNotFoundException si no existe fichero
	 */
	public static void insertMasivoArcos(String rutaFichero) throws SQLException, FileNotFoundException {
		try (Connection con = obtenerConexionArcos()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaInsertMasivo(s, con);
			}
		}
	}

	/** ejecuta instrucciones SQL del fichero separadas por ; en bbdd uxxiac.
	 * 
	 * <p>los comentarios deben empezar por -- y terminar en la misma linea en ;.</p>
	 * @param rutaFichero ruta del fichero a cargar
	 * @throws SQLException si error en bd
	 * @throws FileNotFoundException si no existe fichero
	 */
	public static void insertMasivoAc(String rutaFichero) throws SQLException, FileNotFoundException {
		try (Connection con = obtenerConexionAc()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaInsertMasivo(s, con);
			}
		}
	}
	
	/** ejecuta instrucciones SQL del fichero separadas por ; en bbdd uxxiac.
	 * 
	 * <p>los comentarios deben empezar por -- y terminar en la misma linea en ;.</p>
	 * @param rutaFichero ruta del fichero a cargar
	 * @throws SQLException si error en bd
	 * @throws FileNotFoundException si no existe fichero
	 */
	public static void insertMasivoRh(String rutaFichero) throws SQLException, FileNotFoundException {
		try (Connection con = obtenerConexionRh()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaInsertMasivo(s, con);
			}
		}
	}

	private static void procesarLineaInsertMasivo(Scanner s, Connection conexion) throws SQLException {
		s.useDelimiter(";");
		while (s.hasNext()) {
			String instruccion = s.next().trim();
			if (VERBOSE) {
				LOGGER.log(Level.INFO, instruccion);
				LOGGER.log(Level.INFO, "-------------------------------------------------");
			}
			if (instruccion.startsWith("--")) {
				LOGGER.log(Level.INFO, instruccion);
			} else {
				try (Statement statement = conexion.createStatement()) {
					statement.execute(instruccion);
				}
			}
		}
	}
	
	private static void procesarLineaEjecutar(Scanner s, Connection conexion) throws SQLException {
		final int longitudMinimaIntruccion = 10;
		s.useDelimiter("--/////////////////////");
		while (s.hasNext()) {
			boolean ejecutado = false;
			boolean quitarFinal = true;
			String instruccion = s.next().trim();
			instruccion = limpiaComentarios(instruccion);
			instruccion = instruccion.trim();
			instruccion = instruccion.replace("CREATE TRIGGER", "create trigger");
			instruccion = instruccion.replace("DROP", "drop");
			if (instruccion.startsWith("create trigger")) {
				quitarFinal = false;
			}
			if (quitarFinal && instruccion.endsWith(";")) {
				instruccion = instruccion.substring(0, instruccion.length() - 1);
			}
			if (VERBOSE) {
				LOGGER.log(Level.INFO, instruccion);
				LOGGER.log(Level.INFO, "-------------------------------------------------");
			}
			if (instruccion.startsWith("drop")) {
				String[] partes = instruccion.split(" ");
				drop(conexion, partes[1], partes[2]);
				ejecutado = true;
			}
			if (instruccion.startsWith("--")) {
				ejecutado = true;
			}
			if (!ejecutado && instruccion.length() > longitudMinimaIntruccion) {
				try (Statement statement = conexion.createStatement()) {
					statement.execute(instruccion);
				}
			}
		}
	}
	
	private static String limpiaComentarios(String cadena) {
		StringBuilder salida = new StringBuilder();
		String[] lineas = cadena.split("\\r?\\n");
		for (String linea : lineas) {
			int offset = linea.indexOf("--");
			if (-1 != offset) {
			    salida.append(linea.substring(0, offset));
			} else {
				salida.append(linea);
			}
			salida.append(System.lineSeparator());
		}
		return salida.toString();
	}

	/** ejecutar fichero con instrucciones SQL separada por el delimitadoren bd uvirtual.
	 * @param rutaFichero ruta del fichero a cargar en la bd
	 * @throws IOException si error lectura fichero
	 * @throws SQLException si error en bd
	 */
	public static void ejecutar(String rutaFichero) throws IOException, SQLException {
		try (Connection con = obtenerConexionUvirtual()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaEjecutar(s, con);
			}
		}
	}
	
	/** ejecutar fichero con instrucciones SQL separada por el delimitadoren bd Arcos.
	 * @param rutaFichero ruta del fichero a cargar en la bd
	 * @throws IOException si error lectura fichero
	 * @throws SQLException si error en bd
	 */
	public static void ejecutarArcos(String rutaFichero) throws IOException, SQLException {
		try (Connection con = obtenerConexionArcos()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaEjecutar(s, con);
			}
		}
	}
	
	/** ejecutar fichero con instrucciones SQL separada por el delimitadoren bd uxxiac.
	 * @param rutaFichero ruta del fichero a cargar en la bd
	 * @throws IOException si error lectura fichero
	 * @throws SQLException si error en bd
	 */
	public static void ejecutarAc(String rutaFichero) throws IOException, SQLException {
		try (Connection con = obtenerConexionAc()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaEjecutar(s, con);
			}
		}
	}

	/** ejecutar fichero con instrucciones SQL separada por el delimitadoren bd rrhh.
	 * @param rutaFichero ruta del fichero a cargar en la bd
	 * @throws IOException si error lectura fichero
	 * @throws SQLException si error en bd
	 */
	public static void ejecutarRh(String rutaFichero) throws IOException, SQLException {
		try (Connection con = obtenerConexionRh()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				procesarLineaEjecutar(s, con);
			}
		}
	}

	/** Borrar elemento de la bbdd.
	 * @param conexion conexion a la bd
	 * @param tipo tipo de elemento, como tabla, secuencia, etc
	 * @param nombre nombre del elemento a borrar
	 * @throws SQLException si error en bd
	 */
	private static void drop(Connection conexion, String tipo, String nombre) throws SQLException {
		String sql = "BEGIN"
				   + "  EXECUTE IMMEDIATE 'DROP " + tipo + " " + nombre + "'; "
				   + "  EXCEPTION " 
				   + "    WHEN OTHERS THEN "
				   + "      IF ((SQLCODE != -942) AND (SQLCODE != -2289)) THEN "
				   + "        RAISE; "
				   + "      END IF; "
				   + "END;";
		try (Statement stmt = conexion.createStatement()) {
			stmt.execute(sql);
		}
	}
	
	/** devuelve el servidor memcache.
	 * @return servidor memcache a usar
	 */
	public static String getServidorMemcache() {
		String memcacheSystem = System.getProperty("memcacheUrl");
		if (memcacheSystem != null && !"".equals(memcacheSystem)) {
			return memcacheSystem;
		}
		return servidorMemcacheDefecto;
	}
	
	private static String getCadenaConexionBd() {
		String urlDbSystem = System.getProperty("dbUrl");
		if (urlDbSystem != null && !"".equals(urlDbSystem)) {
			return "jdbc:oracle:thin:@" + urlDbSystem + ":1521:XE";
		}
		return cadenaConexionDefecto;
	}
	
	
	/**
	 * Consulta SQL para Insertar y Eliminar datos de  prueba individual.
	 * @param consulta SQL
	 */
	public static void insertDelete(String consulta) {
		try (Connection conexion = obtenerConexionUvirtual();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.logp(Level.SEVERE, NOMBREDEESTACLASE, "Exception SQL ", e.getMessage());
		} 
	} 	
}

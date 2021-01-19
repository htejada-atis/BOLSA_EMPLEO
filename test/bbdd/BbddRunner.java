package bbdd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
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
	
    private static String cadenaConexion = "jdbc:oracle:thin:@192.168.56.10:1521:XE";
    private static String usuarioBdUv = "uvirtual";
    private static String usuarioBdArcos = "arcos";
    private static String usuarioBdRh = "uxxirrhh";
    private static String usuarioBdAc = "uxxiac";
    private static String pwBd = "pwd123";
    private static OracleDataSource odsUv = null;
    private static OracleDataSource odsArcos = null;
    private static OracleDataSource odsRh = null;
    private static OracleDataSource odsAc = null;
    
    private static String servidorMemcache = "192.168.56.10";

    public static final boolean VERBOSE = false;
	
    private BbddRunner() { }
	
    static {
    	LOGGER.log(Level.INFO, "inicializacion de oracle");
		try {
			odsUv = new OracleDataSource();
			odsUv.setURL(cadenaConexion);
			odsUv.setUser(usuarioBdUv);
			odsUv.setPassword(pwBd);

			odsArcos = new OracleDataSource();
			odsArcos.setURL(cadenaConexion);
			odsArcos.setUser(usuarioBdArcos);
			odsArcos.setPassword(pwBd);

			odsRh = new OracleDataSource();
			odsRh.setURL(cadenaConexion);
			odsRh.setUser(usuarioBdRh);
			odsRh.setPassword(pwBd);

			odsAc = new OracleDataSource();
			odsAc.setURL(cadenaConexion);
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

	private static Connection obtenerConexionUvirtual() throws SQLException {
		return odsUv.getConnection();
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
	
	/** ejecuta instrucciones SQL del fichero separadas por ;.
	 * @param rutaFichero ruta del fichero a cargar
	 * @throws SQLException si error en bd
	 * @throws FileNotFoundException si no existe fichero
	 */
	public static void insertMasivo(String rutaFichero) throws SQLException, FileNotFoundException {
		try (Connection con = obtenerConexionUvirtual();
			 Statement statement = con.createStatement()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				s.useDelimiter(";");
				while (s.hasNext()) {
					String instruccion = s.next().trim();
					if (VERBOSE) {
						LOGGER.log(Level.INFO, instruccion);
						LOGGER.log(Level.INFO, "-------------------------------------------------");
					}
					statement.execute(instruccion);
				}
			}
		}
	}
	
	/** ejecutar fichero con instrucciones SQL separada por el delimitador.
	 * @param rutaFichero ruta del fichero a cargar en la bd
	 * @throws IOException si error lectura fichero
	 * @throws SQLException si error en bd
	 */
	public static void ejecutar(String rutaFichero) throws IOException, SQLException {
		final int longitudMinimaIntruccion = 10;
		try (Connection con = obtenerConexionUvirtual();
			 Statement statement = con.createStatement()) {
			try (Scanner s = new Scanner(new BufferedReader(new FileReader(rutaFichero)))) {
				s.useDelimiter("--/////////////////////");
				while (s.hasNext()) {
					boolean ejecutado = false;
					boolean quitarFinal = true;
					String instruccion = s.next().trim();
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
						drop(partes[1], partes[2]);
						ejecutado = true;
					}
					if (instruccion.startsWith("--")) {
						ejecutado = true;
					}
					if (!ejecutado && instruccion.length() > longitudMinimaIntruccion) {
							statement.execute(instruccion);
					}
				}
			}
		}
	}
	
	/** Borrar elemento de la bbdd.
	 * @param tipo tipo de elemento, como tabla, secuencia, etc
	 * @param nombre nombre del elemento a borrar
	 * @throws SQLException si error en bbdd
	 */
	private static void drop(String tipo, String nombre) throws SQLException {
		String sql = "BEGIN"
				   + "  EXECUTE IMMEDIATE 'DROP " + tipo + " " + nombre + "'; "
				   + "  EXCEPTION " 
				   + "    WHEN OTHERS THEN "
				   + "      IF ((SQLCODE != -942) AND (SQLCODE != -2289)) THEN "
				   + "        RAISE; "
				   + "      END IF; "
				   + "END;";
		try (Connection con = obtenerConexionUvirtual();
			 Statement stmt = con.createStatement()) {
			stmt.execute(sql);
		}
	}
	
	public static String getServidorMemcache() {
		return servidorMemcache;
	}
}

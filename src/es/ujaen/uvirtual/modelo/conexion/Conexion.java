package es.ujaen.uvirtual.modelo.conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/** clase conexion singleton.
 */
public class Conexion {
	private static String eNombreDeEstaClase = Conexion.class.getName();
	private static Conexion eInstancia = null;
    
    private static Context envContext = null;
    private static DataSource eDataSourceUvirtual = null;
    private static DataSource eDataSourceArcos = null;
    private static DataSource eDataSourceRrhh = null;
    private static DataSource eDataSourceAc = null;
    private static final Logger ELOGGER = Logger.getLogger(eNombreDeEstaClase);

    /**
     * Constructor privado de modo que no se pueden crear clases del objeto. 
     */
    protected Conexion() { }
    
    /**
     * Establece la conexión con la base de datos.
     */
    protected static void establecerDataSource() {
    	ELOGGER.logp(Level.FINE, eNombreDeEstaClase, "establecerDataSource", "Estableciendo contexto");
    	try {
    		if (envContext == null) {
    			Context initContext = new InitialContext();
    			envContext = (Context) initContext.lookup("java:/comp/env");
    		}
    		eDataSourceUvirtual = establecerDataSourceJndi("jdbc/uvirtual");
    		eDataSourceArcos = establecerDataSourceJndi("jdbc/arcos");
    		eDataSourceRrhh = establecerDataSourceJndi("jdbc/uxxirrhh");
    		eDataSourceAc = establecerDataSourceJndi("jdbc/uxxiac");
    	} catch (NamingException e) {
    		ELOGGER.logp(Level.WARNING, eNombreDeEstaClase, "establecerDataSource", "Imposible establecer contexto");
    	}
    }
    
    private static DataSource establecerDataSourceJndi(String cadena) {
    	ELOGGER.logp(Level.FINE, eNombreDeEstaClase, "establecerDataSourceJndi", "Estableciendo conexión con " + cadena);
		DataSource salida = null;
    	try {
	        salida = (DataSource) envContext.lookup(cadena);
    	} catch (NamingException e) {
    		ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, "establecerDataSourceJndi", "Imposible recuperar información de " + cadena);
    	} 
    	return salida;
    }
    
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new Conexion();
			establecerDataSource();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static Conexion obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
    
    protected Connection conexionUvirtual() throws SQLException {
    	if (eDataSourceUvirtual == null) {
    		throw new SQLException("Imposible obtener conexión de Uvirtual");
    	}
    	return eDataSourceUvirtual.getConnection();
    }

    protected Connection conexionArcos() throws SQLException {
    	if (eDataSourceArcos == null) {
    		throw new SQLException("Imposible obtener conexión de Arcos");
    	}
    	return eDataSourceArcos.getConnection();
    }

    protected Connection conexionUxxiRrhh() throws SQLException {
    	if (eDataSourceRrhh == null) {
    		throw new SQLException("Imposible obtener conexión de RRHH");
    	}
    	return eDataSourceRrhh.getConnection();
    }
    
    protected Connection conexionUxxiAc() throws SQLException {
    	if (eDataSourceAc == null) {
    		throw new SQLException("Imposible obtener conexión de AC");
    	}
    	return eDataSourceAc.getConnection();
    }

    /** Solo usado para las pruebas unitarias.
     * @param dataSource datasource
     */
    public static void setConexionUvirtual(DataSource dataSource) {
    	eDataSourceUvirtual = dataSource;
    }
    
    /** Solo usado para las pruebas unitarias.
     * @param dataSource datasource
     */
    public static void setConexionArcos(DataSource dataSource) {
    	eDataSourceArcos = dataSource;
    }

    /** Solo usado para las pruebas unitarias.
     * @param dataSource datasource
     */
    public static void setConexionUxxiRrhh(DataSource dataSource) {
    	eDataSourceRrhh = dataSource;
    }

    /** Solo usado para las pruebas unitarias.
     * @param dataSource datasource
     */
    public static void setConexionUxxiAc(DataSource dataSource) {
    	eDataSourceAc = dataSource;
    }
    
}

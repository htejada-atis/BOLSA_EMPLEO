package es.ujaen.uvirtual.utilidades;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * Clase para la gestión del memcached.
 * Información obtenida de: http://sacharya.com/using-memcached-with-java/
 * 
 * @author julopez
 *
 */
public class Memcache {
	private static String fNamespace = "";
	
	@SuppressWarnings("checkstyle:magicnumber")
	private static int timeToLive = 60 * 60 * 2; // 2 horas
	private static boolean enabled = false;
	private static Memcache fInstance = null;
	private static MemcachedClient[] fClient = null;
	private static HashMap<String, String> fKeys = null;
    private static final Logger FLOGGER = Logger.getLogger(Memcache.class.getPackage().getName());
	private static String fClassName = Memcache.class.getName();
    
	// Comstructor privado, de modo que sólo se permita singleton
	private Memcache() {	
	}
	
    /**
     * Reconexión con los servidores.
     */
	private static void getConnection() {
		fNamespace = ConfiguracionGlobal.getMemcacheNamespace();
		int poolsize = 1;
		enabled = ConfiguracionGlobal.isMemcacheEnabled();
		timeToLive = ConfiguracionGlobal.getMemcacheTTL();

        // Configuramos el logger para que solo muestre de advertencias para arriba...
        Logger.getLogger("net.spy.memcached").setLevel(Level.WARNING);
		
		try {
			if (fKeys == null) {
				fKeys = new HashMap<>();
			}
			if (enabled) {
				fClient = new MemcachedClient[poolsize];
				for (int i = 0; i < poolsize; i++) {
					MemcachedClient c = new MemcachedClient(
							new BinaryConnectionFactory(), 
							AddrUtil.getAddresses(ConfiguracionGlobal.getMemcacheServers())
					);
					fClient[i] = c;
				}
			}
		} catch (Exception e) {
    		FLOGGER.logp(Level.SEVERE, fClassName, "Memcache", 
    				"Error estableciendo conexión memcache: " + ConfiguracionGlobal.getMemcacheServers() + " " + e.toString());
		} 
		
	}

    /**
     * Forzar la reconexión con el servidor.
     */
    public void refreshConnection() {
    	getConnection();
    }

	
	/**
	 * Obtiene la instancia singleton del memcache.
	 * @return Memcache la instancia singleton de memcache
	 */
	public static synchronized Memcache getInstance() {
		if (fInstance == null) {
			fInstance = new Memcache();
			getConnection();
		}
		return fInstance;
	}

	/**
	 * Define un nuevo valor en el memcache (el TTL viene definido por la configuración).
	 * @param key clave a definir
	 * @param o valor para la clave
	 */
	public void set(String key, final Object o) {
		if (enabled) {
			MemcachedClient c = getCache();
			if (c != null) {
				fKeys.put(fNamespace + key, "x");
				c.set(fNamespace + key, timeToLive, o);
			}
		}
	}

	/**
	 * Obtiene un valor del memcache.
	 * @param key clave a obtener
	 * @return valor de la clave | null
	 */
	public Object get(String key) {
		if (enabled) {
			MemcachedClient c = getCache();
			Object o = null;
			if (c != null) {
				Future<Object> f = c.asyncGet(fNamespace + key);
				try {
					final int timeout = 5;
					o = f.get(timeout, TimeUnit.SECONDS);
				} catch (Exception e) {
					try {
						f.cancel(true);
					} catch (Exception ex) { 
						// no se hace nada
					}
					getConnection();
				}
		    }
			FLOGGER.logp(Level.FINEST, fClassName, "get", (o == null) ? "memcache miss para " : "memcache hit para " + key);
			return o;
		} else {
			return null;
		}
	}

	/**
	 * Borrado de un valor del memcache.
	 * @param key clave a borrar
	 * @return objeto borrado
	 */
	public Object delete(String key) {
		if (enabled) {
			MemcachedClient c = getCache();
			if (c != null) {
				fKeys.remove(fNamespace + key);
				return c.delete(fNamespace + key);
			}
		}
		return null;
			
	}

	/**
	 * Obtiene una conexión al memcache del pool interno de conexiones.
	 * @return MemcachedClient la conexión con el memcache
	 */
	private MemcachedClient getCache() {
		MemcachedClient c = null;
		try {
			c = fClient[0];
		} catch (Exception e) {
			//
		}
		return c;
	}
	

	/**
	 * Habilita la conexión memcache.
	 */
	public static void enable() {
		enabled = true;
		getConnection();
	}


	/**
	 * Desactiva la conexión memcaché y cierra las conexiones actuales.
	 */
	public static void disable() {
		enabled = false;
		if (fClient != null) {
			for (int i = 0; i < fClient.length; i++) {
				try {
					MemcachedClient c = fClient[i];
					c.shutdown();
				} catch (Exception e) {
					//
				}
			}
			fClient = null;
		}
	}
	
	/**
	 * Elimina todas las claves definidas en el servidor memcached.
	 */
	public void invalidateAllKeys() {
		Iterator<String> it = fKeys.keySet().iterator();
		MemcachedClient c = null;
		c = getCache();
		if (c != null) {
			while (it.hasNext()) {
				c.delete(it.next());
			}
			fKeys.clear();
		}
	}
	
	/** get stats.
	 * @return stats
	 */
	public static Map<SocketAddress, Map<String, String>> getStats() {
		if (enabled) {
			MemcachedClient c = fClient[0];
			if (c != null) {
				try {
					return c.getStats();
				} catch (Exception e) {
					//
				}
			}
		}
		return null;
	}
}

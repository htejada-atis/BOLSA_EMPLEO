package es.ujaen.uvirtual.utilidades.ficheros;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import java.util.logging.Logger;

import java.util.Base64;
import java.util.Base64.Encoder;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Utilidades para la grabaciÃ³n en el sistema de archivos de campos de bbdd que contienen ficheros.
 * 
 * @author jalucena
 *
 */
 
public class FileSystemUtils {
	
	private FileSystemUtils() {
		//no se puede instanciar
	}

    private static final Logger log = Logger.getLogger(FileSystemUtils.class.getName());

    public static String subdir(Long id) {
    
        int nivel = ConfiguracionGlobal.getParametroEnteroNE("filesystem.nivel.subdirectorios");
    
        String base = "0";
        for (int i = 0; i < nivel; i++) {
            base += "0";
        }
        base += id.toString();
        log.fine("Subdir (" + id + "): " + base.substring(base.length() - nivel));
        return base.substring(base.length() - nivel);
    }

    public static String getPath(Long id, String esquema, String tabla) {
        
        String path = ConfiguracionGlobal.getParametroCadenaNE("filesystem.directoriobd");  
        
        path += System.getProperty("file.separator")
        + esquema
        + System.getProperty("file.separator")
        + tabla
        + System.getProperty("file.separator")
        + subdir(id)
        + System.getProperty("file.separator")
        + id.toString()
        + System.getProperty("file.separator");
        log.fine("Path (" + esquema + "." + tabla + "." + id + "): " + path);
        return path;
    }

    public static byte[] obtenerFichero(Long id, String esquema, String tabla, String campo) {
        
    	log.fine("ObtenerFichero (" + esquema + "." + tabla + "." + id + "." + campo + ")");
    	Path fichero = Paths.get(getPath(id, esquema, tabla) + id.toString() + "." + campo);
    	
    	if (fichero.toFile().exists()) {
    		log.fine("ObtenerFichero (" + esquema + "." + tabla + "." + id + "." + campo + ") desde filesystem");
    		try {
    			return Files.readAllBytes(fichero);
    		} catch (IOException e) { 
    			log.severe("ERROR ObtenerFichero: " + e.getMessage());
    		}
    	}

    	log.fine("ObtenerFichero (" + esquema + "." + tabla + "." + id + "." + campo + ") desde db");
       
        return null;
    }


    public static boolean grabarFichero(Long id, String esquema, String tabla, String campo, byte[] datos) throws IOException {
        // Si no existe la ruta la crea
        Path subdir = Paths.get(getPath(id, esquema, tabla));
        Files.createDirectories(subdir);

        Path fichero = Paths.get(getPath(id, esquema, tabla) + id.toString() + "." + campo);
        
        log.fine("grabarFichero: " + fichero.toString());
        Files.write(fichero, datos); // Por defecto create, truncate_existing, write
        return true;
    }


    public static long tamanio(Long id, String esquema, String tabla, String campo) throws IOException {
        long tamanio = -1;
        
        log.fine("tamanioArchivo (" + esquema + "." + tabla + "." + id + "." + campo + ")");
        Path fichero = Paths.get(getPath(id, esquema, tabla) + id.toString() + "." + campo);
        
        if (fichero.toFile().exists()) {
        	tamanio = Files.size(fichero);
            log.fine("tamanio (" + esquema + "." + tabla + "." + id + "." + campo + "): " + tamanio + " desde filesystem");
        }
        
        return tamanio;
    }

    public static boolean existeFichero(Long id, String esquema, String tabla, String campo) throws IOException {
        Path fichero = Paths.get(getPath(id, esquema, tabla) + id.toString() + "." + campo);
        log.fine("existe fichero: " + fichero.toString());
        return fichero.toFile().exists();
    }
    
    @SuppressWarnings("unused")
	public void fromInputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
    	int read;
    	int total = 0;
    	byte[ ] buffer = new byte[2048];
    	while ((read = is.read(buffer)) > 0) {
    		total += read;
    		os.write(buffer, 0, read);
    	}
	}

    @SuppressWarnings("unused")
	public static byte[] fromInputStreamToByteArray(InputStream is) throws UVException {
    	byte[] bytes = null;
    	try {
    		bytes = IOUtils.toByteArray(is);
    	} catch (Exception e) {
    		log.severe("ERROR: fromInputStreamToByteArray, " + e.getMessage());
    		throw new UVException(e.getMessage());
    	}
    	  return bytes;
    }    
    
}
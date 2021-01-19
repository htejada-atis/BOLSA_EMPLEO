package es.ujaen.uvirtual.utilidades;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;

/**
 * Clase para extraer/generar URLs para el sistema de universidad virtual.
 * Se supone que las urls tendran el formato (camino_hasta_posicion_inicial)/idioma/[formato]/controlador/[sep parametros]/[parametros]
 * @author julopez
 */
public class AyudaURL {
	public static final String PREFIJO_URL_ERROR = "/error";
	public static final String PREFIJO_URL_PUBLICA = "/pub/";
	public static final String PREFIJO_URL_AUTENTICADA = "/srv/";
	
	public static final String FORMATO_HTML = "html";
	public static final String FORMATO_PDF = "pdf";
	public static final String FORMATO_AJAX = "ajax";
	public static final String FORMATO_WORD = "docx";
	public static final String FORMATO_EXCEL = "xlsx";
	public static final String FORMATO_CSV = "csv";
	

	/**
	 * Posición de la url en la que empiezan los datos.
	 */
	private static String separadorDeParametros = null;
	private static String divisor = "/";
	private static String idiomaPorDefecto = null;
	private static String controladorPorDefecto = null;
	private static String prefijoUrlProtegida = null;
	private static String prefijoUrlAnonimo = null;	
	private static String formatosSoportados = null;
	private static String formatoPorDefecto = null;
	private static int posicionInicial = 2;
	
	private AyudaURL() { }
	
	/**
	 * Recupera los parámetros de global.properties.
	 */
	public static void init() {
		if (separadorDeParametros == null) {
			posicionInicial = ConfiguracionGlobal.getAyudaUrlPosicionInicial();
			separadorDeParametros = ConfiguracionGlobal.getAyudaUrlSeparadorParametros();
			idiomaPorDefecto = ConfiguracionGlobal.getAyudaUrlIdiomaPorDefecto();
			controladorPorDefecto = ConfiguracionGlobal.getAyudaUrlControladorPorDefecto();
			prefijoUrlProtegida = ConfiguracionGlobal.getAyudaUrlPrefijoUrlProtegida();
			prefijoUrlAnonimo = ConfiguracionGlobal.getAyudaUrlPrefijoUrlAccesoAnonimo();
			formatosSoportados = ConfiguracionGlobal.getAyudaUrlFormatosSoportados();
			formatoPorDefecto = ConfiguracionGlobal.getAyudaUrlFormatoPorDefecto();
		}
	}
	
	/**
	 * división de la URL en partes usando como separador la "/".
	 * @param url Elemento a dividir
	 * @return String[] Distintas partes de la petición
	 */
	private static final String[] splitter(String url) {
		return url.split(divisor);
	}
	
	/**
	 * Obtiene el idioma de la URL.
	 * @param url Elemento del que extraer el idioma
	 * @return String con el idioma
	 */
	public static String obtenerIdioma(String url) {
		init();
		String[] fields = splitter(url);
		
		return (fields.length > posicionInicial + 1) ? fields[posicionInicial + 1] : idiomaPorDefecto;
	}

	/**
	 * Obtiene el formato de la URL.
	 * @param url Elemento del que extraer el formato
	 * @return String con el formato o nulo si no existe
	 */
	@SuppressWarnings("checkstyle:MethodName")
	private static String _obtenerFormato(String url) {
		init();
		String[] fields = splitter(url);
		
		String formato = (fields.length > posicionInicial + 2) ? fields[posicionInicial + 2] : null;
		boolean esUnFormato = formatosSoportados.indexOf("/" + formato + "/") >= 0;
		if (esUnFormato) {
			return formato;
		}
		return null;
	}
	
	/**
	 * Obtiene el formato de la URL.
	 * @param url Elemento del que extraer el formato
	 * @return String con el formato o el formato por defecto si no existe en la url
	 */
	public static String obtenerFormato(String url) {
		String formato = _obtenerFormato(url);
		return (formato != null) ? formato : formatoPorDefecto;
	}

	
	/**
	 * Obtiene el controlador, elemento situado tras el idioma hasta el final de la url o hasta el separador de parámetros.
	 * @param url de dónde extraer los parámetros
	 * @return nombre del controlador elementos separados por "."
	 */
	public static String obtenerControlador(String url) {
		init();
		String[] fields = splitter(url);
		int incremento = 2;
		if (_obtenerFormato(url) != null) {
			incremento++;
		}
		
		if (posicionInicial + incremento < fields.length) { 
			int i = posicionInicial + incremento + 1;
			String servlet = fields[posicionInicial + incremento];
			while ((i < fields.length) && (!separadorDeParametros.equals(fields[i]))) {
				servlet += "." + fields[i]; 
				i++;
			}
			return servlet;
		} else {
			return controladorPorDefecto;
		}
	}
	
	/**
	 * Obtiene la lista de parámetros.
	 * @param url de dónde extraer los parámetros
	 * @return lista de parámetros
	 */
	public static String[] obtenerParametros(String url) {
		init();
		String[] fields = splitter(url);
		if (posicionInicial + 2 <= fields.length) { 
			int i = posicionInicial + 2;
			while ((i < fields.length) && (!separadorDeParametros.equals(fields[i]))) {
				i++;
			}
			i++; // saltar el separador de parámetros
			if (fields.length > i) {
				String[] params = new String[fields.length - i];
				for (int j = i; j < fields.length; j++) {
					params[j - i] = fields[j]; 
				}
				return params;
			} 
		} 
		return null;
	}
	
	/**
	 * Obtiene una cadena con los parametros de la peticion.
	 * @param request peticion
	 * @return cadena con parametros
	 */
	public static String obtenerCadenaParametros(HttpServletRequest request) {
		Enumeration<?> nombresParametros = request.getParameterNames();
		String parametros = null;
		String sep = "?";
		while (nombresParametros.hasMoreElements()) {
			String nombreParametro = (String) nombresParametros.nextElement();
			parametros = ((parametros == null) ? "" : parametros) + sep + nombreParametro + "=" + request.getParameter(nombreParametro);
			sep = "&";
		}		
		return parametros;
	}

	/**
	 * Obtiene una cadena con las cabeceras de la peticion.
	 * @param request peticion
	 * @return cadena con cabeceras
	 */
	public static String obtenerCadenaCabeceras(HttpServletRequest request) {
		Enumeration<?> nombresParametros = request.getHeaderNames();
		String parametros = null;
		String sep = "?";
		while (nombresParametros.hasMoreElements()) {
			String nombreParametro = (String) nombresParametros.nextElement();
			parametros = ((parametros == null) ? "" : parametros) + sep + nombreParametro + "=" + request.getHeader(nombreParametro);
			sep = "&";
		}		
		return parametros;
	}

	
	/** obtener url con idioma.
	 * @param url url
	 * @param idioma idioma
	 * @return url con idioma
	 */
	public static String obtenerUrlConIdioma(String url, String idioma) {
		init();
		String[] fields = splitter(url);
		
		if (fields.length > posicionInicial + 1) {
			fields[posicionInicial + 1] = idioma;
			String newUrl = fields[0];
			for (int i = 1; i < fields.length; i++) {
				newUrl += "/" + fields[i];
			}
			return newUrl;
		} else {
			return url;
		}
	}

	/** obtener url con formato.
	 * @param url url 
	 * @param formato formato
	 * @return url con formato
	 */
	public static String obtenerUrlConFormato(String url, String formato) {
		init();
		String[] fields = splitter(url);
		boolean agregarParametro = true;
		if (fields.length > posicionInicial + 2) {
			if (formatosSoportados.indexOf("/" + fields[posicionInicial + 2] + "/") >= 0) {
				fields[posicionInicial + 2] = formato;
				agregarParametro = false;
			}
			String newUrl = fields[0];
			for (int i = 1; i < fields.length; i++) {
				if ((i == posicionInicial + 2) && agregarParametro) {
					newUrl += "/" + formato;
				}
				newUrl += "/" + fields[i];
			}
			return newUrl;
		} else {
			return url;
		}
	}
	
	
	/** obtener url controlador.
	 * @param controlador controlador
	 * @param idioma idioma
	 * @return url
	 */
	public static String obtenerUrlControlador(String controlador, String idioma) {
		init();
		return prefijoUrlProtegida + "/" + idioma + "/" + controlador.replace('.', '/');
	}
	
	/** obtener url controlador.
	 * @param controlador controlador
	 * @param idioma idioma
	 * @param formato formato
	 * @return url
	 */
	public static String obtenerUrlControlador(String controlador, String idioma, String formato) {
		init();
		return prefijoUrlProtegida + "/" + idioma + "/" + formato + "/" + controlador.replace('.', '/');
	}
	
	/** obtener url controlador.
	 * @param controlador controlador
	 * @param accesoAnonimo si es anonimo
	 * @param idioma idioma
	 * @return url
	 */
	public static String obtenerUrlControlador(String controlador, boolean accesoAnonimo, String idioma) {
		init();
		return (accesoAnonimo ? prefijoUrlAnonimo : prefijoUrlProtegida) + "/" + idioma + "/" + controlador.replace('.', '/');
	}
	
	/** obtener ruta fichero imagen.
	 * @param codigo codigo
	 * @param base base
	 * @param niveles niveles
	 * @return ruta
	 */
	public static String obtenerRutaFicheroImagen(String codigo, String base, int niveles) {
		String resultado = base;
		int i = codigo.length() - niveles;
		while (i < codigo.length()) {
			resultado += "/" + codigo.charAt(i);
			i++;
		}
		return resultado + "/" + codigo + ".jpg"; 
	}
	
	/** obtener ruta fichero imagen thumbnail.
	 * @param codigo codigo
	 * @param base base
	 * @param niveles niveles
	 * @return ruta
	 */
	public static String obtenerRutaFicheroImagenTmb(String codigo, String base, int niveles) {
		String resultado = base;
		int i = codigo.length() - niveles;
		while (i < codigo.length()) {
			resultado += "/" + codigo.charAt(i);
			i++;
		}
		return resultado + "/" + codigo + ".jpgtmb.jpg"; 
	}
	
	/**
	 * Obtiene la url que se le pasa sin los posibles parámetros que tenga.
	 * @param url la url desde la que obtener la información
	 * @param separadorParametros separador
	 * @return la url sin parámetros
	 */
	public static String obtenerUrlSinParametros(String url, boolean separadorParametros) {
		init();
		String[] fields = splitter(url);
		String urlSinParametros = fields[0];
		int i = 1;
		while ((i < fields.length) && (!separadorDeParametros.equals(fields[i]))) {
			urlSinParametros += divisor + fields[i]; 
			i++;
		}
		if (separadorParametros) {
			urlSinParametros += divisor + separadorDeParametros;
		}
		
		return urlSinParametros;
	}
	
	/** get separador de parametros.
	 * @return the separadorDeParametros
	 */
	public static String getSeparadorDeParametros() {
		return separadorDeParametros;
	}
	
}

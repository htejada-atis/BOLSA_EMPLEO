package es.ujaen.uvirtual.utilidades;

import java.nio.charset.StandardCharsets;

/**
 * Escapa los textos para evitar problemas.
 * @author julopez
 *
 */
public class EscapaHTML {
	
	private EscapaHTML() { }
	
	/**
	 * Escapa una cadena de caracteres para ser mostrada en XHTML y eliminar las etiqutas html, eliminando los caracteres "<" ">" por sus valores html.
	 * @param origen cadena que queremos escapar
	 * @return la cadena mostrable en formato xhtml.
	 */
	public static String escapa(String origen) {
		if (origen == null) {
			return ""; //20111227 - evita generar espacios en blanco
		}
		// 20120529 - julopez- escapa & previo a escapar, y luego añade escape sobre el caracter "
		String resultado = escapaCadena(origen);
		resultado = resultado.replace("\"", "&#34;");
		resultado = resultado.replace("<", "&#60;");
		resultado = resultado.replace(">", "&#62;");
		
		// 20131011 - julopez - escapa "'" y "& " 
		resultado = resultado.replace("'", "&#39;");
		resultado = resultado.replace("& ", "&#38; ");
		return resultado;
	}

	/**
	 * Escapa una cadena de caracteres para ser mostrada en xhtml.
	 * @param origen la cadena a ser escapada
	 * @return la cadena con todos sus caracteres escapados
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public static String escapaCadena(String origen) {
		if (origen == null) {
			return ""; 
		}
		StringBuilder buff = new StringBuilder();
		int code;
		for (int i = 0; i < origen.length(); i++) {
			code = origen.codePointAt(i);
			if (code >= 0x20 && code <= 0x7F && code != 0x26) {
				buff.append((char) code);
			} else {
				buff.append(String.format("&#%d;", code));
			}
		}
		return buff.toString();
	}

	/**
	 * Escapa una cadena de caracteres para ser mostrada en XHTML sin eliminar las etiqutas html.
	 * @param origen cadena que queremos escapar
	 * @return la cadena mostrable en formato xhtml.
	 */
	public static String escapaHTML(String origen) {
		if (origen == null) {
			return "";
		}

		String origenLimpio = origen.replace(" & ", " &amp; "); // para los & que se usan como y
		String resultado = escapaCadena(origenLimpio);
		// al escapar la cadena el símbolo & es modificado y debemos restaurarlo para no tener un doble & u que no se muestren bien ciertos caracteres
		resultado = resultado.replace("&#38;", "&"); 
		resultado = resultado.replace("&#13;&#10;", ""); // el editor añade retornos de carro que debemos borrar
		resultado = resultado.replace("&#10;", ""); // 20120710 - el retorno de carro, lo eliminamos
		return resultado;
	}

	
	/**
	 * Ajusta la codificación de cadena y la devuelve como un string java (para parámetros que vengan con otras codificaciones).
	 * http://stackoverflow.com/questions/887148/how-to-determine-if-a-string-contains-invalid-encoded-characters
	 * 
	 * @param latin1 cadena en codificación iso-8859-1
	 * @return la cadena en codificación UTF-8
	 */
	@SuppressWarnings("checkstyle:ReturnCount")
	public static String ajustaCodificacion(String latin1) {
		if (latin1 == null) {
			return latin1;
		}
		byte[] bytes = latin1.getBytes(StandardCharsets.ISO_8859_1);
		if (!esUTF8(bytes)) {
			return latin1;
		}
		return new String(bytes);
	}
	 
	/** ajustaCodificacionCadenaVaciaComoNulo.
	 * @param latin1 cadena
	 * @return cadena ajustada
	 */
	public static String ajustaCodificacionCadenaVaciaComoNulo(String latin1) {
		if (latin1 == null || "".equals(latin1)) {
			return null;
		} else {
			return ajustaCodificacion(latin1);
		}
	}

	/** es utf8.
	 * @param input cadena
	 * @return si es utf8
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:magicnumber", "checkstyle:NPathComplexity",
		"checkstyle:ReturnCount", "checkstyle:BooleanExpressionComplexity", "java:S3776"})
	public static boolean esUTF8(byte[] input) {
		int i = 0;
		// Check for BOM
		if (input.length >= 3 && (input[0] & 0xFF) == 0xEF && (input[1] & 0xFF) == 0xBB && (input[2] & 0xFF) == 0xBF) { 
		   i = 3;
		}
		
		int end;
		for (int j = input.length; i < j; ++i) {
			int octet = input[i];
			if ((octet & 0x80) == 0) {
				continue; // ASCII
			}
			
			// Check for UTF-8 leading byte
			if ((octet & 0xE0) == 0xC0) {
				end = i + 1;
			} else if ((octet & 0xF0) == 0xE0) {
				end = i + 2;
			} else if ((octet & 0xF8) == 0xF0) {
				end = i + 3;
			} else {
				// Java only supports BMP so 3 is max
				return false;
			}
			
			//20120510 - es posible que luego no tengamos tantos bytes...
			if (input.length <= end) {
				return false;
			}
			
			while (i < end) {
				i++;
				octet = input[i];
				if ((octet & 0xC0) != 0x80) {
					// Not a valid trailing byte
					return false;
				}
			}
		}
		return true;
	}
}

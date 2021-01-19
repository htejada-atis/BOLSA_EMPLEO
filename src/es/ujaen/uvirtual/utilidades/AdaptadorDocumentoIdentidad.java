package es.ujaen.uvirtual.utilidades;

import es.ujaen.uvirtual.beans.Usuario;

/**
 * Adaptador del documento de identidad de un usuario a los distintos sistemas fuente (se considera como base el de Arcos.
 * 
 * @author julopez
 *
 */
public class AdaptadorDocumentoIdentidad {
	public static final String UXXIRRHH = "uxxirrhh";
	public static final String UXXIAC = "uxxiac";
	
	private AdaptadorDocumentoIdentidad() {
		//no de permite instanciacion
	}
	
	/**
	 * Convierte el número de documento al formato en el sistema de destino.
	 * @param sistemaDestino sistema en el que queremos obtener el formato del número de documento
	 * @param tipoDocumento tipo de documento tal y como se refleja en Usuario
	 * @param numDocumento número de documento tal y como aparece en Usuario
	 * @return el número de documento en el formato del sistema destino
	 */
	public static String numeroDocumento(String sistemaDestino, String tipoDocumento, String numDocumento) {
		String resultado = null;
		if ((numDocumento != null) && (tipoDocumento != null)) {
			if (sistemaDestino.equals(UXXIRRHH)) {
				resultado = numDocumento;
			} else if (sistemaDestino.equals(UXXIAC)) {
				// Eliminar la letra del nif/nie
				if ("NIF".equals(tipoDocumento) || "NIE".equals(tipoDocumento)) {
					resultado = numDocumento.substring(0, numDocumento.length() - 1);
				} else {
					resultado = numDocumento;
				}
			}
		}
		return resultado;
	}
	

	/**
	 * Convierte el número de documento al formato en el sistema de destino.
	 * @param sistemaDestino sistemaDestino sistema en el que queremos obtener el formato del número de documento
	 * @param usuario usuario del que queremos obtener el número de documento.
	 * @return numero de documento en formato sistema destino
	 */
	public static String numeroDocumento(String sistemaDestino, Usuario usuario) {
		return numeroDocumento(sistemaDestino, usuario.getDocumentoTipo(), usuario.getDocumentoNumero());
	}

	/**
	 * Obtiene la letra del nif de un número de documento.
	 * @param tipoDocumento tipo de documento
	 * @param numDocumento número de documento a partir del que se generará la letra (debe ser en el formato ARCOS)
	 * @return letra del NIF
	 */
	public static String letraNIF(String tipoDocumento, String numDocumento) {
		if (("NIF".equals(tipoDocumento) || "NIE".equals(tipoDocumento)) && numDocumento != null) {
			return numDocumento.substring(numDocumento.length() - 1);
		}
		return null;
	}
	
	
}

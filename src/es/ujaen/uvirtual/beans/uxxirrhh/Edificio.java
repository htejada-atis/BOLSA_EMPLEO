package es.ujaen.uvirtual.beans.uxxirrhh;

import es.ujaen.uvirtual.beans.CodigoAlfDescripcion;

/**
 * Clase que representa la información de edificios en UXXIRRHH.
 * @author julopez
 *
 */
public class Edificio extends CodigoAlfDescripcion {
	
	private static final long serialVersionUID = 7684569734912514725L;

	/** constructor con parametros.
	 * @param codigo codigo
	 * @param descripcion descripcion
	 */
	public Edificio(String codigo, String descripcion) {
		super(codigo, descripcion);
	}

}

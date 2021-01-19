package es.ujaen.uvirtual.beans;

import java.io.Serializable;

/** codigo alfabetico y descripcion.
 * @author julopez
 *
 */
@SuppressWarnings("checkstyle:AbstractClassName")
public abstract class CodigoAlfDescripcion implements Serializable {
	private static final long serialVersionUID = -7464913055569069794L;
	
	protected String codigo = null;
	protected String descripcion = null;

	/** constructor por defecto. */
	protected CodigoAlfDescripcion() {
		super();
	}
	
	/** constructor con parametros.
	 * @param pcodigo codigo
	 * @param pdescripcion descripcion
	 */
	protected CodigoAlfDescripcion(String pcodigo, String pdescripcion) {
		super();
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return this.codigo + " - " + this.descripcion;
	}
}
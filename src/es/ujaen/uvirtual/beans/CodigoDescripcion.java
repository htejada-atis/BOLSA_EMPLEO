package es.ujaen.uvirtual.beans;

import java.io.Serializable;

/** Codigo y descripcion.
 * @author julopez
 *
 */
@SuppressWarnings("java:S1948")
public class CodigoDescripcion implements Serializable {
	private static final long serialVersionUID = -7464913055569069794L;
	
	protected Object codigo = null;
	protected Object descripcion = null;

	/** constructor por defecto.
	 */
	public CodigoDescripcion() {
		super();
	}

	/** constructor con parametros.
	 * @param pcodigo codigo
	 * @param pdescripcion descripcion
	 */
	public CodigoDescripcion(Object pcodigo, Object pdescripcion) {
		super();
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
	}
	
	public Object getCodigo() {
		return codigo;
	}

	public Integer getIntegerCodigo() {
		return (Integer) codigo;
	}

	public Integer getCodigoComoInteger() {
		return (Integer) codigo;
	}

	public String getStringCodigo() {
		return (String) codigo;
	}

	public String getCodigoComoString() {
		return (String) codigo;
	}

	public void setCodigo(Object codigo) {
		this.codigo = codigo;
	}

	public Object getDescripcion() {
		return descripcion;
	}

	public String getStringDescripcion() {
		return (String) descripcion;
	}

	public String getDescripcionComoString() {
		return (String) descripcion;
	}

	public void setDescripcion(Object descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return this.codigo.toString() + " - " + this.descripcion.toString();
	}
}
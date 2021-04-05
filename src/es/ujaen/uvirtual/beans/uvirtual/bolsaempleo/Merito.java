package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;


/** Clase mérito de bolsa empleo.
 * @author jlopez
 *
 */
public class Merito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Float valor;
	private String descripcion;
	private String observacion;
	private ItemBaremacion item;
	
	/** Constructor por defecto.
	 */
	public Merito() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pvalor .
	 * @param pdescripcion .
	 * @param pobservacion .
	 * @param pitem .
	 */
	public Merito(Integer pcodNum, Float pvalor, String pdescripcion, String pobservacion, ItemBaremacion pitem) {
		super();
		this.codNum = pcodNum;
		this.valor = pvalor;
		this.descripcion = pdescripcion;
		this.observacion = pobservacion;
		this.item = pitem;
	}
	
	/** Constructor copia.
	 * @param copia Noticia a copiar
	 */
	public Merito(Merito copia) {
		this.codNum = copia.codNum;
		this.valor = copia.valor;
		this.descripcion = copia.descripcion;
		this.observacion = copia.observacion;
		this.item = copia.item;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public Float getValor() {
		return valor;
	}
	
	public void setValor(Float valor) {
		this.valor = valor;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getObservacion() {
		return observacion;
	}
	
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Noticia [codNum=" + codNum + ", valor=" + valor
				+ ", descripcion=" + descripcion + ", observacion=" + observacion 
				+ ", item=" + item + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((observacion == null) ? 0 : observacion.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}
	
	@Override
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity"})
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Merito other = (Merito) obj;
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (observacion == null) {
			if (other.observacion != null) {
				return false;
			}
		} else if (!observacion.equals(other.observacion)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		
		return true;
	}
	
}

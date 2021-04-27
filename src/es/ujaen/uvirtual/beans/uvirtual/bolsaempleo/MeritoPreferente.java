package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;

/** Clase mérito preferente.
 * 
 * @author ATISoluciones 2021 
 */
public class MeritoPreferente implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private String tipo;
	private String aplicable;
	private String factor;
	private Float valorMaximo;	
	private ItemBaremacion tipoItemBaremacion;	
	private BloqueBaremacion aplicableBloqueBaremacion;
	private ApartadoBaremacion aplicableApartadoBaremacion;
	private ItemBaremacion aplicableItemBareamcion;
	private Boolean borrado;
	
	/** Constructor por defecto.
	 */
	public MeritoPreferente() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pdescripcion .
	 * @param ptipo .
	 * @param paplicable .
	 * @param pfactor .
	 * @param pvalorMaximo .	 
	 * @param ptipoItemBaremacion .
	 * @param paplicableBloqueBaremacion .
	 * @param paplicableApartadoBaremacion .
	 * @param paplicableItemBareamcion .
	 * @param pborrado .
	 */
	public MeritoPreferente(Integer pcodNum, String pdescripcion, String ptipo, String paplicable, String pfactor, Float pvalorMaximo, 
			ItemBaremacion ptipoItemBaremacion, BloqueBaremacion paplicableBloqueBaremacion, ApartadoBaremacion paplicableApartadoBaremacion, 
			ItemBaremacion paplicableItemBareamcion, Boolean pborrado) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
		this.tipo = ptipo;
		this.aplicable = paplicable;
		this.factor = pfactor;
		this.valorMaximo = pvalorMaximo;
		this.tipoItemBaremacion = ptipoItemBaremacion;
		this.aplicableBloqueBaremacion = paplicableBloqueBaremacion;
		this.aplicableApartadoBaremacion = paplicableApartadoBaremacion;
		this.aplicableItemBareamcion = paplicableItemBareamcion;
		this.borrado = pborrado;
	}
		
	/** Constructor copia.
	 * @param copia .
	 */
	public MeritoPreferente(MeritoPreferente copia) {
		this.codNum = copia.codNum;
		this.descripcion = copia.descripcion;
		this.tipo = copia.tipo;
		this.aplicable = copia.aplicable;
		this.factor = copia.factor;
		this.valorMaximo = copia.valorMaximo;		
		this.tipoItemBaremacion = copia.tipoItemBaremacion;
		this.aplicableBloqueBaremacion = copia.aplicableBloqueBaremacion;
		this.aplicableApartadoBaremacion = copia.aplicableApartadoBaremacion;
		this.aplicableItemBareamcion = copia.aplicableItemBareamcion;
		this.borrado = copia.borrado;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
		
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getAplicable() {
		return aplicable;
	}

	public void setAplicable(String aplicable) {
		this.aplicable = aplicable;
	}
	
	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}
		
	public Float getValorMaximo() {
		return this.valorMaximo;
	}

	public void setValorMaximo(Float valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
		
	public ItemBaremacion getTipoItemBaremacion() {
		return this.tipoItemBaremacion;
	}

	public void setTipoItemBaremacion(ItemBaremacion itemBaremacion) {
		this.tipoItemBaremacion = itemBaremacion;
	}
	
	public BloqueBaremacion getAplicableBloqueBaremacion() {
		return this.aplicableBloqueBaremacion;
	}

	public void setAplicableBloqueBaremacion(BloqueBaremacion aplicableBloqueBaremacion) {
		this.aplicableBloqueBaremacion = aplicableBloqueBaremacion;
	}
	
	public ApartadoBaremacion getAplicableApartadoBaremacion() {
		return this.aplicableApartadoBaremacion;
	}

	public void setAplicableApartadoBaremacion(ApartadoBaremacion aplicableApartadoBaremacion) {
		this.aplicableApartadoBaremacion = aplicableApartadoBaremacion;
	}
	
	public ItemBaremacion getAplicableItemBaremacion() {
		return this.aplicableItemBareamcion;
	}

	public void setAplicableItemBaremacion(ItemBaremacion aplicableItemBaremacion) {
		this.aplicableItemBareamcion = aplicableItemBaremacion;
	}
	
	public Boolean getBorrado() {
		return this.borrado;
	}

	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {		
		return "ApartadoBaremacion [codNum=" + codNum + ", descripcion=" + descripcion + ", tipo=" + tipo
				+ ", aplicable=" + aplicable + ", factor=" + factor + ", valorMaximo=" + valorMaximo + ", tipoItemBaremacion=" + tipoItemBaremacion 
				+ ", aplicableBloqueBaremacion" + aplicableBloqueBaremacion + ", aplicableApartadoBaremacion=" + aplicableApartadoBaremacion 
				+ ", aplicableItemBareamcion=" + aplicableItemBareamcion + ", borrado=" + borrado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());	
		result = prime * result + ((aplicable == null) ? 0 : aplicable.hashCode());
		result = prime * result + ((factor == null) ? 0 : factor.hashCode());
		result = prime * result + ((valorMaximo == null) ? 0 : valorMaximo.hashCode());		
		result = prime * result + ((tipoItemBaremacion == null) ? 0 : tipoItemBaremacion.hashCode());
		result = prime * result + ((aplicableBloqueBaremacion == null) ? 0 : aplicableBloqueBaremacion.hashCode());
		result = prime * result + ((aplicableApartadoBaremacion == null) ? 0 : aplicableApartadoBaremacion.hashCode());
		result = prime * result + ((aplicableItemBareamcion == null) ? 0 : aplicableItemBareamcion.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
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
		MeritoPreferente other = (MeritoPreferente) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (tipo == null) {
			if (other.tipo != null) {
				return false;
			}
		} else if (!tipo.equals(other.tipo)) {
			return false;
		}
		if (aplicable == null) {
			if (other.aplicable != null) {
				return false;
			}
		} else if (!aplicable.equals(other.aplicable)) {
			return false;
		}
		if (factor == null) {
			if (other.factor != null) {
				return false;
			}
		} else if (!factor.equals(other.factor)) {
			return false;
		}
		if (valorMaximo == null) {
			if (other.valorMaximo != null) {
				return false;
			}
		} else if (!valorMaximo.equals(other.valorMaximo)) {
			return false;
		}
		if (tipoItemBaremacion == null) {
			if (other.tipoItemBaremacion != null) {
				return false;
			}
		} else if (!tipoItemBaremacion.equals(other.tipoItemBaremacion)) {
			return false;
		}
		if (aplicableBloqueBaremacion == null) {
			if (other.aplicableBloqueBaremacion != null) {
				return false;
			}
		} else if (!aplicableBloqueBaremacion.equals(other.aplicableBloqueBaremacion)) {
			return false;
		}
		if (aplicableApartadoBaremacion == null) {
			if (other.aplicableApartadoBaremacion != null) {
				return false;
			}
		} else if (!aplicableApartadoBaremacion.equals(other.aplicableApartadoBaremacion)) {
			return false;
		}
		if (aplicableItemBareamcion == null) {
			if (other.aplicableItemBareamcion != null) {
				return false;
			}
		} else if (!aplicableItemBareamcion.equals(other.aplicableItemBareamcion)) {
			return false;
		}
		if (borrado == null) {
			if (other.borrado != null) {
				return false;
			}
		} else if (!borrado.equals(other.borrado)) {
			return false;
		}
		
		return true;
	}
	
}


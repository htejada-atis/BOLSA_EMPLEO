package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase para controlar el listado de meritos que excluyen a otros .
 * 
 * @author fcampos
 */
public class MeritosExcluyentes implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private ItemBaremacion itemPadre;
	private ItemBaremacion itemHijo;

	/**
	 * Constructor por defecto.
	 */
	public MeritosExcluyentes() {

	}

	/**
	 * Constructor con parametros.
	 *
	 * @param pcodNum .
	 * @param item1   .
	 * @param item2 .
	 */
	public MeritosExcluyentes(Integer pcodNum, ItemBaremacion item1, ItemBaremacion item2) {
		this.codNum = pcodNum;
		this.itemPadre = item1;
		this.itemHijo = item2;		
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public ItemBaremacion getItemPadre() {
		return this.itemPadre;
	}

	public void setItemPadre(ItemBaremacion item) {
		this.itemPadre = item;
	}
	
	public ItemBaremacion getItemHijo() {
		return this.itemHijo;
	}

	public void setItemHijo(ItemBaremacion item) {
		this.itemHijo = item;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoSolicitud [codNum=" + codNum + ", itemPadre=" + itemPadre + ", itemHijo=" + itemHijo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((itemPadre == null) ? 0 : itemPadre.hashCode());
		result = prime * result + ((itemHijo == null) ? 0 : itemHijo.hashCode());		
		return result;
	}

	@Override
	@SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
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
		MeritosExcluyentes other = (MeritosExcluyentes) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (itemPadre == null) {
			if (other.itemPadre != null) {
				return false;
			}
		} else if (!itemPadre.equals(other.itemPadre)) {
			return false;
		}
		if (itemHijo == null) {
			if (other.itemHijo != null) {
				return false;
			}
		} else if (!itemHijo.equals(other.itemHijo)) {
			return false;
		}
		
		return true;
	}

}

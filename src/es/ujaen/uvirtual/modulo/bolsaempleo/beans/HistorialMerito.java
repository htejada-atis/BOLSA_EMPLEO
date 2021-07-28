package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase historial mérito .
 * 
 * @author ATISoluciones
 */
public class HistorialMerito extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Double valor;
	private ItemBaremacion item;
	private String comparaValor;
	private String comparaItem;
	
	
	/**
	 * Constructor por defecto.
	 */
	public HistorialMerito() {

	}

	/** Constructor con parametros .
	 * @param phistorial .
	 * @param pitem .
	 * @param pvalor .
	 * @param pcomparaValor .
	 * @param pcomparaItem .
	 */
	public HistorialMerito(Historial phistorial, ItemBaremacion pitem, Double pvalor, String pcomparaValor, String pcomparaItem) {
		super(phistorial);
		this.item = pitem;
		this.valor = pvalor;
		this.comparaValor = pcomparaValor;
		this.comparaItem = pcomparaItem;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Mérito a copiar
	 */
	public HistorialMerito(HistorialMerito copia) {
		super(copia);
		this.item = copia.item;
		this.valor = copia.valor;
		this.comparaValor = copia.comparaValor;
		this.comparaItem = copia.comparaItem;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public ItemBaremacion getItem() {
		return item;
	}

	public void setItem(ItemBaremacion item) {
		this.item = item;
	}

	public String getComparaValor() {
		return comparaValor;
	}

	public void setComparaValor(String comparaValor) {
		this.comparaValor = comparaValor;
	}

	public String getComparaItem() {
		return comparaItem;
	}

	public void setComparaItem(String comparaItem) {
		this.comparaItem = comparaItem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial Mérito [historial=" + super.toString() + ", item=" + item + ", valor=" + valor 
				+ ", comparaValor=" + comparaValor + ", comparaItem=" + comparaItem + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((comparaValor == null) ? 0 : comparaValor.hashCode());
		result = prime * result + ((comparaItem == null) ? 0 : comparaItem.hashCode());
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
		HistorialMerito other = (HistorialMerito) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (comparaValor == null) {
			if (other.comparaValor != null) {
				return false;
			}
		} else if (!comparaValor.equals(other.comparaValor)) {
			return false;
		}
		if (comparaItem == null) {
			if (other.comparaItem != null) {
				return false;
			}
		} else if (!comparaItem.equals(other.comparaItem)) {
			return false;
		}
		
		return true;
	}

}

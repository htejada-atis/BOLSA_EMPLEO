package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase historial SBM .
 * 
 * @author ATISoluciones
 */
public class HistorialSBM extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean excluido;
	private Boolean validado;
	private Double resultado;
	private Double valor;
	private ItemBaremacion item;
	private String observacionCandidato;
	private String comparaExcluido;
	private String comparaValidado;
	private String comparaValor;
	private String comparaResultado;
	private String comparaItem;
	

	/**
	 * Constructor por defecto.
	 */
	public HistorialSBM() {

	}

	/** Constructor con parametros .
	 * @param phistorial .
	 * @param pexcluido .
	 * @param pvalidado .
	 * @param pobservacionCandidato .
	 * @param pitem .
	 * @param presultado .
	 * @param pvalor .
	 * @param pcomparaExcluido .
	 * @param pcomparaValidado .
	 * @param pcomparaValor .
	 * @param pcomparaResultado .
	 * @param pcomparaItem .
	 */
	public HistorialSBM(Historial phistorial, Boolean pexcluido, Boolean pvalidado, String pobservacionCandidato, ItemBaremacion pitem, 
			Double presultado, Double pvalor, String pcomparaExcluido, String pcomparaValidado, String pcomparaValor, String pcomparaResultado, String pcomparaItem) {
		super(phistorial);
		this.excluido = pexcluido;
		this.validado = pvalidado;
		this.observacionCandidato = pobservacionCandidato;
		this.item = pitem;
		this.resultado = presultado;
		this.valor = pvalor;
		this.comparaExcluido = pcomparaExcluido;
		this.comparaValidado = pcomparaValidado;
		this.comparaValor = pcomparaValor;
		this.comparaResultado = pcomparaResultado;
		this.comparaItem = pcomparaItem;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Validación a copiar
	 */
	public HistorialSBM(HistorialSBM copia) {
		super(copia);
		this.excluido = copia.excluido;
		this.validado = copia.validado;
		this.observacionCandidato = copia.observacionCandidato;
		this.item = copia.item;
		this.resultado = copia.resultado;
		this.valor = copia.valor;
		this.comparaExcluido = copia.comparaExcluido;
		this.comparaValidado = copia.comparaValidado;
		this.comparaValor = copia.comparaValor;
		this.comparaResultado = copia.comparaResultado;
		this.comparaItem = copia.comparaItem;
	}

	public Boolean getExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	public String getObservacionCandidato() {
		return observacionCandidato;
	}

	public void setObservacionCandidato(String observacionCandidato) {
		this.observacionCandidato = observacionCandidato;
	}
	
	public Double getResultado() {
		return resultado;
	}

	public void setResultado(Double resultado) {
		this.resultado = resultado;
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

	public String getComparaExcluido() {
		return comparaExcluido;
	}

	public void setComparaExcluido(String comparaExcluido) {
		this.comparaExcluido = comparaExcluido;
	}

	public String getComparaValidado() {
		return comparaValidado;
	}

	public void setComparaValidado(String comparaValidado) {
		this.comparaValidado = comparaValidado;
	}

	public String getComparaValor() {
		return comparaValor;
	}

	public void setComparaValor(String comparaValor) {
		this.comparaValor = comparaValor;
	}

	public String getComparaResultado() {
		return comparaResultado;
	}

	public void setComparaResultado(String comparaResultado) {
		this.comparaResultado = comparaResultado;
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
		return "Historial SBM [historial=" + super.toString() + ", excluido=" + excluido + ", validado=" + validado + ", observacionCandidato=" 
				+ observacionCandidato + ", item=" + item + ", resultado=" + resultado + ", valor=" + valor 
				+ ", comparaExcluido=" + comparaExcluido + ", comparaValidado=" + comparaValidado + ", comparaValor=" + comparaValor + ", comparaResultado=" 
				+ comparaResultado + ", comparaItem=" + comparaItem + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());
		result = prime * result + ((observacionCandidato == null) ? 0 : observacionCandidato.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((resultado == null) ? 0 : resultado.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((comparaExcluido == null) ? 0 : comparaExcluido.hashCode());
		result = prime * result + ((comparaValidado == null) ? 0 : comparaValidado.hashCode());
		result = prime * result + ((comparaValor == null) ? 0 : comparaValor.hashCode());
		result = prime * result + ((comparaResultado == null) ? 0 : comparaResultado.hashCode());
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
		HistorialSBM other = (HistorialSBM) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (validado == null) {
			if (other.validado != null) {
				return false;
			}
		} else if (!validado.equals(other.validado)) {
			return false;
		}
		if (observacionCandidato == null) {
			if (other.observacionCandidato != null) {
				return false;
			}
		} else if (!observacionCandidato.equals(other.observacionCandidato)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		if (resultado == null) {
			if (other.resultado != null) {
				return false;
			}
		} else if (!resultado.equals(other.resultado)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (comparaExcluido == null) {
			if (other.comparaExcluido != null) {
				return false;
			}
		} else if (!comparaExcluido.equals(other.comparaExcluido)) {
			return false;
		}
		if (comparaValidado == null) {
			if (other.comparaValidado != null) {
				return false;
			}
		} else if (!comparaValidado.equals(other.comparaValidado)) {
			return false;
		}
		if (comparaValor == null) {
			if (other.comparaValor != null) {
				return false;
			}
		} else if (!comparaValor.equals(other.comparaValor)) {
			return false;
		}
		if (comparaResultado == null) {
			if (other.comparaResultado != null) {
				return false;
			}
		} else if (!comparaResultado.equals(other.comparaResultado)) {
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

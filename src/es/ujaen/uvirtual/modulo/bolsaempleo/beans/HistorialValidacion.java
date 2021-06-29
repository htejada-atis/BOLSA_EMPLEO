package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase historial validación para la tabla de historial de validaciones de un mérito.
 * 
 * @author ATISoluciones
 */
public class HistorialValidacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date fecha;
	private String codigoItem;
	private String evaluador;
	private String excluido;
	private String observacionCandidato;
	private String validado;
	private String valoracionMerito;
	private String valorMerito;
	

	/**
	 * Constructor por defecto.
	 */
	public HistorialValidacion() {

	}

	/** Constructor con parametros.
	 * @param pfecha .
	 * @param pcodigoItem .
	 * @param pevaluador .
	 * @param pexcluido .
	 * @param pobservacionCandidato .
	 * @param pvalidado .
	 * @param pvaloracionMerito .
	 * @param pvalorMerito .
	 */
	public HistorialValidacion(Date pfecha, String pcodigoItem, String pevaluador, String pexcluido, String pobservacionCandidato,
			String pvalidado, String pvaloracionMerito, String pvalorMerito) {
		super();
		this.fecha = pfecha;
		this.codigoItem = pcodigoItem;
		this.evaluador = pevaluador;
		this.excluido = pexcluido;
		this.observacionCandidato = pobservacionCandidato;
		this.validado = pvalidado;
		this.valoracionMerito = pvaloracionMerito;
		this.valorMerito = pvalorMerito;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Validación a copiar
	 */
	public HistorialValidacion(HistorialValidacion copia) {
		this.fecha = copia.fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}

	public String getEvaluador() {
		return evaluador;
	}

	public void setEvaluador(String evaluador) {
		this.evaluador = evaluador;
	}

	public String getExcluido() {
		return excluido;
	}

	public void setExcluido(String excluido) {
		this.excluido = excluido;
	}

	public String getObservacionCandidato() {
		return observacionCandidato;
	}

	public void setObservacionCandidato(String observacionCandidato) {
		this.observacionCandidato = observacionCandidato;
	}

	public String getValidado() {
		return validado;
	}

	public void setValidado(String validado) {
		this.validado = validado;
	}

	public String getValoracionMerito() {
		return valoracionMerito;
	}

	public void setValoracionMerito(String valoracionMerito) {
		this.valoracionMerito = valoracionMerito;
	}

	public String getValorMerito() {
		return valorMerito;
	}

	public void setValorMerito(String valorMerito) {
		this.valorMerito = valorMerito;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial validación [fecha=" + fecha + "codigoItem=" + codigoItem + "evaluador=" + evaluador + "excluido=" + excluido + "observacionCandidato="
					+ observacionCandidato + "validado=" + validado + "valoracionMerito=" + valoracionMerito + "valorMerito=" + valorMerito + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((codigoItem == null) ? 0 : codigoItem.hashCode());
		result = prime * result + ((evaluador == null) ? 0 : evaluador.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((observacionCandidato == null) ? 0 : observacionCandidato.hashCode());
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());
		result = prime * result + ((valoracionMerito == null) ? 0 : valoracionMerito.hashCode());
		result = prime * result + ((valorMerito == null) ? 0 : valorMerito.hashCode());
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
		HistorialValidacion other = (HistorialValidacion) obj;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fecha == null) {
			if (other.fecha != null) {
				return false;
			}
		} else if (formater.format(fecha).compareTo(formater.format(other.fecha)) != 0) {
			return false;
		}
		if (codigoItem == null) {
			if (other.codigoItem != null) {
				return false;
			}
		} else if (!codigoItem.equals(other.codigoItem)) {
			return false;
		}
		if (evaluador == null) {
			if (other.evaluador != null) {
				return false;
			}
		} else if (!evaluador.equals(other.evaluador)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (observacionCandidato == null) {
			if (other.observacionCandidato != null) {
				return false;
			}
		} else if (!observacionCandidato.equals(other.observacionCandidato)) {
			return false;
		}
		if (validado == null) {
			if (other.validado != null) {
				return false;
			}
		} else if (!validado.equals(other.validado)) {
			return false;
		}
		if (valoracionMerito == null) {
			if (other.valoracionMerito != null) {
				return false;
			}
		} else if (!valoracionMerito.equals(other.valoracionMerito)) {
			return false;
		}
		if (valorMerito == null) {
			if (other.valorMerito != null) {
				return false;
			}
		} else if (!valorMerito.equals(other.valorMerito)) {
			return false;
		}
		
		return true;
	}

}

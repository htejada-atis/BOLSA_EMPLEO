package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Clase para controlar el listado de meritos del resultado .
 * 
 * @author ATISoluciones 2021
 */
public class MeritoResultado extends Merito implements Serializable {

	private static final long serialVersionUID = 1L;

	// id de solicitud bolsa mérito
	private Integer codNumMeritoSolicitud; 
	private List<MeritoSolicitudValoracion> valoraciones;
	private String observacionCandidato;
	private String desglose;
	private Double valorMeritoSolicitud;
	private Double resultado;
	private Boolean excluido;
	private Boolean validado;
	private ItemBaremacion itemMeritoSolicitud;
	
	
	/**
	 * Constructor por defecto.
	 */
	public MeritoResultado() {

	}

	/** Constructor con parametros .
	 * @param pmerito .
	 * @param pcodNumMeritoSolicitud .
	 * @param pvaloraciones .
	 * @param pobservacionCandidato .
	 * @param pdesglose .
	 * @param pvalor .
	 * @param presultado .
	 * @param pexcluido .
	 * @param pvalidado .
	 * @param pitemMeritoSolicitud .
	 */
	public MeritoResultado(Merito pmerito, Integer pcodNumMeritoSolicitud, List<MeritoSolicitudValoracion> pvaloraciones, String pobservacionCandidato, 
			String pdesglose, Double pvalor, Double presultado, Boolean pexcluido, Boolean pvalidado, ItemBaremacion pitemMeritoSolicitud) {
		super(pmerito);
		this.codNumMeritoSolicitud = pcodNumMeritoSolicitud;
		this.valoraciones = pvaloraciones;
		this.observacionCandidato = pobservacionCandidato;
		this.desglose = pdesglose;
		this.valorMeritoSolicitud = pvalor;
		this.resultado = presultado;
		this.excluido = pexcluido;
		this.validado = pvalidado;
		this.itemMeritoSolicitud = pitemMeritoSolicitud;
	}

	public Integer getCodNumMeritoSolicitud() {
		return this.codNumMeritoSolicitud;
	}

	public void setCodNumMeritoSolicitud(Integer codNumMeritoSolicitud) {
		this.codNumMeritoSolicitud = codNumMeritoSolicitud;
	}

	public List<MeritoSolicitudValoracion> getValoraciones() {
		return this.valoraciones;
	}

	public void setValoraciones(List<MeritoSolicitudValoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
	
	public String getObservacionCandidato() {
		return observacionCandidato;
	}

	public void setObservacionCandidato(String observacionCandidato) {
		this.observacionCandidato = observacionCandidato;
	}
	
	public String getDesglose() {
		return desglose;
	}

	public void setDesglose(String desglose) {
		this.desglose = desglose;
	}

	public Double getResultado() {
		return resultado;
	}

	public void setResultado(Double resultado) {
		this.resultado = resultado;
	}
	
	public Double getValorMeritoSolicitud() {
		return this.valorMeritoSolicitud;
	}
	
	public void setValorMeritoSolicitud(Double valor) {
		this.valorMeritoSolicitud = valor;
	}
	
	public Boolean isExcluido() {
		return this.excluido;
	}
	
	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	public Boolean isValidado() {
		return this.validado;
	}
	
	public void setValidado(Boolean validado) {
		this.validado = validado;
	}
	
	public ItemBaremacion getItemMeritoSolicitud() {
		return itemMeritoSolicitud;
	}

	public void setItemMeritoSolicitud(ItemBaremacion itemMeritoSolicitud) {
		this.itemMeritoSolicitud = itemMeritoSolicitud;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoResultado [merito=" + super.toString() + ", codNumMeritoSolicitud=" + codNumMeritoSolicitud + ", valoraciones=" + valoraciones 
				+ ", observacionCandidato=" + observacionCandidato + ", desglose=" + desglose + ", valorMeritoSolicitud=" + valorMeritoSolicitud 
				+ ", resultado=" + resultado + ", excluido=" + excluido + ", validado=" + validado + ", itemMeritoSolicitud=" + itemMeritoSolicitud + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNumMeritoSolicitud == null) ? 0 : codNumMeritoSolicitud.hashCode());
		result = prime * result + ((valoraciones == null) ? 0 : valoraciones.hashCode());
		result = prime * result + ((observacionCandidato == null) ? 0 : observacionCandidato.hashCode());
		result = prime * result + ((desglose == null) ? 0 : desglose.hashCode());
		result = prime * result + ((valorMeritoSolicitud == null) ? 0 : valorMeritoSolicitud.hashCode());
		result = prime * result + ((resultado == null) ? 0 : resultado.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());
		result = prime * result + ((itemMeritoSolicitud == null) ? 0 : itemMeritoSolicitud.hashCode());
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
		MeritoResultado other = (MeritoResultado) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (valoraciones == null) {
			if (other.valoraciones != null) {
				return false;
			}
		} else if (!valoraciones.equals(other.valoraciones)) {
			return false;
		}
		if (observacionCandidato == null) {
			if (other.observacionCandidato != null) {
				return false;
			}
		} else if (!observacionCandidato.equals(other.observacionCandidato)) {
			return false;
		}
		if (desglose == null) {
			if (other.desglose != null) {
				return false;
			}
		} else if (!desglose.equals(other.desglose)) {
			return false;
		}
		if (valorMeritoSolicitud == null) {
			if (other.valorMeritoSolicitud != null) {
				return false;
			}
		} else if (!valorMeritoSolicitud.equals(other.valorMeritoSolicitud)) {
			return false;
		}
		if (resultado == null) {
			if (other.resultado != null) {
				return false;
			}
		} else if (!resultado.equals(other.resultado)) {
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
		if (itemMeritoSolicitud == null) {
			if (other.itemMeritoSolicitud != null) {
				return false;
			}
		} else if (!itemMeritoSolicitud.equals(other.itemMeritoSolicitud)) {
			return false;
		}
		
		return true;
	}
	
}

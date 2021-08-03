package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/** Clase oferta candidato para la contratación .
 * @author atis
 */
public class OfertaCandidato implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private PlazaOfertada plaza;
	private UsuarioBolsaEmpleo candidato;
	private String resultado;
	private Date fechaResultado;
	

	/** Constructor por defecto.
	 */
	public OfertaCandidato() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public OfertaCandidato(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	/** Constructor copia.
	 * @param copia Dedicacion a copiar
	 */
	public OfertaCandidato(OfertaCandidato copia) {
		this.codNum = copia.codNum;
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer idBolsa) {
		this.codNum = idBolsa;
	}
	
	
	@Override
	public String toString() {
		return "Contratacion [codNum=" + codNum + ", plaza=" + plaza + ", candidato=" + candidato 
				+ ", resultado=" + resultado + ", fechaResultado=" + fechaResultado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((plaza == null) ? 0 : plaza.hashCode());
		result = prime * result + ((candidato == null) ? 0 : candidato.hashCode());
		result = prime * result + ((resultado == null) ? 0 : resultado.hashCode());
		result = prime * result + ((fechaResultado == null) ? 0 : fechaResultado.hashCode());
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
		
		OfertaCandidato other = (OfertaCandidato) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (plaza == null) {
			if (other.plaza != null) {
				return false;
			}
		} else if (!plaza.equals(other.plaza)) {
			return false;
		}
		if (candidato == null) {
			if (other.candidato != null) {
				return false;
			}
		} else if (!candidato.equals(other.candidato)) {
			return false;
		}
		if (resultado == null) {
			if (other.resultado != null) {
				return false;
			}
		} else if (!resultado.equals(other.resultado)) {
			return false;
		}
		if (fechaResultado == null) {
			if (other.fechaResultado != null) {
				return false;
			}
		} else if (!fechaResultado.equals(other.fechaResultado)) {
			return false;
		}
		
		return true;
	}
}
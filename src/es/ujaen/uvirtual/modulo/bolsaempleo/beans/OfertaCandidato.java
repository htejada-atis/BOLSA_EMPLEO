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
	private Boolean resultado;
	private Date fechaResultado;
	private Integer preferencia;
	

	/** Constructor por defecto.
	 */
	public OfertaCandidato() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pplaza .
	 * @param pcandidato .
	 * @param presultado .
	 * @param pfechaResultado .
	 * @param ppreferencia .
	 */
	public OfertaCandidato(Integer pcodNum, PlazaOfertada pplaza, UsuarioBolsaEmpleo pcandidato, boolean presultado, Date pfechaResultado, Integer ppreferencia) {
		super();
		this.codNum = pcodNum;
		this.plaza = pplaza;
		this.candidato = pcandidato;
		this.resultado = presultado;
		this.fechaResultado = pfechaResultado;
		this.preferencia = ppreferencia;
	}
	
	/** Constructor copia.
	 * @param copia Dedicacion a copiar
	 */
	public OfertaCandidato(OfertaCandidato copia) {
		this.codNum = copia.codNum;
		this.plaza = copia.plaza;
		this.candidato = copia.candidato;
		this.resultado = copia.resultado;
		this.fechaResultado = copia.fechaResultado;
		this.preferencia = copia.preferencia;
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer idOferta) {
		this.codNum = idOferta;
	}
	
	public PlazaOfertada getPlaza() {
		return plaza;
	}
	
	public void setPlaza(PlazaOfertada plaza) {
		this.plaza = plaza;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}
	
	public void setCandidato(UsuarioBolsaEmpleo candidato) {
		this.candidato = candidato;
	}
	
	public Boolean isResultado() {
		return resultado;
	}
	
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
	
	public Date getFechaResultado() {
		return fechaResultado;
	}
	
	public void setFechaResultado(Date fechaResultado) {
		this.fechaResultado = fechaResultado;
	}
	
	public Integer getPreferencia() {
		return preferencia;
	}

	public void setPreferencia(Integer preferencia) {
		this.preferencia = preferencia;
	}
	
	@Override
	public String toString() {
		return "Contratacion [codNum=" + codNum + ", plaza=" + plaza + ", candidato=" + candidato 
				+ ", resultado=" + resultado + ", fechaResultado=" + fechaResultado + ", preferencia=" + preferencia + "]";
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
		result = prime * result + ((preferencia == null) ? 0 : preferencia.hashCode());
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
		if (preferencia == null) {
			if (other.preferencia != null) {
				return false;
			}
		} else if (!preferencia.equals(other.preferencia)) {
			return false;
		}
		
		return true;
	}
	
}
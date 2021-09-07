package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/** Clase contratación para la contratación en la bolsa de empleo .
 * @author atis
 */
public class Contratacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private PlazaOfertada plaza;
	private UsuarioBolsaEmpleo candidato;
	private String resultado;
	private Date fechaCita;
	private Date fechaResultado;
	

	/** Constructor por defecto.
	 */
	public Contratacion() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pplaza .
	 * @param pcandidato .
	 * @param presultado .
	 * @param pfechaCita .
	 * @param pfechaResultado .
	 */
	public Contratacion(Integer pcodNum, PlazaOfertada pplaza, UsuarioBolsaEmpleo pcandidato, String presultado, Date pfechaCita, Date pfechaResultado) {
		super();
		this.codNum = pcodNum;
		this.plaza = pplaza;
		this.candidato = pcandidato;
		this.resultado = presultado;
		this.fechaCita = pfechaCita;
		this.fechaResultado = pfechaResultado;
	}
	
	/** Constructor con parametros.
	 * @param pcandidato .
	 * @param pfechaCita .
	 */
	public Contratacion(UsuarioBolsaEmpleo pcandidato, Date pfechaCita) {
		super();
		this.candidato = pcandidato;
		this.fechaCita = pfechaCita;
	}
	
	/** Constructor copia.
	 * @param copia Contratación a copiar
	 */
	public Contratacion(Contratacion copia) {
		this.codNum = copia.codNum;
		this.plaza = copia.plaza;
		this.candidato = copia.candidato;
		this.resultado = copia.resultado;
		this.fechaCita = copia.fechaCita;
		this.fechaResultado = copia.fechaResultado;
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer idBolsa) {
		this.codNum = idBolsa;
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

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public Date getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(Date fechaCita) {
		this.fechaCita = fechaCita;
	}
	
	public Date getFechaResultado() {
		return fechaResultado;
	}

	public void setFechaResultado(Date fechaResultado) {
		this.fechaResultado = fechaResultado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	@Override
	public String toString() {
		return "Contratacion [codNum=" + codNum + ", plaza=" + plaza + ", candidato=" + candidato 
				+ ", resultado=" + resultado + ", fechaCita=" + fechaCita + ", fechaResultado=" + fechaResultado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((plaza == null) ? 0 : plaza.hashCode());
		result = prime * result + ((candidato == null) ? 0 : candidato.hashCode());
		result = prime * result + ((resultado == null) ? 0 : resultado.hashCode());
		result = prime * result + ((fechaCita == null) ? 0 : fechaCita.hashCode());
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
		
		Contratacion other = (Contratacion) obj;
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
		if (fechaCita == null) {
			if (other.fechaCita != null) {
				return false;
			}
		} else if (!fechaCita.equals(other.fechaCita)) {
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
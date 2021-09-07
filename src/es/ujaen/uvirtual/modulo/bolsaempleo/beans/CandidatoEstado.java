package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase estado del candidato de bolsa empleo.
 * @author ATISoluciones 2021
 *
 */
public class CandidatoEstado extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNumEstado;
	private Bolsa bolsa;
	private PlazaOfertada plaza;
	private String estado;

	
	/** Constructor por defecto.
	 */
	public CandidatoEstado() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param pcodNumEstado .
	 * @param pbolsa .
	 * @param pplaza .
	 * @param pestado .
	 */
	public CandidatoEstado(UsuarioBolsaEmpleo pusuario, Integer pcodNumEstado, Bolsa pbolsa, PlazaOfertada pplaza, String pestado) {
		super(pusuario);
		this.codNumEstado = pcodNumEstado;
		this.bolsa = pbolsa;
		this.plaza = pplaza;
		this.estado = pestado;
		this.bolsa = pbolsa;
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 */
	public CandidatoEstado(UsuarioBolsaEmpleo pusuario) {
		super(pusuario);
	}
	
	/** Constructor copia.
	 * @param copia CandidatoEstado a copiar
	 */
	public CandidatoEstado(CandidatoEstado copia) {
		super(copia);
		this.codNumEstado = copia.codNumEstado;
		this.bolsa = copia.bolsa;
		this.plaza = copia.plaza;
		this.estado = copia.estado;
	}

	public Integer getCodNumEstado() {
		return codNumEstado;
	}

	public void setCodNumEstado(Integer codNumEstado) {
		this.codNumEstado = codNumEstado;
	}

	public Bolsa getBolsa() {
		return bolsa;
	}

	public void setBolsa(Bolsa bolsa) {
		this.bolsa = bolsa;
	}

	public PlazaOfertada getPlaza() {
		return plaza;
	}

	public void setPlaza(PlazaOfertada plaza) {
		this.plaza = plaza;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Candidato estado [usuario= " + super.toString() + ", codNumEstado=" + codNumEstado + ", bolsa=" + bolsa
				+ ", plaza=" + plaza + ", estado=" + estado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNumEstado == null) ? 0 : codNumEstado.hashCode());
		result = prime * result + ((bolsa == null) ? 0 : bolsa.hashCode());
		result = prime * result + ((plaza == null) ? 0 : plaza.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
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
		CandidatoEstado other = (CandidatoEstado) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (codNumEstado == null) {
			if (other.codNumEstado != null) {
				return false;
			}
		} else if (!codNumEstado.equals(other.codNumEstado)) {
			return false;
		}
		if (bolsa == null) {
			if (other.bolsa != null) {
				return false;
			}
		} else if (!bolsa.equals(other.bolsa)) {
			return false;
		}
		if (plaza == null) {
			if (other.plaza != null) {
				return false;
			}
		} else if (!plaza.equals(other.plaza)) {
			return false;
		}
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		
		return true;
	}
	
}

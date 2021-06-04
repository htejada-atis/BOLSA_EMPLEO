package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase con los datos del Usuario que suministra la vista
 * VUJA_NET_BEP_AR_PERSONA.
 * 
 * @author ATISoluciones 2021
 */
public class UsuarioBolsaEmpleoVuja implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codInt;
	private String strTipoDocumento;
	private String idNif;
	private String letraNif;
	private String prsNif;
	private String strNombre;
	private String strApellido1;
	private String strApellido2;
	private String emailAlta;

	/**
	 * Constructor por defecto.
	 */
	public UsuarioBolsaEmpleoVuja() {

	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodInt           .
	 * @param pstrTipoDocumento .
	 * @param pidNif            .
	 * @param pletraNif         .
	 * @param ppprsNif          .
	 * @param pstrNombre        .
	 * @param pstrApellido1     .
	 * @param pstrApellido2     .
	 * @param pemailAlta        .
	 */
	public UsuarioBolsaEmpleoVuja(Integer pcodInt, String pstrTipoDocumento, String pidNif, String pletraNif,
			String ppprsNif, String pstrNombre, String pstrApellido1, String pstrApellido2, String pemailAlta) {
		super();
		this.codInt = pcodInt;
		this.strTipoDocumento = pstrTipoDocumento;
		this.idNif = pidNif;
		this.letraNif = pletraNif;
		this.prsNif = ppprsNif;
		this.strNombre = pstrNombre;
		this.strApellido1 = pstrApellido1;
		this.strApellido2 = pstrApellido2;
		this.emailAlta = pemailAlta;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Usuario a copiar
	 */
	public UsuarioBolsaEmpleoVuja(UsuarioBolsaEmpleoVuja copia) {
		this.codInt = copia.codInt;
		this.strTipoDocumento = copia.strTipoDocumento;
		this.idNif = copia.idNif;
		this.letraNif = copia.letraNif;
		this.prsNif = copia.prsNif;
		this.strNombre = copia.strNombre;
		this.strApellido1 = copia.strApellido1;
		this.strApellido2 = copia.strApellido2;
		this.emailAlta = copia.emailAlta;
	}

	public Integer getCodInt() {
		return codInt;
	}

	public void setCodInt(Integer codInt) {
		this.codInt = codInt;
	}

	public String getStrTipoDocumento() {
		return strTipoDocumento;
	}

	public void setStrTipoDocumento(String pstrTipoDocumento) {
		this.strTipoDocumento = pstrTipoDocumento;
	}

	public String getIdNif() {
		return idNif;
	}

	public void setIdNif(String idNif) {
		this.idNif = idNif;
	}

	public String getLetraNif() {
		return letraNif;
	}

	public void setLetraNif(String letraNif) {
		this.letraNif = letraNif;
	}

	public String getPrsNif() {
		return prsNif;
	}

	public void setPrsNif(String prsNif) {
		this.prsNif = prsNif;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public String getStrApellido1() {
		return strApellido1;
	}

	public void setStrApellido1(String strApellido1) {
		this.strApellido1 = strApellido1;
	}

	public String getStrApellido2() {
		return strApellido2;
	}

	public void setStrApellido2(String pstrApellido2) {
		this.strApellido2 = pstrApellido2;
	}

	public String getEmailAlta() {
		return emailAlta;
	}

	public void setEmailAlta(String emailAlta) {
		this.emailAlta = emailAlta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UsuarioBolsaEmpleoVuja [codInt=" + codInt + ", strTipoDocumento=" + strTipoDocumento + ", idNif=" + idNif
				+ ", letraNif=" + letraNif + ", prsNif=" + prsNif + ", strNombre=" + strNombre + ", strApellido1="
				+ strApellido1 + ", strApellido2=" + strApellido2 + ", emailAlta=" + emailAlta + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codInt == null) ? 0 : codInt.hashCode());
		result = prime * result + ((strTipoDocumento == null) ? 0 : strTipoDocumento.hashCode());
		result = prime * result + ((idNif == null) ? 0 : idNif.hashCode());
		result = prime * result + ((letraNif == null) ? 0 : letraNif.hashCode());
		result = prime * result + ((prsNif == null) ? 0 : prsNif.hashCode());
		result = prime * result + ((strNombre == null) ? 0 : strNombre.hashCode());
		result = prime * result + ((strApellido1 == null) ? 0 : strApellido1.hashCode());
		result = prime * result + ((strApellido2 == null) ? 0 : strApellido2.hashCode());
		result = prime * result + ((emailAlta == null) ? 0 : emailAlta.hashCode());
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
		
		UsuarioBolsaEmpleoVuja other = (UsuarioBolsaEmpleoVuja) obj;
		if (codInt == null) {
			if (other.codInt != null) {
				return false;
			}
		} else if (!codInt.equals(other.codInt)) {
			return false;
		}
		if (strTipoDocumento == null) {
			if (other.strTipoDocumento != null) {
				return false;
			}
		} else if (!strTipoDocumento.equals(other.strTipoDocumento)) {
			return false;
		}
		if (idNif == null) {
			if (other.idNif != null) {
				return false;
			}
		} else if (!idNif.equals(other.idNif)) {
			return false;
		}
		if (letraNif == null) {
			if (other.letraNif != null) {
				return false;
			}
		} else if (!letraNif.equals(other.letraNif)) {
			return false;
		}
		if (prsNif == null) {
			if (other.prsNif != null) {
				return false;
			}
		} else if (!prsNif.equals(other.prsNif)) {
			return false;
		}
		if (strNombre == null) {
			if (other.strNombre != null) {
				return false;
			}
		} else if (!strNombre.equals(other.strNombre)) {
			return false;
		}
		if (strApellido1 == null) {
			if (other.strApellido1 != null) {
				return false;
			}
		} else if (!strApellido1.equals(other.strApellido1)) {
			return false;
		}
		if (strApellido2 == null) {
			if (other.strApellido2 != null) {
				return false;
			}
		} else if (!strApellido2.equals(other.strApellido2)) {
			return false;
		}
		if (emailAlta == null) {
			if (other.emailAlta != null) {
				return false;
			}
		} else if (!emailAlta.equals(other.emailAlta)) {
			return false;
		}

		return true;
	}
}

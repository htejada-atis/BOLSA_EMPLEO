package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase destinatario de bolsa empleo.
 * @author ATISoluciones 2021
 *
 */
public class Destinatario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Mensaje mensaje;
	private UsuarioBolsaEmpleo usuario;
	private String estado;
	private String email;

	
	/** Constructor por defecto.
	 */
	public Destinatario() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pmensaje .
	 * @param pusuario .
	 * @param pestado .
	 */
	public Destinatario(Integer pcodNum, Mensaje pmensaje, UsuarioBolsaEmpleo pusuario, String pestado) {
		super();
		this.codNum = pcodNum;
		this.mensaje = pmensaje;
		this.usuario = pusuario;
		this.estado = pestado;
	}
	
	/** Constructor copia.
	 * @param copia Destinatario a copiar
	 */
	public Destinatario(Destinatario copia) {
		this.codNum = copia.codNum;
		this.mensaje = copia.mensaje;
		this.usuario = copia.usuario;
		this.estado = copia.estado;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Destinatario [codNum= " + codNum + ", mensaje=" + mensaje + ", usuario=" + usuario + ", estado=" + estado + ", email=" + email + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((mensaje == null) ? 0 : mensaje.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Destinatario other = (Destinatario) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (mensaje == null) {
			if (other.mensaje != null) {
				return false;
			}
		} else if (!mensaje.equals(other.mensaje)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		
		return true;
	}
	
}

package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase destinatario de bolsa empleo.
 * @author ATISoluciones 2021
 *
 */
public class Destinatario extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNumMensaje;
	private String estado;

	
	/** Constructor por defecto.
	 */
	public Destinatario() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param pcodNumMensaje .
	 * @param pestado .
	 */
	public Destinatario(UsuarioBolsaEmpleo pusuario, Integer pcodNumMensaje, String pestado) {
		super(pusuario);
		this.codNumMensaje = pcodNumMensaje;
		this.estado = pestado;
	}
	
	/** Constructor copia.
	 * @param copia Destinatario a copiar
	 */
	public Destinatario(Destinatario copia) {
		super(copia);
		this.codNumMensaje = copia.codNumMensaje;
		this.estado = copia.estado;
	}
	
	public Integer getCodNumMensaje() {
		return codNumMensaje;
	}

	public void setCodNumMensaje(Integer codNumMensaje) {
		this.codNumMensaje = codNumMensaje;
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
		return "Destinatario [usuario= " + super.toString() + ", codNumMensaje=" + codNumMensaje + ", estado=" + estado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNumMensaje == null) ? 0 : codNumMensaje.hashCode());
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
		Destinatario other = (Destinatario) obj;
		if (!super.equals(other)) {
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

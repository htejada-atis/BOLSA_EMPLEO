package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;


/** Clase convocatoria de docentia.
 * @author jlopez
 *
 */
public class Noticia implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String enlace;
	private String texto;
	private Date fecha;
	
	
	/** Constructor por defecto.
	 */
	public Noticia() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param penlace .
	 * @param ptexto .
	 * @param pfecha .
	 */
	public Noticia(Integer pcodNum, String penlace, String ptexto, Date pfecha) {
		super();
		this.codNum = pcodNum;
		this.enlace = penlace;
		this.texto = ptexto;
		this.fecha = pfecha;
	}
	
	/** Constructor copia.
	 * @param copia Noticia a copiar
	 */
	public Noticia(Noticia copia) {
		this.codNum = copia.codNum;
		this.enlace = copia.enlace;
		this.texto = copia.texto;
		this.fecha = copia.fecha;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Noticia [codNum=" + codNum + ", enlace=" + enlace
				+ ", texto=" + texto + ", fecha=" + fecha + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enlace == null) ? 0 : enlace.hashCode());
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
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
		Noticia other = (Noticia) obj;
		if (enlace == null) {
			if (other.enlace != null) {
				return false;
			}
		} else if (!enlace.equals(other.enlace)) {
			return false;
		}
		if (texto == null) {
			if (other.texto != null) {
				return false;
			}
		} else if (!texto.equals(other.texto)) {
			return false;
		}
		if (fecha == null) {
			if (other.fecha != null) {
				return false;
			}
		} else if (!fecha.equals(other.fecha)) {
			return false;
		}
		
		return true;
	}
	
}

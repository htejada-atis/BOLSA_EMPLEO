package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/**
 * Clase mensaje de bolsa empleo.
 * 
 * @author ATISoluciones
 */
public class Mensaje implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String titulo;
	private String cuerpo;
	private Date fechaCreacion;
	private String estado;
	private transient InputStream adjunto;

	/**
	 * Constructor por defecto.
	 */
	public Mensaje() {
		super();
		this.codNum = null;
		this.titulo = "";
		this.cuerpo = "";
		this.fechaCreacion = BolsaEmpleoUtils.getCurrentDateTime();
		this.estado = "N";
		this.adjunto = null;
	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum        .
	 * @param ptitulo        .
	 * @param pcuerpo        .
	 * @param pfechaCreacion .
	 * @param pestado        .
	 */
	public Mensaje(Integer pcodNum, String ptitulo, String pcuerpo, Date pfechaCreacion, String pestado) {
		super();
		this.codNum = pcodNum;
		this.titulo = ptitulo;
		this.cuerpo = pcuerpo;
		this.fechaCreacion = pfechaCreacion;
		this.estado = pestado;
	}
	
	/** Constructor con parametros.
	 * @param ptitulo .
	 * @param pcuerpo .
	 * @param pfechaCreacion .
	 * @param pestado .
	 */
	public Mensaje(String ptitulo, String pcuerpo, Date pfechaCreacion, String pestado) {
		super();
		this.titulo = ptitulo;
		this.cuerpo = pcuerpo;
		this.fechaCreacion = pfechaCreacion;
		this.estado = pestado;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public Mensaje(Mensaje copia) {
		this.codNum = copia.codNum;
		this.titulo = copia.titulo;
		this.cuerpo = copia.cuerpo;
		this.fechaCreacion = copia.fechaCreacion;
		this.estado = copia.estado;
		this.adjunto = copia.adjunto;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String ptitulo) {
		this.titulo = ptitulo;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public void setFechaCreacion(Date f) {
		this.fechaCreacion = f;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}
	
	public void setEstado(String e) {
		this.estado = e;
	}

	public String getEstado() {
		return this.estado;
	}
	
	public InputStream getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(InputStream adjunto) {
		this.adjunto = adjunto;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Mensaje [codNum=" + codNum + ", titulo=" + titulo + ", cuerpo=" + cuerpo + ", fechaCreacion=" 
				+ fechaCreacion + ", estado=" + estado + ", adjunto=" + adjunto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((cuerpo == null) ? 0 : cuerpo.hashCode());
		result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((adjunto == null) ? 0 : adjunto.hashCode());
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
		Mensaje other = (Mensaje) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (titulo == null) {
			if (other.titulo != null) {
				return false;
			}
		} else if (!titulo.equals(other.titulo)) {
			return false;
		}
		if (cuerpo == null) {
			if (other.cuerpo != null) {
				return false;
			}
		} else if (!cuerpo.equals(other.cuerpo)) {
			return false;
		}
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null) {
				return false;
			}
		} else if (!fechaCreacion.equals(other.fechaCreacion)) {
			return false;
		}
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (adjunto == null) {
			if (other.adjunto != null) {
				return false;
			}
		} else if (!adjunto.equals(other.adjunto)) {
			return false;
		}

		return true;
	}
	
}

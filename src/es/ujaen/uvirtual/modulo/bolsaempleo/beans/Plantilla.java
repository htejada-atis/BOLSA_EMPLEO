package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase plantilla para los mensajes de bolsa empleo.
 * 
 * @author ATISoluciones
 */
public class Plantilla implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	private String titulo;
	private String cuerpo;

	/**
	 * Constructor por defecto.
	 */
	public Plantilla() {
		super();
		this.codNum = null;
		this.titulo = "";
		this.cuerpo = "";
	}

	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param ptitulo .
	 * @param pcuerpo .
	 * @param pnombre .
	 */
	public Plantilla(Integer pcodNum, String ptitulo, String pcuerpo, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
		this.titulo = ptitulo;
		this.cuerpo = pcuerpo;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public Plantilla(Plantilla copia) {
		this.codNum = copia.codNum;
		this.nombre = copia.nombre;
		this.titulo = copia.titulo;
		this.cuerpo = copia.cuerpo;
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
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Plantilla [codNum=" + codNum + ", titulo=" + titulo + ", cuerpo=" + cuerpo + ", nombre=" + nombre + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((cuerpo == null) ? 0 : cuerpo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Plantilla other = (Plantilla) obj;
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
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}

		return true;
	}
	
}

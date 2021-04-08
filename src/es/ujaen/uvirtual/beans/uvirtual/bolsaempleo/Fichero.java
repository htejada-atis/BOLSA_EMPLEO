package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.InputStream;
import java.io.Serializable;

/** Clase fichero de bolsa empleo.
 * @author jlopez
 *
 */
public class Fichero implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	private String titulo;
	private InputStream archivo;
	
	
	/** Constructor por defecto.
	 */
	public Fichero() {
		
	}
	
	/** Constructor con parametros.
	 * @param pnombre .
	 * @param ptitulo .
	 * @param parchivo .
	 */
	public Fichero(String pnombre, String ptitulo, InputStream parchivo) {
		super();
		this.nombre = pnombre;
		this.titulo = ptitulo;
		this.archivo = parchivo;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pnombre .
	 * @param ptitulo .
	 * @param parchivo .
	 */
	public Fichero(Integer pcodNum, String pnombre, String ptitulo, InputStream parchivo) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
		this.titulo = ptitulo;
		this.archivo = parchivo;
	}
	
	
	/** Constructor copia.
	 * @param copia Fichero a copiar
	 */
	public Fichero(Fichero copia) {
		this.codNum = copia.codNum;
		this.nombre = copia.nombre;
		this.titulo = copia.titulo;
		this.archivo = copia.archivo;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}
	
	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Fichero [codNum=" + codNum + ", nombre=" + nombre 
				+ ", titulo=" + titulo + ", archivo=" + archivo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
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
		Fichero other = (Fichero) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (titulo == null) {
			if (other.titulo != null) {
				return false;
			}
		} else if (!titulo.equals(other.titulo)) {
			return false;
		}
		if (archivo == null) {
			if (other.archivo != null) {
				return false;
			}
		} else if (!archivo.equals(other.archivo)) {
			return false;
		}
		
		return true;
	}
	
}

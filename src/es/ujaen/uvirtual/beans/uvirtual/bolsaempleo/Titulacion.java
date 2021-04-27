package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.InputStream;
import java.io.Serializable;


/** Clase titulación de bolsa empleo.
 * @author jlopez
 *
 */
public class Titulacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	private String descripcion;
	private InputStream archivo;
	private Titulacion titulacion;
	private UsuarioBolsaEmpleo usuario;

	
	/** Constructor por defecto.
	 */
	public Titulacion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pnombre .
	 */
	public Titulacion(Integer pcodNum, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pnombre .
	 */
	public Titulacion(String pnombre) {
		super();
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public Titulacion(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Titulacion(Titulacion copia) {
		this.codNum = copia.codNum;
		this.nombre = copia.nombre;
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
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}
	
	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}
	
	public Titulacion getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}
	
	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public String toString() {
		return "Titulación [codNum=" + codNum + ", nombre=" + nombre + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Titulacion other = (Titulacion) obj;
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

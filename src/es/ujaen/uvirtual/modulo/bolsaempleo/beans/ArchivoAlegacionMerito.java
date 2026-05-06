package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase que representa un archivo de alegación de mérito.
 * 
 * @author ATISoluciones
 */
public class ArchivoAlegacionMerito implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nombre;

	/**
	 * Constructor por defecto.
	 */
	public ArchivoAlegacionMerito() {
	}

	/**
	 * Constructor con parámetros.
	 * 
	 * @param id     El identificador del archivo.
	 * @param nombre El nombre del archivo.
	 */
	public ArchivoAlegacionMerito(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	/**
	 * Constructor con parámetros.
	 * 
	 * @param nombre El nombre del archivo.
	 */
	public ArchivoAlegacionMerito(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Constructor con parámetros.
	 * 
	 * @param id El identificador del archivo.
	 */
	public ArchivoAlegacionMerito(Integer id) {
		this.id = id;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia El archivo a copiar.
	 */
	public ArchivoAlegacionMerito(ArchivoAlegacionMerito copia) {
		this.id = copia.id;
		this.nombre = copia.nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "ArchivoAlegacionMerito [id=" + id + ", nombre=" + nombre + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
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
		ArchivoAlegacionMerito other = (ArchivoAlegacionMerito) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
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
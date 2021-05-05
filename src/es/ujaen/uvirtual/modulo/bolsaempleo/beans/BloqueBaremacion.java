package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase bloque de baremación de bolsa empleo.
 * 
 * @author ATISoluciones 2021 
 */
public class BloqueBaremacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String nombre;
	private Boolean activo;
	private ApartadoBaremacion apartado;
	private Integer numeroMaximoMeritos;
	
	/** Constructor por defecto.
	 */
	public BloqueBaremacion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param papartado .
	 * @param pcodigo .
	 * @param pnombre .
	 * @param pactivo .
	 * @param pnumeroMaximoMeritos .
	 */
	public BloqueBaremacion(Integer pcodNum, ApartadoBaremacion papartado, String pcodigo, String pnombre, Boolean pactivo, Integer pnumeroMaximoMeritos) {
		super();
		this.apartado = papartado;
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
		this.activo = pactivo;		
		this.numeroMaximoMeritos = pnumeroMaximoMeritos;
	}
		
	/** Constructor copia.
	 * @param copia .
	 */
	public BloqueBaremacion(BloqueBaremacion copia) {
		this.codNum = copia.codNum;
		this.apartado = copia.apartado;
		this.codigo = copia.codigo;
		this.nombre = copia.nombre;
		this.activo = copia.activo;
		this.numeroMaximoMeritos = copia.numeroMaximoMeritos;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public ApartadoBaremacion getApartadoBaremacion() {
		return this.apartado;
	}
	
	public void setApartadoBaremacion(ApartadoBaremacion papartado) {
		this.apartado = papartado;
	} 

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Boolean isActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public Integer getNumeroMaximoMeritos() {
		return numeroMaximoMeritos;
	}

	public void setNumeroMaximoMeritos(Integer numeroMaximoMeritos) {
		this.numeroMaximoMeritos = numeroMaximoMeritos;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", apartado=" + apartado + ", codigo=" + codigo
				+ ", nombre=" + nombre + ", activo=" + activo + ", numeroMaximoMeritos=" + numeroMaximoMeritos + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((apartado == null) ? 0 : apartado.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());	
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
		result = prime * result + ((numeroMaximoMeritos == null) ? 0 : numeroMaximoMeritos.hashCode());
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
		BloqueBaremacion other = (BloqueBaremacion) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (apartado == null) {
			if (other.apartado != null) {
				return false;
			}
		} else if (!apartado.equals(other.apartado)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (activo == null) {
			if (other.activo != null) {
				return false;
			}
		} else if (!activo.equals(other.activo)) {
			return false;
		}
		if (numeroMaximoMeritos == null) {
			if (other.numeroMaximoMeritos != null) {
				return false;
			}
		} else if (!numeroMaximoMeritos.equals(other.numeroMaximoMeritos)) {
			return false;
		}
		
		return true;
	}
	
}

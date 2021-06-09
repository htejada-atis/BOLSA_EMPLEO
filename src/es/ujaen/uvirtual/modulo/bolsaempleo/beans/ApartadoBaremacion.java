package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase apartado de baremación de bolsa empleo.
 * 
 * @author ATISoluciones 2021
 */
public class ApartadoBaremacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String nombre;
	private Boolean activo;
	private Double puntuacionMaxima;
	private Double porcentajeMaximo;

	/**
	 * Constructor por defecto.
	 */
	public ApartadoBaremacion() {

	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum           .
	 * @param pcodigo           .
	 * @param pnombre           .
	 * @param pactivo           .
	 * @param ppuntuacionMaxima .
	 * @param pporcentajeMaximo .
	 */
	public ApartadoBaremacion(Integer pcodNum, String pcodigo, String pnombre, Boolean pactivo, Double ppuntuacionMaxima, Double pporcentajeMaximo) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
		this.activo = pactivo;
		this.puntuacionMaxima = ppuntuacionMaxima;
		this.porcentajeMaximo = pporcentajeMaximo;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public ApartadoBaremacion(ApartadoBaremacion copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.nombre = copia.nombre;
		this.activo = copia.activo;
		this.puntuacionMaxima = copia.puntuacionMaxima;
		this.porcentajeMaximo = copia.porcentajeMaximo;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
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

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public Double getPuntuacionMaxima() {
		return puntuacionMaxima;
	}

	public void setPuntuacionMaxima(Double puntuacionMaxima) {
		this.puntuacionMaxima = puntuacionMaxima;
	}
	
	public Double getPorcentajeMaximo() {
		return porcentajeMaximo;
	}

	public void setPorcentajeMaximo(Double porcentajeMaximo) {
		this.porcentajeMaximo = porcentajeMaximo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", codigo=" + codigo + ", nombre=" + nombre + ", activo="
				+ activo + ", puntuacionMaxima=" + puntuacionMaxima + ", porcentajeMaximo=" + porcentajeMaximo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
		result = prime * result + ((puntuacionMaxima == null) ? 0 : puntuacionMaxima.hashCode());
		result = prime * result + ((porcentajeMaximo == null) ? 0 : porcentajeMaximo.hashCode());
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
		ApartadoBaremacion other = (ApartadoBaremacion) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
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
		if (puntuacionMaxima == null) {
			if (other.puntuacionMaxima != null) {
				return false;
			}
		} else if (!puntuacionMaxima.equals(other.puntuacionMaxima)) {
			return false;
		}
		if (porcentajeMaximo == null) {
			if (other.porcentajeMaximo != null) {
				return false;
			}
		} else if (!porcentajeMaximo.equals(other.porcentajeMaximo)) {
			return false;
		}

		return true;
	}

}

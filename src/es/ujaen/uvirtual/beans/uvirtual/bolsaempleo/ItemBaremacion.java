package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;

/** Clase ítem de baremación de bolsa empleo.
 * 
 * @author ATISoluciones 2021 
 */
public class ItemBaremacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private BloqueBaremacion bloque;
	private String codigo;
	private String nombre;
	private String descripcion;
	private Boolean activo;	
	private String unidades;
	private Float valor;
	private Float valorMinimo;
	private Float valorMaximo;
	private String afinidad;
	private Boolean individualizado;
	
	/** Constructor por defecto.
	 */
	public ItemBaremacion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pbloque .
	 * @param pcodigo .
	 * @param pnombre .
	 * @param pactivo .
	 * @param punidades .
	 * @param pvalor .
	 * @param pvalorMinimo .
	 * @param pvalorMaximo .
	 * @param pafinidad .
	 * @param pindividualizado .
	 */
	public ItemBaremacion(Integer pcodNum, BloqueBaremacion pbloque, String pcodigo, String pnombre, Boolean pactivo, 
			String punidades, Float pvalor, Float pvalorMinimo, Float pvalorMaximo, String pafinidad, Boolean pindividualizado) {
		super();
		this.bloque = pbloque;
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
		this.activo = pactivo;
		this.unidades = punidades;
		this.valor = pvalor;
		this.valorMinimo = pvalorMinimo;
		this.valorMaximo = pvalorMaximo;
		this.afinidad = pafinidad;
		this.individualizado = pindividualizado;
	}
		
	/** Constructor copia.
	 * @param copia .
	 */
	public ItemBaremacion(ItemBaremacion copia) {
		this.codNum = copia.codNum;
		this.bloque = copia.bloque;
		this.codigo = copia.codigo;
		this.nombre = copia.nombre;
		this.activo = copia.activo;
		this.unidades = copia.unidades;
		this.valor = copia.valor;
		this.valorMinimo = copia.valorMinimo;
		this.valorMaximo = copia.valorMaximo;
		this.afinidad = copia.afinidad;
		this.individualizado = copia.individualizado;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public BloqueBaremacion getBloqueBaremacion() {
		return this.bloque;
	}
	
	public void setBloqueBaremacion(BloqueBaremacion pbloque) {
		this.bloque = pbloque;
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
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public String getUnidades() {
		return this.unidades;
	}

	public void setUnidades(String unidades) {
		this.unidades = unidades;
	}
	
	public Float getValor() {
		return this.valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
	
	public Float getValorMinimo() {
		return this.valorMinimo;
	}

	public void setValorMinimo(Float valorMinimo) {
		this.valorMinimo = valorMinimo;
	}
	
	public Float getValorMaximo() {
		return this.valorMaximo;
	}

	public void setValorMaximo(Float valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
	
	public String getAfinidad() {
		return this.afinidad;
	}

	public void setAfinidad(String afinidad) {
		this.afinidad = afinidad;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Boolean getIndividualizado() {
		return individualizado;
	}

	public void setIndividualizado(Boolean individualizado) {
		this.individualizado = individualizado;
	}
	
	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", bloque=" + bloque + ", codigo=" + codigo
				+ ", nombre=" + nombre + ", activo=" + activo + ", unidades=" + unidades + ", valorMinimo=" + valorMinimo 
				+ ", valorMaximo=" + valorMaximo + ", afinidad=" + afinidad + ", individualizado=" + individualizado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((bloque == null) ? 0 : bloque.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());		
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());	
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
		result = prime * result + ((unidades == null) ? 0 : unidades.hashCode());
		result = prime * result + ((valorMinimo == null) ? 0 : valorMinimo.hashCode());
		result = prime * result + ((valorMaximo == null) ? 0 : valorMaximo.hashCode());
		result = prime * result + ((afinidad == null) ? 0 : afinidad.hashCode());
		result = prime * result + ((individualizado == null) ? 0 : individualizado.hashCode());
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
		ItemBaremacion other = (ItemBaremacion) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (bloque == null) {
			if (other.bloque != null) {
				return false;
			}
		} else if (!bloque.equals(other.bloque)) {
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
		if (unidades == null) {
			if (other.unidades != null) {
				return false;
			}
		} else if (!unidades.equals(other.unidades)) {
			return false;
		}
		if (valorMinimo == null) {
			if (other.valorMinimo != null) {
				return false;
			}
		} else if (!valorMinimo.equals(other.valorMinimo)) {
			return false;
		}
		if (valorMaximo == null) {
			if (other.valorMaximo != null) {
				return false;
			}
		} else if (!valorMaximo.equals(other.valorMaximo)) {
			return false;
		}
		if (afinidad == null) {
			if (other.afinidad != null) {
				return false;
			}
		} else if (!afinidad.equals(other.afinidad)) {
			return false;
		}
		if (individualizado == null) {
			if (other.individualizado != null) {
				return false;
			}
		} else if (!individualizado.equals(other.individualizado)) {
			return false;
		}
		
		return true;
	}
	
}

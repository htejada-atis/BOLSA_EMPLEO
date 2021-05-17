package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase mérito preferente.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoPreferente implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String nombre;
	private String observaciones;
	private String tipo;
	private String tipoCalculo;
	private String aplicable;
	private Double base;
	private Double factor;
	private Double valorMaximo;
	private ItemBaremacion tipoItemBaremacion;
	private BloqueBaremacion aplicableBloqueBaremacion;
	private ApartadoBaremacion aplicableApartadoBaremacion;
	private ItemBaremacion aplicableItemBareamcion;
	private Boolean activo;

	/**
	 * Constructor por defecto.
	 */
	public MeritoPreferente() {

	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum                      .
	 * @param pcodigo                 .
	 * @param pobservaciones               .
	 * @param ptipo                        .
	 * @param ptipoCalculo                 .
	 * @param paplicable                   .
	 * @param pbase                        .
	 * @param pfactor                      .
	 * @param pvalorMaximo                 .
	 * @param ptipoItemBaremacion          .
	 * @param paplicableBloqueBaremacion   .
	 * @param paplicableApartadoBaremacion .
	 * @param paplicableItemBareamcion     .
	 * @param pactivo                      .
	 */
	public MeritoPreferente(Integer pcodNum, String pcodigo, String pnombre, String pobservaciones, String ptipo, String ptipoCalculo, String paplicable,
			Double pbase, Double pfactor, Double pvalorMaximo, ItemBaremacion ptipoItemBaremacion,
			BloqueBaremacion paplicableBloqueBaremacion, ApartadoBaremacion paplicableApartadoBaremacion,
			ItemBaremacion paplicableItemBareamcion, Boolean pactivo) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
		this.observaciones = pobservaciones;
		this.tipo = ptipo;
		this.tipoCalculo = ptipoCalculo;
		this.aplicable = paplicable;
		this.base = pbase;
		this.factor = pfactor;
		this.valorMaximo = pvalorMaximo;
		this.tipoItemBaremacion = ptipoItemBaremacion;
		this.aplicableBloqueBaremacion = paplicableBloqueBaremacion;
		this.aplicableApartadoBaremacion = paplicableApartadoBaremacion;
		this.aplicableItemBareamcion = paplicableItemBareamcion;
		this.activo = pactivo;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public MeritoPreferente(MeritoPreferente copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.nombre = copia.nombre;
		this.observaciones = copia.observaciones;
		this.tipo = copia.tipo;
		this.tipoCalculo = copia.tipoCalculo;
		this.aplicable = copia.aplicable;
		this.base = copia.base;
		this.factor = copia.factor;
		this.valorMaximo = copia.valorMaximo;
		this.tipoItemBaremacion = copia.tipoItemBaremacion;
		this.aplicableBloqueBaremacion = copia.aplicableBloqueBaremacion;
		this.aplicableApartadoBaremacion = copia.aplicableApartadoBaremacion;
		this.aplicableItemBareamcion = copia.aplicableItemBareamcion;
		this.activo = copia.activo;
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
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoCalculo() {
		return tipoCalculo;
	}

	public void setTipoCalculo(String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	public String getAplicable() {
		return aplicable;
	}

	public void setAplicable(String aplicable) {
		this.aplicable = aplicable;
	}

	public Double getBase() {
		return base;
	}

	public void setBase(Double base) {
		this.base = base;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	public Double getValorMaximo() {
		return this.valorMaximo;
	}

	public void setValorMaximo(Double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public ItemBaremacion getTipoItemBaremacion() {
		return this.tipoItemBaremacion;
	}

	public void setTipoItemBaremacion(ItemBaremacion itemBaremacion) {
		this.tipoItemBaremacion = itemBaremacion;
	}

	public BloqueBaremacion getAplicableBloqueBaremacion() {
		return this.aplicableBloqueBaremacion;
	}

	public void setAplicableBloqueBaremacion(BloqueBaremacion aplicableBloqueBaremacion) {
		this.aplicableBloqueBaremacion = aplicableBloqueBaremacion;
	}

	public ApartadoBaremacion getAplicableApartadoBaremacion() {
		return this.aplicableApartadoBaremacion;
	}

	public void setAplicableApartadoBaremacion(ApartadoBaremacion aplicableApartadoBaremacion) {
		this.aplicableApartadoBaremacion = aplicableApartadoBaremacion;
	}

	public ItemBaremacion getAplicableItemBaremacion() {
		return this.aplicableItemBareamcion;
	}

	public void setAplicableItemBaremacion(ItemBaremacion aplicableItemBaremacion) {
		this.aplicableItemBareamcion = aplicableItemBaremacion;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", codigo=" + codigo + " , nombre=" + nombre + " , observaciones=" + observaciones + ", tipo=" + tipo
				+ ", tipoCalculo=" + tipoCalculo + "aplicable=" + aplicable + ", base=" + base + ", factor=" + factor
				+ ", valorMaximo=" + valorMaximo + ", tipoItemBaremacion=" + tipoItemBaremacion
				+ ", aplicableBloqueBaremacion" + aplicableBloqueBaremacion + ", aplicableApartadoBaremacion="
				+ aplicableApartadoBaremacion + ", aplicableItemBareamcion=" + aplicableItemBareamcion + ", activo="
				+ activo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((tipoCalculo == null) ? 0 : tipoCalculo.hashCode());
		result = prime * result + ((aplicable == null) ? 0 : aplicable.hashCode());
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((factor == null) ? 0 : factor.hashCode());
		result = prime * result + ((valorMaximo == null) ? 0 : valorMaximo.hashCode());
		result = prime * result + ((tipoItemBaremacion == null) ? 0 : tipoItemBaremacion.hashCode());
		result = prime * result + ((aplicableBloqueBaremacion == null) ? 0 : aplicableBloqueBaremacion.hashCode());
		result = prime * result + ((aplicableApartadoBaremacion == null) ? 0 : aplicableApartadoBaremacion.hashCode());
		result = prime * result + ((aplicableItemBareamcion == null) ? 0 : aplicableItemBareamcion.hashCode());
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
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
		MeritoPreferente other = (MeritoPreferente) obj;
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
		if (observaciones == null) {
			if (other.observaciones != null) {
				return false;
			}
		} else if (!observaciones.equals(other.observaciones)) {
			return false;
		}
		if (tipo == null) {
			if (other.tipo != null) {
				return false;
			}
		} else if (!tipo.equals(other.tipo)) {
			return false;
		}
		if (tipoCalculo == null) {
			if (other.tipoCalculo != null) {
				return false;
			}
		} else if (!tipoCalculo.equals(other.tipoCalculo)) {
			return false;
		}
		if (aplicable == null) {
			if (other.aplicable != null) {
				return false;
			}
		} else if (!aplicable.equals(other.aplicable)) {
			return false;
		}
		if (base == null) {
			if (other.base != null) {
				return false;
			}
		} else if (!base.equals(other.base)) {
			return false;
		}
		if (factor == null) {
			if (other.factor != null) {
				return false;
			}
		} else if (!factor.equals(other.factor)) {
			return false;
		}
		if (valorMaximo == null) {
			if (other.valorMaximo != null) {
				return false;
			}
		} else if (!valorMaximo.equals(other.valorMaximo)) {
			return false;
		}
		if (tipoItemBaremacion == null) {
			if (other.tipoItemBaremacion != null) {
				return false;
			}
		} else if (!tipoItemBaremacion.equals(other.tipoItemBaremacion)) {
			return false;
		}
		if (aplicableBloqueBaremacion == null) {
			if (other.aplicableBloqueBaremacion != null) {
				return false;
			}
		} else if (!aplicableBloqueBaremacion.equals(other.aplicableBloqueBaremacion)) {
			return false;
		}
		if (aplicableApartadoBaremacion == null) {
			if (other.aplicableApartadoBaremacion != null) {
				return false;
			}
		} else if (!aplicableApartadoBaremacion.equals(other.aplicableApartadoBaremacion)) {
			return false;
		}
		if (aplicableItemBareamcion == null) {
			if (other.aplicableItemBareamcion != null) {
				return false;
			}
		} else if (!aplicableItemBareamcion.equals(other.aplicableItemBareamcion)) {
			return false;
		}
		if (activo == null) {
			if (other.activo != null) {
				return false;
			}
		} else if (!activo.equals(other.activo)) {
			return false;
		}

		return true;
	}

}

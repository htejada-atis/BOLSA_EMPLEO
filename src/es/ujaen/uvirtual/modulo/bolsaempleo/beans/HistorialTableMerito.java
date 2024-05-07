package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase historial mérito .
 * 
 * @author ATISoluciones
 */
public class HistorialTableMerito extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Integer bepiteCodNum;
	private Integer bepusuCodNum;
	private Double valor;
	private String descripcion;
	private String observacion;
	private ItemBaremacion item;
	private UsuarioBolsaEmpleo usuario;

	/**
	 * Constructor por defecto.
	 */
	public HistorialTableMerito() {

	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Mérito a copiar
	 */
	public HistorialTableMerito(HistorialTableMerito copia) {
		super(copia);
		this.codNum = copia.codNum;
		this.bepiteCodNum = copia.bepiteCodNum;
		this.bepusuCodNum = copia.bepusuCodNum;
		this.valor = copia.valor;
		this.descripcion = copia.descripcion;
		this.observacion = copia.observacion;
		this.item = copia.item;
		this.usuario = copia.usuario;
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer pcodNum) {
		this.codNum = pcodNum;
	}

	public Integer getBepIteCodNum() {
		return this.bepiteCodNum;
	}

	public void setBepIteCodNum(Integer param) {
		this.bepiteCodNum = param;
	}

	public Integer getBepEsuCodNum() {
		return this.bepusuCodNum;
	}

	public void setBepEsuCodNum(Integer param) {
		this.bepusuCodNum = param;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String param) {
		this.descripcion = param;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String param) {
		this.observacion = param;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial Mérito [historial=" + super.toString() + ", codNum=" + codNum + ", bepiteCodNum="
				+ bepiteCodNum + ", bepusuCodNum=" + bepusuCodNum + ", valor=" + valor + ", descripcion=" + descripcion
				+ ", observacion=" + observacion + ", item=" + item + ", usuario=" + usuario + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((bepiteCodNum == null) ? 0 : bepiteCodNum.hashCode());
		result = prime * result + ((bepusuCodNum == null) ? 0 : bepusuCodNum.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((observacion == null) ? 0 : observacion.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		HistorialTableMerito other = (HistorialTableMerito) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (bepiteCodNum == null) {
			if (other.bepiteCodNum != null) {
				return false;
			}
		} else if (!bepiteCodNum.equals(other.bepiteCodNum)) {
			return false;
		}
		if (bepusuCodNum == null) {
			if (other.bepusuCodNum != null) {
				return false;
			}
		} else if (!bepusuCodNum.equals(other.bepusuCodNum)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (observacion == null) {
			if (other.observacion != null) {
				return false;
			}
		} else if (!observacion.equals(other.observacion)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}

		return true;
	}

	public ItemBaremacion getItem() {
		return item;
	}

	public void setItem(ItemBaremacion item) {
		this.item = item;
	}

	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}

}

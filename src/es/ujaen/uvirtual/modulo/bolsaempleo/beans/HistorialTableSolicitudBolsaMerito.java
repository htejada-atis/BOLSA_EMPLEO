package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase historial mérito .
 * 
 * @author ATISoluciones
 */
public class HistorialTableSolicitudBolsaMerito extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Integer bepsboCodNum;
	private Integer bepmerCodNum;
	private Boolean flgExcluido;
	private Boolean flgValidado;
	private String observacionCandidato;
	private Double valor;
	private String desglose;
	private Double resultado;
	private Integer bepiteCodNum;
	private ItemBaremacion itemSolicitudBolsa;
	private String codigo;
	private String nombre;
	private Merito merito;

	/**
	 * Constructor por defecto.
	 */
	public HistorialTableSolicitudBolsaMerito() {

	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Mérito a copiar
	 */
	public HistorialTableSolicitudBolsaMerito(HistorialTableSolicitudBolsaMerito copia) {
		super(copia);
		this.codNum = copia.codNum;
		this.setBepsboCodNum(copia.getBepsboCodNum());
		this.setBepmerCodNum(copia.getBepmerCodNum());
		this.setFlgExcluido(copia.getFlgExcluido());
		this.setFlgValidado(copia.getFlgValidado());
		this.setObservacionCandidato(copia.getObservacionCandidato());
		this.setValor(copia.getValor());
		this.setDesglose(copia.getDesglose());
		this.setResultado(copia.getResultado());
		this.setBepiteCodNum(copia.getBepiteCodNum());
		this.setCodigo(copia.getCodigo());
		this.setNombre(copia.getNombre());
		this.itemSolicitudBolsa = copia.itemSolicitudBolsa;
		this.merito = copia.merito;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial Solicitud Bolsa Mérito [historial=" + super.toString() + ", codNum=" + codNum
				+ ", bepsboCodNum=" + bepsboCodNum + ", bepmerCodNum=" + bepmerCodNum + ", flgExcluido=" + flgExcluido
				+ ", flgValidado=" + flgValidado + ", observacionCandidato=" + observacionCandidato + ", valor=" + valor
				+ ", desglose=" + desglose + ", resultado=" + resultado + ", bepiteCodNum=" + bepiteCodNum + ", codigo="
				+ codigo + ", nombre=" + nombre + ", item=" + itemSolicitudBolsa + ", merito=" + getMerito() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((bepsboCodNum == null) ? 0 : bepsboCodNum.hashCode());
		result = prime * result + ((bepmerCodNum == null) ? 0 : bepmerCodNum.hashCode());
		result = prime * result + ((flgExcluido == null) ? 0 : flgExcluido.hashCode());
		result = prime * result + ((flgValidado == null) ? 0 : flgValidado.hashCode());
		result = prime * result + ((observacionCandidato == null) ? 0 : observacionCandidato.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((desglose == null) ? 0 : desglose.hashCode());
		result = prime * result + ((resultado == null) ? 0 : resultado.hashCode());
		result = prime * result + ((bepiteCodNum == null) ? 0 : bepiteCodNum.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((itemSolicitudBolsa == null) ? 0 : itemSolicitudBolsa.hashCode());
		result = prime * result + ((getMerito() == null) ? 0 : getMerito().hashCode());
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
		HistorialTableSolicitudBolsaMerito other = (HistorialTableSolicitudBolsaMerito) obj;
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
		if (bepsboCodNum == null) {
			if (other.bepsboCodNum != null) {
				return false;
			}
		} else if (!bepsboCodNum.equals(other.bepsboCodNum)) {
			return false;
		}
		if (bepmerCodNum == null) {
			if (other.bepmerCodNum != null) {
				return false;
			}
		} else if (!bepmerCodNum.equals(other.bepmerCodNum)) {
			return false;
		}
		if (flgExcluido == null) {
			if (other.flgExcluido != null) {
				return false;
			}
		} else if (!flgExcluido.equals(other.flgExcluido)) {
			return false;
		}
		if (flgValidado == null) {
			if (other.flgValidado != null) {
				return false;
			}
		} else if (!flgValidado.equals(other.flgValidado)) {
			return false;
		}
		if (observacionCandidato == null) {
			if (other.observacionCandidato != null) {
				return false;
			}
		} else if (!observacionCandidato.equals(other.observacionCandidato)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (desglose == null) {
			if (other.desglose != null) {
				return false;
			}
		} else if (!desglose.equals(other.desglose)) {
			return false;
		}
		if (resultado == null) {
			if (other.resultado != null) {
				return false;
			}
		} else if (!resultado.equals(other.resultado)) {
			return false;
		}
		if (bepiteCodNum == null) {
			if (other.bepiteCodNum != null) {
				return false;
			}
		} else if (!bepiteCodNum.equals(other.bepiteCodNum)) {
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
		if (itemSolicitudBolsa == null) {
			if (other.itemSolicitudBolsa != null) {
				return false;
			}
		} else if (!itemSolicitudBolsa.equals(other.itemSolicitudBolsa)) {
			return false;
		}
		if (getMerito() == null) {
			if (other.getMerito() != null) {
				return false;
			}
		} else if (!getMerito().equals(other.getMerito())) {
			return false;
		}

		return true;
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer pcodNum) {
		this.codNum = pcodNum;
	}

	public Integer getBepsboCodNum() {
		return bepsboCodNum;
	}

	public void setBepsboCodNum(Integer bepsboCodNum) {
		this.bepsboCodNum = bepsboCodNum;
	}

	public Integer getBepmerCodNum() {
		return bepmerCodNum;
	}

	public void setBepmerCodNum(Integer bepmerCodNum) {
		this.bepmerCodNum = bepmerCodNum;
	}

	public Boolean getFlgExcluido() {
		return flgExcluido;
	}

	public void setFlgExcluido(Boolean flgExcluido) {
		this.flgExcluido = flgExcluido;
	}

	public Boolean getFlgValidado() {
		return flgValidado;
	}

	public void setFlgValidado(Boolean flgValidado) {
		this.flgValidado = flgValidado;
	}

	public String getObservacionCandidato() {
		return observacionCandidato;
	}

	public void setObservacionCandidato(String observacionCandidato) {
		this.observacionCandidato = observacionCandidato;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDesglose() {
		return desglose;
	}

	public void setDesglose(String desglose) {
		this.desglose = desglose;
	}

	public Double getResultado() {
		return resultado;
	}

	public void setResultado(Double resultado) {
		this.resultado = resultado;
	}

	public Integer getBepiteCodNum() {
		return bepiteCodNum;
	}

	public void setBepiteCodNum(Integer bepiteCodNum) {
		this.bepiteCodNum = bepiteCodNum;
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

	public ItemBaremacion getItemSolicitudBolsa() {
		return itemSolicitudBolsa;
	}

	public void setItemSolicitudBolsa(ItemBaremacion itemSolicitudBolsa) {
		this.itemSolicitudBolsa = itemSolicitudBolsa;
	}

	public Merito getMerito() {
		return merito;
	}

	public void setMerito(Merito merito) {
		this.merito = merito;
	}

}

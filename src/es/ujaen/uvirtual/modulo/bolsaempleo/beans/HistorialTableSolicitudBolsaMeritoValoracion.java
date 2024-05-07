package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase historial mérito .
 * 
 * @author ATISoluciones
 */
public class HistorialTableSolicitudBolsaMeritoValoracion extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Integer bepsbmCodNum;
	private Integer bepafiCodNum;
	private Double valor;
	private Afinidad afinidad;

	/**
	 * Constructor por defecto.
	 */
	public HistorialTableSolicitudBolsaMeritoValoracion() {

	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Mérito a copiar
	 */
	public HistorialTableSolicitudBolsaMeritoValoracion(HistorialTableSolicitudBolsaMeritoValoracion copia) {
		super(copia);
		this.codNum = copia.codNum;
		this.bepsbmCodNum = copia.bepsbmCodNum;
		this.bepafiCodNum = copia.bepafiCodNum;
		this.valor = copia.valor;
		this.afinidad = copia.afinidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial Solicitud Bolsa Merito Valoracion [historial=" + super.toString() + ", codNum=" + codNum
				+ ", bepsbmCodNum=" + bepsbmCodNum + ", bepafiCodNum=" + bepafiCodNum + ", valor=" + valor
				+ ", afinidad=" + afinidad + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((bepsbmCodNum == null) ? 0 : bepsbmCodNum.hashCode());
		result = prime * result + ((bepafiCodNum == null) ? 0 : bepafiCodNum.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((afinidad == null) ? 0 : afinidad.hashCode());
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
		HistorialTableSolicitudBolsaMeritoValoracion other = (HistorialTableSolicitudBolsaMeritoValoracion) obj;
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
		if (bepsbmCodNum == null) {
			if (other.bepsbmCodNum != null) {
				return false;
			}
		} else if (!bepsbmCodNum.equals(other.bepsbmCodNum)) {
			return false;
		}
		if (bepafiCodNum == null) {
			if (other.bepafiCodNum != null) {
				return false;
			}
		} else if (!bepafiCodNum.equals(other.bepafiCodNum)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (afinidad == null) {
			if (other.afinidad != null) {
				return false;
			}
		} else if (!afinidad.equals(other.afinidad)) {
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

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getBepsbmCodNum() {
		return bepsbmCodNum;
	}

	public void setBepsbmCodNum(Integer bepsbmCodNum) {
		this.bepsbmCodNum = bepsbmCodNum;
	}

	public Integer getBepafiCodNum() {
		return bepafiCodNum;
	}

	public void setBepafiCodNum(Integer bepafiCodNum) {
		this.bepafiCodNum = bepafiCodNum;
	}

	public Afinidad getAfinidad() {
		return afinidad;
	}

	public void setAfinidad(Afinidad afinidad) {
		this.afinidad = afinidad;
	}

}

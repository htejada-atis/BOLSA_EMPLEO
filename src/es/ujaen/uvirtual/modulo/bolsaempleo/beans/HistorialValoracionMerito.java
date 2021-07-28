package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase historial Valoración Mérito .
 * 
 * @author ATISoluciones
 */
public class HistorialValoracionMerito extends Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Double valor;
	private Afinidad afinidad;
	private String comparaValoracion;
	
	
	/**
	 * Constructor por defecto.
	 */
	public HistorialValoracionMerito() {

	}

	/** Constructor con parametros .
	 * @param phistorial .
	 * @param pvalor .
	 * @param pafinidad .
	 * @param pcomparaValoracion .
	 */
	public HistorialValoracionMerito(Historial phistorial, Double pvalor, Afinidad pafinidad, String pcomparaValoracion) {
		super(phistorial);
		this.valor = pvalor;
		this.afinidad = pafinidad;
		this.comparaValoracion = pcomparaValoracion;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Valoración Mérito a copiar
	 */
	public HistorialValoracionMerito(HistorialValoracionMerito copia) {
		super(copia);
		this.valor = copia.valor;
		this.afinidad = copia.afinidad;
		this.comparaValoracion = copia.comparaValoracion;
	}
	
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public String getComparaValoracion() {
		return comparaValoracion;
	}
	
	public void setComparaValoracion(String comparaValoracion) {
		this.comparaValoracion = comparaValoracion;
	}
	
	public Afinidad getAfinidad() {
		return afinidad;
	}
	
	public void setAfinidad(Afinidad afinidad) {
		this.afinidad = afinidad;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Historial Valoración Mérito [historial=" + super.toString() + ", valor=" + valor + ", afinidad=" + afinidad
				+ ", comparaValoracion=" + comparaValoracion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((afinidad == null) ? 0 : afinidad.hashCode());
		result = prime * result + ((comparaValoracion == null) ? 0 : comparaValoracion.hashCode());
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
		HistorialValoracionMerito other = (HistorialValoracionMerito) obj;
		if (!super.equals(other)) {
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
		if (comparaValoracion == null) {
			if (other.comparaValoracion != null) {
				return false;
			}
		} else if (!comparaValoracion.equals(other.comparaValoracion)) {
			return false;
		}
		
		return true;
	}

}
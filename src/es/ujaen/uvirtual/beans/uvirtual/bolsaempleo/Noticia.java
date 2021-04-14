package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.ujaen.uvirtual.utilidades.Formateador;


/** Clase noticia de bolsa empleo.
 * @author jlopez
 *
 */
public class Noticia implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String enlace;
	private String texto;
	private Date fecha;
	private Boolean publica;
	private Boolean activa;
	
	
	/** Constructor por defecto.
	 */
	public Noticia() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param penlace .
	 * @param ptexto .
	 * @param pfecha .
	 * @param ppublica .
	 * @param pactiva .
	 */
	public Noticia(Integer pcodNum, String penlace, String ptexto, Date pfecha, Boolean ppublica, Boolean pactiva) {
		super();
		this.codNum = pcodNum;
		this.enlace = penlace;
		this.texto = ptexto;
		this.fecha = pfecha;
		this.publica = ppublica;
		this.activa = pactiva;
	}
	
	/** Constructor con parametros.
	 * @param penlace .
	 * @param ptexto .
	 * @param pfecha .
	 * @param ppublica .
	 * @param pactiva .
	 */
	public Noticia(String penlace, String ptexto, Date pfecha, Boolean ppublica, Boolean pactiva) {
		super();
		this.enlace = penlace;
		this.texto = ptexto;
		this.fecha = pfecha;
		this.publica = ppublica;
		this.activa = pactiva;
	}
	
	/** Constructor copia.
	 * @param copia Noticia a copiar
	 */
	public Noticia(Noticia copia) {
		this.codNum = copia.codNum;
		this.enlace = copia.enlace;
		this.texto = copia.texto;
		this.fecha = copia.fecha;
		this.publica = copia.publica;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha() {
		return fecha;
	}
	
	public String getFechaFormato() {
		return Formateador.formatoFecha(fecha, Formateador.FORMATO_FECHA_DDMMYYYY);
	}
	
	public String getFechaHoraFormato() {
		return Formateador.formatoFecha(fecha, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Boolean isPublica() {
		return publica;
	}

	public void setPublica(Boolean publica) {
		this.publica = publica;
	}
	
	public Boolean isActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	@Override
	public String toString() {
		return "Noticia [codNum=" + codNum + ", enlace=" + enlace
				+ ", texto=" + texto + ", fecha=" + fecha 
				+ ", publica=" + publica + ", activa=" + activa + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enlace == null) ? 0 : enlace.hashCode());
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((publica == null) ? 0 : publica.hashCode());
		result = prime * result + ((activa == null) ? 0 : activa.hashCode());
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
		Noticia other = (Noticia) obj;
		if (enlace == null) {
			if (other.enlace != null) {
				return false;
			}
		} else if (!enlace.equals(other.enlace)) {
			return false;
		}
		if (texto == null) {
			if (other.texto != null) {
				return false;
			}
		} else if (!texto.equals(other.texto)) {
			return false;
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fecha == null) {
			if (other.fecha != null) {
				return false;
			}
		} else if (formater.format(fecha).compareTo(formater.format(other.fecha)) != 0) {
			return false;
		}
		if (publica == null) {
			if (other.publica != null) {
				return false;
			}
		} else if (!publica.equals(other.publica)) {
			return false;
		}
		if (activa == null) {
			if (other.activa != null) {
				return false;
			}
		} else if (!activa.equals(other.activa)) {
			return false;
		}
		
		return true;
	}
	
}

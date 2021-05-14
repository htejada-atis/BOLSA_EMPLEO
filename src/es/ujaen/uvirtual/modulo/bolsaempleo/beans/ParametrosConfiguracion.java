package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase parametros de configuracion de bolsa empleo.
 * @author fcampos
 *
 */
public class ParametrosConfiguracion implements Serializable {
	private static final long serialVersionUID = 1L;
	private String configCodAlf;
	private String nombre;
	private String valor;
	private String descripcion;
	private Boolean adm;
	
	/** Constructor por defecto.
	 */
	public ParametrosConfiguracion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pconfigCodAlf .
	 * @param pnombre .
	 * @param pvalor .
	 * @param pdescripcion .
	 */
	public ParametrosConfiguracion(String pconfigCodAlf, String pnombre, String pvalor, String pdescripcion) {
		super();
		this.configCodAlf = pconfigCodAlf;
		this.nombre = pnombre;
		this.valor = pvalor;
		this.descripcion = pdescripcion;
	}
	
	/** Constructor copia.
	 * @param copia Noticia a copiar
	 */
	public ParametrosConfiguracion(ParametrosConfiguracion copia) {
		this.configCodAlf = copia.configCodAlf;
		this.nombre = copia.nombre;
		this.valor = copia.valor;
		this.descripcion = copia.descripcion;
	}
	
	public String getCodNum() {
		return configCodAlf;
	}

	public void setCodNum(String pconfigCodAlf) {
		this.configCodAlf = pconfigCodAlf;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Boolean getAdm() {
		return adm;
	}

	public void setAdm(Boolean adm) {
		this.adm = adm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "ParametrosConfiguracion [codNum=" + configCodAlf + ", nombre=" + nombre
				+ ", valor=" + valor + ", descripcion=" + descripcion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((configCodAlf == null) ? 0 : configCodAlf.hashCode());
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
		ParametrosConfiguracion other = (ParametrosConfiguracion) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
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
		
		return true;
	}
}

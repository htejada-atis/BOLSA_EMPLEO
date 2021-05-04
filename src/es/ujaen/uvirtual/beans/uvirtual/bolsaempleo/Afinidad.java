package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

/** Clase afinidades de bolsa empleo.
 * @author fcampos
 *
 */
public class Afinidad {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String descripcion;
	private Float modulacion;

	
	/** Constructor por defecto.
	 */
	public Afinidad() {
		super();
		this.codNum = null;
		this.codigo = "";
		this.descripcion = "";
		this.modulacion = null;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pcodigo .
	 * @param pdescripcion .
	 * @param pmodulacion .
	 */
	public Afinidad(Integer pcodNum, String pcodigo, String pdescripcion, Float pmodulacion) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
		this.modulacion = pmodulacion;
	}

	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Afinidad(Afinidad copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.descripcion = copia.descripcion;
		this.modulacion = copia.modulacion;
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

	public void setCodigo(String pcodigo) {
		this.codigo = pcodigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Float getModulacion() {
		return modulacion;
	}

	public void setModulacion(Float pmodulacion) {
		this.modulacion = pmodulacion;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Afinidad [codNum=" + codNum + ", codigo=" + codigo + ", descripcion=" + descripcion + ", modulacion="
				+ modulacion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((modulacion == null) ? 0 : modulacion.hashCode());
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
		Afinidad other = (Afinidad) obj;
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
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (modulacion == null) {
			if (other.modulacion != null) {
				return false;
			}
		} else if (!modulacion.equals(other.modulacion)) {
			return false;
		}
		
		return true;
	}
}

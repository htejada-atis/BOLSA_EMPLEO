package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;

/** Clase area de conocimiento de bolsaempleo.
 * @author atis
 */
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;	// CODNUM
	private Departamento departamento; // BEPDEP_CODNUM
	private String idAreaExterno; // ID_AREA_CONOCIMIENTO id externo del area de conocimento
	private String idSeccion; // ID_SECCION
	private String descripcion; // DES_AREA_CONOCIMIENTO
			
	/** Constructor por defecto.
	 */
	public Area() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pCodNum .
	 * @param pdepartamento .
	 * @param pidAreaExterno .
	 * @param pidSeccion .
	 * @param pdescripcion .
	 */
	public Area(Integer pCodNum, Departamento pdepartamento, String pidAreaExterno, String pidSeccion, String pdescripcion) {
		super();
		this.codNum = pCodNum;
		this.departamento = pdepartamento;
		this.idAreaExterno = pidAreaExterno;		
		this.idSeccion = pidSeccion;
		this.descripcion = pdescripcion;
	}
	
	/** Constructor con parametros.
	 * @param pCodNum .
	 */
	public Area(Integer pCodNum) {
		super();
		this.codNum = pCodNum;
	}
	
	/** Constructor copia.
	 * @param copia AreaConocimiento a copiar
	 */
	public Area(Area copia) {
		this.codNum = copia.codNum;
		this.departamento = copia.departamento;
		this.idAreaExterno = copia.idAreaExterno;		
		this.idSeccion = copia.idSeccion;
		this.descripcion = copia.descripcion;		
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	public String getIdAreaExterno() {
		return idAreaExterno;
	}
	
	public void setIdAreaExterno(String idAreaExterno) {
		this.idAreaExterno = idAreaExterno;
	}
	
	public String getIdSeccion() {
		return idSeccion;
	}
	
	public void setIdSeccion(String idSeccion) {
		this.idSeccion = idSeccion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return "Area Conocimiento [idArea=" + codNum + ", departamento=" + departamento
				+ ", idAreaExterno=" + idAreaExterno + ", idSeccion=" + idSeccion  
				+ ", descripcion=" + descripcion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departamento == null) ? 0 : departamento.hashCode());		
		result = prime * result + ((idAreaExterno == null) ? 0 : idAreaExterno.hashCode());
		result = prime * result + ((idSeccion == null) ? 0 : idSeccion.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
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
		Area other = (Area) obj;
		if (departamento == null) {
			if (other.departamento != null) {
				return false;
			}
		} else if (!departamento.equals(other.departamento)) {
			return false;
		}
		if (idAreaExterno == null) {
			if (other.idAreaExterno != null) {
				return false;
			}
		} else if (!idAreaExterno.equals(other.idAreaExterno)) {
			return false;
		}
		if (idSeccion == null) {
			if (other.idSeccion != null) {
				return false;
			}
		} else if (!idSeccion.equals(other.idSeccion)) {
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

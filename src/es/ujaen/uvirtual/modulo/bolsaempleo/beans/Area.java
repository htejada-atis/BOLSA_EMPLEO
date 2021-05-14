package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase area de conocimiento de bolsaempleo.
 * @author atis
 */
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;	// CODNUM
	private String idAreaExterno; // ID_AREA_CONOCIMIENTO id externo del area de conocimento	
	private String descripcion; // DES_AREA_CONOCIMIENTO
			
	/** Constructor por defecto.
	 */
	public Area() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pCodNum .
	 * @param pidAreaExterno .
	 * @param pdescripcion .
	 */
	public Area(Integer pCodNum, String pidAreaExterno, String pdescripcion) {
		super();
		this.codNum = pCodNum;
		this.idAreaExterno = pidAreaExterno;		
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
		this.idAreaExterno = copia.idAreaExterno;		
		this.descripcion = copia.descripcion;		
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public String getIdAreaExterno() {
		return idAreaExterno;
	}
	
	public void setIdAreaExterno(String idAreaExterno) {
		this.idAreaExterno = idAreaExterno;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return "Area Conocimiento [idArea=" + codNum + ", idAreaExterno=" + idAreaExterno + ", descripcion=" + descripcion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((idAreaExterno == null) ? 0 : idAreaExterno.hashCode());
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
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (idAreaExterno == null) {
			if (other.idAreaExterno != null) {
				return false;
			}
		} else if (!idAreaExterno.equals(other.idAreaExterno)) {
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

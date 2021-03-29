package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;

/** Clase departamento de una area de conocimiento.
 * @author atis
 */
public class Departamento implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;	// CODNUM
	private String idDepartamentoExterno; // ID_DEPARTAMENTO
	private String descripcion; // DES_DEPARTAMENTO
			
	/** Constructor por defecto.
	 */
	public Departamento() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pidDepartamentoExterno .
	 * @param pdescripcion .
	 */
	public Departamento(Integer pcodNum, String pidDepartamentoExterno, String pdescripcion) {
		super();
		this.codNum = pcodNum;
		this.idDepartamentoExterno = pidDepartamentoExterno;
		this.descripcion = pdescripcion;		
	}
	
	/** Constructor copia.
	 * @param copia AreaConocimiento a copiar
	 */
	public Departamento(Departamento copia) {
		this.codNum = copia.codNum;
		this.idDepartamentoExterno = copia.idDepartamentoExterno;
		this.descripcion = copia.descripcion;		
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public String getIdDepartamentoExterno() {
		return idDepartamentoExterno;
	}
	
	public void setIdDepartamentoExterno(String idDepartamentoExterno) {
		this.idDepartamentoExterno = idDepartamentoExterno;
	}
		
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return "Departamento [idDepartamento=" + codNum + ", idDepartamentoExterno=" + idDepartamentoExterno + " descripcion=" + descripcion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDepartamentoExterno == null) ? 0 : idDepartamentoExterno.hashCode());
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
		Departamento other = (Departamento) obj;
		if (idDepartamentoExterno == null) {
			if (other.idDepartamentoExterno != null) {
				return false;
			}
		} else if (!idDepartamentoExterno.equals(other.idDepartamentoExterno)) {
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

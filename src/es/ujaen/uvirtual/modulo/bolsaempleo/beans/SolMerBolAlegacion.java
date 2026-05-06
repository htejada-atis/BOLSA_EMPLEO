package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa la tabla TBEP_SOL_MER_ALEGACION.
 * 
 * @author ATISoluciones 2024
 */
public class SolMerBolAlegacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codNum;
	private Integer bepsbmCodNum;
	private Integer bepaleCodNum;
	private String descripcion;
	private String uidUsuario;
	private List<ArchivoAlegacionGeneral> archivosAlegacion;

	/** Constructor por defecto */
	public SolMerBolAlegacion() {
	}

	/**
	 * Constructor con parámetros
	 * 
	 * @param codNum       Identificador único de la alegación
	 * @param bepsbmCodNum Código del mérito de bolsa
	 * @param bepaleCodNum Código de la alegación
	 * @param descripcion  Descripción de la alegación
	 * @param uidUsuario   UID del usuario que realiza la alegación
	 */
	public SolMerBolAlegacion(Integer codNum, Integer bepsbmCodNum, Integer bepaleCodNum, String descripcion,
			String uidUsuario) {
		this.codNum = codNum;
		this.bepsbmCodNum = bepsbmCodNum;
		this.bepaleCodNum = bepaleCodNum;
		this.descripcion = descripcion;
		this.uidUsuario = uidUsuario;
	}

	// Getters y Setters
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public Integer getBepsbmCodNum() {
		return bepsbmCodNum;
	}

	public void setBepsbmCodNum(Integer bepsbmCodNum) {
		this.bepsbmCodNum = bepsbmCodNum;
	}

	public Integer getBepaleCodNum() {
		return bepaleCodNum;
	}

	public void setBepaleCodNum(Integer bepaleCodNum) {
		this.bepaleCodNum = bepaleCodNum;
	}

	public List<ArchivoAlegacionGeneral> getArchivosAlegacion() {
		return archivosAlegacion;
	}

	public void setArchivosAlegacion(List<ArchivoAlegacionGeneral> archivosAlegacion) {
		this.archivosAlegacion = archivosAlegacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUidUsuario() {
		return uidUsuario;
	}

	public void setUidUsuario(String uidUsuario) {
		this.uidUsuario = uidUsuario;
	}

	@Override
	public String toString() {
		return "SolMerAlegacion [codNum=" + codNum + ", bepsbmCodNum=" + bepsbmCodNum + ", bepaleCodNum=" + bepaleCodNum
				+ ", descripcion=" + descripcion + ", uidUsuario=" + uidUsuario + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((bepsbmCodNum == null) ? 0 : bepsbmCodNum.hashCode());
		result = prime * result + ((bepaleCodNum == null) ? 0 : bepaleCodNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((uidUsuario == null) ? 0 : uidUsuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SolMerBolAlegacion other = (SolMerBolAlegacion) obj;
		return (codNum != null && codNum.equals(other.codNum))
				&& (bepsbmCodNum != null && bepsbmCodNum.equals(other.bepsbmCodNum))
				&& (bepaleCodNum != null && bepaleCodNum.equals(other.bepaleCodNum))
				&& (descripcion != null && descripcion.equals(other.descripcion))
				&& (uidUsuario != null && uidUsuario.equals(other.uidUsuario));
	}
}
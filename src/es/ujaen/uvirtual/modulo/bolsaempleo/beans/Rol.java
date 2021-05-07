package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase rol de UVIRTUAL.
 * @author ATISoluciones 2021
 *
 */
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private String valor;

	/** Constructor por defecto.
	 */
	public Rol() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pdescripcion .
	 */
	public Rol(Integer pcodNum, String pdescripcion) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public Rol(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

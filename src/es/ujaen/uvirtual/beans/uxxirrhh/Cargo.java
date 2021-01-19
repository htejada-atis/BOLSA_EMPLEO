package es.ujaen.uvirtual.beans.uxxirrhh;

import java.util.Date;

import es.ujaen.uvirtual.beans.CodigoAlfDescripcion;

/**
 * Clase que representa el concepto cargo de UXXIRRHH.
 * @author julopez
 *     20130918 - julopez - le añade los parámetros extra para mostrar los cargos en el expediente administrativo
 */
public class Cargo extends CodigoAlfDescripcion {
	

	private static final long serialVersionUID = 5847759221210871775L;

	private Date desde = null;
	
	private Date hasta = null;
	
	
	
	/** Constructor con parametros.
	 * @param codigo codigo
	 * @param descripcion descripcion
	 */
	public Cargo(String codigo, String descripcion) {
		super(codigo, descripcion);
	}
	
	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	@Override
	public String toString() {
		return this.codigo + " - " + this.descripcion;
	}
}

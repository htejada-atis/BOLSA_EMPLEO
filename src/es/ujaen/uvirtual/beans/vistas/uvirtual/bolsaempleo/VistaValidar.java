package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaValidar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private Convocatoria conv;
	private BolsaEmpleoDataTable<BolsaValidacion> datatable;
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public void setDatatable(BolsaEmpleoDataTable<BolsaValidacion> dataTable2) {
		this.datatable = dataTable2;
	}
	
	public BolsaEmpleoDataTable<BolsaValidacion> getDatatable() {
		return this.datatable;	
	}
	
	public Convocatoria getConvocatoria() {
		return conv;
	}
	
	public void setConvocatoria(Convocatoria pconv) {
		this.conv = pconv;
	}
}

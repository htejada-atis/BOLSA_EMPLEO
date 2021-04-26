package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Afinidad;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaAfinidades extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Afinidad> dataTable;
	private Afinidad afinidad;
	private String vista;
	
	
	public BolsaEmpleoDataTable<Afinidad> getDatatableAfinidades() {
		return dataTable;
	}
	
	public void setDatatableAfinidades(BolsaEmpleoDataTable<Afinidad> dt) {
		this.dataTable = dt;
	}
	
	public Afinidad getAfinidad() {
		return afinidad;
	}
	
	public void setAfinidad(Afinidad afinidad) {
		this.afinidad = afinidad;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}

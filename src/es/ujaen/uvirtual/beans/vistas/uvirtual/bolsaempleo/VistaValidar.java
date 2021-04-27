package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaValidar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private List<Area> areas = new ArrayList<>();
	private Convocatoria conv;
	private BolsaEmpleoDataTable<Bolsa> datatable;
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public void setDatatable(BolsaEmpleoDataTable<Bolsa> dt) {
		this.datatable = dt;
	}
	
	public BolsaEmpleoDataTable<Bolsa> getDatatable() {
		return this.datatable;	
	}
	
	public Convocatoria getConvocatoria() {
		return conv;
	}
	
	public void setConvocatoria(Convocatoria pconv) {
		this.conv = pconv;
	}
}

package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaConvocatorias extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private DataTable<Convocatoria> dataTable;
	private Convocatoria convocatoria;
			
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public DataTable<Convocatoria> getDatatableConvocatorias() {
		return dataTable;
	}
	
	public void setDatatableConvocatorias(DataTable<Convocatoria> dt) {
		this.dataTable = dt;
	}
	
	public Convocatoria getConvocatoria() {
		return convocatoria;
	}
	
	public void setConvocatoria(Convocatoria pconvocatoria) {
		this.convocatoria = pconvocatoria;
	}
}

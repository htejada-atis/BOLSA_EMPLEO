package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaSolicitudes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<Solicitud> dataTable;
	private Solicitud solicitud;
			
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public BolsaEmpleoDataTable<Solicitud> getDatatableSolicitudes() {
		return dataTable;
	}
	
	public void setDatatableSolicitudes(BolsaEmpleoDataTable<Solicitud> dt) {
		this.dataTable = dt;
	}
	
	public Solicitud getSolicitud() {
		return solicitud;
	}
	
	public void setSolicitud(Solicitud psolicitud) {
		this.solicitud = psolicitud;
	}
}

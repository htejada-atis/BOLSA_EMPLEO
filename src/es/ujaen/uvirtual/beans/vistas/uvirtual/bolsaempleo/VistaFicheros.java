package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaFicheros extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataTable<Fichero> dataTable;
	private Fichero fichero;
	private String vista;
	
	public DataTable<Fichero> getDatatableFicheros() {
		return dataTable;
	}
	
	public void setDatatableFicheros(DataTable<Fichero> dt) {
		this.dataTable = dt;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public Fichero getFichero() {
		return fichero;
	}
	
	public void setFichero(Fichero fichero) {
		this.fichero = fichero;
	}
	
}

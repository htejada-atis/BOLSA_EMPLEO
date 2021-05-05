package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaFicheros extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Fichero> dataTable;
	private Fichero fichero;
	private String vista;
	
	public BolsaEmpleoDataTable<Fichero> getDatatableFicheros() {
		return dataTable;
	}
	
	public void setDatatableFicheros(BolsaEmpleoDataTable<Fichero> dt) {
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

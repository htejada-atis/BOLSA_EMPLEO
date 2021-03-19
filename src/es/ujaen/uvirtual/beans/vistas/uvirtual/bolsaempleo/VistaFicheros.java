package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaFicheros extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> ficheros = new ArrayList<>();
	private String fichero;
	private String vista;
	
	public List<String> getFicheros() {
		return ficheros;
	}
	
	public void setFicheros(List<String> ficheros) {
		this.ficheros = ficheros;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public String getFichero() {
		return fichero;
	}
	
	public void setFichero(String fichero) {
		this.fichero = fichero;
	}
	
}

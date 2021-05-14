package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaResultados extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
}

package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones.
 */
public class VistaConfiguracion extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}

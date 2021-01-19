package es.ujaen.uvirtual.beans.vistas.uvirtual.docentia;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author jmoral
 */
public class VistaConvocatoriaCRUD extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Convocatoria> convocatorias;
	private Convocatoria convocatoria;
	private String vista;
	
	public List<Convocatoria> getConvocatorias() {
		return convocatorias;
	}
	
	public void setConvocatorias(List<Convocatoria> convocatorias) {
		this.convocatorias = convocatorias;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public Convocatoria getConvocatoria() {
		return convocatoria;
	}
	
	public void setConvocatoria(Convocatoria convocatoria) {
		this.convocatoria = convocatoria;
	}
	
}

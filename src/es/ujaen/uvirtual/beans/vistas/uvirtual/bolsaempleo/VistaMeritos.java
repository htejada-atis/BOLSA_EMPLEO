package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author atis
 */
public class VistaMeritos extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private DataTable<?> dataTable;
    private Merito merito;
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public DataTable<?> getDatatable() {
		return dataTable;
	}
	
	public void setDatatable(DataTable<?> dt) {
		this.dataTable = dt;
	}
	
	public Merito getMerito() {
		return this.merito;
	}
	
	public void setMerito(Merito merito) {
		this.merito = merito;
	}
	
}

package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author atis
 */
public class VistaEstadoBolsas extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private DataTable<Bolsa> dataTable;
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public DataTable<Bolsa> getDatatableBolsas() {
    	return dataTable;
    }
    
    public void setDatatableBolsas(DataTable<Bolsa> dt) {
    	this.dataTable = dt;    	
    }
}

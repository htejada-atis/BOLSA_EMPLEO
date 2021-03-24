package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author atis
 */
public class VistaAreasBaremar extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }    
}

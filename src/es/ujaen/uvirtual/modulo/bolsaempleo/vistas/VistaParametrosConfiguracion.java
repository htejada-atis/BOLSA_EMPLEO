package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaParametrosConfiguracion extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<ParametrosConfiguracion> dataTable;
	private List<ParametrosConfiguracion> parametros = new ArrayList<>();
	private ParametrosConfiguracion parametro;
	private String vista;
	
	public BolsaEmpleoDataTable<ParametrosConfiguracion> getDatatableNoticias() {
		return dataTable;
	}
	
	public void setDatatableNoticias(BolsaEmpleoDataTable<ParametrosConfiguracion> dt) {
		this.dataTable = dt;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public ParametrosConfiguracion getParametro() {
		return parametro;
	}
	
	public void setParametro(ParametrosConfiguracion parametro) {
		this.parametro = parametro;
	}
	
	 public List<ParametrosConfiguracion> getParametros() {
		 return parametros;
	 }
	 
	 public void setParametros(List<ParametrosConfiguracion> pparametros) {
		 this.parametros = pparametros;
	 }
}

package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaTitulaciones extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Titulacion> dataTable;
	private BolsaEmpleoDataTable<TitulacionUsuario> dataTableUsuario;
	private Titulacion titulacion;
	private TitulacionUsuario titulacionUsuario;
	private String vista;
	
	public BolsaEmpleoDataTable<Titulacion> getDatatableTitulaciones() {
		return dataTable;
	}
	
	public void setDatatableTitulaciones(BolsaEmpleoDataTable<Titulacion> dt) {
		this.dataTable = dt;
	}
	
	public BolsaEmpleoDataTable<TitulacionUsuario> getDatatableTitulacionesUsuario() {
		return dataTableUsuario;
	}
	
	public void setDatatableTitulacionesUsuario(BolsaEmpleoDataTable<TitulacionUsuario> dt) {
		this.dataTableUsuario = dt;
	}
	
	public Titulacion getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	public TitulacionUsuario getTitulacionUsuario() {
		return titulacionUsuario;
	}
	
	public void setTitulacionUsuario(TitulacionUsuario titulacionUsuario) {
		this.titulacionUsuario = titulacionUsuario;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}

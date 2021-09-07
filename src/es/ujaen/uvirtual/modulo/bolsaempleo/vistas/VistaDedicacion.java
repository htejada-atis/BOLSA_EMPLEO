package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaDedicacion extends Vista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Dedicacion> datatableDedicaciones;
	private Dedicacion dedicacion;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo pusuario) {
		this.usuarioLogeado = pusuario;
	}
	
	public BolsaEmpleoDataTable<Dedicacion> getDatatableDedicaciones() {
		return datatableDedicaciones;
	}
	
	public void setDatatableDedicaciones(BolsaEmpleoDataTable<Dedicacion> datatable) {
		this.datatableDedicaciones = datatable;
	}
	
	public Dedicacion getDedicacion() {
		return dedicacion;
	}
	
	public void setDedicacion(Dedicacion dedicacion) {
		this.dedicacion = dedicacion;
	}
	
}

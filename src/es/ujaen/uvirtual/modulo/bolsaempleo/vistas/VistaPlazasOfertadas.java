package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaPlazasOfertadas extends Vista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<PlazaOfertada> datatablePlazasOfertadas;
	private PlazaOfertada plazaOfertada;
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
	
	public BolsaEmpleoDataTable<PlazaOfertada> getDatatablePlazasOfertadas() {
		return datatablePlazasOfertadas;
	}
	
	public void setDatatablePlazasOfertadas(BolsaEmpleoDataTable<PlazaOfertada> datatablePlazasOfertadas) {
		this.datatablePlazasOfertadas = datatablePlazasOfertadas;
	}
	
	public PlazaOfertada getPlazaOfertada() {
		return plazaOfertada;
	}
	
	public void setPlazaOfertada(PlazaOfertada plazaOfertada) {
		this.plazaOfertada = plazaOfertada;
	}
	
}

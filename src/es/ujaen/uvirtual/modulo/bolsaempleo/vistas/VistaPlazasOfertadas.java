package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
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
	private transient BolsaEmpleoDataTable<OfertaCandidato> datatablePlazasOfertadas;
	private OfertaCandidato ofertaCandidato;
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
	
	public BolsaEmpleoDataTable<OfertaCandidato> getDatatablePlazasOfertadas() {
		return datatablePlazasOfertadas;
	}
	
	public void setDatatablePlazasOfertadas(BolsaEmpleoDataTable<OfertaCandidato> datatablePlazasOfertadas) {
		this.datatablePlazasOfertadas = datatablePlazasOfertadas;
	}
	
	public OfertaCandidato getOfertaCandidato() {
		return ofertaCandidato;
	}
	
	public void setOfertaCandidato(OfertaCandidato ofertaCandidato) {
		this.ofertaCandidato = ofertaCandidato;
	}
	
}

package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoEstado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaContratacion extends Vista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<Area> listaAreas = new ArrayList<>();
	private List<Dedicacion> listaDedicaciones = new ArrayList<>();
	private List<OfertaCandidato> listaOfertas = new ArrayList<>();
	private List<String> cursos = new ArrayList<>();
	private List<CandidatoEstado> candidatosEstado = new ArrayList<>(); 
	private transient BolsaEmpleoDataTable<PlazaOfertada> datatablePlazasOfertadas;
	private transient BolsaEmpleoDataTable<OfertaCandidato> datatableCandidatos;
	private transient BolsaEmpleoDataTable<CandidatoEstado> datatableCandidatosEstado;
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
	
	public List<Area> getListaAreas() {
		return listaAreas;
	}
	
	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	
	public List<Dedicacion> getListaDedicaciones() {
		return listaDedicaciones;
	}
	
	public void setListaDedicaciones(List<Dedicacion> listaDedicaciones) {
		this.listaDedicaciones = listaDedicaciones;
	}
	
	public List<String> getCursos() {
		return cursos;
	}

	public void setCursos(List<String> cursos) {
		this.cursos = cursos;
	}
	
	public BolsaEmpleoDataTable<OfertaCandidato> getDatatableCandidatos() {
		return datatableCandidatos;
	}
	
	public void setDatatableCandidatos(BolsaEmpleoDataTable<OfertaCandidato> datatableCandidatos) {
		this.datatableCandidatos = datatableCandidatos;
	}

	public List<OfertaCandidato> getListaOfertas() {
		return listaOfertas;
	}

	public void setListaOfertas(List<OfertaCandidato> listaOfertas) {
		this.listaOfertas = listaOfertas;
	}

	public List<CandidatoEstado> getCandidatosEstado() {
		return candidatosEstado;
	}
	
	public void setCandidatosEstado(List<CandidatoEstado> candidatosEstado) {
		this.candidatosEstado = candidatosEstado;
	}
	
	public BolsaEmpleoDataTable<CandidatoEstado> getDatatableCandidatosEstado() {
		return datatableCandidatosEstado;
	}

	public void setDatatableCandidatosEstado(BolsaEmpleoDataTable<CandidatoEstado> datatableCandidatosEstado) {
		this.datatableCandidatosEstado = datatableCandidatosEstado;
	}
	
}
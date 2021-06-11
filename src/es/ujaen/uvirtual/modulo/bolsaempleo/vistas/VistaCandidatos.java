package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaCandidatos extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private UsuarioBolsaEmpleo candidato;
	private Rol role;
	private List<Rol> roles = new ArrayList<>();
	private List<Area> listaAreas = new ArrayList<>();
	private transient BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTableCandidatos;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private transient BolsaEmpleoDataTable<Solicitud> dataTableSolicitudes;
	private Boolean apartadoAreasExcluidas;
	private Boolean apartadoSolicitudes;
	private Boolean apartadoComunicaciones;
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

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public Rol getRol() {
		return role;
	}

	public void setRol(Rol drole) {
		this.role = drole;
	}

	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}

	public void setCandidato(UsuarioBolsaEmpleo usuario) {
		this.candidato = usuario;
	}

	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> getDatatableCandidatos() {
		return dataTableCandidatos;
	}

	public void setDatatableCandidatos(BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt) {
		this.dataTableCandidatos = dt;
	}

	public void setDatatableAreas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreas = dt;
	}

	public BolsaEmpleoDataTable<Bolsa> getDatatableAreas() {
		return dataTableAreas;
	}

	public void setApartadoAreasExcluidas(Boolean apartadoAreasExcluidas) {
		this.apartadoAreasExcluidas = apartadoAreasExcluidas;
	}

	public Boolean getApartadoAreasExcluidas() {
		return apartadoAreasExcluidas;
	}

	public void setApartadoSolicitudes(Boolean apartadoSolicitudes) {
		this.apartadoSolicitudes = apartadoSolicitudes;
	}

	public Boolean getApartadoSolicitudes() {
		return apartadoSolicitudes;
	}

	public void setApartadoComunicaciones(Boolean apartadoComunicaciones) {
		this.apartadoComunicaciones = apartadoComunicaciones;
	}

	public Boolean getApartadoComunicaciones() {
		return apartadoComunicaciones;
	}

	public List<Area> getListaAreas() {
		return listaAreas;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}

	public BolsaEmpleoDataTable<Solicitud> getDataTableSolicitudes() {
		return dataTableSolicitudes;
	}

	public void setDataTableSolicitudes(BolsaEmpleoDataTable<Solicitud> dataTableSolicitudes) {
		this.dataTableSolicitudes = dataTableSolicitudes;
	}
}

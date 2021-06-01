package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaUsuarioBolsaEmpleo extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
	private UsuarioBolsaEmpleo usuario;
	private Usuario usuarioArcos;
	private Rol role;
	private List<Rol> roles = new ArrayList<>();
	private Boolean busqueda;
	private BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable;
	private BolsaEmpleoDataTable<Bolsa> dataTableBolsa;
	private Boolean apartadoAreasExcluidas;
	private Boolean apartadoSolicitudes;
	private Boolean apartadoComunicaciones;

	public List<UsuarioBolsaEmpleo> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioBolsaEmpleo> pusuarios) {
		this.usuarios = pusuarios;
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

	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioArcos() {
		return usuarioArcos;
	}

	public void setUsuarioArcos(Usuario pusuario) {
		this.usuarioArcos = pusuario;
	}

	public void setBusqueda(Boolean busqueda) {
		this.busqueda = busqueda;
	}

	public Boolean getBusqueda() {
		return busqueda;
	}

	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> getDatatable() {
		return dataTable;
	}

	public void setDatatable(BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt) {
		this.dataTable = dt;
	}

	public void setDatatableBolsa(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableBolsa = dt;
	}

	public BolsaEmpleoDataTable<Bolsa> getDatatableBolsa() {
		return dataTableBolsa;
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
}

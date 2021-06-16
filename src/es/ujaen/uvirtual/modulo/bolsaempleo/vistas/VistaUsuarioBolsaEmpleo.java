package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaUsuarioBolsaEmpleo extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private UsuarioBolsaEmpleo usuario;
	private Usuario usuarioArcos;
	private Rol role;
	private List<Rol> roles = new ArrayList<>();
	private Boolean busqueda;
	private transient BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTableUsuarios;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableAreasEvaluables;
	private Boolean apartadoAreasEvaluables;
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

	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> getDatatableUsuarios() {
		return dataTableUsuarios;
	}

	public void setDatatableUsuarios(BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt) {
		this.dataTableUsuarios = dt;
	}

	public void setDatatableAreasEvaluables(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreasEvaluables = dt;
	}

	public BolsaEmpleoDataTable<Bolsa> getDatatableAreasEvaluables() {
		return dataTableAreasEvaluables;
	}

	public void setApartadoAreasEvaluables(Boolean apartadoAreasEvaluables) {
		this.apartadoAreasEvaluables = apartadoAreasEvaluables;
	}

	public Boolean getApartadoAreasEvaluables() {
		return apartadoAreasEvaluables;
	}

}

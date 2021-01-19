package es.ujaen.uvirtual.beans.vistas;

import java.util.ArrayList;
import java.util.HashMap;

import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.Usuario;

/** vista de universidad virtual.
 * @author julopez
 *
 */
@SuppressWarnings("java:S1319")
public class VistaUVirtual extends Vista {
	private static final long serialVersionUID = -4881076533210316567L;
	
	private HashMap<String, String> idiomas = null;
	private Usuario usuario = null;
	private String identificadorUsuario = null;
	private String identificadorUsuarioAutenticado = null;
	private ArrayList<Menu> menuPrincipal = null;
	private ArrayList<Menu> migaDePan = null;
	private ArrayList<Menu> menusHijos = null;
	private ArrayList<Menu> menusMismoNivel = null;
	private Menu menu = null;
	private String paginaInicio = null;
	
	private int avisosSinLeer = 0;

	public HashMap<String, String> getIdiomas() {
		return idiomas;
	}

	public void setIdiomas(HashMap<String, String> idiomas) {
		this.idiomas = idiomas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getIdentificadorUsuario() {
		return identificadorUsuario;
	}

	public void setIdentificadorUsuario(String identificadorUsuario) {
		this.identificadorUsuario = identificadorUsuario;
	}

	public String getIdentificadorUsuarioAutenticado() {
		return identificadorUsuarioAutenticado;
	}

	public void setIdentificadorUsuarioAutenticado(String identificadorUsuarioAutenticado) {
		this.identificadorUsuarioAutenticado = identificadorUsuarioAutenticado;
	}

	public ArrayList<Menu> getMenuPrincipal() {
		return menuPrincipal;
	}

	public void setMenuPrincipal(ArrayList<Menu> menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
	}

	public ArrayList<Menu> getMigaDePan() {
		return migaDePan;
	}

	public void setMigaDePan(ArrayList<Menu> migaDePan) {
		this.migaDePan = migaDePan;
	}

	public ArrayList<Menu> getMenusHijos() {
		return menusHijos;
	}

	public void setMenusHijos(ArrayList<Menu> menusHijos) {
		this.menusHijos = menusHijos;
	}

	public ArrayList<Menu> getMenusMismoNivel() {
		return menusMismoNivel;
	}

	public void setMenusMismoNivel(ArrayList<Menu> menusMismoNivel) {
		this.menusMismoNivel = menusMismoNivel;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getPaginaInicio() {
		return paginaInicio;
	}

	public void setPaginaInicio(String paginaInicio) {
		this.paginaInicio = paginaInicio;
	}

	public int getAvisosSinLeer() {
		return avisosSinLeer;
	}

	public void setAvisosSinLeer(int avisosSinLeer) {
		this.avisosSinLeer = avisosSinLeer;
	}
}
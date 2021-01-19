package es.ujaen.uvirtual.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaBean que implementa el elemento menú, con accesos, idiomas, roles de usuarios y administradores.
 * 
 * @author julopez
 */
@SuppressWarnings("java:S1319")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1741741354231340794L;
	protected int codigo = -1;
	protected String controlador = null;
	protected boolean disponible = false;
	protected boolean mostrar = false;
	protected boolean crearMenu = false;
	protected boolean pdfInterno = false;
	protected boolean excel = false;
	protected boolean word = false;
	protected boolean accesoAnonimo = false;
	protected boolean registrarLog = true;
	protected int codigoPadre = -1;
	protected Menu menuPadre = null;
	protected String direccion = null;
	protected String telefono = null;
	protected String fax = null;
	protected String email = null;
	
	private ArrayList<MenuRol> rolesAcceso = null;
	private ArrayList<MenuRol> rolesAdministrador = null;
	private HashMap<String, String> idiomas = null;
	
	private ArrayList<MenuSubred> accesoSubredes = null;
	
	private ArrayList<String> sistemas = null;
	
	/**
	 * Constructor por defecto. 
	 */
	public Menu() {
		super();
	}
	
	/**
	 * Constructor indicando sólo el controlador.
	 * @param pcontrolador nombre del controlador
	 */
	public Menu(String pcontrolador) {
		super();
		this.controlador = pcontrolador;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getControlador() {
		return controlador;
	}

	public void setControlador(String controlador) {
		this.controlador = controlador;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public boolean isMostrar() {
		return mostrar;
	}
	
	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}
	
	public boolean isCrearMenu() {
		return crearMenu;
	}
	
	public void setCrearMenu(boolean crearMenu) {
		this.crearMenu = crearMenu;
	}
	
	public int getCodigoPadre() {
		return codigoPadre;
	}
	
	public void setCodigoPadre(int codigoPadre) {
		this.codigoPadre = codigoPadre;
	}
	
	public Menu getAccionPadre() {
		return menuPadre;
	}
	
	public void setAccionPadre(Menu pmenuPadre) {
		this.menuPadre = pmenuPadre;
	}
	
	public ArrayList<MenuRol> getRolesAcceso() {
		return rolesAcceso;
	}
	
	public void setRolesAcceso(ArrayList<MenuRol> rolesAcceso) {
		this.rolesAcceso = rolesAcceso;
	}
	
	public ArrayList<MenuRol> getRolesAdministrador() {
		return rolesAdministrador;
	}
	
	public void setRolesAdministrador(ArrayList<MenuRol> rolesAdministrador) {
		this.rolesAdministrador = rolesAdministrador;
	}
	
	public HashMap<String, String> getIdiomas() {
		return idiomas;
	}
	
	public void setIdiomas(HashMap<String, String> idiomas) {
		this.idiomas = idiomas;
	}
	
	public ArrayList<MenuSubred> getAccesoSubredes() {
		return accesoSubredes;
	}
	
	public void setAccesoSubredes(ArrayList<MenuSubred> accesoSubredes) {
		this.accesoSubredes = accesoSubredes;
	}

	public boolean isPdfInterno() {
		return pdfInterno;
	}

	public void setPdfInterno(boolean pdfInterno) {
		this.pdfInterno = pdfInterno;
	}

	public boolean isAccesoAnonimo() {
		return accesoAnonimo;
	}

	public void setAccesoAnonimo(boolean accesoAnonimo) {
		this.accesoAnonimo = accesoAnonimo;
	}

	public Menu getMenuPadre() {
		return menuPadre;
	}

	public void setMenuPadre(Menu menuPadre) {
		this.menuPadre = menuPadre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

	public boolean isWord() {
		return word;
	}

	public void setWord(boolean word) {
		this.word = word;
	}

	public ArrayList<String> getSistemas() {
		return sistemas;
	}

	public void setSistemas(ArrayList<String> sistemas) {
		this.sistemas = sistemas;
	}

	public boolean isRegistrarLog() {
		return registrarLog;
	}

	public void setRegistrarLog(boolean registrarLog) {
		this.registrarLog = registrarLog;
	}
	
	@Override
	@SuppressWarnings("checkstyle:ExecutableStatementCount")
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("\n\n");
		buffer.append("Código .........: ").append(this.codigo).append("\n");
		buffer.append("Controlador ....: ").append(this.controlador).append("\n");
		buffer.append("Cód. Padre .....: ").append(this.codigoPadre).append("\n");
		buffer.append("Dirección ......: ").append(this.direccion).append("\n");
		buffer.append("Teléfono .......: ").append(this.telefono).append("\n");
		buffer.append("Fax ............: ").append(this.fax).append("\n");
		buffer.append("Correo electr. .: ").append(this.email).append("\n");
		buffer.append("Disponible .....: ").append(this.disponible).append("\n");
		buffer.append("Mostrar ........: ").append(this.mostrar).append("\n");
		buffer.append("Crear Menú .....: ").append(this.crearMenu).append("\n");
		buffer.append("PDF Interno ....: ").append(this.pdfInterno).append("\n");
		buffer.append("Exporta Excel ..: ").append(this.excel).append("\n");
		buffer.append("Exporta Word ...: ").append(this.word).append("\n");
		buffer.append("Acceso anónimo .: ").append(this.accesoAnonimo).append("\n");
		buffer.append("Registrar Log ..: ").append(this.registrarLog).append("\n");
		
		if (rolesAcceso != null) {
			buffer.append("\n\n");
			buffer.append("ROLES CON ACCESO").append("\n");
			buffer.append("================").append("\n");
			for (MenuRol rol : rolesAcceso) {
				buffer.append(rol.valorRol).append(" - ").append(rol.descripcionRol).append(" - Desactivado: ").append(rol.desactivado).append("\n");
			}
		}

		if (rolesAdministrador != null) {
			buffer.append("\n\n");
			buffer.append("ROLES CON ACCESO DE ADMINISTRADOR").append("\n");
			buffer.append("=================================").append("\n");
			for (MenuRol rol : rolesAdministrador) {
				buffer.append(rol.valorRol).append(" - ").append(rol.descripcionRol).append(" - Desactivado: ").append(rol.desactivado).append("\n");
			}
		}
		
		buffer.append("\n\n");
		buffer.append("NOMBRE DEL MENU").append("\n");
		buffer.append("===============").append("\n");
		for (Map.Entry<String, String> idioma : idiomas.entrySet()) {
			buffer.append(idioma.getKey()).append(" : ").append(idioma.getValue()).append("\n");
		}

		if (accesoSubredes != null) {
			buffer.append("\n\n");
			buffer.append("SUBREDES CON ACCESO").append("\n");
			buffer.append("===================").append("\n");
			for (MenuSubred subred : accesoSubredes) {
				buffer.append(subred.red).append(" (").append(subred.descripcion).append(" ) - Desactivado: ").append(subred.desactivado).append("\n");
			}
		}
		
		if (sistemas != null) {
			buffer.append("\n\n");
			buffer.append("SISTEMAS DE LOS QUE DEPENDE").append("\n");
			buffer.append("===========================").append("\n");
			for (String sistema : sistemas) {
				buffer.append(sistema).append("\n");
			}
		}
		
		return buffer.toString();
	}
}
package es.ujaen.uvirtual.beans;

import java.io.Serializable;

/**
 * Bean de asociación entre menú y rol. 
 * @author julopez
 *
 */
public class MenuRol implements Serializable {
	private static final long serialVersionUID = -1636231096111394770L;
	protected int codigoMenu = -1;
	protected int codigoRol = -1;
	protected boolean desactivado = false;
	protected boolean administrador = false;
	protected String descripcionRol = null;
	protected String valorRol = null;
	
		
	/** constructor por defecto.
	 * 
	 */
	public MenuRol() {
		super();
	}

	/** Constructor con parametros.
	 * @param pcodigoMenu codigo menu
	 * @param pcodigoRol codigo rol
	 * @param pdesactivado desactivado
	 * @param padministrador administrador
	 * @param pdescripcionRol descripcion rol
	 * @param pvalorRol valor rol
	 */
	public MenuRol(int pcodigoMenu, int pcodigoRol, boolean pdesactivado,
			boolean padministrador, String pdescripcionRol, String pvalorRol) {
		this.codigoMenu = pcodigoMenu;
		this.codigoRol = pcodigoRol;
		this.desactivado = pdesactivado;
		this.administrador = padministrador;
		this.descripcionRol = pdescripcionRol;
		this.valorRol = pvalorRol;
	}

	public int getCodigoMenu() {
		return codigoMenu;
	}
	
	public void setCodigoMenu(int codigoMenu) {
		this.codigoMenu = codigoMenu;
	}
	
	public int getCodigoRol() {
		return codigoRol;
	}
	
	public void setCodigoRol(int codigoRol) {
		this.codigoRol = codigoRol;
	}
	
	public boolean isDesactivado() {
		return desactivado;
	}
	
	public void setDesactivado(boolean desactivado) {
		this.desactivado = desactivado;
	}
	
	public boolean isAdministrador() {
		return administrador;
	}
	
	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}
	
	public String getDescripcionRol() {
		return descripcionRol;
	}
	
	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}
	
	public String getValorRol() {
		return valorRol;
	}
	
	public void setValorRol(String valorRol) {
		this.valorRol = valorRol;
	}
}
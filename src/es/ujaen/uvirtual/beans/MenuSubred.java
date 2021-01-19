package es.ujaen.uvirtual.beans;

import java.io.Serializable;

/**
 * subredes con acceso al menú.
 * @author julopez
 *
 */
public class MenuSubred implements Serializable {
	private static final long serialVersionUID = 3193933746912078307L;
	protected int codigoMenu = -1;
	protected boolean desactivado = false;
	protected String descripcion = null;
	protected String red = null;
	
	
	
	/** constructor con parametros.
	 * @param pcodigoMenu codigo menu
	 * @param pdesactivado desactivado
	 * @param pdescripcion descripcion
	 * @param pred red
	 */
	public MenuSubred(int pcodigoMenu, boolean pdesactivado, String pdescripcion,
			String pred) {
		super();
		this.codigoMenu = pcodigoMenu;
		this.desactivado = pdesactivado;
		this.descripcion = pdescripcion;
		this.red = pred;
	}
	
	/** constructor por defecto.
	 * 
	 */
	public MenuSubred() {
		super();
	}

	public int getCodigoMenu() {
		return codigoMenu;
	}
	
	public void setCodigoMenu(int codigoMenu) {
		this.codigoMenu = codigoMenu;
	}
	
	public boolean isDesactivado() {
		return desactivado;
	}
	
	public void setDesactivado(boolean desactivado) {
		this.desactivado = desactivado;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}
}
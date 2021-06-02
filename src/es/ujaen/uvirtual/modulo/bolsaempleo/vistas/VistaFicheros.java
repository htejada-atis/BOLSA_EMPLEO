package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaFicheros extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Fichero> dataTable;
	private Fichero fichero;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public BolsaEmpleoDataTable<Fichero> getDatatableFicheros() {
		return dataTable;
	}

	public void setDatatableFicheros(BolsaEmpleoDataTable<Fichero> dt) {
		this.dataTable = dt;
	}

	public Fichero getFichero() {
		return fichero;
	}

	public void setFichero(Fichero fichero) {
		this.fichero = fichero;
	}
	
	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}
}

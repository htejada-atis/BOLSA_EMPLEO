package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021.
 */
public class VistaMensajes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Mensaje> dataTable;
	private BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTableDestinatarios;
	private Mensaje mensaje;
	private String vista;
	private List<Convocatoria> convocatorias;
	private List<Area> areas;

	public BolsaEmpleoDataTable<Mensaje> getDatatableMensajes() {
		return dataTable;
	}

	public void setDatatableMensajes(BolsaEmpleoDataTable<Mensaje> dt) {
		this.dataTable = dt;
	}
	
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> getDatatableDestinatarios() {
		return dataTableDestinatarios;
	}

	public void setDatatableDestinatarios(BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt) {
		this.dataTableDestinatarios = dt;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public List<Convocatoria> getConvocatorias() {
		return convocatorias;
	}
	
	public void setConvocatorias(List<Convocatoria> convocatorias) {
		this.convocatorias = convocatorias;
	}
	
	public List<Area> getAreas() {
		return areas;
	}
	
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}

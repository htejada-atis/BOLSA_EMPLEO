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
	private transient BolsaEmpleoDataTable<Mensaje> dataTable;
	private transient BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTableDestinatarios;
	private Mensaje mensaje;
	private List<Convocatoria> convocatorias;
	private List<Area> areas;
	private List<UsuarioBolsaEmpleo> destinatarios;
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
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}

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
	
	public List<UsuarioBolsaEmpleo> getDestinatarios() {
		return destinatarios;
	}
	
	public void setDestinatarios(List<UsuarioBolsaEmpleo> destinatarios) {
		this.destinatarios = destinatarios;
	}
}

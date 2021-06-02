package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaCandidatoTitulacionesArea extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private transient BolsaEmpleoDataTable<TitulacionArea> dataTableTitulaciones;
	private Bolsa area;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public BolsaEmpleoDataTable<Bolsa> getDatatableAreas() {
		return dataTableAreas;
	}

	public void setDatatableAreas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreas = dt;
	}

	public BolsaEmpleoDataTable<TitulacionArea> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}

	public void setDatatableTitulaciones(BolsaEmpleoDataTable<TitulacionArea> dt) {
		this.dataTableTitulaciones = dt;
	}

	public Bolsa getArea() {
		return area;
	}

	public void setArea(Bolsa area) {
		this.area = area;
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

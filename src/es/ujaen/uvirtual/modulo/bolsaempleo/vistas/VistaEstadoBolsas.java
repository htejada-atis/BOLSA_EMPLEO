package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021.
 */
public class VistaEstadoBolsas extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Bolsa> dataTable;
	private Integer totalBolsas;
	private Integer totalBolsasBloqueadas;
	private Integer totalBolsasRevisadas;
	private Integer totalBolsasBaremables;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public Integer getTotalBolsas() {
		return totalBolsas;
	}

	public void setTotalBolsas(Integer num) {
		this.totalBolsas = num;
	}

	public Integer getTotalBolsasBloqueadas() {
		return totalBolsasBloqueadas;
	}

	public void setTotalBolsasBloqueadas(Integer num) {
		this.totalBolsasBloqueadas = num;
	}

	public Integer getTotalBolsasRevisadas() {
		return totalBolsasRevisadas;
	}

	public void setTotalBolsasRevisadas(Integer num) {
		this.totalBolsasRevisadas = num;
	}

	public Integer getTotalBolsasBaremables() {
		return totalBolsasBaremables;
	}

	public void setTotalBolsasBaremables(Integer num) {
		this.totalBolsasBaremables = num;
	}

	public BolsaEmpleoDataTable<Bolsa> getDatatableBolsas() {
		return dataTable;
	}

	public void setDatatableBolsas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTable = dt;
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

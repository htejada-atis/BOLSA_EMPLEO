package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 *
 * @author ATISoluciones 2021
 */
public class VistaAlegaciones extends Vista implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Convocatoria> listaConvocatorias = new ArrayList<>();
	private List<String> cursos = new ArrayList<>();
	private transient BolsaEmpleoDataTable<Alegacion> dataTable;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;
	private Alegacion alegacion;
	private Convocatoria ultimaConvocatoria;
	private BolsaResultado bolsaResultado;

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public BolsaResultado getBolsaResultado() {
		return bolsaResultado;
	}

	public void setBolsaResultado(BolsaResultado bolsaResultado) {
		this.bolsaResultado = bolsaResultado;
	}

	public BolsaEmpleoDataTable<Alegacion> getDatatableBolsas() {
		return dataTable;
	}

	public void setDatatableBolsas(BolsaEmpleoDataTable<Alegacion> dt) {
		this.dataTable = dt;
	}

	public Alegacion getAlegacion() {
		return alegacion;
	}

	// Setter para alegacion
	public void setAlegacion(Alegacion alegacion) {
		this.alegacion = alegacion;
	}

	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}

	public void setUsuarioLogeado(UsuarioBolsaEmpleo pusuario) {
		this.usuarioLogeado = pusuario;
	}

	public List<Convocatoria> getListaConvocatorias() {
		return listaConvocatorias;
	}

	public void setListaConvocatorias(List<Convocatoria> listaConvocatorias) {
		this.listaConvocatorias = listaConvocatorias;
	}

	public List<String> getCursos() {
		return cursos;
	}

	public void setCursos(List<String> cursos) {
		this.cursos = cursos;
	}

	public void setConvocatoria(Convocatoria ultimaConvocatoria) {
		this.ultimaConvocatoria = ultimaConvocatoria;
	}

	public Convocatoria getConvocatoria() {
		return ultimaConvocatoria;
	}

}
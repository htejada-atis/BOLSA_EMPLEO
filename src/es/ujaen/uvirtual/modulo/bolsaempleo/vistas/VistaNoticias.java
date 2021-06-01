package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaNoticias extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Noticia> dataTable;
	private Noticia noticia;

	public BolsaEmpleoDataTable<Noticia> getDatatableNoticias() {
		return dataTable;
	}

	public void setDatatableNoticias(BolsaEmpleoDataTable<Noticia> dt) {
		this.dataTable = dt;
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}
}

package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

/** Clase convocatoria de docentia.
 * @author jlopez
 *
 */
public class Noticia implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idNoticia;
	private String enlace;
	private String texto;
	private Date fecha;
	
	
	/** Constructor por defecto.
	 */
	public Noticia() {
		
	}
	
	/** Constructor con parametros.
	 * @param pidNoticia .
	 * @param penlace .
	 * @param ptexto .
	 * @param pfecha .
	 */
	public Noticia(Integer pidNoticia, String penlace, String ptexto, Date pfecha) {
		super();
		this.idNoticia = pidNoticia;
		this.enlace = penlace;
		this.texto = ptexto;
		this.fecha = pfecha;
	}
	
	public Integer getIdNoticia() {
		return idNoticia;
	}

	public void setIdNoticia(Integer idNoticia) {
		this.idNoticia = idNoticia;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Noticia [idNoticia=" + idNoticia + ", enlace=" + enlace
				+ ", texto=" + texto + ", fecha=" + fecha + "]";
	}
}

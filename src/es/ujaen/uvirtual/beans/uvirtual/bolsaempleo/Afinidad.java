package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

/** Clase afinidades de bolsa empleo.
 * @author fcampos
 *
 */
public class Afinidad {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String descripcion;
	private Float modulacion;

	
	/** Constructor por defecto.
	 */
	public Afinidad() {
		super();
		this.codNum = null;
		this.codigo = "";
		this.descripcion = "";
		this.modulacion = null;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pcodigo .
	 * @param pdescripcion .
	 * @param pmodulacion .
	 */
	public Afinidad(Integer pcodNum, String pcodigo, String pdescripcion, Float pmodulacion) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
		this.modulacion = pmodulacion;
	}

	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Afinidad(Afinidad copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.descripcion = copia.descripcion;
		this.modulacion = copia.modulacion;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String pcodigo) {
		this.codigo = pcodigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Float getModulacion() {
		return modulacion;
	}

	public void setModulacion(Float pmodulacion) {
		this.modulacion = pmodulacion;
	}
}

package es.ujaen.uvirtual.beans.uxxirrhh;

import java.io.Serializable;

/**
 * Elemento que representa una unidad/subunidad/departamento/área de conocimiento.
 * En las unidades/departamentos no existiŕa el código padre.
 * 
 * @author julopez
 *
 */
public class Unidad implements Serializable {
	private static final long serialVersionUID = 1573414782688391236L;
	public static final String TIPO_UNIDAD = "P.A.S.";
	public static final String TIPO_DEPARTAMENTO = "P.D.I.";

	protected String codigo = null;
	protected String codigoPadre = null;
	protected String tipo = null;
	protected String nombre = null;
	
	/**
	 * Constructor con todos los valores para subunidades/áreas de c.
	 * @param ptipo si es una unidad/subunidad o un departamento/área de c.
	 * @param pcodigo id. de la subunidad/área de c.
	 * @param pcodigoPadre código de unidad/departamento al que pertenece
	 * @param pnombre nombre de la unidad/subunidad/depto./área de c.
	 */
	public Unidad(String ptipo, String pcodigo, String pcodigoPadre, String pnombre) {
		super();
		this.codigo = pcodigo;
		this.codigoPadre = pcodigoPadre;
		this.tipo = ptipo;
		this.nombre = pnombre;
	}

	/**
	 * Constructor para unidades / departamentos.
	 * @param ptipo si es una unidad/subunidad o un departamento/área de c.
	 * @param pcodigo id. de la subunidad/área de c.
	 * @param pnombre nombre de la unidad/subunidad/depto./área de c.
	 */
	public Unidad(String ptipo, String pcodigo, String pnombre) {
		super();
		this.codigo = pcodigo;
		this.tipo = ptipo;
		this.nombre = pnombre;
	}

	/**
	 * Constructor básico.
	 */
	public Unidad() {
		super();
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigoPadre() {
		return codigoPadre;
	}

	public void setCodigoPadre(String codigoPadre) {
		this.codigoPadre = codigoPadre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}

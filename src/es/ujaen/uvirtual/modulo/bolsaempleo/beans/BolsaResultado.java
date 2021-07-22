package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Clob;
import java.util.List;

/** Resultado de una solicitud para una bolsa .
 * @author ATISoluciones 2021
 */
public class BolsaResultado extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private String desgloseTotal;
	private String desgloseDescripcion;
	private Double total;
	private Double totalSinAplicar;
	private transient InputStream archivo;
	private List<MeritoResultado> listaMeritos;
	private List<MeritoResultado> listaMeritosExcluidos;
	private List<MeritoResultado> listaMeritosNoEvaluados;
	private Boolean resultadoActual;
	private transient Clob acreditacionesValidadas;
	private transient Clob titulacionesValidadas;
	
	
	/** Constructor por defecto .
	 */
	public BolsaResultado() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros .
	 * @param pbolsa .
	 * @param pdesgloseTotal .
	 * @param pdesgloseDescripcion .
	 * @param ptotal .
	 * @param ptotalSinAplicar .
	 * @param plistaMeritos .
	 * @param plistaMeritosExcluidos .
	 * @param plistaMeritosNoEvaluados .
	 * @param parchivo .
	 */
	public BolsaResultado(Bolsa pbolsa, String pdesgloseTotal, String pdesgloseDescripcion, Double ptotal, Double ptotalSinAplicar, List<MeritoResultado> plistaMeritos,
			List<MeritoResultado> plistaMeritosExcluidos, List<MeritoResultado> plistaMeritosNoEvaluados, InputStream parchivo, Clob pacreditaciones, Clob ptitulaciones) {
		super(pbolsa);
		this.desgloseTotal = pdesgloseTotal;
		this.desgloseDescripcion = pdesgloseDescripcion;
		this.total = ptotal;
		this.totalSinAplicar = ptotalSinAplicar;
		this.listaMeritos = plistaMeritos;
		this.listaMeritosExcluidos = plistaMeritosExcluidos;
		this.listaMeritosNoEvaluados = plistaMeritosNoEvaluados;
		this.archivo = parchivo;
		this.acreditacionesValidadas = pacreditaciones;
		this.titulacionesValidadas = ptitulaciones;
	}
	
	/** Constructor con parametros .
	 * @param pbolsa .
	 * @param pdesgloseTotal .
	 * @param pdesgloseDescripcion .
	 * @param ptotal .
	 * @param ptotalSinAplicar .
	 */
	public BolsaResultado(Bolsa pbolsa, String pdesgloseTotal, String pdesgloseDescripcion, Double ptotal, Double ptotalSinAplicar) {
		super(pbolsa);
		this.desgloseTotal = pdesgloseTotal;
		this.desgloseDescripcion = pdesgloseDescripcion;
		this.total = ptotal;
		this.totalSinAplicar = ptotalSinAplicar;
	}
	
	/** Constructor con parametros .
	 * @param pbolsa .
	 * @param ptotal .
	 * @param presultadoActual .
	 */
	public BolsaResultado(Bolsa pbolsa, Double ptotal, Boolean presultadoActual) {
		super(pbolsa);
		this.total = ptotal;
		this.resultadoActual = presultadoActual;
	}
	
	/** Constructor con parametros .
	 * @param pbolsa .
	 * @param presultadoActual .
	 */
	public BolsaResultado(Bolsa pbolsa, Boolean presultadoActual) {
		super(pbolsa);
		this.resultadoActual = presultadoActual;
	}
	
	public String getDesgloseTotal() {
		return desgloseTotal;
	}

	public void setDesgloseTotal(String desgloseTotal) {
		this.desgloseTotal = desgloseTotal;
	}
	
	public String getDesgloseDescripcion() {
		return desgloseDescripcion;
	}

	public void setDesgloseDescripcion(String desgloseDescripcion) {
		this.desgloseDescripcion = desgloseDescripcion;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Double getTotalSinAplicar() {
		return totalSinAplicar;
	}

	public void setTotalSinAplicar(Double totalSinAplicar) {
		this.totalSinAplicar = totalSinAplicar;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}

	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}
	
	public List<MeritoResultado> getListaMeritos() {
		return listaMeritos;
	}
	
	public Integer getNumeroMeritos() {
		return listaMeritos.size();
	}
	
	public void setListaMeritos(List<MeritoResultado> meritos) {
		this.listaMeritos = meritos;
	}
	
	public List<MeritoResultado> getListaMeritosExcluidos() {
		return listaMeritosExcluidos;
	}

	public void setListaMeritosExcluidos(List<MeritoResultado> listaMeritosExcluidos) {
		this.listaMeritosExcluidos = listaMeritosExcluidos;
	}

	public List<MeritoResultado> getListaMeritosNoEvaluados() {
		return listaMeritosNoEvaluados;
	}

	public void setListaMeritosNoEvaluados(List<MeritoResultado> listaMeritosNoEvaluados) {
		this.listaMeritosNoEvaluados = listaMeritosNoEvaluados;
	}
	
	public Boolean isResultadoActual() {
		return resultadoActual;
	}

	public void setResultadoActual(Boolean resultadoActual) {
		this.resultadoActual = resultadoActual;
	}
	
	public Clob getTitulacionesValidadas() {
		return titulacionesValidadas;
	}

	public void setTitulacionesValidadas(Clob titulacionesValidadas) {
		this.titulacionesValidadas = titulacionesValidadas;
	}

	public Clob getAcreditacionesValidadas() {
		return acreditacionesValidadas;
	}

	public void setAcreditacionesValidadas(Clob acreditacionesValidadas) {
		this.acreditacionesValidadas = acreditacionesValidadas;
	}
	
	
	@Override
	public String toString() {
		return "BolsaResultado [bolsa=" + super.toString() + ", desgloseTotal=" + desgloseTotal + ", desgloseDescripcion=" + desgloseDescripcion
				+ ", total=" + total + ", totalSinAplicar=" + totalSinAplicar + ", archivo=" + archivo + ", meritos=" + listaMeritos
				+ ", listaMeritosExcluidos=" + listaMeritosExcluidos + ", listaMeritosNoEvaluados=" + listaMeritosNoEvaluados
				+ ", resultadoActual=" + resultadoActual + ", titulacionesValidadas=" + titulacionesValidadas + ", acreditacionesValidadas=" + acreditacionesValidadas + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((desgloseTotal == null) ? 0 : desgloseTotal.hashCode());
		result = prime * result + ((desgloseDescripcion == null) ? 0 : desgloseDescripcion.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime * result + ((totalSinAplicar == null) ? 0 : totalSinAplicar.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
		result = prime * result + ((listaMeritos == null) ? 0 : listaMeritos.hashCode());
		result = prime * result + ((listaMeritosExcluidos == null) ? 0 : listaMeritosExcluidos.hashCode());
		result = prime * result + ((listaMeritosNoEvaluados == null) ? 0 : listaMeritosNoEvaluados.hashCode());
		result = prime * result + ((resultadoActual == null) ? 0 : resultadoActual.hashCode());
		result = prime * result + ((titulacionesValidadas == null) ? 0 : titulacionesValidadas.hashCode());
		result = prime * result + ((acreditacionesValidadas == null) ? 0 : acreditacionesValidadas.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity"})
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}		
		BolsaResultado other = (BolsaResultado) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (desgloseTotal == null) {
			if (other.desgloseTotal != null) {
				return false;
			}
		} else if (!desgloseTotal.equals(other.desgloseTotal)) {
			return false;
		}
		if (desgloseDescripcion == null) {
			if (other.desgloseDescripcion != null) {
				return false;
			}
		} else if (!desgloseDescripcion.equals(other.desgloseDescripcion)) {
			return false;
		}
		if (total == null) {
			if (other.total != null) {
				return false;
			}
		} else if (!total.equals(other.total)) {
			return false;
		}
		if (totalSinAplicar == null) {
			if (other.totalSinAplicar != null) {
				return false;
			}
		} else if (!totalSinAplicar.equals(other.totalSinAplicar)) {
			return false;
		}
		if (archivo == null) {
			if (other.archivo != null) {
				return false;
			}
		} else if (!archivo.equals(other.archivo)) {
			return false;
		}
		if (listaMeritos == null) {
			if (other.listaMeritos != null) {
				return false;
			}
		} else if (!listaMeritos.equals(other.listaMeritos)) {
			return false;
		}
		if (listaMeritosExcluidos == null) {
			if (other.listaMeritosExcluidos != null) {
				return false;
			}
		} else if (!listaMeritosExcluidos.equals(other.listaMeritosExcluidos)) {
			return false;
		}
		if (listaMeritosNoEvaluados == null) {
			if (other.listaMeritosNoEvaluados != null) {
				return false;
			}
		} else if (!listaMeritosNoEvaluados.equals(other.listaMeritosNoEvaluados)) {
			return false;
		}
		if (resultadoActual == null) {
			if (other.resultadoActual != null) {
				return false;
			}
		} else if (!resultadoActual.equals(other.resultadoActual)) {
			return false;
		}
		if (titulacionesValidadas == null) {
			if (other.titulacionesValidadas != null) {
				return false;
			}
		} else if (!titulacionesValidadas.equals(other.titulacionesValidadas)) {
			return false;
		}
		if (acreditacionesValidadas == null) {
			if (other.acreditacionesValidadas != null) {
				return false;
			}
		} else if (!acreditacionesValidadas.equals(other.acreditacionesValidadas)) {
			return false;
		}
		
		return true;
	}
	
}


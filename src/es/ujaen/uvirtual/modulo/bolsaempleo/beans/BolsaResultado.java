package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/** Resultado de una solicitud para una bolsa .
 * @author ATISoluciones 2021
 */
public class BolsaResultado extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private String desgloseTotal;
	private Double total;
	private Double totalSinAplicar;
	private transient InputStream archivo;
	private List<MeritoResultado> listaMeritos;
	
	
	/** Constructor por defecto .
	 */
	public BolsaResultado() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros .
	 * @param pbolsa .
	 * @param pdesgloseTotal .
	 * @param ptotal .
	 * @param ptotalSinAplicar .
	 */
	public BolsaResultado(Bolsa pbolsa, String pdesgloseTotal, Double ptotal, Double ptotalSinAplicar) {
		super(pbolsa);
		this.desgloseTotal = pdesgloseTotal;
		this.total = ptotal;
		this.totalSinAplicar = ptotalSinAplicar;
	}
	
	public String getDesgloseTotal() {
		return desgloseTotal;
	}

	public void setDesgloseTotal(String desgloseTotal) {
		this.desgloseTotal = desgloseTotal;
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
	
	@Override
	public String toString() {
		return "BolsaResultado [bolsa=" + super.toString() + ", desgloseTotal=" + desgloseTotal + ", total=" + total 
				+ ", totalSinAplicar=" + totalSinAplicar + ", archivo=" + archivo + ", meritos=" + listaMeritos + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((desgloseTotal == null) ? 0 : desgloseTotal.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime * result + ((totalSinAplicar == null) ? 0 : totalSinAplicar.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
		result = prime * result + ((listaMeritos == null) ? 0 : listaMeritos.hashCode());
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
		
		return true;
	}
}


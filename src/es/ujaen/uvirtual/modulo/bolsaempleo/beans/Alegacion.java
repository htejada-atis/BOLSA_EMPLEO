package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean que representa los datos de la tabla TBEP_ALEGACIONES.
 *
 * @author ATISOLUIONES
 */
public class Alegacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer codNum;
    private Integer bepsboCodNum;
    private String estado;
    private Date fechaCreacion;
    private Date fechaConfirmacion;
    private Date fechaEnvioDepartamento;
    private Date fechaResolucionDepartamento;
    private Date fechaResolucionFinal;
    private String desResolucionDep;
    private String desResolucionFinal;
    private UsuarioBolsaEmpleo candidato;
    private UsuarioBolsaEmpleo usuario;
    private Area area;
    private Convocatoria convocatoria;

    /**
     * Constructor por defecto.
     */
    public Alegacion() {
        // este constructor esta vacio intencionadamente
    }

    /**
     * Constructor con parámetros.
     */
    public Alegacion(Integer pcodNum, Integer pbepsboCodNum, String pestado, Date pfechaCreacion, Date pfechaConfirmacion,
            Date pfechaEnvioDepartamento, Date pfechaResolucionDepartamento, Date pfechaResolucionFinal,
            String pdesResolucionDep, String pdesResolucionFinal, UsuarioBolsaEmpleo pcandidato) {
        super();
        this.codNum = pcodNum;
        this.bepsboCodNum = pbepsboCodNum;
        this.estado = pestado;
        this.fechaCreacion = pfechaCreacion;
        this.fechaConfirmacion = pfechaConfirmacion;
        this.fechaEnvioDepartamento = pfechaEnvioDepartamento;
        this.fechaResolucionDepartamento = pfechaResolucionDepartamento;
        this.fechaResolucionFinal = pfechaResolucionFinal;
        this.desResolucionDep = pdesResolucionDep;
        this.desResolucionFinal = pdesResolucionFinal;
        this.candidato = pcandidato;
    }

    /**
     * Constructor copia.
     *
     * @param copia Alegacion a copiar
     */
    public Alegacion(Alegacion copia) {
        this.codNum = copia.codNum;
        this.bepsboCodNum = copia.bepsboCodNum;
        this.estado = copia.estado;
        this.fechaCreacion = copia.fechaCreacion;
        this.fechaConfirmacion = copia.fechaConfirmacion;
        this.fechaEnvioDepartamento = copia.fechaEnvioDepartamento;
        this.fechaResolucionDepartamento = copia.fechaResolucionDepartamento;
        this.fechaResolucionFinal = copia.fechaResolucionFinal;
        this.desResolucionDep = copia.desResolucionDep;
        this.desResolucionFinal = copia.desResolucionFinal;
        this.candidato = copia.candidato;
        this.usuario = copia.usuario;
    }

    public Integer getCodNum() {
        return codNum;
    }

    public void setCodNum(Integer codNum) {
        this.codNum = codNum;
    }

    public Integer getBepsboCodNum() {
        return bepsboCodNum;
    }

    public void setBepsboCodNum(Integer bepsboCodNum) {
        this.bepsboCodNum = bepsboCodNum;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(Date fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public Date getFechaEnvioDepartamento() {
        return fechaEnvioDepartamento;
    }

    public void setFechaEnvioDepartamento(Date fechaEnvioDepartamento) {
        this.fechaEnvioDepartamento = fechaEnvioDepartamento;
    }

    public Date getFechaResolucionDepartamento() {
        return fechaResolucionDepartamento;
    }

    public void setFechaResolucionDepartamento(Date fechaResolucionDepartamento) {
        this.fechaResolucionDepartamento = fechaResolucionDepartamento;
    }

    public Date getFechaResolucionFinal() {
        return fechaResolucionFinal;
    }

    public void setFechaResolucionFinal(Date fechaResolucionFinal) {
        this.fechaResolucionFinal = fechaResolucionFinal;
    }

    public String getDesResolucionDep() {
        return desResolucionDep;
    }

    public void setDesResolucionDep(String desResolucionDep) {
        this.desResolucionDep = desResolucionDep;
    }

    public String getDesResolucionFinal() {
        return desResolucionFinal;
    }

    public void setDesResolucionFinal(String desResolucionFinal) {
        this.desResolucionFinal = desResolucionFinal;
    }

    public UsuarioBolsaEmpleo getCandidato() {
        return candidato;
    }

    public void setCandidato(UsuarioBolsaEmpleo candidato) {
        this.candidato = candidato;
    }

    public UsuarioBolsaEmpleo getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBolsaEmpleo usuario) {
        this.usuario = usuario;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    @Override
    public String toString() {
        return "Alegacion [codNum=" + codNum + ", bepsboCodNum=" + bepsboCodNum + ", estado=" + estado
            + ", fechaCreacion=" + fechaCreacion + ", fechaConfirmacion=" + fechaConfirmacion
            + ", fechaEnvioDepartamento=" + fechaEnvioDepartamento + ", fechaResolucionDepartamento="
            + fechaResolucionDepartamento + ", fechaResolucionFinal=" + fechaResolucionFinal
            + ", desResolucionDep=" + desResolucionDep + ", desResolucionFinal=" + desResolucionFinal
            + ", area=" + area + ", convocatoria=" + convocatoria + ", candidato=" + candidato + ", usuario=" + usuario + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bepsboCodNum == null) ? 0 : bepsboCodNum.hashCode());
        result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
        result = prime * result + ((desResolucionDep == null) ? 0 : desResolucionDep.hashCode());
        result = prime * result + ((desResolucionFinal == null) ? 0 : desResolucionFinal.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((fechaConfirmacion == null) ? 0 : fechaConfirmacion.hashCode());
        result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
        result = prime * result + ((fechaEnvioDepartamento == null) ? 0 : fechaEnvioDepartamento.hashCode());
        result = prime * result + ((fechaResolucionDepartamento == null) ? 0 : fechaResolucionDepartamento.hashCode());
        result = prime * result + ((fechaResolucionFinal == null) ? 0 : fechaResolucionFinal.hashCode());
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + ((convocatoria == null) ? 0 : convocatoria.hashCode());
        result = prime * result + ((candidato == null) ? 0 : candidato.hashCode());
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
        Alegacion other = (Alegacion) obj;
        if (bepsboCodNum == null) {
            if (other.bepsboCodNum != null) {
                return false;
            }
        } else if (!bepsboCodNum.equals(other.bepsboCodNum)) {
            return false;
        }
        if (codNum == null) {
            if (other.codNum != null) {
                return false;
            }
        } else if (!codNum.equals(other.codNum)) {
            return false;
        }
        if (desResolucionDep == null) {
            if (other.desResolucionDep != null) {
                return false;
            }
        } else if (!desResolucionDep.equals(other.desResolucionDep)) {
            return false;
        }
        if (desResolucionFinal == null) {
            if (other.desResolucionFinal != null) {
                return false;
            }
        } else if (!desResolucionFinal.equals(other.desResolucionFinal)) {
            return false;
        }
        if (estado == null) {
            if (other.estado != null) {
                return false;
            }
        } else if (!estado.equals(other.estado)) {
            return false;
        }
        if (fechaConfirmacion == null) {
            if (other.fechaConfirmacion != null) {
                return false;
            }
        } else if (!fechaConfirmacion.equals(other.fechaConfirmacion)) {
            return false;
        }
        if (fechaCreacion == null) {
            if (other.fechaCreacion != null) {
                return false;
            }
        } else if (!fechaCreacion.equals(other.fechaCreacion)) {
            return false;
        }
        if (fechaEnvioDepartamento == null) {
            if (other.fechaEnvioDepartamento != null) {
                return false;
            }
        } else if (!fechaEnvioDepartamento.equals(other.fechaEnvioDepartamento)) {
            return false;
        }
        if (fechaResolucionDepartamento == null) {
            if (other.fechaResolucionDepartamento != null) {
                return false;
            }
        } else if (!fechaResolucionDepartamento.equals(other.fechaResolucionDepartamento)) {
            return false;
        }
        if (fechaResolucionFinal == null) {
            if (other.fechaResolucionFinal != null) {
                return false;
            }
        } else if (!fechaResolucionFinal.equals(other.fechaResolucionFinal)) {
            return false;
        }
        if (candidato == null) {
            if (other.candidato != null) {
                return false;
            }
        } else if (!candidato.equals(other.candidato)) {
            return false;
        }
        if (usuario == null) {
            if (other.usuario != null) {
                return false;
            }
        } else if (!usuario.equals(other.usuario)) {
            return false;
        }
        return true;
    }

}

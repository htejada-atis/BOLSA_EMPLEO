package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaUsuarioBolsaEmpleo extends Vista implements Serializable {
	 private static final long serialVersionUID = 1L;
	 private List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
	 private UsuarioBolsaEmpleo usuario;
	 private String vista;
	    
	 public List<UsuarioBolsaEmpleo> getUsuarios() {
		 return usuarios;
	 }
	 
	 public void setUsuarios(List<UsuarioBolsaEmpleo> pusuarios) {
		 this.usuarios = pusuarios;
	 }
	 
	 public String getVista() {
		 return vista;
	 }
	 
	 public void setVista(String vista) {
		 this.vista = vista;
	 }    
	 
	 public UsuarioBolsaEmpleo getUsuario() {
		 return usuario;
	 }
	 
	 public void setUsuario(UsuarioBolsaEmpleo usuario) {
		 this.usuario = usuario;
	 }
}

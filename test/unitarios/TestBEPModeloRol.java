package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo rol.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloRol {
	private static final int NUM_ROLES = 4;
	private static final int COD_NUM_INVALIDO = 111_111_111;
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
	
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en ficheros
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		DataSource ds = BbddRunner.obtenerDataSourceUv();
		Conexion.setConexionUvirtual(ds);
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	/**
	 * Test listado roles.
	 */
	@Test
	public void testA01ListaRoles() {		
		try {
			List<Rol> listado = ModeloRol.obtenerInstancia().listaRoles();
			assertEquals(listado.size(), NUM_ROLES);			
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}	
	
	/**
	 * Get roles por id.
	 */
	@Test
	public void testA02GetRolPorId() {		
		try {
			Rol rol;
			
			rol = ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO);
			assertEquals(rol.getCodNum(), ModeloRol.ID_ROL_CANDIDATO);
			assertEquals(rol.getValor(), ModeloRol.ROL_CANDIDATO);
			
			rol = ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO);
			assertEquals(rol.getCodNum(), ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO);
			assertEquals(rol.getValor(), ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
			
			rol = ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_MIEMBRO_COMISION);
			assertEquals(rol.getCodNum(), ModeloRol.ID_ROL_MIEMBRO_COMISION);
			assertEquals(rol.getValor(), ModeloRol.ROL_MIEMBRO_COMISION);
			
			rol = ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_SERVICIO_PERSONAL);
			assertEquals(rol.getCodNum(), ModeloRol.ID_ROL_SERVICIO_PERSONAL);
			assertEquals(rol.getValor(), ModeloRol.ROL_SERVICIO_PERSONAL);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Errores leyendo roles.
	 */
	@Test
	public void testE01RolRequerido() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloRol.obtenerInstancia().getRoleById(null));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloRol.ERROR_ROL_REQUERIDO, throwable.getMessage());
		
		throwable = assertThrows(Throwable.class,
				() -> ModeloRol.obtenerInstancia().getRoleById(COD_NUM_INVALIDO));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloRol.ERROR_ROL_NO_EXISTE, throwable.getMessage());
	}
}

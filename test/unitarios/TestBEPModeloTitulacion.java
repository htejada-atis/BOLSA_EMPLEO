package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo titulación. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloTitulacion {

	private static final String NOMBRE_TITULACION = "nombre titulacion";

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws ParseException si error fecha
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException, ParseException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * test acierto insertar titulacion.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar titulación
	 */
	@Test
	public void testA01InsertaTitulacion() throws SQLException, UVException {
		Titulacion titulacion = new Titulacion();
		titulacion.setNombre(NOMBRE_TITULACION);
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		modelo.insertaTitulacion(titulacion, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		List<Titulacion> titulaciones = modelo.listaTitulaciones();
		Boolean insertado = false;
		for (Titulacion tit : titulaciones) {
			if (tit.getNombre().equals(titulacion.getNombre())) {
				insertado = true;
			}
		}
		assertTrue("titulacion insertada debe ser listada", insertado);
	}

	/**
	 * test error inserta titulación null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE01InsertaTitulacionNull() throws SQLException, UVException {
		Titulacion titulacion = null;
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		modelo.insertaTitulacion(titulacion, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

	/**
	 * test error inserta titulación sin nombre.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE02InsertaTitulacionSinNombre() throws SQLException, UVException {
		Titulacion titulacion = new Titulacion();
		ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
		modelo.insertaTitulacion(titulacion, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

}

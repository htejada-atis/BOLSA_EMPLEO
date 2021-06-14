package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloFichero;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo fichero. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloFichero {

	private static final Integer CODNUM = 5;
	private static final String NOMBRE_FICHERO = "nombre fichero";
	private static final String TITULO_FICHERO = "titulo fichero";
	private static final Boolean PUBLICO_FICHERO = true;
	private static final InputStream ARCHIVO_FICHERO = new ByteArrayInputStream("archivo de prueba".getBytes());

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws ParseException si error fecha
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * test acierto insertar fichero.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar fichero
	 */
	@Test
	public void testA01InsertaFichero() throws SQLException, UVException {
		Fichero fichero = new Fichero();
		fichero.setCodNum(CODNUM);
		fichero.setNombre(NOMBRE_FICHERO);
		fichero.setTitulo(TITULO_FICHERO);
		fichero.setArchivo(ARCHIVO_FICHERO);
		fichero.setPublico(PUBLICO_FICHERO);
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		modelo.insertaFichero(fichero, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		List<Fichero> ficheros = modelo.listaFicheros();

		Boolean eje = false;

		for (Fichero fich : ficheros) {
			if (fich.getCodNum().equals(fichero.getCodNum())) {
				eje = true;
			}
		}

		assertTrue("fichero insertado debe ser listado", eje);
	}

	/**
	 * test acierto borrar fichero.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar fichero
	 */
	@Test
	public void testA02BorraFichero() throws SQLException, UVException {
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		List<Fichero> ficheros = modelo.listaFicheros();
		Fichero fichero = ficheros.get(0);
		modelo.borraFichero(fichero);
		try {
			modelo.listaFichero(fichero.getCodNum());
			fail();
		} catch (UVException e) {
			// se expera excepcion
		}
		List<Fichero> ficherosFiltrados = modelo.listaFicheros();
		assertTrue("fichero borrado no debe ser listado", !ficherosFiltrados.contains(fichero));
		assertTrue("ficheros debe tener un elemento menos", ficheros.size() - 1 == ficherosFiltrados.size());
	}

	/**
	 * test error inserta fichero null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE01InsertaFicheroNull() throws SQLException, UVException {
		Fichero fichero = null;
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		modelo.insertaFichero(fichero, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

	/**
	 * test error inserta fichero sin nombre.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE02InsertaFicheroSinNombre() throws SQLException, UVException {
		Fichero fichero = new Fichero();
		fichero.setTitulo(TITULO_FICHERO);
		fichero.setArchivo(ARCHIVO_FICHERO);
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		modelo.insertaFichero(fichero, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

	/**
	 * test error inserta fichero sin título.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE03InsertaFicheroSinTitulo() throws SQLException, UVException {
		Fichero fichero = new Fichero();
		fichero.setNombre(NOMBRE_FICHERO);
		fichero.setArchivo(ARCHIVO_FICHERO);
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		modelo.insertaFichero(fichero, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

	/**
	 * test error inserta fichero sin título.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE04InsertaFicheroSinArchivo() throws SQLException, UVException {
		Fichero fichero = new Fichero();
		fichero.setNombre(NOMBRE_FICHERO);
		fichero.setTitulo(TITULO_FICHERO);
		ModeloFichero modelo = ModeloFichero.obtenerInstancia();
		modelo.insertaFichero(fichero, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}

}
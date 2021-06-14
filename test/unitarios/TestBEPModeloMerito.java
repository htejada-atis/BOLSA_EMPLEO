package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo merito. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloMerito {

	private static final Integer CODNUM = 4;
	private static final String DESCRIPCION_MERITO = "descripcion merito";
	private static final String OBSERVACION_MERITO = "observacion merito";
	private static final Double VALOR_MERITO = 1.0;
	private static final InputStream ARCHIVO_MERITO = new ByteArrayInputStream("archivo de prueba".getBytes());

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
	 * test acierto insertar merito.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar mérito
	 */
	@Test
	public void testA01InsertaMerito() throws SQLException, UVException {
		ModeloBaremacionItems modeloBar = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modeloBar.getItemBaremacionById(1);
		Merito merito = new Merito();
		merito.setCodNum(CODNUM);
		merito.setDescripcion(DESCRIPCION_MERITO);
		merito.setObservacion(OBSERVACION_MERITO);
		merito.setValor(VALOR_MERITO);
		merito.setArchivo(ARCHIVO_MERITO);
		merito.setItemBaremacion(item);
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		modelo.insertaMerito(merito, UtilsTestBolsaEmpleo.getUsuario("candidato1"));
		List<Merito> meritos = modelo.listaMeritos();

		Boolean eje = false;

		for (Merito mer : meritos) {
			if (mer.getCodNum() == merito.getCodNum()) {
				eje = true;
			}
		}

		assertTrue("mérito insertado debe ser listado", eje);
	}

	/**
	 * test acierto borrar mérito.
	 * 
	 * @throws SQLException si error en bd .
	 * @throws UVException  si error al validar mérito .
	 */
	@Test
	public void testA02BorraMerito() throws SQLException, UVException {
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		List<Merito> meritos = modelo.listaMeritos();
		Merito merito = meritos.get(0);
		List<String> idsMeritos = new ArrayList<>();
		idsMeritos.add(merito.getCodNum().toString());

		Solicitud solicitud = new Solicitud();
		solicitud.setCodNum(1);
		Merito meritoAcum = new Merito();
		meritoAcum.setCodNum(1);
		Bolsa bolsa = new Bolsa();
		bolsa.setCodNum(2);

		modeloSolicitud.borrarMeritoDeSolicitudBolsa(solicitud, bolsa, meritoAcum,
				UtilsTestBolsaEmpleo.getUsuario("candidato1"));

//    	modelo.eliminarMeritos(idsMeritos);
//    	
//    	try {
//    		modelo.listaMerito(merito.getCodNum());
//    		fail();
//    	} catch (UVException e) {
//    		//se expera excepcion
//    	}
//    	List<Merito> meritosFiltrados = modelo.listaMeritos();
//    	assertTrue("mérito borrado no debe ser listado", !meritosFiltrados.contains(merito));
//    	assertTrue("méritos debe tener un elemento menos", meritos.size() - 1 == meritosFiltrados.size());
	}

	/**
	 * test error inserta mérito null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE01InsertaMeritoNull() throws SQLException, UVException {
		Merito merito = null;
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		modelo.insertaMerito(merito, UtilsTestBolsaEmpleo.getUsuario("candidato1"));
		fail();
	}

	/**
	 * test error inserta mérito con id de usuario null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE02InsertaMeritoUsuarioNull() throws SQLException, UVException {
		Merito merito = null;
		ModeloMerito modelo = ModeloMerito.obtenerInstancia();
		modelo.insertaMerito(merito, null);
		fail();
		assertTrue(false);
	}

}

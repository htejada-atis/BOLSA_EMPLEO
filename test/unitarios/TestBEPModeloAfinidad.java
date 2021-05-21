package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/** test afinidades.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloAfinidad {
	private static final String CODIGO = "AAA";
	private static final String DESCRIPCION = "pruebas de afinidad";
	private static final Double MODULACION = (double) 10000;
	
	private static final String CODIGOEDITAR = "EEEE";
	private static final String DESCRIPCIONEDITAR = "NUEVA DESCRIPCON";
	private static final Double MODULACIONEDITAR = (double) 100;
	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
    
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
	@BeforeClass
    public static void preparaBd() throws SQLException, IOException {
		DataSource ds = BbddRunner.obtenerDataSourceUv();
		Conexion.setConexionUvirtual(ds);
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    /**
     * nuevaAfinidad.
     */
	@Test
    public void testA01nuevaAfinidad() {
		try {			
			Integer codNum = createAfinidad();
			
			Afinidad a = ModeloAfinidad.obtenerInstancia().getAfinidadById(codNum);			
			a.setCodigo(CODIGOEDITAR);
			a.setDescripcion(DESCRIPCIONEDITAR);
			a.setModulacion(MODULACIONEDITAR);			
			ModeloAfinidad.obtenerInstancia().actualizaAfinidad(a);			
			Afinidad b = ModeloAfinidad.obtenerInstancia().getAfinidadById(codNum);
			assertEquals(b.getCodigo(), CODIGOEDITAR);
			assertEquals(b.getDescripcion(), DESCRIPCIONEDITAR);
			assertEquals(b.getModulacion(), MODULACIONEDITAR);			
			borraAfinidad(codNum);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
    }
	
    /**
     * getAfinidadesByIds.
     */
	@Test
    public void testA02getAfinidadesByIds() {
		try {						
			List<Afinidad> lista = ModeloAfinidad.obtenerInstancia().listaAfinidades();
			int[] ids = lista.stream().mapToInt(Afinidad::getCodNum).toArray();			
			List<Afinidad> listaNew = ModeloAfinidad.obtenerInstancia().getAfinidadesByIds(ids);
			assertEquals(lista, listaNew);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
    }
	
    /**
     * getTiposAfinidad.
     */
	@Test
    public void testA03getTiposAfinidad() {
		try {				
			List<String> tiposAfinidad = ModeloAfinidad.obtenerInstancia().getTiposAfinidad();
			assertFalse(tiposAfinidad.isEmpty());					
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
    }
	
    /**
     * listaAfinidadesDatatable.
     */
	@Test
    public void testA04listaAfinidadesDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloAfinidad.ORDER_COLUMN_INDEX_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<Afinidad> dt = ModeloAfinidad.obtenerInstancia().listaAfinidadesDatatable(params);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
    }
	
	
    
	/**
	 * nuevaAfinidad null.
	 */
	@Test
	public void testE01nuevaAfinidad() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloAfinidad.obtenerInstancia().nuevaAfinidad(null));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloAfinidad.MENSAJE_ERROR_AFINIDAD_NULL, throwable.getMessage());
	}
	
	/**
	 * actualizaAfinidad null.
	 */
	@Test
	public void testE02actualizaAfinidad() {
		Throwable throwable = assertThrows(Throwable.class, () -> ModeloAfinidad.obtenerInstancia().actualizaAfinidad(null));
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloAfinidad.MENSAJE_AFINIDAD_OBLIGATORIA, throwable.getMessage());
		
		throwable = assertThrows(Throwable.class, () -> ModeloAfinidad.obtenerInstancia().actualizaAfinidad(new Afinidad()));
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloAfinidad.MENSAJE_AFINIDAD_CODNUM_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * getAfinidadById null.
	 */
	@Test
	public void testE03getAfinidadById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloAfinidad.obtenerInstancia().getAfinidadById(null));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloAfinidad.MENSAJE_ERROR_NO_EXISTE_AFINIDAD, throwable.getMessage());
	}
	
	private Integer createAfinidad() throws SQLException, UVException {
		Afinidad afinidad = new Afinidad();
		afinidad.setCodigo(CODIGO);
		afinidad.setDescripcion(DESCRIPCION);
		afinidad.setModulacion(MODULACION);
		
		int codNum = ModeloAfinidad.obtenerInstancia().nuevaAfinidad(afinidad);
		Afinidad anew = ModeloAfinidad.obtenerInstancia().getAfinidadById(codNum);
		assertEquals(afinidad.getCodigo(), anew.getCodigo());
		assertEquals(afinidad.getCodigoDescripcion(), anew.getCodigoDescripcion());
		assertEquals(afinidad.getDescripcion(), anew.getDescripcion());
		assertEquals(afinidad.getFechaBorrada(), anew.getFechaBorrada());
		assertEquals(afinidad.getModulacion(), anew.getModulacion());
		
		return codNum;
	}
	
	private void borraAfinidad(Integer codNum) throws SQLException, UVException {
		ArrayList<Afinidad> borrar = new ArrayList<>();
		borrar.add(ModeloAfinidad.obtenerInstancia().getAfinidadById(codNum));		
		ModeloAfinidad.obtenerInstancia().borraAfinidades(borrar);
		Afinidad borrada = ModeloAfinidad.obtenerInstancia().getAfinidadById(codNum);
		assertEquals(borrada.getCodNum(), codNum);
		assertTrue(borrada.getBorrada());
		assertNotNull(borrada.getFechaBorrada());
	}
}

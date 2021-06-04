package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo item baremacion.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloBaremacionItems {
	
	private static final Integer CODNUM = 1;
	private static final Integer CODNUM_2 = 2;
	private static final Integer CODNUM_NOEXISTE = 111_111_111;
	private static final String CODIGO = "1";
	private static final String NOMBRE = "NOMBRE ÍTEM";
	private static final String DESCRIPCION = "DESCRIPCIÓN ÍTEM";
	private static final Float VALOR = (float) 2.1;
	private static final Float VALOR_MINIMO = (float) 0.1;
	private static final Float VALOR_MAXIMO = (float) 1000;
	private static final String AFINIDAD = null;
	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
	private static final String MENSAJE_ITEMS_DEVUELTOS = "Debe devolver ítems";
    
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws ParseException si error fecha
	 * @throws UVException    .
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
	 * getItemBaremacionById.
	 */
	@Test
	public void testA01getItemBaremacionById() {
		try {
			ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
			assertEquals(item.getCodNum(), CODNUM);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getUltimoCodigoItem.
	 */
	@Test
	public void testA02getUltimoCodigoItem() {
		try {
			String codigo = ModeloBaremacionItems.obtenerInstancia().getUltimoCodigoItem(this.obtenerPrimerBloque());
			assertNotNull(codigo);
			assertNotEquals(codigo, "0");
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listadoItemsBaremacionDatatable.
	 */
	@Test
	public void testA03listadoItemsBaremacionDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloBaremacionItems.ORDER_COLUMN_INDEX_ITEMS_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<ItemBaremacion> dt = ModeloBaremacionItems.obtenerInstancia().listadoItemsBaremacionDatatable(params, this.obtenerPrimerBloque());
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * insertaItem.
	 */
	@Test
	public void testA04insertaItem() {
		try {
			BloqueBaremacion bloque = this.obtenerPrimerBloque();
			ItemBaremacion item = new ItemBaremacion();
			item.setBloqueBaremacion(bloque);
			item.setCodigo(CODIGO);
			item.setNombre(NOMBRE);
			item.setDescripcion(DESCRIPCION);
			item.setUnidades(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO);
			item.setValor(VALOR);
			item.setValorMinimo(VALOR_MINIMO);
			item.setValorMaximo(VALOR_MAXIMO);
			item.setAfinidad(AFINIDAD);
			item.setActivo(true);
			
			List<ItemBaremacion> lista = desactivarTodosItems();
			Integer codNum = ModeloBaremacionItems.obtenerInstancia().insertaItem(item, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			ItemBaremacion creado = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(codNum);			
			activarItemsLista(lista);
			
			assertEquals(creado.getCodigo(), CODIGO);
			assertEquals(creado.getNombre(), NOMBRE);
			assertEquals(creado.getDescripcion(), DESCRIPCION);
			assertEquals(creado.getUnidades(), ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO);
			assertEquals(creado.getValor(), VALOR);
			assertEquals(creado.getValorMinimo(), VALOR_MINIMO);
			assertEquals(creado.getValorMaximo(), VALOR_MAXIMO);
			assertEquals(creado.getAfinidad(), AFINIDAD);
			assertEquals(creado.getActivo(), true);
			assertEquals(creado.getBloqueBaremacion(), bloque);
			
			desactivarItem(codNum);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * actualizaItem.
	 */
	@Test
	public void testA06actualizaItem() {
		try {					
			BloqueBaremacion bloque = this.obtenerPrimerBloque();
			ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
			assertEquals(item.getCodNum(), CODNUM);
			
			item.setBloqueBaremacion(bloque);
			item.setCodigo(CODIGO);
			item.setNombre(NOMBRE);
			item.setDescripcion(DESCRIPCION);
			item.setUnidades(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO);
			item.setValor(VALOR);
			item.setValorMinimo(VALOR_MINIMO);
			item.setValorMaximo(VALOR_MAXIMO);
			item.setAfinidad(AFINIDAD);
			item.setActivo(true);
			
			ModeloBaremacionItems.obtenerInstancia().actualizaItem(item, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			ItemBaremacion itemUpd = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
			
			assertEquals(itemUpd.getCodigo(), CODIGO);
			assertEquals(itemUpd.getNombre(), NOMBRE);
			assertEquals(itemUpd.getDescripcion(), DESCRIPCION);
			assertEquals(itemUpd.getUnidades(), ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO);
			assertEquals(itemUpd.getValor(), VALOR);
			assertEquals(itemUpd.getValorMinimo(), VALOR_MINIMO);
			assertEquals(itemUpd.getValorMaximo(), VALOR_MAXIMO);
			assertEquals(itemUpd.getAfinidad(), AFINIDAD);
			assertEquals(itemUpd.getActivo(), true);
			assertEquals(itemUpd.getBloqueBaremacion(), bloque);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaItemsBaremacionActivosOrdenadosPorCodigo.
	 */
	@Test
	public void testA07listaItemsBaremacionActivosOrdenadosPorCodigo() {
		try {
			List<ItemBaremacion> lista = ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion();
			for (ItemBaremacion a : lista) {
				assertTrue(a.getActivo());
			}
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaItemsBaremacionDeUnApartado.
	 */
	@Test
	public void testA08listaItemsBaremacionDeUnApartado() {
		try {
			List<ItemBaremacion> listaItems = ModeloBaremacionItems.obtenerInstancia().getItemsDeApartado(this.obtenerPrimerBloque().getApartadoBaremacion());
			assertNotEquals(MENSAJE_ITEMS_DEVUELTOS, 0, listaItems.size());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * asignarItemsExcluyente .
	 */
	@Test
	public void testA09asignarItemsExcluyente() {
		try {
			ModeloBaremacionItems modeloItems = ModeloBaremacionItems.obtenerInstancia();
			ItemBaremacion itemPadre = modeloItems.getItemBaremacionById(CODNUM);
			ItemBaremacion itemHijo = modeloItems.getItemBaremacionById(CODNUM_2);
			
			modeloItems.asignarItemsExcluyentesAItem(itemPadre, itemHijo, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			
			List<ItemBaremacion> itemsExcluyentes = modeloItems.getItemsExcluyentes(itemPadre);
			boolean excluyentes = modeloItems.checkItemsExcluyentes(itemPadre, itemHijo);
			assertNotEquals(MENSAJE_ITEMS_DEVUELTOS, 0, itemsExcluyentes.size());
			assertEquals(true, excluyentes);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listadoItemsBaremacionExcluyentesDatatable.
	 */
	@Test
	public void testA10listadoItemsBaremacionExcluyentesDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloBaremacionItems.ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			ItemBaremacion itemPadre = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
			BolsaEmpleoDataTable<ItemBaremacion> dt = ModeloBaremacionItems.obtenerInstancia().listadoItemsBaremacionExcluyentesDatatable(params, itemPadre);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * borrarItemExcluyenteAItem .
	 */
	@Test
	public void testA11borrarItemExcluyenteAItem() {
		try {
			ModeloBaremacionItems modeloItems = ModeloBaremacionItems.obtenerInstancia();
			ItemBaremacion itemPadre = modeloItems.getItemBaremacionById(CODNUM);
			ItemBaremacion itemHijo = modeloItems.getItemBaremacionById(CODNUM_2);
			
			modeloItems.borrarItemsExcluyentesAItem(itemPadre, itemHijo, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			
			List<ItemBaremacion> itemsExcluyentes = modeloItems.getItemsExcluyentes(itemPadre);
			boolean excluyentes = modeloItems.checkItemsExcluyentes(itemPadre, itemHijo);
			assertEquals(0, itemsExcluyentes.size());
			assertNotEquals(true, excluyentes);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	
	// ERRORES
	
	/**
	 * getItemBaremacionById null.
	 */
	@Test
	public void testE01getItemBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(null));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionItems.ERROR_ITEM_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * getItemBaremacionById.
	 */
	@Test
	public void testE02getItemBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM_NOEXISTE));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionItems.ERROR_ITEM_NOEXITE, throwable.getMessage());
	}
	
	/**
	 * insertaItem.
	 */
	@Test
	public void testE03insertaItem() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionItems.obtenerInstancia().insertaItem(null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionItems.ERROR_ITEM_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * existeOtroItemActivoPorCodigo.
	 */
	@Test
	public void testE04existeOtroItemActivoPorCodigo() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			ItemBaremacion item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
			item.setCodNum(CODNUM_NOEXISTE);
			ModeloBaremacionItems.obtenerInstancia().insertaItem(item, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionItems.ERROR_ITEM_MISMO_CODIGO, throwable.getMessage());
	}
	
	// UTILS
	
	private BloqueBaremacion obtenerPrimerBloque() throws SQLException, UVException {
		return ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM);
	}
	
	private List<ItemBaremacion> desactivarTodosItems() throws UVException {
		List<ItemBaremacion> lista = null;
		
		try {
			lista = ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion();
			for (ItemBaremacion a : lista) {
				desactivarItem(a.getCodNum());
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return lista;
	}
	
	private void activarItemsLista(List<ItemBaremacion> lista) {
		for (ItemBaremacion a : lista) {
			activarItem(a.getCodNum());
		}
	}
	
	private void desactivarItem(Integer codNum) {
		try {
			ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
			ItemBaremacion item = modelo.getItemBaremacionById(codNum);
			assertEquals(item.getCodNum(), codNum);
			modelo.desactivarItem(item, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			item = modelo.getItemBaremacionById(codNum);
			assertFalse(item.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	private void activarItem(Integer codNum) {
		try {
			ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
			ItemBaremacion item = modelo.getItemBaremacionById(codNum);
			assertEquals(item.getCodNum(), codNum);
			modelo.activarItem(item, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			item = modelo.getItemBaremacionById(codNum);
			assertTrue(item.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
}

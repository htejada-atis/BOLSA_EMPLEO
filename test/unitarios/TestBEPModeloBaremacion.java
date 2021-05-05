package unitarios;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo baremacion.
*
*/
public class TestBEPModeloBaremacion {
	private static final Integer APARTADO_CODNUM = 4;
	private static final String APARTADO_CODIGO = "AAA";
	private static final String APARTADO_CODIGO2 = "AAA2";
	private static final String APARTADO_NOMBRE = "EEEE";
	private static final Boolean APARTADO_ACTIVO = true;
	private static final Float APARTADO_PUNTUACIONMAXIMA = (float) 1;
	private static final Float APARTADO_PORCENTAJEMAXIMO = (float) 1;
	
	private static final Integer BLOQUE_CODNUM = 7;
	private static final String BLOQUE_CODIGO = "A";
	private static final String BLOQUE_CODIGO2 = "B";
	private static final String BLOQUE_NOMBRE = "EE";
	private static final Boolean BLOQUE_ACTIVO = true;
	private static final ApartadoBaremacion BLOQUE_APARTADO =
			new ApartadoBaremacion(APARTADO_CODNUM, APARTADO_CODIGO, APARTADO_NOMBRE, APARTADO_ACTIVO, APARTADO_PUNTUACIONMAXIMA, APARTADO_PORCENTAJEMAXIMO);
	private static final Integer BLOQUE_NUM_MAXIMO_MERITOS = 1;
	
	private static final Integer ITEM_CODNUM = 1;
	private static final String ITEM_CODIGO = "AAA";
	private static final String ITEM_CODIGO2 = "BBB";
	private static final String ITEM_NOMBRE = "EEEE";
	private static final String ITEM_DESCRIPCION = "EEEE";
	private static final Boolean ITEM_ACTIVO = true;
	private static final BloqueBaremacion ITEM_BLOQUE =
			new BloqueBaremacion(5, BLOQUE_APARTADO, BLOQUE_CODIGO, BLOQUE_NOMBRE, BLOQUE_ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);
	private static final String ITEM_UNIDADES = "ENTERO";
	private static final Float ITEM_VALOR = (float) 10;
	private static final Float ITEM_VALORMINIMO = (float) 1;
	private static final Float ITEM_VALORMAXIMO = (float) 100;
	private static final String ITEM_AFINIDAD = "EEEE";
    
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
     */
    @BeforeClass
    public static void preparaBd() throws SQLException, IOException, ParseException {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    /** test acierto inserta apartado.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar apartado
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaApartado() throws SQLException, ParseException, UVException {
    	
    	ApartadoBaremacion apartado = new ApartadoBaremacion();
    	apartado.setCodNum(APARTADO_CODNUM);
    	apartado.setCodigo(APARTADO_CODIGO);
    	apartado.setNombre(APARTADO_NOMBRE);
    	apartado.setActivo(APARTADO_ACTIVO);
    	apartado.setPorcentajeMaximo(APARTADO_PORCENTAJEMAXIMO);
    	
    	ApartadoBaremacion apartado2 = new ApartadoBaremacion();
    	apartado2.setCodigo(APARTADO_CODIGO2);
    	apartado2.setNombre(APARTADO_NOMBRE);
    	apartado2.setActivo(APARTADO_ACTIVO);
    	apartado2.setPorcentajeMaximo(APARTADO_PORCENTAJEMAXIMO);
    	
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaApartado(apartado);
    	modelo.insertaApartado(apartado2);
    	
    	ApartadoBaremacion apartadoCont = new ApartadoBaremacion();
    	apartadoCont = modelo.getApartadoBaremacionById(apartado.getCodNum());
    	List<ApartadoBaremacion> apartados = modelo.listaApartadoBaremacion();
    	Boolean eje = false;
    	
        for (ApartadoBaremacion apar : apartados) {
            if (apar.getCodNum() == apartadoCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("afinidad insertada debe ser listada", eje);
    }
    
    
    /** test acierto insertar bloque.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar bloque
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA02InsertaBloque() throws SQLException, ParseException, UVException {
    	
    	BloqueBaremacion bloque = new BloqueBaremacion();
    	bloque.setCodNum(BLOQUE_CODNUM);
    	bloque.setCodigo(BLOQUE_CODIGO);
    	bloque.setNombre(BLOQUE_NOMBRE);
    	bloque.setActivo(BLOQUE_ACTIVO);
    	bloque.setApartadoBaremacion(BLOQUE_APARTADO);
    	bloque.setNumeroMaximoMeritos(BLOQUE_NUM_MAXIMO_MERITOS);
    	
    	BloqueBaremacion bloque2 = new BloqueBaremacion();
    	bloque2.setCodigo(BLOQUE_CODIGO2);
    	bloque2.setNombre(BLOQUE_NOMBRE);
    	bloque2.setActivo(BLOQUE_ACTIVO);
    	bloque2.setApartadoBaremacion(BLOQUE_APARTADO);
    	bloque2.setNumeroMaximoMeritos(BLOQUE_NUM_MAXIMO_MERITOS);
    	
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaBloque(bloque);
    	modelo.insertaBloque(bloque2);
    	
    	BloqueBaremacion bloqueCont = new BloqueBaremacion();
    	bloqueCont = modelo.getBloqueBaremacionById(bloque.getCodNum());
    	List<BloqueBaremacion> bloques = modelo.listaBloqueBaremacion();
    	Boolean eje = false;
    	
        for (BloqueBaremacion blo : bloques) {
            if (blo.getCodNum() == bloqueCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("bloque insertado debe ser listado", eje);
    }
    
    /** test acierto insertar item.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar item
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA03InsertaItem() throws SQLException, ParseException, UVException {
    	
    	ItemBaremacion item = new ItemBaremacion();
    	item.setCodNum(ITEM_CODNUM);
    	item.setCodigo(ITEM_CODIGO);
    	item.setNombre(ITEM_NOMBRE);
    	item.setDescripcion(ITEM_DESCRIPCION);
    	item.setActivo(ITEM_ACTIVO);
    	item.setBloqueBaremacion(ITEM_BLOQUE);
    	item.setUnidades(ITEM_UNIDADES);
    	item.setValor(ITEM_VALOR);
    	item.setValorMinimo(ITEM_VALORMINIMO);
    	item.setValorMaximo(ITEM_VALORMAXIMO);
    	item.setAfinidad(ITEM_AFINIDAD);
    	
    	ItemBaremacion item2 = new ItemBaremacion();
    	item2.setCodNum(ITEM_CODNUM);
    	item2.setCodigo(ITEM_CODIGO2);
    	item2.setNombre(ITEM_NOMBRE);
    	item2.setDescripcion(ITEM_DESCRIPCION);
    	item2.setActivo(ITEM_ACTIVO);
    	item2.setBloqueBaremacion(ITEM_BLOQUE);
    	item2.setUnidades(ITEM_UNIDADES);
    	item2.setValor(ITEM_VALOR);
    	item2.setValorMinimo(ITEM_VALORMINIMO);
    	item2.setValorMaximo(ITEM_VALORMAXIMO);
    	item2.setAfinidad(ITEM_AFINIDAD);
    	
		ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaItem(item);
    	modelo.insertaItem(item2);
    	
    	ItemBaremacion itemCont = new ItemBaremacion();
    	itemCont = modelo.getItemBaremacionById(item.getCodNum());
    	List<ItemBaremacion> items = modelo.listaItemBaremacion();
    	Boolean eje = false;
    	
        for (ItemBaremacion ite : items) {
            if (ite.getCodNum() == itemCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("item insertado debe ser listado", eje);
    }

    
    /** test acierto editar apartado.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar apartado
     */
    @Test
    public void testA04EditarApartado() throws SQLException, UVException {
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	List<ApartadoBaremacion> apartados = modelo.listaApartadoBaremacion();
    	ApartadoBaremacion apartado = apartados.get(0);
    	ApartadoBaremacion apartadoActualizado = modelo.getApartadoBaremacionById(apartado.getCodNum());
    	apartado.setNombre(APARTADO_NOMBRE);
    	modelo.actualizaApartado(apartado);
    	
    	assertFalse("apartado debe ser actualizado", apartado.equals(apartadoActualizado));
    }
    
    /** test acierto editar apartado.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar apartado
     */
    @Test
    public void testA05EditarBloque() throws SQLException, UVException {
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	List<BloqueBaremacion> bloques = modelo.listaBloqueBaremacion();
    	BloqueBaremacion bloque = bloques.get(0);
    	BloqueBaremacion bloqueActualizado = modelo.getBloqueBaremacionById(bloque.getCodNum());
    	bloque.setNombre(BLOQUE_NOMBRE);
    	modelo.actualizaBloque(bloque);

    	assertFalse("apartado debe ser actualizado", bloque.equals(bloqueActualizado));
    }
    
    /** test acierto editar apartado.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar apartado
     */
    @Test
    public void testA06EditarItem() throws SQLException, UVException {
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	List<ItemBaremacion> items = modelo.listaItemBaremacion();
    	ItemBaremacion item = items.get(0);
    	ItemBaremacion itemActualizado = modelo.getItemBaremacionById(item.getCodNum());
    	item.setNombre(ITEM_NOMBRE);
    	modelo.actualizaItem(item);

    	assertFalse("item debe ser actualizado", item.equals(itemActualizado));
    }
    
    
    /** test error inserta apartado null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaApartadoNull() throws SQLException, UVException {
    	ApartadoBaremacion apartado = null;
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaApartado(apartado);
    	fail();
    }
    
    /** test error inserta bloque null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE02InsertaBloqueNull() throws SQLException, UVException {
    	BloqueBaremacion bloque = null;
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaBloque(bloque);
    	fail();
    }
    
    /** test error inserta item null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE03InsertaItemNull() throws SQLException, UVException {
    	ItemBaremacion item = null;
    	ModeloBaremacion modelo = ModeloBaremacion.obtenerInstancia();
    	modelo.insertaItem(item);
    	fail();
    }
}

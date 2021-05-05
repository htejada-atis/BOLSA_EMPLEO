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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo meritos preferentes.
*
*/
public class TestBEPModeloMeritosPreferentes {
	private static final Integer CODNUM = 1;
	private static final String DESCRIPCION = "descripcion";
	private static final String TIPO = "MERITO";
	private static final String APLICABLE = "BLOQUE";
	private static final String FACTOR = "factor";
	private static final Float VALORMAXIMO = (float) 100.0;
	private static final Boolean ACTIVO = true;
	private static final String ITEM_UNIDADES = "ENTERO";
	private static final Float ITEM_VALOR = (float) 10;
	private static final Float ITEM_VALORMINIMO = (float) 1;
	private static final Float ITEM_VALORMAXIMO = (float) 100;
	private static final String ITEM_AFINIDAD = "EEEE";	
	private static final Integer BLOQUE_NUM_MAXIMO_MERITOS = 1;
	private static final Float APARTADO_PUNTUACIONMAXIMA = (float) 1;
	private static final Float APARTADO_PORCENTAJEMAXIMO = (float) 1;
	
	private static final ApartadoBaremacion APARTADO = new ApartadoBaremacion(CODNUM, DESCRIPCION, DESCRIPCION, ACTIVO, APARTADO_PUNTUACIONMAXIMA, APARTADO_PORCENTAJEMAXIMO);
	private static final BloqueBaremacion BLOQUE = new BloqueBaremacion(CODNUM, APARTADO, DESCRIPCION, DESCRIPCION, ACTIVO, BLOQUE_NUM_MAXIMO_MERITOS);

	private static final ItemBaremacion ITEM = 
			new ItemBaremacion(CODNUM, BLOQUE, DESCRIPCION, DESCRIPCION, ACTIVO, ITEM_UNIDADES, ITEM_VALOR, ITEM_VALORMINIMO, ITEM_VALORMAXIMO, ITEM_AFINIDAD);
	private static final ItemBaremacion ITEM2 = 
			new ItemBaremacion(CODNUM, BLOQUE, DESCRIPCION, DESCRIPCION, ACTIVO, ITEM_UNIDADES, ITEM_VALOR, ITEM_VALORMINIMO, ITEM_VALORMAXIMO, ITEM_AFINIDAD);

    
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
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaMeritoPreferente() throws SQLException, ParseException, UVException {
    	MeritoPreferente merito = new MeritoPreferente();
		merito.setCodNum(CODNUM);
		merito.setDescripcion(DESCRIPCION);
		merito.setTipo(TIPO);
		merito.setAplicable(APLICABLE);
		merito.setFactor(FACTOR);
		merito.setValorMaximo(VALORMAXIMO);
		merito.setTipoItemBaremacion(ITEM);
		merito.setAplicableBloqueBaremacion(BLOQUE);
		merito.setAplicableApartadoBaremacion(APARTADO);
		merito.setAplicableItemBaremacion(ITEM2);
		merito.setActivo(ACTIVO);
		ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
    	modelo.crearMeritoPreferente(merito);
    	
    	MeritoPreferente meritoCont = modelo.getMeritoPreferenteById(merito.getCodNum());
    	List<MeritoPreferente> meritosPreferentes = modelo.listaMeritosPreferentes();
    	Boolean eje = false;
    	
        for (MeritoPreferente mer : meritosPreferentes) {
            if (mer.getCodNum() == meritoCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("merito insertado debe ser listado", eje);
    }

    /** test acierto borrar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario 
     */
    @Test
    public void testA02DesactivarMeritoPreferente() throws SQLException, UVException {
    	ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
    	
    	List<MeritoPreferente> meritos = modelo.listaMeritosPreferentes();
    	MeritoPreferente merito = meritos.get(0);

    	modelo.cambiaFlagActivoMeritoPreferente(merito, "N");
    	
    	MeritoPreferente meritoCont = modelo.getMeritoPreferenteById(merito.getCodNum());

    	assertFalse("merito debe ser actualizado", merito.getActivo().equals(meritoCont.getActivo()));
    }
    
    /** test acierto editar merito preferente.
     * @throws SQLException si error en bd
     * @throws UVException si error el merito preferente.
     */
    @Test
    public void testA03EditarMerito() throws SQLException, UVException {
    	ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
    	
    	List<MeritoPreferente> meritos = modelo.listaMeritosPreferentes();
    	MeritoPreferente merito = meritos.get(0);
    	MeritoPreferente meritoCont = modelo.getMeritoPreferenteById(merito.getCodNum());
    	merito.setDescripcion("EJEMPLO");
    	modelo.editarMeritoPreferente(merito);
    	
    	assertFalse("usuario debe ser actualizado", merito.equals(meritoCont));
    }    
    
    /** test error inserta merito preferente null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaMeritoPreferenteNull() throws SQLException, UVException {
    	MeritoPreferente merito = null;
    	ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
    	modelo.crearMeritoPreferente(merito);
    	fail();
    }
}

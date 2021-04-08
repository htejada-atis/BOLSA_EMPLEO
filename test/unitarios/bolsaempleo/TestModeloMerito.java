package unitarios.bolsaempleo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBaremacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMerito;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase para probar el modelo fichero. */
public class TestModeloMerito {
	
    private static final String DESCRIPCION_MERITO = "descripcion merito";
    private static final String OBSERVACION_MERITO = "observacion merito";
    private static final Float VALOR_MERITO = 1.0f;
    private static final InputStream ARCHIVO_MERITO = new ByteArrayInputStream("archivo de prueba".getBytes());
    
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
    
    /** test acierto insertar merito.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar mérito
     */
    @Test
    public void testA01InsertaMerito() throws SQLException, UVException {
    	ItemBaremacion item = new ModeloBaremacion().getItemBaremacionById(1);
    	Merito merito = new Merito();
    	merito.setDescripcion(DESCRIPCION_MERITO);
    	merito.setObservacion(OBSERVACION_MERITO);
    	merito.setValor(VALOR_MERITO);
    	merito.setArchivo(ARCHIVO_MERITO);
    	merito.setItemBaremacion(item);
    	ModeloMerito modelo = new ModeloMerito();
    	modelo.insertaMerito(merito, 1);
    	List<Merito> meritos = modelo.listaMeritos();
    	assertTrue("mérito insertado debe ser listado", meritos.contains(merito));
    }
    
    /** test acierto borrar mérito.
     * @throws SQLException si error en bd .
     * @throws UVException si error al validar mérito .
     */
    @Test
    public void testA02BorraMerito() throws SQLException, UVException {
    	ModeloMerito modelo = new ModeloMerito();
    	List<Merito> meritos = modelo.listaMeritos();
    	Merito merito = meritos.get(0);
    	List<String> idsMeritos = new ArrayList<>();
    	idsMeritos.add(merito.getCodNum().toString());
    	modelo.eliminarMeritos(idsMeritos);
    	try {
    		modelo.listaMerito(merito.getCodNum());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	}
    	List<Merito> meritosFiltrados = modelo.listaMeritos();
    	assertTrue("mérito borrado no debe ser listado", !meritosFiltrados.contains(merito));
    	assertTrue("méritos debe tener un elemento menos", meritos.size() - 1 == meritosFiltrados.size());
    }
    
    /** test error inserta mérito null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaMeritoNull() throws SQLException, UVException {
    	Merito merito = null;
    	ModeloMerito modelo = new ModeloMerito();
    	modelo.insertaMerito(merito, 1);
    	fail();
    }
    
    /** test error inserta mérito con id de usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE02InsertaMeritoUsuarioNull() throws SQLException, UVException {
    	ItemBaremacion item = new ItemBaremacion(1);
    	Merito merito = new Merito();
    	merito.setDescripcion(DESCRIPCION_MERITO);
    	merito.setObservacion(OBSERVACION_MERITO);
    	merito.setValor(VALOR_MERITO);
    	merito.setArchivo(ARCHIVO_MERITO);
    	merito.setItemBaremacion(item);
    	ModeloMerito modelo = new ModeloMerito();
    	modelo.insertaMerito(merito, null);
    	fail();
    }
    
}
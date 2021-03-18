package unitarios.bolsaempleo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo noticia. */
public class TestModeloNoticia {
	
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String FECHA_STRING_EJEMPLO = "31/12/2028";
    private static final String ENLACE_NOTICIA = "enlace noticia";
    private static final String TEXTO_NOTICIA = "texto noticia";
    private static final Boolean PUBLICA_NOTICIA = true;
    private static java.util.Date fechaEjemplo;
    
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
    	fechaEjemplo = new SimpleDateFormat(FORMATO_FECHA).parse(FECHA_STRING_EJEMPLO);
    }
    
    /** test acierto insertar noticia.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar noticia
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaNoticia() throws SQLException, ParseException, UVException {
    	Noticia noticia = new Noticia();
    	noticia.setEnlace(ENLACE_NOTICIA);
    	noticia.setTexto(ENLACE_NOTICIA);
    	noticia.setFecha(fechaEjemplo);
    	noticia.setPublica(PUBLICA_NOTICIA);
    	ModeloNoticia modelo = new ModeloNoticia();
    	modelo.insertaNoticia(noticia);
    	List<Noticia> noticias = modelo.listaNoticias();
    	assertTrue("noticia insertada debe ser listada", noticias.contains(noticia));
    }

    /** test acierto borrar noticia.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar noticia 
     */
    @Test
    public void testA02BorraNoticia() throws SQLException, UVException {
    	ModeloNoticia modelo = new ModeloNoticia();
    	List<Noticia> noticias = modelo.listaNoticias();
    	Noticia noticia = noticias.get(0);
    	modelo.borraNoticia(noticia);
    	try {
    		modelo.listaNoticia(noticia.getCodNum());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	} 
    	List<Noticia> noticiasFiltradas = modelo.listaNoticias();
    	assertTrue("noticia borrada no debe ser listada", !noticiasFiltradas.contains(noticia));
    	assertTrue("noticias debe tener un elemento menos", noticias.size() - 1 == noticiasFiltradas.size());
    }
    
    /** test acierto editar noticia.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar noticia
     */
    @Test
    public void testA03EditarNoticia() throws SQLException, UVException {
    	ModeloNoticia modelo = new ModeloNoticia();
    	List<Noticia> noticias = modelo.listaNoticias();
    	Noticia noticia = noticias.get(0);
    	noticia.setTexto("texto actualizado");
    	modelo.actualizaNoticia(noticia); 
    	Noticia noticiaActualizada = modelo.listaNoticia(noticia.getCodNum());
    	assertTrue("noticia debe ser actualizada", noticia.equals(noticiaActualizada)); 
    }

    /** test error inserta noticia null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaNoticiaNull() throws SQLException, UVException {
    	Noticia noticia = null;
    	ModeloNoticia modelo = new ModeloNoticia();
    	modelo.insertaNoticia(noticia);
    	fail();
    }
    
    /** test error inserta noticia sin enlace.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws ParseException si error en fecha
     */
    @Test(expected = UVException.class)
    public void testE02InsertaNoticiaSinEnlace() throws SQLException, UVException, ParseException {
    	Noticia noticia = new Noticia();
    	noticia.setTexto(TEXTO_NOTICIA);
    	noticia.setFecha(fechaEjemplo);
    	noticia.setPublica(PUBLICA_NOTICIA);
    	ModeloNoticia modelo = new ModeloNoticia();
    	modelo.insertaNoticia(noticia);
    	fail();
    }
    
    /** test error inserta noticia sin texto.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws ParseException si error en fecha
     */
    @Test(expected = UVException.class)
    public void testE03InsertaNoticiaSinTexto() throws SQLException, UVException, ParseException {
    	Noticia noticia = new Noticia();
    	noticia.setEnlace(ENLACE_NOTICIA);
    	noticia.setFecha(fechaEjemplo);
    	noticia.setPublica(PUBLICA_NOTICIA);
    	ModeloNoticia modelo = new ModeloNoticia();
    	modelo.insertaNoticia(noticia);
    	fail();
    }
    
    /** test error inserta noticia sin fecha.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE04InsertaNoticiaSinFecha() throws SQLException, UVException {
    	Noticia noticia = new Noticia();
    	noticia.setEnlace(ENLACE_NOTICIA);
    	noticia.setTexto(TEXTO_NOTICIA);
    	noticia.setPublica(PUBLICA_NOTICIA);
    	ModeloNoticia modelo = new ModeloNoticia();
    	modelo.insertaNoticia(noticia);
    	fail();
    }
    
}
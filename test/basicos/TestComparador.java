package basicos;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.comparador.CompMenuPorIdioma;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/** test Comparador.
 *
 */
public class TestComparador {
	private static Menu menuInformacionAcademica;
	private static Menu menuInformacionAdministrativa;
	
	private static final String MENSAJE_MENOR = "Debe ser menor";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws SQLException {
    	BbddRunner.conectarBd();
    	String controladorInformacionAcademica = AyudaURL.obtenerControlador("/srv/es/informacionacademica");
    	String controladorInformacionAdministrativa = AyudaURL.obtenerControlador("/srv/es/informacionadministrativa");
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		menuInformacionAcademica = modeloAdministracion.listaMenuDeControlador(controladorInformacionAcademica);
		menuInformacionAdministrativa = modeloAdministracion.listaMenuDeControlador(controladorInformacionAdministrativa);
    }

	private CompMenuPorIdioma comparadorMenu = new CompMenuPorIdioma("es");

	/** test comparador igual.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA01Igual() throws SQLException {
        int result = comparadorMenu.compare(menuInformacionAcademica, menuInformacionAcademica);
	    assertTrue("deben ser iguales", result == 0);
	}

	/** test comparador mayor.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA02Mayor() throws SQLException {
        int result = comparadorMenu.compare(menuInformacionAdministrativa, menuInformacionAcademica);
        assertTrue("debe ser mayor", result >= 1);
	}

	/** test comparador menor.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA03Menor() throws SQLException {
        int result = comparadorMenu.compare(menuInformacionAcademica, menuInformacionAdministrativa);
        assertTrue(MENSAJE_MENOR, result <= -1);
	}
	
	/** test comparador idioma raro.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA04IdiomaMal() throws SQLException {
		String controladorUno = AyudaURL.obtenerControlador("/srv/kk/informacionacademica");
		String controladorDos = AyudaURL.obtenerControlador("/srv/kk/informacionadministrativa");
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuUno = modeloAdministracion.listaMenuDeControlador(controladorUno);
		Menu menuDos = modeloAdministracion.listaMenuDeControlador(controladorDos);
        int result = comparadorMenu.compare(menuUno, menuDos);
        assertTrue(MENSAJE_MENOR, result <= -1);
	}
	
	/** test comparador error.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE01() throws SQLException {
		String controladorUno = AyudaURL.obtenerControlador("/srv/es/informacionacademica");
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuUno = modeloAdministracion.listaMenuDeControlador(controladorUno);
		HashMap<String, String> idiomas = new HashMap<>();
		menuUno.setIdiomas(idiomas);
        int result = comparadorMenu.compare(menuUno, menuUno);
        assertTrue(MENSAJE_MENOR, result == 0);
	}
}

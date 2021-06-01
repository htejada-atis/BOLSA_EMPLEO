package unitarios;

import static org.junit.Assert.assertFalse;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo area.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloArea {
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";

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
     * nuevaAfinidad.
     */
	@Test
    public void testA01listaAreas() {
		try {			
			List<Area> areas = ModeloArea.obtenerInstancia().listaAreas();
			assertFalse(areas.isEmpty());
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
    }
}

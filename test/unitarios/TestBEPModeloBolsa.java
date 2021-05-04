package unitarios;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloBolsa;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo bolsa.
*
*/
public class TestBEPModeloBolsa {
	
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
    
    /** test acierto bloquear bolsas .
     * @throws SQLException si error en bd
     * @throws UVException si error al validar bolsa
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01BloquearBolsas() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.bloquearBolsas(bolsas);
		
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getEstado() == bolsaCont.getEstado()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa bloqueada debe ser listado", eje);
    }
    
    /** test acierto desbloquear bolsa.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar bolsa
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA02DesbloquearBolsas() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.desbloquearBolsas(bolsas);
		
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getEstado() == bolsaCont.getEstado()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA03AreaBaremable() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.ponerAreaComoBaremable(bolsas);
    	
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getBaremable() == bolsaCont.getBaremable()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA04AreaNoBaremable() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.ponerAreaComoNoBaremable(bolsas);
    	
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getBaremable() == bolsaCont.getBaremable()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA05BolsaAlegaciones() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.ponerBolsasEnAlegaciones(bolsas);
    	
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getEstado() == bolsaCont.getEstado()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
    
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA06BolsaBaremacion() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.ponerBolsasEnBaremacion(bolsas);
    	
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getEstado() == bolsaCont.getEstado()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
    
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA07BolsaRevision() throws SQLException, ParseException, UVException {
		ModeloBolsa modelo = ModeloBolsa.obtenerInstancia();
		List<Bolsa> bolsas = modelo.getBolsas();
		bolsas.remove(0);
		bolsas.remove(1);
		bolsas.remove(2);
		Bolsa bolsaCont = bolsas.get(0);
		modelo.ponerBolsasEnRevision(bolsas);
    	
    	Boolean eje = false;
    	
        for (Bolsa bol : bolsas) {
            if (bol.getCodNum() == bolsaCont.getCodNum()) {
                if (bol.getEstado() == bolsaCont.getEstado()) {
                	eje = true;
                }
            }
        }
    	
    	assertTrue("bolsa desbloqueada debe ser listado", eje);
    }
}

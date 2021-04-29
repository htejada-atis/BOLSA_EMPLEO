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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Afinidad;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloAfinidad;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test afinidades.
*
*/
public class TestBEPModeloAfinidad {
	private static final Integer ID_AFINIDAD = 4;
	private static final String CODIGO = "AAA";
	private static final String CODIGOEDITAR = "EEEE";
	private static final String DESCRIPCION = "pruebas de afinidad";
	private static final Float MODULACION = (float) 10000;
	private static final Boolean SUJETOAFINIDAD = true;
    
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
    public void testA01InsertaAfinidad() throws SQLException, ParseException, UVException {
    	Afinidad afinidad = new Afinidad();
    	afinidad.setCodNum(ID_AFINIDAD);
    	afinidad.setCodigo(CODIGO);
    	afinidad.setDescripcion(DESCRIPCION);
    	afinidad.setModulacion(MODULACION);
    	afinidad.setSujetoAfinidad(SUJETOAFINIDAD);
    	
    	Afinidad afinidad2 = new Afinidad();
    	afinidad2.setCodigo(CODIGO);
    	afinidad2.setDescripcion(DESCRIPCION);
    	afinidad2.setModulacion(MODULACION);
    	afinidad2.setSujetoAfinidad(SUJETOAFINIDAD);
    	
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	modelo.nuevaAfinidad(afinidad);
    	modelo.nuevaAfinidad(afinidad2);
    	
    	Afinidad afinidadCont = new Afinidad();
    	afinidadCont = modelo.getAfinidadById(afinidad.getCodNum());
    	List<Afinidad> afinidades = modelo.listaAfinidades();
    	Boolean eje = false;
    	
        for (Afinidad afi : afinidades) {
            if (afi.getCodNum() == afinidadCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("afinidad insertada debe ser listada", eje);
    }

    /** test acierto borrar afinidad.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar afinidad 
     */
    @Test
    public void testA02BorraAfinidad() throws SQLException, UVException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	List<Afinidad> afinidades = modelo.listaAfinidades();
    	Afinidad afinidad = afinidades.get(0);
    	modelo.borraAfinidad(afinidad);
    	try {
    		modelo.getAfinidadById(afinidad.getCodNum());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	}
    	List<Afinidad> afinidadesFiltradas = modelo.listaAfinidades();
    	assertTrue("fichero borrado no debe ser listado", !afinidadesFiltradas.contains(afinidad));
    }
    
    /** test acierto editar afinidad.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar afinidad
     */
    @Test
    public void testA03EditarAfinidad() throws SQLException, UVException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	List<Afinidad> afinidades = modelo.listaAfinidades();
    	Afinidad afinidad = afinidades.get(0);
    	afinidad.setCodigo(CODIGOEDITAR);
    	modelo.actualizaAfinidad(afinidad);
    	Afinidad afinidadActualizada = modelo.getAfinidadById(afinidad.getCodNum());
    	
    	assertFalse("afinidad debe ser actualizada", afinidad.equals(afinidadActualizada));
    }
    
    
    /** test error inserta usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaAfinidadNull() throws SQLException, UVException {
    	Afinidad afinidad = null;
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	modelo.nuevaAfinidad(afinidad);
    	fail();
    }
}

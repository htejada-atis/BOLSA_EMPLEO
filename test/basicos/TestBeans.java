package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.beans.CodigoDescripcion;
import es.ujaen.uvirtual.beans.MenuRol;
import es.ujaen.uvirtual.beans.MenuSubred;
import es.ujaen.uvirtual.beans.uxxirrhh.Cargo;
import es.ujaen.uvirtual.beans.uxxirrhh.Edificio;
import es.ujaen.uvirtual.beans.uxxirrhh.Plaza;
import es.ujaen.uvirtual.beans.uxxirrhh.Unidad;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.beans.vistas.VistaUVirtual;
import es.ujaen.uvirtual.modelo.conexion.Conexion;

/** pruebas edificio.
 *
 */
public class TestBeans {
	private static final String CADENA = "cadena";
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	System.setProperty("memcacheUrl", BbddRunner.getServidorMemcache());
    }
    
    /** prueba constructor edificio.
     */
    @Test
    public void testA01() {
    	String codigo = "codigoEdificio";
    	String descripcion = "descripcionEdificio";
    	Edificio edificio = new Edificio(codigo, descripcion);
    	assertEquals(codigo, edificio.getCodigo());
    	assertEquals(descripcion, edificio.getDescripcion());
    }

    /** prueba constructor plaza.
     */
    @Test
    public void testA02() {
    	String codigo = "codigoEdificio";
    	String descripcion = "descripcionEdificio";
    	Plaza plaza = new Plaza();
    	plaza.setCodigo(codigo);
    	plaza.setDescripcion(descripcion);
    	assertEquals(codigo, plaza.getCodigo());
    	assertEquals(descripcion, plaza.getDescripcion());
    	assertNotNull(plaza.toString());
    }

    /** prueba constructor plaza.
     */
    @Test
    public void testA03() {
    	Edificio edificio = new Edificio(CADENA, CADENA);
    	edificio.setCodigo(CADENA);
    	edificio.setDescripcion(CADENA);
    	Unidad unidad = new Unidad();
    	unidad.setCodigo(CADENA);
    	unidad.setCodigoPadre(CADENA);
    	unidad.setNombre(CADENA);
    	unidad.setTipo(CADENA);
    	Unidad subunidad = new Unidad();
    	Plaza plaza = new Plaza(CADENA, CADENA, CADENA, CADENA, CADENA, CADENA, CADENA, unidad, subunidad, edificio);
    	assertEquals(CADENA, plaza.getCodigo());
    	assertEquals(CADENA, plaza.getDescripcion());
    	assertEquals(CADENA, plaza.getBuzon());
    	assertEquals(CADENA, plaza.getDespacho());
    	assertEquals(CADENA, plaza.getLocalidad());
    	assertEquals(CADENA, plaza.getTelefono());
    	assertEquals(CADENA, plaza.getTipoPersonal());
    	assertEquals(CADENA, plaza.getUnidad().getCodigo());
    	assertEquals(CADENA, plaza.getUnidad().getCodigoPadre());
    	assertEquals(CADENA, plaza.getUnidad().getNombre());
    	assertEquals(CADENA, plaza.getUnidad().getTipo());
    	assertEquals(CADENA, plaza.getEdificio().getCodigo());
    	assertEquals(CADENA, plaza.getEdificio().getDescripcion());
    }
    
    /** prueba constructor plaza.
     */
    @Test
    public void testA04() {
    	Edificio edificio = new Edificio(CADENA, CADENA);
    	Plaza plaza = new Plaza();
    	plaza.setCodigo(CADENA);
    	plaza.setDescripcion(CADENA);
    	plaza.setBuzon(CADENA);
    	plaza.setDespacho(CADENA);
    	plaza.setEdificio(edificio);
    	plaza.setLocalidad(CADENA);
    	Unidad unidad = new Unidad(CADENA, CADENA, CADENA, CADENA);
    	Unidad subunidad = new Unidad(CADENA, CADENA, CADENA, CADENA);
    	subunidad.setCodigo(CADENA);
    	plaza.setSubunidad(subunidad);
    	plaza.setTelefono(CADENA);
    	plaza.setTipoPersonal(CADENA);
    	plaza.setUnidad(unidad);
    	assertEquals(CADENA, plaza.getCodigo());
    	assertEquals(CADENA, plaza.getDescripcion());
    	assertEquals(CADENA, plaza.getBuzon());
    	assertEquals(CADENA, plaza.getDespacho());
    	assertEquals(CADENA, plaza.getLocalidad());
    	assertEquals(CADENA, plaza.getTelefono());
    	assertEquals(CADENA, plaza.getTipoPersonal());
    	assertEquals(CADENA, plaza.getSubunidad().getCodigo());
    	assertNotNull(plaza.toString());
    }
    
    /** prueba constructor unidad.
     */
    @Test
    public void testA05() {
    	Unidad unidad = new Unidad(CADENA, CADENA, CADENA);
    	assertEquals(CADENA, unidad.getCodigo());
    	assertEquals(CADENA, unidad.getNombre());
    	assertEquals(CADENA, unidad.getTipo());
    }

    /** prueba constructor unidad.
     */
    @Test
    public void testA06() {
    	Unidad unidad = new Unidad();
    	unidad.setCodigo(CADENA);
    	unidad.setCodigoPadre(CADENA);
    	unidad.setNombre(CADENA);
    	unidad.setTipo(CADENA);
    	assertEquals(CADENA, unidad.getCodigo());
    	assertEquals(CADENA, unidad.getCodigoPadre());
    	assertEquals(CADENA, unidad.getNombre());
    	assertEquals(CADENA, unidad.getTipo());
    }

    /** prueba constructor cargo.
     */
    @Test
    public void testA07() {
    	Cargo cargo = new Cargo(CADENA, CADENA);
    	java.sql.Date ahora = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    	cargo.setDesde(ahora);
    	cargo.setHasta(ahora);
    	assertEquals(CADENA, cargo.getCodigo());
    	assertEquals(CADENA, cargo.getDescripcion());
    	assertEquals(ahora, cargo.getDesde());
    	assertEquals(ahora, cargo.getHasta());
    	assertNotNull(cargo.toString());
    }
    
    /** prueba codigo descripcion.
     */
    @Test
    public void testA08() {
    	Integer codigo = 1;
    	CodigoDescripcion cd = new CodigoDescripcion(codigo, CADENA);
    	assertEquals(codigo, cd.getCodigo());
    	assertEquals(CADENA, cd.getDescripcion());
    	assertNotNull(cd.toString());
    	assertEquals(codigo, cd.getCodigoComoInteger());
    	assertEquals(codigo, cd.getIntegerCodigo());
    	assertEquals(CADENA, cd.getDescripcionComoString());
    	assertEquals(CADENA, cd.getStringDescripcion());
    	
    	CodigoDescripcion cd2 = new CodigoDescripcion();
    	cd2.setCodigo(CADENA);
    	cd2.setDescripcion(CADENA);
    	assertEquals(CADENA, cd2.getCodigo());
    	assertEquals(CADENA, cd2.getDescripcion());
    	assertEquals(CADENA, cd2.getCodigoComoString());
    	assertEquals(CADENA, cd2.getStringCodigo());
    }
    
    /** Test Vista.
     */
    @Test
    public void testA09() {
    	Vista vista = new Vista();
    	vista.getMensajesDeAdvertencia().add(CADENA);
    	vista.getMensajesDeError().add(Vista.MENSAJE_SIN_ESCAPAR + CADENA);
    	vista.getMensajesDeExito().add(CADENA);
    	vista.getMensajesDeExito().add(CADENA);
    	vista.getMensajesInformativos().add(CADENA);
    	vista.getMensajesInformativos().add("");
    	vista.getMensajesInformativos().add(Vista.MENSAJE_SIN_ESCAPAR);
    	assertTrue(vista.getMensajesDeAdvertencia().contains(CADENA));
    	assertTrue(vista.getMensajesDeError().contains(Vista.MENSAJE_SIN_ESCAPAR + CADENA));
    	assertTrue(vista.getMensajesDeExito().contains(CADENA));
    	assertTrue(vista.getMensajesInformativos().contains(CADENA));
    	
    	assertEquals(CADENA, vista.formatearMensajesDeAdvertencia());
    	assertEquals(CADENA, vista.formatearMensajesDeError());
    	assertTrue(vista.formatearMensajesDeExito().contains("ul"));
    	assertTrue(vista.formatearMensajesInformativos().contains("ul"));
    }

    /** Test Vista vacia.
     */
    @Test
    public void testA10() {
    	Vista vista = new Vista();
    	vista.setModoExcel(false);
    	vista.setModoPdf(true);
    	assertFalse(vista.isModoExcel());
    	assertTrue(vista.isModoPdf());
    	assertEquals("", vista.formatearMensajesDeError());
    	assertEquals("", vista.formatearMensajesDeError());
    	assertTrue(vista.getObjetos().isEmpty());
    }
    
    /** Test Vista uvirtual.
     */
    @Test
    public void testA11() {
    	VistaUVirtual vista = new VistaUVirtual();
    	vista.setAvisosSinLeer(0);
    	vista.setIdentificadorUsuario(CADENA);
    	vista.setIdentificadorUsuarioAutenticado(CADENA);
    	vista.setIdiomas(null);
    	vista.setMenu(null);
    	vista.setMenuPrincipal(null);
    	vista.setMenusHijos(null);
    	vista.setMenusMismoNivel(null);
    	vista.setMigaDePan(null);
    	vista.setPaginaInicio(CADENA);
    	vista.setUsuario(null);
    	assertEquals(0, vista.getAvisosSinLeer());
    	assertEquals(CADENA, vista.getIdentificadorUsuario());
    	assertEquals(CADENA, vista.getIdentificadorUsuarioAutenticado());
    	assertNull(vista.getIdiomas());
    	assertNull(vista.getMenu());
    	assertNull(vista.getMenuPrincipal());
    	assertNull(vista.getMenusHijos());
    	assertNull(vista.getMenusMismoNivel());
    	assertNull(vista.getMigaDePan());
    	assertEquals(CADENA, vista.getPaginaInicio());
    	assertNull(vista.getUsuario());
    }
    
    /** test menuRol.
     */
    @Test
    public void testA12() {
    	MenuRol menuRol = new MenuRol();
    	menuRol.setAdministrador(false);
    	menuRol.setCodigoMenu(0);
    	menuRol.setCodigoRol(0);
    	menuRol.setDesactivado(false);
    	menuRol.setDescripcionRol(CADENA);
    	menuRol.setValorRol(CADENA);
    	assertFalse(menuRol.isAdministrador());
    	assertEquals(0, menuRol.getCodigoMenu());
    	assertEquals(0, menuRol.getCodigoRol());
    	assertFalse(menuRol.isDesactivado());
    	assertEquals(CADENA, menuRol.getDescripcionRol());
    	assertEquals(CADENA, menuRol.getValorRol());
    }
    
    /** test menuRol.
     */
    @Test
    public void testA13() {
    	MenuRol menuRol = new MenuRol(0, 0, true, true, CADENA, CADENA);
    	assertTrue(menuRol.isAdministrador());
    	assertEquals(0, menuRol.getCodigoMenu());
    	assertEquals(0, menuRol.getCodigoRol());
    	assertTrue(menuRol.isDesactivado());
    	assertEquals(CADENA, menuRol.getDescripcionRol());
    	assertEquals(CADENA, menuRol.getValorRol());
    }

    /** test menuSubred.
     */
    @Test
    public void testA14() {
    	MenuSubred menuSubred = new MenuSubred();
    	menuSubred.setCodigoMenu(0);
    	menuSubred.setDesactivado(false);
    	menuSubred.setDescripcion(CADENA);
    	menuSubred.setRed(CADENA);
    	assertEquals(0, menuSubred.getCodigoMenu());
    	assertFalse(menuSubred.isDesactivado());
    	assertEquals(CADENA, menuSubred.getDescripcion());
    	assertEquals(CADENA, menuSubred.getRed());
    }

}

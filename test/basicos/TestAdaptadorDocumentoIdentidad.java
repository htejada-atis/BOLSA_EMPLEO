package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;

/** test adaptador documento identidad.
 *
 */
public class TestAdaptadorDocumentoIdentidad {
	private static Usuario usuario;
	private static final String SIN_LETRA = "12345678";
	private static final String CON_LETRA = "12345678Z";
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	BbddRunner.conectarBd();
    	usuario = CrearUsuario.usuario("usig");
    }
    
    /** test numero de documento conocido NIF.
     * 
     */
    @Test 
    public void testA01() {
    	usuario.setDocumentoTipo("NIF");
    	String documentoAc = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario);
    	assertEquals(SIN_LETRA, documentoAc);
    	String documentoRh = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIRRHH, usuario);
    	assertEquals(CON_LETRA, documentoRh);
    }

    /** test numero de documento conocido NIE.
     * 
     */
    @Test 
    public void testA02() {
    	usuario.setDocumentoTipo("NIE");
    	String documentoAc = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario);
    	assertEquals(SIN_LETRA, documentoAc);
    	String documentoRh = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIRRHH, usuario);
    	assertEquals(CON_LETRA, documentoRh);
    }

    /** test numero de documento conocido OTR.
     * 
     */
    @Test 
    public void testA03() {
    	usuario.setDocumentoTipo("OTR");
    	String documentoAc = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario);
    	assertEquals(CON_LETRA, documentoAc);
    	String documentoRh = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIRRHH, usuario);
    	assertEquals(CON_LETRA, documentoRh);
    }

    /** null.
     * 
     */
    @Test 
    public void testA04() {
    	usuario.setDocumentoTipo("OTR");
    	String documentoAc = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, null, usuario.getDocumentoTipo());
    	assertNull(documentoAc);
    	String documentoRh = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIRRHH, usuario.getDocumentoNumero(), null);
    	assertNull(documentoRh);
    }

    /** sistema no valido.
     * 
     */
    @Test 
    public void testA05() {
    	usuario.setDocumentoTipo("OTR");
    	String documentoAc = AdaptadorDocumentoIdentidad.numeroDocumento("no valido", usuario);
    	assertNull(documentoAc);
    	String documentoRh = AdaptadorDocumentoIdentidad.numeroDocumento("no valido", usuario);
    	assertNull(documentoRh);
    }

    /** letraNIF.
     * 
     */
    @Test 
    public void testA08() {
    	usuario.setDocumentoTipo("OTR");
    	String letraNif = AdaptadorDocumentoIdentidad.letraNIF("NIF", CON_LETRA);
    	assertEquals("Z", letraNif);
    	String letraNie = AdaptadorDocumentoIdentidad.letraNIF("NIE", CON_LETRA);
    	assertEquals("Z", letraNie);
    	String letraNull = AdaptadorDocumentoIdentidad.letraNIF("NIE", null);
    	assertNull(letraNull);
    	String letraOtr = AdaptadorDocumentoIdentidad.letraNIF("OTR", null);
    	assertNull(letraOtr);
    }

}

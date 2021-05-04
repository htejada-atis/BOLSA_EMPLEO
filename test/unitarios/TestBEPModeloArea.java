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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Departamento;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloArea;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo area.
*
*/
public class TestBEPModeloArea {
	private static final Integer CODNUM = 100;
	private static final Departamento DEPARTAMENTO = new Departamento();
	private static final String ID_AREA_EXTERNO = "AREAEXTERNA";
	private static final String SECCION = "SECCION";
	private static final String DESCRIPCION = "DESCRIPCION";
	
	
	private Integer codNum;	// CODNUM
	private Departamento departamento; // BEPDEP_CODNUM
	private String idAreaExterno; // ID_AREA_CONOCIMIENTO id externo del area de conocimento
	private String idSeccion; // ID_SECCION
	private String descripcion; // DES_AREA_CONOCIMIENTO
    
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
    

}

package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/** test formateador.
 */
public class TestFormateador {
	String cadenaTrue = "true";
	String cadenaFalse = "false";
	String textoDia = "20/12/2020";

	String cadenaChunga = "abc123ñáéíóú";
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    }
    
	/** lee parametros.
	 * 
	 */
	@Test
	public void testA01() {
		final String porDefecto = "defecto";
		String parametro1 = Formateador.leeParametroString(null);
		assertEquals("", parametro1);
		String parametro2 = Formateador.leeParametroString(cadenaChunga);
		assertEquals(cadenaChunga, parametro2);
		String parametro3 = Formateador.leeParametroString(null, porDefecto);
		assertEquals(porDefecto, parametro3);
		String parametro4 = Formateador.leeParametroString("", porDefecto);
		assertEquals(porDefecto, parametro4);
		String parametro5 = Formateador.leeParametroString(cadenaChunga, porDefecto);
		assertEquals(cadenaChunga, parametro5);
	}
	
	/** formato creditos.
	 */
	@Test
	public void testA02() {
		final Integer tres = 3;
		final Double creditosEntrada = 2.5;
		final Double ceroCreditos = 0.0;
		final Double creditosNegativos = -10.0;
		String creditos = Formateador.formatoCreditos(creditosEntrada, 1);
		assertEquals("2,5", creditos);
		String creditos1 = Formateador.formatoCreditos(creditosEntrada, -1, false);
		assertEquals("#¡ERROR DE PRECISIÓN!#", creditos1);
		String creditos2 = Formateador.formatoCreditos(creditosEntrada, tres, false);
		assertEquals("#¡ERROR DE PRECISIÓN!#", creditos2);
		String creditos3 = Formateador.formatoCreditos(null, 1);
		assertEquals("", creditos3);
		String creditos4 = Formateador.formatoCreditos(ceroCreditos, 1, false);
		assertEquals("", creditos4);
		String creditos5 = Formateador.formatoCreditos(creditosNegativos, 1, false);
		assertEquals("#¡NÚMERO DE CRÉDITOS NO VÁLIDO!#", creditos5);
		String creditos6 = Formateador.formatoCreditos(creditosEntrada, 1, true);
		assertEquals("2,5", creditos6);
		String creditos7 = Formateador.formatoCreditos(creditosEntrada, 0, true);
		assertEquals("2", creditos7);
		String creditos8 = Formateador.formatoCreditos(creditosEntrada, 2, true);
		assertEquals("2,5", creditos8);
	}
	
	/** formato fecha.
	 * @throws ParseException si error fecha
	 */
	@Test
	public void testA03() throws ParseException {
		String textoHora = "13:59:48";
		String textoEntrada = textoDia + " " + textoHora;
		SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		Date fechaEntrada = formateadorFecha.parse(textoEntrada);
		String fechaFormato = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
		assertEquals(textoEntrada, fechaFormato);
		String fechaFormato2 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_DDMMYYYY);
		assertEquals(textoDia, fechaFormato2);
		String fechaFormato3 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_DIA_MES);
		assertEquals("20 de diciembre", fechaFormato3);
		String fechaFormato4 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_DIA_MES_ANIO);
		assertEquals("20 de diciembre de 2020", fechaFormato4);
		String fechaFormato5 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_YYYYMMDD);
		assertEquals("2020/12/20", fechaFormato5);
		String fechaFormato6 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_YYYYMMDD_HHMMSS);
		assertEquals("2020/12/20 " + textoHora, fechaFormato6);
		String fechaFormato7 = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_HORA_MINUTOS);
		assertEquals("13:59", fechaFormato7);
	}

	/** formato no nulo.
	 */
	@Test
	public void testA04() {
		String textoAntes = "antes";
		String textoEntrada = "texto prueba";
		String textoDespues = "despues";
		String textoFormato = Formateador.formatoNoNulo(textoEntrada);
		assertEquals(textoEntrada, textoFormato);
		String textoFormato2 = Formateador.formatoNoNulo(textoAntes, textoEntrada);
		assertEquals(textoAntes + textoEntrada, textoFormato2);
		String textoFormato3 = Formateador.formatoNoNulo(null, textoEntrada);
		assertEquals(textoEntrada, textoFormato3);
		String textoFormato4 = Formateador.formatoNoNulo(null);
		assertEquals("", textoFormato4);
		String textoFormato5 = Formateador.formatoNoNulo(textoAntes, textoEntrada, textoDespues);
		assertEquals(textoAntes + textoEntrada + textoDespues, textoFormato5);
		String textoFormato6 = Formateador.formatoNoNulo(null, textoEntrada, null);
		assertEquals(textoEntrada, textoFormato6);
	}

	/** formato calificaion.
	 */
	@Test
	public void testA05() {
		final int numeroMaximoDecimales = 3;
		final Double calificacion = 2.4999;
		String textoFormato = Formateador.formatoCalificacion(calificacion, 0);
		assertEquals("2", textoFormato);
		String textoFormato2 = Formateador.formatoCalificacion(calificacion, 1);
		assertEquals("2.5", textoFormato2);
		String textoFormato3 = Formateador.formatoCalificacion(calificacion, 2);
		assertEquals("2.50", textoFormato3);
		String textoFormato4 = Formateador.formatoCalificacion(calificacion, numeroMaximoDecimales);
		assertEquals("2.500", textoFormato4);
	}
	
	/** limita.
	 */
	@Test
	public void testA06() {
		final String textoLargo = "cadena larga para limitar en tamaño";
		final String textoCorto = "corto";
		final int limite = 10;
		String textoFormato = Formateador.limita(textoLargo, limite);
		assertEquals("cadena lar...", textoFormato);
		String textoFormato2 = Formateador.limita(textoCorto, limite);
		assertEquals(textoCorto, textoFormato2);
		String textoFormato3 = Formateador.limita(null, limite);
		assertNull(textoFormato3);
	}

	/** formatoMoneda.
	 */
	@Test
	public void testA07() {
		final Double entrada = 1010.12;
		final Double cero = 0.0;
		String textoFormato = Formateador.formatoMoneda(entrada);
		assertEquals("1.010,12 €", textoFormato);
		String textoFormato2 = Formateador.formatoMoneda(entrada, false);
		assertEquals("1.010,12 €", textoFormato2);
		String textoFormato3 = Formateador.formatoMoneda(cero, false);
		assertEquals("", textoFormato3);
		String textoFormato4 = Formateador.formatoMoneda(cero, true);
		assertEquals("0,00 €", textoFormato4);
	}

	/** lee parametro integer.
	 */
	@Test
	public void testA08() {
		final Integer esperado = 3;
		Integer valorFormato = Formateador.leeParametroInteger("3");
		assertTrue(esperado.equals(valorFormato));
		Integer valorFormato2 = Formateador.leeParametroInteger("A");
		assertNull(valorFormato2);
	}

	/** lee parametro fecha.
	 * @throws UVException si error uv
	 */
	@Test
	public void testA09() throws UVException {
		Date fecha1 = Formateador.leeParametroFecha(textoDia, Formateador.FORMATO_FECHA_DDMMYYYY, "/");
		assertNotNull(fecha1);
		Date fecha2 = Formateador.leeParametroFecha("20122020", Formateador.FORMATO_FECHA_DDMMYYYY, null);
		assertNotNull(fecha2);
		Date fecha3 = Formateador.leeParametroFecha(null, Formateador.FORMATO_FECHA_DDMMYYYY, null);
		assertNull(fecha3);
		Date fecha4 = Formateador.leeParametroFecha("", Formateador.FORMATO_FECHA_DDMMYYYY, null);
		assertNull(fecha4);
		Date fecha5 = Formateador.leeParametroFecha(textoDia + " ", Formateador.FORMATO_FECHA_DDMMYYYY, null);
		assertNull(fecha5);
		Date fecha6 = Formateador.leeParametroFecha(textoDia + "/5", Formateador.FORMATO_FECHA_DDMMYYYY, "/");
		assertNull(fecha6);
	}

	/** lee parametro boolean.
	 * @throws UVException si error uv
	 */
	@Test
	public void testA10() throws UVException {
		boolean boolean1 = Formateador.leeParametroBoolean(cadenaTrue, cadenaTrue, cadenaFalse);
		assertTrue(boolean1);
		boolean boolean2 = Formateador.leeParametroBoolean(cadenaFalse, cadenaTrue, cadenaFalse);
		assertFalse(boolean2);
	}

	/** lee parametro boolean.
	 */
	@Test
	public void testA11() {
		String cadena1 = Formateador.noNulo(cadenaChunga);
		assertEquals(cadenaChunga, cadena1);
		String cadena2 = Formateador.noNulo(null);
		assertEquals("", cadena2);
	}

	/** lee parametro integer.
	 */
	@Test
	public void testA12() {
		final Integer dos = 2;
		final Integer cuatro = 4;
		Integer integer1 = Formateador.leeParametroInteger(cadenaChunga);
		assertNull(integer1);
		Integer integer2 = Formateador.leeParametroInteger("2");
		assertEquals(dos, integer2);
		Integer integer3 = Formateador.leeParametroInteger(cadenaChunga, dos);
		assertEquals(dos, integer3);
		Integer integer4 = Formateador.leeParametroInteger("4", dos);
		assertEquals(cuatro, integer4);
	}
	
	/** lee parametro double.
	 */
	@Test
	public void testA13() {
		final Double dospPuntoCinco = 2.5;
		Double double1 = Formateador.leeParametroDouble(cadenaChunga);
		assertNull(double1);
		Double double2 = Formateador.leeParametroDouble("2.5");
		assertEquals(dospPuntoCinco, double2);
	}

	/** getNumeroDecimales.
	 */
	@Test
	public void testA14() {
		final Integer cero = 0;
		final Integer uno = 1;
		//final Integer cuatro = 4;
		final Double tresPuntoCinco = 3.5;
		//final Float tresPuntoCuarenta = 3.4005F;
		//Integer integer1 = Formateador.getNumeroDecimales(tresPuntoCuarenta);
		//assertEquals(cuatro, integer1);
		Integer integer2 = Formateador.getNumeroDecimales(tresPuntoCinco);
		assertEquals(uno, integer2);
		Integer integer3 = Formateador.getNumeroDecimales(null);
		assertEquals(cero, integer3);
	}

	/** formato fecha erroneo.
	 * @throws ParseException si error fecha
	 */
	@Test
	public void testE01() throws ParseException {
		String textoHora = "13:59:48";
		String textoEntrada = textoDia + " " + textoHora;
		SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		Date fechaEntrada = formateadorFecha.parse(textoEntrada);
		String fechaFormato = Formateador.formatoFecha(fechaEntrada, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS, "--");
		assertEquals("#¡SEPARADOR DE FECHAS NO VÁLIDO!#", fechaFormato);
		String fechaFormato2 = Formateador.formatoFecha(fechaEntrada, "YYMMDD");
		assertEquals("#¡FORMATO DE FECHA NO VÁLIDO!#", fechaFormato2);
	}
	
	/** lee parametro fecha.
	 * @throws UVException se espera error
	 */
	@Test(expected = UVException.class)
	public void testE02() throws UVException {
		Formateador.leeParametroFecha(textoDia, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS, null);
	}

	/** lee parametro fecha.
	 * @throws UVException se espera error
	 */
	@Test(expected = UVException.class)
	public void testE03() throws UVException {
		Formateador.leeParametroFecha("textoDia", Formateador.FORMATO_FECHA_DDMMYYYY, "*");
	}
	
	/** lee parametro boolean.
	 * @throws UVException si error uv
	 */
	@Test(expected = UVException.class)
	public void testE04() throws UVException {
		Formateador.leeParametroBoolean(null, cadenaTrue, cadenaFalse);
	}
	
	/** lee parametro boolean.
	 * @throws UVException si error uv
	 */
	@Test(expected = UVException.class)
	public void testE05() throws UVException {
		Formateador.leeParametroBoolean(cadenaTrue, null, cadenaFalse);
	}
	
	/** lee parametro boolean.
	 * @throws UVException si error uv
	 */
	@Test(expected = UVException.class)
	public void testE06() throws UVException {
		Formateador.leeParametroBoolean(cadenaTrue, cadenaTrue, null);
	}

	/** lee parametro boolean.
	 * @throws UVException si error uv
	 */
	@Test(expected = UVException.class)
	public void testE07() throws UVException {
		Formateador.leeParametroBoolean("pepe", cadenaTrue, cadenaFalse);
	}
	
}

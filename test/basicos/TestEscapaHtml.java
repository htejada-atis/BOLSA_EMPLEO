package basicos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ujaen.uvirtual.utilidades.EscapaHTML;

/** tests para escapahtml.
 *
 */
public class TestEscapaHtml {
	private static final String MENSAJE_IGUAL = "debe coincidir con el valor esperado"; 
	private static final String TABLA_HTML = "<table></table>";
	private static final String AMPERSANDS = " &  & ";
	private static final String RETORNOS_CARRO = "&#13;&#10;&#13;&#10;";
	private static final String LETRAS_NUMEROS = "abcxzABCXZ12390";
	private static final String SIMBOLOS = "!\"··$%&/()=?¿";

	/** test escapahtml.
     */
    @Test
    public void testA01() {
    	assertEquals(MENSAJE_IGUAL, "", EscapaHTML.escapaHTML(null));
    	assertEquals(MENSAJE_IGUAL, TABLA_HTML, EscapaHTML.escapaHTML(TABLA_HTML));
    	assertEquals(MENSAJE_IGUAL, " &amp;  &amp; ", EscapaHTML.escapaHTML(AMPERSANDS));
    	assertEquals(MENSAJE_IGUAL, "", EscapaHTML.escapaHTML(RETORNOS_CARRO));
    	assertEquals(MENSAJE_IGUAL, "&#38;&#38;", EscapaHTML.escapaHTML("&#38;&#38;"));
    	assertEquals(MENSAJE_IGUAL, "!\"&#183;&#183;$%&/()=?&#191;", EscapaHTML.escapaHTML(SIMBOLOS));
    }

    /** test escapa cadena.
     */
    @Test
    public void testA02() {
    	assertEquals(MENSAJE_IGUAL, "", EscapaHTML.escapaCadena(null));
    	assertEquals(MENSAJE_IGUAL, TABLA_HTML, EscapaHTML.escapaCadena(TABLA_HTML));
    	assertEquals(MENSAJE_IGUAL, " &#38;  &#38; ", EscapaHTML.escapaCadena(AMPERSANDS));
    	assertEquals(MENSAJE_IGUAL, LETRAS_NUMEROS, EscapaHTML.escapaCadena(LETRAS_NUMEROS));
    	assertEquals(MENSAJE_IGUAL, "!\"&#183;&#183;$%&#38;/()=?&#191;", EscapaHTML.escapaCadena(SIMBOLOS));
    }

    /** test escapa.
     */
    @Test
    public void testA03() {
    	assertEquals(MENSAJE_IGUAL, "", EscapaHTML.escapa(null));
    	assertEquals(MENSAJE_IGUAL, LETRAS_NUMEROS, EscapaHTML.escapa(LETRAS_NUMEROS));
    	assertEquals(MENSAJE_IGUAL, " &#38;  &#38; ", EscapaHTML.escapa(AMPERSANDS));
    	assertEquals(MENSAJE_IGUAL, "&#60;table&#62;&#60;/table&#62;", EscapaHTML.escapa(TABLA_HTML));
    	assertEquals(MENSAJE_IGUAL, "!&#34;&#183;&#183;$%&#38;/()=?&#191;", EscapaHTML.escapa(SIMBOLOS));
    }

    /** test ajusta codificacion.
     */
    @Test
    public void testA04() {
    	assertEquals(MENSAJE_IGUAL, null, EscapaHTML.ajustaCodificacion(null));
    	assertEquals(MENSAJE_IGUAL, LETRAS_NUMEROS, EscapaHTML.ajustaCodificacion(LETRAS_NUMEROS));
    	assertEquals(MENSAJE_IGUAL, AMPERSANDS, EscapaHTML.ajustaCodificacion(AMPERSANDS));
    	assertEquals(MENSAJE_IGUAL, TABLA_HTML, EscapaHTML.ajustaCodificacion(TABLA_HTML));
    	assertEquals(MENSAJE_IGUAL, SIMBOLOS, EscapaHTML.ajustaCodificacion(SIMBOLOS));
    }

    /** test ajustaCodificacionCadenaVaciaComoNulo.
     */
    @Test
    public void testA05() {
    	assertEquals(MENSAJE_IGUAL, null, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(null));
    	assertEquals(MENSAJE_IGUAL, null, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(""));
    	assertEquals(MENSAJE_IGUAL, LETRAS_NUMEROS, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(LETRAS_NUMEROS));
    	assertEquals(MENSAJE_IGUAL, AMPERSANDS, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(AMPERSANDS));
    	assertEquals(MENSAJE_IGUAL, TABLA_HTML, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(TABLA_HTML));
    	assertEquals(MENSAJE_IGUAL, SIMBOLOS, EscapaHTML.ajustaCodificacionCadenaVaciaComoNulo(SIMBOLOS));
    }

}

package usuario;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utilidades comunes a los test de usuario de bolsa de empleo.
 */
public class UtilsTestBEP extends UtilsTestUsuarioBase {
	private static final Pattern REGEX_TOTAL_TABLE = Pattern.compile("Total (\\d+)( \\(Seleccionados (\\d+)\\))?");
	private static final Integer REGEX_TOTAL = 1;
	private static final Integer REGEX_TOTAL_SELECTED = 3;
	
	public static final Integer WAIT_ELEMENT = 5; // segundos		
	
	/**
	 * Devuelve el waiter de elementos.
	 * @return .
	 */
	public static WebDriverWait getWaiter() {
		return new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);		
    }
		
	/**
	 * Devuelve el titulo de la página.
	 * @param main .
	 * @return .
	 */
	public String getTitlePage(WebElement main) {
		WebElement h2 = main.findElement(By.tagName("h2"));
		return h2.getText();
	}
	
	/**
	 * Devuelve el titulo de la tabla.
	 * @param tabla .
	 * @return .
	 */
	public String getTitleTable(WebElement tabla) {		
		WebElement caption = tabla.findElement(By.tagName("caption"));
		return caption.getText();
	}
	
	/**
	 * Devuelve el total de la tabla.
	 * @param table .
	 * @return .
	 */
	public int getTotalTable(WebElement table) {
		WebElement foot = table.findElement(By.tagName("tfoot"));
		WebElement total = foot.findElement(By.className("total"));
		
		Matcher matcher = REGEX_TOTAL_TABLE.matcher(total.getText());
		if (!matcher.find()) {
			return 0;
		}
		
		return Integer.parseInt(matcher.group(REGEX_TOTAL));
	}
	
	/**
	 * Devuelve el total de filas seleccionadas de la tabla.
	 * @param table .
	 * @return .
	 */
	public int getTotalSelectedTable(WebElement table) {
		WebElement foot = table.findElement(By.tagName("tfoot"));
		WebElement total = foot.findElement(By.className("total"));
		
		Matcher matcher = REGEX_TOTAL_TABLE.matcher(total.getText());
		if (!matcher.find()) {
			return 0;
		}
		
		return Integer.parseInt(matcher.group(REGEX_TOTAL_SELECTED));
	}
	
	/**
	 * Devuelve el texto de una celda de la tabla.
	 * @param table .
	 * @param indexTr .
	 * @param indexCol .
	 * @return .
	 */
	public String getTextCellTable(WebElement table, int indexTr, int indexCol) {
		WebElement tr = getRowByIndex(table, indexTr); 
		WebElement td = getColumnByIndex(tr, indexCol);
		
		return td.getText();
	}
	
	/**
	 * Devuelve un array de mensajes de exito.
	 * @return .
	 */
	public List<String> getMensajesDeExito() {
		return getMensajesDeExito("exito");		
	}
	
	/**
	 * Devuelve un array de mensajes de error.
	 * @return .
	 */
	public List<String> getMensajesDeError() {
		return getMensajesDeExito("error");
	}
	
	/**
	 * Devuelve la primera fila de la tabla.
	 * @param table .
	 * @param indexTr indice de la fila, comenzando por 0.
	 * @return .
	 */
	public WebElement getRowByIndex(WebElement table, int indexTr) {
		String idTable = table.getAttribute("id");
		String idTr = idTable + "_row_" + indexTr;
		
		return waitVisibility(By.id(idTr));
	}
	
	/**
	 * Devuelve el td de una fila, por su indice.
	 * @param tr .
	 * @param indexCol indice de la columna, comenzando por 0.
	 * @return .
	 */
	public WebElement getColumnByIndex(WebElement tr, int indexCol) {
		return tr.findElement(By.xpath("td[" + (indexCol + 1) + "]"));
	}
	
	/**
	 * Devuelve un filtro select de la tabla.
	 * @param table .
	 * @param indexFilter .
	 * @return .
	 */
	public Select getFilterSelectByIndex(WebElement table, int indexFilter) {
		WebElement tr = table.findElement(By.cssSelector("tr.filterable"));
		WebElement th = tr.findElement(By.xpath("th[" + (indexFilter + 1) + "]"));
		
		List<WebElement> elementos = th.findElements(By.xpath("*"));
		
		return new Select(elementos.get(0));		
	}
	
	/**
	 * Devuelve un filtro de tipo input de la tabla de la columna indicada.
	 * @param table .
	 * @param indexFilter .
	 * @return .
	 */
	public WebElement getFilterInputByIndex(WebElement table, int indexFilter) {
		WebElement tr = table.findElement(By.cssSelector("tr.filterable"));
		WebElement th = tr.findElement(By.xpath("th[" + (indexFilter + 1) + "]"));
		
		return th.findElement(By.tagName("input"));		
	}
	
	/**
	 * Devuelve un botón de acción de la tabla.
	 * @param table .
	 * @param text .
	 * @return .
	 */
	public WebElement getActionTableByText(WebElement table, String text) {
		WebElement footer = table.findElement(By.tagName("tfoot"));
		WebElement actions = footer.findElement(By.className("actions"));
		
		for (WebElement btn : actions.findElements(By.tagName("button"))) {
			if (btn.getText().equals(text)) {
				return btn;
			}
		}
		
		return null;
	}
	
	/**
	 * Hace click sobre el check de la fila de la tabla. Devuelve al fila.
	 * @param table .
	 * @param index indice de la fila.
	 * @return fila .
	 */
	public WebElement selectRowTable(WebElement table, Integer index) {
		WebElement tr = getRowByIndex(table, index); 
		WebElement td = getColumnByIndex(tr, 0);
		WebElement check = td.findElement(By.tagName("input"));
		check.click();	
		
		return tr;
	}
	
	/**
	 * Devuelve el dialgo ui-dialog.
	 * 
	 * @return .
	 */
	public WebElement getDialog() {
		return waitVisibility(By.className("ui-dialog"));
	}
	
	/**
	 * Devuelve el titulo del popup.
	 * @param dialog .
	 * @return .
	 */
	public String getTitlePopup(WebElement dialog) {
		return dialog.findElement(By.className("ui-dialog-title")).getText();		
	}
	
	/**
	 * Devuelve el btn del dialogo con el texto indicando.
	 * @param dialog .
	 * @param text .
	 * @return .
	 */
	public WebElement getButtonDialog(WebElement dialog, String text) {
		WebElement div = dialog.findElement(By.className("ui-dialog-buttonset"));
		
		for (WebElement btn : div.findElements(By.tagName("button"))) {
			if (btn.getAttribute("class").contains("ui-button") && btn.findElement(By.tagName("span")).getText().equals(text)) {
				return btn;
			}
		}
				
		return null;		
	}
		
	/**
	 * Comprueba el titulo de la página. Devuelve el div principal.
	 * @param main .
	 * @param title .
	 */
	public void assertTitlePage(WebElement main, String title) {
		assertEquals(getTitlePage(main), title);
	}
	
	/**
	 * Devuelve el título de la tabla.
	 * 
	 * @param tabla .
	 * @param title .
	 */
	public void assertTitleTable(WebElement tabla, String title) {
		assertEquals(getTitleTable(tabla), title);
	}

	/**
	 * Comprueba si el total de elementos de la tabla conincide.
	 * @param tabla .
	 * @param total .
	 */
	public void assertTotalTable(WebElement tabla, int total) {
		assertEquals(getTotalTable(tabla), total);		
	}
	
	/**
	 * Comprueba si el total de elementos seleccionados de la tabla conincide.
	 * @param tabla .
	 * @param total .
	 */
	public void assertTotalSelectedTable(WebElement tabla, int total) {
		assertEquals(getTotalSelectedTable(tabla), total);		
	}	
	
	/**
	 * Comprueba si el dialgo tiene el titulo pasado.
	 * @param title .
	 * @param dialog .
	 */
	public void assertTitleDialgo(WebElement dialog, String title) {
		assertEquals(getTitlePopup(dialog), title);
	}
	
	/**
	 * Espera hasta que un elemento sea visible.
	 * @param by condición de búsqueda.
	 * @return .
	 */
	public WebElement waitVisibility(By by) {
		return getWaiter().until(ExpectedConditions.visibilityOfElementLocated(by));		
	}
	
	/**
	 * Espera hasta que un elemento sea clickable.
	 * @param by condición de búsqueda.
	 * @return .
	 */
	public WebElement waitClickable(By by) {
		return getWaiter().until(ExpectedConditions.elementToBeClickable(by));		
	} 

	private List<String> getMensajesDeExito(String idDivMensaje) {
		List<String> mensajes = null;
		WebElement div = waitVisibility(By.id(idDivMensaje));
		
		mensajes = div.findElements(By.tagName("li")).stream().map(WebElement::getText).collect(Collectors.toList());
		if (mensajes.isEmpty()) {
			mensajes.add(div.getText());			
		}
		
		return mensajes;		
	} 
}

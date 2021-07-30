package bbdd;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.tareas.BaremarBolsa;
import es.ujaen.uvirtual.utilidades.UVException;
import usuario.DriverUv;

/**
 * Utilidades para los test bolsa de empleo.
 */
public final class UtilsTestBolsaEmpleo {
	private static final String NOMBREDEESTACLASE = UtilsTestBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final String UID_PRUEBAS = "usig";
	private static final String UID_CANDIDATO_PRUEBAS = "candidato1";
	private static final String UID_CANDIDATO2_PRUEBAS = "candidato2";
	private static final String UID_CANDIDATO3_PRUEBAS = "candidato3";
	private static final String UID_PERSONAL_PRUEBAS = "personal1";
	private static final Pattern RE_FILE_MIGRATION = Pattern.compile("\\d+-(im|eje)-[a-zA-Z]+\\.sql", Pattern.DOTALL);
	private static final String ID_DEPARTAMENTO = "ID_DEPARTAMENTO";
	private static final String CODNUM = "CODNUM";
	private static final Pattern REGEX_TOTAL_TABLE = Pattern.compile("Total (\\d+)( \\(Seleccionados (\\d+)\\))?");
	private static final Integer REGEX_TOTAL = 1;
	private static final Integer REGEX_TOTAL_SELECTED = 3;
	private static final int LETRA_A = 97;
	private static final int LETRA_Z = 122;
	
	public static final String ESQUEMA_ARCOS = "arcos";
	public static final String ESQUEMA_RRHH = "rrhh";
	public static final String ESQUEMA_UVIRTUAL = "uvirtual";	
	public static final Integer WAIT_ELEMENT = 10; // segundos
	public static final boolean VERBOSE = false;
	
	private static boolean cargado = false;

	private UtilsTestBolsaEmpleo() {
	}

	/**
	 * Inicializa la bd para BolsaEmpleo.
	 * 
	 * @throws Exception    .
	 * @throws SQLException .
	 * @throws IOException  .
	 */
	public static void inicializaBolsaEmpleo() throws SQLException, IOException {
		if (!cargado) {
			// mocks esquema arcos
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("MOCKS ARCOS", "Documentos/scripts/opc.bolsaempleo/mock_arcos", ESQUEMA_ARCOS, false);
	
			// mocks esquema rrhh
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("MOCKS RRHH", "Documentos/scripts/opc.bolsaempleo/mock_rrhh", ESQUEMA_RRHH, false);
			
			// limpieza migraciones uvirtual
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL CLEAN MIGRACIONES",
					"Documentos/scripts/opc.bolsaempleo/datos_desarrollo/clean_migraciones", ESQUEMA_UVIRTUAL, true);
	
			// limpieza uvirtual
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL CLEAN", "Documentos/scripts/opc.bolsaempleo/datos_desarrollo/clean", ESQUEMA_UVIRTUAL, true);
	
			// creación tablas uvirtual
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL TABLAS", "Documentos/scripts/opc.bolsaempleo", ESQUEMA_UVIRTUAL, false);
	
			// carga datos de departamentos y areas desde uvirtual
			try {
				logFile("Insertando areas ...");
				UtilsTestBolsaEmpleo.insertarDepartamentosAreas();
				logFile("Insertando areas => OK");
			} catch (SQLException ex) {
				logFile("Error insertando areas y departamentos: " + ex, false);
				throw ex;
			}
			
			// carga de datos para produccion
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("DATOS PRODUCCION", "Documentos/scripts/opc.bolsaempleo/datos_produccion", ESQUEMA_UVIRTUAL, false);
			
			// datos para pruebas
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("DATOS PRUEBA", "Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba", ESQUEMA_UVIRTUAL, false);
			cargado = true;
			
			// migraciones
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/01-exclusionsolicitud.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/02-mensajesdestinatarios.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/03-valorsolicitudbolsasmeritos.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/04-resultados.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/05-itemsolicitudbolsasmeritos.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/06-resultadosevaluadores.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/07-prefijomeritospreferentes.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/08-directoresasignarevaluadores.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/09-titulacionesacreditacionesresultados.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/10-pesoapartados.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/11-solicitudbolsasmeritostriggers.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/12-dedicaciones.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/13-plazasofertadas.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/14-contrataciones.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/15-ofertascandidatos.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/16-menuscontratacion.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba_migraciones/01-im-solicitudbolsasmeritos.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba_migraciones/02-im-prefijomeritospreferentes.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba_migraciones/03-im-dedicaciones.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba_migraciones/04-im-plazasofertadas.sql");
		}
	}

	/**
	 * obtiene una peticion autenticada con el usuario de pruebas de BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticada() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario personal5 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaPersonal() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_PERSONAL_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_PERSONAL_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario candidato1 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaCandidato() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_CANDIDATO_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_CANDIDATO_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion autenticada con el usuario candidato2 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaCandidato2() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_CANDIDATO2_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_CANDIDATO2_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}
	
	/**
	 * obtiene una peticion autenticada con el usuario candidato2 de pruebas de
	 * BolsaEmpleo.
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAutenticadaCandidato3() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_CANDIDATO3_PRUEBAS);

		Usuario usuario = CrearUsuario.usuario(UID_CANDIDATO3_PRUEBAS);
		datos.setUsuario(usuario);

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}

	/**
	 * obtiene una peticion sin usuario logueado .
	 * 
	 * @return peticion autenticada
	 */
	public static PeticionHttp peticionAnonima() {
		UVDatos datos = new UVDatos();

		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);

		return peticion;
	}
	
	/**
	 * Ejecuta un script en un esquema.
	 * @param path .
	 * @param esquema .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public static void ejecutarScript(String path, String esquema) throws IOException, SQLException {
		File directory = new File(path);
		UtilsTestBolsaEmpleo.ejecutarFile(directory, esquema);
	}

	/**
	 * Ejecuta todos los scripts de un path.
	 * @param title .
	 * @param path .
	 * @param esquema .
	 * @param reverse .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public static void ejecutarMultiplesScripts(String title, String path, String esquema, boolean reverse) throws IOException, SQLException {
		if (VERBOSE) {
			LOGGER.log(Level.INFO, "[{0}]", title + " esquema: " + esquema);
		}

		File directoryPath = new File(path);
		File[] filesList = directoryPath.listFiles();
		Arrays.sort(filesList);

		if (!reverse) {
			for (int i = 0; i < filesList.length; i++) {
				UtilsTestBolsaEmpleo.ejecutarFile(filesList[i], esquema);

			}
		} else {
			for (int i = filesList.length - 1; i >= 0; i--) {
				UtilsTestBolsaEmpleo.ejecutarFile(filesList[i], esquema);
			}
		}
	}	
	
	/**
	 * Importa departamentos y areas de vuja.
	 * @throws SQLException .
	 */
	public static void insertarDepartamentosAreas() throws SQLException {
		try (Connection conRh = BbddRunner.obtenerConexionRh(); Connection conUv = BbddRunner.obtenerConexionUvirtual()) {
			// departamentos

			String sqlDeptSelect = "SELECT DISTINCT uvnbrdsa.ID_DEPARTAMENTO, uvnbrdsa.DES_DEPARTAMENTO "
					+ "FROM UXXIRRHH.VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa ORDER BY uvnbrdsa.ID_DEPARTAMENTO";

			try (PreparedStatement stmtSelect = conRh.prepareStatement(sqlDeptSelect)) {
				try (ResultSet rs = stmtSelect.executeQuery()) {
					while (rs.next()) {
						insertDepartamento(conUv, rs.getString(ID_DEPARTAMENTO), rs.getString("DES_DEPARTAMENTO"));
					}
				}
			}

			// areas

			String sqlAreaSelect = "SELECT " 
					+ "		uvnbrdsa.ID_DEPARTAMENTO, "
					+ "		uvnbrdsa.ID_AREA_CONOCIMIENTO, " 
					+ "		MIN(uvnbrdsa.DES_SECCION) DES_SECCION,"
					+ "		MIN(uvnbrdsa.ID_SECCION) ID_SECCION, "
					+ "		MIN(uvnbrdsa.DES_AREA_CONOCIMIENTO) DES_AREA_CONOCIMIENTO "
					+ " FROM UXXIRRHH.VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa "
					+ " GROUP BY uvnbrdsa.ID_DEPARTAMENTO, uvnbrdsa.ID_AREA_CONOCIMIENTO "
					+ " ORDER BY uvnbrdsa.ID_AREA_CONOCIMIENTO";

			try (PreparedStatement stmtSelect = conRh.prepareStatement(sqlAreaSelect)) {
				try (ResultSet rs = stmtSelect.executeQuery()) {
					while (rs.next()) {
						Integer idDepartamento = getDepartamentoByCodigoRh(conUv, rs.getString(ID_DEPARTAMENTO));

						if (idDepartamento != null) {
							Integer idArea = insertArea(conUv, rs.getString("ID_AREA_CONOCIMIENTO"),
									rs.getString("DES_AREA_CONOCIMIENTO"));
							insertAreaDepartamento(idDepartamento, idArea, rs.getString("ID_SECCION"),
									rs.getString("DES_SECCION"));
						} else {
							throw new SQLException("CARGA DE AREAS FAIL, DEPARTAMENTO NO ENCONTRADO "
									+ rs.getString(ID_DEPARTAMENTO));
						}
					}
				}
			}
		}
	}
	
	/** Barema una bolsa .
	 * @param bolsa .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public static void baremarBolsa(Bolsa bolsa) throws SQLException, UVException, IOException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		bolsa.setEstado(ModeloBolsa.BOLSA_ESTADO_BLOQUEADA);
		modeloBolsa.ponerBolsaComoPendienteBaremacion(bolsa, getUsuario("personal1"));
		BaremarBolsa.runFromTest();
	}
	
	/**
	 * Devuelve un usuario por su codcuenta.
	 * @param codCuenta .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public static UsuarioBolsaEmpleo getUsuario(String codCuenta) throws SQLException, UVException {
		ModeloUsuarioBolsaEmpleo u = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		u.refrescarUsuarioBEP(CrearUsuario.refrescarUsuario(codCuenta));		
		return u.getUsuarioByCodCuenta(codCuenta);
	}
	
	/**
	 * Ejecute un delete en uvirtual. (SOLO PARA LOS TESTS).
	 * @param sql .
	 * @throws SQLException .
	 */
	public static void sqlExecute(String sql) throws SQLException {
		try (Connection con = BbddRunner.obtenerConexionUvirtual(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.executeUpdate();
		}
	}
		
	/**
	 * Genera una string random de longitud indicada.
	 * @param targetStringLength .
	 * @return .
	 */
	public static String generateRandomString(int targetStringLength) {
		// letter 'a'
		int leftLimit = LETRA_A;
		
		// letter 'z'
		int rightLimit = LETRA_Z;

		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
	
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
	public static String getTitlePage(WebElement main) {
		WebElement h2 = main.findElement(By.tagName("h2"));
		return h2.getText();
	}
	
	/**
	 * Devuelve el titulo de la tabla.
	 * @param tabla .
	 * @return .
	 */
	public static String getTitleTable(WebElement tabla) {		
		WebElement caption = tabla.findElement(By.tagName("caption"));
		return caption.getText();
	}
	
	/**
	 * Devuelve el total de la tabla.
	 * @param table .
	 * @return .
	 */
	public static int getTotalTable(WebElement table) {
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
	public static int getTotalSelectedTable(WebElement table) {
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
	public static String getTextCellTable(WebElement table, int indexTr, int indexCol) {
		WebElement tr = getRowByIndex(table, indexTr); 
		WebElement td = getColumnByIndex(tr, indexCol);
		
		return td.getText();
	}
	
	/**
	 * Devuelve un array de mensajes de exito.
	 * @return .
	 */
	public static List<String> getMensajesDeExito() {
		return getMensajesDeExito("exito");		
	}
	
	/**
	 * Devuelve un array de mensajes de error.
	 * @return .
	 */
	public static List<String> getMensajesDeError() {
		return getMensajesDeExito("error");
	}
	
	/**
	 * Devuelve la primera fila de la tabla.
	 * @param table .
	 * @param indexTr indice de la fila, comenzando por 0.
	 * @return .
	 */
	public static WebElement getRowByIndex(WebElement table, int indexTr) {
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
	public static WebElement getColumnByIndex(WebElement tr, int indexCol) {
		return tr.findElement(By.xpath("td[" + (indexCol + 1) + "]"));
	}
	
	/**
	 * Devuelve un filtro select de la tabla.
	 * @param table .
	 * @param indexFilter .
	 * @return .
	 */
	public static Select getFilterSelectByIndex(WebElement table, int indexFilter) {
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
	public static WebElement getFilterInputByIndex(WebElement table, int indexFilter) {
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
	public static WebElement getActionTableByText(WebElement table, String text) {
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
	public static WebElement selectRowTable(WebElement table, Integer index) {
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
	public static WebElement getDialog() {
		return waitVisibility(By.className("ui-dialog"));
	}
	
	/**
	 * Devuelve el titulo del popup.
	 * @param dialog .
	 * @return .
	 */
	public static String getTitlePopup(WebElement dialog) {
		return dialog.findElement(By.className("ui-dialog-title")).getText();		
	}
	
	/**
	 * Devuelve el btn del dialogo con el texto indicando.
	 * @param dialog .
	 * @param text .
	 * @return .
	 */
	public static WebElement getButtonDialog(WebElement dialog, String text) {
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
	public static void assertTitlePage(WebElement main, String title) {
		assertEquals(getTitlePage(main), title);
	}
	
	/**
	 * Devuelve el título de la tabla.
	 * 
	 * @param tabla .
	 * @param title .
	 */
	public static void assertTitleTable(WebElement tabla, String title) {
		assertEquals(getTitleTable(tabla), title);
	}

	/**
	 * Comprueba si el total de elementos de la tabla conincide.
	 * @param tabla .
	 * @param total .
	 */
	public static void assertTotalTable(WebElement tabla, int total) {
		assertEquals(getTotalTable(tabla), total);		
	}
	
	/**
	 * Comprueba si el total de elementos seleccionados de la tabla conincide.
	 * @param tabla .
	 * @param total .
	 */
	public static void assertTotalSelectedTable(WebElement tabla, int total) {
		assertEquals(getTotalSelectedTable(tabla), total);		
	}	
	
	/**
	 * Comprueba si el dialgo tiene el titulo pasado.
	 * @param title .
	 * @param dialog .
	 */
	public static void assertTitleDialgo(WebElement dialog, String title) {
		assertEquals(getTitlePopup(dialog), title);
	}
	
	/**
	 * Espera hasta que un elemento sea visible.
	 * @param by condición de búsqueda.
	 * @return .
	 */
	public static WebElement waitVisibility(By by) {
		return getWaiter().until(ExpectedConditions.visibilityOfElementLocated(by));		
	}
	
	/**
	 * Espera hasta que un elemento sea clickable.
	 * @param by condición de búsqueda.
	 * @return .
	 */
	public static WebElement waitClickable(By by) {
		return getWaiter().until(ExpectedConditions.elementToBeClickable(by));		
	} 
	
	private static void ejecutarFile(File file, String esquema) throws IOException, SQLException {
		String name = file.getName();

		// no queremos directorios
		if (!file.isDirectory()) {

			// comprobamos si el fichero tiene la estructura que queremos
			Matcher matcher = RE_FILE_MIGRATION.matcher(name);
			if (matcher.find()) {
				String operation = matcher.group(1);
				try {
					switch (operation) {
						case "im":
							UtilsTestBolsaEmpleo.ejectuarInsertMasivoFileEsquema(file, esquema);
							logFile(file.getAbsolutePath(), true);
							break;
						case "eje":
							UtilsTestBolsaEmpleo.ejectuarFileEsquema(file, esquema);
							logFile(file.getAbsolutePath(), true);
							break;
						default:
							LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el tipo: {0}", name);
					}
				} catch (IOException | SQLException ex) {
					LOGGER.log(Level.SEVERE, "ERROR EJECUTANDO FICHERO: {0}", name + " => " + ex.toString());
					throw ex;
				}
			} else {
				if (VERBOSE) {
					LOGGER.log(Level.INFO, "MIGRACION NO VÁLIDA: {0}", name);
				}
			}
		}
	}

	private static void ejectuarInsertMasivoFileEsquema(File file, String esquema) throws FileNotFoundException, SQLException {
		switch (esquema) {
			case ESQUEMA_ARCOS:
				BbddRunner.insertMasivoArcos(file.getAbsolutePath());
				break;
			case ESQUEMA_RRHH:
				BbddRunner.insertMasivoRh(file.getAbsolutePath());
				break;
			case ESQUEMA_UVIRTUAL:
				BbddRunner.insertMasivo(file.getAbsolutePath());
				break;
			default:
				LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el esquema: {0}", esquema);
		}
	}

	private static void ejectuarFileEsquema(File file, String esquema) throws SQLException, IOException {
		switch (esquema) {
			case ESQUEMA_ARCOS:
				BbddRunner.ejecutarArcos(file.getAbsolutePath());
				break;
			case ESQUEMA_RRHH:
				BbddRunner.ejecutarRh(file.getAbsolutePath());
				break;
			case ESQUEMA_UVIRTUAL:
				BbddRunner.ejecutar(file.getAbsolutePath());
				break;
			default:
				LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el esquema: {0}", esquema);
		}
	}
	
	private static Integer getDepartamentoByCodigoRh(Connection con, String cod) throws SQLException {
		String sql = "SELECT bepdep.CODNUM FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.ID_DEPARTAMENTO = ? FETCH FIRST 1 ROW ONLY";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, cod);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(CODNUM);
				}
			}
		}

		return null;
	}

	private static void insertDepartamento(Connection con, String idDept, String descripcion) throws SQLException {
		String sql = "INSERT INTO TBEP_DEPARTAMENTOS (ID_DEPARTAMENTO, DES_DEPARTAMENTO, UID_USUARIO) VALUES (?,?,'CARGA_INICIAL')";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idDept);
			stmt.setString(parameterIndex++, descripcion);
			stmt.executeUpdate();
		}
	}

	private static Integer insertArea(Connection con, String idAreaConocimiento, String descripcion) throws SQLException {
		// comprobamos si está ya el area en el sistema
		String sql = "SELECT bepare.CODNUM FROM TBEP_AREAS bepare WHERE bepare.ID_AREA_CONOCIMIENTO = ?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idAreaConocimiento);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(CODNUM);
				}
			}
		}

		// insertamos
		sql = "INSERT INTO TBEP_AREAS (ID_AREA_CONOCIMIENTO, DES_AREA_CONOCIMIENTO, UID_USUARIO) VALUES (?,?,'CARGA_INICIAL')";
		try (PreparedStatement stmt = con.prepareStatement(sql, new String[] {CODNUM})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, idAreaConocimiento);
			stmt.setString(parameterIndex++, descripcion);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		}
	}

	private static void insertAreaDepartamento(Integer idDepartamento, Integer idArea, String idSeccion, String descSeccion) throws SQLException {
		String sql = "INSERT INTO TBEP_AREAS_DEPARTAMENTOS (BEPARE_CODNUM,BEPDEP_CODNUM,ID_SECCION,DES_SECCION,UID_USUARIO) VALUES (?,?,?,?,'CARGA_INICIAL')";
		try (Connection con = BbddRunner.obtenerConexionUvirtual(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idArea);
			stmt.setInt(parameterIndex++, idDepartamento);
			stmt.setString(parameterIndex++, idSeccion);
			stmt.setString(parameterIndex++, descSeccion);
			stmt.executeUpdate();
		}
	}

	private static void logFile(String name, boolean ok) {
		logFile(String.format("%s%s", name, ok ? " => OK" : ""));
	}

	private static void logFile(String name) {
		if (VERBOSE) {
			LOGGER.log(Level.INFO, "SQL {0}", name);
		}
	}

	private static List<String> getMensajesDeExito(String idDivMensaje) {
		List<String> mensajes = null;
		WebElement div = waitVisibility(By.id(idDivMensaje));
		
		mensajes = div.findElements(By.tagName("li")).stream().map(WebElement::getText).collect(Collectors.toList());
		if (mensajes.isEmpty()) {
			mensajes.add(div.getText());			
		}
		
		return mensajes;
	}
}

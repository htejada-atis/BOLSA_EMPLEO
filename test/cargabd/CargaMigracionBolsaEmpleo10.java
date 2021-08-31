package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210728 .
 *	- dedicaciones .
 *	- contrataciones .
 *	- ofertas candidatos .
 *	- menus contratación .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo10 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/12-dedicaciones.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/13-plazasofertadas.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/14-contrataciones.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/15-ofertascandidatos.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/16-menuscontratacion.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/17-plantillas.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/18-parametrosplantillas.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/19-estadocandidato.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}

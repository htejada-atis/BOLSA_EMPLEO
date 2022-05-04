package cargabd;

import bbdd.UtilsTestBolsaEmpleo;

/**
 * Migraciones 20211124 . - actualizar item en solicitud bolsa meritos .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo19 {
	/**
	 * Main .
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {
		UtilsTestBolsaEmpleo.updateItemBaremacionEnSolicitudBolsaMeritos();
	}
}

package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.UtilsTestBolsaEmpleo;

public class CargaInicialBolsaEmpleo {

	public static void main(String[] args) {
		try {
			// limpieza uvirtual
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL CLEAN", "Documentos/scripts/opc.bolsaempleo/datos_desarrollo/clean", "uvirtual", true);

			// creación tablas uvirtual
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("UVIRTUAL TABLAS", "Documentos/scripts/opc.bolsaempleo", "uvirtual", false);
			
			// importación de areas y departamentos
			UtilsTestBolsaEmpleo.insertarDepartamentosAreas();
			
			// datos produccion
			UtilsTestBolsaEmpleo.ejecutarMultiplesScripts("DATOS PRODUCCION", "Documentos/scripts/opc.bolsaempleo/datos_produccion", "uvirtual", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

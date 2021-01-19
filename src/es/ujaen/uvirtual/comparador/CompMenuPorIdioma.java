package es.ujaen.uvirtual.comparador;

import java.util.Comparator;

import es.ujaen.uvirtual.beans.Menu;

/** comparador menu por idioma.
 * @author julopez
 *
 */
public class CompMenuPorIdioma implements Comparator<Menu> {
	String idioma = null;
	
	/** constructor.
	 * @param pidioma idioma
	 */
	public CompMenuPorIdioma(String pidioma) {
		this.idioma = pidioma;
	}
	
	/** Compara.
	 * @param o1 objeto1
	 * @param o2 objeto2
	 * @return resultado de la comparacion
	 */
	public int compare(Menu o1, Menu o2) {
		try {
			return o1.getIdiomas().get(idioma) .compareTo(o2.getIdiomas().get(idioma));
		} catch (Exception e) {
			return o1.getControlador().compareTo(o2.getControlador());
		}
	}

}

/**
 * Die Karte stellt ein Spielfeld mit Variabler Größe dar.
 * Die Felder der Karte können Editiert werden.
 * Quasi die Karte die der Hamster (die AI das Hamsters) dabei hat und alle aufgedeckten Felder darauf vermerkt.
*/
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class Karte {

	private ZweiDimensionalesArray<Feld> karte;
	
	/**
	 * Baut ein Spielfeld aus den momentan vorhandenen Informationen
	 * Hier werden, allerding WegberechnungsFeld er verwendet.
	 */
	public Spielfeld createSpielfeld(){
		//int height = minReihe;
		//Spielfeld ret = new Spielfeld(width, height);
		Spielfeld ret = new Spielfeld();
		return null;
	}
	
	public Feld getFeld(int reihe, int spalte){
		return null;
	}
	
	public void update(Karte informationen){
		
	}
	
	
}


/**
 * Die Karte stellt ein Spielfeld mit Variabler Größe dar.
 * Die Felder der Karte können Editiert werden.
 * Quasi die Karte die der Hamster (die AI das Hamsters) dabei hat und alle aufgedeckten Felder darauf vermerkt.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class Karte {

	private ZweiDimensionalesArray<Feld> karte;
	
	public Karte() {
		karte = new ZweiDimensionalesArray<Feld>();
	}
	
	public void setFeld(int reihe, int spalte, Feld feld) {
		karte.setFeld(new Position(spalte, reihe), feld);
	}
		
	
	/**
	 * Baut ein Spielfeld aus den momentan vorhandenen Informationen
	 * Hier werden, allerding WegberechnungsFeld er verwendet.
	 */
	public Spielfeld createSpielfeld(){
		//int height = minReihe;
		//Spielfeld ret = new Spielfeld(width, height);
		ZweiDimensionalesArray<WegberechnungsFeld> felder = new ZweiDimensionalesArray<WegberechnungsFeld>();
		int minX = karte.getMinSpalte();
		int minY = karte.getMinReihe();
		for(int x = minX;x<karte.getMaxSpalte();x++)for(int y = minY;y<karte.getMaxReihe();y++){
			Position pos = new Position(x,y);
			felder.setFeld(new Position(x-minX,y-minY),this.getFeld(pos));
		}
		Spielfeld ret = new Spielfeld(felder);
		return ret;
	}
	
	public Feld getFeld(int reihe, int spalte){
		Position pos = new Position(spalte, reihe);
		if(karte.feldBelegt(pos)) {
			return karte.getFeld(pos);
		}
		return unerkundetesFeld();
	}
	
	private Feld unerkundetesFeld(){
		return new Feld(false, 25);
	}
	
	public void update(Karte informationen){
		
	}
	
	
}

import java.util.HashMap;

/**
 * Die Karte stellt ein Spielfeld mit Variabler Größe dar.
 * Die Felder der Karte können Editiert werden.
*/
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class Karte {

	int minReihe = -1;
	int maxReihe = -1;
	int minSpalte = -1;
	int maxSpalte = -1;
	
	HashMap<String,Feld> felder = new HashMap<String,Feld>();

	/**
	 * Gibt ein Feld mit den bisher auf diesem Feld eingetragenen Informationen zurück.
	 * Dieses kann anschließend editiert werden.
	 * @return - Das Feld an der Stelle reihe,spalte
	*/
	public Feld getFeld(int reihe, int spalte){
		feldZugriff(reihe,spalte);
		String key = ""+reihe+""+spalte+"";
		if(!felder.containsKey(key)){felder.put(key, new Feld());}
		return felder.get(key);
	}
	
	/**
	 * Baut ein Spielfeld aus den momentan vorhandenen Informationen
	 * 
	 */
	public Spielfeld createSpielfeld(){
		
		
		return null;
	}
	
	/**
	 * Bei einem Zugriff auf ein Feld sollte diese Funktion aufgerufen werden.
	 */
	private void feldZugriff(int reihe, int spalte){
		if(reihe<0 || spalte<0)throw new IllegalArgumentException("Ungültige Werte für Reihe und Spalte");
		if(reihe<minReihe || minReihe == -1)minReihe=reihe;
		if(reihe>maxReihe || maxReihe == -1)maxReihe=reihe;
		if(spalte<minSpalte || minSpalte == -1)minSpalte=reihe;
		if(spalte>maxSpalte || maxSpalte == -1)maxSpalte=reihe;
	}
	
}

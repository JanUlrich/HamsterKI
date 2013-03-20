import java.util.HashMap;

/**
 * Diese Klasse stellt ein 2D-Array mit Variabler größe dar.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class ZweiDimensionalesArray<E>{
    
    public ZweiDimensionalesArray(){
    }
    
	private int minReihe = -1;
	private int maxReihe = -1;
	private int minSpalte = -1;
	private int maxSpalte = -1;
	
	private HashMap<String,E> felder = new HashMap<String,E>();
	
	public void setFeld(int reihe, int spalte, E feld) {
		feldZugriff(reihe, spalte);
		String key = generateHashValue(reihe, spalte);
		felder.put(key, feld);
	}

	/**
	 * Gibt ein Feld mit den bisher auf diesem Feld eingetragenen Informationen zurück.
	 * Dieses kann anschließend editiert werden.
	 * @return - Das Feld an der Stelle reihe,spalte / Wenn kein Feld vorhanden, dann wird null zurückgegeben.
	*/
	public E getFeld(int reihe, int spalte){
		feldZugriff(reihe,spalte);
		String key = generateHashValue(reihe,spalte);
		return felder.get(key);
	}
	
	/**
	 * @return True, falls sich bereits ein Feld an Stelle reihe, spalte befindet - ansonsten false
	 */
	public boolean feldBelegt(int reihe, int spalte){
		String key = generateHashValue(reihe,spalte);
		return felder.containsKey(key);
	}
	
	public int getMinSpalte(){
		if(minSpalte==-1)return 0;
		return minSpalte;
	}
	
	public int getMaxSpalte(){
		if(maxSpalte==-1)return 0;
		return maxSpalte;
	}
	
	public int getMinReihe(){
		if(minReihe==-1)return 0;
		return minReihe;
	}
	
	public int getMaxReihe(){
		if(maxReihe==-1)return 0;
		return maxReihe;
	}
	
	/**
	 * Bei einem Zugriff auf ein Feld sollte diese Funktion aufgerufen werden.
	 * Wird benötigt um wichtige Variablen für die createSpielfeld-Methode zu setzen.
	 */
	private void feldZugriff(int reihe, int spalte){
		if(reihe<0 || spalte<0)throw new IllegalArgumentException("Ungültige Werte für Reihe ("+reihe+") und Spalte ("+spalte+")");
		if(reihe<minReihe || minReihe == -1)minReihe=reihe;
		if(reihe>maxReihe || maxReihe == -1)maxReihe=reihe;
		if(spalte<minSpalte || minSpalte == -1)minSpalte=reihe;
		if(spalte>maxSpalte || maxSpalte == -1)maxSpalte=reihe;
	}
	
	private String generateHashValue(int reihe, int spalte){
		return ""+reihe+","+spalte+"";
	}
}

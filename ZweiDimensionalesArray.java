import java.util.HashMap;
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class ZweiDimensionalesArray<E>{
    
    public ZweiDimensionalesArray(){
    }
    
	int minReihe = -1;
	int maxReihe = -1;
	int minSpalte = -1;
	int maxSpalte = -1;
	
	HashMap<String,E> felder = new HashMap<String,E>();

	/**
	 * Gibt ein Feld mit den bisher auf diesem Feld eingetragenen Informationen zurück.
	 * Dieses kann anschließend editiert werden.
	 * @return - Das Feld an der Stelle reihe,spalte
	*/
	public E getFeld(int reihe, int spalte){
		feldZugriff(reihe,spalte);
		String key = generateHashValue(reihe,spalte);
		return felder.get(key);
	}
	
	public boolean feldBelegt(int reihe, int spalte){
	String key = generateHashValue(reihe,spalte);
	return felder.containsKey(key);
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
		return ""+reihe+""+spalte+"";
	}
}

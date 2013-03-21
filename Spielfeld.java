
/**
 * SPielfeld. Wird zur Wegberechnung verwendet
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class Spielfeld {
    
    public int anzahlSpalten;
    public int anzahlReihen;
    
    public Position hamsterPos;
    
    private ZweiDimensionalesArray<WegberechnungsFeld> spielfeld;
    
    public Spielfeld(int width, int height, Position hamsterPosition){
    	anzahlSpalten = width;
    	anzahlReihen = height;
    	hamsterPos = hamsterPosition;
    }
    
    public Spielfeld(ZweiDimensionalesArray<WegberechnungsFeld> felder){
    	this.spielfeld = felder;
    	anzahlSpalten = spielfeld.getMaxSpalte();
    	anzahlReihen = spielfeld.getMaxReihe();
    }
    
    public int getKornChance(Position pos){
    	return spielfeld.getFeld(pos).getKornChance();
    }
    
    public int getWidth(){
    	return anzahlSpalten;
    }
    
    public int getHeight(){
    	return anzahlReihen;
    }
    
    public int getEnergieAufwand(Position pos){
    	return 1;
    }
    
    public void setEnergieAufwand(Position pos, int energie){
    }
    
    public int getChance(int reihe, int spalte){
    	return 0;
    }
    

   	
    /**
     * Wichtige Funktion für Wegberechnung.
     * prüft ob der übergebene Energiewert an der übergebenene Position auf dem Spielfeld einen besseren Wert als der bereits vorhandene darstellt.
     * Wichtig: ist das Spielfeld an der Position pos nicht begehbar, wird false zurückgegeben. Nicht begehbare Felder haben keinen Energiewert.
     * Wenn an der übergebenen Position noch kein Energiewert vorhanden ist, dann wird true zurückgegeben.
     */
    public boolean istBessererEnergiewert(Position pos, int energie){
    	WegberechnungsFeld feld = spielfeld.getFeld(pos);
    	return feld.energieaufwand[pos.direction]>energie;
    }
    
}

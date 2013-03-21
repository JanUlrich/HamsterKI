/**
 * Erweiterung eines normalen Feldes. Es können in dieser Klasse zusätzliche für die Wegberechnung erforderliche Informationen gespeichert werden.
*/
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class WegberechnungsFeld {
	private Feld feld;
    public int[] energieaufwand;
    private int koernerChance;//Prozentchance auf Körner
    
    public WegberechnungsFeld(Feld feld){
    	this.feld = feld;
    	energieaufwand = new int[4];
    	koernerChance = 50; //Initialwert für Körnerchance
    }
    
    public Feld getFeld(){
	    return feld;
    }
    
    public void setKoernerChance(int koerner) {
		this.koernerChance = koernerChance;
	}
	
	public int getKornChance(){
		if(this.getFeld().getMauer())return 0;
		return koernerChance;
	}
    
}

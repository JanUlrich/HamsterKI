/**
 * HamsterWegfindung berechnet die Energieaufwände für einen Hamster um
 * auf die einzelnen Felder des Spielfelds zu gelangen. Die Werte können für weitere AI-Operationen verwendet werden.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class HamsterWegfindung {
    Spielfeld spielfeld;
    
    /**
     * @param hamster - Die Ausgangslage des Hamsters. Dabei ist nur die Position und die Richtung des Hamsters relevant.
     * @param felder - Das Spielfeld. Hier sind alle bisher bekannten Informationen gespeichert.
     */
    public HamsterWegfindung(Hamster hamster, Spielfeld felder){
    	spielfeld = felder;
    }

	
	public void berechneEntfernungen(){
		
	}
	
	private void doStepfrom(int reihe, int spalte, int richtung){
		/*
		Probiere alle Richtungen durch. Falls ein Feld gefunden wird,
		auf dem der hamster noch nicht in entsprechender Richtung war oder hier ein neuer bester Energiewe
		*/
		
	}
}

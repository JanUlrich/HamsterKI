/**
 * HamsterWegfindung berechnet die Energieaufwände für einen Hamster um
 * auf die einzelnen Felder des Spielfelds zu gelangen. Die Werte können für weitere AI-Operationen verwendet werden.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class HamsterWegfindung {
    Spielfeld spielfeld;
    MeinHamster initialHamster;
    
    /**
     * @param hamster - Die Ausgangslage des Hamsters. Dabei ist nur die Position und die Richtung des Hamsters relevant.
     * @param felder - Das Spielfeld. Hier sind alle bisher bekannten Informationen gespeichert.
     */
    public HamsterWegfindung(MeinHamster hamster, Spielfeld felder){
    	spielfeld = felder;
    	initialHamster = hamster;
    }

	public int getBestMove(){
		berechneEntferungen();
	}
	
	private void berechneEntfernungen(){
		doStepfrom(initialHamster.getPosition());
	}
	
	private void doStepfrom(Position pos){
		/*
		Probiere alle Richtungen durch. Falls ein Feld gefunden wird,
		auf dem der hamster noch nicht in entsprechender Richtung war oder hier ein neuer bester Energiewe
		*/
		int momentaneEnergie = spielfeld.getEnergie(pos);
		for(int direction = 0; direction<4;direction++){
			int benoetigteEnergie = 2;

	    	// Schaut der Hamster schon in entsprechende Richtung, 
	    	// braucht er sich nicht mehr drehen
			if(pos.direction == direction)
				benoetigteEnergie = 1;
			
			if(spielfeld.istBessererEnergiewert(pos.getPositionInRichtung(direction), momentaneEnergie + benoetigteEnergie)){
				spielfeld.setEnergie(pos.getPositionInRichtung(direction), momentaneEnergie + benoetigteEnergie);
				doStepfrom(pos.getPositionInRichtung(direction)); // 
			}
		}
	}
}

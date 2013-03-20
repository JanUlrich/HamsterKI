import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class test1 implements de.hamster.model.HamsterProgram {public void main() {
	int gesamtAnzahlKoerner = Territorium.getAnzahlKoerner(); //Ermittelt zu Beginn des Programms die auf dem Spielfeld liegenden Körner ...
    MeinHamster hamster = new MeinHamster(5,5,0,0,3, 20); //Generiere den Hamster.
    
	mainLoop(hamster); //Tu Dinge
	    
	int uebrigeAnzahlKoerner = Territorium.getAnzahlKoerner(); //Ist die KI fertig (alle Körner wurden eingesammelt | Hamster hat keine Energie mehr), dann werden alle übrig gebliebenen Körnen ermittelt ...

    System.out.println("Ende");   // TODO Die Anzahl übrig gebliebener Körnern und andere Informationen kann am Ende als Informationen ausgegeben werden!
}

void mainLoop(MeinHamster hamster) {
    while(true) {
    	try {
	    	hamster.kiStep();
    	} catch(HamsterEnergieException e) {
    		return;
    	}
    }
}
}
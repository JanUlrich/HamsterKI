
/**
 * Ein Hamster mit zusätzlichen Funktionalitäten und begrenzter Energie.
 * Verbraucht für jede Drehung und jede Bewegung auf eine andere Kachel Energie.
 * Kann sich nur so lange bewegen, wie Energie vorhanden.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class MeinHamster extends Hamster {
	private Karte karte;
	private int energieLevel;
    
    public MeinHamster(int p1, int p2, int p3, int p4, int p5, int energie){
    	super(p1,p2,p3,p4,p5);
    	energieLevel = energie;
   		karte = new Karte();
    }

	public MeinHamster dreheInRichtung(int Richtung) throws HamsterEnergieException
	{
		// Sind die Parameter ungültig oder schaut der Hamster bereits 
		// in die gewünschte Richtung wird keine Energie verbraucht und 
		// keine Drehung ausgeführt.
		if(Richtung>3 || Richtung < 0 || this.getBlickrichtung() == Richtung)
			return this;
		
		while(this.getBlickrichtung()!=Richtung)
			this.linksUm();
		
		verbraucheEnergie();
		return this;
	}
	
	 /**
    @author Simon Herrmann
    @param karte Karte des Hamsters
    @return Karte des Hamsters mit nach der Sensorabfrage 
    */
    void KachelnAbfragen()
    {
    	int reihe = getReihe();
    	int spalte = getSpalte();
    	  	
    	for (int i = reihe-1; i < reihe + 2 ; i++)	
    	{
    		for (int j = spalte -1; j < spalte + 2 ; j++)		
    		{
    			if (Territorium.mauerDa(i,j))
    			{
    				karte.getFeld(i,j).setMauer(true);
    			}
    			else
    			{
    				karte.getFeld(i,j).setMauer(false);
    			}
    		}				
    	} 
    	
    	if(!kornDa())
			karte.getFeld(reihe, spalte).setKoerner(false);
    }
	
	public void vorwärts() throws HamsterEnergieException{
		vor();
		verbraucheEnergie();
	}
	
	private void verbraucheEnergie() throws HamsterEnergieException
	{
		if(energieLevel==0)
			throw new HamsterEnergieException();
		energieLevel--;
	}
    
    public Position getPosition(){
    	return new Position(this.getSpalte(), this.getReihe(), this.getBlickrichtung());
    }
    
    /**
     * Bewegt den Hamster KI gesteuert einen Schritt weiter
     */
    public void kiStep() throws HamsterEnergieException{
    	int richtung = berechneZug();
    	dreheInRichtung(richtung);
    	vor();
    	if(kornDa())
    		nimm();
    	KachelnAbfragen();
    }
    
    /**
     * @return - gibt die Richtung zurück
     */
    private int berechneZug(){
    	Spielfeld sp = karte.createSpielfeld();
    	HamsterWegfindung ki = new HamsterWegfindung(this, sp);
    	ki.berechneEntfernungen();
    	
    	if(vornFrei()) 
	    	return getBlickrichtung();
	    else
	    	return (getBlickrichtung()+1) % 4;
    }
    
}

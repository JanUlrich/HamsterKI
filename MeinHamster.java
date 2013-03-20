

import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class MeinHamster extends Hamster {
	private Karte karte;
	private int energieLevel;
    
    public MeinHamster(int p1, int p2, int p3, int p4, int p5){
    	super(p1,p2,p3,p4,p5);
   		karte = new Karte();
    }

	public MeinHamster dreheInRichtung(int Richtung) throws HamsterEnergieException
	{
		//Sind die Parameter ungültig oder schaut der Hamster bereits in die gewünschte Richtung wird keine Energie verbraucht und keine Drehung ausgeführt.
		if(Richtung>3 || Richtung < 0 || this.getBlickrichtung() == Richtung)return this;
		//
		while(this.getBlickrichtung()!=Richtung){
			this.linksUm();
		}
		verbraucheEnergie();
		return this;
	}
	
	@Override
	public void vor(){
		
	}
	
	public void vorwärts() throws HamsterEnergieException{
		super.vor();
		verbraucheEnergie();
	}
	
	private void verbraucheEnergie() throws HamsterEnergieException
	{
		if(energieLevel==0)throw new HamsterEnergieException();
		energieLevel--;
	}
    
    public Position getHamsterPosition(){
    	return new Position(this.getSpalte(), this.getReihe(), this.getBlickrichtung());
    }
    
    /**
     * Bewegt den Hamster KI gesteuert einen Schritt weiter
     */
    public void kiStep() throws HamsterEnergieException{
    	int richtung = berechneZug();
    	dreheInRichtung(richtung);
    	vor();
    }
    
    /**
     * @return - gibt die Richtung zurück
     */
    private int berechneZug(){
    	Spielfeld sp = karte.createSpielfeld();
    	HamsterWegfindung ki = new HamsterWegfindung(this, sp);
    	ki.berechneEntfernungen();
    	
    	return 0;
    }
    
}

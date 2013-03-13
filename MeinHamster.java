

import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class MeinHamster extends Hamster {
	private Karte spielfeld;
	private int energieLevel;
    
    public MeinHamster(int p1, int p2, int p3, int p4){
    	super(p1,p2,p3,p4);
   		spielfeld = new Karte();
    }

	public MeinHamster dreheInRichtung(int Richtung) throws HamsterEnergieException
	{
		if(Richtung>3 || Richtung < 0)return this;
		while(this.getBlickrichtung()!=Richtung){
			this.linksUm();
		}
		verbraucheEnergie();
		return this;
	}
	
	private void verbraucheEnergie() throws HamsterEnergieException
	{
		if(energieLevel==0)throw new HamsterEnergieException();
		energieLevel--;
	}
    
}

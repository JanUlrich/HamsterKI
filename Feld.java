import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class Feld {
    private boolean mauer;
    private int koerner;//Prozentchance auf Körner
	
	public Feld(){
		this.mauer = false;
		this.koerner = 25;
	}
	
	public Feld(boolean mauer, int koerner){
		this.mauer = mauer;
		this.koerner = koerner;
	}

	public void setMauer(boolean mauer){
		this.mauer = mauer;
	}
	
	public boolean getMauer() {
		return this.mauer;
	}
	
	public void setKoerner(int koerner) {
		this.koerner = koerner;
	}
	
	public int getKornChance(){
		if(mauer)return 0;
		return koerner;
	}
}

import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class Feld {
    private boolean mauer;
    private boolean koerner;
	
	public Feld(){
		this(false,true);
	}
	
	public Feld(boolean mauer){
		this(mauer,true);
	}
	
	public Feld(boolean mauer, boolean koerner){
		this.mauer = mauer;
		this.koerner = koerner;
	}

	public void setMauer(boolean mauer){
		this.mauer = mauer;
	}
	
	public boolean getMauer(){
		return mauer;
	}
	
	public void setKoerner(boolean koerner){
		this.koerner = koerner;
	}
}

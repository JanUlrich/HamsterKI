import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class Position {
    public int x;
    public int y;
    public int direction;
    
    public Position(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    public Position(int x, int y, int direction){
    	this(x,y);
    	this.direction = direction;
    }
}

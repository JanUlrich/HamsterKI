import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class Position {
    public int x;
    public int y;
    public int direction;
    
    public Position(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    /**
     * @param x - die Nummer der Spalte
     * @param y - die Nummer der Reihe
     */
    public Position(int x, int y, int direction){
    	this(x,y);
    	this.direction = direction;
    }
    
    /**
     * Gibt eine neue Position zurück. Diese liegt an der Position, welche in Richtung direction an die ursprüngliche Position anliegt.
     * @param direction - direction
     */
    public Position getPositionInRichtung(int direction){
    	return new Position(0,0);
    }
}

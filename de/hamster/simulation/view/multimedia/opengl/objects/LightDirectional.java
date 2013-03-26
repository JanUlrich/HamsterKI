package de.hamster.simulation.view.multimedia.opengl.objects;

/**
 * @author chris
 *
 * Direktionales Licht, hat keine Position, sondern eine Richtung, aus der es scheint.
 */

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;

public class LightDirectional extends LightAbstract {
	
	public LightDirectional(int id, String name) {
		super(id, name);			
	}
	
	public void enable(GLDrawable gld) {
                        
		System.out.println("enable light #" + this.glLightId);
		
		GL gl = gld.getGL();
		
		gl.glLightfv( GL.GL_LIGHT0 + this.glLightId, GL.GL_AMBIENT,  this.ambient.getData()  );
		gl.glLightfv( GL.GL_LIGHT0 + this.glLightId, GL.GL_DIFFUSE,  this.diffuse.getData()  );

		this.pos[3] = 0f; // immer 0 bei directional
		gl.glLightfv(GL.GL_LIGHT0 + this.glLightId, GL.GL_POSITION, this.pos );
	    
	     //Enable the first light and the lighting mode
	     gl.glEnable(GL.GL_LIGHT0 + this.glLightId);
	     gl.glEnable(GL.GL_LIGHTING);
	     this.isUsed=true;			

	}





}

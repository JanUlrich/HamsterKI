package de.hamster.simulation.view.multimedia.opengl.objects;

/**
 * @author chris
 * 
 * Positionales Licht, das ein einer gegebenen Position sitzt und von da in alle
 * Richtungen strahlt.
 * 
 */

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;

public class LightPositional extends LightAbstract {

	protected float   constantAttenuation  = 0.8f;
	protected float   linearAttenuation    = 0.0f;
	protected float   quadraticAttenuation = 0.0f;
	
	public LightPositional(int id, String name) {
		super(id, name);		
	}
	
	public void enable(GLDrawable gld) {
		GL gl = gld.getGL();
					
		gl.glLightfv( GL.GL_LIGHT0 + this.glLightId, GL.GL_AMBIENT,  this.ambient.getData()  );
		gl.glLightfv( GL.GL_LIGHT0 + this.glLightId, GL.GL_DIFFUSE,  this.diffuse.getData()  );

		this.pos[3] = 1f;// immer für positional...

		gl.glLightfv( GL.GL_LIGHT0 + this.glLightId, GL.GL_POSITION, this.pos );

		gl.glLightf (GL.GL_LIGHT0 + this.glLightId, GL.GL_CONSTANT_ATTENUATION,  this.constantAttenuation  );
		gl.glLightf (GL.GL_LIGHT0 + this.glLightId, GL.GL_LINEAR_ATTENUATION,    this.linearAttenuation    );
		gl.glLightf (GL.GL_LIGHT0 + this.glLightId, GL.GL_QUADRATIC_ATTENUATION, this.quadraticAttenuation );

		gl.glEnable(GL.GL_LIGHT0 + this.glLightId);
		gl.glEnable(GL.GL_LIGHTING);
		this.isUsed=true;
	

		
	}

	public float getConstantAttenuation() {
		return constantAttenuation;
	}

	public void setConstantAttenuation(float constantAttenuation) {
		this.constantAttenuation = constantAttenuation;
	}

	public float getLinearAttenuation() {
		return linearAttenuation;
	}

	public void setLinearAttenuation(float linearAttenuation) {
		this.linearAttenuation = linearAttenuation;
	}

	public float getQuadraticAttenuation() {
		return quadraticAttenuation;
	}

	public void setQuadraticAttenuation(float quadraticAttenuation) {
		this.quadraticAttenuation = quadraticAttenuation;
	}
		

}

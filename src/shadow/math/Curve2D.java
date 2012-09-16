/*
	Alessandro Martinelli's Jogl Tutorial
    Copyright (C) 2008  Alessandro Martinelli  <alessandro.martinelli@unipv.it>

    This file is part of Alessandro Martinelli's Jogl Tutorial.

    Alessandro Martinelli's Jogl Tutorial is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Alessandro Martinelli's Jogl Tutorial is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Alessandro Martinelli's Jogl Tutorials.  If not, see <http://www.gnu.org/licenses/>.
 */

package shadow.math;

import javax.media.opengl.GL;



abstract class Curve3D {

	public abstract float getTMin();
	public abstract float getTMax();
	
	public abstract float getX(float t);
	public abstract float getY(float t);
	public abstract float getZ(float t);
	
	public abstract float getDXDt(float t);
	public abstract float getDYDt(float t);
	
	public void draw(GL gl,int DIVISIONS){
		float step=(getTMax()-getTMin())/DIVISIONS;
		gl.glBegin(GL.GL_LINE_STRIP);
			for(int i=0;i<=DIVISIONS;i++){
				float t=getTMin()+step*i;
				gl.glVertex3f(getX(t),getY(t),getZ(t));	
			}
		gl.glEnd();
	}
}
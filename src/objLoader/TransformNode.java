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

package objLoader;

import java.util.ArrayList;

import javax.media.opengl.GL;

public class TransformNode implements Drawable{

	private float tx,ty,tz;
	private float sx=1,sy=1,sz=1;
	private float rx,ry=1,rz,ralpha;
	
	protected ArrayList<Drawable> drawables=new ArrayList<Drawable>();
	private boolean initialized=false;
	
	public TransformNode(float tx, float ty, float tz, float sx, float sy, float sz, float rx, float ry, float rz, float ralpha) {
		super();
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.ralpha = ralpha;
	}
	
	
	
	public TransformNode() {
		super();
	}



	public void setTranslate(float tx,float ty,float tz){
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}

	public void setScale(float sx,float sy,float sz){
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	public void setRotate(float alpha,float rx,float ry,float rz){
		this.ralpha = alpha;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public void draw(GL gl) {
		
		gl.glPushMatrix();
			gl.glTranslatef(tx,ty,tz);
			gl.glRotatef(ralpha,rx,ry,rz);
			gl.glScalef(sx,sy,sz);
				for(int i=0;i<drawables.size();i++){
					(drawables.get(i)).draw(gl);
				}
			// IN ALTERNATIVA AL PUSH E POP POSSO ANTI-TRASFORMARE:
			//gl.glScalef(1.0f/sx,1.0f/sy,1.0f/sz); 
			//gl.glRotatef(-ralpha,rx,ry,rz);
			//gl.glTranslatef(-tx,-ty,tz);
		gl.glPopMatrix();
	}

	public void init(GL gl) {
		initialized=true;
		for(int i=0;i<drawables.size();i++){
			(drawables.get(i)).init(gl);
		}
	}

	public void addDrawable(Drawable d){
		if(!initialized)
			drawables.add(d);
	}

	public float getRalpha() {
		return ralpha;
	}


	public void setRalpha(float ralpha) {
		this.ralpha = ralpha;
	}


	public float getRx() {
		return rx;
	}


	public void setRx(float rx) {
		this.rx = rx;
	}


	public float getRy() {
		return ry;
	}


	public void setRy(float ry) {
		this.ry = ry;
	}


	public float getRz() {
		return rz;
	}


	public void setRz(float rz) {
		this.rz = rz;
	}


	public float getSx() {
		return sx;
	}


	public void setSx(float sx) {
		this.sx = sx;
	}


	public float getSy() {
		return sy;
	}


	public void setSy(float sy) {
		this.sy = sy;
	}


	public float getSz() {
		return sz;
	}


	public void setSz(float sz) {
		this.sz = sz;
	}


	public float getTx() {
		return tx;
	}


	public void setTx(float tx) {
		this.tx = tx;
	}


	public float getTy() {
		return ty;
	}


	public void setTy(float ty) {
		this.ty = ty;
	}


	public float getTz() {
		return tz;
	}


	public void setTz(float tz) {
		this.tz = tz;
	}
	
	
	
	
	
}

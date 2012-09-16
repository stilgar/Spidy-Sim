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

import javax.media.opengl.GL;

import shadow.math.Vertex3f;


public class DisplayListObject implements Drawable{

	private boolean useGeometry[]=new boolean[0];
	private int objPosition;
	private Vertex3f colorsDiff[]=new Vertex3f[0];
	private Vertex3f colorsSpec[]=new Vertex3f[0];
	private float shininess[]=new float[0];
	
	SimpleObjFile file;
	
	public DisplayListObject(String fileName) {
		super();
		file=SimpleObjFile.getFromFile(fileName);
		
		useGeometry=new boolean[file.getGeometriesNumber()];
		colorsDiff=new Vertex3f[file.getGeometriesNumber()];
		colorsSpec=new Vertex3f[file.getGeometriesNumber()];
		shininess=new float[file.getGeometriesNumber()];
		
		for(int i=0;i<file.getGeometriesNumber();i++){
			useGeometry[i]=true;
			colorsDiff[i]=new Vertex3f(1,1,1);
			colorsSpec[i]=new Vertex3f(0,0,0);
		}
	}

	public void draw(GL gl) {
		
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
		
		for(int i=0;i<useGeometry.length;i++){
			if(useGeometry[i])
				gl.glCallList(objPosition+i);
		}
	}

	public void init(GL gl) {
		
		objPosition=gl.glGenLists(file.getGeometriesNumber());
		
		for(int i=0;i<file.getGeometriesNumber();i++){
			
			gl.glNewList(objPosition+i, GL.GL_COMPILE);
				float specColor[]= {colorsSpec[i].x,colorsSpec[i].y,colorsSpec[i].z,1};
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR, specColor,0);
				gl.glMaterialf(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS, shininess[i]);
				gl.glColor3f(colorsDiff[i].x,colorsDiff[i].y,colorsDiff[i].z);
				file.drawGeometry(gl, i);
			gl.glEndList();
		}
	}
	
	public void setUseGeometry(int index,boolean use){
		useGeometry[index]=false;
	}

	public int getGeometriesNumber(){
		return useGeometry.length;
	}
	
	public void setDiffuseColor(int index,float red,float green,float blue){
		colorsDiff[index].set(red, green, blue);
	}
	
	public void setSpecularColor(int index,float red,float green,float blue){
		colorsSpec[index].set(red, green, blue);
	}
	
	public void setShininess(int index,float shininess){
		this.shininess[index]=shininess;
	}
	
}

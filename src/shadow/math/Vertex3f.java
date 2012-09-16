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


/**
 * A vertex in the 3D space given the x,y,z coordinates
 * 
 * @author Alessandro Martinelli
 */
public class Vertex3f {

	public float x,y,z;

	/**
	 * @param x the first coordinate
	 * @param y the second coordinate
	 * @param z the third coordinate
	 */
	public Vertex3f(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Construct a simple Vertex with coordinate (0,0,0)
	 */
	public Vertex3f() {
		super();
	}
	
	/**
	 * Set this Vertex Coordinates to be equal to the coordinate of another Vertex
	 * */
	public void set(Vertex3f toSet){
		this.x=toSet.x;
		this.y=toSet.y;
		this.z=toSet.z;
	}

	protected Object clone() throws CloneNotSupportedException {
		Vertex3f b=new Vertex3f(x,y,z);
		return b;
	}
	
	public String toString(){
		return "x:"+x+",y:"+y+",z:"+z;
	}
	
	/**
	 * Set the coordinate of this Vertex by an index
	 * @param index the inde of the coordinate: 0,X; 1,Y; 2,Z;
	 * @param val The val to set
	 * */
	public void setByIndex(int index,float val){
		if(index==0)
			x=val;
		else if(index==1)
			y=val;
		else if(index==2)
			z=val;
	}
	
	/**
	 * Set this Vertex Coordinates to be the sum of its coordinate and the coordinate in the adding Vertex
	 * */
	public void add(Vertex3f adding){
		this.x+=adding.x;
		this.y+=adding.y;
		this.z+=adding.z;
	}
	
	/**
	 * Set this Vertex Coordinates to be the difference between its coordinate and the coordinate in the adding Vertex
	 * */
	public void subtract(Vertex3f v){
		x-=v.x;
		y-=v.y;
		z-=v.z;
	}
	
	/**
	 * Set this Vertex Coordinates to be the difference between its coordinate and the coordinate in the adding Vertex
	 * */
	public void mult(float a){
		x*=a;
		y*=a;
		z*=a;
	}
	
	/**
	 * return a coordinate given the index 
	 * @param index the inde of the coordinate: 0,X; 1,Y; 2,Z;
	 * */
	public float getByIndex(int index){
		if(index==0)
			return x;
		else if(index==1)
			return y;
		
		return z;
	}

	/**
	 * Set all the coordinates of the Vertex3f
	 */
	public void set(float x,float y,float z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * Set all the coordinates of the Vertex3f only if the vector has length 3
	 */
	public void set(float vector[]){
		if(vector.length==3){
			x=vector[0];
			y=vector[1];
			z=vector[2];
		}
	}
	
	/**
	 * Return the coordinates of the Vertex3f in a single float vector with length 3
	 */
	public float[] get(){
		float f[]=new float[3];
		f[0]=x;
		f[1]=y;
		f[2]=z;
		return f;
	}
	
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}

	
}

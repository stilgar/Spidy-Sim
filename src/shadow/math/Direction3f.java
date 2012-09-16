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
 * A set of 3 coordinates with the same properties of a Vertex3f but with intended to be used as a direction
 * */
public class Direction3f extends Vertex3f{
	
	/**
	 * Construct the default direction (1,0,0)
	 */
	public Direction3f() {
		super();
		setX(1);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Direction3f(float x, float y, float z) {
		super(x, y, z);
	}

	/**
	 * Normalize this Direction
	 * */
	public void normalize(){
		float length=getLength();
		mult(1.0f/length);
	}

	/**
	 * Eval the length of this Direction
	 * */
	public float getLength(){
		return (float)(Math.sqrt(getLength2()));
	}
	
	/**
	 * Produces one direction normal to this one in a robust manner.
	 * */
	public Direction3f getNormalCandidate(){
		float xa=1,ya=0,za=0;
		float x=getX(),y=getY(),z=getZ();
		float projection=x*xa+y*ya+z*za;
		if(projection>0.9f){
			xa=0;
			ya=1;
			za=0;
			projection=x*xa+y*ya+z*za;
		}
		xa-=x*projection;
		ya-=y*projection;
		za-=z*projection;
		return new Direction3f(xa,ya,za);
	}
	
	/**
	 * Produces the Vector Product of this Direction with a second Operator
	 * */
	public Direction3f getVectorProduct(Direction3f secondOperand){
		
		float xa=getX(),ya=getY(),za=getZ();
		float xb=secondOperand.getX(),yb=secondOperand.getY(),zb=secondOperand.getZ();
		
		float x=ya*zb-yb*za;
		float y=za*xb-zb*xa;
		float z=xa*yb-xb*ya;
		
		return new Direction3f(x,y,z);
	}
	
	/**
	 * Return the scalar product of this Direction with Another
	 * */
	public float getScalarProduct(Direction3f secondOperand){
		return getX()*secondOperand.getX()+
				getY()*secondOperand.getY()+
				getZ()*secondOperand.getZ();
	}
	
	/**
	 * Eval the square of the length of this Direction
	 * */
	public float getLength2(){
		float x=getX();
		float y=getY();
		float z=getZ();
		
		return x*x+y*y+z*z;
	}
}

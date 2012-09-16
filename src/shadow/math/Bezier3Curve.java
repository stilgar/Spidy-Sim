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


public class Bezier3Curve extends Curve3D{

	private Vertex3f A,B,C;
	
	public Bezier3Curve(Vertex3f a, Vertex3f b, Vertex3f c) {
		super();
		A = a;
		B = b;
		C = c;
	}

	public float getDXDt(float t) {
		float tm=1-t;
		return A.x*(-2*tm)+B.x*(2-4*t)+C.x*(2*t);
	}

	public float getDYDt(float t) {
		float tm=1-t;
		return A.x*(-2*tm)+B.x*(2-4*t)+C.x*(2*t);
	}

	public float getTMax() {
		return 1;
	}

	public float getTMin() {
		return 0;
	}

	public float getX(float t) {
		float tm=1-t;
		return A.x*tm*tm+B.x*2*tm*t+C.x*t*t;
	}

	public float getY(float t) {
		float tm=1-t;
		return A.y*tm*tm+B.y*2*tm*t+C.y*t*t;
	}

	public float getZ(float t) {
		float tm=1-t;
		return A.z*tm*tm+B.z*2*tm*t+C.z*t*t;
	}

}

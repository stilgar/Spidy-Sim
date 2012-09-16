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

public class ObjFace {
	private int verteces[];
	private int normals[];
	private int texCoord[];
	public ObjFace(int[] verteces, int[] normals, int[] texCoord) {
		super();
		this.verteces = verteces;
		this.normals = normals;
		this.texCoord = texCoord;
	}
	public int[] getNormals() {
		return normals;
	}
	public int[] getTexCoord() {
		return texCoord;
	}
	public int[] getVerteces() {
		return verteces;
	}
}

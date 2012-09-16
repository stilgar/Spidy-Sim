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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.media.opengl.GL;

import shadow.math.Vertex3f;

public class SimpleObjFile {

	private Vertex3f[] vertexes;
	private Vertex3f[] normals;
	private Vertex3f[] texCoord;

	private ObjGeometry[] geometries;
	private String name;
	private boolean normalsB, textsB;

	public static SimpleObjFile getFromFile(String filename) {

		SimpleObjFile obj = new SimpleObjFile();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filename)));

			ArrayList<Vertex3f> vertices = new ArrayList<Vertex3f>();
			ArrayList<Vertex3f> normals = new ArrayList<Vertex3f>();
			ArrayList<Vertex3f> texCoord = new ArrayList<Vertex3f>();
			ArrayList<ObjFace> faces = new ArrayList<ObjFace>();
			ArrayList<ObjGeometry> geometries = new ArrayList<ObjGeometry>();

			String line = reader.readLine();

			while (line != null) {
				StringTokenizer tok = new StringTokenizer(line, " ");
				if (tok.hasMoreTokens()) {
					String identifier = tok.nextToken().trim();
					if (identifier.equalsIgnoreCase("v")) {
						addVertexToList(vertices, tok);
					}
					if (identifier.equalsIgnoreCase("vt")) {
						addVertexToList(texCoord, tok);
					}
					if (identifier.equalsIgnoreCase("vn")) {
						addVertexToList(normals, tok);
					}
					if (identifier.equalsIgnoreCase("o")) {
						if (faces.size() != 0) {
							geometries.add(getGeometry(faces));
							faces.clear();
						}
						obj.name = tok.nextToken().trim();
					}
					if (identifier.equalsIgnoreCase("g")) {// Nuova Geometria
						if (faces.size() != 0) {
							geometries.add(getGeometry(faces));
							faces.clear();
						}
					}
					if (identifier.equalsIgnoreCase("f")) {
						addObjFaceToList(faces, tok);
					}
				}
				line = reader.readLine();
			}

			if (faces.size() != 0) {
				geometries.add(getGeometry(faces));
				faces.clear();
			}

			ObjGeometry[] geoemtries = new ObjGeometry[geometries.size()];
			for (int i = 0; i < geoemtries.length; i++)
				geoemtries[i] = (ObjGeometry) geometries.get(i);

			Vertex3f[] vertexes = new Vertex3f[vertices.size()];
			for (int i = 0; i < vertices.size(); i++)
				vertexes[i] = (Vertex3f) vertices.get(i);

			Vertex3f[] normal = new Vertex3f[normals.size()];
			for (int i = 0; i < normals.size(); i++)
				normal[i] = (Vertex3f) normals.get(i);

			Vertex3f[] txC = new Vertex3f[texCoord.size()];
			for (int i = 0; i < texCoord.size(); i++)
				txC[i] = (Vertex3f) texCoord.get(i);

			obj.vertexes = vertexes;
			obj.normals = normal;
			obj.texCoord = txC;
			obj.geometries = geoemtries;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}

	private static void addFaceToList(ArrayList<?> face, StringTokenizer tok) {
		ArrayList<?> vertexes = new ArrayList<Object>();
		while (tok.hasMoreTokens()) {
			String vertex = tok.nextToken();
			StringTokenizer fTok = new StringTokenizer(vertex, "/");
			int vx = 0, normal = 0, tex = 0;
			if (fTok.hasMoreTokens()) {
				vx = new Integer(fTok.nextToken()).intValue();
			}
			if (fTok.hasMoreTokens()) {
				normal = new Integer(fTok.nextToken()).intValue();
			}
			if (fTok.hasMoreTokens()) {
				tex = new Integer(fTok.nextToken()).intValue();
			}
		}
	}

	private static void addObjFaceToList(ArrayList<ObjFace> face,
			StringTokenizer tok) {

		// System.out.println("Begin token!!! ");

		int size = tok.countTokens();

		int vertex[] = new int[size];
		int normals[] = new int[size];
		int texCoord[] = new int[size];

		int index = 0;

		while (tok.hasMoreElements()) {

			StringTokenizer token = new StringTokenizer(tok.nextToken(), "/");

			if (token.hasMoreElements()) {
				vertex[index] = ((new Integer(token.nextToken())).intValue());
				// System.out.println("Vertex !! "+vertex[index]);
			}
			if (token.hasMoreElements()) {
				texCoord[index] = ((new Integer(token.nextToken())).intValue());
				// System.out.println("TexCoord !! "+texCoord[index]);
			}
			if (token.hasMoreElements()) {
				normals[index] = ((new Integer(token.nextToken())).intValue());
				// System.out.println("Normal !! "+normals[index]);
			} else {
				normals[index] = texCoord[index];
				texCoord[index] = 0;
			}

			index++;
		}
		ObjFace fac = new ObjFace(vertex, normals, texCoord);
		face.add(fac);

		// System.out.println("End token!!! ");
	}

	private static void addVertexToList(ArrayList<Vertex3f> list,
			StringTokenizer tok) {
		Vertex3f v = new Vertex3f();
		try {
			v.x = new Float(tok.nextToken()).floatValue();
			v.y = new Float(tok.nextToken()).floatValue();
			v.z = new Float(tok.nextToken()).floatValue();
		} catch (Exception e) {
		}
		list.add(v);
	}

	private static ObjGeometry getGeometry(ArrayList<ObjFace> faces) {
		ObjFace[] fs = new ObjFace[faces.size()];
		for (int i = 0; i < fs.length; i++) {
			fs[i] = (ObjFace) (faces.get(i));
		}
		ObjGeometry geoemtry = new ObjGeometry(fs);
		return geoemtry;
	}

	public int getGeometriesNumber() {
		return geometries.length;
	}

	public void draw(GL gl) {
		for (int i = 0; i < geometries.length; i++)
			drawGeometry(gl, i);
	}

	public void drawGeometry(GL gl, int index) {

		if (index >= 0 && index < geometries.length) {
			ObjGeometry geometry = geometries[index];

			boolean useNormals = false;
			if (this.normals.length != 0)
				useNormals = true;
			boolean useTexCoord = false;
			if (this.texCoord.length != 0)
				useTexCoord = true;

			gl.glBegin(GL.GL_TRIANGLES);

			for (int i = 0; i < geometry.getFaces().length; i++) {
				ObjFace f = geometry.getFaces()[i];
				int indexes[] = f.getVerteces();
				int normals[] = f.getNormals();
				int texCoord[] = f.getTexCoord();

				// System.err.println("index "+indexes.length);
				// System.err.println("normals "+normals.length+" - "+normals[0]);
				// System.err.println("texCoord "+texCoord.length);
				// System.err.println("index "+this.vertices.length);
				// System.err.println("normals "+this.normals.length);
				// System.err.println("texCoord "+this.texCoord.length);

				Vertex3f v = vertexes[indexes[0] - 1];
				Vertex3f n = null;
				if (useNormals && normals[0] != 0)
					n = this.normals[normals[0] - 1];
				Vertex3f tx = null;
				if (useTexCoord && texCoord[0] != 0)
					tx = this.texCoord[texCoord[0] - 1];

				for (int j = 1; j < indexes.length - 1; j++) {

					if (n != null)
						gl.glNormal3f(n.x, n.y, n.z);
					if (tx != null)
						gl.glTexCoord2f(tx.x, tx.y);
					gl.glVertex3f(v.x, v.y, v.z);

					if (n != null) {
						Vertex3f nTemp = this.normals[normals[j] - 1];
						gl.glNormal3f(nTemp.x, nTemp.y, nTemp.z);
					}
					if (tx != null) {
						Vertex3f txTemp = this.texCoord[texCoord[j] - 1];
						gl.glTexCoord2f(txTemp.x, txTemp.y);
					}
					Vertex3f vTemp = vertexes[indexes[j] - 1];
					gl.glVertex3f(vTemp.x, vTemp.y, vTemp.z);

					if (n != null) {
						Vertex3f nTemp = this.normals[normals[j + 1] - 1];
						gl.glNormal3f(nTemp.x, nTemp.y, nTemp.z);
					}
					if (tx != null) {
						Vertex3f txTemp = this.texCoord[texCoord[j + 1] - 1];
						gl.glTexCoord2f(txTemp.x, txTemp.y);
					}
					vTemp = vertexes[indexes[j + 1] - 1];
					gl.glVertex3f(vTemp.x, vTemp.y, vTemp.z);
				}
			}

			gl.glEnd();
		}
	}

	public void drawOn(TriangleListener listener) {
		for (int i = 0; i < geometries.length; i++)
			drawGeometryOn(listener, i);
	}

	public void drawGeometryOn(TriangleListener listener, int index) {
		if (index >= 0 && index < geometries.length) {
			ObjGeometry geometry = geometries[index];

			boolean useNormals = false;
			if (this.normals.length != 0)
				useNormals = true;
			boolean useTexCoord = false;
			if (this.texCoord.length != 0)
				useTexCoord = true;

			listener.begin();
			Vertex3f A = new Vertex3f();
			Vertex3f B = new Vertex3f();
			Vertex3f C = new Vertex3f();
			Vertex3f An = new Vertex3f();
			Vertex3f Bn = new Vertex3f();
			Vertex3f Cn = new Vertex3f();
			Vertex3f At = new Vertex3f();
			Vertex3f Bt = new Vertex3f();
			Vertex3f Ct = new Vertex3f();

			for (int i = 0; i < geometry.getFaces().length; i++) {
				ObjFace f = geometry.getFaces()[i];
				int indexes[] = f.getVerteces();
				int normals[] = f.getNormals();
				int texCoord[] = f.getTexCoord();

				Vertex3f v = vertexes[indexes[0] - 1];
				A.set(v);
				Vertex3f n = null;
				if (useNormals) {
					n = this.normals[normals[0] - 1];
					An.set(n);
				}
				Vertex3f tx = null;
				if (useTexCoord) {
					tx = this.texCoord[texCoord[0] - 1];
					At.set(n);
				}

				int j = 1;
				if (useNormals) {
					Vertex3f nTemp = this.normals[normals[j] - 1];
					Bn.set(nTemp);
				}
				if (useTexCoord) {
					Vertex3f txTemp = this.texCoord[texCoord[j] - 1];
					Bt.set(txTemp);
				}
				Vertex3f vTemp = vertexes[indexes[j] - 1];
				B.set(vTemp);
				if (useNormals) {
					Vertex3f nTemp = this.normals[normals[j + 1] - 1];
					Cn.set(nTemp);
				}
				if (useTexCoord) {
					Vertex3f txTemp = this.texCoord[texCoord[j + 1] - 1];
					Ct.set(txTemp);
				}
				vTemp = vertexes[indexes[j + 1] - 1];
				C.set(vTemp);

				listener.sendTriangle(A, B, C, An, Bn, Cn, At, Bt, Ct);

				if (indexes.length == 4) {
					B.set(C);
					Bn.set(Cn);
					Bt.set(Ct);
					j = 3;
					if (useNormals) {
						Vertex3f nTemp = this.normals[normals[j] - 1];
						Cn.set(nTemp);
					}
					if (useTexCoord) {
						Vertex3f txTemp = this.texCoord[texCoord[j] - 1];
						Ct.set(txTemp);
					}
					vTemp = vertexes[indexes[j] - 1];
					C.set(vTemp);
					listener.sendTriangle(A, B, C, An, Bn, Cn, At, Bt, Ct);
				}

			}

			listener.end();
		}
	}

	public void stamp() {
		for (int i = 0; i < geometries.length; i++) {
			ObjGeometry geometry = geometries[i];
			for (int j = 0; j < geometry.getFaces().length; j++) {
				ObjFace f = geometry.getFaces()[j];
				System.out.println();
				for (int k = 0; k < f.getNormals().length; k++) {
					System.out.print(" " + f.getVerteces()[k] + " "
							+ f.getNormals()[k] + " " + f.getTexCoord()[k]);
				}
				System.out.println();
			}
		}
	}

	public Vertex3f[] getNormals() {
		return normals;
	}

	public void setNormals(Vertex3f[] normals) {
		this.normals = normals;
	}

	public Vertex3f[] getTexCoord() {
		return texCoord;
	}

	public void setTexCoord(Vertex3f[] texCoord) {
		this.texCoord = texCoord;
	}

	public Vertex3f[] getVertexes() {
		return vertexes;
	}

	public void setVertexes(Vertex3f[] vertexes) {
		this.vertexes = vertexes;
	}

}

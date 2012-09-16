/*
 * Spidy Simulator Copyright (C) 2012 Nicolàs Alejandro Di Risio
 * <nicolas@dirisio.net>
 * 
 * This file is part of Spidy Simulator.
 * 
 * Spidy Simulator is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Spidy Simulator is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Spidy Simulator. If not, see <http://www.gnu.org/licenses/>.
 */
package sim;

import objLoader.SimpleObjFile;
import shadow.math.Vertex3f;

/* @author Nicolàs Alejandro Di Risio */
public class LegSkinning {
	SimpleObjFile file; /* contains all vertex already morphed */
	Vertex3f fileVertexData[]; /* contains all original vertex positions */
	Vertex3f fileNormalsData[]; /* contains all original normals */
	private double[] anglesRad; /* angles to use in morphing */
	private float yOffset; /* below it, object model won't be drawn */
	private float legLengths[]; /* leg lengths to use in morphing stage */
	int side; /* 1 right side, -1 left side */

	/*
	 * Initialization, it saves original position of each point. It needs a
	 * model that has to be extended on Y axe
	 */
	public LegSkinning(SimpleObjFile file, int side, float yOffset,
			float[] legLengths) {
		this.file = file;
		/* Allocation of original data */
		fileVertexData = new Vertex3f[file.getVertexes().length];
		fileNormalsData = new Vertex3f[file.getNormals().length];
		for (int i = 0; i < fileVertexData.length; i++) {
			fileVertexData[i] = new Vertex3f();
			fileVertexData[i].set(file.getVertexes()[i]);
		}
		for (int i = 0; i < fileNormalsData.length; i++) {
			fileNormalsData[i] = new Vertex3f();
			fileNormalsData[i].set(file.getNormals()[i]);
		}
		/* setting parameters */
		anglesRad = new double[3];
		this.yOffset = yOffset;
		this.legLengths = legLengths;
		this.side = side;
	}

	/* Returns true only if morphing process has gone well */
	public boolean updateFile(double reductionCoefficient) {
		/* Vertex Morphing */
		for (int i = 0; i < fileVertexData.length; i++) {
			file.getVertexes()[i].set(morph(fileVertexData[i],
					reductionCoefficient));
		}

		/* Normals Morphing */
		for (int i = 0; i < fileNormalsData.length; i++) {
			file.getNormals()[i].set(morph(fileNormalsData[i],
					reductionCoefficient));
		}
		
		return true;
	}

	/* This is a reduction of proportions to adjust model */
	private Vertex3f reduced(Vertex3f point, double reductionCoefficient) {
		return new Vertex3f((float) (point.x / reductionCoefficient),
				(float) (point.y / reductionCoefficient),
				(float) (point.z / reductionCoefficient));
	}

	/* It saves angles of 3 engines */
	public void setAngles(int[] engineDegrees) {
		for (int i = 0; i < 3; i++)
			anglesRad[i] = engineDegrees[i] * Math.PI / 180;
		anglesRad[0] *= side; /* Only first engine has a different side */
	}

	/* by height of leg model it will morph point or normal as it has to be */
	private Vertex3f morph(Vertex3f original, double reductionCoefficient) {
		Vertex3f point = new Vertex3f(0, 0, 0);
		int index = 3;
		double dist, angleBase;
		double xInd, yInd, zInd;
		double xInd2, yInd2, zInd2;
		/* determines on which segment we are */
		if (original.getY() < yOffset) {
			index = 0;
		} else if (original.getY() < yOffset + legLengths[0]) {
			index = 1;
		} else if (original.getY() < yOffset + legLengths[0] + legLengths[1]) {
			index = 2;
		}
		/* Below origin = all written on origin, so it would be a dot */
		if (index == 0)
			return reduced(point, reductionCoefficient);

		/* First Segment - independent movement of anglesRad[0] */
		if (index == 1) {
			dist = Math.sqrt(Math.pow(original.y - yOffset, 2)
					+ Math.pow(original.x, 2));
			/* from -90 degree to 90 degree, and result is in radiant */
			angleBase = Math.asin(original.x / dist);
			point.z = original.z;
		} else {
			dist = legLengths[0];
			angleBase = 0;
			point.z = 0;
		}
		point.x += dist * Math.sin(angleBase + anglesRad[0]);
		point.y += dist * Math.cos(angleBase + anglesRad[0]);
		if (index == 1)
			return reduced(point, reductionCoefficient);

		/* Second Segment - independent movement of anglesRad[1] */
		if (index == 2) {
			dist = Math.sqrt(Math.pow(original.y - yOffset - legLengths[0], 2)
					+ Math.pow(original.z, 2));
			angleBase = Math.asin(original.z / dist);
			xInd = original.x;
		} else {
			dist = legLengths[1];
			angleBase = 0;
			xInd = 0;
		}
		yInd = dist * Math.cos(angleBase + anglesRad[1]);
		zInd = dist * Math.sin(angleBase + anglesRad[1]);
		/* axes rotation on y of anglesRad[0] with translation */
		point.x += xInd * Math.cos(anglesRad[0]) + yInd
				* Math.sin(anglesRad[0]);
		point.y += yInd * Math.cos(anglesRad[0]) - xInd
				* Math.sin(anglesRad[0]);
		point.z += zInd;
		if (index == 2)
			return reduced(point, reductionCoefficient);

		/* Third Segment - independent movement of anglesRad[2] */
		dist = Math.sqrt(Math.pow(original.y - yOffset - legLengths[0]
				- legLengths[1], 2)
				+ Math.pow(original.z, 2));
		angleBase = Math.asin(original.z / dist);
		xInd = original.x;
		yInd = dist * Math.cos(angleBase + anglesRad[2]);
		zInd = dist * Math.sin(angleBase + anglesRad[2]);
		/* axes rotation on z of anglesRad[1] */
		xInd2 = xInd;
		yInd2 = yInd * Math.cos(anglesRad[1]) - zInd * Math.sin(anglesRad[1]);
		zInd2 = zInd * Math.cos(anglesRad[1]) + yInd * Math.sin(anglesRad[1]);
		/* axes rotation on y of anglesRad[0] with translation */
		point.x += xInd2 * Math.cos(anglesRad[0]) + yInd2
				* Math.sin(anglesRad[0]);
		point.y += yInd2 * Math.cos(anglesRad[0]) - xInd2
				* Math.sin(anglesRad[0]);
		point.z += zInd2;
		return reduced(point, reductionCoefficient);

	}
}
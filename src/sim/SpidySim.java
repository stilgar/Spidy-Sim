/*
 *	  Spidy Simulator
 *	Copyright (C) 2012  Nicolàs Alejandro Di Risio <nicolas@dirisio.net>
 *
 *	This file is part of Spidy Simulator.
 *
 *	Spidy Simulator is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Spidy Simulator is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Spidy Simulator.  If not, see <http://www.gnu.org/licenses/>.
 */

package sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SpidySim {

	static String pipeFilename = "/home/darwi/git/Spidy/pipe_sim";

	public static void main(final String[] args) {

		/* Start model view */
		VirtualView vv = new VirtualView();
		vv.start();

		/* Read from pipe every value */
		String s;
		BufferedReader reader = null;

		/* opening reader */
		try {
			System.out.println("Searching input on pipe ");
			reader = new BufferedReader(new FileReader(pipeFilename));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File can't be opened. " + pipeFilename);
			System.exit(1);
		}
		try {
			/* readline doesn't detect if files is removed */
			while ((new File(pipeFilename)).exists() && vv.start) {
				s = reader.readLine();
				if (s != null) {
					Scanner sc = new Scanner(s);
					int engineCode = sc.nextInt();
					int degree = sc.nextInt();
					vv.newCouple(engineCode, degree);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR: Generic Exepction reading " + pipeFilename);
			e.printStackTrace();
			System.exit(1);
		}
		/* Close stream */
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("ERROR: IO Error reading " + pipeFilename);
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}
package org.neotouch.neopad.resource;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.neotouch.neopad.launchpad.Pattern;

public class PatternFile
{
	private static int SQUARE_SIDE = 9;
	private File file;

	public PatternFile(File file)
	{
		this.file = file;
	}

	public Pattern loadPattern() throws IOException
	{
		Pattern p = new Pattern();

		InputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,
				Charset.forName("ASCII"));
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		while ((line = br.readLine()) != null) {
			String[] pieces = line.split("\t");

			int row = Integer.parseInt(pieces[0]);
			int col = Integer.parseInt(pieces[1]);

			p.setButtonColor(row, col, new Color(Integer.parseInt(pieces[2]),
					Integer.parseInt(pieces[3]), Integer.parseInt(pieces[4])));
			p.setButtonOn(row, col, true);
		}

		br.close();
		isr.close();
		fis.close();

		return p;
	}

	public void savePattern(Pattern p)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(file, "ASCII");

		for (int row = 0; row < SQUARE_SIDE; row++) {
			for (int col = 0; col < SQUARE_SIDE; col++) {
				if (row == 0 && col == 8) {
					continue;
				}
				if (p.isButtonOn(row, col)) {
					Color buttonColor = p.getButtonColor(row, col);
					if (buttonColor == null) {
						buttonColor = Color.BLACK;
					}
					writer.print(
							"" + row + "\t" + col + "\t" + buttonColor.getRed()
									+ "\t" + buttonColor.getGreen() + "\t"
									+ buttonColor.getBlue() + "\n");
				}
			}
		}

		writer.close();
	}
}

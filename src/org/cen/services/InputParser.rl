%%{
	machine cen2015;
	
	action clear_arg {
		argLen = 0;
	}
	
	action add_arg {
		arg[argLen] = fc;
		argLen++;
	}
	
	action done {
		fgoto main;
	}
	
	action set_number {
		String s = new String(arg, 0, argLen);
		number = Integer.parseInt(s);
	}

	action add_position {
		position[positionIndex] = number;
		positionIndex++;
	}

	action clear_position {
		positionIndex = 0;
	}

	action set_position {
		handlePosition(position);
	}

	number = ('-'? digit{1,4}) >clear_arg $add_arg %set_number;

	position = ('p: ' (number %add_position ','){2} number %add_position) >clear_position %set_position;

	command = position '\n' @done;

	main := command*;
}%%

package org.cen.services;

import java.awt.geom.Point2D;

public class InputParser {
	private byte[] arg = new byte[10];
	private int argLen = 0;
	private int[] position = new int[3];
	private int positionIndex = 0;
	private int number;
	private int parserState;
	private InputData inputData;

	public InputParser(InputData data) {
		super();
		inputData = data;
	}
	
	%%{
		write data;
	}%%

	private void handlePosition(int[] values) {
		Point2D position = new Point2D.Double(values[0], values[1]);
		inputData.setPosition(position, Math.toRadians(values[2]));
	}

	private void initParser() {
		int cs;
		%% write init;
		parserState = cs;
	}

	public void parse(byte[] data) {
		parse(data, data.length);
	}
	
	public void parse(byte[] data, int len) {
		int p = 0;
		int pe = len;
		int cs = parserState;
		
		%% write exec;

		if (cs == 0) {
			initParser();
		} else {
			parserState = cs;
		}
	}

	private void test(String text) {
		byte[] data = text.getBytes();
		parse(data);
	}
	
	public static void main(String[] args) {
		InputData inputData = new InputData() {
			@Override
			public void setPosition(Point2D position, double angle) {
				System.out.println(String.format("position: (%s), angle: %.0f°", position.toString(), angle));
			}
		};
		InputParser p = new InputParser(inputData);
		p.initParser();
		p.test("p: 123,456,112\n");
		p.test("p: -123,456,112\n");
		p.test("p: 123a,456b,-112c\n");
		p.test("p: 123,-456,112\n");
		p.test("p: 123,456,-112\n");
	}
}

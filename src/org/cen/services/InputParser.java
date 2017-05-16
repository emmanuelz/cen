
// line 1 "robot.txt"

// line 42 "robot.txt"


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
	
	
// line 24 "robot.java"
private static byte[] init__cen2015_actions_0()
{
	return new byte [] {
	    0,    1,    1,    1,    5,    2,    0,    1,    2,    3,    4,    4,
	    3,    4,    6,    2
	};
}

private static final byte _cen2015_actions[] = init__cen2015_actions_0();


private static byte[] init__cen2015_key_offsets_0()
{
	return new byte [] {
	    0,    0,    1,    2,    5,    7,   10,   13,   15,   18,   21,   23,
	   26,   29,   32,   33,   36,   39,   40,   43,   46,   47
	};
}

private static final byte _cen2015_key_offsets[] = init__cen2015_key_offsets_0();


private static char[] init__cen2015_trans_keys_0()
{
	return new char [] {
	   58,   32,   45,   48,   57,   48,   57,   44,   48,   57,   45,   48,
	   57,   48,   57,   44,   48,   57,   45,   48,   57,   48,   57,   10,
	   48,   57,   10,   48,   57,   10,   48,   57,   10,   44,   48,   57,
	   44,   48,   57,   44,   44,   48,   57,   44,   48,   57,   44,  112,
	    0
	};
}

private static final char _cen2015_trans_keys[] = init__cen2015_trans_keys_0();


private static byte[] init__cen2015_single_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    1,    0,    1,    1,    0,    1,    1,    0,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1
	};
}

private static final byte _cen2015_single_lengths[] = init__cen2015_single_lengths_0();


private static byte[] init__cen2015_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    0,    1,    1,    0,    1,    1,    0,    0
	};
}

private static final byte _cen2015_range_lengths[] = init__cen2015_range_lengths_0();


private static byte[] init__cen2015_index_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    4,    7,    9,   12,   15,   17,   20,   23,   25,
	   28,   31,   34,   36,   39,   42,   44,   47,   50,   52
	};
}

private static final byte _cen2015_index_offsets[] = init__cen2015_index_offsets_0();


private static byte[] init__cen2015_indicies_0()
{
	return new byte [] {
	    0,    1,    2,    1,    3,    4,    1,    5,    1,    6,    7,    1,
	    8,    9,    1,   10,    1,   11,   12,    1,   13,   14,    1,   15,
	    1,   16,   17,    1,   16,   18,    1,   16,   19,    1,   16,    1,
	   11,   20,    1,   11,   21,    1,   11,    1,    6,   22,    1,    6,
	   23,    1,    6,    1,   24,    1,    0
	};
}

private static final byte _cen2015_indicies[] = init__cen2015_indicies_0();


private static byte[] init__cen2015_trans_targs_0()
{
	return new byte [] {
	    2,    0,    3,    4,    5,    5,    6,   18,    7,    8,    8,    9,
	   15,   10,   11,   11,   21,   12,   13,   14,   16,   17,   19,   20,
	    1
	};
}

private static final byte _cen2015_trans_targs[] = init__cen2015_trans_targs_0();


private static byte[] init__cen2015_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    5,    5,    1,    8,    1,    5,    5,    1,    8,
	    1,    5,    5,    1,   11,    1,    1,    1,    1,    1,    1,    1,
	    3
	};
}

private static final byte _cen2015_trans_actions[] = init__cen2015_trans_actions_0();


static final int cen2015_start = 21;
static final int cen2015_first_final = 21;
static final int cen2015_error = 0;

static final int cen2015_en_main = 21;


// line 64 "robot.txt"


	private void handlePosition(int[] values) {
		Point2D position = new Point2D.Double(values[0], values[1]);
		inputData.setPosition(position, Math.toRadians(values[2]));
	}

	private void initParser() {
		int cs;
		
// line 148 "robot.java"
	{
	cs = cen2015_start;
	}

// line 74 "robot.txt"
		parserState = cs;
	}

	public void parse(byte[] data) {
		parse(data, data.length);
	}
	
	public void parse(byte[] data, int len) {
		int p = 0;
		int pe = len;
		int cs = parserState;
		
		
// line 161 "robot.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_match: do {
	_keys = _cen2015_key_offsets[cs];
	_trans = _cen2015_index_offsets[cs];
	_klen = _cen2015_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _cen2015_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _cen2015_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _cen2015_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _cen2015_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _cen2015_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _cen2015_indicies[_trans];
	cs = _cen2015_trans_targs[_trans];

	if ( _cen2015_trans_actions[_trans] != 0 ) {
		_acts = _cen2015_trans_actions[_trans];
		_nacts = (int) _cen2015_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _cen2015_actions[_acts++] )
			{
	case 0:
// line 4 "robot.txt"
	{
		argLen = 0;
	}
	break;
	case 1:
// line 8 "robot.txt"
	{
		arg[argLen] = data[p];
		argLen++;
	}
	break;
	case 2:
// line 13 "robot.txt"
	{
		{cs = 21; _goto_targ = 2; if (true) continue _goto;}
	}
	break;
	case 3:
// line 17 "robot.txt"
	{
		String s = new String(arg, 0, argLen);
		number = Integer.parseInt(s);
	}
	break;
	case 4:
// line 22 "robot.txt"
	{
		position[positionIndex] = number;
		positionIndex++;
	}
	break;
	case 5:
// line 27 "robot.txt"
	{
		positionIndex = 0;
	}
	break;
	case 6:
// line 31 "robot.txt"
	{
		handlePosition(position);
	}
	break;
// line 286 "robot.java"
			}
		}
	}

case 2:
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
case 5:
	}
	break; }
	}

// line 83 "robot.txt"

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

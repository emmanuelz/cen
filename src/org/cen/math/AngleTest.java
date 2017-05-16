package org.cen.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class AngleTest {
	private static final double EPSILON = 1e-6;

	@Test
	public void testGetRotationAngle1() {
		double actual = Angle.getRotationAngle(3, -3);
		assertEquals(2 * (Math.PI - 3), actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle2() {
		double actual = Angle.getRotationAngle(-3, 3);
		assertEquals(-2 * (Math.PI - 3), actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle3() {
		double actual = Angle.getRotationAngle(1, -1);
		assertEquals(-2, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle4() {
		double actual = Angle.getRotationAngle(-1, 1);
		assertEquals(2, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle5() {
		double actual = Angle.getRotationAngle(Math.PI, -Math.PI / 2);
		assertEquals(Math.PI / 2, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle6() {
		double actual = Angle.getRotationAngle(-Math.PI / 2, Math.PI);
		assertEquals(-Math.PI / 2, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle7() {
		double actual = Angle.getRotationAngle(-Math.PI, 0);
		assertEquals(Math.PI, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle8() {
		double actual = Angle.getRotationAngle(Math.PI, 0);
		assertEquals(-Math.PI, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle9() {
		double actual = Angle.getRotationAngle(0, Math.PI);
		assertEquals(Math.PI, actual, EPSILON);
	}

	@Test
	public void testGetRotationAngle10() {
		double actual = Angle.getRotationAngle(0, -Math.PI);
		assertEquals(-Math.PI, actual, EPSILON);
	}
}

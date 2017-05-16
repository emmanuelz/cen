package org.cen.persistance;

import java.awt.geom.Point2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class OrientedPositionMixIn {
	@JsonIgnore
	public abstract Point2D getLocation();
}

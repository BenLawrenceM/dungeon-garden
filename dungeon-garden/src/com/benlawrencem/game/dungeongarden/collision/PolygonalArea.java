package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class PolygonalArea extends Area {
	private float[] points;
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private Shape polyRender;

	public PolygonalArea(float[] points) {
		super(0, 0);
		setPoints(points);
	}

	public void setPoints(float[] points) {
		minX = points[0];
		maxX = points[0];
		minY = points[1];
		maxY = points[1];
		for(int i = 2; i < points.length; i++) {
			if(i%2 == 0) {
				if(points[i] < minX)
					minX = points[i];
				else if(points[i] > maxX)
					maxX = points[i];
			}
			else {
				if(points[i] < minY)
					minY = points[i];
				else if(points[i] > maxY)
					maxY = points[i];
			}
		}
		this.points = points;
		polyRender = null;
	}

	@Override
	public void render(Graphics g, Color color) {
		if(polyRender == null)
			polyRender = new Polygon(points);
		polyRender.setCenterX(getX());
		polyRender.setCenterY(getY());
		g.setColor(color);
		g.fill(polyRender);
	}

	@Override
	protected boolean checkForIntersection(Area other, boolean callOnHitAfter, boolean callOnCollisionAfter) {
		//TODO fill out this function
		return false;
	}

	@Override
	public float getTop() {
		return getY() + minY;
	}

	@Override
	public float getBottom() {
		return getY() + maxY;
	}

	@Override
	public float getLeft() {
		return getX() + minX;
	}

	@Override
	public float getRight() {
		return getX() + maxX;
	}

	@Override
	public Type getType() {
		return Type.POLYGONAL;
	}
}

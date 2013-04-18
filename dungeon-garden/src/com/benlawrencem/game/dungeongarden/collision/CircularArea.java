package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.entity.Entity;

public class CircularArea extends Area {
	private float radius;

	public CircularArea(float radius) {
		super();
		setRadius(radius);
	}

	public CircularArea(float offsetX, float offsetY, float radius) {
		super(offsetX, offsetY);
		setRadius(radius);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public void render(Graphics g, Color color) {
		g.setColor(color);
		//keep in mind the parameters are for a box containing the arc, and then the degrees of the arc to draw within that box
		g.fillArc(getX() - radius, getY() - radius, 2 * radius, 2 * radius, 0, 360);
	}

	@Override
	protected boolean checkForIntersection(Area other, boolean callOnCollisionAfter) {
		if(other.getType() == Type.POINT || other.getType() == Type.RECTANGULAR || other.getType() == Type.POLYGONAL)
			return other.checkForIntersection(this, callOnCollisionAfter); //avoids duplicate logic

		//two circles are intersecting iff the distance between their centers is less than the sum of their radii
		if(other.getType() == Type.CIRCULAR) {
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float radii = radius + ((CircularArea) other).getRadius();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;

			//same as saying two circles are intersecting iff the SQUARE distance between their centers is less than the SQUARED sum of their radii
			if(squareDistance < radii * radii) {
				if(callOnCollisionAfter) {
					//since the circles are intersecting, the overlap is the sum of their radii minus the distance between their centers
					float scalar = Entity.calculateCollisionscalar(getParent(), other.getParent());
					float distance = (float) Math.sqrt(squareDistance);
					float xNorm = horizontalDistance / distance;
					float yNorm = verticalDistance / distance;
					getParent().onCollision(other.getParent(),
							-xNorm * (radii - distance) * scalar,
							-yNorm * (radii - distance) * scalar);
					other.getParent().onCollision(getParent(),
							xNorm * (radii - distance) * (1 - scalar),
							yNorm * (radii - distance) * (1 - scalar));
				}
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public float getTop() {
		return getY() - radius;
	}

	@Override
	public float getBottom() {
		return getY() + radius;
	}

	@Override
	public float getLeft() {
		return getX() - radius;
	}

	@Override
	public float getRight() {
		return getX() + radius;
	}

	@Override
	public Type getType() {
		return Type.CIRCULAR;
	}
}

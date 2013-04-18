package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
	public boolean isCollidingWith(Area other) {
		if(other.getType() == Type.POINT || other.getType() == Type.RECTANGULAR || other.getType() == Type.POLYGONAL)
			return other.isCollidingWith(this); //avoids duplicate logic

		//two circles are colliding if the distance between their centers is less than the sum of their radii
		if(other.getType() == Type.CIRCULAR) {
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			float radii = radius + ((CircularArea) other).getRadius();
			//same as saying the square distance between their centers is less than the squared sum of their radii
			return (squareDistance < radii * radii);
		}

		return false;
	}

	@Override
	public boolean handleCollisionWith(Area other, float dislodgeWeight) {
		if(other.getType() == Type.POINT || other.getType() == Type.RECTANGULAR || other.getType() == Type.POLYGONAL)
			return other.handleCollisionWith(this, 1 - dislodgeWeight); //avoids duplicate logic

		//handling a collision between two circles involves pushing each away from the other's center
		if(other.getType() == Type.CIRCULAR) {
			//two circles are colliding if the distance between their centers is less than the sum of their radii
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float radii = radius + ((CircularArea) other).getRadius();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;

			//same as saying the square distance between their centers is less than the squared sum of their radii
			if(squareDistance < radii * radii) {
				//since the circles are intersecting, dislodge them an amount equal to the overlap (radii - distance between centers)
				float distance = (float) Math.sqrt(squareDistance);
				float xNorm = horizontalDistance / distance;
				float yNorm = verticalDistance / distance;
				getParent().dislodgeFrom(other.getParent(),
						-xNorm * (radii - distance) * dislodgeWeight,
						-yNorm * (radii - distance) * dislodgeWeight);
				other.getParent().dislodgeFrom(getParent(),
						xNorm * (radii - distance) * (1 - dislodgeWeight),
						yNorm * (radii - distance) * (1 - dislodgeWeight));
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public void render(Graphics g, Color color) {
		g.setColor(color);
		//keep in mind the parameters are for a box containing the arc, and then the degrees of the arc to draw within that box
		g.fillArc(getX() - radius, getY() - radius, 2 * radius, 2 * radius, 0, 360);
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

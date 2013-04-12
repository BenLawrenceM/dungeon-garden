package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CircularHitBox extends HitBox {
	private float radius;

	public CircularHitBox(float radius) {
		super();
		setRadius(radius);
	}

	public CircularHitBox(float offsetX, float offsetY, float radius) {
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
	public boolean isCollidingWith(HitBox other) {
		//avoid duplicate logic
		if(other.getType() == Type.POINT || other.getType() == Type.RECTANGULAR)
			return other.isCollidingWith(this);

		//move the circles away from each others' centers
		if(other.getType() == Type.CIRCULAR) {
			float otherRadius = ((CircularHitBox) other).getRadius();
			float radii = radius + otherRadius;
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			return (squareDistance < radii * radii);
		}

		return false;
	}

	@Override
	public boolean handleCollisionWith(HitBox other, float dislodgeWeight) {
		//avoid duplicate logic
		if(other.getType() == Type.POINT || other.getType() == Type.RECTANGULAR)
			return other.handleCollisionWith(this, 1 - dislodgeWeight);

		//move the circles away from each others' centers
		if(other.getType() == Type.CIRCULAR) {
			float otherRadius = ((CircularHitBox) other).getRadius();
			float radii = radius + otherRadius;
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			if(squareDistance < radii * radii) {
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
	public Type getType() {
		return Type.CIRCULAR;
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
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillArc(getX() - radius, getY() - radius, 2 * radius, 2 * radius, 0, 360);
	}
}

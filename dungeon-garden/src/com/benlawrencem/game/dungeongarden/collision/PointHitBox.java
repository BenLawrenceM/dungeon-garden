package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PointHitBox extends HitBox {
	public PointHitBox() {
		super();
	}

	public PointHitBox(float offsetX, float offsetY) {
		super(offsetX, offsetY);
	}

	@Override
	public boolean isCollidingWith(HitBox other) {
		//two points are colliding if they are exactly on top of each other
		if(other.getType() == Type.POINT) {
			return getX() == other.getX() && getY() == other.getY();
		}

		//a point is intersecting a circle if the distance between the point and the circle's center is less than the circle's radius
		if(other.getType() == Type.CIRCULAR) {
			float radius = ((CircularHitBox) other).getRadius();
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			//same as saying the square distance between them is less than the square radius
			return (squareDistance < radius * radius);
		}

		//a point is intersecting a rectangle if the point is between the rectangle's horizontal and vertical bounds
		if(other.getType() == Type.RECTANGULAR) {
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();
			return (otherLeft < getX() && getX() < otherRight && otherTop < getY() && getY() < otherBottom);
		}

		return false;
	}

	@Override
	public boolean handleCollisionWith(HitBox other, float dislodgeWeight) {
		//two points shouldn't be dislodged--the result is unpredictable
		if(other.getType() == Type.POINT) {
			return getX() == other.getX() && getY() == other.getY();
		}

		//move the point away from the center of the circle
		if(other.getType() == Type.CIRCULAR) {
			float radius = ((CircularHitBox) other).getRadius();
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			if(squareDistance < radius * radius) {
				float distance = (float) Math.sqrt(squareDistance);
				float xNorm = horizontalDistance / distance;
				float yNorm = verticalDistance / distance;
				getParent().dislodgeFrom(other.getParent(),
						-xNorm * (radius - distance) * dislodgeWeight,
						-yNorm * (radius - distance) * dislodgeWeight);
				other.getParent().dislodgeFrom(getParent(),
						xNorm * (radius - distance) * (1 - dislodgeWeight),
						yNorm * (radius - distance) * (1 - dislodgeWeight));
				return true;
			}
			return false;
		}

		//move the point away from the nearest side of the rectangle
		if(other.getType() == Type.RECTANGULAR) {
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();
			if(otherLeft < getX() && getX() < otherRight && otherTop < getY() && getY() < otherBottom) {
				float distanceToLeft = getX() - otherLeft;
				float distanceToRight = otherRight - getX();
				float distanceToTop = getY() - otherTop;
				float distanceToBottom = otherBottom - getY();
				float dislodgeX = 0;
				float dislodgeY = 0;
				float otherDislodgeX = 0;
				float otherDislodgeY = 0;
				if(distanceToLeft < distanceToRight && distanceToLeft < distanceToTop && distanceToLeft < distanceToBottom) {
					otherDislodgeX = -distanceToLeft * dislodgeWeight;
					otherDislodgeX = distanceToLeft * (1 - dislodgeWeight);
				}
				else if(distanceToRight < distanceToTop && distanceToRight < distanceToBottom) {
					otherDislodgeX = distanceToRight * dislodgeWeight;
					otherDislodgeX = -distanceToRight * (1 - dislodgeWeight);
				}
				else if(distanceToTop < distanceToBottom) {
					dislodgeY = -distanceToTop * dislodgeWeight;
					otherDislodgeY = distanceToTop * (1 - dislodgeWeight);
				}
				else {
					dislodgeY = distanceToBottom * dislodgeWeight;
					otherDislodgeY = -distanceToBottom * (1 - dislodgeWeight);
				}
				getParent().dislodgeFrom(other.getParent(), dislodgeX, dislodgeY);
				other.getParent().dislodgeFrom(getParent(), otherDislodgeX, otherDislodgeY);
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillArc(getX() - 1, getY() - 1, 2, 2, 0, 360);
	}

	@Override
	public Type getType() {
		return Type.POINT;
	}

	@Override
	public float getTop() {
		return getY();
	}

	@Override
	public float getBottom() {
		return getY();
	}

	@Override
	public float getLeft() {
		return getX();
	}

	@Override
	public float getRight() {
		return getX();
	}
}

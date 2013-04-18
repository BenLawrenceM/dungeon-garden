package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.entity.Entity;

public class PointArea extends Area {
	public PointArea() {
		super();
	}

	public PointArea(float offsetX, float offsetY) {
		super(offsetX, offsetY);
	}

	@Override
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillArc(getX() - 1, getY() - 1, 2, 2, 0, 360);
	}

	@Override
	protected boolean checkForIntersection(Area other, boolean callOnCollisionAfter) {
		//two points are intersecting if they are exactly on top of one other
		if(other.getType() == Type.POINT) {
			if(getX() == other.getX() && getY() == other.getY()) {
				//there's no good way to dislodge two points, so don't!
				if(callOnCollisionAfter) {
					getParent().onCollision(other.getParent(), 0, 0);
					other.getParent().onCollision(getParent(), 0, 0);
				}
				return true;
			}
			return false;
		}

		//a point is intersecting a circle if the distance from the point to the center of the circle is less than the radius
		if(other.getType() == Type.CIRCULAR) {
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float radius = ((CircularArea) other).getRadius();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;

			//same as saying a point is intersecting a circle if the SQUARE distance from the point to the center of the circle is less than the SQUARE radius
			if(squareDistance < radius * radius) {
				//since they are intersecting, the overlap is the radius minus the distance from the point to the center of the circle
				if(callOnCollisionAfter) {
					float scalar = Entity.calculateCollisionscalar(getParent(), other.getParent());
					float distance = (float) Math.sqrt(squareDistance);
					float xNorm = horizontalDistance / distance;
					float yNorm = verticalDistance / distance;
					getParent().onCollision(other.getParent(),
							-xNorm * (radius - distance) * scalar,
							-yNorm * (radius - distance) * scalar);
					other.getParent().onCollision(getParent(),
							xNorm * (radius - distance) * (1 - scalar),
							yNorm * (radius - distance) * (1 - scalar));
				}
				return true;
			}
			return false;
		}

		//a point is intersecting a rectangle if it lies between the left/right bounds and the top/bottom bounds
		if(other.getType() == Type.RECTANGULAR) {
			if(other.getLeft() < getX() && getX() < other.getRight() && other.getTop() < getY() && getY() < other.getBottom()) {
				//the point is overlapping an amount equal to the distance straight to the closest edge
				if(callOnCollisionAfter) {
					float scalar = Entity.calculateCollisionscalar(getParent(), other.getParent());
					float distanceToLeft = getX() - other.getLeft();
					float distanceToRight = other.getRight() - getX();
					float distanceToTop = getY() - other.getTop();
					float distanceToBottom = other.getBottom() - getY();
					float overlapX = 0;
					float overlapY = 0;

					//the point is closest to the left edge
					if(distanceToLeft < distanceToRight && distanceToLeft < distanceToTop && distanceToLeft < distanceToBottom)
						overlapX = -distanceToLeft;

					//the point is closest to the right edge
					else if(distanceToRight < distanceToTop && distanceToRight < distanceToBottom)
						overlapX = distanceToRight;

					//the point is closest to the top edge
					else if(distanceToTop < distanceToBottom)
						overlapY = -distanceToTop;

					//the point is closest to the bottom edge
					else
						overlapY = distanceToBottom;

					getParent().onCollision(other.getParent(), overlapX * scalar, overlapY * scalar);
					other.getParent().onCollision(getParent(), -overlapX * (1 - scalar), -overlapY * (1 - scalar));
				}
				return true;
			}
			return false;
		}

		return false;
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

	@Override
	public Type getType() {
		return Type.POINT;
	}
}

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
	public void render(Graphics g, Color color) {
		g.setColor(color);
		//keep in mind the parameters are for a box containing the arc, and then the degrees of the arc to draw within that box
		g.fillArc(getX() - radius, getY() - radius, 2 * radius, 2 * radius, 0, 360);
	}

	@Override
	protected boolean checkForIntersection(Area other, boolean callOnHitAfter, boolean callOnCollisionAfter) {
		if(other.getType() == Type.POINT || other.getType() == Type.POLYGONAL)
			return other.checkForIntersection(this, callOnHitAfter, callOnCollisionAfter); //avoid duplicate logic

		//two circles are intersecting iff the distance between their centers is less than the sum of their radii
		if(other.getType() == Type.CIRCULAR) {
			float horizontalDistance = other.getX() - getX();
			float verticalDistance = other.getY() - getY();
			float radii = radius + ((CircularArea) other).getRadius();
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;

			//same as saying two circles are intersecting iff the SQUARE distance between their centers is less than the SQUARED sum of their radii
			if(squareDistance < radii * radii) {
				if(callOnHitAfter || callOnCollisionAfter) {
					//since the circles are intersecting, the overlap is the sum of their radii minus the distance between their centers
					float distance = (float) Math.sqrt(squareDistance);
					float directionX = horizontalDistance / distance;
					float directionY = verticalDistance / distance;
					if(callOnHitAfter)
						callOnHit(other, directionX, directionY);
					if(callOnCollisionAfter)
						callOnCollision(other, directionX * (radii - distance), directionY * (radii - distance));
				}
				return true;
			}
			return false;
		}

		//a circle intersects a rectangle iff the circle intersects one of the rectangle's corners or if it is directly above/below/to the side
		// of the rectangle and the horizontal and vertical shadows overlap
		if(other.getType() == Type.RECTANGULAR) {
			//if the circle's center is directly above or below the rectangle, just do a shadow collision check
			if(other.getLeft() <= getX() && getX() < other.getRight()) {
				if((getTop() <= other.getTop() && other.getTop() < getBottom()) || (other.getTop() <= getTop() && getTop() < other.getBottom())) {
					if(callOnHitAfter || callOnCollisionAfter) {
						float overlapY = (other.getY() < getY() ? getTop() - other.getBottom() : getBottom() - other.getTop());
						if(callOnHitAfter)
							callOnHit(other, 0, (overlapY > 0 ? 1 : -1));
						if(callOnCollisionAfter)
							callOnCollision(other, 0, overlapY);
					}
					return true;
				}
				return false;
			}

			//if the circle's center is directly to the left or right of the rectangle, just do a shadow collision check
			if(other.getTop() < getY() && getY() < other.getBottom()) {
				if(((getLeft() <= other.getLeft() && other.getLeft() < getRight()) || (other.getLeft() <= getLeft() && getLeft() < other.getRight()))) {
					if(callOnHitAfter || callOnCollisionAfter) {
						float overlapX = (other.getX() < getX() ? getLeft() - other.getRight() : getRight() - other.getLeft());
						if(callOnHitAfter)
							callOnHit(other, (overlapX > 0 ? 1 :- 1), 0);
						if(callOnCollisionAfter)
							callOnCollision(other, overlapX, 0);
					}
					return true;
				}
				return false;
			}

			//otherwise, the rectangle and circle are intersecting iff the distance to the nearest rectangle corner is less than the circle's radius
			float horizontalDistance = (getX() > other.getX() ? other.getRight() - getX() : other.getLeft() - getX());
			float verticalDistance = (getY() > other.getY() ? other.getBottom() - getY() : other.getTop() - getY());
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			//same as saying the rectangle and circle are intersecting iff the SQUARE distance to the nearest rectangle corner is less than the circle's SQUARE radius
			if(squareDistance < getRadius() * getRadius()) {
				if(callOnHitAfter || callOnCollisionAfter) {
					float distance = (float) Math.sqrt(squareDistance);
					float directionX = horizontalDistance / distance;
					float directionY = verticalDistance / distance;
					if(callOnHitAfter)
						callOnHit(other, directionX, directionY);
					if(callOnCollisionAfter)
						callOnCollision(other, directionX * (getRadius() - distance), directionY * (getRadius() - distance));
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

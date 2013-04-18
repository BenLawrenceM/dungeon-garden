package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RectangularArea extends Area {
	private float width;
	private float height;

	public RectangularArea(float width, float height) {
		super();
		setDimensions(width, height);
	}

	public RectangularArea(float offsetX, float offsetY, float width, float height) {
		super(offsetX, offsetY);
		setDimensions(width, height);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		setDimensions(width, this.height);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		setDimensions(this.width, height);
	}

	public void setDimensions(float width, float height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(getLeft(), getTop(), width, height);
	}

	@Override
	protected boolean checkForIntersection(Area other, boolean callOnCollisionAfter) {
		if(other.getType() == Type.POINT)
			return other.checkForIntersection(this, callOnCollisionAfter); //avoids duplicate logic

		//two rectangles are intersecting iff their horizontal and vertical shadows both clip
		if(other.getType() == Type.RECTANGULAR) {
			if(((other.getLeft() <= getLeft() && getLeft() < other.getRight())				//l-----L-=-=-r=====R   (uppercase = this, lowercase = other)
					|| (getLeft() <= other.getLeft() && other.getLeft() < getRight()))		//L=====l=-=-=R-----r
					&& ((other.getTop() <= getTop() && getTop() < other.getBottom())		//t-----T-=-=-b=====B
					|| (getTop() <= other.getTop() && other.getTop() < getBottom()))) {		//T=====t=-=-=B-----b
				//the rectangles overlap an amount equal to the minimum overlap of their two axes
				if(callOnCollisionAfter) {
					float horizontalDistance = (getX() < other.getX() ? getRight() - other.getLeft() : other.getRight() - getLeft());
					float verticalDistance = (getY() < other.getY() ? getBottom() - other.getTop() : other.getBottom() - getTop());
					float overlapX = 0;
					float overlapY = 0;
					if(horizontalDistance < verticalDistance)
						overlapX = (getX() < other.getX() ? -horizontalDistance : horizontalDistance);
					else
						overlapY = (getY() < other.getY() ? -verticalDistance : verticalDistance);
					callOnCollision(other, overlapX, overlapY);
				}
				return true;
			}
			return false;
		}

		//a rectangle intersects a circle iff the circle intersects one of the rectangle's corners or if it is directly above/below/to the side
		// and the horizontal and vertical shadows overlap
		if(other.getType() == Type.CIRCULAR) {
			float x = getX();
			float y = getY();
			float left = getLeft();
			float right = getRight();
			float top = getTop();
			float bottom = getBottom();
			float otherX = other.getX();
			float otherY = other.getY();
			float otherRadius = ((CircularArea) other).getRadius();
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();

			//if the circle's center is directly above or below the rectangle, just do a shadow collision check
			if(left <= otherX && otherX < right) {
				if((otherTop <= top && top < otherBottom) || (top <= otherTop && otherTop < bottom)) {
					if(callOnCollisionAfter) {
						float overlapY = (y < otherY ? otherTop - bottom : otherBottom - top);
						callOnCollision(other, 0, overlapY);
					}
					return true;
				}
				return false;
			}

			//if the circle's center is directly to the left or right of the rectangle, just do a shadow collision check
			if(top < otherY && otherY < bottom) {
				if(((otherLeft <= left && left < otherRight) || (left <= otherLeft && otherLeft < right))) {
					if(callOnCollisionAfter) {
						float overlapX = (x < otherX ? otherLeft - right : otherRight - left);
						callOnCollision(other, overlapX, 0);
					}
					return true;
				}
				return false;
			}

			//otherwise, the rectangle and circle are intersecting iff the distance to the nearest rectangle corner is less than the circle's radius
			float horizontalDistance = (otherX > x ? right - otherX : left - otherX);
			float verticalDistance = (otherY > y ? bottom - otherY : top - otherY);
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			//same as saying the rectangle and circle are intersecting iff the SQUARE distance to the nearest rectangle corner is less than the circle's SQUARE radius
			if(squareDistance < otherRadius * otherRadius) {
				if(callOnCollisionAfter) {
					float distance = (float) Math.sqrt(squareDistance);
					float overlapX = horizontalDistance / distance * (otherRadius - distance);
					float overlapY = verticalDistance / distance * (otherRadius - distance);
					callOnCollision(other, overlapX, overlapY);
				}
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public float getTop() {
		return getY() - height / 2;
	}

	@Override
	public float getBottom() {
		return getY() + height / 2;
	}

	@Override
	public float getLeft() {
		return getX() - width / 2;
	}

	@Override
	public float getRight() {
		return getX() + width / 2;
	}

	@Override
	public Type getType() {
		return Type.RECTANGULAR;
	}
}
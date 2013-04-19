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
	protected boolean checkForIntersection(Area other, boolean callOnHitAfter, boolean callOnCollisionAfter) {
		if(other.getType() == Type.POINT || other.getType() == Type.CIRCULAR || other.getType() == Type.POLYGONAL)
			return other.checkForIntersection(this, callOnHitAfter, callOnCollisionAfter); //avoid duplicate logic

		//two rectangles are intersecting iff their horizontal and vertical shadows both clip
		if(other.getType() == Type.RECTANGULAR) {
			if(((other.getLeft() <= getLeft() && getLeft() < other.getRight())				//l-----L-=-=-r=====R   (uppercase = this, lowercase = other)
					|| (getLeft() <= other.getLeft() && other.getLeft() < getRight()))		//L=====l=-=-=R-----r
					&& ((other.getTop() <= getTop() && getTop() < other.getBottom())		//t-----T-=-=-b=====B
					|| (getTop() <= other.getTop() && other.getTop() < getBottom()))) {		//T=====t=-=-=B-----b
				//the rectangles overlap an amount equal to the minimum overlap of their two axes
				if(callOnHitAfter || callOnCollisionAfter) {
					float horizontalDistance = (getX() < other.getX() ? getRight() - other.getLeft() : other.getRight() - getLeft());
					float verticalDistance = (getY() < other.getY() ? getBottom() - other.getTop() : other.getBottom() - getTop());
					float directionX = 0;
					float directionY = 0;
					float overlapX = 0;
					float overlapY = 0;
					if(horizontalDistance < verticalDistance) {
						overlapX = (getX() < other.getX() ? horizontalDistance : -horizontalDistance);
						directionX = (overlapX > 0 ? 1 : -1);
					}
					else {
						overlapY = (getY() < other.getY() ? verticalDistance : -verticalDistance);
						directionY = (overlapY > 0 ? 1 : -1);
					}

					if(callOnHitAfter)
						callOnHit(other, directionX, directionY);
					if(callOnCollisionAfter)
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
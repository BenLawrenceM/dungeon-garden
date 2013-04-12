package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RectangularHitBox extends HitBox {
	private float width;
	private float height;

	public RectangularHitBox(float width, float height) {
		super();
		setDimensions(width, height);
	}

	public RectangularHitBox(float offsetX, float offsetY, float width, float height) {
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
	public boolean isCollidingWith(HitBox other) {
		//avoid duplicate logic
		if(other.getType() == Type.POINT)
			return other.isCollidingWith(this);

		//move the rectangles either vertically or horizontally (but not diagonally) to get them to no longer intersect
		if(other.getType() == Type.RECTANGULAR) {
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();

			//tests to see if the rectangles are overlapping horizontally and vertically--if so they are intersecting, if not they aren't
			return (((otherLeft <= getLeft() && getLeft() < otherRight) || (getLeft() <= otherLeft && otherLeft < getRight()))
					&& ((otherTop <= getTop() && getTop() < otherBottom) || (getTop() <= otherTop && otherTop < getBottom())));
		}

		//move the rectangle away from the circle's center if it's intersecting one of the corners, but only vertically or horizontally otherwise
		if(other.getType() == Type.CIRCULAR) {
			float myX = getX();
			float myY = getY();
			float myLeft = getLeft();
			float myRight = getRight();
			float myTop = getTop();
			float myBottom = getBottom();
			float otherX = other.getX();
			float otherY = other.getY();
			float otherRadius = ((CircularHitBox) other).getRadius();
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();

			//if the circle's center is directly above or below the rectangle, just do a shadow collision check
			if(myLeft <= otherX && otherX < myRight)
				return ((otherTop <= myTop && myTop < otherBottom) || (myTop <= otherTop && otherTop < myBottom));

			//if the circle's center is directly to the left or right of the rectangle, just do a shadow collision check
			if(myTop < otherY && otherY < myBottom)
				return (((otherLeft <= myLeft && myLeft < otherRight) || (myLeft <= otherLeft && otherLeft < myRight)));

			//otherwise, check to see if the distance to the nearest rectangle corner is less than the circle's radius
			float horizontalDistance = (otherX > myX ? myRight - otherX : myLeft - otherX);
			float verticalDistance = (otherY > myY ? myBottom - otherY : myTop - otherY);
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			//same as checking to see if the square distance from the nearest rectangle corner is less than the square of the circle's radius
			return (squareDistance < otherRadius * otherRadius);
		}

		return false;
	}

	@Override
	public boolean handleCollisionWith(HitBox other, float dislodgeWeight) {
		//avoid duplicate logic
		if(other.getType() == Type.POINT)
			return other.handleCollisionWith(this, 1 - dislodgeWeight);

		//move the rectangles either vertically or horizontally (but not diagonally) to get them to no longer intersect
		if(other.getType() == Type.RECTANGULAR) {
			float otherLeft = ((RectangularHitBox) other).getLeft();
			float otherRight = ((RectangularHitBox) other).getRight();
			float otherTop = ((RectangularHitBox) other).getTop();
			float otherBottom = ((RectangularHitBox) other).getBottom();

			//tests to see if the rectangles are overlapping horizontally and vertically--if so they are intersecting, if not they aren't
			if(((otherLeft <= getLeft() && getLeft() < otherRight) || (getLeft() <= otherLeft && otherLeft < getRight()))
					&& ((otherTop <= getTop() && getTop() < otherBottom) || (getTop() <= otherTop && otherTop < getBottom()))) {
				float horizontalOverlap = (getX() < other.getX() ? getRight() - otherLeft : otherRight - getLeft());
				float verticalOverlap = (getY() < other.getY() ? getBottom() - otherTop : otherBottom - getTop());
				float dislodgeX = 0;
				float dislodgeY = 0;
				float otherDislodgeX = 0;
				float otherDislodgeY = 0;
				if(horizontalOverlap < verticalOverlap) {
					if(getX() < other.getX()) {
						dislodgeX = -horizontalOverlap * dislodgeWeight;
						otherDislodgeX = horizontalOverlap * (1 - dislodgeWeight);
					}
					else {
						dislodgeX = horizontalOverlap * dislodgeWeight;
						otherDislodgeX = -horizontalOverlap * (1 - dislodgeWeight);
					}
				}
				else {
					if(getY() < other.getY()) {
						dislodgeY = -verticalOverlap * dislodgeWeight;
						otherDislodgeY = verticalOverlap * (1 - dislodgeWeight);
					}
					else {
						dislodgeY = verticalOverlap * dislodgeWeight;
						otherDislodgeY = -verticalOverlap * (1 - dislodgeWeight);
					}
				}
				getParent().dislodgeFrom(other.getParent(), dislodgeX, dislodgeY);
				other.getParent().dislodgeFrom(getParent(), otherDislodgeX, otherDislodgeY);
				return true;
			}
			return false;
		}

		//move the rectangle away from the circle's center if it's intersecting one of the corners, but only vertically or horizontally otherwise
		if(other.getType() == Type.CIRCULAR) {
			float myX = getX();
			float myY = getY();
			float myLeft = getLeft();
			float myRight = getRight();
			float myTop = getTop();
			float myBottom = getBottom();
			float otherX = other.getX();
			float otherY = other.getY();
			float otherRadius = ((CircularHitBox) other).getRadius();
			float otherLeft = other.getLeft();
			float otherRight = other.getRight();
			float otherTop = other.getTop();
			float otherBottom = other.getBottom();

			//if the circle's center is directly above or below the rectangle, just do a shadow collision check
			if(myLeft <= otherX && otherX < myRight) {
				if((otherTop <= myTop && myTop < otherBottom) || (myTop <= otherTop && otherTop < myBottom)) {
					if(myY < otherY) {
						float verticalDistance = myBottom - otherTop;
						getParent().dislodgeFrom(other.getParent(), 0, -verticalDistance * dislodgeWeight);
						other.getParent().dislodgeFrom(getParent(), 0, verticalDistance * (1 - dislodgeWeight));
					}
					else {
						float verticalDistance = otherBottom - myTop;
						getParent().dislodgeFrom(other.getParent(), 0, verticalDistance * dislodgeWeight);
						other.getParent().dislodgeFrom(getParent(), 0, -verticalDistance * (1 - dislodgeWeight));
					}
					return true;
				}
				return false;
			}

			//if the circle's center is directly to the left or right of the rectangle, just do a shadow collision check
			if(myTop < otherY && otherY < myBottom) {
				if(((otherLeft <= myLeft && myLeft < otherRight) || (myLeft <= otherLeft && otherLeft < myRight))) {
					if(myX < otherX) {
						float horizontalDistance = myRight - otherLeft;
						getParent().dislodgeFrom(other.getParent(), -horizontalDistance * dislodgeWeight, 0);
						other.getParent().dislodgeFrom(getParent(), horizontalDistance * (1 - dislodgeWeight), 0);
					}
					else {
						float horizontalDistance = otherRight - myLeft;
						getParent().dislodgeFrom(other.getParent(), horizontalDistance * dislodgeWeight, 0);
						other.getParent().dislodgeFrom(getParent(), -horizontalDistance * (1 - dislodgeWeight), 0);
					}
					return true;
				}
				return false;
			}

			//otherwise, check to see if the distance to the nearest rectangle corner is less than the circle's radius
			float horizontalDistance = (otherX > myX ? myRight - otherX : myLeft - otherX);
			float verticalDistance = (otherY > myY ? myBottom - otherY : myTop - otherY);
			float squareDistance = horizontalDistance * horizontalDistance + verticalDistance * verticalDistance;
			//same as checking to see if the square distance from the nearest rectangle corner is less than the square of the circle's radius
			if(squareDistance < otherRadius * otherRadius) {
				float distance = (float) Math.sqrt(squareDistance);
				float xNorm = horizontalDistance / distance;
				float yNorm = verticalDistance / distance;
				getParent().dislodgeFrom(other.getParent(),
						xNorm * (otherRadius - distance) * dislodgeWeight,
						yNorm * (otherRadius - distance) * dislodgeWeight);
				other.getParent().dislodgeFrom(getParent(),
						-xNorm * (otherRadius - distance) * (1 - dislodgeWeight),
						-yNorm * (otherRadius - distance) * (1 - dislodgeWeight));
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public Type getType() {
		return Type.RECTANGULAR;
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
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(getLeft(), getTop(), width, height);
	}
}
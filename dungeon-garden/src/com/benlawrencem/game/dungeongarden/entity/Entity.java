package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.Area;
import com.benlawrencem.game.dungeongarden.level.Level;

public abstract class Entity {
	protected static final int COLLISION_WEIGHT_CLASS_LIGHT = 0;
	protected static final int COLLISION_WEIGHT_CLASS_MEDIUM = 1;
	protected static final int COLLISION_WEIGHT_CLASS_HEAVY = 2;
	protected static final int COLLISION_WEIGHT_CLASS_IMMOVABLE = 3;

	public static float calculateCollisionScalar(Entity entity1, Entity entity2) {
		if(entity1.getCollisionWeightClass() > entity2.getCollisionWeightClass())
			return 0;
		if(entity1.getCollisionWeightClass() < entity2.getCollisionWeightClass())
			return 1;
		return 0.5f;
	}

	private Level level;
	private Area hitArea;
	private Area collisionArea;
	private float x;
	private float y;

	public Entity(Level level) {
		this(level, 0, 0);
	}

	public Entity(Level level, float x, float y) {
		this.level = level;
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void adjustX(float amt) {
		x += amt;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void adjustY(float amt) {
		y += amt;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Area getHitArea() {
		return hitArea;
	}

	public void setHitArea(Area hitArea) {
		if(hitArea != null)
			hitArea.setParent(this);
		this.hitArea = hitArea;
	}

	private boolean canHit(Entity other) {
		return getHitPrecedence(other) + other.getHitPrecedence(this) > 0;
	}

	protected int getHitPrecedence(Entity other) {
		return 1;
	}

	public boolean isHitting(Entity other) {
		if(getHitArea() != null && other.getHitArea() != null && canHit(other))
			return getHitArea().isIntersecting(other.getHitArea());
		return false;
	}

	public boolean checkForHit(Entity other) {
		if(getHitArea() != null && other.getHitArea() != null && canHit(other))
			return getHitArea().checkForHit(other.getHitArea());
		return false;
	}

	public abstract void onHit(Entity other);

	public Area getCollisionArea() {
		return collisionArea;
	}

	public void setCollisionArea(Area collisionArea) {
		if(collisionArea != null)
			collisionArea.setParent(this);
		this.collisionArea = collisionArea;
	}

	private boolean canCollideWith(Entity other) {
		return getCollisionPrecedence(other) + other.getCollisionPrecedence(this) > 0;
	}

	protected int getCollisionPrecedence(Entity other) {
		return 1;
	}

	public boolean isCollidingWith(Entity other) {
		if(getCollisionArea() != null && other.getCollisionArea() != null && canCollideWith(other))
			return getCollisionArea().isIntersecting(other.getCollisionArea());
		return false;
	}

	public boolean checkForCollision(Entity other) {
		if(getCollisionArea() != null && other.getCollisionArea() != null && canCollideWith(other))
			return getHitArea().checkForCollision(other.getHitArea());
		return false;
	}

	public void onCollision(Entity other, float overlapX, float overlapY) {
		adjustX(overlapX);
		adjustY(overlapY);
	}

	public abstract void update(int delta);

	public abstract void render(Graphics g);

	public void applyBounds(float left, float right, float top, float bottom) {
		applyLeftBound(left);
		applyRightBound(right);
		applyTopBound(top);
		applyBottomBound(bottom);
	}

	public void applyLeftBound(float left) {
		if(hitArea != null && hitArea.getLeft() < left)
			adjustX(left - hitArea.getLeft());
	}

	public void applyRightBound(float right) {
		if(hitArea != null && hitArea.getRight() > right)
			adjustX(right - hitArea.getRight());
	}

	public void applyTopBound(float top) {
		if(hitArea != null && hitArea.getTop() < top)
			adjustY(top - hitArea.getTop());
	}

	public void applyBottomBound(float bottom) {
		if(hitArea != null && hitArea.getBottom() > bottom)
			adjustY(bottom - hitArea.getBottom());
	}

	public float getTop() {
		return collisionArea.getTop();
	}

	public float getBottom() {
		return collisionArea.getBottom();
	}

	public float getLeft() {
		return collisionArea.getLeft();
	}

	public float getRight() {
		return collisionArea.getRight();
	}

	public Level getLevel() {
		return level;
	}

	public int getCollisionWeightClass() {
		return Entity.COLLISION_WEIGHT_CLASS_MEDIUM;
	}

	public boolean isImmobile() {
		return true;
	}
}
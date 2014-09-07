package com.splatform.physics;

public interface IFlyer {

	public boolean isFlying();
	
	public void setFlying(boolean isFlying);
	
	public float getFlyTime();
	
	public void setFlyTime(float flyTime);
	
	public float getMaxFlyTime();
	
	public void setMaxFlyTime(float maxFlyTime);
	
	public void resetFlyTime();
	
	public void fly(float delta);
}

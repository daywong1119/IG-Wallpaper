package com.daywong.igwallpaper;

public class MyPoint {
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	String text;
	private int x;
	private int y;

	public MyPoint(String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}
}
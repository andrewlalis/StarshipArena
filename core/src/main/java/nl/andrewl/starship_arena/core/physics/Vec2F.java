package nl.andrewl.starship_arena.core.physics;

/**
 * Standard 2-dimensional floating-point vector implementation.
 */
public final class Vec2F {
	public float x;
	public float y;

	public Vec2F(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2F(float n) {
		this(n, n);
	}

	public Vec2F() {
		this(0);
	}

	public Vec2F(Vec2F other) {
		this(other.x, other.y);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float dot(Vec2F other) {
		return x * other.x + y * other.y;
	}

	public Vec2F add(Vec2F other) {
		x += other.x;
		y += other.y;
		return this;
	}

	public Vec2F sub(Vec2F other) {
		x -= other.x;
		y -= other.y;
		return this;
	}

	public Vec2F mul(float factor) {
		x *= factor;
		y *= factor;
		return this;
	}

	public Vec2F div(float factor) {
		x /= factor;
		y /= factor;
		return this;
	}

	public Vec2F normalize() {
		return div(length());
	}

	public Vec2F toPolar() {
		float r = length();
		float theta = (float) Math.atan2(y, x);
		x = r;
		y = theta;
		return this;
	}

	public Vec2F toCartesian() {
		float cx = (float) (x * Math.cos(y));
		float cy = (float) (x * Math.sin(y));
		x = cx;
		y = cy;
		return this;
	}

	@Override
	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}
}

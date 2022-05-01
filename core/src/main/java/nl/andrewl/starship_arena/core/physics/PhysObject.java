package nl.andrewl.starship_arena.core.physics;

public abstract class PhysObject {
	public Vec2F pos;
	public Vec2F vel;
	public float rotation;
	public float rotationSpeed;

	public void setRotationNormalized(float rotation) {
		while (rotation < 0) rotation += 2 * Math.PI;
		while (rotation > 2 * Math.PI) rotation -= 2 * Math.PI;
		this.rotation = rotation;
	}

	public float[] toArray() {
		return new float[]{
				pos.x, pos.y, vel.x, vel.y, rotation, rotationSpeed
		};
	}

	public void fromArray(float[] values) {
		pos.x = values[0];
		pos.y = values[1];
		vel.x = values[2];
		vel.y = values[3];
		rotation = values[4];
		rotationSpeed = values[5];
	}
}

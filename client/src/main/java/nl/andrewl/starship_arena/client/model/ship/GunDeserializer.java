package nl.andrewl.starship_arena.client.model.ship;

import com.google.gson.*;

import java.awt.geom.Point2D;
import java.lang.reflect.Type;

public class GunDeserializer implements JsonDeserializer<Gun> {
	@Override
	public Gun deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = jsonElement.getAsJsonObject();
		return new Gun(
				obj.get("name").getAsString(),
				ctx.deserialize(obj.get("location"), Point2D.Float.class),
				(float) Math.toRadians(obj.get("rotation").getAsFloat()),
				(float) Math.toRadians(obj.get("maxRotation").getAsDouble()),
				(float) Math.toRadians(obj.get("minRotation").getAsFloat()),
				obj.get("barrelWidth").getAsFloat(),
				obj.get("barrelLength").getAsFloat()
		);
	}
}

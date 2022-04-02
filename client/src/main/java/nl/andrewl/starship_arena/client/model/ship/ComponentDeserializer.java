package nl.andrewl.starship_arena.client.model.ship;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom deserializer that's used to deserialize components based on their
 * "type" property.
 */
public class ComponentDeserializer implements JsonDeserializer<ShipComponent> {
	@Override
	public ShipComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = jsonElement.getAsJsonObject();
		String componentTypeName = obj.get("type").getAsString();
		Type componentType = switch (componentTypeName) {
			case "panel" -> Panel.class;
			case "cockpit" -> Cockpit.class;
			case "gun" -> Gun.class;
			default -> throw new JsonParseException("Invalid ship component type: " + componentTypeName);
		};
		return ctx.deserialize(obj, componentType);
	}
}

package nl.andrewl.starship_arena.server.api;

import lombok.RequiredArgsConstructor;
import nl.andrewl.starship_arena.server.SocketGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/gateway")
@RequiredArgsConstructor
public class GatewayController {
	private final SocketGateway gateway;

	@GetMapping
	public Object getInfo() {
		return Map.of(
				"tcp", gateway.getTcpPort(),
				"udp", gateway.getUdpPort()
		);
	}
}

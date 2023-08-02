package ru.mrnightfury.autologger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class AutoLoggerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommandsManager.load();
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (!client.isInSingleplayer()) {
				String command = CommandsManager.getCommand(client.getCurrentServerEntry().address);
				if (command != null) {
//					handler.sendChatMessage(command);
					handler.sendChatCommand(command);
				}
			}

//			handler.sendChatMessage("Single" + String.valueOf(client.isInSingleplayer()));
//			client.getNetworkHandler().sendChatMessage("I'm here!");
		});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			Log.debug(LogCategory.LOG, "JOIN");
		});
		ClientPlayConnectionEvents.INIT.register((a, b) -> {
			Log.debug(LogCategory.LOG, "JOIN");
		});

//		UseItemCallback.EVENT.register(((player, world, hand) -> {
////			player.sendMessage(player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getName());
//			MinecraftClient.getInstance().getNetworkHandler().sendChatMessage("asd");
//			return new TypedActionResult<>(ActionResult.SUCCESS, player.getEquippedStack(EquipmentSlot.MAINHAND));
//		}));
	}
}

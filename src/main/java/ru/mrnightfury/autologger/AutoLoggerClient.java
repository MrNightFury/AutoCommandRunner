package ru.mrnightfury.autologger;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;

public class AutoLoggerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommandsManager.load();
		ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
			Log.info(LogCategory.LOG, "ChatMessage: " + message.getContent().getString());
		});
		ServerMessageEvents.COMMAND_MESSAGE.register((message, sender, params) -> {
			Log.info(LogCategory.LOG, "CommandMessage: " + message.getContent().getString());
		});
		ServerMessageEvents.GAME_MESSAGE.register((server, message, overlay) -> {
			Log.info(LogCategory.LOG, "GameMessage: " + message.getString());
		});
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (!client.isInSingleplayer()) {

				String command = CommandsManager.getCommand(client.getCurrentServerEntry().address);
				if (command != null) {
					handler.sendChatCommand(command);
//					ClientCommandManager
//					client.getServer().getCommandManager()
//							.executeWithPrefix(client.player.getCommandSource(), command);
//					try {
//						ClientCommandManager.getActiveDispatcher().execute("login aaaa",(FabricClientCommandSource) client.getNetworkHandler().getCommandSource());
//					} catch (CommandSyntaxException e) {
//						Log.info(LogCategory.LOG, "asd");
//					}
//					IntegratedServer serv = MinecraftClient.getInstance().getServer();
//					serv.getCommandManager()
//							.execute(serv.getCommandSource(), "/login aaaa");
//					client.player.sendMessage(Text.of("/login aaaa"));
				}
			}
		});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			Log.info(LogCategory.LOG, "JOIN");
		});
		ClientPlayConnectionEvents.INIT.register((a, b) -> {
			Log.info(LogCategory.LOG, "INIT");
		});

//		UseItemCallback.EVENT.register(((player, world, hand) -> {
////			player.sendMessage(player.getEquippedStack(EquipmentSlot.MAINHAND).getItem().getName());
//			MinecraftClient.getInstance().getNetworkHandler().sendChatMessage("asd");
//			return new TypedActionResult<>(ActionResult.SUCCESS, player.getEquippedStack(EquipmentSlot.MAINHAND));
//		}));
	}
}

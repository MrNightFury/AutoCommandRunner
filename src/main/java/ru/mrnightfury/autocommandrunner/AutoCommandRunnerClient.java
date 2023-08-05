package ru.mrnightfury.autocommandrunner;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class AutoCommandRunnerClient implements ClientModInitializer {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onInitializeClient() {
		CommandsManager.load();
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (!client.isInSingleplayer()) {
				String command = CommandsManager.getCommand(client.getCurrentServerEntry().address);
				if (command != null) {
					if (ClientCommandInternals.getActiveDispatcher() == null) {
						new Thread(() -> {
							int time = 0;
							Log.info(LogCategory.LOG, "Waiting for dispatcher...");
							while (ClientCommandInternals.getActiveDispatcher() == null) {
								time++;
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}
							}
							Log.info(LogCategory.LOG, "Time elapsed: " + time + "0");
							handler.sendChatCommand(command);
						}).start();
					} else {
						handler.sendChatCommand(command);
					}
				}
			}
		});
	}
}

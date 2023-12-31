package ru.mrnightfury.autocommandrunner;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ServerList;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class CommandsManager {
	private static File file;
	private static final ArrayList<Command> commands = new ArrayList<>();
	private static void prepareConfigFile() {
		if (file != null) {
			return;
		}
		file = new File(FabricLoader.getInstance().getConfigDir().toFile(), AutoCommandRunner.MOD_ID + ".json");
	}

	public static void load() {
		prepareConfigFile();
		if (!file.exists()) {
			Log.info(LogCategory.LOG, "File not found, creating...");
			save();
		}
		ServerList list = new ServerList(MinecraftClient.getInstance());
		list.loadFile();

		try {
			commands.clear();
			BufferedReader br = new BufferedReader(new FileReader(file));
			JsonArray arr = new Gson().fromJson(br, JsonArray.class);
			boolean isOutdated = false;
			for (JsonElement link : arr.asList()) {
				JsonObject obj = link.getAsJsonObject();
				String address = obj.get("address").getAsString();
				String command = obj.get("command").getAsString();
				if (list.get(address) != null) {
					commands.add(new Command(address,
							command.charAt(0) == '/' ? command.substring(1) : command));
				} else {
					isOutdated = true;
				}
			}
			if (isOutdated) {
				Log.info(LogCategory.LOG, "Server commands file outdated, rewriting");
				save();
			}
		} catch (Exception e) {
			Log.error(LogCategory.LOG, "Couldn't load server commands file");
			Log.error(LogCategory.LOG, e.getMessage());
		}
	}

	public static void save() {
		prepareConfigFile();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = g.toJson(commands);

		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(jsonString);
			Log.info(LogCategory.LOG, "Successfully saved commands");
		} catch (IOException e) {
			System.err.println("Couldn't save Commands list");
			Log.error(LogCategory.LOG, e.getMessage());
		}
	}

	public static String getCommand(String address) {
		Command com = findCommandDesc(address);
		return com == null ? null : com.command;
	}

	private static Command findCommandDesc (String address) {
		for (Command com : commands) {
			if (Objects.equals(com.address, address)) {
				return com;
			}
		}
		return null;
	}

	public static void addCommand(String address, String command) {
		Command com = findCommandDesc(address);
		if (com != null) {
			commands.remove(com);
		}
		commands.add(new Command(address, command));
		Log.info(LogCategory.LOG, "Command added, saving...");
		save();
	}

	private static class Command {
		@Expose
		String address;
		@Expose
		String command;

		public Command(String address, String command) {
			this.address = address;
			this.command = command;
		}
	}
}

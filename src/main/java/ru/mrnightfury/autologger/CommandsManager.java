package ru.mrnightfury.autologger;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.terraformersmc.modmenu.ModMenu;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;

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
		file = new File(FabricLoader.getInstance().getConfigDir().toFile(), AutoLogger.MOD_ID + ".json");
	}

	public static void load() {
		prepareConfigFile();
		if (!file.exists()) {
			save();
		}
		try {
			if (file.exists()) {
				commands.clear();
				BufferedReader br = new BufferedReader(new FileReader(file));
				JsonArray arr = new Gson().fromJson(br, JsonArray.class);
//				new JsonParser().parse(br).getAsJsonArray();
				for (JsonElement link : arr.asList()) {
					JsonObject obj = link.getAsJsonObject();
					String command = obj.get("command").getAsString();
					commands.add(new Command(obj.get("address").getAsString(),
							command.charAt(0) == '/' ? command.substring(1) : command));
				}
			}
		} catch (Exception e) {
			Log.error(LogCategory.LOG, "Couldn't load ");
			e.printStackTrace();
		}
	}

	public static void save() {
//		ModMenu.clearModCountCache();
		prepareConfigFile();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = g.toJson(commands);
//		Log.log(LogLevel.INFO, LogCategory.LOG, jsonString);

		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(jsonString);
			Log.info(LogCategory.LOG, "Successfully saved commands");
		} catch (IOException e) {
			System.err.println("Couldn't save Commands list");
			e.printStackTrace();
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

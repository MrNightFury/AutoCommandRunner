package ru.mrnightfury.autologger;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.text.Text;

import java.io.*;

public class ModMenuLoader implements ModMenuApi {
	private static File file;
	private static void prepareConfigFile() {
		if (file != null) {
			return;
		}
		file = new File(FabricLoader.getInstance().getConfigDir().toFile(), AutoLogger.MOD_ID + ".json");
	}

	private static void load() {
//		prepareConfigFile();
//		if (!file.exists()) {
//			save();
//		}
//		try {
//			if (file.exists()) {
//				BufferedReader br = new BufferedReader(new FileReader(file));
//				JsonArray arr = new JsonParser().parse(br).getAsJsonArray();
//				for (JsonElement link : arr.asList()) {
//					JsonObject obj = link.getAsJsonObject();
//					Log.log(LogLevel.INFO, LogCategory.LOG, obj.toString());
//				}
//			}
//		} catch (Exception e) {
//			Log.error(LogCategory.LOG, "Couldn't load Mod Menu configuration file; reverting to defaults");
//			e.printStackTrace();
//		}
	}

	private static void save() {
//		ModMenu.clearModCountCache();
//		prepareConfigFile();
//
//		JsonArray commands = new JsonArray();
//
//		String jsonString = ModMenu.GSON.toJson(commands);
//
//		try (FileWriter fileWriter = new FileWriter(file)) {
//			fileWriter.write(jsonString);
//		} catch (IOException e) {
//			System.err.println("Couldn't save Mod Menu configuration file");
//			e.printStackTrace();
//		}
	}



	String text = "";
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setTitle(Text.translatable("ru.mrnightfury.configScreenTitle"))
					.setSavingRunnable(() -> {
						load();
						Log.log(LogLevel.INFO, LogCategory.LOG, text);
					});
			ConfigCategory category = builder.getOrCreateCategory(Text.translatable("ru.mrnightfury.configCategoryTitle"));
			category.addEntry(builder.entryBuilder()
					.startStrField(Text.translatable("ru.mrnightfury.configEntryTitle"), "asd")
					.setDefaultValue("Text")
					.setSaveConsumer(newValue -> text = newValue)
					.build());
			return builder.build();
		};
	}
}

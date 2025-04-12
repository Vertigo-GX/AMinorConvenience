package vertigo.aminorconvenience;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {

	private static final String SEPARATOR = " = ";
	private static final String SWAP_FLOWERS = "swapFlowers";
	private static final String CYCLE_PAINTINGS = "cyclePaintings";
	private static final String SEAMLESS_CREATIVE_FLIGHT = "seamlessCreativeFlight";
	private static final String ENCHANTMENT_NAMES = "enchantmentNames";

	public boolean swapFlowers = true;
	public boolean cyclePaintings = true;
	public boolean seamlessCreativeFlight = false;
	public boolean enchantmentNames = false;

	public Config() {
		if (!read()) {
			write();
		}
	}

	public void write() {
		File file = getFile();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(SWAP_FLOWERS + SEPARATOR + swapFlowers + System.lineSeparator());
			writer.write(CYCLE_PAINTINGS + SEPARATOR + cyclePaintings + System.lineSeparator());
			writer.write(SEAMLESS_CREATIVE_FLIGHT + SEPARATOR + seamlessCreativeFlight + System.lineSeparator());
			writer.write(ENCHANTMENT_NAMES + SEPARATOR + enchantmentNames);
		} catch (IOException e) {
			AMinorConvenience.LOGGER.error("Failed to write config ({})", file.getPath());
		}
	}

	public boolean read() {
		File file = getFile();
		if (!file.exists()) {
			return false;
		}
		try (BufferedReader reader = new BufferedReader((new FileReader(file)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] segments = line.split(SEPARATOR);
				if (segments.length != 2 || segments[0].isEmpty() || segments[1].isEmpty()) {
					continue;
				}
				switch (segments[0]) {
					case SWAP_FLOWERS -> swapFlowers = segments[1].equals("true");
					case CYCLE_PAINTINGS -> cyclePaintings = segments[1].equals("true");
					case SEAMLESS_CREATIVE_FLIGHT -> seamlessCreativeFlight = segments[1].equals("true");
					case ENCHANTMENT_NAMES -> enchantmentNames = segments[1].equals("true");
				}
			}
		} catch (IOException e) {
			AMinorConvenience.LOGGER.error("Failed to read config ({})", file.getPath());
		}
		return true;
	}

	private File getFile() {
		return FabricLoader.getInstance().getGameDir().resolve("config").resolve(AMinorConvenience.MOD_ID + ".ini").toFile();
	}

}
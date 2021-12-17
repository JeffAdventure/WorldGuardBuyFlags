package wgbuyflags;

import org.bukkit.plugin.java.JavaPlugin;

import wgbuyflags.commands.Commands;

public class WGBuyFlags extends JavaPlugin {

	@Override
	public void onEnable() {
		Config config = new Config(this);
		config.loadConfig();
		getCommand("buyflag").setExecutor(new Commands(config));
	}

}

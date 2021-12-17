package wgbuyflags.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import wgbuyflags.Config;
import wgbuyflags.utils.Validate;

public class ListPriceCommand implements SubCommand {

	private final Config config;
	public ListPriceCommand(Config config) {
		this.config = config;
	}

	@Override
	public void execute(CommandSender sender, String[] args)  throws Exception {
		Validate.isTrue(sender.hasPermission("buyflags.use"), ChatColor.RED + "У вас нет к этому доступа!");
		config.getFlagCosts().forEach((flag, cost) -> sender.sendMessage(ChatColor.YELLOW + "Флаг: "+flag + " | Цена: " + cost+" игр."));
	}

}

package wgbuyflags.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import wgbuyflags.Config;
import wgbuyflags.utils.Validate;

public class Commands implements CommandExecutor {

	private final HashMap<String, SubCommand> subcmds = new HashMap<>();

	public Commands(Config config) {
		this.subcmds.put("buy", new BuyFlagCommand(config));
		this.subcmds.put("list", new ListPriceCommand(config));
		this.subcmds.put("reload", new ReloadConfigCommand(config));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		SubCommand subcmd;
		try {
			subcmd = Validate.notNull(subcmds.get(args[0].toLowerCase()), ChatColor.RED + "Данной команды не существует!");
			subcmd.execute(sender, Arrays.copyOfRange(args, 1, args.length));

		} catch (Validate.InvalidateException e) {
			sender.sendMessage(e.getMessage());
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Используйте команду: /buyflag <buy|list>");
			sender.sendMessage(ChatColor.RED + "Если хотите приобрести флаг: /buyflag buy <Название региона> <Флаг> <ALLOW | DENY>");
			sender.sendMessage(ChatColor.RED + "Посмотреть доступные флаги: /buyflag list");
		}
		return true;
	}

}

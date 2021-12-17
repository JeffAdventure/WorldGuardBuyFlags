package wgbuyflags.commands;

import java.util.Arrays;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wgbuyflags.Config;
import wgbuyflags.utils.Utils;
import wgbuyflags.utils.Validate;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BuyFlagCommand implements SubCommand {

	private static final Economy econ = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();

	private final Config config;
	public BuyFlagCommand(Config config) {
		this.config = config;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws Exception {
		try {
			Validate.isTrue(sender.hasPermission("buyflags.use"), ChatColor.RED + "У вас нет к этому доступа!");
			Validate.isTrue(args.length >= 3, ChatColor.RED + "§4§lНедостаточно аргументов.§r Пример: §e/buyflag buy §l"+args[0]+"§r §e<§lflag§r§e> <§e§lALLOW§r §e┃§r §e§lDENY§r§e>§r");
			Player player = Validate.cast(() -> (Player) sender, ChatColor.RED + "Only for players");

			RegionManager rm = Validate.notNull(WGBukkit.getRegionManager(player.getWorld()), ChatColor.RED + "Регионы выключены в этом мире");
			ProtectedRegion region = Validate.notNull(rm.getRegion(args[0].toLowerCase()), ChatColor.RED + "Региона " + args[0].toLowerCase() + " не существует");
			Validate.isTrue(region.isOwner(WGBukkit.getPlugin().wrapPlayer(player, true)), ChatColor.RED + "Вы не являетесь владельцем региона " + region.getId());

			Flag<?> flag = Validate.notNull(Utils.findFlag(args[1].toLowerCase()), ChatColor.RED + "Флаг "+args[1].toLowerCase()+" не продается");
			Double cost = Validate.notNull(config.getFlagCost(flag.getName()), ChatColor.RED + "Данный флаг не доступен для покупки");
			Validate.isTrue(econ.getBalance(player) >= cost, ChatColor.RED + "У вас не хватает денег для покупки флага "+args[1].toLowerCase());

			econ.withdrawPlayer(player, cost);
			Utils.setFlag(region, flag, String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
			sender.sendMessage(ChatColor.YELLOW + "Флаг был успешно куплен и установлен");
		} catch (CommandException | InvalidFlagFormat e) {
			sender.sendMessage(ChatColor.RED + "Невозможно установить флаг. Ошибка № "+e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

}

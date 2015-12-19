package com.mistphizzle.voteranker;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteCommand {

	static VoteRanker plugin;
	
	public VoteCommand(VoteRanker plugin) {
		this.plugin = plugin;
		init();
	}
	
	private void init() {
		PluginCommand voteranker = plugin.getCommand("voteranker");
		CommandExecutor exe;
		
		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				if (args.length > 1) {
					s.sendMessage(ChatColor.RED + "Proper Usage: /vr [Player]");
					return true;
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (!s.hasPermission("voteranker.reload")) {
						s.sendMessage(ChatColor.RED + "You don't have permission to do that.");
						return true;
					}
					
					plugin.reloadConfig();
					s.sendMessage(ChatColor.GREEN + "Config reloaded.");
				}
				if (args.length == 1) {
					if (!s.hasPermission("voteranker.check.others")) {
						s.sendMessage(ChatColor.RED + "You don't have permission to do that.");
						return true;
					}
				}
				if (!s.hasPermission("voteranker.check")) {
					s.sendMessage(ChatColor.RED + "You don't have permission to do that.");
					return true;
				}
				
				String playerName = null;
				if (args.length == 0) {
					playerName = s.getName();
				} else {
					playerName = args[0];
				}
				
				
				OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
				if (player == null) {
					s.sendMessage(ChatColor.RED + "Unable to find " + playerName + ", are you sure they've played here?");
					return true;
				}
				
				s.sendMessage(ChatColor.GREEN + player.getName() + " has " + Methods.getVotes(player.getUniqueId()) + " votes logged.");
				
				return true;
			}
		}; voteranker.setExecutor(exe);
	}
}

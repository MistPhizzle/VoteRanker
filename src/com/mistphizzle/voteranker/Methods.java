package com.mistphizzle.voteranker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Methods {

	public static VoteRanker plugin;
	
	public Methods(VoteRanker plugin) {
		this.plugin = plugin;
	}
	
	public static boolean isInTable(UUID uuid) {
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM voters WHERE uuid = '" + uuid.toString() + "'");
		try {
			if (rs2.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addToTable(UUID uuid) {
		if (isInTable(uuid)) return; // We don't want the same UUID in there twice.
		DBConnection.sql.modifyQuery("INSERT INTO voters (uuid, votes) VALUES ('" + uuid.toString() + "', 0)");
	}
	
	public static int getVotes(UUID uuid) {
		if (!isInTable(uuid)) return 0;
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM voters WHERE uuid = '" + uuid.toString() + "'");
		try {
			if (rs2.next()) {
				return rs2.getInt("votes");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void addVote(UUID uuid) {
		if (!isInTable(uuid)) addToTable(uuid);
		int votes = getVotes(uuid) + 1;
		DBConnection.sql.modifyQuery("UPDATE voters SET votes = " + votes + " WHERE uuid = '" + uuid.toString() + "'");
	}
	
	public static void checkVote(UUID uuid) {
		if (!isInTable(uuid)) addToTable(uuid);
		int votes = getVotes(uuid);
		
		OfflinePlayer player = Bukkit.getPlayer(uuid);
		for (String rank: plugin.getConfig().getConfigurationSection("").getKeys(false)) {
			if (plugin.getConfig().getInt(rank + ".votes") == votes) { // Player has reached number of votes for package.
				List<String> ignoreif = plugin.getConfig().getStringList(rank + ".ignoreif");
				for (String group: ignoreif) {
					if (VoteRanker.permission.playerInGroup(null, player, group)) {
						return;
					}
				}
				List<String> commands = plugin.getConfig().getStringList(rank + ".commands");
				for (String command: commands) {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player", player.getName()));
				}
			}
		}
	}
}

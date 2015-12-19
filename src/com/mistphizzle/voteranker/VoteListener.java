package com.mistphizzle.voteranker;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener {

	VoteRanker plugin;
	
	public VoteListener(VoteRanker plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (!Methods.isInTable(uuid)) {
			Methods.addToTable(uuid);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		
		OfflinePlayer op = Bukkit.getOfflinePlayer(vote.getUsername());
		if (op == null) {
			plugin.log.info("Failed to log vote for " + vote.getUsername() + ", doesn't appear that they have played on this server before!");
			return;
		}
		
		UUID uuid = op.getUniqueId();
		if (!Methods.isInTable(uuid)) Methods.addToTable(uuid);
		
		Methods.addVote(uuid);
		Methods.checkVote(uuid);
		
	}
}

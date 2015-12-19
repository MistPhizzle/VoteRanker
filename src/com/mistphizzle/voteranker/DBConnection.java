package com.mistphizzle.voteranker;

import com.mistphizzle.voteranker.sqls.Database;
import com.mistphizzle.voteranker.sqls.SQLite;

public class DBConnection {

	public static Database sql;
	
	public static void init() {
			sql = new SQLite(VoteRanker.log, "[VoteRanker] Establishing SQLite Connection.", "voters.db", VoteRanker.plugin.getDataFolder().getAbsolutePath());
			((SQLite) sql).open();

			if (!sql.tableExists("voters")) {
				VoteRanker.log.info("Creating voter table.");
				String query = "CREATE TABLE `voters` ("
						+ "`id` INTEGER PRIMARY KEY,"
						+ "`uuid` TEXT(255),"
						+ "`votes` INTEGER(255));";
				sql.modifyQuery(query);
			}
		}
	}


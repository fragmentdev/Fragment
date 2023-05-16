package me.xemu.fragment.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Interaction implements Listener {
	private Plugin plugin;
	private Map<UUID, CompletableFuture<String>> awaitingAnswers;

	public Interaction(Plugin plugin) {
		this.plugin = plugin;
		this.awaitingAnswers = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public CompletableFuture<String> startInteraction(Player player, String question) {
		CompletableFuture<String> future = new CompletableFuture<>();

		player.sendTitle(ChatColor.GREEN + "§l" + question, "Write your answer in the chat.");

		awaitingAnswers.put(player.getUniqueId(), future);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!future.isDone()) {
					future.complete(null);
					awaitingAnswers.remove(player.getUniqueId());
				}
			}
		}.runTaskLater(plugin, 20 * 30); // Adjust the delay (in ticks) to the desired timeout period

		return future;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		CompletableFuture<String> future = awaitingAnswers.get(playerUUID);
		if (future != null) {
			String answer = event.getMessage();
			future.complete(answer);
			player.resetTitle();

			awaitingAnswers.remove(playerUUID);
			event.setCancelled(true);
		}
	}
}

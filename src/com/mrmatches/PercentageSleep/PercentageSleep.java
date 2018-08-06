package com.mrmatches.PercentageSleep;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PercentageSleep extends JavaPlugin implements Listener {
    private List<Player> inBedPlayers = new ArrayList<>();
    private int PercentageSetting;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        PercentageSetting = getConfig().getInt("Percentage");
    }

    @EventHandler
    public void PlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player triggerPlayer = event.getPlayer();
        inBedPlayers.add(triggerPlayer);
        if (!checkSkip()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + "[系統] " + ChatColor.YELLOW + triggerPlayer.getDisplayName() + "累了想睡覺～");
            }
            announce();
        }
    }

    @EventHandler
    public void PlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        Player triggerPlayer = event.getPlayer();
        inBedPlayers.remove(triggerPlayer);
        if (!checkSkip()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + "[系統] " + ChatColor.YELLOW + triggerPlayer.getDisplayName() + "睡不着起牀了");
            }
            announce();
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (!checkSkip()) {
            announce();
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (!checkSkip()) {
            announce();
        }
    }

    private void announce() {
        int Players = this.getServer().getOnlinePlayers().size();
        int SleptPlayers = inBedPlayers.size();
        float Percentage = ((float) SleptPlayers / (float) Players) * 100;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.RED + "[系統] " + ChatColor.YELLOW + "暫時有" + SleptPlayers + "個玩家想睡覺，佔全體 " + (int) Percentage + "%");
            player.sendMessage(ChatColor.RED + "[系統] " + ChatColor.YELLOW + "距離目標還差 " + (int) (PercentageSetting - Percentage) + "%");
        }
    }

    private boolean checkSkip() {
        if (inBedPlayers.size() == 0) return true;
        int Players = this.getServer().getOnlinePlayers().size();
        int SleptPlayers = inBedPlayers.size();
        float Percentage = ((float) SleptPlayers / (float) Players) * 100;
        getLogger().info("" + Percentage);
        if (Percentage >= (float) PercentageSetting) {
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                world.setTime(1000);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + "[系統] " + ChatColor.YELLOW + "早晨啊咁多位");
            }
            inBedPlayers.clear();
            return true;
        } else return false;
    }
}

package me.pjsph.inspectoruhc.listeners;

import me.pjsph.inspectoruhc.InspectorUHC;
import me.pjsph.inspectoruhc.events.GameStartsEvent;
import me.pjsph.inspectoruhc.teams.Team;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameListener implements Listener {
    private InspectorUHC plugin;

    public GameListener(InspectorUHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        if(!plugin.getGameManager().hasStarted()) {
            plugin.getGameManager().initPlayer(ev.getPlayer());

            plugin.getRulesManager().displayRulesTo(ev.getPlayer());
        }

        /* TODO scoreboard manager */

        if(plugin.getGameManager().hasStarted() && !plugin.getGameManager().getAlivePlayers().contains(ev.getPlayer())) {
            plugin.getSpectatorsManager().setSpectating(ev.getPlayer(), true);
        }
        plugin.getGameManager().updatePlayer(ev.getPlayer());

        if(!plugin.getGameManager().hasStarted()) {
            ev.getPlayer().getInventory().clear();

            ev.getPlayer().setGameMode(ev.getPlayer().isOp() ? GameMode.CREATIVE : GameMode.ADVENTURE);
            ev.getPlayer().teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation().add(0, 1, 0));
        } else {
            if(Team.getTeamForPlayer(ev.getPlayer()) != null) {
                ev.getPlayer().loadData();
            } else {
                ev.getPlayer().setGameMode(GameMode.SPECTATOR);
                ev.getPlayer().teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
            }
        }

        plugin.getScoreboardManager().matchInfo();
    }

    @EventHandler
    public void onGameStarts(GameStartsEvent ev) {
        plugin.getBorderManager().scheduleBorderReduction();
    }

}
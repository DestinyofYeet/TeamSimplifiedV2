package de.uwuwhatsthis.TeamsSimplifiedV2.teams;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uwuwhatsthis.TeamsSimplifiedV2.extensions.Bluemap.ExtensionBlueMap;
import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Defaults;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TeamManager {
    private ArrayList<Team> allTeams;

    private ExtensionBlueMap extensionBlueMap;
    private BukkitTask task;

    public TeamManager(){
        allTeams = new ArrayList<>();
    }

    public Team getTeamByPlayer(Player player){
        UUID pUUID = player.getUniqueId();
        for (Team team : allTeams) {
            if (team.getOwner().equals(pUUID)
                    || team.getModerators().contains(pUUID)
                    || team.getPlayers().contains(pUUID)
            ) {
                return team;
            }
        }

        return null;
    }

    public boolean isChuckClaimed(int chunkX, int chunkZ, UUID worldID){
        return isChunkClaimed(new Chunk(chunkX, chunkZ, worldID));
    }

    public boolean isChunkClaimed(Chunk chunk){
        for (Team team: allTeams){
            for (Chunk chunk1: team.getClaimedChunks()){
                if (chunk.compareTo(chunk1) == 0){
                    return true;
                }
            }
        }

        return false;
    }

    public Team chunkIsClaimedBy(Chunk chunk){
        for (Team team: allTeams){
            for (Chunk chunk1: team.getClaimedChunks()){
                if (chunk.compareTo(chunk1) == 0){
                    return team;
                }
            }
        }

        return null;
    }

    public Team getTeamByName(String name){
        for (Team team: allTeams){
            if (team.getName().equals(name)){
                return team;
            }
        }

        return null;
    }

    public Errors createTeam(String name, String tag, String color, Player player){
        if (getTeamByPlayer(player) != null){
            return Errors.PLAYER_ALREADY_IN_TEAM;
        }

        for (Team team: allTeams){
            if (team.getName().equals(name)){
                return Errors.TEAM_NAME_ALREADY_USED;
            }

            if (team.getTag().equals(tag)){
                return Errors.TEAM_TAG_ALREADY_USED;
            }
        }

        Team newTeam = getNewDefaultTeam(name, tag, color, player);
        allTeams.add(newTeam);

        Main.getManager().updateDisplayName(player);
        saveTeams();
        triggerRerender();
        return Errors.SUCCESS;
    }

    public Errors deleteTeam(Team team){
        for (UUID uuid: team.getCompleteListOfPlayers()){
            Player player = Main.getPlugin().getServer().getPlayer(uuid);

            if (player != null){
                player.sendMessage("§aThe team '§6" + team.getName() + "§a' got deleted!");
                clearDisplayName(player);
            }
        }

        allTeams.remove(team);
        saveTeams();
        return Errors.SUCCESS;
    }

    public Errors unclaimChunk(Team team, Chunk chunk) {
        if (!isChunkClaimed(chunk)){
            return Errors.CHUNK_NOT_CLAIMED;
        }

        if (team.compareTo(chunkIsClaimedBy(chunk)) != 0){
            return Errors.CHUNK_NOT_CLAIMED;
        }

        team.getClaimedChunks().removeIf(chunk1 -> (chunk1.compareTo(chunk) == 0));

        saveTeams();
        triggerRerender();
        return Errors.SUCCESS;
    }

    public Errors claimChunk(Team team, Chunk chunk){

        if (isChunkClaimed(chunk)){
            return Errors.CHUNK_ALREADY_CLAIMED;
        }

        team.getClaimedChunks().add(chunk);

        saveTeams();
        triggerRerender();
        return Errors.SUCCESS;
    }

    public void saveTeams(){
        Gson gson = getGson();

        try (FileWriter writer = new FileWriter(Defaults.DATA_FILE_NAME.getValue())){
            gson.toJson(Main.getManager(), writer);

        } catch (IOException ex){
            Main.getPlugin().getLogger().severe("Failed to save teams!\n" + ex.getMessage());
        }
    }

    public void updateDisplayName(Player player){
        updateDisplayName(player, null);
    }

    public void updateDisplayName(Player player, Team team){
        if (team == null){
            team = getTeamByPlayer(player);
        }
        if (team == null) return;

        boolean showInTab = Main.getPlugin().getConfig().getBoolean(Defaults.CONFIG_SHOW_FANCY_TAB_NAME.getValue());

        String color = team.getColor().replace("&", "§");

        player.setDisplayName("[" + color + team.getTag() + "§r] " + player.getName());
        if (showInTab){
            player.setPlayerListName("[" + color + team.getTag() + "§r] " + player.getName());
        }
    }

    public void clearDisplayName(Player player){
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    public void triggerRerender(){
        if (Main.getPlugin().getConfig().getBoolean(Defaults.CONFIG_EXTENSIONS_ENABLE_BLUEMAP.getValue())){
            if (extensionBlueMap == null){
                extensionBlueMap = new ExtensionBlueMap();
            }

            if (task == null){
                task = Main.getPlugin().getServer().getScheduler().runTaskAsynchronously(
                        Main.getPlugin(), () -> {
                            Main.getPlugin().getLogger().info("Started async render thread");
                            extensionBlueMap.triggerRender();
                        }
                );
            } else {
                extensionBlueMap.triggerRender();
            }
        }

    }

    private static Gson getGson(){
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                if (field.getDeclaringClass() == TeamManager.class) {
                    switch (field.getName()){
                        case "task", "extensionBlueMap": return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };


        return new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(strategy)
                .create();
    }

    public static TeamManager loadTeams(){
        Gson gson = getGson();

        try (FileReader reader = new FileReader(Defaults.DATA_FILE_NAME.getValue())){
            return gson.fromJson(reader, TeamManager.class);

        } catch (IOException ex){
//            Main.getPlugin().getLogger().severe("Failed to load teams!\n" + ex.getMessage());
        }

        return null;
    }

    private Team getNewDefaultTeam(String name, String tag, String color, Player owner){
        return new Team(name, tag, color, new ArrayList<>(), new ArrayList<>(), owner.getUniqueId(), false, new ArrayList<>(), new ArrayList<>());
    }

    public ArrayList<Team> getAllTeams() {
        return allTeams;
    }

    public ArrayList<String> getAllTeamNames(){
        return new ArrayList<>(allTeams.size()){{
            for (Team team: allTeams) add(team.getName());
        }};
    }

    public void stopRenderTask(){
        this.task.cancel();
        extensionBlueMap.shutdown();
    }

    public BukkitTask getTask() {
        return task;
    }
}

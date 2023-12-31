package de.uwuwhatsthis.TeamsSimplifiedV2.teams;

import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Errors;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.UUID;

public class Team implements Comparable<Team> {

    private String name, tag, color;

    private ArrayList<UUID> players;
    private ArrayList<UUID> moderators;
    private UUID owner;
    private boolean isOpen, explosionsEnabled;

    private ArrayList<Chunk> claimedChunks;
    private ArrayList<UUID> invitedPlayers;


    public Team(String name, String tag, String color, ArrayList<UUID> players, ArrayList<UUID> moderators, UUID owner, boolean isOpen, ArrayList<Chunk> claimedChunks, ArrayList<UUID> invitedPlayers, boolean explosionsEnabled){
        this.name = name;
        this.tag = tag;
        this.color = color;

        this.players = players;
        this.moderators = moderators;
        this.owner = owner;

        this.isOpen = isOpen;

        this.claimedChunks = claimedChunks;
        this.invitedPlayers = invitedPlayers;

        this.explosionsEnabled = explosionsEnabled;
    }

    public Errors claimChunk(org.bukkit.Chunk chunk, Player player){
        return claimChunk(chunk.getX(), chunk.getZ(), player);
    }

    public Errors unclaimChunk(org.bukkit.Chunk chunk, Player player){
        return unclaimChunk(chunk.getX(), chunk.getZ(), player);
    }

    public Errors unclaimAllChunks(Player player){
        ArrayList<Chunk> copy = new ArrayList<>(claimedChunks);
        for (Chunk chunk: copy){
            Errors returnValue = unclaimChunk(chunk.getChunkX(), chunk.getChunkZ(), player);
            if (returnValue != Errors.SUCCESS){
                return returnValue;
            }
        }

        return Errors.SUCCESS;
    }

    public Errors unclaimChunk(int chunkX, int chunkZ, Player player){
        if (!(isOwner(player) || isModerator(player))){
            return Errors.INSUFFICIENT_PERMISSIONS;
        }

        return Main.getManager().unclaimChunk(this, new Chunk(chunkX, chunkZ, player.getWorld().getUID()));
    }

    public Errors claimChunk(int chunkX, int chunkZ, Player player){
        if (!(isOwner(player) || isModerator(player))){
            return Errors.INSUFFICIENT_PERMISSIONS;
        }
        return Main.getManager().claimChunk(this, new Chunk(chunkX, chunkZ, player.getWorld().getUID()));
    }

    public void updateDisplayNameOfMembers(){
        ArrayList<UUID> playersToUpdate = getCompleteListOfPlayers();

        for (UUID uuid: playersToUpdate){
            Player player = Main.getPlugin().getServer().getPlayer(uuid);

            if (player != null){
                Main.getManager().updateDisplayName(player, this);
            }
        }
    }

    public TeamRank getTeamRank(Player player){
        if (player.getUniqueId().toString().equals(owner.toString())){
            return TeamRank.OWNER;
        } else if (moderators.contains(player.getUniqueId())){
            return TeamRank.MODERATOR;
        } else if (players.contains(player.getUniqueId())){
            return TeamRank.PLAYER;
        }

        return null;
    }

    public Errors joinTeam(Player player){
        return joinTeam(player, false);
    }

    public Errors joinTeam(Player player, boolean invited){
        players.add(player.getUniqueId());

        Main.getManager().updateDisplayName(player, this);

        if (invited){
            invitedPlayers.remove(player.getUniqueId());
        }

        return Errors.SUCCESS;
    }

    public Errors removePlayer(Player player){
        TeamRank rank = getTeamRank(player);

        if (rank == TeamRank.MODERATOR){
            moderators.remove(player.getUniqueId());
        } else {
            players.remove(player.getUniqueId());
        }

        Main.getManager().clearDisplayName(player);
        Main.getManager().saveTeams();
        return Errors.SUCCESS;
    }

    public ArrayList<UUID> getCompleteListOfPlayers(){
        return new ArrayList<>(players){{
            addAll(moderators);
            add(owner);
        }};
    }

    public Errors promotePlayer(Player player){
        players.remove(player.getUniqueId());
        moderators.add(player.getUniqueId());

        Main.getManager().saveTeams();
        return Errors.SUCCESS;
    }

    public Errors demotePlayer(Player player){
        moderators.remove(player.getUniqueId());
        players.add(player.getUniqueId());

        Main.getManager().saveTeams();
        return Errors.SUCCESS;
    }

    public Chunk getChunkData(Chunk wantedChunk){
        for (Chunk chunk: claimedChunks){
            if (chunk.compareTo(wantedChunk) == 0) return chunk;
        }

        return null;
    }

    public boolean isPlayerPartOfTeam(Player player){
        return getCompleteListOfPlayers().contains(player.getUniqueId());
    }

    public boolean isOwner(Player player){
        return (player.getUniqueId().equals(this.owner));
    }

    public boolean isModerator(Player player){
        return (this.moderators.contains(player.getUniqueId()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Main.getManager().saveTeams();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        Main.getManager().saveTeams();
    }

    public ArrayList<Chunk> getClaimedChunks() {
        return claimedChunks;
    }

    public ArrayList<Chunk> getLoadedChunks(){
        return new ArrayList<>(){{
            for (Chunk chunk: claimedChunks){
                if (chunk.isChunkLoaded()){
                    add(chunk);
                }
            }
        }};
    }

    public void setClaimedChunks(ArrayList<Chunk> claimedChunks) {
        this.claimedChunks = claimedChunks;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public ArrayList<UUID> getModerators() {
        return moderators;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ArrayList<UUID> getInvitedPlayers() {
        return invitedPlayers;
    }

    @Override
    public int compareTo(Team o) {
        if (o == null){
            return -1;
        }

        if (o.getName().equals(name)){
            return 0;
        }

        return o.getName().compareTo(name);
    }

    public void setColor(String color) {
        this.color = color;
        Main.getManager().saveTeams();
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        Main.getManager().saveTeams();
    }

    public void setOpen(boolean open) {
        isOpen = open;
        Main.getManager().saveTeams();
    }

    public boolean isInvited(Player player){
        return invitedPlayers.contains(player.getUniqueId());
    }

    public boolean isExplosionsEnabled() {
        return explosionsEnabled;
    }

    public void setExplosionsEnabled(boolean explosionsEnabled) {
        this.explosionsEnabled = explosionsEnabled;
        Main.getManager().saveTeams();
    }
}

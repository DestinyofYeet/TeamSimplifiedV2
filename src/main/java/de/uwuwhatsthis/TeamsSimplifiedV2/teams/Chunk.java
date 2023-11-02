package de.uwuwhatsthis.TeamsSimplifiedV2.teams;

import org.bukkit.entity.Player;

import java.util.UUID;

public class Chunk implements Comparable<Chunk> {
    private int chunkX, chunkZ;
    private UUID worldID;

    private boolean explosionEnabled;
    private boolean explosionEnabledSet;

    private boolean isChunkLoaded;

    public Chunk(int chunkX, int chunkZ, UUID worldID){
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.worldID = worldID;

        explosionEnabled = false;
        isChunkLoaded = false;
        explosionEnabledSet = false;
    }

    public Chunk(Player player){
        org.bukkit.Chunk chunk = player.getLocation().getChunk();

        new Chunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
    }

    public boolean isClaimed(){
        return false;
    }

    public static Chunk fromCoordinates(int chunkX, int chunkZ, UUID worldID){
        return new Chunk(chunkX, chunkZ, worldID);
    }

    @Override
    public int compareTo(Chunk o) {
        if (o.chunkZ == this.chunkZ
                && o.chunkX == this.chunkX
                && o.worldID.equals(this.worldID)){
            return 0;
        }

        if (o.chunkZ > this.chunkZ
                && o.chunkX > this.chunkZ) return 1;

        return -1;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public UUID getWorldUUID() {
        return worldID;
    }

    public boolean isExplosionEnabled() {
        return explosionEnabled;
    }

    public void setExplosionEnabled(boolean explosionEnabled) {
        this.explosionEnabled = explosionEnabled;
        this.explosionEnabledSet = true;
    }

    public boolean isChunkLoaded() {
        return isChunkLoaded;
    }

    public void setChunkLoaded(boolean chunkLoaded) {
        isChunkLoaded = chunkLoaded;
    }
}

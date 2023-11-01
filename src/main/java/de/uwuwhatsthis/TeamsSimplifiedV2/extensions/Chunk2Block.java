package de.uwuwhatsthis.TeamsSimplifiedV2.extensions;

public class Chunk2Block {

    private final int chunkX;
    private final int chunkZ;

    public Chunk2Block(int chunkX, int chunkZ){
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkXStart(){
        return this.chunkX * 16;
    }

    public int getChunkXEnd(){
        return getChunkXStart() + 16;
    }

    public int getChunkZStart(){
        return this.chunkZ * 16;
    }

    public int getChunkZEnd(){
        return getChunkZStart() + 16;
    }

    public int getOrigChunkX() {
        return chunkX;
    }

    public int getOrigChunkZ() {
        return chunkZ;
    }
}

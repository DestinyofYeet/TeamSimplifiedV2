package de.uwuwhatsthis.TeamsSimplifiedV2.extensions.Bluemap;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Shape;
import de.uwuwhatsthis.TeamsSimplifiedV2.extensions.Chunk2Block;
import de.uwuwhatsthis.TeamsSimplifiedV2.main.Main;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Chunk;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.Team;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class ExtensionBlueMap {
    private final Object lock;
    private final Object triggerRerender;
    private boolean isApiAvailable;
    private final ArrayList<String> putChunks;

    public ExtensionBlueMap(){
        isApiAvailable = false;
        lock = new Object();
        triggerRerender = new Object();
        putChunks = new ArrayList<>();
    }

    public void triggerRender(){
        Main.getPlugin().getLogger().info("Received trigger for re-render.");

        if (!isApiAvailable){
            BlueMapAPI.getInstance().ifPresentOrElse(null, () -> {
                BlueMapAPI.onEnable(api -> {
                    isApiAvailable = true;
                    startSyncThread(api);
                });
            });
        } else {
            synchronized (triggerRerender){
                triggerRerender.notify();
            }
        }
    }

    public void shutdown(){
        synchronized (lock){
            lock.notifyAll();
        }

        synchronized (triggerRerender){
            triggerRerender.notifyAll();
        }
    }

    private void waitForAvailableApi(){
        BlueMapAPI.onEnable(blueMapAPI -> {
            synchronized (lock){
                lock.notifyAll();
                Main.getPlugin().getLogger().info("Bluemap Api available");
            }
        });

        BlueMapAPI.getInstance().ifPresent(
                blueMapAPI -> {
                    synchronized (lock){
                        Main.getPlugin().getLogger().info("Bluemap Api available");
                        lock.notifyAll();
                    }
                }
        );
    }

    private void startSyncThread(BlueMapAPI api){
        Main.getPlugin().getLogger().info("Started sync thread");

        boolean firstRun = true;

        while (!Main.getManager().getTask().isCancelled()){

            if (!firstRun){
                synchronized (triggerRerender){
                    try {
                        Main.getPlugin().getLogger().info("Waiting for re-render of chunks.");
                        triggerRerender.wait();
                    } catch (InterruptedException e) {
                        Main.getPlugin().getLogger().info("Got an interrupted exception in the render thread. If shutting down, this is normal.");
                        return;
                    }
                }
            }

            firstRun = false;

            if (Main.getManager().getTask().isCancelled()){
                break;
            }

            Main.getPlugin().getLogger().info("Rendering claimed chunks");

            for (BlueMapWorld world: api.getWorlds()){
                for (BlueMapMap map: world.getMaps()){
                    for (String key: putChunks){
                        map.getMarkerSets().remove(key);
                    }
                }

                int i = 0;
                for (Team team: Main.getManager().getAllTeams()){
                    ArrayList<Chunk> currentWorldChunks = new ArrayList<>();

                    for (Chunk chunk: team.getClaimedChunks()){
                        if (chunk.getWorldUUID().toString().equals(world.getId())){
                            currentWorldChunks.add(chunk);
                        }
                    }

                    if (currentWorldChunks.isEmpty()) continue;

                    ArrayList<Shape> shapes = new ArrayList<>(){{
                        currentWorldChunks.forEach(chunk -> {
                            Chunk2Block convert = new Chunk2Block(chunk.getChunkX(), chunk.getChunkZ());
                            add(Shape.createRect(convert.getChunkXStart(), convert.getChunkZStart(), convert.getChunkXEnd(), convert.getChunkZEnd()));
                        });
                    }};

                    int j = 0;
                    MarkerSet set = MarkerSet.builder().label("Claimed by " + team.getName()).build();

                    World serverWorld = Main.getPlugin().getServer().getWorld(UUID.fromString(world.getId()));
                    assert serverWorld != null;

                    // TODO: make shape pretty

                    for (Shape shape: shapes){
                        j++;
                        ExtrudeMarker marker = ExtrudeMarker.builder().label("Claimed by " + team.getName())
                                .fillColor(ColorMapping.getColor(team.getColor()))
                                .lineColor(ColorMapping.getColor(team.getColor()))
                                .shape(shape, serverWorld.getMinHeight(), serverWorld.getMaxHeight())
                                .build();
                        String key = i + "-" + j;
                        set.put(key, marker);

                        putChunks.add(key);
                    }

                    int h = 0;
                    for (BlueMapMap map: world.getMaps()){
                        h++;
                        String key = i + "-" + h;
                        map.getMarkerSets().put(key, set);
                        putChunks.add(key);
                    }
                }
            };
        }

        Main.getPlugin().getLogger().info("Shutting down rendering thread.");

    }
}



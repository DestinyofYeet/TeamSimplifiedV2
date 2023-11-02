package de.uwuwhatsthis.TeamsSimplifiedV2.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ChunkTabCompleter implements TabCompleter {

    private List<String> returnNothing(){
        return new ArrayList<>() {{
            add("");
        }};
    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equals("chunk")){
            if (args.length == 1){
                return new ArrayList<>(){{
                    add("claim");
                    add("unclaim");
                    add("revoke-claim");
                }};
            }
        }

        return null;
    }
}

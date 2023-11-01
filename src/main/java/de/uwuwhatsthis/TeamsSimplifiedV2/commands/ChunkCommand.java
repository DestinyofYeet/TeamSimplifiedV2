package de.uwuwhatsthis.TeamsSimplifiedV2.commands;

import de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk.ChunkClaimCommand;
import de.uwuwhatsthis.TeamsSimplifiedV2.commands.chunk.ChunkUnclaimCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChunkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("TeamsSimplifiedV2.chunk.chunk")){
            commandSender.sendMessage("§cInsufficient permissions!");
            return true;
        }

        if (args.length < 1){
            commandSender.sendMessage("§cYou need to provide an argument!");
            return true;
        }

        String arg = args[0];

        return switch (arg) {
            case "claim" -> new ChunkClaimCommand().onCommand(commandSender, command, s, args);
            case "unclaim" -> new ChunkUnclaimCommand().onCommand(commandSender, command, s, args);
            default -> true;
        };
    }
}

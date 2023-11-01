package de.uwuwhatsthis.TeamsSimplifiedV2.utils;

public enum Errors {

    SUCCESS(""),

    TEAM_NAME_ALREADY_USED("§cThat team name is already taken!"),
    TEAM_TAG_ALREADY_USED("§cThat tag is already taken!"),
    INSUFFICIENT_PERMISSIONS("§cYou need to have higher privileges to run that command!"),
    INSUFFICIENT_PERMISSIONS_OWNER("§cYou need to be the owner of the team to run that command!"),
    INSUFFICIENT_PERMISSIONS_MODERATOR("§cYou need to be a moderator of the team to run that command!"),
    CHUNK_ALREADY_CLAIMED("§cThat chunk is already claimed!"),
    CHUNK_NOT_CLAIMED("§cThat chunk is not claimed by your team!"),
    PLAYER_ALREADY_IN_TEAM("§cYou are already in a team!");

    private String value;

    Errors(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

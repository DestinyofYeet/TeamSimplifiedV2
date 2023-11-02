package de.uwuwhatsthis.TeamsSimplifiedV2.utils;

public enum Defaults {
    CONFIG_MAX_TEAM_NAME_LENGTH("Config.MaxTeamNameLength"),
    CONFIG_MAX_TEAM_TAG_LENGTH("Config.MaxTeamTagLength"),
    CONFIG_MAX_LOADED_CHUNKS_PER_TEAM("Config.MaxLoadedChunksPerTeam"),
    CONFIG_SHOW_FANCY_TAB_NAME("Config.ShowFancyTabName"),
    CONFIG_EXTENSIONS_ENABLE_DYNMAP("Config.Extensions.EnableDynmap"),
    CONFIG_EXTENSIONS_ENABLE_BLUEMAP("Config.Extensions.EnableBluemap"),
    CONFIG_BSTATS_ENABLE("Config.EnableBstats"),
    DATA_FILE_NAME("plugins/TeamsSimplifiedV2/teams.json");

    private final String value;

    Defaults(String s){
        value = s;
    }

    public String getValue(){
        return value;
    }
}

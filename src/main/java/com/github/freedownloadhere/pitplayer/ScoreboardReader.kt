package com.github.freedownloadhere.pitplayer

import net.minecraft.scoreboard.ScorePlayerTeam

object ScoreboardReader {
    private val emojiRegex = "[\\uD83D\\uDD2B\\uD83C\\uDF6B\\uD83D\\uDCA3\\uD83D\\uDC7D\\uD83D\\uDD2E\\uD83D\\uDC0D\\uD83D\\uDC7E\\uD83C\\uDF20\\uD83C\\uDF6D\\u26BD\\uD83C\\uDFC0\\uD83D\\uDC79\\uD83C\\uDF81\\uD83C\\uDF89\\uD83C\\uDF82]+".toRegex()
    private val colorRegex = "(\\u00A7.)+".toRegex()
    var contents = "";

    fun updateContents() {
        contents = ""
        if(mc.thePlayer == null || mc.theWorld == null) return
        val scoreboard = mc.theWorld.scoreboard ?: return
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return
        contents += objective.displayName.replace(colorRegex, "") + ' '
        val scores = scoreboard.getSortedScores(objective)
        for(line in scores) {
            val team = scoreboard.getPlayersTeam(line.playerName)
            val scoreboardLine = ScorePlayerTeam.formatPlayerName(team, line.playerName).trim()
            contents += scoreboardLine.replace(emojiRegex, "").replace(colorRegex, "")
            contents += ' '
        }
    }
}
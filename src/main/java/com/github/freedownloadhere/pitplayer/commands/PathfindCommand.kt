package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.CombatModule
import com.github.freedownloadhere.pitplayer.GPS
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.Vec3i

class PathfindCommand : CommandBase() {
    override fun getCommandName(): String {
        return "pathfind"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args == null || args.size < 3) {
            CombatModule.findTarget()
            return
        }
        val dest = Vec3i(args[0].toInt(), args[1].toInt(), args[2].toInt())
        GPS.makeRoute(dest)
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}
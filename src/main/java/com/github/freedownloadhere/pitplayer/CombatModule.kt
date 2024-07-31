package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.blockBelow
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.toPlayerHead
import com.github.freedownloadhere.pitplayer.extensions.world
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import java.util.*
import kotlin.math.floor

object CombatModule {
    val state : String
        get() = "\u00A7lTarget: \u00A7${if(target == null) "7None" else "6${target!!.name}"}"

    var target : Entity? = null
    private val targetList = PriorityQueue { e1 : Entity, e2 : Entity -> Int
        val playerPos = player.positionVector
        floor(playerPos.squareDistanceTo(e1.positionVector) - playerPos.squareDistanceTo(e2.positionVector)).toInt()
    }
    private var maxEngageDistance = 900

    private fun makeTargetList() {
        targetList.clear()
        val playerPos = player.positionVector
        val playerHead = playerPos.toPlayerHead()
        for(entity in world.loadedEntityList) {
            if (entity == null || entity == player || entity !is EntityLiving) continue

            val entityPos = entity.positionVector

            if(playerPos.squareDistanceTo(entityPos) >= maxEngageDistance) continue

            val distance = playerPos.squareDistanceTo(entityPos)
            if(distance > maxEngageDistance) continue

            val ray1 = world.rayTraceBlocks(playerHead, entityPos)
            val ray2 = world.rayTraceBlocks(playerHead, entityPos.toPlayerHead())
            if(ray1 != null && ray2 != null) continue

            targetList.add(entity)
        }
    }

    fun findTarget() {
        target = null
        makeTargetList()
        while(targetList.isNotEmpty()) {
            val entity = targetList.remove()
            Pathfinder.pathfind(entity.blockBelow, player.blockBelow) ?: return
            target = entity
            return
        }
    }

    fun attackTarget() {
        if(target == null) return
        GPS.dest = target!!.blockBelow
        GPS.traverseRoute()
        PlayerRemote.attack()
    }

    fun findAndAttackTarget() {
        findTarget()
        attackTarget()
    }
}
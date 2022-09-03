package me.auuki.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.Vector

class Cuboid : Cloneable, ConfigurationSerializable, Iterable<Block> {
    lateinit var name: String


    private val minimumPoint: Vector
    private val maximumPoint: Vector

    constructor(cuboid: Cuboid) : this(
        cuboid.name,
        cuboid.minimumPoint.x,
        cuboid.minimumPoint.y,
        cuboid.minimumPoint.z,
        cuboid.maximumPoint.x,
        cuboid.maximumPoint.y,
        cuboid.maximumPoint.z
    )

    constructor(
        name: String, x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double
    ) {
        this.name = name
        val xPos1 = x1.coerceAtMost(x2)
        val xPos2 = x1.coerceAtLeast(x2)
        val yPos1 = y1.coerceAtMost(y2)
        val yPos2 = y1.coerceAtLeast(y2)
        val zPos1 = z1.coerceAtMost(z2)
        val zPos2 = z1.coerceAtLeast(z2)
        minimumPoint = Vector(xPos1, yPos1, zPos1)
        maximumPoint = Vector(xPos2, yPos2, zPos2)
    }

    constructor(blockPosOne: Location, blockPosTwo: Location) {
        check(blockPosOne.world?.uid == blockPosTwo.world?.uid) {
            "The two locations aren't in the world!"
        }
        name = blockPosOne.world!!.name
        val xPos1 = blockPosOne.x.coerceAtMost(blockPosTwo.x)
        val yPos1 = blockPosOne.y.coerceAtMost(blockPosTwo.y)
        val zPos1 = blockPosOne.z.coerceAtMost(blockPosTwo.z)
        val xPos2 = blockPosOne.x.coerceAtLeast(blockPosTwo.x)
        val yPos2 = blockPosOne.y.coerceAtLeast(blockPosTwo.y)
        val zPos2 = blockPosOne.z.coerceAtLeast(blockPosTwo.z)
        minimumPoint = Vector(xPos1, yPos1, zPos1)
        maximumPoint = Vector(xPos2, yPos2, zPos2)
    }

    fun contains(location: Location): Boolean =
        location.world?.name == name && location.toVector().isInAABB(minimumPoint, maximumPoint)

    fun contains(vector: Vector): Boolean = vector.isInAABB(minimumPoint, maximumPoint)

    override fun serialize(): MutableMap<String, Any> {
        val serializedCuboid: MutableMap<String, Any> = HashMap()
        serializedCuboid["name"] = name
        serializedCuboid["x1"] = minimumPoint.x
        serializedCuboid["x2"] = maximumPoint.x
        serializedCuboid["y1"] = minimumPoint.y
        serializedCuboid["y2"] = maximumPoint.y
        serializedCuboid["z1"] = minimumPoint.z
        serializedCuboid["z2"] = maximumPoint.z
        return serializedCuboid
    }

    override fun iterator(): Iterator<Block> = blocks.listIterator()

    var world: World = Bukkit.getServer().getWorld(name) ?: throw NullPointerException("Unable to get world!")
        set(world) = if (world != null) name = world.name else throw NullPointerException("The world cannot be null.")

    val blocks: List<Block>
        get() {
            val blockList = ArrayList<Block>()
            repeat((minimumPoint.blockX..maximumPoint.blockX).count()) {
                var y = minimumPoint.blockY
                while (y <= maximumPoint.blockY && y <= world.maxHeight) {
                    (minimumPoint.blockZ..maximumPoint.blockZ).forEach { blockList.add(world.getBlockAt(it, y, it)) }
                    y++
                }
            }
            return blockList
        }

    companion object {
        fun deserialize(serializedCuboid: Map<String?, Any?>): Cuboid? = try {
            val worldName = serializedCuboid["worldName"] as String
            val xPos1 = serializedCuboid["x1"] as Double
            val xPos2 = serializedCuboid["x2"] as Double
            val yPos1 = serializedCuboid["y1"] as Double
            val yPos2 = serializedCuboid["y2"] as Double
            val zPos1 = serializedCuboid["z1"] as Double
            val zPos2 = serializedCuboid["z2"] as Double
            Cuboid(worldName, xPos1, yPos1, zPos1, xPos2, yPos2, zPos2)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    val lowerLocation
        get() = minimumPoint.toLocation(world)
    val lowerX
        get() = minimumPoint.x
    val lowerY
        get() = minimumPoint.y
    val lowerZ
        get() = minimumPoint.z
    val upperLocation
        get() = maximumPoint.toLocation(world)
    val upperX
        get() = maximumPoint.x
    val upperY
        get() = maximumPoint.y
    val upperZ
        get() = maximumPoint.z
    val volume = (upperX - lowerX + 1) * (upperY - lowerY + 1) * (upperZ - lowerZ + 1)
}
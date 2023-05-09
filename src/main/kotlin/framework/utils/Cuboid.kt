package framework.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.Vector


data class Cuboid(
    var name: String,
    val minimumPoint: Vector,
    val maximumPoint: Vector
) : Cloneable, ConfigurationSerializable, Iterable<Block> {
    constructor(cuboid: Cuboid) : this(
        cuboid.name,
        cuboid.minimumPoint,
        cuboid.maximumPoint
    )

    constructor(
        name: String,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double
    ) : this(
        name,
        Vector(x1.coerceAtMost(x2), y1.coerceAtMost(y2), z1.coerceAtMost(z2)),
        Vector(x1.coerceAtLeast(x2), y1.coerceAtLeast(y2), z1.coerceAtLeast(z2))
    )

    constructor(blockPosOne: Location, blockPosTwo: Location) : this(
        blockPosOne.world?.name ?: throw IllegalArgumentException("Location has no world"),
        Vector(
            blockPosOne.x.coerceAtMost(blockPosTwo.x),
            blockPosOne.y.coerceAtMost(blockPosTwo.y),
            blockPosOne.z.coerceAtMost(blockPosTwo.z)
        ),
        Vector(
            blockPosOne.x.coerceAtLeast(blockPosTwo.x),
            blockPosOne.y.coerceAtLeast(blockPosTwo.y),
            blockPosOne.z.coerceAtLeast(blockPosTwo.z)
        )
    )

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
        get() = (minimumPoint.blockX..maximumPoint.blockX).flatMap { x ->
            (minimumPoint.blockY..maximumPoint.blockY).flatMap { y ->
                (minimumPoint.blockZ..maximumPoint.blockZ).mapNotNull { z ->
                    world.getBlockAt(x, y, z)
                }
            }
        }

    companion object {
        fun deserialize(serializedCuboid: Map<String?, Any?>): Cuboid? {
            val worldName = serializedCuboid["worldName"] as? String ?: return null
            val xPos1 = serializedCuboid["x1"] as? Double ?: return null
            val xPos2 = serializedCuboid["x2"] as? Double ?: return null
            val yPos1 = serializedCuboid["y1"] as? Double ?: return null
            val yPos2 = serializedCuboid["y2"] as? Double ?: return null
            val zPos1 = serializedCuboid["z1"] as? Double ?: return null
            val zPos2 = serializedCuboid["z2"] as? Double ?: return null
            return Cuboid(worldName, xPos1, yPos1, zPos1, xPos2, yPos2, zPos2)
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
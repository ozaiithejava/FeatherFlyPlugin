import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.plugin.java.JavaPlugin

class FeatherFlightPlugin : JavaPlugin(), Listener {

    private val feathers = mutableSetOf<Player>()

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            if (player.inventory.itemInMainHand.type == Material.FEATHER) {
                event.isCancelled = true
                toggleFeather(player)
                player.playSound(player.location, Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        val player = event.player
        if (feathers.contains(player)) {
            event.isCancelled = true
            player.allowFlight = false
            player.isFlying = false
            player.playSound(player.location, Sound.ENTITY_FIREWORK_LAUNCH, 1.0f, 1.0f)
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (feathers.contains(player)) {
            if (player.location.block.type != Material.AIR) {
                player.playSound(player.location, Sound.ENTITY_SLIME_JUMP, 1.0f, 1.0f)
                feathers.remove(player)
            }
        }
    }

    private fun toggleFeather(player: Player) {
        if (feathers.contains(player)) {
            feathers.remove(player)
        } else {
            feathers.add(player)
        }
        player.allowFlight = feathers.contains(player)
    }
}

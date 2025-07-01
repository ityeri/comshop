import com.github.ityeri.comshop.Comshop
import org.bukkit.plugin.java.JavaPlugin

class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        Comshop()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

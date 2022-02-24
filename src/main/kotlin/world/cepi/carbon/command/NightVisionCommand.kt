package world.cepi.carbon.command

import net.kyori.adventure.sound.Sound
import net.minestom.server.potion.Potion
import net.minestom.server.potion.PotionEffect
import net.minestom.server.sound.SoundEvent
import world.cepi.kstom.command.kommand.Kommand

object NightVisionCommand : Kommand({
    default {

        if (player.activeEffects.any { it.potion.effect == PotionEffect.NIGHT_VISION }) {
            player.removeEffect(PotionEffect.NIGHT_VISION)
            player.playSound(Sound.sound(SoundEvent.ENTITY_GENERIC_DRINK, Sound.Source.MASTER, 1f, 1f))
            return@default
        }

        player.addEffect(Potion(PotionEffect.NIGHT_VISION, 1, Int.MAX_VALUE))
        player.playSound(Sound.sound(SoundEvent.BLOCK_BREWING_STAND_BREW, Sound.Source.MASTER, 1f, 1f))

    }
}, "nightvision", "nv")
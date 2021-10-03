package world.cepi.carbon.warp.commands

import world.cepi.kstom.command.kommand.Kommand

object WarpsCommand : Kommand({
    addSubcommands(SetWarp, ListWarps, DeleteWarp)
}, "warps")
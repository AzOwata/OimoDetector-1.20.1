package net.azowata.oimodetector;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OimoCommands {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LOGGER.info("Registering Oimo command.");
        dispatcher.register(Commands.literal("oimo")
                .then(Commands.literal("detect")
                        .then(Commands.argument("player", StringArgumentType.string())
                            .executes(OimoCommands::detect)))
                .then(Commands.literal("shimmer")
                        .then(Commands.argument("player", StringArgumentType.string())
                            .executes(OimoCommands::shimmer))));
    }
    private static int detect(CommandContext<CommandSourceStack> command){
        double x;
        double y;
        double z;
        ServerPlayer serverPlayer = command.getSource().getServer().getPlayerList().getPlayerByName(StringArgumentType.getString(command, "player"));
        if(serverPlayer != null) {
            x = Math.floor(serverPlayer.getX() * 100) / 100;
            y = Math.floor(serverPlayer.getY() * 100) / 100;
            z = Math.floor(serverPlayer.getZ() * 100) / 100;

            if(command.getSource().getEntity() instanceof Player player){
                player.sendSystemMessage(Component.literal("Target's Position: " + x + ", " + y + ", " + z));
            }
        } else {
            if(command.getSource().getEntity() instanceof Player player){
                player.sendSystemMessage(Component.literal("Error: Can't detect player!").withStyle(style -> style.withColor(0xFF0000)));
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int shimmer(CommandContext<CommandSourceStack> command){
        ServerPlayer serverPlayer = command.getSource().getServer().getPlayerList().getPlayerByName(StringArgumentType.getString(command, "player"));
        if(serverPlayer != null) {
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20*60, 0));
            serverPlayer.sendSystemMessage(Component.literal("You got shimmered lol (^o^)m9"));
            if(command.getSource().getEntity() instanceof Player player) {
                player.sendSystemMessage(Component.literal("Target shimmers!"));
            }
        } else {
            if(command.getSource().getEntity() instanceof Player player) {
                player.sendSystemMessage(Component.literal("Error: Can't shimmer player!").withStyle(style -> style.withColor(0xFF0000)));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}

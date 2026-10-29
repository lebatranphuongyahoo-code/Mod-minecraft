package com.cataclysm.disasters.command;

import com.cataclysm.disasters.CataclysmDisasters;
import com.cataclysm.disasters.disaster.DisasterType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class DisasterCommands {
    public static void register(com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher) {
        var start = Commands.literal("start").then(Commands.argument("type", StringArgumentType.word())
            .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(java.util.Arrays.stream(DisasterType.values()).map(t -> t.name().toLowerCase()), builder))
            .then(Commands.argument("intensity", IntegerArgumentType.integer(1, 5))
                .executes(ctx -> start(ctx.getSource(), StringArgumentType.getString(ctx, "type"), IntegerArgumentType.getInteger(ctx, "intensity"), 120))
                .then(Commands.argument("duration", IntegerArgumentType.integer(1, 36000))
                    .executes(ctx -> start(ctx.getSource(), StringArgumentType.getString(ctx, "type"), IntegerArgumentType.getInteger(ctx, "intensity"), IntegerArgumentType.getInteger(ctx, "duration"))))));
        var schedule = Commands.literal("schedule")
            .then(Commands.literal("on").executes(ctx -> { CataclysmDisasters.DISASTERS.setAutomatic(true); return 1; }))
            .then(Commands.literal("off").executes(ctx -> { CataclysmDisasters.DISASTERS.setAutomatic(false); return 1; }));
        dispatcher.register(Commands.literal("disaster").requires(source -> source.hasPermission(2))
            .then(start)
            .then(Commands.literal("stop").executes(ctx -> { CataclysmDisasters.DISASTERS.stop(ctx.getSource().getLevel()); return 1; }))
            .then(Commands.literal("list").executes(ctx -> { ctx.getSource().sendSuccess(() -> Component.literal(String.join(", ", java.util.Arrays.stream(DisasterType.values()).map(t -> t.name().toLowerCase()).toList())), false); return 1; }))
            .then(schedule));
    }
    private static int start(net.minecraft.commands.CommandSourceStack source, String value, int intensity, int duration) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        try { if (CataclysmDisasters.DISASTERS.start(source.getLevel(), DisasterType.parse(value), intensity, duration)) { source.sendSuccess(() -> Component.literal("Disaster scheduled."), true); return 1; } }
        catch (IllegalArgumentException e) { throw new com.mojang.brigadier.exceptions.SimpleCommandExceptionType(Component.literal(e.getMessage())).create(); }
        source.sendFailure(Component.literal("A disaster is already active or cooling down.")); return 0;
    }
    private DisasterCommands() { }
}

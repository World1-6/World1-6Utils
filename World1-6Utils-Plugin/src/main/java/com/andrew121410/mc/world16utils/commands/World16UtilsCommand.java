package com.andrew121410.mc.world16utils.commands;

import com.andrew121410.ccutils.utils.HashBasedUpdater;
import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class World16UtilsCommand {

    private final World16Utils plugin;

    public World16UtilsCommand(World16Utils plugin) {
        this.plugin = plugin;
        registerCommand();
    }

    private void registerCommand() {
        plugin.getCommand("world1-6utils").setExecutor((sender, command, s, args) -> {
            if (args.length == 0) {
                if (!sender.hasPermission("world16.world1-6utils")) {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }

                if (sender instanceof Player player) {
                    openMainCommandDialog(player);
                } else {
                    sendCommandHelp(sender);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                if (!sender.hasPermission("world16.world1-6utils")) {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }

                String pluginName = args[1];

                if (pluginName.equalsIgnoreCase("all")) {
                    UpdateManager.updateAll(sender);
                    return true;
                }

                UpdateManager.update(sender, pluginName);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list-updaters")) {
                if (!sender.hasPermission("world16.world1-6utils")) {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }

                sendUpdaterList(sender);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("callclickevent")) { // Don't need permission for this
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                Map<String, Consumer<Player>> map = plugin.getChatClickCallbackManager().getMap().get(player.getUniqueId());
                if (map == null) return true;

                String key = args[1];
                Consumer<Player> consumer = map.get(key);
                if (consumer == null) return true;
                consumer.accept(player);
                map.remove(key);
            }
            return true;
        });

        plugin.getCommand("world1-6utils").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) return null;

            if (args.length == 1) {
                return Arrays.asList("update", "list-updaters");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                List<String> strings = UpdateManager.getPluginNamesFromUpdaters();
                strings.add("all");
                return TabUtils.getContainsString(args[1], strings);
            }
            return null;
        });
    }

    private void openMainCommandDialog(Player player) {
        ActionButton updaterButton = ActionButton.builder(Component.text("Updates", NamedTextColor.GREEN))
                .tooltip(Component.text("Manage plugin updates"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        openUpdaterDialog(p);
                    }
                }, callbackOptions()))
                .build();

        ActionButton usageButton = ActionButton.builder(Component.text("Show Commands", NamedTextColor.AQUA))
                .tooltip(Component.text("Show command usage in chat"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        sendCommandHelp(p);
                    }
                }, callbackOptions()))
                .build();

        ActionButton closeButton = ActionButton.builder(Component.text("Close", NamedTextColor.RED))
                .tooltip(Component.text("Close dialog"))
                .width(120)
                .action(null)
                .build();

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("World1-6Utils", NamedTextColor.GOLD))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(List.of(
                                DialogBody.plainMessage(Component.text("Built on " + World16Utils.DATE_OF_VERSION, NamedTextColor.GRAY)),
                                DialogBody.plainMessage(Component.text("Select an action:", NamedTextColor.YELLOW))
                        ))
                        .build())
                .type(DialogType.multiAction(List.of(updaterButton, usageButton))
                        .columns(1)
                        .exitAction(closeButton)
                        .build()));

        player.showDialog(dialog);
    }

    private void openUpdaterDialog(Player player) {
        Component description = Translate.miniMessage("<yellow>World1-6Utils can update plugins that implement the World1-6Utils API, if they register an updater.</yellow>");

        ActionButton updateAllButton = ActionButton.builder(Component.text("Update All", NamedTextColor.GREEN))
                .tooltip(Component.text("Update all plugins with registered updaters"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        UpdateManager.updateAll(p);
                    }
                }, callbackOptions()))
                .build();

        ActionButton listUpdatersButton = ActionButton.builder(Component.text("List Updaters", NamedTextColor.YELLOW))
                .tooltip(Component.text("Show all plugins that have updaters"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        sendUpdaterList(p);
                    }
                }, callbackOptions()))
                .build();

        ActionButton backButton = ActionButton.builder(Component.text("Back", NamedTextColor.YELLOW))
                .tooltip(Component.text("Return to main menu"))
                .width(120)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        openMainCommandDialog(p);
                    }
                }, callbackOptions()))
                .build();

        ActionButton closeButton = ActionButton.builder(Component.text("Close", NamedTextColor.RED))
                .tooltip(Component.text("Close dialog"))
                .width(120)
                .action(null)
                .build();

        // Show initial dialog with "checking" states
        List<String> pluginNames = UpdateManager.getPluginNamesFromUpdaters();
        List<DialogBody> initialBodies = new ArrayList<>();
        initialBodies.add(DialogBody.plainMessage(Component.text("Manage plugin updates", NamedTextColor.GRAY)));
        for (String pluginName : pluginNames) {
            initialBodies.add(DialogBody.item(InventoryUtils.createItem(Material.YELLOW_WOOL, 1, pluginName))
                    .description(DialogBody.plainMessage(Component.text("Checking for updates...", NamedTextColor.GRAY)))
                    .build());
        }

        Dialog initialDialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("Updates", NamedTextColor.GREEN))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(initialBodies)
                        .build())
                .type(DialogType.multiAction(List.of(updateAllButton, listUpdatersButton, backButton))
                        .columns(1)
                        .exitAction(closeButton)
                        .build()));

        player.showDialog(initialDialog);

        // Now, check all updaters asynchronously and update dialog once all are done
        Map<String, DialogBody> resultBodies = new LinkedHashMap<>();
        AtomicInteger completed = new AtomicInteger(0);

        for (String pluginName : pluginNames) {
            HashBasedUpdater updater = UpdateManager.getUpdater(pluginName);
            if (updater == null) {
                resultBodies.put(pluginName, DialogBody.item(InventoryUtils.createItem(Material.RED_WOOL, 1, pluginName + " (error)"))
                        .description(DialogBody.plainMessage(Component.text("Updater not found.", NamedTextColor.RED)))
                        .build());
                if (completed.incrementAndGet() == pluginNames.size()) {
                    // All done, update dialog
                    updateUpdaterDialog(player, resultBodies, updateAllButton, listUpdatersButton, backButton, closeButton);
                }
                continue;
            }

            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                boolean needsUpdate = false;
                String error = null;
                try {
                    needsUpdate = updater.shouldUpdate();
                } catch (Exception ex) {
                    error = ex.getMessage();
                }
                DialogBody body;
                if (error != null) {
                    body = DialogBody.item(InventoryUtils.createItem(Material.RED_WOOL, 1, pluginName + " (error)"))
                            .description(DialogBody.plainMessage(Component.text("Error: " + error, NamedTextColor.RED)))
                            .build();
                } else if (needsUpdate) {
                    body = DialogBody.item(InventoryUtils.createItem(Material.GREEN_WOOL, 1, pluginName + " has an update available!"))
                            .description(DialogBody.plainMessage(Component.text("Update available!", NamedTextColor.GREEN)))
                            .build();
                } else {
                    body = DialogBody.item(InventoryUtils.createItem(Material.GRAY_WOOL, 1, pluginName + " is up to date."))
                            .description(DialogBody.plainMessage(Component.text("Up to date.", NamedTextColor.GRAY)))
                            .build();
                }
                synchronized (resultBodies) {
                    resultBodies.put(pluginName, body);
                }
                if (completed.incrementAndGet() == pluginNames.size()) {
                    // All done, update dialog
                    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                        updateUpdaterDialog(player, resultBodies, updateAllButton, listUpdatersButton, backButton, closeButton);
                    });
                }
            });
        }
    }

    private void updateUpdaterDialog(Player player, Map<String, DialogBody> resultBodies,
                                     ActionButton updateAllButton, ActionButton listUpdatersButton,
                                     ActionButton backButton, ActionButton closeButton) {
        List<DialogBody> bodies = new ArrayList<>();
        bodies.add(DialogBody.plainMessage(Component.text("Manage plugin updates", NamedTextColor.GRAY)));
        bodies.addAll(resultBodies.values());

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("Updates", NamedTextColor.GREEN))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(bodies)
                        .build())
                .type(DialogType.multiAction(List.of(updateAllButton, listUpdatersButton, backButton))
                        .columns(1)
                        .exitAction(closeButton)
                        .build()));

        player.showDialog(dialog);
    }

    private void sendCommandHelp(CommandSender sender) {
        sender.sendMessage(Translate.miniMessage("<gold>World1-6Utils</gold> <gray>(Built on " + World16Utils.DATE_OF_VERSION + ")</gray>"));
        sender.sendMessage(Translate.miniMessage("<yellow>Available Commands:</yellow>"));
        sender.sendMessage(Translate.miniMessage("<gold>/world1-6utils update <pluginName></gold> <gray>- Update the specified plugin.</gray>"));
        sender.sendMessage(Translate.miniMessage("<gold>/world1-6utils update all</gold> <gray>- Update all plugins with registered updaters.</gray>"));
        sender.sendMessage(Translate.miniMessage("<gold>/world1-6utils list-updaters</gold> <gray>- Show plugins that have updaters available.</gray>"));
    }

    private void sendUpdaterList(CommandSender sender) {
        sender.sendMessage(Translate.miniMessage("List of plugins with updaters:"));
        for (String pluginName : UpdateManager.getPluginNamesFromUpdaters()) {
            sender.sendMessage(Translate.miniMessage("- <yellow>" + pluginName));
        }
    }

    private static ClickCallback.Options callbackOptions() {
        return ClickCallback.Options.builder()
                .uses(1)
                .lifetime(Duration.ofMinutes(10))
                .build();
    }
}


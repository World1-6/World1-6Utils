package com.andrew121410.mc.world16utils.commands;

import com.andrew121410.ccutils.utils.HashBasedUpdater;
import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
                    openMainDialog(player);
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
                } else {
                    UpdateManager.update(sender, pluginName);
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("list-updaters")) {
                if (!sender.hasPermission("world16.world1-6utils")) {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }

                sendUpdaterList(sender);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("callclickevent")) {
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

    // ---- Main dialog ----

    private void openMainDialog(Player player) {
        ActionButton updatesButton = ActionButton.builder(Component.text("Updates", NamedTextColor.GREEN))
                .tooltip(Component.text("Check and manage plugin updates"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) openUpdaterDialog(p);
                }, callbackOptions()))
                .build();

        ActionButton commandsButton = ActionButton.builder(Component.text("Show Commands", NamedTextColor.AQUA))
                .tooltip(Component.text("Show command usage in chat"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) sendCommandHelp(p);
                }, callbackOptions()))
                .build();

        ActionButton closeButton = ActionButton.builder(Component.text("Close", NamedTextColor.RED))
                .tooltip(Component.text("Close this dialog"))
                .width(120)
                .action(null)
                .build();

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("World1-6Utils", NamedTextColor.GOLD))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(List.of(
                                DialogBody.plainMessage(Translate.miniMessage("<rainbow>World1-6Utils is a library plugin used by all World1-6 plugins.")),
                                DialogBody.plainMessage(Component.text("Built on " + World16Utils.DATE_OF_VERSION, NamedTextColor.GRAY))
                        ))
                        .build())
                .type(DialogType.multiAction(List.of(updatesButton, commandsButton))
                        .columns(1)
                        .exitAction(closeButton)
                        .build()));

        player.showDialog(dialog);
    }

    // ---- Updater dialog ----

    private enum UpdateStatus { CHECKING, UP_TO_DATE, UPDATE_AVAILABLE, ERROR }

    private void openUpdaterDialog(Player player) {
        List<String> pluginNames = UpdateManager.getPluginNamesFromUpdaters();

        if (pluginNames.isEmpty()) {
            openNoUpdatersDialog(player);
            return;
        }

        // Show initial dialog with all plugins in "checking" state
        showUpdaterDialog(player, pluginNames, buildPluginButtons(player, pluginNames, null), true);

        // Async status checks
        Map<String, UpdateStatus> statuses = new ConcurrentHashMap<>();
        AtomicInteger completed = new AtomicInteger(0);

        for (String pluginName : pluginNames) {
            HashBasedUpdater updater = UpdateManager.getUpdater(pluginName);
            if (updater == null) {
                statuses.put(pluginName, UpdateStatus.ERROR);
                if (completed.incrementAndGet() == pluginNames.size()) {
                    plugin.getServer().getScheduler().runTask(plugin, () ->
                            showUpdaterDialog(player, pluginNames, buildPluginButtons(player, pluginNames, statuses), false));
                }
                continue;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                UpdateStatus status;
                try {
                    status = updater.shouldUpdate() ? UpdateStatus.UPDATE_AVAILABLE : UpdateStatus.UP_TO_DATE;
                } catch (Exception e) {
                    status = UpdateStatus.ERROR;
                }
                statuses.put(pluginName, status);

                if (completed.incrementAndGet() == pluginNames.size()) {
                    plugin.getServer().getScheduler().runTask(plugin, () ->
                            showUpdaterDialog(player, pluginNames, buildPluginButtons(player, pluginNames, statuses), false));
                }
            });
        }
    }

    private List<ActionButton> buildPluginButtons(Player player, List<String> pluginNames, Map<String, UpdateStatus> statuses) {
        List<ActionButton> buttons = new ArrayList<>();
        for (String pluginName : pluginNames) {
            UpdateStatus status = statuses == null ? UpdateStatus.CHECKING : statuses.getOrDefault(pluginName, UpdateStatus.CHECKING);

            NamedTextColor color;
            Component tooltip;
            boolean clickable;

            switch (status) {
                case UPDATE_AVAILABLE -> {
                    color = NamedTextColor.GREEN;
                    tooltip = Component.text("Update available! Click to update.", NamedTextColor.GREEN);
                    clickable = true;
                }
                case UP_TO_DATE -> {
                    color = NamedTextColor.GRAY;
                    tooltip = Component.text("Up to date.", NamedTextColor.GRAY);
                    clickable = false;
                }
                case ERROR -> {
                    color = NamedTextColor.RED;
                    tooltip = Component.text("Failed to check for updates.", NamedTextColor.RED);
                    clickable = false;
                }
                default -> { // CHECKING
                    color = NamedTextColor.YELLOW;
                    tooltip = Component.text("Checking for updates...", NamedTextColor.YELLOW);
                    clickable = false;
                }
            }

            ActionButton.Builder builder = ActionButton.builder(Component.text(pluginName, color))
                    .tooltip(tooltip)
                    .width(150);

            if (clickable) {
                builder.action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) {
                        UpdateManager.update(p, pluginName);
                    }
                }, callbackOptions()));
            } else {
                builder.action(null);
            }

            buttons.add(builder.build());
        }
        return buttons;
    }

    private void showUpdaterDialog(Player player, List<String> pluginNames, List<ActionButton> pluginButtons, boolean checking) {
        ActionButton updateAllButton = ActionButton.builder(Component.text("Update All", NamedTextColor.GREEN))
                .tooltip(Component.text("Update all plugins that have updaters registered"))
                .width(160)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) UpdateManager.updateAll(p);
                }, callbackOptions()))
                .build();

        ActionButton backButton = ActionButton.builder(Component.text("← Back", NamedTextColor.YELLOW))
                .tooltip(Component.text("Return to main menu"))
                .width(120)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) openMainDialog(p);
                }, callbackOptions()))
                .build();

        List<ActionButton> allButtons = new ArrayList<>(pluginButtons);
        allButtons.add(updateAllButton);

        Component subtitle = checking
                ? Component.text("Checking for updates...", NamedTextColor.YELLOW)
                : Component.text(pluginNames.size() + " plugin(s) registered", NamedTextColor.GRAY);

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("Updates", NamedTextColor.GREEN))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(List.of(DialogBody.plainMessage(subtitle)))
                        .build())
                .type(DialogType.multiAction(allButtons)
                        .columns(1)
                        .exitAction(backButton)
                        .build()));

        player.showDialog(dialog);
    }

    private void openNoUpdatersDialog(Player player) {
        ActionButton backButton = ActionButton.builder(Component.text("← Back", NamedTextColor.YELLOW))
                .tooltip(Component.text("Return to main menu"))
                .width(120)
                .action(DialogAction.customClick((view, audience) -> {
                    if (audience instanceof Player p) openMainDialog(p);
                }, callbackOptions()))
                .build();

        Dialog dialog = Dialog.create(factory -> factory.empty()
                .base(DialogBase.builder(Component.text("Updates", NamedTextColor.GREEN))
                        .canCloseWithEscape(true)
                        .afterAction(DialogBase.DialogAfterAction.CLOSE)
                        .body(List.of(
                                DialogBody.plainMessage(Component.text("No updaters are registered.", NamedTextColor.RED)),
                                DialogBody.plainMessage(Component.text("Plugins must register an updater with World1-6Utils to appear here.", NamedTextColor.GRAY))
                        ))
                        .build())
                .type(DialogType.notice(backButton)));

        player.showDialog(dialog);
    }

    // ---- Chat helpers ----

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

package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.LanguageLocale;
import com.andrew121410.mc.world16utils.sign.SignCache;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignLinePattern;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SignScreenManager {

    private JavaPlugin plugin;

    private String name;
    private Location location;

    private ISignScreen signScreen;

    private SignLayout currentLayout;
    private SignPage currentPage;
    private int currentSide = 1;

    private int pointerLine = 0;
    private int pointerLineOffset = 0;

    private SignCache signCache = null;

    private boolean needsLineChanged;
    private boolean needsTextChanged;

    private boolean isTickerRunning = false;
    private boolean stop = false;

    public static final long DEFAULT_TICK_SPEED = 10L;
    private long tickSpeed;

    public SignScreenManager(JavaPlugin plugin, String name, Location location, Long tickSpeed, ISignScreen signScreen) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;
        if (tickSpeed == null) {
            this.tickSpeed = DEFAULT_TICK_SPEED;
        } else this.tickSpeed = tickSpeed;
        this.signScreen = signScreen;

        this.signScreen.onDoneConstructed(this);
    }

    public void onClick(Player player) {
        if (!isTickerRunning) {
            tick(player);
        }

        this.signScreen.onButton(this, player, this.currentLayout, this.currentPage, this.pointerLine, this.currentSide);
    }

    public void onScroll(Player player, boolean up) {
        if (!isTickerRunning) {
            tick(player);
        }

        SignLinePattern signLinePattern = this.currentPage.getSignLinePatternMap().get(this.pointerLine);

        if (up) {
            int toSide = this.currentSide - 1;
            if (toSide >= signLinePattern.getMinSide()) {
                Integer index = signLinePattern.getIndexOfSide(toSide);
                if (index == null) {
                    player.sendMessage(LanguageLocale.color("&4Index == null [UP]"));
                    return;
                }
                this.pointerLineOffset = index;
                this.currentSide = toSide;
                this.needsLineChanged = true;
            } else if (this.pointerLine != this.currentPage.getMin()) {
                stageLine(this.pointerLine - 1, true);
                this.needsLineChanged = true;
            } else {
                SignPage newPage = this.currentLayout.getReversePage(this.currentPage.getPageNumber());
                if (newPage == null) {
                    if (!this.signScreen.nullPage(this, player, true))
                        player.sendMessage(LanguageLocale.color("&4No new page was found going [UP]"));
                    return;
                }
                stagePageAndLine(newPage, true);
                this.signCache = newPage.toSignCache();
                this.needsTextChanged = true;
            }
        } else {
            int toSide = this.currentSide + 1;
            if (toSide <= signLinePattern.getMaxSide()) {
                Integer index = signLinePattern.getIndexOfSide(toSide);
                if (index == null) {
                    player.sendMessage(LanguageLocale.color("&4Index == null [DOWN]"));
                    return;
                }
                this.pointerLineOffset = index;
                this.currentSide = toSide;
                this.needsLineChanged = true;
            } else if (this.pointerLine != this.currentPage.getMax()) {
                stageLine(this.pointerLine + 1, false);
                this.needsLineChanged = true;
            } else {
                SignPage newPage = this.currentLayout.getNextPage(this.currentPage.getPageNumber());
                if (newPage == null) {
                    if (!this.signScreen.nullPage(this, player, false))
                        player.sendMessage(LanguageLocale.color("&4No new page was found going [DOWN]"));
                    return;
                }
                stagePageAndLine(newPage, false);
                this.signCache = newPage.toSignCache();
                this.needsTextChanged = true;
            }
        }
    }

    public void tick(Player player) {
        if (!this.isTickerRunning) {
            player.sendMessage(LanguageLocale.color("&bStarting tick() please wait..."));
            tick();
            player.sendMessage(LanguageLocale.color("&6Running!!!"));
        }
    }

    public void tick() {
        if (isTickerRunning) {
            return;
        }
        this.isTickerRunning = true;

        Sign sign = World16Utils.getInstance().getClassWrappers().getSignUtils().isSign(location.getBlock());
        if (sign == null) throw new NullPointerException("SignScreenManager : tick() : sign == null : NULL");

        new BukkitRunnable() {
            private boolean hold = false;
            private int pointerAT = 5;
            private int oldPointerLine = pointerLine;
            private int oldPointerLineOffset = pointerLineOffset;
            private final SignCache signCacheSave = new SignCache();
            private StringBuffer stringBuffer;

            @Override
            public void run() {
                if (stop && pointerAT == 0) {
                    isTickerRunning = false;
                    stop = false;
                    this.cancel();
                    return;
                }
                //Holding
                if (hold || signCache == null) return;

                if (!needsTextChanged && !needsLineChanged && this.pointerAT != 5) {
                    if (pointerAT == 0) {
                        clearStringBufferAndUpdate();
                        this.stringBuffer.insert(this.oldPointerLineOffset, ">");
                        sign.setLine(this.oldPointerLine, LanguageLocale.color(this.stringBuffer.toString()));
                        if (!sign.update()) stop = true;
                        pointerAT++;
                    } else {
                        if (!signCacheSave.updateFancy(sign)) stop = true;
                        pointerAT--;
                    }
                } else if (needsTextChanged || this.pointerAT == 5) {
                    this.oldPointerLine = pointerLine;
                    this.oldPointerLineOffset = pointerLineOffset;
                    signCache.updateFancy(sign);
                    this.signCacheSave.fromSign(sign);
                    clearStringBufferAndUpdate();
                    this.pointerAT = 0;
                    needsTextChanged = false;
                } else if (needsLineChanged) {
                    this.oldPointerLine = pointerLine;
                    this.oldPointerLineOffset = pointerLineOffset;
                    clearStringBufferAndUpdate();
                    this.pointerAT = 0;
                    needsLineChanged = false;
                }
            }

            private void clearStringBufferAndUpdate() {
                this.stringBuffer = new StringBuffer();
                this.stringBuffer.append(this.signCacheSave.getLine(this.oldPointerLine));
            }
        }.runTaskTimer(plugin, this.tickSpeed, this.tickSpeed);
    }

    public void updateLayoutAndPage(SignLayout signLayout, int beginningPage) {
        if (signLayout == null) {
            throw new NullPointerException("SignScreenManager : goToLayoutAndPage(String, Int) : signLayout == null : NULL");
        }
        SignPage signPage = signLayout.getSignPage(beginningPage);
        if (signPage == null) {
            throw new NullPointerException("SignScreenManager : goToLayoutAndPage(String, Int) : signPage == null : NULL");
        }
        this.currentLayout = signLayout;
        this.currentPage = signPage;
        this.pointerLine = signPage.getStartLine();
        this.pointerLineOffset = signPage.getSignLinePatternMap().get(this.pointerLine).getIndexOfSide(1);
        this.currentSide = signPage.getSignLinePatternMap().get(this.pointerLine).getMinSide();
        this.signCache = signPage.toSignCache();
        this.needsTextChanged = true;
    }

    private void stagePageAndLine(SignPage signPage, boolean startWithMax) {
        stagePage(signPage);
        if (startWithMax) {
            stageLine(signPage.getMax(), true);
        } else {
            stageLine(signPage.getStartLine(), false);
        }
    }

    private void stagePage(SignPage signPage) {
        this.currentPage = signPage;
    }

    private void stageLine(int line, boolean startWithMax) {
        this.pointerLine = line;
        SignLinePattern signLinePattern = this.currentPage.getSignLinePatternMap().get(this.pointerLine);
        if (startWithMax) {
            this.currentSide = signLinePattern.getMaxSide();
        } else {
            this.currentSide = signLinePattern.getMinSide();
        }
        this.pointerLineOffset = signLinePattern.getIndexOfSide(this.currentSide);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ISignScreen getSignScreen() {
        return signScreen;
    }

    public void setSignScreen(ISignScreen signScreen) {
        this.signScreen = signScreen;
    }

    public SignLayout getCurrentLayout() {
        return currentLayout;
    }

    public void setCurrentLayout(SignLayout currentLayout) {
        this.currentLayout = currentLayout;
    }

    public SignPage getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(SignPage currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentSide() {
        return currentSide;
    }

    public void setCurrentSide(int currentSide) {
        this.currentSide = currentSide;
    }

    public int getPointerLine() {
        return pointerLine;
    }

    public void setPointerLine(int pointerLine) {
        this.pointerLine = pointerLine;
    }

    public int getPointerLineOffset() {
        return pointerLineOffset;
    }

    public void setPointerLineOffset(int pointerLineOffset) {
        this.pointerLineOffset = pointerLineOffset;
    }

    public SignCache getSignCache() {
        return signCache;
    }

    public void setSignCache(SignCache signCache) {
        this.signCache = signCache;
    }

    public boolean isNeedsLineChanged() {
        return needsLineChanged;
    }

    public void setNeedsLineChanged(boolean needsLineChanged) {
        this.needsLineChanged = needsLineChanged;
    }

    public boolean isNeedsTextChanged() {
        return needsTextChanged;
    }

    public void setNeedsTextChanged(boolean needsTextChanged) {
        this.needsTextChanged = needsTextChanged;
    }

    public boolean isTickerRunning() {
        return isTickerRunning;
    }

    public void setTickerRunning(boolean tickerRunning) {
        isTickerRunning = tickerRunning;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public long getTickSpeed() {
        return tickSpeed;
    }

    public void setTickSpeed(long tickSpeed) {
        this.tickSpeed = tickSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignScreenManager that = (SignScreenManager) o;
        return currentSide == that.currentSide &&
                pointerLine == that.pointerLine &&
                pointerLineOffset == that.pointerLineOffset &&
                needsLineChanged == that.needsLineChanged &&
                needsTextChanged == that.needsTextChanged &&
                isTickerRunning == that.isTickerRunning &&
                stop == that.stop &&
                tickSpeed == that.tickSpeed &&
                Objects.equals(plugin, that.plugin) &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(signScreen, that.signScreen) &&
                Objects.equals(currentLayout, that.currentLayout) &&
                Objects.equals(currentPage, that.currentPage) &&
                Objects.equals(signCache, that.signCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, name, location, signScreen, currentLayout, currentPage, currentSide, pointerLine, pointerLineOffset, signCache, needsLineChanged, needsTextChanged, isTickerRunning, stop, tickSpeed);
    }

    @Override
    public String toString() {
        return "SignScreenManager{" +
                "plugin=" + plugin +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", signScreen=" + signScreen +
                ", currentLayout=" + currentLayout +
                ", currentPage=" + currentPage +
                ", currentSide=" + currentSide +
                ", pointerLine=" + pointerLine +
                ", pointerLineOffset=" + pointerLineOffset +
                ", signCache=" + signCache +
                ", needsLineChanged=" + needsLineChanged +
                ", needsTextChanged=" + needsTextChanged +
                ", isTickerRunning=" + isTickerRunning +
                ", stop=" + stop +
                ", tickSpeed=" + tickSpeed +
                '}';
    }
}
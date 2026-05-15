package com.andrew121410.mc.world16utils.gui.animation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Animation {

    private static final long FRAME_DURATION_MS = 50;
    private static final int FADING_FRAMES = 40;
    private static final int TYPEWRITER_HOLD_FRAMES = 10;
    private static final char[] GLITCH_CHARS = "!@#$%^&*<>?/\\|~`".toCharArray();

    private static final Map<String, List<Component>> CACHE = new ConcurrentHashMap<>();

    /**
     * Parses a MiniMessage color tag (e.g. "&lt;gold&gt;", "&lt;#FF0000&gt;") into a TextColor.
     * Useful for passing currency colors or other MiniMessage colors into animation methods.
     */
    public static TextColor color(String miniMessageTag) {
        Component c = MiniMessage.miniMessage().deserialize(miniMessageTag + "x");
        TextColor color = c.color();
        if (color == null && !c.children().isEmpty()) {
            color = c.children().get(0).color();
        }
        return color != null ? color : TextColor.color(0xFFFFFF);
    }

    /**
     * Color sweeps across each character of the text.
     * Colors define the gradient — at least 2 recommended.
     */
    public static Component wave(String text, TextColor... colors) {
        String key = "wave|" + text + "|" + colorKey(colors);
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildWaveFrames(text, colors));
        return frames.get(currentIndex(frames.size()));
    }

    /**
     * Entire text fades between the provided colors.
     * Colors define the gradient — at least 2 recommended.
     */
    public static Component fading(String text, TextColor... colors) {
        String key = "fading|" + text + "|" + colorKey(colors);
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildFadingFrames(text, colors));
        return frames.get(currentIndex(frames.size()));
    }

    /**
     * Characters appear one at a time left to right, hold, then disappear right to left.
     */
    public static Component typewriter(String text, TextColor color) {
        String key = "typewriter|" + text + "|" + color.asHexString();
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildTypewriterFrames(text, color));
        return frames.get(currentIndex(frames.size()));
    }

    /**
     * Snaps between two colors — good for alerts or highlighting.
     */
    public static Component blink(String text, TextColor color1, TextColor color2) {
        String key = "blink|" + text + "|" + color1.asHexString() + "|" + color2.asHexString();
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildBlinkFrames(text, color1, color2));
        return frames.get(currentIndex(frames.size()));
    }

    /**
     * Randomly substitutes characters with symbols for a glitchy effect.
     */
    public static Component glitch(String text, TextColor color) {
        String key = "glitch|" + text + "|" + color.asHexString();
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildGlitchFrames(text, color));
        return frames.get(currentIndex(frames.size()));
    }

    private static int currentIndex(int frameCount) {
        return (int) ((System.currentTimeMillis() / FRAME_DURATION_MS) % frameCount);
    }

    // --- Frame builders ---

    private static List<Component> buildWaveFrames(String text, TextColor[] colors) {
        char[] chars = text.toCharArray();
        int gradientSize = Math.max(chars.length * 2, 2);
        List<TextColor> gradient = buildGradient(colors, gradientSize);

        List<Component> frames = new ArrayList<>(gradientSize);
        for (int frame = 0; frame < gradientSize; frame++) {
            Component line = Component.empty();
            for (int j = 0; j < chars.length; j++) {
                TextColor c = gradient.get((frame + j) % gradientSize);
                line = line.append(Component.text(chars[j]).color(c)
                        .decoration(TextDecoration.ITALIC, false));
            }
            frames.add(line);
        }
        return frames;
    }

    private static List<Component> buildFadingFrames(String text, TextColor[] colors) {
        List<TextColor> forward = buildGradient(colors, FADING_FRAMES / 2 + 1);
        List<TextColor> pingpong = new ArrayList<>(FADING_FRAMES);
        pingpong.addAll(forward.subList(0, forward.size() - 1));
        List<TextColor> reversed = new ArrayList<>(forward.subList(1, forward.size() - 1));
        java.util.Collections.reverse(reversed);
        pingpong.addAll(reversed);

        List<Component> frames = new ArrayList<>(pingpong.size());
        for (TextColor c : pingpong) {
            frames.add(Component.text(text).color(c).decoration(TextDecoration.ITALIC, false));
        }
        return frames;
    }

    private static List<Component> buildTypewriterFrames(String text, TextColor color) {
        List<Component> frames = new ArrayList<>();

        // Appear: show 1 char, 2 chars, ... n chars
        for (int i = 1; i <= text.length(); i++) {
            frames.add(Component.text(text.substring(0, i)).color(color)
                    .decoration(TextDecoration.ITALIC, false));
        }

        // Hold at full text
        Component full = Component.text(text).color(color).decoration(TextDecoration.ITALIC, false);
        for (int i = 0; i < TYPEWRITER_HOLD_FRAMES; i++) {
            frames.add(full);
        }

        // Disappear: show n-1 chars, n-2 chars, ... 1 char
        for (int i = text.length() - 1; i >= 1; i--) {
            frames.add(Component.text(text.substring(0, i)).color(color)
                    .decoration(TextDecoration.ITALIC, false));
        }

        // Hold at single char before looping
        for (int i = 0; i < TYPEWRITER_HOLD_FRAMES; i++) {
            frames.add(Component.text(String.valueOf(text.charAt(0))).color(color)
                    .decoration(TextDecoration.ITALIC, false));
        }

        return frames;
    }

    private static List<Component> buildBlinkFrames(String text, TextColor color1, TextColor color2) {
        // 10 frames each color — slow enough to read, fast enough to notice
        int framesPerColor = 10;
        List<Component> frames = new ArrayList<>(framesPerColor * 2);
        Component c1 = Component.text(text).color(color1).decoration(TextDecoration.ITALIC, false);
        Component c2 = Component.text(text).color(color2).decoration(TextDecoration.ITALIC, false);
        for (int i = 0; i < framesPerColor; i++) frames.add(c1);
        for (int i = 0; i < framesPerColor; i++) frames.add(c2);
        return frames;
    }

    private static List<Component> buildGlitchFrames(String text, TextColor color) {
        int totalFrames = 30;
        List<Component> frames = new ArrayList<>(totalFrames);
        char[] chars = text.toCharArray();

        for (int frame = 0; frame < totalFrames; frame++) {
            // ~20% of frames are glitched
            boolean isGlitched = frame % 5 == 0;
            if (!isGlitched) {
                frames.add(Component.text(text).color(color).decoration(TextDecoration.ITALIC, false));
                continue;
            }

            // Deterministic per frame: seed from frame index
            Random rng = new Random(frame * 31L + text.hashCode());
            Component line = Component.empty();
            for (char ch : chars) {
                boolean replace = rng.nextFloat() < 0.25f;
                char display = replace ? GLITCH_CHARS[rng.nextInt(GLITCH_CHARS.length)] : ch;
                line = line.append(Component.text(display).color(color)
                        .decoration(TextDecoration.ITALIC, false));
            }
            frames.add(line);
        }
        return frames;
    }

    // --- Color math ---

    private static List<TextColor> buildGradient(TextColor[] colors, int steps) {
        List<TextColor> gradient = new ArrayList<>(steps);
        if (colors.length == 1 || steps == 1) {
            for (int i = 0; i < steps; i++) gradient.add(colors[0]);
            return gradient;
        }
        int segments = colors.length - 1;
        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1) * segments;
            int seg = Math.min((int) t, segments - 1);
            gradient.add(lerp(colors[seg], colors[seg + 1], t - seg));
        }
        return gradient;
    }

    private static TextColor lerp(TextColor a, TextColor b, float t) {
        int r  = (int) (a.red()   + (b.red()   - a.red())   * t);
        int g  = (int) (a.green() + (b.green() - a.green()) * t);
        int bl = (int) (a.blue()  + (b.blue()  - a.blue())  * t);
        return TextColor.color(clamp(r), clamp(g), clamp(bl));
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private static String colorKey(TextColor[] colors) {
        return Arrays.stream(colors).map(TextColor::asHexString).collect(Collectors.joining(","));
    }
}

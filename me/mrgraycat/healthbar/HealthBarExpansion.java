package me.mrgraycat.healthbar;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthBarExpansion extends PlaceholderExpansion implements Configurable {
	private final Map<String, Object> defaults = new LinkedHashMap<String, Object>() {{
		put("show-empty-hearts", true);
		put("show-absorption-hearts", false);
		put("max-absorption-hearts", 2);

		put("icons.full-heart", "\u2764");
		put("icons.half-heart", "\u2665");
		put("icons.empty-heart", "\u2764");
		put("icons.absorption-heart", "\u2764");
		put("icons.half-absorption-heart", "\u2665");

		put("colors.big-heart", "&4");
		put("colors.half-heart", "&c");
		put("colors.empty-heart", "&7");
		put("colors.absorption-heart", "&6");
		put("colors.half-absorption-heart", "&e");
	}};

	boolean showEmptyHearts;
	boolean showAbsorptionHearts;
	int maxAbsorptionHearts;

	String iconFullHeart;
	String iconHalfHeart;
	String iconEmptyHeart;
	String iconAbsorptionHeart;
	String iconHalfAbsorptionHeart;

	String colorBigHeart;
	String colorHalfHeart;
	String colorEmptyHeart;
	String colorAbsorptionHeart;
	String colorHalfAbsorptionHeart;

	@Override
	public @NotNull String getAuthor() {
		return "MrGraycat";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "healthbar";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.3";
	}

	@Override
	public boolean canRegister() {
		loadValues();
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String identifier) {
		if (player == null) {
			return null;
		}

		StringBuilder healthbar = new StringBuilder();
		double health = Math.ceil((Objects.requireNonNull(player.getPlayer()).getHealth()));
		double absorption = Math.ceil((player.getPlayer().getAbsorptionAmount()));

		if (health > 20 || player.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			health = 20;
		}

		for (double i = 10; i > 0; i--) {
			if (health >= 2) {
				healthbar.append(this.colorBigHeart).append(this.iconFullHeart);
				health = health - 2;
			} else if (health > 0) {
				healthbar.append(this.colorHalfHeart).append(this.iconHalfHeart);
				health = 0;
			} else {
				if (this.showEmptyHearts) {
					healthbar.append(this.colorEmptyHeart).append(this.iconEmptyHeart);
				}
			}
		}

		if (this.showAbsorptionHearts) {
			for (int i = this.maxAbsorptionHearts; i > 0; i--) {

				if (absorption >= 2) {
					healthbar.append(this.colorAbsorptionHeart).append(this.iconAbsorptionHeart);
					absorption = absorption - 2;
				} else if (absorption > 0) {
					healthbar.append(this.colorHalfAbsorptionHeart).append(this.iconHalfAbsorptionHeart);
					absorption = 0;
				} else {
					break;
				}
			}
		}
		return toChatColor(healthbar.toString());
	}

	@Override
	public Map<String, Object> getDefaults() {
		return this.defaults;
	}

	public void loadValues() {
		this.showEmptyHearts = getBoolean("show-empty-hearts", true);
		this.showAbsorptionHearts = getBoolean("show-absorption-hearts", false);
		this.maxAbsorptionHearts = getInt("max-absorption-hearts", 2);

		this.iconFullHeart = getString("icons.full-heart", "\u2764");
		this.iconHalfHeart = getString("icons.half-heart", "\u2665");
		this.iconEmptyHeart = getString("icons.empty-heart", "\u2764");
		this.iconAbsorptionHeart = 	getString("icons.absorption-heart", "\u2764");
		this.iconHalfAbsorptionHeart = 	getString("icons.half-absorption-heart", "\u2764");

		this.colorBigHeart = getString("colors.big-heart", "&4");
		this.colorHalfHeart = getString("colors.half-heart", "&c");
		this.colorEmptyHeart = getString("colors.empty-heart", "&7");
		this.colorAbsorptionHeart =	getString("colors.absorption-heart", "&6");
		this.colorHalfAbsorptionHeart = getString("colors.half-absorption-heart", "&e");
	}

	private String toChatColor(String text) {
		if (text.contains("#")) {
			text = translateHexColorCodes(text);
		}
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

	public static String translateHexColorCodes(String message) {
		Matcher matcher = pattern.matcher(message);
		while (matcher.find()) {
			String color = message.substring(matcher.start(), matcher.end());
			message = message.replace(color, ChatColor.COLOR_CHAR + color);
		}
		return message;
	}
}

package venture.Aust1n46.shopsearcher.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
	public static final char BUKKIT_COLOR_CODE_PREFIX_CHAR = '\u00A7';
	public static final String BUKKIT_COLOR_CODE_PREFIX = String.valueOf(BUKKIT_COLOR_CODE_PREFIX_CHAR);
	public static final String BUKKIT_HEX_COLOR_CODE_PREFIX = "x";

	private static final Pattern LEGACY_CHAT_COLOR_DIGITS_PATTERN = Pattern.compile("&([0-9])");
	private static final Pattern LEGACY_CHAT_COLOR_PATTERN = Pattern
			.compile("(?<!(&x(&[a-fA-F0-9]){5}))(?<!(&x(&[a-fA-F0-9]){4}))(?<!(&x(&[a-fA-F0-9]){3}))(?<!(&x(&[a-fA-F0-9]){2}))(?<!(&x(&[a-fA-F0-9]){1}))(?<!(&x))(&)([0-9a-fA-F])");

	/**
	 * Convert a single hex color code to a single Bukkit hex color code.
	 *
	 * @param color to convert.
	 * @return {@link String}
	 */
	public static String convertHexColorCodeToBukkitColorCode(String color) {
		color = color.replace("&", "");
		StringBuilder bukkitColorCode = new StringBuilder(BUKKIT_COLOR_CODE_PREFIX + BUKKIT_HEX_COLOR_CODE_PREFIX);
		for (int a = 1; a < color.length(); a++) {
			bukkitColorCode.append(BUKKIT_COLOR_CODE_PREFIX + color.charAt(a));
		}
		return bukkitColorCode.toString().toLowerCase();
	}

	/**
	 * Convert an entire String of hex color codes to Bukkit hex color codes.
	 *
	 * @param string to convert.
	 * @return {@link String}
	 */
	public static String convertHexColorCodeStringToBukkitColorCodeString(String string) {
		Pattern pattern = Pattern.compile("(&?#[0-9a-fA-F]{6})");
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			int indexStart = matcher.start();
			int indexEnd = matcher.end();
			String hexColor = string.substring(indexStart, indexEnd);
			String bukkitColor = convertHexColorCodeToBukkitColorCode(hexColor);
			string = string.replaceAll(hexColor, bukkitColor);
			matcher.reset(string);
		}
		return string;
	}

	/**
	 * Formats a string with both Spigot legacy colors codes and Spigot and
	 * VentureChat hex color codes.
	 *
	 * @param string to format.
	 * @return {@link String}
	 */
	public static String FormatStringColor(String string) {
		String allFormated = string;
		allFormated = LEGACY_CHAT_COLOR_DIGITS_PATTERN.matcher(allFormated).replaceAll("\u00A7$1");

		allFormated = allFormated.replaceAll("&[x]", BUKKIT_COLOR_CODE_PREFIX + "x");
		allFormated = allFormated.replaceAll("&[aA]", BUKKIT_COLOR_CODE_PREFIX + "a");
		allFormated = allFormated.replaceAll("&[bB]", BUKKIT_COLOR_CODE_PREFIX + "b");
		allFormated = allFormated.replaceAll("&[cC]", BUKKIT_COLOR_CODE_PREFIX + "c");
		allFormated = allFormated.replaceAll("&[dD]", BUKKIT_COLOR_CODE_PREFIX + "d");
		allFormated = allFormated.replaceAll("&[eE]", BUKKIT_COLOR_CODE_PREFIX + "e");
		allFormated = allFormated.replaceAll("&[fF]", BUKKIT_COLOR_CODE_PREFIX + "f");

		allFormated = allFormated.replaceAll("%", "\\%");

		allFormated = convertHexColorCodeStringToBukkitColorCodeString(allFormated);
		return allFormated;
	}

	/**
	 * Formats a string with only legacy Spigot color codes &[0-9a-f]. Does not
	 * format the legacy color codes that make up a Spigot hex color code.
	 *
	 * @param string to format.
	 * @return {@link String}
	 */
	public static String FormatStringLegacyColor(String string) {
		String allFormated = string;

		allFormated = LEGACY_CHAT_COLOR_PATTERN.matcher(allFormated).replaceAll("\u00A7$13");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[A]", BUKKIT_COLOR_CODE_PREFIX + "a");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[B]", BUKKIT_COLOR_CODE_PREFIX + "b");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[C]", BUKKIT_COLOR_CODE_PREFIX + "c");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[D]", BUKKIT_COLOR_CODE_PREFIX + "d");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[E]", BUKKIT_COLOR_CODE_PREFIX + "e");
		allFormated = allFormated.replaceAll(BUKKIT_COLOR_CODE_PREFIX + "[F]", BUKKIT_COLOR_CODE_PREFIX + "f");

		allFormated = allFormated.replaceAll("%", "\\%");
		return allFormated;
	}

	/**
	 * Formats a string with Spigot formatting codes.
	 *
	 * @param string to format.
	 * @return {@link String}
	 */
	public static String FormatString(String string) {
		String allFormated = string;
		allFormated = allFormated.replaceAll("&[kK]", BUKKIT_COLOR_CODE_PREFIX + "k");
		allFormated = allFormated.replaceAll("&[lL]", BUKKIT_COLOR_CODE_PREFIX + "l");
		allFormated = allFormated.replaceAll("&[mM]", BUKKIT_COLOR_CODE_PREFIX + "m");
		allFormated = allFormated.replaceAll("&[nN]", BUKKIT_COLOR_CODE_PREFIX + "n");
		allFormated = allFormated.replaceAll("&[oO]", BUKKIT_COLOR_CODE_PREFIX + "o");
		allFormated = allFormated.replaceAll("&[rR]", BUKKIT_COLOR_CODE_PREFIX + "r");

		allFormated = allFormated.replaceAll("%", "\\%");
		return allFormated;
	}

	/**
	 * Formats a string with Spigot legacy colors codes, Spigot and VentureChat hex
	 * color codes, and Spigot formatting codes.
	 *
	 * @param string to format.
	 * @return {@link String}
	 */
	public static String FormatStringAll(String string) {
		String allFormated = FormatString(string);
		allFormated = FormatStringColor(allFormated);
		return allFormated;
	}
}

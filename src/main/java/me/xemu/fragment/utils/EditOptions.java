package me.xemu.fragment.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditOptions {
	WEIGHT("weight"),
	PREFIX("prefix"),
	SUFFIX("suffix"),
	FORMAT("format");

	private String key;

	public static EditOptions getOptionByKey(String key) {
		return switch (key.toLowerCase()) {
			case "weight" -> WEIGHT;
			case "prefix" -> PREFIX;
			case "suffix" -> SUFFIX;
			case "format" -> FORMAT;
			default -> null;
		};
	}

}
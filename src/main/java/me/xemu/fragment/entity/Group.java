package me.xemu.fragment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Group {
	private String name;
	private int weight;
	private String prefix;
	private String suffix;
	private String format;
	private List<String> permissions;
}



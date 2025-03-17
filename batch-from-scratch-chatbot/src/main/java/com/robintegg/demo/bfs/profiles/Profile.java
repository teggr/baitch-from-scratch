package com.robintegg.demo.bfs.profiles;

public class Profile {

	public enum Type {
		USER,
		ASSISTANT
	}

	private final String name;
	private final String image;
	private final Type type;

	public Profile( String name, String image, Type type ) {
		this.name = name;
		this.image = image;
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static Profile user() {
		return new Profile( "Laura",
				"/images/laura.webp",
				Type.USER );
	}

	public static Profile joe() {
		return new Profile( "Joe",
				"/images/joe.png",
				Type.ASSISTANT );
	}

	public static Profile suzanne() {
		return new Profile( "Suzanne",
				"/images/suzanne.png",
				Type.ASSISTANT );
	}

}

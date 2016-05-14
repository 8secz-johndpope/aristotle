package com.aristotle.member;

public class CountryBean {
	private Long id;
	private String name;
	private String profilePic = "https://cdn4.iconfinder.com/data/icons/info-graphics-4-glyph-free/64/one_user_data_graphic_info_infos-24.png";
	
	public CountryBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CountryBean(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
}

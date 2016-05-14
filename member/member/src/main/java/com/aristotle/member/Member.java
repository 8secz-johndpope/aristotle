package com.aristotle.member;

import java.io.Serializable;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

public class Member implements Serializable{

	private static final long serialVersionUID = -1284957351628127727L;
	
	private String nri;
	private String name;
	private String creationtype;
	private String phones[];
	private String member;
	private Date mstartdate;
	private Date menddate;
	private String donor;
	private String id;
	private String votingdistrict;
	private String votingpcid;
	private String livingdistrictid;
	private String livingac;
	private String livingpcid;
	private String livingacid;
	private String livingstate;
	private String votingdistrictid;
	private String livingpc;
	private String votingstateid;
	private String emails[];
	private String votingstate;
	private String livingstateid;
	private Date dob;
	private String votingac;
	private String votingacid;
	private String livingdistrict;
	private String votingpc;
	private String profilepic;
	private Resource profilepicResource;
	private String memberid;
	private String state;
	
	public String getState(){
		if(!StringUtils.isEmpty(livingstate)){
			return livingstate;
		}
		if(!StringUtils.isEmpty(votingstate)){
			return votingstate;
		}
		return "";
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNri() {
		return nri;
	}
	public void setNri(String nri) {
		this.nri = nri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreationtype() {
		return creationtype;
	}
	public void setCreationtype(String creationtype) {
		this.creationtype = creationtype;
	}
	public String[] getPhones() {
		return phones;
	}
	public void setPhones(String[] phones) {
		this.phones = phones;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public Date getMstartdate() {
		return mstartdate;
	}
	public void setMstartdate(Date mstartdate) {
		this.mstartdate = mstartdate;
	}
	public Date getMenddate() {
		return menddate;
	}
	public void setMenddate(Date menddate) {
		this.menddate = menddate;
	}
	public String getDonor() {
		return donor;
	}
	public void setDonor(String donor) {
		this.donor = donor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVotingdistrict() {
		return votingdistrict;
	}
	public void setVotingdistrict(String votingdistrict) {
		this.votingdistrict = votingdistrict;
	}
	public String getVotingpcid() {
		return votingpcid;
	}
	public void setVotingpcid(String votingpcid) {
		this.votingpcid = votingpcid;
	}
	public String getLivingdistrictid() {
		return livingdistrictid;
	}
	public void setLivingdistrictid(String livingdistrictid) {
		this.livingdistrictid = livingdistrictid;
	}
	public String getLivingac() {
		return livingac;
	}
	public void setLivingac(String livingac) {
		this.livingac = livingac;
	}
	public String getLivingpcid() {
		return livingpcid;
	}
	public void setLivingpcid(String livingpcid) {
		this.livingpcid = livingpcid;
	}
	public String getLivingacid() {
		return livingacid;
	}
	public void setLivingacid(String livingacid) {
		this.livingacid = livingacid;
	}
	public String getLivingstate() {
		return livingstate;
	}
	public void setLivingstate(String livingstate) {
		this.livingstate = livingstate;
	}
	public String getVotingdistrictid() {
		return votingdistrictid;
	}
	public void setVotingdistrictid(String votingdistrictid) {
		this.votingdistrictid = votingdistrictid;
	}
	public String getLivingpc() {
		return livingpc;
	}
	public void setLivingpc(String livingpc) {
		this.livingpc = livingpc;
	}
	public String getVotingstateid() {
		return votingstateid;
	}
	public void setVotingstateid(String votingstateid) {
		this.votingstateid = votingstateid;
	}
	public String[] getEmails() {
		return emails;
	}
	public void setEmails(String[] emails) {
		this.emails = emails;
	}
	public String getVotingstate() {
		return votingstate;
	}
	public void setVotingstate(String votingstate) {
		this.votingstate = votingstate;
	}
	public String getLivingstateid() {
		return livingstateid;
	}
	public void setLivingstateid(String livingstateid) {
		this.livingstateid = livingstateid;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getVotingac() {
		return votingac;
	}
	public void setVotingac(String votingac) {
		this.votingac = votingac;
	}
	public String getVotingacid() {
		return votingacid;
	}
	public void setVotingacid(String votingacid) {
		this.votingacid = votingacid;
	}
	public String getLivingdistrict() {
		return livingdistrict;
	}
	public void setLivingdistrict(String livingdistrict) {
		this.livingdistrict = livingdistrict;
	}
	public String getVotingpc() {
		return votingpc;
	}
	public void setVotingpc(String votingpc) {
		this.votingpc = votingpc;
	}
	public String getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
		if(!StringUtils.isEmpty(profilepic)){
			this.profilepicResource = new ExternalResource("http://static.swarajabhiyan.org/"+profilepic);
		}else{
			this.profilepicResource = new ExternalResource("https://cdn3.iconfinder.com/data/icons/blackblue/32/iPhoto.png");
		}
	}
	public Resource getProfilepicResource() {
		return profilepicResource;
	}
	public void setProfilepicResource(Resource profilepicResource) {
	}
	public String getMemberid() {
		if(StringUtils.isEmpty(memberid)){
			return id;
		}
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

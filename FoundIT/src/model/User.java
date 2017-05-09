package model;

public class User {
	String userId;
	String password;
	String realName;
	String phoneNum;
	String address;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User(String userId, String password, String realName,
			String phoneNum, String address) {
		this.userId = userId;
		this.password = password;
		this.realName = realName;
		this.phoneNum = phoneNum;
		this.address = address;
	}
	
}

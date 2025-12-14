package com.library.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Member model class representing a library member or librarian
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int memberId;
    private String name;
    private String email;
    private String password;
    private String membershipId;
    private String phone;
    private String address;
    private LocalDate registrationDate;
    private String status;
    private String userType; // MEMBER or LIBRARIAN
    
    public Member() {
        this.status = "ACTIVE";
        this.userType = "MEMBER";
    }
    
    public Member(String name, String email, String password, String membershipId) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.membershipId = membershipId;
        this.registrationDate = LocalDate.now();
    }
    
    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }
    
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getMembershipId() {
        return membershipId;
    }
    
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String getRegistrationDateString() {
        if (registrationDate != null) {
            return registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
    
    public void setRegistrationDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.registrationDate = LocalDate.parse(dateString);
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public boolean isLibrarian() {
        return "LIBRARIAN".equalsIgnoreCase(userType);
    }
    
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", membershipId='" + membershipId + '\'' +
                ", userType='" + userType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return memberId == member.memberId || 
               (email != null && email.equals(member.email)) ||
               (membershipId != null && membershipId.equals(member.membershipId));
    }
    
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : memberId;
    }
}


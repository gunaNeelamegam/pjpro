package com.zilogic.pjproject;




//@Getter
//@Setter
//@ToString
class AddAccount {

    @Override
    public String toString() {
        return "AddAccount{" + "username=" + username + ", password=" + password + ", domainAddress=" + domainAddress + '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDomainAddress(String domainAddress) {
        this.domainAddress = domainAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDomainAddress() {
        return domainAddress;
    }

    String username;
    String password;
    String domainAddress;

}

package com.ecommerce.model;
public abstract class User {
    private final String id, name, email, phone;
    protected User(String id, String name, String email, String phone) {
        this.id=id; this.name=name; this.email=email; this.phone=phone;
    }
    public String getId(){return id;} public String getName(){return name;}
    public String getEmail(){return email;} public String getPhone(){return phone;}
}

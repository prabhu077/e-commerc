package com.ecommerce.model;
public class Address {
    private final String id,line,city,state,pincode;
    public Address(String id,String line,String city,String state,String pincode){this.id=id;this.line=line;this.city=city;this.state=state;this.pincode=pincode;}
    public String getId(){return id;} public String toString(){return line+", "+city+", "+state+" - "+pincode;}
}

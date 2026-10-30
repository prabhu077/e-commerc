package com.ecommerce.model;
import java.util.*;
public class Customer extends User {
    private final List<Address> addresses = new ArrayList<>();
    private String defaultAddressId;
    public Customer(String id,String name,String email,String phone,Address first){
        super(id,name,email,phone); addAddress(first); setDefaultAddress(first.getId());
    }
    public void addAddress(Address a){ if(addresses.size()>=5) throw new IllegalStateException("Maximum 5 addresses"); addresses.add(a); }
    public void setDefaultAddress(String id){ if(addresses.stream().noneMatch(a->a.getId().equals(id))) throw new IllegalArgumentException("Address not found"); defaultAddressId=id; }
    public Address getDefaultAddress(){return addresses.stream().filter(a->a.getId().equals(defaultAddressId)).findFirst().orElseThrow();}
    public List<Address> getAddresses(){return Collections.unmodifiableList(addresses);}
}

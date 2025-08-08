package com.example.doctorapp.model;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "doctors")

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private Long specialtyId ;
     private Long userId ;
     private String firstName ;
     private String lastName;
     private String email ; 
     private String location;
     private int experience ;
     
     public Doctor(){}
     public Doctor (Long id , Long specialtyId , Long userId , String firstName , String lastName , String email ,String location, int experience ){
         this.id = id;
         this.specialtyId = specialtyId ;
         this.userId=userId;
         this.firstName=firstName;
         this.lastName=lastName;
         this.email=email;
         this.location=location;
         this.experience=experience;
         
     }
     public Long getId(){return id ;}
     public void setId(Long id){this.id=id; }
     
     public Long getSpecialtyId( ){return specialtyId;}
     public void setSpecialtyId (Long specialtyId ){ this.specialtyId =specialtyId ; }
     
     public Long getUserId() { return userId; }
     public void setUserId(Long userId) { this.userId = userId; }  
     
     public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
     
    public String getLocation(){return location;}
    public void setLocation( String location){this.location=location;}
    
    public int getExperience(){return experience;}
    public void setExperience(int experience ){this.experience=experience;}
}
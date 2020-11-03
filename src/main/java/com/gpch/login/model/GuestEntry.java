package com.gpch.login.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guest_entry")
public class GuestEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "guest_entry_id")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @Column(name = "text")    
    private String text;
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length=100000)
    private byte[] image;
    @Column(name = "approved")
    private Boolean approved;
    
    
    

}

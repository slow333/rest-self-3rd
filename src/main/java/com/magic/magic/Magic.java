package com.magic.magic;

import com.magic.hero.Hero;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@Data
public class Magic {
   
   @Id
   private String id;
   
   private String name;
   
   private String description;

   @ManyToOne
   private Hero owner;
}

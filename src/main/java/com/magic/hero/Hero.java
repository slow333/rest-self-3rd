package com.magic.hero;

import java.util.ArrayList;
import java.util.List;

import com.magic.magic.Magic;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
public class Hero {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;

   private String name;

   @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "owner")
   private List<Magic> magics = new ArrayList<>();

   public void addMagic(@NotNull Magic magic) {
      magic.setOwner(this);
      magics.add(magic);
   }

   public void removeMagic(Magic magic) {
      magics.remove(magic);
      magic.setOwner(null);
   }

   public Integer getNumberOfMagics() {
      return magics.size();
   }

   public void deleteAllMagic() {
      magics.forEach(m -> m.setOwner(null));
      this.magics = List.of();
   }
}

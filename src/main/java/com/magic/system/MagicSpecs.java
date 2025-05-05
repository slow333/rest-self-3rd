package com.magic.system;

import com.magic.magic.Magic;
import org.springframework.data.jpa.domain.Specification;

public class MagicSpecs {

  public static Specification<Magic> hasId(String id) {
    return (root, query, cb) ->
            cb.equal(root.get("id"), id);
  }

  public static Specification<Magic> nameLike(String name) {
    return (root, query, cb)
            -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  public static Specification<Magic> containsDescription(String description) {
    return (root, query, cb)
            -> cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
  }

  public static Specification<Magic> hasOwner(String ownerName) {
    return (root, query, cb) ->
            cb.like(cb.lower(root.get("owner").get("name")), "%" + ownerName.toLowerCase()+"%");
  }
}
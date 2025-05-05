package com.magic.magic;

import ch.qos.logback.core.util.StringUtil;
import com.magic.system.IdWorker;
import com.magic.system.MagicSpecs;
import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MagicService {

  private final MagicRepository magicRepository;
  private final IdWorker idWorker;

  public Magic findById(String magicId) throws ObjectNotFoundException {
    return magicRepository.findById(magicId)
            .orElseThrow(() -> new ObjectNotFoundException("magic", magicId));
  }
  public List<Magic> findAll() {
    return magicRepository.findAll();
  }

  public Page<Magic> findAll(Pageable pageable) {
    return magicRepository.findAll(pageable);
  }

  public Magic add(Magic magic){
    magic.setId(idWorker.nextId() + "");
    return magicRepository.save(magic);
  }

  public Magic update(String magicId, Magic update) throws ObjectNotFoundException {
    Magic old = magicRepository.findById(magicId)
            .orElseThrow(() -> new ObjectNotFoundException("magic", magicId));
    old.setId(update.getId());
    old.setName(update.getName());
    old.setDescription(update.getDescription());
    old.setOwner(update.getOwner());
    magicRepository.save(old);
    return old;
  }

  public void delete(String magicId) throws ObjectNotFoundException {
    magicRepository.findById(magicId)
            .orElseThrow(() -> new ObjectNotFoundException("magic", magicId));
    magicRepository.deleteById(magicId);
  }

  public Page<Magic> findByQuery(Map<String, String> query, Pageable pageable) {
    Specification<Magic> spec = Specification.where(null);
    if(StringUtils.hasLength(query.get("name"))){
      spec = spec.and(MagicSpecs.nameLike(query.get("name")));
    }
    if(StringUtils.hasLength(query.get("description"))){
      spec = spec.and(MagicSpecs.containsDescription(query.get("description")));
    }
    if(StringUtils.hasLength(query.get("id"))){
      spec = spec.and(MagicSpecs.hasId(query.get("id")));
    }
    if(StringUtils.hasLength(query.get("ownerName"))){
      spec = spec.and(MagicSpecs.hasOwner(query.get("ownerName")));
    }
    return magicRepository.findAll(spec, pageable);
  }
}

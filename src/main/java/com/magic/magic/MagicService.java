package com.magic.magic;

import com.magic.system.IdWorker;
import com.magic.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}

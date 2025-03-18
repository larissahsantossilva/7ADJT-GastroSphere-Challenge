package br.com.fiap.gastrosphere.repositories;

import java.util.UUID;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemMenuRepository extends CrudRepository<Item, UUID> {

}

package br.com.power.correios.repository;

import br.com.power.correios.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, String> { }

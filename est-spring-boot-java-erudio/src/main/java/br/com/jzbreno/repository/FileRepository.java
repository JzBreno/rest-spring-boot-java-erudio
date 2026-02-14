package br.com.jzbreno.repository;

import br.com.jzbreno.model.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File,Long> {
}

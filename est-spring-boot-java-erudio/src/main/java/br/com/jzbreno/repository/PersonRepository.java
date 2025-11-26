package br.com.jzbreno.repository;

import br.com.jzbreno.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    //adicionando Modifying ele garente o uso de ACED no banco de dados
    //usando clearAutomatically = true para corrigir o cache de segundo nivel do hibernate
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p set p.enabled = false WHERE p.id =:id")
    void disablePerson( @Param("id") Long id);

}

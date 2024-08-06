package br.com.guilherme.gestao_vagas.modules.company.repositories;

import br.com.guilherme.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

    // O Containing faz algo como SELECT * FROM job WHERE description LIKE %filter%
    /*
        O IgnoreCase faz com que o valor do filter seja igual ao banco de dados
        assim mesmo que o usuário digite de forma diferente que no banco se o
        conteudo da string for o mesmo tanto no banco quanto na requisição a informação
        vai ser achada;
    */
    List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
}

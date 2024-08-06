package br.com.guilherme.gestao_vagas.modules.company.repositories;

import br.com.guilherme.gestao_vagas.modules.company.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    Optional<CompanyEntity> findByUsername(String username);
    Optional<CompanyEntity> findByEmail(String email);

}

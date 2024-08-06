package br.com.guilherme.gestao_vagas.modules.company.useCases;

import br.com.guilherme.gestao_vagas.exceptions.UserFoundException;
import br.com.guilherme.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.guilherme.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity){

        companyRepository.findByUsername(companyEntity.getUsername()).ifPresent((user) -> {
                    throw new UserFoundException("Username " + user.getUsername() + " já cadastrado");
                });

        companyRepository.findByEmail(companyEntity.getEmail()).ifPresent((user) -> {
                    throw new UserFoundException("Email " + user.getEmail() + " já cadastrado");
                });

        String password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return companyRepository.save(companyEntity);
    }
}

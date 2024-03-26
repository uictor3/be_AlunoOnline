package br.com.AlunoOnline.api.service;


import br.com.AlunoOnline.api.model.Aluno;
import br.com.AlunoOnline.api.model.Professor;
import br.com.AlunoOnline.api.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;


    public void create(Professor professor){
        professorRepository.save(professor);
    }

    public List<Professor> findAll(){
        return professorRepository.findAll();
    }

    public Optional<Professor> findById(Long id){
      return professorRepository.findById(id) ;
    }

    public void update(Long id, Professor professor){
        Optional<Professor> professorFromDb = findById(id);

        if (professorFromDb.isEmpty()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);

                }
        Professor professorUpdate = professorFromDb.get();
        professorUpdate.setName(professor.getName());
        professorUpdate.setEmail(professor.getEmail());
        professorRepository.save(professorUpdate);
    }

    public void deleteById(Long id){
        professorRepository.deleteById(id);
    }

}

package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {
    @Autowired
    DisciplinaRepository disciplinaRepository;

    public void create(Disciplina subject){
        disciplinaRepository.save(subject);
    }

    public List<Disciplina> findAll(){
        return disciplinaRepository.findAll();
    }

    public List<Disciplina> findByProfessorId(Long professorId){
        return disciplinaRepository.findByProfessorId(professorId);
    }

    public Optional<Disciplina> findById(Long id){
        return disciplinaRepository.findById(id);
    }

    public void update(Long id, Disciplina subject){
        Optional<Disciplina>disciplinaFromDb = findById(id);

        if (disciplinaFromDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Disciplina não encontrada no banco de dados.");
        }

        Disciplina subjectUpdated = disciplinaFromDb.get();

        subjectUpdated.setName(subject.getName());

        disciplinaRepository.save(subjectUpdated);
    }

    public void deleteById(Long id){
        disciplinaRepository.deleteById(id);
    }
}

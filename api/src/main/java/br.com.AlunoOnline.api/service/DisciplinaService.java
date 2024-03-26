package br.com.AlunoOnline.api.service;


import br.com.AlunoOnline.api.model.Disciplina;
import br.com.AlunoOnline.api.repository.DisciplinaRepository;
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


    public void create(Disciplina disciplina){
        disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> findAll(){
        return disciplinaRepository.findAll();
    }

    public Optional<Disciplina> findById(Long id){
        return disciplinaRepository.findById(id) ;
    }

    public void update(Long id, Disciplina disciplina){
        Optional<Disciplina> disciplinaFromDb = findById(id);

        if (disciplinaFromDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }
        Disciplina disciplinaUpdate = disciplinaFromDb.get();
        disciplinaUpdate.setName(disciplina.getName());
        disciplinaRepository.save(disciplinaUpdate);
    }

    public void deleteById(Long id){
        disciplinaRepository.deleteById(id);
    }

}

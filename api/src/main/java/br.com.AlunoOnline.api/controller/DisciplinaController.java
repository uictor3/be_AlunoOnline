package br.com.AlunoOnline.api.controller;


import br.com.AlunoOnline.api.model.Disciplina;
import br.com.AlunoOnline.api.service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    DisciplinaService disciplinaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Disciplina disciplina){
        disciplinaService.create(disciplina);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findAll(){
        return disciplinaService.findAll();
    }

    @GetMapping("/{Id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Disciplina> findById(@PathVariable Long Id){
        return disciplinaService.findById(Id);
    }

    @PutMapping("/{Id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Disciplina disciplina, Long Id){
        disciplinaService.update(Id, disciplina);
    }


    @DeleteMapping("/{Id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deleteById(@PathVariable Disciplina disciplina, Long Id){
        disciplinaService.deleteById(Id);
    }
}

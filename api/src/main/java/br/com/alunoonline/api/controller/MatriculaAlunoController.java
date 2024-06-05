package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.AtualizarNotasRequest;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponse;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.service.MatriculaAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matricula-aluno")
public class MatriculaAlunoController {
    @Autowired
    MatriculaAlunoService matriculaAlunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody MatriculaAluno studentRegistry){
        matriculaAlunoService.create(studentRegistry);
    }

    @PatchMapping("/update-grades/{matriculaAlunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGrades(@RequestBody AtualizarNotasRequest updateGradesRequest, @PathVariable Long matriculaAlunoId){
        matriculaAlunoService.updateGrades(matriculaAlunoId, updateGradesRequest);
    }

    @PatchMapping("/update-status-to-break/{matriculaAlunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatusToBreak(@PathVariable Long matriculaAlunoId){
        matriculaAlunoService.updateStatusToBreak(matriculaAlunoId);
    }

    @GetMapping("/academic-transcript/{alunoId}")
    @ResponseStatus(HttpStatus.OK)
    public HistoricoAlunoResponse getAcademicTranscript(@PathVariable Long alunoId){
        return matriculaAlunoService.getAcademicTranscript(alunoId);
    }
}

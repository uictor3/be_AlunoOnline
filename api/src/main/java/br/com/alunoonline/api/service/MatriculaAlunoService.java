package br.com.alunoonline.api.service;

import br.com.alunoonline.api.Enums.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.dtos.AtualizarNotasRequest;
import br.com.alunoonline.api.dtos.DisciplinasAlunoResponse;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponse;
import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaAlunoService {

    public static final double GRADE_AVG_TO_APPROVE = 7.0;

    @Autowired
    MatriculaAlunoRepository matriculaAlunoRepository;
    public void create(MatriculaAluno studentRegistry){
        studentRegistry.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        matriculaAlunoRepository.save(studentRegistry);
    }

    public void updateGrades(Long matriculaAlunoId, AtualizarNotasRequest updateGradesRequest){
        MatriculaAluno studentRegistry = matriculaAlunoRepository.findById(matriculaAlunoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula não encontrada.")
        );
        updateStudentGrades(studentRegistry, updateGradesRequest);
        updateStudentStatus(studentRegistry);

        matriculaAlunoRepository.save(studentRegistry);
    }

    public void updateStudentGrades(MatriculaAluno studentRegistry, AtualizarNotasRequest updateGradesRequest){
        if (updateGradesRequest.getGrade1() != null){
            studentRegistry.setGrade1(updateGradesRequest.getGrade1());
        }

        if (updateGradesRequest.getGrade2() != null){
            studentRegistry.setGrade2(updateGradesRequest.getGrade2());
        }
    }

    public void updateStudentStatus(MatriculaAluno studentRegistry){
        Double nota1 = studentRegistry.getGrade1();
        Double nota2 = studentRegistry.getGrade2();

        if (nota1 != null && nota2 != null){
            double average = (nota1 + nota2) / 2;
            studentRegistry.setStatus(average >= GRADE_AVG_TO_APPROVE ? MatriculaAlunoStatusEnum.APROVADO : MatriculaAlunoStatusEnum.REPROVADO);
        }
    }

    public void updateStatusToBreak(Long matriculaId){
        MatriculaAluno studentRegistry = matriculaAlunoRepository.findById(matriculaId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula não encontrada.")
        );

        if (!MatriculaAlunoStatusEnum.MATRICULADO.equals(studentRegistry.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possivel trancar uma matricula com o status MATRICULADO");
        }
        changeStatus(studentRegistry, MatriculaAlunoStatusEnum.TRANCADO);
    }

    public void changeStatus(MatriculaAluno studentRegistry, MatriculaAlunoStatusEnum matriculaAlunoStatusEnum){
        studentRegistry.setStatus(matriculaAlunoStatusEnum);
        matriculaAlunoRepository.save(studentRegistry);
    }

    public HistoricoAlunoResponse getAcademicTranscript(Long studentId){
        List<MatriculaAluno> matriculasDoAluno = matriculaAlunoRepository.findByStudentId(studentId);

        if (matriculasDoAluno.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matriculas");
        }

        HistoricoAlunoResponse historico = new HistoricoAlunoResponse();
        historico.setStudentName(matriculasDoAluno.get(0).getStudent().getName());
        historico.setStudentEmail(matriculasDoAluno.get(0).getStudent().getEmail());

        List<DisciplinasAlunoResponse> disciplinasList = new ArrayList<>();

        for (MatriculaAluno registry : matriculasDoAluno) {
            DisciplinasAlunoResponse disciplinasAlunoResponse = new DisciplinasAlunoResponse();
            disciplinasAlunoResponse.setSubjectName(registry.getSubject().getNome());
            disciplinasAlunoResponse.setProfessorName(registry.getSubject().getProfessor().getName());
            disciplinasAlunoResponse.setGrade1(registry.getGrade1());
            disciplinasAlunoResponse.setGrade2(registry.getGrade2());

            //FAZER CALCULO DA MEDIA EM OUTRO METODO
            if (registry.getGrade1() != null && registry.getGrade2() != null){
                disciplinasAlunoResponse.setAverage((registry.getGrade1() + registry.getGrade2()) / 2);
            } else {
                disciplinasAlunoResponse.setAverage(null);
            }

            disciplinasAlunoResponse.setStatus(registry.getStatus());
            disciplinasList.add(disciplinasAlunoResponse);
        }

        historico.setStudentSubjectsResponseList(disciplinasList);

        return historico;
    }
}

package br.com.alunoonline.api.service;

import br.com.alunoonline.api.Enums.FinanceiroStatusEnum;
import br.com.alunoonline.api.dtos.CriarAlunoRequest;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Curso;
import br.com.alunoonline.api.model.FinanceiroAluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import br.com.alunoonline.api.repository.CursoRepository;
import br.com.alunoonline.api.repository.FinanceiroAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    FinanceiroAlunoRepository financeiroAlunoRepository;

    @Autowired
    CursoRepository cursoRepository;

    public void create(CriarAlunoRequest createStudentRequest){
        Curso curso = cursoRepository.findById(createStudentRequest.getCourseId())
                .orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "This course was not found in the database")
                );

        Aluno alunoSaved = alunoRepository.save(
                new Aluno(
                        null,
                        createStudentRequest.getName(),
                        createStudentRequest.getEmail(),
                        curso
                )
        );

        createFinanceiroInformation(alunoSaved, createStudentRequest);
    }

    public List<Aluno> findAll(){
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id){
        return alunoRepository.findById(id);
    }

    public void update(Long id, Aluno student){
        Optional<Aluno>studentFromDb = findById(id);

        if (studentFromDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This student was not found in the database");
        }

        Aluno studentUpdated = studentFromDb.get();

        studentUpdated.setName(student.getName());
        studentUpdated.setEmail(student.getEmail());

        alunoRepository.save(studentUpdated);
    }

    public void deleteById(Long id){
        alunoRepository.deleteById(id);
    }

    public void createFinanceiroInformation(Aluno aluno, CriarAlunoRequest criarAlunoRequest){
        FinanceiroAluno financeiroAluno = new FinanceiroAluno(
                null,
                aluno,
                criarAlunoRequest.getDiscount(),
                criarAlunoRequest.getDueDate(),
                FinanceiroStatusEnum.EM_DIA
        );

        financeiroAlunoRepository.save(financeiroAluno);
    }
}

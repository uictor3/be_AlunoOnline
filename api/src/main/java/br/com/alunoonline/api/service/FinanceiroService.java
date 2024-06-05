package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Fatura;
import br.com.alunoonline.api.model.FinanceiroAluno;
import br.com.alunoonline.api.repository.FaturaRepository;
import br.com.alunoonline.api.repository.FinanceiroAlunoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class FinanceiroService {

    private static final Integer QUANTITY_OF_DAYS_BEFORE_GENERATE = 10;

    private static final Logger logger = LoggerFactory.getLogger(FinanceiroService.class);

    @Autowired
    FinanceiroAlunoRepository financeiroAlunoRepository;

    @Autowired
    FaturaRepository faturaRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void faturaGeneration() {
        logger.info("Iniciando a geração de faturas...");

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime generationDeadline = currentDate.plusDays(QUANTITY_OF_DAYS_BEFORE_GENERATE);

        // BUSCAR TODOS REGISTROS DO FINANCEIROALUNO
        List<FinanceiroAluno> financeiroAlunos = financeiroAlunoRepository.findAll();

        for (FinanceiroAluno financeiroAluno : financeiroAlunos) {
            Integer dueDay = financeiroAluno.getDueDate();

            if (dueDay != null) {
                // CALCULAR DATA DE VENCIMENTO ATUAL
                LocalDate dueDateCurrentMonth = calculateDueDate(dueDay, currentDate.getYear(), currentDate.getMonthValue());

                // Se a data de vencimento do mês atual já passou, calcular a data de vencimento do próximo mês
                if (dueDateCurrentMonth.isBefore(currentDate.toLocalDate())) {
                    dueDateCurrentMonth = calculateDueDate(dueDay, currentDate.getYear(), currentDate.getMonthValue() + 1);
                }

                // VERIFICAR SE FALTAM 10 OU MAIS DIAS PARA A FATURA
                if (dueDateCurrentMonth != null && (dueDateCurrentMonth.isBefore(generationDeadline.toLocalDate()) || dueDateCurrentMonth.isEqual(generationDeadline.toLocalDate()))) {
                    // VERIFICAR SE JA EXISTE FATURA E A DUODATE DELA
                    if (faturaRepository.existsByStudentFinancialAndDueDate(financeiroAluno, dueDateCurrentMonth.atTime(LocalTime.MIDNIGHT))) {
                        // logger.info("Fatura já existe para o aluno: {} com data de vencimento: {}", financeiroAluno.getId(), dueDateCurrentMonth);
                        continue;
                    }

                    logger.info("Gerando fatura para o aluno: {}", financeiroAluno.getId());

                    // CRIAR NOVA FATURA
                    Fatura fatura = new Fatura();
                    fatura.setStudentFinancial(financeiroAluno);
                    fatura.setDueDate(dueDateCurrentMonth.atTime(LocalTime.MIDNIGHT));
                    fatura.setCreatedAt(currentDate);

                    // SALVAR FATURA
                    faturaRepository.save(fatura);

                    logger.info("Fatura gerada para o aluno: {} com data de vencimento: {}", financeiroAluno.getId(), dueDateCurrentMonth);

                }
            }
        }

        logger.info("Geração de faturas concluída.");
    }

    private LocalDate calculateDueDate(Integer dueDay, int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);

        int dayOfMonth = Math.min(dueDay, yearMonth.lengthOfMonth());

        return LocalDate.of(year, month, dayOfMonth);
    }
}

package prontuario.drnubia.facade;

import prontuario.drnubia.dao.ExameDAO;
import prontuario.drnubia.dao.PacienteDAO;
import prontuario.drnubia.database.DatabaseConnectionMySQL;
import prontuario.drnubia.exception.DataExameInvalidaException;
import prontuario.drnubia.exception.ExameDuplicadoException;
import prontuario.drnubia.model.Exame;
import prontuario.drnubia.model.Paciente;

import java.time.LocalDateTime;
import java.util.List;

public class ExameFacade {

    private ExameDAO exameDAO;
    private PacienteDAO pacienteDAO;

    public ExameFacade() {
        var conn = new DatabaseConnectionMySQL();
        exameDAO = new ExameDAO(conn);
        pacienteDAO = new PacienteDAO(conn);
    }

    public void criarExame(Exame exame) {
        validarExame(exame);
        exameDAO.create(exame);
    }

    public void atualizarExame(Exame exame) {
        validarExame(exame);
        exameDAO.update(exame);
    }
    
    private void validarExame(Exame exame) {
        if (exame.getDataExame().isBefore(LocalDateTime.now())) {
            throw new DataExameInvalidaException("A data e hora do exame não podem estar no passado.");
        }

        boolean existe = exameDAO.buscarPorPaciente(exame.getPaciente()).stream()
                .anyMatch(e -> e.getDataExame().isEqual(exame.getDataExame())
                        && (exame.getId() == null || !e.getId().equals(exame.getId())));

        if (existe) {
            throw new ExameDuplicadoException("Já existe um exame para este paciente nesta data e hora.");
        }
    }
    
    public boolean existeExameNoMesmoHorario(Paciente paciente, LocalDateTime dataHora) {
        return exameDAO.existeExameNoMesmoHorario(paciente, dataHora);
    }


    public void excluirExame(Exame exame) {
        exameDAO.delete(exame);
    }

    public List<Exame> listarTodos() {
        return exameDAO.findAll();
    }

    public Exame buscarPorId(Long id) {
        return exameDAO.findById(id);
    }

    public List<Paciente> listarPacientes() {
        return pacienteDAO.findAll();
    }
}

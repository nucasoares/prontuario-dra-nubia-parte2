package prontuario.drnubia.facade;

import prontuario.drnubia.dao.ExameDAO;
import prontuario.drnubia.dao.PacienteDAO;
import prontuario.drnubia.database.DatabaseConnectionMySQL;
import prontuario.drnubia.model.Exame;
import prontuario.drnubia.model.Paciente;

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
        exameDAO.create(exame);
    }

    public void atualizarExame(Exame exame) {
        exameDAO.update(exame);
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

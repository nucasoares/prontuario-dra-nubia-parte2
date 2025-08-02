package prontuario.drnubia.facade;

import java.time.LocalDateTime;
import java.util.List;

import prontuario.drnubia.dao.PacienteDAO;
import prontuario.drnubia.database.DatabaseConnectionMySQL;
import prontuario.drnubia.exception.PacienteJaCadastradoException;
import prontuario.drnubia.model.Paciente;

public class PacienteFacade {
    private static PacienteDAO daoPaciente = new PacienteDAO(new DatabaseConnectionMySQL());
    private static PacienteFacade instance;

    private PacienteFacade() {}

    public static PacienteFacade getInstance() {
        if (instance == null) {
            instance = new PacienteFacade();
        }
        return instance;
    }

    public void criarPaciente(String nome, String cpf, LocalDateTime dataNascimento) {
        if (daoPaciente.buscarPorCpf(cpf) != null) {
            throw new PacienteJaCadastradoException("Paciente com CPF já cadastrado.");
        }
        daoPaciente.create(new Paciente(0L, nome, cpf, dataNascimento));
    }

    public List<Paciente> listarPacientes() {
        return daoPaciente.findAll();
    }

    public List<Paciente> buscarTodos() {
        return daoPaciente.findAll();
    }

    public Paciente buscarPorCpf(String cpf) {
        return daoPaciente.buscarPorCpf(cpf);
    }

    public Paciente buscarPorId(Long id) {
        return daoPaciente.findById(id);
    }

    public void atualizarPaciente(Paciente p) {
        Paciente existente = daoPaciente.buscarPorCpf(p.getCpf());
        if (existente != null && !existente.getId().equals(p.getId())) {
            throw new PacienteJaCadastradoException("Outro paciente já possui esse CPF.");
        }
        daoPaciente.update(p);
    }

    public void deletarPaciente(Paciente p) {
        daoPaciente.delete(p);
    }
}

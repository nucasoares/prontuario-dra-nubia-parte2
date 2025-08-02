package prontuario.drnubia.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import prontuario.drnubia.database.IConnection;
import prontuario.drnubia.model.Exame;
import prontuario.drnubia.model.Paciente;

public class ExameDAO implements IEntityDAO<Exame> {
    private IConnection conn;
    private PacienteDAO pacienteDAO;

    public ExameDAO(IConnection connection) {
        this.conn = connection;
        this.pacienteDAO = new PacienteDAO(connection);
    }

    @Override
    public void create(Exame exame) {
        if (exame.getDescricao() == null || exame.getDataExame() == null
                || exame.getPaciente() == null || exame.getPaciente().getId() == null) {
            throw new IllegalArgumentException("Descrição, Data do exame e Paciente são obrigatórios.");
        }

        String sql = "INSERT INTO EXAMES (descricao, data_exame, paciente_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, exame.getDescricao());
            pstm.setTimestamp(2, Timestamp.valueOf(exame.getDataExame()));
            pstm.setLong(3, exame.getPaciente().getId());

            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    exame.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Exame findById(Long id) {
        String sql = "SELECT * FROM EXAMES WHERE id = ?";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Exame exame = new Exame();
                    exame.setId(rs.getLong("id"));
                    exame.setDescricao(rs.getString("descricao"));
                    exame.setDataExame(rs.getTimestamp("data_exame").toLocalDateTime());

                    Long pacienteId = rs.getLong("paciente_id");
                    Paciente paciente = pacienteDAO.findById(pacienteId);
                    exame.setPaciente(paciente);

                    return exame;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Exame exame) {
        String sql = "UPDATE EXAMES SET descricao = ?, data_exame = ?, paciente_id = ? WHERE id = ?";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, exame.getDescricao());
            pstm.setTimestamp(2, Timestamp.valueOf(exame.getDataExame()));
            pstm.setLong(3, exame.getPaciente().getId());
            pstm.setLong(4, exame.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Exame exame) {
        String sql = "DELETE FROM EXAMES WHERE id = ?";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, exame.getId());
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Exame> findAll() {
        List<Exame> lista = new ArrayList<>();
        String sql = "SELECT * FROM EXAMES";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId(rs.getLong("id"));
                exame.setDescricao(rs.getString("descricao"));
                exame.setDataExame(rs.getTimestamp("data_exame").toLocalDateTime());

                Long pacienteId = rs.getLong("paciente_id");
                Paciente paciente = pacienteDAO.findById(pacienteId);
                exame.setPaciente(paciente);

                lista.add(exame);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Exame> buscarPorPaciente(Paciente paciente) {
        List<Exame> lista = new ArrayList<>();
        String sql = "SELECT * FROM EXAMES WHERE paciente_id = ?";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, paciente.getId());
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Exame exame = new Exame();
                    exame.setId(rs.getLong("id"));
                    exame.setDescricao(rs.getString("descricao"));
                    exame.setDataExame(rs.getTimestamp("data_exame").toLocalDateTime());
                    exame.setPaciente(paciente);
                    lista.add(exame);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean existeExameNoMesmoHorario(Paciente paciente, LocalDateTime dataHora) {
        String sql = "SELECT COUNT(*) FROM EXAMES WHERE paciente_id = ? AND data_exame = ?";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, paciente.getId());
            pstm.setTimestamp(2, Timestamp.valueOf(dataHora));

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

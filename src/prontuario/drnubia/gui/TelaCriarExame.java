package prontuario.drnubia.gui;

import prontuario.drnubia.exception.ExameInvalidoException;
import prontuario.drnubia.facade.ExameFacade;
import prontuario.drnubia.facade.PacienteFacade;
import prontuario.drnubia.model.Exame;
import prontuario.drnubia.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TelaCriarExame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtDescricao;
    private JTextField txtData;         
    private JSpinner horaSpinner;
    private JComboBox<Paciente> comboPacientes;
    private ExameFacade exameFacade;
    private PacienteFacade pacienteFacade;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCriarExame() {
        setTitle("Criar Exame");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        exameFacade = new ExameFacade();
        pacienteFacade = PacienteFacade.getInstance();  

        JLabel lblDescricao = new JLabel("Descrição:");
        txtDescricao = new JTextField(20);

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        txtData = new JTextField(10);

        JLabel lblHora = new JLabel("Hora:");
        SpinnerDateModel horaModel = new SpinnerDateModel();
        horaSpinner = new JSpinner(horaModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(horaSpinner, "HH:mm");
        horaSpinner.setEditor(timeEditor);
        horaSpinner.setValue(new Date()); 

        JLabel lblPaciente = new JLabel("Paciente:");
        comboPacientes = new JComboBox<>();
        carregarPacientes();

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblDescricao);
        panel.add(txtDescricao);
        panel.add(lblData);
        panel.add(txtData);
        panel.add(lblHora);
        panel.add(horaSpinner);
        panel.add(lblPaciente);
        panel.add(comboPacientes);
        panel.add(new JLabel());
        panel.add(btnSalvar);

        add(panel);
        setVisible(true);
    }

    private void carregarPacientes() {
        List<Paciente> pacientes = pacienteFacade.listarPacientes();
        for (Paciente p : pacientes) {
            comboPacientes.addItem(p);
        }
    }

    private void salvar() {
        try {
            String descricao = txtDescricao.getText().trim();
            String dataTexto = txtData.getText().trim();
            Date horaSelecionada = (Date) horaSpinner.getValue();
            Paciente paciente = (Paciente) comboPacientes.getSelectedItem();

            if (descricao.isEmpty() || dataTexto.isEmpty() || paciente == null) {
                throw new ExameInvalidoException("Todos os campos devem ser preenchidos.");
            }

            LocalDate data;
            try {
                data = LocalDate.parse(dataTexto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                throw new ExameInvalidoException("Formato da data inválido. Use dd/MM/yyyy.");
            }

          
            LocalTime hora = horaSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            if (dataHora.isBefore(LocalDateTime.now())) {
                throw new ExameInvalidoException("A data e hora do exame não podem ser no passado.");
            }

            if (exameFacade.existeExameNoMesmoHorario(paciente, dataHora)) {
                throw new ExameInvalidoException("O paciente já possui um exame agendado para essa data e hora.");
            }

            Exame exame = new Exame(null, descricao, dataHora, paciente);
            exameFacade.criarExame(exame);

            JOptionPane.showMessageDialog(this, "Exame criado com sucesso!");
            dispose();

        } catch (ExameInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

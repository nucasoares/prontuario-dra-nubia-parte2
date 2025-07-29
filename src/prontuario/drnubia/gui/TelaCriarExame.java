package prontuario.drnubia.gui;

import prontuario.drnubia.facade.ExameFacade;
import prontuario.drnubia.model.Paciente;
import prontuario.drnubia.model.Exame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaCriarExame extends JFrame {

    private JTextArea campoDescricao;
    private JTextField campoData;
    private JComboBox<Paciente> comboPacientes;
    private ExameFacade exameFacade;

    public TelaCriarExame() {
        super("Criar Exame");
        exameFacade = new ExameFacade();
        inicializarComponentes();
        setVisible(true);  // Exibir janela ao criar
    }

    private void inicializarComponentes() {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e Combo paciente
        JLabel lblPaciente = new JLabel("Paciente:");
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblPaciente, gbc);

        comboPacientes = new JComboBox<>();
        for (Paciente p : exameFacade.listarPacientes()) {
            comboPacientes.addItem(p); // Mostrar nome+CPF via toString()
        }
        gbc.gridx = 1; gbc.gridy = 0;
        painel.add(comboPacientes, gbc);

        // Label e campo descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(lblDescricao, gbc);

        campoDescricao = new JTextArea(5, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scrollDescricao, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e campo data
        JLabel lblData = new JLabel("Data (dd/MM/yyyy HH:mm):");
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(lblData, gbc);

        campoData = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        painel.add(campoData, gbc);

        // Botão salvar
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarExame);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);

        add(painel);
    }

    private void salvarExame(ActionEvent e) {
        try {
            Paciente pacienteSelecionado = (Paciente) comboPacientes.getSelectedItem();
            String descricao = campoDescricao.getText().trim();
            String dataTexto = campoData.getText().trim();

            if (pacienteSelecionado == null || descricao.isEmpty() || dataTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime data = LocalDateTime.parse(dataTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Exame exame = new Exame(null, descricao, data, pacienteSelecionado);
            exameFacade.criarExame(exame);

            JOptionPane.showMessageDialog(this, "Exame criado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

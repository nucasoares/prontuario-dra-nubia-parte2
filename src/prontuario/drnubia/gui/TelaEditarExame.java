package prontuario.drnubia.gui;

import prontuario.drnubia.facade.ExameFacade;
import prontuario.drnubia.model.Paciente;
import prontuario.drnubia.model.Exame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaEditarExame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Exame exame;
    private JTextArea txtDescricao;
    private JTextField txtData;
    private JComboBox<Paciente> comboPacientes;
    private ExameFacade exameFacade;

    public TelaEditarExame(Exame exame) {
        super("Editar Exame");
        this.exame = exame;
        this.exameFacade = new ExameFacade();
        inicializarComponentes();
        setVisible(true);  
    }

    private void inicializarComponentes() {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblPaciente = new JLabel("Paciente:");
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblPaciente, gbc);

        comboPacientes = new JComboBox<>();
        for (Paciente p : exameFacade.listarPacientes()) {
            comboPacientes.addItem(p);
        }

        if (exame.getPaciente() != null) {
            for (int i = 0; i < comboPacientes.getItemCount(); i++) {
                if (comboPacientes.getItemAt(i).getId().equals(exame.getPaciente().getId())) {
                    comboPacientes.setSelectedIndex(i);
                    break;
                }
            }
        }

        gbc.gridx = 1; gbc.gridy = 0;
        painel.add(comboPacientes, gbc);

        JLabel lblDescricao = new JLabel("Descrição:");
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(lblDescricao, gbc);

        txtDescricao = new JTextArea(5, 20);
        txtDescricao.setText(exame.getDescricao());
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(scrollDescricao, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e campo data
        JLabel lblData = new JLabel("Data (dd/MM/yyyy HH:mm):");
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(lblData, gbc);

        txtData = new JTextField(exame.getDataExame().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        gbc.gridx = 1; gbc.gridy = 2;
        painel.add(txtData, gbc);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(this::salvarAlteracoes);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        painel.add(btnSalvar, gbc);

        add(painel);
    }

    private void salvarAlteracoes(ActionEvent e) {
        try {
            Paciente pacienteSelecionado = (Paciente) comboPacientes.getSelectedItem();
            String descricao = txtDescricao.getText().trim();
            String dataTexto = txtData.getText().trim();

            if (pacienteSelecionado == null || descricao.isEmpty() || dataTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            exame.setPaciente(pacienteSelecionado);
            exame.setDescricao(descricao);
            exame.setDataExame(LocalDateTime.parse(dataTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            exameFacade.atualizarExame(exame);
            JOptionPane.showMessageDialog(this, "Exame atualizado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar exame: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

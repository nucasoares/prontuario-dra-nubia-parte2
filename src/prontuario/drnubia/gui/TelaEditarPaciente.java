package prontuario.drnubia.gui;

import prontuario.drnubia.facade.PacienteFacade;
import prontuario.drnubia.model.Paciente;

import javax.swing.*;
import java.awt.*;

public class TelaEditarPaciente extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private Paciente paciente;
    private PacienteFacade facade;
    private TelaListarPacientes telaListar;

    public TelaEditarPaciente(Frame owner, Paciente paciente) {
        super(owner, "Editar Paciente", true);
        this.paciente = paciente;
        this.facade = PacienteFacade.getInstance();

        if (owner instanceof TelaListarPacientes) {
            this.telaListar = (TelaListarPacientes) owner;
        }

        setSize(300, 150);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(3, 2, 5, 5));
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        add(new JLabel("Nome:"));
        txtNome = new JTextField(paciente.getNome());
        add(txtNome);

        add(new JLabel("CPF:"));
        txtCpf = new JTextField(paciente.getCpf());
        add(txtCpf);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        add(btnSalvar);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarAlteracoes() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF não podem ficar vazios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        paciente.setNome(nome);
        paciente.setCpf(cpf);

        try {
            facade.atualizarPaciente(paciente);
            JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");
            if (telaListar != null) {
                telaListar.carregarPacientes();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar paciente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

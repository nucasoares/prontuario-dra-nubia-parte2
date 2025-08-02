package prontuario.drnubia.gui;

import prontuario.drnubia.facade.PacienteFacade;
import prontuario.drnubia.model.Paciente;
import prontuario.drnubia.util.Validacoes;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaEditarPaciente extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtDataNascimento;
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
        setLayout(new GridLayout(4, 2, 5, 5));
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
        
        add(new JLabel("Data Nascimento (dd/MM/yyyy):"));
        String dataFormatada = paciente.getDataNascimento() != null
                ? Validacoes.formatarData(paciente.getDataNascimento().toLocalDate().toString())
                : "";
        txtDataNascimento = new JTextField(dataFormatada);

            add(txtDataNascimento);
        

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        add(btnSalvar);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarAlteracoes() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().replaceAll("\\D", ""); 
        String dataStr = txtDataNascimento.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!Validacoes.validarCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validacoes.validarData(dataStr)) {
            JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!paciente.getCpf().equals(cpf)) {
            Paciente existente = facade.buscarPorCpf(cpf);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, "Este CPF já está cadastrado para outro paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            paciente.setNome(nome);
            paciente.setCpf(cpf);
            paciente.setDataNascimento(data.atStartOfDay());

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
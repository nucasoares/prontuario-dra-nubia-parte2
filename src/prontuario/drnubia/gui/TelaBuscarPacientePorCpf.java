package prontuario.drnubia.gui;

import prontuario.drnubia.dao.PacienteDAO;
import prontuario.drnubia.database.DatabaseConnectionMySQL;
import prontuario.drnubia.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaBuscarPacientePorCpf extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtCpf;
    private JButton btnPesquisar, btnSair;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private PacienteDAO pacienteDAO;

    public TelaBuscarPacientePorCpf(Frame parent) {
        super(parent, "Buscar Paciente por CPF", true);

        this.pacienteDAO = new PacienteDAO(new DatabaseConnectionMySQL());

        setLayout(new BorderLayout());

        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.add(new JLabel("CPF:"));
        txtCpf = new JTextField(15);
        painelBusca.add(txtCpf);

        btnPesquisar = new JButton("Pesquisar");
        painelBusca.add(btnPesquisar);

        btnSair = new JButton("Sair");
        painelBusca.add(btnSair);

        add(painelBusca, BorderLayout.NORTH);


        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF"}, 0);
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

      
        btnPesquisar.addActionListener(e -> buscarPaciente());
        btnSair.addActionListener(e -> dispose());

        setSize(500, 300);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void buscarPaciente() {
        String cpf = txtCpf.getText().trim();

        if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "CPF deve conter 11 dígitos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Paciente paciente = pacienteDAO.buscarPorCpf(cpf);
        modeloTabela.setRowCount(0);
        if (paciente != null) {
            modeloTabela.addRow(new Object[]{
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum paciente encontrado com este CPF.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

package prontuario.drnubia.gui;

import prontuario.drnubia.facade.PacienteFacade;
import prontuario.drnubia.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaListarPacientes extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField campoCpfBusca;
    private JButton btnBuscar, btnAtualizar, btnEditar, btnDeletar;

    private PacienteFacade facade;

    public TelaListarPacientes() {
        super("Lista de Pacientes");
        facade = PacienteFacade.getInstance();

        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        criarComponentes();
        carregarPacientes();

        setVisible(true);
    }

    private void criarComponentes() {
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        painelTopo.add(new JLabel("Buscar por CPF:"));
        campoCpfBusca = new JTextField(15);
        painelTopo.add(campoCpfBusca);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPorCpf());
        painelTopo.add(btnBuscar);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarPacientes());
        painelTopo.add(btnAtualizar);

        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "CPF"}, 0);
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel();
        btnEditar = new JButton("Editar");
        btnDeletar = new JButton("Deletar");
        painelInferior.add(btnEditar);
        painelInferior.add(btnDeletar);
        add(painelInferior, BorderLayout.SOUTH);

        btnEditar.addActionListener(e -> editarPaciente());
        btnDeletar.addActionListener(e -> deletarPaciente());
    }

    public void carregarPacientes() {
        modeloTabela.setRowCount(0);
        List<Paciente> pacientes = facade.buscarTodos();
        for (Paciente p : pacientes) {
            modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getCpf()
            });
        }
    }

    private void buscarPorCpf() {
        String cpf = campoCpfBusca.getText().trim();
        modeloTabela.setRowCount(0);

        if (cpf.isEmpty()) {
            carregarPacientes();
            return;
        }

        Paciente paciente = facade.buscarPorCpf(cpf);
        if (paciente != null) {
            modeloTabela.addRow(new Object[]{
                    paciente.getId(), paciente.getNome(), paciente.getCpf()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado para o CPF informado.");
        }
    }

    private void editarPaciente() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para editar.");
            return;
        }
        Number num = (Number) modeloTabela.getValueAt(linha, 0);
        Long id = num.longValue();

        Paciente paciente = facade.buscarPorId(id);
        if (paciente != null) {
            new TelaEditarPaciente(this, paciente).setVisible(true);
            carregarPacientes();
        } else {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
        }
    }

    private void deletarPaciente() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para deletar.");
            return;
        }
        Number num = (Number) modeloTabela.getValueAt(linha, 0);
        Long id = num.longValue();

        Paciente paciente = facade.buscarPorId(id);
        if (paciente != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                facade.deletarPaciente(paciente);
                carregarPacientes();
                JOptionPane.showMessageDialog(this, "Paciente deletado com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
        }
    }
}

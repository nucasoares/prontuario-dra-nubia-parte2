package prontuario.drnubia.gui;

import prontuario.drnubia.facade.ExameFacade;
import prontuario.drnubia.model.Exame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaListarExames extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ExameFacade exameFacade;

    public TelaListarExames() {
        super("Lista de Exames");
        exameFacade = new ExameFacade();

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarExames();

        setVisible(true);
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Descrição");
        modeloTabela.addColumn("Data do Exame");
        modeloTabela.addColumn("Paciente");

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton btnEditar = new JButton("Editar Exame");
        btnEditar.addActionListener(e -> editarExameSelecionado());

        JButton btnDeletar = new JButton("Deletar Exame");
        btnDeletar.addActionListener(e -> deletarExameSelecionado());

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> carregarExames());

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(btnEditar);
        botoesPanel.add(btnDeletar);
        botoesPanel.add(btnAtualizar);

        add(scrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);
    }

    private void carregarExames() {
        modeloTabela.setRowCount(0);
        List<Exame> exames = exameFacade.listarTodos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Exame exame : exames) {
            String pacienteStr = (exame.getPaciente() != null)
                    ? exame.getPaciente().getNome() + " - " + exame.getPaciente().getCpf()
                    : "Paciente não informado";

            modeloTabela.addRow(new Object[]{
                    exame.getDescricao(),
                    exame.getDataExame().format(formatter),
                    pacienteStr
            });
        }
    }

    private Exame buscarExameSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um exame primeiro.");
            return null;
        }

        String descricao = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        String dataStr = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String pacienteStr = (String) modeloTabela.getValueAt(linhaSelecionada, 2);

        List<Exame> exames = exameFacade.listarTodos();

        return exames.stream()
                .filter(e -> e.getDescricao().equals(descricao)
                        && e.getDataExame().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).equals(dataStr)
                        && (e.getPaciente() != null ?
                        (e.getPaciente().getNome() + " - " + e.getPaciente().getCpf()).equals(pacienteStr) :
                        pacienteStr.equals("Paciente não informado")))
                .findFirst()
                .orElse(null);
    }

    private void editarExameSelecionado() {
        Exame exameSelecionado = buscarExameSelecionado();
        if (exameSelecionado != null) {
            new TelaEditarExame(exameSelecionado);
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível localizar o exame selecionado.");
        }
    }

    private void deletarExameSelecionado() {
        Exame exameSelecionado = buscarExameSelecionado();
        if (exameSelecionado == null) return;

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja deletar o exame selecionado?",
                "Confirmação de exclusão",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                exameFacade.excluirExame(exameSelecionado);
                JOptionPane.showMessageDialog(this, "Exame deletado com sucesso!");
                carregarExames();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar exame: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

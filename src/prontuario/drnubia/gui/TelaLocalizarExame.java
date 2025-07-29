package prontuario.drnubia.gui;

import prontuario.drnubia.facade.ExameFacade;
import prontuario.drnubia.model.Exame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TelaLocalizarExame extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtBusca;
    private JButton btnPesquisar, btnSair;
    private JTable tabelaResultados;
    private DefaultTableModel modeloTabela;

    private ExameFacade facade;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TelaLocalizarExame(Frame parent) {
        super(parent, "Localizar Exame", true);
        facade = new ExameFacade();

        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5,5));

        criarComponentes();

        setVisible(true);
    }

    private void criarComponentes() {
        JPanel painelBusca = new JPanel();
        painelBusca.add(new JLabel("Buscar por nome do paciente:"));
        txtBusca = new JTextField(20);
        painelBusca.add(txtBusca);

        btnPesquisar = new JButton("Pesquisar");
        painelBusca.add(btnPesquisar);

        btnSair = new JButton("Sair");
        painelBusca.add(btnSair);

        add(painelBusca, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID Exame", "Nome Paciente", "Data", "Descrição"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // somente leitura
            }
        };

        tabelaResultados = new JTable(modeloTabela);
        add(new JScrollPane(tabelaResultados), BorderLayout.CENTER);

        btnPesquisar.addActionListener(e -> pesquisarExames());
        btnSair.addActionListener(e -> dispose());
    }

    private void pesquisarExames() {
        String termo = txtBusca.getText().trim().toLowerCase();

        List<Exame> exames = facade.listarTodos(); // Atenção: este método precisa existir e retornar todos exames

        List<Exame> resultadosFiltrados = exames.stream()
            .filter(e -> e.getPaciente() != null &&
                         e.getPaciente().getNome() != null &&
                         e.getPaciente().getNome().toLowerCase().contains(termo))
            .collect(Collectors.toList());

        atualizarTabela(resultadosFiltrados);
    }

    private void atualizarTabela(List<Exame> exames) {
        modeloTabela.setRowCount(0);

        for (Exame e : exames) {
            modeloTabela.addRow(new Object[]{
                e.getId(),
                e.getPaciente() != null ? e.getPaciente().getNome() : "",
                e.getDataExame() != null ? e.getDataExame().format(formatter) : "",
                e.getDescricao()
            });
        }
    }
}

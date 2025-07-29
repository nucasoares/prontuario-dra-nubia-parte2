package prontuario.drnubia.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar;
    private JMenu menuPaciente;
    private JMenuItem menuItemNovo;
    private JMenuItem menuItemListar; 
    private JMenuItem menuItemBuscar;

    public TelaPrincipal() {
        setTitle("Sistema da Dra. Nubia - Programa de Prontuário Médico");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        criarMenu();

        setVisible(true);
    }

    private void criarMenu() {
        menuBar = new JMenuBar();

        // Menu Paciente
        menuPaciente = new JMenu("Paciente");

        menuItemNovo = new JMenuItem("Novo");
        menuItemNovo.addActionListener(e -> new TelaCadastroPaciente());

        menuItemListar = new JMenuItem("Listar");
        menuItemListar.addActionListener(e -> new TelaListarPacientes());

        menuItemBuscar = new JMenuItem("Buscar por CPF");
        menuItemBuscar.addActionListener(e -> new TelaBuscarPacientePorCpf(this));

        menuPaciente.add(menuItemNovo);
        menuPaciente.add(menuItemListar);
        menuPaciente.add(menuItemBuscar);

        // Menu Exame
        JMenu menuExame = new JMenu("Exame");

        JMenuItem menuItemExameNovo = new JMenuItem("Novo");
        menuItemExameNovo.addActionListener(e -> new TelaCriarExame());

        JMenuItem menuItemExameListar = new JMenuItem("Listar");
        // Ajuste aqui: TelaListarExames() sem parâmetro, conforme a versão corrigida
        menuItemExameListar.addActionListener(e -> new TelaListarExames());

        menuExame.add(menuItemExameNovo);
        menuExame.add(menuItemExameListar);

        menuBar.add(menuPaciente);
        menuBar.add(menuExame);

        setJMenuBar(menuBar);
    }
}

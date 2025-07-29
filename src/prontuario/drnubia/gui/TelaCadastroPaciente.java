package prontuario.drnubia.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import prontuario.drnubia.exception.PacienteJaCadastradoException;
import prontuario.drnubia.facade.PacienteFacade;

public class TelaCadastroPaciente extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel painelForm;
	private JPanel painelBotoes;
	private JLabel lblNome;
	private JLabel lblCpf;
	private JTextField txtNome;
	private JTextField txtCpf;

	private JButton btnSalvar;
	private JButton btnLimpar;
	private JButton btnSair;

	public TelaCadastroPaciente() {
		setTitle("Cadastro de Paciente");
		setSize(320, 200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		setLocationRelativeTo(null); // centraliza a janela

		carregarForm();
		carregarBotoes();

		add(painelForm, BorderLayout.CENTER);
		add(painelBotoes, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void carregarForm() {
		painelForm = new JPanel(new FlowLayout());

		lblNome = new JLabel("Nome:");
		lblCpf = new JLabel("CPF:");
		txtNome = new JTextField(20);
		txtCpf = new JTextField(11);

		painelForm.add(lblNome);
		painelForm.add(txtNome);
		painelForm.add(lblCpf);
		painelForm.add(txtCpf);
	}

	private void carregarBotoes() {
		painelBotoes = new JPanel(new FlowLayout());

		btnSalvar = new JButton("Salvar");
		btnLimpar = new JButton("Limpar");
		btnSair = new JButton("Sair");

		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = txtNome.getText().trim();
					String cpf = txtCpf.getText().trim();

					PacienteFacade.getInstance().criarPaciente(nome, cpf);

					JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso!");
					dispose(); // fecha a janela
				} catch (PacienteJaCadastradoException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao cadastrar paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limparComponentes();
			}
		});

		btnSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // fecha a janela
			}
		});

		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnLimpar);
		painelBotoes.add(btnSair);
	}

	private void limparComponentes() {
		txtNome.setText("");
		txtCpf.setText("");
	}
}

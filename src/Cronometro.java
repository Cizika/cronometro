import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Cronometro extends JFrame implements ActionListener, Runnable {

    private JPanel fieldPanel;
    private JPanel buttonPanel;

    private JTextField minutosText;
    private JTextField segundosText;
    private JTextField milissegundosText;

    private int minutos;
    private int segundos;
    private int milissegundos;

    private JButton closeButton;
    private JButton resetButton;
    private JButton startButton;

    private boolean ligado = false;

    public DecimalFormat formatoTempo = new DecimalFormat("00");
    public DecimalFormat formatoMilissegundos = new DecimalFormat("000");

    public Cronometro() {

        // Iniciando JPanels
        fieldPanel = new JPanel();
        buttonPanel = new JPanel();

        // TextField de minutos
        minutosText = new JTextField();
        minutosText.setFont(new Font("Consolas", Font.PLAIN, 35));
        minutosText.setText("00");
        minutosText.setHorizontalAlignment(JTextField.CENTER);
        minutosText.setBackground(Color.BLACK);
        minutosText.setForeground(Color.WHITE);
        minutosText.setEditable(false);
        minutosText.setPreferredSize(new Dimension(100, 40));

        // TextField de segundos
        segundosText = new JTextField();
        segundosText.setFont(new Font("Consolas", Font.PLAIN, 35));
        segundosText.setText("00");
        segundosText.setHorizontalAlignment(JTextField.CENTER);
        segundosText.setBackground(Color.BLACK);
        segundosText.setForeground(Color.WHITE);
        segundosText.setEditable(false);
        segundosText.setPreferredSize(new Dimension(100, 40));

        // TextField de milissegundos
        milissegundosText = new JTextField();
        milissegundosText.setFont(new Font("Consolas", Font.PLAIN, 35));
        milissegundosText.setText("000");
        milissegundosText.setHorizontalAlignment(JTextField.CENTER);
        milissegundosText.setBackground(Color.BLACK);
        milissegundosText.setForeground(Color.WHITE);
        milissegundosText.setEditable(false);
        milissegundosText.setPreferredSize(new Dimension(100, 40));

        //Adicionando TextFields ao textPanel
        fieldPanel.add(minutosText);
        fieldPanel.add(segundosText);
        fieldPanel.add(milissegundosText);

        // Botão de Iniciar
        startButton = new JButton("Iniciar");
        startButton.setFocusable(false);
        startButton.addActionListener(this);
        startButton.setHorizontalTextPosition(JButton.LEFT);

        // Botão de Reset
        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        resetButton.setHorizontalTextPosition(JButton.LEFT);

        // Botão de Fechar
        closeButton = new JButton("Fechar");
        closeButton.setFocusable(false);
        closeButton.addActionListener(this);
        closeButton.setHorizontalTextPosition(JButton.LEFT);

        // Adicionando Botões ao buttonPanel
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(closeButton);

        // Adicionando Panels ao Frame
        this.add(fieldPanel);
        this.add(buttonPanel);

        // Configurando Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 3, 10, 10));
        this.setTitle("Cronômetro");
        this.setResizable(false);
        this.setIconImage(new ImageIcon("clock.png").getImage());
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    // Método para atualizar tempo no GUI
    public void atualizarVisualmente(){
        minutosText.setText(formatoTempo.format(minutos));
        segundosText.setText(formatoTempo.format(segundos));
        milissegundosText.setText(formatoMilissegundos.format(milissegundos));
    }

    @Override
    public void run() {

        // Enquanto o cronômetro estiver ligado, a thread continua rodando
        while(ligado){
            try {
                // Passou-se um milissegundo
                TimeUnit.MILLISECONDS.sleep(1);
                milissegundos++;

                // Caso passe 1000 milissegundos, soma 1 segundo e zera milissegundos
                if(milissegundos == 1000){
                    milissegundos = 0;
                    segundos++;
                }

                // Caso passe 60 segundos, soma 1 minuto e zera segundos
                if(segundos == 60){
                    segundos = 0;
                    minutos++;
                }

                // Atualizando valores na tela
                atualizarVisualmente();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Inicia/Pausa a contagem
        if(e.getSource() == startButton){

            // Muda o comportamento do botão
            if(ligado)
                startButton.setText("Iniciar");
            else
                startButton.setText("Pausar");

            // Toggle do Ligado (Iniciar/Parar)
            ligado = !ligado;

            // Iniciando Thread de Contagem
            Thread t = new Thread(this);
            t.start();

        }
        // Reseta a contagem
        else if(e.getSource() == resetButton){

            // Desligando o cronômetro
            ligado = false;

            // Aguardando um milissegundo para a thread ser morta
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // Mudando botão novamente para iniciar
            startButton.setText("Iniciar");

            // Reniciando contagem
            minutos = 0;
            segundos = 0;
            milissegundos = 0;

            // Atualizando visualmente
            atualizarVisualmente();
        }
        // Fecha o cronômetro
        else if(e.getSource() == closeButton)
            this.dispose();
    }
}

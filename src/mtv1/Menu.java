/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtv1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;

/**
 *
 * @author Neto
 */
public class Menu extends javax.swing.JFrame implements WindowListener,ActionListener {
    // Variáveis para desenhso 2D no JFrame
    final static Color bg = Color.white;
    final static Color fg = Color.black;
    final static BasicStroke stroke = new BasicStroke(2.0f);
    final static BasicStroke wideStroke = new BasicStroke(8.0f);
    final static float dash1[] = {5.0f};
    final static BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);
    
    // Variáveis dos objetos de tela para referência no código
    JButton btnPasso, btnReset, btnParar, btnRodar, btnCompilar, btnLer, btnExMono1, btnExMono2, btnClear;
    JLabel txtResultado, txtPassos, txtEstado;
    JTextField txfEntrada;
    JTextPane jTextPane1;
    JScrollPane jScrollPane1;
    Timer timer;

    // Variáveis de controle da Máquina de Turing Universal
    final String[] resultado = {"", "ACEITO", "REJEITADO"};
    final Color[] cores = {Color.BLACK, Color.BLUE, Color.RED};
    MT mtu = new MT();
    DoublyLinkedList<TipoIntString> fita;
    boolean parado = false;
    private final long PERIOD = 1000L; // Adjust to suit timing
    public final static int ONE_SECOND = 1000;
    private long lastTime = System.currentTimeMillis() - PERIOD;
    
    
    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador de Máquinas de Turing");
        setForeground(new java.awt.Color(204, 204, 204));
        setName("FrameMT"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    /**
     * Método que desenha grade de retângulos com textos para animação da
     * Máquina de Turing.
     * @param g O próprio objeto gráfico do JFrame
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g); // Dica para acabar com erro de renderização da tela em runtime
        Graphics2D g2 = (Graphics2D) g;
        int x = 12;
        int width = 27;
        int qtde = 14;
        
        // Configurações de texto para desenhar a entrada da fita
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Calibri", Font.BOLD, 20);
        g2.setFont(font);
        
        // Desenha retângulos da fita de entrada da Máquina de Turing
        for (int i = 0; i <= qtde; i++) {
            GradientPaint graygradient = new GradientPaint(x,0,Color.LIGHT_GRAY,x+width, 0,Color.WHITE);
            if (i == qtde/2){
                g2.setPaint(Color.LIGHT_GRAY);
                g2.setStroke(dashed);
            }else{
                g2.setPaint(graygradient);
                g2.setStroke(stroke);
            }
            g2.fill(new RoundRectangle2D.Double(x, 80, width, 30, 6, 6));
            g2.setPaint(Color.DARK_GRAY);
            g2.draw(new RoundRectangle2D.Double(x, 80, width, 30, 6, 6));
            x += width;
        }
        
        //Desenha os textos dentro da fita desenhada a partir da variável fita
        fita = mtu.getFita();
        ListIterator<TipoIntString> iterator;
        if (fita != null) {
            iterator = fita.iterator();
            while (iterator.hasNext()){
                TipoIntString elemento = iterator.next();
                if (elemento.getX() >= 21 && elemento.getX() <= 399 && elemento.getDado() != '_')
                    g2.drawString( Character.toString(elemento.getDado()) , elemento.getX(), 102);
            }
        }
                
        // Desenha o cabeçote de leitura da Máquina de Turing
        int x1Points[] = {208, 215, 223};
        int y1Points[] = {72, 77, 72};
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,x1Points.length);
        polygon.moveTo(x1Points[0], y1Points[0]);
        
        for (int index = 1; index < x1Points.length; index++)
            polygon.lineTo(x1Points[index], y1Points[index]);

        polygon.closePath();
        g2.draw(polygon);

    }
    
    
    
    public void compilarButtonPressed() {
        String programa = jTextPane1.getText();
        String mensagem = mtu.compilar(programa);
        
        if (mensagem != null && !mensagem.startsWith(":"))
            JOptionPane.showMessageDialog(this, mensagem);
        else if (mensagem != null) {
            txtResultado.setText("");
            txtPassos.setText("0");
            txtEstado.setText(mensagem.substring(1));
        }
    }
    
    
    public void lerButtonPressed() {
        String entrada = txfEntrada.getText();
        
        if (entrada.equals(""))
            JOptionPane.showMessageDialog(this, "Insira uma entrada no campo para leitura.");
        else {
            entrada += "_";     // Adiciona primeiro campo vazio da fita
            mtu.setFita(entrada);
            btnPasso.setEnabled(true);
            btnRodar.setEnabled(true);
        }
        
        repaint();  // Atualiza a pintura da tela
    }
    
    
    public void rodarButtonPressed() {
        // Inicia temporizador para passos automáticos
        parado = false;
        timer.start();
    }
    
    
    public void pararButtonPressed() {
        if (mtu.parar() != 0)
            JOptionPane.showMessageDialog(this, "Necessário compilar um programa.");
        else {
            parado = true;
            timer.stop();
        }
    }
    
    
    public void resetButtonPressed() {
        if (mtu.reset() != 0)
            JOptionPane.showMessageDialog(this, "Necessário compilar um programa.");
        else {
            txtResultado.setText("");
            txtPassos.setText("0");
            txtEstado.setText("");
            parado = false;
            timer.stop();
        }
        repaint();
    }
    
    
    public void passoButtonPressed() {
        TipoResultado retorno = mtu.passo();
        if (retorno == null)
            JOptionPane.showMessageDialog(this, "Necessário compilar um programa.");
        else {
            txtEstado.setText(retorno.getEstado());
            txtPassos.setText( String.valueOf(retorno.getPassos()) );
            if (retorno.getResultado() <= 2) {
                txtResultado.setText(resultado[retorno.getResultado()]);
                txtResultado.setForeground(cores[retorno.getResultado()]);
            }
        }
        repaint();  // Atualiza a pintura da tela
    }
    
    
    public void timerOn() {
        // Executa a mesma lógica do método de passos
        TipoResultado retorno = mtu.rodar();
        if (retorno == null)
            JOptionPane.showMessageDialog(this, "Necessário compilar um programa.");
        else {
            if (!parado) {
                txtEstado.setText(retorno.getEstado());
                txtPassos.setText( String.valueOf(retorno.getPassos()) );
                if (retorno.getResultado() <= 2) {
                    txtResultado.setText(resultado[retorno.getResultado()]);
                    txtResultado.setForeground(cores[retorno.getResultado()]);
                }
                repaint();
                
                // Aqui verifica se houve aceitação ou rejeição para parar o automático
                if (retorno.getResultado() != 0) {
                    parado = true;
                    timer.stop();
                }
            }
        }
    }
    
    
    public void parDeZerosButtonPressed() {
        jTextPane1.setText( "init: q0\n"+
                            "accept: qAccept\n\n"+
                            "q0,0\n"+
                            "q1,0,>\n\n"+
                            "q1,0\n"+
                            "q0,0,>\n\n"+
                            "q0,1\n"+
                            "q0,1,>\n\n"+
                            "q1,1\n"+
                            "q1,1,>\n\n"+
                            "q0,_\n"+
                            "qAccept,_,-");
    }
    
    public void parBinPalindromoButtonPressed() {
        jTextPane1.setText( "init: q0\n" +
                            "accept: qAccept\n" +
                            "\n" +
                            "q0,0\n" +
                            "qRight0,_,>\n" +
                            "\n" +
                            "qRight0,0\n" +
                            "qRight0,0,>\n" +
                            "\n" +
                            "qRight0,1\n" +
                            "qRight0,1,>\n" +
                            "\n" +
                            "q0,1\n" +
                            "qRight1,_,>\n" +
                            "\n" +
                            "qRight1,0\n" +
                            "qRight1,0,>\n" +
                            "\n" +
                            "qRight1,1\n" +
                            "qRight1,1,>\n" +
                            "\n" +
                            "qRight0,_\n" +
                            "qSearch0L,_,<\n" +
                            "\n" +
                            "qSearch0L,0\n" +
                            "q1,_,<\n" +
                            "\n" +
                            "qRight1,_\n" +
                            "qSearch1L,_,<\n" +
                            "\n" +
                            "qSearch1L,1\n" +
                            "q1,_,<\n" +
                            "\n" +
                            "q1,0\n" +
                            "qLeft0,_,<\n" +
                            "\n" +
                            "qLeft0,0\n" +
                            "qLeft0,0,<\n" +
                            "\n" +
                            "qLeft0,1\n" +
                            "qLeft0,1,<\n" +
                            "\n" +
                            "q1,1\n" +
                            "qLeft1,_,<\n" +
                            "\n" +
                            "qLeft1,0\n" +
                            "qLeft1,0,<\n" +
                            "\n" +
                            "qLeft1,1\n" +
                            "qLeft1,1,<\n" +
                            "\n" +
                            "qLeft0,_\n" +
                            "qSearch0R,_,>\n" +
                            "\n" +
                            "qSearch0R,0\n" +
                            "q0,_,>\n" +
                            "\n" +
                            "qLeft1,_\n" +
                            "qSearch1R,_,>\n" +
                            "\n" +
                            "qSearch1R,1\n" +
                            "q0,_,>\n" +
                            "\n" +
                            "qSearch0R,1\n" +
                            "qReject,1,-\n" +
                            "\n" +
                            "qSearch1R,0\n" +
                            "qReject,0,-\n" +
                            "\n" +
                            "qSearch0L,1\n" +
                            "qReject,1,-\n" +
                            "\n" +
                            "qSearch1L,0\n" +
                            "qReject,0,-\n" +
                            "\n" +
                            "q0,_\n" +
                            "qAccept,_,-\n" +
                            "\n" +
                            "q1,_\n" +
                            "qAccept,_,-\n" +
                            "\n" +
                            "qSearch0L,_\n" +
                            "qAccept,_,-\n" +
                            "\n" +
                            "qSearch0R,_\n" +
                            "qAccept,_,-\n" +
                            "\n" +
                            "qSearch1L,_\n" +
                            "qAccept,_,-\n" +
                            "\n" +
                            "qSearch1R,_\n" +
                            "qAccept,_,-");
    }
    
    

    /**
     * Método que cria objetos gráficos Swing na tela com suas propriedades
     */
    public void desenhaTela() {
        Container c = getContentPane();
        
        setTitle(" Simulador de Máquinas de Turing ");
        setSize( 430, 490);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(Menu.EXIT_ON_CLOSE);
        
        txfEntrada = new JTextField();
        txfEntrada.setToolTipText("Digitar entrada para a MT compilada");
        txfEntrada.setBounds(7, 116, 304, 25);
        txfEntrada.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.add(txfEntrada);
        
        txtEstado = new JLabel("qEstado");
        Font font = getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        txtEstado.setFont(boldFont);
        txtEstado.setBounds(10,10,140,25);
        c.add(txtEstado);
        
        txtResultado = new JLabel("");
        txtResultado.setFont(boldFont);
        txtResultado.setBounds(170,10,140,25);
        c.add(txtResultado);
        
        JLabel txtPasso = new JLabel("Passos: ");
        txtPasso.setFont(boldFont);
        txtPasso.setBounds(320,10,140,25);
        c.add(txtPasso);
        
        txtPassos = new JLabel("0");
        txtPassos.setFont(boldFont);
        txtPassos.setBounds(390,10,140,25);
        c.add(txtPassos);
        
        JSeparator separador1 = new JSeparator();
        separador1.setBounds(0, 104, 430, 5);
        c.add(separador1);
        
        jTextPane1 = new JTextPane();
        Font fontPane = new Font("Courier New", Font.PLAIN, 12);
        jScrollPane1 = new JScrollPane(jTextPane1);
        jScrollPane1.setBounds(110, 200, 200, 210);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jScrollPane1.setFont(fontPane);
        c.add(jScrollPane1);
        jTextPane1.setText( "// EXEMPLOS AO LADO ->\n" +
                            "// Clique nos botões\n" +
                            "// então clique em Compilar\n" +
                            "\n" +
                            "// SINTAXE:\n" +
                            "// (1) Configurações:\n" +
                            "init: [estado_inicial]\n" +
                            "accept: [estado_aceitação]\n" +
                            "\n" +
                            "// (2) Transições:\n" +
                            "[estado_atual],[símbolo_atual]\n" +
                            "[próx_est],[próx_símb],[> OU < OU -]\n" +
                            "\n" +
                            "// (3) Cabeçote:\n" +
                            "// < move p/ esquerda\n" +
                            "// > move p/ direita\n" +
                            "// - nao move\n" +
                            "\n" +
                            "// (Obs:)\n" +
                            "// _ célula vazia\n" +
                            "// Programa é case-sensitive\n");
        
        btnCompilar = new JButton("COMPILAR");
        btnCompilar.setToolTipText("Compilar programa digitado para a MT");
        btnCompilar.setBounds(160, 420, 100, 28);
        btnCompilar.addActionListener((ActionEvent e) -> {
            compilarButtonPressed();
        });
        c.add(btnCompilar);
        
        JSeparator separador2 = new JSeparator();
        separador2.setBounds(0, 185, 430, 5);
        c.add(separador2);
        
        btnLer = new JButton("LER");
        btnLer.setToolTipText("Carregar a entrada na fita da MT");        
        btnLer.setBounds(320, 114, 100, 28);
        btnLer.addActionListener((ActionEvent e) -> {
            lerButtonPressed();
        });
        c.add(btnLer);
        
        btnRodar = new JButton("RODAR");
        btnRodar.setToolTipText("Executar a MT");
        btnRodar.setBounds(5, 148, 100, 28);
        btnRodar.addActionListener((ActionEvent e) -> {
            rodarButtonPressed();
        });
        btnRodar.setEnabled(false);
        c.add(btnRodar);
        
        btnParar = new JButton("PARAR");
        btnParar.setToolTipText("Parar execução atual da MT");
        btnParar.setBounds(110, 148, 100, 28);
        btnParar.addActionListener((ActionEvent e) -> {
            pararButtonPressed();
        });
        c.add(btnParar);
        
        btnReset = new JButton("RESET");
        btnReset.setToolTipText("Reiniciar a MT");
        btnReset.setBounds(215, 148, 100, 28);
        btnReset.addActionListener((ActionEvent e) -> {
            resetButtonPressed();
        });
        c.add(btnReset);
        
        btnPasso = new JButton("PASSO");
        btnPasso.setToolTipText("Executar a MT passo-a-passo");
        btnPasso.setBounds(320, 148, 100, 28);
        btnPasso.addActionListener((ActionEvent e) -> {
            passoButtonPressed();
        });
        btnPasso.setEnabled(false);
        c.add(btnPasso);
        
        btnExMono1 = new JButton("<html>Número par<br/>de zeros</html>");
        btnExMono1.setToolTipText("Programa que verifica número par de zeros na fita");
        btnExMono1.setBounds(320, 198, 100, 40);
        btnExMono1.addActionListener((ActionEvent e) -> {
            parDeZerosButtonPressed();
        });
        c.add(btnExMono1);
        
        btnExMono2 = new JButton("<html>Palíndromo<br/>binário</html>");
        btnExMono2.setToolTipText("Programa que verifica número par de zeros na fita");
        btnExMono2.setBounds(320, 248, 100, 40);
        btnExMono2.addActionListener((ActionEvent e) -> {
            parBinPalindromoButtonPressed();
        });
        c.add(btnExMono2);
        
        btnClear = new JButton("<html>Limpar<br/>Campo</html>");
        btnClear.setToolTipText("Limpar campo de digitação de programas");
        btnClear.setBounds(3, 198, 100, 40);
        btnClear.addActionListener((ActionEvent e) -> {
            jTextPane1.setText("");
        });
        c.add(btnClear);
        
        timer = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timerOn();
            }    
        });
        
        setVisible(true);
    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        new Menu().desenhaTela();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

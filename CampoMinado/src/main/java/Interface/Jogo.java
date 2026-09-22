/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author IsraelSantos
 */
public final class Jogo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Jogo.class.getName());
    
    // É O LOCAL ONDE CRIAMOS AS NOSSAS VARIAVEIS
    
    // JButton precisa da importação da sua biblioteca
    // btnCampos é o nome da variavel - (você que escolhe)
    // matriz com - 10 linhas e 10 colunas
    JButton [][] btnCampos = new JButton [10][10];
    
    // MATRIZ PARA GUARDAR AS BOMBAS - true p/ bomba, false p/ numero
    boolean [][] bombas = new boolean [10][10];
    
    // MATRIZ PARA GUARDAR OS CAMPOS QUE FOREM ABERTOS
    boolean [][] abertos = new boolean [10][10];
    
    int quantidadeBombas = 15;
    int qtdCasasAbertas = 0;
    
    boolean jogoEncerrado = false;
    
    int segundosPassados = 0;
    Timer cronometro;
    /**
     * Creates new form Jogo
     */
    
    // CONSTRUTOR DA CLASSE/TELA - SEM ELE A TELA NÃO FUNCIONA
    public Jogo() {
        initComponents();
        //definir um tamanho para o meu painel
        painelCampo.setPreferredSize(new Dimension(900,700));
        
        CriarTabuleiro();
    }
    
    // CRIAR AS NOSSAS FUNÇÕES/MÉTODOS
    public void CriarTabuleiro(){
        painelCampo.setLayout(new GridLayout(10,10,2,2)); // permite uma área com divisão de linha e colunas (Linha, Coluna, Largura, Altura)
        
        for(int coluna=0;coluna<=9;coluna++){        
            for(int linha =0; linha<=9;linha++){
                // váriavel botão para guardar os dados provisorios
                JButton botao = new JButton();
                botao.setFont(new Font("Arial",Font.BOLD,16)); // FONTE
                botao.setBackground(new Color(97,30,30)); //COR DE FUNDO
                botao.setForeground(Color.WHITE); //COR DE TEXTO
                
                //remover marcas do botão que vem por padrão
                botao.setFocusPainted(false);
                botao.setEnabled(false); // desabilita o botão do campo minado
                
                final int linhaSelecionada = linha;
                final int colunaSelecionada = coluna;
                
                //adicionar o evento de clique para abrir as casas
                botao.addActionListener((ActionEvent Evento)->{abrirBotao(linhaSelecionada,colunaSelecionada);
                });
                //adicionar o botao dentro da matriz
                btnCampos[linha][coluna]=botao;
                
                // adicionar ele dentro do painel
                painelCampo.add(botao);
                
            } //fim do 2º for
            
        } //fim do 1º for
    } //fim do método CriarTabuleiro
    
    public void AdicionarBombas(){
        // Criar uma variavel Random para gerar valores aleatorios
        Random sorteador = new Random();
        int bombasAdicionadas = 0;
        
        while( bombasAdicionadas < quantidadeBombas){
            // sortear o nº da linha e coluna que vai ficar a bomba
            int linha = sorteador.nextInt(10);
            int coluna = sorteador.nextInt(10);
            // verifica se não existe bomba adicionada no local
            if(!bombas[linha][coluna]){
                // adicionar a bomba na matriz
                bombas[linha][coluna] = true;
                bombasAdicionadas++;
            }
        }
        
    } // fim do AdicionarBombas
    
    public void IniciarJogo(){
        LimparJogo();
        
        // Chamar o metodo adicionarBombas
        AdicionarBombas();
        IniciarCronometro();
                
        //depois precisamos iniciar os botoes do jogo
        for(int coluna = 0; coluna <= 9; coluna++){
            for (int linha=0;linha <=9; linha++){
                JButton botao = btnCampos[linha][coluna];
                //deixar os botoes visiveis e clicaveis
                botao.setEnabled(true);
            } // fim do 2º for
        } // fim do 1º for
        btnIniciar.setText("REINICIAR");
    } // fim do IniciarJogo
    
    public void abrirBotao(int linha, int coluna){
        // verificar se o jogo foi finalizado
        if(jogoEncerrado) return; // return é a função abrir botão vai dar ao usuário
        /*if(jogoEncerrado){
            return; 
        }*/
        
        // verificar se o botao ja foi aberto
        if(abertos[linha][coluna]){
            return;
        }
        /* Se o jogo ainda estiver rodando e o botão ainda não tiver
        sido aberto - então vamos abrir o botão*/
        abertos[linha][coluna] = true;
        qtdCasasAbertas++;
        
        // acessar o que tem dentro do botão
        JButton botao = btnCampos [linha][coluna];
        // se o botão tiver uma bomba, então vamos mostrar a bomba a ele
        if(bombas[linha][coluna]){
            //variavel que recebe nossa imagem
            ImageIcon imgBomba = new ImageIcon(getClass().getResource("/assets/bomb.png"));
        
            // colocar a imagem botao
            botao.setIcon(imgBomba);
            FinalizarJogo(false); // chamando o método FinalizarJogo
            return;
        }else{
            ImageIcon imgBandeira = new ImageIcon(getClass().getResource("/assets/flag.png"));
            botao.setIcon(imgBandeira);
            return;
        }
        
    } // fim do metodo abrirBotao
    
    // este metodo informa quando a pessoa perder ou ganhar o jogo
    public void FinalizarJogo(boolean venceu){
        MostrarBombas();
        // vamos informar que o jogo acabou
        jogoEncerrado = true;
        cronometro.stop();
        // verificar se a oessia venceu ou nao
        if(venceu){
            JOptionPane.showMessageDialog(this,"Parabéns, voce venceu!");
            LimparJogo();
        }else{
            JOptionPane.showMessageDialog(this, "Ops, voce perdeu o jogo!");
            LimparJogo();
        }
        
    }// fim do FinalizarJogo
    public void VerificarVitoria(){
        // armazenar a quantidade de casas com bandeiras
        int casasSemBomba = 100 - quantidadeBombas;
        /* se a pessoa abriu todas as bandeiras e não abriu nenhuma bomba
        então ela venceu o jogo, e o método FinalizarJogo imprime a mensagem */
        if(qtdCasasAbertas == casasSemBomba){
            FinalizarJogo(true);
        }
    }
    public void IniciarCronometro(){
        // zerar o cronometro caso tenha tido um jogo anterior
        if(cronometro != null){
            cronometro.stop();
        }
        // reseta o cronometro
        segundosPassados = 0;
        tfTempo.setText("00:00");
        
        // converter o tempo em minutos e segundos
        cronometro = new Timer(1000, Evento->{
            segundosPassados++;
            int minutos = segundosPassados/60;
            int horas = minutos/60;
            int segundo = segundosPassados%60;
            // mostrar o tempo dentro da variável
            tfTempo.setText(
            String.format("%02d:%02d",minutos,segundo)
            );
        });
        cronometro.start();
    }
    public void LimparJogo(){
        qtdCasasAbertas=0;
        jogoEncerrado = false;
        
        
        
        
        for(int coluna=0;coluna<=9;coluna++){
            for(int linha=0;linha<=9;linha++){
                bombas[linha][coluna]=false;
                abertos[linha][coluna]=false;
                               
                // limpeza dos botões
                JButton botao = btnCampos[linha][coluna];
                botao.setIcon(null);
                
                
                
            }// fim do 2º for
        } // fim do 1º for
        AdicionarBombas();
        IniciarCronometro();
        
    } // fim do LimparJogo
    public void MostrarBombas(){
        for(int coluna=0;coluna<=9;coluna++){
            for(int linha=0;linha<=9;linha++){
                JButton botao = btnCampos [linha][coluna];
                // se o botão tiver uma bomba, então vamos mostrar a bomba a ele
                if(bombas[linha][coluna]){
                    //variavel que recebe nossa imagem
                    ImageIcon imgBomba = new ImageIcon(getClass().getResource("/assets/bomb.png"));
                    // colocar a imagem botao
                    botao.setIcon(imgBomba);
                    
                } // fim do if
            } // fim do 2º for
        } // fim do 1º for
        
    } // fim do método MostrarBombas
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        titulo = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        tfTempo = new javax.swing.JTextField();
        painelCampo = new javax.swing.JPanel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        titulo.setBackground(new java.awt.Color(102, 102, 102));
        titulo.setFont(new java.awt.Font("Eras Bold ITC", 0, 48)); // NOI18N
        titulo.setForeground(new java.awt.Color(204, 51, 0));
        titulo.setText("Campo Minado");

        btnIniciar.setBackground(new java.awt.Color(0, 153, 153));
        btnIniciar.setFont(new java.awt.Font("Franklin Gothic Heavy", 0, 14)); // NOI18N
        btnIniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciar.setText("INICIAR");
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);

        tfTempo.setEditable(false);
        tfTempo.setBackground(new java.awt.Color(204, 204, 0));
        tfTempo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tfTempo.setText("00:00");
        tfTempo.addActionListener(this::tfTempoActionPerformed);

        painelCampo.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout painelCampoLayout = new javax.swing.GroupLayout(painelCampo);
        painelCampo.setLayout(painelCampoLayout);
        painelCampoLayout.setHorizontalGroup(
            painelCampoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        painelCampoLayout.setVerticalGroup(
            painelCampoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titulo)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(btnIniciar)
                        .addGap(18, 18, 18)
                        .addComponent(tfTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(181, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelCampo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(titulo)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar)
                    .addComponent(tfTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                .addComponent(painelCampo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(278, 278, 278))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tfTempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTempoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTempoActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        // TODO add your handling code here:
        IniciarJogo();
    }//GEN-LAST:event_btnIniciarActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Jogo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JPanel painelCampo;
    private javax.swing.JTextField tfTempo;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}

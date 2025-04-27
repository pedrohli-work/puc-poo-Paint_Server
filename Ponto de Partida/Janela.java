import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Janela extends JFrame {
    protected static final long serialVersionUID = 1L;

    protected JButton btnPonto = new JButton("Ponto"),
            btnLinha = new JButton("Linha"),
            btnCirculo = new JButton("Circulo"),
            btnElipse = new JButton("Elipse"),
            btnCores = new JButton("Cores"),
            btnAbrir = new JButton("Abrir"),
            btnSalvar = new JButton("Salvar"),
            btnApagar = new JButton("Apagar"),
            btnSair = new JButton("Sair");

    protected MeuJPanel pnlDesenho = new MeuJPanel();

    protected JLabel statusBar1 = new JLabel("Mensagem:"),
            statusBar2 = new JLabel("Coordenada:");

    protected boolean esperaPonto, esperaInicioReta, esperaFimReta;

    protected Color corAtual = Color.BLACK;
    protected Ponto p1;

    protected Vector<Figura> figuras = new Vector<Figura>();

    public Janela() {
        super("Editor Gr�fico");

        try {
            Image btnPontoImg = ImageIO.read(getClass().getResource("ponto.jpg"));
            btnPonto.setIcon(new ImageIcon(btnPontoImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo ponto.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnLinhaImg = ImageIO.read(getClass().getResource("linha.jpg"));
            btnLinha.setIcon(new ImageIcon(btnLinhaImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo linha.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnCirculoImg = ImageIO.read(getClass().getResource("circulo.jpg"));
            btnCirculo.setIcon(new ImageIcon(btnCirculoImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo circulo.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnElipseImg = ImageIO.read(getClass().getResource("elipse.jpg"));
            btnElipse.setIcon(new ImageIcon(btnElipseImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo elipse.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnCoresImg = ImageIO.read(getClass().getResource("cores.jpg"));
            btnCores.setIcon(new ImageIcon(btnCoresImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo cores.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnAbrirImg = ImageIO.read(getClass().getResource("abrir.jpg"));
            btnAbrir.setIcon(new ImageIcon(btnAbrirImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo abrir.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnSalvarImg = ImageIO.read(getClass().getResource("salvar.jpg"));
            btnSalvar.setIcon(new ImageIcon(btnSalvarImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo salvar.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnApagarImg = ImageIO.read(getClass().getResource("apagar.jpg"));
            btnApagar.setIcon(new ImageIcon(btnApagarImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo apagar.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        try {
            Image btnSairImg = ImageIO.read(getClass().getResource("sair.jpg"));
            btnSair.setIcon(new ImageIcon(btnSairImg));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo sair.jpg n�o foi encontrado",
                    "Arquivo de imagem ausente",
                    JOptionPane.WARNING_MESSAGE);
        }

        btnPonto.addActionListener(new DesenhoDePonto());
        btnLinha.addActionListener(new DesenhoDeReta());
        btnCores.addActionListener(e -> escolherCor());
        btnAbrir.addActionListener(e -> escolherArquivoParaAbrir());
        // btnSalvar.addActionListener(e -> enviarImagemParaServidor());
        btnSalvar.addActionListener(e -> {
            String[] options = { "Salvar no Computador", "Enviar para o Servidor" };
            int response = JOptionPane.showOptionDialog(this,
                    "Como você deseja salvar o desenho?",
                    "Salvar Desenho",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (response == 0) {
                escolherArquivoParaSalvar();
            } else if (response == 1) {
                enviarImagemParaServidor();
            }
        });

        JPanel pnlBotoes = new JPanel();
        FlowLayout flwBotoes = new FlowLayout();
        pnlBotoes.setLayout(flwBotoes);

        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnCores);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        JPanel pnlStatus = new JPanel();
        GridLayout grdStatus = new GridLayout(1, 2);
        pnlStatus.setLayout(grdStatus);

        pnlStatus.add(statusBar1);
        pnlStatus.add(statusBar2);

        Container cntForm = this.getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(pnlBotoes, BorderLayout.NORTH);
        cntForm.add(pnlDesenho, BorderLayout.CENTER);
        cntForm.add(pnlStatus, BorderLayout.SOUTH);

        this.addWindowListener(new FechamentoDeJanela());

        this.setSize(700, 500);
        this.setVisible(true);
    }

    protected class MeuJPanel extends JPanel implements MouseListener, MouseMotionListener {
        private Ponto pTemp; // Ponto temporário para desenho da linha

        public MeuJPanel() {
            super();
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

        public void paint(Graphics g) {
            super.paint(g); // Chama o paint da superclasse para limpar o painel
            for (Figura f : figuras) {
                f.torneSeVisivel(g);
            }
            if (esperaFimReta && pTemp != null) {
                g.setColor(corAtual); // Define a cor da linha temporária
                g.drawLine(p1.getX(), p1.getY(), pTemp.getX(), pTemp.getY()); // Desenha a linha temporária
            }
        }

        public void mousePressed(MouseEvent e) {
            if (esperaPonto) {
                figuras.add(new Ponto(e.getX(), e.getY(), corAtual));
                figuras.get(figuras.size() - 1).torneSeVisivel(pnlDesenho.getGraphics());
                esperaPonto = false;
            } else if (esperaInicioReta) {
                p1 = new Ponto(e.getX(), e.getY(), corAtual);
                esperaInicioReta = false;
                esperaFimReta = true;
                statusBar1.setText("Mensagem: clique o ponto final da reta");
            } else if (esperaFimReta) {
                esperaInicioReta = false;
                esperaFimReta = false;
                figuras.add(new Linha(p1.getX(), p1.getY(), e.getX(), e.getY(), corAtual));
                figuras.get(figuras.size() - 1).torneSeVisivel(pnlDesenho.getGraphics());
                statusBar1.setText("Mensagem:");
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (esperaFimReta) {
                figuras.add(new Linha(p1.getX(), p1.getY(), e.getX(), e.getY(), corAtual));
                pTemp = null; // Limpa o ponto temporário
                repaint(); // Redesenha o painel
                esperaInicioReta = true;
                esperaFimReta = false;
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if (esperaFimReta) {
                pTemp = new Ponto(e.getX(), e.getY(), corAtual);
                repaint(); // Redesenha o painel para mostrar a linha temporária
            }
        }

        public void mouseMoved(MouseEvent e) {
            statusBar2.setText("Coordenada: " + e.getX() + "," + e.getY());
        }
    }

    protected class DesenhoDePonto implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            esperaPonto = true;
            esperaInicioReta = false;
            esperaFimReta = false;

            statusBar1.setText("Mensagem: clique o local do ponto desejado");
        }
    }

    protected class DesenhoDeReta implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            esperaPonto = false;
            esperaInicioReta = true;
            esperaFimReta = false;

            statusBar1.setText("Mensagem: clique o ponto inicial da reta");
        }
    }

    protected class FechamentoDeJanela extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    public void salvarDesenho(String nomeArquivo) {
        try {
            FileWriter fw = new FileWriter(nomeArquivo);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Figura f : figuras) {
                bw.write(f.toString());
                bw.newLine();
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirDesenho(String nomeArquivo) {
        try {
            FileReader fr = new FileReader(nomeArquivo);
            BufferedReader br = new BufferedReader(fr);
            String linha;

            figuras.clear(); // Limpa o vetor atual de figuras

            while ((linha = br.readLine()) != null) {
                Figura figura = criarFiguraDeString(linha);
                if (figura != null) {
                    figuras.add(figura);
                }
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Figura criarFiguraDeString(String s) {
        // Aqui você deve interpretar a string 's' e criar a figura correspondente
        // Exemplo:
        if (s.startsWith("p:")) {
            return new Ponto(s);
        } else if (s.startsWith("r:")) {
            return new Linha(s);
        }
        // Adicione casos para Circulo, Elipse, etc.
        return null;
    }

    public void escolherArquivoParaSalvar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Desenho");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            salvarDesenho(fileToSave.getAbsolutePath());
        }
    }

    public void escolherArquivoParaAbrir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir Desenho");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            abrirDesenho(fileToOpen.getAbsolutePath());
        }
    }

    private BufferedImage capturarImagemDoPainel() {
        pnlDesenho.repaint();
        BufferedImage imagem = new BufferedImage(pnlDesenho.getWidth(), pnlDesenho.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagem.createGraphics();
        pnlDesenho.printAll(g2d);
        g2d.dispose();
        return imagem;
    }

    private void enviarImagemParaServidor() {
        BufferedImage imagem = capturarImagemDoPainel();
        try (MongoClient mongoClient = MongoClients
                .create("mongodb+srv://your_mongodb-key.vtnlrbv.mongodb.net/")) {
            MongoDatabase database = mongoClient.getDatabase("POO_JAVA");
            MongoCollection<Document> collection = database.getCollection("desenhos");

            // Converta a imagem para um formato adequado para armazenamento, por exemplo,
            // base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagem, "png", baos);
            byte[] imagemBytes = baos.toByteArray();
            String imagemBase64 = Base64.getEncoder().encodeToString(imagemBytes);

            // Crie um documento para armazenar a imagem no MongoDB
            Document doc = new Document("imagem", imagemBase64);

            // Insira o documento na coleção do MongoDB
            collection.insertOne(doc);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar no MongoDB: " + e.getMessage());
        }
    }

    private void escolherCor() {
        Color novaCor = JColorChooser.showDialog(this, "Escolha uma cor", corAtual);
        if (novaCor != null) {
            corAtual = novaCor;
        }
    }
}

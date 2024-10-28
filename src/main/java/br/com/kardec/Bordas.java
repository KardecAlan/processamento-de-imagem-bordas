package br.com.kardec;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Bordas extends JFrame {
    private JLabel imageLabel;
    private BufferedImage originalImage;
    private BufferedImage processedImage;

    public Bordas() {
        setTitle("Processador de Imagem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel para mostrar a imagem
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // Painel de botões (parâmetros)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1));

        JButton openButton = new JButton("Abrir Imagem");
        JButton grayscaleButton = new JButton("Converter para Tons de Cinza");
        JButton sobelButton = new JButton("Filtro Sobel");
        JButton prewittButton = new JButton("Filtro Prewitt");
        JButton cannyButton = new JButton("Filtro Canny");
        JButton combinedEdgesButton = new JButton("Bordas Combinadas");

        // Ações dos botões
        openButton.addActionListener(e -> openImage());
        grayscaleButton.addActionListener(e -> convertToGrayscale());
        sobelButton.addActionListener(e -> applySobelFilter());
        prewittButton.addActionListener(e -> applyPrewittFilter());
        cannyButton.addActionListener(e -> applyCannyFilter());
        combinedEdgesButton.addActionListener(e -> applyCombinedEdgeDetection());

        buttonPanel.add(openButton);
        buttonPanel.add(grayscaleButton);
        buttonPanel.add(sobelButton);
        buttonPanel.add(prewittButton);
        buttonPanel.add(cannyButton);
        buttonPanel.add(combinedEdgesButton);

        add(buttonPanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null); // Centraliza a janela
        setVisible(true);
    }

    // Abrir imagem
    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(file);
                processedImage = originalImage;
                imageLabel.setIcon(new ImageIcon(processedImage));
                pack();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem");
            }
        }
    }

    // Converter para tons de cinza
    private void convertToGrayscale() {
        if (processedImage != null) {
            BufferedImage grayscaleImage = new BufferedImage(
                    processedImage.getWidth(), processedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = grayscaleImage.getGraphics();
            g.drawImage(processedImage, 0, 0, null);
            g.dispose();
            processedImage = grayscaleImage;
            imageLabel.setIcon(new ImageIcon(processedImage));
        }
    }

    // Método para aplicar o filtro de Sobel na imagem atual
    private void applySobelFilter() {
        if (processedImage != null) {
            processedImage = applySobelFilter(processedImage);
            imageLabel.setIcon(new ImageIcon(processedImage));
        }
    }

    // Método Sobel com argumento para combinar bordas
    private BufferedImage applySobelFilter(BufferedImage img) {
        BufferedImage sobelImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int y = 1; y < img.getHeight() - 1; y++) {
            for (int x = 1; x < img.getWidth() - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int gray = new Color(img.getRGB(x + i, y + j)).getRed();
                        gx += gray * sobelX[j + 1][i + 1];
                        gy += gray * sobelY[j + 1][i + 1];
                    }
                }

                int gradient = (int) Math.sqrt(gx * gx + gy * gy);
                gradient = Math.min(Math.max(gradient, 0), 255);
                Color newColor = new Color(gradient, gradient, gradient);
                sobelImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return sobelImage;
    }

    // Método para aplicar o filtro de Prewitt na imagem atual
    private void applyPrewittFilter() {
        if (processedImage != null) {
            processedImage = applyPrewittFilter(processedImage);
            imageLabel.setIcon(new ImageIcon(processedImage));
        }
    }

    // Método Prewitt com argumento para combinar bordas
    private BufferedImage applyPrewittFilter(BufferedImage img) {
        BufferedImage prewittImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        int[][] prewittX = {
                {-1, 0, 1},
                {-1, 0, 1},
                {-1, 0, 1}
        };

        int[][] prewittY = {
                {-1, -1, -1},
                {0, 0, 0},
                {1, 1, 1}
        };

        for (int y = 1; y < img.getHeight() - 1; y++) {
            for (int x = 1; x < img.getWidth() - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int gray = new Color(img.getRGB(x + i, y + j)).getRed();
                        gx += gray * prewittX[j + 1][i + 1];
                        gy += gray * prewittY[j + 1][i + 1];
                    }
                }

                int gradient = (int) Math.sqrt(gx * gx + gy * gy);
                gradient = Math.min(Math.max(gradient, 0), 255);
                Color newColor = new Color(gradient, gradient, gradient);
                prewittImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return prewittImage;
    }

    // Método para aplicar o filtro Canny na imagem atual
    private void applyCannyFilter() {
        if (processedImage != null) {
            processedImage = applyCannyFilter(processedImage);
            imageLabel.setIcon(new ImageIcon(processedImage));
        }
    }

    // Método Canny com argumento para combinar bordas
    private BufferedImage applyCannyFilter(BufferedImage img) {
        BufferedImage cannyImage = applySobelFilter(img);

        for (int y = 0; y < cannyImage.getHeight(); y++) {
            for (int x = 0; x < cannyImage.getWidth(); x++) {
                int gray = new Color(cannyImage.getRGB(x, y)).getRed();
                if (gray < 128) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                Color newColor = new Color(gray, gray, gray);
                cannyImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return cannyImage;
    }

    // Aplicar detecção de bordas combinada
    private void applyCombinedEdgeDetection() {
        if (processedImage != null) {
            BufferedImage sobelImage = applySobelFilter(processedImage);
            BufferedImage prewittImage = applyPrewittFilter(processedImage);
            BufferedImage cannyImage = applyCannyFilter(processedImage);

            BufferedImage edgeImage = new BufferedImage(processedImage.getWidth(), processedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            for (int y = 0; y < processedImage.getHeight(); y++) {
                for (int x = 0; x < processedImage.getWidth(); x++) {
                    int sobelGray = new Color(sobelImage.getRGB(x, y)).getRed();
                    int prewittGray = new Color(prewittImage.getRGB(x, y)).getRed();
                    int cannyGray = new Color(cannyImage.getRGB(x, y)).getRed();

                    int combinedGray = (int) (0.25 * sobelGray + 0.25 * prewittGray + 0.5 * cannyGray);
                    Color newColor = new Color(combinedGray, combinedGray, combinedGray);
                    edgeImage.setRGB(x, y, newColor.getRGB());
                }
            }

            processedImage = edgeImage;
            imageLabel.setIcon(new ImageIcon(processedImage));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Bordas::new);
    }
}

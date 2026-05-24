package view; //La classe appartient au package view

import controller.AuthController; //utiliser la méthode register()
//les composants Swing
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterView extends JFrame { //RegisterView hérite de JFrame, donc c’est une fenêtre.

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField; //mot de passe. Le texte est masqué.
    private JPasswordField confirmPasswordField;
    private JButton registerButton; //Bouton pour créer le compte 
    private JButton backButton; //bouton pour revenir au login.
    private JLabel errorLabel; //Label utilisé pour afficher les erreurs dans la fenêtre.

    private AuthController authController;  //Contrôleur utilisé pour inscrire l’utilisateur dans la base de données.

    // ── Palette (identique LoginView / DashboardView) ─────────────────────────
    private static final Color BG         = new Color(0xF4F6FB);
    private static final Color SURFACE    = Color.WHITE;
    private static final Color BORDER_CLR = new Color(0xE4E8F0);
    private static final Color PRIMARY    = new Color(0x2563EB);
    private static final Color PRIMARY_H  = new Color(0x1D4ED8);
    private static final Color TEXT_MAIN  = new Color(0x111827);
    private static final Color TEXT_SEC   = new Color(0x6B7280);
    private static final Color DANGER     = new Color(0xDC2626);
    private static final Color SUCCESS    = new Color(0x16A34A);
    private static final Color HEADER_BG  = new Color(0x1E3A8A);
    private static final Color INPUT_FOC  = new Color(0x93C5FD);

    public RegisterView() { //C’est le constructeur. Il s’exécute quand on fait : new RegisterView()
        authController = new AuthController(); //On crée le contrôleur pour gérer l’inscription.

        setTitle("Inscription — To Do List");
        setSize(420, 700);
        setMinimumSize(new Dimension(380, 680));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Ferme seulement cette fenêtre, pas toute l’application.
        setLocationRelativeTo(null);
        setResizable(false); //Empêche l’utilisateur de redimensionner la fenêtre.

        // Root
        JPanel root = new JPanel(new GridBagLayout()); //Crée le panneau principal avec GridBagLayout.
        root.setBackground(BG);
        setContentPane(root);

        // ── Card ──────────────────────────────────────────────────────────────
        JPanel card = new JPanel() { //crée une carte personnalisée
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_CLR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(360, 640));

        // ── Header stripe ─────────────────────────────────────────────────────
        JPanel stripe = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 16, 16, 16);
            }
        };
        stripe.setOpaque(false);
        stripe.setPreferredSize(new Dimension(0, 90));
        stripe.setLayout(new BorderLayout());

        JPanel logoBlock = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 22));
        logoBlock.setOpaque(false);

        JPanel logoCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String t = "✓";
                g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        logoCircle.setPreferredSize(new Dimension(40, 40));
        logoCircle.setOpaque(false);

        JLabel appName = new JLabel("To Do List");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(Color.WHITE);

        logoBlock.add(logoCircle);
        logoBlock.add(appName);
        stripe.add(logoBlock, BorderLayout.CENTER);
        card.add(stripe, BorderLayout.NORTH);

        // ── Body ──────────────────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel title = new JLabel("Créer un compte");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Rejoignez-nous et organisez vos tâches");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SEC);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(title);
        body.add(Box.createVerticalStrut(4));
        body.add(sub);
        body.add(Box.createVerticalStrut(22));

        // Name
        body.add(buildFieldLabel("Nom complet"));
        body.add(Box.createVerticalStrut(6));
        nameField = buildTextField("Jean Dupont");
        body.add(nameField);
        body.add(Box.createVerticalStrut(14));

        // Email
        body.add(buildFieldLabel("Adresse email"));
        body.add(Box.createVerticalStrut(6));
        emailField = buildTextField("exemple@email.com");
        body.add(emailField);
        body.add(Box.createVerticalStrut(14));

        // Password
        body.add(buildFieldLabel("Mot de passe"));
        body.add(Box.createVerticalStrut(6));
        passwordField = new JPasswordField();
        styleInput(passwordField);
        body.add(passwordField);
        body.add(Box.createVerticalStrut(14));

        // Confirm password
        body.add(buildFieldLabel("Confirmer le mot de passe"));
        body.add(Box.createVerticalStrut(6));
        confirmPasswordField = new JPasswordField();
        styleInput(confirmPasswordField);
        body.add(confirmPasswordField);
        body.add(Box.createVerticalStrut(8));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(errorLabel);
        body.add(Box.createVerticalStrut(18));

        // Register button
        registerButton = buildPrimaryButton("Créer mon compte");
        body.add(registerButton);
        body.add(Box.createVerticalStrut(12));

        // Separator
        body.add(buildSeparator());
        body.add(Box.createVerticalStrut(12));

        // Back button
        backButton = buildOutlineButton("← Retour à la connexion");
        body.add(backButton);

        card.add(body, BorderLayout.CENTER);
        root.add(card);

        // ── Actions ───────────────────────────────────────────────────────────
        registerButton.addActionListener(e -> doRegister());
        confirmPasswordField.addActionListener(e -> doRegister());

        backButton.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        setVisible(true);
    }

    private void doRegister() {
        String name            = nameField.getText().trim();
        String email           = emailField.getText().trim();
        String password        = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Basic placeholder check
        if (name.equals("Jean Dupont") || name.isEmpty()) {
            showError("Veuillez entrer votre nom complet.");
            return;
        }
        if (email.equals("exemple@email.com") || email.isEmpty()) {
            showError("Veuillez entrer une adresse email valide.");
            return;
        }
        if (password.isEmpty()) {
            showError("Veuillez choisir un mot de passe.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas.");
            confirmPasswordField.setText("");
            confirmPasswordField.requestFocus();
            return;
        }

        boolean success = authController.register(name, email, password);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Compte créé avec succès ! Vous pouvez maintenant vous connecter.",
                    "Inscription réussie", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginView();
        } else {
            showError("Cet email est déjà utilisé. Essayez-en un autre.");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
    }

    // ── Field helpers ─────────────────────────────────────────────────────────
    private JLabel buildFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_MAIN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField f = new JTextField();
        f.setForeground(TEXT_SEC);
        f.setText(placeholder);
        styleInput(f);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(TEXT_MAIN);
                }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setForeground(TEXT_SEC);
                    f.setText(placeholder);
                }
            }
        });
        return f;
    }

    private void styleInput(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (f.getForeground() == null || f.getForeground().equals(TEXT_MAIN))
            f.setForeground(TEXT_MAIN);
        f.setBackground(SURFACE);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setPreferredSize(new Dimension(0, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(INPUT_FOC, 2, true),
                        new EmptyBorder(5, 11, 5, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_CLR, 1, true),
                        new EmptyBorder(6, 12, 6, 12)
                ));
            }
        });
    }

    private JButton buildPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? PRIMARY_H : PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(11, 0, 11, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildOutlineButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 15));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(PRIMARY);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buildSeparator() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JSeparator l = new JSeparator(); l.setForeground(BORDER_CLR);
        JSeparator r = new JSeparator(); r.setForeground(BORDER_CLR);
        JLabel or = new JLabel("ou");
        or.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        or.setForeground(TEXT_SEC);
        or.setHorizontalAlignment(JLabel.CENTER);
        p.add(l, BorderLayout.WEST);
        p.add(or, BorderLayout.CENTER);
        p.add(r, BorderLayout.EAST);
        return p;
    }
}
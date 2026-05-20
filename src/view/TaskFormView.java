package view;

import controller.TaskController;
import model.Task;
import model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskFormView extends JFrame {

    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField dueDateField;
    private JComboBox<String> statusCombo;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel errorLabel;

    private TaskController taskController;
    private User currentUser;
    private Task existingTask;
    private DashboardView dashboard;

    // ── Palette (identique à toute l'app) ─────────────────────────────────────
    private static final Color BG         = new Color(0xF4F6FB);
    private static final Color SURFACE    = Color.WHITE;
    private static final Color BORDER_CLR = new Color(0xE4E8F0);
    private static final Color PRIMARY    = new Color(0x2563EB);
    private static final Color PRIMARY_H  = new Color(0x1D4ED8);
    private static final Color TEXT_MAIN  = new Color(0x111827);
    private static final Color TEXT_SEC   = new Color(0x6B7280);
    private static final Color DANGER     = new Color(0xDC2626);
    private static final Color HEADER_BG  = new Color(0x1E3A8A);
    private static final Color INPUT_FOC  = new Color(0x93C5FD);
    private static final Color SUCCESS    = new Color(0x16A34A);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TaskFormView(User user, Task task, DashboardView dashboard) {
        this.currentUser  = user;
        this.existingTask = task;
        this.dashboard    = dashboard;
        this.taskController = new TaskController();

        boolean isEditing = (task != null);

        setTitle(isEditing ? "Modifier la tâche — To Do List" : "Nouvelle tâche — To Do List");
        setSize(440, 580);
        setMinimumSize(new Dimension(400, 540));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Root
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        setContentPane(root);

        // ── Card ──────────────────────────────────────────────────────────────
        JPanel card = new JPanel() {
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
        card.setPreferredSize(new Dimension(390, 520));

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
        stripe.setPreferredSize(new Dimension(0, 80));
        stripe.setLayout(new BorderLayout());

        JPanel logoBlock = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 18));
        logoBlock.setOpaque(false);

        // Icon circle
        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String ico = isEditing ? "✏" : "+";
                g2.drawString(ico, (getWidth() - fm.stringWidth(ico)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        iconCircle.setPreferredSize(new Dimension(38, 38));
        iconCircle.setOpaque(false);

        JLabel headerTitle = new JLabel(isEditing ? "Modifier la tâche" : "Nouvelle tâche");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerTitle.setForeground(Color.WHITE);

        logoBlock.add(iconCircle);
        logoBlock.add(headerTitle);
        stripe.add(logoBlock, BorderLayout.CENTER);
        card.add(stripe, BorderLayout.NORTH);

        // ── Body ──────────────────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(26, 32, 26, 32));

        JLabel sub = new JLabel(isEditing
                ? "Modifiez les informations de votre tâche"
                : "Remplissez les informations de la nouvelle tâche");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SEC);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(sub);
        body.add(Box.createVerticalStrut(20));

        // Title
        body.add(buildFieldLabel("Titre *"));
        body.add(Box.createVerticalStrut(6));
        titleField = buildTextField("Ex : Réviser le cours de Java");
        body.add(titleField);
        body.add(Box.createVerticalStrut(14));

        // Description
        body.add(buildFieldLabel("Description"));
        body.add(Box.createVerticalStrut(6));
        descriptionField = buildTextField("Détails optionnels...");
        body.add(descriptionField);
        body.add(Box.createVerticalStrut(14));

        // Due date
        body.add(buildFieldLabel("Date d'échéance  (yyyy-MM-dd HH:mm)"));
        body.add(Box.createVerticalStrut(6));
        dueDateField = buildTextField("2026-12-31 23:59");
        body.add(dueDateField);
        body.add(Box.createVerticalStrut(14));

        // Status
        body.add(buildFieldLabel("Statut"));
        body.add(Box.createVerticalStrut(6));
        statusCombo = buildStyledCombo();
        body.add(statusCombo);
        body.add(Box.createVerticalStrut(8));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(errorLabel);
        body.add(Box.createVerticalStrut(16));

        // Buttons row
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        cancelButton = buildOutlineButton("Annuler");
        saveButton   = buildPrimaryButton(isEditing ? "Enregistrer" : "Ajouter la tâche");

        btnRow.add(cancelButton);
        btnRow.add(saveButton);
        body.add(btnRow);

        card.add(body, BorderLayout.CENTER);
        root.add(card);

        // ── Pre-fill if editing ───────────────────────────────────────────────
        if (isEditing) {
            titleField.setText(task.getTitle());
            titleField.setForeground(TEXT_MAIN);
            descriptionField.setText(task.getDescription());
            descriptionField.setForeground(TEXT_MAIN);
            if (task.getDueDate() != null) {
                dueDateField.setText(task.getDueDate().format(FORMATTER));
                dueDateField.setForeground(TEXT_MAIN);
            }
            statusCombo.setSelectedItem(task.getStatus());
        }

        // ── Actions ───────────────────────────────────────────────────────────
        saveButton.addActionListener(e -> doSave(isEditing));
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void doSave(boolean isEditing) {
        String title       = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String dueDateText = dueDateField.getText().trim();
        String status      = (String) statusCombo.getSelectedItem();

        // Ignore placeholder values
        if (title.equals("Ex : Réviser le cours de Java") || title.isEmpty()) {
            errorLabel.setText("Le titre est obligatoire.");
            titleField.requestFocus();
            return;
        }
        if (description.equals("Détails optionnels...")) description = "";

        LocalDateTime dueDate;
        try {
            dueDate = LocalDateTime.parse(dueDateText, FORMATTER);
        } catch (Exception ex) {
            errorLabel.setText("Format de date invalide. Exemple : 2026-12-31 23:59");
            dueDateField.requestFocus();
            return;
        }

        errorLabel.setText(" ");

        if (isEditing) {
            existingTask.setTitle(title);
            existingTask.setDescription(description);
            existingTask.setDueDate(dueDate);
            existingTask.setStatus(status);
            taskController.updateTask(existingTask);
        } else {
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setCreationDate(LocalDateTime.now());
            newTask.setDueDate(dueDate);
            newTask.setStatus(status);
            newTask.setUser(currentUser);
            taskController.addTask(newTask);
        }

        dashboard.loadTasks("All");
        dispose();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel buildFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_MAIN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT_SEC);
        f.setText(placeholder);
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
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(TEXT_MAIN);
                }
                f.setBorder(new CompoundBorder(
                        new LineBorder(INPUT_FOC, 2, true),
                        new EmptyBorder(5, 11, 5, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setForeground(TEXT_SEC);
                    f.setText(placeholder);
                }
                f.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_CLR, 1, true),
                        new EmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return f;
    }

    private JComboBox<String> buildStyledCombo() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"TODO", "DONE"});
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(SURFACE);
        combo.setForeground(TEXT_MAIN);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setBorder(new CompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(2, 8, 2, 8)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                if (isSelected) {
                    setBackground(new Color(0xDBEAFE));
                    setForeground(PRIMARY);
                } else {
                    setBackground(SURFACE);
                    setForeground(TEXT_MAIN);
                }
                return this;
            }
        });
        return combo;
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 0, 10, 0));
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
                g2.setColor(BORDER_CLR.darker());
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_SEC);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
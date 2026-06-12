package view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class ModernUI {

    static final Color BACKGROUND = new Color(239, 248, 240);
    static final Color SURFACE = Color.WHITE;
    static final Color TABLE_HEADER_GREEN = new Color(34, 139, 34);
    static final Color PRIMARY = new Color(80, 170, 98);
    static final Color PRIMARY_DARK = new Color(39, 126, 58);
    static final Color PRIMARY_LIGHT = new Color(218, 243, 224);
    static final Color DANGER = new Color(214, 76, 82);
    static final Color DANGER_DARK = new Color(176, 48, 56);
    static final Color TEXT = new Color(35, 49, 42);
    static final Color MUTED = new Color(99, 116, 108);
    static final Color BORDER = new Color(196, 225, 203);
    static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private static boolean installed = false;
    private static boolean dialogStylerInstalled = false;
    private static final Map<Class<? extends JFrame>, JFrame> OPEN_WINDOWS = new HashMap<>();

    private ModernUI() {
    }

    static void install() {
        if (installed) {
            return;
        }
        installed = true;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("Panel.background", new ColorUIResource(BACKGROUND));
        UIManager.put("OptionPane.background", new ColorUIResource(PRIMARY_LIGHT));
        UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(8, 10, 8, 10));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXT));
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
        UIManager.put("OptionPane.minimumSize", new Dimension(320, 140));
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(12, 12, 12, 12));
        UIManager.put("OptionPane.foreground", new ColorUIResource(TEXT));
        UIManager.put("OptionPane.questionDialog.titlePane.background", new ColorUIResource(PRIMARY_LIGHT));
        UIManager.put("Button.background", new ColorUIResource(PRIMARY));
        UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("Label.font", FONT);
        UIManager.put("TextField.font", FONT);
        UIManager.put("PasswordField.font", FONT);
        UIManager.put("ComboBox.font", FONT);
        UIManager.put("ComboBox.background", new ColorUIResource(SURFACE));
        UIManager.put("ComboBox.foreground", new ColorUIResource(TEXT));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(PRIMARY_LIGHT));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(TEXT));
        UIManager.put("CheckBox.font", FONT);
        UIManager.put("RadioButton.font", FONT);
        UIManager.put("Table.font", FONT);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("TableHeader.background", new ColorUIResource(TABLE_HEADER_GREEN));
        UIManager.put("TableHeader.foreground", new ColorUIResource(Color.BLACK));
        UIManager.put("TabbedPane.font", FONT_BOLD);
        UIManager.put("ToolTip.background", new ColorUIResource(new Color(250, 255, 251)));
        UIManager.put("ToolTip.foreground", new ColorUIResource(TEXT));
        UIManager.put("ToolTip.border", new RoundedBorder(BORDER, 10, 1, 8, 10));
        UIManager.put("OptionPane.informationIcon", new ActionIcon("info", PRIMARY));
        UIManager.put("OptionPane.warningIcon", new ActionIcon("warning", new Color(223, 157, 42)));
        UIManager.put("OptionPane.errorIcon", new ActionIcon("error", DANGER));
        UIManager.put("OptionPane.questionIcon", new ActionIcon("search", PRIMARY));
        installDialogStyler();
    }

    private static void installDialogStyler() {
        if (dialogStylerInstalled) {
            return;
        }
        dialogStylerInstalled = true;
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof WindowEvent && event.getID() == WindowEvent.WINDOW_OPENED) {
                Window window = ((WindowEvent) event).getWindow();
                if (window instanceof JDialog && containsOptionPane((Container) window)) {
                    styleDialog((JDialog) window);
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    private static boolean containsOptionPane(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JOptionPane) {
                return true;
            }
            if (component instanceof Container && containsOptionPane((Container) component)) {
                return true;
            }
        }
        return false;
    }

    private static void styleDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(PRIMARY_LIGHT);
        styleDialogTree(dialog.getContentPane());
        SwingUtilities.invokeLater(() -> {
            dialog.pack();
            dialog.setMinimumSize(new Dimension(320, 140));
            Dimension packed = dialog.getSize();
            dialog.setSize(Math.max(packed.width, 320), Math.max(packed.height, 140));
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().revalidate();
            dialog.getContentPane().repaint();
        });
    }

    private static void styleDialogTree(Component component) {
        if (component instanceof JPanel || component instanceof JOptionPane) {
            component.setBackground(PRIMARY_LIGHT);
            if (component instanceof JOptionPane) {
                ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            }
        }
        if (component instanceof JLabel) {
            ((JLabel) component).setForeground(TEXT);
            ((JLabel) component).setFont(FONT);
        }
        if (component instanceof JButton) {
            ((JButton) component).putClientProperty("modernStyled", null);
            styleButton((JButton) component);
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                styleDialogTree(child);
            }
        }
    }

    static void registerWindow(Class<? extends JFrame> type, JFrame frame) {
        OPEN_WINDOWS.put(type, frame);
        if (frame.getRootPane().getClientProperty("singletonRegistered") == null) {
            frame.getRootPane().putClientProperty("singletonRegistered", Boolean.TRUE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    OPEN_WINDOWS.remove(type, frame);
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    OPEN_WINDOWS.remove(type, frame);
                }
            });
        }
    }

    private static void bringToFront(JFrame frame) {
        if ((frame.getExtendedState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
            frame.setExtendedState(frame.getExtendedState() & ~Frame.ICONIFIED);
        }
        frame.toFront();
        frame.requestFocus();
        frame.revalidate();
        frame.repaint();
    }

    static void applyFrame(JFrame frame) {
        install();
        frame.getContentPane().setBackground(BACKGROUND);
        SwingUtilities.updateComponentTreeUI(frame);
        styleTree(frame.getContentPane());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    static void styleTree(Component component) {
        if (component instanceof JPanel) {
            stylePanel((JPanel) component);
        }
        if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        }
        if (component instanceof JButton) {
            styleButton((JButton) component);
        }
        if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        }
        if (component instanceof JTextArea) {
            styleTextArea((JTextArea) component);
        }
        if (component instanceof JComboBox) {
            styleCombo((JComboBox<?>) component);
        }
        if (component instanceof JTable) {
            styleTable((JTable) component);
        }
        if (component instanceof JScrollPane) {
            styleScroll((JScrollPane) component);
        }
        if (component instanceof JMenuBar) {
            styleMenuBar((JMenuBar) component);
        }
        if (component instanceof JTabbedPane) {
            styleTabbedPane((JTabbedPane) component);
        }
        if (component instanceof AbstractButton && !(component instanceof JButton)) {
            styleToggle((AbstractButton) component);
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                styleTree(child);
            }
        }
    }

    static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        return button;
    }

    static Icon accentIcon(String text, Color color) {
        Icon icon = iconForText(text == null ? "" : text, false);
        return icon instanceof ActionIcon ? ((ActionIcon) icon).withColor(color) : new ActionIcon("info", color);
    }

    static Icon fieldIcon(String type) {
        return new ActionIcon(type, PRIMARY_DARK);
    }

    static void configureTableHeader(JTable table) {
        styleTableHeader(table);
    }

    static Border cardBorder() {
        return new ShadowBorder(16, new Insets(14, 16, 18, 16));
    }

    private static void stylePanel(JPanel panel) {
        if (panel.getBackground() != null && panel.getBackground().equals(new Color(245, 245, 245))) {
            panel.setBackground(BACKGROUND);
        }
        Border border = panel.getBorder();
        if (panel.isOpaque() && SURFACE.equals(panel.getBackground()) && border != null) {
            String borderName = border.getClass().getName().toLowerCase(Locale.ROOT);
            if (borderName.contains("compound") || borderName.contains("lineborder")) {
                panel.setBorder(cardBorder());
            }
        }
        relaxMaximumSize(panel);
    }

    private static void relaxMaximumSize(JPanel panel) {
        Dimension maximum = panel.getMaximumSize();
        if (maximum == null || maximum.height == Integer.MAX_VALUE) {
            return;
        }
        Dimension preferred = panel.getPreferredSize();
        if (preferred != null && preferred.height > maximum.height) {
            panel.setMaximumSize(new Dimension(maximum.width, preferred.height + 24));
        }
    }

    private static void styleLabel(JLabel label) {
        label.setFont(adjustFont(label.getFont()));
        if (label.getForeground() == null || Color.BLACK.equals(label.getForeground())) {
            label.setForeground(TEXT);
        }
    }

    private static void styleTextField(JTextField field) {
        if (field.getClientProperty("modernTextStyled") != null) {
            return;
        }
        field.putClientProperty("modernTextStyled", Boolean.TRUE);
        field.setFont(adjustFont(field.getFont()));
        field.setForeground(TEXT);
        field.setCaretColor(PRIMARY_DARK);
        field.setSelectionColor(PRIMARY_LIGHT);
        field.setSelectedTextColor(TEXT);
        field.setOpaque(true);
        field.setBackground(SURFACE);
        field.setBorder(new RoundedBorder(BORDER, 14, 1, 10, 12));
        field.setMinimumSize(new Dimension(120, Math.max(38, field.getPreferredSize().height)));
        field.putClientProperty("JTextField.placeholderText", guessPlaceholder(field));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new RoundedBorder(PRIMARY, 14, 2, 10, 12));
                field.setBackground(new Color(251, 255, 252));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new RoundedBorder(BORDER, 14, 1, 10, 12));
                field.setBackground(SURFACE);
            }
        });
        if (field.getToolTipText() == null || field.getToolTipText().trim().isEmpty()) {
            field.setToolTipText("Preencha este campo");
        }
    }

    private static void styleTextArea(JTextArea area) {
        if (area.getClientProperty("modernTextAreaStyled") != null) {
            return;
        }
        area.putClientProperty("modernTextAreaStyled", Boolean.TRUE);
        area.setFont(adjustFont(area.getFont()));
        area.setForeground(TEXT);
        area.setCaretColor(PRIMARY_DARK);
        area.setSelectionColor(PRIMARY_LIGHT);
        area.setSelectedTextColor(TEXT);
        area.setBackground(SURFACE);
        area.setBorder(new RoundedBorder(BORDER, 14, 1, 10, 12));
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                area.setBorder(new RoundedBorder(PRIMARY, 14, 2, 10, 12));
                area.setBackground(new Color(251, 255, 252));
            }

            @Override
            public void focusLost(FocusEvent e) {
                area.setBorder(new RoundedBorder(BORDER, 14, 1, 10, 12));
                area.setBackground(SURFACE);
            }
        });
    }

    private static void styleCombo(JComboBox<?> combo) {
        if (combo.getClientProperty("modernComboStyled") != null) {
            combo.setUI(new ModernComboBoxUI());
            combo.setBackground(SURFACE);
            combo.setForeground(TEXT);
            combo.setBorder(new RoundedBorder(BORDER, 14, 1, 7, 10));
            combo.revalidate();
            combo.repaint();
            return;
        }
        combo.putClientProperty("modernComboStyled", Boolean.TRUE);
        combo.setFont(adjustFont(combo.getFont()));
        combo.setForeground(TEXT);
        combo.setBackground(SURFACE);
        combo.setBorder(new RoundedBorder(BORDER, 14, 1, 7, 10));
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        combo.setOpaque(false);
        combo.setRenderer(new ModernComboRenderer(combo.getRenderer()));
        combo.setUI(new ModernComboBoxUI());
        combo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                combo.setBorder(new RoundedBorder(PRIMARY, 14, 1, 7, 10));
                combo.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!combo.hasFocus()) {
                    combo.setBorder(new RoundedBorder(BORDER, 14, 1, 7, 10));
                    combo.repaint();
                }
            }
        });
        combo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                combo.setBorder(new RoundedBorder(PRIMARY_DARK, 14, 2, 7, 10));
                combo.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                combo.setBorder(new RoundedBorder(BORDER, 14, 1, 7, 10));
                combo.repaint();
            }
        });
        if (combo.getToolTipText() == null || combo.getToolTipText().trim().isEmpty()) {
            combo.setToolTipText("Selecione uma opção");
        }
    }

    private static void styleTable(JTable table) {
        table.setFont(adjustFont(table.getFont()));
        table.setRowHeight(Math.max(table.getRowHeight(), 38));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(225, 236, 229));
        table.setSelectionBackground(new Color(198, 234, 207));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        styleTableHeader(table);

        final TableCellRenderer previousRenderer = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = previousRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    ((JComponent) c).setOpaque(true);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else if (isPlainTableColor(c.getBackground())) {
                    c.setBackground(row % 2 == 0 ? SURFACE : new Color(247, 252, 248));
                    c.setForeground(TEXT);
                } else if (Color.WHITE.equals(c.getForeground())) {
                    c.setForeground(TEXT);
                }
                return c;
            }
        });
    }

    private static void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        header.setBackground(TABLE_HEADER_GREEN);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 44));
        header.setReorderingAllowed(false);

        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        if (defaultRenderer instanceof DefaultTableCellRenderer) {
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) defaultRenderer;
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setBackground(TABLE_HEADER_GREEN);
            renderer.setForeground(Color.BLACK);
            renderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
            renderer.setOpaque(true);
        }

        header.setDefaultRenderer(new SolidHeaderRenderer(defaultRenderer));
        header.repaint();
        header.revalidate();
    }

    private static class SolidHeaderRenderer extends DefaultTableCellRenderer {
        private final TableCellRenderer delegate;

        SolidHeaderRenderer(TableCellRenderer delegate) {
            this.delegate = delegate;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component component = delegate == null
                    ? super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
                    : delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            JLabel label = component instanceof JLabel ? (JLabel) component : this;
            label.setOpaque(true);
            label.setBackground(TABLE_HEADER_GREEN);
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(24, 112, 45)),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)
            ));
            label.setText(value == null ? "" : value.toString());
            return label;
        }
    }

    private static boolean isPlainTableColor(Color color) {
        return color == null || Color.WHITE.equals(color) || Color.BLACK.equals(color)
                || new Color(248, 248, 248).equals(color) || new Color(240, 240, 240).equals(color);
    }

    private static String guessPlaceholder(JTextField field) {
        if (field instanceof JPasswordField) {
            return "Digite sua senha";
        }
        String name = field.getName();
        if (name != null && !name.trim().isEmpty()) {
            return "Digite " + name;
        }
        return "Digite aqui";
    }

    private static class ModernComboRenderer extends DefaultListCellRenderer {
        private final ListCellRenderer<Object> delegate;

        @SuppressWarnings("unchecked")
        ModernComboRenderer(ListCellRenderer<?> delegate) {
            this.delegate = (ListCellRenderer<Object>) delegate;
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component component = delegate == null
                    ? super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                    : delegate.getListCellRendererComponent((JList) list, value, index, isSelected, cellHasFocus);
            JLabel label = component instanceof JLabel ? (JLabel) component : this;
            label.setOpaque(true);
            label.setFont(FONT);
            label.setForeground(TEXT);
            label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            label.setBackground(isSelected ? PRIMARY_LIGHT : SURFACE);
            if (isSelected) {
                label.setForeground(PRIMARY_DARK);
            }
            label.setText(value == null ? "" : value.toString());
            return label;
        }
    }

    private static class ModernComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            return new ComboArrowButton();
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hasFocus ? new Color(251, 255, 252) : SURFACE);
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 14, 14);
            g2.dispose();
        }
    }

    private static class ComboArrowButton extends JButton {
        private boolean hover;

        ComboArrowButton() {
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setIcon(new ArrowIcon(Color.WHITE));
            setDisabledIcon(new ArrowIcon(new Color(82, 104, 90)));
            setPreferredSize(new Dimension(42, 34));
            setMinimumSize(new Dimension(42, 30));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color background = isEnabled()
                    ? (hover ? PRIMARY_DARK : PRIMARY)
                    : new Color(184, 202, 189);
            Color arrowColor = isEnabled() ? Color.WHITE : new Color(82, 104, 90);
            g2.setColor(background);
            g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 12, 12);
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2 + 1;
            int halfWidth = 7;
            int halfHeight = 4;
            g2.setColor(arrowColor);
            g2.fillPolygon(
                    new int[]{centerX - halfWidth, centerX + halfWidth, centerX},
                    new int[]{centerY - halfHeight, centerY - halfHeight, centerY + halfHeight},
                    3
            );
            g2.setColor(new Color(255, 255, 255, isEnabled() ? 95 : 40));
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(centerX - halfWidth, centerY - halfHeight, centerX, centerY + halfHeight);
            g2.drawLine(centerX + halfWidth, centerY - halfHeight, centerX, centerY + halfHeight);
            g2.dispose();
        }
    }

    private static class ArrowIcon implements Icon {
        private final Color color;

        ArrowIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            Path2D path = new Path2D.Double();
            path.moveTo(x + 3, y + 5);
            path.lineTo(x + 9, y + 11);
            path.lineTo(x + 15, y + 5);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(path);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }

    private static class ChoiceIcon implements Icon {
        private final boolean radio;
        private final boolean selected;
        private final boolean hover;

        ChoiceIcon(boolean radio, boolean selected, boolean hover) {
            this.radio = radio;
            this.selected = selected;
            this.hover = hover;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color border = hover ? PRIMARY_DARK : BORDER;
            Color fill = selected ? PRIMARY : SURFACE;
            g2.setColor(fill);
            if (radio) {
                g2.fillOval(x + 2, y + 2, 16, 16);
            } else {
                g2.fillRoundRect(x + 2, y + 2, 16, 16, 5, 5);
            }
            g2.setColor(border);
            g2.setStroke(new BasicStroke(2f));
            if (radio) {
                g2.drawOval(x + 2, y + 2, 16, 16);
            } else {
                g2.drawRoundRect(x + 2, y + 2, 16, 16, 5, 5);
            }
            if (selected) {
                g2.setColor(Color.WHITE);
                if (radio) {
                    g2.fillOval(x + 7, y + 7, 6, 6);
                } else {
                    g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 6, y + 10, x + 9, y + 14);
                    g2.drawLine(x + 9, y + 14, x + 15, y + 6);
                }
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 22;
        }

        @Override
        public int getIconHeight() {
            return 22;
        }
    }

    private static void styleScroll(JScrollPane scroll) {
        scroll.setBorder(new RoundedBorder(BORDER, 16, 1, 0, 0));
        scroll.getViewport().setBackground(SURFACE);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        scroll.getVerticalScrollBar().setBlockIncrement(42);
        scroll.getHorizontalScrollBar().setUnitIncrement(10);
        scroll.getHorizontalScrollBar().setBlockIncrement(42);
    }

    private static void styleMenuBar(JMenuBar bar) {
        bar.setBackground(PRIMARY_DARK);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    private static void styleTabbedPane(JTabbedPane tabs) {
        tabs.setFont(FONT_BOLD);
        tabs.setForeground(TEXT);
        tabs.setBackground(BACKGROUND);
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private static void styleToggle(AbstractButton button) {
        if (button.getClientProperty("modernToggleStyled") != null) {
            return;
        }
        button.putClientProperty("modernToggleStyled", Boolean.TRUE);
        button.setFont(adjustFont(button.getFont()));
        button.setForeground(TEXT);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boolean radio = button instanceof JRadioButton;
        button.setIcon(new ChoiceIcon(radio, false, false));
        button.setSelectedIcon(new ChoiceIcon(radio, true, false));
        button.setRolloverIcon(new ChoiceIcon(radio, false, true));
        button.setRolloverSelectedIcon(new ChoiceIcon(radio, true, true));
        button.setIconTextGap(8);
        button.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 10));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(PRIMARY_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT);
            }
        });
    }

    private static void styleButton(final JButton button) {
        if (Boolean.TRUE.equals(button.getClientProperty("linkButton"))) {
            styleLinkButton(button);
            return;
        }
        if (button.getClientProperty("modernStyled") != null) {
            restoreButtonVisual(button);
            return;
        }
        button.putClientProperty("modernStyled", Boolean.TRUE);
        String text = button.getText() == null ? "" : button.getText();
        boolean danger = isDanger(text) || DANGER.equals(button.getBackground()) || new Color(220, 53, 69).equals(button.getBackground());
        Color base = danger ? DANGER : PRIMARY;
        Color hover = danger ? DANGER_DARK : PRIMARY_DARK;
        Color pressed = danger ? new Color(137, 38, 45) : new Color(28, 96, 45);

        button.setFont(adjustFont(button.getFont()).deriveFont(Font.BOLD));
        applyButtonPaint(button, base);
        button.setIcon(iconForText(text, danger));
        button.setIconTextGap(8);
        button.setBorder(new RoundedBorder(base, 16, 1, 10, 16));
        enforceButtonSize(button, text);
        if (button.getToolTipText() == null || button.getToolTipText().trim().isEmpty()) {
            button.setToolTipText(tooltipFor(text));
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                    button.setBorder(new RoundedBorder(hover, 16, 1, 10, 16));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(base);
                button.setBorder(new RoundedBorder(base, 16, 1, 10, 16));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(pressed);
                    button.setBorder(new RoundedBorder(pressed, 16, 1, 11, 16));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(button.contains(e.getPoint()) ? hover : base);
                }
            }
        });
    }

    private static void styleLinkButton(final JButton button) {
        if (button.getClientProperty("modernLinkStyled") != null) {
            return;
        }
        button.putClientProperty("modernLinkStyled", Boolean.TRUE);
        String text = button.getText() == null ? "" : button.getText().replaceAll("<[^>]*>", "");
        button.setText("<html><u>" + text + "</u></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(PRIMARY_DARK);
        button.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIcon(null);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(23, 98, 43));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(PRIMARY_DARK);
            }
        });
    }

    private static void restoreButtonVisual(JButton button) {
        String text = button.getText() == null ? "" : button.getText();
        boolean danger = isDanger(text) || DANGER.equals(button.getBackground()) || DANGER_DARK.equals(button.getBackground())
                || new Color(220, 53, 69).equals(button.getBackground());
        Color base = danger ? DANGER : PRIMARY;
        applyButtonPaint(button, base);
        button.setBorder(new RoundedBorder(base, 16, 1, 10, 16));
        if (button.getIcon() == null) {
            button.setIcon(iconForText(text, danger));
        }
        button.revalidate();
        button.repaint();
    }

    private static void applyButtonPaint(JButton button, Color base) {
        button.setForeground(Color.WHITE);
        button.setBackground(base);
        button.setFocusPainted(false);
        button.setUI(new RoundedButtonUI());
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void enforceButtonSize(JButton button, String text) {
        FontMetrics metrics = button.getFontMetrics(button.getFont());
        int iconWidth = button.getIcon() == null ? 0 : button.getIcon().getIconWidth() + button.getIconTextGap();
        int textWidth = metrics.stringWidth(text == null ? "" : text);
        int minWidth = Math.max(112, textWidth + iconWidth + 44);
        int minHeight = Math.max(42, metrics.getHeight() + 22);
        Dimension preferred = button.getPreferredSize();
        Dimension fixed = new Dimension(Math.max(preferred.width, minWidth), Math.max(preferred.height, minHeight));
        button.setMinimumSize(fixed);
        button.setPreferredSize(fixed);
        Dimension maximum = button.getMaximumSize();
        if (maximum != null && maximum.width < Integer.MAX_VALUE) {
            button.setMaximumSize(new Dimension(Math.max(maximum.width, fixed.width), Math.max(maximum.height, fixed.height)));
        } else {
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(maximum == null ? 0 : maximum.height, fixed.height)));
        }
    }

    private static Font adjustFont(Font original) {
        if (original == null) {
            return FONT;
        }
        return new Font("Segoe UI", original.getStyle(), Math.max(original.getSize(), 12));
    }

    private static boolean isDanger(String text) {
        String normalized = text.toLowerCase(Locale.ROOT);
        return normalized.contains("excluir") || normalized.contains("remover")
                || normalized.contains("sair") || normalized.contains("voltar");
    }

    private static String tooltipFor(String text) {
        String normalized = text.toLowerCase(Locale.ROOT);
        if (normalized.contains("salvar")) return "Salvar informações";
        if (normalized.contains("editar")) return "Editar registro selecionado";
        if (normalized.contains("excluir") || normalized.contains("remover")) return "Excluir registro selecionado";
        if (normalized.contains("pesquisar") || normalized.contains("buscar")) return "Pesquisar registros";
        if (normalized.contains("relat")) return "Abrir relatórios";
        if (normalized.contains("cliente")) return "Abrir cadastro de clientes";
        if (normalized.contains("fornecedor")) return "Abrir cadastro de fornecedores";
        if (normalized.contains("produto")) return "Abrir cadastro de produtos";
        if (normalized.contains("usu")) return "Abrir cadastro de usuários";
        if (normalized.contains("config")) return "Abrir configurações";
        if (normalized.contains("sair")) return "Sair do sistema";
        if (normalized.contains("voltar")) return "Voltar ao dashboard";
        if (normalized.contains("entrar")) return "Entrar no sistema";
        return text;
    }

    private static Icon iconForText(String text, boolean danger) {
        String normalized = text.toLowerCase(Locale.ROOT);
        Color color = Color.WHITE;
        if (normalized.contains("salvar")) return new ActionIcon("save", color);
        if (normalized.contains("editar")) return new ActionIcon("edit", color);
        if (normalized.contains("excluir") || normalized.contains("remover")) return new ActionIcon("trash", color);
        if (normalized.contains("pesquisar") || normalized.contains("buscar")) return new ActionIcon("search", color);
        if (normalized.contains("relat")) return new ActionIcon("chart", color);
        if (normalized.contains("cliente") || normalized.contains("usu")) return new ActionIcon("user", color);
        if (normalized.contains("senha")) return new ActionIcon("lock", color);
        if (normalized.contains("fornecedor")) return new ActionIcon("building", color);
        if (normalized.contains("produto")) return new ActionIcon("box", color);
        if (normalized.contains("registrar") || normalized.contains("venda")) return new ActionIcon("cart", color);
        if (normalized.contains("config")) return new ActionIcon("gear", color);
        if (normalized.contains("sair")) return new ActionIcon("door", color);
        if (normalized.contains("voltar")) return new ActionIcon("back", color);
        if (normalized.contains("limpar") || normalized.contains("cancelar")) return new ActionIcon("clean", color);
        if (normalized.contains("novo") || normalized.contains("adicionar")) return new ActionIcon("plus", color);
        if (normalized.contains("entrar")) return new ActionIcon("door", color);
        return null;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final int thickness;
        private final int vertical;
        private final int horizontal;

        RoundedBorder(Color color, int radius, int thickness, int vertical, int horizontal) {
            this.color = color;
            this.radius = radius;
            this.thickness = thickness;
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(vertical, horizontal, vertical, horizontal);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = vertical;
            insets.left = horizontal;
            insets.bottom = vertical;
            insets.right = horizontal;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
    }

    private static class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(button.isEnabled() ? button.getBackground() : new Color(178, 194, 183));
            g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 16, 16);
            g2.dispose();
            super.paint(g, c);
        }
    }

    private static class ShadowBorder extends AbstractBorder {
        private final int radius;
        private final Insets insets;

        ShadowBorder(int radius, Insets insets) {
            this.radius = radius;
            this.insets = insets;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return insets;
        }

        @Override
        public Insets getBorderInsets(Component c, Insets values) {
            values.top = insets.top;
            values.left = insets.left;
            values.bottom = insets.bottom;
            values.right = insets.right;
            return values;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int shadow = 6;
            g2.setColor(new Color(40, 90, 55, 22));
            g2.fillRoundRect(x + shadow, y + shadow, width - shadow - 1, height - shadow - 1, radius, radius);
            g2.setColor(c.getBackground() == null ? SURFACE : c.getBackground());
            g2.fillRoundRect(x, y, width - shadow - 1, height - shadow - 1, radius, radius);
            g2.setColor(BORDER);
            g2.drawRoundRect(x, y, width - shadow - 1, height - shadow - 1, radius, radius);
            g2.dispose();
        }
    }

    private static class ActionIcon implements Icon {
        private final String type;
        private final Color color;
        private final int size;

        ActionIcon(String type, Color color) {
            this.type = type;
            this.color = color;
            this.size = 18;
        }

        ActionIcon withColor(Color newColor) {
            return new ActionIcon(type, newColor);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(color);
            int s = size;

            if ("save".equals(type)) {
                g2.drawRoundRect(x + 3, y + 2, s - 6, s - 4, 3, 3);
                g2.drawLine(x + 6, y + 2, x + 6, y + 7);
                g2.drawLine(x + 11, y + 2, x + 11, y + 7);
                g2.drawRect(x + 6, y + 11, s - 12, 5);
            } else if ("edit".equals(type)) {
                g2.drawLine(x + 4, y + 14, x + 13, y + 5);
                g2.drawLine(x + 11, y + 3, x + 15, y + 7);
                g2.drawLine(x + 3, y + 15, x + 7, y + 14);
            } else if ("trash".equals(type)) {
                g2.drawLine(x + 4, y + 5, x + 14, y + 5);
                g2.drawLine(x + 7, y + 3, x + 11, y + 3);
                g2.drawRoundRect(x + 5, y + 7, 8, 8, 2, 2);
                g2.drawLine(x + 8, y + 9, x + 8, y + 14);
                g2.drawLine(x + 11, y + 9, x + 11, y + 14);
            } else if ("search".equals(type)) {
                g2.drawOval(x + 3, y + 3, 9, 9);
                g2.drawLine(x + 11, y + 11, x + 15, y + 15);
            } else if ("chart".equals(type)) {
                g2.drawLine(x + 3, y + 15, x + 15, y + 15);
                g2.fillRoundRect(x + 4, y + 9, 2, 6, 2, 2);
                g2.fillRoundRect(x + 8, y + 5, 2, 10, 2, 2);
                g2.fillRoundRect(x + 12, y + 7, 2, 8, 2, 2);
            } else if ("user".equals(type)) {
                g2.drawOval(x + 6, y + 3, 6, 6);
                g2.drawArc(x + 3, y + 10, 12, 8, 0, 180);
            } else if ("lock".equals(type)) {
                g2.drawRoundRect(x + 4, y + 8, 10, 8, 3, 3);
                g2.drawArc(x + 6, y + 3, 6, 8, 0, 180);
            } else if ("building".equals(type)) {
                g2.drawRect(x + 4, y + 4, 10, 12);
                g2.drawLine(x + 7, y + 7, x + 7, y + 7);
                g2.drawLine(x + 11, y + 7, x + 11, y + 7);
                g2.drawLine(x + 7, y + 11, x + 7, y + 11);
                g2.drawLine(x + 11, y + 11, x + 11, y + 11);
            } else if ("gear".equals(type)) {
                g2.drawOval(x + 5, y + 5, 8, 8);
                g2.drawLine(x + 9, y + 2, x + 9, y + 4);
                g2.drawLine(x + 9, y + 14, x + 9, y + 16);
                g2.drawLine(x + 2, y + 9, x + 4, y + 9);
                g2.drawLine(x + 14, y + 9, x + 16, y + 9);
            } else if ("door".equals(type)) {
                g2.drawRect(x + 5, y + 3, 8, 12);
                g2.drawLine(x + 13, y + 15, x + 15, y + 15);
                g2.fillOval(x + 10, y + 9, 2, 2);
            } else if ("back".equals(type)) {
                g2.drawLine(x + 5, y + 9, x + 14, y + 9);
                g2.drawLine(x + 5, y + 9, x + 9, y + 5);
                g2.drawLine(x + 5, y + 9, x + 9, y + 13);
            } else if ("plus".equals(type)) {
                g2.drawLine(x + 9, y + 4, x + 9, y + 14);
                g2.drawLine(x + 4, y + 9, x + 14, y + 9);
            } else if ("clean".equals(type)) {
                g2.drawRoundRect(x + 4, y + 5, 10, 8, 3, 3);
                g2.drawLine(x + 6, y + 13, x + 13, y + 13);
            } else if ("cart".equals(type)) {
                g2.drawPolyline(new int[]{x + 3, x + 5, x + 14}, new int[]{y + 4, y + 12, y + 12}, 3);
                g2.drawOval(x + 6, y + 14, 2, 2);
                g2.drawOval(x + 12, y + 14, 2, 2);
            } else if ("box".equals(type)) {
                Path2D box = new Path2D.Double();
                box.moveTo(x + 9, y + 3);
                box.lineTo(x + 15, y + 6);
                box.lineTo(x + 15, y + 13);
                box.lineTo(x + 9, y + 16);
                box.lineTo(x + 3, y + 13);
                box.lineTo(x + 3, y + 6);
                box.closePath();
                g2.draw(box);
                g2.drawLine(x + 3, y + 6, x + 9, y + 9);
                g2.drawLine(x + 15, y + 6, x + 9, y + 9);
                g2.drawLine(x + 9, y + 9, x + 9, y + 16);
            } else if ("warning".equals(type)) {
                g2.drawPolygon(new int[]{x + 9, x + 16, x + 2}, new int[]{y + 2, y + 15, y + 15}, 3);
                g2.drawLine(x + 9, y + 7, x + 9, y + 11);
                g2.drawLine(x + 9, y + 14, x + 9, y + 14);
            } else if ("error".equals(type)) {
                g2.drawOval(x + 3, y + 3, 12, 12);
                g2.drawLine(x + 6, y + 6, x + 12, y + 12);
                g2.drawLine(x + 12, y + 6, x + 6, y + 12);
            } else {
                g2.drawOval(x + 3, y + 3, 12, 12);
                g2.drawLine(x + 9, y + 8, x + 9, y + 13);
                g2.drawLine(x + 9, y + 5, x + 9, y + 5);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}

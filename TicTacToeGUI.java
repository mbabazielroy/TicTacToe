import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TicTacToeGUI implements ActionListener {
    private JButton[][] buttons;
    private JFrame frame;
    private TicTacToe game;
    private JLabel statusLabel;
    private JLabel toastLabel;
    private JLabel statsLabel;
    private JLabel headerTitleLabel;
    private JLabel heroBadgeLabel;
    private JLabel turnBadge;
    private JLabel modeBadge;
    private JLabel difficultyBadge;
    private JLabel streakBadge;
    private JLabel xScoreBadge;
    private JLabel oScoreBadge;
    private JLabel drawScoreBadge;
    private JLabel moveTimerLabel;
    private JLabel lastMoveLabel;
    private JLabel hintReasonLabel;
    private JButton resetButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton autoReplayButton;
    private JButton stepBackButton;
    private JButton stepForwardButton;
    private JButton exitReplayButton;
    private JButton exportButton;
    private JButton importButton;
    private JButton copyClipboardButton;
    private JButton pasteClipboardButton;
    private JButton clearStatsButton;
    private JButton summaryButton;
    private JToggleButton advancedToggleButton;
    private JCheckBox computerCheckBox;
    private JCheckBox highContrastCheckBox;
    private JCheckBox compactModeCheckBox;
    private JCheckBox largeControlCheckBox;
    private JCheckBox colorblindCheckBox;
    private JCheckBox demoCheckBox;
    private JCheckBox hintCheckBox;
    private JCheckBox soundCheckBox;
    private JCheckBox countdownCheckBox;
    private JSpinner countdownSpinner;
    private JSpinner seriesSpinner;
    private JButton seriesResetButton;
    private JCheckBox dyslexiaFontCheckBox;
    private JTextField xNameField;
    private JTextField oNameField;
    private JComboBox<String> themeSelector;
    private JComboBox<String> densitySelector;
    private JCheckBox highVisibilityFocusCheckBox;
    private JSlider fontSizeSlider;
    private JComboBox<String> starterSelector;
    private JComboBox<String> difficultySelector;
    private DefaultListModel<String> historyModel;
    private JList<String> historyList;
    private JSlider replaySlider;
    private JProgressBar thinkingBar;
    private char computerSymbol = 'O';
    private char startingPlayer = 'X';
    private Color defaultButtonColor;
    private Color hoverColor = new Color(230, 230, 230);
    private Color pressColor = new Color(210, 210, 210);
    private final Color hintColor = new Color(200, 255, 200);
    private boolean replaying = false;
    private boolean showReplayNumbers = false;
    private boolean aiThinking = false;
    private boolean demoMode = false;
    private boolean showHints = false;
    private boolean settingsWriteWarned = false;
    private List<TicTacToe.Move> replayBuffer = new ArrayList<>();
    private int replayIndex = 0;
    private int focusedRow = 0;
    private int focusedCol = 0;
    private int xWins = 0, oWins = 0, draws = 0;
    private int currentStreak = 0;
    private char streakPlayer = '-';
    private int longestStreak = 0;
    private char longestStreakPlayer = '-';
    private int seriesLength = 5;
    private int seriesX = 0;
    private int seriesO = 0;
    private boolean sliderAdjusting = false;
    private final File settingsFile = new File(System.getProperty("user.home"), ".tictactoe.properties");
    private Timer toastTimer;
    private final List<JPanel> cards = new ArrayList<>();
    private final Color accentBlue = new Color(0, 120, 215);
    private final Color accentBlueSoft = new Color(230, 242, 255);
    private final Color surfaceColor = new Color(245, 247, 250);
    private final Color cardBorderColor = new Color(218, 226, 235);
    private final Color textPrimary = new Color(32, 36, 42);
    private final Color successGreen = new Color(46, 204, 113);
    private final Color warningAmber = new Color(255, 193, 7);
    private final Font titleFont = new Font("Segoe UI Semibold", Font.BOLD, 22);
    private final Font subtitleFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font controlFont = new Font("Segoe UI", Font.PLAIN, 13);
    private JPanel headerPanel;
    private GradientPanel backgroundPanel;
    private boolean soundEnabled = true;
    private boolean colorblindMode = false;
    private boolean largeControls = false;
    private long gameStartTime = System.currentTimeMillis();
    private long moveStartTime = System.currentTimeMillis();
    private long fastestWinMs = Long.MAX_VALUE;
    private Timer moveTimer;
    private int moveSecondsElapsed = 0;
    private boolean countdownEnabled = false;
    private int countdownSeconds = 10;
    private char timerPlayer = 'X';
    private int lastMoveRow = -1;
    private int lastMoveCol = -1;
    private int baseFontSize = 13;

    public TicTacToeGUI() {
        game = new TicTacToe();
        frame = new JFrame("Tic-Tac-Toe");
        backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        JPanel topPanel = buildTopPanel();
        JPanel boardPanel = createCard("Game Board", buildBoardPanel());
        JPanel sidePanel = buildSidePanel();
        JPanel bottomPanel = buildBottomPanel();

        JPanel centerWrapper = new JPanel(new BorderLayout(12, 0));
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerWrapper.setOpaque(false);
        centerWrapper.add(boardPanel, BorderLayout.CENTER);
        centerWrapper.add(sidePanel, BorderLayout.EAST);
        centerWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new javax.swing.BoxLayout(scrollContent, javax.swing.BoxLayout.Y_AXIS));
        scrollContent.add(topPanel);
        scrollContent.add(javax.swing.Box.createVerticalStrut(16));
        scrollContent.add(centerWrapper);
        scrollContent.add(javax.swing.Box.createVerticalStrut(16));

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(1020, 640);
        frame.setMinimumSize(new Dimension(900, 560));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameStartTime = System.currentTimeMillis();
        loadSettings();
        applyTheme();
        applyDensitySpacing();
        applyCompactMode(compactModeCheckBox.isSelected());
        applyFontChoice();
        updateStatus();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        focusButton(0, 0);
        if (demoCheckBox.isSelected()) {
            toggleDemoMode();
        } else {
            maybeAutoComputerMove();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (aiThinking || replaying) return;
        JButton clickedButton = (JButton) e.getSource();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    handleMove(i, j);
                    focusRowCol(i, j);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        setLookAndFeel();
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }

    private JPanel buildBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        boardPanel.setOpaque(true);
        boardPanel.setBackground(new Color(238, 243, 250));
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(readableFont(44));
                buttons[i][j].setMargin(new Insets(10, 10, 10, 10));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBorder(new LineBorder(new Color(205, 213, 223), 2, true));
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBackground(new Color(246, 249, 253));
                buttons[i][j].setOpaque(true);
                buttons[i][j].setToolTipText("Row " + (i + 1) + ", Col " + (j + 1) + " (Enter to place)");
                installButtonEffects(buttons[i][j]);
                boardPanel.add(buttons[i][j]);
            }
        }
        defaultButtonColor = buttons[0][0].getBackground();
        setupKeyBindings(boardPanel);

        JPanel headerRow = new JPanel(new GridLayout(1, 0, 8, 0));
        headerRow.setOpaque(false);
        turnBadge = pillLabel("Turn: X", accentBlueSoft, accentBlue, textPrimary);
        modeBadge = pillLabel("Mode: Local", new Color(235, 244, 255), new Color(0, 93, 166), textPrimary);
        difficultyBadge = pillLabel("AI: Heuristic", new Color(230, 242, 255), accentBlue, textPrimary);
        streakBadge = pillLabel("Streak: none", new Color(243, 243, 243), new Color(90, 90, 90), textPrimary);
        headerRow.add(turnBadge);
        headerRow.add(modeBadge);
        headerRow.add(difficultyBadge);
        headerRow.add(streakBadge);
        moveTimerLabel = pillLabel("Move: 00s", new Color(243, 243, 243), new Color(120, 120, 120), textPrimary);
        headerRow.add(moveTimerLabel);
        lastMoveLabel = pillLabel("Last: --", new Color(243, 243, 243), new Color(160, 160, 160), textPrimary);
        headerRow.add(lastMoveLabel);

        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(520, 520));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(headerRow, BorderLayout.NORTH);
        wrapper.add(boardPanel, BorderLayout.CENTER);
        return wrapper;
    }
    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.setOpaque(false);
        statusLabel = new JLabel("Ready");
        statusLabel.getAccessibleContext().setAccessibleName("Game status");

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        headerPanel.setBackground(accentBlue);
        headerTitleLabel = new JLabel("Tic-Tac-Toe");
        headerTitleLabel.setFont(titleFont);
        headerTitleLabel.setForeground(Color.WHITE);
        statusLabel.setFont(subtitleFont);
        statusLabel.setForeground(new Color(235, 245, 255));
        heroBadgeLabel = new JLabel("Windows look");
        heroBadgeLabel.setOpaque(true);
        heroBadgeLabel.setBackground(new Color(255, 255, 255, 40));
        heroBadgeLabel.setForeground(Color.WHITE);
        heroBadgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        heroBadgeLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        headerText.add(headerTitleLabel);
        headerText.add(statusLabel);
        headerPanel.add(headerText, BorderLayout.WEST);
        headerPanel.add(heroBadgeLabel, BorderLayout.EAST);

        resetButton = new JButton("New Game");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        autoReplayButton = new JButton("Auto Replay");
        stepBackButton = new JButton("Step Back");
        stepForwardButton = new JButton("Step Forward");
        exitReplayButton = new JButton("Exit Replay");
        exportButton = new JButton("Export");
        importButton = new JButton("Import");
        copyClipboardButton = new JButton("Copy Game");
        pasteClipboardButton = new JButton("Paste Game");
        clearStatsButton = new JButton("Clear Stats");
        summaryButton = new JButton("Summary");
        computerCheckBox = new JCheckBox("Play vs Computer (O)");
        highContrastCheckBox = new JCheckBox("High Contrast");
        compactModeCheckBox = new JCheckBox("Compact Mode");
        largeControlCheckBox = new JCheckBox("Large Controls");
        colorblindCheckBox = new JCheckBox("Colorblind Palette");
        demoCheckBox = new JCheckBox("Demo (AI vs AI)");
        hintCheckBox = new JCheckBox("Hint (best move)");
        soundCheckBox = new JCheckBox("Sound On");
        countdownCheckBox = new JCheckBox("Countdown");
        dyslexiaFontCheckBox = new JCheckBox("Readable Font");
        soundCheckBox.setSelected(true);
        advancedToggleButton = new JToggleButton("More Controls");
        densitySelector = new JComboBox<>(new String[]{"Comfortable", "Dense"});
        highVisibilityFocusCheckBox = new JCheckBox("High-visibility Focus");
        fontSizeSlider = new JSlider(11, 18, baseFontSize);
        xNameField = new JTextField("Player X", 10);
        oNameField = new JTextField("Player O", 10);
        themeSelector = new JComboBox<>(new String[]{"Classic", "Neon", "Pastel"});
        countdownSpinner = new JSpinner(new SpinnerNumberModel(10, 3, 60, 1));
        seriesSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 15, 2));
        seriesResetButton = new JButton("Reset Series");
        starterSelector = new JComboBox<>(new String[]{"X", "O"});
        difficultySelector = new JComboBox<>(new String[]{"Random", "Heuristic", "Perfect"});
        thinkingBar = new JProgressBar();
        thinkingBar.setIndeterminate(true);
        thinkingBar.setVisible(false);

        resetButton.setMnemonic('N');
        undoButton.setMnemonic('U');
        redoButton.setMnemonic('R');
        autoReplayButton.setMnemonic('A');
        stepBackButton.setMnemonic('B');
        stepForwardButton.setMnemonic('F');
        exitReplayButton.setMnemonic('E');
        exportButton.setMnemonic('X');
        importButton.setMnemonic('I');
        copyClipboardButton.setMnemonic('C');
        pasteClipboardButton.setMnemonic('P');
        clearStatsButton.setMnemonic('C');
        summaryButton.setMnemonic('S');

        resetButton.setToolTipText("Start a fresh game");
        undoButton.setToolTipText("Undo last move");
        redoButton.setToolTipText("Redo last undone move");
        autoReplayButton.setToolTipText("Replay the current game automatically");
        stepBackButton.setToolTipText("Step backward through the game");
        stepForwardButton.setToolTipText("Step forward through the game");
        exitReplayButton.setToolTipText("Return to the live game state");
        computerCheckBox.setToolTipText("Toggle computer opponent");
        difficultySelector.setToolTipText("Computer difficulty");
        highContrastCheckBox.setToolTipText("High contrast theme");
        compactModeCheckBox.setToolTipText("Compact/mobile-friendly layout");
        largeControlCheckBox.setToolTipText("Larger buttons and fonts");
        colorblindCheckBox.setToolTipText("Use colorblind-safe palette");
        soundCheckBox.setToolTipText("Toggle sound effects");
        countdownCheckBox.setToolTipText("Enable per-move countdown");
        countdownSpinner.setToolTipText("Seconds before auto-move when countdown is on");
        seriesSpinner.setToolTipText("Series length (best-of)");
        seriesResetButton.setToolTipText("Reset series counters");
        themeSelector.setToolTipText("Visual theme");
        densitySelector.setToolTipText("Layout density");
        fontSizeSlider.setToolTipText("UI font size");
        xNameField.setToolTipText("Name for player X");
        oNameField.setToolTipText("Name for player O (Computer when enabled)");
        exportButton.setToolTipText("Export current game to file");
        importButton.setToolTipText("Import game from file");
        copyClipboardButton.setToolTipText("Copy game state to clipboard");
        pasteClipboardButton.setToolTipText("Paste game state from clipboard");
        clearStatsButton.setToolTipText("Reset session stats");
        summaryButton.setToolTipText("Show session summary");
        demoCheckBox.setToolTipText("Watch AI play both sides");
        hintCheckBox.setToolTipText("Highlight best move for you");
        dyslexiaFontCheckBox.setToolTipText("Switch to a more readable font");

        styleControls();

        resetButton.addActionListener(e -> resetGame());
        undoButton.addActionListener(e -> {
            if (game.undo()) {
                refreshBoardFromModel(false);
                setBoardButtonsEnabled(true);
                clearHighlights();
                saveSettings();
            }
        });
        redoButton.addActionListener(e -> {
            TicTacToe.MoveResult result = game.redo();
            if (result != TicTacToe.MoveResult.INVALID) {
                refreshBoardFromModel(false);
                if (result == TicTacToe.MoveResult.WIN || result == TicTacToe.MoveResult.DRAW) {
                    handleGameOverIfNeeded(result);
                }
                saveSettings();
            }
        });
        autoReplayButton.addActionListener(e -> startAutoReplay());
        stepBackButton.addActionListener(e -> stepReplay(-1));
        stepForwardButton.addActionListener(e -> stepReplay(1));
        exitReplayButton.addActionListener(e -> exitReplayMode(true));
        exportButton.addActionListener(e -> exportGame());
        importButton.addActionListener(e -> importGame());
        copyClipboardButton.addActionListener(e -> copyGameToClipboard());
        pasteClipboardButton.addActionListener(e -> pasteGameFromClipboard());
        clearStatsButton.addActionListener(e -> clearStats());
        summaryButton.addActionListener(e -> showSummaryDialog());
        demoCheckBox.addActionListener(e -> toggleDemoMode());
        hintCheckBox.addActionListener(e -> {
            showHints = hintCheckBox.isSelected();
            updateHintHighlight();
        });
        soundCheckBox.addActionListener(e -> {
            soundEnabled = soundCheckBox.isSelected();
            saveSettings();
        });
        dyslexiaFontCheckBox.addActionListener(e -> {
            applyFontChoice();
            saveSettings();
        });
        computerCheckBox.addActionListener(e -> {
            maybeAutoComputerMove();
            saveSettings();
        });
        starterSelector.addActionListener(e -> saveSettings());
        difficultySelector.addActionListener(e -> saveSettings());
        highContrastCheckBox.addActionListener(e -> {
            applyTheme();
            saveSettings();
        });
        themeSelector.addActionListener(e -> {
            applyTheme();
            saveSettings();
        });
        densitySelector.addActionListener(e -> {
            applyDensitySpacing();
            saveSettings();
        });
        compactModeCheckBox.addActionListener(e -> {
            applyCompactMode(compactModeCheckBox.isSelected());
            applyFontChoice();
            saveSettings();
        });
        largeControlCheckBox.addActionListener(e -> {
            largeControls = largeControlCheckBox.isSelected();
            applyCompactMode(compactModeCheckBox.isSelected());
            applyFontChoice();
            saveSettings();
        });
        colorblindCheckBox.addActionListener(e -> {
            colorblindMode = colorblindCheckBox.isSelected();
            refreshBoardFromModel(false);
            applyTheme();
            saveSettings();
        });
        countdownCheckBox.addActionListener(e -> {
            countdownEnabled = countdownCheckBox.isSelected();
            restartMoveTimer();
            saveSettings();
        });
        countdownSpinner.addChangeListener(e -> {
            countdownSeconds = (int) ((JSpinner) e.getSource()).getValue();
            restartMoveTimer();
            saveSettings();
        });
        seriesSpinner.addChangeListener(e -> {
            seriesLength = (int) ((JSpinner) e.getSource()).getValue();
            saveSettings();
        });
        seriesResetButton.addActionListener(e -> resetSeries());
        highVisibilityFocusCheckBox.addActionListener(e -> saveSettings());
        fontSizeSlider.addChangeListener(e -> {
            baseFontSize = fontSizeSlider.getValue();
            applyFontChoice();
            saveSettings();
        });
        xNameField.addActionListener(e -> saveSettings());
        oNameField.addActionListener(e -> saveSettings());

        JPanel toolbar = new JPanel();
        toolbar.setOpaque(false);
        toolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 4));
        setIconLabel(resetButton, "\u23EE", "New Game");
        setIconLabel(undoButton, "\u2B8C", "Undo");
        setIconLabel(redoButton, "\u2B8E", "Redo");
        setIconLabel(exportButton, "\u21E3", "Export");
        setIconLabel(importButton, "\u21E1", "Import");
        setIconLabel(clearStatsButton, "\u267B", "Clear Stats");
        setIconLabel(summaryButton, "\u2139", "Summary");
        toolbar.add(resetButton);
        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.add(exportButton);
        toolbar.add(importButton);
        toolbar.add(clearStatsButton);
        toolbar.add(summaryButton);
        JPanel toolbarCard = createCard("Quick Actions", toolbar);
        toolbarCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 220, 235), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)));

        JPanel actionsGrid = new JPanel(new GridLayout(0, 3, 8, 8));
        actionsGrid.setOpaque(false);
        actionsGrid.add(autoReplayButton);
        actionsGrid.add(stepBackButton);
        actionsGrid.add(stepForwardButton);
        actionsGrid.add(exitReplayButton);
        actionsGrid.add(copyClipboardButton);
        actionsGrid.add(pasteClipboardButton);
        actionsGrid.add(thinkingBar);
        JPanel gameCard = createCard("Replay & Sharing", actionsGrid);

        JPanel optionsGrid = new JPanel(new GridLayout(0, 2, 8, 8));
        optionsGrid.setOpaque(false);
        optionsGrid.add(labeledField("Starter", starterSelector));
        optionsGrid.add(labeledField("AI Strength", difficultySelector));
        optionsGrid.add(computerCheckBox);
        optionsGrid.add(demoCheckBox);
        optionsGrid.add(hintCheckBox);
        optionsGrid.add(highContrastCheckBox);
        optionsGrid.add(compactModeCheckBox);
        optionsGrid.add(dyslexiaFontCheckBox);
        optionsGrid.add(largeControlCheckBox);
        optionsGrid.add(colorblindCheckBox);
        optionsGrid.add(soundCheckBox);
        optionsGrid.add(highVisibilityFocusCheckBox);
        JPanel optionsCard = createCard("Assist & Display", optionsGrid);

        JPanel playersGrid = new JPanel(new GridLayout(0, 2, 8, 8));
        playersGrid.setOpaque(false);
        playersGrid.add(labeledField("Player X", xNameField));
        playersGrid.add(labeledField("Player O", oNameField));
        playersGrid.add(labeledField("Theme", themeSelector));
        playersGrid.add(labeledField("Countdown (s)", countdownSpinner));
        playersGrid.add(countdownCheckBox);
        playersGrid.add(new JLabel(" "));
        playersGrid.add(labeledField("Series Best-Of", seriesSpinner));
        playersGrid.add(seriesResetButton);
        playersGrid.add(labeledField("Density", densitySelector));
        JPanel fontRow = new JPanel(new BorderLayout(6, 0));
        fontRow.setOpaque(false);
        fontRow.add(new JLabel("Font Size"), BorderLayout.WEST);
        fontRow.add(fontSizeSlider, BorderLayout.CENTER);
        playersGrid.add(fontRow);
        JPanel playersCard = createCard("Players & Session", playersGrid);

        JPanel shortcuts = new JPanel(new GridLayout(2, 1));
        shortcuts.setOpaque(false);
        JLabel shortcutLine1 = new JLabel("Shortcuts: Arrow keys move • Enter/Space place");
        JLabel shortcutLine2 = new JLabel("Ctrl+Z undo • Ctrl+Y redo • ? help");
        shortcutLine1.setFont(subtitleFont);
        shortcutLine2.setFont(subtitleFont);
        shortcuts.add(shortcutLine1);
        shortcuts.add(shortcutLine2);
        JPanel shortcutCard = createCard("Keyboard", shortcuts);

        JPanel cardsRow = new JPanel(new GridLayout(1, 2, 12, 0));
        cardsRow.setOpaque(false);
        cardsRow.add(toolbarCard);
        JPanel advancedPanel = new JPanel(new BorderLayout());
        advancedPanel.setOpaque(false);
        advancedPanel.add(optionsCard, BorderLayout.CENTER);
        advancedPanel.setVisible(false);
        advancedToggleButton.addActionListener(e -> advancedPanel.setVisible(advancedToggleButton.isSelected()));
        JPanel advancedWrap = new JPanel(new BorderLayout());
        advancedWrap.setOpaque(false);
        advancedWrap.add(advancedToggleButton, BorderLayout.NORTH);
        advancedWrap.add(advancedPanel, BorderLayout.CENTER);
        cardsRow.add(advancedWrap);

        JPanel cardsRow2 = new JPanel(new GridLayout(1, 2, 12, 0));
        cardsRow2.setOpaque(false);
        cardsRow2.add(playersCard);
        cardsRow2.add(shortcutCard);

        JPanel cardsWrap = new JPanel(new GridLayout(2, 1, 0, 10));
        cardsWrap.setOpaque(false);
        cardsWrap.add(cardsRow);
        cardsWrap.add(cardsRow2);
        cardsWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardsWrap.getPreferredSize().height));

        top.add(headerPanel, BorderLayout.NORTH);
        top.add(cardsWrap, BorderLayout.CENTER);
        top.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, top.getPreferredSize().height));
        return top;
    }

    private JPanel buildSidePanel() {
        JPanel side = new JPanel(new BorderLayout(0, 12));
        side.setOpaque(false);
        side.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        side.setPreferredSize(new Dimension(300, 0));
        side.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.setMaximumSize(new Dimension(Integer.MAX_VALUE, side.getPreferredSize().height));

        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel historyCard = createCard("Move History", scrollPane);

        replaySlider = new JSlider(0, 0, 0);
        replaySlider.setEnabled(false);
        replaySlider.setToolTipText("Jump to a move");
        replaySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!replaying) return;
                if (sliderAdjusting) return;
                sliderAdjusting = true;
                int target = replaySlider.getValue();
                showReplayState(target);
                sliderAdjusting = false;
            }
        });

        statsLabel = new JLabel("Stats: X 0 | O 0 | Draw 0");
        statsLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel scoreboardRow = new JPanel(new GridLayout(1, 0, 6, 0));
        scoreboardRow.setOpaque(false);
        xScoreBadge = pillLabel("X Wins: 0", new Color(232, 246, 239), successGreen.darker(), textPrimary);
        oScoreBadge = pillLabel("O Wins: 0", new Color(230, 242, 255), new Color(25, 118, 210), textPrimary);
        drawScoreBadge = pillLabel("Draws: 0", new Color(255, 247, 234), warningAmber.darker(), textPrimary);
        scoreboardRow.add(xScoreBadge);
        scoreboardRow.add(oScoreBadge);
        scoreboardRow.add(drawScoreBadge);

        JPanel replayPanel = new JPanel(new BorderLayout(6, 6));
        replayPanel.setOpaque(false);
        replayPanel.add(scoreboardRow, BorderLayout.NORTH);
        replayPanel.add(replaySlider, BorderLayout.CENTER);
        replayPanel.add(statsLabel, BorderLayout.SOUTH);

        JPanel replayCard = createCard("Replay & Stats", replayPanel);

        side.add(historyCard, BorderLayout.CENTER);
        side.add(replayCard, BorderLayout.SOUTH);
        return side;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        toastLabel = new JLabel(" ");
        toastLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        bottom.add(toastLabel, BorderLayout.CENTER);
        hintReasonLabel = new JLabel(" ");
        hintReasonLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        bottom.add(hintReasonLabel, BorderLayout.EAST);
        return bottom;
    }

    private JPanel createCard(String title, JComponent content) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorderColor, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 12, 12)));
        card.setBackground(Color.WHITE);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(textPrimary);
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label, BorderLayout.WEST);

        // leave content opacity as-is so panels like the board and lists paint properly
        card.add(header, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        cards.add(card);
        return card;
    }

    private JLabel pillLabel(String text, Color bg, Color accent, Color fg) {
        JLabel pill = new JLabel(text);
        pill.setOpaque(true);
        pill.setBackground(bg);
        pill.setForeground(fg);
        pill.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        pill.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accent, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return pill;
    }

    private void styleControls() {
        JButton[] controlButtons = {resetButton, undoButton, redoButton, autoReplayButton, stepBackButton, stepForwardButton, exitReplayButton, exportButton, importButton, copyClipboardButton, pasteClipboardButton, clearStatsButton, summaryButton, seriesResetButton};
        for (JButton button : controlButtons) {
            button.setFont(controlFont);
            button.setFocusPainted(false);
            button.setBackground(new Color(247, 249, 252));
            button.setBorder(new LineBorder(cardBorderColor, 1, true));
        }
        JButton[] ribbonButtons = {resetButton, undoButton, redoButton, exportButton, importButton, clearStatsButton, summaryButton};
        for (JButton rb : ribbonButtons) {
            rb.setPreferredSize(new Dimension(96, 52));
            rb.setBackground(new Color(235, 242, 250));
            rb.setBorder(new LineBorder(new Color(206, 220, 235), 1, true));
        }
        JCheckBox[] toggles = {computerCheckBox, highContrastCheckBox, compactModeCheckBox, demoCheckBox, hintCheckBox, dyslexiaFontCheckBox, largeControlCheckBox, colorblindCheckBox, soundCheckBox, countdownCheckBox, highVisibilityFocusCheckBox};
        for (JCheckBox toggle : toggles) {
            toggle.setFont(controlFont);
            toggle.setOpaque(false);
            toggle.setFocusPainted(false);
            toggle.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }
        starterSelector.setFont(controlFont);
        difficultySelector.setFont(controlFont);
        starterSelector.setBackground(Color.WHITE);
        difficultySelector.setBackground(Color.WHITE);
        starterSelector.setBorder(new LineBorder(cardBorderColor, 1, true));
        difficultySelector.setBorder(new LineBorder(cardBorderColor, 1, true));
        themeSelector.setFont(controlFont);
        themeSelector.setBackground(Color.WHITE);
        themeSelector.setBorder(new LineBorder(cardBorderColor, 1, true));
        xNameField.setFont(controlFont);
        oNameField.setFont(controlFont);
        countdownSpinner.setFont(controlFont);
        seriesSpinner.setFont(controlFont);
        thinkingBar.setPreferredSize(new Dimension(140, 12));
        thinkingBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    private void setIconLabel(JButton button, String icon, String text) {
        String html = "<html><div style='text-align:center;padding:2px 6px;'>"
                + "<div style='font-size:16px;font-weight:bold;line-height:16px;'>" + icon + "</div>"
                + "<div style='font-size:10px;font-weight:600;'>" + text + "</div>"
                + "</div></html>";
        button.setText(html);
        button.setMargin(new Insets(2, 4, 2, 4));
    }

    private JPanel labeledField(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(subtitleFont);
        label.setForeground(textPrimary);
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void refreshLabelColors(Container container, Color color) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(color);
            }
            if (component instanceof Container) {
                refreshLabelColors((Container) component, color);
            }
        }
    }

    private void restylePill(JLabel pill, Color bg, Color border, Color fg) {
        if (pill == null) return;
        pill.setBackground(bg);
        pill.setForeground(fg);
        pill.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }
    private void handleMove(int row, int col) {
        if (aiThinking || replaying) return;
        if (!game.isValidMove(row, col)) {
            showToast("Invalid move. Choose an empty cell.");
            return;
        }

        char player = game.getCurrentPlayer();
        TicTacToe.MoveResult result = game.makeMove(row, col);
        buttons[row][col].setText(String.valueOf(player));
        setButtonStyle(buttons[row][col], player);
        animateMark(buttons[row][col]);
        highlightLastMove(row, col);
        historyModel.addElement(formatHistoryEntry(player, row, col));

        handleGameOverIfNeeded(result);
        playSound(1);
        updateStatus();
        saveSettings();
        updateHintHighlight();

        if (!game.isGameOver() && computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol) {
            makeComputerMove();
        }
    }

    private String formatHistoryEntry(char player, int row, int col) {
        List<TicTacToe.Move> moves = game.getMoveHistorySnapshot();
        long firstTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(0).timestamp;
        long nowTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(moves.size() - 1).timestamp;
        long delta = nowTime - firstTime;
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date(nowTime));
        return getDisplayName(player) + " (" + player + ") -> (" + (row + 1) + ", " + (col + 1) + ") at " + timestamp + " (+" + delta + " ms)";
    }

    private void handleGameOverIfNeeded(TicTacToe.MoveResult result) {
        if (result == TicTacToe.MoveResult.WIN) {
            clearHintHighlights();
            highlightWinningCells();
            setBoardButtonsEnabled(false);
            char winner = getWinnerChar();
            updateStats(winner);
            showToast(winner + " wins!");
        } else if (result == TicTacToe.MoveResult.DRAW) {
            clearHintHighlights();
            setBoardButtonsEnabled(false);
            updateStats('-');
            showToast("It's a draw!");
        }
    }

    private void resetGame() {
        exitReplayMode(false);
        startingPlayer = starterSelector.getSelectedItem().toString().charAt(0);
        game.initializeBoard(startingPlayer);
        clearBoardUI();
        historyModel.clear();
        replaySlider.setValue(0);
        replaySlider.setMaximum(0);
        replaySlider.setEnabled(false);
        setBoardButtonsEnabled(true);
        clearHighlights();
        updateStatus();
        gameStartTime = System.currentTimeMillis();
        restartMoveTimer();
        saveSettings();
        updateHintHighlight();
        if (demoMode) {
            runDemoTurn();
        } else {
            maybeAutoComputerMove();
        }
    }

    private void updateStatus() {
        String text;
        if (game.isGameOver()) {
            char winner = getWinnerChar();
            if (winner != '-') {
                text = "Game over: " + getDisplayName(winner) + " wins";
            } else {
                text = "Game over: draw";
            }
        } else if (computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol) {
            text = getDisplayName(computerSymbol) + " thinking (" + difficultySelector.getSelectedItem() + ")...";
        } else {
            text = "Turn: " + getDisplayName(game.getCurrentPlayer());
        }
        statusLabel.setText(text);
        statusLabel.getAccessibleContext().setAccessibleDescription(text);
        frame.setTitle("Tic-Tac-Toe - " + text);
        updateBadges(text);
        ensureMoveTimer();
    }

    private void ensureMoveTimer() {
        if (replaying || aiThinking || demoMode) return;
        if (game.isGameOver()) {
            stopMoveTimer();
            return;
        }
        char current = game.getCurrentPlayer();
        if (moveTimer == null || current != timerPlayer) {
            restartMoveTimer();
        }
    }

    private void restartMoveTimer() {
        timerPlayer = game.getCurrentPlayer();
        moveSecondsElapsed = 0;
        moveStartTime = System.currentTimeMillis();
        updateMoveTimerLabel();
        stopMoveTimer();
        moveTimer = new Timer(1000, e -> {
            moveSecondsElapsed++;
            updateMoveTimerLabel();
            if (countdownEnabled && moveSecondsElapsed >= countdownSeconds) {
                timeoutAutoMove();
            }
        });
        moveTimer.start();
    }

    private void stopMoveTimer() {
        if (moveTimer != null) {
            moveTimer.stop();
        }
    }

    private void updateMoveTimerLabel() {
        if (moveTimerLabel != null) {
            String time = String.format("Move: %02ds", moveSecondsElapsed);
            if (countdownEnabled) {
                int remaining = Math.max(0, countdownSeconds - moveSecondsElapsed);
                time = "Move: " + remaining + "s left";
            }
            moveTimerLabel.setText(time);
        }
    }

    private void timeoutAutoMove() {
        if (game.isGameOver() || replaying || aiThinking) return;
        if (computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol) {
            makeComputerMove();
            return;
        }
        int[] best = game.findBestMove(game.getCurrentPlayer(), getSelectedAiLevel());
        if (best == null) {
            best = game.findFirstAvailable();
        }
        if (best != null) {
            showToast("Countdown expired — auto move placed.");
            handleMove(best[0], best[1]);
        }
    }

    private void updateBadges(String statusText) {
        if (turnBadge == null) return;
        boolean highContrast = highContrastCheckBox.isSelected();
        Color baseBg = highContrast ? new Color(58, 58, 58) : accentBlueSoft;
        Color baseBorder = highContrast ? new Color(90, 90, 90) : accentBlue;
        Color baseFg = highContrast ? Color.WHITE : textPrimary;

        if (game.isGameOver()) {
            char winner = getWinnerChar();
            if (winner == '-') {
                restylePill(turnBadge, new Color(255, 245, 226), new Color(210, 163, 57), baseFg);
                turnBadge.setText("Result: Draw");
            } else {
                restylePill(turnBadge, new Color(232, 246, 239), successGreen.darker(), baseFg);
                turnBadge.setText("Winner: " + getDisplayName(winner));
            }
        } else if (computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol) {
            restylePill(turnBadge, baseBg, baseBorder, baseFg);
            turnBadge.setText("AI Turn: " + getDisplayName(computerSymbol));
        } else {
            restylePill(turnBadge, baseBg, baseBorder, baseFg);
            turnBadge.setText("Turn: " + getDisplayName(game.getCurrentPlayer()));
        }

        String modeText;
        if (demoMode) {
            modeText = "Mode: Demo (AI vs AI)";
        } else if (computerCheckBox.isSelected()) {
            modeText = "Mode: Player vs Computer";
        } else {
            modeText = "Mode: Local Multiplayer";
        }
        modeBadge.setText(modeText);
        restylePill(modeBadge, highContrast ? new Color(70, 70, 70) : new Color(235, 244, 255), baseBorder, baseFg);

        String aiLabel = "AI: " + difficultySelector.getSelectedItem();
        difficultyBadge.setText(aiLabel);
        restylePill(difficultyBadge, highContrast ? new Color(70, 70, 70) : new Color(230, 242, 255), baseBorder, baseFg);

        updateStreakBadge();
    }

    private void updateStreakBadge() {
        if (streakBadge == null) return;
        boolean highContrast = highContrastCheckBox.isSelected();
        Color baseBorder = highContrast ? new Color(120, 120, 120) : new Color(150, 150, 150);
        Color baseBg = highContrast ? new Color(70, 70, 70) : new Color(243, 243, 243);
        Color baseFg = highContrast ? Color.WHITE : textPrimary;
        if (currentStreak <= 0 || streakPlayer == '-') {
            streakBadge.setText("Streak: none");
            restylePill(streakBadge, baseBg, baseBorder, baseFg);
        } else {
            streakBadge.setText("Streak: " + streakPlayer + " x" + currentStreak);
            restylePill(streakBadge, new Color(232, 246, 239), successGreen.darker(), baseFg);
        }
    }

    private void updateScoreBadges() {
        if (xScoreBadge != null) xScoreBadge.setText("X Wins: " + xWins);
        if (oScoreBadge != null) oScoreBadge.setText("O Wins: " + oWins);
        if (drawScoreBadge != null) drawScoreBadge.setText("Draws: " + draws);
    }

    private void updateSeriesScore(char winner) {
        if (winner == 'X') seriesX++;
        if (winner == 'O') seriesO++;
        int target = (seriesLength / 2) + 1;
        if (seriesX >= target) {
            showToast("Series winner: X");
            seriesX = 0;
            seriesO = 0;
        } else if (seriesO >= target) {
            showToast("Series winner: O");
            seriesX = 0;
            seriesO = 0;
        }
    }

    private void resetSeries() {
        seriesX = 0;
        seriesO = 0;
        showToast("Series reset");
    }

    private char getWinnerChar() {
        List<int[]> winCells = game.getWinningCells();
        if (!winCells.isEmpty()) {
            int[] cell = winCells.get(0);
            char[][] snapshot = game.getBoardSnapshot();
            return snapshot[cell[0]][cell[1]];
        }
        return '-';
    }

    private void highlightWinningCells() {
        clearHighlights();
        Color winHighlight = highContrastCheckBox.isSelected() ? new Color(255, 193, 7) : new Color(255, 223, 128);
        for (int[] cell : game.getWinningCells()) {
            buttons[cell[0]][cell[1]].setBackground(winHighlight);
        }
        pulseWinningCells(winHighlight);
    }

    private void pulseWinningCells(Color target) {
        Timer pulse = new Timer(160, null);
        final int[] count = {0};
        pulse.addActionListener(e -> {
            count[0]++;
            boolean bright = count[0] % 2 == 0;
            for (int[] cell : game.getWinningCells()) {
                buttons[cell[0]][cell[1]].setBackground(bright ? target : defaultButtonColor);
            }
            if (count[0] > 8) {
                pulse.stop();
                for (int[] cell : game.getWinningCells()) {
                    buttons[cell[0]][cell[1]].setBackground(target);
                }
            }
        });
        pulse.start();
    }

    private void clearHighlights() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setBackground(defaultButtonColor);
            }
        }
    }

    private void clearHintHighlights() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setBackground(defaultButtonColor);
                }
            }
        }
        if (hintReasonLabel != null) {
            hintReasonLabel.setText(" ");
        }
    }

    private String describeMoveReason(int row, int col, char player) {
        char[][] board = game.getBoardSnapshot();
        if (isWinningMove(board, row, col, player)) return "Finish the game";
        char opponent = player == 'X' ? 'O' : 'X';
        if (isWinningMove(board, row, col, opponent)) return "Block opponent win";
        if (row == 1 && col == 1) return "Control the center";
        if ((row == 0 && col == 0) || (row == 0 && col == 2) || (row == 2 && col == 0) || (row == 2 && col == 2)) {
            return "Grab a corner";
        }
        return "Best available move";
    }

    private boolean isWinningMove(char[][] board, int row, int col, char player) {
        if (board[row][col] != '-') return false;
        char original = board[row][col];
        board[row][col] = player;
        boolean win =
                (board[row][0] == player && board[row][1] == player && board[row][2] == player) ||
                        (board[0][col] == player && board[1][col] == player && board[2][col] == player) ||
                        (row == col && board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                        (row + col == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player);
        board[row][col] = original;
        return win;
    }

    private void updateHintHighlight() {
        clearHintHighlights();
        if (!showHints || replaying || aiThinking || demoMode) return;
        if (game.isGameOver()) return;
        if (computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol) return;
        int[] best = game.findBestMove(game.getCurrentPlayer(), getSelectedAiLevel());
        if (best != null && buttons[best[0]][best[1]].getText().isEmpty()) {
            buttons[best[0]][best[1]].setBackground(hintColor);
            hintReasonLabel.setText("Hint: " + describeMoveReason(best[0], best[1], game.getCurrentPlayer()));
        } else {
            hintReasonLabel.setText(" ");
        }
    }

    private void setBoardButtonsEnabled(boolean enabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(enabled && !aiThinking);
            }
        }
        refreshInteractivity();
    }

    private void refreshInteractivity() {
        boolean baseEnabled = !aiThinking && !replaying;
        resetButton.setEnabled(baseEnabled);
        undoButton.setEnabled(baseEnabled);
        redoButton.setEnabled(baseEnabled);
        autoReplayButton.setEnabled(baseEnabled && !game.getMoveHistorySnapshot().isEmpty());
        stepBackButton.setEnabled(replaying);
        stepForwardButton.setEnabled(replaying);
        exitReplayButton.setEnabled(replaying);
        exportButton.setEnabled(baseEnabled);
        importButton.setEnabled(baseEnabled);
        copyClipboardButton.setEnabled(baseEnabled);
        pasteClipboardButton.setEnabled(baseEnabled);
        clearStatsButton.setEnabled(baseEnabled);
        summaryButton.setEnabled(baseEnabled);
        starterSelector.setEnabled(baseEnabled);
        difficultySelector.setEnabled(baseEnabled);
        computerCheckBox.setEnabled(baseEnabled && !demoMode);
        demoCheckBox.setEnabled(!replaying);
        hintCheckBox.setEnabled(baseEnabled && !demoMode && !replaying);
        highContrastCheckBox.setEnabled(baseEnabled);
        compactModeCheckBox.setEnabled(baseEnabled);
        largeControlCheckBox.setEnabled(baseEnabled);
        colorblindCheckBox.setEnabled(baseEnabled);
        dyslexiaFontCheckBox.setEnabled(baseEnabled);
        soundCheckBox.setEnabled(baseEnabled);
        countdownCheckBox.setEnabled(baseEnabled);
        countdownSpinner.setEnabled(baseEnabled && countdownCheckBox.isSelected());
        seriesSpinner.setEnabled(baseEnabled);
        seriesResetButton.setEnabled(baseEnabled);
        xNameField.setEnabled(baseEnabled);
        oNameField.setEnabled(baseEnabled && !computerCheckBox.isSelected());
        themeSelector.setEnabled(baseEnabled);
        thinkingBar.setVisible(aiThinking || replaying);
    }

    private void clearBoardUI() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(defaultButtonColor);
            }
        }
    }
    private void refreshBoardFromModel(boolean annotateMoves) {
        clearBoardUI();
        historyModel.clear();
        List<TicTacToe.Move> moves = game.getMoveHistorySnapshot();
        long firstTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(0).timestamp;
        int index = 1;
        for (TicTacToe.Move move : moves) {
            String text = String.valueOf(move.player);
            if (annotateMoves) {
                text += index;
            }
            buttons[move.row][move.col].setText(text);
            setButtonStyle(buttons[move.row][move.col], move.player);
            long delta = move.timestamp - firstTime;
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date(move.timestamp));
            historyModel.addElement(getDisplayName(move.player) + " (" + move.player + ") -> (" + (move.row + 1) + ", " + (move.col + 1) + ") at " + timestamp + " (+" + delta + " ms)");
            index++;
        }
        if (!moves.isEmpty()) {
            TicTacToe.Move last = moves.get(moves.size() - 1);
            highlightLastMove(last.row, last.col);
        }
        if (replaying) {
            setBoardButtonsEnabled(false);
        } else if (game.isGameOver() && !game.getWinningCells().isEmpty()) {
            highlightWinningCells();
            setBoardButtonsEnabled(false);
        } else {
            setBoardButtonsEnabled(!game.isGameOver());
        }
        updateStatus();
        updateHintHighlight();
    }

    private void setButtonStyle(JButton button, char player) {
        if (colorblindMode) {
            if (player == 'X') {
                button.setForeground(new Color(0, 118, 163));
            } else {
                button.setForeground(new Color(204, 85, 0));
            }
        } else {
            if (player == 'X') {
                button.setForeground(new Color(220, 20, 60));
            } else {
                button.setForeground(new Color(25, 118, 210));
            }
        }
    }

    private void makeComputerMove() {
        aiThinking = true;
        setBoardButtonsEnabled(false);
        refreshInteractivity();
        updateStatus();
        Timer timer = new Timer(350, null);
        timer.addActionListener(event -> {
            timer.stop();
            int[] bestMove = game.findBestMove(computerSymbol, getSelectedAiLevel());
            aiThinking = false;
            setBoardButtonsEnabled(true);
            refreshInteractivity();
            if (bestMove != null) {
                handleMove(bestMove[0], bestMove[1]);
            } else {
                updateStatus();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private TicTacToe.AiLevel getSelectedAiLevel() {
        String level = difficultySelector.getSelectedItem().toString();
        switch (level) {
            case "Perfect":
                return TicTacToe.AiLevel.PERFECT;
            case "Random":
                return TicTacToe.AiLevel.RANDOM;
            default:
                return TicTacToe.AiLevel.HEURISTIC;
        }
    }
    private void startAutoReplay() {
        if (!prepareReplay()) return;
        replayIndex = 0;
        Timer timer = new Timer(600, null);
        timer.addActionListener(event -> {
            if (replayIndex > replayBuffer.size()) {
                timer.stop();
                exitReplayMode(true);
                return;
            }
            showReplayState(replayIndex);
            replayIndex++;
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void stepReplay(int delta) {
        if (!replaying) {
            if (!prepareReplay()) return;
        }
        replayIndex = Math.max(0, Math.min(replayIndex + delta, replayBuffer.size()));
        showReplayState(replayIndex);
    }

    private boolean prepareReplay() {
        replayBuffer = game.getMoveHistorySnapshot();
        if (replayBuffer.isEmpty()) {
            showToast("No moves to replay.");
            return false;
        }
        stopMoveTimer();
        replaying = true;
        showReplayNumbers = true;
        clearHighlights();
        setBoardButtonsEnabled(false);
        replaySlider.setEnabled(true);
        replaySlider.setMaximum(replayBuffer.size());
        replaySlider.setValue(0);
        return true;
    }

    private void showReplayState(int movesToShow) {
        if (!replaying) return;
        sliderAdjusting = true;
        replaySlider.setValue(movesToShow);
        sliderAdjusting = false;
        char start = replayBuffer.isEmpty() ? startingPlayer : replayBuffer.get(0).player;
        game.applyMoves(replayBuffer.subList(0, movesToShow), start);
        refreshBoardFromModel(showReplayNumbers);
        if (movesToShow == replayBuffer.size() && game.isGameOver()) {
            highlightWinningCells();
        }
    }

    private void exitReplayMode(boolean restoreFull) {
        if (!replaying) return;
        replaying = false;
        showReplayNumbers = false;
        replaySlider.setEnabled(false);
        if (restoreFull) {
            game.applyMoves(replayBuffer, replayBuffer.isEmpty() ? startingPlayer : replayBuffer.get(0).player);
            refreshBoardFromModel(false);
            if (game.isGameOver() && !game.getWinningCells().isEmpty()) {
                highlightWinningCells();
                setBoardButtonsEnabled(false);
            } else {
                setBoardButtonsEnabled(true);
            }
        }
        updateStatus();
        restartMoveTimer();
    }

    private void maybeAutoComputerMove() {
        if (replaying) return;
        if (demoMode) return;
        if (computerCheckBox.isSelected() && game.getCurrentPlayer() == computerSymbol && !game.isGameOver()) {
            makeComputerMove();
        }
    }

    private void toggleDemoMode() {
        demoMode = demoCheckBox.isSelected();
        if (demoMode) {
            computerCheckBox.setSelected(false);
            showHints = false;
            hintCheckBox.setSelected(false);
            updateHintHighlight();
            runDemoTurn();
        }
        refreshInteractivity();
    }

    private void runDemoTurn() {
        if (!demoMode || replaying || aiThinking || game.isGameOver()) return;
        aiThinking = true;
        refreshInteractivity();
        Timer timer = new Timer(400, null);
        timer.addActionListener(event -> {
            timer.stop();
            int[] best = game.findBestMove(game.getCurrentPlayer(), getSelectedAiLevel());
            aiThinking = false;
            refreshInteractivity();
            if (best != null) {
                handleMove(best[0], best[1]);
                runDemoTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateStats(char winner) {
        if (winner == 'X') {
            xWins++;
        } else if (winner == 'O') {
            oWins++;
        } else {
            draws++;
        }
        if (winner != '-') {
            long duration = System.currentTimeMillis() - gameStartTime;
            if (duration < fastestWinMs) {
                fastestWinMs = duration;
            }
            updateSeriesScore(winner);
        }
        if (winner == '-') {
            currentStreak = 0;
            streakPlayer = '-';
        } else if (winner == streakPlayer) {
            currentStreak++;
        } else {
            streakPlayer = winner;
            currentStreak = 1;
        }
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
            longestStreakPlayer = streakPlayer;
        }
        statsLabel.setText("Stats: X " + xWins + " | O " + oWins + " | Draw " + draws);
        updateStreakBadge();
        updateScoreBadges();
        saveSettings();
    }

    private void clearStats() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        currentStreak = 0;
        longestStreak = 0;
        streakPlayer = '-';
        longestStreakPlayer = '-';
        fastestWinMs = Long.MAX_VALUE;
        seriesX = 0;
        seriesO = 0;
        statsLabel.setText("Stats: X 0 | O 0 | Draw 0");
        updateStreakBadge();
        updateScoreBadges();
        saveSettings();
        showToast("Stats cleared");
    }

    private void showSummaryDialog() {
        List<TicTacToe.Move> moves = game.getMoveHistorySnapshot();
        long avgMoveMs = 0;
        if (moves.size() > 1) {
            long total = moves.get(moves.size() - 1).timestamp - moves.get(0).timestamp;
            avgMoveMs = total / moves.size();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Session summary:\n");
        sb.append("X wins: ").append(xWins).append("\n");
        sb.append("O wins: ").append(oWins).append("\n");
        sb.append("Draws: ").append(draws).append("\n");
        sb.append("Longest streak: ").append(longestStreak).append(" by ").append(longestStreakPlayer == '-' ? "N/A" : longestStreakPlayer).append("\n");
        sb.append("Fastest win: ").append(fastestWinMs == Long.MAX_VALUE ? "N/A" : fastestWinMs + " ms").append("\n");
        sb.append("Series best-of-").append(seriesLength).append(": X ").append(seriesX).append(" | O ").append(seriesO).append("\n");
        sb.append("Average move time (current game): ").append(avgMoveMs).append(" ms\n");
        JOptionPane.showMessageDialog(frame, sb.toString(), "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showToast(String message) {
        if (message == null || message.isEmpty()) message = " ";
        if (message.contains("wins")) {
            toastLabel.setForeground(new Color(46, 125, 50));
        } else if (message.contains("draw")) {
            toastLabel.setForeground(new Color(66, 66, 66));
        } else if (message.toLowerCase().contains("invalid")) {
            toastLabel.setForeground(new Color(198, 40, 40));
        } else {
            toastLabel.setForeground(Color.BLACK);
        }
        toastLabel.setText(message);
        if (toastTimer != null) toastTimer.stop();
        toastTimer = new Timer(2000, e -> toastLabel.setText(" "));
        toastTimer.setRepeats(false);
        toastTimer.start();
        playSoundFromMessage(message);
    }

    private void showShortcutOverlay() {
        String shortcuts = """
                Shortcuts:
                Arrows: Move focus
                Enter/Space: Place mark
                Ctrl+Z: Undo (Alt+U)
                Ctrl+Y: Redo (Alt+R)
                ?: Show this help
                """;
        JOptionPane.showMessageDialog(frame, shortcuts, "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void playSoundFromMessage(String message) {
        if (!soundEnabled) return;
        if (message.contains("wins") || message.contains("winner")) {
            playSound(2);
        } else if (message.toLowerCase().contains("draw")) {
            playSound(3);
        } else {
            playSound(1);
        }
    }

    private void playSound(int variant) {
        if (!soundEnabled) return;
        Toolkit.getDefaultToolkit().beep();
        if (variant >= 2) {
            new Timer(120, e -> Toolkit.getDefaultToolkit().beep()).start();
        }
    }
    private void setupKeyBindings(JPanel boardPanel) {
        InputMap im = boardPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = boardPanel.getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("ENTER"), "press");
        im.put(KeyStroke.getKeyStroke("SPACE"), "press");
        im.put(KeyStroke.getKeyStroke("?"), "help");
        im.put(KeyStroke.getKeyStroke("control Z"), "undo");
        im.put(KeyStroke.getKeyStroke("control Y"), "redo");

        am.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(-1, 0);
            }
        });
        am.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(1, 0);
            }
        });
        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(0, -1);
            }
        });
        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(0, 1);
            }
        });
        am.put("press", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttons[focusedRow][focusedCol].isEnabled()) {
                    buttons[focusedRow][focusedCol].doClick();
                }
            }
        });
        am.put("help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShortcutOverlay();
            }
        });
        am.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoButton.doClick();
            }
        });
        am.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoButton.doClick();
            }
        });
    }

    private void moveFocus(int dr, int dc) {
        focusedRow = (focusedRow + dr + 3) % 3;
        focusedCol = (focusedCol + dc + 3) % 3;
        focusButton(focusedRow, focusedCol);
    }

    private void focusButton(int row, int col) {
        focusedRow = row;
        focusedCol = col;
        buttons[row][col].requestFocusInWindow();
    }

    private void focusRowCol(int row, int col) {
        focusedRow = row;
        focusedCol = col;
    }

    private void highlightLastMove(int row, int col) {
        if (lastMoveRow != -1 && lastMoveCol != -1 && buttons != null) {
            if (buttons[lastMoveRow][lastMoveCol].getText().isEmpty()) {
                buttons[lastMoveRow][lastMoveCol].setBackground(defaultButtonColor);
            }
        }
        lastMoveRow = row;
        lastMoveCol = col;
        buttons[row][col].setBackground(new Color(255, 245, 200));
        if (lastMoveLabel != null) {
            lastMoveLabel.setText("Last: " + (row + 1) + "," + (col + 1));
        }
    }

    private String getDisplayName(char player) {
        if (player == 'X') {
            return xNameField.getText().trim().isEmpty() ? "Player X" : xNameField.getText().trim();
        }
        String oName = oNameField.getText().trim().isEmpty() ? "Player O" : oNameField.getText().trim();
        if (computerCheckBox.isSelected()) {
            oName = "Computer (O)";
        }
        return oName;
    }

    private void installButtonEffects(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(defaultButtonColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(pressColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hoverColor);
            }
        });
        button.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (highVisibilityFocusCheckBox.isSelected()) {
                    button.setBorder(new LineBorder(new Color(0, 120, 215), 3, true));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                button.setBorder(new LineBorder(new Color(205, 213, 223), 2, true));
            }
        });
    }

    private void animateMark(JButton button) {
        Font base = button.getFont();
        int target = base.getSize() + 6;
        Timer anim = new Timer(30, null);
        final int[] step = {0};
        anim.addActionListener(e -> {
            step[0]++;
            int size = step[0] <= 5 ? base.getSize() + step[0] : target - (step[0] - 5);
            button.setFont(base.deriveFont((float) size));
            if (step[0] > 8) {
                button.setFont(base);
                anim.stop();
            }
        });
        anim.start();
    }
    private void loadSettings() {
        if (!settingsFile.exists()) return;
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(settingsFile)) {
            props.load(fis);
            startingPlayer = props.getProperty("starter", "X").charAt(0);
            starterSelector.setSelectedItem(String.valueOf(startingPlayer));
            computerCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("vsComputer", "false")));
            difficultySelector.setSelectedItem(props.getProperty("difficulty", "Heuristic"));
            highContrastCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("highContrast", "false")));
            compactModeCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("compactMode", "false")));
            largeControlCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("largeControls", "false")));
            colorblindCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("colorblind", "false")));
            demoCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("demoMode", "false")));
            hintCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("showHints", "false")));
            soundCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("sound", "true")));
            soundEnabled = soundCheckBox.isSelected();
            largeControls = largeControlCheckBox.isSelected();
            colorblindMode = colorblindCheckBox.isSelected();
            themeSelector.setSelectedItem(props.getProperty("theme", "Classic"));
            xNameField.setText(props.getProperty("xName", "Player X"));
            oNameField.setText(props.getProperty("oName", "Player O"));
        countdownCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("countdown", "false")));
        countdownSeconds = Integer.parseInt(props.getProperty("countdownSeconds", "10"));
        countdownSpinner.setValue(countdownSeconds);
        countdownEnabled = countdownCheckBox.isSelected();
        seriesLength = Integer.parseInt(props.getProperty("seriesLength", "5"));
        seriesSpinner.setValue(seriesLength);
        dyslexiaFontCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("readableFont", "false")));
        densitySelector.setSelectedItem(props.getProperty("density", "Comfortable"));
        baseFontSize = Integer.parseInt(props.getProperty("fontSize", Integer.toString(baseFontSize)));
        fontSizeSlider.setValue(baseFontSize);
        highVisibilityFocusCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("focusHighlight", "false")));
        showHints = hintCheckBox.isSelected();
        xWins = Integer.parseInt(props.getProperty("xWins", "0"));
        oWins = Integer.parseInt(props.getProperty("oWins", "0"));
        draws = Integer.parseInt(props.getProperty("draws", "0"));
        statsLabel.setText("Stats: X " + xWins + " | O " + oWins + " | Draw " + draws);
            fastestWinMs = Long.parseLong(props.getProperty("fastestWin", Long.toString(Long.MAX_VALUE)));
            updateScoreBadges();
            applyFontChoice();
            String moveString = props.getProperty("moves", "");
            List<TicTacToe.Move> moves = decodeMoves(moveString);
            game.applyMoves(moves, startingPlayer);
            gameStartTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(0).timestamp;
            refreshBoardFromModel(false);
            if (game.isGameOver() && !game.getWinningCells().isEmpty()) {
                highlightWinningCells();
            }
            restartMoveTimer();
        } catch (IOException ignored) {
        }
    }

    private void saveSettings() {
        Properties props = new Properties();
        props.setProperty("starter", starterSelector.getSelectedItem().toString());
        props.setProperty("vsComputer", Boolean.toString(computerCheckBox.isSelected()));
        props.setProperty("difficulty", difficultySelector.getSelectedItem().toString());
        props.setProperty("highContrast", Boolean.toString(highContrastCheckBox.isSelected()));
        props.setProperty("compactMode", Boolean.toString(compactModeCheckBox.isSelected()));
        props.setProperty("largeControls", Boolean.toString(largeControlCheckBox.isSelected()));
        props.setProperty("colorblind", Boolean.toString(colorblindCheckBox.isSelected()));
        props.setProperty("demoMode", Boolean.toString(demoCheckBox.isSelected()));
        props.setProperty("showHints", Boolean.toString(hintCheckBox.isSelected()));
        props.setProperty("sound", Boolean.toString(soundCheckBox.isSelected()));
        props.setProperty("theme", themeSelector.getSelectedItem().toString());
        props.setProperty("density", densitySelector.getSelectedItem().toString());
        props.setProperty("fontSize", Integer.toString(baseFontSize));
        props.setProperty("focusHighlight", Boolean.toString(highVisibilityFocusCheckBox.isSelected()));
        props.setProperty("xName", xNameField.getText());
        props.setProperty("oName", oNameField.getText());
        props.setProperty("countdown", Boolean.toString(countdownCheckBox.isSelected()));
        props.setProperty("countdownSeconds", Integer.toString(countdownSeconds));
        props.setProperty("seriesLength", Integer.toString(seriesLength));
        props.setProperty("readableFont", Boolean.toString(dyslexiaFontCheckBox.isSelected()));
        props.setProperty("xWins", Integer.toString(xWins));
        props.setProperty("oWins", Integer.toString(oWins));
        props.setProperty("draws", Integer.toString(draws));
        props.setProperty("fastestWin", Long.toString(fastestWinMs));
        props.setProperty("moves", encodeMoves(game.getMoveHistorySnapshot()));
        try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
            props.store(fos, "TicTacToe settings");
            settingsWriteWarned = false;
        } catch (IOException ex) {
            if (!settingsWriteWarned) {
                settingsWriteWarned = true;
                showToast("Could not save settings: " + ex.getMessage());
            }
        }
    }

    private String encodeMoves(List<TicTacToe.Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            TicTacToe.Move m = moves.get(i);
            sb.append(m.player).append(",").append(m.row).append(",").append(m.col).append(",").append(m.timestamp);
            if (i < moves.size() - 1) sb.append("|");
        }
        return sb.toString();
    }

    private List<TicTacToe.Move> decodeMoves(String encoded) {
        List<TicTacToe.Move> moves = new ArrayList<>();
        if (encoded == null || encoded.isEmpty()) return moves;
        String[] parts = encoded.split("\\|");
        for (String part : parts) {
            String[] fields = part.split(",");
            if (fields.length >= 3) {
                try {
                    char player = fields[0].charAt(0);
                    int row = Integer.parseInt(fields[1]);
                    int col = Integer.parseInt(fields[2]);
                    long timestamp = fields.length >= 4 ? Long.parseLong(fields[3]) : System.currentTimeMillis();
                    moves.add(new TicTacToe.Move(row, col, player, timestamp));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return moves;
    }

    private void exportGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Game");
        int choice = chooser.showSaveDialog(frame);
        if (choice != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        Properties props = new Properties();
        props.setProperty("starter", starterSelector.getSelectedItem().toString());
        props.setProperty("vsComputer", Boolean.toString(computerCheckBox.isSelected()));
        props.setProperty("difficulty", difficultySelector.getSelectedItem().toString());
        props.setProperty("xWins", Integer.toString(xWins));
        props.setProperty("oWins", Integer.toString(oWins));
        props.setProperty("draws", Integer.toString(draws));
        props.setProperty("moves", encodeMoves(game.getMoveHistorySnapshot()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            props.store(fos, "TicTacToe export");
            showToast("Exported game to " + file.getName());
        } catch (IOException ex) {
            showToast("Export failed: " + ex.getMessage());
        }
    }

    private void copyGameToClipboard() {
        Properties props = new Properties();
        props.setProperty("starter", starterSelector.getSelectedItem().toString());
        props.setProperty("vsComputer", Boolean.toString(computerCheckBox.isSelected()));
        props.setProperty("difficulty", difficultySelector.getSelectedItem().toString());
        props.setProperty("xWins", Integer.toString(xWins));
        props.setProperty("oWins", Integer.toString(oWins));
        props.setProperty("draws", Integer.toString(draws));
        props.setProperty("moves", encodeMoves(game.getMoveHistorySnapshot()));
        StringBuilder sb = new StringBuilder();
        props.forEach((k, v) -> sb.append(k).append("=").append(v).append("\n"));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = new StringSelection(sb.toString());
        clipboard.setContents(t, null);
        showToast("Copied game to clipboard");
    }

    private void pasteGameFromClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String data = clipboard.getData(java.awt.datatransfer.DataFlavor.stringFlavor).toString();
            Properties props = new Properties();
            for (String line : data.split("\\n")) {
                if (!line.contains("=")) continue;
                int idx = line.indexOf('=');
                props.setProperty(line.substring(0, idx), line.substring(idx + 1));
            }
            starterSelector.setSelectedItem(props.getProperty("starter", "X"));
            computerCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("vsComputer", "false")));
            difficultySelector.setSelectedItem(props.getProperty("difficulty", "Heuristic"));
            xWins = Integer.parseInt(props.getProperty("xWins", "0"));
            oWins = Integer.parseInt(props.getProperty("oWins", "0"));
            draws = Integer.parseInt(props.getProperty("draws", "0"));
            statsLabel.setText("Stats: X " + xWins + " | O " + oWins + " | Draw " + draws);
            updateScoreBadges();
            String moveString = props.getProperty("moves", "");
            List<TicTacToe.Move> moves = decodeMoves(moveString);
            startingPlayer = starterSelector.getSelectedItem().toString().charAt(0);
            game.applyMoves(moves, startingPlayer);
            gameStartTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(0).timestamp;
            refreshBoardFromModel(false);
            if (game.isGameOver() && !game.getWinningCells().isEmpty()) {
                highlightWinningCells();
            }
            restartMoveTimer();
            saveSettings();
            showToast("Pasted game from clipboard");
        } catch (Exception ex) {
            showToast("Paste failed: " + ex.getMessage());
        }
    }

    private void importGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Import Game");
        int choice = chooser.showOpenDialog(frame);
        if (choice != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
            starterSelector.setSelectedItem(props.getProperty("starter", "X"));
            computerCheckBox.setSelected(Boolean.parseBoolean(props.getProperty("vsComputer", "false")));
            difficultySelector.setSelectedItem(props.getProperty("difficulty", "Heuristic"));
            xWins = Integer.parseInt(props.getProperty("xWins", "0"));
            oWins = Integer.parseInt(props.getProperty("oWins", "0"));
            draws = Integer.parseInt(props.getProperty("draws", "0"));
            statsLabel.setText("Stats: X " + xWins + " | O " + oWins + " | Draw " + draws);
            updateScoreBadges();
            String moveString = props.getProperty("moves", "");
            List<TicTacToe.Move> moves = decodeMoves(moveString);
            startingPlayer = starterSelector.getSelectedItem().toString().charAt(0);
            game.applyMoves(moves, startingPlayer);
            gameStartTime = moves.isEmpty() ? System.currentTimeMillis() : moves.get(0).timestamp;
            refreshBoardFromModel(false);
            if (game.isGameOver() && !game.getWinningCells().isEmpty()) {
                highlightWinningCells();
            }
            restartMoveTimer();
            saveSettings();
            showToast("Imported game from " + file.getName());
        } catch (IOException ex) {
            showToast("Import failed: " + ex.getMessage());
        }
    }

    private void applyTheme() {
        boolean highContrast = highContrastCheckBox.isSelected();
        String theme = themeSelector.getSelectedItem() != null ? themeSelector.getSelectedItem().toString() : "Classic";
        Color baseAccent = accentBlue;
        Color baseAccentSoft = accentBlueSoft;
        Color baseSurface = surfaceColor;
        Color baseCard = Color.WHITE;
        Color baseControl = new Color(247, 249, 252);
        if (!highContrast) {
            switch (theme) {
                case "Neon":
                    baseAccent = new Color(0, 207, 255);
                    baseAccentSoft = new Color(224, 255, 250);
                    baseSurface = new Color(18, 22, 30);
                    baseCard = new Color(26, 32, 44);
                    baseControl = new Color(38, 46, 60);
                    break;
                case "Pastel":
                    baseAccent = new Color(255, 140, 164);
                    baseAccentSoft = new Color(255, 235, 240);
                    baseSurface = new Color(247, 245, 250);
                    baseCard = Color.WHITE;
                    baseControl = new Color(250, 246, 252);
                    break;
                default:
                    break;
            }
        }

        Color bg = highContrast ? new Color(24, 24, 24) : baseSurface;
        Color cardBg = highContrast ? new Color(36, 36, 36) : baseCard;
        Color border = highContrast ? new Color(75, 75, 75) : cardBorderColor;
        Color fg = highContrast ? new Color(235, 235, 235) : (theme.equals("Neon") ? new Color(230, 240, 255) : textPrimary);
        Color controlBg = highContrast ? new Color(48, 48, 48) : baseControl;
        defaultButtonColor = highContrast ? new Color(55, 55, 55) : new Color(246, 249, 253);
        hoverColor = highContrast ? new Color(70, 70, 70) : baseAccentSoft;
        pressColor = highContrast ? new Color(90, 90, 90) : baseAccent.darker();

        frame.getContentPane().setBackground(bg);
        toastLabel.setForeground(fg);
        statsLabel.setForeground(fg);
        if (toastLabel.getParent() instanceof JComponent) {
            ((JComponent) toastLabel.getParent()).setBackground(bg);
        }
        if (hintReasonLabel != null) {
            hintReasonLabel.setForeground(fg);
        }
        if (backgroundPanel != null) {
            Color top = highContrast ? new Color(18, 18, 18) : bg.brighter();
            Color bottom = highContrast ? new Color(32, 32, 32) : cardBg.brighter();
            backgroundPanel.setGradient(top, bottom);
        }

        Color headerBg = highContrast ? new Color(50, 50, 50) : baseAccent;
        if (headerPanel != null) headerPanel.setBackground(headerBg);
        if (headerTitleLabel != null) headerTitleLabel.setForeground(highContrast ? fg : Color.WHITE);
        if (heroBadgeLabel != null) {
            heroBadgeLabel.setBackground(highContrast ? new Color(80, 80, 80) : new Color(255, 255, 255, 40));
            heroBadgeLabel.setForeground(highContrast ? fg : Color.WHITE);
        }
        statusLabel.setForeground(highContrast ? fg : new Color(235, 245, 255));

        JButton[] controls = {resetButton, undoButton, redoButton, autoReplayButton, stepBackButton, stepForwardButton, exitReplayButton, exportButton, importButton, clearStatsButton, summaryButton};
        for (JButton b : controls) {
            b.setBackground(controlBg);
            b.setForeground(fg);
            b.setBorder(new LineBorder(border, 1, true));
        }
        JCheckBox[] toggles = {computerCheckBox, highContrastCheckBox, compactModeCheckBox, demoCheckBox, hintCheckBox, dyslexiaFontCheckBox};
        for (JCheckBox toggle : toggles) {
            toggle.setBackground(cardBg);
            toggle.setForeground(fg);
        }
        starterSelector.setBackground(cardBg);
        starterSelector.setForeground(fg);
        starterSelector.setBorder(new LineBorder(border, 1, true));
        difficultySelector.setBackground(cardBg);
        difficultySelector.setForeground(fg);
        difficultySelector.setBorder(new LineBorder(border, 1, true));
        themeSelector.setBackground(cardBg);
        themeSelector.setForeground(fg);
        themeSelector.setBorder(new LineBorder(border, 1, true));
        densitySelector.setBackground(cardBg);
        densitySelector.setForeground(fg);
        densitySelector.setBorder(new LineBorder(border, 1, true));
        countdownSpinner.getEditor().getComponent(0).setBackground(cardBg);
        countdownSpinner.getEditor().getComponent(0).setForeground(fg);
        seriesSpinner.getEditor().getComponent(0).setBackground(cardBg);
        seriesSpinner.getEditor().getComponent(0).setForeground(fg);
        xNameField.setBackground(cardBg);
        xNameField.setForeground(fg);
        oNameField.setBackground(cardBg);
        oNameField.setForeground(fg);

        for (JPanel card : cards) {
            card.setBackground(cardBg);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(border, 1, true),
                    BorderFactory.createEmptyBorder(10, 12, 12, 12)));
            refreshLabelColors(card, fg);
        }

        if (historyList != null) {
            historyList.setBackground(cardBg);
            historyList.setForeground(fg);
            if (historyList.getParent() instanceof javax.swing.JViewport) {
                ((javax.swing.JViewport) historyList.getParent()).setBackground(cardBg);
            }
        }

        Color pillBg = highContrast ? new Color(58, 58, 58) : baseAccentSoft;
        Color pillBorder = highContrast ? new Color(90, 90, 90) : baseAccent;
        Color pillFg = highContrast ? fg : textPrimary;
        restylePill(turnBadge, pillBg, pillBorder, pillFg);
        restylePill(modeBadge, highContrast ? new Color(70, 70, 70) : new Color(235, 244, 255), pillBorder, pillFg);
        restylePill(difficultyBadge, highContrast ? new Color(70, 70, 70) : new Color(230, 242, 255), pillBorder, pillFg);
        restylePill(streakBadge, highContrast ? new Color(70, 70, 70) : new Color(243, 243, 243), new Color(150, 150, 150), pillFg);
        restylePill(moveTimerLabel, highContrast ? new Color(70, 70, 70) : new Color(243, 243, 243), pillBorder, pillFg);
        restylePill(lastMoveLabel, highContrast ? new Color(70, 70, 70) : new Color(243, 243, 243), pillBorder, pillFg);
        restylePill(xScoreBadge, new Color(232, 246, 239), successGreen.darker(), pillFg);
        restylePill(oScoreBadge, new Color(230, 242, 255), new Color(25, 118, 210), pillFg);
        restylePill(drawScoreBadge, new Color(255, 247, 234), warningAmber.darker(), pillFg);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setBackground(defaultButtonColor);
                String value = buttons[i][j].getText();
                if (value.startsWith("X") || value.startsWith("O")) {
                    setButtonStyle(buttons[i][j], value.charAt(0));
                } else {
                    buttons[i][j].setForeground(fg);
                }
                buttons[i][j].setBorder(new LineBorder(border, 2, true));
            }
        }
    }

    private void applyCompactMode(boolean compact) {
        int fontSize = compact ? 32 : 44;
        if (largeControls) fontSize += 6;
        Font buttonFont = readableFont(fontSize);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setFont(buttonFont);
            }
        }
        int width = compact ? 900 : 1020;
        int height = compact ? 560 : 640;
        if (largeControls) {
            width += 80;
            height += 40;
        }
        frame.setSize(width, height);
    }

    private void applyDensitySpacing() {
        boolean dense = densitySelector != null && "Dense".equals(densitySelector.getSelectedItem());
        int padding = dense ? 8 : 12;
        int paddingVert = dense ? 6 : 12;
        for (JPanel card : cards) {
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(cardBorderColor, 1, true),
                    BorderFactory.createEmptyBorder(paddingVert, padding, paddingVert, padding)));
        }
        if (buttons != null && buttons.length > 0) {
            for (Component comp : buttons[0][0].getParent().getParent().getComponents()) {
                comp.invalidate();
            }
        }
        if (densitySelector != null) {
            densitySelector.revalidate();
        }
    }

    private Font readableFont(int size) {
        String fontName = dyslexiaFontCheckBox != null && dyslexiaFontCheckBox.isSelected() ? "Verdana" : "Segoe UI";
        return new Font(fontName, Font.BOLD, size);
    }

    private void applyFontChoice() {
        Font buttonFont = readableFont(buttons != null ? buttons[0][0].getFont().getSize() : 40);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setFont(buttonFont);
            }
        }
        String baseFont = dyslexiaFontCheckBox != null && dyslexiaFontCheckBox.isSelected() ? "Verdana" : "Segoe UI";
        Font labelFont = new Font(baseFont, Font.PLAIN, baseFontSize);
        Font controlReadable = new Font(baseFont, Font.PLAIN, Math.max(12, baseFontSize - 1));
        statusLabel.setFont(labelFont);
        toastLabel.setFont(labelFont);
        statsLabel.setFont(labelFont);
        JButton[] controls = {resetButton, undoButton, redoButton, autoReplayButton, stepBackButton, stepForwardButton, exitReplayButton, exportButton, importButton, copyClipboardButton, pasteClipboardButton, clearStatsButton, summaryButton, seriesResetButton};
        for (JButton control : controls) {
            control.setFont(controlReadable);
        }
        JCheckBox[] toggles = {computerCheckBox, highContrastCheckBox, compactModeCheckBox, demoCheckBox, hintCheckBox, dyslexiaFontCheckBox, largeControlCheckBox, colorblindCheckBox, soundCheckBox, countdownCheckBox};
        for (JCheckBox toggle : toggles) {
            toggle.setFont(controlReadable);
        }
        starterSelector.setFont(controlReadable);
        difficultySelector.setFont(controlReadable);
        themeSelector.setFont(controlReadable);
        densitySelector.setFont(controlReadable);
        countdownSpinner.setFont(controlReadable);
        seriesSpinner.setFont(controlReadable);
        xNameField.setFont(controlReadable);
        oNameField.setFont(controlReadable);
        if (historyList != null) {
            historyList.setFont(controlReadable);
        }
        Font pillFont = new Font(baseFont, Font.BOLD, 13);
        if (turnBadge != null) turnBadge.setFont(pillFont);
        if (modeBadge != null) modeBadge.setFont(pillFont);
        if (difficultyBadge != null) difficultyBadge.setFont(pillFont);
        if (streakBadge != null) streakBadge.setFont(pillFont);
        if (xScoreBadge != null) xScoreBadge.setFont(pillFont);
        if (oScoreBadge != null) oScoreBadge.setFont(pillFont);
        if (drawScoreBadge != null) drawScoreBadge.setFont(pillFont);
        if (moveTimerLabel != null) moveTimerLabel.setFont(pillFont);
        if (lastMoveLabel != null) lastMoveLabel.setFont(pillFont);
        if (heroBadgeLabel != null) {
            heroBadgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
    }

    private static class GradientPanel extends JPanel {
        private Color top = new Color(242, 247, 252);
        private Color bottom = new Color(223, 231, 243);

        void setGradient(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, getWidth(), getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private static void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName()) || "Windows 10".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception ignored) {
            // fallback to default
        }
    }
}

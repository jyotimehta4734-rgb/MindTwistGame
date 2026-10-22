import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * MindTwistGame - "Socho, Samjho, Jeeto!"
 * A funny, picture-based Swing quiz game for school kids.
 * The child enters their name, plays 12 rounds (picture quiz, odd-one-out,
 * logic trick, BODMAS speed question and a reflex "Click Me" round) and gets a
 * Champion certificate screen with their name (kids can photograph it).
 *
 * Created and developed by Jyoti Mehta.
 *
 * Compile : javac -encoding UTF-8 MindTwistGame.java
 * Run     : java MindTwistGame
 */
public class MindTwistGame extends JFrame {

    // ---------- Theme ----------
    private static final Color BG     = new Color(15, 15, 30);
    private static final Color PANEL  = new Color(28, 28, 52);
    private static final Color YELLOW = new Color(255, 221, 0);
    private static final Color NEON   = new Color(0, 229, 255);
    private static final Color RED    = new Color(255, 82, 110);
    private static final Color GREEN  = new Color(57, 255, 130);
    private static final Color TEXT   = new Color(240, 240, 255);

    private static final Font F_TITLE  = new Font("Arial", Font.BOLD, 28);
    private static final Font F_BIG    = new Font("Arial", Font.BOLD, 22);
    private static final Font F_NORMAL = new Font("Arial", Font.BOLD, 18);
    private static final Font F_EMOJI  = new Font("Segoe UI Emoji", Font.PLAIN, 72);

    // ---------- Emojis (unicode escapes, so file encoding never breaks them) ----------
    private static final String BULB = "\uD83D\uDCA1", CROSS = "\u274C", BRAIN = "\uD83E\uDDE0";
    private static final String CROWN = "\uD83D\uDC51", PARTY = "\uD83C\uDF89", TURTLE = "\uD83D\uDC22";
    private static final String TROPHY = "\uD83C\uDFC6", BOLT = "\u26A1", SMILE = "\uD83D\uDE04";
    private static final String ELEPHANT = "\uD83D\uDC18", LION = "\uD83E\uDD81", MONKEY = "\uD83D\uDC12";
    private static final String APPLE = "\uD83C\uDF4E", BANANA = "\uD83C\uDF4C", CAR = "\uD83D\uDE97";
    private static final String GRAPES = "\uD83C\uDF47", COW = "\uD83D\uDC2E", DOG = "\uD83D\uDC36";
    private static final String CAT = "\uD83D\uDC31", BALL = "\u26BD", SUN = "\uD83C\uDF1E";
    private static final String THINK = "\uD83E\uDD14", DETECTIVE = "\uD83D\uDD75", NUM = "\uD83D\uDD22";
    private static final String PALETTE = "\uD83C\uDFA8", LAUGH = "\uD83D\uDE02", YUM = "\uD83D\uDE0B";
    private static final String MILK = "\uD83E\uDD5B", MOON = "\uD83C\uDF19", SLEEP = "\uD83D\uDE34";
    private static final String TARGET = "\uD83C\uDFAF", CAMERA = "\uD83D\uDCF8", STAR = "\uD83C\uDF1F";

    // ---------- Game content ----------
    private static class Item {
        final String title, emoji, wrong, explain, hint;
        final String[] options;
        final int correct;
        final boolean speed, reflex;

        Item(String title, String emoji, String[] options, int correct, String wrong,
             String explain, String hint, boolean speed, boolean reflex) {
            this.title = title;
            this.emoji = emoji;
            this.options = options;
            this.correct = correct;
            this.wrong = wrong;
            this.explain = explain;
            this.hint = hint;
            this.speed = speed;
            this.reflex = reflex;
        }
    }

    private static Item pic(String title, String emoji, String[] options, int correct, String wrong) {
        return new Item(title, emoji, options, correct, wrong, null,
                "SOCHO: Tasveer dekho aur sahi jawab chuno! " + TARGET, false, false);
    }

    private static final Item[] ITEMS = {
        pic("Ye kaun hai? " + THINK, ELEPHANT, new String[]{"Sher", "Haathi", "Billi", "Bandar"}, 1,
                "Haathi ko billi bol diya?! " + ELEPHANT + LAUGH),
        pic("Ye kaun hai? " + THINK, LION, new String[]{"Sher", "Gadha", "Kutta", "Bhalu"}, 0,
                "Sher ghussa ho gaya! Dobara dekho! " + LION),
        pic("Ye kaun hai? " + THINK, MONKEY, new String[]{"Machhli", "Bandar", "Tota", "Kachhua"}, 1,
                "Arey! Yeh toh pedon par kood raha hai! " + MONKEY),
        pic("Alag kaun hai? " + DETECTIVE, APPLE + " " + BANANA + " " + CAR + " " + GRAPES,
                new String[]{"Seb", "Kela", "Gaadi", "Angoor"}, 2,
                "Gaadi phal nahi hoti! Khayoge kya? " + CAR + YUM),
        pic("Gaay kya bolti hai? " + COW, COW, new String[]{"Bhau-Bhau", "Myaau", "Muu", "Cheen-Cheen"}, 2,
                "Gaay ne bhau-bhau bola toh kutta bhi hans padega! " + DOG + LAUGH),
        pic("Kitne seb hain? " + NUM, APPLE + APPLE + APPLE + APPLE + APPLE,
                new String[]{"3", "4", "5", "6"}, 2,
                "Ungli se gino, seb gayab nahi honge! " + APPLE),
        pic("Kele ka rang kaisa hota hai? " + PALETTE, BANANA,
                new String[]{"Laal", "Peela", "Neela", "Hara"}, 1,
                "Neela kela? Bandar bhi hairan hai! " + MONKEY),
        new Item(null, null, null, 0, null, null,
                "JEETO: Button ko 20 second mein 3 baar pakdo!", false, true),   // reflex round
        pic("Alag kaun hai? " + DETECTIVE, DOG + " " + CAT + " " + COW + " " + BALL,
                new String[]{"Kutta", "Billi", "Gaay", "Football"}, 3,
                "Football ko dudh kaun pilayega? " + BALL + MILK),
        pic("Din mein aasman mein kaun chamakta hai?", SUN,
                new String[]{"Chand", "Suraj", "Taara", "Bijli"}, 1,
                "Chand toh so raha hai abhi! " + MOON + SLEEP),
        new Item("If a farmer has 10 cows, and all but 6 die, how many are left?", null,
                new String[]{"A) 4", "B) 6", "C) 0", "D) 10"}, 1, null,
                "'All but 6 die' matlab 6 bach gaye! Sirf 6 cows zinda hain.",
                "SOCHO: Dhyan se padho, sawal thoda chalaak hai!", false, false),
        new Item("Solve immediately:  5 + 5 x 0 + 5", null,
                new String[]{"A) 0", "B) 10", "C) 5", "D) 25"}, 1, null,
                "BODMAS rule: pehle multiplication! 5 x 0 = 0, so 5 + 0 + 5 = 10.",
                "SAMJHO: Speed challenge! 5 second mein jawab do aur bonus pao " + BOLT, true, false)
    };

    private static final String[] WRONG_MSGS = {
        "Arey re! Dimag ki batti gul! " + BULB + CROSS + " Try again!",
        "Bilkul galat! Thoda badaam khao! " + BRAIN,
        "Oho! Ek baar aur socho! " + BRAIN
    };

    // ---------- Game state ----------
    private String playerName = "";
    private int score = 0;
    private int idx = 0;
    private long questionStartTime;
    private final Random random = new Random();

    // ---------- Swing components ----------
    private final JLabel scoreLabel  = new JLabel();
    private final JLabel hintLabel   = new JLabel("", SwingConstants.CENTER);
    private final CardLayout cards   = new CardLayout();
    private final JPanel centerPanel = new JPanel(cards);

    private final JTextField nameField = new JTextField(16);

    private final JLabel quizTitle  = new JLabel("", SwingConstants.CENTER);
    private final JLabel emojiLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton[] optionButtons = new JButton[4];

    private final JPanel reflexArea = new JPanel(null);
    private final JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel hitsLabel  = new JLabel("", SwingConstants.CENTER);
    private final JButton targetButton = styledButton("CLICK ME!", YELLOW);
    private Timer moveTimer;
    private Timer countdownTimer;
    private int hits = 0;
    private int secondsLeft = 20;

    private final JLabel certName  = new JLabel("", SwingConstants.CENTER);
    private final JLabel certScore = new JLabel("", SwingConstants.CENTER);

    public MindTwistGame() {
        super("Socho, Samjho, Jeeto!  -  MindTwist Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 700);
        setMinimumSize(new Dimension(800, 620));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        add(buildHeader(), BorderLayout.NORTH);

        centerPanel.setBackground(BG);
        centerPanel.add(buildStartPanel(), "START");
        centerPanel.add(buildQuizPanel(), "QUIZ");
        centerPanel.add(buildReflexPanel(), "REFLEX");
        centerPanel.add(buildEndPanel(), "END");
        add(centerPanel, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        showStart();
    }

    // =====================================================================
    //  UI BUILDERS
    // =====================================================================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new GridLayout(1, 3, 10, 0));
        header.setBackground(PANEL);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 4, 0, YELLOW),
                new EmptyBorder(12, 20, 12, 20)));

        JLabel title = new JLabel("Socho, Samjho, Jeeto!", SwingConstants.CENTER);
        title.setFont(F_TITLE);
        title.setForeground(YELLOW);

        scoreLabel.setFont(F_BIG);
        scoreLabel.setForeground(GREEN);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(new JLabel(""));
        header.add(title);
        header.add(scoreLabel);
        return header;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridLayout(2, 1));
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(4, 10, 10, 10));

        hintLabel.setFont(F_NORMAL);
        hintLabel.setForeground(NEON);

        JLabel credit = new JLabel("Created and developed by Jyoti Mehta", SwingConstants.CENTER);
        credit.setFont(new Font("Arial", Font.BOLD, 17));
        credit.setForeground(YELLOW);

        footer.add(hintLabel);
        footer.add(credit);
        return footer;
    }

    private JPanel buildStartPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(PANEL);
        box.setBorder(new CompoundBorder(new LineBorder(NEON, 3, true), new EmptyBorder(30, 50, 30, 50)));

        JLabel t1 = centeredLabel("Welcome, Chhote Einstein! " + BRAIN, F_TITLE, YELLOW);
        JLabel t2 = centeredLabel("Tasveer pehchano, dimag lagao, button pakdo!", F_BIG, NEON);
        JLabel t3 = centeredLabel("Pehle apna naam batao:", F_BIG, TEXT);

        nameField.setFont(new Font("Arial", Font.BOLD, 24));
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        nameField.setBackground(BG);
        nameField.setForeground(TEXT);
        nameField.setCaretColor(YELLOW);
        nameField.setBorder(new CompoundBorder(new LineBorder(NEON, 3, true), new EmptyBorder(8, 10, 8, 10)));
        nameField.setMaximumSize(new Dimension(360, 56));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.addActionListener(e -> startNewGame());

        JButton start = styledButton("START GAME", YELLOW);
        start.setFont(F_TITLE);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> startNewGame());

        box.add(t1);
        box.add(Box.createVerticalStrut(12));
        box.add(t2);
        box.add(Box.createVerticalStrut(20));
        box.add(t3);
        box.add(Box.createVerticalStrut(10));
        box.add(nameField);
        box.add(Box.createVerticalStrut(25));
        box.add(start);
        p.add(box);
        return p;
    }

    private JPanel buildQuizPanel() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(15, 40, 15, 40));

        JPanel qBox = new JPanel();
        qBox.setLayout(new BoxLayout(qBox, BoxLayout.Y_AXIS));
        qBox.setBackground(PANEL);
        qBox.setBorder(new CompoundBorder(new LineBorder(YELLOW, 3, true), new EmptyBorder(15, 20, 15, 20)));

        quizTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        emojiLabel.setFont(F_EMOJI);
        emojiLabel.setForeground(TEXT);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qBox.add(quizTitle);
        qBox.add(emojiLabel);
        p.add(qBox, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(BG);
        grid.setBorder(new EmptyBorder(15, 0, 0, 0));
        for (int i = 0; i < 4; i++) {
            final int choice = i;
            optionButtons[i] = styledButton("", NEON);
            optionButtons[i].setFont(new Font("Arial", Font.BOLD, 24));
            optionButtons[i].addActionListener(e -> onAnswer(choice));
            grid.add(optionButtons[i]);
        }
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildReflexPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel top = new JPanel(new GridLayout(1, 2, 20, 0));
        top.setBackground(BG);
        timerLabel.setFont(F_BIG);
        timerLabel.setForeground(RED);
        hitsLabel.setFont(F_BIG);
        hitsLabel.setForeground(YELLOW);
        top.add(timerLabel);
        top.add(hitsLabel);
        p.add(top, BorderLayout.NORTH);

        reflexArea.setBackground(PANEL);
        reflexArea.setBorder(new LineBorder(NEON, 3, true));
        targetButton.setFont(F_BIG);
        targetButton.setSize(190, 80);
        // mousePressed (not ActionListener) so the click counts even if the button jumps before release
        targetButton.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                onTargetClicked();
            }
        });
        reflexArea.add(targetButton);
        p.add(reflexArea, BorderLayout.CENTER);
        return p;
    }

    /** Champion certificate screen - kids can take a phone photo of it. */
    private JPanel buildEndPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);

        JPanel cert = new JPanel();
        cert.setLayout(new BoxLayout(cert, BoxLayout.Y_AXIS));
        cert.setBackground(PANEL);
        cert.setBorder(new CompoundBorder(new LineBorder(YELLOW, 5),
                new CompoundBorder(new EmptyBorder(5, 5, 5, 5),
                        new CompoundBorder(new LineBorder(YELLOW, 2), new EmptyBorder(25, 50, 25, 50)))));

        certName.setFont(new Font("Arial", Font.BOLD, 44));
        certName.setForeground(YELLOW);
        certScore.setFont(F_BIG);
        certScore.setForeground(GREEN);

        JButton again = styledButton("PLAY AGAIN", NEON);
        again.setAlignmentX(Component.CENTER_ALIGNMENT);
        again.addActionListener(e -> showStart());

        cert.add(centeredLabel(STAR + " CERTIFICATE OF CHAMPION " + STAR, F_BIG, NEON));
        cert.add(Box.createVerticalStrut(12));
        cert.add(centeredLabel("Yeh certificate diya jata hai", F_NORMAL, TEXT));
        cert.add(Box.createVerticalStrut(8));
        certName.setAlignmentX(Component.CENTER_ALIGNMENT);
        cert.add(certName);
        cert.add(Box.createVerticalStrut(8));
        cert.add(centeredLabel("Socho, Samjho, Jeeto! mein shaandaar khelne ke liye " + TROPHY, F_NORMAL, TEXT));
        cert.add(Box.createVerticalStrut(8));
        certScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        cert.add(certScore);
        cert.add(Box.createVerticalStrut(8));
        cert.add(centeredLabel("Aap toh Chhote Einstein nikle! " + CROWN, F_TITLE, YELLOW));
        cert.add(Box.createVerticalStrut(20));
        cert.add(again);
        p.add(cert);
        return p;
    }

    // =====================================================================
    //  GAME FLOW
    // =====================================================================

    private void showStart() {
        stopReflexTimers();
        score = 0;
        updateScore();
        hintLabel.setText("Ready? Naam likho aur dimag ki batti jalao! " + BULB);
        cards.show(centerPanel, "START");
        nameField.requestFocusInWindow();
    }

    private void startNewGame() {
        String n = nameField.getText().trim();
        if (n.isEmpty()) {
            showFunnyDialog("Naam?", "Arey! Pehle apna naam toh batao! " + SMILE, RED);
            nameField.requestFocusInWindow();
            return;
        }
        playerName = n;
        score = 0;
        idx = 0;
        updateScore();
        loadItem();
    }

    private void loadItem() {
        stopReflexTimers();
        if (idx >= ITEMS.length) {
            showEndScreen();
            return;
        }
        Item it = ITEMS[idx];
        hintLabel.setText(it.hint);
        if (it.reflex) {
            prepareReflexRound();
            return;
        }
        boolean hasPicture = it.emoji != null;
        quizTitle.setText("<html><div style='text-align:center;width:640px'>" + it.title + "</div></html>");
        quizTitle.setFont(hasPicture ? F_BIG : new Font("Arial", Font.BOLD, 26));
        quizTitle.setForeground(hasPicture ? NEON : TEXT);
        emojiLabel.setText(hasPicture ? it.emoji : "");
        emojiLabel.setVisible(hasPicture);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(it.options[i]);
        }
        questionStartTime = System.currentTimeMillis();
        cards.show(centerPanel, "QUIZ");
    }

    private void onAnswer(int chosen) {
        Item it = ITEMS[idx];
        if (chosen == it.correct) {
            int points = 100;
            String bonusNote = "";
            if (it.speed && (System.currentTimeMillis() - questionStartTime) / 1000 <= 5) {
                points += 50;
                bonusNote = "<br>" + BOLT + " Speed bonus +50!";
            }
            score += points;
            updateScore();
            showFunnyDialog("Sahi Jawab!",
                    winMessage() + (it.explain != null ? "<br><br>" + it.explain : "")
                            + "<br><br>+" + points + " points" + bonusNote, GREEN);
            idx++;
            loadItem();
        } else {
            score = Math.max(0, score - 10);
            updateScore();
            String msg = it.wrong != null ? it.wrong : WRONG_MSGS[random.nextInt(WRONG_MSGS.length)];
            showFunnyDialog("Oops!", msg + "<br><br>(-10 points)", RED);
        }
    }

    // ---------- Reflex "Click Me" round ----------

    private void prepareReflexRound() {
        resetReflexLabels();
        cards.show(centerPanel, "REFLEX");
        showFunnyDialog("Jeeto!",
                "Button har 1.5 second mein bhagega! " + BOLT + "<br>20 second mein usse 3 baar pakdo.<br>Ready, steady, CLICK!",
                NEON);
        startReflexRound();
    }

    private void resetReflexLabels() {
        hits = 0;
        secondsLeft = 20;
        timerLabel.setText("Time left: " + secondsLeft + "s");
        hitsLabel.setText("Hits: " + hits + " / 3");
    }

    private void startReflexRound() {
        stopReflexTimers();
        resetReflexLabels();
        moveTarget();

        moveTimer = new Timer(1500, e -> moveTarget());
        moveTimer.start();

        countdownTimer = new Timer(1000, e -> {
            secondsLeft--;
            timerLabel.setText("Time left: " + secondsLeft + "s");
            if (secondsLeft <= 0) {
                stopReflexTimers();
                score = Math.max(0, score - 10);
                updateScore();
                showFunnyDialog("Time Up!",
                        "Arey! Aapki ungli toh kachhua nikli! " + TURTLE + "<br>Phir koshish karo, Champion! (-10 points)",
                        RED);
                startReflexRound();
            }
        });
        countdownTimer.start();
    }

    private void moveTarget() {
        int w = reflexArea.getWidth() > 0 ? reflexArea.getWidth() : 700;
        int h = reflexArea.getHeight() > 0 ? reflexArea.getHeight() : 380;
        int x = random.nextInt(Math.max(1, w - targetButton.getWidth() - 10)) + 5;
        int y = random.nextInt(Math.max(1, h - targetButton.getHeight() - 10)) + 5;
        targetButton.setLocation(x, y);
        reflexArea.repaint();
    }

    private void onTargetClicked() {
        if (countdownTimer == null || !countdownTimer.isRunning()) {
            return;
        }
        hits++;
        hitsLabel.setText("Hits: " + hits + " / 3");
        if (hits >= 3) {
            stopReflexTimers();
            score += 150;
            updateScore();
            showFunnyDialog("Jeet gaye!",
                    winMessage() + "<br><br>Teen baar pakad liya! +150 points " + PARTY, GREEN);
            idx++;
            loadItem();
        } else {
            moveTarget();
            moveTimer.restart();   // give a full 1.5 seconds before the next jump
        }
    }

    private void stopReflexTimers() {
        if (moveTimer != null) {
            moveTimer.stop();
        }
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }

    private void showEndScreen() {
        hintLabel.setText(CAMERA + " Shabaash! Phone se is certificate ki photo kheench lo!");
        certName.setText(playerName);
        certScore.setText("Final score: " + score);
        cards.show(centerPanel, "END");
    }

    // =====================================================================
    //  HELPERS
    // =====================================================================

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    private String winMessage() {
        return "Wah " + escapeHtml(playerName) + "! Aap toh Chhote Einstein nikle! " + CROWN;
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    /** Dark-themed modal popup used for every funny message. */
    private void showFunnyDialog(String title, String htmlMessage, Color accent) {
        JDialog d = new JDialog(this, title, true);
        JPanel p = new JPanel(new BorderLayout(15, 20));
        p.setBackground(PANEL);
        p.setBorder(new CompoundBorder(new LineBorder(accent, 4, true), new EmptyBorder(25, 35, 25, 35)));

        JLabel msg = new JLabel("<html><div style='text-align:center;width:400px'>" + htmlMessage + "</div></html>",
                SwingConstants.CENTER);
        msg.setFont(F_BIG);
        msg.setForeground(TEXT);

        JButton ok = styledButton("OK", accent);
        ok.addActionListener(e -> d.dispose());

        JPanel btnRow = new JPanel();
        btnRow.setBackground(PANEL);
        btnRow.add(ok);

        p.add(msg, BorderLayout.CENTER);
        p.add(btnRow, BorderLayout.SOUTH);

        d.setContentPane(p);
        d.getRootPane().setDefaultButton(ok);
        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    /** Creates a neon-bordered, dark button with a hover glow. */
    private static JButton styledButton(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFont(F_BIG);
        b.setForeground(accent);
        b.setBackground(BG);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(accent, 3, true), new EmptyBorder(10, 24, 10, 24)));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                b.setBackground(accent);
                b.setForeground(BG);
            }
            @Override public void mouseExited(MouseEvent e) {
                b.setBackground(BG);
                b.setForeground(accent);
            }
        });
        return b;
    }

    // =====================================================================
    //  MAIN
    // =====================================================================

    public static void main(String[] args) {
        try {
            // Metal L&F respects our custom colors on every OS (Windows, Linux, macOS)
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new MindTwistGame().setVisible(true));
    }
}

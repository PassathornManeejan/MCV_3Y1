package app.view;

import app.controller.RegisterController;
import app.model.entity.Subject;
import app.model.entity.Registered;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private final String studentId;
    private final RegisterController controller;

    private JTable eligibleTable;
    private JTable lockedTable;
    private JTable profileTable;
    private JTextArea detailArea;
    private JButton registerBtn;
    private JLabel statusBar;
    private JTabbedPane tabs;

    public MainFrame(String username, String studentId, RegisterController controller) {
        super("Electives Registration – " + username + " (" + studentId + ")");
        this.studentId = studentId;
        this.controller = controller;
        initUI();
        refreshAll();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Top toolbar
        var toolbar = new JToolBar();
        toolbar.setFloatable(false);
        var refreshBtn = new JButton("Refresh");
        registerBtn = new JButton("Register Selected");
        registerBtn.setEnabled(false);
        refreshBtn.addActionListener(e -> refreshAll());
        registerBtn.addActionListener(e -> doRegisterSelected());
        toolbar.add(refreshBtn);
        toolbar.add(registerBtn);
        add(toolbar, BorderLayout.NORTH);

        // Center split: left tables (tabs), right detail
        var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.6);

        tabs = new JTabbedPane();

        // Eligible tab
        eligibleTable = makeSubjectsTable();
        eligibleTable.setToolTipText("Click to select, click again to deselect. Use Register button to confirm.");
        eligibleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int viewRow = eligibleTable.rowAtPoint(e.getPoint());
                int selected = eligibleTable.getSelectedRow();
                if (viewRow == selected && selected != -1) {
                    eligibleTable.clearSelection();
                    registerBtn.setEnabled(false);
                    detailArea.setText("");
                }
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                int row = eligibleTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = eligibleTable.convertRowIndexToModel(row);
                    String id = (String) eligibleTable.getModel().getValueAt(modelRow, 0);
                    controller.getSubjectDetail(id).ifPresent(MainFrame.this::showDetail);
                    registerBtn.setEnabled(true);
                } else {
                    detailArea.setText("");
                    registerBtn.setEnabled(false);
                }
            }
        });

        // Locked tab
        lockedTable = makeLockedTable();
        lockedTable.setToolTipText("Click to view details");
        lockedTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int viewRow = lockedTable.rowAtPoint(e.getPoint());
                int selected = lockedTable.getSelectedRow();
                if (viewRow == selected && selected != -1) {
                    lockedTable.clearSelection();
                    detailArea.setText("");
                }
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                int row = lockedTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = lockedTable.convertRowIndexToModel(row);
                    String id = (String) lockedTable.getModel().getValueAt(modelRow, 0);
                    controller.getSubjectDetail(id).ifPresent(MainFrame.this::showDetail);
                }
            }
        });

        // Profile tab
        profileTable = makeProfileTable();
        profileTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int viewRow = profileTable.rowAtPoint(e.getPoint());
                int selected = profileTable.getSelectedRow();
                if (viewRow == selected && selected != -1) {
                    profileTable.clearSelection();
                    detailArea.setText("");
                }
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                int row = profileTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = profileTable.convertRowIndexToModel(row);
                    String id = (String) profileTable.getModel().getValueAt(modelRow, 0);
                    controller.getSubjectDetail(id).ifPresent(MainFrame.this::showDetail);
                }
            }
        });

        tabs.addTab("Eligible", new JScrollPane(eligibleTable));
        tabs.addTab("Locked (with reasons)", new JScrollPane(lockedTable));
        tabs.addTab("Profile", new JScrollPane(profileTable));
        split.setLeftComponent(tabs);

        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        split.setRightComponent(new JScrollPane(detailArea));

        add(split, BorderLayout.CENTER);

        statusBar = new JLabel("Ready.");
        add(statusBar, BorderLayout.SOUTH);
    }

    private JTable makeSubjectsTable() {
        var model = new DefaultTableModel(new Object[]{"ID","Name","Credits","Max","Current"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        var table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private JTable makeLockedTable() {
        var model = new DefaultTableModel(new Object[]{"ID","Name","Reason"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        var table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private JTable makeProfileTable() {
        var model = new DefaultTableModel(new Object[]{"ID","Name","Credits","Grade"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        var table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private void showDetail(Subject s) {
        String prereq = (s.getPrereqId()==null? "-" : s.getPrereqId());
        String text = String.format(
                "Subject Detail%n" +
                "----------------%n" +
                "ID      : %s%n" +
                "Name    : %s%n" +
                "Credits : %d%n" +
                "Teacher : %s%n" +
                "Prereq  : %s%n" +
                "Max     : %d%n" +
                "Current : %d%n",
                s.getId(), s.getName(), s.getCredits(), s.getTeacher(), prereq, s.getMaxEnroll(), s.getCurrentEnroll()
        );
        detailArea.setText(text);
    }

    private void doRegisterSelected() {
        int row = eligibleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subject from Eligible list.");
            return;
        }
        int modelRow = eligibleTable.convertRowIndexToModel(row);
        String id = (String) eligibleTable.getModel().getValueAt(modelRow, 0);
        var res = controller.register(studentId, id);
        JOptionPane.showMessageDialog(this, res.message(), res.ok()? "Success" : "Failed",
                res.ok()? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        // After successful registration, show Profile tab (align with rule: go back to student's profile)
        tabs.setSelectedIndex(2); // Profile tab
        refreshAll();
    }

    private void refreshAll() {
        // Eligible
        List<Subject> eligible = controller.showAvailableSubjects(studentId);
        var m = (DefaultTableModel) eligibleTable.getModel();
        m.setRowCount(0);
        for (Subject s : eligible) {
            m.addRow(new Object[]{
                    s.getId(), s.getName(), s.getCredits(), s.getMaxEnroll(), s.getCurrentEnroll()
            });
        }

        // Locked
        Map<Subject, String> locked = controller.showLockedSubjects(studentId);
        var lm = (DefaultTableModel) lockedTable.getModel();
        lm.setRowCount(0);
        for (var e : locked.entrySet()) {
            var s = e.getKey();
            lm.addRow(new Object[]{ s.getId(), s.getName(), e.getValue() });
        }

        // Profile (registered subjects)
        var pm = (DefaultTableModel) profileTable.getModel();
        pm.setRowCount(0);
        int totalCredits = 0;
        for (Registered r : controller.getRegistered(studentId)) {
            var subjOpt = controller.getSubjectDetail(r.getSubjectId());
            if (subjOpt.isPresent()) {
                var s = subjOpt.get();
                totalCredits += s.getCredits();
                pm.addRow(new Object[]{ s.getId(), s.getName(), s.getCredits(), (r.getGrade()==null? "-" : r.getGrade().toString()) });
            }
        }

        statusBar.setText("Eligible: " + eligible.size() + "  |  Locked: " + locked.size() + "  |  Total Registered Credits: " + totalCredits);
        registerBtn.setEnabled(eligibleTable.getSelectedRow() >= 0);
    }
}

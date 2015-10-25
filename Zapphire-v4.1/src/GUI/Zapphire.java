package GUI;

import Class.FileManager;
import Compiler.Compiler;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Zapphire extends javax.swing.JFrame {

	public Zapphire() {
		initComponents();
		init();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtbTools = new javax.swing.JToolBar();
        jbNew = new javax.swing.JButton();
        jbOpen = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jbFind = new javax.swing.JButton();
        jbCompile = new javax.swing.JButton();
        jlLineColumn = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jtpTabs = new javax.swing.JTabbedPane();
        jtpOut = new javax.swing.JTabbedPane();
        jpErrors = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaErrors = new javax.swing.JTextArea();
        jpResults = new javax.swing.JPanel();
        jpHashTable = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtHashTable = new javax.swing.JTable();
        jtpCompiler = new javax.swing.JTabbedPane();
        jp1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtLexical = new javax.swing.JTable();
        jp2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtSyntactic = new javax.swing.JTree();
        jp3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtSemantic = new javax.swing.JTree();
        jp4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiNew = new javax.swing.JMenuItem();
        jmiOpen = new javax.swing.JMenuItem();
        jmiSave = new javax.swing.JMenuItem();
        jmiSaveAs = new javax.swing.JMenuItem();
        jmiExit = new javax.swing.JMenuItem();
        jmEdit = new javax.swing.JMenu();
        jmiUndo = new javax.swing.JMenuItem();
        jmiRedo = new javax.swing.JMenuItem();
        jmiCut = new javax.swing.JMenuItem();
        jmiCopy = new javax.swing.JMenuItem();
        jmiPaste = new javax.swing.JMenuItem();
        jmiFind = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jcbmiHidePanelCompiler = new javax.swing.JCheckBoxMenuItem();
        jcbmiHidePanelOut = new javax.swing.JCheckBoxMenuItem();
        jmRun = new javax.swing.JMenu();
        jmiCompile = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JUICY");
        setFont(new java.awt.Font("Bradley Hand ITC", 0, 10)); // NOI18N
        setMinimumSize(new java.awt.Dimension(1200, 600));
        setPreferredSize(new java.awt.Dimension(1200, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jtbTools.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jtbTools.setRollover(true);
        jtbTools.setPreferredSize(new java.awt.Dimension(174, 46));

        jbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/32/new32.png"))); // NOI18N
        jbNew.setToolTipText("Nuevo archivo (Ctrl+N)");
        jbNew.setBorderPainted(false);
        jbNew.setFocusable(false);
        jbNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNewActionPerformed(evt);
            }
        });
        jtbTools.add(jbNew);

        jbOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/32/open32.png"))); // NOI18N
        jbOpen.setToolTipText("Abrir archivo (Ctrl+O)");
        jbOpen.setBorderPainted(false);
        jbOpen.setFocusable(false);
        jbOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenActionPerformed(evt);
            }
        });
        jtbTools.add(jbOpen);

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/32/save32.png"))); // NOI18N
        jbSave.setToolTipText("Guardar archivo (Ctrl+S)");
        jbSave.setBorderPainted(false);
        jbSave.setFocusable(false);
        jbSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });
        jtbTools.add(jbSave);

        jbFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/32/find32.png"))); // NOI18N
        jbFind.setToolTipText("Buscar (Ctrl+F)");
        jbFind.setBorderPainted(false);
        jbFind.setFocusable(false);
        jbFind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbFind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFindActionPerformed(evt);
            }
        });
        jtbTools.add(jbFind);

        jbCompile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/32/compile32.png"))); // NOI18N
        jbCompile.setToolTipText("Compilar(Ctrl+R)");
        jbCompile.setBorderPainted(false);
        jbCompile.setFocusable(false);
        jbCompile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCompile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompileActionPerformed(evt);
            }
        });
        jtbTools.add(jbCompile);

        getContentPane().add(jtbTools, java.awt.BorderLayout.NORTH);

        jlLineColumn.setBackground(new java.awt.Color(19, 139, 191));
        jlLineColumn.setForeground(new java.awt.Color(243, 243, 243));
        jlLineColumn.setText("    linea 1, columna 1");
        jlLineColumn.setToolTipText("");
        jlLineColumn.setOpaque(true);
        getContentPane().add(jlLineColumn, java.awt.BorderLayout.SOUTH);

        jSplitPane2.setDividerLocation(800);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(800, 500));
        jSplitPane2.setPreferredSize(new java.awt.Dimension(1200, 700));

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.75);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(800, 600));
        jSplitPane1.setName(""); // NOI18N
        jSplitPane1.setPreferredSize(new java.awt.Dimension(1000, 700));

        jtpTabs.setBackground(new java.awt.Color(255, 255, 255));
        jtpTabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpTabsStateChanged(evt);
            }
        });
        jSplitPane1.setLeftComponent(jtpTabs);

        jtpOut.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jtpOut.setToolTipText("");
        jtpOut.setMinimumSize(new java.awt.Dimension(400, 100));
        jtpOut.setPreferredSize(new java.awt.Dimension(800, 200));

        jpErrors.setPreferredSize(new java.awt.Dimension(0, 150));

        jScrollPane2.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 0));

        jtaErrors.setEditable(false);
        jtaErrors.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jtaErrors.setRows(5);
        jScrollPane2.setViewportView(jtaErrors);

        javax.swing.GroupLayout jpErrorsLayout = new javax.swing.GroupLayout(jpErrors);
        jpErrors.setLayout(jpErrorsLayout);
        jpErrorsLayout.setHorizontalGroup(
            jpErrorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
        );
        jpErrorsLayout.setVerticalGroup(
            jpErrorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );

        jtpOut.addTab("Errores", jpErrors);

        javax.swing.GroupLayout jpResultsLayout = new javax.swing.GroupLayout(jpResults);
        jpResults.setLayout(jpResultsLayout);
        jpResultsLayout.setHorizontalGroup(
            jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 792, Short.MAX_VALUE)
        );
        jpResultsLayout.setVerticalGroup(
            jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jtpOut.addTab("Resultados", jpResults);

        jtHashTable.setAutoCreateRowSorter(true);
        jtHashTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hash", "Nombre", "Localidad", "LIneas", "Valor", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtHashTable.setRequestFocusEnabled(false);
        jScrollPane4.setViewportView(jtHashTable);

        javax.swing.GroupLayout jpHashTableLayout = new javax.swing.GroupLayout(jpHashTable);
        jpHashTable.setLayout(jpHashTableLayout);
        jpHashTableLayout.setHorizontalGroup(
            jpHashTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHashTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpHashTableLayout.setVerticalGroup(
            jpHashTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpHashTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpOut.addTab("Tabla Hash", jpHashTable);

        jSplitPane1.setBottomComponent(jtpOut);

        jSplitPane2.setLeftComponent(jSplitPane1);

        jtpCompiler.setMinimumSize(new java.awt.Dimension(0, 0));
        jtpCompiler.setPreferredSize(new java.awt.Dimension(400, 300));

        jp1.setMinimumSize(new java.awt.Dimension(400, 300));

        jtLexical.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Token", "Componente", "Linea", "Columna"
            }
        ));
        jScrollPane3.setViewportView(jtLexical);

        javax.swing.GroupLayout jp1Layout = new javax.swing.GroupLayout(jp1);
        jp1.setLayout(jp1Layout);
        jp1Layout.setHorizontalGroup(
            jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );
        jp1Layout.setVerticalGroup(
            jp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpCompiler.addTab("Léxico", jp1);

        jScrollPane1.setViewportView(jtSyntactic);

        javax.swing.GroupLayout jp2Layout = new javax.swing.GroupLayout(jp2);
        jp2.setLayout(jp2Layout);
        jp2Layout.setHorizontalGroup(
            jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );
        jp2Layout.setVerticalGroup(
            jp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpCompiler.addTab("Sintáctico", jp2);

        jScrollPane5.setViewportView(jtSemantic);

        javax.swing.GroupLayout jp3Layout = new javax.swing.GroupLayout(jp3);
        jp3.setLayout(jp3Layout);
        jp3Layout.setHorizontalGroup(
            jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addContainerGap())
        );
        jp3Layout.setVerticalGroup(
            jp3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpCompiler.addTab("Semántico", jp3);

        javax.swing.GroupLayout jp4Layout = new javax.swing.GroupLayout(jp4);
        jp4.setLayout(jp4Layout);
        jp4Layout.setHorizontalGroup(
            jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );
        jp4Layout.setVerticalGroup(
            jp4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );

        jtpCompiler.addTab("Código intermedio", jp4);

        jSplitPane2.setRightComponent(jtpCompiler);

        getContentPane().add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jmFile.setText("Archivo");

        jmiNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jmiNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/new16.png"))); // NOI18N
        jmiNew.setText("Nuevo");
        jmiNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewActionPerformed(evt);
            }
        });
        jmFile.add(jmiNew);

        jmiOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jmiOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/open16.png"))); // NOI18N
        jmiOpen.setText("Abrir");
        jmiOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiOpenActionPerformed(evt);
            }
        });
        jmFile.add(jmiOpen);

        jmiSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jmiSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/save16.png"))); // NOI18N
        jmiSave.setText("Guardar");
        jmiSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveActionPerformed(evt);
            }
        });
        jmFile.add(jmiSave);

        jmiSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jmiSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/save16.png"))); // NOI18N
        jmiSaveAs.setText("Guardar como...");
        jmiSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveAsActionPerformed(evt);
            }
        });
        jmFile.add(jmiSaveAs);

        jmiExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jmiExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/exit16.png"))); // NOI18N
        jmiExit.setText("Salir");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExitActionPerformed(evt);
            }
        });
        jmFile.add(jmiExit);

        jMenuBar1.add(jmFile);

        jmEdit.setText("Editar");

        jmiUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jmiUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/undo16.png"))); // NOI18N
        jmiUndo.setText("Deshacer");
        jmiUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiUndoActionPerformed(evt);
            }
        });
        jmEdit.add(jmiUndo);

        jmiRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jmiRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/redo16.png"))); // NOI18N
        jmiRedo.setText("Rehacer");
        jmiRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRedoActionPerformed(evt);
            }
        });
        jmEdit.add(jmiRedo);

        jmiCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/cut16.png"))); // NOI18N
        jmiCut.setText("Cortar");
        jmEdit.add(jmiCut);

        jmiCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/copy16.png"))); // NOI18N
        jmiCopy.setText("Copiar");
        jmEdit.add(jmiCopy);

        jmiPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/paste16.png"))); // NOI18N
        jmiPaste.setText("Pegar");
        jmEdit.add(jmiPaste);

        jmiFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jmiFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/find16.png"))); // NOI18N
        jmiFind.setText("Buscar");
        jmiFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiFindActionPerformed(evt);
            }
        });
        jmEdit.add(jmiFind);

        jMenuBar1.add(jmEdit);

        jMenu1.setText("Ver");

        jcbmiHidePanelCompiler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jcbmiHidePanelCompiler.setSelected(true);
        jcbmiHidePanelCompiler.setText("Mostrar panel de compilador");
        jcbmiHidePanelCompiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmiHidePanelCompilerActionPerformed(evt);
            }
        });
        jMenu1.add(jcbmiHidePanelCompiler);

        jcbmiHidePanelOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jcbmiHidePanelOut.setSelected(true);
        jcbmiHidePanelOut.setText("Mostrar panel de salida");
        jcbmiHidePanelOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmiHidePanelOutActionPerformed(evt);
            }
        });
        jMenu1.add(jcbmiHidePanelOut);

        jMenuBar1.add(jMenu1);

        jmRun.setText("Ejecutar");

        jmiCompile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jmiCompile.setText("Compilar archivo actual");
        jmiCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCompileActionPerformed(evt);
            }
        });
        jmRun.add(jmiCompile);

        jMenuBar1.add(jmRun);

        jmHelp.setText("Ayuda");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/16/about16.png"))); // NOI18N
        jMenuItem1.setText("Acerca de...");
        jmHelp.add(jMenuItem1);

        jMenuBar1.add(jmHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNewActionPerformed
		new TabPanel( TabPanel.NOT_OPEN_FILE );
    }//GEN-LAST:event_jbNewActionPerformed

    private void jbOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenActionPerformed
		new TabPanel( TabPanel.OPEN_FILE );
    }//GEN-LAST:event_jbOpenActionPerformed

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
		saveFile();
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
		exit();
    }//GEN-LAST:event_jmiExitActionPerformed

    private void jmiSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveAsActionPerformed
		saveFileAs();
    }//GEN-LAST:event_jmiSaveAsActionPerformed

    private void jmiOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiOpenActionPerformed
		new TabPanel( TabPanel.OPEN_FILE );
    }//GEN-LAST:event_jmiOpenActionPerformed

    private void jmiNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNewActionPerformed
		new TabPanel( TabPanel.NOT_OPEN_FILE );
    }//GEN-LAST:event_jmiNewActionPerformed

    private void jmiSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveActionPerformed
		saveFile();
    }//GEN-LAST:event_jmiSaveActionPerformed

    private void jmiUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiUndoActionPerformed

    }//GEN-LAST:event_jmiUndoActionPerformed

    private void jmiRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRedoActionPerformed

    }//GEN-LAST:event_jmiRedoActionPerformed

    private void jbFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFindActionPerformed
		find();
    }//GEN-LAST:event_jbFindActionPerformed

    private void jmiFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiFindActionPerformed
		find();
    }//GEN-LAST:event_jmiFindActionPerformed

    private void jtpTabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpTabsStateChanged
		if ( jtpTabs.getTabCount() != 0 ) {
			enableButtons( true );
			getComponentSelected().updateLineColumn();
		} else {
			enableButtons( false );
		}
    }//GEN-LAST:event_jtpTabsStateChanged

    private void jcbmiHidePanelCompilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbmiHidePanelCompilerActionPerformed
		showPanelCompiler();
    }//GEN-LAST:event_jcbmiHidePanelCompilerActionPerformed

    private void jcbmiHidePanelOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbmiHidePanelOutActionPerformed
		showPanelOut();
    }//GEN-LAST:event_jcbmiHidePanelOutActionPerformed

    private void jbCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompileActionPerformed
		compile();
    }//GEN-LAST:event_jbCompileActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void jmiCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCompileActionPerformed
        compile();
    }//GEN-LAST:event_jmiCompileActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main( String args[] ) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ( "GTK+".equals( info.getName() ) ) {
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				} else if ( "Windows".equals( info.getName() ) ) {
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
			//UIManager.setLookAndFeel( SyntheticaBlackEyeLookAndFeel.class.getName() );
		} catch ( ClassNotFoundException ex ) {
			java.util.logging.Logger.getLogger( Zapphire.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( InstantiationException ex ) {
			java.util.logging.Logger.getLogger( Zapphire.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( IllegalAccessException ex ) {
			java.util.logging.Logger.getLogger( Zapphire.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		} catch ( javax.swing.UnsupportedLookAndFeelException ex ) {
			java.util.logging.Logger.getLogger( Zapphire.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater( new Runnable() {
			public void run() {
				new Zapphire().setVisible( true );
			}
		} );
	}

	private void init() {
		TabPanel.tabbedPane = jtpTabs;
		TabPanel.lineColumn = jlLineColumn;
		fileChooser = new FileChooser();
		setLocationRelativeTo( null );
		//setExtendedState( JFrame.MAXIMIZED_BOTH );
		enableButtons( false );
		setDefaultCloseOperation( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE );
		setIconImage( new ImageIcon( getClass().getResource( "/Icons/zapphire.png" ) ).getImage() );
		jtpTabs.requestFocus();

		jSplitPane2.setDividerLocation( this.getWidth() - ( this.getWidth() / 3 - 50 ) );
		jSplitPane1.setDividerLocation( this.getHeight() / 2 );
            // Abrimos un archivo por defecto
            //TabPanel tabPanel = new TabPanel( "C:\\xd.zph");
            
	}

	/**
	 * Guarda el archivo de la pestaña actual, si se tiene una ruta se guarda en
	 * esa ruta, si no se pide que se seleccione una ruta donde guardarlo
	 *
	 * @return Retorna -1 si se ha cerrado la ventana de seleccion de carpeta de
	 * guardado returna 1 si todo esta correcto
	 */
	private int saveFile() {
		TabPanel tabPanel = getComponentSelected();
		if ( tabPanel.isPathNullFile() ) {		// Significa que el archivo no ha sido guardado
			String path = fileChooser.saveFileChooser();
			if ( path != null ) {
				tabPanel.setFile( new FileManager( path ) );
				tabPanel.setTitleTabAndStyle();		// Colocamos el nombre del archivo a la pestaña
				FileManager.save( path, tabPanel.getText() );
			} else {
				return -1;
			}
		} else {
			tabPanel.setAlterFile( false );
			tabPanel.setStyle( Font.PLAIN, TabPanel.SAVE );
			FileManager.save( tabPanel.getPathFile(), tabPanel.getText() );
		}
		return 1;
	}

	private void saveFileAs() {
		TabPanel tabPanel = getComponentSelected();
		String path = fileChooser.saveFileChooser();
		if ( path != null ) {
			tabPanel.setFile( new FileManager( path ) );
			tabPanel.setTitleTab();
			FileManager.save( path, tabPanel.getText() );
		}
	}

	private void compile() {
		if ( saveFile() == -1 ) {
			return;
		}

		String path = FileManager.getPath( getComponentSelected().getPathFile() );
		//String name = getComponentSelected().getNameFile();
		String name = FileManager.getName( getComponentSelected().getPathFile() );

		// Ejecutamos el compilador
		compiler = new Compiler( path, name );

		// Léxico
		jtpCompiler.setTitleAt( 0, "Léxico | " + name + ".zph" );
		jtpOut.setTitleAt( 0, "Errores | " + name + ".zph" );
		jtLexical.setModel( FileManager.getDefaultTableModel( path + "/" + name + ".lzph" ) );

		// Sintáctico
		if ( compiler.lexicalReady ) {
			jtpCompiler.setTitleAt( 1, "Sintáctico | " + name + ".zph" );
			jtSyntactic.setModel( compiler.syntactic.getDefaultTreeModel() );
			expandAllNodes( jtSyntactic );
		}
		
		// Semántico
		if( compiler.syntacticReady ) {
			jtpOut.setTitleAt( 2, "Tabla Hash | " + name + ".zph" );
			jtHashTable.setModel( compiler.semantic.getDefaultTableModel() );
			jtpCompiler.setTitleAt( 2, "Semántico | " + name + ".zph" );
			jtSemantic.setModel( compiler.semantic.getDefaultTreeModel() );
			expandAllNodes( jtSemantic );
		}

		// Errores
		jtaErrors.setText( FileManager.open( path + "/" + name + ".ezph", false ) );
		jtaErrors.setCaretPosition( 0 );

		if ( !jcbmiHidePanelCompiler.getState() ) {
			jcbmiHidePanelCompiler.setState( true );
			showPanelCompiler();
		}

		if ( !jcbmiHidePanelOut.getState() ) {
			jcbmiHidePanelOut.setState( true );
			showPanelOut();
		}
	}

	private void find() {
		String find = JOptionPane.showInputDialog( null, "Cadena a buscar...", "Buscar...", JOptionPane.INFORMATION_MESSAGE );
		if ( find == null ) {
			return;
		}
		Highlighter h = getHighlighterComponentSelected();
		Pattern pattern = Pattern.compile( "\\b" + find + "\\b" );
		Matcher matcher = pattern.matcher( getComponentSelected().getText() );
		boolean matchFound = matcher.matches();
		if ( !matchFound ) {
			h.removeAllHighlights();
			while ( matcher.find() ) {
				try {
					h.addHighlight( matcher.start(), matcher.end(), DefaultHighlighter.DefaultPainter );
				} catch ( BadLocationException e1 ) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void exit() {
		boolean salir = true;
		TabPanel tabPanel;
		for ( int i = 0; i < jtpTabs.getTabCount(); i++ ) {
			tabPanel = getComponentAt( i );
			if ( tabPanel.isAlterFile() ) {
				int respuesta = JOptionPane.showConfirmDialog( null, "El archivo \"" + tabPanel.getNameFile() + "\" ha sido modificado, ¿Desea guardarlo?", "Guardar archivo...", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE );
				if ( respuesta == JOptionPane.YES_OPTION ) {
					if ( saveFile() == -1 ) {
						salir = false;
						break;
					}
				}
				if ( respuesta == JOptionPane.CANCEL_OPTION ) {
					salir = false;
					break;
				}
			}
		}
		if ( salir ) {
			System.exit( 0 );
		}
	}

	private TabPanel getComponentSelected() {
		return ( ( TabPanel ) jtpTabs.getComponentAt( jtpTabs.getSelectedIndex() ) );
	}

	private TabPanel getComponentAt( int index ) {
		return ( ( TabPanel ) jtpTabs.getComponentAt( index ) );
	}

	private Highlighter getHighlighterComponentSelected() {
		return getComponentSelected().getTextPane().getHighlighter();
	}

	private void showPanelCompiler() {
		if ( jcbmiHidePanelCompiler.getState() ) {
			jtpCompiler.setVisible( true );
			jSplitPane2.setDividerLocation( this.getWidth() / 2 + 250 );
		} else {
			jtpCompiler.setVisible( false );
		}
	}

	private void showPanelOut() {
		if ( jcbmiHidePanelOut.getState() ) {
			jtpOut.setVisible( true );
			jSplitPane1.setDividerLocation( this.getHeight() / 2 );
		} else {
			jtpOut.setVisible( false );
		}
	}

	private void enableButtons( boolean enable ) {
		jbCompile.setEnabled( enable );
		jbSave.setEnabled( enable );
		jbFind.setEnabled( enable );

		jmiSave.setEnabled( enable );
		jmiSaveAs.setEnabled( enable );

		jmiUndo.setEnabled( enable );
		jmiRedo.setEnabled( enable );
		jmiCut.setEnabled( enable );
		jmiCopy.setEnabled( enable );
		jmiPaste.setEnabled( enable );
		jmiFind.setEnabled( enable );
	}

	private void expandAllNodes( JTree tree ) {
		for ( int i = 0; i < tree.getRowCount(); i++ ) {
			tree.expandRow( i );
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JButton jbCompile;
    private javax.swing.JButton jbFind;
    private javax.swing.JButton jbNew;
    private javax.swing.JButton jbOpen;
    private javax.swing.JButton jbSave;
    private javax.swing.JCheckBoxMenuItem jcbmiHidePanelCompiler;
    private javax.swing.JCheckBoxMenuItem jcbmiHidePanelOut;
    private javax.swing.JLabel jlLineColumn;
    private javax.swing.JMenu jmEdit;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenu jmRun;
    private javax.swing.JMenuItem jmiCompile;
    private javax.swing.JMenuItem jmiCopy;
    private javax.swing.JMenuItem jmiCut;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiFind;
    private javax.swing.JMenuItem jmiNew;
    private javax.swing.JMenuItem jmiOpen;
    private javax.swing.JMenuItem jmiPaste;
    private javax.swing.JMenuItem jmiRedo;
    private javax.swing.JMenuItem jmiSave;
    private javax.swing.JMenuItem jmiSaveAs;
    private javax.swing.JMenuItem jmiUndo;
    private javax.swing.JPanel jp1;
    private javax.swing.JPanel jp2;
    private javax.swing.JPanel jp3;
    private javax.swing.JPanel jp4;
    private javax.swing.JPanel jpErrors;
    private javax.swing.JPanel jpHashTable;
    private javax.swing.JPanel jpResults;
    private javax.swing.JTable jtHashTable;
    private javax.swing.JTable jtLexical;
    private javax.swing.JTree jtSemantic;
    private javax.swing.JTree jtSyntactic;
    private javax.swing.JTextArea jtaErrors;
    private javax.swing.JToolBar jtbTools;
    private javax.swing.JTabbedPane jtpCompiler;
    private javax.swing.JTabbedPane jtpOut;
    private javax.swing.JTabbedPane jtpTabs;
    // End of variables declaration//GEN-END:variables
	private FileChooser fileChooser;
	private Compiler compiler;
}

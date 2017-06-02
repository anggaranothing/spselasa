/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import com.anggaranothing.pakar.dyslexia.model.Symptom;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.anggaranothing.pakar.dyslexia.dao.IfSymptomDao;

/**
 *
 * @author AnggaraNothing
 */
public class FrameSymptom extends javax.swing.JInternalFrame {

    private List<Symptom>        modelRecord     = new ArrayList<Symptom>();
    private IfSymptomDao         objectDao       = null;
    private ListIterator        findIter;
    private int                 lastFindIndex;
    private boolean             isEditRecord    = false;
    
    private final int           maxTableRows    = 10;
    
    /**
     * Creates new form FrameSymptom
     */
    public FrameSymptom() {
        initComponents();
        
        tblRecord.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                lblSelectedRow.setText( String.valueOf( tblRecord.getSelectedRow() + 1 ) );
                lblPageNumber.setText(  String.valueOf( jSPage.getValue() ) );
            }
        });
        
    }
    
    // method untuk mengambil referensi remote objek
    private void initDao() {  
        String url = String.format( "rmi://%s:%d/%s" , ClientConfig.getRmiHost() , ClientConfig.getRmiPort() , ClientConfig.RMI_SYMPTOM_NAME );
        jBusyLabel.setVisible( true );
        try {
            
            // ambil referensi dari remote object yg ada di server
            objectDao = ( IfSymptomDao ) Naming.lookup(url);
            
        } catch (NotBoundException ex) {
            Helper.logThrowable( "FrameSymptom initDao() failed!", ex );
        } catch (MalformedURLException ex) {
            Helper.logThrowable( "FrameSymptom initDao() failed!", ex );
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to connect to the server !", ex );
        }
        jBusyLabel.setVisible( false );
    }

    // isi semua record DAO ke dalam record model
    private void loadRecord() {
        jBusyLabel.setVisible( true );
        
        try {
            modelRecord = objectDao.read();
            updateMaxPage();
            findIter = modelRecord.listIterator();
            btnAdd.setEnabled( true );
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to read record from the server !", ex );
        } catch(java.lang.NullPointerException ex) {
            Helper.logThrowable( "FrameSymptom loadRecord() failed!", ex );
        }
        
        jBusyLabel.setVisible( false );
    }
    
    // isi tabel dengan record model
    private void loadTable( int pageNumber ) {
        jBusyLabel.setVisible( true );
        
        DefaultTableModel temp = (DefaultTableModel) tblRecord.getModel();
        temp.setRowCount( 0 );
        
        // ekstrak data customer yg ada di dalam objek list
        // kemudian menampilkannya ke objek tabel
        if(      pageNumber > getMaxPages() ) pageNumber = getMaxPages();
        else if( pageNumber < 0 ) pageNumber = 1;
        
        int firstIndex = maxTableRows * --pageNumber,
            lastIndex  = firstIndex + maxTableRows;
        
        if( lastIndex > modelRecord.size() ) lastIndex = modelRecord.size();
        
        for( int i = firstIndex; i < lastIndex; i++ )
            fillToTable(false, modelRecord.get( i ), temp );
        
        /*for( User user : modelRecord ) {
            fillToTable( false, user , temp );
        }*/
        
        tblRecord.setModel( temp );
        
        tblRecord.setEnabled( true );
        tblRecord.requestFocus();
        tblRecord.changeSelection( 0, 0, false, false );
        
        jBusyLabel.setVisible( false );
    }
    
    // method untuk menampilkan data model ke tabel
    private void fillToTable( boolean isEditData, Symptom symp , DefaultTableModel tableModel ) {
        if ( !isEditData ) { // data baru
            Object[] objects = new Object[ tableModel.getColumnCount() ];

            objects[0] = symp.getId();
            objects[1] = symp.getDescription();

            // tambahkan data model ke dalam tabel
            tableModel.addRow( objects );

        } else { // edit data
            // ambil nilai baris yang dipilih
            int row = tblRecord.getSelectedRow();

            if( row > -1 ) {
                // update data customer yg ada di tabel
                tableModel.setValueAt( symp.getId(),   row, 0);
                tableModel.setValueAt( symp.getDescription(), row, 1);
            }
        }
    }
    
    private void updateMaxPage() {
        int maxPage = getMaxPages();
        lblMaxPage.setText( String.valueOf( maxPage ) );
        SpinnerNumberModel spModel = (SpinnerNumberModel) jSPage.getModel();
        spModel.setMaximum( maxPage );
        jSPage.setModel( spModel );
    }
    
    private int getPageOnTableIndex( int index ) {
        return (int) Math.max( Math.ceil( ( (double) index ) / ( (double) (maxTableRows-1) ) ) , 1 );
    }
    
    private int getMaxPages() {
        return getPageOnTableIndex( modelRecord.size() - 1 );
    }
    
    private int getRecordIndexOnTable() {
        return ( ( ( (int) jSPage.getValue() ) - 1 ) * maxTableRows ) + tblRecord.getSelectedRow();
    }
    
    private boolean isTableReady() {
        return !jBusyLabel.isVisible();
    }
    
    private boolean isTableEmpty() {
        return tblRecord.getRowCount() <= 0;
    }
    
    private boolean isRecordEmpty() {
        return modelRecord.size() <= 0;
    }
    
    private void setPageSpinner( Object value ) {
        if( value == null ) return;
        jSPage.setValue( value );
    }
    
    private void setFindStatusText( boolean isClear , boolean isOnTop ) {
        String arg1 = isOnTop ? "top" : "end";
        String status = String.format( "Reached %s of the page.", arg1 );
        if( isClear ) status = "";
        lblFindStatus.setText( status );
        
    }
    
    private void doFindKeyword( String keyword , boolean isPrevious ) {
        if( !isTableReady() || findIter == null ) return;
        
        jBusyLabel.setVisible( true );
        
        new Thread(new Runnable(){ public void run(){
            
            boolean isId               = keyword.toLowerCase().startsWith( "c" ) && Character.isDigit( keyword.charAt(1) );
            boolean isOkayToChangePage = false;
            boolean isFound            = false;
            int selectedColumn         = 1;
            int tableSelectedIndex     = getRecordIndexOnTable();
            
            // ngatur posisi iterator menuju ke selected table index
            if( tableSelectedIndex != lastFindIndex )
            {
                findIter = modelRecord.listIterator();
                int currentIndex = 0;
                while( currentIndex != tableSelectedIndex ) {
                    findIter.next();
                    currentIndex = findIter.nextIndex()-1;
                }
            }
            
            boolean whileCondition = !isPrevious ? findIter.hasNext() : findIter.hasPrevious();
            
            while( whileCondition ) {
                lastFindIndex = !isPrevious ? findIter.nextIndex()-1 : findIter.previousIndex()+1;
                
                // find next
                if( !isPrevious ) {  
                    if( lastFindIndex <= tableSelectedIndex && findIter.hasNext() ) {
                        findIter.next();
                        continue;
                    }
                }
                // find previous
                else {
                    if( lastFindIndex >= tableSelectedIndex && findIter.hasPrevious() ) {
                        findIter.previous();
                        continue;
                    }
                }
                
                setFindStatusText( true, false );
                
                Symptom symp = (Symptom) modelRecord.get( lastFindIndex );
                
                // find by Description
                if( !isId ) {
                    if( symp.getDescription().toLowerCase().contains( keyword.toLowerCase() ) ) {
                        isFound = true;
                        isOkayToChangePage = true;
                    }
                }
                // find by ID
                else {
                    if( symp.getId().toLowerCase().contains( keyword.toLowerCase() ) ) {
                        isFound = true;
                        isOkayToChangePage = true;
                    }
                }
                
                // store current index
                tableSelectedIndex = lastFindIndex;
                
                // find next
                if( !isPrevious ) {
                    // sudah dalam batas pencarian
                    if( !findIter.hasNext() ) {
                        setFindStatusText( false, false );
                        break;
                    }
                }
                // find previous
                else {
                    // sudah dalam batas pencarian
                    if( !findIter.hasPrevious() ) {
                        setFindStatusText( false, true );
                        break;
                    }
                }
                
                // jika sudah ketemu, langsung makan kitkat
                if( isFound ) break;
            }
            
            if( isOkayToChangePage ) {
                setPageSpinner( getPageOnTableIndex( tableSelectedIndex ) );
                int selectedIndex = tableSelectedIndex - ( ( getPageOnTableIndex(tableSelectedIndex ) - 1 ) * maxTableRows );
                tblRecord.changeSelection( selectedIndex, selectedColumn, false, false );
            }
        }}).start();
            
        jBusyLabel.setVisible( false );
    }
    
    private void doAddEditRecord() {
        // tblRecord.setEnabled( false );
        
        Symptom sympObj = new Symptom();
        sympObj.setId(           "C" + tfDlgId.getText() );
        sympObj.setDescription(  Helper.nullifyEmpty( epDlgDesc.getText() ) );
        
        try {
            if( !isEditRecord )
                objectDao.create(sympObj);
            else
                objectDao.update(sympObj);
            
            new Thread(new Runnable() { public void run(){
                loadRecord();
                int page = getMaxPages();
                if( isEditRecord ) {
                    page = (int) jSPage.getValue();
                }
                loadTable( page );
                setPageSpinner( page );
            }}).start();
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to insert/update record to the server !", ex );
        }
        
        tblRecord.setEnabled( true );
    }
    
    private void doDeleteRecord() {
        // tblRecord.setEnabled( false );
        
        Symptom sympObj = new Symptom();
        sympObj.setId( modelRecord.get( getRecordIndexOnTable() ).getId() );
        
        try {
            objectDao.delete( sympObj );
            
            new Thread(new Runnable() { public void run(){
                loadRecord();
                loadTable( (int) jSPage.getValue() );
                setPageSpinner( jSPage.getValue() );
            }}).start();
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to delete record from the server !", ex );
        }
        
        tblRecord.setEnabled( true );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogAddEdit = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        tfDlgId = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnDlgCancel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnDlgConfirm = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        epDlgDesc = new javax.swing.JEditorPane();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecord = new javax.swing.JTable();
        btnPrev = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jBusyLabel = new org.jdesktop.swingx.JXBusyLabel();
        jSPage = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblMaxPage = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfFind = new javax.swing.JTextField();
        btnFindNext = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        lblFindStatus = new javax.swing.JLabel();
        btnFindPrev = new javax.swing.JButton();
        jXTaskPane2 = new org.jdesktop.swingx.JXTaskPane();
        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblSelectedRow = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblPageNumber = new javax.swing.JLabel();
        btnReload = new javax.swing.JButton();

        dialogAddEdit.setTitle("Edit Selected Record");
        dialogAddEdit.setMinimumSize(new java.awt.Dimension(360, 405));
        dialogAddEdit.setModal(true);
        dialogAddEdit.setResizable(false);
        dialogAddEdit.setSize(new java.awt.Dimension(360, 405));
        dialogAddEdit.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                dialogAddEditComponentShown(evt);
            }
        });
        dialogAddEdit.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dialogAddEditWindowClosing(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("ID");

        tfDlgId.setEditable(false);
        tfDlgId.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfDlgId.setText("jTextField1");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("Description");

        btnDlgCancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDlgCancel.setText("Cancel");
        btnDlgCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDlgCancelActionPerformed(evt);
            }
        });

        btnDlgConfirm.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDlgConfirm.setText("Confirm");
        btnDlgConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDlgConfirmActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(epDlgDesc);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("C");

        javax.swing.GroupLayout dialogAddEditLayout = new javax.swing.GroupLayout(dialogAddEdit.getContentPane());
        dialogAddEdit.getContentPane().setLayout(dialogAddEditLayout);
        dialogAddEditLayout.setHorizontalGroup(
            dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(dialogAddEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogAddEditLayout.createSequentialGroup()
                        .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dialogAddEditLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(34, 34, 34))
                            .addGroup(dialogAddEditLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(21, 21, 21)))
                        .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .addGroup(dialogAddEditLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfDlgId))))
                    .addGroup(dialogAddEditLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDlgConfirm)
                        .addGap(18, 18, 18)
                        .addComponent(btnDlgCancel)))
                .addContainerGap())
        );
        dialogAddEditLayout.setVerticalGroup(
            dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAddEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfDlgId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogAddEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDlgCancel)
                    .addComponent(btnDlgConfirm))
                .addContainerGap())
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Symptom Data");
        setToolTipText("");
        setMinimumSize(new java.awt.Dimension(600, 540));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tblRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRecord.setColumnSelectionAllowed(true);
        tblRecord.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblRecord.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblRecord);
        tblRecord.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblRecord.getColumnModel().getColumnCount() > 0) {
            tblRecord.getColumnModel().getColumn(0).setMinWidth(40);
            tblRecord.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        btnPrev.setText("Prev.");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnFirst.setText("First");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText("Last");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        jBusyLabel.setText("Loading...");
        jBusyLabel.setBusy(true);

        jSPage.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1, 1));
        jSPage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSPageStateChanged(evt);
            }
        });

        jLabel1.setText("Page");

        jLabel2.setText("of");

        lblMaxPage.setText("%d");

        jScrollPane2.setAutoscrolls(true);

        org.jdesktop.swingx.VerticalLayout verticalLayout4 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout4.setGap(14);
        jXTaskPaneContainer1.setLayout(verticalLayout4);

        jLabel3.setText("Find");

        tfFind.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfFind.setToolTipText("E-mail or Name");
        tfFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfFindActionPerformed(evt);
            }
        });

        btnFindNext.setText("Find Next");
        btnFindNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindNextActionPerformed(evt);
            }
        });

        jLabel4.setText(":");

        lblFindStatus.setText(" ");

        btnFindPrev.setText("Find Prev.");
        btnFindPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindPrevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFindStatus)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tfFind, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFindPrev)
                        .addGap(18, 18, 18)
                        .addComponent(btnFindNext)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(tfFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindNext)
                    .addComponent(btnFindPrev))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFindStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXTaskPane1.getContentPane().add(jPanel1);

        jXTaskPaneContainer1.add(jXTaskPane1);
        jXTaskPane1.setTitle( "Cari" );
        jXTaskPane1.setCollapsed( true );

        btnAdd.setText("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText(":");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Selected Row");

        lblSelectedRow.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSelectedRow.setText("%d");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Page Number");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText(":");

        lblPageNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPageNumber.setText("%d");

        btnReload.setText("Reload");
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedRow))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(lblPageNumber)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(btnReload)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(lblSelectedRow))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(lblPageNumber)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jXTaskPane2.getContentPane().add(jPanel2);

        jXTaskPaneContainer1.add(jXTaskPane2);
        jXTaskPane2.setTitle( "Command" );
        jXTaskPane2.setCollapsed( false );

        jScrollPane2.setViewportView(jXTaskPaneContainer1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBusyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSPage, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMaxPage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBusyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLast)
                        .addComponent(btnNext)
                        .addComponent(btnPrev)
                        .addComponent(btnFirst)
                        .addComponent(jSPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(lblMaxPage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSPageStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSPageStateChanged
        // TODO add your handling code here:
        Object spinnerValue = jSPage.getValue();
        if( !isTableReady() || spinnerValue == null ) return;
        loadTable( (Integer) spinnerValue );
    }//GEN-LAST:event_jSPageStateChanged

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        setPageSpinner( jSPage.getPreviousValue() );
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        setPageSpinner( jSPage.getNextValue() );
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        setPageSpinner( 1 );
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        setPageSpinner( getMaxPages() );
    }//GEN-LAST:event_btnLastActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        btnAdd.setEnabled( false );
        new Thread(new Runnable() {
            public void run(){
                initDao();
                loadRecord();
                loadTable( 1 );
                setPageSpinner( 1 );
            }
        }).start();
        
    }//GEN-LAST:event_formComponentShown

    private void tfFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfFindActionPerformed
        // TODO add your handling code here:
        btnFindNext.doClick();
    }//GEN-LAST:event_tfFindActionPerformed

    private void btnFindPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindPrevActionPerformed
        // TODO add your handling code here:
        doFindKeyword( tfFind.getText() , true );
    }//GEN-LAST:event_btnFindPrevActionPerformed

    private void btnFindNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindNextActionPerformed
        // TODO add your handling code here:
        doFindKeyword( tfFind.getText() , false );
    }//GEN-LAST:event_btnFindNextActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if( isTableEmpty() || isRecordEmpty() || tblRecord.getSelectedRow() == -1 ) {
            JOptionPane.showMessageDialog( this,
                    "No record loaded or selected",
                    null, JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        if( JOptionPane.showConfirmDialog( this, "Are you sure ?", "Delete Record Confirmation", JOptionPane.YES_NO_OPTION ) == 0 ) {
            doDeleteRecord();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if( isTableEmpty() || isRecordEmpty() || tblRecord.getSelectedRow() == -1 ) {
            JOptionPane.showMessageDialog( this,
                    "No record loaded or selected",
                    null, JOptionPane.WARNING_MESSAGE );
            return;
        }
        isEditRecord = true;
        dialogAddEdit.setVisible( true );
        dialogAddEdit.setLocationRelativeTo( this );
        dialogAddEdit.setModal( true );
        dialogAddEdit.pack();
    }//GEN-LAST:event_btnEditActionPerformed

    private void dialogAddEditWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogAddEditWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_dialogAddEditWindowClosing

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        isEditRecord = false;
        dialogAddEdit.setVisible( true );
        dialogAddEdit.setLocationRelativeTo( this );
        dialogAddEdit.setModal( true );
        dialogAddEdit.pack();
    }//GEN-LAST:event_btnAddActionPerformed

    private void dialogAddEditComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dialogAddEditComponentShown
        // TODO add your handling code here:
        String dlgTitle = "Add New Record",
               dlgId    = "",
               dlgNama  = "";
        
        if( getRecordIndexOnTable() >= 0 ) {
            dlgId = Helper.padWithZero( Integer.valueOf( Helper.removeAllNonNumber( modelRecord.get( modelRecord.size()-1 ).getId() ) ) + 1 , 5 );
            if( isEditRecord ) {
                dlgTitle = "Edit Selected Record";
                dlgId    = Helper.removeAllNonNumber( modelRecord.get( getRecordIndexOnTable() ).getId() );
                dlgNama  = modelRecord.get( getRecordIndexOnTable() ).getDescription();
            }
        }
        dialogAddEdit.setTitle( dlgTitle );
        tfDlgId.setText(    dlgId );
        epDlgDesc.setText(  dlgNama );
    }//GEN-LAST:event_dialogAddEditComponentShown

    private void btnDlgCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDlgCancelActionPerformed
        // TODO add your handling code here:
        dialogAddEdit.dispatchEvent(new WindowEvent( dialogAddEdit, WindowEvent.WINDOW_CLOSING ) );
    }//GEN-LAST:event_btnDlgCancelActionPerformed

    private void btnDlgConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDlgConfirmActionPerformed
        // name is empty?
        if( Helper.nullifyEmpty( epDlgDesc.getText() ) == null ) {
            JOptionPane.showMessageDialog( this,
                 "Description is empty",
                null, JOptionPane.WARNING_MESSAGE );
            
            return;
        }

        if( JOptionPane.showConfirmDialog( this, "Are you sure ?", dialogAddEdit.getTitle() + " Confirmation", JOptionPane.YES_NO_OPTION ) == 0 ) {
            doAddEditRecord();
            dialogAddEdit.dispatchEvent(new WindowEvent( dialogAddEdit, WindowEvent.WINDOW_CLOSING ) );
        }
    }//GEN-LAST:event_btnDlgConfirmActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        Main.frameMap.remove( getClass().getName() );
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        // TODO add your handling code here:
        this.setVisible( false );
        this.setVisible( true );
    }//GEN-LAST:event_btnReloadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDlgCancel;
    private javax.swing.JButton btnDlgConfirm;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFindNext;
    private javax.swing.JButton btnFindPrev;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnReload;
    private javax.swing.JDialog dialogAddEdit;
    private javax.swing.JEditorPane epDlgDesc;
    private org.jdesktop.swingx.JXBusyLabel jBusyLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSPage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane2;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JLabel lblFindStatus;
    private javax.swing.JLabel lblMaxPage;
    private javax.swing.JLabel lblPageNumber;
    private javax.swing.JLabel lblSelectedRow;
    private javax.swing.JTable tblRecord;
    private javax.swing.JTextField tfDlgId;
    private javax.swing.JTextField tfFind;
    // End of variables declaration//GEN-END:variables
}

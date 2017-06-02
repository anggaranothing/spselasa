/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anggaranothing.pakar.dyslexia.client;

import com.anggaranothing.pakar.dyslexia.dao.IfCfLevelDao;
import com.anggaranothing.pakar.dyslexia.dao.IfDiagnosticDao;
import com.anggaranothing.pakar.dyslexia.dao.IfSymptomDao;
import com.anggaranothing.pakar.dyslexia.model.CfLevel;
import com.anggaranothing.pakar.dyslexia.model.Diagnostic;
import com.anggaranothing.pakar.dyslexia.model.Disease;
import com.anggaranothing.pakar.dyslexia.model.Symptom;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JRadioButton;

/**
 *
 * @author AnggaraNothing
 */
public class FrameStartDiagnostic extends javax.swing.JInternalFrame {

    private List<Symptom>               modelRecord     = new ArrayList<Symptom>();
    private IfSymptomDao                objectDao       = null;
    
    private IfCfLevelDao                cflDao          = null;
    private List<CfLevel>               cflRecord       = new ArrayList<CfLevel>();
    
    private IfDiagnosticDao             dgnDao          = null;
    private Map<Disease,Float>          cflResult       = null;
    
    private final List<JRadioButton>    radioList       = new ArrayList<JRadioButton>();
    private final Map<Symptom,Integer>  cflUserMap      = new LinkedHashMap<Symptom,Integer>();
    
    private final DefaultListModel      listModel       = new DefaultListModel();
    private boolean                     isDone          = false;
    
    /**
     * Creates new form FrameStartDiagnostic
     */
    public FrameStartDiagnostic() {
        initComponents();
    }

    private boolean isFormReady() {
        return !busyLabel.isVisible();
    }
    
    private boolean isRecordEmpty() {
        return modelRecord.isEmpty() || cflRecord.isEmpty();
    }
    
    private void initDao() {  
        String url = String.format( "rmi://%s:%d/%s" , ClientConfig.getRmiHost() , ClientConfig.getRmiPort() , ClientConfig.RMI_SYMPTOM_NAME );
        busyLabel.setVisible( true );
        try {
            
            // ambil referensi dari remote object yg ada di server
            objectDao = ( IfSymptomDao ) Naming.lookup(url);
            
            url = String.format( "rmi://%s:%d/%s" , ClientConfig.getRmiHost() , ClientConfig.getRmiPort() , ClientConfig.RMI_CFLEVEL_NAME );
            cflDao    = ( IfCfLevelDao ) Naming.lookup(url);
            
            url = String.format( "rmi://%s:%d/%s" , ClientConfig.getRmiHost() , ClientConfig.getRmiPort() , ClientConfig.RMI_DIAGNOSTIC_NAME );
            dgnDao    = ( IfDiagnosticDao ) Naming.lookup(url);
            
        } catch (NotBoundException ex) {
            Helper.logThrowable( "FrameUserDiagnostic initDao() failed!", ex );
        } catch (MalformedURLException ex) {
            Helper.logThrowable( "FrameUserDiagnostic initDao() failed!", ex );
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to connect to the server !", ex );
        }
        busyLabel.setVisible( false );
    }
    
    private void loadRecord() {
        busyLabel.setVisible( true );
        
        try {
            modelRecord = objectDao.readOrderByCount();
            cflRecord   = cflDao.read();
        } catch (RemoteException ex) {
            Helper.ShowDialogAndLogThrowable( this, "FAILED to read record from the server !", ex );
        } catch(java.lang.NullPointerException ex) {
            Helper.logThrowable( "FrameUserDiagnostic loadRecord() failed!", ex );
        }
        
        busyLabel.setVisible( false );
    }
    
    private void loadList() {
        busyLabel.setVisible( true );
        
        listSmp.clearSelection();
        listModel.clear();
        for( Symptom smp : modelRecord ) {
            listModel.addElement( smp.getId() );
        }
        listSmp.setModel(listModel);
        
        busyLabel.setVisible( false );
    }
    
    private void loadRadioButton() {
        busyLabel.setVisible( true );
        
        if( panelOption.getComponentCount() > 0 ) {
            panelOption.removeAll();
        }
        
        int iterator = 0;
        for( CfLevel cfl : cflRecord ) {
            JRadioButton temp = new JRadioButton( cfl.getDescription(), false );
            
            temp.setToolTipText( String.valueOf( iterator ) );
            temp.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jRadioButtonItemStateChanged(evt);
                }
            });
            
            btnOptionGroup.add( temp );
            panelOption.add( temp );
            
            radioList.add( temp );
            
            iterator++;
        }
        
        // Segarkan kembali mata anda
        //panelOption.revalidate();
        //panelOption.repaint();
        
        //this.pack();
        
        busyLabel.setVisible( false );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOptionGroup = new javax.swing.ButtonGroup();
        busyLabel = new org.jdesktop.swingx.JXBusyLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listSmp = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        taSymptomDesc = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        panelOption = new javax.swing.JPanel();
        jRadioButton = new javax.swing.JRadioButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Diagnostic Form");
        setMinimumSize(new java.awt.Dimension(510, 311));
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
                formInternalFrameOpened(evt);
            }
        });

        busyLabel.setText("Loading...");
        busyLabel.setBusy(true);

        listSmp.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listSmp.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSmpValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listSmp);

        taSymptomDesc.setEditable(false);
        taSymptomDesc.setColumns(20);
        taSymptomDesc.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        taSymptomDesc.setLineWrap(true);
        taSymptomDesc.setRows(5);
        taSymptomDesc.setText("Selamat datang di form Diagnosis Penyakit Dyslexia\n\nTekan tombol Lanjut untuk memulai...");
        jScrollPane2.setViewportView(taSymptomDesc);

        jLabel1.setText("Daftar Gejala");

        panelOption.setBorder(javax.swing.BorderFactory.createTitledBorder("Option(s)"));
        panelOption.setLayout(new java.awt.GridBagLayout());

        jRadioButton.setText("jRadioButton1");
        jRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonItemStateChanged(evt);
            }
        });
        panelOption.add(jRadioButton, new java.awt.GridBagConstraints());

        btnNext.setText("Lanjut");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setText("Sebelum");
        btnPrev.setEnabled(false);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnCancel.setText("Batal");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(busyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 263, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrev)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap())
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelOption, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNext)
                            .addComponent(btnPrev)
                            .addComponent(btnCancel)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        Main.frameMap.remove( getClass().getName() );
        
        if( !isRecordEmpty() && listSmp.getSelectedIndex() > -1 ) {
            
            Diagnostic dgnResult = new Diagnostic();
            dgnResult.setUser( Main.userSession.getUser() );
            dgnResult.setDate( Helper.getCurrentSystemTimestamp() );

            Map<Symptom,Float> cflMap = new LinkedHashMap<Symptom,Float>();
        
            // Diagnostic is finished?
            if( isDone ) {
                dgnResult.setStatus( Diagnostic.StatusEnum.done );
                
                String cfUser = "";
                for( Symptom smp : modelRecord ) {
                    int cflId = cflUserMap.get( smp ) == null? 0 : cflUserMap.get( smp );
                    cflMap.put( smp, cflRecord.get(cflId).getValue() );
                    cfUser += String.format( "%s=%.2f " , smp.getId(), cflRecord.get(cflId).getValue());
                }
                dgnResult.setCfUser( cfUser );
            }
            // canceled?
            else {
                dgnResult.setStatus( Diagnostic.StatusEnum.cancel );
                dgnResult.setCfUser(       "CANCELLED" );
                dgnResult.setCfCombined(   "CANCELLED" );
                dgnResult.setCfPercentage( "CANCELLED" );
                
            }
            
            try {
                cflResult = dgnDao.sendForm( dgnResult, cflMap );
                
                if( isDone )
                    Helper.attachToDesktop( (javax.swing.JDesktopPane) this.getParent(), new FrameDiagnosticResult( cflResult ), true );
            } catch (RemoteException ex) {
            }
        }
    }//GEN-LAST:event_formInternalFrameClosing

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        new Thread(new Runnable() {
            public void run(){
                initDao();
                loadRecord();
                loadList();
                loadRadioButton();
            }
        }).start();
    }//GEN-LAST:event_formInternalFrameOpened

    private void jRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonItemStateChanged
        if( !isFormReady() || listSmp.getSelectedIndex() == -1 ) {
            btnOptionGroup.clearSelection();
            return;
        }
        
        if( ((JRadioButton) evt.getSource()).isSelected() ) {
            cflUserMap.put( modelRecord.get( listSmp.getSelectedIndex() ), Integer.parseInt(((JRadioButton) evt.getSource()).getToolTipText()) );
            
            //System.out.println( modelRecord.get( listSmp.getSelectedIndex() ).getId() +" "+ Float.parseFloat(((JRadioButton) evt.getSource()).getToolTipText()) );
        }
    }//GEN-LAST:event_jRadioButtonItemStateChanged

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        isDone = false;
        this.doDefaultCloseAction();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if( !isFormReady() || isRecordEmpty() )
            return;
        
        int prevId = listSmp.getSelectedIndex()-1;
        if( prevId > -1 )
            listSmp.setSelectedIndex( prevId );
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if( !isFormReady() || isRecordEmpty() )
            return;
        
        int selectedId = listSmp.getSelectedIndex();
        
        if( selectedId == listSmp.getModel().getSize()-1 ) {
            isDone = true;
            this.doDefaultCloseAction();
            return;
        }
        
        int nextId = selectedId+1;
        if( nextId < listSmp.getModel().getSize() )
            listSmp.setSelectedIndex( nextId );
    }//GEN-LAST:event_btnNextActionPerformed

    private void listSmpValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listSmpValueChanged
        // TODO add your handling code here:
        if( !isFormReady() || isRecordEmpty() )
            return;
        
        int selectedIndex = listSmp.getSelectedIndex();
        
        if( selectedIndex == -1 ) {
            taSymptomDesc.setText( "Selamat datang di form Diagnosis Penyakit Dyslexia\n" +
                                   "\n" +
                                   "Tekan tombol Lanjut untuk memulai..." );
            return;
        }
        
        taSymptomDesc.setText( modelRecord.get(selectedIndex).getDescription() );
        listSmp.ensureIndexIsVisible( selectedIndex );
        btnOptionGroup.clearSelection();
        
        if( cflUserMap.containsKey( modelRecord.get(selectedIndex) ) ) {
            radioList.get( cflUserMap.get( modelRecord.get(selectedIndex) ) ).setSelected( true );
        }
        else {
            radioList.get( radioList.size()/2 ).setSelected( true );
        }
        
        if( selectedIndex <= 0 ) {
            btnPrev.setEnabled( false );
        }
        else if( selectedIndex >= modelRecord.size()-1 )
        {
            btnNext.setText( "Selesai" );
        }
        else {
            btnPrev.setEnabled( true );
            btnNext.setText( "Lanjut" );
        }
    }//GEN-LAST:event_listSmpValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNext;
    private javax.swing.ButtonGroup btnOptionGroup;
    private javax.swing.JButton btnPrev;
    private org.jdesktop.swingx.JXBusyLabel busyLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listSmp;
    private javax.swing.JPanel panelOption;
    private javax.swing.JTextArea taSymptomDesc;
    // End of variables declaration//GEN-END:variables
}

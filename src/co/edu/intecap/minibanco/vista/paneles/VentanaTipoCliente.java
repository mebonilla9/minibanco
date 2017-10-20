/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.intecap.minibanco.vista.paneles;

import co.edu.intecap.minibancolibreria.modelo.conexion.Conexion;
import co.edu.intecap.minibancolibreria.modelo.vo.TipoCliente;
import co.edu.intecap.minibancolibreria.negocio.constantes.EMensajes;
import co.edu.intecap.minibancolibreria.negocio.delegado.TipoClienteDelegado;
import co.edu.intecap.minibancolibreria.negocio.excepciones.MiniBancoException;
import co.edu.intecap.minibancolibreria.negocio.util.CheckBoxUtil;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Capacitaciones_pc18
 */
public class VentanaTipoCliente extends javax.swing.JInternalFrame {

    private List<TipoCliente> listaTipoCliente;
    private Connection cnn;
    private Long idTipoClienteEditar;
    private boolean editar;

    /**
     * Creates new form VentanaCliente
     */
    public VentanaTipoCliente() {
        initComponents();
        initListeners();
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setTitle("Tipo Cliente");
        this.setMinimumSize(new Dimension(400, 230));

        cargarListasIniciales();
    }

    private void cargarListasIniciales() {
        try {
            cnn = Conexion.conectar();
            listaTipoCliente = new TipoClienteDelegado(cnn).consultar();
            imprimirTabla();
        } catch (MiniBancoException ex) {
            System.out.println(ex.getMensaje());
        } finally {
            Conexion.desconectar(cnn);
        }
    }

    private void imprimirTabla() throws MiniBancoException {
        try {
            /**
             * Adicion de las columnas de la tabla
             */
            ResultSetMetaData columnasTipoCliente = new TipoClienteDelegado(cnn).consultarColumnasTipoCliente();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            for (int i = 1; i <= columnasTipoCliente.getColumnCount(); i++) {
                modeloTabla.addColumn(columnasTipoCliente.getColumnName(i));
            }

            /**
             * Adicion de las filas de la tabla
             */
            for (TipoCliente tipoClientes : listaTipoCliente) {
                modeloTabla.addRow(convertirTipoClienteFila(tipoClientes));
            }

            /**
             * Asignacion del modelo de tabla al JTable
             */
            tblTipoCliente.setModel(modeloTabla);

        } catch (SQLException e) {
            throw new MiniBancoException(EMensajes.ERROR_CONSULTAR);
        }
    }

    private Object[] convertirTipoClienteFila(TipoCliente tipoCliente) {
        Object[] fila = new Object[3];
        int i = 0;
        fila[i++] = tipoCliente.getIdTipoCliente();
        fila[i++] = tipoCliente.getNombre();
        fila[i++] = tipoCliente.getEstado();
        return fila;
    }

    private void initListeners() {
        tblTipoCliente.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblTipoCliente.getSelectedRow() > -1) {
                    TipoCliente tipoClienteEditar = listaTipoCliente.get(tblTipoCliente.getSelectedRow());
                    asignarAlFormulario(tipoClienteEditar);
                }
            }
        });
    }

    private void limpiarFormulario() {
        editar = false;
        txtNombre.setText("");
        cbxEstado.setSelected(false);
        cbxEstado.setText("Desactivo");
        btnRegistrar.setText("Registrar");
    }

    private void asignarAlFormulario(TipoCliente tipoClienteEditar) {
        this.idTipoClienteEditar = tipoClienteEditar.getIdTipoCliente();
        txtNombre.setText(tipoClienteEditar.getNombre());
        cbxEstado.setSelected(tipoClienteEditar.getEstado());
        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);
        editar = true;
        btnRegistrar.setText("Actualizar");
    }

    private String validarFormularioTipoCliente() {
        StringBuilder sb = new StringBuilder("Los campos (");
        if (txtNombre.getText().equals("")) {
            sb.append(" Nombre ");
        }

        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);

        return sb.substring(0, sb.length() - 1) + ")";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTipoCliente = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblNombre.setText("Nombre:");

        lblEstado.setText("Estado:");

        cbxEstado.setText("Desactivo");
        cbxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEstadoActionPerformed(evt);
            }
        });

        tblTipoCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblTipoCliente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstado)
                            .addComponent(lblNombre))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbxEstado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRegistrar)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxEstado)
                        .addComponent(lblEstado))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRegistrar)
                            .addComponent(btnCancelar))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEstadoActionPerformed
        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);
    }//GEN-LAST:event_cbxEstadoActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        try {

            cnn = Conexion.conectar();
            String mensaje = validarFormularioTipoCliente();

            if (mensaje.length() > 13 && !editar) {
                JOptionPane.showMessageDialog(this, mensaje, "Formulario Incompleto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TipoCliente nuevoTipoCliente = new TipoCliente();
            nuevoTipoCliente.setNombre(txtNombre.getText());
            nuevoTipoCliente.setEstado(cbxEstado.isSelected());

            if (editar) {
                nuevoTipoCliente.setIdTipoCliente(idTipoClienteEditar);
                new TipoClienteDelegado(cnn).editar(nuevoTipoCliente);
                JOptionPane.showMessageDialog(rootPane, EMensajes.MODIFICO.getDescripcion(), "Modificacion de tipo de usuarios", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new TipoClienteDelegado(cnn).insertar(nuevoTipoCliente);
                JOptionPane.showMessageDialog(rootPane, EMensajes.INSERTO.getDescripcion(), "Registro de tipo de usuarios", JOptionPane.INFORMATION_MESSAGE);

            }

            Conexion.commit(cnn);
            limpiarFormulario();
            cargarListasIniciales();

        } catch (MiniBancoException e) {
            JOptionPane.showMessageDialog(this, e.getMensaje(), "Registro tipo de Usuario", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.desconectar(cnn);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JCheckBox cbxEstado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tblTipoCliente;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}

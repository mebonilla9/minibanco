/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.intecap.minibanco.vista.paneles;

import co.edu.intecap.minibancolibreria.modelo.conexion.Conexion;
import co.edu.intecap.minibancolibreria.modelo.vo.TipoCliente;
import co.edu.intecap.minibancolibreria.modelo.vo.TipoProducto;
import co.edu.intecap.minibancolibreria.negocio.constantes.EMensajes;
import co.edu.intecap.minibancolibreria.negocio.delegado.TipoClienteDelegado;
import co.edu.intecap.minibancolibreria.negocio.delegado.TipoProductoDelegado;
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
 * @author jdavellaneda
 */
public class VentanaTipoProducto extends javax.swing.JInternalFrame {
    
    private List<TipoProducto> listaTipoProducto;
    private Connection cnn;
    private Long idTipoProductoEditar;
    private boolean editar;

    /**
     * Creates new form VentanaTipoProducto
     */
    public VentanaTipoProducto() {
        initComponents();
        initListeners();
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setTitle("Tipo Producto");
        this.setMinimumSize(new Dimension(400,230));
        
        cargarListasIniciales();
    }

    private void cargarListasIniciales() {
        try {
            cnn = Conexion.conectar();
            listaTipoProducto = new TipoProductoDelegado(cnn).consultar();
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
            ResultSetMetaData columnasTipoProducto = new TipoProductoDelegado(cnn).consultarColumnasTipoProducto();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            for (int i = 1; i <= columnasTipoProducto.getColumnCount(); i++) {
                modeloTabla.addColumn(columnasTipoProducto.getColumnName(i));
            }

            /**
             * Adicion de las filas de la tabla
             */
            for (TipoProducto tipoProductos : listaTipoProducto) {
                modeloTabla.addRow(convertirTipoProductoFila(tipoProductos));
            }

            /**
             * Asignacion del modelo de tabla al JTable
             */
            tblTipoProducto.setModel(modeloTabla);

        } catch (SQLException e) {
            throw new MiniBancoException(EMensajes.ERROR_CONSULTAR);
        }
    }
    
    private Object[] convertirTipoProductoFila(TipoProducto tipoProducto) {
        Object[] fila = new Object[3];
        int i = 0;
        fila[i++] = tipoProducto.getIdTipoProducto();
        fila[i++] = tipoProducto.getNombre();
        fila[i++] = tipoProducto.getEstado();
        return fila;
    }
    
    private void initListeners() {
        tblTipoProducto.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblTipoProducto.getSelectedRow() > -1) {
                    TipoProducto tipoProductoEditar = listaTipoProducto.get(tblTipoProducto.getSelectedRow());
                    asignarAlFormulario(tipoProductoEditar);
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
    
    private void asignarAlFormulario(TipoProducto tipoProductoEditar) {
        this.idTipoProductoEditar = tipoProductoEditar.getIdTipoProducto();
        txtNombre.setText(tipoProductoEditar.getNombre());
        cbxEstado.setSelected(tipoProductoEditar.getEstado());
        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);
        editar = true;
        btnRegistrar.setText("Actualizar");
    }
    
    private String validarFormularioTipoProducto() {
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

        txtNombre = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTipoProducto = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();

        lblEstado.setText("Estado:");

        cbxEstado.setText("Desactivo");
        cbxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEstadoActionPerformed(evt);
            }
        });

        tblTipoProducto.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblTipoProducto);

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
            String mensaje = validarFormularioTipoProducto();

            if (mensaje.length() > 13 && !editar) {
                JOptionPane.showMessageDialog(this, mensaje, "Formulario Incompleto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TipoProducto nuevoTipoProducto = new TipoProducto();
            nuevoTipoProducto.setNombre(txtNombre.getText());
            nuevoTipoProducto.setEstado(cbxEstado.isSelected());

            if (editar) {
                nuevoTipoProducto.setIdTipoProducto(idTipoProductoEditar);
                new TipoProductoDelegado(cnn).editar(nuevoTipoProducto);
                JOptionPane.showMessageDialog(rootPane, EMensajes.MODIFICO.getDescripcion(), "Modificacion de tipo de usuarios", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new TipoProductoDelegado(cnn).insertar(nuevoTipoProducto);
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
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tblTipoProducto;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.intecap.minibanco.vista.paneles;

import co.edu.intecap.minibancolibreria.modelo.conexion.Conexion;
import co.edu.intecap.minibancolibreria.modelo.vo.Cliente;
import co.edu.intecap.minibancolibreria.modelo.vo.TipoCliente;
import co.edu.intecap.minibancolibreria.modelo.vo.TipoDocumento;
import co.edu.intecap.minibancolibreria.negocio.constantes.EMensajes;
import co.edu.intecap.minibancolibreria.negocio.delegado.ClienteDelegado;
import co.edu.intecap.minibancolibreria.negocio.delegado.TipoClienteDelegado;
import co.edu.intecap.minibancolibreria.negocio.delegado.TipoDocumentoDelegado;
import co.edu.intecap.minibancolibreria.negocio.excepciones.MiniBancoException;
import co.edu.intecap.minibancolibreria.negocio.util.CheckBoxUtil;
import co.edu.intecap.minibancolibreria.negocio.util.CryptoUtil;
import co.edu.intecap.minibancolibreria.negocio.util.PasswordUtil;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Capacitaciones_pc18
 */
public class VentanaCliente extends javax.swing.JInternalFrame {

    private List<TipoCliente> listaTipoCliente;
    private List<TipoDocumento> listaTipoDocumento;
    private List<Cliente> listaCliente;
    private Connection cnn;
    private boolean editar;
    private String contrasenaEditar;
    private Long idClienteEditar;

    /**
     * Creates new form VentanaCliente
     */
    public VentanaCliente() {
        initComponents();
        initListeners();
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setTitle("Cliente");
        this.setMinimumSize(new Dimension(800, 500));

        cargarListasIniciales();

        /*tblClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    System.out.println("fila seleccionada: " + tblClientes.getSelectedRow());
                }
            }
        });*/
    }

    private void cargarListasIniciales() {
        try {
            cnn = Conexion.conectar();
            listaTipoCliente = new TipoClienteDelegado(cnn).consultar();
            listaTipoDocumento = new TipoDocumentoDelegado(cnn).consultar();
            listaCliente = new ClienteDelegado(cnn).consultar();
            imprimirListas();
            imprimirTabla();
        } catch (MiniBancoException ex) {
            System.out.println(ex.getMensaje());
        } finally {
            Conexion.desconectar(cnn);
        }
    }

    private void imprimirListas() {
        DefaultComboBoxModel<String> modeloTipoCliente = new DefaultComboBoxModel<>();
        modeloTipoCliente.addElement("Seleccione un tipo de cliente.");
        for (TipoCliente tipoCliente : listaTipoCliente) {
            modeloTipoCliente.addElement(tipoCliente.getNombre());
        }
        cboTipoCliente.removeAllItems();
        cboTipoCliente.setModel(modeloTipoCliente);

        DefaultComboBoxModel<String> modeloTipoDocumento = new DefaultComboBoxModel<>();
        modeloTipoDocumento.addElement("Seleccione un tipo de documento.");
        for (TipoDocumento tipoDocumento : listaTipoDocumento) {
            modeloTipoDocumento.addElement(tipoDocumento.getNombre());
        }
        cboTipoDocumento.removeAllItems();
        cboTipoDocumento.setModel(modeloTipoDocumento);
    }

    private void imprimirTabla() throws MiniBancoException {
        try {
            /**
             * Adicion de las columnas de la tabla
             */
            ResultSetMetaData columnasCliente = new ClienteDelegado(cnn).consultarColumnasCliente();
            DefaultTableModel modeloTabla = new DefaultTableModel();
            for (int i = 1; i <= columnasCliente.getColumnCount(); i++) {
                modeloTabla.addColumn(columnasCliente.getColumnName(i));
            }

            /**
             * Adicion de las filas de la tabla
             */
            for (Cliente clientes : listaCliente) {
                modeloTabla.addRow(convertirClienteFila(clientes));
            }

            /**
             * Asignacion del modelo de tabla al JTable
             */
            tblCliente.setModel(modeloTabla);

        } catch (SQLException e) {
            throw new MiniBancoException(EMensajes.ERROR_CONSULTAR);
        }
    }

    private Object[] convertirClienteFila(Cliente cliente) {
        Object[] fila = new Object[13];
        int i = 0;
        fila[i++] = cliente.getIdCliente();
        fila[i++] = cliente.getNombre();
        fila[i++] = cliente.getApellido();
        fila[i++] = cliente.getIdentificacion();
        fila[i++] = cliente.getTelefono();
        fila[i++] = cliente.getDireccion();
        fila[i++] = cliente.getUsuario();
        fila[i++] = cliente.getContrasena();
        fila[i++] = cliente.getFechaNacimiento();
        fila[i++] = cliente.getCorreo();

        for (TipoCliente listaTipoClientes : listaTipoCliente) {

            if (cliente.getTipoCliente().getIdTipoCliente() == listaTipoClientes.getIdTipoCliente()) {
                fila[i++] = listaTipoClientes.getNombre();
                break;
            }
        }

        for (TipoDocumento listaTipoDocumentos : listaTipoDocumento) {

            if (cliente.getTipoDocumento().getIdTipoDocumento() == listaTipoDocumentos.getIdTipoDocumento()) {
                fila[i++] = listaTipoDocumentos.getNombre();
                break;
            }
        }

        fila[i++] = cliente.getEstado();
        return fila;
    }

    private String validarFormularioCliente() {
        StringBuilder sb = new StringBuilder("Los campos (");
        if (txtNombre.getText().equals("")) {
            sb.append(" Nombre ");
        }
        if (txtIdentificacion.getText().equals("")) {
            sb.append(" Identificación ");
        }
        if (txtTelefono.getText().equals("")) {
            sb.append(" Telefono ");
        }
        if (txtDireccion.getText().equals("")) {
            sb.append(" Dirección ");
        }
        if (txtUsuario.getText().equals("")) {
            sb.append(" Usuario ");
        }
        if (!validarContrasena()) {
            sb.append(" Contraseña ");
        }
        if (txtFechaNacimiento.getText().equals("")) {
            sb.append(" Usuario ");
        }
        if (txtCorreo.getText().equals("")) {
            sb.append(" Usuario ");
        }
        if (cboTipoCliente.getSelectedIndex() < 1) {
            sb.append(" Tipo Cliente ");
        }
        if (cboTipoDocumento.getSelectedIndex() < 1) {
            sb.append(" Tipo Documento ");
        }

        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);

        return sb.substring(0, sb.length() - 1) + ")";
    }

    private boolean validarContrasena() {
        String contrasena = PasswordUtil.armarContrasena(txtContrasena.getPassword());
        return contrasena.length() > 7 && contrasena.length() < 21;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbgRol = new javax.swing.ButtonGroup();
        txtFechaNacimiento = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        cboTipoCliente = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtContrasena = new javax.swing.JPasswordField();
        txtIdentificacion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();

        jLabel9.setText("Correo:");

        jLabel11.setText("Tipo Documento:");

        cboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setText("Tipo Cliente:");

        cboTipoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Identificación:");

        jLabel4.setText("Telefono:");

        jLabel5.setText("Dirección:");

        jLabel6.setText("Usuario:");

        jLabel7.setText("Contraseña:");

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

        jLabel1.setText("Nombre:");

        jLabel2.setText("Apellido:");

        jLabel8.setText("Fecha Nacimiento:");

        cbxEstado.setText("Desactivo");
        cbxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEstadoActionPerformed(evt);
            }
        });

        jScrollPane2.setAutoscrolls(true);

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(tblCliente);

        jLabel10.setText("Estado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbxEstado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRegistrar))
                            .addComponent(txtNombre)
                            .addComponent(txtApellido)
                            .addComponent(txtIdentificacion)
                            .addComponent(txtTelefono)
                            .addComponent(txtDireccion)
                            .addComponent(txtUsuario)
                            .addComponent(txtCorreo)
                            .addComponent(txtFechaNacimiento)
                            .addComponent(txtContrasena)
                            .addComponent(cboTipoCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboTipoDocumento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboTipoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancelar)
                        .addComponent(btnRegistrar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxEstado)
                        .addComponent(jLabel10)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        try {
            cnn = Conexion.conectar();
            String mensaje = validarFormularioCliente();
            if (mensaje.length() > 13 && !editar) {
                JOptionPane.showMessageDialog(this, mensaje, "Formulario Incompleto", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(txtNombre.getText());
            nuevoCliente.setApellido(txtApellido.getText());
            nuevoCliente.setIdentificacion(txtIdentificacion.getText());
            nuevoCliente.setTelefono(txtTelefono.getText());
            nuevoCliente.setDireccion(txtDireccion.getText());
            nuevoCliente.setUsuario(txtUsuario.getText());
            if (editar && txtContrasena.getPassword().length == 0) {
                nuevoCliente.setContrasena(contrasenaEditar);
            } else {
                nuevoCliente.setContrasena(CryptoUtil.cifrarContrasena(PasswordUtil.armarContrasena(txtContrasena.getPassword())));
            }
            nuevoCliente.setCorreo(txtCorreo.getText());
            String fecha = txtFechaNacimiento.getText().replace("/", "-");
            try {
                //Objeto que valida fechas
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date parse = sdf.parse(fecha);
                nuevoCliente.setFechaNacimiento(parse);
            } catch (ParseException e) {
                throw new MiniBancoException(EMensajes.ERROR_FORMATO_FECHA);
            }
            nuevoCliente.setTipoCliente(listaTipoCliente.get(cboTipoCliente.getSelectedIndex() - 1));
            nuevoCliente.setTipoDocumento(listaTipoDocumento.get(cboTipoDocumento.getSelectedIndex() - 1));
            nuevoCliente.setEstado(cbxEstado.isSelected());

            if (editar) {
                nuevoCliente.setIdCliente(idClienteEditar);
                new ClienteDelegado(cnn).editar(nuevoCliente);
                JOptionPane.showMessageDialog(rootPane, EMensajes.MODIFICO.getDescripcion(), "Modificacion de usuarios", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new ClienteDelegado(cnn).insertar(nuevoCliente);
                JOptionPane.showMessageDialog(rootPane, EMensajes.INSERTO.getDescripcion(), "Registro de usuarios", JOptionPane.INFORMATION_MESSAGE);

            }

            Conexion.commit(cnn);
            limpiarFormulario();
            cargarListasIniciales();
        } catch (MiniBancoException e) {
            JOptionPane.showMessageDialog(this, e.getMensaje(), "Registro Usuario", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.desconectar(cnn);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEstadoActionPerformed
        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);
    }//GEN-LAST:event_cbxEstadoActionPerformed

    private void limpiarFormulario() {
        editar = false;
        txtNombre.setText("");
        txtApellido.setText("");
        txtIdentificacion.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtCorreo.setText("");
        txtFechaNacimiento.setText("");
        rbgRol.clearSelection();
        cboTipoCliente.setSelectedIndex(0);
        cboTipoDocumento.setSelectedIndex(0);
        cbxEstado.setSelected(false);
        cbxEstado.setText("Desactivo");
        btnRegistrar.setText("Registrar");
    }

    private void initListeners() {
        tblCliente.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblCliente.getSelectedRow() > -1) {
                    Cliente clienteEditar = listaCliente.get(tblCliente.getSelectedRow());
                    asignarAlFormulario(clienteEditar);
                }
            }
        });
    }

    private void asignarAlFormulario(Cliente clienteEditar) {
        this.idClienteEditar = clienteEditar.getIdCliente();
        txtNombre.setText(clienteEditar.getNombre());
        txtApellido.setText(clienteEditar.getApellido());
        txtIdentificacion.setText(clienteEditar.getIdentificacion());
        txtTelefono.setText(clienteEditar.getTelefono());
        txtDireccion.setText(clienteEditar.getDireccion());
        txtUsuario.setText(clienteEditar.getUsuario());
        txtCorreo.setText(clienteEditar.getCorreo());
        contrasenaEditar = clienteEditar.getContrasena();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        txtFechaNacimiento.setText(sdf.format(clienteEditar.getFechaNacimiento()));
        cbxEstado.setSelected(clienteEditar.getEstado());
        CheckBoxUtil.validarCheckBoxEstado(cbxEstado);

        asignarTipoCliente(clienteEditar.getTipoCliente().getIdTipoCliente());
        asignarTipoDocumento(clienteEditar.getTipoDocumento().getIdTipoDocumento());
        editar = true;
        btnRegistrar.setText("Actualizar");
    }

    private void asignarTipoCliente(Long idTipoCliente) {
        for (int i = 0; i < listaTipoCliente.size(); i++) {
            if (listaTipoCliente.get(i).getIdTipoCliente() == idTipoCliente) {
                cboTipoCliente.setSelectedIndex(i + 1);
            }
        }
    }

    private void asignarTipoDocumento(Long idTipoDocumento) {
        for (int i = 0; i < listaTipoDocumento.size(); i++) {
            if (listaTipoDocumento.get(i).getIdTipoDocumento() == idTipoDocumento) {
                cboTipoDocumento.setSelectedIndex(i + 1);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboTipoCliente;
    private javax.swing.JComboBox<String> cboTipoDocumento;
    private javax.swing.JCheckBox cbxEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.ButtonGroup rbgRol;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFechaNacimiento;
    private javax.swing.JTextField txtIdentificacion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}

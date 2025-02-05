package core.ui.component.dialog;

import core.ApplicationContext;
import core.ui.MainActivity;
import core.ui.component.GBC;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import util.Log;
import util.automaticBindClick;
import util.functions;

public class GenerateShellLoder extends JDialog {
    private JLabel passwordLabel;
    private JLabel secretKeyLabel;
    private JLabel cryptionLabel;
    private JLabel payloadLabel;
    private JTextField passwordTextField;
    private JTextField secretKeyTextField;
    private JComboBox<String> cryptionComboBox;
    private JComboBox<String> payloadComboBox;
    private JButton generateButton;
    private JButton cancelButton;

    public GenerateShellLoder() {
        super(MainActivity.getFrame(), "GenerateShell", true);
        this.setLayout(new GridBagLayout());
        Container c = this.getContentPane();
        GBC gbcLPassword = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
        GBC gbcPassword = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLSecretKey = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
        GBC gbcSecretKey = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLPayload = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
        GBC gbcPayload = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcLCryption = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
        GBC gbcCryption = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
        GBC gbcGenerate = (new GBC(2, 4)).setInsets(5, -40, 0, 0);
        GBC gbcCancel = (new GBC(1, 4, 3, 1)).setInsets(5, 20, 0, 0);

        this.passwordLabel = new JLabel("密码");
        this.secretKeyLabel = new JLabel("密钥");
        this.payloadLabel = new JLabel("有效载荷");
        this.cryptionLabel = new JLabel("加密器");
        this.passwordTextField = new JTextField(16);
        this.secretKeyTextField = new JTextField(16);
        this.payloadComboBox = new JComboBox();
        this.cryptionComboBox = new JComboBox();
        this.generateButton = new JButton("生成");
        this.cancelButton = new JButton("取消");
        this.passwordTextField.setText("pass");
        this.secretKeyTextField.setText("key");

        c.add(this.passwordLabel, gbcLPassword);
        c.add(this.passwordTextField, gbcPassword);
        c.add(this.secretKeyLabel, gbcLSecretKey);
        c.add(this.secretKeyTextField, gbcSecretKey);
        c.add(this.payloadLabel, gbcLPayload);
        c.add(this.payloadComboBox, gbcPayload);
        c.add(this.cryptionLabel, gbcLCryption);
        c.add(this.cryptionComboBox, gbcCryption);
        c.add(this.generateButton, gbcGenerate);
        c.add(this.cancelButton, gbcCancel);

        this.addToComboBox(this.payloadComboBox, ApplicationContext.getAllPayload());

        // 为payloadComboBox添加事件监听器，以响应用户的选择操作
        this.payloadComboBox.addActionListener(new ActionListener() {
            // 当用户选择不同的payload时，更新加密方式下拉框的内容
            public void actionPerformed(ActionEvent paramActionEvent) {
                // 获取用户选择的payload名称
                String seleteItemString = (String)GenerateShellLoder.this.payloadComboBox.getSelectedItem();
                System.out.println("seleteItemString = " + seleteItemString);
                // 清空加密方式下拉框，准备添加新的加密方式
                GenerateShellLoder.this.cryptionComboBox.removeAllItems();
                // 根据选择的payload，获取所有可用的加密方式，并添加到加密方式下拉框中
                GenerateShellLoder.this.addToComboBox(GenerateShellLoder.this.cryptionComboBox, ApplicationContext.getAllCryption(seleteItemString));
            }
        });

        // 绑定当前窗口的事件处理方法
        automaticBindClick.bindJButtonClick(this, this);
        // 触发payloadComboBox的事件，以初始化界面
        functions.fireActionEventByJComboBox(this.payloadComboBox);
        // 设置窗口大小
        functions.setWindowSize(this, 530, 250);
        // 设置窗口位置，使其居中显示在主窗口之上
        this.setLocationRelativeTo(MainActivity.getFrame());
        // 设置窗口关闭方式，当用户关闭窗口时，释放资源但不退出应用程序
        this.setDefaultCloseOperation(2);
        // 显示窗口
        this.setVisible(true);
    }

    private void generateButtonClick(ActionEvent actionEvent) {
        String password = this.passwordTextField.getText();
        String secretKey = this.secretKeyTextField.getText();
        if (password == null || secretKey == null || password.trim().length() <= 0 || secretKey.trim().length() <= 0) {
            JOptionPane.showMessageDialog(this, "password 或\t secretKey  是空的!", "提示", 2);
        } else if (this.payloadComboBox.getSelectedItem() == null || this.cryptionComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "payload 或  cryption 没有选中!", "提示", 2);
        } else {
            byte[] data = ApplicationContext.getCryption((String) this.payloadComboBox.getSelectedItem(), (String) this.cryptionComboBox.getSelectedItem()).generate(password, secretKey);
            if (data != null) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(0);
                chooser.showDialog(new JLabel(), "选择");
                File selectdFile = chooser.getSelectedFile();
                if (selectdFile != null) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(selectdFile);
                        fileOutputStream.write(data);
                        fileOutputStream.close();
                        JOptionPane.showMessageDialog(this, "success! save file to -> " + selectdFile.getAbsolutePath(), "提示", 1);
                        dispose();
                    } catch (Exception e) {
                        Log.error(e);
                    }
                } else {
                    Log.log("用户取消选择....", new Object[0]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "加密器在生成时返回空", "提示", 2);
            }
        }
    }

    private void cancelButtonClick(ActionEvent actionEvent) {
        dispose();
    }



    private void addToComboBox(JComboBox<String> comboBox, String[] data) {
        for (String str : data) {
            comboBox.addItem(str);
        }
    }
}
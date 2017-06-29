package gui;

import entity.Member;
import filter.ExcelFileFilter;
import util.common.DateUtil;
import util.excel.EntityToExcel;
import util.excel.ExcelToEntity;
import util.excel.TestExcel;
import util.excel.model.HeaderData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2017/6/28.
 */
public class ChooseFile extends JFrame implements ActionListener {
    private JButton open;
    private JPanel contentPane;
    private JComboBox comboBox;
    private JProgressBar progressBar1;
    private JTextField sourcefile = new JTextField(); // 选择待加密或解密文件路径的文本域
    private JTextField targetfile = new JTextField(); // 选择加密或解密后文
    private JButton chooseButton;
    private JFileChooser choosePath;
    private JFileChooser jfc;
    private JButton begin;

    public static void main(String[] args) {
        new ChooseFile();
    }

    public ChooseFile() throws HeadlessException {
        sourcefile.setPreferredSize(new Dimension(300,20));
        sourcefile.setEditable(false);// 设置源文件文本域不可手动修改
        targetfile.setPreferredSize(new Dimension(300,20));
        targetfile.setEditable(false);// 设置目标位置文本域不可手动修改
        open=new JButton("选择excel文件");
        chooseButton=new JButton("选择输出目录");
        chooseButton.addActionListener(new ButtonListen());
        //设置窗体
        this.setBounds(400, 200, 400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        open.addActionListener(this);
        this.setTitle("excel文件转换");
        contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(60,50,5,50));
        this.setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel label=new JLabel("选择模板:");
        contentPane.add(label);
        comboBox=new JComboBox();
        comboBox.addItem("通用模板");
        comboBox.addItem("模板1");
        comboBox.addItem("模板2");
        comboBox.addItem("模板3");
        progressBar1=new JProgressBar();
        progressBar1.setOrientation(JProgressBar.HORIZONTAL);
        progressBar1.setPreferredSize(new Dimension(300,30));
        progressBar1.setMaximum(0);
        progressBar1.setMaximum(100);
        progressBar1.setStringPainted(true);
        contentPane.add(comboBox);
        contentPane.add(sourcefile);
        contentPane.add(open);
        contentPane.add(targetfile);
        contentPane.add(chooseButton);
        contentPane.add(progressBar1);
        begin=new JButton("开始转换");
        begin.addActionListener(new BeginButtonListen());
        contentPane.add(begin);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        // TODO Auto-generated method stub
         jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
        ExcelFileFilter excelFilter = new ExcelFileFilter(); //excel过滤器
        jfc.addChoosableFileFilter(excelFilter);
        jfc.setFileFilter(excelFilter);
        jfc.showDialog(new JLabel(), "选择需要转换的excel文件");
        File file=jfc.getSelectedFile();
        sourcefile.setText(file.getAbsolutePath());

        System.out.println(comboBox.getSelectedItem().toString());


    }



    class ButtonListen implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
             choosePath=new JFileChooser();
            choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            choosePath.showDialog(new JLabel(), "选择输出目录");
            File outPath=choosePath.getSelectedFile();
            if(outPath!=null) {
                targetfile.setText(outPath.getAbsolutePath());
            }
        }
    }

    class BeginButtonListen implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file=jfc.getSelectedFile();
            if(file!=null) {
                if (file.isFile()) {
                    System.out.println("文件:" + file.getAbsolutePath());
                    try {
                        List<Member> data = this.getDataFromExcel(file);
                        progressBar1.setValue(50);
                        progressBar1.setString("读取数据成功！");
                        if(targetfile.getText()!=null&&!targetfile.getText().equals("")){
                            this.createNewExcel(data,targetfile.getText());
                        }else {
                            this.createNewExcel(data, file.getParentFile().getAbsolutePath());
                        }
                        progressBar1.setValue(100);
                        progressBar1.setString("创建excel文件成功！");

                    } catch (Exception e1) {
                        progressBar1.setValue(0);
                        progressBar1.setValue(100);
                        progressBar1.setString("转换异常！");
                        e1.printStackTrace();
                    }
                }
            }
        }


        private List<Member> getDataFromExcel(File file) throws Exception {
            ExcelToEntity<Member> excelToEntity=new ExcelToEntity<Member>(Member.class);
            excelToEntity.openExcel(new FileInputStream(file));
            excelToEntity.readExcel(0);
            return excelToEntity.toEntitys();

        }

        private void createNewExcel(List<Member> data,String path) throws Exception {
            EntityToExcel e2e = new EntityToExcel();
            List<HeaderData> hdList = new ArrayList<HeaderData>();

            hdList.add(new HeaderData("idCard", "身份证号"));
            hdList.add(new HeaderData("name", "会员姓名"));
            hdList.add(new HeaderData("mobile", "手机号"));
            hdList.add(new HeaderData("qq", "qq"));
            hdList.add(new HeaderData("weixin", "微信号"));
            hdList.add(new HeaderData("email", "邮箱"));
            hdList.add(new HeaderData("sex", "性别"));
            e2e.createTableHeader(hdList);

            // 装填数据

            e2e.fillData(data);

            String filePath = path+"\\" ;
            File file = new File(filePath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            String fileName = TestExcel.getYearMonth(DateUtil.getYear(), DateUtil.getMonth()) + jfc.getSelectedFile().getName();
            e2e.toExcel(filePath+fileName);



        }
    }

}

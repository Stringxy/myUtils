package gui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Member;
import filter.ExcelFileFilter;
import util.common.DateUtil;
import util.common.HttpUtil;
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
import java.util.Properties;

/**
 * Created by xy on 2017/6/28.
 */
public class ChooseFile extends JFrame implements ActionListener {
    private JButton open;
    private MyPanel contentPane;
    private JComboBox comboBox;
    private JProgressBar progressBar1;
    private JTextField sourcefile = new JTextField();
//    private JTextField targetfile = new JTextField();
    private JButton chooseButton;
    private JFileChooser choosePath;
    private JFileChooser jfc;
    private JButton begin;
    private static String URL="http://localhost:8088/account/member/addMembers";
    private static String SHOPID="testexcel";

    public static void main(String[] args) {

        try {
            new ChooseFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChooseFile() throws Exception {
        sourcefile.setPreferredSize(new Dimension(300,20));
        sourcefile.setEditable(false);// 设置源文件文本域不可手动修改
        open=new JButton("选择excel文件");

        //设置窗体
        this.setBounds(400, 200, 400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        open.addActionListener(this);
        this.setTitle("excel文件导入工具");
        contentPane=new MyPanel();
        contentPane.setBorder(new EmptyBorder(60,50,5,50));
        this.setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel label=new JLabel("选择模板:");
        contentPane.add(label);
        comboBox=new JComboBox();
        comboBox.addItem("锤子大爷");
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
        contentPane.add(progressBar1);
        begin=new JButton("开始导入");
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
        if(file!=null) {
            sourcefile.setText(file.getAbsolutePath());
        }
        System.out.println(comboBox.getSelectedItem().toString());


    }



//    class ButtonListen implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//             choosePath=new JFileChooser();
//            choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            choosePath.showDialog(new JLabel(), "选择输出目录");
//            File outPath=choosePath.getSelectedFile();
//            if(outPath!=null) {
//                targetfile.setText(outPath.getAbsolutePath());
//            }
//        }
//    }

    class BeginButtonListen implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file=jfc.getSelectedFile();
            if(file!=null) {
                if (file.isFile()) {
                    System.out.println("文件:" + file.getAbsolutePath());
                    try {
                        List<Member> data = this.getDataFromExcel(file);
                        for(int i=0;i<data.size();i++){
                            data.get(i).setShopId(SHOPID);
                            if (data.get(i).getSex().equals("1")){
                                data.get(i).setSex("FEMALE");
                            }else{
                                data.get(i).setSex("MALE");
                            }
                            int proVal=(int)((double)i/data.size()/2*10);
                            System.out.println(proVal);
                            progressBar1.setValue(proVal);
                        }
                        progressBar1.setValue(50);
                        progressBar1.setString("读取数据成功！");
//                        if(targetfile.getText()!=null&&!targetfile.getText().equals("")){
//                            this.createNewExcel(data,targetfile.getText());
//                        }else {
//                            this.createNewExcel(data, file.getParentFile().getAbsolutePath());
//                        }
                        String result=this.insertDataBase(data);
                        progressBar1.setValue(100);
                        progressBar1.setString(result);

                    } catch (Exception e1) {
                        progressBar1.setValue(0);
                        progressBar1.setString("导入异常！");
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


        private String insertDataBase(List<Member> members) throws Exception{
            System.out.println(JSON.toJSONString(members));
            String response=HttpUtil.sendPost(URL, JSON.toJSONString(members));
            System.out.println(response);
            JSONObject json=JSONObject.parseObject(response);
            System.out.println(json.keySet());
            return  json.get("resultNote").toString();
        }
    }


//
//        private void createNewExcel(List<Member> data,String path) throws Exception {
//            EntityToExcel e2e = new EntityToExcel();
//            List<HeaderData> hdList = new ArrayList<HeaderData>();
//
//            hdList.add(new HeaderData("idCard", "身份证号"));
//            hdList.add(new HeaderData("name", "会员姓名"));
//            hdList.add(new HeaderData("mobile", "手机号"));
//            hdList.add(new HeaderData("qq", "qq"));
//            hdList.add(new HeaderData("weixin", "微信号"));
//            hdList.add(new HeaderData("email", "邮箱"));
//            hdList.add(new HeaderData("sex", "性别"));
//            e2e.createTableHeader(hdList);
//
//            // 装填数据
//
//            e2e.fillData(data);
//
//            String filePath = path+"\\" ;
//            File file = new File(filePath);
//            if(!file.exists())
//            {
//                file.mkdirs();
//            }
//            String fileName = TestExcel.getYearMonth(DateUtil.getYear(), DateUtil.getMonth()) + jfc.getSelectedFile().getName();
//            e2e.toExcel(filePath+fileName);
//
//
//
//        }
//    }

}

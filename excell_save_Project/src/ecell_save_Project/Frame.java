package ecell_save_Project;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.attribute.AclEntry.Builder;
import java.text.DecimalFormat;
import java.util.ArrayList;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


/*주의 : 코드막짬 */
public class Frame extends JFrame {

    static ArrayList<String> folderpath = new ArrayList<String>();
    public final static int FOLDER_DIV = 3000;
    public static int excellcount = 0;

    public static Boolean Er = false;

    public static InputStream instream;
    public static OutputStream outStream;

    /* 드라이브 체크 */

    public static ArrayList<String> Erros_list;
    public static ArrayList<String> failedCheck_list = new ArrayList<String>();

    public ArrayList<String> Excell_list;
    public ArrayList<String> Excell_list2;

    public ArrayList<String> File_namelist;
    public final int C = 1;
    public final int D = 2;
    public final int E = 3;
    public final int F = 4;
    /* 콤보박스 라벨값 */
    public String String_array[] = {"직접입력하기", "C", "D", "E", "F"};
    /* 화면 해상도 및 제목 */
    private int x, y;
    private String title;

    /**
     * 파일 입출력 할때 사용
     */

    private String INPUT_PATH; // 텍스트에 입력한 경로값
    private String FileName;
    /* 콤보 박스에서 드라이브 체크 */
    private int new_PATH_CHECK = 0;
    private String new_PATH_DRIVE = "null";
    private String old_PATH_DRIVE = "null";
	private static DecimalFormat df = new DecimalFormat("0000");
    public ArrayList<String> MECRO_OLDLIST = new ArrayList<String>();
    public ArrayList<String> MECRO_NEWLIST = new ArrayList<String>();
    public String MECRO_OLDNAME = "없음";
    public String MECRO_NEWNAME = "없음";
    public final int MECRO_SIZE = 5;
    public int MECRO_COUNT = -1;

    public JLabel result_label;

    /* 직접 경로 체크 -> 파일옮김 (만약 실패 )-> 복사 실패시 */
    public static void copy_upgrade(File old_file, File new_file) throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream fin = null;
        FileOutputStream fout = null;

        System.out.println("old:" + old_file);
        System.out.println("new:" + new_file + File.separator + old_file.getName());

        boolean check = new_file.isDirectory();

        if (!old_file.renameTo(new File(new_file.toString()))) { // renameTo로 이동
            // 실패할 경우

            buf = new byte[1024];
            try {

                fin = new FileInputStream(old_file);
                fout = new FileOutputStream(new_file + File.separator + old_file.getName());
                int read = 0;
                while ((read = fin.read(buf, 0, buf.length)) != -1) {
                    fout.write(buf, 0, read);
                }

                Er = false;
                fin.close();
                fout.close();
                // old_file.delete(); // 복사 후 파일 삭제
            } catch (IOException e) {
                // TODO Auto-generated catch block

                int result = 0;

                // JOptionPane.showMessageDialog(null,""+ e.toString());

                Erros_list.add(e.toString());

                result = JOptionPane.showConfirmDialog(null, e.toString() + " 시스템을 종료하시겠습니까? ");
                if (result == JOptionPane.YES_OPTION) {
                    Er = true;
                    System.exit(1);
                } else {

                }

                e.printStackTrace();
            }
        }

    }

    /* 폴더 전체 체크 -> 이동 (사용X) */
    public static void copy(File sourceF, File targetF) {

        File[] target_file = sourceF.listFiles();

        for (int i = 0; i < target_file.length; i++) {
            System.out.println("Copy:" + target_file[i]);
        }

        // System.out.println("탬프" + temp);
        for (File file : target_file) {
            File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());

            System.out.println("탬프 시작 : " + temp);

            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(file);
                fos = new FileOutputStream(temp);
                byte[] b = new byte[4096];
                int cnt = 0;
                while ((cnt = fis.read(b)) != -1) {
                    fos.write(b, 0, cnt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    public static void Copy_Rename(File sourceF, File targetF) {

        int count = 0;
        int failed = 0;


        String FILEPATHNAME = null;
        String fileNameSub = null;

        File[] ff = sourceF.listFiles();

        File[] countff = targetF.listFiles();
        int count_lastindex = 0;
        if (countff != null) {
            count_lastindex = countff.length;
            System.out.println("마지막 인덱스:" + count_lastindex);
        } else {
            count_lastindex = 0;
            System.out.println("마지막 인덱스:" + count_lastindex);
        }
        for (File file : ff) {
            File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());

            if (file.isDirectory()) {
                temp.mkdir();
                System.out.println("파일 경로 생성" + file);
                Copy_Rename(file, temp);
            } else {
                /* 확장자 명을 잘라준다 . */
                count++;
                count_lastindex++;
                DecimalFormat df = new DecimalFormat("000000");
                df.format(count_lastindex);
                df.format(count);
                System.out.println(count);
                fileNameSub = file.getName();
                //fileNameSub = "." + FilenameUtils.getExtension(file.getName());


                File file_count = new File(targetF.getAbsolutePath());
                file_count.mkdir();
                file_count.mkdirs();

                System.out.println("확장자명 :" + fileNameSub);
                System.out.println("파일이 있는 위치" + file);
                temp = new File(targetF.getAbsolutePath() + File.separator + df.format(count_lastindex).toString() + fileNameSub);


                System.out.println("복사될 위치" + temp);

                FileInputStream fis = null;
                FileOutputStream fos = null;

                FileInputStream back_i = null;
                FileOutputStream back_o = null;


                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        fos.write(b, 0, cnt);

                    }

                    System.out.println("복사완료" + temp);
                } catch (Exception e) {
                    failed = 1;
                    e.printStackTrace();
                } finally {
                    try {

                        if (failed == 0) {
                            fis.close();
                            fos.close();
                            File existscheck = new File(targetF.getAbsolutePath() + File.separator + df.format(count).toString() + fileNameSub);
                            if (file.exists() && !existscheck.exists()) {
                                System.out.println("file:" + file);
                                System.out.println("existscheck:" + existscheck);

                                if (file.delete()) {

                                    System.out.println(file + "파일삭제 성공");

                                } else {
                                    System.out.println(file + "파일삭제 실패");
                                }
                            } else {
                                System.out.println("file:" + file);
                                System.out.println("existscheck:" + existscheck);
                                System.out.println("파일이 겹쳐져있을수도있음.");
                            }
                        }
                    } catch (IOException e) {
           
                        e.printStackTrace();
                    }

                }
            }

        }
    }

    /* 엑셀 -> 엑셀 경로 복사 1차 분배 */
    public static void copy_2(File sourceF, File targetF, String newExcelpath) {

        int i = 0;
        int foldercount = 0;
        int strcount = 0;
        boolean count_result;
        String count_Str = Integer.toString(foldercount);

        File[] target_file = sourceF.listFiles();

        for (File file : target_file) {
            File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());
            System.out.println("빌더" + temp);

         

            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(file);
                fos = new FileOutputStream(temp);
                byte[] b = new byte[4096];
                int cnt = 0;
                while ((cnt = fis.read(b)) != -1) {
                    fos.write(b, 0, cnt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    /* 2차 분 배 */
    public static void copy_3(File sourceF, File targetF, String OLD_PATH, String NEW_PATH) {

        int i = 0;

        int foldercount = 0;
        int strcount = 0;
        boolean count_result;

        String count_Str = Integer.toString(foldercount);
        String FILEPATHNAME = null;
        String CODENAME = null;
        String YEARNAME = null;
        String[] cut = null;
        StringBuilder builder = new StringBuilder();
        String fileNameSub = null;

        ArrayList<String> oldList = new ArrayList<String>();
        ArrayList<String> newList = new ArrayList<String>();

        File[] ff = sourceF.listFiles();
        for (File file : ff) {
            File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());

            if (file.isDirectory()) {
                temp.mkdir();
                failedCheck_list.add(temp.toString());
                System.out.println("파일 경로 생성" + file);

                copy_3(file, temp, NEW_PATH, OLD_PATH);
            } else {
                /* 확장자 명을 잘라준다 . */

                System.out.println(strcount);
                fileNameSub = file.getName();
                //fileNameSub = "." + FilenameUtils.getExtension(file.getName());

                if (fileNameSub.length() >= 4) {
                    String check0 = file.getName(); 
                    String check1 = file.getName();
                    String check2 = file.getName(); 
                    String check3 = file.getName(); 

                    check0 = fileNameSub.substring(fileNameSub.length() - 3,
                            fileNameSub.length());
                    check1 = fileNameSub.substring(fileNameSub.length() - 4,
                            fileNameSub.length());
                    check2 = fileNameSub.substring(fileNameSub.length() - 5,
                            fileNameSub.length());
                    check3 = fileNameSub.substring(fileNameSub.length() - 6,
                            fileNameSub.length());

                    if (check0.contains(".")) {
                        fileNameSub = fileNameSub.substring(fileNameSub.length() - 3, fileNameSub.length());
                    } else if (check1.contains(".")) {
                        fileNameSub = fileNameSub.substring(fileNameSub.length() - 4, fileNameSub.length());
                    } else if (check2.contains(".")) {
                        fileNameSub = fileNameSub.substring(fileNameSub.length() - 5, fileNameSub.length());
                    } else if (check3.contains(".")) {
                        fileNameSub = fileNameSub.substring(fileNameSub.length() - 6, fileNameSub.length());
                    }

                }

                if (i % 3000 == 0) {

                    count_Str = Integer.toString(foldercount);
                    strcount =0;

                    foldercount++; // 폴더 카운터
                    /* 파일폴더 갯수 */

                    File file_count = new File(targetF.getAbsolutePath() + File.separator + df.format(foldercount) + File.separator);
                    File dir_count_check = new File(targetF.getAbsolutePath() + File.separator + df.format(foldercount));
                    while (true) {
                        if (dir_count_check.isDirectory()) {
                            System.out.println(dir_count_check.toString() + " 디렉토리 존재");
                            foldercount++;
                            dir_count_check = new File(targetF.getAbsolutePath() + File.separator + df.format(foldercount));

                        }
                        else
                        {
                            file_count = new File(targetF.getAbsolutePath() + File.separator + df.format(foldercount) + File.separator);
                            System.out.println(dir_count_check.toString() + " 디렉토리 존재X");
                            break;
                        }
                    }
                    builder = new StringBuilder();
                    cut = file_count.toString().split("\\\\");

                    count_result = file_count.mkdir();
                    count_result = file_count.mkdirs();

                    /* cut 0 , 1 은 c:확장자명 / 파일결과물 이름임 */
                    builder = new StringBuilder(); /* 확장자명 , 저장할 폴더의 위치를 뺀 이름 */
                    for (int k = 2; k < cut.length; k++) {
                        builder = builder.append(cut[k]);

                         System.out.println("빌더:" + cut[k]);

                    }
                    CODENAME = cut[cut.length-3];
                    YEARNAME = cut[cut.length-2];
                    FILEPATHNAME = cut[cut.length-1];

                    System.out.println(targetF.getAbsolutePath() + File.separator + count_Str);
                    folderpath.add(
                            cut[cut.length - 3] + File.separator + cut[cut.length - 2] + File.separator + df.format(foldercount).toString());
                    excellcount++;

                }

                i++;
                strcount++; // 파일구별하기위해 파일에 번호를 붙임.
              
                temp = new File(targetF.getAbsolutePath() + File.separator + df.format(foldercount) + File.separator + CODENAME +"-"+ YEARNAME+"-" + FILEPATHNAME
                        + "-" + df.format(strcount) + fileNameSub);
       

                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        fos.write(b, 0, cnt);
                    }
                   // System.out.println("복사완료" + temp);
                } catch (Exception e) {
                    int result = JOptionPane.showConfirmDialog(null,
                            failedCheck_list.get(failedCheck_list.size() - 1) + File.separator + count_Str
                                    + "에 오류가 났습니다 \n 오류내용 :" + e.toString() + "\n 복사전 파일위치" + file.toString()
                                    + "\n 복사후 파일위치" + temp.toString(),
                            "Confirm", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.CLOSED_OPTION) {

                    } else if (result == JOptionPane.YES_OPTION) {

                    }
                    e.printStackTrace();
                } finally {
                    try {

                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    /* (사용X) */
    public static void delete(String path) {

        File folder = new File(path);
        try {
            if (folder.exists()) {
                File[] folder_list = folder.listFiles();

                for (int i = 0; i < folder_list.length; i++) {
                    if (folder_list[i].isFile()) {
                        folder_list[i].delete();
                    } else {
                        delete(folder_list[i].getPath());
                    }
                    folder_list[i].delete();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public Frame(int x, int y, String title) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.Excell_list = new ArrayList<String>();
        this.Excell_list2 = new ArrayList<String>();

        setTitle(this.title);
        setSize(this.x, this.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        /** 메뉴 파일 생성 파트 1 */

        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu("파일");
        JMenuItem openitem = new JMenuItem("열기");

        openitem.addActionListener(new OpenActionListener(Excell_list, Excell_list2));
        fileMenu.add(openitem);
        mb.add(fileMenu);
        this.setJMenuBar(mb);

        /** 메뉴 파일 생성 파트 2 */

        JMenuBar mb2 = new JMenuBar();
        JMenu fileMenu2 = new JMenu("파일");
        JMenuItem openitem2 = new JMenuItem("열기");

        openitem2.addActionListener(new OpenActionListener(Excell_list, Excell_list2));
        fileMenu2.add(openitem2);
        mb2.add(fileMenu2);
        this.setJMenuBar(mb2);

        /** 텍스트 박스 */

        JTextField input_textbox = new JTextField();
        input_textbox.setText("");
        input_textbox.setToolTipText("");
        input_textbox.setBounds((int) (x / 3), y / 90, x / 4, y / 10);
        // getContentPane().add(input_textbox);

        JTextField old_textbox = new JTextField();
        old_textbox.setVisible(true);
        old_textbox.setText("c:\\\\");
        old_textbox.setToolTipText("");
        old_textbox.setBounds((x / 5), y / 2, x / 5, y / 20);
        getContentPane().add(old_textbox);

        JTextField new_textbox = new JTextField();
        new_textbox.setVisible(true);
        new_textbox.setText("c:\\\\");
        new_textbox.setToolTipText("");
        new_textbox.setBounds((x / 2), y / 2, x / 5, y / 20);
        getContentPane().add(new_textbox);

        JTextField mecro_old_textbox = new JTextField();
        mecro_old_textbox.setVisible(true);
        mecro_old_textbox.setText("X");
        mecro_old_textbox.setToolTipText("");
        mecro_old_textbox.setBounds((x / 5), (int) (y / 1.5), x / 5, y / 40);
        getContentPane().add(mecro_old_textbox);

        JTextField mecro_new_textbox = new JTextField();
        mecro_new_textbox.setVisible(true);
        mecro_new_textbox.setText("X");
        mecro_new_textbox.setToolTipText("");
        mecro_new_textbox.setBounds((x / 2), (int) (y / 1.5), x / 5, y / 40);
        getContentPane().add(mecro_new_textbox);

        JTextField mecro_old_textbox2 = new JTextField();
        mecro_old_textbox2.setVisible(true);
        mecro_old_textbox2.setText("X");
        mecro_old_textbox2.setToolTipText("");
        mecro_old_textbox2.setBounds((x / 5), (int) (y / 1.4), x / 5, y / 40);
        getContentPane().add(mecro_old_textbox2);

        JTextField mecro_new_textbox2 = new JTextField();
        mecro_new_textbox2.setVisible(true);
        mecro_new_textbox2.setText("X");
        mecro_new_textbox2.setToolTipText("");
        mecro_new_textbox2.setBounds((x / 2), (int) (y / 1.4), x / 5, y / 40);
        getContentPane().add(mecro_new_textbox2);

        JTextField mecro_old_textbox3 = new JTextField();
        mecro_old_textbox3.setVisible(true);
        mecro_old_textbox3.setText("X");
        mecro_old_textbox3.setToolTipText("");
        mecro_old_textbox3.setBounds((x / 5), (int) (y / 1.3), x / 5, y / 40);
        getContentPane().add(mecro_old_textbox3);

        JTextField mecro_new_textbox3 = new JTextField();
        mecro_new_textbox3.setVisible(true);
        mecro_new_textbox3.setText("X");
        mecro_new_textbox3.setToolTipText("");
        mecro_new_textbox3.setBounds((x / 2), (int) (y / 1.3), x / 5, y / 40);
        getContentPane().add(mecro_new_textbox3);

        JTextField mecro_old_textbox4 = new JTextField();
        mecro_old_textbox4.setVisible(true);
        mecro_old_textbox4.setText("X");
        mecro_old_textbox4.setToolTipText("");
        mecro_old_textbox4.setBounds((x / 5), (int) (y / 1.2), x / 5, y / 40);
        getContentPane().add(mecro_old_textbox4);

        JTextField mecro_new_textbox4 = new JTextField();
        mecro_new_textbox4.setVisible(true);
        mecro_new_textbox4.setText("X");
        mecro_new_textbox4.setToolTipText("");
        mecro_new_textbox4.setBounds((x / 2), (int) (y / 1.2), x / 5, y / 40);
        getContentPane().add(mecro_new_textbox4);

        /*
         * JTextField filename_textbox = new JTextField();
         * filename_textbox.setText("wow"); filename_textbox.setBounds((int) (x
         * / 4), y / 5, x / 4, y / 10); getContentPane().add(filename_textbox);
         */

        /** 라벨 */
        result_label = new JLabel();
        result_label.setBounds(0, y / 10, x, y / 80);

        result_label.setText("");
        // result_label.setHorizontalAlignment(JLabel.CENTER); // 수평 가운데 정렬

        JLabel old_label = new JLabel();
        old_label.setBounds((x / 5), y / 5, x, y / 2);
        old_label.setText("이동 전 경로");

        JLabel __label = new JLabel();
        __label.setBounds((int) (x / 3), y / 5, x, y / 2);
        __label.setText("------------>>>>");

        JLabel new_label = new JLabel();
        new_label.setBounds((x / 2), y / 5, x, y / 2);
        new_label.setText("이동 후 경로");

        getContentPane().add(result_label);
        getContentPane().add(old_label);
        getContentPane().add(new_label);
        getContentPane().add(__label);
        /** 버튼 */

        JButton save_button = new JButton("저장");
        save_button.setBounds((int) (x / 1.5), y / 90, x / 10, y / 10);
        getContentPane().add(save_button);

        JButton old_button = new JButton("저장");
        old_button.setBounds((int) (x / 5), (int) (y / 1.8), x / 10, y / 15);
        getContentPane().add(old_button);

        JButton new_button = new JButton("저장");
        new_button.setBounds((int) (x / 2), (int) (y / 1.8), x / 10, y / 15);
        getContentPane().add(new_button);

        /** 콤보박스 */
        JComboBox<String> new_path_list = new JComboBox<String>(String_array);
        new_path_list.setBounds((x / 2), y / 5, x / 5, y / 10);
        getContentPane().add(new_path_list);

        /** 콤보박스 */
        JComboBox<String> old_path_list = new JComboBox<String>(String_array);
        old_path_list.setBounds((x / 5), y / 5, x / 5, y / 10);
        getContentPane().add(old_path_list);

        /** 파일 찾기 */

        /** 버튼 리스너 */
        save_button.addActionListener(event -> {
            INPUT_PATH = input_textbox.getText();
            // FileName= "\\"+filename_textbox.getText();

            try {
                File_Save_Update2(new_PATH_DRIVE, old_PATH_DRIVE);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // File_Save_Update(new_PATH_DRIVE, old_PATH_DRIVE);

            // result_label.setText("");

        });

        old_button.addActionListener(event -> {
            old_PATH_DRIVE = old_textbox.getText();
            MECRO_OLDLIST.clear();
            MECRO_OLDLIST.add(mecro_old_textbox.getText());
            MECRO_OLDLIST.add(mecro_old_textbox2.getText());
            MECRO_OLDLIST.add(mecro_old_textbox3.getText());
            MECRO_OLDLIST.add(mecro_old_textbox4.getText());
            MECRO_OLDLIST.add("X");
            String result = MECRO_OLD_NAMEFunction();

            JOptionPane.showMessageDialog(null, old_PATH_DRIVE + "\n 매크로 : " + result);

        });
        new_button.addActionListener(event -> {
            INPUT_PATH = input_textbox.getText();
            new_PATH_DRIVE = new_textbox.getText();
            MECRO_NEWLIST.clear();
            MECRO_NEWLIST.add(mecro_new_textbox.getText());
            MECRO_NEWLIST.add(mecro_new_textbox2.getText());
            MECRO_NEWLIST.add(mecro_new_textbox3.getText());
            MECRO_NEWLIST.add(mecro_new_textbox4.getText());
            MECRO_NEWLIST.add("X");
            String result = MECRO_NEW_NAMEFunction();
            JOptionPane.showMessageDialog(null, new_PATH_DRIVE + INPUT_PATH + "\n 매크로 : " + result);
        });

        /** 콤보 박스 리스너 */
        new_path_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (new_path_list.getSelectedItem().toString() == "C") {
                    new_textbox.setVisible(false);
                    new_button.setVisible(false);
                    new_PATH_DRIVE = "C:\\";
                    new_PATH_CHECK = C;
                } else if (new_path_list.getSelectedItem().toString() == "D") {
                    new_textbox.setVisible(false);
                    new_button.setVisible(false);
                    new_PATH_DRIVE = "D:\\";
                    new_PATH_CHECK = D;
                } else if (new_path_list.getSelectedItem().toString() == "E") {
                    new_textbox.setVisible(false);
                    new_button.setVisible(false);
                    new_PATH_DRIVE = "E:\\";
                    new_PATH_CHECK = E;
                } else if (new_path_list.getSelectedItem().toString() == "F") {
                    new_textbox.setVisible(false);
                    new_button.setVisible(false);
                    new_PATH_DRIVE = "F:\\";
                    new_PATH_CHECK = F;
                } else {
                    new_textbox.setVisible(true);
                    new_button.setVisible(true);
                    new_PATH_CHECK = F;
                }

            }
        });

        old_path_list.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (old_path_list.getSelectedItem().toString() == "C") {
                    old_textbox.setVisible(false);
                    old_button.setVisible(false);
                    old_PATH_DRIVE = "C:\\";
                } else if (old_path_list.getSelectedItem().toString() == "D") {
                    old_textbox.setVisible(false);
                    old_button.setVisible(false);
                    old_PATH_DRIVE = "D:\\";
                } else if (old_path_list.getSelectedItem().toString() == "E") {
                    old_textbox.setVisible(false);
                    old_button.setVisible(false);
                    old_PATH_DRIVE = "E:\\";
                } else if (old_path_list.getSelectedItem().toString() == "F") {
                    old_textbox.setVisible(false);
                    old_button.setVisible(false);
                    old_PATH_DRIVE = "F:\\";
                } else {
                    old_textbox.setVisible(true);
                    old_button.setVisible(true);

                }
            }
        });

        setVisible(true);

    }

    public String MECRO_OLD_NAMEFunction() {
        String name = "\n";
        for (int i = 0; i < MECRO_OLDLIST.size(); i++) {
            name += MECRO_OLDLIST.get(i) + "\n";
            System.out.println(MECRO_OLDLIST.get(i));
        }
        return name;
    }

    public String MECRO_NEW_NAMEFunction() {
        String name = "\n";
        for (int i = 0; i < MECRO_NEWLIST.size(); i++) {
            name += MECRO_NEWLIST.get(i) + "\n";
        }
        return name;
    }

    public void File_ReNmae(String newPathDrive, String oldPathDirve) {

    }

    public void File_Save_Rename(String newPathDrive, String oldPathDirve) throws InterruptedException {
        MECRO_COUNT++;
        int result;
        int count = 0;
        folderpath = new ArrayList<String>();
        excellcount = 0;

        boolean old_result;
        boolean new_result;
        boolean count_result;

        String OLD_PATH = oldPathDirve;
        String NEW_PATH = newPathDrive;

        File oldFilefolder = new File(OLD_PATH);
        File newFilefolder = new File(NEW_PATH);

        System.out.println(OLD_PATH);
        System.out.println(NEW_PATH);

        Copy_Rename(oldFilefolder, newFilefolder);

    

        dispose a = new dispose();

        a.start();

        result_label.setText(result_label.getText() + "" + MECRO_COUNT + ":" + folderpath.size() + ",");

        result = JOptionPane
                .showConfirmDialog(JOptionPane.getRootFrame(),
                        "저장 완료!                                                                                                                                                                    "
                                + "\n\n\n\n\n\n\n\n\n\n 분류횟수: " + folderpath.size(),
                        "Confirm", JOptionPane.CLOSED_OPTION);

        System.out.println("다이로그 종료");

        if (!(MECRO_OLDLIST.get(MECRO_COUNT).equals("X") || MECRO_NEWLIST.get(MECRO_COUNT).equals("X"))) {
            System.out.println("매크로  MECRO_COUNT:" + MECRO_COUNT);
            System.out.println("매크로  MECRO_NEWLIST.size():" + MECRO_NEWLIST.size());
            if (MECRO_NEWLIST.size() != MECRO_COUNT) {
                System.out.println("매크로가 실행 됩니다 \n 잠시만 기다려주세요.");
                Thread.sleep(3000);
                System.out.println("매크로  MECRO_COUNT:" + MECRO_COUNT);
                System.out.println("매크로  OLD :" + MECRO_OLDLIST.get(MECRO_COUNT));
                System.out.println("매크로  NEW :" + MECRO_NEWLIST.get(MECRO_COUNT));
                File_Save_Rename(MECRO_NEWLIST.get(MECRO_COUNT), MECRO_OLDLIST.get(MECRO_COUNT));
            }
        } else {
            System.out.println("매크로작동X 종료");
            Thread.sleep(3000);
        }

    }

    public void File_Save_Update2(String newPathDrive, String oldPathDirve) throws InterruptedException {
        MECRO_COUNT++;
        int result;
        int count = 0;
        folderpath = new ArrayList<String>();
        excellcount = 0;

        getContentPane().add(result_label);
        boolean old_result;
        boolean new_result;
        boolean count_result;

        String OLD_PATH = oldPathDirve;
        String NEW_PATH = newPathDrive;

        // System.out.println("올드버전 : "+ OLD_PATH);
        // System.out.println("새버전 : "+ NEW_PATH);

        File oldFilefolder = new File(OLD_PATH);
        File newFilefolder = new File(NEW_PATH);

        System.out.println(OLD_PATH);
        System.out.println(NEW_PATH);

        copy_3(oldFilefolder, newFilefolder, OLD_PATH, NEW_PATH);

        {
            int excelcount = 0; // 엑셀
            System.out.println("엑셀저장중.....");

            FileOutputStream fos2 = null;
            try {
                System.out.println("엑셀 저장 완료 ! 총 갯수 : " + folderpath.size());
                fos2 = new FileOutputStream(NEW_PATH + File.separator + "작업분류.xlsx");

       

                dispose a = new dispose();

                a.start();

                result_label.setText(result_label.getText() + "" + MECRO_COUNT + ":" + folderpath.size() + ",");

                result = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                        "저장 완료!                                                                                                                                                                    "
                                + "\n\n\n\n\n\n\n\n\n\n 분류횟수: " + folderpath.size(),
                        "Confirm", JOptionPane.CLOSED_OPTION);

                System.out.println("다이로그 종료");

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {

                try {
                    if (fos2 != null) {
                        fos2.close(); // file resource 반환
                    }


                    if (!(MECRO_OLDLIST.get(MECRO_COUNT).equals("X") || MECRO_NEWLIST.get(MECRO_COUNT).equals("X"))) {
                        System.out.println("매크로  MECRO_COUNT:" + MECRO_COUNT);
                        System.out.println("매크로  MECRO_NEWLIST.size():" + MECRO_NEWLIST.size());
                        if (MECRO_NEWLIST.size() != MECRO_COUNT) {
                            System.out.println("매크로가 실행 됩니다 \n 잠시만 기다려주세요.");
                            Thread.sleep(3000);
                            System.out.println("매크로  MECRO_COUNT:" + MECRO_COUNT);
                            System.out.println("매크로  OLD :" + MECRO_OLDLIST.get(MECRO_COUNT));
                            System.out.println("매크로  NEW :" + MECRO_NEWLIST.get(MECRO_COUNT));
                            File_Save_Update2(MECRO_NEWLIST.get(MECRO_COUNT), MECRO_OLDLIST.get(MECRO_COUNT));
                        }
                    } else {
                        MECRO_COUNT = 0;
                        System.out.println("매크로작동X 종료");
                        Thread.sleep(3000);
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public void File_Save_Update(String newPathDrive, String oldPathDirve) // 엑셀->엑셀
    {
        boolean result;
        int count = 0;
        String filepath = newPathDrive;
        // Excel excel = new Excel(filepath, Excell_list, Excell_list2);
        for (int i = 0; i < Excell_list.size(); i++) {

            boolean old_result;
            boolean new_result;
            boolean count_result;

            String OLD_PATH = oldPathDirve + Excell_list.get(i);
            String NEW_PATH = newPathDrive + Excell_list2.get(i);

    

            File oldFilefolder = new File(OLD_PATH);
            File newFilefolder = new File(NEW_PATH);

            old_result = oldFilefolder.mkdir();
            new_result = newFilefolder.mkdir();
       

            old_result = oldFilefolder.mkdirs();
            new_result = newFilefolder.mkdirs();

            copy_2(oldFilefolder, newFilefolder, Excell_list2.get(i));

        }

    }

    public void File_Save(String newPathDrive, String oldPathDirve, String PATH, String FileName) throws IOException {
        /** 파일 입출력 */
        int check_result = 0;
        check_result = JOptionPane.showConfirmDialog(null,
                "" + old_PATH_DRIVE + "\\" + "(엑셀파일 경로) ->     " + new_PATH_DRIVE + "\\" + PATH + "으로 파일을 옮기겠습니까? \n");
        if (check_result == JOptionPane.YES_OPTION) {

            if (newPathDrive != "null" && oldPathDirve != "null") {

                String move_PATH = new_PATH_DRIVE + PATH;

                boolean result;
                boolean result_file;
                File newFilefolder = new File(move_PATH);
                // 최 하위 디렉토리에 대해서만 생성을 함.
                // 최 하위 디렉토리의 바루 상위 디렉토리가 존재하지 않을 경우,
                // 디렉토리가 생성되지 못하고, false를 리턴함
                result = newFilefolder.mkdir();
                System.out.println(
                        result ? newFilefolder + "에 폴더를 생성 하였습니다" : newFilefolder + "에 경로가 존재 하지않아  상위 디렉토리를 생성 합니다.");
                // 상위 디렉토리가 존재하지 않을 경우, 상위 디렉토리까지 생성함
                result = newFilefolder.mkdirs();
                System.out.println(result == true ? "상위 디렉토리 생성 완료" : "상위 디렉토리 생성 실패");
                for (int i = 0; i < Excell_list.size(); i++) {

                    String[] cut = Excell_list.get(i).split("\\\\"); // <- \ 임
                    // 4개써야

                    String[] cut2 = Excell_list2.get(i).split("\\\\"); // <- \ 임

                    Erros_list = new ArrayList<String>(); // 오류가 안뜸

                    File_namelist = new ArrayList<String>();

                    StringBuilder old_String_path = new StringBuilder();
                    StringBuilder new_String_path = new StringBuilder();
                    old_String_path.append(old_PATH_DRIVE);

                    for (int j = 0; j < cut.length; j++) {

                        if (j != cut.length - 1) {
                            if (j != cut.length - 2) /* 마지막은 경로 생략 */ {
                                old_String_path.append(cut[j] + "\\");
                            } else {
                                old_String_path.append(cut[j]);
                            }
                        } else {
                            File_namelist.add(cut[j]);
                        }

                        for (int k = 0; k < File_namelist.size(); k++) {
                            System.out.println("파일이름:" + File_namelist.get(k));
                        }
                        System.out.println("파일명 자르기 :" + old_String_path.toString());

                    }

                    System.out.println(old_String_path.toString());
                
                    File oldFilePath = new File(old_String_path.toString());
                    // copy_upgrade(oldFile, newFilePath);
                    if (Er == true)
                        break;
              

                }

            }

        } else {

        }

    }

}

/* 메뉴 열기 리스너 */
class OpenActionListener implements ActionListener {

    ArrayList<String> Excell_list;
    ArrayList<String> Excell_list2;

    JFileChooser chooser;

    public OpenActionListener(ArrayList<String> Excell_list, ArrayList<String> Excell_list2) {
        this.Excell_list = Excell_list;
        this.Excell_list2 = Excell_list2;

        chooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter(".xlsx  .xls ", "xlsx", "xls");
        chooser.setFileFilter(filter);

        int ret = chooser.showOpenDialog(null);
        if (ret != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "파일 선택 X ", "경고", JOptionPane.WARNING_MESSAGE);

            return;

        }

        String filepath = chooser.getSelectedFile().getPath();


    }

}

class dispose extends Thread {
    private boolean stopped = false;

    public dispose() {

        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        try {
            System.out.println("쓰레드 실행전");
            Thread.sleep(10000);
            System.out.println("쓰레드 실행끝");
            JOptionPane.getRootFrame().dispose();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        stopped = true;

    }

}

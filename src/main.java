import Implement.Block;
import Implement.BlockManager;
import Implement.FileManager;
import util.config;
import util.*;

import java.io.*;
import java.util.*;

import static java.lang.Integer.parseInt;
import static util.data.fmList;
import static util.util.getFileName;

public class main {
    public static void init() {
        System.out.println("开始初始化……");
        //初始化blockManager和fileManager
        for (int i = 0; i < 3; i++) {
            BlockManager bm = new BlockManager("BM" + i);
            FileManager fm = new FileManager("FM" + i);
            data.bmList.add(bm);
            fmList.add(fm);
        }


        //读取block并分配给各个blockManager
        File block = new File(config.BlockPath);
        File[] tempList = block.listFiles();
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();

        try {
            for (File value : tempList) {
                if (value.isFile()) {
                    String fileName = value.getName();
                    if (util.checkSuffix(fileName).equals(".meta")) {
                        try {
                            reader = new BufferedReader(new FileReader(value));
                            String tempStr;
                            while ((tempStr = reader.readLine()) != null) {
                                sbf.append(tempStr);
                                sbf.append("\n");
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("读取block文件时发生错误，初始化失败");
                        }
                        HashMap<String, String> inf = util.getMetaInfBlock(sbf.toString());
                        //生成保存在blockManager中的block
                        Block blo = new Block(getFileName(fileName), parseInt(inf.get("size")), inf.get("hash"), inf.get("check"), parseInt(inf.get("order")));
                        Random r = new Random();
                        data.bmList.get(r.nextInt(data.bmList.size())).addBlock(blo);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("<!> block文件夹为空");
        }


        //读取文件并分配给各个fileManager
        List<String> files = new ArrayList<String>();
        File file = new File(config.FilePath);
        File[] fileList = file.listFiles();
        BufferedReader reader2 = null;
        StringBuffer sbf2 = new StringBuffer();

        try {
            for (File value : fileList) {
                if (value.isFile()) {
                    String fileName = value.getName();
                    if (util.checkSuffix(fileName).equals(".meta")) {
                        try {
                            reader2 = new BufferedReader(new FileReader(value));
                            String tempStr;
                            while ((tempStr = reader2.readLine()) != null) {
                                sbf2.append(tempStr);
                                sbf2.append("\n");
                            }
                            reader2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("读取file文件时发生错误，初始化失败");
                        }
                        HashMap<String, String> inf = util.getMetaInfFile(sbf2.toString());
                        //生成保存在blockManager中的block
                        Random r = new Random();
                        Implement.File f = new Implement.File(inf.get("hash"), parseInt(inf.get("size")), util.getFileName(fileName));
                        fmList.get(r.nextInt(fmList.size())).addFile(f);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("<!> file文件夹为空");
        }
        System.out.println("初始化结束……");
    }

    public static void mainCircle() {
        System.out.println("文件管理系统运行中");
        for (FileManager fm : fmList) {
            System.out.println(fm.name + ":");
            for (Implement.File f : fm.getFileList()) {
                System.out.print(f.getName());
            }
            System.out.println();
        }
    }




    public static void main(String[] args) {
        init();
        mainCircle();
        test.readTest();

    }
}

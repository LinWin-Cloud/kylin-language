

import KylinException.KylinRuntimeException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("ALL")
public class KptMain {
    public static final String info = " Kpt:\n" +
            " -n [name]              Create a new package.\n" +
            " -i [package path]      Install a package on system.\n" +
            " -version               Show the version information.\n" +
            " -list                  List all the install package.";
    public static final String VERSION = "1.0";
    public static final String jarDirectory = null;
    static {
        try {
            //获取jar包的位置
            jarDirectory = new File(KptMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getName(String filename) {
        try {
            return filename.substring(0 , filename.lastIndexOf("."));
        }catch (Exception exception) {
            return filename;
        }
    }
    public static String getLastName(String filename) {
        try {
            return filename.substring(filename.lastIndexOf(".")+1);
        }catch (Exception e) {
            return filename;
        }
    }
    public static void main(String[] args) {
        try {
            if ("-version".equals(args[0])) {
                System.out.println(version);
            }
            else if ("-n".equals(args[0])) {
                String name = args[1];
                new File(name).mkdir();
                FileWriter fileWriter = new FileWriter(name+"/info.json");
                fileWriter.write("{\n" +
                        "  \"name\" : \""+name+"\",\n" +
                        "  \"author\": \"\",\n" +
                        "  \"time\": \"\",\n" +
                        "  \"depend\": null,\n" +
                        "  \"version\": 1.0\n" +
                        "}");
                fileWriter.close();
                System.out.println("Create Package: "+name);
            }
            else if ("-i".equals(args[0])) {
                unzip(args[1] , jarDirectory+"/../lib/");
            }
            else if ("-list".equals(args[0])) {
                System.out.println(" [Package] ==>");
                File[] files = new File(jarDirectory+"/../lib_info/").listFiles();
                StringBuilder stringBuilder = new StringBuilder();
                for (File i : Objects.requireNonNull(files)) {
                    if ("json".equals(getLastName(i.getName()))) {
                        stringBuilder.append(" - ");
                        stringBuilder.append(getLastName(getName(i.getName())));
                        stringBuilder.append("\n");
                    }
                }
                System.out.println(stringBuilder);
            }
        }catch (Exception e) {
            System.out.println(info);
        }
    }
    /**
     * 解压ZIP文件到指定目录
     *
     * @param zipFilePath ZIP文件的路径
     * @param destDirPath 解压目标目录的路径
     */
    public static void unzip(String zipFilePath, String destDirPath) {
        // 创建目标目录
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry entry = zipIn.getNextEntry();
            // 遍历ZIP文件中的每一个条目
            while (entry != null) {
                String filePath = destDirPath + File.separator + entry.getName();
                System.out.println("[INFO] INSTALL: "+entry.getName()+".");
                if (!entry.isDirectory()) {
                    if ("info.json".equals(entry.getName())) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipIn));
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuilder.append(line);
                        }
                        //bufferedReader.close();
                        FileWriter fileWriter = new FileWriter(jarDirectory+"/../lib_info/"+getName(new File(zipFilePath).getName())+".json");
                        fileWriter.write(stringBuilder.toString());
                        fileWriter.close();
                        entry = zipIn.getNextEntry();
                        continue;
                    }
                    // 解压文件
                    extractFile(zipIn, filePath);
                } else {
                    // 创建目录
                    File dir1 = new File(filePath);
                    dir1.mkdirs();
                }
                entry = zipIn.getNextEntry();
            }
            System.out.println("[FINISH] Install OK.");
        } catch (IOException e) {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException(e.getMessage(),0,true);
            kylinRuntimeException.setStackTrace(e.getStackTrace());
        }
    }
    // 解压文件到指定路径
    @SuppressWarnings("IOStreamConstructor")
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        //System.out.println(filePath);
        //noinspection IOStreamConstructor
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}

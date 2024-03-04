import main.mainApp;

import java.io.File;
import java.net.URISyntaxException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class KptMain {
    public static String info = """
 Kpt:
 -n [name]              Create a new package.
 -i [package path]      Install a package on system.
 -version               Show the version information.
            """;
    public static String version = "1.0";
    public static String jarDirectory = null;
    static {
        try {
            //获取jar包的位置
            jarDirectory = new File(mainApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        try {
            if (args[0].equals("-version")) {
                System.out.println(version);
            }
            else if (args[0].equals("-n")) {

            }
            else if (args[0].equals("-i")) {
                unzip(args[1] , jarDirectory+"/../lib/");
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
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            // 遍历ZIP文件中的每一个条目
            while (entry != null) {
                String filePath = destDirPath + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // 解压文件
                    extractFile(zipIn, filePath);
                } else {
                    // 创建目录
                    File dir1 = new File(filePath);
                    dir1.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 解压文件到指定路径
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}

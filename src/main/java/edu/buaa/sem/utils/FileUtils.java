package edu.buaa.sem.utils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.lf5.util.StreamUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;

public class FileUtils {
    private static final int buffer = 2048;

    /**
     * 获得根目录".../sett/"
     *
     * @return
     */
    public static String getRootPath() {
        Class<FileUtils> clazz = FileUtils.class;
        String path = clazz.getResource("/").getPath();// 得到包含/sett/WEB-INF/classes/的路径
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().endsWith("linux")
                || osName.toLowerCase().startsWith("mac")) {
            path = path.substring(0, path.indexOf("WEB-INF"));
        } else {
            path = path.substring(1, path.indexOf("WEB-INF"));
        }
        return path;
    }

    /**
     * 某目录下所有文件
     *
     * @param path
     * @return
     */
    public static String[] findAll(String path) {
        String filePath = getRealPath(path);
        File file = new File(filePath);
        return file.list();
    }

    /**
     * 获取虚拟目录"/file"下的实际文件地址
     *
     * @param path 以"/file/"开头
     * @return
     */
    public static String getRealPath(String path) {
        System.out.println(ContextLoader.getCurrentWebApplicationContext()
                .getServletContext().getContext("/file"));
        return ContextLoader.getCurrentWebApplicationContext()
                .getServletContext().getContext("/file")
                .getRealPath(path.substring(5));
    }

    /**
     * 读取文件内容，UTF-8
     *
     * @param path
     * @return
     */
    public static String getFileContent(String path) {
        StringBuffer buffer = new StringBuffer();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 将数据写入文件
     *
     * @param directory 服务器上的文件夹,即webapp下的文件夹,如webapp下有文件夹images, 则directory="/images"
     * @param fileName  文件名,包括扩展名
     * @param content   要写入的数据
     */
    public static void writeContentToFile(String directory, String fileName,
                                          String content) {
        String filePath = ContextLoader.getCurrentWebApplicationContext()
                .getServletContext().getRealPath(directory);
        fileName = filePath + "/" + fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fileOutpurStream = null;
        OutputStreamWriter writer = null;
        try {
            fileOutpurStream = new FileOutputStream(fileName, false);
            writer = new OutputStreamWriter(fileOutpurStream, "UTF-8");
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutpurStream != null) {
                try {
                    fileOutpurStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String uploadUUID(MultipartFile file, String savePath) {
        if (file != null && savePath != null && !savePath.isEmpty()) {
            // 文件真实路径
            String realPath = getRealPath(savePath);
            // 新文件名
            String newFileName = "";
            String fileName = java.util.UUID.randomUUID().toString();
            // 原文件后缀名
            String suffix = file.getOriginalFilename().substring(
                    file.getOriginalFilename().lastIndexOf("."));

            // 新文件名
            newFileName = fileName + suffix;

            try {
                file.transferTo(new File(realPath + "/" + newFileName));
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            String pathOnServer = savePath + newFileName;
            return pathOnServer;
        }
        return "error";
    }

    /**
     * 上传文件方法
     *
     * @param file             文件
     * @param savePath         文件上传目录(如：/file/activity/images/)
     * @param hasLastUnderline 新文件名后缀前是否含有下划线
     * @return 返回 路径+新文件名 组成的字符串
     */
    public static String uploadFileWithNewName(MultipartFile file,
                                               String savePath, boolean hasLastUnderline) {
        if (savePath != null && !savePath.isEmpty()) {
            // 文件真实路径
            String realPath = getRealPath(savePath);
            // 新文件名
            String newFileName = "";
            String fileName = file.getOriginalFilename();
            try {
                // 原文件后缀名
                String suffix = fileName
                        .substring(fileName.lastIndexOf(".") + 1);
                // 原文件名称（不含后缀名）
                String fileNameWithoutSuffix = fileName.substring(0,
                        fileName.lastIndexOf(".")).replaceAll(" ", "");
                // 新文件名
                if (hasLastUnderline) {
                    newFileName = fileNameWithoutSuffix + "-"
                            + new java.util.Date().getTime() + "_." + suffix;// 这里新文件名：原文件名_当时毫秒数_.后缀名
                } else {
                    newFileName = fileNameWithoutSuffix + "-"
                            + new java.util.Date().getTime() + "." + suffix;// 这里新文件名：原文件名_当时毫秒数.后缀名
                }
                file.transferTo(new File(realPath + "/" + newFileName));
                String pathOnServer = savePath + newFileName;
                return pathOnServer;
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }
        return "error";
    }

    /**
     * 上传图片方法，根据desWidth和desHeight强制拉伸或缩放
     *
     * @param file
     * @param savePath
     * @param hasLastUnderline
     * @param desHeight        目标图片的高度，若为0，则默认为图片原高度
     * @param desWidth         目标图片的宽度，若为0，则默认为图片原高度
     * @return
     */
    public static String uploadImageToFixedHeightAndWidth(MultipartFile file,
                                                          String savePath, boolean hasLastUnderline, int desHeight,
                                                          int desWidth) {
        String uploadFileName = uploadFileWithNewName(file, savePath,
                hasLastUnderline);
        if (uploadFileName != null && !uploadFileName.equals("error")) {
            // 文件上传后的路径
            String realPath = getRealPath(uploadFileName);
            try {
                File sourceImgFile = new File(realPath);
                BufferedImage bi = ImageIO.read(sourceImgFile);
                int srcWidth = bi.getWidth();
                int srcHeight = bi.getHeight();
                int destWidth = desWidth == 0 ? srcWidth : desWidth;
                int destHeight = desHeight == 0 ? srcHeight : desHeight;
                return zoomImageByWidthAndHeight(uploadFileName, destWidth,
                        destHeight);
            } catch (Exception e) {
                e.printStackTrace();
                // 若异常，则删除该图片
                deleteFileByPath(uploadFileName);
                return "error";
            }
        }
        // 若异常，则删除该图片
        deleteFileByPath(uploadFileName);
        return "error";
    }

    /**
     * 根据制定的高度和宽度，以高度比和宽度比两者中的较大比例，拉伸或压缩图片
     *
     * @param sourcePath
     * @param height     目标图片的高度
     * @param width      目标图片的宽度
     * @return
     */
    public static String scaleImageByWidthAndHeight(String sourcePath,
                                                    int width, int height) {
        String realPath = getRealPath(sourcePath);
        try {
            File sourceImgFile = new File(realPath);
            BufferedImage bi = ImageIO.read(sourceImgFile);
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();

            if (srcHeight > width || srcWidth > height) {
                double ratioHeight = (double) height / srcHeight;
                double ratioWight = (double) width / srcWidth;
                double ratio = ratioHeight > ratioWight ? ratioWight
                        : ratioHeight;
                srcHeight = (int) Math.round(srcHeight * ratio);
                srcWidth = (int) Math.round(srcWidth * ratio);
            }

            return zoomImageByWidthAndHeight(sourcePath, srcWidth, srcHeight);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 按比例拉伸或缩放图片到指定高度,若指定高度为0,则不缩放
     *
     * @param sourcePath
     * @param height     目标图片的高度
     * @return
     */
    public static String scaleImageByHeight(String sourcePath, int height) {
        String realPath = getRealPath(sourcePath);
        try {
            File sourceImgFile = new File(realPath);
            BufferedImage bi = ImageIO.read(sourceImgFile);
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            int destWidth = srcWidth;
            int destHeight = height == 0 ? srcHeight : height;

            double ratio = (double) destHeight / srcHeight;
            destHeight = (int) Math.round(srcHeight * ratio);
            destWidth = (int) Math.round(srcWidth * ratio);

            return zoomImageByWidthAndHeight(sourcePath, destWidth, destHeight);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 按比例拉伸或缩放图片到指定宽度,若指定宽度为0,则不缩放
     *
     * @param sourcePath
     * @param width      目标图片的宽度
     * @return
     */
    public static String scaleImageByWidth(String sourcePath, int width) {
        String realPath = getRealPath(sourcePath);
        try {
            File sourceImgFile = new File(realPath);
            BufferedImage bi = ImageIO.read(sourceImgFile);
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            int destWidth = width == 0 ? srcWidth : width;
            int destHeight = srcHeight;

            double ratio = (double) destWidth / srcWidth;
            destHeight = (int) Math.round(srcHeight * ratio);
            destWidth = (int) Math.round(srcWidth * ratio);

            return zoomImageByWidthAndHeight(sourcePath, destWidth, destHeight);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 裁剪图片
     *
     * @param sourcePath 图片源路径（格式：/file/images/142020405464_.jpg）
     * @param x1         裁剪参数
     * @param x2         裁剪参数
     * @param y1         裁剪参数
     * @param y2         裁剪参数
     * @param imageScale 图片缩放比例，实际裁剪的图片的长宽为目标矩形的长宽乘以缩放比例
     * @return 返回新文件名，会自动删除掉原有路径上的图片
     */
    public static String cutImage(String sourcePath, int x1, int x2, int y1,
                                  int y2, int imageScale, boolean hasLastUnderline) {
        try {
            if (sourcePath != null && !sourcePath.isEmpty()) {
                String sourceRealPath = getRealPath(sourcePath);
                Image img;
                ImageFilter cropFilter;
                File sourceImgFile = new File(sourceRealPath);
                BufferedImage bi = ImageIO.read(sourceImgFile);
                int srcWidth = bi.getWidth();
                int srcHeight = bi.getHeight();
                int destWidth = x2 - x1;
                int destHeight = y2 - y1;
                // 如果没裁剪，默认整个图片都有，保持正方形
                if (x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0) {
                    destWidth = srcWidth > srcHeight ? srcHeight : srcWidth;
                    destHeight = destWidth;
                }
                if (srcWidth >= destWidth && srcHeight >= destHeight) {
                    Image image = bi.getScaledInstance(srcWidth, srcHeight,
                            Image.SCALE_DEFAULT);
                    cropFilter = new CropImageFilter(x1, y1, destWidth,
                            destHeight);
                    img = Toolkit.getDefaultToolkit().createImage(
                            new FilteredImageSource(image.getSource(),
                                    cropFilter));
                    BufferedImage tag = new BufferedImage(destWidth,
                            destHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = tag.createGraphics();
                    // 源文件的后缀名
                    String suffix = sourcePath.substring(sourcePath
                            .lastIndexOf(".") + 1);
                    if (!suffix.equals("png")) {
                        tag = g.getDeviceConfiguration().createCompatibleImage(
                                destWidth, destHeight);
                    } else
                        tag = g.getDeviceConfiguration()
                                .createCompatibleImage(destWidth, destHeight,
                                        Transparency.TRANSLUCENT);
                    g = tag.createGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    String newPath = "";
                    if (sourcePath.lastIndexOf("-origin") != -1) {
                        sourcePath = sourcePath.substring(0,
                                sourcePath.lastIndexOf("-origin"))
                                + sourcePath.substring(
                                sourcePath.lastIndexOf("-origin") + 7,
                                sourcePath.length());
                    }
                    if (hasLastUnderline) {
                        newPath = sourcePath.substring(0,
                                sourcePath.lastIndexOf("-") + 1)
                                + new java.util.Date().getTime()
                                + "_."
                                + suffix;
                    } else {
                        newPath = sourcePath.substring(0,
                                sourcePath.lastIndexOf("-") + 1)
                                + new java.util.Date().getTime() + "." + suffix;
                    }
                    String desRealPath = getRealPath(newPath);
                    // 输出内存中的图像
                    ImageIO.write(tag, suffix, new File(desRealPath));
                    return newPath;
                } else {
                    return "error";
                }
            }
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String zoomImageForThinktank(String sourcePath) {
        return zoomImageForMark(sourcePath);
    }

    public static String zoomImageForDemand(String sourcePath) {
        return zoomImageForMark(sourcePath);
    }

    public static String zoomImageForHomePage(String sourcePath) {
        return zoomImageForMark(sourcePath);
    }

    public static String zoomImageForMarkImage(String sourcePath) {
        return zoomImageForMark(sourcePath);
    }

    public static String zoomImageForThinktankAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForDiscussionAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForDemandAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForTopicAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForPersonalAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForActivityAd(String sourcePath) {
        return zoomImageForAd(sourcePath);
    }

    public static String zoomImageForMark(String sourcePath) {
        // return zoomImageByPath(sourcePath, 600, 400); 美工的比例
        return zoomImageByWidthAndHeight(sourcePath, 770, 360);
    }

    public static String zoomImageForAd(String sourcePath) {
        return scaleImageByWidth(sourcePath, 350);
    }

    /**
     * 拉伸或缩放图片方法，根据desWidth和desHeight强制拉伸或缩放
     *
     * @param sourcePath
     * @param height     目标图片的高度，若为0，则默认为图片原高度
     * @param width      目标图片的宽度，若为0，则默认为图片原高度
     * @return
     */
    public static String zoomImageByWidthAndHeight(String sourcePath,
                                                   int width, int height) {
        String realPath = getRealPath(sourcePath);
        String suffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
        try {
            File sourceImgFile = new File(realPath);
            BufferedImage bi = ImageIO.read(sourceImgFile);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB); // 缩放图像
            Image image = bi.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            Graphics2D g = tag.createGraphics();
            if (!suffix.equals("png")) {
                tag = g.getDeviceConfiguration().createCompatibleImage(width,
                        height);
            } else
                tag = g.getDeviceConfiguration().createCompatibleImage(width,
                        height, Transparency.TRANSLUCENT);
            g = tag.createGraphics();
            g.drawImage(image, 0, 0, null); // 绘制目标图
            g.dispose();
            // 输出内存中的图像，以源文件的文件类型生成，生成的名字和源文件相同,路径也相同.
            ImageIO.write(tag, suffix, new File(realPath));
            return sourcePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 将文件重命名为原文件名_.原文件后缀名 即：将原文件加上下划线，所有文件必须加上下划线才是有用的文件，否则可能被认为是无效文件而被清理
     *
     * @param filePath 文件路径
     * @return 返回新文件的路径（由原文件重命名而来）
     */
    public static String renameFileWithUnderline(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            String fileRealPath = getRealPath(filePath);
            if (fileRealPath == null || fileRealPath.isEmpty()) {
                return "error";
            }
            File file = new File(fileRealPath);

            // 去除后缀后的名字
            String fileNameWithoutSuffix = filePath.substring(0,
                    filePath.lastIndexOf("."));

            // 如果文件名自带下划线
            if (fileNameWithoutSuffix.endsWith("_")) {
                // 如果服务器存在带下划线的文件，直接返回
                if (file != null && file.isFile() && file.exists()) {
                    return filePath;
                    // 如果不存在带下划线的文件，看看是否有不带下划线的文件。如果有，修改之返回
                } else {
                    String newPath = filePath.substring(0,
                            filePath.lastIndexOf("_"))
                            + filePath.substring(filePath.lastIndexOf("."));
                    String newRealPath = getRealPath(newPath);
                    File file0 = new File(newRealPath);
                    if (file0 != null && file0.isFile() && file0.exists()) {
                        file0.renameTo(new File(fileRealPath));
                        return filePath;
                    }
                }
                // 如果文件名不带下划线
            } else {
                // 如果服务器存在不带下划线的文件，重命名后返回
                if (file != null && file.isFile() && file.exists()) {
                    String newPath = filePath.substring(0,
                            filePath.lastIndexOf("."))
                            + "_"
                            + filePath.substring(filePath.lastIndexOf("."));
                    String newRealPath = getRealPath(newPath);
                    file.renameTo(new File(newRealPath));
                    return newPath;
                    // 如果服务器存在带下划线的文件，直接返回
                } else {
                    String newPath = filePath.substring(0,
                            filePath.lastIndexOf("."))
                            + "_"
                            + filePath.substring(filePath.lastIndexOf("."));
                    String newRealPath = getRealPath(newPath);
                    File file0 = new File(newRealPath);
                    if (file0 != null && file0.isFile() && file0.exists()) {
                        return newPath;
                    }
                }
            }
        }
        return "error";
    }

    /**
     * 根据路径删除文件
     *
     * @param path
     */
    public static void deleteFileByPath(String path) {

        if (path == null || path.isEmpty() || path.contains("default")) {
            return;
        }
        if (!isFromPictureHouse(path)) {
            try {
                String filePath = getRealPath(path);
                File file = new File(filePath);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件时重命名文件
     *
     * @param savePath
     * @param multiFile
     * @return
     */
    public static String uploadWithRename(String savePath,
                                          MultipartFile multiFile) {
        if (savePath != null && !savePath.isEmpty()) {
            String fileName = multiFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 原文件名称（不含后缀名）
            String fileNameWithoutSuffix = fileName.substring(0,
                    fileName.lastIndexOf("."));
            String newFileName = null;
            try {
                newFileName = EncryptionUtils.getMD5(fileNameWithoutSuffix) + "-"
                        + new java.util.Date().getTime() + "." + suffix;
            } catch (Exception e) {
                e.printStackTrace();
            }
            String newFilePath = savePath + newFileName;
            String realPath = getRealPath(newFilePath);
            try {
                multiFile.transferTo(new File(realPath));
                return newFilePath;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return "error";
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "error";
        }
    }

    /**
     * 上传文件时重命名文件,知识库素材使用，与上一个方法就差一个编码不同
     *
     * @param savePath
     * @param multiFile
     * @return
     */
    public static String uploadWithRenameForKnowledge(String savePath,
                                                      MultipartFile multiFile) {
        if (savePath != null && !savePath.isEmpty()) {
            String fileName = multiFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 原文件名称（不含后缀名）
            String fileNameWithoutSuffix = fileName.substring(0,
                    fileName.lastIndexOf("."));
            String newFileName = fileNameWithoutSuffix + "-"
                    + new java.util.Date().getTime() + "_." + suffix;
            String newFilePath = savePath + newFileName;
            String realPath = getRealPath(newFilePath);
            try {
                multiFile.transferTo(new File(realPath));
                return newFilePath;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return "error";
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "error";
        }
    }

    /**
     * 将压缩包解压,解压后的文件夹名字与压缩包的相同，保存时执行
     *
     * @param savePath  保存的路径（非真实路径）
     * @param filePath  要解压的压缩包位置（非真实路径）
     * @param multiFile
     * @return
     */
    public static String uploadWithRenameZip(String savePath,
                                             String filePath) {
        if (savePath != null && !savePath.isEmpty()) {

            //新生成的文件夹名字与zip保持一致
            String originalFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            byte buf[] = new byte[buffer];
            int count = -1;
            //第一个文件夹是整体的文件夹，需要保存名字作为url的对象
            int checkFirstFile = 0;
            String firstFile = "";

            try {
                ZipFile zipFile = new ZipFile(getRealPath(filePath), "GB18030");
                Enumeration entries = zipFile.getEntries();


                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String filename = entry.getName();
                    String fileName2 = entry.getName();
                    boolean ismkdir = false;
                    if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
                        ismkdir = true;
                    }
                    filename = getRealPath(savePath) + filename;
                    if (entry.isDirectory()) { //如果是文件夹先创建
                        if (checkFirstFile == 0) {
                            checkFirstFile = 1;
                            firstFile = fileName2.replace("/", "");
                        }
                        File file = new File(filename);
                        file.mkdirs();
                        continue;
                    }
                    File file = new File(filename);
                    if (!file.exists()) { //如果是目录先创建
                        if (ismkdir) {
                            new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                        }
                    }
                    file.createNewFile(); //创建文件
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer);
                    while ((count = is.read(buf)) > -1) {
                        bos.write(buf, 0, count);
                    }

                    bos.flush();
                    bos.close();
                    fos.close();
                    is.close();

                }
                zipFile.close();
            } catch (IOException e) {
                return "error";
            }

//			// 将压缩包删除
//			deleteFileByPathForZip(getRealPath(filePath));
            //将新建成的文件夹改名为与zip文件相同
            File file = new File(getRealPath(savePath) + firstFile);
            String newFileName = originalFileName;
            file.renameTo(new File(getRealPath(savePath) + newFileName));
            String newFilePath = savePath + newFileName;
            return newFilePath;
        } else {
            return "error";
        }
    }


    /**
     * 解压zip文件、文件夹名字修改为与压缩包相同、上传
     *
     * @param savePath 保存的路径（非真实路径）
     * @param filePath 要解压的压缩包位置（非真实路径）
     * @return
     */
    public static String uploadWithZip(String savePath,
                                       String filePath, String fileName) {
        if (savePath != null && !savePath.isEmpty()) {
            String dictionaryPath = savePath + fileName;
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            byte buf[] = new byte[buffer];
            int count = -1;

            //第一个文件夹是整体的文件夹，需要保存名字作为url的对象
            int checkFirstFile = 0;
            String firstFile = "";

            try {
                ZipFile zipFile = new ZipFile(getRealPath(filePath), "GB18030");
                Enumeration entries = zipFile.getEntries();


                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String filename = entry.getName();
                    String fileName2 = entry.getName();
                    boolean ismkdir = false;
                    if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
                        ismkdir = true;
                    }
                    filename = getRealPath(savePath) + filename;
                    if (entry.isDirectory()) { //如果是文件夹先创建
                        if (checkFirstFile == 0) {
                            checkFirstFile = 1;
                            firstFile = fileName2.replace("/", "");
                        }
                        File file = new File(filename);
                        file.mkdirs();
                        continue;
                    }
                    File file = new File(filename);
                    if (!file.exists()) { //如果是目录先创建
                        if (ismkdir) {
                            new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                        }
                    }
                    file.createNewFile(); //创建文件
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer);
                    while ((count = is.read(buf)) > -1) {
                        bos.write(buf, 0, count);
                    }

                    bos.flush();
                    bos.close();
                    fos.close();
                    is.close();

                }
                zipFile.close();
            } catch (IOException e) {
                return "error";
            }

//			// 将压缩包删除
//			deleteFileByPathForZip(getRealPath(filePath));
            //将新建成的文件夹改名为一般标准（名字 + - + 时间毫秒）,压缩包和文件夹改为一个
            File file = new File(getRealPath(savePath) + firstFile);
            String newFileName = fileName.substring(0, fileName.lastIndexOf("."));
            file.renameTo(new File(getRealPath(savePath) + newFileName));
            String newFilePath = savePath + newFileName;
            return newFilePath;
        } else {
            return "error";
        }
    }


    /**
     * 上传压缩包时删除压缩包
     *
     * @param path
     */
    public static void deleteFileByPathForZip(String path) {


        if (path == null || path.isEmpty() || path.contains("default")) {
            return;
        }
        if (!isFromPictureHouse(path)) {
            try {
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String renameFileWithOrigin(String sourcePath, String newPath) {
        if (sourcePath != null && !sourcePath.isEmpty()) {
            String sourceRealPath = getRealPath(sourcePath);
            File sourceImgFile = new File(sourceRealPath);
            String newPathFirst = newPath
                    .substring(0, newPath.lastIndexOf("-"));
            String newPathLast = newPath.substring(newPath.lastIndexOf("-"),
                    newPath.length());
            if (sourceImgFile != null && sourceImgFile.isFile()
                    && sourceImgFile.exists()) {
                String sourceNewPath = newPathFirst + "-origin" + newPathLast;
                String sourceNewRealPath = getRealPath(sourceNewPath);
                sourceImgFile.renameTo(new File(sourceNewRealPath));
                return sourceNewPath;
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    /**
     * 判断图片来源，是否来自图片库
     *
     * @param imagepath
     * @return
     */
    public static boolean isFromPictureHouse(String imagepath) {
        if (imagepath.contains("/file/picturehouse")) {
            return true;
        }
        return false;

    }

    // 删除文件夹

    /**
     * 删除文件夹下所有文件及其文件夹
     *
     * @param folderPath 文件夹的绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件,由delFolder调用，不可单独使用
     *
     * @param filePath 文件夹完整绝对路径
     * @return
     */
    private static boolean delAllFile(String filePath) {

        boolean flag = false;
        File file = new File(filePath);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + tempList[i]);
            } else {
                temp = new File(filePath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(filePath + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(filePath + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void downloadFile(String filePath, HttpServletResponse response) {
        try {
            File downloadFile = new File(filePath);
            String fileName = downloadFile.getName();
            // TODO HttpServletResponse response =
            // ServletActionContext.getResponse();
            response.reset();
            // 设置response流信息的头类型，MIME码
            response.setContentType("application/msexcel;charset=UTF-8");

            response.addHeader(
                    "Content-Disposition",
                    "attachment;filename=\""
                            + new String(fileName.getBytes("GBK"), "ISO8859_1")
                            + "\"");
            InputStream iStream = new FileInputStream(downloadFile);
            OutputStream out = response.getOutputStream();
            out.write(StreamUtils.getBytes(iStream));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 判断文件夹是否为空
     *
     * @param filePath
     * @return
     */
    public static boolean isDirectoryEmpty(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files.length > 0) {
                return false;
            }
        }

        return true;
    }
}

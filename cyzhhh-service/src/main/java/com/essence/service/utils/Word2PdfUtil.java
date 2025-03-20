package com.essence.service.utils;

import com.aspose.words.Shape;
import com.aspose.words.*;

import java.awt.*;
import java.io.*;

/**
 * word转pdf工具类
 *
 * @author shmily
 */
public class Word2PdfUtil {

    /**
     * 许可证字符串(可以放到resource下的xml文件中也可)
     */
    private static final String LICENSE = "<License>" + "<Data>" + "<Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products>" + "<EditionType>Enterprise</EditionType>" + "<SubscriptionExpiry>20991231</SubscriptionExpiry>" + "<LicenseExpiry>20991231</LicenseExpiry>" + "<SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" + "</Data>" + "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" + "</License>";


    /**
     * 设置 license 去除水印
     */
    private static void setLicense() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(LICENSE.getBytes());
        License license = new License();
        try {
            license.setLicense(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * word转PDF （）
     * @param wordPath
     * @param pdfPath
     * @return
     */
    public static boolean wordConvertPdfFile(String wordPath, String pdfPath) {
        FileOutputStream fileOutputStream = null;
        try {
            pdfPath = pdfPath == null ? getPdfFilePath(wordPath) : pdfPath;
            setLicense();
            File file = new File(pdfPath);  //新建一个pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(wordPath);  //Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB,
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//
//    public static void main(String[] args) {
//        wordConvertPdfFile("/Users/bird/Downloads/bushu/kaka.doc", "/Users/bird/Downloads/bushu/kakaLiu.pdf","sdf");
//    }
//

    /**
     * word 转 pdf 生成至指定路径，pdf为空则上传至word同级目录
     *  vr
     * @param wordPath word文件路径
     * @param pdfPath  pdf文件路径
     */
    @Deprecated
    public static void wordConvertPdfFile(String wordPath, String pdfPath, String watermarkText) {
        FileOutputStream fileOutputStream = null;
        try {
            pdfPath = pdfPath == null ? getPdfFilePath(wordPath) : pdfPath;
            setLicense();
            File file = new File(pdfPath);
            fileOutputStream = new FileOutputStream(file);
            Document doc = new Document(wordPath);

            // Ensure fonts are embedded
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setEmbedFullFonts(true);
            saveOptions.setCompliance(PdfCompliance.PDF_A_1_B);

//            if (watermarkText != null && !watermarkText.isEmpty()) {
//                addWatermark(doc, watermarkText);
//            }
            doc.save(fileOutputStream, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addTopRightText(Document doc, String topRightText) throws Exception {
        for (Section sect : doc.getSections()) {
            HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(HeaderFooterType.HEADER_PRIMARY);
            if (header == null) {
                header = new HeaderFooter(sect.getDocument(), HeaderFooterType.HEADER_PRIMARY);
                sect.getHeadersFooters().add(header);
            }
            Paragraph para = new Paragraph(doc);
            Run run = new Run(doc, topRightText);
            para.appendChild(run);
            para.getParagraphFormat().setAlignment(ParagraphAlignment.RIGHT);
            header.appendChild(para);
        }
    }

    private static void addWatermark(Document doc, String watermarkText) throws Exception {
        for (Section sect : doc.getSections()) {
            // 创建四个水印形状（一共需要四个水印）
            for (int i = 0; i < 4; i++) {
                Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);

                // 设置水印文本
                watermark.getTextPath().setText(watermarkText);
                watermark.getTextPath().setFontFamily("黑体");  // 改为黑体
                watermark.getTextPath().setSize(28);  // 改为28号
                watermark.getTextPath().setBold(true);

                // 设置水印大小
                watermark.setWidth(300);
                watermark.setHeight(100);
                watermark.setRotation(-45);

                // 设置水印颜色和透明度
                Color watermarkColor = new Color(192, 192, 192, 128);
                watermark.setFillColor(watermarkColor);
                watermark.setStrokeColor(new Color(0, 0, 0, 0));

                // 设置水印位置
                watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
                watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);

                // 根据索引设置不同的位置
                switch (i) {
                    case 0: // 左上
                        watermark.setLeft(100);
                        watermark.setTop(100);
                        watermark.setRotation(-45);
                        break;
                    case 1: // 中间
                        watermark.setWidth(300);
                        watermark.setHeight(100);
                        watermark.setRotation(-45);
                        break;
                    case 2: // 右下
                        watermark.setLeft(400);
                        watermark.setTop(400);
                        watermark.setRotation(-45);
                        break;
                }

                watermark.setBehindText(true);

                // 创建段落并添加水印
                Paragraph watermarkPara = new Paragraph(doc);
                watermarkPara.appendChild(watermark);

                // 创建页眉并添加水印
                HeaderFooter header = new HeaderFooter(doc, HeaderFooterType.HEADER_PRIMARY);
                header.appendChild(watermarkPara.deepClone(true));
                sect.getHeadersFooters().add(header);

                // 为第一页创建页眉并添加水印
                HeaderFooter headerFirst = new HeaderFooter(doc, HeaderFooterType.HEADER_FIRST);
                headerFirst.appendChild(watermarkPara.deepClone(true));
                sect.getHeadersFooters().add(headerFirst);
            }
        }

    }


    /**
     * word 转 pdf 生成byte字节流
     *
     * @param wordPath word所在的目录地址
     * @return
     */
    public static byte[] wordConvertPdfByte(String wordPath) {
        ByteArrayOutputStream fileOutputStream = null;
        try {
            setLicense();
            fileOutputStream = new ByteArrayOutputStream();
            Document doc = new Document(wordPath);
            doc.save(fileOutputStream, SaveFormat.PDF);
            return fileOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    /**
     * 获取 生成的 pdf 文件路径，默认与源文件同一目录
     *
     * @param wordPath word文件
     * @return 生成的 pdf 文件
     */
    private static String getPdfFilePath(String wordPath) {
        int lastIndexOfPoint = wordPath.lastIndexOf(".");
        String pdfFilePath = "";
        if (lastIndexOfPoint > -1) {
            pdfFilePath = wordPath.substring(0, lastIndexOfPoint);
        }
        return pdfFilePath + ".pdf";
    }

}


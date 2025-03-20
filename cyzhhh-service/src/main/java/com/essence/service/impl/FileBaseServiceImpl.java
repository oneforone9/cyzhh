package com.essence.service.impl;

import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.common.utils.FileUtils;
import com.essence.dao.FileBaseDao;
import com.essence.dao.entity.FileBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.FileUtil;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.FileBaseEsu;
import com.essence.interfaces.param.FileBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterFileBaseEtoR;
import com.essence.service.converter.ConverterFileBaseEtoT;
import com.essence.service.converter.ConverterFileBaseTtoR;
import com.essence.service.utils.Word2PdfUtil;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.Date;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Slf4j
@Service
public class FileBaseServiceImpl extends BaseApiImpl<FileBaseEsu, FileBaseEsp, FileBaseEsr, FileBase> implements FileBaseService {

    @Value("${file.browse.url}")
    private String fileBrowsePath;
    @Value("${file.local.path}")
    private String fileLocalPath;

    @Autowired
    private FileBaseDao fileBaseDao;
    @Autowired
    private ConverterFileBaseEtoT converterFileBaseEtoT;
    @Autowired
    private ConverterFileBaseTtoR converterFileBaseTtoR;
    @Autowired
    private ConverterFileBaseEtoR converterFileBaseEtoR;


    public FileBaseServiceImpl(FileBaseDao fileBaseDao, ConverterFileBaseEtoT converterFileBaseEtoT, ConverterFileBaseTtoR converterFileBaseTtoR) {
        super(fileBaseDao, converterFileBaseEtoT, converterFileBaseTtoR);
    }

    @Override
    public FileBaseEsr upload(MultipartFile file) {
        return uploadForWatermark(file, null);
    }


    @Override
    public FileBaseEsr uploadForWatermark(MultipartFile file, String waterMark) {
        String id = UuidUtil.get32UUIDStr();
        // 文件名称
        String originalFilename = file.getOriginalFilename();

        // 1 上传到本地
        String url;
        FileBaseEsu fileBaseEsu = new FileBaseEsu();
        String localPath = "";
        String localUrlPath = "";
        try {
            localPath = fileLocalPath + File.separator + ItemConstant.FILE_LIST_NAME + File.separator + id + File.separator + originalFilename;
            localUrlPath = fileLocalPath + File.separator + ItemConstant.FILE_LIST_NAME + File.separator + id + File.separator + originalFilename.substring(0, originalFilename.lastIndexOf(".") + 1) + "pdf";
            File localFile = new File(localPath);
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            file.transferTo(localFile);
            url = localPath.replace(fileLocalPath, fileBrowsePath).replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("文件上传失败！");
        }

        fileBaseEsu.setId(id);
        // file_name
        String filename = FileUtil.getFileNameWithoutSuffix(originalFilename);
        fileBaseEsu.setFileName(filename);
        // file_format
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        fileBaseEsu.setFileFormat(fileSuffix);
        // file_size
        fileBaseEsu.setFileSize(FileUtils.fileSize(file.getSize()));
        // file_url
        fileBaseEsu.setFileUrl(url);
        fileBaseEsu.setIsDelete(ItemConstant.FILE_NO_DELETE);
        fileBaseEsu.setGmtCreate(new Date());
        fileBaseEsu.setGmtModified(new Date());
        int insert = this.insert(fileBaseEsu);
        if (insert < 1) {
            throw new BusinessException("文件上传失败");
        }

        if (StringUtils.isNotBlank(originalFilename) && originalFilename.contains(".")) {
            String lx = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
            if (lx.equals("docx") || lx.equals("doc")) {
                //pdf
//                Word2PdfUtil.wordConvertPdfFile(localPath, localUrlPath, waterMark);
                Word2PdfUtil.wordConvertPdfFile(localPath, localUrlPath);
                String pdfurl = localUrlPath.replace(fileLocalPath, fileBrowsePath).replace("\\", "/");
                fileBaseEsu.setPdfUrl(pdfurl);
                fileBaseDao.updateData(id, pdfurl);
            }
        }
        return converterFileBaseEtoR.toBean(fileBaseEsu);
    }

    @Override
    public void makeWaterMark(String fildId, String waterMark,String htbh) {
        FileBase fileBase = fileBaseDao.selectById(fildId);
        if (fileBase == null) {
            log.warn("没有文件信息，{}", fildId);
        }
        String originalFilename = fileBase.getFileName() + fileBase.getFileFormat();
        String localPath = fileLocalPath + File.separator + ItemConstant.FILE_LIST_NAME + File.separator + fildId + File.separator + originalFilename;
        String localUrlPath = fileLocalPath + File.separator + ItemConstant.FILE_LIST_NAME + File.separator + fildId + File.separator + originalFilename.substring(0, originalFilename.lastIndexOf(".") + 1) + "pdf";
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (fileSuffix.equals("docx") || fileSuffix.equals("doc")) {

//            Word2PdfUtil.wordConvertPdfFile(localPath, localUrlPath, waterMark);
            localPath = localUrlPath;
            originalFilename = fileBase.getFileName() +".pdf";
        }
//        else if (fileSuffix.equals("pdf")) {
            String resourcePath = localPath;
            localUrlPath = fileLocalPath + File.separator + ItemConstant.FILE_LIST_NAME + File.separator + fildId + File.separator + "waterMark" + originalFilename.substring(0, originalFilename.lastIndexOf(".") + 1) + "pdf";
            addWatermarkToPdf(localPath, localUrlPath, waterMark,htbh);

            File localUrlFile = new File(resourcePath);
            File renamedLocalUrlFile = new File(localUrlFile.getParent(), "originalFilename.pdf");
            localUrlFile.renameTo(renamedLocalUrlFile);

            File generateUrlFile = new File(localUrlPath);
            File renamedGenerateUrlFile = new File(generateUrlFile.getParent(), originalFilename);
            generateUrlFile.renameTo(renamedGenerateUrlFile);
//        }
        String pdfurl = localUrlPath.replace(fileLocalPath, fileBrowsePath).replace("\\", "/");
        fileBaseDao.updateData(fildId, pdfurl);
    }

    private void addWatermarkToPdf(String src, String dest, String watermark, String contractCode) {
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
            Document document = new Document(pdfDoc);

            // 获取PDF页数
            int numberOfPages = pdfDoc.getNumberOfPages();

            // 使用黑体字体
            PdfFont font = null;
            try {
                // 使用黑体
                font = PdfFontFactory.createFont("C:/Windows/Fonts/simhei.ttf", PdfEncodings.IDENTITY_H, true);
            } catch (Exception e) {
                // 备选字体
                font = PdfFontFactory.createFont("C:/Windows/Fonts/msyh.ttc,0", PdfEncodings.IDENTITY_H, true);
            }

            // 设置水印样式
            Paragraph paragraph = new Paragraph(watermark)
                    .setFont(font)
                    .setFontSize(28)  // 设置字体大小为28号
                    .setFontColor(ColorConstants.BLACK, 0.1f);  // 设置为黑色，透明度0.1

            // 设置合同编码水印样式
            Paragraph contractParagraph = null;
            if (contractCode != null && !contractCode.isEmpty()) {
                contractParagraph = new Paragraph(contractCode)
                        .setFont(font)
                        .setFontSize(12)  // 设置字体大小为12号
                        .setFontColor(ColorConstants.BLACK, 0.8f);  // 设置为黑色，透明度0.8
            }

            // 在每一页添加水印
            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = pdfDoc.getPage(i);
                Rectangle pageSize = page.getPageSize();

                // 第一个水印位置（左上部分）
                float x1 = pageSize.getWidth() / 4;
                float y1 = pageSize.getHeight() * 3 / 4;
                document.showTextAligned(paragraph, x1, y1, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);

                // 第二个水印位置（中间）
                float x2 = pageSize.getWidth() / 2;
                float y2 = pageSize.getHeight() / 2;
                document.showTextAligned(paragraph, x2, y2, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);

                // 第三个水印位置（右下部分）
                float x3 = pageSize.getWidth() * 3 / 4;
                float y3 = pageSize.getHeight() / 4;
                document.showTextAligned(paragraph, x3, y3, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);

                // 仅在第一页的右上角添加合同编码水印
                if (i == 1 && contractParagraph != null) {
                    float xContract = pageSize.getWidth() - 20;  // 距右边缘20个单位
                    float yContract = pageSize.getHeight() - 20; // 距上边缘20个单位
                    document.showTextAligned(contractParagraph, xContract, yContract, i,
                            TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("PDF水印添加失败！");
        }
    }


    @Override
    public int deleteById(Serializable id) {
        FileBase fileBase = new FileBase();
        fileBase.setId((String) id);
        fileBase.setIsDelete(ItemConstant.FILE_IS_DELETE);
        return fileBaseDao.updateById(fileBase);
    }

    @Transactional
    @Override
    public void previewFile(HttpServletRequest request, HttpServletResponse response, String id, String size) throws Exception {
        // 查询文件或缩略图是否存在
        FileBase fileBase = fileBaseDao.selectById(id + (null == size ? "" : size));
        // 存在返回
        if (null != fileBase) {
            FileUtil.openFilebBreakpoint(request, response, new File(fileBase.getFileUrl().replace(fileBrowsePath, fileLocalPath)), fileBase.getFileName() + fileBase.getFileFormat());
            return;
        }
        if (null == size) {
            throw new BusinessException("图片不存在");
        }
        // 不存在
        // 1 获取文件
        FileBase originalFile = fileBaseDao.selectById(id);
        String fileUrl = originalFile.getFileUrl();
        // 2 获取本地文件路径
        String localPath = fileUrl.replace(fileBrowsePath, fileLocalPath);
        // 2.2 本地文件
        File localFile = new File(localPath);
        // 2.3 缩略图文件名称
        String abridgeFilePath = localPath.replace(originalFile.getFileName(), originalFile.getFileName() + size);
        // 3 压缩文件
        File file = FileUtil.resizeImage(localFile, abridgeFilePath, size, true);
        // 4 上传数据库
        FileBaseEsu fileBaseEsu = new FileBaseEsu();
        fileBaseEsu.setId(id + size);
        // file_name
        String originalFilename = file.getName();
        String filename = FileUtil.getFileNameWithoutSuffix(originalFilename);
        fileBaseEsu.setFileName(filename);
        // file_format
        String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));

        fileBaseEsu.setFileFormat(fileSuffix);
        // file_size
        fileBaseEsu.setFileSize(FileUtils.fileSize(file.length()));
        // file_url
        // 更换url中的ip
        String path = file.getAbsolutePath().replace(fileLocalPath, fileBrowsePath).replace("\\", "/");
        fileBaseEsu.setFileUrl(path);
        fileBaseEsu.setIsDelete(ItemConstant.FILE_NO_DELETE);
        fileBaseEsu.setGmtCreate(new Date());
        fileBaseEsu.setGmtModified(new Date());
        int insert = this.insert(fileBaseEsu);
        if (insert > 0) {
            FileUtil.openFilebBreakpoint(request, response, file, originalFilename);
        }
    }
}

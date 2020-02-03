package uk.gov.hmcts.reform.juddata.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.configuration.SftpConfig;
import uk.gov.hmcts.reform.juddata.service.FileReadService;

@Slf4j
@Service
public class ReadSftpFileServiceImpl implements FileReadService {

    @Autowired
    SftpConfig sftpConfig;

    @Override
    public List<File> read() throws IOException, URISyntaxException {

        FileSystemOptions opts = new FileSystemOptions();
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        SftpFileSystemConfigBuilder.getInstance().setConnectTimeoutMillis(opts, sftpConfig.getTimeout());
        SftpFileSystemConfigBuilder.getInstance().setSessionTimeoutMillis(opts, sftpConfig.getTimeout());
        String userInfo = sftpConfig.getUserName() + ":" + sftpConfig.getUserPassword();
        FileSystemManager manager = VFS.getManager();
        return  readFileFromSftp(manager, opts, userInfo);
    }

    public List<File> readFileFromSftp(FileSystemManager manager, FileSystemOptions opts, String userInfo) throws URISyntaxException, IOException {
        List<File> sftpFiles = new ArrayList<>();
        for (String fileName : sftpConfig.getFileNames()) {
            String path = "/" + fileName;
            URI sftpUri = new URI("sftp", userInfo, sftpConfig.getHost(), 22, path, null, null);
            FileObject remoteFile = manager.resolveFile(sftpUri.toString(), opts);
            remoteFile.createFile();
            FileContent fileContent = remoteFile.getContent();
            sftpFiles.add(createLocalFile(fileContent, fileName));
        }
        return sftpFiles;
    }

    public File createLocalFile(FileContent fileContent, String fileName) throws IOException {
        InputStream inputStream = fileContent.getInputStream();
        File sftpFile = new File(fileName);
        FileUtils.copyInputStreamToFile(inputStream, sftpFile);
        return sftpFile;
    }

}

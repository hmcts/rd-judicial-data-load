package uk.gov.hmcts.reform.juddata.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.callbacks.KeyringConfigCallbacks;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfig;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.configuration.GpgConfig;
import uk.gov.hmcts.reform.juddata.service.FileDecryptionService;
import uk.gov.hmcts.reform.juddata.service.FileDeletionService;

@Service
@Slf4j
public class FileDecryptionServiceImpl implements FileDecryptionService {

    @Autowired
    private GpgConfig gpgConfig;

    @Autowired
    private FileDeletionService fileDeletionService;

    /**
     * Decrypts files.
     * @param files files needs to be decrypted
     * @return List of decrypted files
     * @throws IOException IOException
     * @throws NoSuchProviderException  NoSuchProviderException
     */
    @Override
    public List<File> decrypt(List<File> files) throws IOException, NoSuchProviderException {

        List<File> decryptedFiles = new ArrayList<File>();
        File publicKeyFile = createTempFile(new ByteArrayInputStream(gpgConfig.getPublicKey().getBytes(StandardCharsets.UTF_8)), "publicKey", ".gpg");
        File privateKeyFile = createTempFile(new ByteArrayInputStream(gpgConfig.getPrivateKey().getBytes(StandardCharsets.UTF_8)), "privateKey", ".gpg");
        KeyringConfig keyringConfig = keyringConfigInMemoryForKeys(publicKeyFile, privateKeyFile, gpgConfig.getPassword());
        Security.addProvider(new BouncyCastleProvider());

        for (File file : files) {
            final FileInputStream cipherTextStream = new FileInputStream(file);
            InputStream plaintextStream = BouncyGPG
                    .decryptAndVerifyStream()
                    .withConfig(keyringConfig)
                    .andIgnoreSignatures()
                    .fromEncryptedInputStream(cipherTextStream);

            decryptedFiles.add(createTempFile(plaintextStream, file.getName().replace(".csv.gpg", ""), ".csv"));
        }
        fileDeletionService.delete(Arrays.asList(new File[]{publicKeyFile, publicKeyFile}));
        log.info("decryption successful!!");
        return decryptedFiles;
    }

    /**
     * Creates KeyringConfig.
     * @param publicKeyFile public key file
     * @param privateKeyFile private key file
     * @param passphrase passphrase to decrypt
     * @return KeyringConfig
     * @throws IOException IOException
     */
    public KeyringConfig keyringConfigInMemoryForKeys(final File publicKeyFile, final File privateKeyFile, final String passphrase) throws IOException {
        final KeyringConfig keyringConfig = KeyringConfigs
                .withKeyRingsFromFiles(
                        publicKeyFile,
                        privateKeyFile,
                        KeyringConfigCallbacks.withPassword(passphrase));

        return keyringConfig;
    }

    /**
     * Creates temp file.
     * @param inputStream file input stream
     * @param fileName file name
     * @param fileExt file ext
     * @return temp file
     * @throws IOException IOException
     */
    public File createTempFile(InputStream inputStream, String fileName, String fileExt) throws IOException {
        File tempFile = new File(fileName + fileExt);
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile;
    }
}

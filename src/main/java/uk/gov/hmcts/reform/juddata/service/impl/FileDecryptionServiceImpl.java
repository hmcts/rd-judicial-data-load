package uk.gov.hmcts.reform.juddata.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchProviderException;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.callbacks.KeyringConfigCallbacks;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfig;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.configuration.GpgConfig;
import uk.gov.hmcts.reform.juddata.service.FileDecryptionService;

@Service
@Slf4j
public class FileDecryptionServiceImpl implements FileDecryptionService {

    @Autowired
    public GpgConfig gpgConfig;

    @Override
    public File decrypt() throws IOException, NoSuchProviderException {

        KeyringConfig keyringConfig = keyringConfigInMemoryForKeys(gpgConfig.getPublicKey(), gpgConfig.getPrivateKey(), gpgConfig.getPassword());


        final File sourceFile = new File(this.getClass().getClassLoader().getResource("testfile.txt.gpg").getFile());
        final FileInputStream cipherTextStream = new FileInputStream(sourceFile);


        Security.addProvider(new BouncyCastleProvider());
        String text = null;
        InputStream plaintextStream;
        plaintextStream = BouncyGPG
                .decryptAndVerifyStream()
                .withConfig(keyringConfig)
                .andIgnoreSignatures()
                .fromEncryptedInputStream(cipherTextStream);

        text = IOUtils.toString(plaintextStream, StandardCharsets.UTF_8.name());
        log.info(text);
        return new File("");
    }

    public KeyringConfig keyringConfigInMemoryForKeys(final String exportedPubKey, final String exportedPrivateKey, final String passphrase) throws IOException {

        final File publicKeyFile = new File(this.getClass().getClassLoader().getResource("my_public_key.gpg").getFile());
        final File privateKeyFile = new File(this.getClass().getClassLoader().getResource("my_private_key.gpg").getFile());

        final KeyringConfig keyringConfig = KeyringConfigs
                .withKeyRingsFromFiles(
                        publicKeyFile,
                        privateKeyFile,
                        KeyringConfigCallbacks.withPassword(passphrase));

        return keyringConfig;
    }
}

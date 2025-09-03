package com.wagnerdf.fancollectorsmedia.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.ApiConfig;
import com.wagnerdf.fancollectorsmedia.repository.ApiConfigRepository;
import com.wagnerdf.fancollectorsmedia.util.EncryptionUtil;

import jakarta.annotation.PostConstruct;

@Service
public class ApiConfigService {
	
	@Value("${api.config.secret}")
    private String SECRET_KEY;

    @Autowired
    private ApiConfigRepository repository;
    
    @Autowired
    private ApiConfigRepository apiConfigRepository;
    
    @Autowired
    private EncryptionUtil encryptionUtil;

    @PostConstruct
    private void validateSecret() {
        System.out.println("SECRET_KEY: " + SECRET_KEY + " (length=" + (SECRET_KEY != null ? SECRET_KEY.length() : "null") + ")");
        if (SECRET_KEY == null || !(SECRET_KEY.length() == 16 || SECRET_KEY.length() == 24 || SECRET_KEY.length() == 32)) {
            throw new RuntimeException("API_CONFIG_SECRET inválida. Deve ter 16, 24 ou 32 bytes.");
        }
    }

 // 🔹 Obter valor descriptografado
    public String getValor(String chave) {
        Optional<ApiConfig> opt = apiConfigRepository.findByChave(chave);
        if (opt.isEmpty()) return "";
        return encryptionUtil.decrypt(opt.get().getValor());
    }

    private String encrypt(String strToEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }

    private String decrypt(String strToDecrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), StandardCharsets.UTF_8);
    }

 // 🔹 Salvar valor criptografado
    public void atualizarValor(String chave, String valor) {
        ApiConfig config = apiConfigRepository.findByChave(chave)
                .orElse(new ApiConfig(chave, null));

        String valorCriptografado = encryptionUtil.encrypt(valor);
        config.setValor(valorCriptografado);
        apiConfigRepository.save(config);
    }

    public String obterValor(String chave) {
        return apiConfigRepository.findByChave(chave)
                .map(ApiConfig::getValor)
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada para a chave: " + chave));
    }
    
 // 🔹 Apenas buscar valor (opcional)
    public String buscarValor(String chave) {
        return getValor(chave);
    }

}
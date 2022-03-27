package org.example.utils;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXISource;
import net.jpountz.lz4.LZ4DecompressorWithLength;
import net.jpountz.lz4.LZ4Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ExiUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExiUtil.class);

    public static String decompressEXI(byte[] data) {
        try {
            EXIFactory exiFactory = DefaultEXIFactory.newInstance();
            ByteArrayInputStream inStreamEXI = new ByteArrayInputStream(data);
            InputSource isEXI = new InputSource(inStreamEXI);
            SAXSource exiSource = new EXISource(exiFactory);
            exiSource.setInputSource(isEXI);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            ByteArrayOutputStream decodeOs = new ByteArrayOutputStream();
            Result result1 = new StreamResult(decodeOs);
            transformer.transform(exiSource, result1);
            return decodeOs.toString(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.error("Decompress with EXI by data size: [{}] with error: [{}]", data.length, ex.getMessage(), ex);
        }
        return null;
    }

    public static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decompressLZ4(byte[] data) {
        LZ4Factory lz4Factory = LZ4Factory.fastestInstance();
        LZ4DecompressorWithLength lz4Decompressor = new LZ4DecompressorWithLength(
                lz4Factory.fastDecompressor());
        byte[] lz4Decompressed = lz4Decompressor.decompress(data);
        return lz4Decompressed;
    }
}

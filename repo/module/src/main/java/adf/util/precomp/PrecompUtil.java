package adf.util.precomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import javax.xml.crypto.Data;
import java.io.*;

public class PrecompUtil {

    public static final File PRECOMP_DATA_DIR = new File("precomp_data");

    public static boolean writeData(DataStorage dataStorage) throws IOException {
        try {
            if(!PRECOMP_DATA_DIR.exists()) {
                if(!PRECOMP_DATA_DIR.mkdir()) return false;
            }
            ObjectMapper om = new ObjectMapper(new MessagePackFactory());
            byte[] binary = om.writeValueAsBytes(dataStorage);
            FileOutputStream fos = new FileOutputStream(new File(PRECOMP_DATA_DIR, dataStorage.getFileName()));
            fos.write(binary);
            fos.close();
            return true;
        }
        catch(IOException e) {
            return false;
        }
    }

    public static DataStorage readData(String name) throws IOException {
        try {
            if(!PRECOMP_DATA_DIR.exists()) {
                if(!PRECOMP_DATA_DIR.mkdir()) return null;
            }
            File readFile = new File(PRECOMP_DATA_DIR, name);
            if(!readFile.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(readFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte [] binary = new byte[1024];
            while(true) {
                int len = bis.read(binary);
                if(len < 0) {
                    break;
                }
                bout.write(binary, 0, len);
            }
            binary = bout.toByteArray();
            ObjectMapper om = new ObjectMapper(new MessagePackFactory());
            DataStorage ds = om.readValue(binary, DataStorage.class);
            bis.close();
            fis.close();
            return ds;
        }
        catch (IOException e) {

        }
        return null;
    }
}

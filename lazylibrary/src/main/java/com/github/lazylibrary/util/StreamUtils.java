
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtils {

	public static String streamToString(InputStream inputStream) {
		try{
			//创建一个内存字节写入流
			ByteArrayOutputStream  out = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len = 0;
			while(( len = inputStream.read(buffer)) != -1){
				out.write(buffer, 0, len);
				out.flush();
			}
			
			//将一个内存写入流转换成字符串
			String  result = out.toString();
			out.close();
			inputStream.close();
			return result;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}

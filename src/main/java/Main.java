import com.pojo.Student;
import com.server.RestServer;

public class Main {

	public static void main(String[] args) {
		RestServer.start(Student.class);
	}

}

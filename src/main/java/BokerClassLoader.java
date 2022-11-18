import java.security.SecureClassLoader;

public class BokerClassLoader extends SecureClassLoader {

	public BokerClassLoader(ClassLoader parent) {
		super(parent);
	}
	@Override
	public native Class<?> findClass(String name);
}

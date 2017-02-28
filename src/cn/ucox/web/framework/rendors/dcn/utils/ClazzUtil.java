/**
 * cn.ucox.web.logservice.rendors.orcldcn.plugin.ClazzUtil
 *
 * @author chenw
 * @create 16/2/20.00:14
 * @email javacspring@hotmail.com
 */

package cn.ucox.web.framework.rendors.dcn.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 类加载工具类
 *
 * @author chenw
 * @create 16/2/20 00:14
 * @email javacspring@gmail.com
 */
@Deprecated
class ClazzUtil {

    /**
     * 通过指定接口或父类,返回此接口或父类)全部实现类(或子类）
     *
     * @param clazz 接口或父类
     * @return 扫描包名
     */
    public static List<Class> getInterfaceImplClass(Class clazz, String packageName) {
        List<Class> returnClassList = new ArrayList<>(); //返回结果
        if (clazz.isInterface()) {
            try {
                List<Class> allClass = getClasses(packageName); //获得当前包下以及子包下的所有类
                //判断是否是同一个接口
                for (Class allClas : allClass) {
                    if (clazz.isAssignableFrom(allClas)) { //判断是不是一个接口
                        if (!clazz.equals(allClas)) { //本身不加进去
                            returnClassList.add(allClas);
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return returnClassList;
    }

    /***
     * 从一个包中查找出所有的类，在jar包中不能查找
     *
     * @param packageName 包名称
     * @return List<Class>
     * @throws ClassNotFoundException
     * @throws IOException
     */

    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /***
     * 找出目录directory下所有的class文件并以“完整包名”+“类名”的形式返回类名
     *
     * @param directory   指定目录路径
     * @param packageName 包名
     * @return List<Class>
     * @throws ClassNotFoundException
     */

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }
}

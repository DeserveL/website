package com.deservel.website.generator;

/**
 * 项目常量
 */
public final class ProjectConstant {
    public static final String BASE_PACKAGE = "com.deservel.website";//项目基础包名称，根据自己公司的项目修改

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model.po";//Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";//Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".common.mapper.MyMapper";//Mapper插件基础接口的完全限定名

    public static final String MY_COMMENT_GENERATOR = BASE_PACKAGE + ".generator.MyCommentGenerator";//generator注释类自定义

    //JDBC配置，请修改为你项目的实际配置
    public static final String JDBC_URL = "jdbc:mysql://101.132.134.52:3306/website";
    public static final String JDBC_USERNAME = "root";
    public static final String JDBC_PASSWORD = "root";
    public static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    public static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    public static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/generator/template";//模板位置

    public static final String JAVA_PATH = "/src/main/java"; //java文件路径
    public static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    public static final String PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);//生成的Service存放路径
    public static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    public static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);//生成的Controller存放路径

    public static final String AUTHOR = "CodeGenerator";//@author

    public static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }
}

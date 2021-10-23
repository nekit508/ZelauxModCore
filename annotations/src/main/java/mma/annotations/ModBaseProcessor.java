package mma.annotations;

import arc.files.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import com.squareup.javapoet.*;
import mindustry.annotations.*;

import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.tools.*;
import java.io.*;
import java.nio.file.*;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public abstract class ModBaseProcessor extends BaseProcessor{
    static final String parentName = "mindustry.gen";
    static final StringMap annotationProperties = new StringMap();
    public static String rootPackageName = null;

    {
        enableTimer = true;
    }

    public static void print(String obj, Object... args){
        String message = Strings.format(obj.toString(), args);
        System.out.println(message);
    }

    public static void write(TypeSpec.Builder builder, String packageName) throws Exception{
        write(builder, packageName, (Seq<String>)null);
    }

    protected static Fi getFilesFi(StandardLocation location) throws IOException{
        return getFilesFi(location, "no", "no").parent().parent();
    }

    protected static Fi getFilesFi(StandardLocation location, String packageName, String className) throws IOException{
        return Fi.get(filer.getResource(location, packageName, className)
        .toUri().toURL().toString().substring(OS.isWindows ? 6 : "file:".length()));
    }

    /**
     * revisionsPath
     * classPrefix
     * rootPackage
     */
    public StringMap annotationsSettings(){
        Fi annotationPropertiesFile = rootDirectory.child("annotation.properties");
        Fi[] list = rootDirectory.child("core/src").list();
        boolean debug = list[0].name().equals("mma") && list.length == 1;
        if(debug){
//            annotationProperties.put("debug", "true");
            return annotationProperties;
        }
        if(annotationPropertiesFile.exists()){
            PropertiesUtils.load(annotationProperties, annotationPropertiesFile.reader());
        }else{
            annotationPropertiesFile.writeString("");
        }
        Fi classPrefixTxt = rootDirectory.child("annotations/classPrefix.txt");
        if(classPrefixTxt.exists()){
            annotationProperties.put("classPrefix", classPrefixTxt.readString());
            try{
                PropertiesUtils.store(annotationProperties, annotationPropertiesFile.writer(false), null);
                classPrefixTxt.delete();
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }
        return annotationProperties;
    }

    public String classPrefix(){
        String classNamePrefix = "Mod";
        if(!rootPackageName.equals("mma")){
            classNamePrefix = annotationsSettings(AnnotationSetting.classPrefix, Strings.capitalize(rootPackageName));
        }
        return classNamePrefix;
    }

    public String annotationsSettings(AnnotationSetting settings, String defvalue){
        return annotationsSettings().get(settings.name(), defvalue);
    }

    @Override
    protected String getPackageName(){
        packageName = (rootPackageName = annotationsSettings(AnnotationSetting.rootPackage, rootDirectory.child("core/src").list()[0].name())) + ".gen";
        return packageName;
    }

    public void delete(String packageName, String name) throws IOException{
//        print("delete name: @",name);
        FileObject resource;
        resource = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, name);
//        boolean delete = resource.delete();
//        print("delete: @ ,named: @, filer: @",delete,resource.getName(),resource.getClass().getName());
        Files.delete(Paths.get(resource.getName() + ".java"));
    }

    public void delete(String name) throws IOException{
        delete(packageName, name);
    }

    public void debugLog(String text, Object... args){
        System.out.println("[D]" + Strings.format(text, args));
    }
}

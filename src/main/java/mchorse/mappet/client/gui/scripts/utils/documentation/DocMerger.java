package mchorse.mappet.client.gui.scripts.utils.documentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mchorse.mappet.ClientProxy;
import mchorse.mappet.client.gui.scripts.GuiDocumentationOverlayPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DocMerger
{
    public static Docs getMergedDocs()
    {
        Minecraft mc = Minecraft.getMinecraft();
        Language language = mc.getLanguageManager().getCurrentLanguage();
        Gson gson = new GsonBuilder().create();
        List<Docs> docsList = new ArrayList<>();

        File docsFolder = new File(ClientProxy.configFolder.getPath(), "documentation");

        docsFolder.mkdirs();

        addAddonsDocs(gson, docsList);

        List<File> files = FileUtils.listFiles(docsFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                .filter(File::isFile).collect(Collectors.toList());

        List<File> translationFiles = files.stream().filter(file -> file.getParent().equals("translation")).collect(Collectors.toList());
        for (File file : files)
        {
            boolean isTranslation = file.getParent().equals("translation");
            if (!isTranslation)
            {
                addDocToList(gson, docsList, file);
            }
        }


        for (File file : translationFiles)
        {
            if (language.getLanguageCode().equals(file.getName()))
            {
                addDocToList(gson, docsList, file);
            }
        }

        InputStream stream = GuiDocumentationOverlayPanel.class.getResourceAsStream("/assets/mappet/docs.json");
        Scanner scanner = new Scanner(stream, "UTF-8");
        Docs mainDocs = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);

        for (Docs doc : docsList)
        {
            DocMerger.mergeDocs(mainDocs, doc);
        }

        return mainDocs;
    }

    private static void addAddonsDocs(Gson gson, List<Docs> docsList) {
        /* Place for mixins */
    }

    private static void mergeDocs(Docs docsMain, Docs docsAdd)
    {
        for (DocClass classAdd : docsAdd.classes)
        {
            classAdd.source = docsAdd.source;

            DocClass classMain = docsMain.getClass(classAdd.name);

            if (classMain == null)
            {
                docsMain.classes.add(classAdd);
            }
            else
            {
                classMain.doc = classAdd.doc.trim().isEmpty() ? classMain.doc : classAdd.doc.trim();
                DocMerger.mergeClasses(classMain, classAdd);
            }
        }

        for (DocPackage packageAdd : docsAdd.packages)
        {
            packageAdd.source = docsAdd.source;

            DocPackage packageMain = docsMain.getPackage(packageAdd.name);

            if (packageMain == null)
            {
                docsMain.packages.add(packageAdd);
            }
            else
            {
                packageMain.doc = packageAdd.doc;
                Collections.replaceAll(docsMain.packages,packageMain,packageAdd);
            }
        }
    }

    private static void mergeClasses(DocClass classMain, DocClass classAdd)
    {
        for (DocMethod methodAdd : classAdd.methods)
        {
            methodAdd.source = classAdd.source;

            DocMethod methodMain = classMain.getExactMethod(methodAdd.name);

            if (methodMain == null)
            {
                classMain.methods.add(methodAdd);
            }
            else
            {
                methodMain.doc = methodAdd.doc;
                Collections.replaceAll(classMain.methods,methodMain,methodAdd);
            }
        }
    }

    private static void addDocToList(Gson gson, List<Docs> list, File file)
    {
        try
        {
            InputStream stream = new FileInputStream(file);
            Scanner scanner = new Scanner(stream, "UTF-8");
            Docs docs = gson.fromJson(scanner.useDelimiter("\\A").next(), Docs.class);
            docs.source = new File(ClientProxy.configFolder.getPath()).toPath().relativize(file.toPath()).toFile().getPath();
            docs.classes.forEach(clazz -> {
                clazz.source = docs.source;
                clazz.methods.forEach(method -> method.source = docs.source);
            });
            list.add(docs);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}

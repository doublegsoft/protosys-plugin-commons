/*
 * Copyright 2017 doublegsoft.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.doublegsoft.protosys.commons;

import com.doublegsoft.jcommons.lang.HashObject;
import com.doublegsoft.jcommons.lang.Option;
import com.doublegsoft.jcommons.lang.StringHolder;
import com.doublegsoft.jcommons.lang.StringPair;
import com.doublegsoft.jcommons.metabean.AttributeDefinition;
import com.doublegsoft.jcommons.metabean.ModelDefinition;
import com.doublegsoft.jcommons.metabean.ObjectDefinition;
import com.doublegsoft.jcommons.metabean.ObjectRole;
import com.doublegsoft.jcommons.metamodel.ApiDefinition;
import com.doublegsoft.jcommons.metamodel.ApplicationApiDefinition;
import com.doublegsoft.jcommons.metamodel.ApplicationDefinition;
import com.doublegsoft.jcommons.metamodel.UsecaseDefinition;
import com.doublegsoft.jcommons.metaui.PageDefinition;
import com.doublegsoft.jcommons.metaui.WidgetDefinition;
import com.doublegsoft.jcommons.metaui.layout.Position;
import com.doublegsoft.jcommons.metaui.layout.Size;
import com.doublegsoft.jcommons.programming.NamingConvention;
import com.doublegsoft.jcommons.programming.NamingConventions;
import com.doublegsoft.jcommons.programming.c.CConventions;
import com.doublegsoft.jcommons.programming.cpp.CppConventions;
import com.doublegsoft.jcommons.programming.cpp.CppNamingConvention;
import com.doublegsoft.jcommons.programming.csharp.CSharpConventions;
import com.doublegsoft.jcommons.programming.csharp.CSharpNamingConvention;
import com.doublegsoft.jcommons.programming.dart.DartConventions;
import com.doublegsoft.jcommons.programming.groovy.GroovyConventions;
import com.doublegsoft.jcommons.programming.groovy.GroovyNamingConvention;
import com.doublegsoft.jcommons.programming.java.JavaConventions;
import com.doublegsoft.jcommons.programming.java.JavaNamingConvention;
import com.doublegsoft.jcommons.programming.javascript.JavaScriptConventions;
import com.doublegsoft.jcommons.programming.javascript.JavaScriptNamingConvention;
import com.doublegsoft.jcommons.programming.objc.ObjcConventions;
import com.doublegsoft.jcommons.programming.objc.ObjcNamingConvention;
import com.doublegsoft.jcommons.programming.pascal.PascalConventions;
import com.doublegsoft.jcommons.programming.pascal.PascalNamingConvention;
import com.doublegsoft.jcommons.programming.ruby.RubyConventions;
import com.doublegsoft.jcommons.programming.ruby.RubyNamingConvention;
import com.doublegsoft.jcommons.programming.rust.RustConventions;
import com.doublegsoft.jcommons.programming.sql.SQLConventions;
import com.doublegsoft.jcommons.programming.sql.SQLNamingConvention;
import com.doublegsoft.jcommons.programming.swift.SwiftConventions;
import com.doublegsoft.jcommons.programming.swift.SwiftNamingConvention;
import com.doublegsoft.jcommons.programming.typescript.TypeScriptConventions;
import com.doublegsoft.jcommons.programming.typescript.TypeScriptNamingConvention;
import com.doublegsoft.jcommons.programming.xml.XMLConventions;
import com.doublegsoft.jcommons.programming.xml.XMLNamingConvention;
import com.doublegsoft.jcommons.utils.Strings;
import com.doublegsoft.misuml.MisumlContext;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.doublegsoft.modelbase.Modelbase;
import io.doublegsoft.typebase.Typebase;
import org.doublegsoft.protosys.plugin.Plugin;
import org.doublegsoft.protosys.reader.MetaReader;
import org.doublegsoft.protosys.reader.XlsxReader;
import org.doublegsoft.protosys.util.Io;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * It is base code generator plugin and includes the base mechanism functions.
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class FileSystemTemplateBasedPlugin implements Plugin {

  public final static CConventions C = new CConventions();

  public final static CppConventions CPP = new CppConventions();

  public final static SQLConventions SQL = new SQLConventions();

  public final static XMLConventions XML = new XMLConventions();

  public final static RubyConventions RUBY = new RubyConventions();

  public final static JavaConventions JAVA = new JavaConventions();

  public final static CSharpConventions CSHARP = new CSharpConventions();

  public final static PascalConventions PASCAL = new PascalConventions();

  public final static ObjcConventions OBJC = new ObjcConventions();

  public final static DartConventions DART = new DartConventions();

  public final static JavaScriptConventions JAVASCRIPT = new JavaScriptConventions();

  public final static TypeScriptConventions TYPESCRIPT = new TypeScriptConventions();

  public final static SwiftConventions SWIFT = new SwiftConventions();

  public final static GroovyConventions GROOVY = new GroovyConventions();

  public final static RustConventions RUST = new RustConventions();

  public final static Configuration FREEMARKER = new Configuration(Configuration.getVersion());

  public final static Typebase TYPEBASE = new Typebase();

  public final static LayoutHelper LAYOUT = new LayoutHelper();

  public final static Modelbase MODELBASE = new Modelbase();

  protected Map<String, Object> globalVariables = new HashMap<>();

  /**
   * describes the current file type of template to render.
   */
  protected transient String renderingFileType = null;

  protected transient ApplicationDefinition application = null;

  @Override
  public void prototype(MisumlContext[] misumls, ModelDefinition model, String outputRoot, String templateRoot, HashObject globals) throws IOException {
    FileTemplateLoader specific = new FileTemplateLoader(new File(templateRoot));
    FileTemplateLoader common = new FileTemplateLoader(new File(templateRoot + "/.."));
    MultiTemplateLoader templateLoader = new MultiTemplateLoader(new TemplateLoader[]{common, specific});
    FREEMARKER.setTemplateLoader(templateLoader);

    decorate(model, globals);

    ApplicationDefinition app = convertToApplication(misumls[0], model);
    this.application = app;
    for (int i = 1; i < misumls.length; i++) {
      ApplicationDefinition otherApp = convertToApplication(misumls[i], model);
      for (ApplicationApiDefinition apiApp : otherApp.getAPI()) {
        app.addAPI(apiApp);
      }
      for (UsecaseDefinition usecase : otherApp.getUsecases()) {
        app.addUsecase(usecase);
      }
    }

    decorate(app, globals);

    if (globals != null) {
      globalVariables.putAll(globals);
    }
    visitAndRender(outputRoot, "", templateRoot, "", app, new HashObject());
  }

  /**
   * Decorates the model or misuml context by specific sub-plugins.
   *
   * @param model   the model definition
   * @param globals the global variables applied in template
   * @throws IOException in case of IO errors
   */
  public void decorate(ModelDefinition model, HashObject globals) throws IOException {

  }

  /**
   * Decorates the model or misuml context in application by specific sub-plugins.
   *
   * @param application the application definition
   * @param globals     the global variables applied in template
   * @throws IOException in case of IO errors
   */
  public void decorate(ApplicationDefinition application, HashObject globals) throws IOException {

  }

  /**
   * Converts to model definition from predefined excels.
   *
   * @param excels the excel files
   * @return the model
   * @throws IOException in case of any errors
   */
  public ModelDefinition createModelFromMetadata(String... excels) throws IOException {
    Map<String, String> mappings = new HashMap<>();
    mappings.put(MetaReader.OBJECT_PERSISTENCE_NAME, "表名");
    mappings.put(MetaReader.OBJECT_NAME, "对象名称");
    mappings.put(MetaReader.OBJECT_TEXT, "表说明");
    mappings.put(MetaReader.OBJECT_ROLE, "表类型");
    mappings.put(MetaReader.ATTRIBUTE_PERSISTENCE_NAME, "字段名");
    mappings.put(MetaReader.ATTRIBUTE_NAME, "对象属性名称");
    mappings.put(MetaReader.ATTRIBUTE_TEXT, "字段说明");
    mappings.put(MetaReader.ATTRIBUTE_DATA_TYPE, "数据类型");
    mappings.put(MetaReader.ATTRIBUTE_UNIT, "单位");
    mappings.put(MetaReader.ATTRIBUTE_NULLABLE, "可以为空");
    mappings.put(MetaReader.ATTRIBUTE_DEFAULT_VALUE, "默认值");
    mappings.put(MetaReader.ATTRIBUTE_SYSTEM, "列属性");
    mappings.put(MetaReader.ATTRIBUTE_IDENTIFIABLE, "主键序号");
    mappings.put(MetaReader.ATTRIBUTE_RELATIONSHIP, "关联表达式");
    mappings.put(MetaReader.ATTRIBUTE_EXTENSION, "列属性");
    mappings.put(MetaReader.ATTRIBUTE_DOMAIN_TYPE, "域对象表达式");
    mappings.put(MetaReader.ATTRIBUTE_TEST_DATA, "测试数据");
    XlsxReader reader = new XlsxReader(mappings);
    List<InputStream> streams = new ArrayList<>();
    for (String path : excels) {
      streams.add(new FileInputStream(path));
    }
    return reader.readFrom(streams.toArray(new InputStream[streams.size()]));
  }

  /**
   * Creates the {@link ModelDefinition} instance from model definition files using {@link Modelbase} framework.
   *
   * @param modelPaths the model definition files
   * @return the {@link ModelDefinition} instance
   * @throws IOException in case of IO errors
   * @since 1.1
   */
  public ModelDefinition createModelFromModelbase(String... modelPaths) throws IOException {
    Modelbase modelbase = new Modelbase();
    StringHolder dsl = new StringHolder();
    for (String path : modelPaths) {
      if (Strings.isEmpty(path.trim())) {
        continue;
      }
      File file = new File(path);
      if (file.exists() && !file.isDirectory()) {
        dsl.append(new String(Files.readAllBytes(file.toPath()), "UTF-8")).linefeed();
      }
    }
    if (dsl.toString().trim().isEmpty()) {
      return new ModelDefinition();
    }
    return modelbase.parse(dsl.toString());
  }

  /**
   * Creates the {@link MisumlContext} objects from misuml files using misuml framework.
   *
   * @param misumls the misuml files
   * @return the misuml contexts
   * @throws IOException in case of IO errors
   */
  public MisumlContext[] createMisumlContexts(String... misumls) throws IOException {
    List<MisumlContext> retVal = new ArrayList<>();
    for (String misuml : misumls) {
      retVal.add(MisumlContext.from(new FileInputStream(misuml)));
    }
    return retVal.toArray(new MisumlContext[retVal.size()]);
  }

  /**
   * Renders the widget source code <b>and is used by templates</b>.
   * <ul>
   * <li>vals: "[A: 有效, D: 无效]"</li>
   * <li>pos: "(1 , 1, 50, 400)" or "(1, 1)", deprecated</li>
   * <li>size: "(50, 400)" or "(1, 1)", deprecated</li>
   * </ul>
   *
   * @param obj    the widget object or map object
   * @param indent the indent size
   * @return the rendered source code for widget
   */
  public String render(Object obj, int indent) {
    if (obj == null) {
      return "";
    }
    String widgetType = null;
    Map<String, Object> templateData = createTemplateData(this.application);

    if (obj instanceof WidgetDefinition) {
      WidgetDefinition widget = (WidgetDefinition) obj;
      widgetType = widget.getType();
      templateData.putAll(toTemplateData(widget));
    } else if (Map.class.isAssignableFrom(obj.getClass())) {
      Map<String, Object> widgetMap = (Map<String, Object>) obj;
      widgetType = (String) widgetMap.get("type");
      templateData.putAll(widgetMap);
    } else if (Collection.class.isAssignableFrom(obj.getClass())) {
      Collection<Object> children = (Collection<Object>) obj;
      StringHolder retVal = new StringHolder();
      for (Object child : children) {
        retVal.append(render(child, indent)).linefeed();
      }
      return retVal.toString();
    } else {
      return obj.toString();
    }

    try {
      return renderBy("$/" + widgetType, get$TemplateFileName(widgetType), templateData, indent);
    } catch (IOException ex) {

    }
    try {
      return renderBy("$/elements", get$TemplateFileName("elements"), templateData, indent);
    } catch (IOException ex) {

    }
    try {
      return renderBy("$/element", get$TemplateFileName("element"), templateData, indent);
    } catch (IOException ex) {

    }
    try {
      return renderBy("$/unsupport", get$TemplateFileName("unsupport"), templateData, indent);
    } catch (IOException ex) {

    }
//        return Io.indent(indent, "<!-- 暂不支持" + id + ":" + widgetType + "-->");
    return "";
  }

  public String render(Object obj, int indent, String filetype) {
    renderingFileType = filetype;
    return render(obj, indent);
  }

  /**
   * Renders the widget source code <b>and is used by templates</b>.
   *
   * @param obj      the object in template
   * @param optName  the option name
   * @param optValue the option value
   */
  public void addOption(HashObject obj, String optName, String optValue) {
    obj.set(optName, optValue);
  }

  /**
   * Visits the template root and outputs to the project root from application definitions, and the directories are
   * aligned excluding dollar($) directory.
   * <p>
   * And changelogs as listed:
   * <ul>
   *   <li>add namespace as part of file name on Jan 23, 2019.</li>
   * </ul>
   *
   * @param outputRoot
   *        the output root
   *
   * @param outputPath
   *        the output path, relative path under the output root
   *
   * @param templateRoot
   *        the template root
   *
   * @param templatePath
   *        the template path, relative path under the template root
   *
   * @param app
   *        the application
   *
   * @param filters
   *        the unknown??
   *
   * @throws IOException
   *        in case of any errors
   *
   * @version 3.3 - added outputRoot, outputPath in template variables.
   */
  protected void visitAndRender(String outputRoot, String outputPath, String templateRoot, String templatePath, ApplicationDefinition app, HashObject filters) throws IOException {
    if (!templatePath.contains("$")) {
      filters.clear();
    }
    Map<String, Object> templateData = createTemplateData(app);
    templateData.put("outputRoot", outputRoot);
    templateData.put("outputPath", outputPath);
    File templateRootDir = new File(templateRoot + "/" + templatePath);
    for (File file : templateRootDir.listFiles()) {
      String templateFileName = file.getName();
      if (templateFileName.equals("$")) {
        // $ is the template-only resources root directory, not template for application
        continue;
      }

      /*!
      ** directory-first
      */
      if (file.isDirectory()) {
        if (templateFileName.contains("$obj$")) {
          for (ObjectDefinition obj : app.getModel().getObjects()) {
            String fileOutputPath = outputPath + "/" + templateFileName.replace("$obj$", obj.getName().toLowerCase());
            filters.put("obj", obj.getName());
            visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
          }
        } else if (templateFileName.contains("$namespace$")) {
          String namespace = (String) globalVariables.get("namespace");
          String fileOutputPath = outputPath;

          if (namespace != null) {
            String namespacePath = namespace.replace('.', '/');
            fileOutputPath += "/" + namespacePath;
          }
          if (fileOutputPath != null) {
            visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
          }
        } else if (templateFileName.contains("$module$")) {
          if (app.getModules().length > 0) {
            for (String module : app.getModules()) {
              String fileOutputPath;
              if (module.contains("/")) {
                fileOutputPath = outputPath + "/" + templateFileName.replace("$module$", module);
              } else {
                fileOutputPath = outputPath + "/" + templateFileName.replace("$module$", JAVA.nameNamespace(module));
              }
              filters.put("module", module);
              visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
            }
          } else {
            /*!
            ** 处理Modelbase的模块Module分类
            **
            ** 2024-01-04
            */
            for (ObjectDefinition obj : app.getModel().getObjects()) {
              String module = obj.getModuleName();
              String fileOutputPath;
              if (module.contains("/")) {
                fileOutputPath = outputPath + "/" + templateFileName.replace("$module$", module);
              } else {
                fileOutputPath = outputPath + "/" + templateFileName.replace("$module$", JAVA.nameNamespace(module));
              }
              filters.put("module", module);
              visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
            }
          }
        } else if (templateFileName.contains("$page$")) {
          for (PageDefinition page : app.getPages()) {
            String module = page.getModule();
            String fileOutputPath = outputPath + "/";
            if (!Strings.isEmpty(module) && !"unknown".equals(module)) {
              fileOutputPath += module + "/";
            }
            NamingConvention namingConvention = (NamingConvention) globalVariables.get("globalNamingConvention");
            fileOutputPath += templateFileName.replace("$page$", namingConvention.nameFile(page.getName()));
            filters.put("page", page);
            visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
          }
        } else if (templateFileName.contains("$usecase$")) {
          for (UsecaseDefinition usecase : app.getUsecases()) {
            String fileOutputPath = outputPath + "/" + templateFileName.replace("$usecase$", usecase.getName());
            filters.put("usecase", usecase);
            visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
          }
        } else if (templateFileName.contains("$parapp$")) {
          String fileOutputPath = outputPath + "/" + templateFileName.replace("$parapp$", (String)globalVariables.get("parentApplication"));
          filters.put("parapp", globalVariables.get("parentApplication"));
          visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
        } else if (templateFileName.contains("$app$")) {
          String language = (String)globalVariables.get("language");
          String namespace = "";
          // FIXME: 这里好像是个补丁
          if ("csharp".equals(language)) {
            namespace = CSHARP.nameNamespace(app.getName());
          } else if (templateFileName.contains(".xcodeproj")) {
            String appalias = (String) globalVariables.get("appalias");
            if (!Strings.isEmpty(appalias)) {
              namespace = OBJC.nameFile(appalias);
            } else {
              namespace = OBJC.nameFile(app.getName());
            }
          } else {
            namespace = JAVA.nameNamespace(app.getName());
          }
          String fileOutputPath = outputPath + "/" + templateFileName.replace("$app$", namespace);
          filters.put("app", app.getName());
          visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
        } else if (templateFileName.indexOf("$") != -1 && (templateFileName.indexOf("$") != templateFileName.lastIndexOf("$"))) {
          // for any, object or page
          int firstDollar = templateFileName.indexOf("$");
          int lastDollar = templateFileName.lastIndexOf("$");
          int firstBracket = templateFileName.indexOf("[");
          int lastBracket = templateFileName.indexOf("]");
          int firstParen = templateFileName.indexOf("(");
          int lastParen = templateFileName.indexOf(")");
          String label = templateFileName.substring(firstDollar + 1, lastDollar);
          String type = "";
          String attrname = "";
          String attrval  = "";
          /*!
          ** this if-statement make "$page[navigator](id=home)$" be allowed.
          */
          if (firstBracket != -1) {
            label = templateFileName.substring(firstDollar + 1, firstBracket);
            type = templateFileName.substring(firstBracket + 1, lastBracket);

          }
          if (firstParen != -1) {
            label = templateFileName.substring(firstDollar + 1, firstParen);
            String attr = templateFileName.substring(firstParen + 1, lastParen);
            String[] strs = attr.split("=");
            attrname = strs[0];
            attrval = strs[1];
          }
          if ("widget".equals(label)) {
            for (PageDefinition page : app.getPages()) {
              for (WidgetDefinition widget : page.getPageWidgets()) {
                if (!type.equals(widget.getType())) {
                  continue;
                }
                NamingConvention namingConvention = (NamingConvention) globalVariables.get("globalNamingConvention");
                filters.put("widget", widget);
                visitAndRender(outputRoot, outputPath + "/" + namingConvention.nameFile(widget.getId()), templateRoot, templatePath + "/" + templateFileName, app, filters);
              }
            }
          } else if ("page".equals(label)) {
            for (PageDefinition page : app.getPages()) {
              if (attrname.equals("")) {
                continue;
              }
              if (attrname.equals("id")) {
                if (attrval.equals(page.getId())) {
                  NamingConvention namingConvention = (NamingConvention) globalVariables.get("globalNamingConvention");
                  filters.put("page", page);
                  visitAndRender(outputRoot, outputPath + "/" + namingConvention.nameFile(page.getId()), templateRoot, templatePath + "/" + templateFileName, app, filters);
                }
              } else {
                if (attrval.equals(page.getOption(attrname))) {
                  NamingConvention namingConvention = (NamingConvention) globalVariables.get("globalNamingConvention");
                  filters.put("page", page);
                  visitAndRender(outputRoot, outputPath + "/" + namingConvention.nameFile(page.getId()), templateRoot, templatePath + "/" + templateFileName, app, filters);
                }
              }
            }
          }
          if (app.getModel() != null) {
            for (ObjectDefinition obj : app.getModel().getObjects()) {
              if (obj.isLabelled(label) && !obj.isLabelled("generated")) {
                String alias = obj.getLabelledOptions(label).get("alias");
                NamingConvention namingConvention = (NamingConvention) globalVariables.get("globalNamingConvention");
                String fileOutputPath = outputPath + "/" + namingConvention.nameFile(
                    templateFileName.replace("$" + label + "$", Strings.isEmpty(alias) ? obj.getName().toLowerCase() : alias));
                // System.out.println(obj.getName() + " ----- " + label + " ----------- " + filters.get("module") + " ---- " + obj.getModuleName());
                filters.put(label, Strings.isEmpty(alias) ? obj.getName().toLowerCase() : alias);
                globalVariables.put(label, obj);
                /*!
                ** 补丁（微信小程序补丁）：
                **   当存在模块的情况下，在此处过滤掉模块不相同对象。
                **
                ** 2025-01-26
                */
                if (filters.containsKey("module")) {
                  if (!obj.getModuleName().equals(filters.get("module"))) {
                    continue;
                  }
                }
                visitAndRender(outputRoot, fileOutputPath, templateRoot, templatePath + "/" + templateFileName, app, filters);
              }
            }
          }
        } else {
          visitAndRender(outputRoot, outputPath + "/" + templateFileName, templateRoot, templatePath + "/" + templateFileName, app, filters);
        }
        /*！
        ** DIRECCTORY COMPLETED
        */
        continue;
      }
      if (!templateFileName.endsWith(".ftl")) {
        // copy file directly
        copyTo(outputRoot, outputPath, templateRoot, templatePath, templateFileName);
        continue;
      }
      /*!
      ** 正式的文件处理逻辑，开始
      */
      String outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
      NamingConvention naming = getNamingConvention(templateFileName);
      if (templateFileName.contains("$obj$")) {
        for (ObjectDefinition obj : app.getModel().getObjects()) {
          if (obj.isLabelled("generated")) {
            continue;
          }
          if (obj.getRole() == ObjectRole.RELATIOINSHIP || !matchLevelled(obj, filters)) {
            continue;
          }
          outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
          outputName = outputName.replace("$obj$", naming.nameFile(obj.getName()));
          // 同时需要查看是否含有ns变量，此对应的是namespace，满足objective-c的命名规范
          if (templateFileName.contains("$ns$")) {
            String namespace = (String) globalVariables.get("namespace");
            namespace = namespace != null ? namespace : "";
            outputName = outputName.replace("$ns$", namespace);
          }
          templateData.put("obj", obj);
          renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
        }
      } else if (templateFileName.contains("$module$")) {
        for (String module : app.getModules()) {
          // TODO: NOT SUPPORT
          if (!matchLevelled(module, filters)) {
            continue;
          }
          String APIType = null;
          if (templateFileName.contains("[")) {
            APIType = templateFileName.substring(templateFileName.indexOf("[") + 1, templateFileName.indexOf("]"));
          }
          outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
          outputName = outputName.replace("$module$", naming.nameFile(module));
          // if contains two naming variables, FIXME: be better
          outputName = outputName.replace("$app$", naming.nameFile(app.getName()));
          outputName = outputName.replace("[", "");
          outputName = outputName.replace("]", "");
          templateData.put("module", module);
          // pages
          List<PageDefinition> pages = new ArrayList<>();
          for (PageDefinition page : app.getPages()) {
            if (page.getModule().equals(module)) {
              pages.add(page);
            }
          }
          templateData.put("pages", pages);
          List<ApiDefinition> APIs = new ArrayList<>();
          for (ApplicationApiDefinition appAPI : app.getAPI()) {
            for (ApiDefinition API : appAPI.getAPIs()) {
              if (module.equalsIgnoreCase(API.getModule())) {
                if (APIType == null) {
                  APIs.add(API);
                } else if (APIType.equalsIgnoreCase(API.getType())) {
                  APIs.add(API);
                }
              }
            }
          }
          templateData.put("APIs", APIs);
          if (!APIs.isEmpty() || !pages.isEmpty()) {
            renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
          }
        }
      } else if (templateFileName.contains("$page$")) {
        for (PageDefinition page : app.getPages()) {
          // grouping
          if (!matchLevelled(page, filters)) {
            continue;
          }
          page.setId(page.getName());
          String pageName = "";
          String newOutputPath = outputPath;
          int lastBackslashIndex = page.getName().lastIndexOf("/");
          if (lastBackslashIndex != -1) {
            String subpath = page.getName().substring(0, lastBackslashIndex);
            String[] strs = page.getName().split("/");
            /*!
            ** 通常窗体路径命名的规范为 module/object/action
            */
            if (!Strings.isEmpty((String)globalVariables.get("allowModuleAsDirectory"))) {
              for (int i = 1; i < strs.length; i++) {
                if (!"".equals(pageName)) {
                  pageName += "_";
                }
                pageName += strs[i];
              }
              if ("upper".equals(globalVariables.get("allowModuleAsDirectory"))) {
                newOutputPath += "/" + strs[0].toUpperCase();
              } else {
                newOutputPath += "/" + strs[0];
              }
            } else if ("true".equals(globalVariables.get("notAllowDirectory"))) {
              for (int i = 0; i < strs.length; i++) {
                if (!"".equals(pageName)) {
                  pageName += "_";
                }
                pageName += strs[i];
              }
            } else {
              pageName = strs[strs.length - 1];
              newOutputPath += "/" + subpath;
            }
          } else {
            pageName = page.getName();
          } // if (lastBackslashIndex != -1)

          outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
          outputName = outputName.replace("$page$", naming.nameFile(pageName));
          templateData.put("page", toTemplateData(page));
          templateData.put("pagedef", page);
          templateData.putAll(toTemplateData(page));
          renderTo(outputRoot, newOutputPath, outputName, templatePath, templateFileName, templateData);
        }
      } else if (templateFileName.contains("$tile$")) {
        /*!
        ** added tile support on Feb 12, 2024.
        */
        for (PageDefinition page : app.getPages()) {
          for (WidgetDefinition widget : page.getPageWidgets()) {
            if ("listview".equalsIgnoreCase(widget.getType()) ||
                "gridview".equalsIgnoreCase(widget.getType())) {
              WidgetDefinition tile = new WidgetDefinition();
              tile.setTiled(true);
              tile.setId(widget.getId() + "_tile");
              tile.setType("tile");
              for (WidgetDefinition child : widget.getWidgets()) {
                tile.addWidget(child);;
              }
              outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
              outputName = outputName.replace("$tile$", naming.nameFile(tile.getId()));
              templateData.put("page", toTemplateData(page));
              templateData.put("tile", toTemplateData(tile));
              renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
            }
            if (widget.isTiled()) {
              // FIXME: NEED DO IT OR NOT
              if (!matchLevelled(page, filters)) {
                continue;
              }
            }
          }
        }
      } else if (templateFileName.contains("$usecase$")) {
        // TODO
        for (UsecaseDefinition usecase : app.getUsecases()) {
          outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
          outputName = outputName.replace("$usecase$", naming.nameFile(usecase.getName()));
          templateData.put("usecase", usecase);
          renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
        }
      } else if (templateFileName.contains("$app$")) {
        outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
        outputName = outputName.replace("$app$", naming.nameFile(app.getName()));
        // 同时需要查看是否含有ns变量，此对应的是namespace，满足objective-c的命名规范
        if (templateFileName.contains("$ns$")) {
          String namespace = (String) globalVariables.get("namespace");
          namespace = namespace != null ? namespace : "";
          outputName = outputName.replace("$ns$", namespace);
        }
        templateData.put("app", app);
        renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
      } else if (templateFileName.contains("$namespace$")) {
        String namespace = (String) globalVariables.get("namespace");
        outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
        outputName = outputName.replace("$namespace$", naming.nameFile(namespace));
        templateData.put("app", app);
        renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
      } else if (templateFileName.contains("$usecases$")) {
        for (ObjectDefinition obj : app.getModel().getObjects()) {
          if (!obj.isLabelled("usecases"))
            continue;
          Map<String, String> usecases = obj.getLabelledOptions("usecases");
          for (Map.Entry<String, String> entry : usecases.entrySet()) {
            outputName = templateFileName.substring(0, templateFileName.lastIndexOf(".ftl"));
            outputName = outputName.replace("$usecases$", naming.nameFile(entry.getKey()));
            templateData.put("app", app);
            templateData.put("obj", obj);
            renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
          }
        }
      } else if (templateFileName.contains("$") && (templateFileName.indexOf("$") != templateFileName.lastIndexOf("$"))) {
        // THIS BLOCK IS VERY IMPORTANT AND SMART!!!
        // only for object and page

        /*!
        ** 主要实体。
        */
        int firstDollar = templateFileName.indexOf("$");
        int lastDollar = templateFileName.lastIndexOf("$");

        /*!
        ** 实体中的属性（下属实体）作为文件输出。
        */
        int firstBracket = templateFileName.indexOf("[");
        int lastBracket = templateFileName.indexOf("]");

        /*!
        ** 主要实体的过滤条件。
        **
        ** FIXME: SUPPORT ATTRIBUTE AS OUTPUT FILE
        */
        int firstParen = templateFileName.indexOf("(");
        int lastParen = templateFileName.indexOf(")");

        String label = templateFileName.substring(firstDollar + 1, lastDollar);
        String type = "";
        String attrname = "";
        String attrval  = "";
        /*!
         ** this if-statement make "$page[navigator](id=home)$" be allowed.
         */
        if (firstBracket != -1) {
          label = templateFileName.substring(firstDollar + 1, firstBracket);
          type = templateFileName.substring(firstBracket + 1, lastBracket);
        }
        if (firstParen != -1) {
          label = templateFileName.substring(firstDollar + 1, firstParen);
          String attr = templateFileName.substring(firstParen + 1, lastParen);
          String[] strs = attr.split("=");
          attrname = strs[0];
          attrval = strs[1];
        }

        if (firstBracket != -1) {
          /*!
          ** 把数据模型对象的属性渲染成代码文件
          */
          renderAttributesToFiles(naming, app, templateFileName, outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
          continue;
        }
        /*!
         ** 把页面部件的属性渲染成代码文件
         */
        if ("widget".equals(label)) {
          renderWidgetsToFiles(naming, app, templateFileName, outputRoot, outputPath, outputName, templatePath, templateFileName, templateData, filters);
        } else if ("page".equals(label)) {
          for (PageDefinition page : app.getPages()) {
            if (attrname.equals("")) {
              continue;
            }

            if (attrname.equals("id")) {
              if (attrval.equals(page.getId())) {
                outputName = templateFileName.replaceAll("\\$([^$]+)\\$", naming.nameFile(page.getId())).replace(".ftl", "");
                templateData.put("page", toTemplateData(page));
                templateData.put("pagedef", page);
                templateData.putAll(toTemplateData(page));
                renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
              }
            } else {
              if (attrval.equals(page.getOption(attrname))) {
                outputName = templateFileName.replaceAll("\\$([^$]+)\\$", naming.nameFile(page.getId())).replace(".ftl", "");
                templateData.put("page", toTemplateData(page));
                templateData.put("pagedef", page);
                templateData.putAll(toTemplateData(page));
                renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
              }
            }
          }
        }

        if (firstParen != -1) {
          /*!
          ** FIXME: 只输出在括号内满足条件的
          */
          continue;
        }
        /*!
        ** 处理对象的标注代码。
        */
        if (app.getModel() != null) {
          /*!
          ** 把数据模型对象渲染成代码文件。
          */
          renderLabelledObjectsToFiles(naming, app, filters, templateFileName, outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
        }
      } else {
        templateData.put("module", filters.get("module"));
        if (filters.get("page") != null) {
          PageDefinition page = (PageDefinition)filters.get("page");
          templateData.put("page", toTemplateData(page));
          templateData.put("pagedef", page);
          templateData.putAll(toTemplateData(page));
        }
        renderTo(outputRoot, outputPath, outputName, templatePath, templateFileName, templateData);
      }
    }
  }

  /**
   * Renders template and output to the specific path.
   *
   * @param outputRoot
   *        the output root, or the project root directory
   *
   * @param outputPath
   *        the output relative path under the root
   *
   * @param outputName
   *        the output file name
   *
   * @param templatePath
   *        the template relative path under the template root
   *
   * @param templateName
   *        the template file name
   *
   * @param templateData
   *        the template data
   *
   * @throws IOException
   *        in case of any errors
   */
  protected void renderTo(String outputRoot, String outputPath, String outputName, String templatePath, String templateName, Object templateData) throws IOException {
    Template template = FREEMARKER.getTemplate(templatePath + "/" + templateName, "UTF-8");
    File dir = new File(outputRoot, outputPath);
    dir.mkdirs();

    String filepath = outputRoot + "/" + outputPath + "/" + outputName;
    try (OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8")) {
      StringWriter writer = new StringWriter();
      template.process(templateData, writer);

      String[] lines = writer.toString().split("\\r\\n|\\r|\\n");
      int emptyLineCount = 0;
      for (String line : lines) {
        if (line.trim().isEmpty()) {
          emptyLineCount++;
        } else {
          emptyLineCount = 0;
        }
        if (emptyLineCount <= 1) {
          fileWriter.write(line + "\n");
        }
      }
      fileWriter.flush();
    } catch (TemplateException ex) {
      throw new IOException(ex);
    }
  }

  /**
   * For example: <p>
   *
   *   $entity(source)$.java.ftl
   *
   * @param naming
   * @param app
   * @param filename
   * @param outputRoot
   * @param outputPath
   * @param outputName
   * @param templatePath
   * @param templateName
   * @param templateData
   * @throws IOException
   */
  private void renderAttributesToFiles(NamingConvention naming, ApplicationDefinition app,
                                       String filename, String outputRoot, String outputPath, String outputName,
                                       String templatePath, String templateName, Map<String, Object> templateData) throws IOException {
    if (app.getModel() == null) {
      return;
    }
    int indexLeftBracket = filename.indexOf("[");
    int indexRightBracket = filename.indexOf("]");
    String labelObj = filename.substring(filename.indexOf("$") + 1, indexLeftBracket);
    String labelAttr = filename.substring(indexLeftBracket + 1, indexRightBracket);

    for (ObjectDefinition obj : app.getModel().getObjects()) {
      if (!obj.isLabelled(labelObj) || obj.isLabelled("generated")) {
        continue;
      }
      String alias = obj.getLabelledOptions(labelObj).get("alias");
      alias = Strings.isEmpty(alias) ? obj.getName() : alias;
      for (AttributeDefinition attr : obj.getAttributes()) {
        if (!attr.isLabelled(labelAttr) || !outputPath.endsWith(alias) /* FIXME: THE PARENT LEVEL DIRECTORY */) {
          continue;
        }
        String labelledOptionValue = attr.getLabelledOptions(labelAttr).get("name");
        if (labelledOptionValue == null) {
          labelledOptionValue = attr.getName();
        }
        /*!
        ** the real output file name
        */
        templateData.put(labelObj, obj);
        outputName = filename.substring(0, filename.lastIndexOf(".ftl"));
        outputName = outputName.replace("$" + labelObj + "[" + labelAttr + "]$", naming.nameFile(labelledOptionValue));
        templateData.put(labelAttr, attr);
        renderTo(outputRoot, outputPath, outputName, templatePath, filename, templateData);
      }
    }
  }

  private void renderWidgetsToFiles(NamingConvention naming, ApplicationDefinition app,
                                    String filename, String outputRoot, String outputPath, String outputName,
                                    String templatePath, String templateName, Map<String, Object> templateData,
                                    HashObject filters) throws IOException {
    int indexLeftBracket = filename.indexOf("[");
    int indexRightBracket = filename.indexOf("]");
    String labelObj = "";
    String labelWidgetType = "";
    if (indexLeftBracket != -1) {
      // 说明指明类型
      labelObj = filename.substring(filename.indexOf("$") + 1, indexLeftBracket);
      labelWidgetType = filename.substring(indexLeftBracket + 1, indexRightBracket);
    } else {
      labelObj = filename.substring(filename.indexOf("$") + 1, filename.lastIndexOf("$"));
    }
    if (!"widget".equals(labelObj)) {
      return;
    }
    if ("".equals(labelWidgetType)) {
      for (PageDefinition page : app.getPages()) {
        for (WidgetDefinition widget : page.getPageWidgets()) {
          if (widget.getId() == null || !widget.getId().equals(filters.get("widget"))) {
            continue;
          }
          /*!
           ** the real output file name
           */
          outputName = filename.substring(0, filename.lastIndexOf(".ftl"));
          outputName = outputName.replace("$" + labelObj + "$", naming.nameFile(widget.getId()));
          templateData.put("widget", toTemplateData(widget));
          templateData.put("page", page);
          renderTo(outputRoot, outputPath, outputName, templatePath, filename, templateData);
        }
      }
    } else {
      for (PageDefinition page : app.getPages()) {
        for (WidgetDefinition widget : page.getPageWidgets()) {
          if (!widget.getType().equalsIgnoreCase(labelWidgetType)) {
            continue;
          }
          /*!
           ** the real output file name
           */
          outputName = filename.substring(0, filename.lastIndexOf(".ftl"));
          outputName = outputName.replace("$" + labelObj + "[" + labelWidgetType + "]$", naming.nameFile(widget.getId()));
          templateData.put("widget", toTemplateData(widget));
          templateData.put("page", page);
          renderTo(outputRoot, outputPath, outputName, templatePath, filename, templateData);
        }
      }
    }

//    for (ObjectDefinition obj : app.getModel().getObjects()) {
//      if (!obj.isLabelled(labelObj) || obj.isLabelled("generated")) {
//        continue;
//      }
//      String alias = obj.getLabelledOptions(labelObj).get("alias");
//      alias = Strings.isEmpty(alias) ? obj.getName() : alias;
//      for (AttributeDefinition attr : obj.getAttributes()) {
//        if (!attr.isLabelled(labelAttr) || !outputPath.endsWith(alias) /* FIXME: THE PARENT LEVEL DIRECTORY */) {
//          continue;
//        }
//        String labelledOptionValue = attr.getLabelledOptions(labelAttr).get("name");
//        if (labelledOptionValue == null) {
//          labelledOptionValue = attr.getName();
//        }
//        /*!
//         ** the real output file name
//         */
//        templateData.put(labelObj, obj);
//        outputName = filename.substring(0, filename.lastIndexOf(".ftl"));
//        outputName = outputName.replace("$" + labelObj + "(" + labelAttr + ")$", naming.nameFile(labelledOptionValue));
//        templateData.put(labelAttr, attr);
//        renderTo(outputRoot, outputPath, outputName, templatePath, filename, templateData);
//      }
//    }
  }

  private void renderLabelledObjectsToFiles(NamingConvention naming, ApplicationDefinition app, HashObject filters,
                                            String filename, String outputRoot, String outputPath, String outputName,
                                            String templatePath, String templateName, Map<String, Object> templateData) throws IOException {
    int firstDollar = filename.indexOf("$");
    int lastDollar = filename.lastIndexOf("$");
    int firstBracket = filename.indexOf("[");
    int lastBracket = filename.indexOf("]");

    String label;
    if (firstBracket != -1) {
      label = filename.substring(firstDollar + 1, firstBracket);
    } else {
      label = filename.substring(firstDollar + 1, lastDollar);
    }

    for (ObjectDefinition obj : app.getModel().getObjects()) {
      if (!obj.isLabelled(label) || obj.isLabelled("generated")) {
        continue;
      }
      /*!
      ** 2025-01-04
      */
      if (!matchLevelled(obj, filters)) {
        continue;
      }
      /*!
      ** FIXME: SUPPORT OBJECT OPTION OUTPUT FILE UNDER OBJECT DIRECTORY
      */
      String alias = obj.getLabelledOptions(label).get("alias");
      alias = Strings.isEmpty(alias) ? obj.getName() : alias;
      if (filters.containsKey(label)) {
        if (!filters.get(label).equals(alias)) {
          continue;
        }
      }
      List<String> labelIds = obj.getLabelIdList(label);
      if (labelIds.isEmpty()) {
        // default implement if no label id
        labelIds.add(null);
      }
      for (String labelId : labelIds) {
        String labelledOptionName = null;
        String labelledOptionValue = obj.getName();
        if (firstBracket != -1) {
          labelledOptionName = filename.substring(firstBracket + 1, lastBracket);
          labelledOptionValue = obj.getLabelledOptions(label + (labelId == null ? "" : "#" + labelId)).get(labelledOptionName);
          if (labelledOptionValue == null) {
            labelledOptionValue = obj.getName();
          }
        }
        outputName = filename.substring(0, filename.lastIndexOf(".ftl"));
        if (firstBracket == -1) {
          outputName = outputName.replace("$" + label + "$", naming.nameFile(alias));
        } else {
          outputName = outputName.replace("$" + label + "[" + labelledOptionName + "]$", naming.nameFile(labelledOptionValue));
        }
        templateData.put(label, obj);
        templateData.put("labelId", labelId);
        renderTo(outputRoot, outputPath, outputName, templatePath, filename, templateData);
      }
    }
  }

  /**
   * Copies the file from template directory to output directory.
   *
   * @param outputRoot   the output root, or the project root directory
   * @param outputPath   the output relative path under the root
   * @param templateRoot the template root
   * @param templatePath the template relative path under the template root
   * @param filename     the resource file name
   * @throws IOException in case of any errors
   */
  protected void copyTo(String outputRoot, String outputPath, String templateRoot, String templatePath, String filename) throws IOException {
    File dir = new File(outputRoot, outputPath);
    dir.mkdirs();
    String sourcepath = templateRoot + "/" + templatePath + "/" + filename;
    Files.copy(new File(sourcepath).toPath(), new FileOutputStream(outputRoot + "/" + outputPath + "/" + filename));
  }

  /**
   * Gets the rendered html from template-category repository.
   *
   * @param templatePath the template path
   * @param templateName the template name
   * @param templateData the template data
   * @param indent       the indent size
   * @return the rendered source
   * @throws IOException in case of any errors
   */
  protected String renderBy(String templatePath, String templateName, Map<String, Object> templateData, int indent) throws IOException {
    try {
      Template template = FREEMARKER.getTemplate(templatePath + "/" + templateName, "UTF-8");
      StringWriter writer = new StringWriter();
      template.process(templateData, writer);
      return Io.indent(indent, writer.toString());
    } catch (TemplateException ex) {
      throw new IOException(ex);
    }
  }

  /**
   *
   * @param app
   *
   * @return
   *
   * @version change scope to public on 2018-12-24
   */
  public Map<String, Object> createTemplateData(ApplicationDefinition app) {
    HashObject retVal = new HashObject();
    retVal.put("app", app);
    if (app != null) {
      retVal.put("model", app.getModel());
    }
    retVal.put("c", C);
    retVal.put("cpp", CPP);
    retVal.put("sql", SQL);
    retVal.put("xml", XML);
    retVal.put("ruby", RUBY);
    retVal.put("java", JAVA);
    retVal.put("csharp", CSHARP);
    retVal.put("js", JAVASCRIPT);
    retVal.put("ts", TYPESCRIPT);
    retVal.put("swift", SWIFT);
    retVal.put("typebase", TYPEBASE);
    retVal.put("groovy", GROOVY);
    retVal.put("objc", OBJC);
    retVal.put("objectivec", OBJC);
    retVal.put("pascal", PASCAL);
    retVal.put("rust", RUST);
    retVal.put("dart", DART);


    retVal.put("layout", LAYOUT);
    retVal.put("plugin", this);
    retVal.putAll(globalVariables);
    // put self as self template variable
    retVal.put("self", retVal);
    return retVal;
  }

  protected Map<String, Object> toTemplateData(WidgetDefinition widget) {
    HashObject retVal = new HashObject();
    retVal.putAll(widget.getOptions());
    retVal.set("id", widget.getId());
    retVal.set("type", widget.getType());
    retVal.set("pageOwner", widget.getPage());
    retVal.set("container", widget.getContainer());
    if (widget.getPosition() != null) {
      retVal.set("pos", widget.getPosition());
      retVal.set("position", widget.getPosition());
      retVal.set("size", widget.getPosition().getSize());
    } else {
      retVal.set("pos", Position.DEFAULT);
      retVal.set("position", Position.DEFAULT);
      retVal.set("size", Size.DEFAULT);
    }
    retVal.set("process", widget.getProcess());
    List<Map<String, Object>> children = new ArrayList<>();
    widget.getWidgets().forEach(child -> {
      children.add(toTemplateData(child));
    });
    if (widget instanceof PageDefinition) {
      PageDefinition page = (PageDefinition) widget;
      List<Map<String, Object>> pageWidgets = new ArrayList<>();
      page.getPageWidgets().forEach(child -> {
        pageWidgets.add(toTemplateData(child));
      });
      retVal.set("pageWidgets", pageWidgets);
      retVal.set("module", page.getModule());
      retVal.set("text", page.getTitle());
      retVal.set("title", page.getTitle());
    }
    String opts = widget.getOption("opts");
    opts = opts == null ? widget.getOption("options") : opts;
    if (opts != null) {
      if (opts.indexOf("[") == 0) {
        List<StringPair> pairs = TYPEBASE.enumtype("enum" + opts);
        List<Option> options = new ArrayList<>();
        pairs.forEach(pair -> {
          Option opt = new Option();
          opt.setValue(pair.getKey());
          opt.setText(pair.getValue());
          options.add(opt);
        });
        retVal.set("options", options);
        retVal.set("opts", options);
      } else if (opts.indexOf("(") == 0) {
        String name = opts.substring(0, opts.indexOf("("));
        String tuple = opts.substring(opts.indexOf("("));
        List<String> kvNames = TYPEBASE.tupletype(tuple);
        String keyName = kvNames.get(0);
        String valueName = kvNames.get(1);
        HashObject optionsSource = new HashObject();
        optionsSource.set("name", name);
        optionsSource.set("value", keyName);
        optionsSource.set("text", valueName);
        retVal.set("optionsSource", optionsSource);
      } else if (opts.contains("(")) {
        int index = opts.indexOf("[");
        String itemsString = opts.substring(0, index);
        String enumString = opts.substring(index);
        List<StringPair> pairs = TYPEBASE.enumtype("enum" + enumString);
        StringPair pair = pairs.get(0);
        HashObject optsModel = new HashObject();
        optsModel.set("text", pair.getValue());
        optsModel.set("value", pair.getKey());

        String items = itemsString.substring(0, itemsString.indexOf("("));
        String item = itemsString.substring(itemsString.indexOf("(") + 1, itemsString.indexOf(")"));

        optsModel.set("items", items);
        optsModel.set("item", item);

        retVal.set("optsModel", optsModel);
      }
    }

    retVal.set("children", children);
    return retVal;
  }

  /**
   * 避免不同module下生成相同的文件。
   *
   * @param definition 任何定义
   * @param filters    过滤条件
   * @return 是或否
   */
  protected boolean matchLevelled(Object definition, HashObject filters) {
    if (filters.isEmpty()) {
      return true;
    }
    boolean retVal = true;
    if (definition instanceof PageDefinition) {
      PageDefinition page = (PageDefinition) definition;
      if (filters.containsKey("module")) {
        retVal = page.getModule().equals(filters.get("module"));
        if (!retVal) {
          return retVal;
        }
      }
      if (filters.containsKey("page")) {
        retVal = page.getId().equals(filters.get("page"));
        if (!retVal) {
          return retVal;
        }
      }
    } else if (definition instanceof WidgetDefinition) {
      WidgetDefinition widget = (WidgetDefinition) definition;
      if (filters.containsKey("module")) {
        retVal = widget.getPage().getModule().equals(filters.get("module"));
        if (!retVal) {
          return retVal;
        }
      }
      if (filters.containsKey("page")) {
        retVal = widget.getPage().getId().equals(filters.get("page"));
        if (!retVal) {
          return retVal;
        }
      }
    } else if (definition instanceof ObjectDefinition) {
      ObjectDefinition obj = (ObjectDefinition) definition;
      if (filters.containsKey("module")) {
        retVal = obj.getModuleName().equals(filters.get("module"));
        if (!retVal) {
          return false;
        }
      }
      if (filters.containsKey("obj")) {
        retVal = obj.getName().equals(filters.get("obj"));
        if (!retVal) {
          return false;
        }
      }
    } else if (definition instanceof ApiDefinition) {
      ApiDefinition API = (ApiDefinition) definition;
      if (filters.containsKey("module")) {
        retVal = API.getModule().equals(filters.get("module"));
        if (!retVal) {
          return retVal;
        }
      }
      if (filters.containsKey("API")) {
        retVal = API.getName().equals(filters.get("API"));
        if (!retVal) {
          return retVal;
        }
      }
    } else if (definition instanceof String) {
      if (filters.containsKey("module")) {
        retVal = definition.equals(filters.get("module"));
        if (!retVal) {
          return retVal;
        }
      }
    }
    return retVal;
  }

  @Deprecated
  protected String getConfiguration(String type, String configName, String defaultValue) {
    FileTemplateLoader loader = (FileTemplateLoader) FREEMARKER.getTemplateLoader();
    Properties props = new Properties();
    try {
      props.load(new FileInputStream(new File(loader.baseDir + "/$/" + type + "/" + type + ".conf")));
    } catch (Exception ex) {
      // no-op
    }
    if (props.containsKey(configName)) {
      return props.getProperty(configName);
    }
    return defaultValue;
  }

  private void collectWidgets(List<WidgetDefinition> collector, WidgetDefinition container, String widgetType) {
    if (widgetType.equals(container.getType())) {
      collector.add(container);
    }
    container.getWidgets().forEach(widget -> {
      collectWidgets(collector, widget, widgetType);
    });
  }

  private String get$TemplateFileName(String widgetType) {
    MultiTemplateLoader loader = (MultiTemplateLoader) FREEMARKER.getTemplateLoader();
    FileTemplateLoader subloader = (FileTemplateLoader) loader.getTemplateLoader(1);
    File dir = new File(subloader.getBaseDirectory(), "$/" + widgetType);
    if (!dir.exists()) {
      return null;
    }
    for (File tpl : dir.listFiles()) {
      String filename = tpl.getName();
      if (renderingFileType != null) {
        if (filename.matches(widgetType + "\\." + renderingFileType + "\\.ftl")) {
          return filename;
        }
      } else {
        if (filename.matches(widgetType + "\\..+\\.ftl")) {
          return filename;
        }
      }

    }
    return null;
  }

  private NamingConvention getNamingConvention(String filename) {
    if (filename.contains(".java")) {
      return new JavaNamingConvention();
    } else if (filename.contains(".rb")) {
      return new RubyNamingConvention();
    } else if (filename.contains(".sql")) {
      return new SQLNamingConvention();
    } else if (filename.contains(".js")) {
      return new JavaScriptNamingConvention();
//    } else if (filename.contains(".xml")) {
//      return new XMLNamingConvention();
    } else if (filename.contains(".ts")) {
      return new TypeScriptNamingConvention();
    } else if (filename.contains(".swift")) {
      return new SwiftNamingConvention();
    } else if (filename.contains(".groovy")) {
      return new GroovyNamingConvention();
    } else if (filename.contains(".m")) {
      return new ObjcNamingConvention();
    } else if (filename.contains(".mm")) {
      return new ObjcNamingConvention();
    } else if (filename.contains(".pas")) {
      return new PascalNamingConvention();
    } else if (filename.contains(".cs")) {
      return new CSharpNamingConvention();
    } else if (filename.contains(".xcodeproj")) {
      return new ObjcNamingConvention();
    }
//    else if (filename.contains(".hpp")) {
//      return new CppNamingConvention();
//    }
//    else if (filename.contains(".h")) {
//      return new CNamingConvention();
//    }
    if (globalVariables.get("globalNamingConvention") != null) {
      return (NamingConvention) globalVariables.get("globalNamingConvention");
    }
    // TODO: ENRICH
    return new JavaScriptNamingConvention();
  }

}

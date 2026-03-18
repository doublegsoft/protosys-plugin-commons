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

import com.doublegsoft.jcommons.programming.java.JavaConventions;
import com.doublegsoft.jcommons.programming.javascript.JavaScriptConventions;
import com.doublegsoft.jcommons.programming.ruby.RubyConventions;
import com.doublegsoft.jcommons.programming.sql.SQLConventions;
import com.doublegsoft.jcommons.programming.swift.SwiftConventions;
import com.doublegsoft.jcommons.programming.typescript.TypeScriptConventions;
import com.doublegsoft.jcommons.programming.xml.XMLConventions;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import io.doublegsoft.typebase.Typebase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * It is base code generator plugin for specific component and includes the base mechanism functions.
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class FileSystemTemplateBasedComponentPlugin {

  public final static SQLConventions SQL = new SQLConventions();

  public final static XMLConventions XML = new XMLConventions();

  public final static RubyConventions RUBY = new RubyConventions();

  public final static JavaConventions JAVA = new JavaConventions();

  public final static JavaScriptConventions JAVASCRIPT = new JavaScriptConventions();

  public final static TypeScriptConventions TYPESCRIPT = new TypeScriptConventions();

  public final static SwiftConventions SWIFT = new SwiftConventions();

  public final static Configuration FREEMARKER = new Configuration(Configuration.getVersion());

  public final static Typebase TYPEBASE = new Typebase();

  protected Map<String, Object> globalVariables = new HashMap<>();

  protected String templateRoot;

  /**
   * Constructs a plugin instance with the template root.
   *
   * @param templateRoot the template root
   * @throws IOException in case of freemarker template loader configuration errors
   */
  public FileSystemTemplateBasedComponentPlugin(String templateRoot) throws IOException {
    this.templateRoot = templateRoot;
    FREEMARKER.setTemplateLoader(new FileTemplateLoader(new File(templateRoot)));
  }

}

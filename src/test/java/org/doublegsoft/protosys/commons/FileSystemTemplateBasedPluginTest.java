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
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: ADD DESCRIPTION
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class FileSystemTemplateBasedPluginTest {

  public static final String CODEBASE_ROOT = "E:/local/works/doublegsoft.io/codebase/03.Development/codebase-data";

  public static final String GROWING_APPLICATION_ROOT = "E:/local/works/doublegsoft.me/growing/03.Development";

  @Ignore
  public void test_rails_rest() throws Exception {
    String templateRoot = CODEBASE_ROOT + "/ruby-rest@rails-1.0";
    String outputRoot = GROWING_APPLICATION_ROOT + "/growing-server";

    FileSystemTemplateBasedPlugin plugin = new FileSystemTemplateBasedPlugin();
    plugin.prototype(
        plugin.createMisumlContexts(new String[]{outputRoot + "/../../02.Designing/growing-server.misuml"}),
        plugin.createModelFromMetadata(new String[]{outputRoot + "/../../02.Designing/growing-sqlite3.xlsx"}),
        outputRoot, templateRoot, new HashObject());
  }

  @Ignore
  public void test_angular_covalent() throws Exception {
    String templateRoot = CODEBASE_ROOT + "/html-angular+covalent-1.0";
    String outputRoot = GROWING_APPLICATION_ROOT + "/growing-web";

    FileSystemTemplateBasedPlugin plugin = new FileSystemTemplateBasedPlugin();
    plugin.prototype(
        plugin.createMisumlContexts(new String[]{outputRoot + "/../../02.Designing/growing-server.misuml"}),
        plugin.createModelFromMetadata(new String[]{outputRoot + "/../../02.Designing/growing-sqlite3.xlsx"}),
        outputRoot, templateRoot, new HashObject());
  }

  @Ignore
  public void test_android_java() throws Exception {
    String templateRoot = CODEBASE_ROOT + "/android-mvp@java-1.0";
    String outputRoot = GROWING_APPLICATION_ROOT + "/growing-android";

    HashObject globals = new HashObject();
    globals.put("namespace", "me.doublegsoft");
    FileSystemTemplateBasedPlugin plugin = new FileSystemTemplateBasedPlugin();
    plugin.prototype(
        plugin.createMisumlContexts(new String[]{outputRoot + "/../../02.Designing/growing-mobile.misuml"}),
        plugin.createModelFromMetadata(new String[]{outputRoot + "/../../02.Designing/growing-sqlite3.xlsx"}),
        outputRoot, templateRoot, globals);
  }

  @Ignore
  public void test_vue_vuetify() throws Exception {
    String templateRoot = CODEBASE_ROOT + "/html-vue+vuetify-1.0";
    String outputRoot = GROWING_APPLICATION_ROOT + "/growing-desktop";

    FileSystemTemplateBasedPlugin plugin = new FileSystemTemplateBasedPlugin();
    plugin.prototype(
        plugin.createMisumlContexts(new String[]{outputRoot + "/../../02.Designing/growing-mobile.misuml"}),
        plugin.createModelFromMetadata(new String[]{outputRoot + "/../../02.Designing/growing-sqlite3.xlsx"}),
        outputRoot, templateRoot, new HashObject());
  }

  @Ignore
  public void test_angular_ionic() throws Exception {
    String templateRoot = CODEBASE_ROOT + "/html-angular+ionic-1.0";
    String outputRoot = GROWING_APPLICATION_ROOT + "/growing-mobile";

    FileSystemTemplateBasedPlugin plugin = new FileSystemTemplateBasedPlugin();
    plugin.prototype(
        plugin.createMisumlContexts(new String[]{outputRoot + "/../../02.Designing/growing-mobile.misuml"}),
        plugin.createModelFromMetadata(new String[]{outputRoot + "/../../02.Designing/growing-sqlite3.xlsx"}),
        outputRoot, templateRoot, new HashObject());
  }

}

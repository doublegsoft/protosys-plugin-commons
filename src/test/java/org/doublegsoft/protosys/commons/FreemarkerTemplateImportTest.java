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

import com.doublegsoft.jcommons.metaui.layout.Size;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: ADD DESCRIPTION
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class FreemarkerTemplateImportTest {

  @Test
  public void test() throws Exception {
    Configuration freemarker = new Configuration(Configuration.getVersion());
    freemarker.setClassForTemplateLoading(FreemarkerTemplateImportTest.class, "/");
    Template template = freemarker.getTemplate("/test.ftl");
    Map<String, Object> data = new HashMap<>();
    StringWriter writer = new StringWriter();

    //
    Size size = Size.of("(30%, 400px)");
    data.put("size", size);

    template.process(data, writer);
    System.out.println(writer);
  }

}

begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|swagger
operator|.
name|ui
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SwaggerUIResourceLocatorTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MalformedURLException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testLocateWithBadCharactersInUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|String
name|url
init|=
literal|"jar:file:/Volumes/bigdrive/test157/jetty/base/webapps/"
operator|+
literal|"Rhythmyx/WEB-INF/lib/swagger-ui-2.2.10-1.jar!/META-INF/resources/"
operator|+
literal|"webjars/swagger-ui/2.2.10-1/assets/by-path//Assets/uploads/"
operator|+
literal|"Screen Shot 2020-02-05 at 10.50.53 AM.png"
decl_stmt|;
name|SwaggerUiResourceLocator
name|locator
init|=
operator|new
name|SwaggerUiResourceLocator
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|locator
operator|.
name|locate
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


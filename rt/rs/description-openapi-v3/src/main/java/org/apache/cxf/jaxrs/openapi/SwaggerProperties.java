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
name|openapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_interface
interface|interface
name|SwaggerProperties
block|{
name|String
name|RESOURCE_PACKAGE_PROPERTY
init|=
literal|"resource.package"
decl_stmt|;
name|String
name|TITLE_PROPERTY
init|=
literal|"title"
decl_stmt|;
name|String
name|VERSION_PROPERTY
init|=
literal|"version"
decl_stmt|;
name|String
name|DESCRIPTION_PROPERTY
init|=
literal|"description"
decl_stmt|;
name|String
name|CONTACT_PROPERTY
init|=
literal|"contact"
decl_stmt|;
name|String
name|LICENSE_PROPERTY
init|=
literal|"license"
decl_stmt|;
name|String
name|LICENSE_URL_PROPERTY
init|=
literal|"license.url"
decl_stmt|;
name|String
name|TERMS_URL_PROPERTY
init|=
literal|"terms.url"
decl_stmt|;
name|String
name|PRETTY_PRINT_PROPERTY
init|=
literal|"pretty.print"
decl_stmt|;
name|String
name|FILTER_CLASS_PROPERTY
init|=
literal|"filter.class"
decl_stmt|;
comment|/**      * Read the Swagger-specific properties from the property file (to seamlessly      * support the migration from older Swagger features).      * @param location property file location      * @param bus bus instance      * @return the properties if available       */
specifier|default
name|Properties
name|getSwaggerProperties
parameter_list|(
name|String
name|location
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getClasspathResourceStream
argument_list|(
name|location
argument_list|,
name|SwaggerProperties
operator|.
name|class
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|props
operator|=
literal|null
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
return|return
name|props
return|;
block|}
block|}
end_interface

end_unit


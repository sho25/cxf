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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Replace by org.springframework.core.io.support.PropertiesLoaderUtils  * when moving to Spring 2.0.  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PropertiesLoaderUtils
block|{
comment|/**      * Prevents instantiation.      */
specifier|private
name|PropertiesLoaderUtils
parameter_list|()
block|{     }
comment|/**      * Load all properties from the given class path resource, using the given      * class loader.      *<p>      * Merges properties if more than one resource of the same name found in the      * class path.      *      * @param resourceName the name of the class path resource      * @param classLoader the ClassLoader to use for loading (or      *<code>null</code> to use the default class loader)      * @return the populated Properties instance      * @throws IOException if loading failed      */
specifier|public
specifier|static
name|Properties
name|loadAllProperties
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|loadAllProperties
argument_list|(
name|resourceName
argument_list|,
name|classLoader
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Properties
name|loadAllProperties
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|Level
name|level
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// Use default class loader if neccessary
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|(
name|classLoader
operator|!=
literal|null
condition|?
name|classLoader
else|:
name|PropertiesLoaderUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|)
operator|.
name|getResources
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|msg
argument_list|,
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
init|)
block|{
name|properties
operator|.
name|loadFromXML
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit


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
name|maven_plugin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
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
name|tools
operator|.
name|wsdlto
operator|.
name|WSDLToJava
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CodegenUtils
block|{
specifier|static
name|long
name|timestamp
decl_stmt|;
specifier|private
name|CodegenUtils
parameter_list|()
block|{
comment|//not consructed
block|}
specifier|public
specifier|static
name|long
name|getCodegenTimestamp
parameter_list|()
block|{
if|if
condition|(
name|timestamp
operator|!=
literal|0
condition|)
block|{
return|return
name|timestamp
return|;
block|}
name|getClassTime
argument_list|(
name|CodegenUtils
operator|.
name|class
argument_list|)
expr_stmt|;
name|getClassTime
argument_list|(
name|WSDLToJava
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|timestamp
return|;
block|}
specifier|private
specifier|static
name|void
name|getClassTime
parameter_list|(
name|Class
name|class1
parameter_list|)
block|{
name|String
name|str
init|=
literal|"/"
operator|+
name|class1
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
name|URL
name|url
init|=
name|class1
operator|.
name|getResource
argument_list|(
name|str
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|str
operator|=
name|url
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|str
operator|.
name|lastIndexOf
argument_list|(
literal|"!"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|lastIndexOf
argument_list|(
literal|"!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return;
block|}
block|}
try|try
block|{
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|timestamp
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|lastModified
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|Enumeration
name|entries
init|=
name|jar
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|entries
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|entry
init|=
operator|(
name|JarEntry
operator|)
name|entries
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"META"
argument_list|)
operator|&&
name|entry
operator|.
name|getTime
argument_list|()
operator|>
name|timestamp
condition|)
block|{
name|timestamp
operator|=
name|entry
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


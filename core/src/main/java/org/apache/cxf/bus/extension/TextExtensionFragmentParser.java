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
name|bus
operator|.
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
specifier|public
class|class
name|TextExtensionFragmentParser
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TextExtensionFragmentParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|loader
decl_stmt|;
specifier|public
name|TextExtensionFragmentParser
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Extension
argument_list|>
name|getExtensions
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
return|return
name|getExtensions
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
block|}
comment|/**      * Reads extension definitions from a Text file and instantiates them      * The text file has the following syntax      * classname:interfacename:deferred(true|false):optional(true|false)      *      * @param is stream to read the extension from      * @return list of Extensions      * @throws IOException      */
specifier|public
name|List
argument_list|<
name|Extension
argument_list|>
name|getExtensions
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|extensions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Extension
name|extension
init|=
name|getExtensionFromTextLine
argument_list|(
name|line
argument_list|)
decl_stmt|;
if|if
condition|(
name|extension
operator|!=
literal|null
condition|)
block|{
name|extensions
operator|.
name|add
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
return|return
name|extensions
return|;
block|}
specifier|private
name|Extension
name|getExtensionFromTextLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|isEmpty
argument_list|()
operator|||
name|line
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'#'
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Extension
name|ext
init|=
operator|new
name|Extension
argument_list|(
name|loader
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|parts
init|=
name|line
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|ext
operator|.
name|setClassname
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|ext
operator|.
name|getClassname
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|parts
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|String
name|interfaceName
init|=
name|parts
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|interfaceName
operator|!=
literal|null
operator|&&
name|interfaceName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|interfaceName
operator|=
literal|null
expr_stmt|;
block|}
name|ext
operator|.
name|setInterfaceName
argument_list|(
name|interfaceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parts
operator|.
name|length
operator|>=
literal|3
condition|)
block|{
name|ext
operator|.
name|setDeferred
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|parts
index|[
literal|2
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parts
operator|.
name|length
operator|>=
literal|4
condition|)
block|{
name|ext
operator|.
name|setOptional
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ext
return|;
block|}
block|}
end_class

end_unit


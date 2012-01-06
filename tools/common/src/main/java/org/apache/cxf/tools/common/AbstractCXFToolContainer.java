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
name|tools
operator|.
name|common
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|common
operator|.
name|toolspec
operator|.
name|AbstractToolContainer
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
name|common
operator|.
name|toolspec
operator|.
name|ToolSpec
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
name|common
operator|.
name|toolspec
operator|.
name|parser
operator|.
name|BadUsageException
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
name|common
operator|.
name|toolspec
operator|.
name|parser
operator|.
name|CommandDocument
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
name|common
operator|.
name|toolspec
operator|.
name|parser
operator|.
name|CommandLineParser
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
name|common
operator|.
name|toolspec
operator|.
name|parser
operator|.
name|ErrorVisitor
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
name|util
operator|.
name|ClassCollector
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
name|version
operator|.
name|Version
import|;
end_import

begin_comment
comment|/**  * Common processing for the CXF tools. Processes common options.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCXFToolContainer
extends|extends
name|AbstractToolContainer
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractCXFToolContainer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
name|CommandDocument
name|commandDocument
decl_stmt|;
specifier|private
name|boolean
name|verbose
decl_stmt|;
specifier|private
name|String
name|usage
decl_stmt|;
specifier|private
specifier|final
name|ErrorVisitor
name|errors
init|=
operator|new
name|ErrorVisitor
argument_list|()
decl_stmt|;
specifier|public
name|AbstractCXFToolContainer
parameter_list|(
name|String
name|nm
parameter_list|,
name|ToolSpec
name|toolspec
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|toolspec
argument_list|)
expr_stmt|;
name|name
operator|=
name|nm
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasInfoOption
parameter_list|()
throws|throws
name|ToolException
block|{
name|commandDocument
operator|=
name|getCommandDocument
argument_list|()
expr_stmt|;
if|if
condition|(
name|commandDocument
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|(
name|commandDocument
operator|.
name|hasParameter
argument_list|(
literal|"help"
argument_list|)
operator|)
operator|||
operator|(
name|commandDocument
operator|.
name|hasParameter
argument_list|(
literal|"version"
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|boolean
name|exitOnFinish
parameter_list|)
throws|throws
name|ToolException
block|{
name|super
operator|.
name|execute
argument_list|(
name|exitOnFinish
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasInfoOption
argument_list|()
condition|)
block|{
name|outputInfo
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|commandDocument
operator|.
name|hasParameter
argument_list|(
name|ToolConstants
operator|.
name|CFG_VERBOSE
argument_list|)
condition|)
block|{
name|verbose
operator|=
literal|true
expr_stmt|;
name|outputFullCommandLine
argument_list|()
expr_stmt|;
name|outputVersion
argument_list|()
expr_stmt|;
block|}
name|checkParams
argument_list|(
name|errors
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|outputInfo
parameter_list|()
block|{
name|CommandLineParser
name|parser
init|=
name|getCommandLineParser
argument_list|()
decl_stmt|;
if|if
condition|(
name|commandDocument
operator|.
name|hasParameter
argument_list|(
literal|"help"
argument_list|)
condition|)
block|{
try|try
block|{
name|out
operator|.
name|println
argument_list|(
name|name
operator|+
literal|" "
operator|+
name|getUsage
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Options: "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|parser
operator|.
name|getFormattedDetailedUsage
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|toolUsage
init|=
name|parser
operator|.
name|getToolUsage
argument_list|()
decl_stmt|;
if|if
condition|(
name|toolUsage
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|toolUsage
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Error: Could not output detailed usage"
argument_list|)
expr_stmt|;
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|commandDocument
operator|.
name|hasParameter
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|outputVersion
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Check command-line parameters for validity. Since subclasses delegate down to here,      * this cannot complain about unwanted options.      * @param err place to report errors.      * @throws ToolException for impossible options.      */
specifier|public
name|void
name|checkParams
parameter_list|(
name|ErrorVisitor
name|err
parameter_list|)
throws|throws
name|ToolException
block|{
comment|//nothing to do here
block|}
specifier|public
name|boolean
name|isVerboseOn
parameter_list|()
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|context
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|verbose
return|;
block|}
specifier|public
name|String
name|getToolName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getUsage
parameter_list|()
block|{
if|if
condition|(
name|usage
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|CommandLineParser
name|parser
init|=
name|getCommandLineParser
argument_list|()
decl_stmt|;
if|if
condition|(
name|parser
operator|!=
literal|null
condition|)
block|{
name|usage
operator|=
name|parser
operator|.
name|getUsage
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|usage
operator|=
literal|"Could not get usage for the tool"
expr_stmt|;
block|}
block|}
return|return
name|usage
return|;
block|}
specifier|public
name|void
name|outputVersion
parameter_list|()
block|{
name|out
operator|.
name|println
argument_list|(
name|name
operator|+
literal|" - "
operator|+
name|Version
operator|.
name|getCompleteVersionString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|outputFullCommandLine
parameter_list|()
block|{
name|out
operator|.
name|print
argument_list|(
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getArgument
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|getArgument
argument_list|()
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getFileBase
parameter_list|(
name|String
name|wsdlUrl
parameter_list|)
block|{
name|String
name|fileBase
init|=
name|wsdlUrl
decl_stmt|;
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|wsdlUrl
argument_list|,
literal|"\\/"
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|fileBase
operator|=
name|tok
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fileBase
operator|.
name|endsWith
argument_list|(
literal|".wsdl"
argument_list|)
condition|)
block|{
name|fileBase
operator|=
operator|new
name|String
argument_list|(
name|fileBase
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileBase
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|fileBase
return|;
block|}
specifier|public
name|void
name|printUsageException
parameter_list|(
name|String
name|toolName
parameter_list|,
name|BadUsageException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|verbose
condition|)
block|{
name|outputFullCommandLine
argument_list|()
expr_stmt|;
block|}
name|err
operator|.
name|println
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|err
operator|.
name|println
argument_list|(
literal|"Usage : "
operator|+
name|toolName
operator|+
literal|" "
operator|+
name|ex
operator|.
name|getUsage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|outputVersion
argument_list|()
expr_stmt|;
block|}
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getFileName
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
name|int
name|idx
init|=
name|loc
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|loc
operator|=
name|loc
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|idx
operator|=
name|loc
operator|.
name|lastIndexOf
argument_list|(
literal|"\\"
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|loc
operator|=
name|loc
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|idx
operator|=
name|loc
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|loc
operator|=
name|loc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|StringTokenizer
name|strToken
init|=
operator|new
name|StringTokenizer
argument_list|(
name|loc
argument_list|,
literal|"-.!~*'();?:@&=+$,"
argument_list|)
decl_stmt|;
name|StringBuilder
name|strBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|strToken
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|strBuf
operator|.
name|append
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|strToken
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|strBuf
operator|.
name|append
argument_list|(
name|strToken
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|strToken
operator|.
name|countTokens
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|strBuf
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|strBuf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|AbstractCXFToolContainer
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|InputStream
name|ins
init|=
name|cl
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|ins
operator|==
literal|null
operator|&&
name|resource
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|ins
operator|=
name|cl
operator|.
name|getResourceAsStream
argument_list|(
name|resource
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ins
return|;
block|}
specifier|public
name|Properties
name|loadProperties
parameter_list|(
name|InputStream
name|inputs
parameter_list|)
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|p
operator|.
name|load
argument_list|(
name|inputs
argument_list|)
expr_stmt|;
name|inputs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore, use defaults
block|}
return|return
name|p
return|;
block|}
specifier|public
name|Properties
name|loadProperties
parameter_list|(
name|String
name|propertyFile
parameter_list|)
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|ins
init|=
name|getResourceAsStream
argument_list|(
name|propertyFile
argument_list|)
decl_stmt|;
name|p
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore, use defaults
block|}
return|return
name|p
return|;
block|}
specifier|protected
name|String
index|[]
name|getDefaultExcludedNamespaces
parameter_list|(
name|String
name|excludeProps
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
name|loadProperties
argument_list|(
name|excludeProps
argument_list|)
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|Enumeration
argument_list|<
name|?
argument_list|>
name|nexcludes
init|=
name|props
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|nexcludes
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
operator|(
name|String
operator|)
name|nexcludes
operator|.
name|nextElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * get all parameters in a map      * @param stringArrayKeys, contains keys, whose value should be string array      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParametersMap
parameter_list|(
name|Set
name|stringArrayKeys
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|CommandDocument
name|doc
init|=
name|getCommandDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return
name|map
return|;
block|}
name|String
index|[]
name|keys
init|=
name|doc
operator|.
name|getParameterNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|keys
operator|==
literal|null
condition|)
block|{
return|return
name|map
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keys
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|stringArrayKeys
operator|.
name|contains
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|,
name|doc
operator|.
name|getParameters
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|,
name|doc
operator|.
name|getParameter
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|protected
name|ClassCollector
name|createClassCollector
parameter_list|()
block|{
name|ClassCollector
name|collector
init|=
operator|new
name|ClassCollector
argument_list|()
decl_stmt|;
name|String
name|reserved
index|[]
init|=
operator|(
name|String
index|[]
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_RESERVE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|reserved
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|r
range|:
name|reserved
control|)
block|{
name|collector
operator|.
name|reserveClass
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collector
return|;
block|}
block|}
end_class

end_unit


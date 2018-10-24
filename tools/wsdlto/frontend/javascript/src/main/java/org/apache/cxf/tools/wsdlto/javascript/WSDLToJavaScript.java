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
name|wsdlto
operator|.
name|javascript
package|;
end_package

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
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|CommandInterfaceUtils
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
name|ToolConstants
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
name|ToolContext
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
name|ToolException
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
name|ToolRunner
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLToJavaScript
block|{
specifier|private
name|String
index|[]
name|args
decl_stmt|;
specifier|public
name|WSDLToJavaScript
parameter_list|()
block|{
name|args
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
specifier|public
name|WSDLToJavaScript
parameter_list|(
name|String
index|[]
name|pargs
parameter_list|)
block|{
name|args
operator|=
name|pargs
expr_stmt|;
block|}
specifier|private
name|boolean
name|isExitOnFinish
parameter_list|()
block|{
name|String
name|exit
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"exitOnFinish"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|exit
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|"YES"
operator|.
name|equalsIgnoreCase
argument_list|(
name|exit
argument_list|)
operator|||
literal|"TRUE"
operator|.
name|equalsIgnoreCase
argument_list|(
name|exit
argument_list|)
return|;
block|}
specifier|public
name|void
name|run
parameter_list|(
name|ToolContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_CMD_ARG
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|toolspecStream
init|=
name|getResourceAsStream
argument_list|(
name|WSDLToJavaScript
operator|.
name|class
argument_list|,
literal|"javascript-toolspec.xml"
argument_list|)
decl_stmt|;
name|ToolRunner
operator|.
name|runTool
argument_list|(
name|JavaScriptContainer
operator|.
name|class
argument_list|,
name|toolspecStream
argument_list|,
literal|false
argument_list|,
name|args
argument_list|,
name|isExitOnFinish
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isVerbose
parameter_list|()
block|{
return|return
name|isSet
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-V"
block|,
literal|"-verbose"
block|}
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isSet
parameter_list|(
name|String
index|[]
name|keys
parameter_list|)
block|{
if|if
condition|(
name|args
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|pargs
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
name|pargs
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setArguments
parameter_list|(
name|String
index|[]
name|pargs
parameter_list|)
block|{
name|args
operator|=
name|pargs
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|pargs
parameter_list|)
block|{
name|CommandInterfaceUtils
operator|.
name|commandCommonMain
argument_list|()
expr_stmt|;
name|WSDLToJavaScript
name|w2j
init|=
operator|new
name|WSDLToJavaScript
argument_list|(
name|pargs
argument_list|)
decl_stmt|;
try|try
block|{
name|w2j
operator|.
name|run
argument_list|(
operator|new
name|ToolContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ToolException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WSDLToJS Error : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|w2j
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|w2j
operator|.
name|isExitOnFinish
argument_list|()
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|1
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WSDLToJS Error : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|w2j
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|w2j
operator|.
name|isExitOnFinish
argument_list|()
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|w2j
operator|.
name|isExitOnFinish
argument_list|()
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|InputStream
name|getResourceAsStream
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|String
name|file
parameter_list|)
block|{
return|return
name|clz
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
end_class

end_unit


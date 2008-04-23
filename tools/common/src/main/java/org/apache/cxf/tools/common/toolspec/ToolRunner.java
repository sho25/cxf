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
operator|.
name|toolspec
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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|i18n
operator|.
name|Message
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

begin_class
specifier|public
specifier|final
class|class
name|ToolRunner
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
name|ToolRunner
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ToolRunner
parameter_list|()
block|{
comment|// utility class - never constructed
block|}
specifier|public
specifier|static
name|void
name|runTool
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|clz
parameter_list|,
name|InputStream
name|toolspecStream
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|runTool
argument_list|(
name|clz
argument_list|,
name|toolspecStream
argument_list|,
name|validate
argument_list|,
name|args
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|runTool
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|clz
parameter_list|,
name|InputStream
name|toolspecStream
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|ToolContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|runTool
argument_list|(
name|clz
argument_list|,
name|toolspecStream
argument_list|,
name|validate
argument_list|,
name|args
argument_list|,
literal|true
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|runTool
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|clz
parameter_list|,
name|InputStream
name|toolspecStream
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|boolean
name|exitOnFinish
parameter_list|)
throws|throws
name|Exception
block|{
name|runTool
argument_list|(
name|clz
argument_list|,
name|toolspecStream
argument_list|,
name|validate
argument_list|,
name|args
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|runTool
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|clz
parameter_list|,
name|InputStream
name|toolspecStream
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|boolean
name|exitOnFinish
parameter_list|,
name|ToolContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|ToolContainer
name|container
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Constructor
argument_list|<
name|?
extends|extends
name|ToolContainer
argument_list|>
name|cons
init|=
name|clz
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|ToolSpec
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|container
operator|=
name|cons
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|ToolSpec
argument_list|(
name|toolspecStream
argument_list|,
name|validate
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"CLZ_CANNOT_BE_CONSTRUCTED"
argument_list|,
name|LOG
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
try|try
block|{
name|container
operator|.
name|setArguments
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|container
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|container
operator|.
name|execute
argument_list|(
name|exitOnFinish
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit


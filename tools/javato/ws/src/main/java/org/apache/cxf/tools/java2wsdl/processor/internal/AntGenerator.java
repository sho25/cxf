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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
package|;
end_package

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
name|Map
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
name|model
operator|.
name|JavaInterface
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
name|model
operator|.
name|JavaModel
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
name|tools
operator|.
name|wsdlto
operator|.
name|core
operator|.
name|AbstractGenerator
import|;
end_import

begin_class
specifier|public
class|class
name|AntGenerator
extends|extends
name|AbstractGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BUILD_TEMPLATE
init|=
literal|"org/apache/cxf/tools/java2wsdl/processor/internal/build.xml.vm"
decl_stmt|;
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|ClassCollector
name|collector
parameter_list|,
name|String
name|packageName
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
comment|//nothing to do
block|}
specifier|public
name|boolean
name|passthrough
parameter_list|()
block|{
return|return
operator|!
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_ANT
argument_list|)
return|;
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|penv
parameter_list|)
throws|throws
name|ToolException
block|{
name|this
operator|.
name|env
operator|=
name|penv
expr_stmt|;
if|if
condition|(
name|passthrough
argument_list|()
condition|)
block|{
return|return;
block|}
name|JavaModel
name|javaModel
init|=
name|env
operator|.
name|get
argument_list|(
name|JavaModel
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|JavaInterface
argument_list|>
name|interfaces
init|=
name|javaModel
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serverClassNamesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clientClassNamesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|JavaInterface
name|intf
range|:
name|interfaces
operator|.
name|values
argument_list|()
control|)
block|{
name|clientClassNamesMap
operator|.
name|put
argument_list|(
name|intf
operator|.
name|getName
argument_list|()
operator|+
literal|"Client"
argument_list|,
name|intf
operator|.
name|getFullClassName
argument_list|()
operator|+
literal|"Client"
argument_list|)
expr_stmt|;
name|serverClassNamesMap
operator|.
name|put
argument_list|(
name|intf
operator|.
name|getName
argument_list|()
operator|+
literal|"Server"
argument_list|,
name|intf
operator|.
name|getFullClassName
argument_list|()
operator|+
literal|"Server"
argument_list|)
expr_stmt|;
block|}
name|clearAttributes
argument_list|()
expr_stmt|;
name|setAttributes
argument_list|(
literal|"clientClassNamesMap"
argument_list|,
name|clientClassNamesMap
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"serverClassNamesMap"
argument_list|,
name|serverClassNamesMap
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"srcdir"
argument_list|,
name|penv
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SOURCEDIR
argument_list|)
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"clsdir"
argument_list|,
name|penv
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"classpath"
argument_list|,
name|penv
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSPATH
argument_list|)
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"classpath"
argument_list|,
name|penv
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSPATH
argument_list|)
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|BUILD_TEMPLATE
argument_list|,
name|parseOutputName
argument_list|(
literal|null
argument_list|,
literal|"build"
argument_list|,
literal|".xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


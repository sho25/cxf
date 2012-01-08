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
name|frontend
operator|.
name|jaxws
operator|.
name|generators
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|helpers
operator|.
name|CastUtils
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|common
operator|.
name|model
operator|.
name|JavaPort
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
name|JavaServiceClass
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
name|NameUtil
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
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|WSDLToJavaProcessor
import|;
end_import

begin_class
specifier|public
class|class
name|AntGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ANT_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/build.vm"
decl_stmt|;
specifier|public
name|AntGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|ANT_GENERATOR
expr_stmt|;
block|}
specifier|public
name|boolean
name|passthrough
parameter_list|()
block|{
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_ANT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_ANT
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
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
name|Map
argument_list|<
name|QName
argument_list|,
name|JavaModel
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|penv
operator|.
name|get
argument_list|(
name|WSDLToJavaProcessor
operator|.
name|MODEL_MAP
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|JavaModel
name|javaModel
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|javaModel
operator|.
name|getServiceClasses
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|ServiceInfo
name|serviceInfo
init|=
name|env
operator|.
name|get
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|wsdl
init|=
name|serviceInfo
operator|.
name|getDescription
argument_list|()
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"CAN_NOT_GEN_ANT"
argument_list|,
name|LOG
argument_list|,
name|wsdl
argument_list|)
decl_stmt|;
if|if
condition|(
name|penv
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
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
name|JavaInterface
argument_list|>
name|interfaces
init|=
name|javaModel
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
for|for
control|(
name|JavaServiceClass
name|js
range|:
name|javaModel
operator|.
name|getServiceClasses
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|JavaPort
name|jp
range|:
name|js
operator|.
name|getPorts
argument_list|()
control|)
block|{
name|String
name|interfaceName
init|=
name|jp
operator|.
name|getInterfaceClass
argument_list|()
decl_stmt|;
name|JavaInterface
name|intf
init|=
name|interfaces
operator|.
name|get
argument_list|(
name|interfaceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|intf
operator|==
literal|null
condition|)
block|{
name|interfaceName
operator|=
name|jp
operator|.
name|getPortType
argument_list|()
expr_stmt|;
name|intf
operator|=
name|interfaces
operator|.
name|get
argument_list|(
name|interfaceName
argument_list|)
expr_stmt|;
block|}
name|String
name|clientClassName
init|=
name|intf
operator|.
name|getPackageName
argument_list|()
operator|+
literal|"."
operator|+
name|interfaceName
operator|+
literal|"_"
operator|+
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
name|jp
operator|.
name|getPortName
argument_list|()
argument_list|)
operator|+
literal|"_Client"
decl_stmt|;
name|String
name|serverClassName
init|=
name|intf
operator|.
name|getPackageName
argument_list|()
operator|+
literal|"."
operator|+
name|interfaceName
operator|+
literal|"_"
operator|+
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
name|jp
operator|.
name|getPortName
argument_list|()
argument_list|)
operator|+
literal|"_Server"
decl_stmt|;
name|String
name|clientTargetName
init|=
name|interfaceName
operator|+
literal|"Client"
decl_stmt|;
name|boolean
name|collison
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|clientClassNamesMap
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|clientTargetName
argument_list|)
condition|)
block|{
name|clientTargetName
operator|=
name|clientTargetName
operator|+
name|index
expr_stmt|;
name|collison
operator|=
literal|true
expr_stmt|;
block|}
name|String
name|serverTargetName
init|=
name|interfaceName
operator|+
literal|"Server"
decl_stmt|;
if|if
condition|(
name|serverClassNamesMap
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|serverTargetName
argument_list|)
condition|)
block|{
name|serverTargetName
operator|=
name|serverTargetName
operator|+
name|index
expr_stmt|;
name|collison
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|collison
condition|)
block|{
name|index
operator|++
expr_stmt|;
block|}
name|clientClassNamesMap
operator|.
name|put
argument_list|(
name|clientTargetName
argument_list|,
name|clientClassName
argument_list|)
expr_stmt|;
name|serverClassNamesMap
operator|.
name|put
argument_list|(
name|serverTargetName
argument_list|,
name|serverClassName
argument_list|)
expr_stmt|;
block|}
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
literal|"wsdlLocation"
argument_list|,
name|javaModel
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|ANT_TEMPLATE
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
block|}
end_class

end_unit


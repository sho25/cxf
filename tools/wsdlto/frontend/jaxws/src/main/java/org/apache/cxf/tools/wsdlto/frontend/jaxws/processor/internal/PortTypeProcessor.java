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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|InterfaceInfo
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
name|OperationInfo
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
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBinding
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
name|internal
operator|.
name|mapper
operator|.
name|InterfaceMapper
import|;
end_import

begin_class
specifier|public
class|class
name|PortTypeProcessor
extends|extends
name|AbstractProcessor
block|{
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|operationMap
init|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|PortTypeProcessor
parameter_list|(
name|ToolContext
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JavaInterface
name|getInterface
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|InterfaceInfo
name|interfaceInfo
parameter_list|)
throws|throws
name|ToolException
block|{
name|JavaInterface
name|intf
init|=
name|interfaceInfo
operator|.
name|getProperty
argument_list|(
literal|"JavaInterface"
argument_list|,
name|JavaInterface
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|intf
operator|==
literal|null
condition|)
block|{
name|intf
operator|=
operator|new
name|InterfaceMapper
argument_list|(
name|context
argument_list|)
operator|.
name|map
argument_list|(
name|interfaceInfo
argument_list|)
expr_stmt|;
name|JAXWSBinding
name|jaxwsBinding
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|serviceInfo
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jaxwsBinding
operator|=
name|serviceInfo
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|JAXWSBinding
name|infBinding
init|=
name|interfaceInfo
operator|.
name|getExtensor
argument_list|(
name|JAXWSBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|infBinding
operator|!=
literal|null
operator|&&
name|infBinding
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|intf
operator|.
name|setPackageName
argument_list|(
name|infBinding
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jaxwsBinding
operator|!=
literal|null
operator|&&
name|jaxwsBinding
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|intf
operator|.
name|setPackageName
argument_list|(
name|jaxwsBinding
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|infBinding
operator|!=
literal|null
operator|&&
operator|!
name|infBinding
operator|.
name|getPackageJavaDoc
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|intf
operator|.
name|setPackageJavaDoc
argument_list|(
name|infBinding
operator|.
name|getPackageJavaDoc
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jaxwsBinding
operator|!=
literal|null
operator|&&
operator|!
name|jaxwsBinding
operator|.
name|getPackageJavaDoc
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|intf
operator|.
name|setPackageJavaDoc
argument_list|(
name|jaxwsBinding
operator|.
name|getPackageJavaDoc
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|name
init|=
name|intf
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|infBinding
operator|!=
literal|null
operator|&&
name|infBinding
operator|.
name|getJaxwsClass
argument_list|()
operator|!=
literal|null
operator|&&
name|infBinding
operator|.
name|getJaxwsClass
argument_list|()
operator|.
name|getClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|infBinding
operator|.
name|getJaxwsClass
argument_list|()
operator|.
name|getClassName
argument_list|()
expr_stmt|;
name|intf
operator|.
name|setClassJavaDoc
argument_list|(
name|infBinding
operator|.
name|getJaxwsClass
argument_list|()
operator|.
name|getComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClassCollector
name|collector
init|=
name|context
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_AUTORESOLVE
argument_list|)
condition|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|String
name|checkName
init|=
name|name
decl_stmt|;
while|while
condition|(
name|collector
operator|.
name|isReserved
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|checkName
argument_list|)
condition|)
block|{
name|checkName
operator|=
name|name
operator|+
literal|"_"
operator|+
operator|(
operator|++
name|count
operator|)
expr_stmt|;
block|}
name|name
operator|=
name|checkName
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|collector
operator|.
name|isReserved
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"RESERVED_SEI_NAME"
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
throw|;
block|}
name|interfaceInfo
operator|.
name|setProperty
argument_list|(
literal|"InterfaceName"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|intf
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|collector
operator|.
name|addSeiClassName
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|intf
operator|.
name|getName
argument_list|()
argument_list|,
name|intf
operator|.
name|getPackageName
argument_list|()
operator|+
literal|"."
operator|+
name|intf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|interfaceInfo
operator|.
name|setProperty
argument_list|(
literal|"JavaInterface"
argument_list|,
name|intf
argument_list|)
expr_stmt|;
block|}
return|return
name|intf
return|;
block|}
specifier|public
name|void
name|processClassNames
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|ToolException
block|{
name|InterfaceInfo
name|interfaceInfo
init|=
name|serviceInfo
operator|.
name|getInterface
argument_list|()
decl_stmt|;
if|if
condition|(
name|interfaceInfo
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|getInterface
argument_list|(
name|serviceInfo
argument_list|,
name|interfaceInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|process
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|ToolException
block|{
name|operationMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|JavaModel
name|jmodel
init|=
name|context
operator|.
name|get
argument_list|(
name|JavaModel
operator|.
name|class
argument_list|)
decl_stmt|;
name|InterfaceInfo
name|interfaceInfo
init|=
name|serviceInfo
operator|.
name|getInterface
argument_list|()
decl_stmt|;
if|if
condition|(
name|interfaceInfo
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|JavaInterface
name|intf
init|=
name|getInterface
argument_list|(
name|serviceInfo
argument_list|,
name|interfaceInfo
argument_list|)
decl_stmt|;
name|intf
operator|.
name|setJavaModel
argument_list|(
name|jmodel
argument_list|)
expr_stmt|;
name|Element
name|handler
init|=
operator|(
name|Element
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|HANDLER_CHAIN
argument_list|)
decl_stmt|;
name|intf
operator|.
name|setHandlerChains
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|OperationInfo
argument_list|>
name|operations
init|=
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationInfo
name|operation
range|:
name|operations
control|)
block|{
if|if
condition|(
name|isOverloading
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SKIP_OVERLOADED_OPERATION"
argument_list|,
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|OperationProcessor
name|operationProcessor
init|=
operator|new
name|OperationProcessor
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|operationProcessor
operator|.
name|process
argument_list|(
name|intf
argument_list|,
name|operation
argument_list|)
expr_stmt|;
block|}
name|jmodel
operator|.
name|setLocation
argument_list|(
name|intf
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|jmodel
operator|.
name|addInterface
argument_list|(
name|intf
operator|.
name|getName
argument_list|()
argument_list|,
name|intf
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isOverloading
parameter_list|(
name|QName
name|operationName
parameter_list|)
block|{
if|if
condition|(
name|operationMap
operator|.
name|contains
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|operationMap
operator|.
name|add
argument_list|(
name|operationName
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit


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
name|ws
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|UnknownExtensibilityElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|Attr
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
name|Document
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|annotations
operator|.
name|Policies
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
name|annotations
operator|.
name|Policy
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|endpoint
operator|.
name|Endpoint
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
name|helpers
operator|.
name|DOMUtils
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
name|resource
operator|.
name|ExtendedURIResolver
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
name|factory
operator|.
name|AbstractServiceFactoryBean
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
name|factory
operator|.
name|FactoryBeanListener
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
name|AbstractPropertiesHolder
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
name|BindingFaultInfo
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|DescriptionInfo
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
name|FaultInfo
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PolicyAnnotationListener
implements|implements
name|FactoryBeanListener
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
name|PolicyAnnotationListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXTRA_POLICIES
init|=
name|PolicyAnnotationListener
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".EXTRA_POLICIES"
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|PolicyAnnotationListener
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|void
name|handleEvent
parameter_list|(
name|Event
name|ev
parameter_list|,
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
switch|switch
condition|(
name|ev
condition|)
block|{
case|case
name|INTERFACE_CREATED
case|:
block|{
name|InterfaceInfo
name|ii
init|=
operator|(
name|InterfaceInfo
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|ii
argument_list|,
name|cls
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|ENDPOINT_SELECTED
case|:
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|2
index|]
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|implCls
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|3
index|]
decl_stmt|;
name|Endpoint
name|ep
init|=
operator|(
name|Endpoint
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|,
name|cls
argument_list|)
expr_stmt|;
comment|// this will allow us to support annotations in Implementations, but only for
comment|// class level annotations.  Method level annotations are not currently supported
comment|// for implementations.  The call has been moved here so that the ServiceInfo
comment|// policy stuff is loaded before jaxws factory calls the PolicyEngineImpl
name|addEndpointImplPolicies
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|,
name|implCls
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
name|INTERFACE_OPERATION_BOUND
case|:
block|{
name|OperationInfo
name|inf
init|=
operator|(
name|OperationInfo
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|Method
name|m
init|=
operator|(
name|Method
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|inf
argument_list|,
name|m
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|BINDING_OPERATION_CREATED
case|:
name|BindingOperationInfo
name|boi
init|=
operator|(
name|BindingOperationInfo
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|Method
name|m
init|=
operator|(
name|Method
operator|)
name|args
index|[
literal|2
index|]
decl_stmt|;
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|//ignore
block|}
block|}
specifier|private
name|void
name|addPolicies
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|OperationInfo
name|inf
parameter_list|,
name|Method
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Policy
name|p
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policies
name|ps
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|Policies
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
operator|||
name|ps
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Policy
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ps
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ListIterator
argument_list|<
name|Policy
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|p
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|Policy
operator|.
name|Placement
name|place
init|=
name|p
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|place
operator|==
name|Policy
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|place
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|BINDING_OPERATION
expr_stmt|;
block|}
name|ServiceInfo
name|service
init|=
name|inf
operator|.
name|getInterface
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|m
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|place
condition|)
block|{
case|case
name|PORT_TYPE_OPERATION
case|:
name|addPolicy
argument_list|(
name|inf
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|inf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypeOpPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
case|case
name|PORT_TYPE_OPERATION_INPUT
case|:
name|addPolicy
argument_list|(
name|inf
operator|.
name|getInput
argument_list|()
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|inf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypeOpInputPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
case|case
name|PORT_TYPE_OPERATION_OUTPUT
case|:
name|addPolicy
argument_list|(
name|inf
operator|.
name|getOutput
argument_list|()
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|inf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypeOpOutputPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
case|case
name|PORT_TYPE_OPERATION_FAULT
case|:
block|{
for|for
control|(
name|FaultInfo
name|f
range|:
name|inf
operator|.
name|getFaults
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|faultClass
argument_list|()
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|addPolicy
argument_list|(
name|f
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|f
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypeOpFaultPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
break|break;
block|}
default|default:
comment|//nothing
block|}
block|}
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|stuff
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|inf
operator|.
name|getProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|stuff
operator|!=
literal|null
condition|)
block|{
name|stuff
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inf
operator|.
name|setProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|addPolicies
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Endpoint
name|ep
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|removeProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|,
name|cls
argument_list|,
name|list
argument_list|,
name|Policy
operator|.
name|Placement
operator|.
name|BINDING
argument_list|)
expr_stmt|;
block|}
name|ServiceInfo
name|service
init|=
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|binfo
range|:
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|later
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|binfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|removeProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|later
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Policy
name|p
range|:
name|later
control|)
block|{
switch|switch
condition|(
name|p
operator|.
name|placement
argument_list|()
condition|)
block|{
case|case
name|DEFAULT
case|:
case|case
name|BINDING_OPERATION
case|:
name|addPolicy
argument_list|(
name|binfo
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|binfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"BindingOpPolicy"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING_OPERATION_INPUT
case|:
name|addPolicy
argument_list|(
name|binfo
operator|.
name|getInput
argument_list|()
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|binfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"BindingOpInputPolicy"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING_OPERATION_OUTPUT
case|:
name|addPolicy
argument_list|(
name|binfo
operator|.
name|getOutput
argument_list|()
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|binfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"BindingOpOutputPolicy"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING_OPERATION_FAULT
case|:
block|{
for|for
control|(
name|BindingFaultInfo
name|f
range|:
name|binfo
operator|.
name|getFaults
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|faultClass
argument_list|()
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|addPolicy
argument_list|(
name|f
argument_list|,
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|f
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"BindingOpFaultPolicy"
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
block|}
default|default:
comment|//nothing
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|addEndpointImplPolicies
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|removeProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|endpoint
argument_list|,
name|cls
argument_list|,
name|list
argument_list|,
name|Policy
operator|.
name|Placement
operator|.
name|BINDING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Policy
name|p
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policies
name|ps
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Policies
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
operator|||
name|ps
operator|!=
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|Policy
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ps
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|addPolicies
argument_list|(
name|factory
argument_list|,
name|endpoint
argument_list|,
name|cls
argument_list|,
name|list
argument_list|,
name|Policy
operator|.
name|Placement
operator|.
name|SERVICE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addPolicies
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|Policy
argument_list|>
name|list
parameter_list|,
name|Policy
operator|.
name|Placement
name|defaultPlace
parameter_list|)
block|{
name|ListIterator
argument_list|<
name|Policy
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|inf
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|BindingInfo
name|binf
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Policy
name|p
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Policy
operator|.
name|Placement
name|place
init|=
name|p
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|place
operator|==
name|Policy
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|place
operator|=
name|defaultPlace
expr_stmt|;
block|}
switch|switch
condition|(
name|place
condition|)
block|{
case|case
name|PORT_TYPE
case|:
block|{
name|addPolicy
argument_list|(
name|inf
argument_list|,
name|si
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|inf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypePolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|BINDING
case|:
block|{
name|addPolicy
argument_list|(
name|binf
argument_list|,
name|si
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|binf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"BindingPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|SERVICE
case|:
block|{
name|addPolicy
argument_list|(
name|si
argument_list|,
name|si
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|si
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"ServicePolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|SERVICE_PORT
case|:
block|{
name|addPolicy
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|si
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortPolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
default|default:
block|}
block|}
block|}
specifier|private
name|void
name|addPolicies
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|InterfaceInfo
name|ii
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Policy
name|p
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policies
name|ps
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Policies
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
operator|||
name|ps
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Policy
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ps
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ps
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ListIterator
argument_list|<
name|Policy
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|p
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|Policy
operator|.
name|Placement
name|place
init|=
name|p
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|place
operator|==
name|Policy
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|place
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|BINDING
expr_stmt|;
block|}
switch|switch
condition|(
name|place
condition|)
block|{
case|case
name|PORT_TYPE
case|:
block|{
name|addPolicy
argument_list|(
name|ii
argument_list|,
name|ii
operator|.
name|getService
argument_list|()
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|ii
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"PortTypePolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|SERVICE
case|:
block|{
name|addPolicy
argument_list|(
name|ii
operator|.
name|getService
argument_list|()
argument_list|,
name|ii
operator|.
name|getService
argument_list|()
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|ii
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"ServicePolicy"
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
default|default:
block|}
block|}
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Policy
argument_list|>
name|stuff
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ii
operator|.
name|getProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|stuff
operator|!=
literal|null
condition|)
block|{
name|stuff
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ii
operator|.
name|setProperty
argument_list|(
name|EXTRA_POLICIES
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|addPolicy
parameter_list|(
name|AbstractPropertiesHolder
name|place
parameter_list|,
name|ServiceInfo
name|service
parameter_list|,
name|Policy
name|p
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|defName
parameter_list|)
block|{
name|Element
name|el
init|=
name|addPolicy
argument_list|(
name|service
argument_list|,
name|p
argument_list|,
name|cls
argument_list|,
name|defName
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|UnknownExtensibilityElement
name|uee
init|=
operator|new
name|UnknownExtensibilityElement
argument_list|()
decl_stmt|;
name|uee
operator|.
name|setElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setRequired
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setElementType
argument_list|(
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|el
argument_list|)
argument_list|)
expr_stmt|;
name|place
operator|.
name|addExtensor
argument_list|(
name|uee
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Element
name|addPolicy
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|Policy
name|p
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|defName
parameter_list|)
block|{
name|String
name|uri
init|=
name|p
operator|.
name|uri
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|Constants
operator|.
name|URI_POLICY_NS
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|includeInWSDL
argument_list|()
condition|)
block|{
name|Element
name|element
init|=
name|loadPolicy
argument_list|(
name|uri
argument_list|,
name|defName
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// might have been updated on load policy
name|uri
operator|=
name|getPolicyId
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|ns
operator|=
name|element
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
if|if
condition|(
name|service
operator|.
name|getDescription
argument_list|()
operator|==
literal|null
operator|&&
name|cls
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|setDescription
argument_list|(
operator|new
name|DescriptionInfo
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|u
init|=
name|cls
operator|.
name|getResource
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|getDescription
argument_list|()
operator|.
name|setBaseURI
argument_list|(
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if not already added to service add it, otherwise ignore
comment|// and just create the policy reference.
if|if
condition|(
operator|!
name|isExistsPolicy
argument_list|(
name|service
argument_list|,
name|uri
argument_list|)
condition|)
block|{
name|UnknownExtensibilityElement
name|uee
init|=
operator|new
name|UnknownExtensibilityElement
argument_list|()
decl_stmt|;
name|uee
operator|.
name|setElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setRequired
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setElementType
argument_list|(
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|getDescription
argument_list|()
operator|.
name|addExtensor
argument_list|(
name|uee
argument_list|)
expr_stmt|;
block|}
name|uri
operator|=
literal|"#"
operator|+
name|uri
expr_stmt|;
block|}
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|ns
argument_list|,
literal|"wsp:"
operator|+
name|Constants
operator|.
name|ELEM_POLICY_REF
argument_list|)
decl_stmt|;
name|Attr
name|att
init|=
name|doc
operator|.
name|createAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"URI"
argument_list|)
decl_stmt|;
name|att
operator|.
name|setValue
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|el
operator|.
name|setAttributeNodeNS
argument_list|(
name|att
argument_list|)
expr_stmt|;
return|return
name|el
return|;
block|}
specifier|private
name|String
name|getPolicyId
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|element
operator|.
name|getAttributeNS
argument_list|(
name|PolicyConstants
operator|.
name|WSU_NAMESPACE_URI
argument_list|,
name|PolicyConstants
operator|.
name|WSU_ID_ATTR_NAME
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isExistsPolicy
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|Object
name|exts
index|[]
init|=
name|service
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensors
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|exts
operator|=
name|exts
operator|==
literal|null
condition|?
operator|new
name|Object
index|[
literal|0
index|]
else|:
name|exts
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|exts
control|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|UnknownExtensibilityElement
condition|)
block|{
name|UnknownExtensibilityElement
name|uee
init|=
operator|(
name|UnknownExtensibilityElement
operator|)
name|o
decl_stmt|;
name|String
name|uri2
init|=
name|getPolicyId
argument_list|(
name|uee
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|uri2
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|Element
name|loadPolicy
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|defName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|uri
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
return|return
name|loadRemotePolicy
argument_list|(
name|uri
argument_list|,
name|defName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|loadLocalPolicy
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
specifier|private
name|Element
name|loadRemotePolicy
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|defName
parameter_list|)
block|{
name|ExtendedURIResolver
name|resolver
init|=
operator|new
name|ExtendedURIResolver
argument_list|()
decl_stmt|;
name|InputSource
name|src
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|uri
argument_list|,
literal|"classpath:"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|src
condition|)
block|{
return|return
literal|null
return|;
block|}
name|XMLStreamReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|uri
operator|=
name|getPolicyId
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|Attr
name|att
init|=
name|doc
operator|.
name|createAttributeNS
argument_list|(
name|PolicyConstants
operator|.
name|WSU_NAMESPACE_URI
argument_list|,
literal|"wsu:"
operator|+
name|PolicyConstants
operator|.
name|WSU_ID_ATTR_NAME
argument_list|)
decl_stmt|;
name|att
operator|.
name|setNodeValue
argument_list|(
name|defName
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|setAttributeNodeNS
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
finally|finally
block|{
try|try
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Element
name|loadLocalPolicy
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|PolicyBean
name|pb
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
operator|.
name|getBeanOfType
argument_list|(
name|uri
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|,
name|PolicyBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|pb
condition|)
block|{
return|return
name|pb
operator|.
name|getElement
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit


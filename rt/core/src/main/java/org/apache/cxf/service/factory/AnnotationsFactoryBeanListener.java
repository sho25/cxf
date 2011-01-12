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
name|service
operator|.
name|factory
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
name|DataBinding
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
name|EndpointProperties
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
name|EndpointProperty
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
name|FactoryType
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
name|FastInfoset
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
name|GZIP
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
name|Logging
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
name|SchemaValidation
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
name|WSDLDocumentation
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
name|WSDLDocumentation
operator|.
name|Placement
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
name|WSDLDocumentationCollection
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|endpoint
operator|.
name|Server
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
name|feature
operator|.
name|AbstractFeature
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
name|feature
operator|.
name|LoggingFeature
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
name|interceptor
operator|.
name|FIStaxInInterceptor
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
name|interceptor
operator|.
name|FIStaxOutInterceptor
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
name|interceptor
operator|.
name|InterceptorProvider
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
name|message
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
name|resource
operator|.
name|ResourceManager
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
name|invoker
operator|.
name|Factory
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
name|invoker
operator|.
name|FactoryInvoker
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
name|invoker
operator|.
name|Invoker
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
name|invoker
operator|.
name|PerRequestFactory
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
name|invoker
operator|.
name|PooledFactory
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
name|invoker
operator|.
name|SessionFactory
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
name|invoker
operator|.
name|SingletonFactory
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
name|invoker
operator|.
name|SpringBeanFactory
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AnnotationsFactoryBeanListener
implements|implements
name|FactoryBeanListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EXTRA_DOCUMENTATION
init|=
name|AnnotationsFactoryBeanListener
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".EXTRA_DOCS"
decl_stmt|;
comment|/** {@inheritDoc}*/
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
name|WSDLDocumentation
name|doc
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|ii
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
name|WSDLDocumentationCollection
name|col
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentationCollection
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|ii
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE
argument_list|,
name|col
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setDataBinding
argument_list|(
name|factory
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|DataBinding
operator|.
name|class
argument_list|)
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
name|implCls
init|=
name|args
operator|.
name|length
operator|>
literal|3
condition|?
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
else|:
literal|null
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
literal|2
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
name|Bus
name|bus
init|=
name|factory
operator|.
name|getBus
argument_list|()
decl_stmt|;
comment|// To avoid the NPE
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|addSchemaValidationSupport
argument_list|(
name|ep
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|SchemaValidation
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addFastInfosetSupport
argument_list|(
name|ep
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|FastInfoset
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addGZipSupport
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|GZIP
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addLoggingSupport
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Logging
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addEndpointProperties
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|EndpointProperty
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointProperties
name|props
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|EndpointProperties
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|addEndpointProperties
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|,
name|props
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// To avoid the NPE
if|if
condition|(
name|implCls
operator|==
literal|null
operator|||
name|implCls
operator|==
name|cls
condition|)
block|{
return|return;
block|}
name|WSDLDocumentation
name|doc
init|=
name|implCls
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|ep
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|SERVICE
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
name|WSDLDocumentationCollection
name|col
init|=
name|implCls
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentationCollection
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|ep
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|SERVICE
argument_list|,
name|col
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|InterfaceInfo
name|i
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSDLDocumentation
argument_list|>
name|docs
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
name|i
operator|.
name|removeProperty
argument_list|(
name|EXTRA_DOCUMENTATION
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|docs
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|ep
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|SERVICE
argument_list|,
name|docs
operator|.
name|toArray
argument_list|(
operator|new
name|WSDLDocumentation
index|[
name|docs
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|addBindingOperationDocs
argument_list|(
name|ep
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|SERVER_CREATED
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
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Server
name|server
init|=
operator|(
name|Server
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|addGZipSupport
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|GZIP
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addSchemaValidationSupport
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|SchemaValidation
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addFastInfosetSupport
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|FastInfoset
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addLoggingSupport
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Logging
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addEndpointProperties
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|EndpointProperty
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointProperties
name|props
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|EndpointProperties
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|addEndpointProperties
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|,
name|props
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setScope
argument_list|(
name|factory
argument_list|,
name|server
argument_list|,
name|cls
argument_list|)
expr_stmt|;
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
name|WSDLDocumentation
name|doc
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|inf
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
name|WSDLDocumentationCollection
name|col
init|=
name|m
operator|.
name|getAnnotation
argument_list|(
name|WSDLDocumentationCollection
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|!=
literal|null
condition|)
block|{
name|addDocumentation
argument_list|(
name|inf
argument_list|,
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION
argument_list|,
name|col
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
default|default:
comment|//do nothing
block|}
block|}
specifier|private
name|void
name|setScope
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Server
name|server
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|FactoryType
name|scope
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|FactoryType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|Invoker
name|i
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInvoker
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|FactoryInvoker
condition|)
block|{
name|Factory
name|f
decl_stmt|;
if|if
condition|(
name|scope
operator|.
name|factoryClass
argument_list|()
operator|==
name|FactoryType
operator|.
name|DEFAULT
operator|.
name|class
condition|)
block|{
switch|switch
condition|(
name|scope
operator|.
name|value
argument_list|()
condition|)
block|{
case|case
name|Session
case|:
if|if
condition|(
name|scope
operator|.
name|args
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|f
operator|=
operator|new
name|SessionFactory
argument_list|(
name|cls
argument_list|,
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|scope
operator|.
name|args
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|=
operator|new
name|SessionFactory
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|PerRequest
case|:
name|f
operator|=
operator|new
name|PerRequestFactory
argument_list|(
name|cls
argument_list|)
expr_stmt|;
break|break;
case|case
name|Pooled
case|:
name|f
operator|=
operator|new
name|PooledFactory
argument_list|(
name|cls
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|scope
operator|.
name|args
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Spring
case|:
name|f
operator|=
operator|new
name|SpringBeanFactory
argument_list|(
name|scope
operator|.
name|args
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
break|break;
default|default:
name|f
operator|=
operator|new
name|SingletonFactory
argument_list|(
name|cls
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
try|try
block|{
name|f
operator|=
operator|(
name|Factory
operator|)
name|scope
operator|.
name|factoryClass
argument_list|()
operator|.
name|getConstructor
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|cls
argument_list|,
name|scope
operator|.
name|args
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
operator|(
operator|(
name|FactoryInvoker
operator|)
name|i
operator|)
operator|.
name|setFactory
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addEndpointProperties
parameter_list|(
name|Endpoint
name|ep
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|EndpointProperty
modifier|...
name|annotations
parameter_list|)
block|{
for|for
control|(
name|EndpointProperty
name|prop
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|s
index|[]
init|=
name|prop
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|prop
operator|.
name|key
argument_list|()
argument_list|,
name|s
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|prop
operator|.
name|key
argument_list|()
argument_list|,
name|s
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|setDataBinding
parameter_list|(
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|DataBinding
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
operator|&&
name|factory
operator|.
name|getDataBinding
argument_list|(
literal|false
argument_list|)
operator|==
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|annotation
operator|.
name|ref
argument_list|()
argument_list|)
condition|)
block|{
name|factory
operator|.
name|setDataBinding
argument_list|(
name|factory
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
operator|.
name|resolveResource
argument_list|(
name|annotation
operator|.
name|ref
argument_list|()
argument_list|,
name|annotation
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setDataBinding
argument_list|(
name|annotation
operator|.
name|value
argument_list|()
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//REVISIT - log a warning
block|}
block|}
block|}
specifier|private
name|void
name|addLoggingSupport
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|Logging
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|LoggingFeature
name|lf
init|=
operator|new
name|LoggingFeature
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
name|lf
operator|.
name|initialize
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addGZipSupport
parameter_list|(
name|Endpoint
name|ep
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|GZIP
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.transport.common.gzip.GZIPFeature"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|AbstractFeature
name|feature
init|=
operator|(
name|AbstractFeature
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|cls
operator|.
name|getMethod
argument_list|(
literal|"setThreshold"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Integer
operator|.
name|TYPE
block|}
argument_list|)
operator|.
name|invoke
argument_list|(
name|feature
argument_list|,
name|annotation
operator|.
name|threshold
argument_list|()
argument_list|)
expr_stmt|;
name|feature
operator|.
name|initialize
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore - just assume it's an unsupported/unknown annotation
block|}
block|}
block|}
specifier|private
name|void
name|addSchemaValidationSupport
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|SchemaValidation
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|annotation
operator|.
name|enabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addFastInfosetSupport
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|FastInfoset
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|FIStaxInInterceptor
name|in
init|=
operator|new
name|FIStaxInInterceptor
argument_list|()
decl_stmt|;
name|FIStaxOutInterceptor
name|out
init|=
operator|new
name|FIStaxOutInterceptor
argument_list|(
name|annotation
operator|.
name|force
argument_list|()
argument_list|)
decl_stmt|;
name|provider
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|provider
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addBindingOperationDocs
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
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
name|WSDLDocumentation
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
name|getProperty
argument_list|(
name|EXTRA_DOCUMENTATION
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
name|WSDLDocumentation
name|doc
range|:
name|later
control|)
block|{
switch|switch
condition|(
name|doc
operator|.
name|placement
argument_list|()
condition|)
block|{
case|case
name|BINDING_OPERATION
case|:
name|binfo
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING_OPERATION_INPUT
case|:
name|binfo
operator|.
name|getInput
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING_OPERATION_OUTPUT
case|:
name|binfo
operator|.
name|getOutput
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
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
name|doc
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
name|f
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
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
name|addDocumentation
parameter_list|(
name|OperationInfo
name|inf
parameter_list|,
name|Placement
name|defPlace
parameter_list|,
name|WSDLDocumentation
modifier|...
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|WSDLDocumentation
argument_list|>
name|later
init|=
operator|new
name|ArrayList
argument_list|<
name|WSDLDocumentation
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WSDLDocumentation
name|doc
range|:
name|values
control|)
block|{
name|WSDLDocumentation
operator|.
name|Placement
name|p
init|=
name|doc
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|p
operator|=
name|defPlace
expr_stmt|;
block|}
switch|switch
condition|(
name|p
condition|)
block|{
case|case
name|PORT_TYPE_OPERATION
case|:
name|inf
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|PORT_TYPE_OPERATION_INPUT
case|:
name|inf
operator|.
name|getInput
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|PORT_TYPE_OPERATION_OUTPUT
case|:
name|inf
operator|.
name|getOutput
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|FAULT_MESSAGE
case|:
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
name|doc
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
if|if
condition|(
name|p
operator|==
name|Placement
operator|.
name|FAULT_MESSAGE
condition|)
block|{
name|f
operator|.
name|setMessageDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
break|break;
block|}
case|case
name|INPUT_MESSAGE
case|:
name|inf
operator|.
name|getInput
argument_list|()
operator|.
name|setMessageDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|OUTPUT_MESSAGE
case|:
name|inf
operator|.
name|getOutput
argument_list|()
operator|.
name|setMessageDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|later
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|later
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|WSDLDocumentation
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
name|EXTRA_DOCUMENTATION
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
name|later
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inf
operator|.
name|setProperty
argument_list|(
name|EXTRA_DOCUMENTATION
argument_list|,
name|later
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addDocumentation
parameter_list|(
name|InterfaceInfo
name|interfaceInfo
parameter_list|,
name|WSDLDocumentation
operator|.
name|Placement
name|defPlace
parameter_list|,
name|WSDLDocumentation
modifier|...
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|WSDLDocumentation
argument_list|>
name|later
init|=
operator|new
name|ArrayList
argument_list|<
name|WSDLDocumentation
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WSDLDocumentation
name|doc
range|:
name|values
control|)
block|{
name|WSDLDocumentation
operator|.
name|Placement
name|p
init|=
name|doc
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|p
operator|=
name|defPlace
expr_stmt|;
block|}
switch|switch
condition|(
name|p
condition|)
block|{
case|case
name|PORT_TYPE
case|:
name|interfaceInfo
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SERVICE
case|:
name|interfaceInfo
operator|.
name|getService
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TOP
case|:
name|interfaceInfo
operator|.
name|getService
argument_list|()
operator|.
name|setTopLevelDoc
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|later
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|later
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|WSDLDocumentation
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
name|interfaceInfo
operator|.
name|getProperty
argument_list|(
name|EXTRA_DOCUMENTATION
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
name|later
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|interfaceInfo
operator|.
name|setProperty
argument_list|(
name|EXTRA_DOCUMENTATION
argument_list|,
name|later
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addDocumentation
parameter_list|(
name|Endpoint
name|ep
parameter_list|,
name|WSDLDocumentation
operator|.
name|Placement
name|defPlace
parameter_list|,
name|WSDLDocumentation
modifier|...
name|values
parameter_list|)
block|{
for|for
control|(
name|WSDLDocumentation
name|doc
range|:
name|values
control|)
block|{
name|WSDLDocumentation
operator|.
name|Placement
name|p
init|=
name|doc
operator|.
name|placement
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|DEFAULT
condition|)
block|{
name|p
operator|=
name|defPlace
expr_stmt|;
block|}
switch|switch
condition|(
name|p
condition|)
block|{
case|case
name|PORT_TYPE
case|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TOP
case|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|setTopLevelDoc
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SERVICE
case|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SERVICE_PORT
case|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|BINDING
case|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|setDocumentation
argument_list|(
name|doc
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|//nothing?
block|}
block|}
block|}
block|}
end_class

end_unit


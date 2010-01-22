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
name|binding
operator|.
name|http
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
name|binding
operator|.
name|AbstractBindingFactory
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
name|binding
operator|.
name|Binding
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
name|binding
operator|.
name|http
operator|.
name|interceptor
operator|.
name|ContentTypeOutInterceptor
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
name|binding
operator|.
name|http
operator|.
name|interceptor
operator|.
name|DatabindingInSetupInterceptor
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
name|binding
operator|.
name|http
operator|.
name|interceptor
operator|.
name|DatabindingOutSetupInterceptor
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
name|binding
operator|.
name|http
operator|.
name|strategy
operator|.
name|ConventionStrategy
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
name|binding
operator|.
name|http
operator|.
name|strategy
operator|.
name|JRAStrategy
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
name|binding
operator|.
name|http
operator|.
name|strategy
operator|.
name|ResourceStrategy
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
name|binding
operator|.
name|xml
operator|.
name|XMLBinding
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
name|binding
operator|.
name|xml
operator|.
name|interceptor
operator|.
name|XMLFaultInInterceptor
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
name|binding
operator|.
name|xml
operator|.
name|interceptor
operator|.
name|XMLFaultOutInterceptor
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|frontend
operator|.
name|MethodDispatcher
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
name|AttachmentInInterceptor
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
name|AttachmentOutInterceptor
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
name|StaxOutInterceptor
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
name|Service
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
name|ReflectionServiceFactoryBean
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
name|ServiceConstructionException
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

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
block|{
literal|"bus"
block|}
argument_list|)
specifier|public
class|class
name|HttpBindingFactory
extends|extends
name|AbstractBindingFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_BINDING_ID
init|=
literal|"http://apache.org/cxf/binding/http"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ResourceStrategy
argument_list|>
name|strategies
init|=
operator|new
name|ArrayList
argument_list|<
name|ResourceStrategy
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|HttpBindingFactory
parameter_list|()
block|{
name|strategies
operator|.
name|add
argument_list|(
operator|new
name|JRAStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|strategies
operator|.
name|add
argument_list|(
operator|new
name|ConventionStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Binding
name|createBinding
parameter_list|(
name|BindingInfo
name|bi
parameter_list|)
block|{
name|XMLBinding
name|binding
init|=
operator|new
name|XMLBinding
argument_list|(
name|bi
argument_list|)
decl_stmt|;
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DatabindingInSetupInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ContentTypeOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DatabindingOutSetupInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLFaultInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ContentTypeOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLFaultOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|public
name|BindingInfo
name|createBindingInfo
parameter_list|(
name|Service
name|service
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
name|URIMapper
name|mapper
init|=
operator|new
name|URIMapper
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|BindingInfo
name|info
init|=
operator|new
name|BindingInfo
argument_list|(
name|si
argument_list|,
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
decl_stmt|;
name|info
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
name|si
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|si
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"HttpBinding"
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|put
argument_list|(
name|URIMapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|service
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|OperationInfo
name|o
range|:
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|info
operator|.
name|buildOperation
argument_list|(
name|o
operator|.
name|getName
argument_list|()
argument_list|,
name|o
operator|.
name|getInputName
argument_list|()
argument_list|,
name|o
operator|.
name|getOutputName
argument_list|()
argument_list|)
decl_stmt|;
name|info
operator|.
name|addOperation
argument_list|(
name|bop
argument_list|)
expr_stmt|;
name|Method
name|m
init|=
name|md
operator|.
name|getMethod
argument_list|(
name|bop
argument_list|)
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Class
operator|)
name|service
operator|.
name|get
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|ENDPOINT_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|m
operator|=
name|c
operator|.
name|getMethod
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// attempt to map the method to a resource using different strategies
for|for
control|(
name|ResourceStrategy
name|s
range|:
name|strategies
control|)
block|{
comment|// Try different ones until we find one that succeeds
if|if
condition|(
name|s
operator|.
name|map
argument_list|(
name|bop
argument_list|,
name|m
argument_list|,
name|mapper
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
block|}
return|return
name|info
return|;
block|}
specifier|public
name|List
argument_list|<
name|ResourceStrategy
argument_list|>
name|getStrategies
parameter_list|()
block|{
return|return
name|strategies
return|;
block|}
specifier|public
name|void
name|setStrategies
parameter_list|(
name|List
argument_list|<
name|ResourceStrategy
argument_list|>
name|strategies
parameter_list|)
block|{
name|this
operator|.
name|strategies
operator|=
name|strategies
expr_stmt|;
block|}
block|}
end_class

end_unit


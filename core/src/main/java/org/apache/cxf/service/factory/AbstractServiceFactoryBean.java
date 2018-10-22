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
name|io
operator|.
name|IOException
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|common
operator|.
name|util
operator|.
name|ModCountCopyOnWriteArrayList
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
name|databinding
operator|.
name|AbstractDataBinding
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
name|databinding
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
name|interceptor
operator|.
name|OneWayProcessorInterceptor
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
name|OutgoingChainInterceptor
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
name|ServiceInvokerInterceptor
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
name|resource
operator|.
name|URIResolver
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractServiceFactoryBean
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
name|AbstractServiceFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|boolean
name|dataBindingSet
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|List
argument_list|<
name|FactoryBeanListener
argument_list|>
name|listeners
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sessionState
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
specifier|abstract
name|Service
name|create
parameter_list|()
function_decl|;
comment|/**      * Returns a map that is useful for ServiceFactoryBeanListener to store state across      * events during processing.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getSessionState
parameter_list|()
block|{
return|return
name|sessionState
return|;
block|}
specifier|public
name|void
name|sendEvent
parameter_list|(
name|FactoryBeanListener
operator|.
name|Event
name|ev
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
for|for
control|(
name|FactoryBeanListener
name|l
range|:
name|listeners
control|)
block|{
name|l
operator|.
name|handleEvent
argument_list|(
name|ev
argument_list|,
name|this
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|initializeDefaultInterceptors
parameter_list|()
block|{
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ServiceInvokerInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|OutgoingChainInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|OneWayProcessorInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initializeDataBindings
parameter_list|()
block|{
if|if
condition|(
name|getDataBinding
argument_list|()
operator|instanceof
name|AbstractDataBinding
operator|&&
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|fillDataBindingSchemas
argument_list|()
expr_stmt|;
block|}
name|getDataBinding
argument_list|()
operator|.
name|initialize
argument_list|(
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|setDataBinding
argument_list|(
name|getDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|DATABINDING_INITIALIZED
argument_list|,
name|dataBinding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|setBus
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
name|FactoryBeanListenerManager
name|m
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|FactoryBeanListenerManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|listeners
operator|.
name|addAll
argument_list|(
name|m
operator|.
name|getListeners
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|()
block|{
return|return
name|getDataBinding
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
if|if
condition|(
name|dataBinding
operator|==
literal|null
operator|&&
name|create
condition|)
block|{
name|dataBinding
operator|=
name|createDefaultDataBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|dataBinding
return|;
block|}
specifier|protected
name|DataBinding
name|createDefaultDataBinding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|)
block|{
name|this
operator|.
name|dataBinding
operator|=
name|dataBinding
expr_stmt|;
name|this
operator|.
name|dataBindingSet
operator|=
name|dataBinding
operator|!=
literal|null
expr_stmt|;
block|}
specifier|public
name|Service
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|protected
name|void
name|setService
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
specifier|private
name|void
name|fillDataBindingSchemas
parameter_list|()
block|{
name|ResourceManager
name|rr
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DOMSource
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|l
range|:
name|schemaLocations
control|)
block|{
name|URL
name|url
init|=
name|rr
operator|.
name|resolveResource
argument_list|(
name|l
argument_list|,
name|URL
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|URIResolver
name|res
decl_stmt|;
try|try
block|{
name|res
operator|=
operator|new
name|URIResolver
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_SCHEMA_URL"
argument_list|,
name|LOG
argument_list|,
name|l
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|res
operator|.
name|isResolved
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_SCHEMA_URL"
argument_list|,
name|LOG
argument_list|,
name|l
argument_list|)
argument_list|)
throw|;
block|}
name|url
operator|=
name|res
operator|.
name|getURL
argument_list|()
expr_stmt|;
block|}
name|Document
name|d
decl_stmt|;
try|try
block|{
name|d
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|url
operator|.
name|openStream
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
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ERROR_READING_SCHEMA"
argument_list|,
name|LOG
argument_list|,
name|l
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|schemas
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|d
argument_list|,
name|url
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|AbstractDataBinding
operator|)
name|getDataBinding
argument_list|()
operator|)
operator|.
name|setSchemas
argument_list|(
name|schemas
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


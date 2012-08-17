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
name|endpoint
package|;
end_package

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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|BusException
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
name|BusFactory
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
name|BindingFactory
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
name|BindingFactoryManager
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
name|configuration
operator|.
name|Configurable
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
name|Feature
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
name|AbstractAttributedInterceptorProvider
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
name|ClientFaultConverter
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
name|InFaultChainInitiatorObserver
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
name|MessageSenderInterceptor
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
name|OutFaultChainInitiatorObserver
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
name|EndpointInfo
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
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_class
specifier|public
class|class
name|EndpointImpl
extends|extends
name|AbstractAttributedInterceptorProvider
implements|implements
name|Endpoint
implements|,
name|Configurable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7660560719050162091L
decl_stmt|;
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
name|EndpointImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|MessageObserver
name|inFaultObserver
decl_stmt|;
specifier|private
name|MessageObserver
name|outFaultObserver
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|activeFeatures
decl_stmt|;
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Service
name|s
parameter_list|,
name|QName
name|endpointName
parameter_list|)
throws|throws
name|EndpointException
block|{
name|this
argument_list|(
name|bus
argument_list|,
name|s
argument_list|,
name|s
operator|.
name|getEndpointInfo
argument_list|(
name|endpointName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Service
name|s
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|EndpointException
block|{
if|if
condition|(
name|ei
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"EndpointInfo can not be null!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
name|service
operator|=
name|s
expr_stmt|;
name|endpointInfo
operator|=
name|ei
expr_stmt|;
name|createBinding
argument_list|(
name|endpointInfo
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|inFaultObserver
operator|=
operator|new
name|InFaultChainInitiatorObserver
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|outFaultObserver
operator|=
operator|new
name|OutFaultChainInitiatorObserver
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ClientFaultConverter
argument_list|()
argument_list|)
expr_stmt|;
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageSenderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageSenderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".endpoint"
return|;
block|}
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|()
block|{
return|return
name|endpointInfo
return|;
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
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
operator|==
literal|null
condition|?
name|service
operator|.
name|getExecutor
argument_list|()
else|:
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|e
parameter_list|)
block|{
name|executor
operator|=
name|e
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
block|}
specifier|final
name|void
name|createBinding
parameter_list|(
name|BindingInfo
name|bi
parameter_list|)
throws|throws
name|EndpointException
block|{
if|if
condition|(
literal|null
operator|!=
name|bi
condition|)
block|{
name|String
name|namespace
init|=
name|bi
operator|.
name|getBindingId
argument_list|()
decl_stmt|;
name|BindingFactory
name|bf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bf
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getBindingFactory
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|bf
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"NO_BINDING_FACTORY"
argument_list|,
name|BUNDLE
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|EndpointException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|binding
operator|=
name|bf
operator|.
name|createBinding
argument_list|(
name|bi
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EndpointException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|MessageObserver
name|getInFaultObserver
parameter_list|()
block|{
return|return
name|inFaultObserver
return|;
block|}
specifier|public
name|MessageObserver
name|getOutFaultObserver
parameter_list|()
block|{
return|return
name|outFaultObserver
return|;
block|}
specifier|public
name|void
name|setInFaultObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|inFaultObserver
operator|=
name|observer
expr_stmt|;
block|}
specifier|public
name|void
name|setOutFaultObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|outFaultObserver
operator|=
name|observer
expr_stmt|;
block|}
comment|/**      * Utility method to make it easy to set properties from Spring.      *       * @param properties      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the list of features<b>already</b> activated for this endpoint.      */
specifier|public
name|List
argument_list|<
name|Feature
argument_list|>
name|getActiveFeatures
parameter_list|()
block|{
return|return
name|activeFeatures
return|;
block|}
comment|/**      * @param the list of features<b>already</b> activated for this endpoint.      */
specifier|public
name|void
name|initializeActiveFeatures
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
name|features
parameter_list|)
block|{
name|activeFeatures
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|features
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the hashCode bsed on the EndpointInfo so that this object      * can be used as a map key.      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|endpointInfo
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
name|bus
operator|.
name|managers
package|;
end_package

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
name|Set
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
name|ConcurrentHashMap
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
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|transport
operator|.
name|ConduitInitiator
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
name|ConduitInitiatorManager
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
name|TransportFinder
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
specifier|final
class|class
name|ConduitInitiatorManagerImpl
implements|implements
name|ConduitInitiatorManager
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|ConduitInitiatorManagerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ConduitInitiator
argument_list|>
name|conduitInitiators
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|failed
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|loaded
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|ConduitInitiatorManagerImpl
parameter_list|()
block|{
name|conduitInitiators
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|8
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConduitInitiatorManagerImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|conduitInitiators
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|8
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.cxf.bus.ConduitInitiatorManager#registerConduitInitiator(java.lang.String,      *      org.apache.cxf.transports.ConduitInitiator)      */
specifier|public
name|void
name|registerConduitInitiator
parameter_list|(
name|String
name|namespace
parameter_list|,
name|ConduitInitiator
name|factory
parameter_list|)
block|{
name|conduitInitiators
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.cxf.bus.ConduitInitiatorManager#deregisterConduitInitiator(java.lang.String)      */
specifier|public
name|void
name|deregisterConduitInitiator
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|conduitInitiators
operator|.
name|remove
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.cxf.bus.ConduitInitiatorManager#ConduitInitiator(java.lang.String)      */
comment|/**      * Returns the conduit initiator for the given namespace, constructing it      * (and storing in the cache for future reference) if necessary, using its      * list of factory classname to namespace mappings.      *      * @param namespace the namespace.      */
specifier|public
name|ConduitInitiator
name|getConduitInitiator
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|BusException
block|{
name|ConduitInitiator
name|factory
init|=
name|conduitInitiators
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
operator|&&
operator|!
name|failed
operator|.
name|contains
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|factory
operator|=
operator|new
name|TransportFinder
argument_list|<>
argument_list|(
name|bus
argument_list|,
name|conduitInitiators
argument_list|,
name|loaded
argument_list|,
name|ConduitInitiator
operator|.
name|class
argument_list|)
operator|.
name|findTransportForNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|failed
operator|.
name|add
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BusException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_CONDUIT_INITIATOR"
argument_list|,
name|BUNDLE
argument_list|,
name|namespace
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|factory
return|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// nothing to do
block|}
specifier|public
name|ConduitInitiator
name|getConduitInitiatorForUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|TransportFinder
argument_list|<
name|ConduitInitiator
argument_list|>
argument_list|(
name|bus
argument_list|,
name|conduitInitiators
argument_list|,
name|loaded
argument_list|,
name|ConduitInitiator
operator|.
name|class
argument_list|)
operator|.
name|findTransportForURI
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit


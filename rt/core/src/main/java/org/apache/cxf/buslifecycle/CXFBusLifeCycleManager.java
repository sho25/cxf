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
name|buslifecycle
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
class|class
name|CXFBusLifeCycleManager
implements|implements
name|BusLifeCycleManager
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|BusLifeCycleListener
argument_list|>
name|listeners
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|boolean
name|preShutdownCalled
decl_stmt|;
specifier|private
name|boolean
name|postShutdownCalled
decl_stmt|;
specifier|public
name|CXFBusLifeCycleManager
parameter_list|()
block|{
name|listeners
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|BusLifeCycleListener
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CXFBusLifeCycleManager
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|listeners
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|BusLifeCycleListener
argument_list|>
argument_list|()
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
specifier|final
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
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.cxf.buslifecycle.BusLifeCycleManager#registerLifeCycleListener(      * org.apache.cxf.buslifecycle.BusLifeCycleListener)      */
specifier|public
name|void
name|registerLifeCycleListener
parameter_list|(
name|BusLifeCycleListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.cxf.buslifecycle.BusLifeCycleManager#unregisterLifeCycleListener(      * org.apache.cxf.buslifecycle.BusLifeCycleListener)      */
specifier|public
name|void
name|unregisterLifeCycleListener
parameter_list|(
name|BusLifeCycleListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|preShutdownCalled
operator|=
literal|false
expr_stmt|;
name|postShutdownCalled
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|BusLifeCycleListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|initComplete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
comment|// TODO inverse order of registration?
if|if
condition|(
operator|!
name|preShutdownCalled
condition|)
block|{
name|preShutdownCalled
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|BusLifeCycleListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|preShutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
if|if
condition|(
operator|!
name|preShutdownCalled
condition|)
block|{
name|preShutdown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|postShutdownCalled
condition|)
block|{
name|postShutdownCalled
operator|=
literal|true
expr_stmt|;
comment|// TODO inverse order of registration?
for|for
control|(
name|BusLifeCycleListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


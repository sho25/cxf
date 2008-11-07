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
name|systest
operator|.
name|wsdl
package|;
end_package

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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|LifeCycleListenerTester
implements|implements
name|BusLifeCycleListener
block|{
specifier|static
name|int
name|initCount
decl_stmt|;
specifier|public
name|LifeCycleListenerTester
parameter_list|()
block|{     }
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"org.apache.cxf.buslifecycle.BusLifeCycleManager"
argument_list|)
specifier|public
name|void
name|setBusLifeCycleManager
parameter_list|(
name|BusLifeCycleManager
name|mgr
parameter_list|)
block|{
name|mgr
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|int
name|getInitCount
parameter_list|()
block|{
return|return
name|initCount
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|initCount
operator|++
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{      }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{      }
block|}
end_class

end_unit

